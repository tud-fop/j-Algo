package org.jalgo.module.app.view;

import java.awt.Color;
import java.awt.Font;

import org.jalgo.module.app.view.graph.HighlightState;

/**
 * This class holds all of the constants (colors, font sizes and other sizes)
 * needed to adjust the view of the module interface. These constants should
 * only be used by the components in the view.
 * 
 */
public class InterfaceConstants {

	private static Color defaultRed() {
		return new Color(186, 58, 55);
	}

	private static Color defaultGreen() {
		return new Color(62, 136, 62);
	}

	private static Color defaultBlue() {
		return new Color(56, 111, 159);
	}

	/**
	 * Gets the border color for the node in the respective highlighting state
	 * (no highlighting, selected, current, pre_path and post_path highlighting)
	 * 
	 * @see HighlightState
	 * @param state
	 *            the <code>HighLightState</code> for which the border color
	 *            is needed
	 * @return the color for the given <code>HighLightState</code>
	 */
	public static Color borderColorForHighlightState(HighlightState state) {
		switch (state) {
		case NONE:
			return Color.BLACK;

		case SELECTED:
		case CURRENT:
			return defaultRed(); // Red

		case PRE_PATH:
			return defaultGreen(); // Green

		case POST_PATH:
			return defaultBlue(); // Blue
		}

		return null;
	}

	/**
	 * Gets the fill color for the node in the respective highlighting state (no
	 * highlighting, selected, current, pre_path and post_path highlighting)
	 * 
	 * @see HighlightState
	 * @param state
	 *            the <code>HighLightState</code> for which the fill color is
	 *            needed
	 * @return the color for the given <code>HighLightState</code>
	 */
	public static Color fillColorForHighlightStateNode(HighlightState state) {
		switch (state) {
		case NONE:
			return Color.WHITE;

		case SELECTED:
		case CURRENT:
			return defaultRed(); // Red

		case PRE_PATH:
			return defaultGreen(); // Green

		case POST_PATH:
			return defaultBlue(); // Blue
		}

		return null;
	}

	/**
	 * Gets the fill color for the edge in the respective highlighting state (no
	 * highlighting, selected, current, pre_path and post_path highlighting)
	 * 
	 * @see HighlightState
	 * @param state
	 *            the <code>HighLightState</code> for which the fill color is
	 *            needed
	 * @return the color for the given <code>HighLightState</code>
	 */
	public static Color fillColorForHighlightStateEdge(HighlightState state) {
		switch (state) {
		case NONE:
			return Color.WHITE;

		case SELECTED:
		case CURRENT:
			return defaultRed(); // Red

		case PRE_PATH:
			return Color.WHITE;

		case POST_PATH:
			return Color.WHITE;
		}

		return null;
	}

	/**
	 * Gets the text color for the node in the respective highlighting state (no
	 * highlighting, selected, current, pre_path and post_path highlighting)
	 * 
	 * @see HighlightState
	 * @param state
	 *            the <code>HighLightState</code> for which the text color is
	 *            needed
	 * @return the color for the given <code>HighLightState</code>
	 */
	public static Color textColorForHighlightStateNode(HighlightState state) {
		switch (state) {
		case NONE:
			return Color.BLACK;

		default:
			return Color.WHITE;
		}
	}

	/**
	 * Gets the text color for the edge weight in the respective highlighting
	 * state (no highlighting, selected, current, pre_path and post_path
	 * highlighting)
	 * 
	 * @see HighlightState
	 * @param state
	 *            the <code>HighLightState</code> for which the text color is
	 *            needed
	 * @return the color for the given <code>HighLightState</code>
	 */
	public static Color textColorForHighlightStateEdge(HighlightState state) {
		switch (state) {
		case NONE:
			return Color.BLACK;

		case SELECTED:
		case CURRENT:
			return Color.WHITE;

		case PRE_PATH:
			return defaultGreen(); // Green

		case POST_PATH:
			return defaultBlue(); // Blue

		default:
			return Color.WHITE;
		}
	}

