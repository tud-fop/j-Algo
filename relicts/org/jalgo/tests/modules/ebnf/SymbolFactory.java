package org.jalgo.tests.ebnf;

public class SymbolFactory {

	private static SynDiaElem element;
	
	public SymbolFactory() {}
	
	public static SynDiaElem createElement(String type) {
		
		if (type.equals("terminal"))
			element = new Terminal("leer");
		else if (type.equals("variable"))
			element = new Variable("<Leer>");
		else if (type.equals("branch"))
			element = new Branch("close");
		return element;
		
	}
	
	
	
}
