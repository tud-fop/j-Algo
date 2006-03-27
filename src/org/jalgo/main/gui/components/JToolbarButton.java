package org.jalgo.main.gui.components;

import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * This class defines a <code>JButton</code>, which is specialized for
 * <code>JToolbar</code>s. This means, that the standard <code>JButton</code>
 * has a border, which is not appropriate for creating nice toolbars ;o)
 * Furthermore the native look&feel doesn't support the rollover mechanism.
 * 
 * @author Alexander Claus
 */
public class JToolbarButton
extends JButton {

	private static final long serialVersionUID = 1764614541666747667L;
	static Border oneLineRaisedBevelBorder =
		BorderFactory.createBevelBorder(
			BevelBorder.RAISED, Color.white, SystemColor.control,
			Color.gray, SystemColor.control);
	static Border oneLineLoweredBevelBorder =
		BorderFactory.createBevelBorder(
			BevelBorder.LOWERED, Color.white, SystemColor.control,
			Color.gray, SystemColor.control);
	static Border emptyBorder = new EmptyBorder(2, 2, 2, 2);

	RolloverHandler rolloverHandler;

	public JToolbarButton(Icon icon, String tooltipText, String actionCommand) {
		super(icon);
		setBorder(emptyBorder);
		setActionCommand(actionCommand);
		setToolTipText(tooltipText);
		//implementation of the standard rollover behaviour
		rolloverHandler = new RolloverHandler();
		if (isEnabled()) addMouseListener(rolloverHandler);
		addPropertyChangeListener("enabled", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if ((Boolean)evt.getNewValue()) addMouseListener(rolloverHandler);
				else {
					removeMouseListener(rolloverHandler);
					setBorder(emptyBorder);
				}
			}
		});
	}

	private class RolloverHandler
	extends MouseAdapter {
		boolean mouseOver = false;
		boolean mouseDown = false;
		public void mouseExited(MouseEvent e) {
			mouseOver = false;
			setBorder(emptyBorder);				
		}
		public void mouseEntered(MouseEvent e) {
			mouseOver = true;
			if (mouseDown) setBorder(oneLineLoweredBevelBorder);
			else setBorder(oneLineRaisedBevelBorder);
		}
		public void mousePressed(MouseEvent e) {
			mouseDown = true;
			setBorder(oneLineLoweredBevelBorder);
		}
		public void mouseReleased(MouseEvent e) {
			mouseDown = false;
			//check for isEnabled here, because, sometimes a click on a button
			//disables the button
			if (mouseOver && isEnabled()) setBorder(oneLineRaisedBevelBorder);
			else setBorder(emptyBorder);
		}		
	}
}