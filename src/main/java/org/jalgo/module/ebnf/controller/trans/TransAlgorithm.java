/**
 * 
 */
package org.jalgo.module.ebnf.controller.trans;

import java.util.ArrayList;
import java.util.List;

import org.jalgo.module.ebnf.model.ebnf.Definition;
import org.jalgo.module.ebnf.model.ebnf.DefinitionFormatException;
import org.jalgo.module.ebnf.model.ebnf.EAlternative;
import org.jalgo.module.ebnf.model.ebnf.ECompoundTerm;
import org.jalgo.module.ebnf.model.ebnf.EConcatenation;
import org.jalgo.module.ebnf.model.ebnf.EOption;
import org.jalgo.module.ebnf.model.ebnf.ERepetition;
import org.jalgo.module.ebnf.model.ebnf.ETerminalSymbol;
import org.jalgo.module.ebnf.model.ebnf.EVariable;
import org.jalgo.module.ebnf.model.ebnf.Rule;
import org.jalgo.module.ebnf.model.ebnf.Term;
import org.jalgo.module.ebnf.model.syndia.Branch;
import org.jalgo.module.ebnf.model.syndia.Concatenation;
import org.jalgo.module.ebnf.model.syndia.NoNullElemException;
import org.jalgo.module.ebnf.model.syndia.Repetition;
import org.jalgo.module.ebnf.model.syndia.SynDiaElem;
import org.jalgo.module.ebnf.model.syndia.SynDiaSystem;
import org.jalgo.module.ebnf.model.syndia.SyntaxDiagram;
import org.jalgo.module.ebnf.model.trans.TransMap;

/**
 * @author Andre
 *
 */
public class TransAlgorithm {
	
	private static TransMap transMap = null;
	
	/**
	 * this function transforms a <code>Definition</code> into an aquivalent
	 * <code>SynDiaSystem</code>
	 * @param ebnfDef 
	 * @param tm an empty TransMap
	 * 
	 * @return a <code>SynDiaSystem</code>
	 */

	public static SynDiaSystem transformEbnf(Definition ebnfDef, TransMap tm) {
		
		transMap = tm;
		
		SynDiaSystem sds = new SynDiaSystem();
		
		//Startvariable setzen
		sds.setStartDiagram(ebnfDef.getStartVariable().getName());
		
		// Variablenliste transformieren
		for (EVariable ev : ebnfDef.getVariables()) {
			
			sds.addVariable(ev.getName());
			
		}
		
		
		// Reglen abarbeiten
				
		List<Rule> ruleBuffer = new ArrayList<Rule>();
		for (Rule rule : ebnfDef.getRules()) {
			
			if (sds.getStartDiagram().equals(rule.getName().getName())) {
				sds.insertSyntaxDiagram(transformRule(rule, sds));
			} else {
				ruleBuffer.add(rule);
			}
		}
		for (Rule rule : ruleBuffer)
			sds.insertSyntaxDiagram(transformRule(rule, sds));
						
		return sds;
	}

	/**
	 * Transforms a Rule into a SyntaxDiagram
	 * 
	 * @param rule a Rule
	 * @return A SyntaxDiagram
	 */
	private static SyntaxDiagram transformRule(Rule rule, SynDiaSystem sds) {

		SyntaxDiagram sd = new SyntaxDiagram(rule.getName().getName(), sds);

		transformTerm(rule.getTerm(), sd.getRoot());

		sd.removeNullElems();

		return sd;
	}

