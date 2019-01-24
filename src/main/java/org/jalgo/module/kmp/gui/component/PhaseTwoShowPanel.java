package org.jalgo.module.kmp.gui.component;

import org.jalgo.main.util.Messages;

import org.jalgo.module.kmp.algorithm.*;
import org.jalgo.module.kmp.algorithm.phasetwo.*;
import org.jalgo.module.kmp.gui.GUIConstants;
import org.jalgo.module.kmp.gui.event.PhaseTwoScreenListener;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;


/**
 * This class paints the text & the pattern table for the visualisation
 * of the kmp algorithmin in action.
 *
 * @author         Sebastian Patschorke
 *
 */
public class PhaseTwoShowPanel extends JPanel {
    private static final long serialVersionUID = 888797759979595933L;

    /** Searchtext-Arithmetics */
    private Rectangle searchTextBounds;

    /** GraphicObject for the Searchtext(animation) */
    private Graphics2D g2SearchT;

    /** buffers the searchtext for animation */
    private Image searchTImage;

    /** buffers the whole panel without searchtext and
     *  viewwindow for animation */
    private Image panelImage;

    /** was the animation shown */
    private boolean animated = false;

    /** is the animation in process */
    private boolean animationStarted = false;

    /** The Thread for the animation */
    private Thread aniThread;

    /** just a listener */
    @SuppressWarnings("unused")
    private PhaseTwoScreenListener listener;

    /** holds the (importent) pattern */
    private Pattern pattern;

    /** holds the Text in which the pattern is searched */
    private String searchText;

    /** holds the actual step of the history, which should be shown */
    private Step actStep;

    /** the basic font */
    private Font showPanelFont;

    /** distance between each table and heading. */
    private int distance;

    /** letter arithmetics of the basic font (showPanelFont) */
    private int letterWidth;

    /** letter arithmetics of the basic font (showPanelFont) */
    private int letterHeight;

    /** how many letters is the pattern long */
    private int patternLength;

    /** how many pixel is the table head width */
    private int headLength;

    /** how many cells are there in the searchtext row */
    private int cellNr;

    /** after how many searchtext cells starts the pattern */
    private int patternOffset;

    /** arithmetics of one cell */
    private int cellWidth;

    /** arithmetics of one cell */
    private int cellHeight;

    /** cellspacing in one cell, distance between left border
     *  and one Character in the cell */
    private int cellspacingHorizontal;

    /** cellspacing in one cell, distance between top border
     *  and the Character */
    private int cellspacingVertical;

    /** maximal letter length of all table headings */
    private int maxHeadLetterLength;

    /** what stands in the head of the text indicies */
    private String headTextPos;

    /** what stands in the head of the text */
    private String headText;

    /** what stands in the head of the pattern characters */
    private String headPattern;

    /** what stands in the head of the pattern tableentries */
    private String headTable;

    /** what stands in the head of the pattern indicies */
    private String headIndex;

    /**
     * The constructor of the <code>PhaseTwoShowPanel</code>.
     * It initialises some variables.
     * 
     * @param l <code>PhaseTwoScreenListener</code>
     */
    public PhaseTwoShowPanel(PhaseTwoScreenListener l) {
        listener = l;

        this.setDoubleBuffered(true);

        /* initial setting of the constant font */
        showPanelFont = GUIConstants.SHOW_PANEL_FONT;

        /* get the table headings */
        headTextPos = Messages.getString("kmp", "ShowPanel.Head_textposition");
        headText = Messages.getString("kmp", "ShowPanel.Head_text");
        headPattern = Messages.getString("kmp", "ShowPanel.Head_pattern");
        headTable = Messages.getString("kmp", "ShowPanel.Head_table");
        headIndex = Messages.getString("kmp", "ShowPanel.Head_index");

        /* finds the maximum length of the headings */
        maxHeadLetterLength = headTextPos.length();

        if (headText.length() > maxHeadLetterLength) {
            maxHeadLetterLength = headText.length();
        }

        if (headPattern.length() > maxHeadLetterLength) {
            maxHeadLetterLength = headPattern.length();
        }

        if (headTable.length() > maxHeadLetterLength) {
            maxHeadLetterLength = headTable.length();
        }

        if (headIndex.length() > maxHeadLetterLength) {
            maxHeadLetterLength = headIndex.length();
        }
    }

