package org.jalgo.module.hoare.control;

import java.security.InvalidParameterException;

public interface UndoableEdit extends javax.swing.undo.UndoableEdit {
	void apply() throws InvalidParameterException;
}
