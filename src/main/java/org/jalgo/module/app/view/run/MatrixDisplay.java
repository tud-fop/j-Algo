package org.jalgo.module.app.view.run;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jalgo.module.app.core.Matrix;
import org.jalgo.module.app.view.InterfaceConstants;
import org.jalgo.module.app.view.graph.HighlightState;

public class MatrixDisplay extends JPanel {

	private static final long serialVersionUID = 5100649092337883024L;
	
	private GridBagLayout matrixLayout;

	private JPanel matrixPanel;

	private ParenthesisDisplay openingParenthesis;
	private ParenthesisDisplay closingParenthesis;

	private MatrixLabel[][] labels;
	private int matrixSize;
	
	private boolean isOpaque;
	
	private int highlightedStart, highlightedInner, highlightedEnd;
	
	private int scaleHeight, scaleWidth;
	
	private Font defaultFont;
	private Font currentFont;
	private Graphics2D matrixGraphics;
	private FontRenderContext renderContext;
	
	private Matrix currentMatrix;
	private int[][] cellStates;
	private int[] colSizes;
	private int totalFreeSpace;
	
	private MatrixNamePanel matrixNamePanel;
	
	private boolean fontForced;
	
	private boolean beamerMode;
	
	private class CellDescriptor implements Comparable<CellDescriptor> {
		public int row, col, cellWidth;
		
		public CellDescriptor(int col, int row, int cellWidth) {
			this.row = row;
			this.col = col;
			this.cellWidth = cellWidth;
		}
		
		public int compareTo(CellDescriptor o) {
			CellDescriptor other;
			
			if ((o == null) || (!(o instanceof CellDescriptor)))
				throw new IllegalArgumentException();
			
			other = (CellDescriptor)o;
			
			return cellWidth - other.cellWidth;
		}
	}
	
	/**
	 * A panel to display a n x n Matrix (incl. parenthesis). Elements of a
	 * matrix can be highlighted (change in font style or in background color),
	 * if needed.
	 * 
	 * @param isOpaque
	 *            if <code>true</code>, set background non-transparent; if
	 *            <code>false</code>, set background transparent.
	 */
	public MatrixDisplay(boolean isOpaque) {
		this.beamerMode = false;
		
		GridBagConstraints constr = new GridBagConstraints();
		
		matrixLayout = new GridBagLayout();

		highlightedStart = -1;
		highlightedInner = -1;
		highlightedEnd = -1;
		
		renderContext = new FontRenderContext(new AffineTransform(), true, false);
		
		openingParenthesis = new ParenthesisDisplay(true);
		closingParenthesis = new ParenthesisDisplay(false);

		matrixPanel = new JPanel();
		matrixPanel.setLayout(matrixLayout);

		matrixNamePanel = new MatrixNamePanel();
		
		this.setLayout(new GridBagLayout());		
		
		constr.gridy = 0;
		constr.gridheight = 1;
		constr.gridwidth = 1;
		constr.fill = GridBagConstraints.REMAINDER;
		constr.ipady = InterfaceConstants.matrixVgap();
		constr.ipadx = 0; //InterfaceConstants.matrixHgap();		

		constr.gridx = 0;
		this.add(openingParenthesis, constr);

		constr.gridx = 1;
		constr.fill = GridBagConstraints.CENTER;
		this.add(matrixPanel, constr);
		
		constr.gridx = 2;
		constr.fill = GridBagConstraints.REMAINDER;
		this.add(closingParenthesis, constr);
		
		constr.gridx = 1;
		constr.gridy = 1;
		constr.fill = GridBagConstraints.CENTER;
		this.add(matrixNamePanel, constr);
			
		this.isOpaque = isOpaque;
				
		if (isOpaque) {
			this.setBackground(InterfaceConstants.formulaBackgroundColor());
		
			matrixPanel.setBackground(InterfaceConstants.formulaBackgroundColor());
			openingParenthesis.setBackground(InterfaceConstants.formulaBackgroundColor());
			closingParenthesis.setBackground(InterfaceConstants.formulaBackgroundColor());
		}

	}

