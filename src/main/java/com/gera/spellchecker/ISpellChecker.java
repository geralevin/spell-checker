package com.gera.spellchecker;

/**
 * SpellChecker interface is a regular function that accepts a string and spits
 * the corrected string out.
 */
@FunctionalInterface
public interface ISpellChecker {
	String check(String string);
}
