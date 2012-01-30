package org.jalgo.module.em.gui.input;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.jalgo.main.util.Messages;

/**
 * The {@code YFrequencyCellEditor} is a specialized CellEditor for the
 * frequency input table. It verifies cell inputs via
 * {@link org.jalgo.module.em.gui.input.InputStringParser#parseInputString(String, double, double)}
 * method.
 * 
 * @author Tobias Nett
 * 
 */
public class YFrequencyCellEditor extends DefaultCellEditor {

	private static final long serialVersionUID = 1L;
	private JTextField tf;
	private FrequencyVerifier v;

	/**
	 * Creates a CellEditor, which checks the input using an
	 * <code>InputVerifier</code>. If the entered value is valid or not is
	 * checked when the cell editing is stopped. The cell's values are displayed
	 * in a normal <code>JTextField</code>.
	 */
	public YFrequencyCellEditor() {
		super(new JTextField());
		tf = (JTextField) getComponent();
		v = new FrequencyVerifier();
		tf.setInputVerifier(v);
	}

	@Override
	public boolean stopCellEditing() {
		if (!v.verify(tf)) {
			tf.setBorder(BorderFactory.createLineBorder(Color.red));
			JOptionPane.showMessageDialog(null, Messages.getString("em",
					"YFrequencyPanel.ErrorMessageDescription"), Messages
					.getString("em", "YFrequencyPanel.ErrorMessageTitle"),
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return super.stopCellEditing();
	}

	/**
	 * @author Tobias Nett
	 * 
	 *         Specified <code>InputVerifier</code> for double values entered in
	 *         the <code>YFrequencyTable</code>.
	 */
	static class FrequencyVerifier extends InputVerifier {

		@Override
		public boolean verify(JComponent input) {
			boolean wasValid = true;
			double freq = 0;
//			String text = "0";
			String text = "";
			if (input instanceof JTextComponent){
				JTextComponent textComponent = (JTextComponent) input;
				text = textComponent.getText();
			}
			try {
				freq = InputStringParser.parseInputString(
						text, 0, Double.MAX_VALUE);
			} catch (NumberFormatException e) {
				wasValid = false;
			} catch (IllegalArgumentException e2) {
				wasValid = false;
			}
			if (freq < 0) {
				wasValid = false;
			}
			return wasValid;
		}

	}

}
