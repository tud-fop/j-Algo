/**
 *
 */

/* Created on 15.06.2007 */
package org.jalgo.module.pulsemem.gui.components;

import java.awt.Color;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.*;

import org.jalgo.module.pulsemem.gui.GUIConstants;

/**
 * CurrentLineHighlighter.java
 * <p>
 * Colors the current line.
 * <p>
 *
 * @version $Revision: 1.2 $
 * @author Martin Brylski - TU Dresden, SWTP 2007
 */
public class CurrentLineHighlighter implements CaretListener {

    private Highlighter.HighlightPainter painter;

    private Object highlight;

    public CurrentLineHighlighter() {
        this(null);
    }

    public CurrentLineHighlighter(Color highlightColor) {
        Color c = highlightColor != null ? highlightColor
                : GUIConstants.LineHighlightColor;
        painter = new DefaultHighlighter.DefaultHighlightPainter(c);
    }

    public void caretUpdate(CaretEvent evt) {
        JTextComponent comp = (JTextComponent) evt.getSource();
        if (comp != null && highlight != null) {
            comp.getHighlighter().removeHighlight(highlight);
            highlight = null;
        }

        int pos = comp.getCaretPosition();
        Element elem = Utilities.getParagraphElement(comp, pos);
        int start = elem.getStartOffset();
        int end = elem.getEndOffset();
        try {
            highlight = comp.getHighlighter().addHighlight(start, end, painter);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }
}
