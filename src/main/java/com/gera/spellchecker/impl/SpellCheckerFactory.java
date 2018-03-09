package com.gera.spellchecker.impl;

import static java.util.Collections.newSetFromMap;
import static java.util.Optional.ofNullable;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.zip.ZipInputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.gera.spellchecker.ISpellChecker;
import com.gera.spellchecker.ISpellCheckerFactory;
import com.gera.spellchecker.SpellCheckConfiguration;
import com.google.common.base.Suppliers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Factory to create spell checker
 */
@Service
@Scope(value = SCOPE_SINGLETON)
@Slf4j
public final class SpellCheckerFactory implements ISpellCheckerFactory {
	private static final int READ_BLOCK = 1024;
	private final Pattern punctuation = Pattern
			.compile("[\\d\\s!@#$%^&*\"'\\(\\)\\[\\]{}\\-\\+_=<>\\?,\\./~`\\r?\\n;:]+");

	@Value("classpath:dictionary.zip")
	private Resource dictionaryZip;

	private com.google.common.base.Supplier<Set<String>> dictionarySupplier;

	@PostConstruct
	void init() {
		// simplified dictionary supplier based of some text input
		dictionarySupplier = Suppliers.memoize(() -> {
			try (InputStream fis = dictionaryZip.getInputStream(); ZipInputStream zis = new ZipInputStream(fis)) {

				Set<String> dictionary = newSetFromMap(new ConcurrentHashMap<>());
				SpecialString specialString = null;
				while ((zis.getNextEntry()) != null) {
					byte data[] = new byte[READ_BLOCK];
					while (zis.read(data, 0, READ_BLOCK) != -1) {
						specialString = bytesToString(new String(data),
								ofNullable(specialString).map(SpecialString::getLeftover));
						String[] arr = punctuation.split(specialString.string);
						Arrays.stream(arr).parallel().filter(s -> !s.isEmpty()) // throw away empty
								.filter(s -> s.length() > 1) // throw away single letter words
								.map(String::toLowerCase).forEach(w -> dictionary.add(w));
					}
				}

				dictionary.remove("s"); // due to 's like (men's esteem)
				dictionary.remove("i");
				dictionary.add("I");
				return dictionary;
			} catch (Exception e) {
				log.error("Dictionary Supplier failure {}", e);
				throw new RuntimeException(e);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation creates spell checker with bundled dictionary
	 */
	public ISpellChecker createSpellChecker(Optional<SpellCheckConfiguration> spellCheckConfiguration) {
		log.info("Created spell checker with configuration {}", spellCheckConfiguration.toString());
		return SpellChecker.of(dictionarySupplier.get(), spellCheckConfiguration);
	}

	private static SpecialString bytesToString(final String str, Optional<String> leftOver) {
		String s = leftOver.map(lo -> lo + str).orElse(str);
		for (int i = s.length() - 1; i >= 0; i--) {
			char ch = s.charAt(i);
			if (ch == ' ') {
				SpecialString specialString = new SpecialString(s.substring(0, i),
						(i + 1 < s.length()) ? s.substring(i, s.length()) : "");
				return specialString;
			}
		}

		return new SpecialString();
	}

	@AllArgsConstructor
	@NoArgsConstructor
	private static class SpecialString {
		@Getter
		private String string = "";
		@Getter
		private String leftover = null;
	}
}
