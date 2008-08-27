package org.jalgo.module.hoare.view.formula;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;

import org.jalgo.main.util.Messages;
import org.jalgo.main.util.Settings;

/**
 * Concrete editor for formulas.
 * @author Antje
 *
 */
public class ConcreteFormulaEditor extends JFrame implements FormulaEditor {
	
	private static final long serialVersionUID = 7986457509356594920L;
	
	/**
	 * Array of all special characters needed in formulas.
	 */
	protected static final String[] SYMBOLS = {
		Symbols.SYMBOL_AND,
		Symbols.SYMBOL_OR,
		Symbols.SYMBOL_NOT,
		Symbols.SYMBOL_SUM,
		Symbols.SYMBOL_PROD,
		Symbols.SYMBOL_ABS+"x"+Symbols.SYMBOL_ABS,
		Symbols.SYMBOL_LN,
		Symbols.SYMBOL_EXP,
		"x^y",
		Symbols.SYMBOL_SQRT,
		"()"
	};
	
	/**
	 * Background color of the formula preview if there is a parse error for the formula.
	 */
	protected static final Color ERROR_COLOR = new Color(255, 150, 150);
	/**
	 * Map that specifies for each entry of <code>SYMBOLS</code> which string should be inserted into the editor before the selected text.
	 */
	private static final Map<String,String> SYMBOL_REPLACEMENTS_REVERSE_IN_FRONT_OF_SELECTED_TEXT = new HashMap<String,String>();
	/**
	 * Map that specifies for each entry of <code>SYMBOLS</code> which string should be inserted into the editor after the selected text.
	 */
	private static final Map<String,String> SYMBOL_REPLACEMENTS_REVERSE_AFTER_SELECTED_TEXT = new HashMap<String,String>();
	/**
	 * Map that specifies for each entry of <code>SYMBOLS</code> if the selected text should be added between <code>SYMBOL_REPLACEMENTS_REVERSE_BEFORE_TEXT</code> and <code>SYMBOL_REPLACEMENT_REVERSE_AFTER_TEXT</code>.
	 */
	private static final Map<String,Boolean> SYMBOL_REPLACEMENTS_REVERSE_ADD_SELECTED_TEXT = new HashMap<String,Boolean>();
	/**
	 * Map that specifies for each entry of <code>SYMBOLS</code> if the selected text should be added, but there is none there, the default text will be added instead.
	 */
	private static final Map<String,String> SYMBOL_REPLACEMENTS_REVERSE_DEFAULT_TEXT = new HashMap<String,String>();
	
