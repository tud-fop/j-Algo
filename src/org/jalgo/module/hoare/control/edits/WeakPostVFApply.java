package org.jalgo.module.hoare.control.edits;

import java.security.InvalidParameterException;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.jalgo.module.hoare.control.ProgramControl;
import org.jalgo.module.hoare.control.UndoableEdit;
import org.jalgo.module.hoare.model.DummyAssertion;
import org.jalgo.module.hoare.model.VerificationFormula;
import org.jalgo.module.hoare.model.WeakPostVF;

public class WeakPostVFApply implements UndoableEdit {

	private VerificationFormula vf;

	private WeakPostVF post;

	private ProgramControl programControl;

	public WeakPostVFApply(VerificationFormula vf, ProgramControl programControl) {
		this.vf = vf;

		this.programControl = programControl;
	}

	public void apply() throws InvalidParameterException {

		post = new WeakPostVF(vf.getPreAssertion(), new DummyAssertion(), vf);
		for (VerificationFormula child : vf.getChildren()) {
			child.replaceAssertion(vf.getPostAssertion(), post
					.getPostAssertion());
			if (programControl.getGuiControl().getNode(child.getID())
					.isVisible())
				post.setRuleApplied(true);

		}

		programControl.getGuiControl().registerNode(post.getID(),
				programControl.getGuiControl().getDefaultLabel(vf.pureVF()),
				true, true);

		vf.insertChild(post);
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
			child.replaceAssertion(vf.getPostAssertion(), post
					.getPostAssertion());

		vf.insertChild(post);
		vf.setRuleApplied(true);
	}

	public boolean replaceEdit(javax.swing.undo.UndoableEdit anEdit) {
		// TODO Auto-generated method stub
		return false;
	}

	public void undo() throws CannotUndoException {
		vf.deleteChild();
		for (VerificationFormula child : vf.getChildren())
			child.replaceAssertion(post.getPostAssertion(), vf
					.getPostAssertion());

		vf.setRuleApplied(false);
	}

}
