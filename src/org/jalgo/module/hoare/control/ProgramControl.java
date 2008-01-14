package org.jalgo.module.hoare.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import org.antlr.runtime.RecognitionException;
import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.control.edits.AssertionEdit;
import org.jalgo.module.hoare.control.edits.AssignVFApply;
import org.jalgo.module.hoare.control.edits.DeleteNodeApply;
import org.jalgo.module.hoare.gui.GuiControl;
import org.jalgo.module.hoare.gui.Node;
import org.jalgo.module.hoare.gui.Node.Status;
import org.jalgo.module.hoare.model.AndAssertion;
import org.jalgo.module.hoare.model.Assertion;
import org.jalgo.module.hoare.model.AssignVF;
import org.jalgo.module.hoare.model.ConcreteAssertion;
import org.jalgo.module.hoare.model.IfVF;
import org.jalgo.module.hoare.model.ModelControl;
import org.jalgo.module.hoare.model.NotAssertion;
import org.jalgo.module.hoare.model.OrAssertion;
import org.jalgo.module.hoare.model.StrongPreVF;
import org.jalgo.module.hoare.model.VerificationFormula;
import org.jalgo.module.hoare.model.WeakPostVF;

import c00.AST;
import c00.AST.ASTTree;

/**
 * The Program Controler
 * 
 * @author Stefan, Peter, Gerald
 * 
 */
public class ProgramControl {

	public enum Edit {
		DeleteNode, AssertionEdit, AssignVFApply, CompoundVFApply, IfVFApply, IfElseVFApply, StatSeqVFApply, StrongPreVFApply, WeakPostVFApply, IterationVFApply
	}

	UndoManager undoManager;

	GuiControl guiControl;

	ModelControl modelControl;

	Evaluation evaluator;

	public ProgramControl() {
		undoManager = new UndoManager();
		evaluator = new JepEvaluator(15, 50, this);
	}

	public synchronized void reportResult(int id, boolean result, String message) {
		if (result) {
			guiControl.reportInfo(Messages.getString("hoare", "out.evalOfNode")
					+ Messages.getString("hoare", "out.success"));
			guiControl.getNode(id).setStatus(Node.Status.ResultOK);
		} else {
			guiControl.reportError(Messages.getString("hoare", "out.evalError")
					+ message);
			guiControl.getNode(id).setStatus(Node.Status.ResultWrong);
		}
		guiControl.update(null, null);
	}

	public synchronized void reportError(int id, String message) {
		guiControl.reportError(message);
		guiControl.getNode(id).setStatus(Node.Status.Error);
		guiControl.update(null, null);
	}

	public boolean applyTreeEdit(Edit edit, int id) {

		UndoableEdit e = null;
		if (id < 0) {
			guiControl.reportError(Messages.getString("hoare",
					"out.noValidNode"));
			return false;
		}

		if (!(edit == Edit.WeakPostVFApply || edit == Edit.StrongPreVFApply || edit == Edit.DeleteNode))
			if (modelControl.getVF(id).isRuleApplied()) {
				guiControl.reportError(Messages.getString("hoare",
						"out.alreadyApplied"));

				return false;
			}

		switch (edit) {
		case AssignVFApply:
			e = AssignmentRule.getUndoableEdit(modelControl.getVF(id),
					modelControl.getVF(id).getPreAssertion(), modelControl
							.getVF(id).getPostAssertion(), modelControl
							.getCodeSegment(id), this);
			break;
		case CompoundVFApply:
			e = CompoundRule.getUndoableEdit(modelControl.getVF(id), guiControl
					.getNode(id));
			break;
		case StrongPreVFApply:
			e = StrongPreRule.getUndoableEdit(modelControl.getVF(id), this);
			break;
		case WeakPostVFApply:
			e = WeakPostRule.getUndoableEdit(modelControl.getVF(id), this);
			break;
		case DeleteNode:
			e = new DeleteNodeApply(modelControl.getVF(id), this);
			break;
		case StatSeqVFApply:
			e = StatementSequenceRule.getUndoableEdit(modelControl.getVF(id),
					guiControl.getNode(id));
			break;
		case IfVFApply:
			e = IfRule.getUndoableEdit(modelControl.getVF(id), guiControl
					.getNode(id), this);
			break;
		case IfElseVFApply:
			e = IfElseRule.getUndoableEdit(modelControl.getVF(id), guiControl
					.getNode(id));
			break;
		case IterationVFApply:
			e = IterationRule.getUndoableEdit(modelControl.getVF(id),
					guiControl.getNode(id));
			break;
		default:
			guiControl.reportError(edit + " "
					+ Messages.getString("hoare", "out.notApplicable"));
			return false;
		}

		try {
			e.apply();
			//if (e instanceof AssignVFApply)
				//guiControl.reportInfo(Messages.getString("hoare",	"out.assSuccess"));
			undoManager.addEdit(e);
			modelControl.notifyObservers();
			return true;
		} catch (Exception exception) {
			guiControl.reportError(exception.getMessage());
			modelControl.notifyObservers();
		}
	
		return false;
	}

