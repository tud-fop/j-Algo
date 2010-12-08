package org.jalgo.module.am0c0.gui.jeditor;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Observer;

import javax.print.attribute.IntegerSyntax;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.Utilities;

import org.jalgo.main.util.Messages;
import org.jalgo.module.am0c0.gui.jeditor.jedit.JEditTextArea;
import org.jalgo.module.am0c0.gui.jeditor.jedit.SyntaxDocument;
import org.jalgo.module.am0c0.gui.jeditor.jedit.SyntaxStyle;
import org.jalgo.module.am0c0.gui.jeditor.jedit.TextAreaDefaults;
import org.jalgo.module.am0c0.gui.jeditor.jedit.tokenmarker.Token;
import org.jalgo.module.am0c0.gui.jeditor.jedit.tokenmarker.TokenMarker;
import org.jalgo.module.am0c0.model.CodeObject;
import org.jalgo.module.am0c0.model.GenericProgram;

/**
 * Improved {@link JEditTextArea} with support for line numbers, an underlying code
 * model, automatic code highlighting and a marker.
 * 
 * @author Felix Schmitt
 * 
 */
public class JEditor extends JEditTextArea {

	/**
	 * Implementation of IntegerSyntax with set() feature.
	 * 
	 * @author Felix Schmitt
	 * 
	 */
	public static class ExInteger extends IntegerSyntax {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected int val;

		public ExInteger(int value) {
			super(value);
			val = value;
		}

		/**
		 * sets new integer value
		 * 
		 * @param value
		 *            new value
		 */
		public final void set(int value) {
			val = value;
		}

