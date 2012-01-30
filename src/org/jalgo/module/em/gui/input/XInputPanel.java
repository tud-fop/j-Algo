package org.jalgo.module.em.gui.input;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import org.jalgo.main.util.Messages;
import org.jalgo.module.em.data.Event;
import org.jalgo.module.em.data.StartParameters;
import org.jalgo.module.em.gui.UIConstants;

/**
 * A JPanel which contains all UI elements for creating a new experiment by
 * adding n-sided objects to the experiment.
 * <p>
 * If there is an icon available, the user will get a graphical representation
 * of the object, if not a textual representation of an 'n-sided dice' is shown.
 * 
 * @author Tobias Nett
 * 
 */
public class XInputPanel extends JPanel implements InputPanel, ActionListener,
		UIConstants {

	private static final ImageIcon ADD_ICON = new ImageIcon(
			Messages.getResourceURL("em", "ButtonIcon.bAdd"));

	private static final ImageIcon ADD_DICE_ICON = new ImageIcon(
			Messages.getResourceURL("em", "ButtonIcon.bAddDice"));

	private static final ImageIcon ADD_COIN_ICON = new ImageIcon(
			Messages.getResourceURL("em", "ButtonIcon.bAddCoin"));

	private static final ImageIcon ADD_ICON_SMALL = new ImageIcon(
			Messages.getResourceURL("em", "ButtonIcon.bAdd.small"));

	private static final ImageIcon ADD_DICE_ICON_SMALL = new ImageIcon(
			Messages.getResourceURL("em", "ButtonIcon.bAddDice.small"));

	private static final ImageIcon ADD_COIN_ICON_SMALL = new ImageIcon(
			Messages.getResourceURL("em", "ButtonIcon.bAddCoin.small"));

	/**
	 * The {@code SideVerifier} accepts only integer value greater 0.
	 * 
	 * @author Tobias Nett
	 * 
	 */
	static class SideVerifier extends InputVerifier {

		@Override
		public boolean shouldYieldFocus(JComponent input) {
			if (!verify(input)) {
				input.requestFocus();
				if (input instanceof JTextComponent) {
					((JTextComponent) input).selectAll();
				}
				return false;
			} else {
				return true;
			}
		}

		@Override
		public boolean verify(JComponent input) {
			boolean wasValid = true;
			int sides = 0;
			try {
				sides = Integer.parseInt(((JTextField) input).getText());
			} catch (NumberFormatException e) {
				wasValid = false;
			}
			if (sides < 1) {
				wasValid = false;
			}
			return wasValid;
		}

	}

	private static final long serialVersionUID = -6075272644553822013L;

	// error codes for input error types
	private static final int FORMAT_ERROR = 0;
	private static final int NEGATIVE_AMOUNT = 1;

	private boolean beamerMode = false;

	// Control Panel
	private JPanel controlPanel;
	private JButton bAdd;
	private JButton bAddDice;
	private JButton bAddCoin;

	// private JButton bRemove;
	private JButton bRemoveLast;
	// Picture Panel
	private List<JLabel> objects;
	private JPanel picturePanel;
	private JPanel imagePane;

	private JLabel lHeading;
	private JLabel description;

	// experiment vector
	private Vector<Integer> experiment;
	// ButtonPanel
	private JPanel buttonPanel;

	private JButton bForward;

	private JTextField tSides;

	private StartParameters params;

	/**
	 * Creates a new XInputPanel object.
	 * 
	 * @param params
	 *            StartParamter object in which the XInput has to be saved
	 * @param beamerMode
	 *            specifies if beamer mode is enabled or not
	 */
	public XInputPanel(StartParameters params, boolean beamerMode) {
		this.params = params;
		objects = new ArrayList<JLabel>();
		experiment = new Vector<Integer>();
		this.beamerMode = beamerMode;
		this.init();
		// restore old experiment from StartParameters
		if (params.getExperiment() != null) {
			this.objectsFromExperiment(params.getExperiment());
		}
	}

	/**
	 * Initializes the images for the objects represented by this experiment
	 * vector.
	 * 
	 * @param experiment
	 *            the vector whose values should be shown in the panel
	 */
	private void objectsFromExperiment(Vector<Integer> experiment) {
		for (Integer sides : experiment) {
			this.addX(sides);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bAddCoin) {
			this.addX(2);
		}
		if (e.getSource() == bAddDice) {
			this.addX(6);
		}
		if (e.getSource() == bAdd || e.getSource() == tSides) {
			this.addObject();
		}
		if (e.getSource() == bRemoveLast) {
			if (objects.size() > 0) {
				imagePane.remove(objects.size() - 1);
				objects.remove(objects.size() - 1);
				experiment.remove(experiment.size() - 1);
			}
			if (experiment.size() < 1)
				bForward.setEnabled(false);
			imagePane.updateUI();
		}
	}

	private void addObject() {
		int errorCode = FORMAT_ERROR;
		try {
			Integer sides = Integer.parseInt(tSides.getText());
			if (sides < 1)
				errorCode = NEGATIVE_AMOUNT;
			this.addX(sides);
		} catch (IllegalArgumentException ex) {
			if (errorCode == NEGATIVE_AMOUNT) {
				JOptionPane.showMessageDialog(null, Messages.getString("em",
						"XInputPanel.ErrorMessageDescriptionNegative"),
						Messages.getString("em",
								"XInputPanel.ErrorMessageTitle"),
						JOptionPane.ERROR_MESSAGE);
			} else if (errorCode == FORMAT_ERROR) {
				JOptionPane.showMessageDialog(null, Messages.getString("em",
						"XInputPanel.ErrorMessageDescription"), Messages
						.getString("em", "XInputPanel.ErrorMessageTitle"),
						JOptionPane.ERROR_MESSAGE);
			}
			tSides.requestFocus();
			tSides.selectAll();
		}
	}

	/**
	 * Adds a new n-sided object to the experiment, where n is given by
	 * <i>sides</i>. The object must have at least 1 side.
	 * 
	 * @param sides
	 *            count of object's sides
	 * @throws IllegalArgumentException
	 *             is raised if {@code sides} is less 1
	 */
	public void addX(int sides) throws IllegalArgumentException {
		if (sides < 1)
			throw new IllegalArgumentException();
		JLabel picLabel = null;
		try {
			picLabel = new JLabel(new ImageIcon(Messages.getResourceURL("em",
					"Object." + sides)));
		} catch (NullPointerException e1) {
			picLabel = new JLabel("W" + sides);
			picLabel.setFont(new Font(picLabel.getFont().getFontName(),
					Font.BOLD, 75));
		}
		objects.add(picLabel);
		imagePane.add(picLabel);
		imagePane.updateUI();
		// add object to experiment vector
		experiment.add(sides);
		// enable forward button
		bForward.setEnabled(true);
	}

	/**
	 * Calculates the cartesian product <i>AxB</i> of a set <i>A</i> and a tupel
	 * <i>B</i>.
	 * 
	 * @param a
	 *            set of vectors, set A
	 * @param b
	 *            tupel of Integer, tupel B
	 * @return the cartesian product of a and b as Set<Vector<Integer>>
	 */
	private Set<Vector<Integer>> cartesianProduct(Set<Vector<Integer>> a,
			Vector<Integer> b) {
		Set<Vector<Integer>> product = new HashSet<Vector<Integer>>();
		for (Vector<Integer> v1 : a) {
			for (Integer i : b) {
				Vector<Integer> v = new Vector<Integer>(v1);
				v.add(i);
				product.add(v);
			}
		}
		return product;
	}

	/**
	 * Because there is no back button in the first input panel, this methods
	 * returns <code>null</code> on every call. Be aware of
	 * <code>NullPointerException</code>.
	 * 
	 * @return null there is no need to go back from here.
	 */
	@Override
	public JButton getBackButton() {
		return null;
	}

	@Override
	public JButton getForwardButton() {
		return bForward;
	}

	/**
	 * Initializes this view and its components. Therefore the picture panel
	 * with the heading is placed on the top, the control elements will be in
	 * the middle and the 'next' button is located at the bottom.
	 */
	public void init() {

		this.initButtonPanel();
		this.initControlPanel();
		this.initPicturePanel();

		// Layout
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);

		layout.setHorizontalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								layout.createParallelGroup(
										GroupLayout.Alignment.TRAILING)
										.addComponent(picturePanel,
												GroupLayout.Alignment.LEADING,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(controlPanel,
												GroupLayout.Alignment.LEADING,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(buttonPanel,
												GroupLayout.Alignment.LEADING,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE))
						.addContainerGap()));

		layout.setVerticalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING)
				.addGroup(
						GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(picturePanel,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(controlPanel,
										GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(buttonPanel,
										GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));

		bForward.setEnabled(false);
		this.setBeamerMode(beamerMode);
	}

	/**
	 * Initializes the button panel which contains the 'next' button. The 'next'
	 * button is placed on the right side.
	 */
	private void initButtonPanel() {
		buttonPanel = new JPanel();

		// labels the button in the specified language
		String bForwardText = Messages.getString("em", "InputPanel.bForward");
		bForward = new JButton(bForwardText);
		bForward.setToolTipText(bForwardText);

		GroupLayout l = new GroupLayout(buttonPanel);
		buttonPanel.setLayout(l);

		l.setHorizontalGroup(l.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				l.createSequentialGroup().addContainerGap(499, Short.MAX_VALUE)
						.addComponent(bForward).addContainerGap()));

		l.setVerticalGroup(l.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						l.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(bForward)));
	}

	/**
	 * Initializes the control panel which contains all control elements for
	 * adding or removing new objects to the experiment.
	 */
	private void initControlPanel() {
		controlPanel = new JPanel();
		tSides = new JTextField();
		tSides.setInputVerifier(new SideVerifier());

		bAddCoin = new JButton(Messages.getString("em", "XInputPanel.bAddCoin"));
		bAddCoin.setToolTipText(Messages
				.getString("em", "XInputPanel.bAddCoin"));
		bAddDice = new JButton(Messages.getString("em", "XInputPanel.bAddDice"));
		bAddDice.setToolTipText(Messages
				.getString("em", "XInputPanel.bAddDice"));
		bAdd = new JButton(Messages.getString("em", "XInputPanel.bAdd"));
		bAdd.setToolTipText(Messages.getString("em", "XInputPanel.bAdd"));
		bRemoveLast = new JButton(Messages.getString("em",
				"XInputPanel.bRemoveLast"));
		bRemoveLast.setToolTipText(Messages.getString("em",
				"XInputPanel.bRemoveLast"));

		GroupLayout layout = new GroupLayout(controlPanel);
		controlPanel.setLayout(layout);

		layout.setHorizontalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(bAddCoin)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(bAddDice)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(bAdd)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(tSides,
										30, 120,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.UNRELATED,
										93, Short.MAX_VALUE)
								.addComponent(bRemoveLast).addContainerGap()));

		layout.setVerticalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup()
						.addContainerGap(GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addGroup(
								layout.createParallelGroup(
										GroupLayout.Alignment.BASELINE)
										.addComponent(bAddCoin)
										.addComponent(bAddDice)
										.addComponent(bAdd)
										.addComponent(tSides,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(bRemoveLast))
						.addContainerGap()));

		layout.linkSize(SwingConstants.VERTICAL, bAdd, tSides);

		bAddCoin.addActionListener(this);
		bAddDice.addActionListener(this);
		bAdd.addActionListener(this);
		bRemoveLast.addActionListener(this);
		tSides.addActionListener(this);
	}

	/**
	 * Initializes the picture panel, which contains a label with the heading of
	 * this section and a scroll pane which will hold the figurative
	 * representation of the objects.
	 */
	private void initPicturePanel() {
		picturePanel = new JPanel();
		lHeading = new JLabel(Messages.getString("em", "XInputPanel.lXInput"));
		description = new JLabel(Messages.getString("em",
				"XInputPanel.description"));
		imagePane = new JPanel();
		JScrollPane sp = new JScrollPane(imagePane);

		GroupLayout layout = new GroupLayout(picturePanel);
		picturePanel.setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						javax.swing.GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.TRAILING)
												.addComponent(
														sp,
														javax.swing.GroupLayout.Alignment.LEADING,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														605, Short.MAX_VALUE)
												.addComponent(
														lHeading,
														javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														description,
														javax.swing.GroupLayout.Alignment.LEADING,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														605, Short.MAX_VALUE))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(lHeading)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(description)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(sp,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										160,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));
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
			bAddCoin.setIcon(ADD_COIN_ICON);
			bAddCoin.setText(null);
			bAddDice.setIcon(ADD_DICE_ICON);
			bAddDice.setText(null);
			bAdd.setIcon(ADD_ICON);
			bAdd.setText(null);
			bRemoveLast.setIcon(REMOVE_ICON);
			bRemoveLast.setText(null);
			tSides.setFont(BEAMER_FONT);

			bForward.setIcon(FORWARD_ICON);
			bForward.setText(null);

			description.setFont(PRESENTATION_FONT);
		} else {
			bAddCoin.setIcon(ADD_COIN_ICON_SMALL);
			bAddCoin.setText(Messages.getString("em", "XInputPanel.bAddCoin"));
			bAddDice.setIcon(ADD_DICE_ICON_SMALL);
			bAddDice.setText(Messages.getString("em", "XInputPanel.bAddDice"));
			bAdd.setIcon(ADD_ICON_SMALL);
			bAdd.setText(Messages.getString("em", "XInputPanel.bAdd"));
			bRemoveLast.setIcon(REMOVE_ICON_SMALL);
			bRemoveLast.setText(Messages.getString("em",
					"XInputPanel.bRemoveLast"));
			tSides.setFont(DEFAULT_FONT);

			bForward.setText(Messages.getString("em", "InputPanel.bForward"));
			bForward.setIcon(FORWARD_ICON_SMALL);

			description.setFont(DEFAULT_FONT);
		}
	}

	/**
	 * Calculates all Events based on the specified objects and writes the into
	 * the StartParameter object.
	 */
	public void writeExperiment() {
		// if the events to this experiment vector are already calculated and
		// saved in StartParameters, this method has nothing to do
		if (experiment.equals(params.getExperiment())) {
			if (params.getEvents() != null)
				return;
		}

		params.setExperiment(experiment);

		Set<Event> events = new TreeSet<Event>();
		Set<Vector<Integer>> tupel = new HashSet<Vector<Integer>>();
		List<Vector<Integer>> start = new ArrayList<Vector<Integer>>();

		for (Integer sides : experiment) {
			Vector<Integer> v = new Vector<Integer>();
			for (int i = 1; i <= sides; i++) {
				v.add(i);
			}
			start.add(v);
		}
		for (Integer i : start.get(0)) {
			Vector<Integer> v = new Vector<Integer>();
			v.add(i);
			tupel.add(v);
		}
		// calculate single event vectors if there is more than one object
		if (experiment.size() > 1) {
			for (int i = 1; i < experiment.size(); i++) {
				tupel = this.cartesianProduct(tupel, start.get(i));
			}
		}
		for (Vector<Integer> t : tupel) {
			events.add(new Event(t, experiment));
		}
		params.setEvents(events);

		// if the event set has to be recalculated, the observation must be
		// cleared.
		params.setObservations(null);
		int[] observationType = { 0, -1, -1};
		params.setObservationType(observationType);
	}
}
