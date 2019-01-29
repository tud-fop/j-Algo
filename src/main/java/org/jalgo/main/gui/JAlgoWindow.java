package org.jalgo.main.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import org.jalgo.main.AbstractModuleConnector;
import org.jalgo.main.JAlgoMain;
import org.jalgo.main.AbstractModuleConnector.SaveStatus;
import org.jalgo.main.gui.components.HelpSystem;
import org.jalgo.main.gui.components.JToolbarButton;
import org.jalgo.main.gui.components.ModuleChooseDialog;
import org.jalgo.main.gui.event.JAlgoWindowActionHandler;
import org.jalgo.main.gui.event.StatusLineUpdater;
import org.jalgo.main.util.Messages;
import org.jalgo.main.util.Settings;

/**
 * The class <code>JAlgoWindow</code> represents the main frame of the j-Algo
 * main program.
 * 
 * @author Alexander Claus
 */
public class JAlgoWindow
extends JFrame {

	private static final long serialVersionUID = -5392277576384049416L;

	/** The singleton instance of <code>JalgoWindow</code> */
	private static JAlgoWindow instance;

	private static Window splash;
	private JPanel contentPane;
	private JPanel toolbarPane;
	private JLabel statusLine;
	private JTabbedPane tabbedPane;
	private JButton closeButton;
	private JMenuItem closeMenuItem;

	private JMenuBar menubar;
	private LinkedList<JMenuItem> newModuleMenuItems;
	private JButton saveButton;
	private JMenuItem saveMenuItem;
	private JButton saveAsButton;
	private JMenuItem saveAsMenuItem;
	private JMenuItem aboutModuleMenuItem;

	private static JFileChooser openFileChooser;
	private static JFileChooser saveFileChooser;

	private JAlgoWindowActionHandler action;

	//management of module gui compontents
	private List<AbstractModuleConnector> openModuleInstances;
	private HashMap<AbstractModuleConnector, JComponent> moduleComponents;
	private HashMap<AbstractModuleConnector, JMenu> moduleMenus;
	private HashMap<AbstractModuleConnector, JToolBar> moduleToolbars;

	public JAlgoWindow() {
		super("j-Algo");

		if (instance != null) {
			System.err.println(Messages.getString(
				"main", "JalgoWindow.No_second_instance")); //$NON-NLS-1$ //$NON-NLS-2$
			System.err.println(Messages.getString(
				"main", "JalgoWindow.Application_terminates")); //$NON-NLS-1$ //$NON-NLS-2$
			System.exit(1);
		}

		setSize(800, 600);
		setLocation(100, 100);
		if (Settings.getBoolean("main", "MaximizeWindowOnStartup"))
			setExtendedState(MAXIMIZED_BOTH);
		addWindowStateListener(new WindowStateListener() {
			public void windowStateChanged(WindowEvent e) {
				switch (e.getNewState()) {
					case NORMAL:
						Settings.setBoolean("main", "MaximizeWindowOnStartup", false);
						break;
					case MAXIMIZED_BOTH:
						Settings.setBoolean("main", "MaximizeWindowOnStartup", true);
						break;
				}
			}
		});

		setIconImage(Toolkit.getDefaultToolkit().createImage(
			Messages.getResourceURL("main", "ui.Logo_small")));

		String lafClassName = Settings.getString("main", "LookAndFeel");
		if (lafClassName == null || 
		((lafClassName.equals("com.sun.java.swing.plaf.windows.WindowsLookAndFeel")) && 
				(! System.getProperty("os.name").startsWith("Windows")))) {
			Settings.setString("main", "LookAndFeel",
				UIManager.getSystemLookAndFeelClassName());
			lafClassName = Settings.getString("main", "LookAndFeel");
		}
		setLookAndFeel(lafClassName);

		action = new JAlgoWindowActionHandler(this);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@SuppressWarnings("synthetic-access")
			public void windowClosing(WindowEvent arg0) {
				handleCloseEvent();
			}
		});

		createLayout();

		openModuleInstances = new ArrayList<AbstractModuleConnector>();
		moduleComponents = new HashMap<AbstractModuleConnector, JComponent>();
		moduleMenus = new HashMap<AbstractModuleConnector, JMenu>();
		moduleToolbars = new HashMap<AbstractModuleConnector, JToolBar>();

		setVisible(true);

		instance = this;

		hideSplash();

		if (Settings.getBoolean("main", "ShowModuleChooserOnStartup"))
			EventQueue.invokeLater(new Runnable() {
				@SuppressWarnings("synthetic-access")
				public void run() {
					ModuleChooseDialog.open(instance);
				}
			});
	}

	/*--------------------------gui building------------------------------*/
	private void createLayout() {
		setLayout(new BorderLayout());
		contentPane = new JPanel(new BorderLayout());
		add(contentPane, BorderLayout.CENTER);

		installMenu();
		installToolbar();
		installStatusLine();
		installTabbedPane();
	}

	private void installMenu() {
		menubar = new JMenuBar();

		JMenu fileMenu = createMenu("ui.File");

		createNewModuleMenuItems();
		JMenu newMenu = createMenu("ui.New");
		for (JMenuItem item : newModuleMenuItems) newMenu.add(item);
		fileMenu.add(newMenu);
		fileMenu.addSeparator();

		JMenuItem openMenuItem = createMenuItem("ui.Open_file");
		openMenuItem.setAccelerator(
			KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		fileMenu.add(openMenuItem);

		saveMenuItem = createMenuItem("ui.Save_file");
		saveMenuItem.setAccelerator(
			KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		saveMenuItem.setEnabled(false);
		fileMenu.add(saveMenuItem);

		saveAsMenuItem = createMenuItem("ui.Save_as");
		saveAsMenuItem.setEnabled(false);
		fileMenu.add(saveAsMenuItem);

		closeMenuItem = createMenuItem("ui.Close");
		closeMenuItem.setEnabled(false);
		fileMenu.add(closeMenuItem);
		fileMenu.addSeparator();

		JMenuItem prefsMenuItem = createMenuItem("ui.Prefs");
		fileMenu.add(prefsMenuItem);
		fileMenu.addSeparator();

		JMenuItem exitMenuItem = createMenuItem("ui.Exit");
		fileMenu.add(exitMenuItem);

		menubar.add(fileMenu);

		JMenu helpMenu = createMenu("ui.Help");
		
		JMenuItem helpContentsMenuItem = createMenuItem("ui.Help_contents");
		helpContentsMenuItem.setAccelerator(
			KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));	
		helpMenu.add(helpContentsMenuItem); 
		helpMenu.addSeparator();
		
		JMenuItem aboutMenuItem = createMenuItem("ui.About");
		helpMenu.add(aboutMenuItem); 

		aboutModuleMenuItem = createMenuItem("ui.About_module");
		aboutModuleMenuItem.setEnabled(false);
		helpMenu.add(aboutModuleMenuItem); 

		menubar.add(helpMenu);

		setJMenuBar(menubar);
	}
	

	/**
	 * Creates the <code>JMenuItem</code> objects for each registered module.
	 * This objects are later displayed as menu items in the "New" menu.
	 */
	private void createNewModuleMenuItems() {
		newModuleMenuItems = new LinkedList<JMenuItem>();
		JAlgoMain main = JAlgoMain.getInstance();
		JMenuItem current;
		for (int i=0; i<main.getKnownModuleInfos().size(); i++) {
			current = new JMenuItem(
				main.getKnownModuleInfos().get(i).getName(),
				new ImageIcon(main.getKnownModuleInfos().get(i).getLogoURL()));
			current.setToolTipText(
				main.getKnownModuleInfos().get(i).getDescription());
			current.setActionCommand(
				"new"+main.getKnownModuleInfos().get(i).getName());
			current.addActionListener(action);
			current.setAccelerator(KeyStroke.getKeyStroke(
				'1'+i, InputEvent.CTRL_MASK));
			newModuleMenuItems.add(current);
		}
	}	
	
	private void installToolbar() {
		JToolBar standardToolbar = new JToolBar("Standard",
			SwingConstants.HORIZONTAL);
		standardToolbar.setFloatable(false);
		standardToolbar.setRollover(true);

		JButton newButton = createToolbarButton("ui.New");
		newButton.getInputMap().put(
			KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK),
			"ui.New");
		newButton.getActionMap().put("ui.New", action);
		standardToolbar.add(newButton);

		JButton openButton = createToolbarButton("ui.Open_file");
		standardToolbar.add(openButton);
		standardToolbar.addSeparator();
		
		saveButton = createToolbarButton("ui.Save_file");
		saveButton.setEnabled(false);
		standardToolbar.add(saveButton);
			
		saveAsButton = createToolbarButton("ui.Save_as");
		saveAsButton.setEnabled(false);
		standardToolbar.add(saveAsButton);
		standardToolbar.addSeparator();
		standardToolbar.add(Box.createVerticalStrut(20));

		toolbarPane = new JPanel(new BorderLayout());
		toolbarPane.add(standardToolbar, BorderLayout.WEST);
		contentPane.add(toolbarPane, BorderLayout.NORTH);
	}

	private void installStatusLine() {
		JPanel statusPane = new JPanel(new BorderLayout());
		statusLine = new JLabel();
		statusPane.add(Box.createHorizontalStrut(5), BorderLayout.WEST);
		statusPane.add(statusLine, BorderLayout.CENTER);
		statusPane.add(Box.createVerticalStrut(24), BorderLayout.EAST);
		contentPane.add(statusPane, BorderLayout.SOUTH);

	}

	private void installTabbedPane() {
		tabbedPane = new JTabbedPane();
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.addChangeListener(new ChangeListener() {
			@SuppressWarnings("synthetic-access")
			public void stateChanged(ChangeEvent e) {
				/* Warning! since ChangeEvents are also fired at creation and
				 * destruction of module instances, at these circumstances the
				 * programmer has to disable this ChangeListener temporarily
				 * (remove first registered ChangeListener, manipulate tabs,
				 * finally add the removed ChangeListener again,
				 * see #tabClosed() or #createNewModuleGUIComponents() for
				 * example) - AC */ 
				tabSelected();
			}
		});
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		closeButton = createToolbarButton("ui.Close");
		closeButton.setVisible(false);
		toolbarPane.add(closeButton, BorderLayout.EAST);
	}

	private JMenu createMenu(String key) {
		JMenu menu = new JMenu(Messages.getString("main", key));
		menu.setMnemonic(Messages.getString("main", key+"_mnemonic").charAt(0));
		return menu;
	}

	private JMenuItem createMenuItem(String actionKey) {
		JMenuItem item = new JMenuItem(
			Messages.getString("main", actionKey),
			new ImageIcon(Messages.getResourceURL("main", actionKey)));
		item.setMnemonic(
			Messages.getString("main", actionKey+"_mnemonic").charAt(0));
		item.setActionCommand(actionKey);
		//the HelpMenuItem needs its own ActionListener @see JavaHelp
		if (actionKey.equals("ui.Help_contents")){
			HelpSystem hd = new HelpSystem();
			item.addActionListener(hd.getJavaHelpActionListener());
		}
		else item.addActionListener(action);
		return item;
	}

	/**
	 * Here a special ui of buttons is created because of the fact, that the
	 * native look and feel doesn't support rollover mechanism.
	 */
	private JButton createToolbarButton(String actionKey) {
		JToolbarButton button = new JToolbarButton(
			new ImageIcon(Messages.getResourceURL("main", actionKey)), //$NON-NLS-1$
			Messages.getString("main", actionKey+"_tooltip"), //$NON-NLS-1$ //$NON-NLS-2$
			actionKey);
		button.addActionListener(action);
		button.addMouseListener(StatusLineUpdater.getInstance());

		return button;
	}

	/**
	 * Creates and shows a splash screen.
	 */
	public static void createSplashScreen() {
		if (!Settings.getBoolean("main", "ShowSplashOnStartup")) return;

		final Image splashImg = Toolkit.getDefaultToolkit().getImage(
			Messages.getResourceURL("main", "ui.Splash"));
		splash = new Window(new Frame()) {
			private static final long serialVersionUID = -8639948348008098638L;

			@SuppressWarnings("synthetic-access")
			public void paint(Graphics g) {
				g.drawImage(splashImg, 0, 0, splash);
			}
		};
		MediaTracker tracker = new MediaTracker(splash);
		tracker.addImage(splashImg, 0);
		try {tracker.waitForAll();}
		catch (InterruptedException e) {System.err.println(e.toString());}
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		splash.setSize(splashImg.getWidth(splash), splashImg.getHeight(splash));
		splash.setLocation(
			(screenSize.width-splashImg.getWidth(splash))/2,
			screenSize.height*2/5-splashImg.getHeight(splash)/2);
		splash.setAlwaysOnTop(true);
		splash.setVisible(true);
	}

	/**
     * Hides the splash screen.
     */
	private void hideSplash() {
		if (splash != null) splash.dispose();
	}

	public static void setLookAndFeel(String className) {
		try {
			UIManager.setLookAndFeel(className);
		}
		catch (Exception ex) {
			System.out.println("Error setting LAF:");
			ex.printStackTrace();
		}
	}

	/*--------------------------event handling----------------------------*/	
	public void handleCloseEvent() {
		while (JAlgoMain.getInstance().getCurrentInstance() != null) {
			if (!tabClosed()) return;
		}
		dispose();
		System.exit(0);
	}

	/**
	 * If the module data of the current module are not saved, this method asks
	 * the user for saving his work, when the module / program is intended
	 * to be closed. This method returns <code>false</code>, if the user presses
	 * <code>CANCEL</code> during this process, <code>true</code> otherwise.
	 * 
	 * @return <code>true</code>, if module data are saved or if the user closes
	 * 			all dialogs normally, <code>false</code>, if the user presses
	 * 			<code>CANCEL</code> during saving
	 */
	private boolean showFinalSaveDialog(AbstractModuleConnector moduleInstance) {
		if (moduleInstance.getSaveStatus() == SaveStatus.NO_CHANGES ||
			moduleInstance.getSaveStatus() == SaveStatus.NOTHING_TO_SAVE)
			return true;
		switch (showConfirmDialog(Messages.getString("main", "ui.Wish_to_save"), //$NON-NLS-1$ //$NON-NLS-2$
			DialogConstants.YES_NO_CANCEL_OPTION)) {
			case DialogConstants.YES_OPTION:
				return JAlgoMain.getInstance().saveFile();
			case DialogConstants.NO_OPTION:
				return true;
			case DialogConstants.CANCEL_OPTION:
				return false;
			default:
				return false;
		}
	}

	private void tabSelected() {
		int tabIndex = tabbedPane.getSelectedIndex();

		if (tabIndex < 0) {System.out.println("JAlgoWindow.tabSelected(): tabIndex="+tabIndex); return;}
		setCurrentInstanceVisible(false);
		JAlgoMain.getInstance().setCurrentInstance(
			openModuleInstances.get(tabIndex));
		setCurrentInstanceVisible(true);
		updateTitle();
	}

	public boolean tabClosed() {
		AbstractModuleConnector currentInstance =
			JAlgoMain.getInstance().getCurrentInstance();
		if (!currentInstance.close() ||
			!showFinalSaveDialog(currentInstance)) return false;

		int tabIndex = tabbedPane.getSelectedIndex();
		setCurrentInstanceVisible(false);
		ChangeListener tabbedPaneListener = tabbedPane.getChangeListeners()[0]; 
		tabbedPane.removeChangeListener(tabbedPaneListener);
		tabbedPane.removeTabAt(openModuleInstances.indexOf(currentInstance));

		moduleComponents.remove(currentInstance);
		moduleMenus.remove(currentInstance);
		moduleToolbars.remove(currentInstance);
		openModuleInstances.remove(currentInstance);

		if (openModuleInstances.isEmpty()) {
			closeButton.setVisible(false);
			closeMenuItem.setEnabled(false);
			aboutModuleMenuItem.setEnabled(false);
			JAlgoMain.getInstance().setCurrentInstance(null);
			updateSaveButtonEnableStatus();
			updateTitle();
		}
		else {
			tabbedPane.setSelectedIndex(Math.max(0, tabIndex-1));
			//JTabbedPane.setSelectedIndex() doesn't fire a change event!
			// since ChangeListener is temporarily deactivated - AC
			JAlgoMain.getInstance().setCurrentInstance(
				openModuleInstances.get(tabbedPane.getSelectedIndex()));
			setCurrentInstanceVisible(true);
			updateTitle();
		}
		tabbedPane.addChangeListener(tabbedPaneListener);
		return true;
	}

	/**
	 * Implements the standard behaviour of the title text of the main program.
	 * This means, that the save status and file name of the current work are
	 * shown and the currently opened module is mentioned.
	 */
	public void updateTitle() {
		AbstractModuleConnector currentInstance =
			JAlgoMain.getInstance().getCurrentInstance();
		StringBuffer title = new StringBuffer();
		if (currentInstance != null) {
			if (currentInstance.getOpenFileName() != null) {
				if (currentInstance.getOpenFileName().length() == 0)
					title.append(Messages.getString("main", "ui.Untitled"));
				else title.append(currentInstance.getOpenFileName());
				if (currentInstance.getSaveStatus() ==
					SaveStatus.CHANGES_TO_SAVE) title.append("*  -  ");
				else title.append("  -  ");
			}
			title.append(currentInstance.getModuleInfo().getName());
			title.append("  -  ");
		}
		title.append(Messages.getString("main", "General.name")); //$NON-NLS-1$ //$NON-NLS-2$
		setTitle(title.toString());
	}

	public void showErrorMessage(String msg) {
		JOptionPane.showOptionDialog(this, msg,
			Messages.getString("main", "DialogConstants.Error"), //$NON-NLS-1$ //$NON-NLS-2$
			JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null,
			DialogConstants.getOptionStrings(DialogConstants.OK_OPTION), null);
	}

	public void showWarningMessage(String msg) {
		JOptionPane.showOptionDialog(this, msg,
			Messages.getString("main", "DialogConstants.Warning"), //$NON-NLS-1$ //$NON-NLS-2$
			JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
			DialogConstants.getOptionStrings(DialogConstants.OK_OPTION), null);
	}

	public void showInfoMessage(String msg) {
		JOptionPane.showOptionDialog(this, msg,
			Messages.getString("main", "DialogConstants.Info"), //$NON-NLS-1$ //$NON-NLS-2$
			JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
			DialogConstants.getOptionStrings(DialogConstants.OK_OPTION), null);
	}

	public int showConfirmDialog(String question, int optionType) {
		return JOptionPane.showOptionDialog(this, question,
			Messages.getString("main", "DialogConstants.Question"), //$NON-NLS-1$ //$NON-NLS-2$
			optionType, JOptionPane.QUESTION_MESSAGE, null,
			DialogConstants.getOptionStrings(optionType), null);
	}

	/**
	 * Sets the message at the status line to the given message string.
	 * 
	 * @param msg the message string
	 */
	public void setStatusMessage(String msg) {
		statusLine.setText(msg);
	}

	public String showOpenDialog(boolean openAsJAlgoFile,
		boolean useCurrentModuleInstance) {
		if (openFileChooser == null) {
			openFileChooser = new JFileChooser(System.getProperty("user.dir"));
			openFileChooser.setFileFilter(new FileFilter() {
				public String getDescription() {
					return "j-Algo Dateien";
				}
				public boolean accept(File f) {
					if (f.isDirectory()) return true;
					if (f.getName().toLowerCase().endsWith(".jalgo"))
						return true;
					return false;
				}
			});
		}
		String fileName = null;
		if (openFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
			try {
				fileName = openFileChooser.getSelectedFile().getCanonicalPath();
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		if (openAsJAlgoFile && fileName != null)
			JAlgoMain.getInstance().openFile(
				fileName, useCurrentModuleInstance);
		return fileName;
	}

	public String showSaveDialog(boolean saveAsJAlgoFile) {
		if (saveFileChooser == null) {
			saveFileChooser = new JFileChooser(System.getProperty("user.dir"));
			saveFileChooser.setFileFilter(new FileFilter() {
				public String getDescription() {
					return "j-Algo Dateien";
				}
				public boolean accept(File f) {
					if (f.isDirectory()) return true;
					if (f.getName().toLowerCase().endsWith(".jalgo"))
						return true;
					return false;
				}
			});
		}
		String fileName = null;
		if (saveFileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
			try {
				fileName = saveFileChooser.getSelectedFile().getCanonicalPath();
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		if (saveAsJAlgoFile && fileName != null) {
			if (!fileName.toLowerCase().endsWith(".jalgo"))
				fileName = fileName.concat(".jalgo");
			if (!JAlgoMain.getInstance().saveFileAs(fileName)) fileName = null;
		}
		return fileName;
	}

	/**
	 * Updates the enabled status of the buttons "Save" and "Save as" to provide
	 * correct semantics. The necessary information to do so is taken from the
	 * currently opened module instance.
	 */
	public void updateSaveButtonEnableStatus() {
		AbstractModuleConnector currentInstance =
			JAlgoMain.getInstance().getCurrentInstance();
		if (currentInstance == null ||
			currentInstance.isSavingBlocked()) {
			saveMenuItem.setEnabled(false);
			saveButton.setEnabled(false);
			saveAsMenuItem.setEnabled(false);
			saveAsButton.setEnabled(false);
		}
		else switch (currentInstance.getSaveStatus()) {
			case NOTHING_TO_SAVE:
				saveMenuItem.setEnabled(false);
				saveButton.setEnabled(false);
				saveAsMenuItem.setEnabled(false);
				saveAsButton.setEnabled(false);
				break;
			case NO_CHANGES:
				saveMenuItem.setEnabled(false);
				saveButton.setEnabled(false);
				saveAsMenuItem.setEnabled(true);
				saveAsButton.setEnabled(true);
				break;
			case CHANGES_TO_SAVE:
				saveMenuItem.setEnabled(true);
				saveButton.setEnabled(true);
				saveAsMenuItem.setEnabled(true);
				saveAsButton.setEnabled(true);
				break;
		}
	}

	/*--------------Management of module's GUI components---------------*/

	/**
	 * Retrieves the main GUI component of the module instance, having the given
	 * <code>AbstractModuleConnector</code>.
	 * 
	 * @param module the <code>AbstractModuleConnector</code> instance of module
	 * 
	 * @return the main GUI component of the module instance
	 */
	protected JComponent getModuleComponent(AbstractModuleConnector module) {
		return moduleComponents.get(module);
	}

	/**
	 * Retrieves the menu of the module instance, having the given
	 * <code>AbstractModuleConnector</code>.
	 * 
	 * @param module the <code>AbstractModuleConnector</code> instance of module
	 * 
	 * @return the menu of the module instance
	 */
	protected JMenu getModuleMenu(AbstractModuleConnector module) {
		return moduleMenus.get(module);
	}

	/**
	 * Retrieves the toolbar of the module instance, having the given
	 * <code>AbstractModuleConnector</code>.
	 * 
	 * @param module the <code>AbstractModuleConnector</code> instance of module
	 * 
	 * @return the toolbar of the module instance
	 */
	protected JToolBar getModuleToolbar(AbstractModuleConnector module) {
		return moduleToolbars.get(module);
	}

	public void setCurrentInstanceVisible(boolean visible) {
		AbstractModuleConnector currentInstance =
			JAlgoMain.getInstance().getCurrentInstance();

		if (getModuleMenu(currentInstance).isEnabled())
			getModuleMenu(currentInstance).setVisible(visible);
		if (getModuleToolbar(currentInstance).isEnabled())
			getModuleToolbar(currentInstance).setVisible(visible);		
		if (visible) updateSaveButtonEnableStatus();
	}

	public void activateNewInstance() {
		// Set tab selected
		tabbedPane.setSelectedComponent(moduleComponents.get(
			JAlgoMain.getInstance().getCurrentInstance()));
		// Set module visible
		setCurrentInstanceVisible(true);
		// Enable 'About module'
		aboutModuleMenuItem.setEnabled(true);
		// Enable 'Close'
		closeButton.setVisible(true);
		closeMenuItem.setEnabled(true);
	}

	public void createNewModuleGUIComponents() {
		AbstractModuleConnector module =
			JAlgoMain.getInstance().getCurrentInstance();

		JMenu moduleMenu = new JMenu(module.getModuleInfo().getName());
		moduleMenus.put(module, moduleMenu);
		moduleMenu.setVisible(false);
		menubar.add(moduleMenu, 1);

		JToolBar moduleToolbar = new JToolBar();
		moduleToolbars.put(module, moduleToolbar);
		moduleToolbar.setFloatable(false);
		moduleToolbar.setVisible(false);
		toolbarPane.add(moduleToolbar, 1);

		moduleComponents.put(module, new JPanel(new BorderLayout()));

		ChangeListener tabbedPaneListener = tabbedPane.getChangeListeners()[0]; 
		tabbedPane.removeChangeListener(tabbedPaneListener);
		tabbedPane.addTab(
			module.getModuleInfo().getName(),
			new ImageIcon(module.getModuleInfo().getLogoURL()),
			moduleComponents.get(module));
		tabbedPane.addChangeListener(tabbedPaneListener);

		openModuleInstances.add(module);
	}
}