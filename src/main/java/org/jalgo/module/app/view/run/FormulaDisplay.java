package org.jalgo.module.app.view.run;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import org.jalgo.module.app.core.step.AtomicStep;
import org.jalgo.module.app.view.InterfaceConstants;
import org.jalgo.module.app.view.graph.HighlightState;

/**
 * Displays the formula of the current <code>step</code>. It is used for
 * <code>FormulaComponent</code> and <code>FormulaWindow</code>.
 * 
 */
public class FormulaDisplay extends JPanel {

	private static final long serialVersionUID = 679622426179670735L;
	private Graphics2D graphics;
	private Font normalFont, boldFont, smallFont;
	
	private OperatorDescription currentOperators;
	private ParameterDescription currentParameters;
	
	private Metrics globalMetrics;
	
	private boolean beamerMode;
	
	private boolean noPainting;
	
	/**
	 * Describes the two Operators used in a <code>SemiRing</code>. 
	 */
	private class OperatorDescription {
		public String multOperator;
		public boolean multInfix;
		
		public String addOperator;
		public boolean addInfix;
		
		/**
		 * Describes the two operators used in a <code>SemiRing</code>.
		 * 
		 * @param addOperator
		 *            the multiplicative operator in its symbolic
		 *            representation.
		 * @param addInfix
		 *            if <code>true</code>, the add operator must be printed
		 *            infix, otherwise prefix.
		 * @param multOperator
		 *            the multiplicative operator in its symbolic
		 *            representation.
		 * @param multInfix
		 *            if <code>true</code>, the multiply operator must be
		 *            printed infix, otherwise prefix.
		 */
		public OperatorDescription(String addOperator, boolean addInfix, String multOperator, boolean multInfix) {
			this.addOperator = addOperator;
			this.addInfix = addInfix;
			this.multOperator = multOperator;
			this.multInfix = multInfix;
		}
	}
	
	private class ParameterDescription {
		public int u, k, v;
		public String[] values;
		
		public ParameterDescription(AtomicStep step) {
			this.u = step.getU() + 1;
			this.k = step.getK() + 1;
			this.v = step.getV() + 1;
			this.values = new String[5];
			
			this.values[0] = step.getGroupStep().getBeforeMatrix().getValueAt(u-1, v-1).toString();
			this.values[1] = step.getGroupStep().getBeforeMatrix().getValueAt(u-1, k-1).toString();
			this.values[2] = step.getGroupStep().getBeforeMatrix().getValueAt(k-1, k-1).toString();
			this.values[3] = step.getGroupStep().getBeforeMatrix().getValueAt(k-1, v-1).toString();
			this.values[4] = step.getValue().toString();
		}
	}
	
	/**
	 * Represents a container for font properties (font and line height) and for
	 * the offset in the x and y direction (used for the points to paint the
	 * next element to).
	 * 
	 */
	private class Metrics {
		public int offsetX, offsetY, fontHeight, lineHeight;
	}
	
	/**
	 * An extension of <code>Metrics</code> used for painting multiply lines
	 * of a formula at once.
	 * 
	 */
	private class MultiLineMetrics extends Metrics {
		public int upperX, upperY;
		public int lowerX, lowerY;
	}
	
	/**
	 * Displays the recursive formula (using variables and its current values
	 * and the result) including highlighting on the algorithm panel during the
	 * algorithm mode.
	 */
	public FormulaDisplay(Font normalFont, Font boldFont, Font smallFont) {
		noPainting = true;
		this.beamerMode = false;

		this.normalFont = normalFont;
		this.boldFont = boldFont;
		this.smallFont = smallFont;
		
		// FIXME: Static calculation
		Dimension panelDimension = new Dimension(normalFont.getSize() * 6, 100);

		this.setMinimumSize(panelDimension);
		this.setPreferredSize(panelDimension);
		this.setMaximumSize(panelDimension);
	
		globalMetrics = new Metrics();
		
		this.setOpaque(true);
		this.setBackground(InterfaceConstants.formulaBackgroundColor());
	}
	
