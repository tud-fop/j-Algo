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

package org.jalgo.main.gui;

import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.List;

import org.eclipse.draw2d.AncestorListener;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.EventDispatcher;
//import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.FocusEvent;
import org.eclipse.draw2d.FocusListener;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.KeyEvent;
import org.eclipse.draw2d.KeyListener;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.UpdateManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;

public class ScrollZoomCanvas extends FigureCanvas {

	public ScrollZoomCanvas(Composite parent) {
		super(parent);
		initialize();
	}

	private ZoomPanel zoomPane;

	// IFigure-Methoden f?r ZoomPane

	public void add(IFigure figure) {
		this.zoomPane.add(figure);
		this.zoomPane.repaint();
	}

	public void addMouseListener(MouseListener listener) {
		this.zoomPane.addMouseListener(listener);
	}

	public void addAncestorListener(AncestorListener listener) {
		this.zoomPane.addAncestorListener(listener);
	}

	public void addFigureListener(FigureListener listener) {
		this.zoomPane.addFigureListener(listener);
	}

	public void addFocusListener(FocusListener listener) {
		this.zoomPane.addFocusListener(listener);
	}

	public void addKeyListener(KeyListener listener) {
		this.zoomPane.addKeyListener(listener);
	}

	public void addMouseMotionListener(MouseMotionListener listener) {
		this.zoomPane.addMouseMotionListener(listener);
	}