	/**
	 * Forces the matrix to display a certain font
	 * 
	 * @param font
	 */
	public void setFontForced(Font font) {
		if ((font == null) || (matrixGraphics == null))
			fontForced = false;
		else {
			fontForced = true;
			currentFont = font;
		}
	}
	
	/**
	 * Returns the font currently used by the matrix
	 * 
	 */
	public Font getCurrentFont() {
		return currentFont;
	}
	
	/**
	 * Sets the labeling of the matrix
	 * 
	 * @param isDistance	
	 * 		True, if matrix is a distance matrix (paints a DG instead of a AG)
	 * @param k
	 * 		The index of the distance matrix
	 * 
	 */
	public void setMatrixName(boolean isDistance, int k) {
		matrixNamePanel.setMatrixName(isDistance, k);
	}
	
	/**
	 * Hides the labeling of the matrix
	 */
	public void hideMatrixName() {
		matrixNamePanel.hideMatrixName();
	}
	
	/**
	 * Toggles the beamer mode.
	 * 
	 * @param beamerMode
	 */
	public void updateBeamerMode(boolean beamerMode){
		this.beamerMode = beamerMode;
		prepareGraphics();
		repaint();
		revalidate();
	}
	
	/*
	 * Perpares the graphics context and the display font of
	 * the current matrix.
	 */
	private void prepareGraphics() {
		matrixGraphics = (Graphics2D)this.getGraphics();
		
		defaultFont = InterfaceConstants.getMatrixFont(beamerMode);
		
		if (!fontForced) {
			currentFont = InterfaceConstants.getMatrixFont(beamerMode);
		}
	}
	
	/**
	 * Defines the maximum bounds of a matrix
	 * 
	 * @param width
	 * @param height
	 */
	public void scaleTo(int width, int height) {
		scaleWidth = width;
		scaleHeight = height - (InterfaceConstants.formulaNormalFont(beamerMode).getSize() * 2);
	}

	private Rectangle2D getCellMetrics(int col, int row, Matrix matrix, Font font) {
		return font.getStringBounds(matrix.getValueAt(row, col).toString(), renderContext);
	}
	
	/**
	 * Calculates the average width of a column of
	 * a given matrix according to the given scaleWidth.
	 * of the display widget. The horizontal gap of each
	 * column will be subtracted of the average width.
	 * 
	 * @param matrix
	 * 		The given matrix.
	 *  
	 */
	private int getAverageColWidth(Font font) {
		int matrixWidth, avgWidth, minimumFontWidth, minimumColWidth;
		
		matrixWidth = scaleWidth - (2 * InterfaceConstants.matrixHgap());
		avgWidth = (matrixWidth / matrixSize) - (1 * InterfaceConstants.matrixHgap());
		minimumFontWidth = (int)font.getStringBounds("...", renderContext).getWidth();
		minimumColWidth = InterfaceConstants.matrixHgap() + minimumFontWidth;
		
		// At least a row has to contain the text "..."
		if (avgWidth < minimumColWidth)
			avgWidth = 0; //minimumColWidth;
		
		return avgWidth;
	}
	
	/**
	 * Returns the height of the matrix if using a default font.
	 * 
	 */
	private int getMatrixHeight() {
		return   (InterfaceConstants.getMatrixFont(beamerMode).getSize() * matrixSize) 
			   + (InterfaceConstants.matrixVgap() * (matrixSize + 2)) 
			  ;
	}
	