	public void setFont(Font normalFont, Font boldFont, Font smallFont) {
		this.normalFont = normalFont;
		this.boldFont = boldFont;
		this.smallFont = smallFont;
	}
	
	/**
	 * Sets the current operators.
	 * 
	 * @param addOperator
	 *            the multiplicative operator in its symbolic representation.
	 * @param addInfix
	 *            if <code>true</code>, the add operator must be printed
	 *            infix, otherwise prefix.
	 * @param multOperator
	 *            the multiplicative operator in its symbolic representation.
	 * @param multInfix
	 *            if <code>true</code>, the multiply operator must be printed
	 *            infix, otherwise prefix.
	 */
	public void setOperators(String addOperator, boolean addInfix, String multOperator, boolean multInfix) {
		currentOperators = new OperatorDescription(addOperator, addInfix, multOperator, multInfix);		
	}
	
	/**
	 * Sets the <code>ParamterDescription</code> used for painting the formula
	 * of the <code>step</code>.
	 * 
	 * @param step
	 *            the Step to be painted in a formula representation.
	 */
	public void setParameterDescription(AtomicStep step) {
		currentParameters = new ParameterDescription(step);

		repaint();
		revalidate();	
	}
	
	/**
	 * Draws a string to the given coordinates.
	 * 
	 * @param string
	 *            the given <code>String</code>
	 * @param font
	 *            the <code>Font</code> used for this string.
	 * @param offsetX
	 *            the x-coordinate
	 * @param offsetY
	 *            the y-coordinate
	 * @return the new <code>offSetX</code> (points to the coordinates at the
	 *         end of the string)
	 */
	private int paintString(String string, Font font, int offsetX, int offsetY) {
		graphics.setFont(font);
		
		graphics.drawString(string, offsetX, offsetY);
		return offsetX + (int)graphics.getFontMetrics(font).getStringBounds(string, graphics).getWidth();
	}
	
	/**
	 * Same as <code>paintString</code>, but using the "normal" font defined
	 * in <code>InterfaceConstants.formulaNormalFont()</code>.
	 * 
	 * @param string
	 *            the given <code>String</code>
	 * @param offsetX
	 *            the x-coordinate
	 * @param offsetY
	 *            the y-coordinate
	 * @return the new <code>offSetX</code> (points to the coordinates at the
	 *         end of the string)
	 */
	private int paintStringNormal(String string, int offsetX, int offsetY) {
		return paintString(string, normalFont, offsetX, offsetY);
	}
	
	/**
	 * Same as <code>paintString</code>, but using the "bold" font defined
	 * in <code>InterfaceConstants.formulaBoldFont()</code>.
	 * 
	 * @param string
	 *            the given <code>String</code>
	 * @param offsetX
	 *            the x-coordinate
	 * @param offsetY
	 *            the y-coordinate
	 * @return the new <code>offSetX</code> (points to the coordinates at the
	 *         end of the string)
	 */
	private int paintStringBold(String string, int offsetX, int offsetY) {
		return paintString(string, boldFont, offsetX, offsetY);
	}	
	
	/**
	 * Same as <code>paintString</code>, but using the "small" font defined
	 * in <code>InterfaceConstants.formulaSmallFont()</code>.
	 * 
	 * @param string
	 *            the given <code>String</code>
	 * @param offsetX
	 *            the x-coordinate
	 * @param offsetY
	 *            the y-coordinate
	 * @return the new <code>offSetX</code> (points to the coordinates at the
	 *         end of the string)
	 */
	private int paintStringSmall(String string, int offsetX, int offsetY) {
		return paintString(string, smallFont, offsetX, offsetY);
	}	
	