	public boolean applyTreeEdit(Edit edit, int id, String preAssertion,
			String postAssertion) {

		UndoableEdit e = null;
		Assertion pre = null;
		Assertion post = null;

		switch (edit) {
		case AssertionEdit:

			try {

				if (!preAssertion.toString().equals(
						modelControl.getVF(id).getPreAssertion().toString()))
					pre = buildAssertionFromString(preAssertion);
				else
					pre = modelControl.getVF(id).getPreAssertion();
				if (!postAssertion.toString().equals(
						modelControl.getVF(id).getPostAssertion().toString()))
					post = buildAssertionFromString(postAssertion);
				else
					post = modelControl.getVF(id).getPostAssertion();

			} catch (IllegalArgumentException exception) {
				guiControl.reportError(Messages.getString("hoare",
						"out.wrongAssertion")
						+ ":<br>" + exception.getMessage());
				return false;
			}

			e = new AssertionEdit(modelControl.getVF(id), pre, post, this);

			break;
		default:
			guiControl.reportError(edit + " "
					+ Messages.getString("hoare", "out.notApplicable"));
			return false;

		}

		try {
			e.apply();
			undoManager.addEdit(e);

			modelControl.notifyObservers();
		} catch (Exception exception) {
			guiControl.reportError(exception.getMessage());
			modelControl.notifyObservers();
			return false;
		}

		return true;
	}
//
//	public void evaluateNode(VerificationFormula vf, Assertion ass) {
//
//		if (vf.getPreAssertion().equals(ass) && vf instanceof StrongPreVF
//				&& guiControl.getNode(vf.getID()).isVisible()) {
//			try {
//				guiControl.getNode(vf.getID()).setStatus(Status.Evaluating);
//				evaluator.evaluate(vf.getID(), modelControl.getPreviousVF(vf)
//						.getPreAssertion(), vf.getPreAssertion());
//			} catch (Exception e) {
//			}
//
//		}
//
//		if (vf.getPostAssertion().equals(ass) && vf instanceof WeakPostVF
//				&& guiControl.getNode(vf.getID()).isVisible()) {
//			try {
//				guiControl.getNode(vf.getID()).setStatus(Status.Evaluating);
//				evaluator.evaluate(vf.getID(), vf.getPostAssertion(),
//						modelControl.getPreviousVF(vf).getPostAssertion());
//			} catch (Exception e) {
//			}
//		}
//
//		if (ass.equals(vf.getPreAssertion())
//				|| ass.equals(vf.getPostAssertion())) {
//			for (VerificationFormula child : vf.getChildren())
//				evaluateNode(child, ass);
//		}
//	}

	
	public boolean testAssignmet(VerificationFormula vf){
		if (!vf.getPreAssertion().containsDummyAssertion()
				&& !vf.getPostAssertion().containsDummyAssertion())
		{
			// throw new
			// InvalidParameterException(Messages.getString("hoare",
			// "out.missingUserInput"));

				if (vf.getPreAssertion().toString().equals
				(
						replaceUnboundVariables(vf.getPostAssertion()
								.toString(), modelControl.getCodeSegment(vf
								.getID())))) {
					return true;
				}
		} 
		return false;
	}
	
	
	