	/**
	 * Gets the text color for a matrix element in the respective highlighting
	 * state (no highlighting, selected, current, pre_path and post_path
	 * highlighting).
	 * 
	 * @see HighlightState
	 * @param state
	 *            the <code>HighLightState</code> for which the text color is
	 *            needed
	 * @return the color for the given <code>HighLightState</code>
	 */
	public static Color matrixForegroundColorForHighlightState(HighlightState state) {
		if (state == HighlightState.NONE)
			return Color.BLACK;

		return Color.WHITE;
	}

	/**
	 * Gets the background color for the matrix element based in the
	 * <code>HighlightState</code>. It is used in the left matrix of the
	 * <code>MatrixComponent</code> during the <code>AlgorithmMode</code>.
	 * 
	 * @see HighlightState
	 * 
	 * @param state
	 *            the <code>HighlightState</code> of the matrix element
	 * @return the color for the given <code>HighlightState</code>. If
	 *         <code>HighLightState</code> is set to <code>NONE</code>,
	 *         return <codeColor.White</code>
	 */
	public static Color matrixBackgroundColorForHighlightState(HighlightState state) {
		if (state == HighlightState.NONE)
			return Color.WHITE;

		return borderColorForHighlightState(state);
	}

	/**
	 * Gets the font color for the matrix element if this element has already
	 * been processed by the Aho-Algorithm. It is used in the right matrix of
	 * the <code>MatrixComponent</code> during the <code>AlgorithmMode</code>.
	 * 
	 * @return the color for the matrix element if this element has already been
	 *         processed by the Aho-Algorithm.
	 */
	public static Color changedMatrixEntryForegroundColor() {
		return Color.BLACK;
	}

	/**
	 * Gets the font color for the matrix element if this element has not been
	 * processed by the Aho-Algorithm. It is used in the right matrix of the
	 * <code>MatrixComponent</code> during the <code>AlgorithmMode</code>.
	 * 
	 * @return the color for the matrix element if this element has not been
	 *         processed by the Aho-Algorithm.
	 */
	public static Color unchangedMatrixEntryForegroundColor() {
		return Color.GRAY;
	}

	/**
	 * Gets the font color for the matrix element if this element is currently
	 * being processed by the Aho-Algorithm. It is used in the right matrix of
	 * the <code>MatrixComponent</code> during the <code>AlgorithmMode</code>.
	 * 
	 * @return the color for the matrix element if this element is currently
	 *         being processed by the Aho-Algorithm.
	 */
	public static Color currentMatrixEntryForegroundColor() {
		return Color.WHITE;
	}

	/**
	 * Gets the background color for the matrix element if this element has
	 * already been process by the Aho-Algorithm. It is used in the right matrix
	 * of the <code>MatrixComponent</code> during the
	 * <code>AlgorithmMode</code>.
	 * 
	 * @return the color for the matrix element if this element has already
	 *         being processed by the Aho-Algorithm.
	 */
	public static Color changedMatrixEntryBackgroundColor() {
		return Color.WHITE;
	}

	/**
	 * Gets the background color for the matrix element if this element has not
	 * been process by the Aho-Algorithm. It is used in the right matrix of the
	 * <code>MatrixComponent</code> during the <code>AlgorithmMode</code>.
	 * 
	 * @return the color for the matrix element if this element has not been
	 *         processed by the Aho-Algorithm.
	 */
	public static Color unchangedMatrixEntryBackgroundColor() {
		return Color.WHITE;
	}

	/**
	 * Gets the background color for the matrix element if this element is
	 * currently being process by the Aho-Algorithm. It is used in the right
	 * matrix of the <code>MatrixComponent</code> during the
	 * <code>AlgorithmMode</code>.
	 * 
	 * @return the color for the matrix element if this element is currently
	 *         being processed by the Aho-Algorithm.
	 */
	public static Color currentMatrixEntryBackgroundColor() {
		return Color.BLACK;
	}

	public static float getNodeDiameter(boolean beamerMode) {
		if (beamerMode)
			return 36.0f;
		else
			return 32.0f;
	}

	public static float getNodeBorderWidth(boolean beamerMode) {
		if (beamerMode)
			return 4.0f;
		else
			return 3.0f;
	}