	public void addNotify() {
		this.zoomPane.addNotify();
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.zoomPane.addPropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(
		String property,
		PropertyChangeListener listener) {
		this.zoomPane.addPropertyChangeListener(property, listener);
	}

	public boolean containsPoint(int x, int y) {
		return this.zoomPane.containsPoint(x, y);
	}

	public void erase() {
		this.zoomPane.erase();
	}

	public IFigure findFigureAt(int x, int y) {

		//return this.figure.findFigureAt(x, y);
		return this.zoomPane.findFigureAt(x, y);
	}

	public IFigure findFigureAt(int x, int y, TreeSearch search) {
		return this.zoomPane.findFigureAt(x, y, search);
	}

	public IFigure findFigureAtExcluding(int x, int y, Collection collection) {
		return this.zoomPane.findFigureAtExcluding(x, y, collection);
	}

	public IFigure findMouseEventTargetAt(int x, int y) {
		return this.zoomPane.findMouseEventTargetAt(x, y);
	}

	public Color getZBackgroundColor() {
		return this.zoomPane.getBackgroundColor();
	}

	public Border getZBorder() {
		return this.zoomPane.getBorder();
	}

	public List getZPanelChildren() {
		return this.zoomPane.getChildren();
	}

	public Cursor getZCursor() {
		return this.zoomPane.getCursor();
	}

	public Color getZForegroundColor() {
		return this.zoomPane.getForegroundColor();
	}

	public Insets getZInsets() {
		return this.zoomPane.getInsets();
	}

	public LayoutManager getLayoutManager() {
		return this.zoomPane.getLayoutManager();
	}

	public Color getZLocalBackgroundColor() {
		return this.zoomPane.getLocalBackgroundColor();
	}

	public Color getZLocalForegroundColor() {
		return this.zoomPane.getLocalForegroundColor();
	}

	public Dimension getZMaximumSize() {
		return this.zoomPane.getMaximumSize();
	}

	public Dimension getZMinimumSize() {
		return this.zoomPane.getMinimumSize();
	}

	public Dimension getZMinimumSize(int wHint, int hHint) {
		return this.zoomPane.getMinimumSize(wHint, hHint);
	}

	public Dimension getZPreferredSize() {
		return this.zoomPane.getPreferredSize();
	}

	public Dimension getZPreferredSize(int wHint, int hHint) {
		return this.zoomPane.getPreferredSize(wHint, hHint);
	}

	public IFigure getToolTip() {
		return this.zoomPane.getToolTip();
	}

	public UpdateManager getUpdateManager() {
		return this.zoomPane.getUpdateManager();
	}

	public void handleFocusGained(FocusEvent event) {
		this.zoomPane.handleFocusGained(event);
	}

	public void handleFocusLost(FocusEvent event) {
		this.zoomPane.handleFocusLost(event);
	}

	public void handleKeyPressed(KeyEvent event) {
		this.zoomPane.handleKeyPressed(event);
	}

	public void handleKeyReleased(KeyEvent event) {
		this.zoomPane.handleKeyReleased(event);
	}

	public void handleMouseDoubleClicked(MouseEvent event) {
		this.zoomPane.handleMouseDoubleClicked(event);
	}

	public void handleMouseDragged(MouseEvent event) {
		this.zoomPane.handleMouseDragged(event);
	}

	public void handleMouseEntered(MouseEvent event) {
		this.zoomPane.handleMouseEntered(event);
	}

	public void handleMouseExited(MouseEvent event) {
		this.zoomPane.handleMouseExited(event);
	}

	public void handleMouseHover(MouseEvent event) {
		this.zoomPane.handleMouseHover(event);
	}

	public void handleMouseMoved(MouseEvent event) {
		this.zoomPane.handleMouseMoved(event);
	}

	public void handleMousePressed(MouseEvent event) {
		this.zoomPane.handleMousePressed(event);
	}

	public void handleMouseReleased(MouseEvent event) {
		this.zoomPane.handleMouseReleased(event);
	}

	public EventDispatcher internalGetEventDispatcher() {
		return this.zoomPane.internalGetEventDispatcher();
	}

	public void invalidate() {
		this.zoomPane.invalidate();
	}

	public void invalidateTree() {
		this.zoomPane.invalidateTree();
	}

	public boolean isFocusTraversable() {
		return this.zoomPane.isFocusTraversable();
	}

	public boolean isOpaque() {
		return this.zoomPane.isOpaque();
	}

	public boolean isRequestFocusEnabled() {
		return this.zoomPane.isRequestFocusEnabled();
	}

	public boolean isVisible() {
		return this.zoomPane.isVisible();
	}

	public void paint(Graphics graphics) {
		this.zoomPane.paint(graphics);
	}

	public void remove(IFigure figure) {
		this.zoomPane.remove(figure);
	}

	public void removeAncestorListener(AncestorListener listener) {
		this.zoomPane.addAncestorListener(listener);
	}

	public void removeFigureListener(FigureListener listener) {
		this.zoomPane.removeFigureListener(listener);
	}

	public void removeFocusListener(FocusListener listener) {
		this.zoomPane.removeFocusListener(listener);
	}

	public void removeKeyListener(KeyListener listener) {
		this.zoomPane.removeKeyListener(listener);
	}

	public void removeMouseListener(MouseListener listener) {
		this.zoomPane.removeMouseListener(listener);
	}

	public void removeMouseMotionListener(MouseMotionListener listener) {
		this.zoomPane.removeMouseMotionListener(listener);
	}

	public void removeNotify() {
		this.zoomPane.removeNotify();
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.zoomPane.removePropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(
		String property,
		PropertyChangeListener listener) {
		this.zoomPane.removePropertyChangeListener(property, listener);
	}

	public void repaint() {
		this.zoomPane.repaint();
	}

	public void repaint(int x, int y, int w, int h) {
		this.zoomPane.repaint(x, y, w, h);
	}

	public void revalidate() {
		this.zoomPane.revalidate();
	}

	public void setBackgroundColor(Color c) {
		this.zoomPane.setBackgroundColor(c);
	}

	public void setConstraint(IFigure child, Object constraint) {
		this.zoomPane.setConstraint(child, constraint);
	}

	public void setCursor(Cursor cursor) {
		this.zoomPane.setCursor(cursor);
	}

	public void setEnabled(boolean value) {
		this.zoomPane.setEnabled(value);
	}

	public void setFocusTraversable(boolean value) {
		this.zoomPane.setFocusTraversable(value);
	}

	public void setFont(Font f) {
		this.zoomPane.setFont(f);
	}

	public void setForegroundColor(Color c) {
		this.zoomPane.setForegroundColor(c);
	}

	public void setLayoutManager(LayoutManager lm) {
		zoomPane.setLayoutManager(lm);
	}

	public void setLocation(Point p) {
		this.zoomPane.setLocation(p);
	}

	public void setZMaximumSize(Dimension size) {
		this.zoomPane.setMaximumSize(size);
//		this.figure.setMaximumSize(size);
	}

	public void setZMinimumSize(Dimension size) {
		this.zoomPane.setMinimumSize(size);
//		this.figure.setMinimumSize(size);
	}

	public void setOpaque(boolean isOpaque) {
		this.zoomPane.setOpaque(isOpaque);
	}

	public void setZPreferredSize(Dimension size) {
		this.zoomPane.setPreferredSize(size);
	}

	public void setRequestFocusEnabled(boolean requestFocusEnabled) {
		this.zoomPane.setRequestFocusEnabled(requestFocusEnabled);
	}

	public void setToolTip(IFigure figure) {
		this.zoomPane.setToolTip(figure);
	}

	public void translate(int x, int y) {
		this.zoomPane.translate(x, y);
	}

	public void translateFromParent(Translatable t) {
		this.zoomPane.translateFromParent(t);
	}

	public void translateToAbsolute(Translatable t) {
		this.zoomPane.translateToAbsolute(t);
	}

	public void translateToParent(Translatable t) {
		this.zoomPane.translateToParent(t);
	}

	public void translateToRelative(Translatable t) {
		this.zoomPane.translateToRelative(t);
	}

	public void validate() {
		this.zoomPane.validate();
	}

	public void setSize(int x, int y) {
		super.setSize(x, y);
		if ((zoomPane.getSize().height < y)
			|| (zoomPane.getSize().width < x)) {
			zoomPane.setSize(x, y);
		}
	}

	public void setBounds(int x, int y, int w, int h) {
		super.setBounds(x, y, w, h);
		if ((zoomPane.getBounds().height < h)
			|| (zoomPane.getBounds().width < w)) {
			zoomPane.setSize(x, y);
		}
	}

	private void initialize() {
		this.zoomPane = new ZoomPanel();
		this.setContents(zoomPane);
		this.setZoom(1.0f);
	}

	public float getZoom() {
		return zoomPane.getZoom();
	}

	public void setZoom(float zoom) {
		zoomPane.setZoom(zoom);
	}

	public void setZoom(float zoom, Point point) {
		zoomPane.setZoom(zoom);
		this.scrollSmoothTo(
			point.x - this.getLocation().x,
			point.y - this.getLocation().y);
	}

	public void resetZoom() {
		zoomPane.setZoom(1.0f);
		zoomPane.repaint();

	}

	protected boolean useLocalCoordinates(){
		return zoomPane.useLocalCoordinates();
	}
}