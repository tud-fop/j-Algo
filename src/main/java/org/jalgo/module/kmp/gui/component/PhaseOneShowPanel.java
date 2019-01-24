package org.jalgo.module.kmp.gui.component;

import org.jalgo.main.util.Messages;

import org.jalgo.module.kmp.algorithm.*;
import org.jalgo.module.kmp.algorithm.phaseone.*;
import org.jalgo.module.kmp.gui.GUIConstants;
import org.jalgo.module.kmp.gui.event.PhaseOneScreenListener;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;


/**
 * This class paints the table for the generation of the pattern in phase 1.
 *
 * @author         Sebastian Patschorke
 */
public class PhaseOneShowPanel extends JPanel {
    private static final long serialVersionUID = 8296370714586057895L;

    /** just a listener */
    @SuppressWarnings("unused")
    private PhaseOneScreenListener listener;

    /** holds the (importent) pattern */
    private Pattern pattern;

    /** holds the actual step of the history, which should be shown */
    private Step actStep;

    /** the basic font */
    private Font showPanelFont;

    /** letter arithmetics of the basic font (showPanelFont) */
    private int letterWidth;

    /** letter arithmetics of the basic font (showPanelFont) */
    private int letterHeight;

    /** how many letters is the pattern long */
    private int patternLength;

    /** how many pixel is the table head width */
    private int headLength;

    /** arithmetics of one cell */
    private int cellWidth;

    /** arithmetics of one cell */
    private int cellHeight;

    /** cellspacing in one cell, distance between left border and one Character in the cell */
    private int cellspacingHorizontal;

    /** cellspacing in one cell, distance between top border and the Character */
    private int cellspacingVertical;

    /** maximal letter length of all table headings */
    private int maxHeadLetterLength;

    /** what stands in the head of the pattern characters */
    private String headPattern;

    /** what stands in the head of the pattern tableentries */
    private String headTable;

    /** what stands in the head of the pattern indicies */
    private String headIndex;

    /** what stands in the head of the cycles */
    private String headCycles;

    /** should the cycles be shown */
    private boolean showCycles = true;

    /** this is (normally) GUIConstants.P1_Y_OFFSET,
     *  only for showCycles==true a bit more */
    private int yOffset;

    /** table arithmetics */
    private int tabWidth;

    /** table arithmetics */
    private int tabHeight;

    /**
     * The constructor of the <code>PhaseOneShowPanel</code>.
     * It initialises some variables.
     *
     * @param l <code>PhaseOneScreenListener</code>
     */
    public PhaseOneShowPanel(PhaseOneScreenListener l) {
        listener = l;

        /* initial setting of the constant font */
        showPanelFont = GUIConstants.SHOW_PANEL_FONT;

        /* get the table headings */
        headPattern = Messages.getString("kmp", "ShowPanel.Head_pattern");
        headTable = Messages.getString("kmp", "ShowPanel.Head_table");
        headIndex = Messages.getString("kmp", "ShowPanel.Head_index");
        headCycles = Messages.getString("kmp", "ShowPanel.Head_cycles");

        /* finds the maximum length of the headings */
        maxHeadLetterLength = headPattern.length();

        if (headTable.length() > maxHeadLetterLength) {
            maxHeadLetterLength = headTable.length();
        }

        if (headIndex.length() > maxHeadLetterLength) {
            maxHeadLetterLength = headIndex.length();
        }

        if (headCycles.length() > maxHeadLetterLength) {
            maxHeadLetterLength = headCycles.length();
        }
    }

    /**
     * Sets the pattern for the showPanel. So all importent informations
     * are available.
     * 
     * @param p <code>Pattern</code>
     */
    public void setPattern(Pattern p) {
        this.pattern = p;
    }

    /**
     * Sets the actual step which should be visualated.
     * 
     * @param step algorithm step
     */
    public void update(Step step) {
        this.actStep = step;
    }

    /**
     * Switch for Showing the cycles in the visualisation
     * 
     * @param sc show cycles
     */
    public void showCycles(boolean sc) {
        this.showCycles = sc;
    }

    /**
     * increments the font size in comparison to the constans font.<br><br>
     * incFontSize(3); incFontSize(3);<br>has the same result as<br>incFontSize(3);
     * 
     * @param s factor
     */
    public void incFontSize(double s) {
        showPanelFont = new Font(GUIConstants.SHOW_PANEL_FONT.getName(),
                GUIConstants.SHOW_PANEL_FONT.getStyle(),
                (int) (GUIConstants.SHOW_PANEL_FONT.getSize() * s));
    }

