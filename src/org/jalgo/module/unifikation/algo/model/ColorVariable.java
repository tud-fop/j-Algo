package org.jalgo.module.unifikation.algo.model;

public class ColorVariable extends HTMLVariable {

	public ColorVariable(String name) {
		super(name);
	}

	public String getName(boolean doFormat) {
		String result=super.getName(doFormat);
		if(doFormat)
			return Formating.addColor(result, Formating.Variable);
		else return result;
	}
}
