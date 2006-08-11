/**
 * 
 */
package org.jalgo.module.ebnf.gui.trans;

import java.awt.Font;

import org.jalgo.module.ebnf.gui.trans.explanations.ExpAlt;
import org.jalgo.module.ebnf.gui.trans.explanations.ExpConc;
import org.jalgo.module.ebnf.gui.trans.explanations.ExpOpt;
import org.jalgo.module.ebnf.gui.trans.explanations.ExpRep;
import org.jalgo.module.ebnf.gui.trans.explanations.ExpTemp;
import org.jalgo.module.ebnf.gui.trans.explanations.ExpTerminal;
import org.jalgo.module.ebnf.gui.trans.explanations.ExpVar;
import org.jalgo.module.ebnf.gui.trans.explanations.Explanation;
import org.jalgo.module.ebnf.model.ebnf.*;

/**
 * @author Andre Viergutz
 *
 */
public class ExplanationFactory {
	
	/** This factory creates an Explanation by getting a Term
	 * @param t The Term, to which the explanation is to be shown
	 * @param ebnffont 
	 * @return an <code>Explanation</code>
	 */
	public static Explanation getExplanation(Term t, Font ebnffont) {
		
		
		if (t instanceof ETerminalSymbol) {
			
			return new ExpTerminal(ebnffont);
			
		} else if (t instanceof EVariable) {
			
			return new ExpVar(ebnffont);
		
		} else if (t instanceof ERepetition) {
			
			return new ExpRep(ebnffont);
		
		} else if (t instanceof EOption) {
			
			return new ExpOpt(ebnffont);
		
		} else if (t instanceof EAlternative) {
			
			return new ExpAlt(ebnffont);
		} else if (t instanceof EConcatenation) {
			
			return new ExpConc(ebnffont);
		}
		
		return new ExpTemp(ebnffont);
		
	}
		

}
