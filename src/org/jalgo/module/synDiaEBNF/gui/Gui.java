/*
 * Created on 01.06.2004
 */
package org.jalgo.module.synDiaEBNF.gui;

import java.io.Serializable;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author Michael Pradel
 * @author Christopher Friedrich
 */
public abstract class Gui implements Serializable {
	
	protected Composite parent;
	
	public Gui(Composite parent) {
		this.parent = parent;
	}
	
	public void dispose() {
		try {
			Control[] children = parent.getChildren();
		
			for (int i=0; i<children.length; i++) {
				children[i].dispose();
			}
		} catch (NullPointerException e) {
			// TODO Handle Exception
		}
	}

}
