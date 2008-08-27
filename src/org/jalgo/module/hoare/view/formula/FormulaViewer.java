package org.jalgo.module.hoare.view.formula;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.constants.ParserAccess;
import org.nfunk.jep.ASTConstant;
import org.nfunk.jep.ASTFunNode;
import org.nfunk.jep.ASTStart;
import org.nfunk.jep.ASTVarNode;
import org.nfunk.jep.Node;
import org.nfunk.jep.Operator;
import org.nfunk.jep.OperatorSet;
import org.nfunk.jep.ParseException;

/**
 * Renders formulas and displays them in a JPanel.
 * @author Antje
 *
 */
public class FormulaViewer extends JLabel {
	private static final long serialVersionUID = -3573478356628706956L;
	
	/**
	 * Possible positions for text.
	 * <code>SUPER</code> means superscript,
	 * <code>NORMAL</code> means normal script,
	 * and <code>SUB</code> means subscript.
	 * @author Antje
	 *
	 */
	protected enum YPos {
		/**
		 * higher than super script (e.g. over a sum or product symbol)
		 */
		SUPERSUPER,
		/**
		 * super script
		 */
		SUPER,
		/**
		 * normal script
		 */
		NORMAL,
		/**
		 * sub script
		 */
		SUB,
		/**
		 * lower than sub script (e.g. under a sum or product symbol)
		 */
		SUBSUB,
		/**
		 * lowest possible position (used to determine the height of the component)
		 */
		MAX
	}
	
	/**
	 * Set of all operators.
	 */
	protected static final OperatorSet operatorSet = new OperatorSet();
	/**
	 * String for an opening bracket.
	 */
	protected static final String BRACKET_OPEN = "(";
	/**
	 * String for a closing bracket.
	 */
	protected static final String BRACKET_CLOSE = ")";
	/**
	 * Array of all possible unary operators.
	 */
	protected static final String[] UNARY_OPERATORS = {
		operatorSet.getNot().getName(),
		operatorSet.getUMinus().getName()
	};
	/**
	 * Array of all possible binary operators.
	 */
	protected static final String[] BINARY_OPERATORS = {
		operatorSet.getAssign().getName(),
		operatorSet.getEQ().getName(),
		operatorSet.getNE().getName(),
		operatorSet.getGT().getName(),
		operatorSet.getGE().getName(),
		operatorSet.getLT().getName(),
		operatorSet.getLE().getName(),
		operatorSet.getAdd().getName(),
		operatorSet.getSubtract().getName(),
		operatorSet.getMultiply().getName(),
		operatorSet.getDivide().getName(),
		operatorSet.getMod().getName(),
		operatorSet.getPower().getName(),
		operatorSet.getAnd().getName(),
		operatorSet.getOr().getName()
	};
	/**
	 * Array of all possible binary operators that don't require brackets.
	 */
	protected static final String[] BINARY_OPERATORS_WITHOUT_BRACKETS = {
		operatorSet.getAnd().getName(),
		operatorSet.getAssign().getName(),
		operatorSet.getEQ().getName(),
		operatorSet.getNE().getName(),
		operatorSet.getGT().getName(),
		operatorSet.getGE().getName(),
		operatorSet.getLT().getName(),
		operatorSet.getLE().getName()
	};
	/**
	 * Array of all possible unary functions.
	 */
	protected static final String[] UNARY_FUNCTIONS = {
		"abs",
		"ln",
		"exp",
		"sqrt"
	};
	/**
	 * Array containing the functions sum and product.
	 */
	protected static final String[] SUM_OR_PROD = {
		"Sum",
		"Product"
	};
	/**
	 * Priority order for all operators and functions (starting with the highest priority).
	 */
	protected static final String[] PRIORITY = {
		operatorSet.getPower().getName(),
		"sqrt",
		operatorSet.getDivide().getName(),
		operatorSet.getMultiply().getName(),
		operatorSet.getMod().getName(),
		"Product",
		operatorSet.getSubtract().getName(),
		operatorSet.getAdd().getName(),
		"Sum",
		operatorSet.getUMinus().getName(),
		"ln",
		"abs",
		operatorSet.getLT().getName(),
		operatorSet.getLE().getName(),
		operatorSet.getGT().getName(),
		operatorSet.getGE().getName(),
		operatorSet.getEQ().getName(),
		operatorSet.getAssign().getName(),
		operatorSet.getNE().getName(),
		operatorSet.getNot().getName(),
		operatorSet.getAnd().getName(),
		operatorSet.getOr().getName()
	};
	/**
	 * List of all binary operators that are not assciative
	 */
	protected static final String[] NOT_ASSOCIATIVE = {
		operatorSet.getPower().getName(),
		operatorSet.getDivide().getName(),
		operatorSet.getSubtract().getName(),
	};
	
