package org.jalgo.module.c0h0.views;

import java.awt.Rectangle;

import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;

import jsyntaxpane.DefaultSyntaxKit;

/**
 * the editor for the C0 code
 * 
 * @author Patrick
 * @author Peter
 * 
 */
public class TextEditor extends JEditorPane {
	private static final long serialVersionUID = -8737844952931068720L;

	public TextEditor() {
		DefaultSyntaxKit.initKit();
	}

	/**
	 * Vertically *centers* the marked function;
	 * 
	 * http://online.chinaitpower.com/source/jdk122/javax/swing/JEditorPane.java
	 * .html helped a lot.
	 * 
	 * @param reference
	 */
	@Override
	public void scrollToReference(String reference) {
		HTMLDocument d = (HTMLDocument) getDocument();
		HTMLDocument.Iterator it = d.getIterator(HTML.Tag.A);
		for (; it.isValid(); it.next()) {
			String name = (String) it.getAttributes().getAttribute(
					HTML.Attribute.NAME);
			if (name != null && reference.equals(name)) {
				try {
					Rectangle r = modelToView(it.getStartOffset());
					if (r != null) {
						// the view is visible, scroll it to the
						// center of the current visible area.
						Rectangle vis = getVisibleRect();
						r.y = Math.max(0, r.y - (vis.height / 2));
						r.height = vis.height;
						scrollRectToVisible(r);
						return;
					}
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
