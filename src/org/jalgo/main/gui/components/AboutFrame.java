package org.jalgo.main.gui.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.jalgo.main.JAlgoMain;
import org.jalgo.main.util.Messages;

/**
 * The class <code>AboutFrame</code> represents an "About" - frame for a short
 * information about j-Algo and its modules. It contains also a logo image.<br>
 * This class has two class methods, which open the requested frame. Internally
 * a singleton instance of <code>AboutFrame</code> is managed for better
 * performance.
 * 
 * @author Alexander Claus
 */
public class AboutFrame
extends JDialog {

	private static final long serialVersionUID = -4496899775387570794L;
	private static AboutFrame instance;
	private JLabel contentLabel;

	private AboutFrame(JFrame parent) {
		super(parent, "", true);
		setAlwaysOnTop(true);

		setLayout(new BorderLayout());

		JLabel imageLabel = new JLabel(new ImageIcon(
			Messages.getResourceURL("main", "About.header"))); //$NON-NLS-1$ //$NON-NLS-2$
		add("North", imageLabel);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		JPanel southPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		southPane.add(okButton);
		add("South", southPane);

		contentLabel = new JLabel("", SwingConstants.CENTER);
		// the following is for optimizing layout under linux, otherwise
		// some of the text does not fit into dialog
		contentLabel.setFont(new Font(
			contentLabel.getFont().getName(),
			contentLabel.getFont().getStyle(),
			11));
		contentLabel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createCompoundBorder(
				new EmptyBorder(5, 5, 5, 5),
				new EtchedBorder()),
			new EmptyBorder(5, 5, 5, 5)));
		add("Center", contentLabel);
	}

	/**
	 * Opens an "About" - frame for the currently opened module instance.
	 * 
	 * @param parent the <code>JAlgoWindow</code> instance
	 */
	public static void openAboutModuleFrame(JFrame parent) {
		openAboutFrame(parent, Messages.getString("main", "ui.About_module") + //$NON-NLS-1$ //$NON-NLS-2$
			" \"" + //$NON-NLS-1$
			JAlgoMain.getInstance().getCurrentInstance().getModuleInfo().getName() +
			"\"", Messages.getModuleInfoAsHTML(-1));
	}

	/**
	 * Opens an "About" - frame for general informations about j-Algo.
	 * 
	 * @param parent the <code>JAlgoWindow</code> instance
	 */
	public static void openAboutJAlgoFrame(JFrame parent) {
		openAboutFrame(parent, Messages.getString("main", "ui.About"), //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getJAlgoInfoAsHTML());
	}
	
	private synchronized static void openAboutFrame(JFrame parent,
			String title, String contentLabelText) {
		if (instance == null) instance = new AboutFrame(parent);
		instance.setSize(558, 320);
		instance.setLocationRelativeTo(parent);
		instance.setTitle(title);
		instance.contentLabel.setText(contentLabelText);
		instance.setVisible(true);
	}
}