	/**
	 * <code>Graphics</code> where the formula is displayed.
	 */
	protected Graphics2D g;
	/**
	 * Image of the <code>Graphics</code> of the last display of the formula.
	 */
	protected Image img;
	/**
	 * Top node of the parsed formula.
	 */
	private Node top;
	/**
	 * <code>FormulaEditor</code> the <code>FormulaViewer</code> belongs to
	 * <code>null</code> if it belongs to no <code>FormulaEditor</code>.
	 */
	protected FormulaEditor editor = null;
	/**
	 * formula that is displayed.
	 */
	protected String formula;
	/**
	 * x position in <code>g</code> where the last writing on <code>YPos.NORMAL</code> ended.
	 */
	private double currentPosX = 0;
	/**
	 * x position where the last writing on <code>YPos.SUB</code> ended relative to <code>currentPosX</code>.
	 */
	private double currentRelSuperSuperPosX = 0;
	/**
	 * x position where the last writing on <code>YPos.SUPER</code> ended relative to <code>currentPosX</code>.
	 */
	private double currentRelSubSubPosX = 0;
	/**
	 * Distance of the super script from the upper border, relative to the size.
	 */
	private double yPosSuperFactor = 0;
	/**
	 * Distance of the normal script from the super script, relative to the size.
	 */
	private double yPosNormalFactor = 0.02;
	/**
	 * Distance of the sub script from the normal script, relative to the size.
	 */
	private double yPosSubFactor = 0.02;
	/**
	 * super super script font size = <code>fontSize</code> * <code>sizeSuperSuperFactor</code>
	 */
	private double sizeSuperSuperFactor = 0.75;
	/**
	 * super script font size = <code>fontSize</code> * <code>sizeSuperFactor</code>
	 */
	private double sizeSuperFactor = 0.75;
	/**
	 * sub script font size = <code>fontSize</code> * <code>sizeSubFactor</code>
	 */
	private double sizeSubFactor = 0.75;
	/**
	 * sub sub script font size = <code>fontSize</code> * <code>sizeSubSubFactor</code>
	 */
	private double sizeSubSubFactor = 0.75;
	/**
	 * Color of the font.
	 */
	private Color color = Color.BLACK;
	/**
	 * Font size of the font in normal script.
	 */
	private int fontSize = 12;
	/**
	 * <code>true</code> if the formula changed since the last rendering, otherwise <code>false</code>.
	 * After intialization and after the setting of the <code>FormulaEditor</code> it is always <code>false</code>.
	 * It is only used for knowing when to draw the PARSE_ERROR_SUBSTITUTION_TEXT.
	 */
	protected boolean changed = false;
	/**
	 * If <code>true</code> something is drawn, otherwise not.
	 * This is used to calculate the length of a string without actually drawing it.
	 */
	private boolean enableDrawing = true;
	/**
	 * Name of the variable that is displayed.
	 */
	protected String variableName;
	/**
	 * <code>true</code> if the instance is still initializing, otherwise it's <code>false</code>.
	 */
	protected boolean init = true;
	/**
	 * Text that is shown instead of the variable value, if there was a parse error for the value on initialization or setting of the <code>FormulaEditor</code>.
	 */
	public static final String PARSE_ERROR_SUBSTITUTION_TEXT;
	/**
	 * If <code>true</code> the <code>PARSE_ERROR_SUBSTITUTION_TEXT</code> will be drawn, otherwise the formula will be drawn.
	 */
	protected boolean drawParseErrorSubstitutionText = false;
	/**
	 * <code>String</code> that is written in front of the Formula
	 */
	protected String preString = "";
	/**
	 * <code>String</code> that is written behind the Formula
	 */
	protected String postString = "";
	
