/* Created on 28.05.2005 */
package org.jalgo.module.avl.gui.components;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

import org.jalgo.module.avl.Controller;
import org.jalgo.module.avl.gui.DisplayModeChangeable;
import org.jalgo.module.avl.gui.DocuManager;
import org.jalgo.module.avl.gui.GUIConstants;
import org.jalgo.module.avl.gui.GUIController;
import org.jalgo.module.avl.gui.Settings;

/**
 * The class <code>DocuPane</code> provides a text component for displaying the
 * algorithm text of the currently running algorithm. The current step is
 * highlighted and scrolled to be visible at each time.
 * 
 * @author Alexander Claus
 */
public class DocuPane
extends JPanel
implements DisplayModeChangeable, GUIConstants {

	private GUIController gui;
	private DocuManager manager;
	private JTextPane textPane;
	private DefaultStyledDocument doc;

	private Map<String, String> steps;
	private int lastHighlightedParagraphOffset = 0;
	private int lastHighlightedParagraphLength = 0;

	/**
	 * Constructs a <code>DocuPane</code> object with the given references.
	 * 
	 * @param gui the <code>GUIController</code> instance of the AVL module
	 * @param controller the <code>Controller</code> instance of the AVL module
	 */
	public DocuPane(GUIController gui, Controller controller) {
		this.gui = gui;
		manager = new DocuManager(controller);

		textPane = new JTextPane();
		textPane.setMargin(new Insets(2, 4, 2, 4));
		textPane.setEditable(false);
		doc = new DefaultStyledDocument();
		textPane.setDocument(doc);

		setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(
			textPane,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollPane, BorderLayout.CENTER);

		//the status line updater
		textPane.addMouseListener(new MouseAdapter() {
			public void mouseExited(MouseEvent e) {
				DocuPane.this.gui.setStatusMessage(null);
			}
			public void mouseEntered(MouseEvent e) {
				DocuPane.this.gui.setStatusMessage("Zeigt den Algorithmustext an");
			}
		});
	}

	/**
	 * If the current step in algorithm has changed, the last step is set back to
	 * normal style, and the new step is highlighted and scrolled to be visible.<br>
	 * For correct scrolling, here is a tricky approach implemented:<br>
	 * Normally you would set the caret position first to the end of the text to be
	 * highlighted, then to the beginning. Unfortunately, only the last call to
	 * <code>setCaretPosition(int)</code> takes effect. Because of this, this
	 * method scrolls the text to the end, when performing a step "forward", and
	 * scrolls the text to the beginning, when undoing a step ("backward").
	 * This is not really comfortable, but it is a workaround for a stable release.
	 */
	public void update() {
		if (steps == null) return;
		doc.setCharacterAttributes(
			lastHighlightedParagraphOffset, lastHighlightedParagraphLength,
			Settings.NORMAL_STYLE[PC_MODE], true);
		lastHighlightedParagraphOffset = manager.getCurrentStepOffset();
		lastHighlightedParagraphLength = manager.getCurrentStepLength();
		if (lastHighlightedParagraphOffset >= 0)
			doc.setCharacterAttributes(
				lastHighlightedParagraphOffset, lastHighlightedParagraphLength,
				Settings.DOCU_HIGHLIGHTED_STYLE[Settings.getDisplayMode()], true);
		//here the mentioned trick:
		if (gui.isPerformStep()) textPane.setCaretPosition(Math.max(0,
				lastHighlightedParagraphOffset+lastHighlightedParagraphLength));
		else textPane.setCaretPosition(Math.max(0, lastHighlightedParagraphOffset));
		//FIXME: der folgende ansatz führt zu darstellungsfehlern,
		//es wird zwar richtig positioniert, allerdings wird der text gelegentlich
		//überlagert, bzw. sogar in der paintarea dargestellt,
		//vermutung: scrollRectToVisible() ist schuld...
		//daher hier getrickst
//		Rectangle visibleRect = textPane.getVisibleRect();
//		try {visibleRect.setLocation(
//				textPane.modelToView(textPane.getCaretPosition()).getLocation());}
//		catch (BadLocationException ex) {ex.printStackTrace();}
//		textPane.scrollRectToVisible(visibleRect);
	}

	/**
	 * Loads the algorithm text of the current algorithm by getting from
	 * <code>DocuManager</code>.
	 */
	public void algorithmStarted() {
		steps = manager.getCurrentAlgorithmDescription();
		try {
			doc.remove(0, doc.getLength());
			for (String key : steps.keySet()) {
				doc.insertString(doc.getLength(), steps.get(key),
					Settings.NORMAL_STYLE[PC_MODE]);
//				doc.insertString(doc.getLength(), lineSep+lineSep,
//					Settings.NORMAL_STYLE[PC_MODE]);
			}
		}
		catch (BadLocationException ex) {
			gui.showErrorMessage("Fehler bei der Anzeige der Dokumentation:\r\n"+
				ex.getMessage());
		}
	}

	/**
	 * Removes all text from the component.
	 */
	public void reset() {
		steps = null;
		try {doc.remove(0, doc.getLength());}
		catch (BadLocationException ex) {
			System.err.println("Sollte nicht auftreten...");
		}
	}

	/**
	 * This method is called, when the display mode has changed between pc mode
	 * and beamer mode. As a result of this the size of the font is changed.
	 */
	public void displayModeChanged() {
		if (lastHighlightedParagraphOffset+lastHighlightedParagraphLength == 0)
			return;
		doc.setCharacterAttributes(
			0, doc.getLength(),
			Settings.NORMAL_STYLE[PC_MODE], true);
		doc.setCharacterAttributes(
			lastHighlightedParagraphOffset, lastHighlightedParagraphLength,
			Settings.DOCU_HIGHLIGHTED_STYLE[Settings.getDisplayMode()], true);
	}
}