	/**
	 * Paints $D_G^{(superscript)} (a,b)$ (LaTeX Math style)
	 * 
	 * @param metrics
	 *            the used metrics for the string
	 * @param superscript
	 * @param a
	 * @param b
	 * @return the new x offset
	 */
	private int paintDistanceSymbol(Metrics metrics, int superscript, int a, int b) {
		// Paint "D"
		graphics.setFont(normalFont);		
		metrics.offsetX = paintStringNormal("D", metrics.offsetX, metrics.offsetY);
		
		// Paint (k) and G
		int offsetSuperscript = paintStringSmall("("+superscript+")", metrics.offsetX, metrics.offsetY - metrics.fontHeight/2);
		int offsetSubscript = paintStringSmall("G", metrics.offsetX, metrics.offsetY + metrics.fontHeight/4);
		
		// Paint (u,v)
		metrics.offsetX = Math.max(offsetSuperscript, offsetSubscript);
		
		graphics.setFont(normalFont);
		
		metrics.offsetX = paintStringNormal( "("+a+","+b+")", metrics.offsetX, metrics.offsetY);
		
		return metrics.offsetX;
	}

	/**
	 * Paints the infix operator (e.g. +)
	 * 
	 * @param metrics
	 *            the font metrics used for this operator
	 * @param operator
	 *            the given operator
	 * @return the new x offset
	 */
	private int paintInfixOperator(MultiLineMetrics metrics, String operator) {
		// Paint Operator
		graphics.setColor(InterfaceConstants.formulaForegroundColor());
		paintStringBold(" "+operator+" ", metrics.offsetX, metrics.upperY);
		metrics.offsetX = paintStringBold(" "+operator+" ", metrics.offsetX, metrics.lowerY);
		
		return metrics.offsetX;
	}
	
	/**
	 * Paints a whole element of the formula (the variable and the value in the
	 * line below), e.g. (D_{G}^{(superscript)} (a,b))*
	 * 
	 * @param metrics
	 *            the used font metrics
	 * @param superscript
	 * @param a
	 * @param b
	 * @param value
	 *            the calculated value for this element
	 * @param hasStar
	 *            <code>true</code>, if D_{G}.. has a star at its end
	 * @param hasComma
	 *            <code>true</code>, if D_{G}.. has a comma at its end
	 * @return
	 */
	private int paintFormulaElement(MultiLineMetrics metrics, int superscript, int a, int b, String value, boolean hasStar, boolean hasComma) {
		String prefix;
				
		Color formulaElementColor;
		
		formulaElementColor = graphics.getColor();
		
		if (hasStar) {
			// set Color to black
			graphics.setColor(InterfaceConstants.formulaForegroundColor());
			
			paintStringNormal("(", metrics.offsetX, metrics.upperY);
			metrics.offsetX = paintStringNormal("(", metrics.offsetX, metrics.lowerY);
			
			// reset Color
			graphics.setColor(formulaElementColor);
		}		
		
		// Paint Element
		metrics.upperX = metrics.offsetX;
		metrics.lowerX = metrics.offsetX;
		
		metrics.upperX = paintDistanceSymbol(metrics, superscript, a, b);
		metrics.lowerX = paintStringNormal(value, metrics.lowerX, metrics.lowerY);
		
		graphics.setColor(InterfaceConstants.formulaForegroundColor());
		
		if (hasStar) {
			metrics.upperX = paintStringNormal(")*", metrics.upperX, metrics.upperY);
			metrics.lowerX = paintStringNormal(")*", metrics.lowerX, metrics.lowerY);		
		}
				
		if (hasComma)
			prefix = ", ";
		else
			prefix = " ";
		
		metrics.upperX = paintStringNormal(prefix, metrics.upperX, metrics.upperY);
		metrics.lowerX = paintStringNormal(prefix, metrics.lowerX, metrics.lowerY);
		
		// Update metrics
		metrics.offsetX = Math.max(metrics.lowerX, metrics.upperX);

		
		return metrics.offsetX;
	}
	
