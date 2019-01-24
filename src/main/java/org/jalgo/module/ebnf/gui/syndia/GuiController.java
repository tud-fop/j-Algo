package org.jalgo.module.ebnf.gui.syndia;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;

import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.gui.components.JToolbarButton;
import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.ModuleConnector;
import org.jalgo.module.ebnf.controller.syndia.SynDiaController;
import org.jalgo.module.ebnf.gui.syndia.display.SynDiaPanel;
import org.jalgo.module.ebnf.model.syndia.ElementNotFoundException;
import org.jalgo.module.ebnf.model.syndia.NullElem;
import org.jalgo.module.ebnf.model.syndia.SynDiaElem;
import org.jalgo.module.ebnf.model.syndia.SynDiaSystem;
import org.jalgo.module.ebnf.model.syndia.SyntaxDiagram;
import org.jalgo.module.ebnf.model.syndia.TerminalSymbol;
import org.jalgo.module.ebnf.model.syndia.Variable;
import org.jalgo.module.ebnf.renderer.EditorRenderer;
import org.jalgo.module.ebnf.renderer.RenderValues;
import org.jalgo.module.ebnf.renderer.elements.RenderBase;
import org.jalgo.module.ebnf.renderer.elements.RenderElement;
import org.jalgo.module.ebnf.renderer.elements.RenderName;
import org.jalgo.module.ebnf.renderer.elements.RenderNullElem;
import org.jalgo.module.ebnf.renderer.elements.RenderSplit;
import org.jalgo.module.ebnf.renderer.elements.RenderTerminal;
import org.jalgo.module.ebnf.renderer.elements.RenderTiledBranch;
import org.jalgo.module.ebnf.renderer.elements.RenderTiledRepetition;
import org.jalgo.module.ebnf.renderer.elements.RenderVariable;

/**
 * The <code>GuiController</code> of the syntax diagram input. Administrates
 * all GUI elements. Its <code>DrawPanel</code> is observer of the syntax
 * diagram model to update the syntax diagram view.
 * 
 * @author Michael Thiele
 * 
 */
public class GuiController extends AbstractSDGuiController {

	private SynDiaController synDiaController;

	private ModuleConnector connector;

	private Map<RenderElement, SynDiaElem> renderMap;

	private RenderTiledBranch renderTiledBranch;

	private RenderTiledRepetition renderTiledRepetition;

	private JPanel rootPane;

	private SynDiaMouseListener synDiaMouseListener;

	private ChangeColorToGreenListener changeColorToGreenListener;

	private ChangeColorToRedListener changeColorToRedListener;

	private ChangeFillColorToListener changeFillColorToListener;

	private ChangeBoldListener changeBoldListener;

	private DeleteElemListener deleteElemListener;

	private DeleteDiagramListener deleteDiagramListener;

	private ChangeEditModeMenu changeEditModeMenu;

	private EditorRenderer synDiaRenderer;

	private JDialog dialogEdit;

	private JDialog dialogNew;

	private JDialog dialogSynDiaName;

	private JTextField newTextField;

	private JTextField editTextField;

	private JTextField synDiaNameTextField;

	private JPanel panel;

	private JCheckBox checkBox;

	private ItemListener checkBoxListener;

	private String oldLabel;

	private boolean setStartDiagram;

	private JButton okButton;

	private SynDiaElem currentSynDiaElem;

	private Component currentRenderElement;

	private SyntaxDiagram currentSyntaxDiagram;

	private int mode;

	private JDialog dialogNoStartDiagram;

	private JPanel popupIncomplete;

	private Cursor cursorBranch;

	private Cursor cursorRepetition;

	private Cursor cursorVariable;

	private Cursor cursorTerminal;

	private Cursor cursorDelete;

	private Cursor cursorEdit;

	private Cursor cursorBranchRight;

	private Cursor cursorRepetitionRight;

	private AbstractAction undoAction;

	private AbstractAction redoAction;

	private JDialog dialog;

	private int topOfDrawPanel, leftOfDrawPanel;

	/**
	 * Initializes the textFields, cursors, panels and mouse listener.
	 * 
	 * @param synDiaController
	 *            interface to the syntax diagram controller; is needed to
	 *            change syntax diagram model
	 * @param connector
	 *            the jalgo module connector
	 * @param rootPane
	 *            the jalgo main panel
	 */
	public GuiController(SynDiaController synDiaController,
			ModuleConnector connector, JPanel rootPane) {
		this.synDiaController = synDiaController;
		this.connector = connector;
		this.rootPane = rootPane;
		init();
	}

