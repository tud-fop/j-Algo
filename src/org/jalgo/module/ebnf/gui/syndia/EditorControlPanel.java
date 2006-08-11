package org.jalgo.module.ebnf.gui.syndia;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jalgo.main.gui.event.StatusLineUpdater;
import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.gui.syndia.display.AbstractControlPanel;
import org.jalgo.module.ebnf.gui.syndia.display.DiagramSize;

/**
 * Control panel for the syntax diagram editor. Administrates all "mode
 * buttons".
 * 
 * @author Michael Thiele
 */
@SuppressWarnings("serial")
public class EditorControlPanel extends AbstractControlPanel {

	private JButton toSynDiaView = new JButton(Messages.getString("ebnf",
			"SynDia.Control_ToSynDiaDisplay"));

	private JToggleButton modeEdit = new JToggleButton(new ImageIcon(Messages
			.getResourceURL("ebnf", "Icon.Edit")));

	private JToggleButton modeAddTerminal = new JToggleButton(new ImageIcon(
			Messages.getResourceURL("ebnf", "Icon.AddTerminal")));

	private JToggleButton modeAddVariable = new JToggleButton(new ImageIcon(
			Messages.getResourceURL("ebnf", "Icon.AddVariable")));

	private JToggleButton modeAddBranch = new JToggleButton(new ImageIcon(
			Messages.getResourceURL("ebnf", "Icon.AddBranch")));

	private JToggleButton modeAddRepetition = new JToggleButton(new ImageIcon(
			Messages.getResourceURL("ebnf", "Icon.AddRepetition")));

	private JToggleButton modeDelete = new JToggleButton(new ImageIcon(Messages
			.getResourceURL("ebnf", "Icon.Delete")));

	private JButton addDiagram = new JButton(new ImageIcon(Messages
			.getResourceURL("ebnf", "Icon.AddDiagram")));

	private JToggleButton lastActiveButton = modeEdit;

	private AbstractSDGuiController controller;

	private ItemListener fitToSizeListener;

	private FocusAdapter zoomerFocusListener;

	private ChangeListener zoomerChangeListener;

	private ActionListener toSynDiaViewListener;

	private ActionListener modeEditListener;

	private ActionListener modeAddTerminalListener;

	private ActionListener modeAddVariableListener;

	private ActionListener modeAddBranchListener;

	private ActionListener modeAddRepetitionListener;

	private ActionListener modeDeleteListener;

	private ActionListener addDiagramListener;
	
	private MouseListener myStatusUpdater;

	/**
	 * Initializes the EditorControllPanel.
	 * 
	 * @param controller
	 *            the guiController this control panel belongs to.
	 */
	public EditorControlPanel(AbstractSDGuiController controller) {
		this.controller = controller;
		fitToSize.setSelected(false);
		init();
		initListener();
	}

