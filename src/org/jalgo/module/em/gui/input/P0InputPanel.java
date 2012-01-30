package org.jalgo.module.em.gui.input;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.jalgo.main.util.Messages;
import org.jalgo.module.em.control.EMModule;
import org.jalgo.module.em.data.EMData;
import org.jalgo.module.em.data.EMState;
import org.jalgo.module.em.data.StartParameters;
import org.jalgo.module.em.gui.UIConstants;

/**
 * Panel that gives the user the ability to enter at least one
 * StartProbabilityDistribution. Checks the Input and Stores it to the
 * StartParameters
 * 
 * @author Kilian Gebhardt
 * 
 */
public class P0InputPanel extends JPanel implements InputPanel, UIConstants {

	private static final String BUTTON_BACKWARD = Messages.getString("em",
			"InputPanel.bBack");
	private static final String BUTTON_FORWARD = Messages.getString("em",
			"InputPanel.bForward");
	private static final String LABEL_P0_INPUT = Messages.getString("em",
			"P0InputPanel.description");
	// Strings and Icons for Labels:
	private static final ImageIcon RANDOM_ICON = new ImageIcon(
			Messages.getResourceURL("em", "ButtonIcon.bRandomFilling"));
	private static final ImageIcon RANDOM_ICON_SMALL = new ImageIcon(
			Messages.getResourceURL("em", "ButtonIcon.bRandomFilling.small"));
	private static final String ERROR_TITLE = Messages.getString("em",
			"P0InputPanel.ErrorMessageTitle");
	private static final String ERROR_MESSAGE_SUM_NOT_1 = Messages.getString(
			"em", "P0InputPanel.ErrorMessageDescriptionSumNot1");
	private static final String ERROR_MESSAGE_NO_SELECTION = Messages
			.getString("em", "P0InputPanel.ErrorMessageDescriptionNoSelection");
	private static final String ERROR_MESSAGE_NO_DOUBLE = Messages.getString(
			"em", "P0InputPanel.ErrorMessageDescriptionNoDouble");
	private static final String ERROR_MESSAGE_NO_P0 = Messages.getString("em",
			"P0InputPanel.ErrorMessageDescriptionNoP0");
	private static final String HEADING_P0_INPUT = Messages.getString("em",
			"P0InputPanel.lP0Input");
	private static final String RANDOM_FILLING_TEXT = Messages.getString("em",
			"P0InputPanel.bRandomFilling");
	private static final String EQUIPARTITION_TEXT = Messages.getString("em",
			"P0InputPanel.bEquipartition");
	private static final String REMOVE_P0_TEXT = Messages.getString("em",
			"P0InputPanel.bRemoveP0");
	private static final String ADD_P0_TEXT = Messages.getString("em",
			"P0InputPanel.bAddP0");
	private static final String ADD_P0_TOOLTIP = Messages.getString("em",
			"P0InputPanel.bAddP0ToolTip");
	private static final String REMOVE_P0_TOOLTIP = Messages.getString("em",
			"P0InputPanel.bRemoveP0ToolTip");
	private static final String BFORWARD_TOOLTIP = Messages.getString("em",
			"P0InputPanel.bForwardToolTip");
	private static final String BBACK_TOOLTIP = Messages.getString("em",
			"P0InputPanel.bBackToolTip");
	private static final String RANDOM_FILLING_TOOLTIP = Messages.getString(
			"em", "P0InputPanel.bRandomFillingToolTip");
	private static final String EQUIPARTITION_TOOLTIP = Messages.getString(
			"em", "P0InputPanel.bEquipartitionToolTip");

	private static final long serialVersionUID = -6221723009063005967L;
	private int lastSelectedColumn;
	private boolean sumNot1Error;

	// Labels
	private JLabel lHeading;
	private JLabel description;

	// Objects for the table
	private JScrollPane tablePanel;
	private JPanel tableCollector;
	private List<JTable> p0Tables;
	private GroupLayout tableLayout;
	private JViewport columnHeader;
	private JTable lastSelectedTable;

	// Buttons
	private JButton bAddP0;
	private JButton bRemoveP0;
	private JButton bForward;
	private JButton bForwardDummy;
	private JButton bBackward;
	private JButton bEquipartition;
	private JButton bRandomFilling;
	private GroupLayout layout;

	// Startparameters
	private StartParameters startParameters;

