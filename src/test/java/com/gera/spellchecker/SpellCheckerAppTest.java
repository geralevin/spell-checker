package com.gera.spellchecker;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.ImmutableList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpellCheckerAppTest {
	@Inject
	private ISpellCheckerFactory spellCheckerFactory;
	private ISpellChecker spellChecker;

	@Before
	public void init() {
		// dictionary supplier (read dictionary from zip)
		spellChecker = spellCheckerFactory.createSpellChecker(Optional.empty());
	}

	@Test
	public void contextLoads() {
	}

	/**
	 * check words
	 */
	@Test
	public void checkWords() {
		List<String> wordsInput = ImmutableList.<String>builder().add("speling").add("korrectud").add("bycycle")
				.add("inconvinient").add("arrainged").add("peotry").add("peotryy").add("word").add("quintessential")
				.build();

		List<String> expected = ImmutableList.<String>builder().add("spelling").add("corrected").add("bicycle")
				.add("inconvenient").add("arranged").add("poetry").add("poetry").add("word").add("quintessential")
				.build();

		List<String> results = wordsInput.stream().map(spellChecker::check).collect(Collectors.toList());

		assertThat("From string: " + wordsInput.toString(), results, is(expected));
	}
}
