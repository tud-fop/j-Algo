package org.jalgo.module.hoare.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import org.jalgo.module.hoare.constants.CodeDimension;
import org.jalgo.module.hoare.view.util.VariableSizeButton;

/**
 * This is the Panel which shows the c0 Programm.
 * 
 * @author Antje
 *
 */
public class WSSource extends WSPart {
	private static final long serialVersionUID = -629213617627596386L;
	
	/**
	 * The newline symbol (either \n or \n\r)
	 */
	protected static final String NEW_LINE = System.getProperty("line.separator");
	
	/**
	 * The gui controller.
	 */
	protected final View gui;
	/**
	 * <code>JScrollPane</code> in which the <code>scrollContainer</code> is displayed.
	 */
	protected JScrollPane scrollPane;
	/**
	 * The source code container.
	 */
	protected Container scrollContainer = new JPanel();
	/**
	 * <code>JTextPane</code> where the source code is shown and can be edited.
	 */
	protected JTextPane sourcePane;
	/**
	 * Highlights the source code belonging to the selected cell in the <code>sourcePane</code>.
	 */
	protected HighlightPainter highlightPainter;
	/**
	 * <code>JButton</code> for making the source code editable.
	 */
	protected JButton editButton;
	/**
	 * <code>JButton</code> for parsing the source code.
	 */
	protected JButton parseButton;
	/**
	 * <code>JButton</code> for setting the source code to the current source code from the model.
	 */
	protected JButton rereadButton;
	/**
	 * <code>JPanel</code> for the <code>parseButton</code> and the <code>rereadButton</code>.
	 */
	protected JPanel buttonPanel;
	/**
	 * <code>true</code> if the source code is editable, otherwise <code>false</code>.
	 */
	protected boolean editable = false;
	
	/**
	 * Creates a new instance of <code>WSSource</code>.
	 * @param gui the gui controller
	 */
	public WSSource(final View gui){
		super("source");
		this.gui = gui;
		
		this.setLayout(new BorderLayout());
		
		sourcePane = new JTextPane();
		
		highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.LIGHT_GRAY);

