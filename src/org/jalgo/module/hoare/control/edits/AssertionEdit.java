package org.jalgo.module.hoare.control.edits;

import java.security.InvalidParameterException;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.jalgo.module.hoare.control.ProgramControl;
import org.jalgo.module.hoare.control.UndoableEdit;
import org.jalgo.module.hoare.model.Assertion;
import org.jalgo.module.hoare.model.StatSeqVF;
import org.jalgo.module.hoare.model.VerificationFormula;

public class AssertionEdit implements UndoableEdit {

	VerificationFormula vf;

	Assertion pre;

	Assertion post;

	Assertion oldPre;

	Assertion oldPost;

	boolean preApplied;

	boolean postApplied;

	ProgramControl programControl;

	public AssertionEdit(VerificationFormula vf, Assertion pre, Assertion post,
			ProgramControl programControl) {
		this.vf = vf;
		this.pre = pre;
		this.post = post;
		preApplied = postApplied = false;
		this.programControl = programControl;

	}

	public void apply() throws InvalidParameterException {

		// replace-Reihenfolge ist essentiel!!!!!!!!!!!!
		if (!vf.getPostAssertion().toString().equals(post.toString())) {
			postApplied = true;
			oldPost = vf.getPostAssertion();
			// damit die kinder der StatementSeq zusammen geändert werden!
			if (vf.getParent() != null
					&& vf.getParent().getType().equals(StatSeqVF.class))
				vf = vf.getParent();
			vf.replaceAssertion(oldPost, post);
		}

		if (!vf.getPreAssertion().toString().equals(pre.toString())) {
			preApplied = true;
			oldPre = vf.getPreAssertion();
			// damit die kinder der StatementSeq zusammen geändert werden!
			if (vf.getParent() != null
					&& vf.getParent().getType().equals(StatSeqVF.class))
				vf = vf.getParent();
			vf.replaceAssertion(oldPre, pre);
		}

		// damit, wenn man unter nem statSeq was ändert, der den trotzdem
		// evaluiert
		programControl.evaluateTree(vf);

	}

	public boolean addEdit(javax.swing.undo.UndoableEdit arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean canRedo() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean canUndo() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return true;
	}

	public void redo() throws CannotRedoException {
		if (postApplied)
			vf.replaceAssertion(oldPost, post);
		if (preApplied)
			vf.replaceAssertion(oldPre, pre);
		programControl.evaluateTree(vf);
	}

	public boolean replaceEdit(javax.swing.undo.UndoableEdit arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public void undo() throws CannotUndoException {
		if (postApplied)
			vf.replaceAssertion(post, oldPost);
		if (preApplied)
			vf.replaceAssertion(pre, oldPre);
		programControl.evaluateTree(vf);
	}

}
