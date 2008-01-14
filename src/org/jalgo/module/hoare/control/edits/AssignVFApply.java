package org.jalgo.module.hoare.control.edits;

import java.security.InvalidParameterException;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.control.ProgramControl;
import org.jalgo.module.hoare.control.UndoableEdit;
import org.jalgo.module.hoare.model.Assertion;
import org.jalgo.module.hoare.model.AssignVF;
import org.jalgo.module.hoare.model.VerificationFormula;

public class AssignVFApply implements UndoableEdit {

	private VerificationFormula vf;

	// Zeiger auf neue PreAssertion
	private Assertion pre;

	private Assertion post;

	private String code;

	private ProgramControl programControl;

	public AssignVFApply(VerificationFormula vf, Assertion pre, Assertion post,
			String code, ProgramControl programControl) {
		this.vf = vf;
		this.pre = pre;
		this.post = post;
		this.code = code;
		this.programControl = programControl;
	}

	public void apply() throws InvalidParameterException {
		if (!(vf.getType().equals(AssignVF.class)))
			throw new InvalidParameterException(Messages.getString("hoare",
					"rule.assign.title")
					+ Messages.getString("hoare", "out.notApplieableOn")
					+ Messages.getString("hoare", "name." + vf.pureVF()));

		// if(vf.getPreAssertion().containsDummyAssertion() ||
		// vf.getPostAssertion().containsDummyAssertion())
		// throw new InvalidParameterException(Messages.getString("hoare",
		// "out.missingUserInput"));
		//		
		// if(pre.toString().equals(replaceUnboundVariables(post.toString(),
		// code))) {
		// vf.setRuleApplied(true);
		// }
		// else throw new InvalidParameterException(Messages.getString("hoare",
		// "out.evalError"));
		if (!programControl.testAssignmet(vf))
			 throw new InvalidParameterException(Messages.getString("hoare","out.assignError"));
		programControl.evaluateTree(vf);
					
	}

	public boolean addEdit(javax.swing.undo.UndoableEdit arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean canRedo() {
		return true;
	}

	public boolean canUndo() {
		return true;
	}

	public void die() {
		// TODO Auto-generated method stub

	}

	public String getPresentationName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRedoPresentationName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUndoPresentationName() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSignificant() {
		return true;
	}

	public void redo() throws CannotRedoException {
		programControl.evaluateTree(vf);
	}

	public boolean replaceEdit(javax.swing.undo.UndoableEdit arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public void undo() throws CannotUndoException {
		/*
		 * if (preApplied) vf.replaceAssertion(replace.getTarget(), oldPre);
		 */

		programControl.evaluateTree(vf);
	}

	// /**
	// * replaces all unbound variables in a string
	// * @param assertionString the string to work on
	// * @param codeSegment an Assignstatement of C0
	// * @return a string in which all (semantically) unbound
	// * variables have been replaced
	// */
	//
	// public String replaceUnboundVariables(String assertionString, String
	// codeSegment) {
	//		
	// String replace, replaceBy;
	// StringBuffer remove = new StringBuffer();
	// Pattern p_bound_variables =
	// Pattern.compile("[a-z]+\\([^,]*,[^,]*,'(?:([a-zA-Z][a-zA-Z0-9]*))','");
	// Matcher matcher = null;
	// Set<String> bound_parameters = new HashSet<String>();
	//		
	// if ((assertionString==null)||(codeSegment==null)) return null;
	// // findet gebundene Variable in Assertions
	// matcher = p_bound_variables.matcher(assertionString);
	// while (matcher.find()) {
	// bound_parameters.add(matcher.group(1));
	// }
	//		
	// // initial enthaelt replace CodeSegment der Form
	// (<Ident>=<SimpleExpression>)
	// // ersetze alle Leerzeichen (sicherheitshalber)
	// replace = codeSegment.replaceAll("\\s", "");
	// // auftrennen in vor dem "=" (replace) und danach (replaceBy)
	// int index = replace.indexOf("=");
	// if (index == -1) return null;
	// else {
	// replaceBy = replace.substring(index+1);
	// // entferne Semikolon am Ende (sonst darf es eh keines geben)
	// replaceBy = replaceBy.replaceAll(";", "");
	// replace = replace.substring(0, index);
	// if (bound_parameters.contains(replace)) return assertionString;
	// // suche erstes Auftreten von replace
	// index = assertionString.indexOf(replace);
	// while (index != -1) {
	// // Test ob nicht Teil eines anderen Wortes:
	// if (assertionString.substring(index-1).matches("\\W"+replace+"\\W.*")) {
	// //Ersetzung:
	// remove.append(assertionString.substring(0, index));
	// remove.append(replaceBy);
	// remove.append(assertionString.substring(index+replace.length()));
	// assertionString = remove.toString();
	// remove = new StringBuffer();
	// // naechstes Vorkommen so vorhanden:
	// index = assertionString.indexOf(replace, index+replaceBy.length());
	// }
	// else index = assertionString.indexOf(replace, index+replace.length());
	// }
	// }
	// return assertionString;
	// }

}
