package org.jalgo.module.lambda.view;

import org.jalgo.module.lambda.ModuleConnector;
import org.jalgo.module.lambda.Constants;
import org.jalgo.module.lambda.ShortcutHandler;
import org.jalgo.module.lambda.controller.*;
import org.jalgo.module.lambda.model.EAvailability;
import org.jalgo.module.lambda.model.FormatString;
import org.jalgo.module.lambda.model.ITermHandler;
import org.jalgo.module.lambda.model.TermHandler;
import org.jalgo.main.AbstractModuleConnector.SaveStatus;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * Main GUI Class. Creates the GUI and is the observer for the model.
 *
 */
public class GUIController implements Observer {

    private Renderer renderer;
    private IController controller;
    private ModuleConnector mc;

    //GUI Komponenten
    private WelcomeScreen welcomeScreen;
    private WorkScreen workScreen;

    private JButton redoStepButton;
    private JButton undoStepButton;
    private JButton redoAllStepsButton;
    private JButton undoAllStepsButton;

    private JButton doAutoStepButton;
    private JButton doShortcutStepButton;
    private JButton doAllAutoStepsButton;

    private JButton alphaButton;
    private JButton betaButton;
    private JButton markFreeVarsButton;
    private JButton markBoundVarsButton;
    private JButton showhideBracketsButton;
    private JButton eliminateSCButton;
    private JButton matchSCButton;

    private JComponent contentPane;
    private JToolBar toolbar;

    public static final Font INPUT_FONT = new Font("Serif", 0, 28);
    public static final String TERM_FONT_HTML = "<font size=7 face='Serif'>";
    public static final Font TERM_FONT = new Font("Serif", Font.BOLD, 32);
    public static final Font TOOLBAR_FONT = new Font("Serif", Font.BOLD, 18);
    
    public static final Color goodNews = new Color(30, 159, 30);
    public static final Color badNews = new Color(185, 0, 0);

    private Iterator<FormatString> currentOutputString;
    private List<RenderElement> renderedTerm = new LinkedList<RenderElement>();

    private boolean markFreeVars = false;
    private boolean markBoundVars = false;
    private boolean hideBrackets = false;

    private String currentTerm;
    private int inpCaret;

    public GUIController(ModuleConnector mc, Controller controller) {
        this.mc = mc;
        this.controller = controller;

        //get JAlgos GUI components
        contentPane = JAlgoGUIConnector.getInstance().getModuleComponent(mc);
        toolbar = JAlgoGUIConnector.getInstance().getModuleToolbar(mc);
    }

    /**
     * intitializes and shows the welcomeScreen
     */
    public void installWelcomeScreen() {
        contentPane.removeAll();
        welcomeScreen = new WelcomeScreen(this);
        contentPane.add(welcomeScreen);
    }

    /**
     * intitializes and shows the workScreen
     */
    public void installWorkScreen() {
        contentPane.removeAll();
        workScreen = new WorkScreen();
        contentPane.add(workScreen);
        initToolbar();
        renderer = new Renderer(workScreen.getRenderLabel()); //initialize Renderer with RenderTarget
        installActionListeners();
        writeComment(Messages.getString("lambda", "ltxt.input"), true);
        updateButtonStatus();
        workScreen.getInputTextField().requestFocus();
    }