	public static float getEdgeWidth(boolean beamerMode) {
		if (beamerMode)
			return 5.0f;
		else
			return 4.0f;
	}

	public static float getEdgeWeightBorder(boolean beamerMode) {
		if (beamerMode)
			return 6.0f;
		else
			return 5.0f;
	}

	public static float getEdgeArrowWidth(boolean beamerMode) {
		if (beamerMode)
			return 14.0f;
		else
			return 12.0f;
	}

	public static float getEdgeArrowLength(boolean beamerMode) {
		if (beamerMode)
			return 16.0f;
		else
			return 14.0f;
	}

	public static float getDoubleEdgeDistance(boolean beamerMode) {
		return 50.0f;
	}

	public static Font getGraphNodeFont(boolean beamerMode) {
		if (beamerMode)
			return new Font("Default", Font.BOLD, 24);
		else
			return new Font("Default", Font.BOLD, 20);
	}

	public static Font getGraphWeightFont(boolean beamerMode) {
		if (beamerMode)
			return new Font("Default", Font.BOLD, 24);
		else
			return new Font("Default", Font.BOLD, 20);
	}

	public static Font getMatrixFont(boolean beamerMode) {
		if (beamerMode)
			return new Font("Default", Font.BOLD, 20);
		else
			return new Font("Default", Font.BOLD, 10);
	}

	/**
	 * Gets the minimum font size for a matrix element. No matter how small the
	 * matrix is scaled, the font size will not be under this value.
	 * 
	 * @param beamerMode
	 *            <code>true</code> if beamerMode is enabled,
	 *            <code>false</code> otherwise
	 * @return the minimum font size
	 */
	public static int getMatrixMinimumFontSize(boolean beamerMode) {
		if (beamerMode)
			return 24;
		else
			return 8;
	}

	/**
	 * Gets the maximum font size for a matrix element. No matter how big the
	 * matrix is scaled, the font size will not be under this value.
	 * 
	 * @param beamerMode
	 *            <code>true</code> if beamerMode is enabled,
	 *            <code>false</code> otherwise
	 * @return the maximum font size
	 */
	public static int getMatrixMaximumFontSize(boolean beamerMode) {
		if (beamerMode)
			return 35;
		else
			return 20;
	}

	/**
	 * Gets the matrix color for the adjacency matrix used in the
	 * <code>MatrixPreviewComponent</code>.
	 * 
	 * @return the matrix (font) color.
	 */
	public static Color getMatrixColor() {
		return Color.BLACK;
	}

	/**
	 * Gets the color for any wrong input.
	 * 
	 * @return A red color.
	 */
	public static Color wrongInputColor() {
		return Color.RED;
	}

	/**
	 * Gets the fill color for the HighlightBox in <code>GraphTextComponent</code>.
	 * If an edge is selected in <code>GraphComponent</code>, the corresponding tuple will be
	 * filled in this color.
	 * 
	 * @return The highlight color.
	 */
	public static Color textViewEdgeFillColor() {
		return new Color(255, 212, 43);
	}

	/**
	 * Gets the line color for the HighlightBox in
	 * <code>GraphTextComponent</code>. If an edge is selected in
	 * <code>GraphComponent</code>, the corresponding tuple's box will have
	 * an outer line of this color.
	 * 
	 * @return The highlight border color.
	 */
	public static Color textViewEdgeLineColor() {
		return new Color(42, 17, 0);
	}

	/**
	 * Gets the font used for the edge list in <code>GraphTextComponent</code>.
	 * 
	 * @param beamerMode
	 *            <code>true</code> if the beamerMode is enabled,
	 *            <code>false</code> otherwise.
	 * @return the font.
	 */
	public static Font GraphTextComponentFont(boolean beamerMode) {
		if (beamerMode)
			return new Font("Default", Font.BOLD, 20);
		else
			return new Font("Default", Font.PLAIN, 16);
	}

	/**
	 * Gets the font color for the formula in <code>FormulaComponent</code>.
	 * 
	 * @return the font color for the formula in <code>FormulaComponent</code>.
	 */
	public static Color formulaForegroundColor() {
		return Color.BLACK;
	}

