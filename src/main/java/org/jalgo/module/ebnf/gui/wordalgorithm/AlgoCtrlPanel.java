package org.jalgo.module.ebnf.gui.wordalgorithm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jalgo.main.gui.event.StatusLineUpdater;
import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.controller.wordalgorithm.WordGenerator;
import org.jalgo.module.ebnf.controller.wordalgorithm.exceptions.GenerateWordException;
import org.jalgo.module.ebnf.gui.syndia.display.DiagramSize;
import org.jalgo.module.ebnf.model.wordalgorithm.WordAlgoModel;

/**
 * This Panel contains the Textfield to enter a word which should be generated,
 * a Button to start the Algorithm and buttons to undo and redo steps in the
 * algorithm.
 * 
 * @author Claas Wilke
 * 
 */
@SuppressWarnings("serial")
public class AlgoCtrlPanel extends JPanel implements Observer {

	private static int counter = 0;

	private final Color bgcolor = Color.WHITE;

	private GuiController myGuiController;

	// Control to zoom or fit the DrawPanel
	private JSlider zoomer = new JSlider(JSlider.HORIZONTAL);

	private JToggleButton fitToSize = new JToggleButton(new ImageIcon(Messages
			.getResourceURL("ebnf", "Icon.FitToSize")));

	// Start and Stop Button
	private JButton startButton;

	private JButton stopButton;

	private JButton wordButton;

	// Textpane fpr Info, that algorithm has ended
	private JTextPane finishPane;

	// Button to return to SynDiaView
	private JButton returnButton;

	// WordInput
	private JTextField inputWord;

	private JTextPane inputPane;

	private JTextPane outputPane;

	private JTextPane inputDescPane;

	private JTextPane outputDescPane;

	private Font outputFont;

	private static final int FONT_SIZE = 15;
	
	private MouseListener myStatusUpdater;