	/**
	 * Initializes the "mode buttons".
	 */
	private void init() {

		// elements
		JLabel empty = new JLabel("      ");
		JLabel empty2 = new JLabel("   ");
		JLabel empty3 = new JLabel("  ");

		// JPanel right = new JPanel();
		right.setBackground(Color.WHITE);
		right.add(toSynDiaView);
		this.add(right, BorderLayout.EAST);

		modeEdit.setBackground(Color.WHITE);
		modeDelete.setBackground(Color.WHITE);
		modeAddTerminal.setBackground(Color.WHITE);
		modeAddVariable.setBackground(Color.WHITE);
		modeAddBranch.setBackground(Color.WHITE);
		modeAddRepetition.setBackground(Color.WHITE);
		addDiagram.setBackground(Color.WHITE);

		modeEdit.setFocusable(false);
		modeDelete.setFocusable(false);
		modeAddTerminal.setFocusable(false);
		modeAddVariable.setFocusable(false);
		modeAddBranch.setFocusable(false);
		modeAddRepetition.setFocusable(false);
		addDiagram.setFocusable(false);

		Insets insets = new Insets(1, 1, 1, 1);
		modeEdit.setMargin(insets);
		modeAddTerminal.setMargin(insets);
		modeAddVariable.setMargin(insets);
		modeAddBranch.setMargin(insets);
		modeAddRepetition.setMargin(insets);
		modeDelete.setMargin(insets);
		addDiagram.setMargin(insets);

		modeEdit.setDisabledSelectedIcon(new ImageIcon(Messages.getResourceURL(
				"ebnf", "Icon.SelectedEdit")));
		modeDelete.setDisabledSelectedIcon(new ImageIcon(Messages
				.getResourceURL("ebnf", "Icon.SelectedDelete")));
		modeAddTerminal.setDisabledSelectedIcon(new ImageIcon(Messages
				.getResourceURL("ebnf", "Icon.SelectedAddTerminal")));
		modeAddVariable.setDisabledSelectedIcon(new ImageIcon(Messages
				.getResourceURL("ebnf", "Icon.SelectedAddVariable")));
		modeAddBranch.setDisabledSelectedIcon(new ImageIcon(Messages
				.getResourceURL("ebnf", "Icon.SelectedAddBranch")));
		modeAddRepetition.setDisabledSelectedIcon(new ImageIcon(Messages
				.getResourceURL("ebnf", "Icon.SelectedAddRepetition")));

		addDiagram.setMnemonic(KeyEvent.VK_N);
		modeEdit.setMnemonic(KeyEvent.VK_E);
		modeDelete.setMnemonic(KeyEvent.VK_D);
		modeAddTerminal.setMnemonic(KeyEvent.VK_T);
		modeAddVariable.setMnemonic(KeyEvent.VK_V);
		modeAddBranch.setMnemonic(KeyEvent.VK_B);
		modeAddRepetition.setMnemonic(KeyEvent.VK_R);

		this.myStatusUpdater = StatusLineUpdater.getInstance();
		
		addDiagram.setToolTipText(Messages.getString("ebnf",
				"SynDiaEditor.ToolTip_AddSynDia"));
		addDiagram.addMouseListener(myStatusUpdater);
		modeEdit.setToolTipText(Messages.getString("ebnf",
				"SynDiaEditor.ToolTip_Edit"));
		modeEdit.addMouseListener(myStatusUpdater);
		modeDelete.setToolTipText(Messages.getString("ebnf",
				"SynDiaEditor.ToolTip_Delete"));
		modeDelete.addMouseListener(myStatusUpdater);
		modeAddTerminal.setToolTipText(Messages.getString("ebnf",
				"SynDiaEditor.ToolTip_AddTerminal"));
		modeAddTerminal.addMouseListener(myStatusUpdater);
		modeAddVariable.setToolTipText(Messages.getString("ebnf",
				"SynDiaEditor.ToolTip_AddVariable"));
		modeAddVariable.addMouseListener(myStatusUpdater);
		modeAddBranch.setToolTipText(Messages.getString("ebnf",
				"SynDiaEditor.ToolTip_AddBranch"));
		modeAddBranch.addMouseListener(myStatusUpdater);
		modeAddRepetition.setToolTipText(Messages.getString("ebnf",
				"SynDiaEditor.ToolTip_AddRepetition"));
		modeAddRepetition.addMouseListener(myStatusUpdater);

		empty.setBackground(Color.WHITE);
		left.add(empty);
		left.add(addDiagram);
		left.add(empty2);
		left.add(modeEdit);
		left.add(modeDelete);
		left.add(empty3);
		left.add(modeAddTerminal);
		left.add(modeAddVariable);
		left.add(modeAddBranch);
		left.add(modeAddRepetition);

		this.add(left, BorderLayout.WEST);

	}

	private void enableLastButton(JToggleButton newButton) {
		// enable last button again
		lastActiveButton.setEnabled(true);
		lastActiveButton.setSelected(false);
		newButton.setSelected(true);
		newButton.setEnabled(false);
		lastActiveButton = newButton;
	}