	/**
	 * Gets the background color for the formula in
	 * <code>FormulaComponent</code>.
	 * 
	 * @return the background color for the formula in
	 *         <code>FormulaComponent</code>.
	 */
	public static Color formulaBackgroundColor() {
		return Color.WHITE;
	}

	/**
	 * Gets the font for every string in the <code>FormulaComponent</code>
	 * that is painted "normally", i.e. it is neither super/subscripted nor
	 * bold.
	 * 
	 * @param beamerMode
	 *            <code>true</code> if the beamerMode is enabled,
	 *            <code>false</code> otherwise.
	 * @return A plain font.
	 */
	public static Font formulaNormalFont(boolean beamerMode) {
		if (beamerMode)
			return new Font("Default", Font.PLAIN, 22);
		else
			return new Font("Default", Font.PLAIN, 14);
	}

	/**
	 * Gets the font for every string in the <code>FormulaComponent</code>
	 * that is painted bold.
	 * 
	 * @param beamerMode
	 *            <code>true</code> if the beamerMode is enabled,
	 *            <code>false</code> otherwise.
	 * @return A bold font.
	 */
	public static Font formulaBoldFont(boolean beamerMode) {
		if (beamerMode)
			return new Font("Default", Font.BOLD, 22);
		else
			return new Font("Default", Font.BOLD, 14);
	}
	
	/**
	 * Gets the font for every string in the <code>FormulaComponent</code>
	 * that is painted in superscript or subscript (used for D_G^{k}.
	 * 
	 * @param beamerMode
	 *            <code>true</code> if the beamerMode is enabled,
	 *            <code>false</code> otherwise.
	 * @return A italic font.
	 */
	public static Font formulaSmallFont(boolean beamerMode) {
		if (beamerMode)
			return new Font("Default", Font.ITALIC, 16);
		else
			return new Font("Default", Font.ITALIC, 10);

	}
	
	/**
	 * Gets the font for every string in the <code>FormulaWindow</code> that
	 * is painted "normally", i.e. it is neither super/subscripted nor bold.
	 * 
	 * @param beamerMode
	 *            <code>true</code> if the beamerMode is enabled,
	 *            <code>false</code> otherwise.
	 * @return A plain font.
	 */
	public static Font formulaWindowNormalFont(boolean beamerMode) {
		if (beamerMode)
			return new Font("Default", Font.PLAIN, 26);
		else
			return new Font("Default", Font.PLAIN, 18);

	}
	
	/**
	 * Gets the font for every string in the <code>FormulaWindow</code> that
	 * is bold.
	 * 
	 * @param beamerMode
	 *            <code>true</code> if the beamerMode is enabled,
	 *            <code>false</code> otherwise.
	 * @return A bold font.
	 */
	public static Font formulaWindowBoldFont(boolean beamerMode) {
		if (beamerMode)
			return new Font("Default", Font.BOLD, 26);
		else
			return new Font("Default", Font.BOLD, 18);
	}
	
	/**
	 * Gets the font for every string in the <code>FormulaWindow</code> that
	 * is painted in superscript or subscript (used for D_G^{k}.
	 * 
	 * @param beamerMode
	 *            <code>true</code> if the beamerMode is enabled,
	 *            <code>false</code> otherwise.
	 * @return A italic font.
	 */
	public static Font formulaWindowSmallFont(boolean beamerMode) {
		if (beamerMode)
			return new Font("Default", Font.ITALIC, 22);
		else
			return new Font("Default", Font.ITALIC, 14);
	}

	/**
	 * Gets the horizontal gap between two matrices in the
	 * <code>MatrixComponent</code>.
	 * 
	 * @return the horizontal gap between two matrices in the
	 *         <code>MatrixComponent</code>.
	 */
	public static int matrixHgap() {
		return 10;
	}

	/**
	 * Gets the vertical gap between two matrices in the
	 * <code>MatrixComponent</code>.
	 * 
	 * @return the vertical gap between two matrices in the
	 *         <code>MatrixComponent</code>.
	 */
	public static int matrixVgap() {
		return 10;
	}

	public static int parenthesisHgap() {
		return 20;
	}

}