    /**
     * This Method is called automaticly if the GUI was scaled.
     * Or it's called after setting a new step
     * 
     * @param g <code>Graphics</code>
     */
    public void paintComponent(Graphics g) {
        setBackground(Color.WHITE);
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

        initPainting();
        paintBasicTab(g2);
        paintStep(g2);
    }

    /**
     * It is (only) called from this.paintComponent<br>
     * Some often used values and the basic layout will be calculated.
     */
    private void initPainting() {
        FontMetrics fm = getFontMetrics(showPanelFont);
        letterHeight = fm.getAscent();
        letterWidth = fm.charWidth('A'); /* it's monospaced */

        cellspacingVertical = letterHeight / 2;

        if (pattern != null) {
            patternLength = pattern.getPattern().length();
        } else {
            patternLength = 6; /* fictiv number to paint something */
        }

        headLength = (2 * cellspacingVertical) +
            (maxHeadLetterLength * letterWidth);

        cellWidth = (getWidth() - headLength - (2 * GUIConstants.P1_X_OFFSET)) / (patternLength +
            1);

        int minCW = (int) (letterWidth * GUIConstants.P1_MIN_CELL_WIDTH); /* min cellWidth */
        int maxCW = (int) (letterWidth * GUIConstants.P1_MAX_CELL_WIDTH); /* max cellWidth */

        if (cellWidth > maxCW) {
            cellWidth = maxCW;
        } else if (cellWidth < minCW) {
            cellWidth = minCW;
        }

        /* 'correction' that the patPos-pointer could be seen after the table (at the end) */
        int rest = getWidth() - headLength - (2 * GUIConstants.P1_X_OFFSET) -
            ((patternLength + 1) * cellWidth);

        if ((rest >= 0) && (rest < (cellWidth / 2))) {
            cellWidth -= (((cellWidth / 2) - rest) / (patternLength + 1));
        }

        if (cellWidth < minCW) {
            cellWidth = minCW;
        }

        cellspacingHorizontal = (cellWidth - letterWidth) / 2;
        cellHeight = (2 * cellspacingVertical) + letterHeight;
        yOffset = GUIConstants.P1_Y_OFFSET;

        if (showCycles) {
            yOffset += cellHeight;
        }

        tabWidth = ((patternLength + 1) * cellWidth) + headLength;
        tabHeight = 3 * cellHeight;
    }

    /**
     * It is (only) called from this.paintComponent and
     * paints the static table.
     * 
     * @param g2 <code>Graphics2D</code>
     */
    private void paintBasicTab(Graphics2D g2) {
        /* (only) gives the head a color */
        g2.setColor(GUIConstants.HEAD_COLOR);
        g2.fill(new Rectangle(GUIConstants.P1_X_OFFSET, yOffset, headLength,
                tabHeight));

        g2.setColor(Color.BLACK);

        /* left collumline */
        g2.draw(new Line2D.Double(GUIConstants.P1_X_OFFSET, yOffset,
                GUIConstants.P1_X_OFFSET, yOffset + tabHeight));

        /* all other collumlines */
        int x = GUIConstants.P1_X_OFFSET + headLength;

        for (int i = 0; i <= (patternLength + 1); i++) {
            g2.draw(new Line2D.Double(x, yOffset, x, yOffset + tabHeight));
            x += cellWidth;
        }

        /* row lines */
        int y = yOffset;

        for (int i = 0; i < 4; i++) {
            g2.draw(new Line2D.Double(GUIConstants.P1_X_OFFSET, y,
                    GUIConstants.P1_X_OFFSET + tabWidth, y));
            y += cellHeight;
        }

        /* fills the head with life
              here: cellspacingHorizontal =^ cellspacingVertical */
        setFont(showPanelFont);
        y = yOffset + letterHeight + cellspacingVertical;
        g2.drawString(headPattern,
            GUIConstants.P1_X_OFFSET + cellspacingVertical, y);
        g2.drawString(headTable,
            GUIConstants.P1_X_OFFSET + cellspacingVertical, y + cellHeight);
        g2.drawString(headIndex,
            GUIConstants.P1_X_OFFSET + cellspacingVertical, y +
            (2 * cellHeight));

        if (showCycles) {
            g2.setColor(Color.YELLOW);
            g2.fill(new Rectangle(GUIConstants.P1_X_OFFSET,
                    yOffset - cellHeight, headLength, cellHeight));
            g2.setColor(Color.BLACK);
            g2.draw(new Rectangle(GUIConstants.P1_X_OFFSET,
                    yOffset - cellHeight, headLength, cellHeight));
            g2.draw(new Line2D.Double(GUIConstants.P1_X_OFFSET,
                    yOffset - cellHeight, GUIConstants.P1_X_OFFSET + tabWidth,
                    yOffset - cellHeight));
            g2.draw(new Line2D.Double(GUIConstants.P1_X_OFFSET + tabWidth,
                    yOffset, GUIConstants.P1_X_OFFSET + tabWidth,
                    yOffset - cellHeight));
            g2.drawString(headCycles,
                GUIConstants.P1_X_OFFSET + cellspacingVertical, y - cellHeight);
        }
    }

