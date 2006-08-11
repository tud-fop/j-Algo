package org.jalgo.tests.ebnf.junit.model.ebnf;

import org.jalgo.module.ebnf.model.ebnf.*;

/**
 * This is a class that contains some static functions that return some EBNF
 * definitions. Its purpose is to simplify the testing of the EBNF definition
 * 
 * @author jm
 *
 */
public class DefinitionLibrary {

	
	static EVariable v1 = null;
	static EVariable v2 = null;
	static EVariable v4 = null;
	static EVariable v5 = null;
	static EVariable v6 = null;
	static EVariable v7 = null;
	static EVariable v8 = null;
	static EVariable v10 = null;
	static EVariable v9 = null;
	static EVariable v3 = null;
	
	static ETerminalSymbol s1 = null;
	static ETerminalSymbol s2 = null;
	static ETerminalSymbol s3 = null;
	static ETerminalSymbol s4 = null;
	static ETerminalSymbol s5 = null;
	static ETerminalSymbol s6 = null;
	static ETerminalSymbol s7 = null;
	static ETerminalSymbol s8 = null;
	static ETerminalSymbol s9 = null;
	static ETerminalSymbol s10 = null;
	
	static Term t1 = null;
	static Term t2 = null;
	static Term t3 = null;
	static Term t4 = null;
	static Term t5 = null;
	static Term t6 = null;
	static Term t7 = null;
	static Term t8 = null;
	static Term t9 = null;
	static Term t10 = null;
	
	static Definition d1 = null;
	static Definition d2 = null;
	static Definition d3 = null;
	static Definition d4 = null;
	static Definition d5 = null;
	static Definition d6 = null;
	static Definition d7 = null;
	static Definition d8 = null;
	static Definition d9 = null;
	static Definition d10 = null;
	
	// a list that contains the rules
	private static java.util.List<Definition> definitions 
						= new java.util.ArrayList<Definition>();
	
	// a list that contains the terms
	private static java.util.List<Term> terms = new java.util.ArrayList<Term>();
	
	// a list that contains the rules
	private static java.util.List<Rule> rules = new java.util.ArrayList<Rule>();
	
	
	
	private DefinitionLibrary() {
	}
	
	public static void setUp() {

		setUpSymbols();
		setUpTerms();
		setUpRules();
		setUpDefinitions();
	}
	
	private static void setUpSymbols() {
		try {
			v1 = new EVariable("v1");
			v2 = new EVariable("v2");
			v3 = new EVariable("v3");
			v4 = new EVariable("v4");
			v5 = new EVariable("v5");
			v6 = new EVariable("v6");
			v7 = new EVariable("v7");
			v8 = new EVariable("v8");
			v9 = new EVariable("v9");
			v10 = new EVariable("v10");
		} catch (DefinitionFormatException e) {
			// there should be no errors here :-)
			System.out.println("ANFANG Fehler beim hinzufügen von Variablen!");
			e.printStackTrace();
			System.out.println("ENDE Fehler beim hinzufügen von Variablen!");
		}
		
		try {
			s1 = new ETerminalSymbol("t1");
			s2 = new ETerminalSymbol("t2");
			s3 = new ETerminalSymbol("t3");
			s4 = new ETerminalSymbol("t4");
			s5 = new ETerminalSymbol("t5");
			s6 = new ETerminalSymbol("t6");
			s7 = new ETerminalSymbol("t7");
			s8 = new ETerminalSymbol("t8");
			s9 = new ETerminalSymbol("t9");
			s10 = new ETerminalSymbol("t10");
		} catch (DefinitionFormatException e) {
			// there should be no errors here :-)
			e.printStackTrace();
		}
	}
	
	private static void setUpTerms() {
		// a temporary list
		java.util.List<Term> tlist = new java.util.ArrayList<Term>();
		
		// creating some symbols


		
		// creating some funny looking terms :-)
		try {
			
			// TERM 1 BEGIN ----------------------------------------------------
			t1 = new EAlternative(v1,v2);
			tlist.clear();
			tlist.add(s1);
			tlist.add(s2);
			tlist.add(s3);
			tlist.add(s4);
			t2 = new EAlternative(tlist);
			t3 = new EAlternative(t1,t2);
			
			terms.add(t3);
			// -----------------------------------------------------------------
			//                        ((v1|v2)|(t1|t2|t3|t4))
			// TERM 1 END ------------------------------------------------------
			
			// TERM 2 BEGIN ----------------------------------------------------
			tlist.clear();
			tlist.add(new ERepetition(s3));
			tlist.add(v1);
			t1 = new EConcatenation(tlist);
			
			terms.add(t1);
			// -----------------------------------------------------------------
			// Skript Seite 149:      {s3}v1
			// TERM 2 END ------------------------------------------------------
			
			// TERM 3 BEGIN ----------------------------------------------------

			tlist.clear();
			tlist.add(s1);
			tlist.add(v1);
			tlist.add(s2);
			
			t1 = new EConcatenation(tlist);
			t2 = new ECompoundTerm(t1);
			t3 = new EAlternative(t2, v1);
			
			terms.add(t3);
			// -----------------------------------------------------------------
			// Skript Seite 149:      ((s1v1s2)|s1)
			// TERM 3 END ------------------------------------------------------			
			
		} catch (DefinitionFormatException e) {
			// there should be no errors here :-)
			e.printStackTrace();
		}
	}
	
	private static void setUpRules() {
		// set up rules --------------------------------------------------------
		try {
			rules.add(new Rule(v1, getTerm(0)));
			rules.add(new Rule(v2, getTerm(1)));
			rules.add(new Rule(v1, getTerm(2)));
		} catch (DefinitionFormatException e) {
			// there should be no errors here :-)
			e.printStackTrace();
		}
		// ---------------------------------------------------------------------
	}
	
	private static void setUpDefinitions() {
		d1 = new Definition();
		try {
		d1.addTerminal(s1);
		d1.addTerminal(s2);
		d1.addTerminal(s3);
		d1.addVariable(v1);
		d1.addVariable(v2);
		
		d1.addRule(rules.get(1));
		d1.addRule(rules.get(2));
		
		d1.setStartVariable(v2);
		
		} catch (DefinitionFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		definitions.add(d1);
		
	}
	
	public static Definition getDefinition(int i) {

		if(i>=0 && i<definitions.size()) return definitions.get(i);
		else return null;
	}
	
	public static Rule getRule(int i) {

		if(i>=0 && i<rules.size()) return rules.get(i);
		else return null;
	}
	
	public static Term getTerm(int i) {
		
		if(i>=0 && i<terms.size()) return terms.get(i);
		else return null;
	}
	
	public static void main (String[] args) {
		setUp();
		System.out.print(getDefinition(0).toString());
	}

}
