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

/* Created on 05.05.2007 */
package org.jalgo.module.pulsemem.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.jalgo.main.AbstractModuleConnector.SaveStatus;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.gui.JAlgoWindow;
import org.jalgo.main.gui.components.JToolbarButton;
import org.jalgo.main.util.Messages;
import org.jalgo.main.gui.event.StatusLineUpdater;
import org.jalgo.module.pulsemem.Admin;
import org.jalgo.module.pulsemem.Controller;
import org.jalgo.module.pulsemem.ModuleConnector;
import org.jalgo.module.pulsemem.core.IOSimulation;
import org.jalgo.module.pulsemem.core.PulsMemLine;
import org.jalgo.module.pulsemem.core.Variable;
import org.jalgo.module.pulsemem.gui.components.InlineBreakpoint;
import org.jalgo.module.pulsemem.gui.components.NOLPanel;
import org.jalgo.module.pulsemem.gui.components.PulseMemTablePanel;
import org.jalgo.module.pulsemem.gui.events.ExportCFileAction;
import org.jalgo.module.pulsemem.gui.events.FirstStepAction;
import org.jalgo.module.pulsemem.gui.events.GoBeamerAction;
import org.jalgo.module.pulsemem.gui.events.ImportCFileAction;
import org.jalgo.module.pulsemem.gui.events.LastStepAction;
import org.jalgo.module.pulsemem.gui.events.NextBreakpointAction;
import org.jalgo.module.pulsemem.gui.events.ParseAction;
import org.jalgo.module.pulsemem.gui.events.PrevBreakpointAction;
import org.jalgo.module.pulsemem.gui.events.StopAction;
import org.jalgo.module.pulsemem.gui.events.WelcomeAction;

/**
 * GUIController.java
 * <p>
 * The class <code>GUIController</code> is the main class of the GUI of the
 * PulsMem module. It initializes the layout of the different parts, offers
 * methods to set the state of buttons etc., to show several dialogs and
 * messages, and causes GUI-components to update when necessary.
 * <p>
 *
 * @version $Revision: 1.54 $
 * @author Martin Brylski - TU Dresden, SWTP 2007
 */
public class GUIController implements GUIConstants, IOSimulation {
    protected ModuleConnector modConn;

    private Controller controller;

    // ////////////////////////
    JComponent contentPane;

    JPanel rootPane;

    JSplitPane splitPane;

    JPanel topPane;

    JPanel leftOne;

    NOLPanel linedTextarea;

    JPanel rightOne;

    JTabbedPane bottomPane;

    JScrollPane consolePane;

    JTextPane console;

    PulseMemTablePanel memPanel;

    InlineBreakpoint lineOfBreakpoint;

    JToolbarButton importCFileToolbarItem;

    JToolbarButton exportCFileToolbarItem;

    JToolbarButton showWelcomeToolbarItem;

    JToolbarButton startParsingToolbarItem;

    JToolbarButton stopParsingToolbarItem;

    JToolbarButton prevBreakpointToolbarItem;

    JToolbarButton nextBreakpointToolbarItem;

    JToolbarButton goBeamerToolbarItem;

    JToolbarButton firstStepToolbarItem;

    JToolbarButton lastStepToolbarItem;

    JSplitPane splitLeftRight;

    ImportCFileAction importCFileAction;

    ExportCFileAction exportCFileAction;

    WelcomeAction welcomeAction;

    ParseAction parseAction;

    StopAction stopAction;

    NextBreakpointAction nextBreakpointAction;

    PrevBreakpointAction prevBreakpointAction;

    GoBeamerAction goBeamerAction;

    FirstStepAction firstStepAction;

    LastStepAction lastStepAction;

    SimpleAttributeSet redSet = new SimpleAttributeSet();

    boolean breakingEnabled = false;

    int guiMode = GUIConstants.PARSE_DISABLED;

