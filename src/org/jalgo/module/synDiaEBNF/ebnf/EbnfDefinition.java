/*
 * Created on 01.05.2004
 */
 
package org.jalgo.module.synDiaEBNF.ebnf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

/**
 * symbolize an Extended Backus-Naur-Form, which consist of a final set of EBNF-Rules, 
 * describing a Language. This EbnfDefinition stands for the tupel. Ebnf=(V,E,S,R), 
 * V is a final set of SyntaxVariables
 * E is a final set of Terminalsymbols
 * S is included in V, and is the startsymbol
 * R is a final set of Rules on the form af v::=...
 *  
 * @author Benjamin Scholz
 * @author Babett Schaliz
 */
public class EbnfDefinition implements Serializable {

	private String label;
	// Name of the EbnfDefinition, mostly the greek "epsilon"
	private Set variables;
	// Set of "EbnfSynVariable"s (the syntactical variables)
	private Set alphabet;
	// Set of "EbnfTerminal"s (the terminal symbols)
	private EbnfSynVariable startVariable;

	/**
	 * standard contructor which takes no arguments
	 */
	public EbnfDefinition() {
		this(null, null);
	}

	/**
	 * Thats the Constructor, give a new EbnfDefinition, with label you give the name.
	 * @param label   name of the EbnfDefinition, mostly the greek "epsilon"
	 */
	public EbnfDefinition(String label) {
		this(label, null);
	}

	/**
	 * That's the Constructor, give a new EbnfDefinition, with label you give the name.
	 * @param label           Name of the EbnfDefinition, mostly the greek "epsilon"
	 * @param startVariable   a EbnfSynVariable 
	 */
	public EbnfDefinition(String label, EbnfSynVariable startVariable) {
		this.label = label;
		this.startVariable = startVariable;
		this.variables = new HashSet();
		this.alphabet = new HashSet();
	}

	/**
	 * Give you the final set of the Terminalsymbols
	 * @return   a Set of <code>EbnfTerminal</code>s (the terminal symbols)
	 */
	public Set getAlphabet() {
		return alphabet;
	}

	/**
	 * Give you the name of the Ebnf-EbnfDefinition.
	 * @return   the Name of the EbnfDefinition, mostly the greek "epsilon"
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Give the special decorated EbnfSynVariable which is the start symbol
	 * @return   the start variable a EbnfSynVariable
	 */
	public EbnfSynVariable getStartVariable() {
		return startVariable;
	}

	/**
	 * Give a final set of the SyntaxVariables.
	 * @return   a Set of <code>EbnfSynVariable</code>s (the syntactical variables)
	 */
	public Set getVariables() {
		return variables;
	}

	/**
	 * You can give EbnfDefinition a name.
	 * @param label   Name of the EbnfDefinition, mostly the greek "epsilon"
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * here you can set the decoration Startvariable on an other EbnfSynVariable
	 * @param startVariable   a EbnfSynVariable
	 */
	public void setStartVariable(EbnfSynVariable startVariable) {
		this.startVariable = startVariable;
	}

	/**
	 * you can't add a SynDiaVariable to a Set if it already exists in it
	 * @param variable   a syntactical variable
	 */
	public void addVariable(EbnfSynVariable variable) {
		variables.add(variable);
	}

	/**
	 * you can't add a terminal symbol to a Set if it already exists in it
	 * @param term   a terminal symbol
	 */
	public void addTerminal(EbnfTerminal terminal) {
		alphabet.add(terminal);
	}

	/**
	 * this method creates the graphical representation of a <code>Definition</code> object
	 * @param shell a <code>org.eclipse.swt.widgets.Shell</code> object
	 * @return a "StyledText" object
	 * @see org.eclipse.swt.widgets.Shell
	 * @see org.eclipse.swt.custom.StyledText
	 */
	public StyledText styledText(Composite parent) {
		StyledText widget = new StyledText(parent, SWT.BORDER);
		widget.append(label + " = (V, \u03A3, " + startVariable.getLabel() + Messages.getString("EbnfDefinition.,_R)_mit_n_2")); //$NON-NLS-1$ //$NON-NLS-2$

		/* variables */
		widget.append("  V = {"); //$NON-NLS-1$
		for (Iterator it = variables.iterator(); it.hasNext();) {
			widget.append(((EbnfSynVariable) it.next()).getLabel());
			if (it.hasNext())
				widget.append(", "); //$NON-NLS-1$
		}
		widget.append("},\n"); //$NON-NLS-1$
		/* alphabet */
		widget.append("  \u03A3 = {"); //$NON-NLS-1$
		for (Iterator it = alphabet.iterator(); it.hasNext();) {
			widget.append(((EbnfTerminal) it.next()).getLabel());
			if (it.hasNext())
				widget.append(", "); //$NON-NLS-1$
		}
		widget.append("},\n"); //$NON-NLS-1$
		/* rules */
		widget.append(Messages.getString("EbnfDefinition.__und_R__n_9")); //$NON-NLS-1$
		for (Iterator it = variables.iterator(); it.hasNext();) {
			EbnfSynVariable var = (EbnfSynVariable) it.next();
			EbnfElement elem = var.getStartElem();
			ArrayList styleList = new ArrayList();
			widget.append("    " + var.getLabel() + " ::= "); //$NON-NLS-1$ //$NON-NLS-2$
			elem.render(
				parent.getShell(),
				styleList,
				widget.getText().length());
			widget.append(elem.toString());
			for (Iterator it2 = styleList.iterator(); it2.hasNext();) {
				widget.setStyleRange((StyleRange) it2.next());
			}
			if (it.hasNext())
				widget.append("\n"); //$NON-NLS-1$
		}
		
		return widget;
	}
}