	public void evaluateTree(VerificationFormula vf) {

		if (vf instanceof StrongPreVF
				&& guiControl.getNode(vf.getID()).isVisible()) {
			if (!(vf.getParent().getPreAssertion()
					.containsDummyAssertion())
					&& !(vf.getPreAssertion().containsDummyAssertion())) {
				guiControl.getNode(vf.getID()).setStatus(Status.Evaluating);
				evaluator.evaluate(vf.getID(), vf.getParent()
						.getPreAssertion(), vf.getPreAssertion());
			}

		} else if (vf instanceof WeakPostVF
				&& guiControl.getNode(vf.getID()).isVisible()) {
			if (!(vf.getParent().getPostAssertion()
					.containsDummyAssertion())
					&& !(vf.getPostAssertion().containsDummyAssertion())) {
				guiControl.getNode(vf.getID()).setStatus(Status.Evaluating);
				evaluator.evaluate(vf.getID(), vf.getPostAssertion(),
						vf.getParent().getPostAssertion());
			}

		} else if (vf.getParent() != null
				&& vf.getParent().getType().equals(IfVF.class)
				&& guiControl.getNode(vf.getID()).isVisible()) {

			if (!(vf.getParent().getPreAssertion().containsDummyAssertion())
					&& !(vf.getParent().getPostAssertion()
							.containsDummyAssertion())) {
				guiControl.getNode(vf.getID()).setStatus(Status.Evaluating);
				evaluator
						.evaluate(vf.getID(), new AndAssertion(vf.getParent()
								.getPreAssertion(), new NotAssertion(((IfVF) vf
								.getParent().pureVF()).getPi())), vf
								.getPostAssertion());
			}

		}

		if (vf.pureVF() instanceof AssignVF
				&& guiControl.getNode(vf.getID()).isVisible()) {
			vf.setRuleApplied(testAssignmet(vf));
		}

		for (VerificationFormula child : vf.getChildren())
			evaluateTree(child);
	}

	/**
	 * replaces all unbound variables in a string
	 * 
	 * @param assertionString
	 *            the string to work on
	 * @param codeSegment
	 *            an Assignstatement of C0
	 * @return a string in which all (semantically) unbound variables have been
	 *         replaced
	 */

	public String replaceUnboundVariables(String assertionString,
			String codeSegment) {

		String replace, replaceBy;
		StringBuffer remove = new StringBuffer();
		Pattern p_bound_variables = Pattern
				.compile("[a-z]+\\([^,]*,[^,]*,'(?:([a-zA-Z][a-zA-Z0-9]*))','");
		Matcher matcher = null;
		Set<String> bound_parameters = new HashSet<String>();

		if ((assertionString == null) || (codeSegment == null))
			return null;
		// findet gebundene Variable in Assertions
		matcher = p_bound_variables.matcher(assertionString);
		while (matcher.find()) {
			bound_parameters.add(matcher.group(1));
		}

		// initial enthaelt replace CodeSegment der Form
		// (<Ident>=<SimpleExpression>)
		// ersetze alle Leerzeichen (sicherheitshalber)
		replace = codeSegment.replaceAll("\\s", "");
		// auftrennen in vor dem "=" (replace) und danach (replaceBy)
		int index = replace.indexOf("=");
		if (index == -1)
			return null;
		else {
			replaceBy = replace.substring(index + 1);
			// entferne Semikolon am Ende (sonst darf es eh keines geben)
			replaceBy = replaceBy.replaceAll(";", "");
			replace = replace.substring(0, index);
			if (bound_parameters.contains(replace))
				return assertionString;
			// suche erstes Auftreten von replace
			index = assertionString.indexOf(replace);
			while (index != -1) {
				// Test ob nicht Teil eines anderen Wortes:
				if (assertionString.substring(index - 1).matches(
						"\\W" + replace + "\\W.*")) {
					// Ersetzung:
					remove.append(assertionString.substring(0, index));
					remove.append(replaceBy);
					remove.append(assertionString.substring(index
							+ replace.length()));
					assertionString = remove.toString();
					remove = new StringBuffer();
					// naechstes Vorkommen so vorhanden:
					index = assertionString.indexOf(replace, index
							+ replaceBy.length());
				} else
					index = assertionString.indexOf(replace, index
							+ replace.length());
			}
		}
		return assertionString;
	}

