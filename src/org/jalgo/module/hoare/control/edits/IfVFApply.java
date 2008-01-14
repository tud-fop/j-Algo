package org.jalgo.module.hoare.control.edits;

import java.security.InvalidParameterException;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.control.ProgramControl;
import org.jalgo.module.hoare.control.UndoableEdit;
import org.jalgo.module.hoare.gui.Node;
import org.jalgo.module.hoare.model.IfVF;
import org.jalgo.module.hoare.model.VerificationFormula;

public class IfVFApply implements UndoableEdit {

	Node node;

	VerificationFormula vf;

	ProgramControl control;

	public IfVFApply(VerificationFormula vf, Node node, ProgramControl control) {
		this.node = node;
		this.vf = vf;
		this.control = control;
	}

	public void apply() throws InvalidParameterException {

		if (vf.getType().equals(IfVF.class)) {
			for (Node ch : node.getChildren())
				ch.setVisible(true);

		} else
			throw new InvalidParameterException(Messages.getString("hoare",
					"rule.alt1.title")
					+ Messages.getString("hoare", "out.notApplieableOn")
					+ Messages.getString("hoare", "name." + vf.pureVF()));

		vf.setRuleApplied(true);

		control.evaluateTree(vf);

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
		for (Node ch : node.getChildren())
			ch.setVisible(true);

		vf.setRuleApplied(true);
	}

	public boolean replaceEdit(javax.swing.undo.UndoableEdit arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public void undo() throws CannotUndoException {
		for (Node ch : node.getChildren())
			ch.setVisible(false);
		vf.setRuleApplied(false);
	}

}