    /**
     * creates the toolbar for the WorkScreen
     */
    private void initToolbar() {
        //create the toolbar
        redoStepButton = new JButton(new ImageIcon(Messages.getResourceURL("main", "Icon.Perform_step")));
        redoStepButton.setToolTipText(Messages.getString("lambda","bts.doStep"));
        undoStepButton = new JButton(new ImageIcon(Messages.getResourceURL("main", "Icon.Undo_step")));
        undoStepButton.setToolTipText(Messages.getString("lambda","bts.undoStep"));
        redoAllStepsButton = new JButton(new ImageIcon(Messages.getResourceURL("main", "Icon.Perform_all")));
        redoAllStepsButton.setToolTipText(Messages.getString("lambda","bts.doAllSteps"));
        undoAllStepsButton = new JButton(new ImageIcon(Messages.getResourceURL("main", "Icon.Undo_all")));
        undoAllStepsButton.setToolTipText(Messages.getString("lambda","bts.undoAllSteps"));

        doAutoStepButton = new JButton(new ImageIcon(Messages.getResourceURL("lambda", "Icon.autoStep")));
        doAutoStepButton.setToolTipText(Messages.getString("lambda", "bts.doAutoStep"));
        doShortcutStepButton = new JButton(new ImageIcon(Messages.getResourceURL("lambda", "Icon.shortcutStep")));
        doShortcutStepButton.setToolTipText(Messages.getString("lambda", "bts.doShortcutStep"));
        doAllAutoStepsButton = new JButton(new ImageIcon(Messages.getResourceURL("main", "Icon.Finish_algorithm")));
        doAllAutoStepsButton.setToolTipText(Messages.getString("lambda", "bts.doAllAutoSteps"));

        alphaButton = new JButton(new ImageIcon(Messages.getResourceURL("lambda", "Icon.alpha")));
        alphaButton.setToolTipText(Messages.getString("lambda","bts.alpha"));
        betaButton = new JButton(new ImageIcon(Messages.getResourceURL("lambda", "Icon.beta")));
        betaButton.setToolTipText(Messages.getString("lambda","bts.beta"));
        markFreeVarsButton = new JButton(new ImageIcon(Messages.getResourceURL("lambda", "Icon.freeVar")));
        markFreeVarsButton.setToolTipText(Messages.getString("lambda","bts.markFreeVars"));
        markBoundVarsButton = new JButton(new ImageIcon(Messages.getResourceURL("lambda", "Icon.boundVar")));
        markBoundVarsButton.setToolTipText(Messages.getString("lambda","bts.markBoundVars"));

        showhideBracketsButton = new JButton(new ImageIcon(Messages.getResourceURL("lambda", "Icon.brackets")));
        showhideBracketsButton.setToolTipText(Messages.getString("lambda","bts.showhideBrackets"));

        eliminateSCButton = new JButton(new ImageIcon(Messages.getResourceURL("lambda", "Icon.sc_brackets_x")));
        eliminateSCButton.setToolTipText(Messages.getString("lambda","bts.shortcutEliminate"));

        matchSCButton = new JButton(new ImageIcon(Messages.getResourceURL("lambda", "Icon.sc_brackets")));
        matchSCButton.setToolTipText(Messages.getString("lambda","bts.shortcutMatch"));

        alphaButton.setFont(TOOLBAR_FONT);
        betaButton.setFont(TOOLBAR_FONT);
        showhideBracketsButton.setFont(TOOLBAR_FONT);
        matchSCButton.setFont(TOOLBAR_FONT);

        toolbar.add(undoAllStepsButton);
        toolbar.add(undoStepButton);
        toolbar.add(redoStepButton);
        toolbar.add(redoAllStepsButton);
        toolbar.addSeparator();
        toolbar.add(doAutoStepButton);
        toolbar.add(doShortcutStepButton);
        toolbar.add(doAllAutoStepsButton);
        toolbar.addSeparator();
        toolbar.add(alphaButton);
        toolbar.add(betaButton);
        toolbar.addSeparator();
        toolbar.add(markFreeVarsButton);
        toolbar.add(markBoundVarsButton);
        toolbar.add(showhideBracketsButton);
        toolbar.addSeparator();
        toolbar.add(eliminateSCButton);
        toolbar.add(matchSCButton);
    }