	public ASTTree createAST(String code, StringBuffer errorBuffer)
			throws RecognitionException {
		return AST.parseString(code, "StatementSequence", errorBuffer);
	}

	/**
	 * builds an assertiontree
	 * 
	 * @param assertion
	 *            the string which should be transformed
	 * @return the assertiontree
	 */

	public Assertion buildAssertionFromString(String assertion)
			throws IllegalArgumentException {

		// akzeptierte Grammatik:
		// (1) Assertion ::= (Assertion) && (Assertion)
		// (2) Assertion ::= Assertion || Assertion
		// (3) Assertion ::= (Assertion)
		// (4) Assertion ::= !(Assertion)
		// (5) Assertion ::= ConcreteAssertion
		// ConcreteAssertion wird nicht geparst!!!

		int currentIndex = 0;
		int index1 = 0;
		int index2 = 1;
		int klammerAuf = 0;
		int klammerZu = 0;
		boolean found = false;

		// Syntaxerror: ")(" :
		if (assertion.matches(".*?\\)\\(.*"))
			throw new IllegalArgumentException(Messages.getString("hoare",
					"out.missingOperator"));

		// Syntaxerror: falls "&&" nicht in dieser Form: "&&( oder &&!"
		// (auf diese Weise wird verhindert das eine falsche AndAssertion
		// erstellt wird)
		if (assertion.matches(".*&&[^!\\(].*"))
			throw new IllegalArgumentException(Messages.getString("hoare",
					"out.missingBracketsAnd"));
		if (assertion.matches(".*[^\\)]&&.*"))
			throw new IllegalArgumentException(Messages.getString("hoare",
					"out.missingBracketsAnd"));
		// analog OrAssertion:
		if (assertion.matches(".*\\|\\|[^!\\(].*"))
			throw new IllegalArgumentException(Messages.getString("hoare",
					"out.missingBracketsOr"));
		if (assertion.matches(".*[^)]\\|\\|.*"))
			throw new IllegalArgumentException(Messages.getString("hoare",
					"out.missingBracketsOr"));

		// Syntaxerror: ungleiche Anzahl "(" und ")" :
		currentIndex = assertion.indexOf("(");
		while (currentIndex != -1) {
			klammerAuf++;
			currentIndex = assertion.indexOf("(", currentIndex + 1);
		}
		currentIndex = assertion.indexOf(")");
		while (currentIndex != -1) {
			klammerZu++;
			currentIndex = assertion.indexOf(")", currentIndex + 1);
		}
		if (klammerAuf != klammerZu)
			throw new IllegalArgumentException(Messages.getString("hoare",
					"out.wrongNrOfBrackets"));

		klammerAuf = 0;
		klammerZu = 0;

		// suche Klammer Nr.1

		currentIndex = assertion.indexOf("(");
		if (currentIndex != -1)
			klammerAuf++;

		// finde eine Stelle an der die Zahl der oeffnenden
		// und schliessenden Klammer gleich gross ist, d.h.
		// Ende oder eine Stelle an der (1) oder (2) auftritt

		while ((!found) && (index1 != index2)) {
			index1 = assertion.indexOf("(", currentIndex + 1);
			index2 = assertion.indexOf(")", currentIndex + 1);

			if (index1 == -1)
				index1 = assertion.length() - 1;
			if (index2 == -1)
				index2 = assertion.length() - 1;

			if (index1 < index2) {
				currentIndex = index1;
				klammerAuf++;
			} else {
				currentIndex = index2;
				klammerZu++;
			}

			if (klammerAuf == klammerZu)
				found = true;
		}

		// Fall (1) oder (2)
		if (currentIndex != (assertion.length() - 1)) {
			// Fall (1) fuehrender Operator UND
			if (assertion.substring(currentIndex + 1).startsWith("&&")) {
				return new AndAssertion(buildAssertionFromString(assertion
						.substring(0, currentIndex + 1)),
						buildAssertionFromString(assertion
								.substring(currentIndex + 3)));
			}
			// Fall (2) fuehrender Operator ODER
			if (assertion.substring(currentIndex + 1).startsWith("||")) {
				return new OrAssertion(buildAssertionFromString(assertion
						.substring(0, currentIndex + 1)),
						buildAssertionFromString(assertion
								.substring(currentIndex + 3)));
			}
		}
		// Fall (3) oder (4)
		else {
			// Fall (3) Klammerung entfernen
			if (assertion.startsWith("(")) {
				return buildAssertionFromString(assertion.substring(1,
						assertion.length() - 1));
			}
			// Fall (4) NOT
			if (assertion.startsWith("!")) {
				return new NotAssertion(buildAssertionFromString(assertion
						.substring(1)));
			}
		}
		// Fall (5)
		// Syntaxerror: ConcreteAssertion enthaelt noch UND bzw.
		// HTML-Ersetzungen oder ein ODER
		if ((assertion.matches(".*&.*")) || (assertion.matches(".*\\|\\|.*")))
			throw new IllegalArgumentException(Messages.getString("hoare",
					"out.syntaxInvalid"));
		return new ConcreteAssertion(assertion);
	}

