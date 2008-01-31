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

/* Created on 05.05.2007 */
package org.jalgo.module.pulsemem.gui.components;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;

import org.jalgo.main.util.Messages;
import org.jalgo.module.pulsemem.core.PulsMemLine;
import org.jalgo.module.pulsemem.core.Variable;
import org.jalgo.module.pulsemem.gui.GUIConstants;

/**
 * NOLPanel.java
 * <p>
 * <code>NOLPanel</code> is a component extending the standard JEditorPane
 * with linenumbers on the left of it.
 * <p>
 *
 * @version $Revision: 1.26 $
 * @author Martin Brylski - TU Dresden, SWTP 2007
 */
public class NOLPanel extends JPanel {

    private final int textEditPosX = 47;

    private JTextArea textarea;

    private JScrollPane scrollPane;

    private int fontHeight;

    private int fontDesc;

    private int starting_y;

    private InlineBreakpoint lineOfBreakpoint;

    private BufferedImage breaker;

    // private Image scaledBreaker;

    private int start;

    private int end;

    private int startline;

    private int endline;

    private int fontWidth;

    private boolean drawBreakpoints = false;

    private List<Variable> vars;

    public NOLPanel(InlineBreakpoint lineOfBreakpointArg) {
        super();

        // get Instance of the Breakpoint handler
        lineOfBreakpoint = lineOfBreakpointArg;

        // new EditorPane
        textarea = new JTextArea() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                NOLPanel.this.repaint();
            }
        };

        // new ScrollPane
        scrollPane = new JScrollPane(textarea);

        textarea.setDisabledTextColor(GUIConstants.DISABLED_TEXTAREA_FONT);

        // textarea.setSelectedTextColor(GUIConstants.CARET_SELECT_COLOR);
        textarea.setSelectionColor(GUIConstants.CARET_SELECT_COLOR);

        textarea.addCaretListener(new CurrentLineHighlighter());

        // get the breakpoint symbol
        try {
            if ((breaker = ImageIO.read(Messages.getResourceURL("pulsemem",
                    "Breakpoint"))) != null) {
                repaint();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        fontHeight = g.getFontMetrics(textarea.getFont()).getHeight();
        fontWidth = g.getFontMetrics(textarea.getFont()).getWidths()[48];
        fontDesc = g.getFontMetrics(textarea.getFont()).getDescent();
        starting_y = -1;

        start = textarea
                .viewToModel(scrollPane.getViewport().getViewPosition());
        end = textarea.viewToModel(new Point(scrollPane.getViewport()
                .getViewPosition().x
                + textarea.getWidth(), scrollPane.getViewport()
                .getViewPosition().y
                + textarea.getHeight()));
        Document doc = textarea.getDocument();
        startline = doc.getDefaultRootElement().getElementIndex(start) + 1;
        endline = doc.getDefaultRootElement().getElementIndex(end) + 1;

        int noc = 0;
        float temp = endline / 10;
        while (temp >= 1) {
            temp = temp / 10;
            noc++;
        }

        setMinimumSize(new Dimension((noc + 3) * fontWidth, 40));
        setPreferredSize(new Dimension((noc + 3) * fontWidth, 40));

        try {
            starting_y = textarea.modelToView(start).y
                    - scrollPane.getViewport().getViewPosition().y + fontHeight
                    - fontDesc;
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }

        // scales the image...
        // if (fontHeight == 12) {
        // scaledBreaker = breaker.getScaledInstance((breaker.getWidth() * 1),
        // (breaker.getHeight() * 1), Image.SCALE_SMOOTH);
        // } else if (fontHeight == 16) {
        // scaledBreaker = breaker.getScaledInstance((breaker.getWidth() * 2),
        // (breaker.getHeight() * 2), Image.SCALE_SMOOTH);
        // }

        for (int line = startline, y = starting_y; line <= endline; y += fontHeight, line++) {
            int tempnoc = noc;
            int mult = 0;
            while (line < Math.pow(10, tempnoc)) {
                tempnoc--;
                mult++;
            }
            g.drawString(Integer.toString(line), mult * fontWidth, y);

            if (drawBreakpoints) {
                if (lineOfBreakpoint.containsLine(line)) {
                    // g.fillOval(20, y + (fontHeight / 2), 7, 7);
                    if (breaker != null)
                        g.drawImage(breaker, (noc + 1) * fontWidth, y
                                - fontDesc - (breaker.getHeight() / 2) - 1,
                                this);
                }
            }
        }
    }

    /**
     * @param pml
     */
    public void highlightVarOccurences(PulsMemLine pml) {
        int sRow = 0, sCol = 0, sLen = 0;
        int pos;
        Set<Integer[]> app;

        Highlighter h = textarea.getHighlighter();
        h.removeAllHighlights();
        
        if (!(pml == null)) {
            vars = pml.getVisibleStack();
            for (Variable variable : vars) {
                app = variable.getVi().getAppearance();
                for (Integer[] integers : app) {
                    sRow = integers[0]-1;
                    sCol = integers[1];
                    sLen = variable.getName().length();

                    try {
                    	pos = textarea.getLineStartOffset(sRow) + sCol;
                        h.addHighlight(pos, pos + sLen, DefaultHighlighter.DefaultPainter);
                    } catch (BadLocationException ble) {
                    }
                }
            }
        }
    }

    /**
     * @return the scrollPane
     */
    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    /**
     * Sets the Text from the Textarea.
     *
     * @param str
     */
    public void setText(String str) {
        textarea.setText(str);
    }

    /**
     * @return the text of the Textarea.
     */
    public String getText() {
        return textarea.getText();
    }

    /**
     * Enables/Disables the textarea.
     *
     * @param b
     *            true if it should be enabled.
     */
    public void setTextareaEnabled(boolean b) {
        textarea.setEnabled(b);
    }

    /**
     * @return the height of the font.
     */
    public int getFontHeight() {
        return fontHeight;
    }

    /**
     * Determine wich line was clicked.
     *
     * @param yPoint
     * @return the line wich was clicked.
     */
    public int determineLineFromPoint(Point point) {
        repaint();

        // Rangecheck
        if (point.y < getY())
            return 0;

        if (point.y > getY() + getHeight())
            return 0;

        if (point.x > textEditPosX)
            return 0;

        final int adjustValue = 1;

        float adj = -getY() - (fontHeight / 2.0f) - adjustValue;

        JScrollBar vertical = (JScrollBar) scrollPane.getComponent(1);
        int result = (int) ((point.y + adj + vertical.getValue()) / fontHeight + 1.5f);
        return result;
    }

    /**
     * adds an ActionListener to the Textarea
     *
     * @param ActLst
     */
    public void addKeyListener(KeyListener KeyLst) {
        textarea.addKeyListener(KeyLst);
    }

    /**
     * Sets the font.
     *
     * @param font
     *            the font.
     */
    public void setTextFont(Font font) {
        textarea.setFont(font);
    }

    /**
     * @return the textarea
     */
    public JTextArea getTextarea() {
        return textarea;
    }

    /**
     * Sets the Caret to a specific line.
     *
     * @param line
     *            must be greater or equal to 1
     */
    public void setCaretToLine(int line) {
        try {
            textarea.setCaretPosition(textarea.getLineStartOffset(line - 1));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * sets whether the Breakpoints should be drawn or not
     *
     * @param boolean
     */

    public void drawBreakpoints(boolean arg) {
        this.drawBreakpoints = arg;
    }
}