		/**
		 * returns integer value
		 * 
		 * @return the value
		 */
		@Override
		public int getValue() {
			return val;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected boolean beamerMode;
	protected boolean modified;
	protected boolean codeHighlightMode;
	protected GenericProgram<? extends CodeObject> model;
	protected JFileChooser fileChooser;

	protected EnhancedPainter enhancedPainter;

	protected Observer observer;

	/**
	 * constructor creates a new JEditor instance with optional code model
	 * 
	 * @param model
	 *            the underlying code model. if model is null, no code model
	 *            will be used
	 */
	public JEditor(Observer observer, GenericProgram<? extends CodeObject> model) {
		this(observer, TextAreaDefaults.getDefaults(), model);
	}

	/**
	 * constructor creates a new JEditor instance with default values for the
	 * TextArea and an optional code model
	 * 
	 * @param defaults
	 *            default values for the TextArea, see documentation for
	 *            {@link JEditTextArea} for further info
	 * @param model
	 *            the underlying code model. if model is null, no code model
	 *            will be used
	 */
	public JEditor(Observer observer, TextAreaDefaults defaults,
			GenericProgram<? extends CodeObject> model) {
		super(defaults);
		setDocument(new SyntaxDocument());
		setCaretBlinkEnabled(false);

		this.observer = observer;
		if (observer == null)
			throw new IllegalArgumentException("The observer should not be null"); //$NON-NLS-1$

		fileChooser = new JFileChooser(System.getProperty("user.dir"));
		
		fileChooser.addChoosableFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return Messages.getString("am0c0", "JEditor.1"); //$NON-NLS-1$
			}

			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().endsWith(".am0"); //$NON-NLS-1$
			}
		});
		
		fileChooser.addChoosableFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return Messages.getString("am0c0", "JEditor.3"); //$NON-NLS-1$
			}

			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().endsWith(".c0"); //$NON-NLS-1$
			}
		});
		
		enhancedPainter = new EnhancedPainter(this, defaults);
		setPainter(enhancedPainter);
		setMouseListener();

		setModified(false);
		setBeamerMode(false);

		setCodeHighlightMode(model != null);

		updateModel(model);
	}
	
	private void setMouseListener() {
		enhancedPainter.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent arg0) {
				int line = yToLine(arg0.getPoint().y);
				updateCodeHighlight(line);
			}

			@Override
			public void mouseDragged(MouseEvent arg0) {
			}
		});
	}

	@Override
	protected void documentChanged(DocumentEvent evt) {
		super.documentChanged(evt);
		setModified(true);
		
	}

	/**
	 * sets the beamer mode
	 * 
	 * @param beamerMode
	 *            value for beamer mode
	 */
	public void setBeamerMode(boolean beamerMode) {
		this.beamerMode = beamerMode;
		repaint();
	}

	/**
	 * returns if code has been altered since last save
	 * 
	 * @return the modified flag
	 */
	public boolean isModified() {
		return modified;
	}

	/**
	 * sets the modified flag
	 * 
	 * @param modified
	 *            new value for modified flag
	 */
	public void setModified(boolean modified) {
		this.modified = modified;
		observer.update(null, this);
		repaint();
	}

	/**
	 * sets line numbers visibility
	 * 
	 * @param visible
	 *            line numbers visibility
	 */
	public void showLineNumbers(boolean visible) {
		enhancedPainter.setLineNumbersVisible(visible);
	}

	/**
	 * sets the range for the line marker. marked lines will be highlighted if
	 * the line marker is visible
	 * 
	 * @param lineStart
	 *            index of the first marked line. set to -1 to mark no line
	 * @param lineEnd
	 *            index of the last marked line
	 */
	public void setLineMarker(int lineStart, int lineEnd) {
		enhancedPainter.setLineMarker(lineStart, lineEnd);
	}

	/**
	 * sets visibility of line marker
	 * 
	 * @param visible
	 *            line marker visibility
	 */
	public void showLineMarker(boolean visible) {
		enhancedPainter.showLineMarker(visible);
	}

	/**
	 * sets if selected code should be highlighted automatically. needs an
	 * underlying code model
	 * 
	 * @param codeHighlight
	 *            if selected code should be highlighted
	 */
	public void setCodeHighlightMode(boolean codeHighlight) {
		this.codeHighlightMode = codeHighlight;
		if (codeHighlight)
			showLineMarker(true);
		repaint();
	}

	/**
	 * updates the highlighted code if codeHighlightMode == true and there is a model
	 * @param line the line to check highlighting for
	 */
	private void updateCodeHighlight(int line) {
		if (model != null && codeHighlightMode) {
			ExInteger startLine = new ExInteger(0);
			CodeObject codeObject = getCodeFromLine(line, startLine, null);
			if (codeObject != null)
				setLineMarker(startLine.getValue(), startLine.getValue()
						+ codeObject.getLinesCount() - 1);
			else
				setLineMarker(-1, -1);
			repaint();
		}
	}

	/**
	 * enables/disabled the component for editing. sets caret's visibility and
	 * line highlights
	 * 
	 * @param enabled
	 *            enables/disables the component
	 */
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		setCaretVisible(enabled);
		getPainter().setLineHighlightEnabled(enabled);
	}

	/**
	 * updates the underlying code model using a {@link GenericProgram}
	 * 
	 * @param model
	 *            new value for the code model. null will disable the underlying
	 *            code model
	 */
	public void updateModel(GenericProgram<? extends CodeObject> model) {
		this.model = model;

		if (model != null) {
			StringBuffer buf = new StringBuffer();
			for (CodeObject codeObj : model)
				buf.append(codeObj.getCodeText() + "\n"); //$NON-NLS-1$
			setText(buf.toString());
		}

		setEditable(model == null);
		enhancedPainter.refreshLineNumbersWidth();

		repaint();
	}

	/**
	 * checks whether an underlying code model has been set or not
	 * 
	 * @return true if the code model != null
	 */
	public boolean hasModel() {
		return model != null;
	}

	/**
	 * saves the entered code if the user confirms the respective dialog and
	 * sets modified to false
	 * 
	 * @return the path if the user confirmed the dialog or "" if not
	 */
	public String saveCode() {
		boolean result = fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION;
		String ret = "";
		if (result) {
			File file = fileChooser.getSelectedFile();
			ret = file.getAbsolutePath();
			PrintWriter printWriter;
			try {
				printWriter = new PrintWriter(new FileWriter(file));

				printWriter.print(getText());
				printWriter.close();

				setModified(false);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), Messages.getString("am0c0", "JEditor.0"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				return "";
			}
		}

		return ret;
	}

	/**
	 * loads new code if the user confirms the respective dialog and sets
	 * modified to false. if there is remaining modified code, the user will be
	 * questioned whether or not to save it.
	 * 
	 * @return the path to the selected file or a empty string if no file was selected
	 */
	public String loadCode() {
		if (isModified()) {
			int saveBeforeClose = JOptionPane.showConfirmDialog(this,
					Messages.getString("am0c0", "JEditor.8"), Messages.getString("am0c0", "JEditor.9"), //$NON-NLS-1$ //$NON-NLS-2$
					JOptionPane.YES_NO_CANCEL_OPTION);

			switch (saveBeforeClose) {
			case JOptionPane.YES_OPTION:
				saveCode();
				setModified(false);
				break;
			case JOptionPane.CANCEL_OPTION:
				return ""; //$NON-NLS-1$
			default:
				break;
			}
		}

		boolean result = fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION;

		if (result) {
			File file = fileChooser.getSelectedFile();

			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(file));

				setText(""); //$NON-NLS-1$
				boolean firstLine = true;

				for (String line; (line = reader.readLine()) != null;) {
					if (firstLine) {
						setText(line);
						firstLine = false;
					} else
						setText(getText() + "\n" + line); //$NON-NLS-1$
				}

				reader.close();

				updateModel(null);
				setModified(false);

				return fileChooser.getSelectedFile().getAbsolutePath();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), Messages.getString("am0c0", "JEditor.13"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				return ""; //$NON-NLS-1$
			}

		}
		
		return ""; //$NON-NLS-1$
	}

	/**
	 * returns the {@link CodeObject} referenced by lineIndex if there is an
	 * underlying code model
	 * 
	 * @param lineIndex
	 *            the lineIndex to check
	 * @param startLine
	 *            returns the first line of the code object as an
	 *            {@link ExInteger}. pass null to ignore this.
	 * @param objectIndex
	 *            returns the list index of the code object as an
	 *            {@link ExInteger}. pass null to ignore this.
	 * @return the referenced {@link CodeObject} or null if there is no such (or
	 *         no code model)
	 */
	public CodeObject getCodeFromLine(int lineIndex, ExInteger startLine, ExInteger objectIndex) {
		if (model == null || lineIndex < 0)
			return null;

		int currentLine = 0;
		for (CodeObject codeObj : model) {
			if (lineIndex < currentLine + codeObj.getLinesCount()) {
				if (startLine != null)
					// pass currentLine to startLine here...
					// I hate Java
					startLine.set(currentLine);
				if (objectIndex != null)
					objectIndex.set(model.indexOf(codeObj));
				return codeObj;
			}
			currentLine += codeObj.getLinesCount();
		}

		return null;
	}

	@Override
	/**
	 * sets the font for this component (and its painter)
	 * refreshed line number width
	 * @param font the new {@link Font}
	 */
	public void setFont(Font font) {
		super.setFont(font);
		
		// save attributes and MouseListeners
		ExInteger lineStart = new ExInteger(0);
		ExInteger lineEnd = new ExInteger(0);
		enhancedPainter.getLineMarker(lineStart, lineEnd);
		
		MouseListener[] mouseListeners = (MouseListener[])enhancedPainter.getListeners(MouseListener.class);
		
		boolean markerVisible = enhancedPainter.isLineMarkerVisible();
		boolean lineHighlightEnabled = enhancedPainter.isLineHighlightEnabled();
		boolean enabled = isEnabled();
		
		int currentLineIndex = enhancedPainter.getCurrentLineIndex();
		Token currentLineToken = enhancedPainter.getCurrentLineTokens();
		
		// reset the painter with new font
		enhancedPainter = new EnhancedPainter(this, TextAreaDefaults.getDefaults());
		setPainter(enhancedPainter);
		getPainter().setFont(font);
		
		// restore MouseListeners and attributes
		setMouseListener();
		enhancedPainter.setLineMarker(lineStart.getValue(), lineEnd.getValue());
		enhancedPainter.showLineMarker(markerVisible);
		enhancedPainter.setCurrentLineIndex(currentLineIndex);
		enhancedPainter.setCurrentLineTokens(currentLineToken);
		enhancedPainter.setLineHighlightEnabled(lineHighlightEnabled);
		
		for (MouseListener ml : mouseListeners)
			enhancedPainter.addMouseListener(ml);
		
		setEnabled(enabled);
		
		updateScrollBars();
		enhancedPainter.refreshLineNumbersWidth();
	}

	@Override
	/**
	 * returns the current {@link Font} of this component (and its painter)
	 * @return the current {@link Font}
	 */
	public Font getFont() {
		return getPainter().getFont();
	}
	
	@Override
	public void updateScrollBars()
	{
		if(vertical != null && visibleLines != 0)
		{
			vertical.setValues(firstLine,visibleLines,0,getLineCount());
			vertical.setUnitIncrement(2);
			vertical.setBlockIncrement(visibleLines);
		}

		int width = getPainter().getWidth();
		if(horizontal != null && width != 0)
		{
			horizontal.setValues(-horizontalOffset,width,0,width * 5);
			horizontal.setUnitIncrement(enhancedPainter.getFontMetrics()
				.charWidth('w'));
			horizontal.setBlockIncrement(width / 2);
		}
	}

	@Override
	public boolean scrollTo(int line, int offset) {
		// visibleLines == 0 before the component is realized
		// we can't do any proper scrolling then, so we have
		// this hack...
		if (visibleLines == 0) {
			setFirstLine(Math.max(0, line - electricScroll));
			return true;
		}

		int newFirstLine = firstLine;
		int newHorizontalOffset = getHorizontalOffset();

		if (line < firstLine + electricScroll) {
			newFirstLine = Math.max(0, line - electricScroll);
		} else if (line + electricScroll >= firstLine + visibleLines) {
			newFirstLine = (line - visibleLines) + electricScroll + 1;
			if (newFirstLine + visibleLines >= getLineCount())
				newFirstLine = getLineCount() - visibleLines;
			if (newFirstLine < 0)
				newFirstLine = 0;
		}

		int x = _offsetToX(line, offset) - enhancedPainter.getLineNumbersWidth();
		int width = enhancedPainter.getFontMetrics().charWidth('w');

		if (x < 0) {
			newHorizontalOffset = Math.min(0, horizontalOffset - x + width + 5);
		} else if (x + width >= enhancedPainter.getWidth()) {
			newHorizontalOffset = horizontalOffset + (enhancedPainter.getWidth() - x) - width - 5;
		}
		
		enhancedPainter.refreshLineNumbersWidth();

		return setOrigin(newFirstLine, newHorizontalOffset);
	}

	@Override
	/**
	 * TODO: there is a bug in this method which generates wring results when TokenMarkers are applied
	 */
	public int _offsetToX(int line, int offset) {
		TokenMarker tokenMarker = getTokenMarker();

		FontMetrics fm = enhancedPainter.getFontMetrics();

		getLineText(line, lineSegment);

		int segmentOffset = lineSegment.offset;
		int lineNumbersWidth = enhancedPainter.getLineNumbersWidth();
		int x = getHorizontalOffset() + lineNumbersWidth;

		if (tokenMarker == null) {
			lineSegment.count = offset;
			return x + Utilities.getTabbedTextWidth(lineSegment, fm, x, enhancedPainter, 0);
		}

		else {
			Token tokens;
			if (enhancedPainter.getCurrentLineIndex() == line && enhancedPainter.getCurrentLineTokens() != null)
				tokens = enhancedPainter.getCurrentLineTokens();
			else {
				enhancedPainter.setCurrentLineIndex(line);
				enhancedPainter.setCurrentLineTokens(tokenMarker.markTokens(lineSegment, line));
				tokens = enhancedPainter.getCurrentLineTokens();
			}

			Font defaultFont = enhancedPainter.getFont();
			SyntaxStyle[] styles = enhancedPainter.getStyles();

			for (;;) {
				byte id = tokens.id;
				if (id == Token.END) {
					return x;
				}

				if (id == Token.NULL)
					fm = enhancedPainter.getFontMetrics();
				else
					fm = styles[id].getFontMetrics(defaultFont);

				int length = tokens.length;

				if (offset + segmentOffset < lineSegment.offset + length) {
					lineSegment.count = offset - (lineSegment.offset - segmentOffset);
					return x + Utilities.getTabbedTextWidth(lineSegment, fm, x, enhancedPainter, 0);
				} else {
					lineSegment.count = length;
					x += Utilities.getTabbedTextWidth(lineSegment, fm, x, enhancedPainter, 0);
					lineSegment.offset += length;
				}
				tokens = tokens.next;
			}
		}
	}

	@Override
	public int xToOffset(int line, int x) {
		TokenMarker tokenMarker = getTokenMarker();

		FontMetrics fm = enhancedPainter.getFontMetrics();

		getLineText(line, lineSegment);

		char[] segmentArray = lineSegment.array;
		int segmentOffset = lineSegment.offset;
		int segmentCount = lineSegment.count;

		int width = getHorizontalOffset() + enhancedPainter.getLineNumbersWidth();

		if (tokenMarker == null) {
			for (int i = 0; i < segmentCount; i++) {
				char c = segmentArray[i + segmentOffset];
				int charWidth;
				if (c == '\t')
					charWidth = (int) enhancedPainter.nextTabStop(width, i) - width;
				else
					charWidth = fm.charWidth(c);

				if (enhancedPainter.isBlockCaretEnabled()) {
					if (x - charWidth <= width) {
						return i;
					}
				} else {
					if (x - charWidth / 2 <= width) {
						return i;
					}
				}

				width += charWidth;
			}

			return segmentCount;
		} else {
			Token tokens;
			if (enhancedPainter.getCurrentLineIndex() == line && enhancedPainter.getCurrentLineTokens() != null)
				tokens = enhancedPainter.getCurrentLineTokens();
			else {
				enhancedPainter.setCurrentLineIndex(line);
				enhancedPainter.setCurrentLineTokens(tokenMarker.markTokens(lineSegment, line));
				tokens = enhancedPainter.getCurrentLineTokens();
			}

			int offset = 0;
			Font defaultFont = enhancedPainter.getFont();
			SyntaxStyle[] styles = enhancedPainter.getStyles();

			for (;;) {
				byte id = tokens.id;
				if (id == Token.END){
					return offset;
				}

				if (id == Token.NULL)
					fm = enhancedPainter.getFontMetrics();
				else
					fm = styles[id].getFontMetrics(defaultFont);

				int length = tokens.length;

				for (int i = 0; i < length; i++) {
					char c = segmentArray[segmentOffset + offset + i];
					int charWidth;
					if (c == '\t') {
						charWidth = (int) enhancedPainter.nextTabStop(width, offset + i) - width;
					}
					else
						charWidth = fm.charWidth(c);

					if (enhancedPainter.isBlockCaretEnabled()) {
						if (x - charWidth <= width) {
							return offset + i;
						}
					} else {
						if (x - charWidth / 2 <= width) {
							return offset + i;
						}
					}

					width += charWidth;
				}

				offset += length;
				tokens = tokens.next;
			}
		}
	}
}