	/**
	 * Paints the inner formula, which is the one after the add operator.
	 * 
	 * @param metrics
	 *            the used font metrics
	 * @param operators
	 *            the multiplicative operator
	 * @param parameter
	 *            the used parameters (u,k,v) in the inner formula
	 * @return the new x offset
	 */
	private int paintInnerFormula(MultiLineMetrics metrics, OperatorDescription operators, ParameterDescription parameter) {
		String openingParenthesis, closingParenthesis;
		
		if (operators.multInfix) {
			// (
			openingParenthesis = "( ";
			closingParenthesis = ") ";
		}
		 else
		{
			 // Prefix ( )
			 openingParenthesis = " "+operators.multOperator+" {";
			 closingParenthesis = "} ";
		}

		// ( or prefix operator
		paintStringNormal(openingParenthesis, metrics.offsetX, metrics.upperY);
		metrics.offsetX = paintStringNormal(openingParenthesis, metrics.offsetX, metrics.lowerY);		
		
		// (u,k)
		graphics.setColor(InterfaceConstants.borderColorForHighlightState(HighlightState.PRE_PATH));
		metrics.offsetX = paintFormulaElement(metrics, 
									  		  parameter.k-1, 
									  		  parameter.u, 
									  		  parameter.k, 
									  		  parameter.values[1], 
									  		  false, 
									  		  !operators.multInfix
									 		 );	
		
		// Infix Operator
		if (operators.multInfix) {
			metrics.offsetX = paintInfixOperator(metrics, operators.multOperator);
		}
				
		// (k,k) *
		graphics.setColor(InterfaceConstants.borderColorForHighlightState(HighlightState.SELECTED));
		metrics.offsetX = paintFormulaElement(metrics, 
				  					  		  parameter.k-1, 
				  					  		  parameter.k, 
						  					  parameter.k, 
						  					  parameter.values[2], 
						  					  true, 
						  					  !operators.multInfix
				 					 		 );
		
		// Infix Operator
		if (operators.multInfix) {
			metrics.offsetX = paintInfixOperator(metrics, operators.multOperator);
		}

		// (k,v)
		graphics.setColor(InterfaceConstants.borderColorForHighlightState(HighlightState.POST_PATH));
		metrics.offsetX = paintFormulaElement(metrics, 
											  parameter.k-1, 
											  parameter.k, 
											  parameter.v, 
											  parameter.values[3], 
											  false, 
											  false
											 );
				
		graphics.setColor(InterfaceConstants.formulaForegroundColor());
		
		// ) or }
		paintStringNormal(closingParenthesis, metrics.offsetX, metrics.upperY);
		metrics.offsetX = paintStringNormal(closingParenthesis, metrics.offsetX, metrics.lowerY);
		
		return metrics.offsetX;
	}
	