	/**
	 * Transforms a Term into a SynDiaElem and adds it to the SyntaxDiagram
	 * 
	 * @param t A Term
	 * @param se A SynDiaElem
	 */
	private static void transformTerm(Term t, SynDiaElem se) {
		try {
			//
			// Terminal Symbol
			//
			if (t.getClass() == ETerminalSymbol.class) {

				ETerminalSymbol et = (ETerminalSymbol) t;
				if (se.getClass() == Concatenation.class) {

					Concatenation c = (Concatenation) se;
					c.addTerminalSymbol(0, et.getName());
					transMap.put(c.getSynDiaElem(1), et);

				}

				//
				// Variable
				//	
			} else if (t.getClass() == EVariable.class) {

				EVariable ev = (EVariable) t;
				if (se.getClass() == Concatenation.class) {

					Concatenation c = (Concatenation) se;
					c.addVariable(0, ev.getName());
					transMap.put(c.getSynDiaElem(1), ev);

				}

				//
				// Concatenation
				//	
			} else if (t.getClass() == EConcatenation.class) {

				EConcatenation ec = (EConcatenation) t;
				Concatenation c = (Concatenation) se;
				int i = 0;
				for (Term term : ec.getTerms()) {

					putTermInSynDia(term, c, 2 * i);
					// TODO CHECK
					transformTerm(term, c.getSynDiaElem(2 * i + 1));

					i++;
				}
				transMap.put(c, t);
				//
				// Alternative
				//		
			} else if (t.getClass() == EAlternative.class) {

				EAlternative ea = (EAlternative) t;
				if (se.getClass() == Concatenation.class) {

					Concatenation c = (Concatenation) se;
					c.addBranch(0, 0);
					transMap.put(c.getSynDiaElem(1), ea);

					transformTerm(ea, c.getSynDiaElem(1));

				} else if (se.getClass() == Branch.class) {

					Branch b = (Branch) se;

					transformTerm(ea.getTerms().get(0), b.getLeft());
					transformTerm(ea.getTerms().get(1), b.getRight());

				}

				//
				// Option
				//		
			} else if (t.getClass() == EOption.class) {

				EOption eo = (EOption) t;

				if (se.getClass() == Concatenation.class) {

					Concatenation c = (Concatenation) se;
					c.addBranch(0, 0);
					transMap.put(c.getSynDiaElem(1), eo);

					transformTerm(eo, c.getSynDiaElem(1));

				} else if (se.getClass() == Branch.class) {

					Branch b = (Branch) se;

					transformTerm(eo.getTerm(), b.getLeft());

				}
				//
				// Repetition
				//		
			} else if (t.getClass() == ERepetition.class) {

				ERepetition er = (ERepetition) t;

				if (se.getClass() == Concatenation.class) {

					Concatenation c = (Concatenation) se;
					c.addRepetition(0, 0);
					transMap.put(c.getSynDiaElem(1), er);

					transformTerm(er, c.getSynDiaElem(1));

				} else if (se.getClass() == Repetition.class) {

					Repetition r = (Repetition) se;

					transformTerm(er.getTerm(), r.getRight());

				}
				// 
				// Compound Term
				//
			} else if (t.getClass() == ECompoundTerm.class) {

				ECompoundTerm ect = (ECompoundTerm) t;

				transformTerm(ect.getTerm(), se);

			}
		} catch (NoNullElemException e) {

			System.out.println("Das war wohl kein Nullelement");

		}

	}

	/**
	 * Creates a SynDiaElem of the given Term and puts this to the given
	 * Concatenation
	 * 
	 * @param t the Term to transform
	 * @param c the Concatenation to put the SynDiaElem in
	 * @param index
	 */
	private static void putTermInSynDia(Term t, Concatenation c, int index) {

		try {
			if (t.getClass() == ETerminalSymbol.class) {
				ETerminalSymbol et = (ETerminalSymbol) t;
				c.addTerminalSymbol(index, et.getName());
				transMap.put(c.getSynDiaElem(index + 1), t);
			} else if (t.getClass() == EVariable.class) {
				EVariable ev = (EVariable) t;
				c.addVariable(index, ev.getName());
				transMap.put(c.getSynDiaElem(index + 1), t);
			} else if (t.getClass() == EAlternative.class) {
				c.addBranch(index, index);
				transMap.put(c.getSynDiaElem(index + 1), t);
			} else if (t.getClass() == EOption.class) {
				c.addBranch(index, index);
				transMap.put(c.getSynDiaElem(index + 1), t);
			} else if (t.getClass() == ERepetition.class) {
				c.addRepetition(index, index);
				transMap.put(c.getSynDiaElem(index + 1), t);
			} 

		} catch (NoNullElemException e) {
			System.out.println("Das war wohl kein Nullelement");
		}

	}
	
//	 FIXME [Andre] TestEbnfDefinition, später auf jeden Fall löschen!
	public static Definition getTestDef() {

		Definition ed = new Definition();
		try {

			EVariable start = new EVariable("Super Ingo");
			EVariable va = new EVariable("Nicht Diesel");

			ETerminalSymbol a = new ETerminalSymbol("a");
			ETerminalSymbol b = new ETerminalSymbol("b");
			ETerminalSymbol c = new ETerminalSymbol("c");

			ed.addTerminal(a);
			ed.addTerminal(b);
			ed.addTerminal(c);

			ed.addVariable(start);
			ed.addVariable(va);

			ed.setStartVariable(va);

			// Regel S Seite 152
			List<Term> sconclist = new ArrayList<Term>();
			List<Term> sconclist2 = new ArrayList<Term>();

			EOption salt = new EOption(b);

			sconclist2.add(c);
			sconclist2.add(salt);
			sconclist2.add(a);
			EConcatenation sconc2 = new EConcatenation(sconclist2);

			ERepetition srep = new ERepetition(sconc2);

			sconclist.add(srep);
			sconclist.add(va);

			EConcatenation sconc = new EConcatenation(sconclist);

			Rule rule1 = new Rule(start, sconc);

			// Regel A Seite 152
			List<Term> aconclist = new ArrayList<Term>();

			aconclist.add(a);
			aconclist.add(va);
			aconclist.add(b);

			EConcatenation aconc = new EConcatenation(aconclist);

			ECompoundTerm acomp = new ECompoundTerm(aconc);

			EAlternative aalt = new EAlternative(acomp, a);

			Rule rule2 = new Rule(va, aalt);

			ed.addRule(rule1);
			ed.addRule(rule2);

		} catch (DefinitionFormatException e) {

			System.out.println("irgendwas falsch hier");

		}

		return ed;
	}
	
	
}
