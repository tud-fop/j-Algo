/*
 * Created on 01.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.gui;

import java.io.Serializable;

import org.eclipse.swt.widgets.Composite;

/**
 * @author Michael Pradel
 * @author Christopher Friedrich
 */
public class NormalViewGui extends Gui implements Serializable {

	private Composite mainComp;

	public NormalViewGui(Composite parent) {
		// TODO: show current SynDia or Ebnf (if not null)
		super(parent);
	}
}