    List<Integer> savedInput = new LinkedList<Integer>();

    protected JMenu menu;

    // ////////////////////////

    /**
     * Constructs the <code>GUIController</code> instance for the current
     * PulsMem module instance.<br>
     * Initializes all layout components, especially the toolbar and the menu.
     * Installs an AWT-Frame on the panel provided by the main program, so that
     * GUI of the PulsMem module can be created on Swing base.
     *
     * @param modConn
     *            the <code>ModuleConnector</code> of the PulsMem module
     * @param controller
     *            the <code>Controller</code> of the PulsMem module
     */
    public GUIController(ModuleConnector modConn, Controller controller) {
        this.modConn = modConn;
        this.controller = controller;
        this.lineOfBreakpoint = new InlineBreakpoint();
        // sets a red font to the SimpleAttributeSet 'redSet'. Used for
        // errorMessages in console
        StyleConstants.setForeground(redSet, Color.RED);
    }

    /**
     * Shows the Welcome Screen of the Pulsemem-Modul.
     */
    public void showWelcomeScreen() {
    }

    public InlineBreakpoint getInlineBreakpoints() {
        return this.lineOfBreakpoint;
    }

    /**
     * Shows the Pulsemem-GUI.
     */
    public void showPulseMem() {
        createActions();
        createToolbar();
        createMenuItems();
        createContent();
    }

    /**
     * Creates the whole content in from the Pulsemem View.<br>
     * It offers a SplitPane, wich TopLevel contains the C-Editor and the
     * MemoryTable and wich BottomLevel contain the Console and some Information
     * output.
     */
    private void createContent() {
        this.contentPane = createJAlgoParentPane();
        this.rootPane = createRootPane();
        this.contentPane.add(this.rootPane, BorderLayout.CENTER);

        // SPLITPANE
        // INCLUDES TOP AND BOTTOM PANE
        this.splitPane = createSplitPane();
        this.splitLeftRight = createSplitLeftRight();

        // TOP PANE
        // INCLUDES LEFT AND RIGHT PANEL
        this.topPane = createTopPanel();

        // LEFT PANE
        this.leftOne = createLeftTopPanel();
        this.linedTextarea = createLinedTeaxtarea();

        this.leftOne.add(this.linedTextarea, BorderLayout.WEST);
        this.leftOne.add(this.linedTextarea.getScrollPane(),
                BorderLayout.CENTER);

        this.splitLeftRight.setLeftComponent(this.leftOne);

        // RIGHT PANEL
        this.rightOne = createRightTopPanel();
        this.splitLeftRight.setRightComponent(this.rightOne);

        this.memPanel = createMemPanel();
        // in der folgenden zeile stand BorderLayout.WEST - AARGH!!!
        // und ich wunder mich wochenlang warum meine anzeige so schei�e is...
        this.rightOne.add(this.memPanel, BorderLayout.CENTER);

        this.topPane.add(splitLeftRight, BorderLayout.CENTER);

        // TABBEDPANE
        // INCLUDES CONSOLESCROLLPANE AND INFORMATIONSCROLLPANE
        this.bottomPane = createBottomPanel();

        this.consolePane = createConsoleScrollPane();
        this.console = createConsoleTextarea();
        this.consolePane.setViewportView(this.console);

        this.bottomPane.addTab(Messages.getString(
                "pulsemem", "GUIController.consoleText"), this.consolePane); //$NON-NLS-1$

        this.splitPane.setTopComponent(this.topPane);
        this.splitPane.setBottomComponent(this.bottomPane);

        this.rootPane.add(this.splitPane, BorderLayout.CENTER);

    }

    /**
     * Creates a rootPane to wich all components nedded by Pulsemem can be
     * added.
     *
     * @return the rootPane
     */
    private JPanel createRootPane() {
        JPanel rootPane = new JPanel();
        rootPane.setLayout(new BorderLayout());
        return rootPane;
    }

