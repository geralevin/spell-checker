package com.gera.spellchecker;

import java.util.Optional;

/**
 * Spell checker factory
 */
public interface ISpellCheckerFactory {

	/**
	 *
	 * @param spellCheckConfiguration
	 *            spell check configuration
	 * @return spell checker
	 */
	ISpellChecker createSpellChecker(Optional<SpellCheckConfiguration> spellCheckConfiguration);
}
