package org.jalgo.module.lambda.view;


import java.awt.Component;

import javax.swing.ComboBoxEditor;
import javax.swing.JTextField;
import javax.swing.text.*;

import org.jalgo.module.lambda.Constants;

	/**
	 * The class ExchangeTextField extends JTextField
	 * to make a LAMBDA (Unicode 03BB) out of a "\" when typed.
	 * 
	 * @author Frank
	 *
	 */

public class ExchangeTextField extends JTextField implements ComboBoxEditor {
	private static final long serialVersionUID = 1L;

	public ExchangeTextField() {
        super();
        this.setBorder(null);
    }

	public ExchangeTextField(String s) {
        super(s);
        this.setBorder(null);
   }

	public ExchangeTextField(int cols) {
        super(cols);
        this.setBorder(null);
    }

    protected Document createDefaultModel() {
        return new ExchangeDocument();
    }

    static class ExchangeDocument extends PlainDocument {
		private static final long serialVersionUID = 1L;

		public void insertString(int offs, String str, AttributeSet a) 
            throws BadLocationException {

            if (str == null) {
                return;
            }
            
            /**
             *  Here the Conversion is performed.
             */
            char[] change = str.toCharArray();
            for (int i = 0; i < change.length; i++) {
            	if (change[i] == '\\') change[i] = Constants.LAMBDA.charAt(0);
            }
            super.insertString(offs, new String(change), a);
        }
    }

	public Component getEditorComponent() {
		return this;
	}

	public Object getItem() {
		return this.getText();
	}

	public void setItem(Object item) {
		if (item == null) {
			this.setText("");
		} else {
			this.setText(item.toString());
		}
	}
}