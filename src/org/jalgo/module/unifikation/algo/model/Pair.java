package org.jalgo.module.unifikation.algo.model;



/**
 * Represents a pair in the set
 * @author Alex
 *
 */
public class Pair {
	private ITerm firstTerm=null;
	private ITerm secondTerm=null;
	private String lastError;
	

	public Pair(){}

	public Pair(ITerm term1, ITerm term2){
	firstTerm=term1;
	secondTerm=term2;
}

	/**
	 * formats brackets
	 */
	protected String formatBracket(String text){
		return text;
	}
	
	/**
	 * returns the last error from a rule-check
	 * @return
	 */
	public String getLastError() {
		return this.lastError;
	}
	
	/**
	 * formats other chars (commas...)
	 */
	protected String formatChar(String text){
		return text;
	}

	/**
	 * returns the unformatted representation of the pair
	 */
	public String toString(){
		if(firstTerm==null || secondTerm==null) return "invalid pair";
		return "("+firstTerm+", "+secondTerm+")";		
	}
	
	/**
	 * returns the formated representation of the pair
	 * @return formated representation of the pair
	 */
	public String getFormatText(){
		if(firstTerm==null || secondTerm==null) return "invalid pair";
		String result=formatBracket("(")+firstTerm.getFormatText();
		result+=formatChar(", ");
		result+=secondTerm.getFormatText()+formatBracket(")");
		return result;
	}
	
	/**
	 * adds the first term
	 * @param term
	 */
	public void addFirstTerm(final ITerm term){
		firstTerm=term;
	}
	
	/**
	 * adds the second term
	 * @param term
	 */
	public void addSecondTerm(final ITerm term){
		secondTerm=term;
	}
	
	public ITerm getFirstTerm(){
		return firstTerm;
	}
	
	public ITerm getSecondTerm(){
		return secondTerm;
	}
	
	/**
	 * Checks if pair is in final form as specified in unification algo: <br />
	 * (x,t(...)) with x not in t
	 * @return True if pair is in final form
	 */
	public boolean isInFinalForm(){
		return (firstTerm instanceof Variable &&
				!secondTerm.containsVar((Variable) firstTerm));
	}
	
	/**
	 * test if permutation could be used
	 * @return boolean
	 */	
	
	public boolean swapCheck()
	{
		if(secondTerm instanceof Variable) {
			if(firstTerm instanceof ConstructorSymbol) {
				return true;
			}
			else {
				this.lastError="Vertauschung nicht anwendbar, da der 1.Term eine Variable ist.";
				return false;
			}
		}
		else {
			this.lastError="Vertauschung nicht anwendbar, da 2.Term keine Variable ist.";
			return false;
		}
		
	}	
	
	/**
	 * test if decomposition could be used
	 * @return boolean
	 */
	public boolean decompositionCheck()
	{ 
		if(firstTerm instanceof ConstructorSymbol){
			if(secondTerm instanceof ConstructorSymbol) {
				if(firstTerm.equalsName(secondTerm)) {
					if(firstTerm.getParameters().size()==secondTerm.getParameters().size()) {
						return true;
					}
					else {
						this.lastError="Dekompositionsregel nicht anwendbar, da die Anzahl der Parameter der Konstruktoren unterschiedlich ist."; //eigentlich durch Parser unmöglich
						return false;
					}
				}
				else {
					this.lastError="Dekompositionsregel nicht anwendbar, da es sich um unterschiedliche Konstruktoren handelt.";
					return false;
					}
				}
			else {
				this.lastError="Dekompositionsregel nicht anwendbar, da 2.Term kein Konstruktor ist.";
				return false;
				}
		}
		else {
			this.lastError="Dekompositionsregel nicht anwendbar, da 1.Term kein Konstruktor ist.";
			return false;
			}
	}
			
	/**
	 * checks whether the two terms of this pair can be eliminated or not
	 * @return
	 */
	public boolean eliminationCheck()
	{
		if (firstTerm instanceof Variable) {
			
			if (secondTerm instanceof Variable) {
			
				if(secondTerm.equals(firstTerm)) {
					return true;
				}
				else {
					this.lastError="Eliminationsregel nicht anwendbar, da es sich um unterschiedliche Variablen handelt.";
					return false;
				}
			}
			else {
				this.lastError="Eliminationsregel nicht anwendbar, da der 2.Term keine Variable ist.";
				return false;
			}
		}
		else {
			this.lastError="Eliminationsregel nicht anwendbar, da der 1.Term keine Variable ist.";
			return false;
		}		
	}
	
	/**
	 * checks whether the first terms can substitute with the second term
	 * @return
	 */
	public boolean substitutionCheck()
	{
		if(firstTerm instanceof Variable) {
			if (!secondTerm.containsVar((Variable) firstTerm)) {
				return true;
			}
			else {
				this.lastError="Substitutionsregel nicht anwendbar, da der 2.Term die Variable " + firstTerm.getName(true) + " beinhaltet.";
				return false;
			}
		}
		else {
			this.lastError="Substitutionsregel nicht anwendbar, da der 1.Term keine Variable ist.";
			return false;
		}
	}
	
	/**
	 * Checks if this pair contains a specified variable
	 * @return True if variable is in pair
	 */
	public boolean containsVar(Variable var){
		return firstTerm.containsVar(var) || secondTerm.containsVar(var);
	}
	
	/**
	 * Substitutes varFrom to termTo
	 * @param varFrom Variable to replace
	 * @param termTo Term that is replaced for varFrom
	 */	
	public void substitute(Variable varFrom, ITerm termTo){
		firstTerm=firstTerm.substitute(varFrom, termTo);
		secondTerm=secondTerm.substitute(varFrom, termTo);		
	}
	
	/**
	 * swaps if possible
	 */
	public boolean swap() //Manual swap function
	{
		if(!swapCheck()) return false;
		ITerm cache = firstTerm;
		firstTerm = secondTerm;
		secondTerm = cache;		
		return true;
	}
}
