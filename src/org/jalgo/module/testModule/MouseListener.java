/*
 * Created on 27.04.2004
 */
package org.jalgo.module.testModule;

import org.eclipse.swt.events.MouseEvent;

/**
 * @author michi
 */
public class MouseListener implements org.eclipse.swt.events.MouseListener {

	private boolean mouseIsDown;

	public MouseListener() {
		mouseIsDown = false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDoubleClick(MouseEvent arg0) {

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDown(MouseEvent arg0) {
		mouseIsDown = true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseUp(MouseEvent arg0) {
		if (mouseIsDown) {
			
		}
		mouseIsDown = false;
	}

}
