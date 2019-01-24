package org.jalgo.module.ebnf.gui.wordalgorithm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.model.wordalgorithm.WordAlgoModel;

/**
 * This Panel contains the Explanation during the WordAlgorithm, its warnings an
 * the word generated during algorithm.
 * 
 * @author Claas Wilke
 * 
 */
@SuppressWarnings("serial")
public class ExplanationPanel extends JPanel implements Observer {

	// Font of Explanation
	// private Font explFont;

	// The Strings and TextPanes which contain the Explanation
	private String explanation;

	private String warning;

	private JTextPane explPane;

	private JTextPane warningPane;

	private JPanel motherPanel;

	private JScrollPane scrollPane;

	private static final int FONT_SIZE = 15;

	/**
	 * Constructor to create a new ExplanationPanel.
	 * 
	 * @param explanation
	 *            The initilization value of the explanation.
	 * @param warning
	 *            The initilization value of the warning.
	 * @param output
	 *            The initilization value of the output.
	 */
	public ExplanationPanel(String explanation, String warning, Font ebnfFont) {

		this.setLayout(null);
		this.setBackground(Color.WHITE);

		// this.explFont = ebnfFont.deriveFont(Font.PLAIN, 15f);

		// Frame Border
		this.setMinimumSize(new Dimension(Short.MAX_VALUE, 150));
		this.setBorder(javax.swing.BorderFactory.createTitledBorder(null, " "
				+ Messages.getString("ebnf",
						"WordAlgo.GuiExplanationPanel_Description") + " ",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Tahoma", 0, 12)));

		this.explanation = explanation;
		this.warning = warning;

		init();

	}

	/**
	 * Initializes the Gui of the Panel and sets its layout.
	 * 
	 */
	private void init() {

		// init
		explPane = new JTextPane();
		warningPane = new JTextPane();

		// set
		explPane.setText(explanation);
		explPane.setFont(new Font("Tahoma", Font.PLAIN, FONT_SIZE));

		warningPane.setText("");
		warningPane.setForeground(Color.RED);
		warningPane.setFont(new Font("Tahoma", Font.PLAIN, FONT_SIZE));

		// TextPanes must be disabled, so the user cant change there
		// content
		explPane.setEnabled(false);
		explPane.setDisabledTextColor(Color.BLACK);
		warningPane.setEnabled(false);
		warningPane.setDisabledTextColor(Color.RED);

		// layout
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);

		scrollPane = new JScrollPane();

		motherPanel = new JPanel();
		motherPanel.setBackground(this.getBackground());
		motherPanel.setLayout(new BoxLayout(motherPanel, BoxLayout.Y_AXIS));
		motherPanel.add(explPane);
		motherPanel.add(warningPane);
		motherPanel.setPreferredSize(getMinDimension());

		scrollPane.setViewportView(motherPanel);
		scrollPane.setBackground(this.getBackground());
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		this.add(scrollPane);

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

			// Change explanation, output and Warning
			this.explanation = myModel.getExplanation();
			this.warning = myModel.getWarning();

			motherPanel.setPreferredSize(getMinDimension());

			explPane.setText(explanation);
			warningPane.setText(warning);

			// If the explanation is empty, it is not shown.
			if (explanation.equals("")) {
				explPane.setVisible(false);
			}
			// Else it is shown.
			else {
				explPane.setVisible(true);
			}
			// If the warning is empty, it is not shown.
			if (warning.equals("")) {
				warningPane.setVisible(false);
			}
			// Else it is shown.
			else {
				warningPane.setVisible(true);
			}

			// scrollPane.set
		}
	}

	/**
	 * Calculates the width and height of explanation an returns the Dimension
	 * 
	 * @return The dimension needed to show explPane and warninPane.
	 */
	private Dimension getMinDimension() {

		FontMetrics fontmetrics = this.getFontMetrics(new Font("Tahoma",
				Font.PLAIN, FONT_SIZE));
		int windowWidth = scrollPane.getVisibleRect().width - 15;
		if (windowWidth == 0)
			windowWidth = 700;
		int explLength = fontmetrics.stringWidth(explanation);
		int warningLength = fontmetrics.stringWidth(warning);
		int textHeight = fontmetrics.getHeight();

		int linesOfText = (int) Math.round((explLength / windowWidth) + .5)
				+ (int) Math.round((warningLength / windowWidth) + .5);

		return new Dimension(windowWidth, (int) Math.round(linesOfText
				* textHeight * 1.2));
	}

	/**
	 * Needed beacause the width of Explanation and Warning should be updated.
	 */
	public void repaint() {
		if (scrollPane != null) {
			motherPanel.setPreferredSize(getMinDimension());
		}
		super.repaint();	
	}
}
