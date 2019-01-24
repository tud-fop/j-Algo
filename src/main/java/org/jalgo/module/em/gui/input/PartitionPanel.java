package org.jalgo.module.em.gui.input;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.jalgo.main.util.Messages;
import org.jalgo.module.em.data.Event;
import org.jalgo.module.em.data.Partition;
import org.jalgo.module.em.data.StartParameters;
import org.jalgo.module.em.gui.UIConstants;

/**
 * Panel which contains all UI elements for partitioning an experiments events.
 * <p>
 * The user can choose out of various predefined partitions or can create an own
 * partition (if the experiments consists of not more than 2 objects).
 * 
 * @author Tobias Nett
 * 
 */
public class PartitionPanel extends JPanel implements InputPanel,
		ActionListener, UIConstants {

	private static final long serialVersionUID = 433929721669238472L;

	private static final int LISTWIDTH = 180;

	private static final int SAMESIDE = 1;
	private static final int SIDECOUNT = 2;
	private static final int SUM = 3;
	private static final int PROPERTY = 4;
	private static final int CUSTOM = 5;

	private int[] observationType;

	private StartParameters params;

	private JPanel buttonPanel;
	private JPanel selectionPanel;
	private JPanel tablePanel;
	private JPanel listPanel;

	// ButtonPanel
	private JButton bForward;
	private JButton bBack;

	// SelectionPanel
	private JLabel lSelection;
	private JRadioButton bSameSide;
	private JRadioButton bSideCount;
	private JRadioButton bObjectProperty;
	private JRadioButton bSum;
	private JRadioButton bCustomize;
	private JComboBox cSideSelection;
	private JComboBox cObjectSelection;
	private JComboBox cPropertySelection;

	// ListPanel
	private JLabel lList;
	private JButton bRemove;
	private JList partitionList;
	private ListModel listModel;

	// TablePanel
	private JLabel lTable;
	private JButton bNew;
	private JButton bFill;
	private PartitionTable table;

	private Set<Partition> observations;

	private boolean beamerMode = false;

	/**
	 * Creates a new object of this panel with the given parameters. Initializes
	 * all UI elements.
	 * 
	 * @param params
	 *            <code>StartParamters</code> object that has to be filled with
	 *            data
	 * @param beamerMode
	 *            specifies if beamer mode is enabled or not
	 */
	public PartitionPanel(final StartParameters params, boolean beamerMode) {
		this.params = params;
		this.beamerMode = beamerMode;
		observationType = params.getObservationType();
		// observations = new HashSet<Partition>();
		this.init();
	}

	@Override
	public final void actionPerformed(final ActionEvent e) {
		Object src = e.getSource();
		if (src instanceof JRadioButton) {
			disableButtons();
			disableCustomizeButtons();
			bForward.setEnabled(true);
			showTable(false);
		}
		// Same Side
		if (src == bSameSide) {
			this.setObservation(SAMESIDE, 0, 0, 0);
		}
		// Side Count
		else if (src == bSideCount) {
			cSideSelection.setEnabled(true);
			this.setObservation(SIDECOUNT,
					cSideSelection.getSelectedIndex() + 1, 0, 0);
		} else if (src == cSideSelection) {
			this.setObservation(SIDECOUNT,
					cSideSelection.getSelectedIndex() + 1, 0, 0);
		}
		// Object Property
		else if (src == bObjectProperty) {
			cObjectSelection.setEnabled(true);
			cPropertySelection.setEnabled(true);
			this.setObservation(PROPERTY, 0,
					cObjectSelection.getSelectedIndex(),
					cPropertySelection.getSelectedIndex() + 1);
		} else if (src == cObjectSelection) {
			cPropertySelection.setModel(new DefaultComboBoxModel(this
					.properties(cObjectSelection.getSelectedIndex())));
			this.setObservation(PROPERTY, 0,
					cObjectSelection.getSelectedIndex(),
					cPropertySelection.getSelectedIndex() + 1);
		} else if (src == cPropertySelection) {
			this.setObservation(PROPERTY, 0,
					cObjectSelection.getSelectedIndex(),
					cPropertySelection.getSelectedIndex() + 1);
		}
		// Sum
		else if (src == bSum) {
			this.setObservation(SUM, 0, 0, 0);
		}
		// own partitioning
		else if (src == bCustomize) {
			bNew.setEnabled(true);
			bFill.setEnabled(true);
			// disable forward button, so the user cannot continue until he
			// defines valid partitions
			bForward.setEnabled(false);

			this.clearObservation();

			table.setModel(new PartitionTableModel(params.getEvents()));
			packColumns(table, 5);
			table.revalidate();

			observationType[0] = CUSTOM;

			showTable(true);
		} else if (src == bNew) {
			this.createNewPartition();
		} else if (src == bRemove) {
			removePartition();
			bForward.setEnabled(false);
		} else if (src == bFill) {
			fillUpPartitions();
		}
		checkIfPartitionsAreValid();
		fireTableDataChanged();
	}

	/**
	 * Calculates a partitioning in which one partition holds all tuple where
	 * x<sub>i</i> shows side <i>s</i>, where <i>i</i> is specified by 'object'
	 * and <i>s</i> is specified by 'property'.
	 * 
	 * @param object
	 *            The object to be viewed.
	 * @param property
	 *            The side the viewed object should show.
	 * 
	 * @return Partitioning of the Events, represented by a Set<Partition>
	 */
	private Set<Partition> calcPropterty(final Integer object,
			final Integer property) {

		Set<Event> events = params.getEvents();
		Map<Boolean, Partition> map = new HashMap<Boolean, Partition>();
		for (Event event : events) {
			boolean hasProperty = event.getTuple().get(object)
					.compareTo(property) == 0;
			if (!map.containsKey(hasProperty)) {
				Partition p = new Partition();
				// build the name depending on the object's properties
				StringBuilder name = new StringBuilder(
						params.getObjectName(object));
				name.append(" ");
				if (hasProperty) {
					name.append(Messages.getString("em",
							"Partition.Name.Property"));
				} else {
					name.append(Messages.getString("em",
							"Partition.Name.PropertyNot"));
				}
				name.append(" ");
				name.append(property);

				p.setName(name.toString());

				map.put(hasProperty, p);
			}
			map.get(hasProperty).addElement(event);
		}

		observationType[0] = PROPERTY;
		observationType[1] = object;
		observationType[2] = property;

		return new HashSet<Partition>(map.values());
	}

	/**
	 * Calculates two partitions, where one holds all doublets, the other one
	 * holds all the rest.
	 * 
	 * @return Partitioning of Events, represented by a Set<Partition>
	 */
	private Set<Partition> calcSameSide() {
		Set<Event> events = params.getEvents();
		Map<Boolean, Partition> map = new HashMap<Boolean, Partition>();

		Vector<Integer> tupel;
		boolean equal;
		Integer comp;

		for (Event event : events) {
			equal = true;
			tupel = event.getTuple();
			comp = tupel.firstElement();

			for (Integer i : tupel) {
				if (i.compareTo(comp) != 0) {
					equal = false;
					break;
				}
			}
			if (map.containsKey(equal)) {
				map.get(equal).addElement(event);
			} else {
				Partition p = new Partition();
				if (equal) {
					p.setName(Messages
							.getString("em", "Partition.Name.Doublet"));
				} else {
					p.setName(Messages.getString("em",
							"Partition.Name.NoDoublet"));
				}
				p.addElement(event);
				map.put(equal, p);
			}
		}
		observationType[0] = SAMESIDE;

		return new HashSet<Partition>(map.values());
	}

	/**
	 * Calculates a partitioning where the count of one side is the criterion.
	 * 
	 * @param side
	 *            The side to be viewed.
	 * 
	 * @return Partitioning of Events, represented by a Set<Partition>
	 */
	private Set<Partition> calcSideCount(final Integer side) {
		Set<Event> events = params.getEvents();
		Vector<Integer> tupel;
		Integer count;
		Map<Integer, Partition> map = new TreeMap<Integer, Partition>();

		for (Event event : events) {
			count = 0;
			tupel = event.getTuple();

			for (Integer i : tupel) {
				if (i.compareTo(side) == 0) {
					count++;
				}
			}

			if (map.containsKey(count)) {
				map.get(count).addElement(event);
			} else {
				Partition p = new Partition();
				p.setName(count
						+ Messages.getString("em", "Partition.Name.SideCount")
						+ " " + side);
				p.addElement(event);
				map.put(count, p);
			}
		}
		observationType[0] = SIDECOUNT;
		observationType[1] = side;

		return new HashSet<Partition>(map.values());
	}

	/**
	 * Calculates a partitioning based on the sum of the shown sides.
	 * 
	 * @return Partitioning of Events, represented by a Set<Partition>
	 */
	private Set<Partition> calcSum() {
		Set<Event> events = params.getEvents();
		Vector<Integer> tupel;
		Integer sum;
		Map<Integer, Partition> map = new TreeMap<Integer, Partition>();

		for (Event event : events) {
			sum = 0;
			tupel = event.getTuple();

			for (Integer i : tupel) {
				sum += i;
			}

			if (map.containsKey(sum)) {
				map.get(sum).addElement(event);
			} else {
				Partition p = new Partition();
				p.setName(Messages.getString("em", "Partition.Name.Sum") + sum);
				p.addElement(event);
				map.put(sum, p);
			}
		}

		observationType[0] = SUM;
		return new HashSet<Partition>(map.values());
	}

	/**
	 * Checks if the current observation set is valid which means all events are
	 * partitioned and each event is only yielded to one partition. Enables the
	 * forward button if the observation set is valid.
	 */
	private void checkIfPartitionsAreValid() {
		int sum = 0;
		Set<Event> all = new HashSet<Event>();
		for (Partition p : observations) {
			sum += p.getElements().size();
			all.addAll(p.getElements());
		}
		if (sum == params.getEvents().size() && all.equals(params.getEvents())) {
			bForward.setEnabled(true);
			bNew.setEnabled(false);
			bFill.setEnabled(false);
		}
	}

	/**
	 * Clear the observations (removes all partitions and deletes all yield that
	 * were set). Sets the
	 */
	private void clearObservation() {
		for (Partition p : observations) {
			p.remove();
		}
		observations.clear();
		PartitionListModel lm = ((PartitionListModel) partitionList.getModel());
		lm.setElements(observations);
	}

	/**
	 * Creates a new <code>Partition</code> object out of the selected table
	 * cells. The created partition is added to the observation set. Asks the
	 * user to enter a name for the partition. The action can be canceled in
	 * this dialog. If no cell is selected, no partition is created.
	 * 
	 * <p>
	 * Enables the remove button if a partition is added to the observation set.
	 */
	private void createNewPartition() {

		if (!(table.getSelecetedEvents().isEmpty())) {
			Partition p = new Partition(table.getSelecetedEvents());
			String name = showNameDialog(Messages.getString("em",
					"PartitionPanel.DefaultPartitionName")
					+ ""
					+ (observations.size() + 1));
			if (name != null) {
				p.setName(name);
				observations.add(p);
				((PartitionListModel) partitionList.getModel())
						.setElements(observations);

				bRemove.setEnabled(true);
			} else {
				p.remove();
			}
			table.clearSelection();
		}
	}

	/**
	 * Disables buttons for the user (e.g. ComboBox)
	 */
	private void disableButtons() {
		cObjectSelection.setEnabled(false);
		cPropertySelection.setEnabled(false);
		cSideSelection.setEnabled(false);

	}

	/**
	 * Disables (grey out) all buttons related to the "own partitioning" option
	 * (e.g. new/delete partition)
	 */
	private void disableCustomizeButtons() {
		if (params.getObjectCount() > 2)
			// bCustomize.setEnabled(false);
			bNew.setEnabled(false);
		bRemove.setEnabled(false);
		bFill.setEnabled(false);
	}

	private void showTable(boolean mode) {
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		if (!mode) {
			bRemove.setVisible(false);

			layout.setHorizontalGroup(layout
					.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addGroup(
							layout.createSequentialGroup()
									.addContainerGap()
									.addGroup(
											layout.createParallelGroup(
													GroupLayout.Alignment.LEADING)
													.addGroup(
															GroupLayout.Alignment.TRAILING,
															layout.createSequentialGroup()
																	.addComponent(
																			selectionPanel,
																			GroupLayout.PREFERRED_SIZE,
																			GroupLayout.DEFAULT_SIZE,
																			GroupLayout.PREFERRED_SIZE)
																	.addPreferredGap(
																			LayoutStyle.ComponentPlacement.RELATED)
																	.addComponent(
																			listPanel,
																			GroupLayout.PREFERRED_SIZE,
																			GroupLayout.DEFAULT_SIZE,
																			Short.MAX_VALUE))
													.addComponent(
															buttonPanel,
															GroupLayout.DEFAULT_SIZE,
															GroupLayout.DEFAULT_SIZE,
															Short.MAX_VALUE))
									.addContainerGap()));

			layout.setVerticalGroup(layout.createParallelGroup(
					GroupLayout.Alignment.LEADING).addGroup(
					GroupLayout.Alignment.TRAILING,
					layout.createSequentialGroup()
							.addContainerGap()
							.addGroup(
									layout.createParallelGroup(
											GroupLayout.Alignment.LEADING)
											.addComponent(selectionPanel,
													GroupLayout.DEFAULT_SIZE,
													GroupLayout.DEFAULT_SIZE,
													Short.MAX_VALUE)
											.addComponent(listPanel,
													GroupLayout.DEFAULT_SIZE,
													GroupLayout.DEFAULT_SIZE,
													Short.MAX_VALUE))
							.addPreferredGap(
									LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(buttonPanel,
									GroupLayout.PREFERRED_SIZE,
									GroupLayout.DEFAULT_SIZE,
									GroupLayout.PREFERRED_SIZE)
							.addContainerGap()));
		} else {
			bRemove.setVisible(true);

			layout.setHorizontalGroup(layout
					.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addGroup(
							layout.createSequentialGroup()
									.addContainerGap()
									.addGroup(
											layout.createParallelGroup(
													GroupLayout.Alignment.LEADING)
													.addGroup(
															GroupLayout.Alignment.TRAILING,
															layout.createSequentialGroup()
																	.addComponent(
																			selectionPanel,
																			GroupLayout.PREFERRED_SIZE,
																			GroupLayout.DEFAULT_SIZE,
																			GroupLayout.PREFERRED_SIZE)
																	.addPreferredGap(
																			LayoutStyle.ComponentPlacement.RELATED)
																	.addComponent(
																			tablePanel,
																			GroupLayout.DEFAULT_SIZE,
																			GroupLayout.DEFAULT_SIZE,
																			Short.MAX_VALUE)
																	.addPreferredGap(
																			LayoutStyle.ComponentPlacement.RELATED)
																	.addComponent(
																			listPanel,
																			GroupLayout.PREFERRED_SIZE,
																			GroupLayout.DEFAULT_SIZE,
																			GroupLayout.PREFERRED_SIZE))
													.addComponent(
															buttonPanel,
															GroupLayout.DEFAULT_SIZE,
															GroupLayout.DEFAULT_SIZE,
															Short.MAX_VALUE))
									.addContainerGap()));

			layout.setVerticalGroup(layout.createParallelGroup(
					GroupLayout.Alignment.LEADING).addGroup(
					GroupLayout.Alignment.TRAILING,
					layout.createSequentialGroup()
							.addContainerGap()
							.addGroup(
									layout.createParallelGroup(
											GroupLayout.Alignment.LEADING)
											.addComponent(selectionPanel,
													GroupLayout.DEFAULT_SIZE,
													GroupLayout.DEFAULT_SIZE,
													Short.MAX_VALUE)
											.addComponent(tablePanel,
													GroupLayout.DEFAULT_SIZE,
													GroupLayout.DEFAULT_SIZE,
													Short.MAX_VALUE)
											.addComponent(listPanel,
													GroupLayout.DEFAULT_SIZE,
													GroupLayout.DEFAULT_SIZE,
													Short.MAX_VALUE))
							.addPreferredGap(
									LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(buttonPanel,
									GroupLayout.PREFERRED_SIZE,
									GroupLayout.DEFAULT_SIZE,
									GroupLayout.PREFERRED_SIZE)
							.addContainerGap()));
		}
	}

	/**
	 * Fills the observation set up with a 'rest' partition, consisting of all
	 * unpartitioned events. Asks the user to enter a name for the partition.
	 * The action can be canceled in this dialog.
	 */
	private void fillUpPartitions() {
		Set<Event> rest = new TreeSet<Event>(table.getUnpartitionedEvents());
		if (!rest.isEmpty()) {
			Partition p = new Partition(rest);

			String name = showNameDialog(Messages.getString("em",
					"PartitionPanel.DefaultPartitionName")
					+ ""
					+ (observations.size() + 1));
			if (name != null) {
				p.setName(name);
				observations.add(p);
				((PartitionListModel) partitionList.getModel())
						.setElements(observations);

				bRemove.setEnabled(true);
			} else {
				p.remove();
			}

			table.clearSelection();
		}
	}

	/**
	 * Fires a TableDataChanged Event for the partition table so that its view
	 * is updated.
	 */
	private void fireTableDataChanged() {
		PartitionTableModel m = (PartitionTableModel) table.getModel();
		m.fireTableDataChanged();
	}

	@Override
	public final JButton getBackButton() {
		return bBack;
	}

	@Override
	public final JButton getForwardButton() {
		return bForward;
	}

	/**
	 * Initialize all UI elements.
	 */
	public final void init() {

		this.initButtonPanel();
		this.initSelectionPanel();
		this.initListPanel();
		this.initTablePanel();

		// initialize buttons and selected combobox
		this.disableCustomizeButtons();
		this.disableButtons();

		this.observations = params.getObservations();

		showTable(false);

		switch (observationType[0]) {
		case SAMESIDE:
			bSameSide.setSelected(true);
			break;
		case SIDECOUNT:
			bSideCount.setSelected(true);
			cSideSelection.setSelectedItem(observationType[1] - 1);
			break;
		case SUM:
			bSum.setSelected(true);
			break;
		case PROPERTY:
			bObjectProperty.setSelected(true);
			cObjectSelection.setSelectedItem(observationType[1]);
			cPropertySelection.setSelectedItem(observationType[2] - 1);
			break;
		case CUSTOM:
			bCustomize.setSelected(true);

			table.setModel(new PartitionTableModel(params.getEvents()));
			bRemove.setEnabled(true);
			bNew.setEnabled(false);
			bFill.setEnabled(false);
			showTable(true);
			break;
		default:
			observations = new HashSet<Partition>();
			bSameSide.setSelected(true);
			this.calcSameSide();
			// initialize list
			this.setObservation(SAMESIDE, 0, 0, 0);

		}
		PartitionListModel lm = ((PartitionListModel) partitionList.getModel());
		lm.setElements(observations);

		this.setBeamerMode(beamerMode);
	}

	/**
	 * Initializes the button panel which contains the 'back' and 'next' button.
	 * The 'back' button is placed on the left side, the next button is place
	 * opposite on the right side.
	 */
	private void initButtonPanel() {
		buttonPanel = new JPanel();

		// labels the buttons in the specified language
		bForward = new JButton(Messages.getString("em", "InputPanel.bForward"));
		bBack = new JButton(Messages.getString("em", "InputPanel.bBack"));

		GroupLayout l = new GroupLayout(buttonPanel);
		buttonPanel.setLayout(l);

		l.setHorizontalGroup(l.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				l.createSequentialGroup()
						.addContainerGap()
						.addComponent(bBack)
						.addPreferredGap(
								LayoutStyle.ComponentPlacement.RELATED, 245,
								Short.MAX_VALUE).addComponent(bForward)
						.addContainerGap()));

		l.setVerticalGroup(l.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						GroupLayout.Alignment.TRAILING,
						l.createSequentialGroup().addGroup(
								l.createParallelGroup(
										GroupLayout.Alignment.BASELINE)
										.addComponent(bBack)
										.addComponent(bForward))));
	}

	/**
	 * Initializes the List Panel which shows the generated Partitions in a
	 * list. Standard representation for Partitions is as a set of tuples.
	 * Alternatively, a user given name of the partition is shown.
	 */
	private void initListPanel() {
		listPanel = new JPanel();
		lList = new JLabel(Messages.getString("em", "PartitionPanel.lList"));
		bRemove = new JButton(
				Messages.getString("em", "PartitionPanel.bRemove"));
		partitionList = new JList();
		JScrollPane sp = new JScrollPane(partitionList);

		listModel = new PartitionListModel();
		partitionList.setModel(listModel);

		partitionList.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = partitionList.locationToIndex(e.getPoint());
					PartitionListModel lm = (PartitionListModel) partitionList
							.getModel();
					Partition p = (Partition) lm.get(index);
					partitionList.ensureIndexIsVisible(index);
					String name = showNameDialog(p.getName());
					if (name != null) {
						p.setName(name);
					}
				}
			}

		});

		GroupLayout layout = new GroupLayout(listPanel);
		listPanel.setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								layout.createParallelGroup(
										GroupLayout.Alignment.TRAILING)
										.addComponent(lList,
												GroupLayout.Alignment.LEADING)
										.addComponent(sp,
												GroupLayout.Alignment.LEADING,
												GroupLayout.DEFAULT_SIZE,
												LISTWIDTH, Short.MAX_VALUE)
										.addComponent(bRemove,
												GroupLayout.Alignment.LEADING,
												GroupLayout.DEFAULT_SIZE,
												LISTWIDTH, Short.MAX_VALUE))
						.addContainerGap()));

		layout.setVerticalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING)
				.addGroup(
						GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(lList)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(sp, GroupLayout.PREFERRED_SIZE,
										284, Short.MAX_VALUE)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(bRemove).addContainerGap()));
		bRemove.addActionListener(this);
	}

	/**
	 * Initializes the Selection Panel which offers the user predefined
	 * partitions.
	 */
	private void initSelectionPanel() {
		selectionPanel = new JPanel();

		lSelection = new JLabel(Messages.getString("em",
				"PartitionPanel.lSelection"));
		// initialize RadioButtons
		bSameSide = new JRadioButton(Messages.getString("em",
				"PartitionPanel.bSameSide"));
		bSideCount = new JRadioButton(Messages.getString("em",
				"PartitionPanel.bSideCount"));
		bObjectProperty = new JRadioButton(Messages.getString("em",
				"PartitionPanel.bObjectProperty"));
		bSum = new JRadioButton(Messages.getString("em", "PartitionPanel.bSum"));
		bCustomize = new JRadioButton(Messages.getString("em",
				"PartitionPanel.bCustomize"));
		// add RadioButtons to ButtonGroup
		ButtonGroup group = new ButtonGroup();
		group.add(bSameSide);
		group.add(bSideCount);
		group.add(bObjectProperty);
		group.add(bSum);
		group.add(bCustomize);
		// create and fill ComboBoxes
		cSideSelection = new JComboBox(this.sides());
		cObjectSelection = new JComboBox(params.getObjectNames());
		cPropertySelection = new JComboBox(this.properties(0));

		GroupLayout layout = new GroupLayout(selectionPanel);
		selectionPanel.setLayout(layout);
		// enable automatic gaps
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.LEADING)
												.addComponent(lSelection)
												.addComponent(bSameSide)
												.addComponent(bSideCount)
												.addGroup(
														layout.createSequentialGroup()
																.addGap(21, 21,
																		21)
																.addComponent(
																		cSideSelection,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE))
												.addComponent(bObjectProperty)
												.addGroup(
														layout.createSequentialGroup()
																.addGap(21, 21,
																		21)
																.addGroup(
																		layout.createParallelGroup(
																				GroupLayout.Alignment.LEADING)
																				.addComponent(
																						cObjectSelection,
																						GroupLayout.PREFERRED_SIZE,
																						GroupLayout.DEFAULT_SIZE,
																						GroupLayout.PREFERRED_SIZE)
																				.addComponent(
																						cPropertySelection,
																						GroupLayout.PREFERRED_SIZE,
																						GroupLayout.DEFAULT_SIZE,
																						GroupLayout.PREFERRED_SIZE)))
												.addComponent(bSum)
												.addComponent(bCustomize))));

		layout.setVerticalGroup(layout
				.createSequentialGroup()
				.addComponent(lSelection)
				.addComponent(bSameSide)
				.addComponent(bSideCount)
				.addComponent(cSideSelection, GroupLayout.PREFERRED_SIZE,
						GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addComponent(bObjectProperty)
				.addComponent(cObjectSelection, GroupLayout.PREFERRED_SIZE,
						GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addComponent(cPropertySelection, GroupLayout.PREFERRED_SIZE,
						GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addComponent(bSum).addComponent(bCustomize));

		layout.linkSize(SwingConstants.HORIZONTAL, cObjectSelection,
				cPropertySelection, cSideSelection);

		bSameSide.addActionListener(this);
		bSideCount.addActionListener(this);
		cSideSelection.addActionListener(this);
		bObjectProperty.addActionListener(this);
		cObjectSelection.addActionListener(this);
		cPropertySelection.addActionListener(this);
		bSum.addActionListener(this);
		bCustomize.addActionListener(this);
	}

	/**
	 * Generates the Table Panel which offers the user the ability to make a
	 * custom partitioning for two objects. Otherwise it won't be active.
	 */
	private void initTablePanel() {
		tablePanel = new JPanel();
		lTable = new JLabel(Messages.getString("em", "PartitionPanel.lTable"));
		bNew = new JButton(Messages.getString("em", "PartitionPanel.bNew"));
		bFill = new JButton(Messages.getString("em", "PartitionPanel.bFill"));

		PartitionTableModel model = new PartitionTableModel(params.getEvents());

		table = new PartitionTable(model);
		table.setTableHeader(null);
		// table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		this.packColumns(table, 5);

		GroupLayout layout = new GroupLayout(tablePanel);
		tablePanel.setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		JScrollPane sp = new JScrollPane(table);

		layout.setHorizontalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup()
								.addContainerGap(34, Short.MAX_VALUE)
								.addComponent(bFill)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(bNew).addContainerGap())
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.LEADING)
												.addComponent(
														sp,
														GroupLayout.PREFERRED_SIZE,
														216, Short.MAX_VALUE)
												.addComponent(lTable))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(lTable)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(sp, GroupLayout.DEFAULT_SIZE,
										247, Short.MAX_VALUE)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.BASELINE)
												.addComponent(bNew)
												.addComponent(bFill))
								.addContainerGap()));

		bNew.addActionListener(this);
		bFill.addActionListener(this);
	}

	/**
	 * Gives a String Array with the possible sides of a given object, e.g.
	 * coin: 'head', 'tail'.
	 * 
	 * @param object
	 *            The index of the viewed object in the experiment.
	 * 
	 * @return Array with String representations of the object's possible sides.
	 */
	private String[] properties(final int object) {
		Integer sides = params.getExperiment().get(object);
		String[] s = new String[sides];
		if (sides == 2) {
			s[0] = Messages.getString("em", "PartitionPanel.coin.head");
			s[1] = Messages.getString("em", "PartitionPanel.coin.tail");
		} else {
			for (int i = 0; i < sides; i++) {
				s[i] = (i + 1) + "";
			}
		}
		return s;
	}

	/**
	 * Removes the selected (in the list) partitions from the current
	 * partitioning and clears the yield. If multiple list entries are selected,
	 * all of them are deleted.
	 */
	private void removePartition() {
		if (partitionList.getSelectedValues() != null) {
			for (Object o : partitionList.getSelectedValues()) {
				Partition p = (Partition) o;
				p.remove();
				observations.remove(p);
			}
			PartitionListModel lm = ((PartitionListModel) partitionList
					.getModel());
			lm.setElements(observations);
			((PartitionTableModel) table.getModel()).fireTableDataChanged();

			bFill.setEnabled(true);
			bNew.setEnabled(true);
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
		beamerMode = modeOn;
		if (beamerMode) {
			for (Component c : selectionPanel.getComponents()) {
				c.setFont(PRESENTATION_FONT);
			}
			table.setFont(PRESENTATION_PARTITION_FONT);
			table.setRowHeight(table.getFont().getSize() + 8);
			this.packColumns(table, 5);
			lTable.setFont(PRESENTATION_FONT);
			partitionList.setFont(PRESENTATION_FONT);

			bRemove.setIcon(REMOVE_ICON);
			bRemove.setText(null);

			bNew.setIcon(PLUS_ICON);
			bNew.setText(null);

			bFill.setIcon(SHIELD_ICON);
			bFill.setText(null);

			bForward.setIcon(FORWARD_ICON);
			bForward.setText(null);
			bBack.setIcon(BACK_ICON);
			bBack.setText(null);

		} else {
			for (Component c : selectionPanel.getComponents()) {
				c.setFont(DEFAULT_FONT);
			}
			table.setFont(DEFAULT_FONT);
			lTable.setFont(DEFAULT_FONT);
			table.setRowHeight(table.getFont().getSize() + 4);
			this.packColumns(table, 5);

			partitionList.setFont(DEFAULT_FONT);

			bRemove.setIcon(REMOVE_ICON_SMALL);
			bRemove.setText(Messages.getString("em", "PartitionPanel.bRemove"));

			bNew.setIcon(PLUS_ICON_SMALL);
			bNew.setText(Messages.getString("em", "PartitionPanel.bNew"));

			bFill.setIcon(SHIELD_ICON_SMALL);
			bFill.setText(Messages.getString("em", "PartitionPanel.bFill"));

			bForward.setText(Messages.getString("em", "InputPanel.bForward"));
			bForward.setIcon(FORWARD_ICON_SMALL);

			bBack.setText(Messages.getString("em", "InputPanel.bBack"));
			bBack.setIcon(BACK_ICON_SMALL);
		}
		table.revalidate();
	}

	/**
	 * Sets the intern partitioning to the specified values. The single
	 * partitions are calculated after this method is called.
	 * 
	 * @param option
	 *            defines the needed values - <i>SAMESIDE</i>, <i>SIDECOUNT</i>,
	 *            <i>SUM</i> and <i>PROPERTY</i> are valid constants
	 * @param side
	 *            number of sides. Required by the option <i>SIDECOUNT</i>
	 * @param object
	 *            index of the viewed object. Required by <i>PROPERTY</i>
	 * @param property
	 *            'side' of the viewed object. Required by <i>PROPERTY</i>
	 */
	private void setObservation(final int option, final Integer side,
			final Integer object, final Integer property) {
		PartitionListModel lm = ((PartitionListModel) partitionList.getModel());
		observations.clear();
		switch (option) {
		case SAMESIDE:
			observations = this.calcSameSide();
			break;
		case SIDECOUNT:
			observations = this.calcSideCount(side);
			break;
		case SUM:
			observations = this.calcSum();
			break;
		case PROPERTY:
			observations = this.calcPropterty(object, property);
		default:
			break;
		}
		lm.setElements(observations);
	}

	/**
	 * @return the name the user entered for this partition - <code>null</code>
	 *         if the user canceled the dialog
	 */
	private String showNameDialog(String defaultName) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String name = (String) JOptionPane.showInputDialog(frame,
				"Geben Sie einen Namen für die Partition ein:",
				"Partition benennen:", JOptionPane.PLAIN_MESSAGE, null, null,
				defaultName);
		return name;
	}

	/**
	 * Gives a String representation of the possible sides, which means the
	 * maximum side count of the experiment's objects.
	 * 
	 * @return Array with String representations of the possible sides.
	 */
	private String[] sides() {
		Integer max = Collections.max(params.getExperiment());
		String[] s = new String[max];
		if (max == 2 && Collections.min(params.getExperiment()) == 2) {
			s[0] = Messages.getString("em", "PartitionPanel.coin.tail");
			s[1] = Messages.getString("em", "PartitionPanel.coin.head");
		} else {
			for (int i = 0; i < max; i++) {
				s[i] = (i + 1) + "";
			}
			if (params.getExperiment().contains(2)) {
				s[0] += "/"
						+ Messages.getString("em", "PartitionPanel.coin.tail");
				s[1] += "/"
						+ Messages.getString("em", "PartitionPanel.coin.head");
			}
		}
		return s;
	}

	/**
	 * Writes the current calculated partitioning to the StartParameters. Has to
	 * be called when the PartitionPanel is left.
	 */
	public final void writeObservation() {
		params.setObservations(observations);
		params.setObservationType(observationType);
	}

	/**
	 * Packs all columns of the specified table with additional width of the
	 * specified margin amount.
	 * 
	 * @param table
	 *            the {@code JTable} whose columns are to be packed
	 * @param margin
	 *            the margin amount
	 */
	public void packColumns(JTable table, int margin) {
		for (int c = 0; c < table.getColumnCount(); c++) {
			packColumn(table, c, margin);
		}
	}

	/**
	 * Sets the preferred width of the visible column specified by
	 * {@code vColIndex}. The column will be just wide enough to show the column
	 * head and the widest cell in the column. Margin pixels are added to the
	 * left and right (resulting in an additional width of 2*margin pixels).
	 * 
	 * @param table
	 *            the {@code JTable} in which the column is located
	 * @param vColIndex
	 *            the index the column that is to be packed
	 * @param margin
	 *            the margin amount for packing
	 */
	public void packColumn(JTable table, int vColIndex, int margin) {
		DefaultTableColumnModel colModel = (DefaultTableColumnModel) table
				.getColumnModel();
		TableColumn col = colModel.getColumn(vColIndex);

		int width = 0;

		// Get width of column header
		TableCellRenderer renderer = col.getHeaderRenderer();
		/*
		 * if (renderer == null) { renderer =
		 * table.getTableHeader().getDefaultRenderer(); } Component comp =
		 * renderer.getTableCellRendererComponent(table, col.getHeaderValue(),
		 * false, false, 0, 0); width = comp.getPreferredSize().width;
		 */

		// Get maximum width of column data
		for (int r = 0; r < table.getRowCount(); r++) {
			renderer = table.getCellRenderer(r, vColIndex);
			Component comp = renderer.getTableCellRendererComponent(table,
					table.getValueAt(r, vColIndex), false, false, r, vColIndex);
			width = Math.max(width, comp.getPreferredSize().width);
		}

		// Add margin
		width += 2 * margin;

		// Set the width
		col.setPreferredWidth(width);
		col.setMinWidth(width);
	}
}
