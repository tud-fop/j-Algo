package org.jalgo.module.hoare.control.edits;

import java.security.InvalidParameterException;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.jalgo.module.hoare.control.ProgramControl;
import org.jalgo.module.hoare.control.UndoableEdit;
import org.jalgo.module.hoare.model.DummyAssertion;
import org.jalgo.module.hoare.model.StrongPreVF;
import org.jalgo.module.hoare.model.VerificationFormula;

public class StrongPreVFApply implements UndoableEdit {

	VerificationFormula vf;

	private StrongPreVF pre;

	private ProgramControl programControl;

	public StrongPreVFApply(VerificationFormula vf,
			ProgramControl programControl) {
		this.vf = vf;

		this.programControl = programControl;
	}

	public void apply() throws InvalidParameterException {

		pre = new StrongPreVF(new DummyAssertion(), vf.getPostAssertion(), vf);

		for (VerificationFormula child : vf.getChildren()) {
			child.replaceAssertion(vf.getPreAssertion(), pre.getPreAssertion());
			if (programControl.getGuiControl().getNode(child.getID())
					.isVisible())
				pre.setRuleApplied(true);
		}

		programControl.getGuiControl().registerNode(pre.getID(),
				programControl.getGuiControl().getDefaultLabel(vf.pureVF()),
				true, true);
		
		vf.insertChild(pre);
		vf.setRuleApplied(true);
		programControl.getModelControl().updateNodeMap();
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

		for (VerificationFormula child : vf.getChildren())
			child.replaceAssertion(vf.getPreAssertion(), pre.getPreAssertion());
		vf.insertChild(pre);
		vf.setRuleApplied(true);
	}

	public boolean replaceEdit(javax.swing.undo.UndoableEdit anEdit) {
		// TODO Auto-generated method stub
		return false;
	}

	public void undo() throws CannotUndoException {
		vf.deleteChild();
		for (VerificationFormula child : vf.getChildren())
			child.replaceAssertion(pre.getPreAssertion(), vf.getPreAssertion());

		vf.setRuleApplied(false);
	}

}