    /**
     * @return the parent pane of jAlgo.
     */
    private JComponent createJAlgoParentPane() {
        JComponent contentPane = JAlgoGUIConnector.getInstance()
                .getModuleComponent(this.modConn);
        contentPane.setLayout(new BorderLayout());
        contentPane.addComponentListener(new ComponentListener() {
            public void componentHidden(ComponentEvent e) {
            };

            public void componentMoved(ComponentEvent e) {
            };

            public void componentResized(ComponentEvent e) {
                splitPane.setDividerLocation((int) (getJAlgoWindow()
                        .getHeight() * 0.85) - 150);
            };

            public void componentShown(ComponentEvent e) {
            };
        });
        return contentPane;
    }

    /**
     * Creates the BottomPane used by Pulsemem.
     *
     * @return the BottomPane.
     */
    private JTabbedPane createBottomPanel() {
        JTabbedPane bottomPane = new JTabbedPane();
        bottomPane.setMaximumSize(new Dimension(50, 50));
        bottomPane.setPreferredSize(new Dimension(50, 50));
        return bottomPane;
    }

    /**
     * Creates a JEditorPane with linenumbers on the left. Breakpoints could
     * also be set.
     *
     * @return a EditorTextarea
     */
    private NOLPanel createLinedTeaxtarea() {
        NOLPanel linedTextarea = new NOLPanel(this.lineOfBreakpoint);
        linedTextarea.setTextFont(GUIConstants.STANDARDFONT);
        // linedTextarea
        // .setText("#include <stdio.h>\n\nvoid test2(int *a, int b)\n{\n while
        // (*a == b)\n {\n scanf(\"%i\", &b);\n if (b == 0)\n break;\n *a =
        // *a+b;\n printf(\"%d\", b);\n }\n}\n\nint main(void) \n{\n int *a, i =
        // 0;\n const int b = 2;\n *a = b+i;\n if (i >= b + *a)\n {\n if (i==4)
        // \n test2(&b, b);\n } else break;\n\t\n printf(\"%d\", i);\n}");

        // adds an KeyListener to the JEditorPane contained in the NOLPanel

        linedTextarea.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent ev) {
                GUIController.this.modConn
                        .setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
            }

            public void keyReleased(KeyEvent ev) {
                GUIController.this.modConn
                        .setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
            }

