package com.gera.spellchecker.impl;

import static java.util.Objects.requireNonNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.gera.spellchecker.ISpellChecker;
import com.gera.spellchecker.SpellCheckConfiguration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Damerau–Levenshtein spell checker with adjacency transposition
 */
public final class SpellChecker implements ISpellChecker {
	private final Set<String> dictionary;
	private final SpellCheckConfiguration spellCheckConfiguration;

	/**
	 * @param dictionary
	 *            dictionary of english words to check against
	 * @param spellCheckConfiguration
	 *            cost of various edits (if not passed the cost is 1)
	 */
	private SpellChecker(Set<String> dictionary, Optional<SpellCheckConfiguration> spellCheckConfiguration) {
		requireNonNull(dictionary);
		requireNonNull(spellCheckConfiguration);

		this.dictionary = dictionary;
		this.spellCheckConfiguration = spellCheckConfiguration.orElse(SpellCheckConfiguration.defaultConfig());
	}

	/**
	 * Gets spell checker instance with optional spell check configuration
	 *
	 * @param dictionary
	 *            dictionary of words
	 * @param spellCheckConfiguration
	 *            spell check configuration
	 * @return spell checker instance
	 */
	public static ISpellChecker of(Set<String> dictionary, Optional<SpellCheckConfiguration> spellCheckConfiguration) {
		requireNonNull(dictionary);
		requireNonNull(spellCheckConfiguration);
		return new SpellChecker(dictionary, spellCheckConfiguration);
	}

	/**
	 * Applies the spell check function on the [space , . ; separated word string to
	 * the string and returns corrected string. Simplified version with no regard to
	 * punctuation.
	 *
	 * @param inputString
	 *            input string to spell check
	 * @return the spelled checked string
	 */
	@Override
	public String check(String inputString) {
		requireNonNull(inputString);

		String[] words = inputString.split("[ |,|.|\t| ;]");
		return Arrays.stream(words).map(w -> {
			if (dictionary.contains(w)) {
				return w;
			}

			// @formatter:off
			return dictionary.parallelStream()
					// filter out what ever far beyond threshold
					.filter(dictionaryEntry -> damerauLevenshteinDistance(w, dictionaryEntry) <= spellCheckConfiguration
							.getSpellCheckCorrectionThreshHold())
					// preprocessor for group by min
					.map(dictionaryEntry -> new DictionaryWordDistance(dictionaryEntry,
							damerauLevenshteinDistance(w, dictionaryEntry)))
					// apply min
					.min(Comparator.comparing(DictionaryWordDistance::getDistance)).map(DictionaryWordDistance::getWord)
					.orElse(w);
			// @formatter:on
		}).collect(Collectors.joining(" "));
	}

	@AllArgsConstructor
	private static final class DictionaryWordDistance {
		@Getter
		private String word;
		@Getter
		private int distance;
	}

	/**
	 * Implementation for a Damerau-Levenstein distance algorithm with adjacent
	 * transposition
	 * <p>
	 * <link>
	 * https://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance</link>
	 *
	 * @param a
	 *            string A
	 * @param b
	 *            string B
	 * @return number of mutations to go from string A to string B (optimal
	 *         alignment distance with transposition)
	 */
	private int damerauLevenshteinDistance(String a, String b) {
		requireNonNull(a);
		requireNonNull(b);

		int maxDistance = a.length() + b.length() + 1;
		int[][] d = new int[a.length() + 2][b.length() + 2];

		for (int i = 0; i <= a.length(); i++) {
			d[i + 1][0] = maxDistance;
			d[i + 1][1] = i;
		}

		for (int j = 0; j <= b.length(); j++) {
			d[0][j + 1] = maxDistance;
			d[1][j + 1] = j;
		}

		Map<Character, Integer> alphabet = getAlphabet(a, b);
		int[] da = new int[alphabet.size()];
		for (int i = 0; i < da.length; i++) {
			da[i] = 0;
		}

		for (int i = 1; i <= a.length(); i++) {
			int db = 0;

			for (int j = 1; j <= b.length(); j++) {

				int k = da[alphabet.get(b.charAt(j - 1))];
				int l = db;
				int cost = ((a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : spellCheckConfiguration.getReplacementCost());
				if (cost == 0)
					db = j;

				// @formatter:off
				d[i + 1][j + 1] = IntStream.of(d[i][j] + cost, // replacement
						d[i + 1][j] + spellCheckConfiguration.getInsertCost(), // insertion
						d[i][j + 1] + spellCheckConfiguration.getDeleteCost(), // deletion
						d[k][l] + (i - k - 1) + (j - l - 1) + spellCheckConfiguration.getTransposeCost() // transposition
				).min().getAsInt();
				// @formatter:on
			}
			da[alphabet.get(a.charAt(i - 1))] = i;
		}

		return d[a.length() + 1][b.length() + 1];
	}

	/**
	 * Build a short alphabet based of 2 strings
	 *
	 * @param str
	 *            strings from which to build alphabet
	 * @return alphabet
	 */
	private static Map<Character, Integer> getAlphabet(String... str) {
		int ind = 0;
		Map<Character, Integer> m = new HashMap<>();
		for (String s : str) {
			for (int i = 0; i < s.length(); i++) {
				Character c = s.charAt(i);
				if (!m.containsKey(c)) {
					m.put(c, ind++);
				}
			}
		}

		return m;
	}
}
