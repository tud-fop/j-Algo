/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and
 * platform independent. j-Algo is developed with the help of Dresden
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
package org.jalgo.module.ebnf;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import org.jalgo.main.AbstractModuleConnector.SaveStatus;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.gui.components.JToolbarButton;
import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.controller.ebnf.EbnfController;
import org.jalgo.module.ebnf.controller.syndia.SynDiaController;
import org.jalgo.module.ebnf.controller.trans.TransController;
import org.jalgo.module.ebnf.controller.wordalgorithm.WordAlgorithmController;
import org.jalgo.module.ebnf.gui.EbnfFont;
import org.jalgo.module.ebnf.gui.GUIConstants;
import org.jalgo.module.ebnf.gui.StartAction;
import org.jalgo.module.ebnf.gui.StartScreen;
import org.jalgo.module.ebnf.gui.syndia.display.DisplayController;
import org.jalgo.module.ebnf.model.ebnf.Definition;
import org.jalgo.module.ebnf.model.syndia.SynDiaSystem;

/**
 * 
 * @author Tom
 * 
 */
public class MainController implements GUIConstants {

	private enum Controller {
		NONE, EBNF, SYNDIA, SYNDIADISPLAY, TRANS, WORD
	}

	private Controller activeController = Controller.NONE;

	private ModuleConnector connector;

	private StartScreen startScreen;

	private JPanel contentPane;

	private EbnfController ebnfController;

	private TransController transController;

	private SynDiaController synDiaController;

	private DisplayController synDiaDisplayController;

	private WordAlgorithmController wordAlgoController;

	private Font ebnfFont;

	private StartAction startAction;

	private boolean isDialogOpen;

	private JMenu menu;

	private JToolBar toolBar;

	private JMenuBar menubar;

	private JMenuBar customMenubar;

	private List<JMenu> customMenuList;

	private List<JMenu> customMenuListBackup;

	public MainController(ModuleConnector connector) {
		this.connector = connector;

		// install the main panel
		JComponent rootPane = JAlgoGUIConnector.getInstance()
				.getModuleComponent(connector);
		rootPane.setLayout(new BorderLayout());
		contentPane = new JPanel();
		rootPane.addComponentListener(new rootPaneListener(this));
		rootPane.getParent().addContainerListener(new jAlgoTabContrainerListener(this));
		rootPane.add(contentPane, BorderLayout.CENTER);
//		rootPane.addContainerListener(new rootPaneListener());
		customMenuList = new ArrayList<JMenu>();
		installToolbar();
		installMenu();
		importFont();
		EbnfFont.setFont(this.ebnfFont);
	}

	public void installStartScreen() {
		startScreen = new StartScreen(this);
		contentPane.removeAll();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(startScreen, BorderLayout.CENTER);
		setToolbarButtonsDisabled();
		connector.setSaveStatus(SaveStatus.NOTHING_TO_SAVE);
		contentPane.updateUI();
		contentPane.validate();
	}

	public void doLayout() {
		contentPane.validate();
	}

	public boolean isDialogOpen() {
		return isDialogOpen;
	}

	public void update() {
		doLayout();
	}

	/**
	 * Start EbnfInputMode.
	 */
	public void setEbnfInputMode(boolean transActive) {

		activeController = Controller.EBNF;
		installToolbar();
		// System.out.println("Starte EbnfInput");
		startAction.setEnabled(true);
		contentPane.removeAll();
		contentPane.setBackground(STANDARD_BACKGROUND);
		contentPane.setLayout(new BorderLayout());

		if (!transActive)
			ebnfController = new EbnfController(this, contentPane, connector);
		else
			ebnfController.switchToChoiceGUI(transActive);

	}