    /**
     * Sets the pattern for the showPanel. So all importent informations are available.
     * 
     * @param pattern <code>Pattern</code>
     */
    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    /**
     * Sets the text, in which the pattern will be found or not.
     * 
     * @param searchText Text to search in
     */
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    /**
     * Sets the actual step which should be visualated.
     *
     * @param step algorithm step
     * @param isforward update in forward direction
     */
    public void update(Step step, boolean isforward) {
        animated = false;

        if (!isforward) {
            animated = true;
        }

        this.actStep = step;

        if (aniThread != null) {
            aniThread.interrupt();
            animationStarted = false;
            paintComponent(this.getGraphics());
        }
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
     * Or it's called after setting a new step.
     *
     * @param g <code>Graphics</code>
     */
    public void paintComponent(Graphics g) {
        initPainting();

        if (!animationStarted) { /* during animation the screen is buffered */
            setBackground(Color.WHITE);
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_SPEED);
            g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);

            paintBasicTabs(g2);

            boolean ani = animated; /* temporary for the viewwindow (paintstep() changes the value */
            paintStep(g2);

            if (searchTImage != null) {
                int x = GUIConstants.P2_X_OFFSET + headLength + distance;
                int y = GUIConstants.P2_Y_OFFSET;
                int w = cellNr * cellWidth;
                int h = 2 * cellHeight;
                g.drawImage(searchTImage, x, y, x + w + 1, y + h + 1, 0, 0,
                    w + 1, h + 1, this); /* '+1' because technical reason */
            }

            if (actStep != null) {
                int patPos = actStep.getPatPos();

                if (!(actStep instanceof P2InnerWhileStep) || ani) {
                    paintViewWindow(g2, patPos, 0);
                }

                if (actStep instanceof P2InnerWhileStep) {
                    if (((P2InnerWhileStep) actStep).getJumpDistance() == 0) {
                        paintViewWindow(g2, patPos, 0);
                    }
                }
            }
        }
    }

    /**
     * It is (only) called from this.paintComponent<br>
     * Some often used values and the basic layout will be calculated. 
     */
    private void initPainting() {
        FontMetrics fm = getFontMetrics(showPanelFont);
        letterHeight = fm.getAscent();
        letterWidth = fm.charWidth('A'); /* it is monospaced */

        distance = (int) (letterHeight * 0.6);

        cellspacingVertical = (int) (letterHeight * 0.4);

        if (pattern != null) {
            patternLength = pattern.getPattern().length();
        } else { /* if there is still no pattern */
            patternLength = 5; /* fictiv number to paint something */
        }

        headLength = (2 * cellspacingVertical) +
            (maxHeadLetterLength * letterWidth);

        /* prefered number of cells */
        cellNr = patternLength + (2 * GUIConstants.P2_PREFERED_CELL_OFFSET);

        /* how many pixel are there for the cells */
        int availableSpace = getWidth() - (2 * GUIConstants.P2_X_OFFSET) -
            headLength - distance;
        /* now all cells get their width */
        cellWidth = availableSpace / cellNr;

        if (cellWidth < GUIConstants.P2_MIN_CELL_WIDTH) {
            cellWidth = GUIConstants.P2_MIN_CELL_WIDTH;
        } else if (cellWidth > GUIConstants.P2_MAX_CELL_WIDTH) {
            cellWidth = GUIConstants.P2_MAX_CELL_WIDTH;
        }

        /* if there is not enough space for the prefered number of cells
           reduces the number of cells, but there are at least 2 cells more
           than the pattern has */
        while (((cellNr - 2) > patternLength) &&
                ((cellNr * cellWidth) > availableSpace)) {
            cellNr--;
        }

        /* if there is enough space increment the number of cells */
        int rest = availableSpace - (cellNr * cellWidth);

        while (rest > GUIConstants.P2_MAX_CELL_WIDTH) {
            cellNr++;
            rest -= GUIConstants.P2_MAX_CELL_WIDTH;
        }

        /* after how many cells the pattern starts */
        patternOffset = ((cellNr - patternLength) / 2);

        cellspacingHorizontal = (cellWidth - letterWidth) / 2;
        cellHeight = (2 * cellspacingVertical) + letterHeight;
        searchTextBounds = new Rectangle(GUIConstants.P2_X_OFFSET + headLength +
                distance, GUIConstants.P2_Y_OFFSET, (cellNr * cellWidth) + 1,
                (2 * cellHeight) + 1);
    }

    /**
     * It is (only) called from this.paintComponent and
     * paints the static table witout the searchtext body
     * 
     * @param g2 <code>Graphics2D</code>
     */
    private void paintBasicTabs(Graphics2D g2) {
        /* (only) gives the head a color */
        g2.setColor(GUIConstants.HEAD_COLOR);
        g2.fill(new Rectangle(GUIConstants.P2_X_OFFSET,
                GUIConstants.P2_Y_OFFSET, headLength, 2 * cellHeight));
        g2.fill(new Rectangle(GUIConstants.P2_X_OFFSET,
                GUIConstants.P2_Y_OFFSET + distance + (2 * cellHeight),
                headLength, 3 * cellHeight));

        g2.setColor(Color.BLACK);

        /* paints the head of the text table */
        for (int i = 0; i < 3; i++)
            g2.draw(new Line2D.Double(GUIConstants.P2_X_OFFSET,
                    GUIConstants.P2_Y_OFFSET + (i * cellHeight),
                    GUIConstants.P2_X_OFFSET + headLength,
                    GUIConstants.P2_Y_OFFSET + (i * cellHeight)));

        g2.draw(new Line2D.Double(GUIConstants.P2_X_OFFSET,
                GUIConstants.P2_Y_OFFSET, GUIConstants.P2_X_OFFSET,
                GUIConstants.P2_Y_OFFSET + (2 * cellHeight)));
        g2.draw(new Line2D.Double(GUIConstants.P2_X_OFFSET + headLength,
                GUIConstants.P2_Y_OFFSET,
                GUIConstants.P2_X_OFFSET + headLength,
                GUIConstants.P2_Y_OFFSET + (2 * cellHeight)));

        /* paints the head of the pattern table */
        for (int i = 0; i < 4; i++) {
            g2.draw(new Line2D.Double(GUIConstants.P2_X_OFFSET,
                    GUIConstants.P2_Y_OFFSET + distance +
                    ((i + 2) * cellHeight),
                    GUIConstants.P2_X_OFFSET + headLength,
                    GUIConstants.P2_Y_OFFSET + distance +
                    ((i + 2) * cellHeight)));
        }

        g2.draw(new Line2D.Double(GUIConstants.P2_X_OFFSET,
                GUIConstants.P2_Y_OFFSET + (2 * cellHeight) + distance,
                GUIConstants.P2_X_OFFSET,
                GUIConstants.P2_Y_OFFSET + (5 * cellHeight) + distance));
        g2.draw(new Line2D.Double(GUIConstants.P2_X_OFFSET + headLength,
                GUIConstants.P2_Y_OFFSET + (2 * cellHeight) + distance,
                GUIConstants.P2_X_OFFSET + headLength,
                GUIConstants.P2_Y_OFFSET + (5 * cellHeight) + distance));

        /* fills the head with life */
        g2.setFont(showPanelFont);

        int y = GUIConstants.P2_Y_OFFSET + letterHeight + cellspacingVertical;
        g2.drawString(headTextPos,
            GUIConstants.P2_X_OFFSET + cellspacingVertical, y);
        g2.drawString(headText, GUIConstants.P2_X_OFFSET + cellspacingVertical,
            y += cellHeight);
        g2.drawString(headPattern,
            GUIConstants.P2_X_OFFSET + cellspacingVertical,
            y += (cellHeight + distance));
        g2.drawString(headTable,
            GUIConstants.P2_X_OFFSET + cellspacingVertical, y += cellHeight);
        g2.drawString(headIndex,
            GUIConstants.P2_X_OFFSET + cellspacingVertical, y += cellHeight);

        /* paints the body of the pattern table
           startX for the pattern table body */
        int startX = GUIConstants.P2_X_OFFSET + headLength + distance +
            (patternOffset * cellWidth);

        /* row lines */
        for (int i = 0; i < 4; i++) {
            g2.draw(new Line2D.Double(startX - cellWidth,
                    GUIConstants.P2_Y_OFFSET + distance +
                    ((i + 2) * cellHeight),
                    startX + (patternLength * cellWidth),
                    GUIConstants.P2_Y_OFFSET + distance +
                    ((i + 2) * cellHeight)));
        }

        /* collum lines */
        for (int i = -1; i <= patternLength; i++) {
            g2.draw(new Line2D.Double(startX + (i * cellWidth),
                    GUIConstants.P2_Y_OFFSET + distance + (2 * cellHeight),
                    startX + (i * cellWidth),
                    GUIConstants.P2_Y_OFFSET + distance + (5 * cellHeight)));
        }

        if (pattern != null) {
            printPatternChars(g2, 0, patternLength - 1, Color.BLACK);
        }
    }

    /**
     * It is (only) called from this.paintComponent and
     * paints the part of the visualisation which is specific for each step.
     * 
     * @param g2 <code>Graphics2D</code>
     */
    private void paintStep(Graphics2D g2) {
        if (actStep != null) {
            int textPos = ((P2Step) actStep).getTextPos();
            int patPos = actStep.getPatPos();

            if (actStep instanceof P2InitStep) {
                printPatternEntries(g2, 0, patternLength - 1, Color.BLACK);
                printPatternIndicies(g2, -1, patternLength - 1, Color.GRAY);
                printSearchtext(textPos - patPos, false);
                paintSearchtextArrows(g2, textPos - patPos);
            } else if (actStep instanceof P2OuterWhileStep) {
                printPatternEntries(g2, 0, patternLength - 1, Color.BLACK);
                printPatternIndicies(g2, -1, patternLength - 1, Color.GRAY);
                printSearchtext(textPos - patPos, false);
                paintSearchtextArrows(g2, textPos - patPos);
            } else if (actStep instanceof P2InnerWhileStep) {
                int jD = ((P2InnerWhileStep) actStep).getJumpDistance();

                if (!animated && (jD > 0)) {
                    animated = true;

                    printSearchtext(textPos - patPos - jD, true);
                    paintSearchtextArrows(g2, textPos - patPos - jD);

                    doAnimation(g2, patPos, jD);
                } else {
                    if (jD > 0) {
                        printPatternEntries(g2, 0, (patPos + jD) - 1,
                            Color.BLACK);
                        printPatternEntries(g2, patPos + jD, patPos + jD,
                            GUIConstants.HIGHLIGHT_COLOR);
                        printPatternEntries(g2, patPos + jD + 1,
                            patternLength - 1, Color.BLACK);
                        printPatternIndicies(g2, -1, patPos - 1, Color.GRAY);
                        printPatternIndicies(g2, patPos, patPos,
                            GUIConstants.HIGHLIGHT_COLOR);
                        printPatternIndicies(g2, patPos + 1, patternLength - 1,
                            Color.GRAY);
                    } else {
                        printPatternEntries(g2, 0, patternLength - 1,
                            Color.BLACK);
                        printPatternIndicies(g2, -1, patternLength - 1,
                            Color.GRAY);
                    }

                    printSearchtext(textPos - patPos, false);
                    paintSearchtextArrows(g2, textPos - patPos);
                }
            } else if (actStep instanceof P2InnerSetStep) {
                printPatternEntries(g2, 0, patternLength - 1, Color.BLACK);
                printPatternIndicies(g2, -1, patternLength - 1, Color.GRAY);
                printSearchtext(textPos - patPos, false);
                paintSearchtextArrows(g2, textPos - patPos);
            } else if (actStep instanceof P2EndStep) {
                printPatternEntries(g2, 0, patternLength - 1, Color.BLACK);
                printPatternIndicies(g2, -1, patternLength - 1, Color.GRAY);
                printSearchtext(textPos - patPos, false);
                paintSearchtextArrows(g2, textPos - patPos);
            }
        } else { /* actStep is null */

            if (pattern != null) {
                printPatternEntries(g2, 0, patternLength - 1, Color.BLACK);
                printPatternIndicies(g2, -1, patternLength - 1, Color.GRAY);
            }

            printSearchtext(0, false);
            paintSearchtextArrows(g2, 0);
        }
    }

    /**
     * It is (only) called from paintStep,
     * paints the basic and static searchtext body and
     * prints the searchtext in the basic table at the position the step is.
     *
     * @param startTextPos is the index at the beginning of the pattern
     * @param animation print it for an animation?
     */
    private void printSearchtext(int startTextPos, boolean animation) {
        if (!animationStarted) {
            int jumpDistance = 0;
            int offset = (cellNr - patternLength) / 2;

            if (!animation) {
                searchTImage = createImage(searchTextBounds.width,
                        searchTextBounds.height);
            } else {
                jumpDistance = ((P2InnerWhileStep) actStep).getJumpDistance();
                searchTImage = createImage(searchTextBounds.width +
                        (jumpDistance * cellWidth), searchTextBounds.height);
            }

            g2SearchT = (Graphics2D) searchTImage.getGraphics();
            g2SearchT.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            g2SearchT.setBackground(Color.WHITE);
            g2SearchT.clearRect(0, 0, searchTImage.getWidth(this),
                searchTImage.getHeight(this));

            /* paints the body */
            for (int i = 0; i < 3; i++) {
                g2SearchT.draw(new Line2D.Double(0, i * cellHeight,
                        (cellNr + jumpDistance) * cellWidth, i * cellHeight));
            }

            for (int i = 0; i <= (cellNr + jumpDistance); i++) {
                g2SearchT.draw(new Line2D.Double(i * cellWidth, 0,
                        i * cellWidth, 2 * cellHeight));
            }

            /* paints the characters */
            if (searchText != null) {
                if (startTextPos < 0) {
                    startTextPos = 0;
                }

                int index;

                g2SearchT.setFont(showPanelFont);

                String tmpString;

                for (int i = 0; i < (cellNr + jumpDistance); i++) {
                    index = (startTextPos + i) - offset;

                    if ((index >= 0) && (index < searchText.length())) {
                        tmpString = (new Integer(index)).toString();

                        /* if number is to long f.e.: 2543764 "become" ..3764 */
                        if (tmpString.length() > (cellWidth / letterWidth)) {
                            tmpString = tmpString.substring(3);

                            while ((tmpString.length() + 2) > (cellWidth / letterWidth)) {
                                tmpString = tmpString.substring(1);
                            }

                            tmpString = ".." + tmpString;
                        }

                        g2SearchT.setColor(Color.GRAY);
                        g2SearchT.drawString(tmpString,
                            ((i * cellWidth) + (cellWidth / 2)) -
                            ((tmpString.length() * letterWidth) / 2),
                            cellspacingVertical + letterHeight);

                        g2SearchT.setColor(Color.BLACK);
                        g2SearchT.drawString(searchText.substring(index,
                                index + 1),
                            (i * cellWidth) + cellspacingHorizontal,
                            cellspacingVertical + letterHeight + cellHeight);
                    }
                }
            }
        }
    }

    /**
     * It paints arrows, if the searchtext goes on the left
     * or right side in the nirvana.
     *
     * @param g2 <code>Graphics2D</code>
     * @param startTextPos start postition of the searchtext
     */
    private void paintSearchtextArrows(Graphics2D g2, int startTextPos) {
        int offset = (cellNr - patternLength) / 2;

        int hch = cellHeight / 2; /* half cellHeight */
        int hd = distance / 2; /* half distance */

        if (searchText != null) {
            int xPos;
            int yPos;

            /* left side */
            if ((startTextPos - offset) > 0) {
                xPos = (GUIConstants.P2_X_OFFSET + headLength + distance) -
                    (hd / 2);
                yPos = GUIConstants.P2_Y_OFFSET + cellHeight;

                int[] xpoints = { xPos, xPos, xPos - hd };
                int[] ypoints = { yPos - hch, yPos + hch, yPos };
                Polygon arrow = new Polygon(xpoints, ypoints, 3);
                g2.fill(arrow);
            }

            /* right side */
            if (((startTextPos + cellNr) - offset) < searchText.length()) {
                xPos = GUIConstants.P2_X_OFFSET + headLength + distance +
                    (cellNr * cellWidth) + (hd / 2);
                yPos = GUIConstants.P2_Y_OFFSET + cellHeight;

                int[] xpoints = { xPos, xPos, xPos + hd };
                int[] ypoints = { yPos - hch, yPos + hch, yPos };
                Polygon arrow = new Polygon(xpoints, ypoints, 3);
                g2.fill(arrow);
            }
        }
    }

    /**
     * It paints the pattern characters.
     *
     * @param g2 <code>Graphics2D</code>
     * @param start start position
     * @param end end position
     * @param col <code>Color</code>
     */
    private void printPatternChars(Graphics2D g2, int start, int end, Color col) {
        if (pattern != null) {
            Color oldcolor = g2.getColor();
            g2.setColor(col);

            int y = GUIConstants.P2_Y_OFFSET + (2 * cellHeight) + distance +
                cellspacingVertical + letterHeight;
            int startX = GUIConstants.P2_X_OFFSET + headLength + distance +
                (patternOffset * cellWidth);

            for (int i = start; i <= end; i++) {
                g2.drawString(pattern.getPattern().substring(i, i + 1),
                    startX + (i * cellWidth) + cellspacingHorizontal, y);
            }

            g2.setColor(oldcolor);
        }
    }

    /**
     * It paints the table entries.
     *
     * @param g2 <code>Graphics2D</code>
     * @param start start position
     * @param end end position
     * @param col <code>Color</code>
     */
    private void printPatternEntries(Graphics2D g2, int start, int end,
        Color col) {
        if (pattern != null) {
            Color oldcolor = g2.getColor();
            g2.setColor(col);

            int y = GUIConstants.P2_Y_OFFSET + (2 * cellHeight) + distance +
                cellspacingVertical + letterHeight;
            int startX = GUIConstants.P2_X_OFFSET + headLength + distance +
                (patternOffset * cellWidth);

            for (int i = start; i <= end; i++) {
                int nr = pattern.getTblEntryAt(i);

                if ((nr >= 0) && (nr < 10)) { /* have it to print 1 or 2 chars (f.e. "-1") */
                    g2.drawString(new Integer(nr).toString(),
                        startX + (i * cellWidth) + cellspacingHorizontal,
                        y + cellHeight);
                } else {
                    g2.drawString(new Integer(nr).toString(),
                        (startX + (i * cellWidth) + cellspacingHorizontal) -
                        (letterWidth / 2), y + cellHeight);
                }
            }

            g2.setColor(oldcolor);
        }
    }

    /**
     * It paints the table indicies.
     *
     * @param g2 <code>Graphics2D</code>
     * @param start start position
     * @param end end position
     * @param col <code>Color</code>
     */
    private void printPatternIndicies(Graphics2D g2, int start, int end,
        Color col) {
        Color oldcolor = g2.getColor();
        g2.setColor(col);

        int y = GUIConstants.P2_Y_OFFSET + (2 * cellHeight) + distance +
            cellspacingVertical + letterHeight;
        int startX = GUIConstants.P2_X_OFFSET + headLength + distance +
            (patternOffset * cellWidth);

        for (int i = start; i <= end; i++) {
            if ((i >= 0) && (i < 10)) { /* have it to print 1 or 2 chars (f.e. "-1") */
                g2.drawString(new Integer(i).toString(),
                    startX + (i * cellWidth) + cellspacingHorizontal,
                    y + (2 * cellHeight));
            } else {
                g2.drawString(new Integer(i).toString(),
                    (startX + (i * cellWidth) + cellspacingHorizontal) -
                    (letterWidth / 2), y + (2 * cellHeight));
            }
        }

        g2.setColor(oldcolor);
    }

    /**
     * It is (only) called from paintStep and
     * paints a view window around the actual compared characters.
     *
     * @param g2 <code>Graphics2D</code>
     * @param pos pattern position
     * @param animationOffset Offset (in px) for the animation
     */
    private void paintViewWindow(Graphics2D g2, int pos, int animationOffset) {
        if (actStep != null) {
            Stroke oldStroke = g2.getStroke();
            g2.setStroke(new BasicStroke((int) (letterHeight * 0.26),
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));

            Color oldColor = g2.getColor();
            g2.setColor(GUIConstants.VIEW_WINDOW_COLOR);
            g2.draw(new Rectangle((GUIConstants.P2_X_OFFSET + headLength +
                    distance + ((pos + patternOffset) * cellWidth)) -
                    animationOffset, GUIConstants.P2_Y_OFFSET, cellWidth,
                    (5 * cellHeight) + distance));
            g2.setColor(oldColor);
            g2.setStroke(oldStroke);
        }
    }

    /**
     * As the name it says.<br>
     * It paints the whole showpanel and the searchtext
     * and makes two static images of it. After that the animation
     * thread will be started.
     * 
     * @param g2 <code>Graphics2D</code>
     * @param patPos actual pattern position
     * @param jD #cells pattern jumped
     */
    private void doAnimation(Graphics2D g2, int patPos, int jD) {
        if (searchTImage != null) {
            animationStarted = true;

            /* buffers the panelgraphics */
            Graphics g = this.getGraphics();
            panelImage = createImage(getWidth(), getHeight());

            Graphics2D g2Panel = (Graphics2D) panelImage.getGraphics();
            g2Panel.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            g2Panel.setBackground(Color.WHITE);
            g2Panel.clearRect(0, 0, getWidth(), getHeight());
            this.paintComponent(g);
            setBackground(Color.WHITE);
            super.paintComponent(g);
            initPainting();

            int pixel = jD * cellWidth;

            paintBasicTabs(g2Panel);
            printPatternEntries(g2Panel, 0, (patPos + jD) - 1, Color.BLACK);
            printPatternEntries(g2Panel, patPos + jD, patPos + jD,
                GUIConstants.HIGHLIGHT_COLOR);
            printPatternEntries(g2Panel, patPos + jD + 1, patternLength - 1,
                Color.BLACK);
            printPatternIndicies(g2Panel, -1, patPos - 1, Color.GRAY);
            printPatternIndicies(g2Panel, patPos, patPos,
                GUIConstants.HIGHLIGHT_COLOR);
            printPatternIndicies(g2Panel, patPos + 1, patternLength - 1,
                Color.GRAY);

            g.drawImage(panelImage, 0, 0, this);

            aniThread = new Thread(new AniThread(g, g2, pixel, patPos, jD, this));
            aniThread.start();  /* The animation is started */
        }
    }

    /**
     * Thread for the animation in phase 2.
     * 
     * @author Sebastian Patschorke
     */
    private class AniThread implements Runnable {
        private Graphics g;
        private Graphics2D g2;
        private int pixel;
        private int patPos;
        private int JumpDis;
        private java.awt.image.ImageObserver io;

        /**
         * The constructor of <code>AniThread</code> with some essential values.
         *
         * @param g <code>Graphics</code>
         * @param g2 <code>Graphics2D</code>
         * @param pixel Animation length in px
         * @param patPos pattern position
         * @param JumpDis distance to animate in 'cells' or better pattern positions
         * @param io <code>ImageObserver</code>
         */
        public AniThread(Graphics g, Graphics2D g2, int pixel, int patPos,
            int JumpDis, java.awt.image.ImageObserver io) {
            this.g = g;
            this.g2 = g2;
            this.pixel = pixel;
            this.patPos = patPos;
            this.JumpDis = JumpDis;
            this.io = io;
        }

        /**
         * what should the thread do<br>
         * it's started (indirectly) from super.start()
         */
        public void run() {
            int imageOffset = 0;
            int x = GUIConstants.P2_X_OFFSET + headLength + distance;
            int y = GUIConstants.P2_Y_OFFSET;
            int w = cellNr * cellWidth;
            int h = 2 * cellHeight;

            try {
                while (pixel >= imageOffset) {
                    g.drawImage(panelImage, 0, 0, io);

                    g.drawImage(searchTImage, x, y, x + w + 1, y + h + 1,
                        0 + imageOffset, 0, w + imageOffset + 1, h + 1, io);
                    paintViewWindow((Graphics2D) g, patPos + JumpDis,
                        imageOffset);
                    Thread.sleep(getTimeToSleep(imageOffset, pixel, 0.3));
                    imageOffset++;
                }

                animationStarted = false;
                paintStep(g2);
            } catch (InterruptedException ie) {
                animationStarted = false;
                paintStep(g2);
            }
        }

        /**
         * Returns the time the animation thread had to sleep between each shown pixel step.<br>
         * Includes a kind of acceleration: At the begin there is a quadratic reduce of the time
         * and at the end an increment. In between there is the constants minimum.
         * 
         * @see GUIConstants
         *
         * @param pp Pixel Position
         * @param pixel animation length
         * @param per percents of param pixel for acceleration
         * @return time thread had to sleep
         */
        private int getTimeToSleep(int pp, int pixel, double per) {
            if (pp > pixel) {
                pp = pixel;
            }

            if (pp < 0) {
                pp = 0;
            }

            if (per < 0.0) {
                per = 0.0;
            }

            if (per > 0.5) {
                per = 0.5;
            }

            double acc = GUIConstants.ACC_LENGTH;

            if (acc > (pixel / 2)) {
                acc = pixel / 2;
            }

            double max = (double) GUIConstants.MAX_PERIOD_TIME;
            double min = (double) GUIConstants.MIN_PERIOD_TIME;

            if ((pp >= acc) && (pp <= (pixel - acc))) {
                return GUIConstants.MIN_PERIOD_TIME;
            } else {
                if (pp > (pixel - acc)) {
                    pp -= pixel;
                    pp = -pp;
                }

                double tts = ((max - min) / (acc * acc) * (double) pp * (double) pp) -
                    ((2.0 * (max - min)) / acc * (double) pp) + max;

                return (int) tts;
            }
        }
    }
}