	/**
	 * Editor that accepts only DoubleValues and Fractions in the Range of 0 ..
	 * 1 in following Representations: "0,14242..." "0.4241" " 132/324".
	 * Whitespace is ignored
	 * 
	 * Colors the Cell Red, if the input was incorrect. Colors the Cell Black
	 * otherwise.
	 * 
	 * @author Kilian Gebhardt
	 * 
	 */
	private static class MyTableCellEditor extends DefaultCellEditor {

		private static final long serialVersionUID = -4429068854127869668L;
		private static final Border red = new LineBorder(Color.red);
		private static final Border black = new LineBorder(Color.black);
		private JTextField textField;

		// This is the component that will handle the editing of the cell value
		public MyTableCellEditor() {
			super(new JTextField());
			textField = (JTextField) getComponent();
			textField.setHorizontalAlignment(JTextField.LEFT);
		}

		@Override
		public Component getTableCellEditorComponent(final JTable table,
				final Object value, final boolean isSelected, final int row,
				final int column) {
			textField.setBorder(black);
			return super.getTableCellEditorComponent(table, value, isSelected,
					row, column);
		}

		@Override
		public boolean stopCellEditing() {
			if ((P0InputTableModel
					.parseInputString(((JTextField) getComponent()).getText()) < 0)
					&& !(((JTextField) getComponent()).getText().equals(""))) {
				((JTextField) getComponent()).setBorder(red);
				return false;
			}
			return super.stopCellEditing();
		}
	}

	/**
	 * Creates new Instance of P0InputPanel. Sets the functions for all Buttons.
	 * 
	 * @param startParameters
	 *            {@code StartParameters} object that has to be filled with data
	 * @param beamerMode
	 *            specifies if beamer mode is enabled or not
	 */
	public P0InputPanel(final StartParameters startParameters,
			final boolean beamerMode) {
		this.startParameters = startParameters;
		tablePanel = new JScrollPane();
		lastSelectedTable = null;
		lastSelectedColumn = -1;
		init();

		// Create Heading and Label
		lHeading = new JLabel(HEADING_P0_INPUT);
		description = new JLabel(LABEL_P0_INPUT);

		// adding bAddP0 Button + ActionListener
		bAddP0 = new JButton(ADD_P0_TEXT);
		bAddP0.setToolTipText(ADD_P0_TOOLTIP);
//		bAddP0.setMinimumSize(bAddP0.getPreferredSize());
		bAddP0.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addColumn();
				updateTable();
				setColumnWidth();
				lastSelectedColumn = -1;
				lastSelectedTable = null;
			}
		});

		// adding bRemoveP0 Button + ActionListener
		bRemoveP0 = new JButton(REMOVE_P0_TEXT);
		bRemoveP0.setToolTipText(REMOVE_P0_TOOLTIP);
