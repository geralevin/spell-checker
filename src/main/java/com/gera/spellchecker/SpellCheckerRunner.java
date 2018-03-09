package com.gera.spellchecker;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spell checker Runner (Damerau-Levenshtein algorithm, language processing,
 * spell checker)
 */

@SpringBootApplication
public class SpellCheckerRunner implements CommandLineRunner {

	private final ISpellCheckerFactory spellCheckerFactory;
	private ISpellChecker spellChecker;

	@Inject
	public SpellCheckerRunner(ISpellCheckerFactory spellCheckerFactory) {
		this.spellCheckerFactory = spellCheckerFactory;
	}

	@PostConstruct
	void init() {
		spellChecker = spellCheckerFactory.createSpellChecker(Optional.empty());
	}

	@Override
	@SuppressWarnings("unchecked")
	public void run(String... args) {
		if (args.length < 1) {
			System.err.println("Please input word or a space separated words.");
			return;
		}

		System.out.println(spellChecker.check(Arrays.stream(args).collect(Collectors.joining(" "))));
	}

	public static void main(String... args) {
		SpringApplication.run(SpellCheckerRunner.class, args);
	}
}