    /**
     * It is (only) called from this.paintComponent.
     * paints the part of the visualisation which is specific for each step
     * 
     * @param g2 <code>Graphics2D</code>
     */
    private void paintStep(Graphics2D g2) {
        if (actStep != null) {
            int vglInd = ((P1Step) actStep).getVglInd();
            int patPos = actStep.getPatPos();

            paintPointers(g2, patPos, vglInd);

            if (showCycles && (patPos < patternLength)) {
                paintCycles(g2, patPos, patPos);
            }

            if (actStep instanceof P1InitStep) {
                printPatternChars(g2, 0, patternLength - 1, Color.BLACK);
                printTableEntrys(g2, 0, 0, GUIConstants.HIGHLIGHT_COLOR);
                printPatternIndicies(g2, -1, patternLength - 1, Color.BLACK);
            } else if (actStep instanceof P1BeginForStep) {
                printPatternChars(g2, 0, patternLength - 1, Color.BLACK);
                printTableEntrys(g2, 0, patPos - 1, Color.BLACK);
                printPatternIndicies(g2, -1, patternLength - 1, Color.BLACK);
            } else if (actStep instanceof P1IfTrueStep) {
                printPatternChars(g2, 0, vglInd - 1, Color.BLACK);
                printPatternChars(g2, vglInd, vglInd, GUIConstants.TRUE_COLOR);
                printPatternChars(g2, vglInd + 1, patPos - 1, Color.BLACK);
                printPatternChars(g2, patPos, patPos, GUIConstants.TRUE_COLOR);
                printPatternChars(g2, patPos + 1, patternLength - 1, Color.BLACK);
                printTableEntrys(g2, 0, patPos - 1, Color.BLACK);
                printTableEntrys(g2, patPos, patPos,
                    GUIConstants.HIGHLIGHT_COLOR);
                printPatternIndicies(g2, -1, patternLength - 1, Color.BLACK);
                paintBox(g2, vglInd, 1, 1, 1, GUIConstants.BOX_COLOR);
                paintArrow(g2, vglInd, patPos, GUIConstants.ARROW_COLOR);
            } else if (actStep instanceof P1IfFalseStep) {
                printPatternChars(g2, 0, vglInd - 1, Color.BLACK);
                printPatternChars(g2, vglInd, vglInd, GUIConstants.FALSE_COLOR);
                printPatternChars(g2, vglInd + 1, patPos - 1, Color.BLACK);
                printPatternChars(g2, patPos, patPos, GUIConstants.FALSE_COLOR);
                printPatternChars(g2, patPos + 1, patternLength - 1, Color.BLACK);
                printTableEntrys(g2, 0, patPos - 1, Color.BLACK);
                printTableEntrys(g2, patPos, patPos,
                    GUIConstants.HIGHLIGHT_COLOR);
                printPatternIndicies(g2, -1, patternLength - 1, Color.BLACK);
                paintBox(g2, vglInd, 2, 1, 1, GUIConstants.BOX_COLOR);
                paintArrow(g2, vglInd, patPos, GUIConstants.ARROW_COLOR);
            } else if (actStep instanceof P1WhileStep) {
                if (((P1WhileStep) actStep).isLastWhileStep()) {
                    if (vglInd == -1) {
                        paintBox(g2, -1, 0, 1, 3, GUIConstants.FALSE_COLOR);
                        printPatternChars(g2, 0, patternLength - 1, Color.BLACK);
                    } else {
                        printPatternChars(g2, 0, vglInd - 1, Color.BLACK);
                        printPatternChars(g2, vglInd, vglInd,
                            GUIConstants.TRUE_COLOR);
                        printPatternChars(g2, vglInd + 1, patPos - 1,
                            Color.BLACK);
                        printPatternChars(g2, patPos, patPos,
                            GUIConstants.TRUE_COLOR);
                        printPatternChars(g2, patPos + 1, patternLength - 1,
                            Color.BLACK);
                    }

                    printTableEntrys(g2, 0, patPos, Color.BLACK);
                    printPatternIndicies(g2, -1, patternLength - 1, Color.BLACK);
                } else {
                    printPatternChars(g2, 0, patternLength - 1, Color.BLACK);

                    int oldVgl = ((P1WhileStep) actStep).getOldVglind();
                    printTableEntrys(g2, 0, oldVgl - 1, Color.BLACK);
                    printTableEntrys(g2, oldVgl, oldVgl,
                        GUIConstants.HIGHLIGHT_COLOR);
                    printTableEntrys(g2, oldVgl + 1, patPos, Color.BLACK);
                    printPatternIndicies(g2, -1, vglInd - 1, Color.BLACK);
                    printPatternIndicies(g2, vglInd, vglInd,
                        GUIConstants.HIGHLIGHT_COLOR);
                    printPatternIndicies(g2, vglInd + 1, patternLength - 1,
                        Color.BLACK);
                }
            } else if (actStep instanceof P1EndForStep) {
                printPatternChars(g2, 0, patternLength - 1, Color.BLACK);
                printTableEntrys(g2, 0, patPos, Color.BLACK);
                printPatternIndicies(g2, -1, patternLength - 1, Color.BLACK);
            }
        } else {
            printPatternChars(g2, 0, patternLength - 1, Color.BLACK);
            printPatternIndicies(g2, -1, patternLength - 1, Color.BLACK);
        }
    }