	/**
	 * Start or continue TransAlgorithm
	 * 
	 * @param def
	 *            the EbnfDefinition to transform
	 * @param restart
	 *            indicates whether te controller should be (re)started or
	 *            continued
	 */
	public void setTransMode(Definition def, boolean restart) {

		activeController = Controller.TRANS;
		installToolbar();

		// System.out.println("Starte TransAlgorithmus");

		contentPane.removeAll();
		contentPane.setBackground(STANDARD_BACKGROUND);
		contentPane.setLayout(new BorderLayout());

		if (restart) {
			transController = new TransController(this, connector, contentPane,
					def);
		} else {
			transController.switchToAlgorithm();
		}
	}

	/**
	 * Start SynDiaDisplayMode.
	 */
	public void setSynDiaDisplayMode(SynDiaSystem sds) {

		activeController = Controller.SYNDIADISPLAY;
		installToolbar();
		// System.out.println("Starte SynDiaDisplay");
		contentPane.removeAll();
		contentPane.setBackground(STANDARD_BACKGROUND);
		contentPane.setLayout(new BorderLayout());

		synDiaDisplayController = new DisplayController(this, connector,
				contentPane, sds);

	}

	/**
	 * Start SynDiaInputMode.
	 */
	public void setSynDiaInputMode(SynDiaSystem sds) {

		activeController = Controller.SYNDIA;
		installToolbar();
		// System.out.println("Starte SynDiaInput");
		contentPane.removeAll();
		contentPane.setBackground(STANDARD_BACKGROUND);
		contentPane.setLayout(new BorderLayout());
		synDiaController = new SynDiaController(this, connector, sds,
				contentPane);

	}

	/**
	 * Start SynDiaInputMode.
	 */
	public void setWordAlgoMode(SynDiaSystem sds) {

		// SynDiaSystem synDiaSystem1 = SynDiaSystemLibrary.getSynDiaSystem2();
		// synDiaSystem1.removeNullElems();
		installToolbar();
		contentPane.removeAll();
		contentPane.setBackground(STANDARD_BACKGROUND);
		wordAlgoController = new WordAlgorithmController(this, connector,
				contentPane, sds);

	}

	/**
	 * Sets up the toolbar.
	 */
	private void installToolbar() {
		toolBar = JAlgoGUIConnector.getInstance().getModuleToolbar(connector);
		toolBar.removeAll();
		startAction = new StartAction(this, connector);
		toolBar.add(createToolbarButton(startAction));
		toolBar.validate();
		toolBar.repaint();
	}

	/**
	 * Add a menu in the j-Algo menubar. The function positions always the
	 * "help" menu at the end.
	 * 
	 * @param newMenu -
	 *            the new JMenu to be added
	 */
	public void addMenu(JMenu newMenu) {
		customMenuList.add(newMenu);
		JMenu helpMenu = menubar.getMenu(menubar.getMenuCount() - 1);
		menubar.remove(helpMenu);
		menubar.add(newMenu);
		menubar.add(helpMenu);
		menubar.validate();
	}

	/**
	 * This function removes everything ebnf-related in the menubar. This means
	 * everything except the "File" and the "Help" menu are removed.
	 * 
	 */
	public void removeCustomMenu() {
		if (menubar != null) {
			customMenuListBackup = new ArrayList(customMenuList);
			for (JMenu item : customMenuList) {
				menubar.remove(item);
			}
			customMenubar = menubar;
			customMenuList.clear();
		}
		menubar.validate();
	}

	/**
	 * Creates a new ToolbarButton with the desired action.
	 */
	public JButton createToolbarButton(Action a) {
		JToolbarButton button = new JToolbarButton((Icon) a
				.getValue(Action.SMALL_ICON), null, null);
		button.setAction(a);
		button.setText("");
		return button;
	}

	/**
	 * Sets up the menu.
	 */
	private void installMenu() {
		menu = JAlgoGUIConnector.getInstance().getModuleMenu(connector);
		menu.setEnabled(false);
		menubar = (JMenuBar) menu.getParent();
		customMenubar = menubar;

		// ((JMenu) (menubar.getComponent(0))).getMenuComponent(4).;
	}

	public void setToolbarButtonsDisabled() {
		startAction.setEnabled(false);
	}