	/**
	 * Is called by the constructor.
	 * 
	 */
	private void init() {

		// set cursor to "wait"
		rootPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		initToolbar();

		ActionListener changeLabel = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeLabelOfElement();
			}
		};

		Font tahoma = new Font("Tahoma", Font.PLAIN, 15);
		// ok-Button
		okButton = new JButton("OK");
		okButton.addActionListener(new OkActionListener(this));
		okButton.setSize(25, 25);
		okButton.setMargin(new Insets(1, 1, 1, 1));
		okButton.setVisible(true);
		okButton.setFocusable(false);

		// dialogEdit: for changes in label names
		dialogEdit = new JDialog((Frame) rootPane.getTopLevelAncestor(), true);
		dialogEdit.setUndecorated(true);
		dialogEdit.setFocusable(false);
		JPanel panelEdit = new JPanel();
		panelEdit.setLayout(new BoxLayout(panelEdit, BoxLayout.X_AXIS));
		editTextField = new JTextField();
		editTextField.setFont(tahoma);
		editTextField.addActionListener(changeLabel);
		editTextField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					dialogEdit.setVisible(false);
				}
			}
		});
		panelEdit.add(editTextField);
		panelEdit.add(okButton);
		dialogEdit.setContentPane(panelEdit);

		// ok-Button
		okButton = new JButton("OK");
		okButton.addActionListener(new OkActionListener(this));
		okButton.setSize(25, 25);
		okButton.setMargin(new Insets(1, 1, 1, 1));
		okButton.setVisible(true);
		okButton.setFocusable(false);

		// newTextField: for input of new terminal symbol or variable names
		dialogNew = new JDialog((Frame) rootPane.getTopLevelAncestor(), true);
		dialogNew.setUndecorated(true);
		dialogNew.setFocusable(false);
		JPanel panelNew = new JPanel();
		panelNew.setLayout(new BoxLayout(panelNew, BoxLayout.X_AXIS));
		newTextField = new JTextField();
		newTextField.setFont(tahoma);
		newTextField.addActionListener(changeLabel);
		newTextField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					dialogNew.setVisible(false);
				}
			}
		});
		panelNew.add(newTextField);
		panelNew.add(okButton);
		dialogNew.setContentPane(panelNew);

		// ok-Button
		okButton = new JButton("OK");
		okButton.addActionListener(new OkActionListener(this));
		okButton.setSize(25, 25);
		okButton.setMargin(new Insets(1, 1, 1, 1));
		okButton.setVisible(true);
		okButton.setFocusable(false);

		// synDiaNameTextField
		dialogSynDiaName = new JDialog((Frame) rootPane.getTopLevelAncestor(),
				true);
		dialogSynDiaName.setUndecorated(true);
		// dialogSynDiaName.setUndecorated(true);
		synDiaNameTextField = new JTextField();
		synDiaNameTextField.setFont(tahoma);
		synDiaNameTextField.addActionListener(changeLabel);
		synDiaNameTextField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					dialogSynDiaName.setVisible(false);
				}
			}
		});
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		int width = 100;
		int height = 25;
		int border = 15;
		editTextField.setSize(width, height);
		panel.setBackground(Color.WHITE);
		panel.setVisible(true);
		JLabel startDiagram = new JLabel("Startdiagramm");
		startDiagram.setBackground(Color.WHITE);
		startDiagram.setSize(width, height);
		startDiagram.setFont(new Font("Courier New", Font.PLAIN, 12));
		startDiagram.repaint();
		startDiagram.setVisible(true);
		checkBox = new JCheckBox();
		checkBox.setEnabled(true);
		checkBox.setBackground(Color.WHITE);
		checkBox.setSize(height, height);
		checkBox.repaint();
		checkBox.setVisible(true);
		checkBoxListener = new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				setStartDiagram = ((JCheckBox)arg0.getSource()).isSelected();
			}
		};

		JPanel panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
		panel1.setBackground(Color.WHITE);
		panel1.add(synDiaNameTextField);
		panel1.add(okButton);

		JPanel panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
		panel2.setBackground(Color.WHITE);
		panel2.add(startDiagram);
		panel2.add(Box.createRigidArea(new Dimension(10, 0)));
		panel2.add(checkBox);

		panel.add(panel1);
		panel.add(panel2);

		panel.setSize(width + height + 2 * border, height * 2
				+ (int) (border * 1.5));
		panel.validate();
		panel.repaint();
		dialogSynDiaName.setSize(panel.getSize());
		dialogSynDiaName.setContentPane(panel);

		// ---
		renderTiledBranch = new RenderTiledBranch();
		renderTiledRepetition = new RenderTiledRepetition();

		// renderer
		synDiaRenderer = new EditorRenderer();

		// scrollPane (from: DisplayController)
		drawScrollPane = new JScrollPane();
		drawPanel = new EditorDrawPanel(synDiaController.getSynDiaSystem(),
				this, synDiaRenderer);
		controlPanel = new EditorControlPanel(this);

		// setting
		rootPane.setBackground(Color.WHITE);
		drawScrollPane.setBackground(Color.WHITE);
		drawScrollPane.setViewportBorder(javax.swing.BorderFactory
				.createLoweredBevelBorder());
		drawScrollPane.setViewportView(drawPanel);
		drawScrollPane.setPreferredSize(new Dimension(Short.MAX_VALUE, rootPane
				.getHeight()
				- controlPanel.getPreferredSize().height));
		drawScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, Messages.getString("ebnf", "Border_Draw"),
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Tahoma", 0, 12)));

		controlPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, controlPanel
				.getPreferredSize().height));

		rootPane.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				drawScrollPane.setPreferredSize(new Dimension(Short.MAX_VALUE,
						rootPane.getHeight()
								- controlPanel.getPreferredSize().height));
			}
		});

		// layout
		rootPane.setLayout(new BoxLayout(rootPane, BoxLayout.Y_AXIS));
		rootPane.add(controlPanel);
		rootPane.add(drawScrollPane);
		rootPane.validate();

		// listener
		changeColorToGreenListener = new ChangeColorToGreenListener();
		changeColorToRedListener = new ChangeColorToRedListener();
		changeFillColorToListener = new ChangeFillColorToListener();
		deleteElemListener = new DeleteElemListener(this);
		changeBoldListener = new ChangeBoldListener();
		deleteDiagramListener = new DeleteDiagramListener(this);

		synDiaMouseListener = new SynDiaMouseListener(this, renderMap);
		((EditorDrawPanel) drawPanel).setMouseListener(synDiaMouseListener);
		changeEditModeMenu = new ChangeEditModeMenu(this);
		drawPanel.addMouseListener(synDiaMouseListener);

		// dialog "do not delete start diagram"
		Font font = new Font("Tahoma", Font.PLAIN, 13);
		dialogNoStartDiagram = new JDialog((Frame) rootPane
				.getTopLevelAncestor(), true);
		dialogNoStartDiagram.setVisible(false);
		dialogNoStartDiagram.setUndecorated(true);
		JPanel popupNote = new JPanel();
		popupNote.setLayout(new BoxLayout(popupNote, BoxLayout.Y_AXIS));
		popupNote.setBackground(Color.WHITE);
		popupNote.setBorder(BorderFactory.createTitledBorder(null, Messages
				.getString("ebnf", "SynDiaEditor.PopupNote_Border"),
				TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, font, Color.RED));
		JLabel doNotDeleteStartDiagram = new JLabel(Messages.getString("ebnf",
				"SynDiaEditor.PopupNote_DoNotDeleteStartDiagram"));
		doNotDeleteStartDiagram.setBackground(Color.WHITE);
		doNotDeleteStartDiagram.setFont(font);
		doNotDeleteStartDiagram.setAlignmentX(Component.CENTER_ALIGNMENT);
		FontMetrics fm = doNotDeleteStartDiagram.getFontMetrics(font);
		JButton okButton = new JButton("OK");
		okButton.setSize(25, 25);
		okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialogNoStartDiagram.setVisible(false);
			}
		});
		popupNote.add(doNotDeleteStartDiagram);
		popupNote.add(Box.createRigidArea(new Dimension(0, 10)));
		popupNote.add(okButton);
		popupNote.validate();
		popupNote.repaint();
		popupNote.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					dialogNoStartDiagram.setVisible(false);
				}
			}
		});
		dialogNoStartDiagram.setContentPane(popupNote);
		dialogNoStartDiagram.setSize(fm.stringWidth(doNotDeleteStartDiagram
				.getText()) / 3, 100);
		dialogNoStartDiagram.setLocationRelativeTo(rootPane);

		// popup incomplete
		popupIncomplete = new JPanel();
		popupIncomplete.setVisible(false);
		popupIncomplete.setLayout(new BoxLayout(popupIncomplete,
				BoxLayout.Y_AXIS));
		popupIncomplete.setBackground(Color.WHITE);
		popupIncomplete.setBorder(BorderFactory.createTitledBorder(null,
				Messages.getString("ebnf", "SynDiaEditor.PopupNote_Border"),
				TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, font, Color.RED));

		initCursor();

		// set cursor back
		rootPane.setCursor(Cursor.getDefaultCursor());

	}

	private void initCursor() {
		// cursor
		cursorBranch = drawPanel.getToolkit().createCustomCursor(
				new ImageIcon(Messages.getResourceURL("ebnf", "Cursor_Branch"))
						.getImage(), new Point(1, 2), "Cursor");
		cursorRepetition = drawPanel.getToolkit().createCustomCursor(
				new ImageIcon(Messages.getResourceURL("ebnf",
						"Cursor_Repetition")).getImage(), new Point(1, 2),
				"Cursor");
		cursorVariable = drawPanel.getToolkit().createCustomCursor(
				new ImageIcon(Messages
						.getResourceURL("ebnf", "Cursor_Variable")).getImage(),
				new Point(1, 2), "Cursor");
		cursorTerminal = drawPanel.getToolkit().createCustomCursor(
				new ImageIcon(Messages
						.getResourceURL("ebnf", "Cursor_Terminal")).getImage(),
				new Point(1, 2), "Cursor");
		cursorDelete = drawPanel.getToolkit().createCustomCursor(
				new ImageIcon(Messages.getResourceURL("ebnf", "Cursor_Delete"))
						.getImage(), new Point(1, 2), "Cursor");
		cursorEdit = drawPanel.getToolkit().createCustomCursor(
				new ImageIcon(Messages.getResourceURL("ebnf", "Cursor_Edit"))
						.getImage(), new Point(1, 2), "Cursor");
		cursorBranchRight = drawPanel.getToolkit().createCustomCursor(
				new ImageIcon(Messages.getResourceURL("ebnf",
						"Cursor_BranchRight")).getImage(), new Point(1, 2),
				"Cursor");
		cursorRepetitionRight = drawPanel.getToolkit().createCustomCursor(
				new ImageIcon(Messages.getResourceURL("ebnf",
						"Cursor_RepetitionRight")).getImage(), new Point(1, 2),
				"Cursor");
		drawPanel.setCursor(cursorEdit);
	}

	private void initToolbar() {
		JToolBar toolBar = JAlgoGUIConnector.getInstance().getModuleToolbar(
				connector);
		toolBar.addSeparator();
		undoAction = new UndoAction(this);
		toolBar.add(createToolbarButton(undoAction));
		redoAction = new RedoAction(this);
		toolBar.add(createToolbarButton(redoAction));
		// redoAction.setEnabled(false);
		// undoAction.setEnabled(false);
		toolBar.validate();
	}

	/**
	 * Creates a <code>JButton</code> object without border and text, which
	 * can be used in <code>JToolBar</code>s
	 * 
	 * @return a <code>JButton</code> instance with the given
	 *         <code>Action</code>
	 */
	private JButton createToolbarButton(Action a) {
		JToolbarButton button = new JToolbarButton((Icon) a
				.getValue(Action.SMALL_ICON), null, null);
		button.setAction(a);
		button.setText("");
		return button;
	}

	/**
	 * Switches to syntax diagram view.
	 * 
	 */
	public void switchToSynDiaView() {
		if (synDiaController.getSynDiaSystem().isComplete()) {
			JToolBar toolBar = JAlgoGUIConnector.getInstance()
					.getModuleToolbar(connector);
			toolBar.removeAll();
			toolBar.validate();
			toolBar.repaint();
			synDiaController.switchToSynDiaView();
		} else {
			ShowIncompleteSystem();
		}
	}

	/**
	 * Sets a new render map.
	 * 
	 * @param renderMap
	 *            the new render map
	 */
	public void setRenderMap(Map<RenderElement, SynDiaElem> renderMap) {
		this.renderMap = renderMap;
	}

	/**
	 * Sets a given cursor type.
	 * 
	 * @param cursor
	 *            name of the new cursor
	 */
	public void setCursor(String cursor) {
		if (cursor.toLowerCase().equals("branch")) {
			drawPanel.setCursor(cursorBranch);
		} else if (cursor.toLowerCase().equals("repetition")) {
			drawPanel.setCursor(cursorRepetition);
		} else if (cursor.toLowerCase().equals("variable")) {
			drawPanel.setCursor(cursorVariable);
		} else if (cursor.toLowerCase().equals("terminal")) {
			drawPanel.setCursor(cursorTerminal);
		} else if (cursor.toLowerCase().equals("delete")) {
			drawPanel.setCursor(cursorDelete);
		} else if (cursor.toLowerCase().equals("edit")) {
			drawPanel.setCursor(cursorEdit);
		} else if (cursor.toLowerCase().equals("branchright")) {
			drawPanel.setCursor(cursorBranchRight);
		} else if (cursor.toLowerCase().equals("repetitionright")) {
			drawPanel.setCursor(cursorRepetitionRight);
		}
	}

	/**
	 * Shows the popup menu, where you can choose the edit mode.
	 * 
	 * @param x
	 *            position where the contect menu should pop up
	 * @param y
	 *            position where the contect menu should pop up
	 */
	public void showChangeEditModeMenu(int x, int y) {
		changeEditModeMenu.show(drawPanel, x, y);
	}

	/**
	 * Returns the syntax diagram controller.
	 * 
	 * @return the syntax diagram controller
	 */
	public SynDiaController getSynDiaController() {
		return synDiaController;
	}

	/**
	 * Returns the renderMap of all syntax diagrams.
	 * 
	 * @return the renderMap of all syntax diagrams
	 */
	public Map<RenderElement, SynDiaElem> getRenderMap() {
		return renderMap;
	}

	/**
	 * NullElems loose their color listener and terminal symbols and variables
	 * get a new color listener.
	 * 
	 */
	public void setChangeElemListener() {
		for (RenderElement re : renderMap.keySet()) {
			if (re instanceof RenderNullElem) {
				re.removeMouseListener(changeColorToGreenListener);
			} else if (re instanceof RenderVariable
					|| re instanceof RenderTerminal) {
				re.addMouseListener(changeFillColorToListener);
			} else if (re instanceof RenderName) {
				re.addMouseListener(changeBoldListener);
			}
		}
	}

	/**
	 * Sets all changes from <code>setChangeElemListener()</code> back.
	 * 
	 */
	public void setChangeElemListenerBack() {
		for (RenderElement re : renderMap.keySet()) {
			if (re instanceof RenderNullElem) {
				re.addMouseListener(changeColorToGreenListener);
			} else if (re instanceof RenderVariable
					|| re instanceof RenderTerminal) {
				re.removeMouseListener(changeFillColorToListener);
			} else if (re instanceof RenderName) {
				re.removeMouseListener(changeBoldListener);
			}
		}
	}

	/**
	 * If mode is delete, remove listener for <code>NullElem</code>s and add
	 * <code>DeleteElemListener</code> for all destroyable elements.
	 * 
	 */
	public void setDeleteElemListener() {
		// set NullElems to background, so that lines can be fully selected
		for (RenderElement re : renderMap.keySet()) {
			// remove listener for NullElems - they are not needed in delete
			// mode
			if (re instanceof RenderNullElem) {
				re.removeMouseListener(changeColorToGreenListener);
				re.getParent().setComponentZOrder(re,
						re.getParent().getComponentCount() - 1);
			} else if (!(re instanceof RenderBase)) {
				re.addMouseListener(deleteElemListener);
				re.addMouseMotionListener(deleteElemListener);
			}
		}
		// search for empty diagrams
		for (SynDiaPanel sdp : drawPanel.getSynDiaPanelList()) {
			// 3 objects in empty diagram: name, base, 1 NullElem
			if (sdp.getComponentCount() == 3) {
				sdp.addMouseListener(deleteDiagramListener);
				for (Component c : sdp.getComponents()) {
					c.addMouseListener(deleteDiagramListener);
				}
			}
		}
	}

	/**
	 * Sets all listener back if there is a change from delete mode to another
	 * mode.
	 * 
	 */
	public void setDeleteElemListenerBack() {
		// set NullElems to foreground, so that they can be fully selected
		for (RenderElement re : renderMap.keySet()) {
			if (!(re instanceof RenderBase) || !(re instanceof RenderNullElem)) {
				re.removeMouseListener(deleteElemListener);
				re.removeMouseMotionListener(deleteElemListener);
			}
			// add listener for NullElems - they are needed now
			if (re instanceof RenderNullElem) {
				re.addMouseListener(changeColorToGreenListener);
				re.getParent().setComponentZOrder(re, 0);
			}
			// if element is instance of RenderSplit it could be highlighted
			if (re instanceof RenderSplit) {
				((RenderSplit) re).setTopHighlight(false,
						RenderElement.STANDARD_COLOR);
				((RenderSplit) re).setBottomHighlight(false,
						RenderElement.STANDARD_COLOR);
			}
			// if element is instance of RenderTerminal or RenderVariable it
			// could be highlighted if it is part of an arm of a repetition
			// or branch that is highlighted
			else if (re instanceof RenderTerminal
					|| re instanceof RenderVariable) {
				re.setColor(RenderElement.STANDARD_FILL_COLOR);
			}
		}
		for (SynDiaPanel sdp : drawPanel.getSynDiaPanelList()) {
			sdp.removeMouseListener(deleteDiagramListener);
			for (Component c : sdp.getComponents()) {
				c.removeMouseListener(deleteDiagramListener);
			}
		}
	}

	/**
	 * Sets a new ColorListener for NullElems that are not on the same line.
	 * 
	 * @param leftNullElem
	 *            left side of the branch - the right side has to be at the same
	 *            line
	 */
	public void setNullElemColorListener(NullElem leftNullElem) {
		for (RenderElement re : renderMap.keySet()) {
			if (re instanceof RenderNullElem) {
				if (((NullElem) renderMap.get(re)).getLine() != leftNullElem
						.getLine()) {
					re.removeMouseListener(changeColorToGreenListener);
					re.addMouseListener(changeColorToRedListener);
				}
			}
		}
	}

	/**
	 * Sets all ColorListener for NullElems back.
	 * 
	 */
	public void setNullElemColorListenerBack() {
		for (RenderElement re : renderMap.keySet()) {
			if (re instanceof RenderNullElem) {
				re.removeMouseListener(changeColorToRedListener);
				// there are some listeners on green - remove them
				re.removeMouseListener(changeColorToGreenListener);
				re.addMouseListener(changeColorToGreenListener);
			}
		}
	}

	/**
	 * Shows a popup window which says, that it is not allowed to delete the
	 * start diagram.
	 */
	public void ShowDoNotDeleteStartDiagram() {
		dialogNoStartDiagram.setVisible(true);
	}

	private void ShowIncompleteSystem() {
		dialog = new JDialog((Frame) rootPane.getTopLevelAncestor(), true);
		dialog.setVisible(false);
		dialog.setBackground(Color.WHITE);
		dialog.setLayout(new BoxLayout(dialog.getContentPane(),
				BoxLayout.Y_AXIS));
		dialog.setUndecorated(true);
		Font font = new Font("Tahoma", Font.PLAIN, 13);
		JLabel incomplete = new JLabel(Messages.getString("ebnf",
				"SynDiaEditor.PopupIncomplete_Top"));
		incomplete.setBackground(Color.WHITE);
		incomplete.setFont(font);
		incomplete.setAlignmentX(Component.CENTER_ALIGNMENT);
		popupIncomplete.add(incomplete);
		popupIncomplete.add(Box.createRigidArea(new Dimension(0, 15)));
		FontMetrics fm = incomplete.getFontMetrics(font);
		popupIncomplete.setSize(fm.stringWidth(incomplete.getText()) / 3, 100);
		JButton okButton = new JButton("OK");
		okButton.setSize(25, 25);
		okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				popupIncomplete.removeAll();
			}
		});
		int counter = 0;
		SynDiaSystem sds = synDiaController.getSynDiaSystem();
		boolean loop = true;
		for (String name : sds.getVariables()) {
			if (loop) {
				try {
					sds.getSyntaxDiagram(name);
				} catch (ElementNotFoundException e) {
					counter++;
					JLabel variable = new JLabel(name);
					variable.setBackground(Color.WHITE);
					variable.setAlignmentX(Component.CENTER_ALIGNMENT);
					variable.setFont(font);
					popupIncomplete.add(variable);
					popupIncomplete.add(Box
							.createRigidArea(new Dimension(0, 10)));
					if (counter > 5) {
						variable.setText("...");
						loop = false;
					}
				}
			}
		}
		popupIncomplete.add(okButton);
		popupIncomplete.setVisible(true);
		popupIncomplete.validate();
		popupIncomplete.repaint();
		dialog.setContentPane(popupIncomplete);
		dialog.setSize(popupIncomplete.getWidth(), popupIncomplete.getHeight()
				+ counter * 30);
		dialog.setLocationRelativeTo(rootPane);
		dialog.setVisible(true);
	}

	/**
	 * Shows popup for editing syntax diagram names.
	 * 
	 * @param sd
	 *            the syntax diagram
	 * @param x
	 *            position x
	 * @param y
	 *            position y
	 * @param topLine
	 *            text for the border
	 */
	public void showEditSynDiaName(SyntaxDiagram sd, int x, int y,
			String topLine) {
		this.currentSyntaxDiagram = sd;
		setStartDiagram = false;
		checkBox.setEnabled(true);
		x -= drawPanel.getX();
		// if you want to edit
		if (sd != null) {
			// test, if this is the old startDiagram
			synDiaNameTextField.setText(sd.getName());
			if (synDiaController.getSynDiaSystem().getStartDiagram().equals(
					sd.getName())) {
				setStartDiagram = true;
				checkBox.setEnabled(false);
			}
		} else {
			synDiaNameTextField.setText("");
			y -= drawPanel.getY();
		}
		checkBox.setSelected(setStartDiagram);
		checkBox.addItemListener(checkBoxListener);
		oldLabel = synDiaNameTextField.getText();
		panel.setBorder(BorderFactory.createTitledBorder(null, topLine,
				TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION));
		panel.repaint();
		// fill topOfDrawPanel + leftOfDrawPanel
		topOfDrawPanel = drawPanel.getLocationOnScreen().y;
		leftOfDrawPanel = drawPanel.getLocationOnScreen().x;
		synDiaNameTextField.requestFocus();
		synDiaNameTextField.selectAll();
		// set size to (0, 0) to get exactly the middle of the draw panel
		dialogSynDiaName.setLocation(leftOfDrawPanel + x, topOfDrawPanel + y);
		dialogSynDiaName.setVisible(true);
	}

	/**
	 * Shows the text field for edit mode.
	 * 
	 * @param x
	 *            position where text field should pop up
	 * @param y
	 *            position where text field should pop up
	 * @param synDiaElem
	 *            is needed to save currentSynDiaElem
	 */
	public void showEditTextField(int x, int y, SynDiaElem synDiaElem) {
		this.currentSynDiaElem = synDiaElem;
		if (currentSynDiaElem instanceof TerminalSymbol) {
			editTextField.setText(((TerminalSymbol) synDiaElem).getLabel());
		} else if (currentSynDiaElem instanceof Variable) {
			editTextField.setText(((Variable) synDiaElem).getLabel());
		}
		oldLabel = editTextField.getText();
		int additionalWidth = 40;
		int width = editTextField.getFontMetrics(editTextField.getFont())
				.charsWidth(editTextField.getText().toCharArray(), 0,
						editTextField.getText().toCharArray().length)
				+ additionalWidth;
		if (width < 60)
			width = 60;
		int height = 25;
		editTextField.setSize(width, height);
		y += 1.2 * getRenderRadius() - 15;
		dialogEdit.setSize(editTextField.getWidth() + 25, editTextField
				.getHeight());
		// fill topOfDrawPanel + leftOfDrawPanel
		topOfDrawPanel = drawPanel.getLocationOnScreen().y;
		leftOfDrawPanel = drawPanel.getLocationOnScreen().x;
		dialogEdit.setLocation(leftOfDrawPanel + x, topOfDrawPanel + y);
		editTextField.requestFocus(true);
		editTextField.selectAll();
		dialogEdit.setVisible(true);
	}

	/**
	 * Shows the text field for creation of a new terminal symbol or variable.
	 * 
	 * @param x
	 *            position where text field should pop up
	 * @param y
	 *            position where text field should pop up
	 * @param mode
	 *            is used to save the mode (addTerminal or addVariable) to
	 *            create right element when text is entered.
	 * @param currentRenderElement
	 *            is needed to save currentRenderElement for later creation of a
	 *            syntax diagram element (in
	 *            <code>actionPerformed(ActionEvent e)</code>)
	 */
	public void showNewTextField(int x, int y, int mode,
			Component currentRenderElement) {
		this.currentRenderElement = currentRenderElement;
		this.mode = mode;
		if (mode == SynDiaMouseListener.MODE_TERMINAL) {
			newTextField.setText("a");
		} else if (mode == SynDiaMouseListener.MODE_VARIABLE) {
			newTextField.setText("A");
		}
		oldLabel = newTextField.getText();
		int width = 60;
		int height = 23;
		newTextField.setSize(width, height);
		y += 1.2 * getRenderRadius() - 15;
		dialogNew.setSize(newTextField.getWidth() + 25, newTextField
				.getHeight());
		// fill topOfDrawPanel + leftOfDrawPanel
		topOfDrawPanel = drawPanel.getLocationOnScreen().y;
		leftOfDrawPanel = drawPanel.getLocationOnScreen().x;
		dialogNew.setLocation(leftOfDrawPanel + x, topOfDrawPanel + y);
		newTextField.requestFocus(true);
		newTextField.selectAll();
		dialogNew.setVisible(true);
	}

	private void showTiledComponent(Component component,
			Component leftRenderElement) {
		int x = leftRenderElement.getX()
				- synDiaRenderer.getRenderValues().radius;
		int y = leftRenderElement.getY();
		int width = synDiaRenderer.getRenderValues().radius * 4;
		int height = synDiaRenderer.getRenderValues().radius * 4;
		component.setBounds(x, y, width, height);
		if (component instanceof RenderTiledBranch) {
			((RenderTiledBranch) component).update();
		} else {
			((RenderTiledRepetition) component).update();
		}
		component.setVisible(true);
		leftRenderElement.getParent().add(component);
		component.requestFocus();
		component.repaint();
	}

	/**
	 * Shows the tiled branch that is used to demonstrate where the new branch
	 * would be added.
	 * 
	 * @param leftRenderElement
	 *            is needed to compute location right
	 */
	public void showTiledBranch(Component leftRenderElement) {
		renderTiledBranch.setRenderValues(synDiaRenderer.getRenderValues());
		showTiledComponent(renderTiledBranch, leftRenderElement);
	}

	/**
	 * Hides the tiled branch.
	 * 
	 */
	public void hideTiledBranch() {
		renderTiledBranch.setVisible(false);
	}

	/**
	 * Is called if the right end of the tiled branch is moved to somewhere new.
	 * 
	 * @param begin
	 *            position of the new left end of the tiled branch
	 * @param end
	 *            position of the new right end of the tiled branch
	 */
	public void setNewTiledBranchEnd(int begin, int end) {
		Rectangle bounds = renderTiledBranch.getBounds();
		if (begin <= end) {
			bounds.width = end - bounds.x + 3
					* synDiaRenderer.getRenderValues().radius;
		} else {
			bounds.width = begin - end + 4
					* synDiaRenderer.getRenderValues().radius;
			bounds.x = end - 1 * synDiaRenderer.getRenderValues().radius;
		}
		renderTiledBranch.setBounds(bounds);
		renderTiledBranch.update();
		renderTiledBranch.repaint();
	}

	/**
	 * Is called if the height of the tiled branch has changed, because the
	 * tiled branch had been moved over new elements with greater or smaller
	 * dimensions.
	 * 
	 * @param height
	 *            the new height of the tiled branch
	 */
	public void setNewTiledBranchHeight(int height) {
		Rectangle bounds = renderTiledBranch.getBounds();
		bounds.height = height;
		renderTiledBranch.setBounds(bounds);
		renderTiledBranch.update();
		renderTiledBranch.repaint();
	}

	/**
	 * Shows the tiled repetition that is used to demonstrate where the new
	 * repetition would be added.
	 * 
	 * @param leftRenderElement
	 *            is needed to compute location right
	 */
	public void showTiledRepetition(Component leftRenderElement) {
		renderTiledRepetition.setRenderValues(synDiaRenderer.getRenderValues());
		showTiledComponent(renderTiledRepetition, leftRenderElement);
	}

	/**
	 * Hdes the tiled branch.
	 * 
	 */
	public void hideTiledRepetition() {
		renderTiledRepetition.setVisible(false);
	}

	/**
	 * Is called if the right end of the tiled repetition is moved to somewhere
	 * new.
	 * 
	 * @param begin
	 *            position of the new left end of the tiled repetition
	 * @param end
	 *            position of the new right end of the tiled repetition
	 */
	public void setNewTiledRepetitionEnd(int begin, int end) {
		Rectangle bounds = renderTiledRepetition.getBounds();
		if (begin <= end) {
			bounds.width = end - bounds.x + 3
					* synDiaRenderer.getRenderValues().radius;
		} else {
			bounds.width = begin - end + 4
					* synDiaRenderer.getRenderValues().radius;
			bounds.x = end - 1 * synDiaRenderer.getRenderValues().radius;
		}
		renderTiledRepetition.setBounds(bounds);
		renderTiledRepetition.update();
		renderTiledRepetition.repaint();
	}

	/**
	 * Is called if the height of the tiled repetition has changed, because the
	 * tiled repetition had been moved over new elements with greater or smaller
	 * dimensions.
	 * 
	 * @param height
	 *            the new height of the tiled repetition
	 */
	public void setNewTiledRepetitionHeight(int height) {
		Rectangle bounds = renderTiledRepetition.getBounds();
		bounds.height = height;
		renderTiledRepetition.setBounds(bounds);
		renderTiledRepetition.update();
		renderTiledRepetition.repaint();
	}

	/**
	 * Is called when syntax diagram panels were redrawn.
	 * 
	 */
	public void hasBeenDrawn() {
		setChangeElemListenerBack();
		// bad, bad smell... no time to change this :-(
		if (drawPanel != null) {
			if (drawPanel.getCursor().equals(cursorDelete)) {
				setDeleteElemListener();
			} else if (drawPanel.getCursor().equals(cursorEdit)) {
				setChangeElemListener();
			}
		}
		// update the toolbar (undo- and redo-buttons)
		undoAction.setEnabled(synDiaController.getActionStack()
				.isUndoPossible());
		redoAction.setEnabled(synDiaController.getActionStack()
				.isRedoPossible());
	}

	/**
	 * Returns the renderRadius of the syntax diagram renderer.
	 * 
	 * @return the renderRadius of the syntax diagram renderer
	 */
	public int getRenderRadius() {
		return synDiaRenderer.getRenderValues().radius;
	}

	/**
	 * Returns the render radius from the render values.
	 * 
	 * @return the render radius from the render values
	 */
	public RenderValues getRenderValues() {
		return synDiaRenderer.getRenderValues();
	}

	/**
	 * Is called when the user presses "Enter" or clicks on the "OK-Button" when
	 * editing a terminal symbol, variable or syntax diagram name.
	 * 
	 */
	public void changeLabelOfElement() {
		// user wants to change label of an existing terminal symobl or variable
		if (dialogEdit.isVisible()) {
			dialogEdit.setVisible(false);
			if (editTextField.getText().trim().equals("")
					|| editTextField.getText().trim().equals(oldLabel))
				return;
			if (this.currentSynDiaElem instanceof TerminalSymbol) {
				synDiaController.renameTerminal(
						(TerminalSymbol) currentSynDiaElem, editTextField
								.getText());
			} else if (this.currentSynDiaElem instanceof Variable) {
				synDiaController.renameVariable((Variable) currentSynDiaElem,
						editTextField.getText());
			}
		}
		// user wants to create a new terminal symbol or variable
		if (dialogNew.isVisible()) {
			dialogNew.setVisible(false);
			if (newTextField.getText().trim().equals(""))
				return;
			String text = newTextField.getText();
			if (mode == SynDiaMouseListener.MODE_TERMINAL) {
				if (renderMap.containsKey(currentRenderElement)) {
					NullElem nullElem = (NullElem) renderMap
							.get(currentRenderElement);
					synDiaController.addTerminal(nullElem, text.trim());
				}
			} else if (mode == SynDiaMouseListener.MODE_VARIABLE) {
				if (renderMap.containsKey(currentRenderElement)) {
					NullElem nullElem = (NullElem) renderMap
							.get(currentRenderElement);
					synDiaController.addVariable(nullElem, text.trim());
				}
			}
		}
		if (dialogSynDiaName.isVisible()) {
			dialogSynDiaName.setVisible(false);
			checkBox.removeItemListener(checkBoxListener);
			// test, if there is already a diagram with the same name
			for (String name : synDiaController.getSynDiaSystem()
					.getLabelsOfVariables()) {
				if (name.equals(synDiaNameTextField.getText().trim())) {
					if (currentSyntaxDiagram != null && name.equals(oldLabel)) {
						synDiaNameTextField.setText(oldLabel);
					} else {
						if (currentSyntaxDiagram != null) {
							showEditSynDiaName(
									currentSyntaxDiagram,
									dialogSynDiaName.getX() - leftOfDrawPanel
											+ drawPanel.getX(),
									dialogSynDiaName.getY() - topOfDrawPanel,
									Messages
											.getString("ebnf",
													"SynDiaEditor.DiagramAlreadyExists"));
						} else {
							showEditSynDiaName(
									currentSyntaxDiagram,
									dialogSynDiaName.getX() - leftOfDrawPanel
											+ drawPanel.getX(),
									dialogSynDiaName.getY() - topOfDrawPanel
											+ drawPanel.getY(),
									Messages
											.getString("ebnf",
													"SynDiaEditor.DiagramAlreadyExists"));

						}
						return;
					}
				}
			}
			if (synDiaNameTextField.getText().trim().equals(""))
				return;
			if (currentSyntaxDiagram != null) {
				synDiaController.renameSyntaxDiagram(currentSyntaxDiagram,
						synDiaNameTextField.getText().trim(), setStartDiagram);
			} else {
				synDiaController.addSyntaxDiagram(synDiaNameTextField.getText()
						.trim(), setStartDiagram);
			}
		}
	}

	/**
	 * Sets the mode to "edit".
	 * 
	 */
	public void setModeEdit() {
		synDiaMouseListener.setMode(SynDiaMouseListener.MODE_EDIT);
		setChangeElemListener();
		setCursor("edit");
		((EditorControlPanel) controlPanel)
				.changeMode(SynDiaMouseListener.MODE_EDIT);
	}

	/**
	 * Sets the mode to "add terminal".
	 * 
	 */
	public void setModeAddTerminal() {
		synDiaMouseListener.setMode(SynDiaMouseListener.MODE_TERMINAL);
		// setStatusLineTextForPanel("terminal");
		setCursor("terminal");
		((EditorControlPanel) controlPanel)
				.changeMode(SynDiaMouseListener.MODE_TERMINAL);
	}

	/**
	 * Sets the mode to "add variable".
	 * 
	 */
	public void setModeAddVariable() {
		synDiaMouseListener.setMode(SynDiaMouseListener.MODE_VARIABLE);
		// setStatusLineTextForPanel("variable");
		setCursor("variable");
		((EditorControlPanel) controlPanel)
				.changeMode(SynDiaMouseListener.MODE_VARIABLE);
	}

	/**
	 * Sets the mode to "add branch".
	 * 
	 */
	public void setModeAddBranch() {
		synDiaMouseListener.setMode(SynDiaMouseListener.MODE_BRANCH);
		setCursor("branch");
		((EditorControlPanel) controlPanel)
				.changeMode(SynDiaMouseListener.MODE_BRANCH);
	}

	/**
	 * Sets the mode to "add repetition".
	 * 
	 */
	public void setModeAddRepetition() {
		synDiaMouseListener.setMode(SynDiaMouseListener.MODE_REPETITION);
		setCursor("repetition");
		((EditorControlPanel) controlPanel)
				.changeMode(SynDiaMouseListener.MODE_REPETITION);
	}

	/**
	 * Sets the mode to "delete".
	 * 
	 */
	public void setModeDelete() {
		synDiaMouseListener.setMode(SynDiaMouseListener.MODE_DELETE);
		setDeleteElemListener();
		setCursor("delete");
		((EditorControlPanel) controlPanel)
				.changeMode(SynDiaMouseListener.MODE_DELETE);
	}

}