    /**
     * paints the pattern characters
     *
     * @param g2 <code>Graphics2D</code>
     * @param start start position
     * @param end end position
     * @param col <code>Color</code>
     */
    private void printPatternChars(Graphics2D g2, int start, int end, Color col) {
        if (pattern != null) {
            if (start < -1) {
                start = -1;
            }

            if (end >= patternLength) {
                end = patternLength - 1;
            }

            Color oldcolor = g2.getColor();
            g2.setColor(col);

            int x = GUIConstants.P1_X_OFFSET + headLength +
                cellspacingHorizontal + ((start + 1) * cellWidth);
            int y = yOffset + cellspacingVertical + letterHeight;

            String pat = new String(pattern.getPattern());

            for (int i = start; i <= end; i++) {
                g2.drawString(pat.substring(i, i + 1), x, y);
                x += cellWidth;
            }

            g2.setColor(oldcolor);
        }
    }

    /**
     * paints the table entries
     *
     * @param g2 <code>Graphics2D</code>
     * @param start start position
     * @param end end position
     * @param col <code>Color</code>
     */
    private void printTableEntrys(Graphics2D g2, int start, int end, Color col) {
        if (pattern != null) {
            Color oldcolor = g2.getColor();
            g2.setColor(col);

            if (start < 0) {
                start = 0;
            }

            if (end >= patternLength) {
                end = patternLength - 1;
            }

            int x = GUIConstants.P1_X_OFFSET + headLength +
                cellspacingHorizontal + ((start + 1) * cellWidth);
            int y = yOffset + cellspacingVertical + letterHeight + cellHeight;
            int nr;

            for (int i = start; i <= end; i++) {
                nr = pattern.getTblEntryAt(i);

                if ((nr >= 0) && (nr < 10)) { /* have it to print 1 or 2 chars (f.e. "-1") */
                    g2.drawString(new Integer(nr).toString(), x, y);
                } else {
                    g2.drawString(new Integer(nr).toString(),
                        x - (letterWidth / 2), y);
                }

                x += cellWidth;
            }

            g2.setColor(oldcolor);
        }
    }

    /**
     * paints the table indicies
     *
     * @param g2 <code>Graphics2D</code>
     * @param start start position
     * @param end end position
     * @param col <code>Color</code>
     */
    private void printPatternIndicies(Graphics2D g2, int start, int end,
        Color col) {
        if (start < -1) {
            start = -1;
        }

        if (end >= patternLength) {
            end = patternLength - 1;
        }

        Color oldcolor = g2.getColor();
        g2.setColor(col);

        int x = GUIConstants.P1_X_OFFSET + headLength + cellspacingHorizontal +
            ((start + 1) * cellWidth);
        int y = yOffset + cellspacingVertical + letterHeight +
            (2 * cellHeight);

        if (start == -1) { /* paints the -1 separatly, because it is 2 Characters long*/
            g2.drawString(new Integer(-1).toString(),
                (x - cellspacingHorizontal + (cellWidth / 2)) - letterWidth, y);
            x += cellWidth;
            start = 0;
        }

        for (int i = start; i <= end; i++) {
            g2.drawString(new Integer(i).toString(), x, y);
            x += cellWidth;
        }

        g2.setColor(oldcolor);
    }