	static {
		String temp = "";
		try {
			temp = Messages.getString("hoare", "view.parseErrorFormulaSubstitution");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		PARSE_ERROR_SUBSTITUTION_TEXT = temp;
	}
	/**
	 * Creates a new <code>FormulaViewer</code> displaying the empty <code>String</code>.
	 *
	 */
	public FormulaViewer() {
		this("");
	}
	
	/**
	 * Creates a new <code>FormulaViewer</code> displaying the specified formula.
	 * @param formula initial formula to display
	 */
	public FormulaViewer(String formula) {
		setFormula(formula);
		changed = false;
		parse();
		setFontSize(20);
		init = false;
	}
	
	/**
	 * Creates a new <code>FormulaViewer</code> displaying the specified formula with the specified variable name.
	 * @param formula initial formula to display
	 * @param variableName name of the variable that is displayed
	 */
	public FormulaViewer(String formula, String variableName) {
		this(formula);
		setVariableName(variableName);
	}
	
	/**
	 * Creates a new <code>FormulaViewer</code> with the specified font displaying the empty string.
	 * @param font font the <code>FormulaViewer</code> will use to render the formula
	 */
	public FormulaViewer(Font font) {
		this();
		setFont(font);
	}
	
	/**
	 * Creates a new <code>FormulaViewer</code> with the specified font displaying the specified formula
	 * @param formula initial formula to display
	 * @param font font the <code>FormulaViewer</code> will use to render the formula
	 */
	public FormulaViewer(String formula, Font font) {
		this(formula);
		setFont(font);
	}

	/**
	 * Creates a new <code>FormulaViewer</code> displaying the specified formula with the specified variable name.
	 * @param font font the <code>FormulaViewer</code> will use to render the formula
	 * @param variableName name of the variable that is displayed
	 */
	public FormulaViewer(Font font, String variableName) {
		this (font);
		setVariableName(variableName);
	}
	
	/**
	 * Creates a new <code>FormulaViewer</code> displaying the specified formula with the specified variable name.
	 * @param formula initial formula to display
	 * @param font font the <code>FormulaViewer</code> will use to render the formula
	 * @param variableName name of the variable that is displayed
	 */
	public FormulaViewer(String formula, Font font, String variableName) {
		this (formula, font);
		setVariableName(variableName);
	}
	
	/**
	 * Sets the <code>FormulaEditor</code> to the specified one.
	 * @param editor the new <code>FormulaEditor</code>
	 */
	public void setFormulaEditor(FormulaEditor editor) {
		this.editor = editor;
		setFormula(editor.getFormula());
		drawParseErrorSubstitutionText = false;
		init = true;
		parse();
		init = false;
		editor.addFormulaEditorObserver(new FormulaEditorObserver() {

			public void applyFormulaChange(FormulaEditor editor) {
				// do nothing
			}

			public void formulaChanged(FormulaEditor editor) {
				setFormula(editor.getFormula());
				parse();
				repaint();
			}

			public void formulaEditorClosed(FormulaEditor editor) {
				// do nothing
			}

			public void receiveParseMessage(FormulaEditor editor, String message) {
				// do nothing
			}
			
		});
		changed = false;
	}
	
	/**
	 * Sets the <code>String</code> that is written in front of the Formula to the specified value
	 * @param preString <code>String</code> that is written in front of the Formula
	 */
	public void setPreString(String preString) {
		this.preString = preString;
	}

	/**
	 * Sets the <code>String</code> that is written behind the Formula to the specified value
	 * @param postString <code>String</code> that is written behind the Formula
	 */
	public void setPostString(String postString) {
		this.postString = postString;
	}
	
	/**
	 * Parses the current formula.
	 *
	 */
	protected void parse() {
		if (formula.equals("")) {
			top = null;
			if (editor!=null) {
				editor.receiveParseMessage(FormulaEditor.MESSAGE_OKAY);
			}
		}
		else {
			try {
				top = ParserAccess.parse(formula);
				if (editor!=null) {
					editor.receiveParseMessage(FormulaEditor.MESSAGE_OKAY);
				}
			}
			catch (ParseException e) {
				handleParseException();
			}
			catch (Exception e) {
				handleParseException();
			}
			catch (Error e) {
				handleParseException();
			}
		}
	}

	/**
	 * Parses the current formula.
	 *
	 */
	protected static boolean isParsable(String formula) {
		if (formula.equals("")) {
			return true;
		}
		try {
			ParserAccess.parse(formula);
			return true;
		}
		catch (ParseException e) {
			return false;
		}
		catch (Exception e) {
			return false;
		}
		catch (Error e) {
			return false;
		}
	}
	
	/**
	 * Is called if an exception occurs while parsing.
	 */
	protected void handleParseException() {
		if (editor!=null) {
			editor.receiveParseMessage(FormulaEditor.MESSAGE_ERROR);
		}
		if (init) {
			drawParseErrorSubstitutionText = true;
		}
	}
	
	/**
	 * Paints the formula on the <code>JPanel</code>.
	 */
	protected void paintComponent(Graphics g) {
		this.g = (Graphics2D)g;
		super.paintComponent(this.g);
		// make the text borders smooth
		this.g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		this.g.setColor(color);
		// set the Font of the Graph2D to the super font
		this.g.setFont(super.getFont());
		// start drawing from the very left
		currentPosX = 0;
		// draw the preString
		drawString(preString, YPos.NORMAL);
		// draw the variable name if there is any
		if (getVariableName()!=null) {
			drawString(getVariableName(), YPos.NORMAL);
			drawString(" := ", YPos.NORMAL);
		}
		// draw the formula
		if (drawParseErrorSubstitutionText) {
			if (changed) {
				drawParseErrorSubstitutionText = false;
			}
			else {
				drawString(PARSE_ERROR_SUBSTITUTION_TEXT, YPos.NORMAL);
			}
		}
		else if (top==null) {
			// draw nothing
		}
		else {
			drawNode(top, YPos.NORMAL, false);
		}
		// draw the postString
		drawString(postString, YPos.NORMAL);
		// set the width to the new length of the formula
		fitSize();
		changed = false;
	}
	
	/**
	 * Draws the node
	 * @param node the node to draw
	 * @param yPos the position where the node will be drawn
	 * @param allowBrackets if <code>false</code> no bracktes will be drawn, otherwise they may be drawn, depending of the kind of node
	 * @return the width that is needed to draw the node
	 */
	protected double drawNode(Node node, YPos yPos, boolean drawBrackets) {
		double result = 0;
		if (node instanceof ASTStart) {
			result = drawNode(node.jjtGetChild(0), yPos, false);
		}
		else if (node instanceof ASTConstant) {
			result = drawString(getConstantString((ASTConstant)node), yPos);
		}
		else if (node instanceof ASTVarNode) {
			result = drawVariable((ASTVarNode)node, yPos);
		}
		else if (node instanceof ASTFunNode) {
			ASTFunNode f = (ASTFunNode)node;
			if (isUnaryOperator(f.getName())) {
				result = drawUnaryOperation(f, yPos, drawBrackets);
			}
			else if (isBinaryOperator(f.getName())) {
				result = drawBinaryOperation(f, yPos, drawBrackets);
			}
			else if (isUnaryFunction(f.getName())) {
				result = drawUnaryFunction(f, yPos, drawBrackets);
			}
			else if (isSumOrProd(f.getName())) {
				result = drawSumOrProd(f, yPos, drawBrackets);
			}
		}
		return result;
	}
	
	/**
	 * Draws a variable.
	 * @param v the variable node
	 * @param yPos the position where the node will be drawn
	 * @return the width that is needed to draw the node
	 */
	protected double drawVariable(ASTVarNode v, YPos yPos) {
		return drawString(getVarString(v), yPos);
	}
	
	/**
	 * Draws a unary operation
	 * @param f the node with the operation its parameter
	 * @param yPos the position where the node will be drawn
	 * @return the width that is needed to draw the node
	 */
	protected double drawUnaryOperation(ASTFunNode f, YPos yPos, boolean drawBrackets) {
		if (f.getName().equals(operatorSet.getNot())) {
			drawBrackets = false;
		}
		double length = 0;
		if (drawBrackets) {
			length = length + drawString(BRACKET_OPEN, yPos);
		}
		length = length + drawString(getOperatorString(f.getOperator()), yPos);
		length = length + drawNode(f.jjtGetChild(0), yPos, areBracketsNecessary(f, 0));
		if (drawBrackets) {
			length = length + drawString(BRACKET_CLOSE, yPos);
		}
		return length;
	}
	
	/**
	 * Draws a binary operation
	 * @param f the node with the operation its parameters
	 * @param yPos the position where the node will be drawn
	 * @return the width that is needed to draw the node
	 */
	protected double drawBinaryOperation(ASTFunNode f, YPos yPos, boolean drawBrackets) {
		if (f.getOperator().getName().equals(operatorSet.getPower().getName())&(yPos==YPos.NORMAL)) {
			double length = 0;
			if (drawBrackets) {
				length = length + drawString(BRACKET_OPEN, YPos.NORMAL);
			}
			length = length + drawNode(f.jjtGetChild(0), YPos.NORMAL, areBracketsNecessary(f, 0));
			length = length + drawNode(f.jjtGetChild(1), YPos.SUPER, false);
			if (drawBrackets) {
				length = length + drawString(BRACKET_CLOSE, YPos.NORMAL);
			}
			return length;
		}
		if (f.getOperator().getName().equals(operatorSet.getSubtract().getName())) {
			double length = 0;
			if (drawBrackets) {
				length = length + drawString(BRACKET_OPEN, yPos);
			}
			boolean drawBracketsFirstChild = areBracketsNecessary(f, 0);
			Node firstChild = f.jjtGetChild(0);
			if (firstChild instanceof ASTFunNode) {
				if (((ASTFunNode)firstChild).getName().equals(operatorSet.getAdd().getName())) {
					drawBracketsFirstChild = false;
				}
			}
			length = length + drawNode(f.jjtGetChild(0), yPos, drawBracketsFirstChild);
			length = length + drawString(getOperatorString(f.getOperator()), yPos);
			length = length + drawNode(f.jjtGetChild(1), yPos, areBracketsNecessary(f, 1));
			if (drawBrackets) {
				length = length + drawString(BRACKET_CLOSE, yPos);
			}
			return length;
		}
		if (f.getOperator().getName().equals(operatorSet.getDivide().getName())) {
			double length = 0;
			if (drawBrackets) {
				length = length + drawString(BRACKET_OPEN, yPos);
			}
			boolean drawBracketsFirstChild = areBracketsNecessary(f, 0);
			Node firstChild = f.jjtGetChild(0);
			if (firstChild instanceof ASTFunNode) {
				if (((ASTFunNode)firstChild).getName().equals(operatorSet.getMultiply().getName())) {
					drawBracketsFirstChild = false;
				}
			}
			length = length + drawNode(f.jjtGetChild(0), yPos, drawBracketsFirstChild);
			length = length + drawString(getOperatorString(f.getOperator()), yPos);
			length = length + drawNode(f.jjtGetChild(1), yPos, areBracketsNecessary(f, 1));
			if (drawBrackets) {
				length = length + drawString(BRACKET_CLOSE, yPos);
			}
			return length;
		}
		else if (isBinaryOperatorWithoutBrackets(f.getName())||(!drawBrackets)) {
			return drawBinaryOperationWithoutBrackets(f, yPos);
		}
		else {
			return drawBinaryOperationWithBrackets(f, yPos);
		}
	}
	
	/**
	 * Draws a binary operation with brackets
	 * @param f the node with the operation its parameters
	 * @param yPos the position where the node will be drawn
	 * @return the width that is needed to draw the node
	 */
	protected double drawBinaryOperationWithBrackets(ASTFunNode f, YPos yPos) {
		double length = 0;
		length = length + drawString(BRACKET_OPEN, yPos);
		length = length + drawBinaryOperationWithoutBrackets(f, yPos);
		length = length + drawString(BRACKET_CLOSE, yPos);
		return length;
	}
	
	/**
	 * Draws a binary operation without brackets
	 * @param f the node with the operation its parameters
	 * @param yPos the position where the node will be drawn
	 * @return the width that is needed to draw the node
	 */
	protected double drawBinaryOperationWithoutBrackets(ASTFunNode f, YPos yPos) {
		double length = 0;
		length = length + drawNode(f.jjtGetChild(0), yPos, areBracketsNecessary(f, 0));
		length = length + drawString(getOperatorString(f.getOperator()), yPos);
		length = length + drawNode(f.jjtGetChild(1), yPos, areBracketsNecessary(f, 1));
		return length;
	}
	
	/**
	 * Draws a unary function
	 * @param f the node with the function its parameter
	 * @param yPos the position where the node will be drawn
	 * @return the width that is needed to draw the node
	 */
	protected double drawUnaryFunction(ASTFunNode f, YPos yPos, boolean drawBrackets) { 
		if (f.getName().equals("abs")) {
			double length = 0;
			length = length + drawString("|", yPos);
			length = length + drawNode(f.jjtGetChild(0), yPos, false);
			length = length + drawString("|", yPos);
			return length;
		}
		else if (f.getName().equals("sqrt")) {
			double length = 0;
			if (drawBrackets) {
				length = length + drawString(BRACKET_OPEN, yPos);
			}
			length = length + drawString(getFunctionString(f.getName()), yPos);
			length = length + drawNode(f.jjtGetChild(0), yPos, areBracketsNecessary(f, 0));
			if (drawBrackets) {
				length = length + drawString(BRACKET_CLOSE, yPos);
			}
			return length;
		}
		else if (f.getName().equals("exp")&(yPos==YPos.NORMAL)) {
			double length = 0;
			if (drawBrackets) {
				length = length + drawString(BRACKET_OPEN, YPos.NORMAL);
			}
			length = length + drawString("e", YPos.NORMAL);
			length = length + drawNode(f.jjtGetChild(0), YPos.SUPER, false);
			if (drawBrackets) {
				length = length + drawString(BRACKET_CLOSE, YPos.NORMAL);
			}
			return length;
		}
		else {
			double length = 0;
			length = length + drawString(getFunctionString(f.getName()), yPos);
			length = length + drawString(BRACKET_OPEN, yPos);
			length = length + drawNode(f.jjtGetChild(0), yPos, false);
			length = length + drawString(BRACKET_CLOSE, yPos);
			return length;
		}
	}

	/**
	 * Draws a sum or a product function
	 * @param f the node with the function its parameters
	 * @param yPos the position where the node will be drawn
	 * @return the width that is needed to draw the node
	 */
	protected double drawSumOrProd(ASTFunNode f, YPos yPos, boolean drawBrackets) {
		double result = 0;
		// draw opening bracket for the sum if necessary
		if (drawBrackets&&(yPos==YPos.NORMAL)) {
			drawString(BRACKET_OPEN, YPos.NORMAL);
		}
		// draw nice sum if in normal position
		if (yPos==YPos.NORMAL) {
			//save starting position
			double startPosX = currentPosX;
			//disable drawing
			enableDrawing = false;
			
			double superLength = 0;
			double normalLength = 0;
			double subLength = 0;
			//calculate the length of the subscript
			currentRelSubSubPosX = 0;
			subLength = subLength + drawNode(f.jjtGetChild(1), YPos.SUBSUB, false);
			currentRelSubSubPosX = subLength;
			subLength = subLength + drawString(getOperatorString(operatorSet.getAssign()), YPos.SUBSUB);
			currentRelSubSubPosX = subLength;
			subLength = subLength + drawNode(f.jjtGetChild(2), YPos.SUBSUB, false);
			//calculate the length of the superscript
			currentRelSuperSuperPosX = 0;
			superLength = superLength + drawNode(f.jjtGetChild(3), YPos.SUPERSUPER, false);
			//calculate the length of the sum symbol
			normalLength = normalLength + drawString(getFunctionString(f.getName()), YPos.NORMAL);
			
			//reset the starting position to the original one (has been changed during the calculation of the length of the sum symbol)
			currentPosX = startPosX;
			//enable drawing
			enableDrawing = true;
			
			//calculate the maximum width needed
			double maxLength = Math.max(superLength, Math.max(normalLength, subLength));
			
			//draw the subscript in the center of the maximum width
			double length = (maxLength-subLength)/2.0;
			currentRelSubSubPosX = length;
			length = length + drawNode(f.jjtGetChild(1), YPos.SUBSUB, false);
			currentRelSubSubPosX = length;
			length = length + drawString(getOperatorString(operatorSet.getAssign()), YPos.SUBSUB);
			currentRelSubSubPosX = length;
			length = length + drawNode(f.jjtGetChild(2), YPos.SUBSUB, false);
			currentRelSubSubPosX = 0;
			//draw the superscript in the center of the maximum width
			length = (maxLength-superLength)/2.0;
			currentRelSuperSuperPosX = length;
			length = length + drawNode(f.jjtGetChild(3), YPos.SUPERSUPER, false);
			currentRelSuperSuperPosX = 0;
			//draw the sum symbol in the center of the maximum width
			length = (maxLength-normalLength)/2.0;
			currentPosX = startPosX + length;
			length = length + drawString(getFunctionString(f.getName()), YPos.NORMAL);
			//adjust the x position and continue drawing the rest
			currentPosX = startPosX + maxLength;
			length = 0;
			length = length + drawNode(f.jjtGetChild(0), YPos.NORMAL, areBracketsNecessary(f, 0));
			
			result = length+maxLength;
		}
		// draw sum as normal function if not in normal position
		else {
			double length = 0;
			length = length + drawString(getFunctionString(f.getName())+BRACKET_OPEN, yPos);
			length = length + drawNode(f.jjtGetChild(0), yPos, false);
			length = length + drawString(", ", yPos);
			length = length + drawNode(f.jjtGetChild(1), yPos, false);
			length = length + drawString(", ", yPos);
			length = length + drawNode(f.jjtGetChild(2), yPos, false);
			length = length + drawString(", ", yPos);
			length = length + drawNode(f.jjtGetChild(3), yPos, false);
			length = length + drawString(BRACKET_CLOSE, yPos);
			result = length;
		}
		// draw opening bracket for the sum if necessary
		if (drawBrackets&&(yPos==YPos.NORMAL)) {
			drawString(BRACKET_CLOSE, YPos.NORMAL);
		}
		return result;
	}
	
	/**
	 * Draws the <code>String</code> on the <code>Graphics</code> <code>g</code>.
	 * @param str the <code>String</code> to be drawn
	 * @param yPos the postion where the <code>String</code> will be drawn
	 * @return the width that is needed to draw the <code>String</code>
	 */
	private double drawString(String str, YPos yPos) {
		double length = 0;
		switch (yPos) {
		case SUPERSUPER:
			length = drawString(str, currentPosX+currentRelSuperSuperPosX, getBaseLineYPos(YPos.SUPERSUPER), getFontSize(YPos.SUPERSUPER));
			currentRelSuperSuperPosX = currentRelSuperSuperPosX + length;
			break;
		case SUPER:
			length = drawString(str, currentPosX, getBaseLineYPos(YPos.SUPER), getFontSize(YPos.SUPER));
			currentPosX = currentPosX + length;
			break;
		case NORMAL:
			length = drawString(str, currentPosX, getBaseLineYPos(YPos.NORMAL), getFontSize(YPos.NORMAL));
			currentPosX = currentPosX + length;
			break;
		case SUB:
			length = drawString(str, currentPosX, getBaseLineYPos(YPos.SUB), getFontSize(YPos.SUB));
			currentPosX = currentPosX + length;
			break;
		case SUBSUB:
			length = drawString(str, currentPosX+currentRelSubSubPosX, getBaseLineYPos(YPos.SUBSUB), getFontSize(YPos.SUBSUB));
			currentRelSubSubPosX = currentRelSubSubPosX + length;
			break;
		}
		return length;
	}

	/**
	 * Draws the <code>String</code> on the <code>Graphics</code> <code>g</code>.
	 * @param str the <code>String</code> to be drawn.
	 * @param x x position of the baseline
	 * @param y y position of the baseline
	 * @param fontSize font size for the font
	 * @return the width that is needed to draw the <code>String</code>
	 */
	private double drawString(String str, double x, double y, int fontSize) {
		g.setFont(g.getFont().deriveFont((float)fontSize));
		if (enableDrawing) {
			g.drawString(str, (int)Math.round(x), (int)Math.round(y));
		}
		return g.getFontMetrics().getStringBounds(str, g).getWidth();
	}
	
	/**
	 * Returns the <code>String</code> that should be drawn for the specified constant.
	 * @param c the constant node
	 * @return the <code>String</code> that should be drawn
	 */
	protected String getConstantString(ASTConstant c) {
		String str = c.getValue().toString();
		if (str.endsWith(".0")) {
			str = str.substring(0, str.length()-".0".length());
		}
		return str;
	}
	
	/**
	 * Returns the <code>String</code> that should be drawn for the specified variable.
	 * @param v the variable node
	 * @return the <code>String</code> that should be drawn
	 */
	protected String getVarString(ASTVarNode v) {
		return v.getName();
	}
	
	/**
	 * Returns the <code>String</code> that should be drawn for the specified operator.
	 * @param op the operator node
	 * @return the <code>String</code> that should be drawn
	 */
	protected String getOperatorString(Operator op) {
		String name = op.getName();
		if (name.equals(operatorSet.getGE().getName())) {
			return Symbols.SYMBOL_GE;
		}
		if (name.equals(operatorSet.getLE().getName())) {
			return Symbols.SYMBOL_LE;
		}
		if (name.equals(operatorSet.getAnd().getName())) {
			return Symbols.SYMBOL_AND;
		}
		if (name.equals(operatorSet.getOr().getName())) {
			return Symbols.SYMBOL_OR;
		}
		if (name.equals(operatorSet.getNot().getName())) {
			return Symbols.SYMBOL_NOT;
		}
		if (name.equals(operatorSet.getNE().getName())) {
			return Symbols.SYMBOL_NE;
		}
		if (name.equals(operatorSet.getEQ().getName())) {
			return operatorSet.getAssign().getName();
		}
		return op.getSymbol();
	}
	
	/**
	 * Returns the <code>String</code> that should be drawn for the specified function.
	 * @param function the function name
	 * @return the <code>String</code> that should be drawn
	 */
	protected String getFunctionString(String function) {
		if (function.equals("Sum")) {
			return Symbols.SYMBOL_SUM;
		}
		else if (function.equals("Product")) {
			return Symbols.SYMBOL_PROD;
		}
		else if (function.equals("sqrt")) {
			return Symbols.SYMBOL_SQRT;
		}
		else {
			return function;
		}
	}
	
	/**
	 * Checks if the specified operator is unary.
	 * @param operatorName name of the operator
	 * @return <code>true</code> if the operator is unary, otherwise <code>false</code>
	 */
	protected boolean isUnaryOperator(String operatorName) {
		for (int i=0;i<UNARY_OPERATORS.length;i++) {
			if (operatorName.equals(UNARY_OPERATORS[i])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the specified operator is binary.
	 * @param operatorName name of the operator
	 * @return <code>true</code> if the operator is binary, otherwise <code>false</code>
	 */
	protected boolean isBinaryOperator(String operatorName) {
		for (int i=0;i<BINARY_OPERATORS.length;i++) {
			if (operatorName.equals(BINARY_OPERATORS[i])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the specified operator is binary and should be displayed without brackets.
	 * @param operatorName name of the operator
	 * @return <code>true</code> if the operator is binary and should be displayed without brackets, otherwise <code>false</code>
	 */
	protected boolean isBinaryOperatorWithoutBrackets(String operatorName) {
		for (int i=0;i<BINARY_OPERATORS_WITHOUT_BRACKETS.length;i++) {
			if (operatorName.equals(BINARY_OPERATORS_WITHOUT_BRACKETS[i])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the specified function is unary.
	 * @param functionName name of the function
	 * @return <code>true</code> if the function is unary, otherwise <code>false</code>
	 */
	protected boolean isUnaryFunction(String functionName) {
		for (int i=0;i<UNARY_FUNCTIONS.length;i++) {
			if (functionName.equals(UNARY_FUNCTIONS[i])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the specified function is the sum or the procuct function.
	 * @param functionName name of the function
	 * @return <code>true</code> if the function is the sum or the product function, otherwise <code>false</code>
	 */
	protected boolean isSumOrProd(String functionName) {
		for (int i=0;i<SUM_OR_PROD.length;i++) {
			if (functionName.equals(SUM_OR_PROD[i])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if brackets are necessary for the child node, based on priority and associativity rules.
	 * @param parentNode the parent operation
	 * @param childNodeIndex index of the child node in question
	 * @return whether brackets are necessary for the child node
	 */
	protected boolean areBracketsNecessary(ASTFunNode parentNode, int childNodeIndex) {
		Node childNode = parentNode.jjtGetChild(childNodeIndex);
		String parentName = parentNode.getName();
		if (childNode instanceof ASTFunNode) {
			ASTFunNode child = (ASTFunNode)childNode;
			String childName = child.getName();
			// The following regards the exponential function as the power operator
			if (parentName.equals("exp")) {
				parentName = operatorSet.getPower().getName();
			}
			if (childName.equals("exp")) {
				childName = operatorSet.getPower().getName();
			}
			int parentPriority = getIndex(PRIORITY, parentName);
			int childPriority = getIndex(PRIORITY, childName);
			if (childPriority > parentPriority) {
				return true;
			}
			else if (childPriority < parentPriority) {
				return false;
			}
			else {
				if (getIndex(NOT_ASSOCIATIVE, parentName)!=-1) {
					if (parentName.equals(operatorSet.getPower().getName())) {
						if (childNodeIndex==0) {
							return true;
						}
					}
					else if (childNodeIndex>0) {
						return true;
					}
				}
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	/**
	 * Returns the index of <code>str</code> in the array <code>strs</code>.
	 * @param strs the array of <code>String</code>
	 * @param str the <code>String</code>
	 * @return the index of <code>str</code> in <code>strs</code>, -1 if <code>str</code> is not contained in <code>strs</code>
	 */
	private int getIndex(String[] strs, String str) {
		for (int i=0; i<strs.length; i++) {
			if (str.equals(strs[i])) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Sets the formula to the new value.
	 * @param formula the new formula
	 */
	public void setFormula(String formula) {
		this.formula = formula;
		changed = true;
	}
	
	/**
	 * Returns the current formula.
	 * @return the current formula
	 */
	public String getFormula() {
		return formula;
	}
	
	/**
	 * Shows the given <code>String</code> as the name of the formula.
	 * @param name name of the formula
	 */
	public void setVariableName(String name) {
		variableName = name;
	}
	
	/**
	 * Returns the <code>String</code> that is shown as the name of the formula.
	 * @return the <code>String</code> that is shown as the name of the formula
	 */
	public String getVariableName() {
		return variableName;
	}
	
	/**
	 * No name will be shown for the formula.
	 *
	 */
	public void removeVariableName() {
		variableName = null;
	}
	
	/**
	 * Sets the font size for normal script to the new size.
	 * @param fontSize the new font size
	 */
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
		yPosInitialized = false;
		fitSize();
	}
	
	/**
	 * Returns the current font size for normal script.
	 * @return the current font size for normal script
	 */
	public int getFontSize() {
		return getFontSize(YPos.NORMAL);
	}
	
	/**
	 * Fits the size of the <code>FormulaViewer</code> to the current font size.
	 *
	 */
	public void fitSize() {
		int height = getBaseLineYPos(YPos.MAX);
		int width = (int)Math.ceil(currentPosX);
		if (width==0) {
			width = 1;
		}
		Dimension d = new Dimension(width, height);
		setSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
		setPreferredSize(d);
	}
	
	/**
	 * Returns the font size for the specified position.
	 * @param yPos the position of the text
	 * @return the font size for the position
	 */
	protected int getFontSize(YPos yPos) {
		switch (yPos) {
		case SUPERSUPER:
			return (int)Math.round(fontSize*sizeSuperSuperFactor);
		case SUPER:
			return (int)Math.round(fontSize*sizeSuperFactor);
		case NORMAL:
			return fontSize;
		case SUB:
			return (int)Math.round(fontSize*sizeSubFactor);
		case SUBSUB:
			return (int)Math.round(fontSize*sizeSubSubFactor);
		default:
			return 0;
		}
	}
	
	/**
	 * <code>true</code> if the y positions of the different script types have been calculated for the current font size, otherwise <code>false</code>.
	 */
	private boolean yPosInitialized = false;
	/**
	 * Highest possible y position of supersuperscript
	 */
	private double posSuperSuperTop;
	/**
	 * Base line position of supersuperscript
	 */
	private double posSuperSuperBase;
	/**
	 * Lowest possible y position of supersuperscript
	 */
	private double posSuperSuperBottom;
	/**
	 * Highest possible y position of superscript
	 */
	private double posSuperTop;
	/**
	 * Base line position of superscript
	 */
	private double posSuperBase;
	/**
	 * Lowest possible y position of superscript
	 */
	private double posSuperBottom;
	/**
	 * Highest possible y position of normal script
	 */
	private double posNormalTop;
	/**
	 * Base line position of normal script
	 */
	private double posNormalBase;
	/**
	 * Lowest possible y position of normal script
	 */
	private double posNormalBottom;
	/**
	 * Highest possible y position of subscript
	 */
	private double posSubTop;
	/**
	 * Base line position of subscript
	 */
	private double posSubBase;
	/**
	 * Lowest possible y position of subscript
	 */
	private double posSubBottom;
	/**
	 * Highest possible y position of subsubscript
	 */
	private double posSubSubTop;
	/**
	 * Base line position of subsubscript
	 */
	private double posSubSubBase;
	/**
	 * Lowest possible y position of subsubscript
	 */
	private double posSubSubBottom;
	/**
	 * Maximal y position, which is equal to the height of the component
	 */
	private double posMax;
	
	/**
	 * Returns the y position of the base line for text on the specified position
	 * @param yPos the position of the text
	 * @return the y position of the base line
	 */
	protected int getBaseLineYPos(YPos yPos) {
		double pos = 0;
		
		if (g!=null) {
			
			if (!yPosInitialized) {
				initYPos();
			}
			
			switch(yPos) {
			case SUPERSUPER:
				return (int)Math.round(posSuperSuperBase);
			case SUPER:
				return (int)Math.round(posSuperBase);
			case NORMAL:
				return (int)Math.round(posNormalBase);
			case SUB:
				return (int)Math.round(posSubBase);
			case SUBSUB:
				return (int)Math.round(posSubSubBase);
			case MAX:
				return (int)Math.round(posMax);
			}
			
		}
		else {
			pos = 1;
		}
		return (int)Math.ceil(pos);
	}
	
	/**
	 * Calculates the y position for all script types for the current font size, if the <code>Graphics</code> of the component have been initialized already
	 */
	private void initYPos() {
		if (g!=null) {
			g.setFont(g.getFont().deriveFont((float)getFontSize(YPos.SUPERSUPER)));
			int ascSuperSuper = g.getFontMetrics().getAscent();
			int descSuperSuper = g.getFontMetrics().getDescent();
			g.setFont(g.getFont().deriveFont((float)getFontSize(YPos.SUPER)));
			int ascSuper = g.getFontMetrics().getAscent();
			int descSuper = g.getFontMetrics().getDescent();
			g.setFont(g.getFont().deriveFont((float)getFontSize(YPos.NORMAL)));
			int ascNormal = g.getFontMetrics().getAscent();
			int descNormal = g.getFontMetrics().getDescent();
			g.setFont(g.getFont().deriveFont((float)getFontSize(YPos.SUB)));
			int ascSub = g.getFontMetrics().getAscent();
			int descSub = g.getFontMetrics().getDescent();
			g.setFont(g.getFont().deriveFont((float)getFontSize(YPos.SUBSUB)));
			int ascSubSub = g.getFontMetrics().getAscent();
			int descSubSub = g.getFontMetrics().getDescent();
			
			posSuperSuperTop = yPosSuperFactor*fontSize;
			posSuperSuperBase = posSuperSuperTop + ascSuperSuper;
			posSuperSuperBottom = posSuperSuperBase + descSuperSuper;
			posNormalTop = posSuperSuperBottom + yPosNormalFactor*fontSize;
			posNormalBase = posNormalTop + ascNormal;
			posNormalBottom = posNormalBase + descNormal;
			posSubSubTop = posNormalBottom + yPosSubFactor*fontSize;
			posSubSubBase = posSubSubTop + ascSubSub;
			posSubSubBottom = posSubSubBase + descSubSub;
			posMax = posSubSubBottom; 
			posSuperBase = posNormalBase - fontSize + getFontSize(YPos.SUPER)*0.75;
			posSuperTop = posSuperBase - ascSuper;
			posSuperBottom = posSuperBase + descSuper;
			posSubBase = posNormalBase + getFontSize(YPos.SUB)*0.5;
			posSubTop = posSubBase - ascSub;
			posSubBottom = posSubBase + descSub;
			
			yPosInitialized = true;
		}
		else {
			yPosInitialized = false;
		}
	}
	
	/**
	 * Changes the color of the font.
	 * @param c the new color
	 */
	public void setColor(Color c) {
		color = c;
		repaint();
	}
	
	/**
	 * Sets the font for this formula viewer.
	 * @param font the desired Font for this formula viewer
	 */
	public void setFont(Font font) {
		super.setFont(font);
		setFontSize(font.getSize());
	}
	
	/**
	 * Calculates and sets size, minimum size, maximum size and preferred size even if it's not drawn yet.
	 *
	 */
	public void initSize() {
		GraphicsConfiguration gfxConf = GraphicsEnvironment.getLocalGraphicsEnvironment(). 
	    getDefaultScreenDevice().getDefaultConfiguration();  
		BufferedImage img = gfxConf.createCompatibleImage(1, 1);
		Graphics2D g = img.createGraphics();
		print(g);
	}
	
}