	public boolean init(File file) {
		long length = file.length();
		char buffer[] = new char[(int) length];// truncation!

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(file));

			reader.read(buffer, 0, (int) length);

		} catch (IOException e) {
			guiControl.reportError(e.getMessage());
			return false;
		}
		return init(String.copyValueOf(buffer));

	}

	public void init() {
		undoManager.discardAllEdits();
		modelControl.setRoot(null);
		modelControl.setCode("");
		modelControl.notifyObservers();
	}

	public boolean init(String code) {
		undoManager.discardAllEdits();

		// scanf und printf entfernen:
		code = Pattern.compile("(scanf|printf).*?;").matcher(code)
				.replaceAll("");

		// anfangs-\n entfernen
		code = Pattern.compile("\\A(\\s)+").matcher(code).replaceAll("");

		// workaround für Linux und Solaris, damit der Highlighter auch bei
		// unter Windows
		// gespeicherten Programmen funktioniert
		if (System.getProperty("line.separator").length() == 1)
			code = code.replaceAll("\\r", "");

		try {
			StringBuffer errorBuffer = new StringBuffer();
			ASTTree tmp = createAST(code, errorBuffer);
			if (tmp != null) {
				// Is n statSeq, da es das anfangssymb war
				modelControl.createVF((AST.StatementSequence) tmp, code);
				modelControl.setCode(code);
				modelControl.notifyObservers();
			} else // error
			{
				guiControl.reportError(errorBuffer.toString());
			}
		} catch (RecognitionException e) {
			guiControl.reportError(e.getMessage());
			return false;
		}

		return true;
	}

	public boolean canRedo() {
		// TODO: gibt immer false zurï¿½ck?
		return undoManager.canRedo();
	}

	public boolean canUndo() {
		// TODO: gibt immer false zurï¿½ck?
		return undoManager.canUndo();
	}

	public void redo() throws CannotRedoException {
		undoManager.redo();
	}

	public void undo() throws CannotUndoException {
		undoManager.undo();
	}

	public GuiControl getGuiControl() {
		return guiControl;
	}

	public void setGuiControl(GuiControl guiControl) {
		this.guiControl = guiControl;
	}

	public ModelControl getModelControl() {
		return modelControl;
	}

	public void setModelControl(ModelControl modelControl) {
		this.modelControl = modelControl;
	}

	public void setMaxEvalTime(int time) {
		evaluator.setMax_eval_time(time);
	}

	public int getEvalAmount() {
		return evaluator.getEvaluations();
	}
}