		editButton = new VariableSizeButton(0.2);
		editButton.setText(getMessage("editButtonText"));
		editButton.setToolTipText(getMessage("editButtonTTT"));
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				makeEditable();
			}
		});
		
		parseButton = new VariableSizeButton(0.2);
		parseButton.setText(getMessage("parseButtonText"));
		parseButton.setToolTipText(getMessage("parseButtonTTT"));
		parseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((gui.getModel().getSource()==null)||(gui.getModel().getSource().equals(""))) {
					gui.parseSourceCode(sourcePane.getText());
				}
				else {
					int choice = JOptionPane.showConfirmDialog(gui.getWorkScreen(), getMessage("confirmParsingMessage"), getMessage("confirmParsingMessageTitle"), JOptionPane.OK_CANCEL_OPTION);
					if (choice==JOptionPane.OK_OPTION) {
						makeNotEditable();
						gui.parseSourceCode(sourcePane.getText());
					}
				}
			}
		});
		
		rereadButton = new VariableSizeButton(0.2);
		rereadButton.setText(getMessage("rereadButtonText"));
		rereadButton.setToolTipText(getMessage("rereadButtonTTT"));
		rereadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				readSourceCode();
			}
		});
		
		buttonPanel = new JPanel(new BorderLayout());
		
		scrollContainer.setLayout(new BorderLayout());
		scrollContainer.add(sourcePane, BorderLayout.CENTER);
		
		scrollPane = new JScrollPane();
		JViewport viewp = new JViewport();
		viewp.setView(scrollContainer);
		scrollPane.setViewport(viewp);
		
		add(title, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		makeEditable();
	}
	
	/**
	 * Allows parsing.
	 * This includes showing the <code>parseButton</code>.
	 */
	protected void makeEditable() {
		editable = true;
		unhighlightSourceCode();
		sourcePane.setFocusable(true);
		buttonPanel.removeAll();
		buttonPanel.add(parseButton, BorderLayout.NORTH);
		buttonPanel.add(rereadButton, BorderLayout.SOUTH);
		validate();
	}
	
	/**
	 * Disables parsing.
	 * This includes removing the <code>parseButton</code>.
	 */
	protected void makeNotEditable() {
		editable = false;
		sourcePane.setFocusable(false);
		buttonPanel.removeAll();
		buttonPanel.add(editButton, BorderLayout.CENTER);
		validate();
	}
	
	/**
	 * Sets the source code that is displayed to the new value
	 * @param sourceCode new source code to display
	 */
	public void setSourceCode(String sourceCode) {
		sourcePane.setText(prepareSource(sourceCode));
		if (sourcePane.getText().equals("")) {
			makeEditable();
		}	else {
			makeNotEditable();
		}
	}
	
	/**
	 * Sets the source code that is displayed to the source code of the model
	 */
	public void readSourceCode() {
		if (gui==null) return;
		
		String sourceCode = "";
		if (gui.getModel()!=null){
			sourceCode=gui.getModel().getSource();
		}
		setSourceCode(sourceCode);
		if ( (gui.getWorkScreen()!=null) &&
			    (gui.getWorkScreen().getGraph().getSelectedCell()!=null) ) {
			highlightSourceCode(gui.getWorkScreen().getGraph().getSelectedCell().getID());
		}
		
	}
	
	/**
	 * Returns the source code that is displayed at the moment
	 * @return the source code that is displayed at the moment
	 */
	public String getSourceCode() {
		return extractSourceCode(sourcePane.getText());
	}
	
	/**
	 * Highlights the code belonging to the selected cell.
	 * Is called when a new cell is selected.
	 * @param cell the cell that has been selected
	 */
	public void newCellSelected(MyCell cell) {
		highlightSourceCode(cell.getID());
	}
	
	/**
	 * Is called when all cells have been unselected.
	 */
	public void cellsUnselected() {
		unhighlightSourceCode();
	}

	/**
	 * Highlights the code belonging to the specified verification formula.
	 * @param verificationFormulaID id of the verification formula
	 */
	public void highlightSourceCode(int verificationFormulaID) {
		if (!editable) {
			CodeDimension d = gui.getModel().getVerificationFormula(verificationFormulaID).getCode();
			int start = getNewCodePos(d.start);
			int end = getNewCodePos(d.end)+1; 
			//"+1" because the position of the last letter is given, not the position after the last
			sourcePane.getHighlighter().removeAllHighlights();
			try {
				sourcePane.getHighlighter().addHighlight(start, end, highlightPainter);
			}
			catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Unhighlights all source code.
	 */
	public void unhighlightSourceCode() {
		sourcePane.getHighlighter().removeAllHighlights();
	}

	/**
	 * This method produces a {@link String} with the given number
	 * of space symbols.<br>
	 * Note: If the parameter is less or equal zero the method return
	 * an empty String. 
	 * @param i number of the space symbols to produce
	 * @return a new {@link String} filled with <code>i</code> space symbols
	 */
	private	String praefix(int i){
		if (i>0){
			char[] result=new char[i];
		 Arrays.fill(result,' ');
		 return new String(result);
		} else {
		 return "";	
		}
	}	
	
	/**
	 * Prepares the source code for displaying it.
	 * This includes adding line separators and indents to the source.
	 * @param source the source code to prepare
	 * @return the prepared source code
	 */
	protected String prepareSource(String source){
	 //RegEx Style
		String result=source;
	 result=result.replaceAll("[{]",NEW_LINE+"{"+NEW_LINE);
	 result=result.replaceAll("[;]",";"+NEW_LINE);
	 result=result.replaceAll("[}]","}"+NEW_LINE);
	 String[] temp=result.split(NEW_LINE);
	 result="";
	 int indent=0;
	 for (int i=0;i<temp.length;i++){
	 	if (temp[i].matches(".*[}].*")){
	 		indent-=1;
	 	}
	 	result+=praefix(indent)+temp[i]+NEW_LINE;
	 	if (temp[i].matches(".*[{].*")){
	 		indent+=1;
	 	}
	 }
		return result.trim();
	}
	
	/**
	 * Extracts the original source code from source code prepared for displaying.
	 * This includes removing the line separators.
	 * @param sourceCode the source code prepared for displaying
	 * @return the extracted source code
	 */
	protected String extractSourceCode(String sourceCode) {
		String newSourceCode = "";
		String[] subStrings = sourceCode.split(NEW_LINE);
		for (int i=0;i<subStrings.length;i++) {
			newSourceCode = newSourceCode+subStrings[i].trim();
		}
		return newSourceCode;
	}
    
	/**
	 * Computes the position in the prepared source code from the position of the original source code
	 * @param oldPos position in the original source code
	 * @return position in the prepared source code
	 */
	protected int getNewCodePos(int oldPos) {
		int count=0,indent=0;
		String sourceCodePart = gui.getModel().getSource().substring(0, oldPos);
	 
		Matcher match=Pattern.compile("([{;}])").matcher(sourceCodePart);
		while (match.find()){
   // +1 because NEW_LINE.length() would not work
			if (match.group().equals("}")){
				indent-=1;
				count+=(1+indent);
			} else	if (match.group().equals(";")){				
				count+=(1+indent);
			} else	if (match.group().equals("{")){
				//Because there is no new line when the first Symbol is a {
				if (match.start()==0) count=-1;
	 		count+=2*(1+indent);
	 		indent+=1;
	 	}
	 }
  return oldPos+count+indent;
	}
	
	/**
	 * <code>true</code> until <code>update(o, arg)</code> is called for the first time, then <code>false</code><br>
	 * In the first update the source code should be read if none was entered yet,
	 * no matter if it is editable or not. This is necessary in case the model has
	 * been initialized in the mean time.
	 */
	private boolean firstUpdate = true;
	
	public void update(Observable o, Object arg) {
		if (firstUpdate&&editable) {
			if (getSourceCode().equals("")) {
				readSourceCode();
			}
		}
		else if (!editable) {
			readSourceCode();
		}
	}
	
	/**
	 * Sets the font to the global font of the gui.
	 */
	public void updateFont() {
		title.setFont(gui.getMainFont().deriveFont(Font.BOLD));
		sourcePane.setFont(gui.getSourceCodeFont());
		editButton.setFont(gui.getMainFont());
		parseButton.setFont(gui.getMainFont());
		rereadButton.setFont(gui.getMainFont());
	}
	
}