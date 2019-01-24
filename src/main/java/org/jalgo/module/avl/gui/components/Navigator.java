/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

/* Created on 29.05.2005 */
package org.jalgo.module.avl.gui.components;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.SystemColor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.border.EtchedBorder;

import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.module.avl.gui.graphics.PaintArea;

/**
 * The class <code>Navigator</code> is a special feature for scrollpanes. It
 * can be added as e.g. lower right corner of the scrollpane and it gets a
 * reference to the viewport of the scrollpane. When clicking in the corner a
 * popup opens with a scaled preview of the viewport content. Dragging the mouse
 * the user can navigate through the scrollpane.
 * 
 * @author Alexander Claus
 */
public class Navigator
extends JComponent
implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 2584571837248927562L;

	// general references
	private PaintArea paintArea;

	// popup components
	private Popup popup;
	private JInternalFrame navigator;
	private JComponent map;
	private Point lowerRightCornerPoint;

	// variables for displaying
	private static final int DIAGONAL = 200;
	private double scale;
	private Rectangle visibleRect;
	private Image scaledImage;
	private Point vrCorner;
	private boolean mouseOver;

	// the "mouse mover"
	private Robot robot;

	/**
	 * Constructs a <code>Navigator</code> object with the given references.
	 * 
	 * @param paintArea the viewport of the scrollpane
	 */
	public Navigator(PaintArea paintArea) {
		this.paintArea = paintArea;

		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		addMouseListener(this);
		navigator = new JInternalFrame(Messages.getString(
			"avl", "Navigator")); //$NON-NLS-1$ //$NON-NLS-2$
		// the map is responsible for displaying the preview
		map = new JComponent() {
			private static final long serialVersionUID = 4023482342502748131L;

			@SuppressWarnings("synthetic-access")
			public void paint(Graphics g) {
				// fill background
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, getWidth(), getHeight());
				// draw preview, offset of 3px because of compensation of
				// localisation conflicts caused by the SWT <-> Swing
				// "cooperation"
				g.drawImage(scaledImage, 3, 3, this);
				// draw visible rect, offset like preview
				updateVisibleRect();
				g.setColor(Color.RED);
				g.drawRect(visibleRect.x + 3, visibleRect.y + 3,
					visibleRect.width, visibleRect.height);
				// paint border
				super.paintBorder(g);
			}
		};
		map.setBorder(new EtchedBorder());
		navigator.add(map);
		navigator.setCursor(Cursor.getPredefinedCursor(
			Cursor.CROSSHAIR_CURSOR));
		// robot is responsible for automatically mouse moving
		try {
			robot = new Robot();
		}
		catch (AWTException ex) {
			JAlgoGUIConnector.getInstance().showErrorMessage(Messages.getString(
				"avl", "No_robot_error")); //$NON-NLS-1$ //$NON-NLS-2$
			ex.printStackTrace();
			robot = null;
		}
	}

	/**
	 * Calculates the scaled version of the visible rect new from getting the
	 * visible rect of the viewport.
	 */
	private void updateVisibleRect() {
		visibleRect = paintArea.getVisibleRect();
		visibleRect.setRect(visibleRect.x * scale, visibleRect.y * scale,
			visibleRect.width * scale, visibleRect.height * scale);
	}

	/**
	 * When left mouse button is pressed, the popup is created and shown. The
	 * mouse cursor is moved to the center of the visible rect.
	 */
	public void mousePressed(MouseEvent e) {
		// ensure that only the left mouse button is pressed once
		if (e.getButton() != MouseEvent.BUTTON1 || e.getClickCount() > 1) return;
		// force popup relicts to disappear
		if (popup != null) popup.hide();

		// calculate size of navigator frame
		scale = DIAGONAL / Math.sqrt(
			paintArea.getWidth() * paintArea.getWidth() +
			paintArea.getHeight() * paintArea.getHeight());
		// border of 9px because of compensation of localisation conflicts
		// caused by the SWT <-> Swing "cooperation"
		map.setPreferredSize(new Dimension(
			(int)(paintArea.getWidth() * scale) + 9,
			(int)(paintArea.getHeight() * scale) + 9));
		// create scaled image of paint area
		scaledImage = paintArea.getScaledImage(scale);
		navigator.pack();
		navigator.setVisible(true);

		// create popup, where lower right corner is the
		// lower right corner of the scrollpane
		lowerRightCornerPoint = getLocationOnScreen();
		lowerRightCornerPoint.translate(getWidth(), getHeight());
		popup = PopupFactory.getSharedInstance().getPopup(this, navigator,
			lowerRightCornerPoint.x - navigator.getWidth(),
			lowerRightCornerPoint.y - navigator.getHeight());
		popup.show();
		// TODO: request focus on navigator does not work...
		// move cursor to center of the visible rect
		updateVisibleRect();
		vrCorner = navigator.getAccessibleContext().getAccessibleChild(0).
			getAccessibleContext().getAccessibleComponent().getBounds().
			getLocation();
		if (robot != null) robot.mouseMove(
			lowerRightCornerPoint.x - navigator.getWidth() + vrCorner.x +
			(int)visibleRect.getCenterX(),
			lowerRightCornerPoint.y - navigator.getHeight() + vrCorner.y +
			(int)visibleRect.getCenterY());
		addMouseMotionListener(this);
	}

	/**
	 * Releasing mouse causes to hide the navigator popup and to move the mouse
	 * cursor back to center of the corner component.
	 */
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() != MouseEvent.BUTTON1 || e.getClickCount() > 1) return;
		popup.hide();
		// remove the listener to avoid drag events during initialization
		// and hiding the popup
		removeMouseMotionListener(this);
		if (robot != null) robot.mouseMove(
			lowerRightCornerPoint.x - getWidth() / 2,
			lowerRightCornerPoint.y - getHeight() / 2);
	}

	/**
	 * Highlights the corner component as selected.
	 */
	public void mouseEntered(MouseEvent e) {
		mouseOver = true;
		repaint();
	}

	/**
	 * Sets the selected state back to normal.
	 */
	public void mouseExited(MouseEvent e) {
		mouseOver = false;
		repaint();
	}

	/**
	 * Paints the corner content.
	 */
	public void paint(Graphics g) {
		g.setColor(SystemColor.control);
		g.fill3DRect(0, 0, getWidth(), getHeight(), !mouseOver);
		g.setColor(Color.WHITE);
		g.fill3DRect(4, 4, getWidth() - 8, getWidth() - 8, mouseOver);
		g.fillRect(5, 5, getWidth() - 10, getWidth() - 10);
	}

	/**
	 * Here the real navigation code. The viewport is scrolled to the rectangle
	 * centered at the event coordinates.
	 */
	public void mouseDragged(MouseEvent e) {
		// calculate the upper left corner of the new visible rect
		Point p = e.getPoint();
		p.translate(navigator.getWidth() - getWidth() - vrCorner.x,
			navigator.getHeight() - getHeight() - vrCorner.y);
		visibleRect.setLocation(p.x - visibleRect.width / 2,
			p.y - visibleRect.height / 2);
		paintArea.scrollRectToVisible(new Rectangle(
			(int)(visibleRect.x / scale),
			(int)(visibleRect.y / scale),
			paintArea.getVisibleRect().width,
			paintArea.getVisibleRect().height));
		map.repaint();
	}

	/**
	 * No action is performed here.
	 */
	public void mouseMoved(MouseEvent e) {
		// this method has no effect
	}

	/**
	 * No action is performed here.
	 */
	public void mouseClicked(MouseEvent e) {
	// this method has no effect
	}
}