    /**
     * adds ActionListeners to the buttons and fields
     */
    private void installActionListeners() {

        workScreen.getInputTextField().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                int key = ke.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    inputCompleteAction();
                }
            }
        });

        workScreen.getDoneButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                inputCompleteAction();
            }
        });

        workScreen.getClearButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                workScreen.getInputTextField().setText("");
                workScreen.getInputTextField().requestFocus();
                workScreen.getOutputTextArea().setText("");
                workScreen.getRenderLabel().clear();
                writeComment(Messages.getString("lambda","ltxt.input"), true);
            }
        });

        alphaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Set<String> uuv;
                clearComment();
                uuv = controller.getAllUnusedVars();
                if (uuv.isEmpty()) {
                    writeComment(Messages.getString("lambda","ltxt.alphaNoVars"), false);
                } else if (!controller.doAlphaConversion(selectNewAlphaVar(uuv))) {
                    writeComment(Messages.getString("lambda","ltxt.alphaFailed"), false);
                } else {
                	renderer.setOpcode("\u03b1");
                }
            }
        });

        betaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                clearComment();
                EAvailability availability = controller.doBetaReduction();
                if (availability == EAvailability.NOTAVAILABLE) {
                    writeComment(Messages.getString("lambda","ltxt.betaFailed"), false);
                } else  if (availability == EAvailability.ALPHANEEDED) {
                    writeComment(Messages.getString("lambda","ltxt.alphaNeeded"), false);
                } else {
                	renderer.setOpcode("\u03b2");
                }
                if (controller.isNormalized()) {
                    writeComment("Normalform erreicht.", true);
                }
            }
        });

        redoStepButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                clearComment();
                String oc = controller.redoStep(GUIController.this);
                if (oc != null) {
                	renderer.setOpcode(oc);
                }
                if (controller.isNormalized()) {
                    writeComment("Normalform erreicht.", true);
                }
            }
        });

        undoStepButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                clearComment();
                String oc = controller.undoStep(GUIController.this);
                if (oc != null) {
                	renderer.setOpcode(oc);
                }
            }
        });

        redoAllStepsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                clearComment();
                String oc = controller.redoAllSteps(GUIController.this);
                if (oc != null) {
                	renderer.setOpcode(oc);
                }
                if (controller.isNormalized()) {
                    writeComment("Normalform erreicht.", true);
                }
            }
        });

        undoAllStepsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                clearComment();
                String oc = controller.undoAllSteps(GUIController.this);
                if (oc != null) {
                	renderer.setOpcode(oc);
                }
            }
        });

        doAutoStepButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                clearComment();
                String oc = controller.doStep();
                if (oc != null) {
                	renderer.setOpcode(oc);
                }
                if (controller.isNormalized()) {
                    writeComment("Normalform erreicht.", true);
                }
            }
        });
        
        doShortcutStepButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                clearComment();
                String oc = controller.doShortcutStep();
                if (oc != null) {
                	renderer.setOpcode(oc);
                }
                if (controller.isNormalized()) {
                    writeComment("Normalform erreicht.", true);
                }
            }
        });

        doAllAutoStepsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                clearComment();
                String oc = controller.doAllSteps();
                if (oc != null) {
                	renderer.setOpcode(oc);
                }
                if (controller.isNormalized()) {
                    writeComment("Normalform erreicht.", true);
                }
            }
        });

        markFreeVarsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                markFreeVars = !markFreeVars;
                
                List<String> l = controller.getFreeVars();

                for(RenderElement re : renderedTerm) {
                    if(l.contains(re.getTermPosition())) {
                    	re.markAsFreeVar(markFreeVars);
                    	
                    } else
                    	re.markAsFreeVar(false);
                    re.repaint();
                }
            }
        });

        markBoundVarsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                markBoundVars = !markBoundVars;

                List<String> l = controller.getBoundVars();

                for(RenderElement re : renderedTerm) {
                    if(l.contains(re.getTermPosition())) {
                    	re.markAsBoundVar(markBoundVars);
                    	
                    } else
                    	re.markAsBoundVar(false);
                    
                    re.repaint();
                }
            }
        });

        workScreen.getRenderLabel().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
            	markBoundVars = false;
            	markFreeVars = false;
            	
                for(RenderElement re : renderedTerm) {
                    re.setSelected(false);
                    re.markAsBoundVar(markBoundVars);
                    re.markAsFreeVar(markFreeVars);
                    re.repaint();
                }
                controller.selectTerm("");
            }
        });

        showhideBracketsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                hideBrackets = !hideBrackets;

                for(RenderElement re : renderedTerm) {
                    re.hideBrackets(hideBrackets);
                    re.repaint();
                }
            }
        });

        eliminateSCButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (controller.eliminateAllShortcuts()) {
                	renderer.setOpcode("=");
                }
            }
        });

        matchSCButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                    if (controller.matchShortcut()) {
                    	renderer.setOpcode("=");
                    }
                }
        });
        
        workScreen.getShortcutListField().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (ShortcutHandler.getInstance().isShortcutPredefined((String) workScreen.getShortcutListField().getSelectedValue()))
					workScreen.getDeleteButton().setEnabled(false);
				else
					workScreen.getDeleteButton().setEnabled(true);				
			}
        });

        workScreen.getInsertButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                insertShortcut();
            }
        });

        workScreen.getShortcutListField().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if ((me.getButton() == MouseEvent.BUTTON1)
                        && (me.getClickCount() == 2)) {
                    insertShortcut();
                }
            }
        });

        workScreen.getDeleteButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                deleteShortcut();
            }
        });

        workScreen.getNewSCButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                createShortcut();
            }
        });

        workScreen.getInputSCName().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                int key = ke.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    inputSCAction();
                }
            }
        });

        workScreen.getInputSCOKButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                inputSCAction();
            }
        });

        workScreen.getInputSCCancelButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                workScreen.changeVisibility();
            }
        });

    }

    /**
     * Highlights the position where the mousecursor is over.
     *
     * @param pos Position to be highlighted.
     */
    public void highlightPosition(String pos) {
        for(RenderElement re : renderedTerm) {
            if(re.getTermPosition().startsWith(pos)) {
                re.setHighlighted(true);
            }
            else
                re.setHighlighted(false);

            re.repaint();
        }
    }

    /**
     * voids the highlightning of the term when the mousecursor isn't over the renderLabel anymore.
     */
    public void unhighlight() {
        for(RenderElement re : renderedTerm) {
            re.setHighlighted(false);
            re.repaint();
        }
    }

    /**
     * Selects/marks a position when a mousebutton is clicked over the renderLabel and hands pos over to the Controller.
     *
     * @param pos Position to be selected
     */
    public void selectPosition(String pos) {
        for(RenderElement re : renderedTerm) {
            if(re.getTermPosition().startsWith(pos)) {
                re.setSelected(true);
            }
            else
                re.setSelected(false);

            re.repaint();
        }
        controller.selectTerm(pos);
    }

    /**
     * implements the Observer-Pattern
     *
     * @param o the Model or Controller which caused the call
     * @param arg unused
     */
    public void update(Observable o, Object arg) {
    	if (o instanceof TermHistory) {
            setOutputTextArea(((TermHistory)o).getCurrentTermSteps());
            
            if (arg != null)
            	setInputTextField((String)arg);

        } else if (o instanceof TermHandler) {
            currentOutputString = ((ITermHandler)o).getFormatString(true);

            //clear RenderTarget
            workScreen.getRenderLabel().clear();

            //render new term
            renderedTerm = renderer.drawTerm(currentOutputString, this);
            
            markFreeVars = false;
            markBoundVars = false;

        } else {
            writeComment("Unknown Observable!", false);
        }
    }

    /**
     * Sets the content of the input text field.
     * 
     * @param s string
     */
    public void setInputTextField(String s) {
    	workScreen.getInputTextField().setText(s);
    }
    
    /**
     * Sets the content of the output text area.
     * 
     * @param list list of strings
     */
    public void setOutputTextArea(List<String> list) {
        workScreen.getOutputTextArea().setText(TERM_FONT_HTML + listToString((List<String>)list) + "</font>");
        updateButtonStatus();
    }
    
    /**
     * Refreshes the shortcut list.
     */
    public void refreshShortcutList() {
    	workScreen.refreshListModel();
    }

    /**
     * Writes a comment to the commentLine Label.
     *
     * @param comment the Commentstring
     * @param ok success or error comment
     */
    public void writeComment(String comment, boolean ok) {
        if (ok) {
            workScreen.getCommentLine().setForeground(goodNews);
        } else {
            workScreen.getCommentLine().setForeground(badNews);
        }
        workScreen.getCommentLine().setText(comment);
    }

    public void clearComment() {
        workScreen.getCommentLine().setText(" ");
    }

    /**
     * Generates formatted outputString for outputTextField
     * from history of mementos.
     *
     * @param List of Strings
     */
    public String listToString(List<String> l) {
        String s, res = "";
        if (l != null) {
            Iterator<String> i = l.iterator();
            s = i.next();
            s = s.replaceAll("<", "&lt;");
            s = s.replaceAll(">", "&gt;");
            res = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + s + "<br>";
            while (i.hasNext()) {
                s = i.next();
                s = s.replaceAll("<", "&lt;");
                s = s.replaceAll(">", "&gt;");
                if (s.charAt(0) == '=')
                    res = res + "<b>" + s.charAt(0) + "</b>" + " " + s.substring(1) + "<br>";
                else
                    res = res + "<b>" + Constants.ARROW_R + "</b>" + "<sub>" + s.charAt(0) + "</sub>" + " " + s.substring(1) + "<br>";
            }
        }
        return res;
    }

    /**
     * helperfunction for ShortcutPanel to insert a shortcut
     *
     * @param the "<shortcut>" to be insert
     */
    public void insertShortcut() {

        if (workScreen.getShortcutListField().getSelectedValue() != null) {
            String s = (String) workScreen.getShortcutListField().getSelectedValue();
            if(s != null) {
                String tft = workScreen.getInputTextField().getText();
                inpCaret = workScreen.getInputTextField().getSelectionStart();
                String part1 = tft.substring(0, inpCaret);
                String part2 = tft.substring(workScreen.getInputTextField().getSelectionEnd());
                workScreen.getInputTextField().setText(part1 + s + part2);
                workScreen.getInputTextField().setCaretPosition(inpCaret + s.length());
                workScreen.getInputTextField().requestFocus();
            }
        }
    }

    /**
     * helperfunction for ShortcutPanel to delete a shortcut from the shortcutList
     *
     */
    public void deleteShortcut() {
        if (workScreen.getShortcutListField().getSelectedValue() != null) {
            String s = (String) workScreen.getShortcutListField().getSelectedValue();
            if (s != null && !ShortcutHandler.getInstance().isShortcutPredefined(s)) {
                ShortcutHandler.getInstance().removeShortcut(s);
                workScreen.refreshListModel();
            }
        }
    }

    /**
     * helperfunction for ShortcutPanel to create a new shortcut for the shortcutList
     *
     */
    public void createShortcut() {
        if (workScreen.getInputTextField().getText().replaceAll(" ", "").equals("")) {
            writeComment(Messages.getString("lambda","ltxt.inputSCTerm"), false);
        } else {
            currentTerm = workScreen.getInputTextField().getText().replaceAll(" ", "");
            workScreen.getInputTextField().setText(currentTerm);

            if (ShortcutHandler.getInstance().getShortcutRepresentation(currentTerm) != null) {
                writeComment(Messages.getString("lambda","ltxt.scTermExists") + " ("
                        + ShortcutHandler.getInstance().getShortcutRepresentation(currentTerm) + ")",false);
            } else {
                LambdaException le = controller.validateInputString(currentTerm);
                if (le != null) {
                    writeComment("Error: " + le.getMessage(), false);
                    workScreen.getInputTextField().setCaretPosition(le.getIndex()-1);
                    workScreen.getInputTextField().moveCaretPosition(le.getIndex());
                } else {
                    writeComment(Messages.getString("lambda","ltxt.termCorrect"), true);
                    clearComment();
                    workScreen.changeVisibility();
                    workScreen.getInputSCName().setText("<name>");
                    workScreen.getInputSCName().setCaretPosition(1);
                    workScreen.getInputSCName().moveCaretPosition(5);
                    workScreen.getInputSCName().requestFocus();
                }
            }
        }
    }

    private void inputSCAction() {
        String name = workScreen.getInputSCName().getText().replaceAll(" ", "");
        if (!name.startsWith("<") || !name.endsWith(">")) {
            writeComment(Messages.getString("lambda","ltxt.scNameFault"), false);
            workScreen.getInputSCName().requestFocus();
        } else {
            if (ShortcutHandler.getInstance().getShortcutList().contains(name)) {
                writeComment(Messages.getString("lambda","ltxt.scNameExists"), false);
                workScreen.getInputSCName().requestFocus();
            } else {
            	try {
					ShortcutHandler.getInstance().addShortcut(name, currentTerm, false);
	                workScreen.changeVisibility();
	                workScreen.refreshListModel();
	                workScreen.getInputTextField().setText(name);
	                writeComment(Messages.getString("lambda", "ltxt.input"), true);
	                workScreen.getInputTextField().requestFocus();
	                mc.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
				} catch (LambdaException e) {
					e.printStackTrace();
				}
            }
        }
    }

    /**
     * opens a dialog to select a new name for the Variable which should be alphaconverted
     *
     * @param unusedVars a Set of Strings containing all possible choices
     * @return returns the choosen name for the Alphaconversion
     */
    public String selectNewAlphaVar(Set<String> unusedVars) {
        String[] unused = unusedVars.toArray(new String[0]);
        return unused[JOptionPane.showOptionDialog(this.contentPane,
                Messages.getString("lambda","dia.alphaMessage"),
                Messages.getString("lambda","dia.alphaTitle"),
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, unused, unused[0])];
    }

    /**
     * action after the input is completed
     */
    public void inputCompleteAction() {
        currentTerm = workScreen.getInputTextField().getText();
        currentTerm = currentTerm.replaceAll(" ", "");
        workScreen.getInputTextField().setText(currentTerm);
        if (currentTerm.length() == 0) return;
        clearComment();
        renderer.setOpcode("");
        LambdaException le = controller.processInputString(currentTerm, GUIController.this);

        workScreen.getInputTextField().requestFocus();
        if(le == null) {
            writeComment(Messages.getString("lambda","ltxt.termCorrect"), true);
            mc.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
        }
        else {
            writeComment("Error: " + le.getMessage(), false);
            workScreen.getInputTextField().setCaretPosition(le.getIndex());
            workScreen.getInputTextField().moveCaretPosition(le.getIndex()+1);
            workScreen.getOutputTextArea().setText("");
            workScreen.getRenderLabel().clear();
        }
    }

    /**
     * enables/disables the redo/undo buttons when redo/undo is(n't) possible
     */
    private void updateButtonStatus() {
        redoStepButton.setEnabled(controller.isRedoStepPossible());
        undoStepButton.setEnabled(controller.isUndoStepPossible());
        redoAllStepsButton.setEnabled(controller.isRedoStepPossible());
        undoAllStepsButton.setEnabled(controller.isUndoStepPossible());
    }

}
