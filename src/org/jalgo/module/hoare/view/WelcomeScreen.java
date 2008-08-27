package org.jalgo.module.hoare.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jalgo.main.util.Messages;
import org.jalgo.main.util.Settings;

/**
 * The Welcome Screen
 * 
 * @author Tomas
 *	most things used from older Hoare version
 *
 *the welcomescreen is it's own mouseListener for the mouseover events
 */
public class WelcomeScreen extends JPanel implements MouseListener{
	
	private static final long serialVersionUID = 1L;
	public final static Color WELCOME_BACKGROUND = new Color(192, 32, 00);
	private JLabel 	descriptionLabel;
	private WelcomeScreenButton loadC0, newC0, loadTree; 
	
	/**
	 * Costructor opens the JPanel
	 * @param View gui
	 */
	
	public WelcomeScreen(final View gui) {
	
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		String lang = Settings.getString("main", "Language");

		if (!lang.equals("de"))
			lang = "en";
		ActionListener l = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gui.init();
				if (e.getActionCommand().equals("openC0")) { 
					gui.loadSourceCode();			
				}
				else if (e.getActionCommand().equals("newC0")) {}
				else if (e.getActionCommand().equals("openTree")) {
					gui.openTree();
				}

			}
		};
		loadC0 = new WelcomeScreenButton(						new ImageIcon(Messages.getResourceURL("hoare", "icon.openC0Welcome0")), 
																new ImageIcon(Messages.getResourceURL("hoare", "icon.openC0Welcome1")),
																new ImageIcon(Messages.getResourceURL("hoare",	"icon.openC0WelcomeDesc_" + lang)),
																"openC0", l, this);

		newC0 = new WelcomeScreenButton(						new ImageIcon(Messages.getResourceURL("hoare", "icon.newWelcome0")), 
																new ImageIcon(Messages.getResourceURL("hoare", "icon.newWelcome1")),
																new ImageIcon(Messages.getResourceURL("hoare",	"icon.newWelcomeDesc_" + lang)),
																"newC0", l, this);

		loadTree = new WelcomeScreenButton(						new ImageIcon(Messages.getResourceURL("hoare", "icon.openWelcome0")),
																new ImageIcon(Messages.getResourceURL("hoare", "icon.openWelcome1")),
																new ImageIcon(Messages.getResourceURL("hoare", "icon.openWelcomeDesc_" + lang)),
																"openTree", l, this);

		descriptionLabel = new JLabel();
		descriptionLabel.setText("    ");
		descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(WELCOME_BACKGROUND);
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.add(loadC0);
		buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));
		buttonPane.add(newC0);
		buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));
		buttonPane.add(loadTree);
		buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));

		add(Box.createVerticalStrut(150));
		add(buttonPane);
		add(Box.createVerticalStrut(50));
		add(descriptionLabel);
	}

/**
 * sets the description label
 * @param Icon desc
 */
	public void setDescription(Icon desc) {
		descriptionLabel.setIcon(desc);
		descriptionLabel.updateUI();
	}

/*   // aus altem Hoare, bis jetzt noch nicht gebraucht!
 
	public void setButtonsEnabled(boolean b) {
		loadC0.setEnabled(b);
		newC0.setEnabled(b);
		if (!b)
			setDescription(null);
	}
*/
	/**
	 * paint function to set background
	 */
	protected void paintComponent(Graphics g) {
		g.setColor(WELCOME_BACKGROUND);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	/**
	 * empty, not needed
	 */
	public void mouseClicked(MouseEvent e) {}
	
	/**
	 * Causes the event source button to be displayed as seleced and to show the
	 * description string from the screen.
	 */
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() instanceof WelcomeScreenButton) {
			WelcomeScreenButton source = (WelcomeScreenButton) e.getSource();
			if (!source.isEnabled())
				return;
			source.setSelected(true);
			setDescription(source.getDescription());
		}
	}

	/**
	 * Causes the event source button to be displayed normally and to remove the
	 * description string from the screen.
	 */
	public void mouseExited(MouseEvent e) {
		if (e.getSource() instanceof WelcomeScreenButton) {
			((WelcomeScreenButton) e.getSource()).setSelected(false);
			setDescription(null);
		}
	}
	
	
	/**
	 * empty, not needed
	 */
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
		/**
		 * empty, not needed
		 */
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
