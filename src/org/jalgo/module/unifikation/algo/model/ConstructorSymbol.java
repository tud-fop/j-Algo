package org.jalgo.module.unifikation.algo.model;

import java.util.*;

public class ConstructorSymbol extends ATerm {
	protected final String name;
	private List<ITerm> parameter;
	
	public ConstructorSymbol(final String name){
		this.name=name;
		this.parameter=new LinkedList<ITerm>();
	}
	
	public boolean addParameter(final ITerm param){
		return parameter.add(param);
	}
	
	public List<ITerm> getParameters(){
		return parameter;
	}

	public String getName(boolean doFormat) {
		return name;
	}

	public String toString(){
		String result=getName(false);
		result+=getParamList(-1);
	    return result;
	}

	public String getFormatText() {
		return getFormatText(0);
	}

	public String getFormatText(int recDepth) {
		String result=getName(true);
		result+=getParamList(recDepth);
	    return result;
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
	 * returns string representation of the parameters list
	 * recDepth<0 -->no formating
	 */
	protected String getParamList(int recDepth){
		boolean doFormat=(recDepth>=0);
		StringBuffer result=new StringBuffer();
		int size=parameter.size();
		if(size>0){
			if(doFormat) result.append(formatBracket("( "));
			else result.append("( ");
		    for (ITerm param : parameter){
		    	if(doFormat){
		    		result.append(param.getFormatText(recDepth+1));
		    	}else result.append(param);
		    	size--;
		    	if(size>0){
					if(doFormat) result.append(formatChar(", "));
					else result.append(", ");
		    	}
		    }
			if(doFormat) result.append(formatBracket(" )"));
			else result.append(" )");
		}
	    return result.toString();
	}

	public boolean containsVar(Variable var) {
	    for (ITerm param : parameter){
	    	if(param.containsVar(var)) return true;
	    }
		return false;
	}
	
	public ITerm substitute(Variable varFrom, ITerm termTo) {
		for (int i=0;i<parameter.size();i++){
			parameter.set(i, parameter.get(i).substitute(varFrom, termTo)); 
		}
		return this;
	}

}
