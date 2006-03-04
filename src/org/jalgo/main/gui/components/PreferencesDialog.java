package org.jalgo.main.gui.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jalgo.main.gui.JAlgoWindow;
import org.jalgo.main.util.Messages;
import org.jalgo.main.util.Settings;

/**
 * @author Alexander Claus
 */
public class PreferencesDialog
extends JDialog {

	private static PreferencesDialog instance;
	private JCheckBox showSplash;
	private JCheckBox showModuleChooser;
	private JComboBox language;
	private JPanel previewPane;
	private JList lafList;
	private JComponent[] prevFrames;
	private static LookAndFeelInfo[] installedLAFs;

	private PreferencesDialog(JFrame parent) {
		super(parent, Messages.getString("main", "ui.Prefs"), true);
		setAlwaysOnTop(true);

		setLayout(new BorderLayout());
		JPanel centerPane = new JPanel();
		centerPane.setLayout(new BoxLayout(centerPane, BoxLayout.PAGE_AXIS));

		showSplash = new JCheckBox(
			Messages.getString("main", "Prefs.ShowSplashOnStartup"));
		showSplash.setAlignmentX(Component.LEFT_ALIGNMENT);
		centerPane.add(showSplash);

		showModuleChooser = new JCheckBox(
			Messages.getString("main", "Prefs.ShowModuleChooserOnStartup"));
		showModuleChooser.setAlignmentX(Component.LEFT_ALIGNMENT);
		centerPane.add(showModuleChooser);
		add(centerPane, BorderLayout.CENTER);

		JPanel langPane = new JPanel();
		langPane.setLayout(new BoxLayout(langPane, BoxLayout.LINE_AXIS));
		language = new JComboBox(getAvailableLanguages());
		langPane.add(language);
		langPane.add(Box.createHorizontalStrut(5));
		JLabel langLabel = new JLabel(
			Messages.getString("main", "Prefs.Language"));
		langPane.add(langLabel);
		langPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		centerPane.add(langPane);
		centerPane.add(Box.createVerticalStrut(5));

		JLabel lafLabel = new JLabel(Messages.getString("main", "Prefs.Skin"));
		lafLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		centerPane.add(lafLabel);
		centerPane.add(Box.createVerticalStrut(5));
		JPanel lafPane = new JPanel();
		lafPane.setLayout(new BoxLayout(lafPane, BoxLayout.LINE_AXIS));
		lafList = new JList(getAvailableLookAndFeelNames());
		lafList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lafList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("synthetic-access")
			public void valueChanged(ListSelectionEvent e) {
				previewPane.removeAll();
				previewPane.add(prevFrames[lafList.getSelectedIndex()]);
				previewPane.updateUI();
			}
		});
		JScrollPane lafScrollPane = new JScrollPane(lafList,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		lafPane.add(lafScrollPane);

		previewPane = new JPanel();
		previewPane.setPreferredSize(new Dimension(120, 120));
		lafPane.add(previewPane);
		lafPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		centerPane.add(lafPane);
		prevFrames = buildPreviews();

		JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton okButton = new JButton(
			Messages.getString("main", "DialogConstants.Ok"));
		okButton.addActionListener(new ActionListener() {
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(ActionEvent e) {
				Settings.setBoolean("ShowSplashOnStartup",
					showSplash.isSelected());
				Settings.setBoolean("ShowModuleChooserOnStartup",
					showModuleChooser.isSelected());
				Settings.setString("Language", Messages.getString("main_res",
					"Available_languages.short").split(",")
					[language.getSelectedIndex()]);
				dispose();
				Settings.setString("LookAndFeel",
					installedLAFs[lafList.getSelectedIndex()].getClassName());
			}
		});
		JButton cancelButton = new JButton(
			Messages.getString("main", "DialogConstants.Cancel"));
		cancelButton.addActionListener(new ActionListener() {
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPane.add(okButton);
		buttonPane.add(cancelButton);
		add(buttonPane, BorderLayout.SOUTH);

		pack();
		setLocationRelativeTo(parent);
	}

	public static void open(JFrame parent) {
		if (instance == null) instance = new PreferencesDialog(parent);
		// update settings
		instance.showSplash.setSelected(
			Settings.getBoolean("ShowSplashOnStartup"));
		instance.showModuleChooser.setSelected(
			Settings.getBoolean("ShowModuleChooserOnStartup"));
		String[] shortLangNames = Messages.getString("main_res",
			"Available_languages.short").split(",");
		String selectedLanguage = Settings.getString("Language");
		for (int i=0; i<shortLangNames.length; i++) {
			if (selectedLanguage.equals(shortLangNames[i])) {
				instance.language.setSelectedIndex(i);
				break;
			}
		}
		String selectedLAF = Settings.getString("LookAndFeel");
		for (int i=0; i<installedLAFs.length; i++) {
			if (selectedLAF.equals(installedLAFs[i].getClassName())) {
				instance.lafList.setSelectedIndex(i);
				instance.previewPane.removeAll();
				instance.previewPane.add(instance.prevFrames[i]);
				break;
			}
		}
		instance.setVisible(true);
	}

	private JComponent[] buildPreviews() {
		JComponent[] previews = new JComponent[installedLAFs.length];
		for (int i=0; i<installedLAFs.length; i++) {
			JAlgoWindow.setLookAndFeel(installedLAFs[i].getClassName());
			JFrame testFrame = new JFrame();
			testFrame.setLocation(-300, -300);
			testFrame.setSize(150, 144);
			JInternalFrame prevFrame = new JInternalFrame("Title");
			prevFrame.setLayout(new BoxLayout(prevFrame.getContentPane(),
				BoxLayout.PAGE_AXIS));
			JMenuBar prevMB = new JMenuBar();
			JMenu prevMenu = new JMenu("Menu");
			prevMB.add(prevMenu);
			prevFrame.setJMenuBar(prevMB);
			prevFrame.add(new JLabel("A Label"));
			prevFrame.add(Box.createVerticalStrut(5));
			prevFrame.add(new JButton("A Button"));
			prevFrame.setSize(150, 120);
			prevFrame.setLocation(0, 0);
			prevFrame.setVisible(true);

			testFrame.add(prevFrame);
			testFrame.setVisible(true);
			final Image image = new BufferedImage(150, 150,
				BufferedImage.TYPE_INT_RGB);
			Graphics g = image.getGraphics();
			g.setColor(previewPane.getBackground());
			g.fillRect(0, 0, 150, 150);
			try {prevFrame.setSelected(true);}
			catch (PropertyVetoException ex) {/*ignore, this is finetuning*/}
			prevFrame.paintAll(g);
			prevFrame.dispose();
			testFrame.dispose();
			previews[i] = new JComponent() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(image, 0, 0, this);
				}
				@Override
				public Dimension getPreferredSize() {
					return new Dimension(120, 120);
				}
			};
		}
		JAlgoWindow.setLookAndFeel(Settings.getString("LookAndFeel"));
		return previews;
	}

	private static String[] getAvailableLanguages() {
		return Messages.getString("main_res",
			"Available_languages.full").split(",");
	}

	private static String[] getAvailableLookAndFeelNames() {
		installedLAFs = UIManager.getInstalledLookAndFeels();
		String[] lafNames = new String[installedLAFs.length];
		for (int i=0; i<installedLAFs.length; i++) {
			lafNames[i] = installedLAFs[i].getName();
		}
		return lafNames;
	}
}