	static {
		SYMBOL_REPLACEMENTS_REVERSE_IN_FRONT_OF_SELECTED_TEXT.put(Symbols.SYMBOL_AND, "&&");
		SYMBOL_REPLACEMENTS_REVERSE_IN_FRONT_OF_SELECTED_TEXT.put(Symbols.SYMBOL_OR, "||");
		SYMBOL_REPLACEMENTS_REVERSE_IN_FRONT_OF_SELECTED_TEXT.put(Symbols.SYMBOL_NOT, "!");
		SYMBOL_REPLACEMENTS_REVERSE_IN_FRONT_OF_SELECTED_TEXT.put(Symbols.SYMBOL_SUM, "Sum(");
		SYMBOL_REPLACEMENTS_REVERSE_IN_FRONT_OF_SELECTED_TEXT.put(Symbols.SYMBOL_PROD, "Product(");
		SYMBOL_REPLACEMENTS_REVERSE_IN_FRONT_OF_SELECTED_TEXT.put(Symbols.SYMBOL_ABS+"x"+Symbols.SYMBOL_ABS, "abs(");
		SYMBOL_REPLACEMENTS_REVERSE_IN_FRONT_OF_SELECTED_TEXT.put(Symbols.SYMBOL_LN, "ln(");
		SYMBOL_REPLACEMENTS_REVERSE_IN_FRONT_OF_SELECTED_TEXT.put(Symbols.SYMBOL_EXP, "exp(");
		SYMBOL_REPLACEMENTS_REVERSE_IN_FRONT_OF_SELECTED_TEXT.put("x^y", "^");
		SYMBOL_REPLACEMENTS_REVERSE_IN_FRONT_OF_SELECTED_TEXT.put(Symbols.SYMBOL_SQRT, "sqrt(");
		SYMBOL_REPLACEMENTS_REVERSE_IN_FRONT_OF_SELECTED_TEXT.put("()", "(");
		
		SYMBOL_REPLACEMENTS_REVERSE_AFTER_SELECTED_TEXT.put(Symbols.SYMBOL_AND, "");
		SYMBOL_REPLACEMENTS_REVERSE_AFTER_SELECTED_TEXT.put(Symbols.SYMBOL_OR, "");
		SYMBOL_REPLACEMENTS_REVERSE_AFTER_SELECTED_TEXT.put(Symbols.SYMBOL_NOT, "");
		SYMBOL_REPLACEMENTS_REVERSE_AFTER_SELECTED_TEXT.put(Symbols.SYMBOL_SUM, ", var, start, end)");
		SYMBOL_REPLACEMENTS_REVERSE_AFTER_SELECTED_TEXT.put(Symbols.SYMBOL_PROD, ", var, start, end)");
		SYMBOL_REPLACEMENTS_REVERSE_AFTER_SELECTED_TEXT.put(Symbols.SYMBOL_ABS+"x"+Symbols.SYMBOL_ABS, ")");
		SYMBOL_REPLACEMENTS_REVERSE_AFTER_SELECTED_TEXT.put(Symbols.SYMBOL_LN, ")");
		SYMBOL_REPLACEMENTS_REVERSE_AFTER_SELECTED_TEXT.put(Symbols.SYMBOL_EXP, ")");
		SYMBOL_REPLACEMENTS_REVERSE_AFTER_SELECTED_TEXT.put("x^y", "");
		SYMBOL_REPLACEMENTS_REVERSE_AFTER_SELECTED_TEXT.put(Symbols.SYMBOL_SQRT, ")");
		SYMBOL_REPLACEMENTS_REVERSE_AFTER_SELECTED_TEXT.put("()", ")");
		
		SYMBOL_REPLACEMENTS_REVERSE_ADD_SELECTED_TEXT.put(Symbols.SYMBOL_AND, false);
		SYMBOL_REPLACEMENTS_REVERSE_ADD_SELECTED_TEXT.put(Symbols.SYMBOL_OR, false);
		SYMBOL_REPLACEMENTS_REVERSE_ADD_SELECTED_TEXT.put(Symbols.SYMBOL_NOT, false);
		SYMBOL_REPLACEMENTS_REVERSE_ADD_SELECTED_TEXT.put(Symbols.SYMBOL_SUM, true);
		SYMBOL_REPLACEMENTS_REVERSE_ADD_SELECTED_TEXT.put(Symbols.SYMBOL_PROD, true);
		SYMBOL_REPLACEMENTS_REVERSE_ADD_SELECTED_TEXT.put(Symbols.SYMBOL_ABS+"x"+Symbols.SYMBOL_ABS, true);
		SYMBOL_REPLACEMENTS_REVERSE_ADD_SELECTED_TEXT.put(Symbols.SYMBOL_LN, true);
		SYMBOL_REPLACEMENTS_REVERSE_ADD_SELECTED_TEXT.put(Symbols.SYMBOL_EXP, true);
		SYMBOL_REPLACEMENTS_REVERSE_ADD_SELECTED_TEXT.put("x^y", false);
		SYMBOL_REPLACEMENTS_REVERSE_ADD_SELECTED_TEXT.put(Symbols.SYMBOL_SQRT, true);
		SYMBOL_REPLACEMENTS_REVERSE_ADD_SELECTED_TEXT.put("()", true);
		
		SYMBOL_REPLACEMENTS_REVERSE_DEFAULT_TEXT.put(Symbols.SYMBOL_AND, "");
		SYMBOL_REPLACEMENTS_REVERSE_DEFAULT_TEXT.put(Symbols.SYMBOL_OR, "");
		SYMBOL_REPLACEMENTS_REVERSE_DEFAULT_TEXT.put(Symbols.SYMBOL_NOT, "");
		SYMBOL_REPLACEMENTS_REVERSE_DEFAULT_TEXT.put(Symbols.SYMBOL_SUM, "expr");
		SYMBOL_REPLACEMENTS_REVERSE_DEFAULT_TEXT.put(Symbols.SYMBOL_PROD, "expr");
		SYMBOL_REPLACEMENTS_REVERSE_DEFAULT_TEXT.put(Symbols.SYMBOL_ABS+"x"+Symbols.SYMBOL_ABS, "x");
		SYMBOL_REPLACEMENTS_REVERSE_DEFAULT_TEXT.put(Symbols.SYMBOL_LN, "x");
		SYMBOL_REPLACEMENTS_REVERSE_DEFAULT_TEXT.put(Symbols.SYMBOL_EXP, "x");
		SYMBOL_REPLACEMENTS_REVERSE_DEFAULT_TEXT.put("x^y", "");
		SYMBOL_REPLACEMENTS_REVERSE_DEFAULT_TEXT.put(Symbols.SYMBOL_SQRT, "x");
		SYMBOL_REPLACEMENTS_REVERSE_DEFAULT_TEXT.put("()", "");
	}
	
