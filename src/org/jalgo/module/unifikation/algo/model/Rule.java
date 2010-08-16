package org.jalgo.module.unifikation.algo.model;

/**
 * Enum field for rules that can be applied to a set
 * @author Alex
 *
 */
public enum Rule {
	/**
	 * Neutral element. e.g. for check which rule can be applied
	 */
	NoRule,
	/**
	 * Swapping in a pair
	 */
	Swap,
	/**
	 * Elimination of a pair
	 */
	Elimination,
	/**
	 * Decomposition of a pair
	 */
	Decomposition,
	/**
	 * Substitution of a term for a variable
	 */
	Substitution;
	
	@Override
	public String toString(){
		switch(this)
		{
		case Swap:
			return "Vertauschung";
		case Elimination:
			return "Elimination";
		case Decomposition:
			return "Dekomposition";
		case Substitution:
			return "Substitution";
		case NoRule:
			return "Keine Regel";
		}
		return "";
		
	}
}
