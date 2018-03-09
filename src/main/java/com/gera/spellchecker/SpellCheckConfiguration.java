package com.gera.spellchecker;

/**
 * Spell check configuration Defines cost of edits, and the threshold when spell
 * check correction will kick in.
 */
@SuppressWarnings("unused")
public final class SpellCheckConfiguration {
	private int deleteCost = 1;
	private int insertCost = 1;
	private int replacementCost = 1;
	private int transposeCost = 1;

	/**
	 * Spell check correction
	 */
	private int spellCheckCorrectionThreshHold = 2;

	private SpellCheckConfiguration() {
	}

	/**
	 * @return default configuration
	 */
	public static SpellCheckConfiguration defaultConfig() {
		return new SpellCheckConfiguration();
	}

	/**
	 * @return delete cost
	 */
	public int getDeleteCost() {
		return deleteCost;
	}

	/**
	 * Sets delete cost
	 *
	 * @param deleteCost
	 *            cost of delete edit
	 */
	public void setDeleteCost(int deleteCost) {
		this.deleteCost = deleteCost;
	}

	/**
	 * Returns cost of insert edit
	 *
	 * @return cost of insert edit
	 */
	public int getInsertCost() {
		return insertCost;
	}

	/**
	 * Sets cost of insertion
	 *
	 * @param insertCost
	 *            cost of insertion
	 */
	public void setInsertCost(int insertCost) {
		this.insertCost = insertCost;
	}

	/**
	 * returns cost of replacement
	 *
	 * @return cost of replacement
	 */
	public int getReplacementCost() {
		return replacementCost;
	}

	/**
	 * Sets cost of replacement
	 *
	 * @param replacementCost
	 *            cost of replacement
	 */
	public void setReplacementCost(int replacementCost) {
		this.replacementCost = replacementCost;
	}

	/**
	 * Returns transpose cost
	 *
	 * @return cost of transpose
	 */
	public int getTransposeCost() {
		return transposeCost;
	}

	/**
	 * Sets transpose cost
	 *
	 * @param transposeCost
	 *            cost of transposition
	 */
	public void setTransposeCost(int transposeCost) {
		this.transposeCost = transposeCost;
	}

	/**
	 * Get correction threshold
	 *
	 * @return threshold when spell check correction gets triggered
	 */
	public int getSpellCheckCorrectionThreshHold() {
		return spellCheckCorrectionThreshHold;
	}

	/**
	 * Set correction threshold
	 *
	 * @param spellCheckCorrectionThreshHold
	 *            spell check correction threshold
	 */
	public void setSpellCheckCorrectionThreshHold(int spellCheckCorrectionThreshHold) {
		this.spellCheckCorrectionThreshHold = spellCheckCorrectionThreshHold;
	}
}
