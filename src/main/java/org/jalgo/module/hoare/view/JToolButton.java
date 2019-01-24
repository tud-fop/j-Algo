package org.jalgo.module.hoare.view;

import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
 * @author Markus   (year 2007)
 * 
 * copied by tomas
 */
public class JToolButton extends JButton {

	private static final long serialVersionUID = 1764614541666747667L;

	private static Border oneLineRaisedBevelBorder = BorderFactory.createBevelBorder(
			BevelBorder.RAISED, Color.white, SystemColor.control, Color.gray,
			SystemColor.control);

	private static Border oneLineLoweredBevelBorder = BorderFactory.createBevelBorder(
			BevelBorder.LOWERED, Color.white, SystemColor.control, Color.gray,
			SystemColor.control);

	private static Border emptyBorder = new EmptyBorder(2, 2, 2, 2);

	private RolloverHandler rolloverHandler;
	
	/**
	 * Creates a new instance of JToolButton
	 * @param image icon that is shown on the button
	 * @param gui controller for the view
	 * @param tooltipText text for the tooltip
	 * @param actionCommand action that is performed when the button is clicked
	 */
	public JToolButton(ImageIcon image, final View gui,final String tooltipText, final AbstractAction actionCommand) {
	
		actionCommand.putValue(Action.SMALL_ICON, image);
		setBorder(emptyBorder);
		setAction(actionCommand);
		setToolTipText(tooltipText);
		addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent arg0) {
				gui.setWelcomeMessage(tooltipText);
			}
		});
		rolloverHandler = new RolloverHandler();
		if (isEnabled())
			addMouseListener(rolloverHandler);
		addPropertyChangeListener("enabled", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if ((Boolean) evt.getNewValue())
					addMouseListener(rolloverHandler);
				else {
					removeMouseListener(rolloverHandler);
					setBorder(emptyBorder);
				}
			}
		});
	}

	private class RolloverHandler extends MouseAdapter {
		
		private boolean mouseOver = false;
		private boolean mouseDown = false;

		public void mouseExited(MouseEvent e) {
			mouseOver = false;
			setBorder(emptyBorder);
		}

		public void mouseEntered(MouseEvent e) {
			mouseOver = true;
			if (mouseDown)
				setBorder(oneLineLoweredBevelBorder);
			else
				setBorder(oneLineRaisedBevelBorder);
		}

		public void mousePressed(MouseEvent e) {
			mouseDown = true;
			setBorder(oneLineLoweredBevelBorder);
		}

		public void mouseReleased(MouseEvent e) {
			mouseDown = false;

			if (mouseOver && isEnabled())
				setBorder(oneLineRaisedBevelBorder);
			else
				setBorder(emptyBorder);
		}
	}
}