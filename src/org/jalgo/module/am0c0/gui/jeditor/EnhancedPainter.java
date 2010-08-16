package org.jalgo.module.am0c0.gui.jeditor;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.text.Segment;

import org.jalgo.module.am0c0.gui.jeditor.JEditor.ExInteger;
import org.jalgo.module.am0c0.gui.jeditor.jedit.*;
import org.jalgo.module.am0c0.gui.jeditor.jedit.tokenmarker.*;
import org.jalgo.module.am0c0.model.CodeObject;

/**
 * Improved TextAreaPainter for JEditor component which features line numbers,
 * marked lines and more.
 * 
 * @author Felix Schmitt
 * 
 */

public class EnhancedPainter extends TextAreaPainter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class used for marking a range of lines with a specific color
	 * 
	 * @author Felix Schmitt
	 * 
	 */
	public static class Marker {
		private Color color;
		private int start;
		private int end;

		/**
		 * sets the line range to mark
		 * 
		 * @param lineStart
		 *            index of the first line
		 * @param lineEnd
		 *            index of the last line
		 */
		public void setLines(int lineStart, int lineEnd) throws IllegalArgumentException {
			this.start = lineStart;
			this.end = lineEnd;

			if (lineEnd < lineStart)
				throw new IllegalArgumentException("last line should be >= first line");
		}
		
		/**
		 * returns the start line
		 * @return the start line
		 */
		public int getStartLine() {
			return start;
		}
		
		/**
		 * returns the end line
		 * @return the end line
		 */
		public int getEndLine() {
			return end;
		}

		/**
		 * sets the {@link Color} of the Marker
		 * 
		 * @param color
		 *            the new {@link Color}
		 */
		public void setColor(Color color) {
			this.color = color;
		}

		/**
		 * returns the {@link Color} of this Marker
		 * 
		 * @return the current {@link Color}
		 */
		public Color getColor() {
			return color;
		}

		/**
		 * checks if line is in marking range
		 * 
		 * @param line
		 *            index of the line to check
		 * @return if line is in marking range
		 */
		public boolean isMarked(int line) {
			return line >= start && line <= end;
		}

		/**
		 * constructor
		 * 
		 * @param lineStart
		 *            index of the first line
		 * @param lineEnd
		 *            index of the last line
		 */
		public Marker(int lineStart, int lineEnd) {
			color = new Color(246, 206, 136);
			setLines(lineStart, lineEnd);
		}
	}

	/**
	 * offset in pixels from line numbers to the seperating line between numbers
	 * and text
	 */
	private static final int LINE_NUMBERS_OFFSET = 5;

	private int lineNumbersWidth;
	private int LINE_NUMBERS_WIDTH;
	private boolean lineNumbersVisible;
	private Marker marker;
	private boolean markerVisible;
	Segment currentLine;

	/**
	 * constructor
	 * 
	 * @param textArea
	 *            the {@link JEditor} for this painter
	 * @param defaults
	 *            the {@link TextAreaDefaults} to use
	 */
	public EnhancedPainter(JEditor textArea, TextAreaDefaults defaults) {
		super(textArea, defaults);

		setTextArea(textArea);
		marker = new Marker(-1, -1);
		markerVisible = false;
		lineNumbersVisible = true;
		LINE_NUMBERS_WIDTH = getFm().stringWidth("000");
		lineNumbersWidth = LINE_NUMBERS_WIDTH + LINE_NUMBERS_OFFSET;
	}

	/**
	 * sets visibility of line numbers. calls repaint
	 * 
	 * @param visible
	 *            new line numbers visibility
	 */
	public void setLineNumbersVisible(boolean visible) {
		lineNumbersVisible = visible;
		if (visible)
			lineNumbersWidth = LINE_NUMBERS_WIDTH + LINE_NUMBERS_OFFSET;
		else
			lineNumbersWidth = 0;
		repaint();
	}

	/**
	 * sets the line range which should be marked. calls repaint
	 * 
	 * @param lineStart
	 *            index of first line
	 * @param lineEnd
	 *            index of last line
	 */
	public void setLineMarker(int lineStart, int lineEnd) {
		marker.setLines(lineStart, lineEnd);
		repaint();
	}
	
	/**
	 * returns the range marked by the line marker
	 * @param lineStart returns the start line index. pass null to ignore
	 * @param lineEnd returns the end line index. pass null to ignore
	 */
	public void getLineMarker(ExInteger lineStart, ExInteger lineEnd) {
		if (lineStart != null)
			lineStart.set(marker.getStartLine());
		if (lineEnd != null)
			lineEnd.set(marker.getEndLine());
	}

	/**
	 * sets visibility of the line marker. calls repaint
	 * 
	 * @param visible
	 *            new line marker visibility
	 */
	public void showLineMarker(boolean visible) {
		markerVisible = visible;
		repaint();
	}
	
	/**
	 * returns the line marker's visibility
	 * @return if the line marker is visible
	 */
	public boolean isLineMarkerVisible() {
		return markerVisible;
	}

	/**
	 * updates the line numbers width to be large enough for {@link String} s
	 * 
	 * @param s
	 *            the {@link String} which has to fit in line numbers width
	 */
	public void updateLineNumbersWidth(String s) {
		FontMetrics fm = getFm();
		if (getLineNumbersWidth() - LINE_NUMBERS_OFFSET < fm.stringWidth(s))
			setLineNumbersWidth(fm.stringWidth(s));
	}

	/**
	 * sets new line numbers width
	 * 
	 * @param width
	 *            new width in pixels
	 */
	public void setLineNumbersWidth(int width) {
		this.LINE_NUMBERS_WIDTH = width;
		setLineNumbersVisible(lineNumbersVisible);
	}

	/**
	 * returns the current line numbers width
	 * 
	 * @return current line numbers width
	 */
	public int getLineNumbersWidth() {
		return lineNumbersWidth;
	}

	/**
	 * returns the index of the current line
	 * 
	 * @return index of current line
	 */
	public int getCurrentLineIndex() {
		return currentLineIndex;
	}

	/**
	 * sets the index of the current line
	 * 
	 * @param index
	 *            index of new current line
	 */
	public void setCurrentLineIndex(int index) {
		this.currentLineIndex = index;
	}

	/**
	 * return cached tokens of current line
	 * 
	 * @return the cached {@link Token}
	 */
	public Token getCurrentLineTokens() {
		return currentLineTokens;
	}

	/**
	 * sets the tokens for current line
	 * 
	 * @param token
	 *            new {@link Token} for current line
	 */
	public void setCurrentLineTokens(Token token) {
		this.currentLineTokens = token;
	}

	/**
	 * refreshed the width of line numbers area to fit the largest line number
	 * available
	 */
	public void refreshLineNumbersWidth() {
		// update to the largest necessary line number width before
		// painting lines
		setLineNumbersWidth(0);
		for (int line = 1; line <= textArea.getLineCount(); line++) {
			if (!((JEditor) textArea).hasModel()) {
				String lineNumber = String.valueOf(line);
				updateLineNumbersWidth(lineNumber);
			} else {
				ExInteger startLine = new ExInteger(0);
				CodeObject codeObject = ((JEditor) textArea).getCodeFromLine(line - 1, startLine, null);

				// if the codeObject is null, we might be at the last line
				// which does not have to have a codeObject
				// so it is fine not to print a line number here
				if (codeObject != null && line - 1 == startLine.getValue() && codeObject.getAddress() != null
						&& codeObject.getAddress().isVisible()) {
					String lineAddress = codeObject.getAddress().toString();
					updateLineNumbersWidth(lineAddress);
				}
			}
		}
	}

	@Override
	/**
	 * see {@link TextAreaPainter}
	 */
	public void paint(Graphics gfx) {
		tabSize = getFm().charWidth(' ') * 4;
				//* ((Integer) textArea.getDocument().getProperty(PlainDocument.tabSizeAttribute)).intValue();

		Rectangle clipRect = gfx.getClipBounds();

		gfx.setColor(getBackground());
		gfx.fillRect(clipRect.x, clipRect.y, clipRect.width, clipRect.height);

		// We don't use yToLine() here because that method doesn't
		// return lines past the end of the document
		int height = getFm().getHeight();
		int firstLine = textArea.getFirstLine();
		int firstInvalid = firstLine + clipRect.y / height;
		// Because the clipRect's height is usually an even multiple
		// of the font height, we subtract 1 from it, otherwise one
		// too many lines will always be painted.
		int lastInvalid = firstLine + (clipRect.y + clipRect.height - 1) / height;

		try {
			TokenMarker tokenMarker = textArea.getDocument().getTokenMarker();
			int x = textArea.getHorizontalOffset() + lineNumbersWidth;

			// paint the actual lines
			for (int line = firstInvalid; line <= lastInvalid + 1; line++) {
				paintLine(gfx, tokenMarker, line, x);
			}

			if (tokenMarker != null && tokenMarker.isNextLineRequested()) {
				int h = clipRect.y + clipRect.height;
				repaint(0, h, getWidth(), getHeight() - h);
			}
		} catch (Exception e) {
			System.err.println("Error repainting line" + " range {" + firstInvalid + "," + (lastInvalid + 1) + "}:");
			e.printStackTrace();
		}
	}

	protected void paintLineNumber(Graphics gfx, int line, int y) {
		Color color = gfx.getColor();
		Font defaultFont = gfx.getFont();

		gfx.setFont(defaultFont.deriveFont(Font.PLAIN));

		gfx.setColor(getBackground());
		gfx.fillRect(0, y, lineNumbersWidth, getFm().getHeight());

		gfx.setColor(Color.LIGHT_GRAY);
		gfx.drawLine(lineNumbersWidth - 1, y, lineNumbersWidth - 1, y + getFm().getHeight());

		gfx.setColor(new Color(100, 100, 100));

		// prints default line numbers or CodeObject addresses
		if (!((JEditor) textArea).hasModel()) {
			if (line <= textArea.getLineCount()) {
				String lineNumber = String.valueOf(line);

				gfx.drawString(lineNumber, lineNumbersWidth - LINE_NUMBERS_OFFSET - getFm().stringWidth(lineNumber), y);
			}
		} else {
			ExInteger startLine = new ExInteger(0);
			CodeObject codeObject = ((JEditor) textArea).getCodeFromLine(line - 1, startLine, null);

			// if the codeObject is null, we might be at the last line which
			// does not have to have a codeObject
			// so it is fine not to print a line number here
			if (codeObject != null && line - 1 == startLine.getValue() && codeObject.getAddress() != null
					&& codeObject.getAddress().isVisible()) {
				String lineAddress = codeObject.getAddress().toString();

				gfx.drawString(lineAddress, lineNumbersWidth - LINE_NUMBERS_OFFSET - getFm().stringWidth(lineAddress),
						y);
			}
		}

		gfx.setFont(defaultFont);
		gfx.setColor(color);
	}

	@Override
	/**
	 * see {@link TextAreaPainter}
	 */
	protected void paintLine(Graphics gfx, TokenMarker tokenMarker, int line, int x) {
		Font defaultFont = getFont();
		((Graphics2D) gfx).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		Color defaultColor = getForeground();

		currentLineIndex = line;
		int y = textArea.lineToY(line);

		if (markerVisible && marker.isMarked(line))
			paintLineMarker(gfx, line, y);

		if (line < 0 || line >= textArea.getLineCount()) {
			if (paintInvalid) {
				paintHighlight(gfx, line, y);

				styles[Token.INVALID].setGraphicsFlags(gfx, defaultFont);
				gfx.drawString("~", 0, y + getFm().getHeight());
			}
		} else if (tokenMarker == null) {
			paintPlainLine(gfx, line, defaultFont, defaultColor, x, y);
		} else {
			paintSyntaxLine(gfx, tokenMarker, line, defaultFont, defaultColor, x, y);
		}

		// draw line numbers
		if (lineNumbersVisible)
			paintLineNumber(gfx, line, y);
		gfx.setFont(defaultFont);
	}

	protected void paintLineMarker(Graphics gfx, int line, int y) {
		int height = getFm().getHeight();
		y += getFm().getLeading() + getFm().getMaxDescent();

		gfx.setColor(marker.getColor());
		gfx.fillRect(lineNumbersWidth, y, getWidth() - lineNumbersWidth, height);
	}

	@Override
	/**
	 * see {@link TextAreaPainter}
	 */
	protected void paintLineHighlight(Graphics gfx, int line, int y) {
		int height = getFm().getHeight();
		y += getFm().getLeading() + getFm().getMaxDescent();

		int selectionStart = textArea.getSelectionStart();
		int selectionEnd = textArea.getSelectionEnd();

		if (lineHighlight && line == textArea.getCaretLine()) {
			gfx.setColor(lineHighlightColor);
			gfx.fillRect(lineNumbersWidth, y, getWidth() - lineNumbersWidth, height);
		}

		if (selectionStart != selectionEnd) {
			if (textArea.isEnabled()) {
				gfx.setColor(selectionColor);

				int selectionStartLine = textArea.getSelectionStartLine();
				int selectionEndLine = textArea.getSelectionEndLine();
				int lineStart = textArea.getLineStartOffset(line);

				int x1, x2;
				if (textArea.isSelectionRectangular()) {
					int lineLen = textArea.getLineLength(line);
					x1 = textArea._offsetToX(line, Math.min(lineLen, selectionStart
							- textArea.getLineStartOffset(selectionStartLine)));
					x2 = textArea._offsetToX(line, Math.min(lineLen, selectionEnd
							- textArea.getLineStartOffset(selectionEndLine)));
					if (x1 == x2)
						x2++;
				} else if (selectionStartLine == selectionEndLine) {
					x1 = textArea._offsetToX(line, selectionStart - lineStart);
					x2 = textArea._offsetToX(line, selectionEnd - lineStart);
				} else if (line == selectionStartLine) {
					x1 = textArea._offsetToX(line, selectionStart - lineStart);
					x2 = getWidth();
				} else if (line == selectionEndLine) {
					x1 = lineNumbersWidth;
					x2 = textArea._offsetToX(line, selectionEnd - lineStart);
				} else {
					x1 = lineNumbersWidth;
					x2 = getWidth();
				}

				// "inlined" min/max()
				gfx.fillRect(x1 > x2 ? (x2) : (x1), y, x1 > x2 ? (x1 - x2) : (x2 - x1), height);
			}
		}
	}
}
