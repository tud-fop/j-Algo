package org.jalgo.module.unifikation.algo.model;

import java.util.LinkedList;
import java.util.List;


/*
 * represents the set of pairs for which the unification problem is to be solved
 */
public class ProblemSet implements IModelFormat{
	protected List<Pair> pairs;
	private int selectedPair;
	private int hoverPair;
	private boolean active;
	private String lastRuleCheckError;
	private Rule lastUsedRule = Rule.NoRule;
	
	public ProblemSet(){
		pairs=new LinkedList<Pair>();
		hoverPair=-1;
		selectedPair=-1;
		active=true;
	}
	
	/**
	 * sets the set to active
	 * @param active true=active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * returns if set is active
	 * @return true=active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * sets the hovered pair
	 * @param hoverPair index of pair, -1 for none
	 */
	public void setHoverPair(int hoverPair) {
		this.hoverPair = hoverPair;
	}

	/**
	 * gets the hovered pair
	 * @return index of hovered pair
	 */
	public int getHoverPair() {
		return hoverPair;
	}

	/**
	 * sets the selected pair
	 * @param selectedPairindex of pair, -1 for none
	 */
	public void setSelectedPair(int selectedPair) {
		this.selectedPair = selectedPair;
	}

	/**
	 * gets the selected pair
	 * @return index of selected pair
	 */
	public int getSelectedPair() {
		return selectedPair;
	}
	
	/**
	 * @return unformatted representation of the set
	 */
	public String toString(){
		StringBuffer result=new StringBuffer("M={ ");
		int remaining=pairs.size();
	    for (Pair pair : pairs){
	    	result.append(pair);
	    	remaining--;
	    	if(remaining>0) result.append(", ");
	    }
	    result.append(" }");
	    return result.toString();
	}
	
	/**
	 * returns a string with the last error that a rule-check generated
	 * @return error message 
	 */
	public String getLastRuleErrorText() {
		return this.lastRuleCheckError;
	}
	
	/**
	 * updates the private String lastRuleCheckError with the last error message from the selected pair
	 */
	public void updateLastRuleError() {
		this.lastRuleCheckError=this.getSelectedPPair().getLastError();
	}
	/**
	 * sets the last error that a rule-check generated
	 * @param errorText
	 */
	public void setLastRuleErrorText(String errorText) {
		this.lastRuleCheckError =errorText;
	}
	
	/**
	 * formats brackets
	 */
	protected String formatBracket(String text){
		return text;
	}
	
	/**
	 * formats other chars (commas...)
	 */
	protected String formatChar(String text){
		return text;
	}
	
	/**
	 * @param finished true if algo is finished
	 * @return formated representation of the set
	 */
	public String getFormatText(boolean finished){
		StringBuffer result=new StringBuffer("M="+formatBracket("{ "));
		int remaining=pairs.size();
	    for (Pair pair : pairs){
	    	result.append(pair.getFormatText());
	    	remaining--;
	    	if(remaining>0) result.append(formatChar(", "));
	    }
	    result.append(formatChar(" }"));
	    return result.toString();
	}
	
	/**
	 * @return formated representation of the set
	 */
	public String getFormatText(){
		return this.getFormatText(false);
	}

	
	/**
	 * adds a pair and returns whether it was successful
	 * @param pair
	 * @return successful add
	 */
	public boolean addPair(final Pair pair){
		return pairs.add(pair);
	}

	/**
	 * adds a pair at a specific position. Avoids duplicate entries
	 * @param index
	 * @param pair
	 */
	public void addPair(int index,final Pair pair){
		for(Pair curPair:pairs){
			if(curPair.toString().equalsIgnoreCase(pair.toString())) return;
		}
		pairs.add(index, pair);
	}
	
	/**
	 * removes the pair if present in list
	 * @return boolean if success
	 */
	public boolean removePair(Pair pair){
		
		return this.pairs.remove(pair);
	}
	
	/**
	 * gets the selected pair
	 * @return selected pair
	 */
	public Pair getSelectedPPair() 
	{
		return (getPair(getSelectedPair()));
	}
	
	/**
	 * gets the last used rule
	 * @return rule
	 */
	public Rule getLastUsedRule() 
	{
		return this.lastUsedRule;
	}
	
	/**
	 * Returns pair defined by index or null if invalid index
	 * @param index Index of pair in set
	 * @return Pair at index or null
	 */
	public Pair getPair(int index){
		if(index>=0 && index<pairs.size()) return pairs.get(index);
		else return null;
	}
	
	/**
	 * Get index of a pair on which a rule can be applied
	 * @return Index of pair in set or -1 if no pair was found
	 */
	public int getUsablePair(){
		for(int i=0;i<pairs.size();i++){
			if(getUsableRule(i)!=Rule.NoRule) return i;
		}
		return -1;
	}
	
	/**
	 * Gets a pair that is not in its final form or -1 if none was found
	 * @return Index of pair in set or -1
	 */
	public int getPairNotFinal(){
		for(int i=0;i<pairs.size();i++){
			if(!isPairInFinalForm(i)) return i;
		}
		return -1;
	}
	
	/**
	 * Checks if a pair with given index is in its final form
	 * @param index Index of pair in set
	 * @return True if pair is in final form or pair with index not found
	 */	
	public boolean isPairInFinalForm(int index){
		Pair pair=getPair(index);
		if(pair==null) return true;
		return (pair.isInFinalForm());
	}
	