	private void checkListener() {
		if (lastActiveButton == modeEdit) {
			((GuiController) controller).setChangeElemListenerBack();
		} else if (lastActiveButton == modeAddBranch
				|| lastActiveButton == modeAddRepetition) {
			((GuiController) controller).setNullElemColorListenerBack();
		} else if (lastActiveButton == modeDelete) {
			((GuiController) controller).setDeleteElemListenerBack();
		}
	}

	private void initListener() {

		fitToSizeListener = new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					controller.setAutoSize(true);
				} else {
					controller.setAutoSize(false);
				}
			}
		};
		fitToSize.addItemListener(fitToSizeListener);

		zoomerFocusListener = new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				fitToSize.setSelected(false);
			}
		};
		zoomer.addFocusListener(zoomerFocusListener);

		zoomerChangeListener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				controller.resizeDrawPanel(zoomer.getValue());
			}
		};
		zoomer.addChangeListener(zoomerChangeListener);

		toSynDiaViewListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DiagramSize
						.setFontSize(controller.getDrawPanel().getFontSize());
				((GuiController) controller).switchToSynDiaView();
			}
		};

		toSynDiaView.addActionListener(toSynDiaViewListener);

		modeEditListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkListener();
				enableLastButton(modeEdit);
				((GuiController) controller).setModeEdit();
			}
		};
		modeEdit.addActionListener(modeEditListener);
		modeEdit.setSelected(true);
		modeEdit.setEnabled(false);

		modeAddTerminalListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkListener();
				enableLastButton(modeAddTerminal);
				((GuiController) controller).setModeAddTerminal();
			}
		};
		modeAddTerminal.addActionListener(modeAddTerminalListener);

		modeAddVariableListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkListener();
				enableLastButton(modeAddVariable);
				((GuiController) controller).setModeAddVariable();
			}
		};
		modeAddVariable.addActionListener(modeAddVariableListener);

		modeAddBranchListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkListener();
				enableLastButton(modeAddBranch);
				((GuiController) controller).setModeAddBranch();
			}
		};
		modeAddBranch.addActionListener(modeAddBranchListener);

		modeAddRepetitionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkListener();
				enableLastButton(modeAddRepetition);
				((GuiController) controller).setModeAddRepetition();
			}
		};
		modeAddRepetition.addActionListener(modeAddRepetitionListener);

		modeDeleteListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkListener();
				enableLastButton(modeDelete);
				((GuiController) controller).setModeDelete();
			}
		};
		modeDelete.addActionListener(modeDeleteListener);

		addDiagramListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((GuiController) controller)
						.showEditSynDiaName(null, 20, 20, Messages.getString(
								"ebnf", "SynDiaEditor.NameOfDiagram"));
			}
		};
		addDiagram.addActionListener(addDiagramListener);

	}

	/**
	 * Is called, if the mode for editing has changed.
	 * 
	 * @param newMode
	 *            the new mode
	 */
	public void changeMode(int newMode) {
		if (newMode == SynDiaMouseListener.MODE_EDIT && modeEdit.isEnabled()) {
			enableLastButton(modeEdit);
		} else if (newMode == SynDiaMouseListener.MODE_TERMINAL
				&& modeAddTerminal.isEnabled()) {
			enableLastButton(modeAddTerminal);
		} else if (newMode == SynDiaMouseListener.MODE_VARIABLE
				&& modeAddVariable.isEnabled()) {
			enableLastButton(modeAddVariable);
		} else if (newMode == SynDiaMouseListener.MODE_BRANCH
				&& modeAddBranch.isEnabled()) {
			enableLastButton(modeAddBranch);
		} else if (newMode == SynDiaMouseListener.MODE_REPETITION
				&& modeAddRepetition.isEnabled()) {
			enableLastButton(modeAddRepetition);
		} else if (newMode == SynDiaMouseListener.MODE_DELETE
				&& modeDelete.isEnabled()) {
			enableLastButton(modeDelete);
		}
	}

}
