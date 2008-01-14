package org.jalgo.module.hoare.control.edits;

import java.security.InvalidParameterException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.jalgo.module.hoare.control.ProgramControl;
import org.jalgo.module.hoare.control.UndoableEdit;
import org.jalgo.module.hoare.gui.Node;
import org.jalgo.module.hoare.model.StrongPreVF;
import org.jalgo.module.hoare.model.VerificationFormula;
import org.jalgo.module.hoare.model.WeakPostVF;

public class DeleteNodeApply implements UndoableEdit {

	VerificationFormula vf;

	LinkedList<Deleter> deleterList;

	ProgramControl programControl;

	private interface Deleter {
		boolean apply();

		void undo();
	};

	private class VFDeleter implements Deleter {
		Node node;

		boolean ruleApplied;

		VerificationFormula vf;

		public VFDeleter(Node node, VerificationFormula vf) {
			this.node = node;
			this.vf = vf;
		}

		public boolean apply() {
			if (!node.isVisible())
				return false;

			node.setVisible(false);
			ruleApplied = vf.isRuleApplied();
			vf.setRuleApplied(false);
			return true;
		}

		public void undo() {
			node.setVisible(true);
			vf.setRuleApplied(ruleApplied);
		}
	}

	private class ConsequenceDeleter implements Deleter {
		VerificationFormula vf;

		VerificationFormula parent;

		VerificationFormula deleted;

		public ConsequenceDeleter(VerificationFormula vf) {
			this.parent = vf.getParent();
			this.vf = vf;
		}

		public boolean apply() {
			parent.deleteChild();
			// parent.setRuleApplied(false);
			return true;
		}

		public void undo() {
			parent.insertChild(vf);
			// parent.setRuleApplied(true);
		}
	}

	private Deleter getDeleter(VerificationFormula vf) {
		if ((vf instanceof WeakPostVF) || (vf instanceof StrongPreVF)) {
			return new ConsequenceDeleter(vf);
		}

		return new VFDeleter(
				programControl.getGuiControl().getNode(vf.getID()), vf);
	}

	public DeleteNodeApply(VerificationFormula vf, ProgramControl programControl) {
		this.vf = vf;
		this.programControl = programControl;
		deleterList = new LinkedList<Deleter>();
	}

	public void apply() throws InvalidParameterException {

		for (VerificationFormula child : vf.getChildren())
			delete(child);
		vf.setRuleApplied(false);
		programControl.getGuiControl().resetActiveNode();

	}

	public boolean delete(VerificationFormula vf) {
		for (VerificationFormula child : vf.getChildren())
			delete(child);

		Deleter del = getDeleter(vf);
		if (del.apply())
			deleterList.addFirst(del);

		return true;
	}

	public boolean addEdit(javax.swing.undo.UndoableEdit anEdit) {
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
		for (int i = deleterList.size() - 1; i >= 0; i--) {
			deleterList.get(i).apply();
		}
		vf.setRuleApplied(false);
	}

	public boolean replaceEdit(javax.swing.undo.UndoableEdit anEdit) {
		// TODO Auto-generated method stub
		return false;
	}

	public void undo() throws CannotUndoException {
		for (Iterator<Deleter> iter = deleterList.iterator(); iter.hasNext();) {
			iter.next().undo();
		}
		vf.setRuleApplied(true);

	}

}