	/**
	 * Calculates the size of each column in the matrix based on the current font. 
	 * If a label is too big for a column, it will be shortened to "..."
	 * 
	 * The function will store the information about all shortened fields to
	 * the private member "cellStates" and the size of each column to "colSizes".
	 * The remaining space in the current matrix will be stored to "totalFreeSpace".
	 *  
	 */
	private int calculateDeactivatedFields() {
		ArrayList<CellDescriptor> tooLongList; 
		int avgColWidth;
		int freeSpace[];
		int elipseWidth;	
		int countDeactivated;
		
		// Prepare data structures
		countDeactivated = 0;
		tooLongList = new ArrayList<CellDescriptor>();

		// Try to find a usable font size
		avgColWidth = 0;
		
		while (avgColWidth == 0) {
			avgColWidth = getAverageColWidth(currentFont);
			
			if (currentFont.getSize() - 1 < InterfaceConstants.getMatrixMinimumFontSize(beamerMode))
				break;
			
			if (avgColWidth == 0) {
				currentFont = new Font(defaultFont.getName(), defaultFont.getStyle(), currentFont.getSize() - 1);
			}
		}

		
		elipseWidth = (int)currentFont.getStringBounds("...", renderContext).getWidth();
		
		cellStates = new int[matrixSize][matrixSize];
		colSizes = new int[matrixSize];
		freeSpace = new int[matrixSize];
	
		// Initialize
		Arrays.fill(colSizes, avgColWidth);
		Arrays.fill(freeSpace, avgColWidth);
		
		totalFreeSpace = avgColWidth * matrixSize;
		
		for (int row = 0; row < matrixSize; row ++) {
			if (avgColWidth > 0)
				Arrays.fill(cellStates[row], 0);
			else
				Arrays.fill(cellStates[row], -1);
		}
		
		if (avgColWidth == 0)
			return matrixSize * matrixSize;
		
		// Remove elements which are too long
		for (int col = 0; col < matrixSize; col ++) {
			int biggest = elipseWidth;
			
			for (int row = 0; row < matrixSize; row ++) {
				int cellMetrics = (int)getCellMetrics(col, row, currentMatrix, currentFont).getWidth();
					
				if (cellMetrics >= colSizes[col]) {
					// Element too long, disable and remember it...
					CellDescriptor descriptor;
					
					cellStates[col][row] = -1;
					
					descriptor = new CellDescriptor(col, row, cellMetrics);
					
					tooLongList.add(descriptor);
					
					cellMetrics = elipseWidth;
					
					countDeactivated ++;
				}

				// Element fits (now) into column, just set free space
				 if (cellMetrics >= biggest) {
					 biggest = cellMetrics;
				 }
			}
			
			colSizes[col] = biggest;
			totalFreeSpace -= biggest;
		}

		Collections.sort(tooLongList);
		
		// Re-enable deactivated fields, if possible
		for (CellDescriptor cell : tooLongList) {
			int need;
			
			need = cell.cellWidth - colSizes[cell.col];
			
			if (need <= 0) {
				cellStates[cell.col][cell.row] = 0;
				countDeactivated --;
			} else if (need <= totalFreeSpace) {
				totalFreeSpace -= need;
				cellStates[cell.col][cell.row] = 0;
				colSizes[cell.col] += need;
				countDeactivated --;
			}
		}
		
		return countDeactivated;
	}
	
	/**
	 * Calculates the size of the font based on the scaling width
	 * of the matrix. It also determines whether a field is shown or not
	 * by calling calculateDeactivatedFields()
	 * 
	 */
	private void calculateColumnSizes() {
		float scaleFactor;
		int deactivatedFields, tmpFields, tmpSize;
		
		// Is there a forced font size?
		if (fontForced == true) {
			calculateDeactivatedFields();
			return;
		}
		
		// Scale matrix font to fit the window
		scaleFactor = (scaleHeight / getMatrixHeight());

		if (scaleFactor < 1)
			scaleFactor = 1;
		
		if ((defaultFont.getSize() * scaleFactor) > InterfaceConstants.getMatrixMaximumFontSize(beamerMode))
			scaleFactor = InterfaceConstants.getMatrixMaximumFontSize(beamerMode) / defaultFont.getSize();
		
		currentFont = new Font(defaultFont.getName(), defaultFont.getStyle(), (int)(defaultFont.getSize() /* * scaleFactor */));
		
		deactivatedFields = calculateDeactivatedFields();
		tmpFields = deactivatedFields;

		// Scale down unless we have minimized the count of deactivated fields
		while(    (deactivatedFields > 0) 
			   && (currentFont.getSize() > InterfaceConstants.getMatrixFont(beamerMode).getSize())
		     )
		{
			currentFont = new Font(defaultFont.getName(), defaultFont.getStyle(), currentFont.getSize() - 1);
			
			deactivatedFields = calculateDeactivatedFields();
		}
		
		// It is still bad after decreasing the font size
		/*if (tmpFields == deactivatedFields)
		 		return;*/
		
		// To maximize font width, scale up unless the count of deactivated fields doesn't increase
		tmpFields = 10000;
		tmpSize = currentFont.getSize() - 1;
		
		while((tmpFields > deactivatedFields) && (currentFont.getSize() < InterfaceConstants.getMatrixMaximumFontSize(beamerMode)) && (tmpSize < currentFont.getSize())) {
			tmpSize = currentFont.getSize();
			
			currentFont = new Font(defaultFont.getName(), defaultFont.getStyle(), currentFont.getSize() + 1);
			
			deactivatedFields = calculateDeactivatedFields();
		}

		// Recalculate font
		currentFont = new Font(defaultFont.getName(), defaultFont.getStyle(), tmpSize);
		
		deactivatedFields = calculateDeactivatedFields();
	}
	
