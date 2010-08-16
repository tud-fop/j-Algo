package org.jalgo.module.unifikation.algo.model;

import java.util.List;

public class Variable extends ATerm {
	protected final String name;
	private final int index;
	
	public Variable(final String name){
		this.name=name.substring(0, 1).toLowerCase();
		if(name.length()>1) this.index=Integer.parseInt(name.substring(1));
		else this.index=-1;
	}
	
	public String toString(){
		return getName(false);
	}
	
	public boolean addParameter(final ITerm param){
		return false;
	}
	
	protected String getIndex(boolean doFormat){
		if(index>=0) return String.valueOf(index);
		else return "";
	}

	public String getFormatText() {
		return getFormatText(0);
	}
	public String getFormatText(int recDepth) {
		return getName(true);
	}

	public String getName(boolean doFormat) {
		return name+getIndex(doFormat);
	}

	public List<ITerm> getParameters() {
		return null;
	}

	public boolean containsVar(Variable var) {
		return getName(false).equalsIgnoreCase(var.getName(false));
	}

	public ITerm substitute(Variable varFrom, ITerm termTo) {
		if(this.equals(varFrom)) return termTo;
		else return this;
	}
}
