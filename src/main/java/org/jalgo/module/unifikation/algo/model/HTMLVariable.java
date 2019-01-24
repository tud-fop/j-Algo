package org.jalgo.module.unifikation.algo.model;

public class HTMLVariable extends Variable {
	public HTMLVariable(String name) {
		super(name);
	}

	protected String getIndex(boolean doFormat){
		String result=super.getIndex(doFormat);
		if(doFormat && result!="") result="<sub>"+result+"</sub>";
		return result;
	}

}
