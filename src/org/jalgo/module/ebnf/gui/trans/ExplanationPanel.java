/**
 * 
 */
package org.jalgo.module.ebnf.gui.trans;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;

import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.gui.EbnfFont;
import org.jalgo.module.ebnf.gui.FontNotInitializedException;
import org.jalgo.module.ebnf.gui.trans.explanations.ExpFinish;
import org.jalgo.module.ebnf.gui.trans.explanations.Explanation;
import org.jalgo.module.ebnf.model.ebnf.Term;

/**
 * This Panel shows the Explanations to the transformation steps
 * 
 * @author Andre Viergutz
 */
@SuppressWarnings("serial")
public class ExplanationPanel extends JPanel {

	private Explanation explanation;
	private Font ebnfFont;

	/**
	 * This is the constructor. It layouts the Panel
	 */
	public ExplanationPanel() {

		this.setLayout(null);
		this.setBackground(Color.WHITE);
		this.setPreferredSize(new Dimension(Short.MAX_VALUE, 150));
		this.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
				Messages.getString("ebnf", "Border_Explanation"),
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Tahoma", 0, 12)));
		try {
			this.ebnfFont = EbnfFont.getFont().deriveFont(Font.PLAIN, 15f);
		} catch (FontNotInitializedException e) {
			System.out.println("DER EBNF-Font ist noch nicht initialisiert");
		}

		this.showExplanation(null, false);

	}

	/**
	 * This function displays an explanation by getting a Term and the
	 * information, whether the algorithm has finished or not
	 * 
	 * @param t an Ebnf <code>Term</code>
	 * @param finish true, of the algorithm has finished
	 */
	public void showExplanation(Term t, boolean finish) {

		this.removeAll();
		if (finish)
			explanation = new ExpFinish(ebnfFont);
		else
			explanation = ExplanationFactory.getExplanation(t, ebnfFont);
		explanation.setLocation(10, 10);
		explanation.setSize(this.getWidth() - 20, 140);
		explanation.setVisible(true);
		this.add(explanation);
		this.repaint();

	}


}