    /**
     * paints the cycles
     *
     * @param g2 <code>Graphics2D</code>
     * @param pos actual position
     * @param patPos actual pattern position
     */
    private void paintCycles(Graphics2D g2, int pos, int patPos) {
        if (pattern != null) {
            int[] cyc = pattern.getCycleDataAt(patPos);
            Color oldColor = g2.getColor();
            g2.setColor(Color.MAGENTA);

            Stroke oldStroke = g2.getStroke();
            int dist = cellHeight / (GUIConstants.MAX_SHOW_CYCLES + 1);
            int thickness = dist / 2;
            thickness = (95 * thickness) / 100;
            g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_MITER));

            for (int i = 0; i < cyc.length; i++) {
                if (cyc[i] <= pos) {
                    if (i < GUIConstants.MAX_SHOW_CYCLES) {
                        int level;

                        if (GUIConstants.MAX_SHOW_CYCLES > cyc.length) {
                            level = cyc.length - i;
                        } else {
                            level = GUIConstants.MAX_SHOW_CYCLES - i;
                        }

                        g2.draw(new Line2D.Double(GUIConstants.P1_X_OFFSET +
                                headLength + ((cyc[i] + 1) * cellWidth),
                                yOffset - (level * dist),
                                (GUIConstants.P1_X_OFFSET + headLength +
                                ((pos + 2) * cellWidth)) - 5, /* the 5 is only for correction, that a cycle is not over any table line */
                                yOffset - (level * dist)));
                    } else {
                        int x = GUIConstants.P1_X_OFFSET + headLength +
                            ((cyc[i] + 1) * cellWidth);
                        g2.draw(new Line2D.Double(x, yOffset - (dist / 2), x,
                                yOffset - dist - (dist / 2)));
                    }
                }
            }

