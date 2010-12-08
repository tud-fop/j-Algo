package org.jalgo.module.bfsdfs.gui;
import java.awt.Color;
import java.awt.Font;

/**
 * The interface <code>GUIConstants</code> is a collection of several constant
 * values used by various GUI classes.
 * 
 * @author Anselm Schmidt
 * @author Florian Dornbusch
 */
public interface GUIConstants {
	/*------------------------------colors----------------------------*/
	/** The background color of the welcome screen. */
	public static final Color WELCOME_SCREEN_BACKGROUND =
		new Color(51, 102, 153);
	/** The background color of the graph view. */
	public static final Color GRAPH_VIEW_BACKGROUND = Color.white;
	
	/** The standard color of a node. */
	public static final Color NODE_COLOR = Color.white;
	/** The color of the node cursor. */
	public static final Color NODE_CURSOR_COLOR = new Color(192, 37, 0);
	/** The color at the top of a node. */
	public static final Color NODE_COLOR_TOP = new Color(255, 255, 255);
	/** The color at the bottom of a node. */
	public static final Color NODE_COLOR_BOTTOM = new Color(218, 221, 225);
	/** The color at the top of a waiting node. */
	public static final Color NODE_COLOR_WAITING_TOP =
		new Color(150, 180, 226);
	/** The color at the bottom of a waiting node. */
	public static final Color NODE_COLOR_WAITING_BOTTOM =
		new Color(78, 136, 202);
	/** The color at the top of a finished node. */
	public static final Color NODE_COLOR_FINISHED_TOP = new Color(27, 56, 89);
	/** The color at the bottom of a finished node. */
	public static final Color NODE_COLOR_FINISHED_BOTTOM = Color.black;
	
	/** The color of a node icon. */
	public static final Color NODE_ICON_COLOR = Color.white;
	/** The border color of a node. */
	public static final Color NODE_BORDER_COLOR = new Color(50,57,62);
	/** The border color of a focused node in erase mode. */
	public static final Color NODE_BORDER_COLOR_FOCUSED =
		new Color(192, 37, 0);
	/** The border color of a node icon. */
	public static final Color NODE_ICON_BORDER_COLOR = Color.black;
	
	/** The label color of a node. */
	public static final Color NODE_LABEL_COLOR = Color.black;
	/** The label color of a focused node in erase mode. */
	public static final Color NODE_LABEL_COLOR_FOCUSED =
		NODE_BORDER_COLOR_FOCUSED;
	/** The label color of a finished node. */
	public static final Color NODE_LABEL_COLOR_FINISHED = Color.white;
	/** The label color of a node icon. */
	public static final Color NODE_ICON_LABEL_COLOR = Color.black;
	
	/** The color of the node distance. */	
	public static final Color NODE_DISTANCE_COLOR = new Color(192, 37, 0);
	
	/** The color at the top of an untouched node. */
	public static final Color NODE_COLOR_UNTOUCHED_TOP = NODE_COLOR_TOP;
	/** The color at the bottom of a untouched node. */
	public static final Color NODE_COLOR_UNTOUCHED_BOTTOM = NODE_COLOR_BOTTOM;
	/** The border color of an untouched node. */
	public static final Color NODE_UNTOUCHED_BORDER_COLOR =
		new Color(31, 34, 35);
	/** The border color of a waiting node. */
	public static final Color NODE_WAITING_BORDER_COLOR =
		NODE_COLOR_FINISHED_TOP;
	/** The border color of a finished node. */
	public static final Color NODE_FINISHED_BORDER_COLOR =
		new Color(0, 0, 0);
	/** The label color of a untouched node. */
	public static final Color NODE_LABEL_COLOR_UNTOUCHED =
		NODE_UNTOUCHED_BORDER_COLOR;
	/** The label color of a waiting node. */
	public static final Color NODE_LABEL_COLOR_WAITING = Color.black;
	
	/** The color of an edge. */
	public static final Color EDGE_COLOR = new Color(0, 0, 0);
	/** The color of a focused edge. */
	public static final Color EDGE_COLOR_FOCUSED = new Color(192, 37, 0);
	
	/** The color of the NodeStackView background. */
	public static final Color NODESTACKVIEW_BACKGROUND_COLOR = Color.white;
	/** The top color of the highlighted first column in NodeStackView. */
	public static final Color NODESTACKVIEW_OWNER_COLOR_LEFT =
		new Color(114,118,146);
	/** The bottom color of the highlighted first column in NodeStackView. */
	public static final Color NODESTACKVIEW_OWNER_COLOR_RIGHT =
		new Color(30,31,38);
	
	/*------------------------------alpha values----------------------*/	
	/** The alpha value of a temporary element. */
	public static final int TEMP_ALPHA_VALUE = 100;
	/** The alpha value of the white rectangle the TreeView uses to paint
	 * on top of the graph. */
	public static final int TREEVIEW_ALPHA_VALUE = 180;
	/** The alpha value of the white rectangle for a disabled interactive
	 * GraphView. */
	public static final int GRAPHVIEW_ALPHA_VALUE = 180;
	/** 100% alpha value. */
	public static final int ALPHA_100_PERCENT = 255;
	