	/**
	 * Collection of observers observing the editor.
	 */
	protected final Collection<FormulaEditorObserver> formulaEditorObservers = new ArrayList<FormulaEditorObserver>();
	/**
	 * Name of the variable that is edited.
	 */
	protected final String variableName;
	/**
	 * Value of the variable.
	 */
	protected String formula;
	/**
	 * Panel for the symbol buttons.
	 */
	protected final JPanel buttonPanel;
	/**
	 * Text field for editing the value of the variable
	 */
	protected final JTextPane textField;
	/**
	 * Container in which the preview of the value is shown.
	 */
	protected Container previewContainer = new JPanel(new BorderLayout());
	/**
	 * Label that shows parse error messages.
	 */
	protected JLabel errorLabel = new JLabel();
	/**
	 * Button to apply the change of the value of the variable.
	 */
	protected JButton applyButton;
	/**
	 * The instance of <code>ConcreteFormulaEditor</code> itself.
	 */
	private final ConcreteFormulaEditor mirror = this;
	
	/**
	 * Creates a new editor for the variable with the specified name and the specified formula as initial value for the variable
	 * @param variableName name of the variable
	 * @param initialFormula initial value of the variable
	 */
	public ConcreteFormulaEditor(String variableName, String initialFormula) {
		super();
		this.variableName = variableName;
		String title = Messages.getString("hoare", "view.editVariable");
		title = title.replaceAll("[$]Var", this.variableName);
		setTitle(title);
		this.setAlwaysOnTop(true);
		getContentPane().setLayout(new BorderLayout());
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		getContentPane().add(mainPanel);
		
		// symbol button bar
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		for (int i=0;i<SYMBOLS.length;i++) {
			SymbolButton sb = new SymbolButton(SYMBOLS[i]);
			sb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					notifyFormulaEditorObserversChange();
					previewContainer.validate();
				}
			});
			sb.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.isControlDown()) {
						if (e.getKeyCode()==KeyEvent.VK_ENTER) {
							applyFormula();
						}
					}
				}
			});
			buttonPanel.add(sb);
		}
		mainPanel.add(buttonPanel, BorderLayout.NORTH);
		
		// text field
		textField = new JTextPane();
		textField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				formula = textField.getText();
				if (FormulaViewer.isParsable(getFormula())) {
					applyButton.setEnabled(true);
				}
				else {
					applyButton.setEnabled(false);
				}
				notifyFormulaEditorObserversChange();
			}
		});
		textField.setBorder(new LineBorder(Color.BLACK, 1, false));
		textField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown()) {
					if (e.getKeyCode()==KeyEvent.VK_ENTER) {
						applyFormula();
					}
				}
			}
		});
		mainPanel.add(textField, BorderLayout.CENTER);
		
		// ok button for applying the new formula
		applyButton = new JButton(Messages.getString("main", "DialogConstants.Ok"));
		applyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applyFormula();
			}
		});
		applyButton.setToolTipText(Messages.getString("hoare", "ttt.applyFormulaChange"));
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BorderLayout());
		southPanel.add(previewContainer, BorderLayout.CENTER);
		southPanel.add(applyButton, BorderLayout.EAST);
		
		// set user defined size
		mainPanel.add(southPanel, BorderLayout.SOUTH);
		pack();
		boolean maximized = false;
		int left = 0;
		int top = 0;
		int width = 200;
		int height = 100;
		try {
			maximized = Settings.getBoolean("hoare", "formulaEditor.maximized");
			left = Integer.valueOf(Settings.getString("hoare", "formulaEditor.left"));
			top =  Integer.valueOf(Settings.getString("hoare", "formulaEditor.top"));
			width = Integer.valueOf(Settings.getString("hoare", "formulaEditor.width"));
			height = Integer.valueOf(Settings.getString("hoare", "formulaEditor.height"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		if (maximized) {
			this.setState(JFrame.MAXIMIZED_BOTH);
		}
		else {
			setBounds(left,
				      top,
				      width,
				      height);
		}
		
		// save settings on close
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		
		// add border (necessary because colors may look different, s.t. default borders may not be seen, if a beamer is used)
		mainPanel.setBorder(new LineBorder(Color.BLACK, 1, false));

		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				textField.grabFocus();
			}
		});
		setFormula(initialFormula);
		setVisible(true);
	}
	
	/**
	 * Sets the edited variable to the new value and closes the editor.
	 */
	public void applyFormula() {
		formula = textField.getText();
		if (FormulaViewer.isParsable(getFormula())) {
			notifyFormulaEditorObserversApply();
			close();
			mirror.setVisible(false);
		}
	}
	
	/**
	 * Saves the settings for the editors.
	 * Is called when the <code>ConcreteFormulaEditor</code> is closed.
	 */
	private void close() {
		try {
			if (getState()==JFrame.MAXIMIZED_BOTH) {
				Settings.setBoolean("hoare", "formulaEditor.maximized", true);
			}
			else {
				Settings.setBoolean("hoare", "formulaEditor.maximized", false);
				Point loc = getLocationOnScreen();
				Settings.setString("hoare", "formulaEditor.left", String.valueOf(loc.x));
				Settings.setString("hoare", "formulaEditor.top", String.valueOf(loc.y));
				Settings.setString("hoare", "formulaEditor.width", String.valueOf(getWidth()));
				Settings.setString("hoare", "formulaEditor.height", String.valueOf(getHeight()));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		notifyFormulaEditorObserversClose();
	}
	
	/**
	 * Returns the name of the variable that is edited.
	 * @return the name of the varialbe that is edited.
	 */
	public String getVariableName() {
		return variableName;
	}
	
	/**
	 * Returns the current value of the variable.
	 * @return the current value of the variable
	 */
	public String getFormula() {
		return getExternalString(formula);
	}
	
	/**
	 * Sets the value of the variable to the specified <code>String</code>.
	 * @param formula the new value of the variable.
	 */
	public void setFormula(String formula) {
		this.formula = getInternalString(formula);
		textField.setText(this.formula);
		if (FormulaViewer.isParsable(getFormula())) {
			applyButton.setEnabled(true);
		}
		else {
			applyButton.setEnabled(false);
		}
	}
	
	/**
	 * Change the component the displays the preview of the current value of the variable.
	 * @param preview new component for displaying the preview of the current value of the variable
	 */
	public void setFormulaPreview(Component preview) {
		previewContainer.removeAll();
		previewContainer.add(preview, BorderLayout.CENTER);
		validate();
	}
	
	/**
	 * Button that can be used to insert special characters into the formula.
	 * @author Antje
	 *
	 */
	protected class SymbolButton extends JButton {
		
		private static final long serialVersionUID = -5734541550815247050L;

		/**
		 * Creates a new button for inserting special characters into the formula.
		 * @param symbol special character that will be inserted
		 */
		public SymbolButton(final String symbol) {
			setText(symbol);
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addTextAtCaret(SYMBOL_REPLACEMENTS_REVERSE_IN_FRONT_OF_SELECTED_TEXT.get(symbol), SYMBOL_REPLACEMENTS_REVERSE_AFTER_SELECTED_TEXT.get(symbol), SYMBOL_REPLACEMENTS_REVERSE_ADD_SELECTED_TEXT.get(symbol), SYMBOL_REPLACEMENTS_REVERSE_DEFAULT_TEXT.get(symbol));
					textField.grabFocus();
				}
			});
		}
		
	}

	/**
	 * Sets the font for this formula editor.
	 * @param font the desired Font for this formula editor
	 */
	public void setFont(Font f) {
		super.setFont(f);
		textField.setFont(f);
		applyButton.setFont(f);
		for (Component c : buttonPanel.getComponents()) {
			c.setFont(f);
		}
		for (int i=0; i<previewContainer.getComponentCount(); i++) {
			previewContainer.getComponent(i).setFont(f);
		}
	}
	
	public void addFormulaEditorObserver(FormulaEditorObserver observer) {
		formulaEditorObservers.add(observer);
	}

	public void removeFormulaEditorObserver(FormulaEditorObserver observer) {
		formulaEditorObservers.remove(observer);
	}
	
	/**
	 * Notifies all observers that the value of the variable changed.
	 *
	 */
	protected void notifyFormulaEditorObserversChange() {
		for (FormulaEditorObserver observer : new ArrayList<FormulaEditorObserver>(formulaEditorObservers)) {
			observer.formulaChanged(this);
		}
	}
	
	/**
	 * Notifies all observers that the change of the value of the variable is supposed to be applied.
	 *
	 */
	protected void notifyFormulaEditorObserversApply() {
		for (FormulaEditorObserver observer : new ArrayList<FormulaEditorObserver>(formulaEditorObservers)) {
			observer.applyFormulaChange(this);
		}
	}
	
	/**
	 * Notifies all observers that the editor is closed.
	 *
	 */
	protected void notifyFormulaEditorObserversClose() {
		for (FormulaEditorObserver observer : new ArrayList<FormulaEditorObserver>(formulaEditorObservers)) {
			observer.formulaEditorClosed(this);
		}
	}
	
	/**
	 * Notifies all observers that the variable value has been parsed.
	 * The message may be one of the constant messages specified in <code>FormulaEditor</code>.
	 * @param message String that describes wether there was an error.
	 */
	protected void notifyFormulaEditorObserversParseMessage(String message) {
		for (FormulaEditorObserver observer : new ArrayList<FormulaEditorObserver>(formulaEditorObservers)) {
			observer.receiveParseMessage(this, message);
		}
	}
	
	public void receiveParseMessage(String message) {
		notifyFormulaEditorObserversParseMessage(message);
	}
	
	public void removeParseMessage() {
		Color normalColor = getBackground();
		previewContainer.setBackground(normalColor);
		for (int i=0; i<previewContainer.getComponentCount(); i++) {
			previewContainer.getComponent(0).setBackground(normalColor);
		}
		validate();
	}
	
	public void setParseMessage(String message) {
		previewContainer.setBackground(ERROR_COLOR);
		for (int i=0; i<previewContainer.getComponentCount(); i++) {
			previewContainer.getComponent(0).setBackground(ERROR_COLOR);
		}
		validate();
	}
	
	/**
	 * Adds the specified text at the caret position to the text field, changing the variable value
	 * @param front String that is added in front of the selected text
	 * @param backText String that is added after the selected text
	 * @param addSelectedText if <code>true</code> the selected text is added between <code>beforeText</code> and <code>afterText</code>, otherwise the selected text is deleted
	 * @param defalutText is used as selected text for inserting if no text is selected and <code>addSelectedText</code> is <code>true</code>
	 */
	protected void addTextAtCaret(String frontText, String backText, boolean addSelectedText, String defaultText) {
		String selectedText = textField.getSelectedText();
		String text = frontText;
		if (addSelectedText) {
			if (selectedText!=null) {
				text = text + selectedText;
			}
			else {
				text = text + defaultText;
			}
		}
		text = text + backText;
		int caret = deleteSelected();
		String formula = this.formula;
		formula = formula.substring(0, caret)+
				  text+
				  formula.substring(caret);
		setFormula(formula);
		// if the selected text should be added inside, but there is none there, select the standard text, as the user is likely to delete it immediately
		if (addSelectedText & (selectedText==null)) {
			caret = caret + frontText.length();
			textField.setSelectionStart(caret);
			caret = caret + defaultText.length();
			textField.setSelectionEnd(caret);
		}
		// if there can't be added text, simply put the cursor at the end
		else {
			caret = caret + text.length();
			textField.setCaretPosition(caret);
		}
		notifyFormulaEditorObserversChange();
	}
	
	/**
	 * Deletes the selected text in the text field.
	 * @return the new position of the caret
	 */
	protected int deleteSelected() {
		String formula = this.formula;
		int caret = textField.getCaretPosition();
		int selStart = textField.getSelectionStart();
		int selEnd = textField.getSelectionEnd();
		if (selStart!=selEnd) {
			formula = formula.substring(0, selStart)+
					  formula.substring(selEnd, formula.length());
			setFormula(formula);
			return selStart;
		}
		return caret;
	}

	/**
	 * Expressions that are replaced
	 */
	private static final String[] REPLACE_REGEX = {
			"==",
			"<=",
			">=",
			"!=",
			"=",
			Symbols.SYMBOL_LE,
			Symbols.SYMBOL_GE,
			Symbols.SYMBOL_NOT
	};
	
	/**
	 * Replacements for expressions in REPLACE_REGEX
	 */
	private static final String[] REPLACE_WITH = {
		"**only use = not ==**",
		Symbols.SYMBOL_LE,
		Symbols.SYMBOL_GE,
		Symbols.SYMBOL_NOT,
		"==",
		"<=",
		">=",
		"!="
	};

	/**
	 * Returns another representation of the specified formula in the format other classes see it.
	 * Replaces substrings with the matching <code>REPLACE_REGEX</code> with <code>REPLACE_WITH</code>.
	 * @param formula original <code>String</code>
	 * @return the <code>String</code> representation of the formula as other classes see it.
	 */
	private static String getExternalString(String str) {
		String newStr = str;
		for (int i=0;i<REPLACE_REGEX.length; i++) {
			newStr = newStr.replaceAll(REPLACE_REGEX[i], REPLACE_WITH[i]);
		}
		return newStr;
	}
	
	/**
	 * Changes the formula representation of other classes to the internal format.
	 * Replaces each "==" with "=".
	 * @param str <code>String</code> provided by another class
	 * @return the <code>String</code> in the internal format
	 */
	private static String getInternalString(String str) {
		return str.replaceAll("==", "=");
	}
	
}