//		bRemoveP0.setMinimumSize(bRemoveP0.getPreferredSize());
		bRemoveP0.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				if ((lastSelectedColumn != -1) && (lastSelectedTable != null)) {
					removeColumn(lastSelectedColumn);
					updateTable();
					setColumnWidth();
				} else {
					showErrorMessage(ERROR_MESSAGE_NO_SELECTION);
				}
				lastSelectedColumn = -1;
				lastSelectedTable = null;
			}
		});

		// adding bEqualpartition Button + Actionlistener
		bEquipartition = new JButton(EQUIPARTITION_TEXT);
		bEquipartition.setToolTipText(EQUIPARTITION_TOOLTIP);
		bEquipartition.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				if ((lastSelectedColumn > 0) && (lastSelectedTable != null)) {
					if (lastSelectedTable.isEditing()
							&& !lastSelectedTable.getCellEditor()
									.stopCellEditing()) {
						lastSelectedTable.getCellEditor().cancelCellEditing();
					}

					double toFill = 1 - ((P0InputTableModel) lastSelectedTable
							.getModel()).getColumnSum(lastSelectedColumn);
					if (toFill >= 0) {
						int toFillNumber = 0;
						for (int i = 0; i < lastSelectedTable.getRowCount(); i++) {
							if (P0InputTableModel
									.parseInputString((String) lastSelectedTable
											.getValueAt(i, lastSelectedColumn)) < 0) {
								toFillNumber++;
							}
						}
						for (int i = 0; i < lastSelectedTable.getRowCount(); i++) {
							if (P0InputTableModel
									.parseInputString((String) lastSelectedTable
											.getValueAt(i, lastSelectedColumn)) < 0) {
								lastSelectedTable.getModel().setValueAt(
										((Double) (toFill / toFillNumber))
												.toString(), i,
										lastSelectedColumn);
							}
						}
					} else {
						showErrorMessage(ERROR_MESSAGE_SUM_NOT_1);
					}
				} else {
					showErrorMessage(ERROR_MESSAGE_NO_SELECTION);
				}
				lastSelectedColumn = -1;
				lastSelectedTable = null;
			}
		});

		// adding bRandomFilling Button + ActionListener
		bRandomFilling = new JButton(RANDOM_FILLING_TEXT);
		bRandomFilling.setToolTipText(RANDOM_FILLING_TOOLTIP);
		bRandomFilling.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if ((lastSelectedColumn > 0) && (lastSelectedTable != null)) {
					if (lastSelectedTable.isEditing()
							&& !lastSelectedTable.getCellEditor()
									.stopCellEditing()) {
						lastSelectedTable.getCellEditor().cancelCellEditing();
					}

					double toFill = 1 - ((P0InputTableModel) lastSelectedTable
							.getModel()).getColumnSum(lastSelectedColumn);

					if (toFill >= 0) {
						double rand[] = new double[lastSelectedTable
								.getRowCount()];
						double randSum = 0;
						for (int i = 0; i < rand.length; i++) {
							if (P0InputTableModel
									.parseInputString((String) lastSelectedTable
											.getValueAt(i, lastSelectedColumn)) < 0) {
								rand[i] = Math.random();
							} else {
								rand[i] = 0;
							}
						}
						for (int i = 0; i < rand.length; i++) {
							randSum += rand[i];
						}
						for (int i = 0; i < rand.length; i++) {
							if (P0InputTableModel
									.parseInputString((String) lastSelectedTable
											.getValueAt(i, lastSelectedColumn)) < 0) {
								lastSelectedTable.getModel().setValueAt(
										((Double) (toFill * rand[i] / randSum))
												.toString(), i,
										lastSelectedColumn);
							}
						}
					} else {
						showErrorMessage(ERROR_MESSAGE_SUM_NOT_1);
					}
				} else {
					showErrorMessage(ERROR_MESSAGE_NO_SELECTION);
				}
				lastSelectedColumn = -1;
				lastSelectedTable = null;
			}

		});

		// creating bForward Button and a listener for Checking the Input
		bForward = new JButton(BUTTON_FORWARD);
		bForward.setToolTipText(BFORWARD_TOOLTIP);
		bForward.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				if (p0Tables.get(0).getColumnCount() < 2) {
					showErrorMessage(ERROR_MESSAGE_NO_P0);
					bAddP0.doClick();
				} else if (checkInput()) {
					bForwardDummy.doClick();
				} else if (sumNot1Error) {
					showErrorMessage(ERROR_MESSAGE_SUM_NOT_1);
				} else {
					showErrorMessage(ERROR_MESSAGE_NO_DOUBLE);
				}
			}
		});

		// creating other buttons
		bBackward = new JButton(BUTTON_BACKWARD);
		bBackward.setToolTipText(BBACK_TOOLTIP);
		bForwardDummy = new JButton();

		setBeamerMode(beamerMode);

		// Setting up Group Layout for the Panel
		layout = new GroupLayout(this);
		this.setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(lHeading)
				.addComponent(description)
				.addComponent(tablePanel)
				.addGroup(
						layout.createSequentialGroup().addComponent(bBackward)
								.addContainerGap(400, 600).addComponent(bAddP0)
								.addComponent(bRemoveP0)
								.addComponent(bEquipartition)
								.addComponent(bRandomFilling)
								.addContainerGap(400, 600)
								.addComponent(bForward)));
		layout.setVerticalGroup(layout
				.createSequentialGroup()
				.addComponent(lHeading)
				.addComponent(description)
				.addComponent(tablePanel)
				.addGroup(
						layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(bBackward).addComponent(bAddP0)
								.addComponent(bRemoveP0)
								.addComponent(bEquipartition)
								.addComponent(bRandomFilling)
								.addComponent(bForward)));
	}

	/**
	 * adds a column to all Tables.
	 */
	private void addColumn() {
		for (JTable table : p0Tables) {
			((P0InputTableModel) table.getModel()).addColumn();
			table.getColumnModel().getColumn(table.getColumnCount() - 1)
					.setCellEditor(new MyTableCellEditor());
		}
	}

	/**
	 * Checks whether the sum of the probabilities for each Single Experiment is
	 * 1. Marks the first Column with a incorrect input.
	 * 
	 * @return true, if the Input is correct; false otherwise
	 */
	private final boolean checkInput() {
		for (JTable table : p0Tables) {
			int column = ((P0InputTableModel) table.getModel()).checkColumns();
			if (column != -1) {

				if ((lastSelectedTable != null)
						&& lastSelectedTable.isEditing()
						&& !lastSelectedTable.getCellEditor().stopCellEditing()) {
					lastSelectedTable.getCellEditor().cancelCellEditing();
				}
				int row = 0;
				for (int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++) {
					if (P0InputTableModel.parseInputString((String) table
							.getValueAt(rowIndex, column)) < 0) {
						row = rowIndex;
						break;
					}
				}
				if (P0InputTableModel.parseInputString((String) table
						.getValueAt(row, column)) < 0) {
					sumNot1Error = false;
				} else {
					sumNot1Error = true;
				}
				table.editCellAt(row, column);
				try {
					((JTextField) table.getCellEditor()
							.getTableCellEditorComponent(table,
									table.getCellEditor().getCellEditorValue(),
									false, row, column)).requestFocus();
					unselectColumns(null);
					lastSelectedColumn = column;
					lastSelectedTable = table;
				} catch (NullPointerException e) {

				}
				return false;
			}
		}
		return true;
	}

	@Override
	public final JButton getBackButton() {
		return bBackward;
	}

	/**
	 * Returns the ForwardButton.
	 * 
	 * @return the forward button
	 */
	@Override
	public final JButton getForwardButton() {
		return bForwardDummy;
	}

	/**
	 * Initializes the Table and updates the Layout of P0InputPanel.
	 */
	public void init() {
		p0Tables = new ArrayList<JTable>();

		/*
		 * Try - Catch - Block is used, to prevent from crashing in case, that
		 * startParameters don't contain any events or Parameters in
		 * startParameters are not initialized yet.
		 * 
		 * This is the case, if P0InputTable is instanced, before the previous
		 * Input-Steps were completed correctly
		 */
		try {
			for (int objectIndex = 0; objectIndex < startParameters
					.getObjectCount(); objectIndex++) {
				p0Tables.add(new JTable(new P0InputTableModel(startParameters,
						objectIndex)));
			}
		} catch (Exception e) {
		}

		// Adding blank Columns to some of the Tables
		int maxPreColumns = 2;
		for (JTable table : p0Tables) {
			if (maxPreColumns < table.getColumnCount()) {
				maxPreColumns = table.getColumnCount();
			}
		}
		for (JTable table : p0Tables) {
			while (table.getColumnCount() < maxPreColumns) {
				((P0InputTableModel) table.getModel()).addColumn();
				((P0InputTableModel) table.getModel())
						.fireTableStructureChanged();
			}
		}
		MyTableCellEditor editor = new MyTableCellEditor();

		for (JTable table : p0Tables) {
			for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
				table.getColumnModel().getColumn(columnIndex)
						.setCellEditor(editor);
			}
		}

		FocusListener focusListener = new FocusListener() {
			@Override
			public void focusLost(final FocusEvent e) {
				if (e.getSource() instanceof JTable) {
					JTable table = (JTable) e.getSource();
					if (table.isEditing()
							&& !table.getCellEditor().stopCellEditing()) {
						table.getCellEditor().cancelCellEditing();
					}
					lastSelectedTable = table;
					lastSelectedColumn = table.getSelectedColumn();
					table.clearSelection();
				}
			}

			@Override
			public void focusGained(final FocusEvent e) {
				if (e.getSource() instanceof JTable) {
					JTable table = (JTable) e.getSource();
					unselectColumns(table);
					if (table.getColumnModel().getSelectionModel()
							.getLeadSelectionIndex() > 0) {
						table.setColumnSelectionInterval(table.getColumnModel()
								.getSelectionModel().getLeadSelectionIndex(),
								table.getColumnModel().getSelectionModel()
										.getLeadSelectionIndex());
					}
				}
			}
		};

		// Adjusting some style settings for all tables
		for (JTable table : p0Tables) {
			// Header
			table.getTableHeader().setResizingAllowed(false);
			table.getTableHeader().setSize(200, 400);
			table.getTableHeader().setReorderingAllowed(false);

			// Font / Sizes
			table.getColumnModel().getColumn(0).setPreferredWidth(200);
			table.getColumnModel().getColumn(1).setPreferredWidth(120);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			table.setRowHeight(23);

			// Set Selection
			table.setRowSelectionAllowed(false);
			table.setColumnSelectionAllowed(false);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			table.addFocusListener(focusListener);
		}

		setColumnWidth();

		// Setting up the Container, that hold all tables
		tableCollector = new JPanel();

		tableLayout = new GroupLayout(tableCollector);
		tableCollector.setLayout(tableLayout);

		tableLayout.setAutoCreateGaps(true);

		Group horizontalGroup = tableLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING, false);
		Group verticalGroup = tableLayout.createSequentialGroup();

		for (JTable p0Table : p0Tables) {
			horizontalGroup.addComponent(p0Table);
			verticalGroup.addComponent(p0Table);
		}
		tableLayout.setHorizontalGroup(horizontalGroup);
		tableLayout.setVerticalGroup(verticalGroup);

		// Scroll Pane in which tableCollector with all tables can be scrolled
		JScrollPane oldtablePanel = tablePanel;
		tablePanel = new JScrollPane(tableCollector);
		columnHeader = new JViewport();
		if (p0Tables.size() > 0) {
			columnHeader.add(p0Tables.get(0).getTableHeader());
		}
		tablePanel.setColumnHeader(columnHeader);

		if (layout != null) {
			layout.replace(oldtablePanel, tablePanel);
		}
	}

	/**
	 * removes a specified Column from all Tables.
	 * 
	 * @param column
	 *            the Index of the Column
	 */
	private void removeColumn(final int column) {
		for (JTable table : p0Tables) {
			((P0InputTableModel) table.getModel()).removeColumn(column);
		}
	}

	/**
	 * Sets the UI mode to beamer or normal mode. Call the method with
	 * <code>true</code> to enter beamer mode or with <code>false</code> to
	 * leave it.
	 * <p>
	 * In beamer mode, all button texts are replaced with icons and the font
	 * size is increased.
	 * 
	 * @param modeOn
	 *            <code>true</code> if the beamer mode should be started,
	 *            <code>false</code> if view should be set to standard view
	 */
	public void setBeamerMode(boolean modeOn) {
		if (modeOn) {
			bForward.setIcon(FORWARD_ICON);
			bForward.setText(null);

			bBackward.setIcon(BACK_ICON);
			bBackward.setText(null);

			bAddP0.setIcon(PLUS_ICON);
			bAddP0.setText(null);
			bAddP0.setToolTipText(ADD_P0_TEXT);
			bAddP0.setMinimumSize(bAddP0.getPreferredSize());

			bRemoveP0.setIcon(REMOVE_ICON);
			bRemoveP0.setText(null);
			bRemoveP0.setToolTipText(REMOVE_P0_TEXT);
			bRemoveP0.setMinimumSize(bRemoveP0.getPreferredSize());

			bEquipartition.setIcon(SHIELD_ICON);
			bEquipartition.setText(null);
			bEquipartition.setToolTipText(EQUIPARTITION_TEXT);

			bRandomFilling.setIcon(RANDOM_ICON);
			bRandomFilling.setText(null);
			bRandomFilling.setToolTipText(RANDOM_FILLING_TEXT);

			lHeading.setFont(PRESENTATION_FONT);
			description.setFont(PRESENTATION_FONT);
			for (JTable table : p0Tables) {
				table.getTableHeader().setFont(PRESENTATION_FONT);
				table.setFont(PRESENTATION_FONT);
			}
		} else {
			bForward.setText(BUTTON_FORWARD);
			bForward.setIcon(FORWARD_ICON_SMALL);

			bBackward.setText(BUTTON_BACKWARD);
			bBackward.setIcon(BACK_ICON_SMALL);

			bRemoveP0.setText(REMOVE_P0_TEXT);
			bRemoveP0.setIcon(REMOVE_ICON_SMALL);
			bRemoveP0.setMinimumSize(bRemoveP0.getPreferredSize());

			bAddP0.setText(ADD_P0_TEXT);
			bAddP0.setIcon(PLUS_ICON_SMALL);
			bAddP0.setMinimumSize(bAddP0.getPreferredSize());

			bEquipartition.setText(EQUIPARTITION_TEXT);
			bEquipartition.setIcon(SHIELD_ICON_SMALL);

			bRandomFilling.setText(RANDOM_FILLING_TEXT);
			bRandomFilling.setIcon(RANDOM_ICON_SMALL);

			lHeading.setFont(DEFAULT_FONT);
			description.setFont(DEFAULT_FONT);

			for (JTable table : p0Tables) {
				table.getTableHeader().setFont(DEFAULT_FONT);
				table.setFont(DEFAULT_FONT);
			}
		}
	}

	/**
	 * Sets all TableColumns to a predefined width.
	 */
	private void setColumnWidth() {
		for (JTable table : p0Tables) {
			table.getColumnModel().getColumn(0).setPreferredWidth(200);
			for (int i = 1; i < table.getModel().getColumnCount(); i++) {
				table.getColumnModel().getColumn(i).setPreferredWidth(120);
			}
		}
	}

	/**
	 * Displays an ErrorMessage.
	 * 
	 * @param errorMessage
	 *            The Message that shall be displayed.
	 */
	private void showErrorMessage(String errorMessage) {
		JOptionPane.showMessageDialog(null, errorMessage, ERROR_TITLE,
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * unselects all tables but one.
	 * 
	 * @param ignore
	 *            the table that should be ignored
	 */
	private final void unselectColumns(final JTable ignore) {
		for (JTable table : p0Tables) {
			if (table != ignore) {
				// table.getCellEditor().cancelCellEditing();
				table.clearSelection();
				// table.updateUI();
			}
		}
	}

	/**
	 * updates all Tables after Data or StructureChanges.
	 */
	private void updateTable() {
		for (JTable table : p0Tables) {
			((P0InputTableModel) table.getModel()).fireTableDataChanged();
			((P0InputTableModel) table.getModel()).fireTableStructureChanged();
			for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
				table.getColumnModel().getColumn(columnIndex)
						.setCellEditor(new MyTableCellEditor());
			}
		}
	}

	/**
	 * Writes the probably incorrect Input to StartParameters. This Function is
	 * used, when BackButton is pressed every Probability that wasn't inserted,
	 * will be set to -1.0
	 */
	public void writeTempP0ToStartParameters() {
		List<EMData> p0DataList = new ArrayList<EMData>();

		// Creating p0 Distributions
		for (int p0Distribution = 1; p0Distribution < (p0Tables.get(0)
				.getModel().getColumnCount()); p0Distribution++) {
			Map<Point, Double> singleEventStartProbability = new HashMap<Point, Double>();
			for (int objectIndex = 0; objectIndex < startParameters
					.getObjectCount(); objectIndex++) {
				for (int objectSide = 1; objectSide <= startParameters
						.getExperiment().get(objectIndex); objectSide++) {
					singleEventStartProbability.put(new Point(objectSide,
							objectIndex),
							P0InputTableModel
									.parseInputString(((String) p0Tables
											.get(objectIndex)
											.getModel()
											.getValueAt(objectSide - 1,
													p0Distribution))));
				}
			}
			EMData p0Data = new EMData(null, null, null, null,
					singleEventStartProbability, null);
			p0DataList.add(p0Data);
		}
		startParameters.setP0EMState(new EMState(p0DataList));

	}

	/**
	 * Creates the first EMState, by reading the Data from the TableModel and
	 * set it to P0EMState of StartParameters.
	 */
	public final void writeP0ToStartParameters() {
		List<EMData> p0DataList = new ArrayList<EMData>();

		// Creating p0 Distributions
		for (int p0Distribution = 1; p0Distribution < (p0Tables.get(0)
				.getModel().getColumnCount()); p0Distribution++) {
			Map<Point, Double> singleEventStartProbability = new HashMap<Point, Double>();
			for (int objectIndex = 0; objectIndex < startParameters
					.getObjectCount(); objectIndex++) {
				for (int objectSide = 1; objectSide <= startParameters
						.getExperiment().get(objectIndex); objectSide++) {
					singleEventStartProbability.put(new Point(objectSide,
							objectIndex),
							P0InputTableModel
									.parseInputString(((String) p0Tables
											.get(objectIndex)
											.getModel()
											.getValueAt(objectSide - 1,
													p0Distribution))));
				}
			}
			EMData p0Data = EMModule.calcFirstStep(singleEventStartProbability,
					startParameters);
			p0DataList.add(p0Data);
		}
		startParameters.setP0EMState(new EMState(p0DataList));
	}
}
