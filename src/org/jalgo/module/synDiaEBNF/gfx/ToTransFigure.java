/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
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
 * Created on 15.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.gfx;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.jalgo.main.gfx.BottomLineBorder;
import org.jalgo.main.gfx.FixPointAnchor;

/**
 * Grafical representation of a ToTransFigure
 * 
 * @author Cornelius Hald
 */
public class ToTransFigure extends StandAloneSynDiaFigure {

	private static final long serialVersionUID = -6709738449786825351L;
	private XYLayout layout;
	private RoundedRectangle inner;
	private Label label1, label2;
	int border;

	/**
	 * Use this constructor to create a ToTransFigure
	 * @param label
	 * @param font
	 */
	public ToTransFigure(String text, Font font) {
		super(text, false);

		border = 5;

		// Set the RoundedRectangle fullsize onto the Figure
		setOpaque(false);
		setLayoutManager(new StackLayout());

		inner = new RoundedRectangle();
		layout = new XYLayout();
		inner.setLayoutManager(layout);
		inner.setOpaque(true);
		add(inner, new Rectangle(0, 0, -1, -1));

		label1 = new Label();
		label1.setFont(font);
		label1.setText("trans"); //$NON-NLS-1$
		inner.add(label1);

		label2 = new Label();
		label2.setFont(font);
		label2.setText("(" + text + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		inner.add(label2);

		setTextUnderline(true);

		reposition();
	}

	/**
	 * @see org.eclipse.draw2d.Figure#setBackgroundColor(org.eclipse.swt.graphics.Color)
	 */
	public void setBackgroundColor(Color color) {
		inner.setBackgroundColor(color);
	}

	public void showBorder(boolean bool) {
		if (bool) {
			inner.setForegroundColor(ColorConstants.black);
		} else {
			inner.setForegroundColor(inner.getBackgroundColor());
			label1.setForegroundColor(ColorConstants.black);
			label2.setForegroundColor(ColorConstants.black);
		}
	}

	private void reposition() {
		layout.setConstraint(label1, new Rectangle(border, border * 2, -1, -1));
		layout.setConstraint(
			label2,
			new Rectangle(
				border + label1.getPreferredSize().width + 1,
				border * 2,
				-1,
				-1));

		// Recursive reposition of parents
		if (this.getParent() instanceof CompositeSynDiaFigure) {
			((CompositeSynDiaFigure) this.getParent()).reposition();
		}
	}

	public void setText(String text) {
		label2.setText("(" + text + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		reposition();
	}

	/**
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int arg0, int arg1) {
		int width =
			border
				+ label1.getPreferredSize().width
				+ 1
				+ label2.getPreferredSize().width
				+ border;
		int heigth = 36; //border + label1.getPreferredSize().height + border;

		return new Dimension(width, heigth);
	}

	/**
	 * @see org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure#getLeftAnchor()
	 */
	public FixPointAnchor getLeftAnchor() {
		return new FixPointAnchor(this, SWT.LEFT);
	}

	/**
	 * @see org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure#getRightAnchor()
	 */
	public FixPointAnchor getRightAnchor() {
		return new FixPointAnchor(this, SWT.RIGHT);
	}

	/**
	 * @return text
	 */
	public String getText() {
		return label2.getText();
	}

	/**
	 * @param bool Enable/Disable underline
	 */
	public void setTextUnderline(boolean bool) {
		if (bool) {
			label1.setBorder(new BottomLineBorder());
		} else {
			label1.setBorder(null);
		}
	}

	public void setFont(Font font) {
		label1.setFont(font);
		label2.setFont(font);
	}

	public RoundedRectangle getInner() {
		return inner;
	}

	/**
	 * @see org.jalgo.module.synDiaEBNF.gfx.StandAloneSynDiaFigure#highlight(boolean)
	 */
	public void highlight(boolean highlight) {
		if (highlight) {
			inner.setForegroundColor(SynDiaColors.highlightEntireFigure);
			label1.setForegroundColor(SynDiaColors.normal);
			label2.setForegroundColor(SynDiaColors.normal);
		} else {
			inner.setForegroundColor(SynDiaColors.normal);
			label1.setForegroundColor(SynDiaColors.normal);
			label2.setForegroundColor(SynDiaColors.normal);
		}
	}
}