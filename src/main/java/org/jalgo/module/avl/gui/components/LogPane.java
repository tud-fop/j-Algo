/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

/* Created on 31.05.2005 */
package org.jalgo.module.avl.gui.components;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Queue;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.module.avl.Controller;
import org.jalgo.module.avl.gui.DisplayModeChangeable;
import org.jalgo.module.avl.gui.GUIConstants;
import org.jalgo.module.avl.gui.Settings;

/**
 * The class <code>LogPane</code> represents a scrollable logbook for logging
 * actions occured during algorithms. The last entry is each highlighted for
 * better recognition of the current action.
 * 
 * @author Alexander Claus
 */
public class LogPane
extends JComponent
implements DisplayModeChangeable, GUIConstants {

	private static final long serialVersionUID = 8981036220693388871L;
	private Controller controller;
	private JTextPane textPane;
	private DefaultStyledDocument doc;

	private String lineSeparator;
	private Queue<String> lastLogDescriptions;
	private int lastHighlightedParagraphOffset = 0;
	private int lastHighlightedParagraphLength = 0;

	/**
	 * Constructs a <code>LogPane</code> object with the given references.
	 * 
	 * @param controller the <code>Controller</code> instance of the AVL
	 *            module
	 */
	public LogPane(Controller controller) {
		this.controller = controller;
		lineSeparator = System.getProperty("line.separator"); //$NON-NLS-1$

		textPane = new JTextPane();
		textPane.setMargin(new Insets(2, 4, 2, 4));
		textPane.setEditable(false);
		doc = new DefaultStyledDocument();
		textPane.setDocument(doc);

		setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(textPane,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);

		// the status line updater
		textPane.addMouseListener(new MouseAdapter() {

			public void mouseExited(MouseEvent e) {
				JAlgoGUIConnector.getInstance().setStatusMessage(null);
			}

			public void mouseEntered(MouseEvent e) {
				JAlgoGUIConnector.getInstance().setStatusMessage(
					Messages.getString("avl", "LogPane.Status_message")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		});
	}

	/**
	 * Takes the last occured actions as queue from the <code>Controller</code>.
	 * This actions are prepared as strings for easy displaying. Highlights the
	 * last entry, sets the pre-last entry back to normal style.
	 */
	public void update() {
		doc.setCharacterAttributes(lastHighlightedParagraphOffset,
			lastHighlightedParagraphLength,
			Settings.NORMAL_STYLE[Settings.getDisplayMode()], true);

		lastLogDescriptions = controller.getLogDescriptions();
		for (String logDesc : lastLogDescriptions) {
			if (logDesc == null || logDesc.equals("")) continue; //$NON-NLS-1$
			lastHighlightedParagraphOffset = doc.getLength();
			try {
				doc.insertString(doc.getLength(), logDesc + lineSeparator,
					Settings.NORMAL_STYLE[Settings.getDisplayMode()]);
			}
			catch (BadLocationException ex) {
				System.err.println(Messages.getString(
					"avl", "LogPane.Update_error")); //$NON-NLS-1$ //$NON-NLS-2$
			}
			lastHighlightedParagraphLength = logDesc.length()
				+ lineSeparator.length();
		}
		doc.setCharacterAttributes(lastHighlightedParagraphOffset,
			lastHighlightedParagraphLength,
			Settings.HIGHLIGHTED_STYLE[Settings.getDisplayMode()], true);
		textPane.setCaretPosition(lastHighlightedParagraphOffset
			+ lastHighlightedParagraphLength);
	}

	/**
	 * Clears the logbook.
	 */
	public void reset() {
		try {
			doc.remove(0, doc.getLength());
		}
		catch (BadLocationException ex) {
			System.err.println(Messages.getString(
				"avl", "Unreachable_error")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		lastHighlightedParagraphLength = 0;
		lastHighlightedParagraphOffset = 0;
	}

	/**
	 * This method is called, when the display mode has changed between pc mode
	 * and beamer mode. As a result of this the size of the font is changed.
	 */
	public void displayModeChanged() {
		if (lastHighlightedParagraphOffset+lastHighlightedParagraphLength == 0)
			return;
		doc.setCharacterAttributes(0, lastHighlightedParagraphOffset,
			Settings.NORMAL_STYLE[Settings.getDisplayMode()], true);
		doc.setCharacterAttributes(lastHighlightedParagraphOffset,
			lastHighlightedParagraphLength,
			Settings.HIGHLIGHTED_STYLE[Settings.getDisplayMode()], true);
		// scroll to end of text
		Rectangle visibleRect = textPane.getVisibleRect();
		// force recalculating of height
		textPane.getParent().doLayout();
		visibleRect.y = textPane.getHeight() - visibleRect.height;
		textPane.scrollRectToVisible(visibleRect);
	}
}