	/**
	 * Constructor to create a new AlgoCtrlPanel.
	 * 
	 */
	public AlgoCtrlPanel(GuiController myGuiController, Font ebnfFont) {

		this.myGuiController = myGuiController;
		this.outputFont = new Font("Tahoma", Font.PLAIN, FONT_SIZE);

		this.setLayout(null);
		this.setBackground(Color.WHITE);
		this.setBorder(javax.swing.BorderFactory.createTitledBorder(null, " "
				+ Messages.getString("ebnf",
						"WordAlgo.GuiControlPanel_Description") + " ",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Tahoma", 0, 12)));
		
		this.myStatusUpdater = StatusLineUpdater.getInstance();

		init();

	}

	/**
	 * Method initializes the Panel. Creates Buttons and Textfield.
	 * 
	 */
	private void init() {

		// init *************************************************************
		startButton = new JButton(Messages.getString("ebnf",
				"WordAlgo.GuiControlPanel_StartLabel"));
		stopButton = new JButton(Messages.getString("ebnf",
				"WordAlgo.GuiControlPanel_StopLabel"));
		wordButton = new JButton(Messages.getString("ebnf",
				"WordAlgo.GuiControlPanel_RandomWordLabel"));
		returnButton = new JButton(Messages.getString("ebnf",
				"SynDia.Control_ToSynDiaDisplay"));
		inputPane = new JTextPane();
		inputWord = new JTextField();
		outputPane = new JTextPane();
		inputDescPane = new JTextPane();
		outputDescPane = new JTextPane();
		finishPane = new JTextPane();
		finishPane.addMouseListener(myStatusUpdater);

		// set *************************************************************
		startButton.setToolTipText(Messages.getString("ebnf",
				"WordAlgo.GuiControlPanel_StartTooltip"));
		startButton.addMouseListener(myStatusUpdater);
		stopButton.setToolTipText(Messages.getString("ebnf",
				"WordAlgo.GuiControlPanel_StopTooltip"));
		stopButton.addMouseListener(myStatusUpdater);
		returnButton.addMouseListener(myStatusUpdater);
		wordButton.setToolTipText(Messages.getString("ebnf",
				"WordAlgo.GuiControlPanel_RandomWordTooltip"));
		wordButton.addMouseListener(myStatusUpdater);
		stopButton.setVisible(false);

		inputWord.setMinimumSize(new Dimension(200, 25));
		inputWord.setPreferredSize(new Dimension(600, 25));

		fitToSize.setMnemonic(KeyEvent.VK_Z);
		fitToSize.setBorder(new EmptyBorder(3, 3, 3, 3));
		fitToSize.setFocusPainted(false);
		fitToSize.setToolTipText(Messages.getString("ebnf",
				"SynDia.Control_FitToSize"));
		fitToSize.addMouseListener(myStatusUpdater);

		zoomer.setMinimum(10);
		zoomer.setMaximum(50);
		zoomer.setValue(DiagramSize.getFontSize());
		zoomer.setPreferredSize(new Dimension(70, 20));
		zoomer.setBackground(Color.WHITE);

		// TextPane must be disabled, so they cant be edited by the user
		inputPane.setFont(outputFont);
		outputPane.setFont(outputFont);
		finishPane.setFont(outputFont);
		inputPane.setDisabledTextColor(Color.BLACK);
		outputPane.setDisabledTextColor(Color.BLACK);
		finishPane.setDisabledTextColor(Color.RED);
		inputPane.setEnabled(false);
		outputPane.setEnabled(false);
		finishPane.setEnabled(false);

		inputDescPane.setText("Zu erzeugendes Wort: ");
		inputDescPane.setFont(outputFont);
		outputDescPane.setText("Erzeugtes Wort: ");
		outputDescPane.setFont(outputFont);
		inputDescPane.setDisabledTextColor(Color.BLACK);
		outputDescPane.setDisabledTextColor(Color.BLACK);
		inputDescPane.setEnabled(false);
		outputDescPane.setEnabled(false);

		// layout *************************************************************
		// Panels needed to order the elements
		JPanel lineOneLeft = new JPanel();
		JPanel lineOneRight = new JPanel();
		JPanel lineOne = new JPanel();
		JPanel lineTwoLeft = new JPanel();
		JPanel lineTwoRight = new JPanel();
		JPanel lineTwo = new JPanel();
		JPanel lineTwoBorder = new JPanel();
		lineOneLeft.setBackground(bgcolor);
		lineOneRight.setBackground(bgcolor);
		lineOne.setBackground(bgcolor);
		lineTwoLeft.setBackground(bgcolor);
		lineTwoRight.setBackground(bgcolor);
		lineTwo.setBackground(bgcolor);
		lineTwoBorder.setBackground(bgcolor);

		lineOneLeft.add(zoomer);
		lineOneLeft.add(fitToSize);
		lineOneLeft.add(startButton);
		lineOneLeft.add(stopButton);
		lineOneLeft.add(wordButton);
		lineOneLeft.add(finishPane);
		lineOneRight.add(returnButton);
		lineOne.setLayout(new BorderLayout(0, 0));
		lineOne.add(lineOneLeft, BorderLayout.WEST);
		lineOne.add(lineOneRight, BorderLayout.EAST);
		lineTwoLeft.setLayout(new BoxLayout(lineTwoLeft, BoxLayout.Y_AXIS));
		lineTwoLeft.add(inputDescPane);
		lineTwoLeft.add(outputDescPane);
		lineTwoRight.setLayout(new BoxLayout(lineTwoRight, BoxLayout.Y_AXIS));
		lineTwoRight.add(inputWord);
		lineTwoRight.add(inputPane);
		lineTwoRight.add(outputPane);
		lineTwo.setLayout(new BoxLayout(lineTwo, BoxLayout.X_AXIS));
		lineTwo.add(lineTwoLeft);
		lineTwo.add(lineTwoRight);
		lineTwoBorder.setLayout(new BorderLayout(0, 0));
		lineTwoBorder.add(lineTwo, BorderLayout.WEST);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(lineOne);
		this.add(lineTwoBorder);

		initListener();

		wordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				generateWord();
			}
		});

	}

	/**
	 * Initializes Listeners
	 * 
	 */
	private void initListener() {

		// Fit the Size
		fitToSize.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED) {

					// An change zoomer value, if Font size changes.
					int newFontSize = myGuiController.setAutoSize(true);
					zoomer.setValue(newFontSize);

				} else {

					myGuiController.setAutoSize(false);

				}
			}
		});

		// Zoomer
		zoomer.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				fitToSize.setSelected(false);
			}
		});
		zoomer.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				myGuiController.setAutoSize(false);
				myGuiController.updateDrawPanel(zoomer.getValue());
			}
		});

		// Switch to SynDiaView
		returnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				DiagramSize.setFontSize(myGuiController.getFontSize());
				myGuiController.switchToSynDiaView();
			}
		});

		// StartButton
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				startAlgorithm();
			}
		});

		// StopButton
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				while (myGuiController.getWordAlgoController().isUndoPossible()) {
					myGuiController.getWordAlgoController().undo();
				}
			}
		});

		// wordInput.enter
		inputWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				startAlgorithm();
			}
		});
	}

	/**
	 * Called by listeners which peform a beginn of the algorithm.
	 * 
	 */
	private void startAlgorithm() {
		// Checkout if input from Textfield is valid.
		if (myGuiController.getWordAlgoController().isWordValid(
				inputWord.getText())) {
			finishPane.setText("");
			myGuiController.getWordAlgoController()
					.setWord(inputWord.getText());
			myGuiController.getWordAlgoController().startAlgorithm();
		}
		// Inform the user that the entered String is incorrect.
		else {
			finishPane.setText(Messages.getString("ebnf",
					"WordAlgo.Warning_WordNotValid"));
			finishPane.setToolTipText(Messages.getString("ebnf",
					"WordAlgo.Warning_WordNotValidToolTip"));
		}
	}

	/**
	 * Method called, if the Model of the WordAlgorithm has been changed.
	 * 
	 */
	public void update(Observable anObservable, Object arg) {
		// Cast the Observable to WordAlgoModel and
		// update the parameters neede to paint the Panel.
		if (anObservable instanceof WordAlgoModel) {

			WordAlgoModel myModel = (WordAlgoModel) anObservable;

			// Check if algorithm is running and enable and
			// disable the Buttons, TextPanes and the TextField.
			if (myModel.isAlgorithmRunning()) {
				startButton.setEnabled(false);
				stopButton.setEnabled(true);
				startButton.setVisible(false);
				stopButton.setVisible(true);
				wordButton.setVisible(false);
				inputWord.setEnabled(false);
				inputWord.setVisible(false);
				inputPane.setVisible(true);
				finishPane.setText("");
				finishPane.setToolTipText("");
				this.inputPane.setText(myModel.getWord());
				this.outputPane.setText(myModel.getOutput());
			} else if (myModel.isAlgorithmFinished()) {
				startButton.setEnabled(false);
				stopButton.setEnabled(true);
				startButton.setVisible(false);
				stopButton.setVisible(true);
				wordButton.setVisible(false);
				inputWord.setEnabled(false);
				inputWord.setVisible(false);
				inputPane.setVisible(true);
				if (myModel.isFinishedWithSuccess()) {
					finishPane.setText(Messages.getString("ebnf",
							"WordAlgo.GuiControlPanel_FinishSuccess"));
					finishPane.setToolTipText(Messages.getString("ebnf",
							"WordAlgo.GuiControlPanel_FinishSuccess"));

				} else {
					finishPane.setText(Messages.getString("ebnf",
							"WordAlgo.GuiControlPanel_FinishUnsuccess"));
					finishPane.setToolTipText(Messages.getString("ebnf",
							"WordAlgo.GuiControlPanel_FinishUnsuccess"));
				}
				this.inputPane.setText(myModel.getWord());
				this.outputPane.setText(myModel.getOutput());
			} else {
				startButton.setEnabled(true);
				stopButton.setEnabled(false);
				startButton.setVisible(true);
				stopButton.setVisible(false);
				wordButton.setVisible(true);
				inputWord.setEnabled(true);
				inputWord.setVisible(true);
				inputPane.setVisible(false);
				finishPane.setText("");
				finishPane.setToolTipText("");
				this.inputPane.setText("");
				this.outputPane.setText("");
			}

			this.validate();
		}
	}

	/**
	 * Generates a word which is possible in the SyntaxDiagram and puts it into
	 * the Textfield.
	 * 
	 */
	private void generateWord() {
		counter = 0;
		WordGenerator wordGenerator = new WordGenerator(myGuiController
				.getWordAlgoController().getSynDiaSystem());
		String word;
		try {
			while ((word = wordGenerator.start()).equals(inputWord.getText())
					&& counter++ < 10)
				;
			inputWord.setText(word);

		} catch (GenerateWordException e) {
			word = Messages.getString("ebnf",
					"WordAlgo.GuiControlPanel_RandomWordError");
			finishPane.setText(word);
		}
	}
}
