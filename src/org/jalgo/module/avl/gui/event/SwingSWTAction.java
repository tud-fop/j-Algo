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

/* Created on 06.06.2005 */
package org.jalgo.module.avl.gui.event;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.event.SwingPropertyChangeSupport;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * The class <code>SwingSWTAction</code> provides an adapter bridge between the
 * both GUI frameworks Swing and SWT. It represents <code>Action</code> objects,
 * which can be used in both frameworks.
 * 
 * @author Alexander Claus
 */
public class SwingSWTAction
extends org.eclipse.jface.action.Action
implements javax.swing.Action {

	private Map<String, Object> values;
	private SwingPropertyChangeSupport changeSupport;

	/**
	 * Creates a new <code>SwingSWTAction</code> object.
	 */
	public SwingSWTAction() {
		values = new HashMap<String, Object>();
	}

	/**
	 * Creates a <code>JButton</code> object without border and text, which can
	 * be used in <code>JToolBar</code>s
	 * 
	 * @return a <code>JButton</code> instance whose <code>Action</code> is the
	 * 			current instance of <code>SwingSWTAction</code>
	 */
	public JButton createToolbarButton() {
		JButton button = new JButton(this);
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setText("");
		return button;
	}

	@Override
	public void setText(String text) {
		super.setText(text);
		putValue(NAME, text);
	}

	@Override
	public void setToolTipText(String text) {
		super.setToolTipText(text);
		putValue(SHORT_DESCRIPTION, text);
	}

	/**
	 * Sets the icon image to this action to the image with the given file name.
	 * 
	 * @param fileName the fileName of the icon image
	 */
	public void setIconImage(String fileName) {
		setImageDescriptor(ImageDescriptor.createFromURL(
			getClass().getResource("/"+fileName)));
		putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/"+fileName)));
	}

	/* (non-Javadoc)
	 * @see javax.swing.Action#getValue(java.lang.String)
	 */
	public Object getValue(String key) {
		return values.get(key);
	}

	/* (non-Javadoc)
	 * @see javax.swing.Action#putValue(java.lang.String, java.lang.Object)
	 */
	public void putValue(String key, Object value) {
		values.put(key, value);
	}

	/* (non-Javadoc)
	 * @see javax.swing.Action#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		if (changeSupport == null)
			changeSupport = new SwingPropertyChangeSupport(this);
		changeSupport.addPropertyChangeListener(listener);
	}

	/* (non-Javadoc)
	 * @see javax.swing.Action#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		if (changeSupport == null) return;
		changeSupport.removePropertyChangeListener(listener);
	}

	/**
	 * Fires a property change event with the given values.
	 * 
	 * @param propertyName the name of the property
	 * @param oldValue the old value of the property
	 * @param newValue the new value of the property
	 */
	@Override
	protected void firePropertyChange(String propertyName, Object oldValue,
			Object newValue) {
		//delegate to swt property change event
		super.firePropertyChange(propertyName, oldValue, newValue);
		//handle swing property change event
		if (changeSupport == null ||
			(oldValue != null && newValue != null && oldValue.equals(newValue)))
			return;
		changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		run();
	}

	@Override
	public void setEnabled(boolean b) {
		if (isEnabled() != b) {
			super.setEnabled(b);
			firePropertyChange("enabled", Boolean.valueOf(!b), Boolean.valueOf(b));
		}
	}

	@Override
	public boolean isEnabled() {
		return super.isEnabled();
	}
}