package org.jalgo.module.hoare.control.edits;

import java.security.InvalidParameterException;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.control.UndoableEdit;
import org.jalgo.module.hoare.gui.Node;
import org.jalgo.module.hoare.model.IterationVF;
import org.jalgo.module.hoare.model.VerificationFormula;

public class IterationVFApply implements UndoableEdit {

	Node node;

	VerificationFormula vf;

	public IterationVFApply(VerificationFormula vf, Node node) {
		this.node = node;
		this.vf = vf;
	}

	public void apply() throws InvalidParameterException {
		if (vf.getType().equals(IterationVF.class)) {
			for (Node ch : node.getChildren())
				ch.setVisible(true);
		} else
			throw new InvalidParameterException(Messages.getString("hoare",
					"rule.iter.title")
					+ Messages.getString("hoare", "out.notApplieableOn")
					+ Messages.getString("hoare", "name." + vf.pureVF()));

		vf.setRuleApplied(true);
	}

	public boolean addEdit(javax.swing.undo.UndoableEdit anEdit) {
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

	public boolean replaceEdit(javax.swing.undo.UndoableEdit anEdit) {
		// TODO Auto-generated method stub
		return false;
	}

	public void undo() throws CannotUndoException {
		for (Node ch : node.getChildren())
			ch.setVisible(false);
		vf.setRuleApplied(false);
	}

}