            public void keyTyped(KeyEvent ev) {
                GUIController.this.modConn
                        .setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
                repaintForm();
                lineOfBreakpoint.clear();
            }

        });
        return linedTextarea;
    }

    /**
     * Creates the ConsoleTextarea used by Pulsemem.
     *
     * @return the ConsoleTextarea.
     */
    private JTextPane createConsoleTextarea() {
        JTextPane console = new JTextPane();
        console.setEditable(false);
        console.setMinimumSize(new Dimension(50, 50));
        console.setPreferredSize(new Dimension(50, 50));
        console.setMaximumSize(new Dimension(50, 50));
        return console;
    }

    /**
     * Creates the ConsoleScrollPane used by Pulsemem.
     *
     * @return the ConsoleScrollPane.
     */
    private JScrollPane createConsoleScrollPane() {
        JScrollPane consolePane = new JScrollPane();
        consolePane
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        consolePane.setMinimumSize(new Dimension(50, 50));
        consolePane.setPreferredSize(new Dimension(50, 50));
        consolePane.setMaximumSize(new Dimension(50, 50));
        return consolePane;
    }

    /**
     * Creates the TopPanel used by Pulsemem.
     *
     * @return the TopPanel.
     */
    private JPanel createTopPanel() {
        JPanel topPane = new JPanel();
        topPane.setLayout(new BorderLayout());
        topPane.setMinimumSize(new Dimension(600, 300));
        topPane.setPreferredSize(new Dimension(800, 600));
        return topPane;
    }

    /**
     * Creates the right panel of the top panel used by Pulsemem.
     *
     * @return the right panel of the top panel
     */
    private JPanel createRightTopPanel() {
        JPanel rightOne = new JPanel();
        rightOne.setBorder(new TitledBorder(Messages.getString(
                "pulsemem", "GUIController.pulsememetableText"))); //$NON-NLS-1$
        rightOne.setLayout(new BorderLayout());
        return rightOne;
    }

    /**
     * Creates the left panel of the top panel used by Pulsemem.
     *
     * @return the left panel of the top panel
     */
    private JPanel createLeftTopPanel() {
        JPanel leftOne = new JPanel();
        leftOne.setBorder(new TitledBorder(Messages.getString(
                "pulsemem", "GUIController.editorText"))); //$NON-NLS-1$
        leftOne.setLayout(new BorderLayout());
        leftOne.setPreferredSize(new Dimension(300, 600));
        leftOne.setMinimumSize(new Dimension(300, 600));

        leftOne.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent arg0) {
                int line = GUIController.this.linedTextarea
                        .determineLineFromPoint(arg0.getPoint());
                if (arg0.getClickCount() == 1
                        && GUIController.this.breakingEnabled) {
                    // Changes were made, saving is now possible
                    GUIController.this.modConn
                            .setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
                    if (GUIController.this.lineOfBreakpoint.containsLine(line)) {
                        GUIController.this.lineOfBreakpoint.removeLine(line);
                        updateTableBreakpoints();
                    } else {
                        GUIController.this.lineOfBreakpoint.addLine(line);
                        updateTableBreakpoints();
                    }
                }
                GUIController.this.linedTextarea.repaint();
            }

            public void mouseEntered(MouseEvent arg0) {
            }

            public void mouseExited(MouseEvent arg0) {
            }

            public void mousePressed(MouseEvent arg0) {
            }

            public void mouseReleased(MouseEvent arg0) {
            }

        });
        return leftOne;
    }

    /**
     * Creates the SplitPane used by Pulsemem.
     *
     * @return the SplitPane.
     */
    private JSplitPane createSplitPane() {
        JSplitPane splitPane = new JSplitPane();
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(getJAlgoWindow().getHeight() - 200);
        return splitPane;
    }

    /**
     * Creates the vertical SplitPane used by Pulsemem.
     *
     * @return the SplitPane.
     */
    private JSplitPane createSplitLeftRight() {
        JSplitPane splitLeftRight = new JSplitPane();
        splitLeftRight.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitLeftRight.setContinuousLayout(true);
        splitLeftRight.setOneTouchExpandable(true);
        splitLeftRight.setDividerLocation(300);
        return splitLeftRight;
    }

    /**
     * Creates the PulseMemTablePanel used by PulseMem.
     *
     * @return the PulseMemTablePanel.
     */
    private PulseMemTablePanel createMemPanel() {
        PulseMemTablePanel memTable = new PulseMemTablePanel();
        memTable.setGUI(this);
        return memTable;
    }

    /**
     * Creates the Actions.
     */
    private void createActions() {
        this.importCFileAction = new ImportCFileAction(this, this.controller);

        this.exportCFileAction = new ExportCFileAction(this, this.controller);

        this.welcomeAction = new WelcomeAction(this, this.controller);

        this.parseAction = new ParseAction(this, this.controller);

        this.stopAction = new StopAction(this, this.controller);

        this.nextBreakpointAction = new NextBreakpointAction(this,
                this.controller);

        this.prevBreakpointAction = new PrevBreakpointAction(this,
                this.controller);

        this.firstStepAction = new FirstStepAction(this, this.controller);

        this.lastStepAction = new LastStepAction(this, this.controller);
    }

    /**
     * Creates the Menu.
     */
    private void createMenuItems() {
        menu = JAlgoGUIConnector.getInstance().getModuleMenu(this.modConn);
        // add all
        menu.add(this.importCFileAction);
        menu.add(this.exportCFileAction);
        menu.addSeparator();
        menu.add(this.parseAction);
        menu.add(this.stopAction);
        menu.add(this.firstStepAction);
        menu.add(this.prevBreakpointAction);
        menu.add(this.nextBreakpointAction);
        menu.add(this.lastStepAction);
        menu.addSeparator();

        this.goBeamerAction = GoBeamerAction.getInstance(this, this.controller);
        menu.add(this.goBeamerAction);

        menu.getItem(4).setEnabled(false);
        menu.getItem(5).setEnabled(false);
        menu.getItem(6).setEnabled(false);
        menu.getItem(7).setEnabled(false);
        menu.getItem(8).setEnabled(false);
        menu.getItem(10).setSelected(false);
    }

    /**
     * Creates the Toolbar.
     */
    private void createToolbar() {
        JToolBar toolbar = JAlgoGUIConnector.getInstance().getModuleToolbar(
                this.modConn);

        // mit JToolbarButton von org.jalgo.main.gui.components.JToolbarButton
        // ersetzen
        this.importCFileToolbarItem = new JToolbarButton(
                (Icon) this.importCFileAction.getValue(Action.SMALL_ICON),
                null, null);
        this.importCFileToolbarItem.setAction(this.importCFileAction);
        this.importCFileToolbarItem.setText(""); //$NON-NLS-1$
        this.importCFileToolbarItem.setToolTipText(Admin
                .getLanguageString("gui.ImportCFileToolTip"));
        this.importCFileToolbarItem.addMouseListener(StatusLineUpdater
                .getInstance());

        this.exportCFileToolbarItem = new JToolbarButton(
                (Icon) exportCFileAction.getValue(Action.SMALL_ICON), null,
                null);
        this.exportCFileToolbarItem.setAction(exportCFileAction);
        this.exportCFileToolbarItem.setText("");
        this.exportCFileToolbarItem.setToolTipText(Admin
                .getLanguageString("gui.ExportCFileToolTip"));
        this.exportCFileToolbarItem.addMouseListener(StatusLineUpdater
                .getInstance());

        /*
         * NOT USED YET showWelcomeToolbarItem = new JToolbarButton((Icon)
         * welcomeAction .getValue(Action.SMALL_ICON), null, null);
         * showWelcomeToolbarItem.setAction(welcomeAction);
         * showWelcomeToolbarItem.setText("");
         */

        this.startParsingToolbarItem = new JToolbarButton(
                (Icon) this.parseAction.getValue(Action.SMALL_ICON), null, null);
        this.startParsingToolbarItem.setAction(this.parseAction);
        this.startParsingToolbarItem.setText(""); //$NON-NLS-1$
        this.startParsingToolbarItem.setToolTipText(Admin
                .getLanguageString("gui.StartParsingToolTip"));
        this.startParsingToolbarItem.addMouseListener(StatusLineUpdater
                .getInstance());

        this.stopParsingToolbarItem = new JToolbarButton((Icon) this.stopAction
                .getValue(Action.SMALL_ICON), null, null);
        this.stopParsingToolbarItem.setAction(this.stopAction);
        this.stopParsingToolbarItem.setText(""); //$NON-NLS-1$
        this.stopParsingToolbarItem.setEnabled(false);
        this.stopParsingToolbarItem.setToolTipText(Admin
                .getLanguageString("gui.StopParsingToolTip"));
        this.stopParsingToolbarItem.addMouseListener(StatusLineUpdater
                .getInstance());

        this.nextBreakpointToolbarItem = new JToolbarButton(
                (Icon) this.nextBreakpointAction.getValue(Action.SMALL_ICON),
                null, null);
        this.nextBreakpointToolbarItem.setAction(this.nextBreakpointAction);
        this.nextBreakpointToolbarItem.setText(""); //$NON-NLS-1$
        this.nextBreakpointToolbarItem.setEnabled(false);
        this.nextBreakpointToolbarItem.setToolTipText(Admin
                .getLanguageString("gui.NextBreakpointToolTip"));
        this.nextBreakpointToolbarItem.addMouseListener(StatusLineUpdater
                .getInstance());

        this.prevBreakpointToolbarItem = new JToolbarButton(
                (Icon) this.prevBreakpointAction.getValue(Action.SMALL_ICON),
                null, null);
        this.prevBreakpointToolbarItem.setAction(this.prevBreakpointAction);
        this.prevBreakpointToolbarItem.setText(""); //$NON-NLS-1$
        this.prevBreakpointToolbarItem.setEnabled(false);
        this.prevBreakpointToolbarItem.setToolTipText(Admin
                .getLanguageString("gui.PrevBreakpointToolTip"));
        this.prevBreakpointToolbarItem.addMouseListener(StatusLineUpdater
                .getInstance());

        this.firstStepToolbarItem = new JToolbarButton(
                (Icon) this.firstStepAction.getValue(Action.SMALL_ICON), null,
                null);
        this.firstStepToolbarItem.setAction(this.firstStepAction);
        this.firstStepToolbarItem.setText(""); //$NON-NLS-1$
        this.firstStepToolbarItem.setEnabled(false);
        this.firstStepToolbarItem.setToolTipText(Admin
                .getLanguageString("gui.FirstStepToolTip"));
        this.firstStepToolbarItem.addMouseListener(StatusLineUpdater
                .getInstance());

        this.lastStepToolbarItem = new JToolbarButton(
                (Icon) this.lastStepAction.getValue(Action.SMALL_ICON), null,
                null);
        this.lastStepToolbarItem.setAction(this.lastStepAction);
        this.lastStepToolbarItem.setText(""); //$NON-NLS-1$
        this.lastStepToolbarItem.setEnabled(false);
        this.lastStepToolbarItem.setToolTipText(Admin
                .getLanguageString("gui.LastStepToolTip"));
        this.lastStepToolbarItem.addMouseListener(StatusLineUpdater
                .getInstance());

        // add all buttons to toolbar
        /*
         * not used yet toolbar.add(showWelcomeToolbarItem);
         * toolbar.addSeparator();
         */
        toolbar.add(this.importCFileToolbarItem);
        toolbar.add(exportCFileToolbarItem);
        toolbar.addSeparator();
        toolbar.add(this.startParsingToolbarItem);
        toolbar.add(this.stopParsingToolbarItem);
        toolbar.addSeparator();
        toolbar.add(this.firstStepToolbarItem);
        toolbar.add(this.prevBreakpointToolbarItem);
        toolbar.add(this.nextBreakpointToolbarItem);
        toolbar.add(this.lastStepToolbarItem);
    }

    private JAlgoWindow getJAlgoWindow() {
        for (Frame f : Frame.getFrames())
            if (f instanceof JAlgoWindow)
                return (JAlgoWindow) f;

        throw new RuntimeException("JAlgoWindow not found");
    }

    /**
     * Gets the source code.
     *
     * @return The source code.
     */
    public String getSourceCode() {
        return this.linedTextarea.getText();
    }

    /**
     * gets the Console. Used for writing into the console from other objects.
     *
     * @return the console
     */
    public JTextPane getConsole() {
        return console;
    }

    /**
     * returns the special TextareaPanel
     *
     * @return current NOLPanel
     */
    public NOLPanel getLinedTextarea() {
        return this.linedTextarea;
    }

    /**
     * returns the MemPanel, used to create FileOpen & FileSave Dialogs above it
     *
     * @return current MemPanel
     */

    public PulseMemTablePanel getMemPanel() {
        return this.memPanel;
    }

    /**
     * returns the current GUIMode (parse disabled/enabled)
     *
     * @return guiMode
     */

    public int getGUIMode() {
        return this.guiMode;
    }

    /**
     * Sets the source code.
     *
     * @param value
     *            The source code.
     */
    public void setSourceCode(String value) {
        this.linedTextarea.setText(value);
        repaintForm();
    }

    // ACTIONS
    /**
     * Enables or disables the parse/stop items.
     *
     * @param wether
     *            to enable or disable parse items. <br>
     *            <code> GUIConstants.PARSE_ENABLED</code> <br>
     *            <code> GUIConstants.PARSE_Disabled</code> <br>
     */
    public void switchParseStopEnabled(int mode) {

        this.guiMode = mode;
        modConn.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);

        if (GUIConstants.PARSE_ENABLED == mode) {
            this.stopParsingToolbarItem.setEnabled(true);
            this.startParsingToolbarItem.setEnabled(false);
            this.nextBreakpointToolbarItem.setEnabled(true);
            this.prevBreakpointToolbarItem.setEnabled(true);
            this.firstStepToolbarItem.setEnabled(true);
            this.lastStepToolbarItem.setEnabled(true);
            this.linedTextarea.setTextareaEnabled(false);
            this.breakingEnabled = true;
            this.linedTextarea.drawBreakpoints(true);

            menu.getItem(3).setEnabled(false);
            menu.getItem(4).setEnabled(true);
            menu.getItem(5).setEnabled(true);
            menu.getItem(6).setEnabled(true);
            menu.getItem(7).setEnabled(true);
            menu.getItem(8).setEnabled(true);

        } else if (GUIConstants.PARSE_DISABLED == mode) {
            this.stopParsingToolbarItem.setEnabled(false);
            this.startParsingToolbarItem.setEnabled(true);
            this.nextBreakpointToolbarItem.setEnabled(false);
            this.prevBreakpointToolbarItem.setEnabled(false);
            this.firstStepToolbarItem.setEnabled(false);
            this.lastStepToolbarItem.setEnabled(false);
            this.linedTextarea.setTextareaEnabled(true);
            this.breakingEnabled = false;
            menu.getItem(3).setEnabled(true);
            menu.getItem(4).setEnabled(false);
            menu.getItem(5).setEnabled(false);
            menu.getItem(6).setEnabled(false);
            menu.getItem(7).setEnabled(false);
            menu.getItem(8).setEnabled(false);
            console.setText("");
            this.linedTextarea.drawBreakpoints(false);
            memPanel.showEmptyTable();
            controller.restoreEditorModeSourceCode();

        }

    }

    /**
     * Switches between standart- and beamer-mode
     *
     * @param mode
     *            true: go to beamermode
     */
    public void goToBeamerMode(boolean mode) {
        if (mode) {
            this.linedTextarea.setTextFont(GUIConstants.BEAMERFONT);
            this.console.setFont(GUIConstants.BEAMERFONT);
            this.memPanel.setBeamerMode(true);
            repaintForm();
        } else {
            this.linedTextarea.setTextFont(GUIConstants.STANDARDFONT);
            this.console.setFont(GUIConstants.STANDARDFONT);
            this.memPanel.setBeamerMode(false);
            repaintForm();
        }
    }

    /**
     * Shows an error message.
     *
     * @param msg
     *            The error message to show.
     */
    public void showErrorMessage(String msg) {
        Document doc = console.getStyledDocument();
        try {
            doc.insertString(doc.getEndPosition().getOffset() - 1, msg + "\n",
                    this.redSet);
        } catch (BadLocationException e) {
            // ignore the error
        }
    }

    /**
     * Ask for Input at a scanf()-statement.
     *
     * @param line
     *            points to the line of the scaf()-statement.
     */
    public int input(int line) {

        String inputStr;
        int input = 0;
        this.linedTextarea.setCaretToLine(line);
        do {
            inputStr = JOptionPane.showInputDialog(Admin
                    .getLanguageString("gui.InputExpected_Start")
                    + line + Admin.getLanguageString("gui.InputExpected_End")); //$NON-NLS-1$

            try {
                input = Integer.parseInt(inputStr);
            } catch (NumberFormatException e) {
                showErrorMessage(Admin
                        .getLanguageString("gui.IntegerParseError_Start") //$NON-NLS-1$
                        + inputStr
                        + Admin.getLanguageString("gui.IntegerParseError_End")); //$NON-NLS-1$
                inputStr = null;
            }
        } while (inputStr == null);

        savedInput.add(input);
        return input;
    }

    /**
     * Prints the output of an printf()-statement.
     *
     * @param string
     *            to be printed out.
     * @param line
     *            pointing to the printf-statement.
     */
    public void output(String output, int line) {
        this.linedTextarea.setCaretToLine(line);
        Document doc = console.getDocument();
        try {
            String outString = Admin
                    .getLanguageString("GUIController.printf_Start")
                    + line
                    + Admin.getLanguageString("GUIController.printf_End") //$NON-NLS-1$
                    + output + "\n";

            doc.insertString(doc.getEndPosition().getOffset() - 1, outString,
                    new SimpleAttributeSet()); //$NON-NLS-1$
        } catch (BadLocationException e) {
            // ignore the error
        }

    }

    /**
     * Commits the current InlineBreakpoint object to the table, which will be
     * rebuilt then.
     */
    public void updateTableBreakpoints() {
        this.memPanel.setBreakpoints(this.lineOfBreakpoint);
    }

    /**
     * Sets the current PulseMemLine list the table gets the data from.
     *
     * @param pml
     */
    public void setTableData(List<PulsMemLine> pml) {
        this.memPanel.setTableData(pml);
    }

    public void showNextLine() {
        this.memPanel.getPulseMemTable().displayUntilNextRow();
        showLineInTextarea();
    }

    public void showPreviousLine() {
        this.memPanel.getPulseMemTable().displayUntilPreviousRow();
        showLineInTextarea();
    }

    public void showLastStep() {
        this.memPanel.getPulseMemTable().displayUntilLastRow();
        showLineInTextarea();
    }

    public void showFirstStep() {
        this.memPanel.getPulseMemTable().displayFirstRowOnly();
        showLineInTextarea();
    }

    /**
     * Clears the saved input. Used whenever saved input becomes irrelevant
     * (i.e. when importing new C00-Code)
     */

    public void clearInput() {
        this.savedInput.clear();
    }

    /**
     * prints a String msg to the conolse
     *
     * @arg String msg
     */

    public void toConsole(String msg) {
        Document doc = console.getStyledDocument();
        try {
            doc.insertString(doc.getEndPosition().getOffset() - 1, msg + "\n",
                    new SimpleAttributeSet());
        } catch (BadLocationException e) {
            // ignore the error
        }
    }

    /**
     * This is used for loading jAlgo files. The saved input is needed to
     * restore the program state at the time of saving.
     *
     * @return a List<Integer> containing the inputs the user made.
     */

    public List<Integer> getInput() {
        return this.savedInput;
    }

    /**
     * Repaints the whole form. <br>
     * Could help in case of faulty drawing.
     */
    public void repaintForm() {
        getJAlgoWindow().repaint();
    }

    /**
     * Shows the line wich is the last in table in textarea. <br>
     * Therefor it sets the caret to the line.
     */
    private void showLineInTextarea() {
        int i = 0, line = 0;
        PulsMemLine pussi = null;
        List<PulsMemLine> pml = this.memPanel.getVisPml();
        for (PulsMemLine pulsMemLine : pml) {
            if (i == this.memPanel.getPulseMemTable().getNumberOfVisibleRows()) {
                break;
            }
            line = pulsMemLine.getCodeLine();
            pussi = pulsMemLine;
            i++;
        }
        if (line >= 1)
            this.linedTextarea.setCaretToLine(line);
    }
}