	public void redrawCurrentMatrix() {
		GridBagConstraints constr = new GridBagConstraints();
		boolean keepLabels = true;
		
		if ((scaleWidth == 0) || (currentMatrix == null) || (currentMatrix.getSize() < 2)) {
			openingParenthesis.setVisible(false);
			closingParenthesis.setVisible(false);
			
			return;
		}
		
		prepareGraphics();
		
		matrixPanel.removeAll();
		
		if (matrixSize != currentMatrix.getSize()) {
			keepLabels = false;
			
			matrixSize = currentMatrix.getSize();
			labels = new MatrixLabel[matrixSize][matrixSize];
		}

		openingParenthesis.setVisible(true);
		closingParenthesis.setVisible(true);
		
		calculateColumnSizes();
		
		for (int row = 0; row < currentMatrix.getSize(); row++) {
			for (int col = 0; col < currentMatrix.getSize(); col++) {
				String value;
				MatrixLabel l;
				
				value = currentMatrix.getValueAt(row, col).toString();
				if (!keepLabels) { 
					l = new MatrixLabel();
					labels[row][col] = l;
				}
				else {
					l = labels[row][col];
				}
				
				l.setHorizontalAlignment(JLabel.CENTER);
				l.setVerticalAlignment(JLabel.CENTER);
				l.setFont(currentFont);
				l.setForeground(InterfaceConstants.getMatrixColor());
				setLabelColorByState(row, col, HighlightState.NONE);
				l.setOpaque(isOpaque);
				
				l.setToolTipText(value);
				
				if (cellStates[col][row] == -1)
					l.setText("...");
				else
					l.setText(value);
				
				constr.gridx = col;
				constr.gridy = row;
				constr.gridheight = 1;
				constr.gridwidth = 1;
				constr.fill = GridBagConstraints.REMAINDER;
				constr.ipady = InterfaceConstants.matrixVgap();
				constr.ipadx = InterfaceConstants.matrixHgap();
				
				matrixPanel.add(l, constr);

				Graphics2D g = (Graphics2D) l.getGraphics();
				if (g != null)
					g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);
			}
		}

		Dimension matrixLayoutDimension = matrixLayout
				.minimumLayoutSize(matrixPanel);
		Dimension parenthesisDimension = new Dimension(11,
				(int) matrixLayoutDimension.getHeight() + 5);

		openingParenthesis.setMinimumSize(parenthesisDimension);
		openingParenthesis.setPreferredSize(parenthesisDimension);
		openingParenthesis.setMaximumSize(parenthesisDimension);

		closingParenthesis.setMinimumSize(parenthesisDimension);
		closingParenthesis.setPreferredSize(parenthesisDimension);
		closingParenthesis.setMaximumSize(parenthesisDimension);

