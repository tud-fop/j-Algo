/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * Created on Apr 17, 2004
 */
 
package org.jalgo.main.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.jalgo.main.gfx.MarkStyle;

/**
 * This class can be used to display text with one or more highlighted text segments.
 * This segments are active, which is indicated with a mark. The style of the
 * highlighted text is set using the MarkStyle class.
 * 
 * @author Cornelius Hald
 * @author Hauke Menges
 */
public class TextCanvas extends Composite {

	private StyledText styledText;

	private int mode;
	public static final int MODE_STYLE_ACTIVE = 1;
	public static final int MODE_SHOW_ACTIVE = 2;

	private MarkStyle markStyle;
	private int position = 0;

	private String[] textSegments;
	private boolean[] activeSegments;
	private StyleRange[] styleRanges;
	private StyleRange styleRangeAll;
	private String text;

	private Color white;
	private Color black;
	private Color red;

	/**
	 * Constructs a new TextCanvas.
	 * A TextCanvas is a {@link Composite} containing a StyledText widget.
	 * 
	 * @param parent 	the parent object
	 * @param style 	a SWT-Style constant
	 * 
	 * @see 			Composite
	 */
	public TextCanvas(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
		styledText =
			new StyledText(
				this,
				SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | style);
		styledText.setEditable(false);

		white = new Color(parent.getDisplay(), 255, 255, 255);
		black = new Color(parent.getDisplay(), 0, 0, 0);
		red = new Color(parent.getDisplay(), 255, 0, 0);

		mode = MODE_STYLE_ACTIVE;
		
		styledText.setBackground(white);

		parent.layout();
	}

	/**
	 * Sets the style of the active text segment which is marked. If no markStyle
	 * is set, the TextCanvas will use a standard style.
	 * @param markStyle the style object.
	 */
	public void setMarkStyle(MarkStyle markStyle) {
		this.markStyle = markStyle;
	}

	/**
	 * Marks the next text segment as active and demarks the current segment.
	 * After reaching the last text segment it will continue at the first element.
	 */
	public void markNext() {
		position++;
		demarkAll();
		mark(position);
	}

	/**
	 * Marks the first text segment as active.
	 */
	public void markFirst() {
		position = 0;
		demarkAll();
		mark(0);
	}

	/**
	 * Marks the textsegment with the given index.
	 * 
	 * @param index
	 */
	public void mark(int index) {
		if (index >= textSegments.length) {
			index = textSegments.length - 1;
		}

		activeSegments[index] = true;
		switch (mode) {
			case MODE_STYLE_ACTIVE :
				int offset = 0;
				int length = 0;

				for (int i = 0; i < index; i++) {

					offset += textSegments[i].length();
				}

				length = textSegments[index].length();

				styleRanges[index].start = offset;
				styleRanges[index].length = length;

				if (markStyle != null) {
					Color background = markStyle.getBgColor();
					Color foreground = markStyle.getFgColor();
					int fontstyle = markStyle.getFontstyle();

					styleRanges[index].background = background;
					styleRanges[index].foreground = foreground;
					styleRanges[index].fontStyle = fontstyle;

				} else {
					styleRanges[index].background = red;
					styleRanges[index].foreground = black;
				}
				styledText.setStyleRange(styleRanges[index]);
				break;
			case MODE_SHOW_ACTIVE :
				text = ""; //$NON-NLS-1$
				for (int i = 0; i < textSegments.length; i++) {
					if (activeSegments[i] == true)
						text += textSegments[i];
				}
				styledText.setText(text);
				break;
		}
	}

	/**
	 * Demarks the textsegment whith the given index.
	 * 
	 * @param index
	 */
	public void demark(int index) {
		if (index >= textSegments.length) {
			index = textSegments.length - 1;
		}

		activeSegments[index] = false;
		switch (mode) {
			case MODE_STYLE_ACTIVE :
				int offset = 0;
				int length = 0;

				for (int i = 0; i < index; i++) {

					offset += textSegments[i].length();
				}

				length = textSegments[index].length();

				styleRanges[index].start = offset;
				styleRanges[index].length = length;
				styleRanges[index].background = styledText.getBackground();
				styleRanges[index].foreground = styledText.getForeground();
				styleRanges[index].fontStyle = SWT.NORMAL;

				styledText.setStyleRange(styleRanges[index]);
				break;
			case MODE_SHOW_ACTIVE :
				text = ""; //$NON-NLS-1$
				for (int i = 0; i < textSegments.length; i++) {
					if (activeSegments[i] == true)
						text += textSegments[i];
				}
				styledText.setText(text);

				break;
		}
	}

