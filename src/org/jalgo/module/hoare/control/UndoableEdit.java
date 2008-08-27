package org.jalgo.module.hoare.control;

/**
 * the interface used by UndoManager and Controller for all the edits,
 * that needs to be undoable
 *
 * @author Johannes
 */
public interface UndoableEdit extends javax.swing.undo.UndoableEdit {
	void apply();
	}