            g2.setStroke(oldStroke);
            g2.setColor(oldColor);
        }
    }

    /**
     * paints the two pointers (patpos & vglind) below the table
     *
     * @param g2 <code>Graphics2D</code>
     * @param patPos pattern position
     * @param vglInd actual index to compare (german: Vergleichsindex)
     */
    private void paintPointers(Graphics2D g2, int patPos, int vglInd) {
        /* xPos, yPos are the top coords of the arrow */
        Font pointerFont = new Font(showPanelFont.getName(),
                showPanelFont.getStyle(), (int) (showPanelFont.getSize() * 0.68));
        FontMetrics fm = getFontMetrics(pointerFont);
        int lh = fm.getAscent();
        int lw = fm.charWidth('A'); /* monospaced! */

        int pH = (int) (lh * 2); /* pointer height */
        int pHH = (int) (lh * 1); /* 'half' pointer height */
        int pHW = (int) (lw * 1.6); /* half pointer width */
        int pQW = (int) (lw * 0.8); /* 'quarter' pointer width */

        int sP = (int) (pHW * 0.5); /* space between 'overlapping' pointers */
        int yO = 5; /* y Offset below table */

        if (g2 != null) {
            Color oldcolor = g2.getColor();
            Font oldfont = g2.getFont();
            g2.setFont(pointerFont);

            /* vglInd */
            int xPos = GUIConstants.P1_X_OFFSET + headLength + (cellWidth / 2) +
                ((vglInd + 1) * cellWidth);
            int yPos = yOffset + tabHeight + yO;

            if ((vglInd == patPos) && (patPos > 0)) {
                xPos -= sP;
            }

            int[] xpointsV = {
                    xPos, xPos + pHW, xPos + pQW, xPos + pQW, xPos - pQW,
                    xPos - pQW, xPos - pHW
                };
            int[] ypointsV = {
                    yPos, yPos + pHH, yPos + pHH, yPos + pH, yPos + pH,
                    yPos + pHH, yPos + pHH
                };
            Polygon arrowVgl = new Polygon(xpointsV, ypointsV, 7);
            g2.setColor(GUIConstants.VGLIND_POINTER_COLOR);
            g2.fill(arrowVgl);
            g2.setColor(Color.BLACK);
            g2.draw(arrowVgl);

            Color c = GUIConstants.VGLIND_POINTER_COLOR;
            Color charColor = new Color(255 - c.getRed(), 255 - c.getGreen(),
                    255 - c.getBlue());
            g2.setColor(charColor);
            g2.drawString("V", xPos - (lw / 2), (yPos + pH) - (int) (lh * 0.3));

            /* patPos */
            if (patPos > 0) {
                if (vglInd == patPos) {
                    xPos += sP;
                }

                xPos += ((-vglInd + patPos) * cellWidth);

                int[] xpointsP = {
                        xPos, xPos + pHW, xPos + pQW, xPos + pQW, xPos - pQW,
                        xPos - pQW, xPos - pHW
                    };
                int[] ypointsP = {
                        yPos, yPos + pHH, yPos + pHH, yPos + pH, yPos + pH,
                        yPos + pHH, yPos + pHH
                    };
                Polygon arrowPat = new Polygon(xpointsP, ypointsP, 7);

                if (patPos < patternLength) {
                    g2.setColor(GUIConstants.PATPOS_POINTER_COLOR);
                } else {
                    g2.setColor(GUIConstants.FALSE_COLOR);
                }

                c = g2.getColor();
                charColor = new Color(255 - c.getRed(), 255 - c.getGreen(),
                        255 - c.getBlue());
                g2.fill(arrowPat);
                g2.setColor(Color.BLACK);
                g2.draw(arrowPat);
                g2.setColor(charColor);
                g2.drawString("P", xPos - (lw / 2),
                    (yPos + pH) - (int) (lh * 0.3));
            }

            g2.setFont(oldfont);
            g2.setColor(oldcolor);
        }
    }

    /**
     * paints an arrow over the table to show what value is copied to which 'cell'
     *
     * @param g2 <code>Graphics2D</code>
     * @param start start position
     * @param end end position
     * @param color <code>Color</code>
     */
    private void paintArrow(Graphics2D g2, int start, int end, Color color) {
        Color oldColor = g2.getColor();
        Stroke oldStroke = g2.getStroke();
        g2.setColor(color);
        g2.setStroke(new BasicStroke((int) (letterHeight * 0.18),
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));

        int xStart = GUIConstants.P1_X_OFFSET + headLength +
            ((start + 1) * cellWidth) + (cellWidth / 2);
        int xEnd = GUIConstants.P1_X_OFFSET + headLength +
            ((end + 1) * cellWidth) + (cellWidth / 2);
        int y = yOffset;

        if (showCycles) {
            y -= cellHeight;
        }

        int plusminus = 0; /* needed if start==end, that there can be seen an arrow */

        if (start == end) {
            plusminus = cellWidth / 4;
        }

        /* start */
        g2.draw(new Line2D.Double(xStart - plusminus, y - 5,
                xStart - plusminus, y - 20));
        /* end */
        g2.draw(new Line2D.Double(xEnd + plusminus, y - 5, xEnd + plusminus,
                y - 20));
        /* line */
        g2.draw(new Line2D.Double(xStart - plusminus, y - 20, xEnd + plusminus,
                y - 20));
        /* left top */
        g2.draw(new Line2D.Double(xEnd - 5 + plusminus, y - 8,
                xEnd + plusminus, y - 5));
        /* right top */
        g2.draw(new Line2D.Double(xEnd + plusminus, y - 5,
                xEnd + 5 + plusminus, y - 8));

        g2.setStroke(oldStroke);
        g2.setColor(oldColor);
    }

    /**
     * paints a rectangle, a box, arround cells
     *
     * @param g2 <code>Graphics2D</code>
     * @param startCol start collum
     * @param startRow start row
     * @param cols #collums
     * @param rows #rows
     * @param col <code>Color</code>
     */
    private void paintBox(Graphics2D g2, int startCol, int startRow, int cols,
        int rows, Color col) {
        Color oldColor = g2.getColor();
        g2.setColor(col);

        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke((int) (letterHeight * 0.24),
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
        g2.draw(new Rectangle(GUIConstants.P1_X_OFFSET + headLength +
                ((startCol + 1) * cellWidth),
                yOffset + (startRow * cellHeight), cols * cellWidth,
                rows * cellHeight));
        g2.setStroke(oldStroke);
        g2.setColor(oldColor);
    }
}
