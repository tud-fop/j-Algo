/*
 * Created on Apr 23, 2004
 */
 
package org.jalgo.main.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Control;

/**
 * @author Cornelius Hald
 * @author Michael Pradel
 */
public class MaximizeAction extends Action {

	private Control c;
	private SashForm sf;

	public MaximizeAction(Control c, SashForm sf) {
		
		this.c = c;
		this.sf = sf;

		setText("Maximize");
		setToolTipText("Maximize.");
		setImageDescriptor(ImageDescriptor.createFromFile(null, "pix/zoom-in.gif"));
	}

	public void run() {

		if (this.c.equals(this.sf.getMaximizedControl())) {
			this.sf.setMaximizedControl(null);
			//this.sf.setOrientation(SWT.HORIZONTAL);
			//this.sf.setOrientation(SWT.VERTICAL);
		}
		else
		{
			this.sf.setMaximizedControl(this.c);
		}

	}

}