	/**
	 * Paints the whole formula (outer and inner formula).
	 * 
	 * @param metrics
	 *            the used font metrics.
	 * @param operators
	 *            the used operators (+,*)
	 * @param parameter
	 *            the used parameters (u,k,v)
	 * @return the new x offset
	 */
	private int paintFormula(Metrics metrics, OperatorDescription operators, ParameterDescription parameter) {
		MultiLineMetrics mlMetrics = new MultiLineMetrics();

		// Calculate multiline metrics
		mlMetrics.offsetX = metrics.offsetX;
		mlMetrics.offsetY = metrics.offsetY;
		mlMetrics.fontHeight = metrics.fontHeight;
		mlMetrics.lineHeight = metrics.lineHeight;
				
		mlMetrics.upperY = mlMetrics.offsetY;
		mlMetrics.lowerY = mlMetrics.offsetY + mlMetrics.lineHeight;
		
		mlMetrics.upperX = mlMetrics.offsetX;
		mlMetrics.lowerX = mlMetrics.offsetX;
		
		// "=" or "= OPERATOR {"
		if (operators.addInfix) {
			paintStringBold("= ", mlMetrics.offsetX, mlMetrics.upperY);
			mlMetrics.offsetX = paintStringBold("= ", mlMetrics.offsetX, mlMetrics.lowerY);
		}
		else {
			paintStringBold("= "+operators.addOperator+"{ ", mlMetrics.offsetX, mlMetrics.upperY);
			mlMetrics.offsetX = paintStringBold("= "+operators.addOperator+"{ ", mlMetrics.offsetX, mlMetrics.lowerY);
		}
		
		// (u,v)
		mlMetrics.offsetX = paintFormulaElement(mlMetrics, 
										  	  	parameter.k-1, 
										  	  	parameter.u, 
										  	  	parameter.v, 
										  	  	parameter.values[0], 
										  	  	false, 
										  	  	!operators.addInfix
									 		   );
		
		// Infix Operator
		if (operators.addInfix) {
			mlMetrics.offsetX = paintInfixOperator(mlMetrics, operators.addOperator);			
		}
		
		mlMetrics.offsetX = paintInnerFormula(mlMetrics, operators, parameter);
		
		// }
		paintStringBold(" }", mlMetrics.offsetX, mlMetrics.upperY);
		mlMetrics.offsetX = paintStringBold(" }", mlMetrics.offsetX, mlMetrics.lowerY);		
		
		return mlMetrics.offsetX;
	}
	
	
	/**
	 * Paints the formula component.
	 */
	public void paint(Graphics output) {
		Metrics metrics;
		
		// Setup
		
		if (output == null)
			return;
		
		graphics = (Graphics2D) output;
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// Draw background
		graphics.setColor(InterfaceConstants.formulaBackgroundColor());
		graphics.setStroke(new BasicStroke(1));

		graphics.fill(new Rectangle(this.getSize()));		

		graphics.setColor(InterfaceConstants.formulaForegroundColor());
		graphics.setStroke(new BasicStroke(1));
	
		// Calculate metrics
		metrics = new Metrics();
		
		metrics.fontHeight = (int)graphics.getFontMetrics(normalFont).getStringBounds("D", graphics).getHeight();
		metrics.offsetX = metrics.fontHeight / 2;
		metrics.offsetY = metrics.fontHeight + metrics.fontHeight / 2;
		metrics.lineHeight = (int)(metrics.fontHeight * 1.5);
		
		// First line
		paintDistanceSymbol(metrics, 
							currentParameters.k,
							currentParameters.u, 
							currentParameters.v
						   );
		metrics.offsetX = metrics.fontHeight / 2;
		
		// Second + Third line
		metrics.offsetY += metrics.lineHeight;
		globalMetrics.offsetX = paintFormula(metrics, currentOperators, currentParameters);
		
		// Last line
		metrics.offsetY += metrics.lineHeight * 2;
		paintStringBold("= "+currentParameters.values[4], metrics.offsetX, metrics.offsetY);
		
		globalMetrics.offsetY = metrics.offsetY + metrics.lineHeight * 2;
	}

	/**
	 * @return the X coordinate from the last <code>String</code> painted.
	 */
	public int getOffsetX() {
		return globalMetrics.offsetX;
	}
	
	/**
	 * @return the Y coordinate from the last <code>String</code> painted.
	 */
	public int getOffsetY() {
		return globalMetrics.offsetY;
	}
	
	/**
	 * @return <code>true</code>, if there should be no painting at this
	 *         point (e.g. when the current <code>Step</code> is a
	 *         <code>GroupStep</code>).
	 */
	public boolean isNoPainting() {
		repaint();
		revalidate();
		
		return noPainting;
	}

	public void updateBeamerMode(boolean beamerMode){
		this.beamerMode = beamerMode;
		this.boldFont = InterfaceConstants.formulaBoldFont(beamerMode);
		this.normalFont = InterfaceConstants.formulaNormalFont(beamerMode);
		this.smallFont = InterfaceConstants.formulaSmallFont(beamerMode);
		repaint();
		revalidate();
	}
	
	/**
	 * Sets the <code>boolean</code> variable <code>noPainting</code>.
	 * 
	 * @param noPainting
	 *            <code>true</code> if the current <code>Step</code> should
	 *            not be painted.
	 */
	public void setNoPainting(boolean noPainting) {
		this.noPainting = noPainting;
		
		this.setVisible(!noPainting);
		
		repaint();
		revalidate();		
	}
}
