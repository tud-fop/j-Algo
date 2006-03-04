/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and
 * platform independant. j-Algo is developed with the help of Dresden
 * University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * Created on 25.05.2004
 */
package org.jalgo.main.gui.widgets;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author Christopher Friedrich
 */

// TODO: Add a disposeListener to ALL *ViewForms. Resources which gets allocated need to be disposed if the ViewForm gets disposed.

public class CustomViewForm extends ViewForm {

	private CLabel label;

	protected ToolBarManager toolbarMgrCenter;
	protected ToolBarManager toolbarMgrRight;

	private Color titleBG;
	private Color titleFG;
	private Color widgetBG;
	private Color[] activeGradient;
	private int[] activeGradientPercents;

	/**
	 * @param parent
	 * @param style
	 */
	public CustomViewForm(Composite parent, int style) {

		super(parent, style);

		// init some colors
		titleBG =
			parent.getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND);
		titleFG =
			parent.getDisplay().getSystemColor(SWT.COLOR_TITLE_FOREGROUND);
		widgetBG =
			parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
		activeGradient = new Color[] { titleBG, widgetBG, widgetBG };
		activeGradientPercents = new int[] { 80, 100 };

		// Create Label
		label = new CLabel(this, SWT.BORDER) {
			public Point computeSize(int wHint, int hHint, boolean changed) {
				return super.computeSize(wHint, Math.max(24, hHint), changed);
			}
		};
		label.setForeground(titleFG);
		label.setBackground(activeGradient, activeGradientPercents);

		// Add Maximize-on-DoubleClick Support
		MouseAdapter ml = new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {
				Control content = getContent();
				if (content != null && content.getBounds().contains(e.x, e.y))
					return;
				Control parent = getParent();
				if (parent instanceof Splitter)
					((Splitter) parent).setMaximizedControl(
						CustomViewForm.this);
			}
		};
		addMouseListener(ml);
		label.addMouseListener(ml);

		addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
			}
			public void focusLost(FocusEvent e) {
			}
		});

		// Extend TopRight-ToolBarManager
		toolbarMgrCenter = new ToolBarManager(SWT.FLAT);
		toolbarMgrRight = new ToolBarManager(SWT.FLAT);

		setTopLeft(label);
		setTopCenterSeparate(true);
		setTopCenter(toolbarMgrCenter.createControl(this));
		setTopRight(toolbarMgrRight.createControl(this));
	}

	/**
	 * Sets title text.
	 * 
	 * @param text the text to be displayed in the title
	 */
	public void setText(String text) {
		label.setText(text);
	}

	/**
	 * Sets title image.
	 * 
	 * @param img the image to be displayed in the title
	 */
	public void setImage(Image img) {
		label.setImage(img);
	}

	public ToolBarManager getToolBarManager() {
		return toolbarMgrCenter;
	}
}