	/*------------------------------fonts-----------------------------*/
	/** The standard font family. */
	public static final String FONT_NAME = "Verdana";
	/** The standard writing font. */
	public static final Font WRITING_FONT = new Font(FONT_NAME, Font.BOLD, 12);
	/** The beamer writing font. */
	public static final Font BEAMER_WRITING_FONT =
		new Font(FONT_NAME, Font.BOLD, 15);
	/** The font of the text in a node. */
	public static final Font NODE_FONT = new Font(FONT_NAME, Font.BOLD, 18);
	/** The beamer font of the text in a node. */
	public static final Font NODE_BEAMER_FONT =
		new Font(FONT_NAME, Font.BOLD, 30);
	/** The font of the text in a node icon. */
	public static final Font NODE_ICON_FONT =
		new Font(FONT_NAME, Font.BOLD, 18);
	/** The beamer font of the text in a node icon. */
	public static final Font NODE_BEAMER_ICON_FONT =
		new Font(FONT_NAME, Font.BOLD, 20);
	/** The font for the node distance in a <code>TreeView</code>. */
	public static final Font NODE_DISTANCE_FONT =
		new Font(FONT_NAME, Font.BOLD, 14);
	/** The font for the node distance in a <code>TreeView</code>. */
	public static final Font NODE_BEAMER_DISTANCE_FONT =
		new Font(FONT_NAME, 0, 20);
	/** The font for the node distance in a <code>TreeView</code>. */
	
	/*--------------------------constant sizes------------------------*/
	/** The maximal row count of the start node chooser in
	 * <code>AlgoTab</code>. */
	public static final Integer MAX_ROW_COUNT = 5;
		
	/** The number of pixel scrolled in <code>SuccessorChooser</code>. */
	public static final Integer SCROLL_FACTOR = 7;
		
	/** The diameter of a node. */
	public static final Integer NODE_SIZE = 30;
	/** The diameter of a beamer node. */
	public static final Integer NODE_BEAMER_SIZE = 50;
	/** The diameter of a node icon. */
	public static final Integer NODE_ICON_SIZE = 30;
	/** The diameter of a beamer node icon. */
	public static final Integer NODE_BEAMER_ICON_SIZE = 36;
	/** The size of a node border. */
	public static final Integer NODE_BORDER_SIZE = 2;
	/** The size of a beamer node border. */
	public static final Integer NODE_BEAMER_BORDER_SIZE = 3;
	/** The size of a node icon border. */
	public static final Integer NODE_ICON_BORDER_SIZE = 2;
	/** The size of a beamer node icon border. */
	public static final Integer NODE_BEAMER_ICON_BORDER_SIZE = 2;
	/** The height of the color gradient at the top of the inner circle of
	 * a node. */
	public static final Integer NODE_GRADIENT_HEIGHT = 25;
	
	/** <code>[size of the node cursor] =
	 * [size of a node] * NODE_CURSOR_SCALE_FACTOR</code>*/
	public static final float NODE_CURSOR_SCALE_FACTOR = 1.2f;
	
	/** The width of an edge. */
	public static final Integer EDGE_WIDTH = 4;
	/** The width of an edge arrow. */
	public static final Integer EDGE_ARROW_WIDTH = 12;
	/** The length of an edge arrow. */
	public static final Integer EDGE_ARROW_LENGTH = 15;
	/** The diameter of an edge circle. */
	public static final Integer EDGE_CIRCLE_SIZE = 60;
	/** The width of the boundaries of an edge. */
	public static final Integer EDGE_BOUNDARIES = 20;
	/** The width of a tree edge. */
	public static final int EDGE_TREE_WIDTH = 5;
	/** The percentage of the tree edges to be clipped on each side
	 * (1 is 100%). */
	public static final double EDGE_TREE_CLIPPING = 0.2;
	/** The maximum clipping size of the tree edges on each side */
	public static final int EDGE_TREE_MAXCLIP = 25;
	/** The width of a tree edge arrow. */
	public static final Integer EDGE_TREE_ARROW_WIDTH = 7;
	/** The length of a tree edge arrow. */
	public static final Integer EDGE_TREE_ARROW_LENGTH = 15;
	
	
	/** A big space between nodes. */
	public static final Integer SPACE_BIG = 30;
	/** A small space between nodes. */
	public static final Integer SPACE_SMALL = 5;
	
	/** The number of successors in the NodeStackView. */
	public static final Integer NODESTACKVIEW_NUMBER_SUCCESSORS = 5;
	
	/** The width of the accessible graph area. */
	public static final Integer MAX_GRAPH_WIDTH = 2000;
	/** The height of the accessible graph area. */
	public static final Integer MAX_GRAPH_HEIGHT = 1000;
	
	/*--------------------------animation times-----------------------*/
	/** The animation speed of playing the algorithm in milliseconds. */
	public static final Integer PLAY_ANIMATION = 1500;
	/** The duration of a tree edge animation in milliseconds. */
	public static final Integer EDGE_ANIMATION = 500;
	/** The duration of a tree node animation in milliseconds. */
	public static final Integer NODE_ANIMATION_TIME = 1000;
	/** The duration of the node cursor's blinking animation in milliseconds.*/
	public static final Integer NODE_CURSOR_ANIMATION_TIME = 2000;
	/** The duration of the node cursor's hiding animation in milliseconds. */
	public static final Integer NODE_CURSOR_HIDING_ANIMATION_TIME = 500;
	/** The repaint delay during animations in milliseconds. */
	public static final Integer ANIMATION_REPAINT_DELAY = 30;
}
