/**
 * 
 */
package org.jalgo.module.ebnf.renderer;

import java.util.List;

import org.jalgo.module.ebnf.renderer.elements.RenderBranch;
import org.jalgo.module.ebnf.renderer.elements.RenderElement;
import org.jalgo.module.ebnf.renderer.elements.RenderNoStairBranch;
import org.jalgo.module.ebnf.renderer.elements.RenderNullElem;
import org.jalgo.module.ebnf.renderer.elements.RenderRepetition;
import org.jalgo.module.ebnf.renderer.elements.RenderTerminal;
import org.jalgo.module.ebnf.renderer.elements.RenderTrans;
import org.jalgo.module.ebnf.renderer.elements.RenderVariable;

/**
 * This factory creates a <code>RenderElement</code> by type
 * 
 * @author Andre Viergutz
 */
public class RenderElementFactory {

	/**
	 * represents a render element
	 */
	private static RenderElement element;

	/**
	 * This method gets the type of the RenderElement to create, to be found in
	 * the RenderValues and the name of the element in case of a Variable or
	 * TerminalSymbol and returns a RenderElement RenderElement
	 * 
	 * @param type the type identified by an int to be found in the RenderValues
	 * @param label the name of a Variable or a TerminalSymbol
	 * @param stringList A list of first children Strings (from EBNF <code>Definition</code>)
	 * @return a RenderElement
	 */
	public static RenderElement createElement(int type, String label, List<String> stringList) {

		if (type == RenderValues.TERMINAL)
			element = new RenderTerminal(label);
		else if (type == RenderValues.VARIABLE)
			element = new RenderVariable(label);
		else if (type == RenderValues.BRANCH)
			element = new RenderBranch();
		else if (type == RenderValues.BRANCHNOSTAIRS)
			element = new RenderNoStairBranch();
		else if (type == RenderValues.REPETITION)
			element = new RenderRepetition();
		else if (type == RenderValues.NULLELEM)
			element = new RenderNullElem();
		else if (type == RenderValues.TRANSTERM)
			element = new RenderTrans(label, stringList);
		return element;

	}

	/**
	 * This method gets the type of the RenderElement to create, to be found in
	 * the RenderValues and returns a
	 * RenderElement
	 * 
	 * @param type the type identified by an int to be found in the RenderValues
	 * @return a RenderElement
	 */
	public static RenderElement createElement(int type) {

		return createElement(type, "leer", null);

	}

}