	/**
	 * gets the position, which is used by markNext() and markFirst.
	 */
	public int getPosition() {
		return position;
	}
	
	/**
	 * sets the position, which is used by markNext() and markFirst.
	 */
	public void setPosition(int pos) {
		position = pos;
	}

	/**
	 * marks a textsegment - this segment is used as base for markNext().
	 * 
	 * @param index The Index of the text segment.
	 */
	public void markPosition(int index) {
		if (index >= textSegments.length) {
			index = textSegments.length - 1;
		}

		position = index;
		demarkAll();
		mark(position);
	}

	/**
	 * Removes all visible marks from the TextCanvas.
	 */
	public void demarkAll() {
		for (int i = 0; i < textSegments.length; i++) {
			activeSegments[i] = false;
		}

		switch (mode) {
			case MODE_STYLE_ACTIVE :
				int offset = 0;
				int length = text.length();

				styleRangeAll.start = offset;
				styleRangeAll.length = length;
				styleRangeAll.background = styledText.getBackground();
				styleRangeAll.foreground = styledText.getForeground();
				styleRangeAll.fontStyle = SWT.NORMAL;

				styledText.setStyleRange(styleRangeAll);
				break;

			case MODE_SHOW_ACTIVE :
				text = ""; //$NON-NLS-1$
				styledText.setText(text);
				break;
		}
	}

	/**
	 * Set the text segments which sould be displayed. The segments should be
	 * entered in the right order and can be marked using the methods provided
	 * in this class.
	 * 
	 * @param textSegments
	 */
	public void setTextSegments(String[] textSegments) {
		this.textSegments = textSegments;

		String text = ""; //$NON-NLS-1$

		for (int i = 0; i < textSegments.length; i++) {
			text = text + textSegments[i];
		}
		this.text = text;
		styledText.setText(text);

		activeSegments = new boolean[textSegments.length];
		styleRanges = new StyleRange[textSegments.length];

		styleRangeAll = new StyleRange();

		for (int i = 0; i < styleRanges.length; i++) {
			styleRanges[i] = new StyleRange();
		}
	}

	/**
	 * Gets the textSegments.
	 * @return an array of Strings containing the text segments.
	 */
	public String[] getText() {
		return textSegments;
	}

	/**
	 * Sets the behavior of the TextCanvas. There are several modes:<p>
	 * <li>1: Changes the forground Color, background Color and the text
	 * style (normal, bold or italic) of the active text segment.
	 * The style is set in MarkStyle.
	 * <li>2: Shows only the activ text segment.
	 * 
	 * @param mode the mode changes the behavior
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}

	/**
	 * Gets the behavior of the TextCanvas.
	 * 
	 * @return the mode of the TextCanvas
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * Gets the background color.
	 */
	public Color getBackground() {
		return styledText.getBackground();
	}
	
	/**
	 * Sets the background color.
	 */
	public void setBackground(Color color) {
		styledText.setBackground(color);
	}

	/**
	 * Adds another segment at the end of the segment list.
	 * 
	 * @param segment	new segment string
	 */
	public void addSegment(String segment) {
		String[] textSegments2 = new String[textSegments.length];
		System.arraycopy(
			textSegments,
			0,
			textSegments2,
			0,
			textSegments.length);
		textSegments = textSegments2;
		textSegments[textSegments.length - 1] = segment;

		boolean[] activeSegments2 = new boolean[activeSegments.length];
		System.arraycopy(
			activeSegments,
			0,
			activeSegments2,
			0,
			activeSegments.length);
		activeSegments = activeSegments2;
		activeSegments[activeSegments.length - 1] = false;

		StyleRange[] styleRanges2 = new StyleRange[styleRanges.length];
		System.arraycopy(styleRanges, 0, styleRanges2, 0, styleRanges.length);
		styleRanges = styleRanges2;
		styleRanges[styleRanges.length - 1] = new StyleRange();

		String text = ""; //$NON-NLS-1$
		for (int i = 0; i < textSegments.length; i++) {
			text = text + textSegments[i];
		}
		this.text = text;
		styledText.setText(text);

	}

}