		matrixPanel.setMinimumSize(matrixLayoutDimension);
		matrixPanel.setPreferredSize(matrixLayoutDimension);
		matrixPanel.setMaximumSize(matrixLayoutDimension);		
	}
	
	/**
	 * Updates every element of the matrix, including its highlighting and its
	 * opaque state and renders it into the panel.
	 * 
	 * @param matrix
	 *            the given matrix
	 */
	public void updateMatrix(Matrix matrix) {
		if ((scaleWidth == 0) || (matrix == null) || (matrix.getSize() < 2)) {
			openingParenthesis.setVisible(false);
			closingParenthesis.setVisible(false);
			
			return;
		}
		
		currentMatrix = matrix;
		redrawCurrentMatrix();
	}
	
	/**
	 * Sets the color of a matrix element to the color given by its state.
	 * 
	 * @param x
	 *            the column
	 * @param y
	 *            the row
	 * @param state
	 *            the state the element is currently in.
	 */
	private void setLabelColorByState(int row, int col, HighlightState state) {
		labels[row][col].setBackground(InterfaceConstants.matrixBackgroundColorForHighlightState(state));
		labels[row][col].setForeground(InterfaceConstants.matrixForegroundColorForHighlightState(state));
	}
	
	/**
	 * Removes the highlighting of the start element, inner element and the end
	 * element of the path.
	 */
	public void removePathHighlighting() {
		if ((highlightedStart == -1) || (highlightedEnd == -1) || (highlightedInner == -1)) {
			return;
		}
		
		setLabelColorByState(highlightedStart, highlightedInner, HighlightState.NONE);
		setLabelColorByState(highlightedInner, highlightedInner, HighlightState.NONE);
		setLabelColorByState(highlightedInner, highlightedEnd, HighlightState.NONE);
	}
	
	/**
	 * Sets the highlighting of the matrix elements according to their
	 * <code>HighlightState</code> in the following painting order:<br>
	 * 1. (start,inner)<br>
	 * 2. (inner,end)<br>
	 * 3. (inner, inner) (the topmost coloring)<br>
	 * 
	 * @param start
	 *            the start coordinate
	 * @param inner
	 *            the inner coordinate
	 * @param end
	 *            the end coordinate
	 */
	public void setPathHighlighting(int start, int inner, int end) {
		
		removePathHighlighting();
		
		if (    (start > matrixSize) || (end > matrixSize) || (inner > matrixSize)
		     || (start < 0) || (end < 0) || (inner < 0)
		   ) 
		{
			throw new IllegalArgumentException();
		}
		
		highlightedStart = start;
		highlightedInner = inner;
		highlightedEnd = end;
				
		setLabelColorByState(highlightedStart, highlightedInner, HighlightState.PRE_PATH);
		setLabelColorByState(highlightedInner, highlightedEnd, HighlightState.POST_PATH);

		// Enforce the color of the current element
		setLabelColorByState(highlightedInner, highlightedInner, HighlightState.CURRENT);
	}
	
	/**
	 * Removes the highlighting of all labels in the matrix
	 */
	public void removeChangeHighlighting() {
		for (int row = 0; row < matrixSize; row ++) {
			for (int col = 0; col < matrixSize; col ++) {
				labels[col][row].setBackground(InterfaceConstants.matrixBackgroundColorForHighlightState(HighlightState.NONE));
				labels[col][row].setForeground(InterfaceConstants.matrixForegroundColorForHighlightState(HighlightState.NONE));
			}
		}
			
	}
	
	/**
	 * Sets the matrix coloring for a whole matrix. All elements up to (<code>endRow,endCol</code>)
	 * are already processed, the (<code>endRow,endCol</code>) is the
	 * current element and the ones after it still need to be processed (and are
	 * therefore unchanged) in the algorithm.
	 * 
	 * @param endRow
	 *            the row of the current element
	 * @param endCol
	 *            the column of the current element
	 */
	public void setChangeHighlighting(int endRow, int endCol) {
		for (int row = 0; row < matrixSize; row ++) {
			for (int col = 0; col < matrixSize; col ++) {
				if ((row > endRow) || ((row == endRow) && (col > endCol))) { 
					labels[row][col].setBackground(InterfaceConstants.unchangedMatrixEntryBackgroundColor());
					labels[row][col].setForeground(InterfaceConstants.unchangedMatrixEntryForegroundColor());					
				}
				else if (row == endRow && col == endCol) {
					labels[row][col].setBackground(InterfaceConstants.currentMatrixEntryBackgroundColor());
					labels[row][col].setForeground(InterfaceConstants.currentMatrixEntryForegroundColor());
				}
				else {
					labels[row][col].setBackground(InterfaceConstants.changedMatrixEntryBackgroundColor());
					labels[row][col].setForeground(InterfaceConstants.changedMatrixEntryForegroundColor());
				}
			}
		}
	}

	/**
	 * A panel for displaying a parenthesis which can either be on the left or
	 * on the right side of a matrix.
	 */
	private class ParenthesisDisplay extends JPanel {

		private static final long serialVersionUID = -8294840496607673029L;

		private boolean isOpeningParenthesis;

		public ParenthesisDisplay(boolean isOpeningParenthesis) {
			this.isOpeningParenthesis = isOpeningParenthesis;
		}

		private GeneralPath createArc(float height) {
			GeneralPath arc = new GeneralPath();
			float arcScale, leftOuter, leftInner;

			if (height < 60) {
				arcScale = (height / 2) / 30.f;
				leftOuter = 10 - 7 * arcScale;
				leftInner = 11 - 6 * arcScale;
			} else {
				arcScale = 1;
				leftOuter = 0;
				leftInner = 2;
			}

			// Outer Shape
			arc.moveTo(10, 0);
			arc.curveTo(leftOuter, 10 * arcScale, leftOuter, 30 * arcScale,
					leftOuter, 40 * arcScale);
			arc.lineTo(leftOuter, height - 40 * arcScale);
			arc.curveTo(leftOuter, height - 30 * arcScale, leftOuter, height
					- 10 * arcScale, 10, height);

			arc.lineTo(11, height);
			arc.curveTo(leftInner, height - 10 * arcScale, leftInner, height
					- 30 * arcScale, leftInner, height - 40 * arcScale);
			arc.lineTo(leftInner, 40 * arcScale);
			arc.curveTo(leftInner, 30 * arcScale, leftInner, 10 * arcScale, 11,
					0);

			arc.closePath();

			return arc;
		}

		public void paint(Graphics output) {
			Graphics2D g;
			GeneralPath path;
			AffineTransform trans;

			// Setup
			g = (Graphics2D) output;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			// Draw background
			g.setColor(this.getBackground());
			g.fill(this.getBounds());

			//
			g.setColor(Color.BLACK);
			g.setStroke(new BasicStroke(10));

			path = createArc(this.getHeight());

			if (isOpeningParenthesis) {
				trans = AffineTransform.getScaleInstance(1, 1);
				trans.translate(0, 0);
			} else {
				trans = AffineTransform.getScaleInstance(-1, 1);
				trans.translate(-this.getWidth(), 0);
			}

			path.transform(trans);

			g.fill(path);

		}
	}

	/**
	 * A single element of a matrix as a <code>JLabel</code>.
	 */
	private class MatrixLabel extends JLabel {
		private static final long serialVersionUID = 1456853217007624321L;
		public boolean isOpaque;
		public Color backgroundColor;
		
		public MatrixLabel() {
			super();
			
			super.setOpaque(false);
			
			isOpaque = false;
			backgroundColor = super.getBackground();
		}
		
		/**
		 * Returns whether the matrix label paints its own background or not.
		 * 
		 * @return
		 */
		public boolean getOpaque() {
			return isOpaque;
		}

		/**
		 * Toggles whether the matrix label paints its own background or not.
		 * 
		 * @param opaque
		 */
		public void setOpaque(boolean opaque) {
			this.isOpaque = opaque;
		}
		
		public Color getBackground() {
			return backgroundColor;
		}
		
		public void setBackground(Color color) {
			if (color == null)
				setOpaque(false);
			
			backgroundColor = color;
		}
		
		public void paint(Graphics graphics) {
			Graphics2D graph;
			RoundRectangle2D box;
			float width, height;

			graph = (Graphics2D)graphics;
			graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
								   RenderingHints.VALUE_ANTIALIAS_ON
								  );				
			
			if (isOpaque) {			
				width = (float)getBounds().getWidth();
				height = (float)getBounds().getHeight();
			
				box = new RoundRectangle2D.Float(0, 
												 0, 
												 width , 
												 height , 
												 8f, 
												 8f
												);
			
				// Draw background
				graph.setColor(this.getBackground());
				graph.fill(box);
			}
			
			super.paint(graphics);
		}
	}
	
	private class MatrixNamePanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private boolean isDistance, doPainting;
		private int currentMatrix;
		private Font normalFont, smallFont;
		private FontMetrics normalMetrics, smallMetrics;
		private Graphics2D graphics;
		private FontRenderContext renderContext;
		
		public MatrixNamePanel() {
			super();
			
			normalFont = InterfaceConstants.formulaWindowNormalFont(beamerMode); 
			smallFont = InterfaceConstants.formulaWindowSmallFont(beamerMode);
			
			renderContext = new FontRenderContext(new AffineTransform(), true, false);
		}
		
		private void defineSize() {
			Dimension dimension = new Dimension(normalFont.getSize() * 3, calculateHeight());
			
			this.setMinimumSize(dimension);
			this.setPreferredSize(dimension);
			this.setMaximumSize(dimension);
		}
		
		public void setMatrixName(boolean isDistance, int k) {
			this.isDistance = isDistance;
			this.doPainting = true;
			currentMatrix = k;
			
			defineSize();
		}

		public void hideMatrixName() {
			this.doPainting = false;

			defineSize();
		}

		private int paintString(String string, Font font, int offsetX, int offsetY) {
			graphics.setFont(font);
			
			graphics.drawString(string, offsetX, offsetY);
			return offsetX + (int)graphics.getFontMetrics(font).getStringBounds(string, graphics).getWidth();
		}
		
		private int paintStringNormal(String string, int offsetX, int offsetY) {
			return paintString(string, normalFont, offsetX, offsetY);
		}
		
		private int paintStringSmall(String string, int offsetX, int offsetY) {
			return paintString(string, smallFont, offsetX, offsetY);
		}		
		
		/**
		 * Paints $D_G^{(superscript)} $ (LaTeX Math style)
		 * 
		 * @param metrics
		 *            the used metrics for the string
		 * @param superscript
		 * @return the new x offset
		 */
		private int paintDistanceSymbol(String symbol, int superscript) {
			int fontHeight, offsetX, offsetY;
			int superscriptOffset, subscriptOffset;
			
			fontHeight = normalFont.getSize();
			
			// Paint "D"
			offsetY = fontHeight;
			offsetX = getWidth()/2 - calculateWidth() /2;
			
			graphics.setFont(normalFont);		
			offsetX = paintStringNormal(symbol, offsetX, offsetY + fontHeight/2);
			
			superscriptOffset = 0;
			
			if (superscript > -1) {
				// Paint (k)
				superscriptOffset = paintStringSmall("("+superscript+")", offsetX, offsetY);
			}
			
			// Paint G
			subscriptOffset = paintStringSmall("G", offsetX, offsetY + fontHeight);
			
			return offsetX + Math.max(subscriptOffset, superscriptOffset);
		}		
		
		private int calculateHeight() {
			return (normalFont.getSize() * 2) + smallFont.getSize();
		}
		
		private int calculateWidth() {
			int superscriptWidth;
			String symbol;
			
			if ((normalMetrics == null) || (smallMetrics == null))
				return 0;

			if (isDistance) {
				symbol = "D";
		   		superscriptWidth = (int)smallFont.getStringBounds("("+currentMatrix+")", renderContext).getWidth();
			}
			else {
				symbol = "A";
				superscriptWidth = 0;
			}
			
			return   (int)normalFont.getStringBounds(symbol, renderContext).getWidth()
			       + Math.max(
			    		   		superscriptWidth,
			    		   		(int)smallFont.getStringBounds("G", renderContext).getWidth()
			    		   	 );
		}
		
		public void paint(Graphics g) {
			// Setup
			this.graphics = (Graphics2D)g;			
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			super.paint(g);
			
			if (isOpaque) {
				// Draw background
				graphics.setColor(InterfaceConstants.formulaBackgroundColor());
				graphics.fill(new Rectangle(this.getSize()));		
			}
			
			if (!doPainting)
				return;

			normalFont = InterfaceConstants.formulaWindowNormalFont(beamerMode); 
			smallFont = InterfaceConstants.formulaWindowSmallFont(beamerMode);			
			
			normalMetrics = graphics.getFontMetrics(normalFont);
			smallMetrics = graphics.getFontMetrics(smallFont);			
			
			graphics.setColor(InterfaceConstants.formulaForegroundColor());
			graphics.setStroke(new BasicStroke(1));			
			
			if (isDistance)
				paintDistanceSymbol("D", currentMatrix);
			else
				paintDistanceSymbol("A", -1);
		}
	}
}