	/**
	 * Gets the rule that can be applied to pair with given index
	 * @param index Index of pair in set
	 * @return Rule that can be applied
	 */
	public Rule getUsableRule(int index){
		for(Rule rule : Rule.values()){
			if(rule==Rule.NoRule) continue;
			if(canUseRule(rule, index)) return rule;
		}
		return Rule.NoRule;
	}
	
	/**
	 * Gets the rule that can be applied to currently selected pair
	 * @return Rule that can be applied
	 */	
	public Rule getUsableRule(){
		return getUsableRule(getSelectedPair());
	}
	
	/**
	 * Returns whether the given rule can be applied to the pair with given index
	 * @param rule Rule that should be checked
	 * @param index Index of pair in set
	 * @return True if rule can be applied
	 */
	public boolean canUseRule(Rule rule, int index){
		Pair targetPair=getPair(index);
		if(targetPair==null) return false;
		if(rule==Rule.NoRule) return true;
		if(rule==Rule.Decomposition){
			if(targetPair.decompositionCheck()) {
				return true;
			}
			else {
				this.lastRuleCheckError=targetPair.getLastError();
				return false;
			}
		}
		if(rule==Rule.Elimination){
			if(targetPair.eliminationCheck()) {
				return true;
			}
			else {
				this.lastRuleCheckError=targetPair.getLastError();
				return false;
			}
		}
		if(rule==Rule.Substitution){
			if(!targetPair.substitutionCheck()) {
				this.lastRuleCheckError=targetPair.getLastError();
				return false;
			}
			for(Pair pair : pairs){
				if(pair==targetPair) continue;
				if(pair.containsVar((Variable) targetPair.getFirstTerm())) return true;
			}
			this.lastRuleCheckError="Substitutionregel kann nicht angewendet werden, da die Variable " + targetPair.getFirstTerm().getName(true) + " nicht mehr in anderen Paaren vorkommt.";
			return false;
		}
		if(rule==Rule.Swap){
			if(targetPair.swapCheck()) {
				return true;
			}
			else {
				this.lastRuleCheckError=targetPair.getLastError();
				return false;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns whether the given rule can be applied to currently selected pair
	 * @param rule Rule that should be checked
	 * @return True if rule can be applied
	 */
	public boolean canUseRule(Rule rule){
		return canUseRule(rule,getSelectedPair());
	}
	
	/**
	 * Tries to use the specified rule to currently selected pair
	 * @param rule Rule to apply
	 * @return True if successfully applied
	 */
	public boolean useRule(Rule rule){
		switch(rule){
		case NoRule: return true;
		case Decomposition: return this.decomposition();
		case Elimination: return this.elimination();
		case Substitution: return this.substitution();
		case Swap: return this.swap();
		}
		return false;
	}

	
	/**
	 * if possible, this function eliminates a pair of the problemset
	 * @return true or false
	 */
	public boolean elimination() {
		if(canUseRule(Rule.Elimination)){
			this.lastUsedRule=Rule.Elimination;
			return this.removePair(getSelectedPPair());
		}	
		return false;
	}
	
	/**
	 * if possible, this tiny function executes the decomposition of two terms
	 * @return false or true
	 */
	public boolean decomposition() {
		
		if (canUseRule(Rule.Decomposition))
		{
			final List<ITerm> parametersTerm1 = this.getSelectedPPair().getFirstTerm().getParameters();
			final List<ITerm> parametersTerm2 = this.getSelectedPPair().getSecondTerm().getParameters();
			int pos = this.getSelectedPair()+1;

			for(int i=0; i < parametersTerm1.size(); i++){
				Pair newPair = new Pair( parametersTerm1.get(i) , parametersTerm2.get(i));
				this.addPair(pos,newPair);
				pos++;
			}
			this.removePair(getSelectedPPair());
			this.lastUsedRule=Rule.Decomposition;
			return true;
		}
		return false;
	}
	
	/**
	 * if possible, this function executes the substitution
	 * @return false or true
	 */
	public boolean substitution()
	{

		if (canUseRule(Rule.Substitution))
		{
			Pair selPair = getSelectedPPair();
			for(Pair curPair : pairs){
				if(curPair==selPair) continue;
				curPair.substitute((Variable) selPair.getFirstTerm(), selPair.getSecondTerm());
				this.lastUsedRule=Rule.Substitution;
			}
			return (true);
		}
		else return (false);
	}
	
	/**
	 * if possible, this function swaps a pair
	 * @return false or true
	 */
	public boolean swap()
	{
		Pair cache = getSelectedPPair();
		if (canUseRule(Rule.Swap))
		{
			cache.swap();
			String cacheS=cache.toString();
			for(Pair curPair:pairs){
				if(curPair==cache) continue;
				if(curPair.toString().equalsIgnoreCase(cacheS)){
					pairs.remove(cache);
					break;
				}
			}
			this.lastUsedRule=Rule.Swap;
			return (true);
		}else return (false);
	}
	
	//method for jUnit test
	/**
	 * removes all pairs of the ProblemSet
	 */
	public void removeAllPairs(){
		pairs.clear();
	}

	public void setLastUsedRule(Rule rule) {
		this.lastUsedRule=rule;		
	}
	
	/**
	 * return the number of pairs 
	 * @return number of pairs
	 */
	public int getNumberOfPairs(){
		return pairs.size();
	}
}
