package org.jalgo.module.ebnf.gui.wordalgorithm;

import java.awt.Dimension;
import java.util.Observer;
import java.util.Observable;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.gui.syndia.display.IDrawPanel;
import org.jalgo.module.ebnf.model.wordalgorithm.WordAlgoModel;

/**
 * This Panel contains the Stack and its description during the WordAlgorithm.
 * 
 * @author Claas Wilke
 * 
 */
@SuppressWarnings("serial")
public class StackPanel extends JPanel implements Observer, IDrawPanel {

	// Represents the actual Status of the Stack.
	// (Just a copy of a part of the Model!!!)
	private List varsOnStack;

	// The part of the Stack which should be scrollable
	private JScrollPane scrollPane;

	// Used to paint the Vars on the Stack
	private StackDrawPanel stackDrawPanel;

	/**
	 * Constructor to create a new <code>StackPanel</code>.
	 * 
	 * @param varsOnStack
	 *            The initilization values on the stack.
	 */
	public StackPanel(List varsOnStack) {

		super();

		this.varsOnStack = varsOnStack;

		this.setBackground(BACKGROUND_COLOR);
		// this.setMinimumSize(new Dimension(Short.MAX_VALUE, 150));
		this.setBorder(javax.swing.BorderFactory.createTitledBorder(null, " "
				+ Messages.getString("ebnf",
						"WordAlgo.GuiStackPanel_Description") + " ",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Tahoma", 0, 12)));

		init();

	}

	/**
	 * Initializes the Stack's graphical Components
	 * 
	 */
	private void init() {

		// init
		scrollPane = new JScrollPane();
		stackDrawPanel = new StackDrawPanel(varsOnStack);

		// set
		stackDrawPanel.setVisible(true);

		scrollPane.setViewportView(stackDrawPanel);
		scrollPane.setPreferredSize(new Dimension((int) this.getPreferredSize()
				.getWidth(), (int) this.getMaximumSize().getHeight()));
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBackground(this.getBackground());

		// layout
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(scrollPane);

	}

	/**
	 * Method called, if the Model of the WordAlgorithm has been changed.
	 * 
	 */
	public void update(Observable anObservable, Object arg) {
		// Try to cast the Observable to WordAlgoModel and
		// update the parameters needed to paint the Panel.
		if (anObservable instanceof WordAlgoModel) {

			// The AdressList is going to be updated
			WordAlgoModel myModel = (WordAlgoModel) anObservable;
			varsOnStack = myModel.getAdressNumbersFromStack();

			// The Graphical Stack is updated.
			stackDrawPanel.paintStack(varsOnStack,
					myModel.isStackHighlighted(), myModel.getStackColor());
		}
	}

}
