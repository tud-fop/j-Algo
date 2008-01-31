/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and
 * platform independent. j-Algo is developed with the help of Dresden
 * University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.jalgo.module.ebnf.gui.ebnf;

import java.util.ArrayList;
import java.util.List;

import org.jalgo.module.ebnf.model.ebnf.*;

public class EbnfRenderer {

	/**
	 * Creates a String using the meta symbols form the ebnfsans font. if
	 * renderStrict is set to true the definition renders the alternatives as if
	 * they were binary. The added parentheses are highlighted.
	 * 
	 * @param term the term to be redered
	 * @param renderStrict includes parentheses to make the definition strict
	 * @return the rendered output-string
	 */
	public static String toRenderString(Term term, boolean renderStrict, String delimiter) {
		String renderString = new String();
		if (term == null)
			return "<null>";

		if (term instanceof ETerminalSymbol) {
			return ((ETerminalSymbol) term).getName();
		} else if (term instanceof EVariable) {
			return ((EVariable) term).getName();
		} else if (term instanceof EOption) {
			renderString = String.valueOf(RenderConstants.LBRACKET);
			renderString += toRenderString(((EOption) term).getTerm(),
					renderStrict, delimiter);
			renderString += String.valueOf(RenderConstants.RBRACKET);
			return renderString;
		} else if (term instanceof ERepetition) {
			renderString = String.valueOf(RenderConstants.LBRACE);
			renderString += toRenderString(((ERepetition) term).getTerm(),
					renderStrict, delimiter);
			renderString += String.valueOf(RenderConstants.RBRACE);
			return renderString;
		} else if (term instanceof ECompoundTerm) {
			renderString = String.valueOf(RenderConstants.LPARENTHESES);
			renderString += toRenderString(((ECompoundTerm) term).getTerm(),
					renderStrict, delimiter);
			renderString += String.valueOf(RenderConstants.RPARENTHESES);
			return renderString;
		} else if (term instanceof EConcatenation) {

			for (Term concatTerm : term.getTerms())
				renderString += toRenderString(concatTerm, renderStrict, delimiter) + delimiter;
			return renderString;
		} else if ((term instanceof EAlternative) && !renderStrict) {
			Term altTerm;
			renderString = String.valueOf(RenderConstants.LPARENTHESES);
			for (int i = 0; i < term.getTerms().size(); i++) {
				altTerm = term.getTerms().get(i);
				renderString += toRenderString(altTerm, renderStrict, delimiter);
				if (i != term.getTerms().size() - 1)
					renderString += String.valueOf(RenderConstants.ALTERNATIVE);
			}
			renderString += RenderConstants.RPARENTHESES;
			return renderString;
		} else if (term instanceof EAlternative && renderStrict) {
			Term altTerm;

			renderString += String.valueOf(RenderConstants.LPARENTHESES);
			altTerm = term.getTerms().get(0);
			renderString += toRenderString(altTerm, renderStrict, delimiter);
			renderString += String.valueOf(RenderConstants.ALTERNATIVE);

			for (int i = 1; i < term.getTerms().size() - 1; i++) {

				renderString += String.valueOf(RenderConstants.LPARENTHESES_M);
				altTerm = term.getTerms().get(i);
				renderString += toRenderString(altTerm, renderStrict, delimiter);
				renderString += String.valueOf(RenderConstants.ALTERNATIVE);

			}

			altTerm = term.getTerms().get(term.getTerms().size() - 1);
			renderString += toRenderString(altTerm, renderStrict, delimiter);

			for (int i = 0; i < term.getTerms().size() - 2; i++) {
				renderString += String.valueOf(RenderConstants.RPARENTHESES_M);

			}
			renderString += RenderConstants.RPARENTHESES;
			return renderString;
		}

		return renderString;
	}

	/**
	 * @param term the term to be redered
	 * @param renderStrict includes parentheses to make the definition strict
	 * @return the rendered output-string-list
	 */
	public static List<String> toRenderStringList(Term term,
			boolean renderStrict) {

		List<String> stringList = new ArrayList<String>();

		if (term == null)
			stringList.add("<null>");

		if (term instanceof ETerminalSymbol) {

			stringList.add(((ETerminalSymbol) term).getName());

		} else if (term instanceof EVariable) {

			stringList.add(((EVariable) term).getName());

		} else if (term instanceof EOption) {
			stringList.add(String.valueOf(RenderConstants.LBRACKET));
			stringList.add(toRenderString(((EOption) term).getTerm(),
					renderStrict, ""));
			stringList.add(String.valueOf(RenderConstants.RBRACKET));

		} else if (term instanceof ERepetition) {
			stringList.add(String.valueOf(RenderConstants.LBRACE));
			stringList.add(toRenderString(((ERepetition) term).getTerm(),
					renderStrict, ""));
			stringList.add(String.valueOf(RenderConstants.RBRACE));

		} else if (term instanceof ECompoundTerm) {
			stringList.add(String.valueOf(RenderConstants.LPARENTHESES));
			stringList.add(toRenderString(((ECompoundTerm) term).getTerm(),
					renderStrict, ""));
			stringList.add(String.valueOf(RenderConstants.RPARENTHESES));

		} else if (term instanceof EConcatenation) {

			for (Term concatTerm : term.getTerms())
				stringList.add(toRenderString(concatTerm, renderStrict, ""));
			
		} else if (term instanceof EAlternative) {
			Term altTerm;
			stringList.add(String.valueOf(RenderConstants.LPARENTHESES));
			for (int i = 0; i < term.getTerms().size(); i++) {
				altTerm = term.getTerms().get(i);
				stringList.add(toRenderString(altTerm, renderStrict, ""));
				if (i != term.getTerms().size() - 1)
					stringList.add(String.valueOf(RenderConstants.ALTERNATIVE));
			}
			stringList.add(String.valueOf(RenderConstants.RPARENTHESES));
			
		} 

		return stringList;

	}

}
