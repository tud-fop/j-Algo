package org.jalgo.module.unifikation.algo.model;

public class ColorConstructorSymbol extends ConstructorSymbol {

	public ColorConstructorSymbol(String name) {
		super(name);
	}
	
	protected String formatBracket(String text){
		return Formating.addColor(text, Formating.Char);
	}
	
	protected String formatChar(String text){
		return Formating.addColor(text, Formating.Char);
	}
	
	public String getName(boolean doFormat) {
		if(doFormat)
			return Formating.addColor(name, Formating.Function);
		else return name;
	}

}