	/**
	 * This function imports our EBNFSans.TTF-Font and makes it accessable via
	 * the Font ebnfFont of the GUI.class
	 */
	public void importFont() {
		// SwingUtilities.invokeLater( new Runnable() {
		// public void run()
		// {
		try {
			InputStream fontStream = Messages.getResourceURL("ebnf",
					"Ebnf_Font").openStream(); // Stream der Font aus
			// res-Ordner :)
			Font onePoint = Font.createFont(Font.TRUETYPE_FONT, fontStream);
			fontStream.close();
			Font ebnfFont = onePoint.deriveFont(Font.PLAIN, 18);

			this.ebnfFont = ebnfFont;

			// System.out.println("Font 'EBNFSans' initialized");
		} catch (FontFormatException e) {
			System.err.println("FontFormaException: " + e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
			System.exit(1);
		}
	}

	// } );
	// }

	public Font getEbnfFont() {
		return ebnfFont;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.main.AbstractModuleConnector#setDataFromFile(java.io.ByteArrayInputStream)
	 */
	public void setDataFromFile(ByteArrayInputStream data) {
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(data);
			try {
				boolean isEbnf = (Boolean) in.readBoolean();

				if (isEbnf) {
					// load the EbnfInputModule
					setEbnfInputMode(false);
					// load the definition into the module
					ebnfController.loadDefinition(in);
					// Show the View-Panel
					ebnfController.switchToChoiceGUI(false);
				} else {
					try {
						SynDiaSystem system = (SynDiaSystem) in.readObject();
						if (system.isComplete()) {
							system.removeNullElems();
							setSynDiaDisplayMode(system);
						} else {
							system.fillWithNullElems();
							setSynDiaInputMode(system);
						}
					} catch (Exception e) {
						JAlgoGUIConnector
								.getInstance()
								.showErrorMessage(
										Messages
												.getString(
														"ebnf", "SynDia.Error.LoadErrorInvalidDef")); //$NON-NLS-1$ //$NON-NLS-2$

					}

				} // END if(isEbnf)
			} // END try
			catch (Exception e) {
				JAlgoGUIConnector.getInstance().showErrorMessage(
						Messages.getString("ebnf", "General.Error.LoadError")); //$NON-NLS-1$ //$NON-NLS-2$

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) { } // ignore
			}
		}

	}

	public ByteArrayOutputStream getDataForFile() {

		if (activeController == Controller.EBNF) {
			return ebnfController.saveDefinition();
		} else if (activeController == Controller.SYNDIADISPLAY) {
			return synDiaDisplayController.saveSystem();

		} else if (activeController == Controller.SYNDIA) {
			return synDiaController.saveSystem();

		}
		return null;
	}

	public JMenu getMenu() {
		return menu;
	}

	public JToolBar getToolBar() {
		return toolBar;
	}

	public JMenuBar getMenubar() {
		return menubar;
	}

	public JPanel getContentPane() {
		return contentPane;
	}

	public void setContentPane(JPanel contentPane) {
		this.contentPane = contentPane;
	}

	public void setMenubar(JMenuBar menubar) {
		this.menubar = menubar;
	}

	public JMenuBar getCustomMenubar() {
		return customMenubar;
	}

	public void setCustomMenubar(JMenuBar customMenubar) {
		this.customMenubar = customMenubar;
	}

	public List<JMenu> getCustomMenuListBackup() {
		return customMenuListBackup;
	}

	public void showErrorDialog(String message, boolean closeModule) {		
		JOptionPane.showMessageDialog(contentPane.getTopLevelAncestor(),
			    message,
			    Messages.getString("ebnf", "Ebnf.Error.Error"),
			    JOptionPane.ERROR_MESSAGE);
		
		JComponent rootPane = JAlgoGUIConnector.getInstance()
		.getModuleComponent(connector);
		
		if (closeModule) {
			System.out.println("critical error - closing module");
			((JTabbedPane)rootPane.getParent()).remove(rootPane);
		}
	}
}
