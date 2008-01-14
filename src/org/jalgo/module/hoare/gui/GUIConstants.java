package org.jalgo.module.hoare.gui;

import java.awt.Color;
import java.awt.Font;
import java.net.URL;

import org.jalgo.main.util.Messages;

/**
 * The interface <code>GUIConstants</code> is a collection of several constant
 * values, used in the GUI implementation of the Hoare module.
 * 
 * @author Markus
 */

public interface GUIConstants {

	// TODO:Konstante GUI-Werte, wie Farben, Fonts, etc deklarieren

	/*------------------------------Colors------------------------------*/
	// Welcome-Screen Background
	public final static Color WELCOME_BACKGROUND = new Color(200, 20, 20);

	// Welcome-Screen Font-Color
	public final static Color WELCOME_FONT_COLOR = new Color(245, 207, 75);

	/*-------------------------------Fonts------------------------------*/
	public static final Font WELCOME_SCREEN_FONT = new Font("SansSerif",
			Font.BOLD, 18);

	public static final Font STANDARD_FONT = new Font("SansSerif", Font.PLAIN,
			12);

	public static final Font TOOLBAR_FONT = new Font("SansSerif", Font.PLAIN,
			11);

	public static final Font BEAMER_FONT = new Font("SansSerif", Font.PLAIN, 20);

	/*-------------------------------StyleSheets------------------------*/
	public static final URL CSS_TREE_NORM = Messages.getResourceURL("hoare",
			"css.tree");

	public static final URL CSS_TREE_BEAM = Messages.getResourceURL("hoare",
			"css.treeB");

	public static final URL CSS_EVENT_NORM = Messages.getResourceURL("hoare",
			"css.log");

	public static final URL CSS_EVENT_BEAM = Messages.getResourceURL("hoare",
			"css.logB");

	public static final URL CSS_RULE_NORM = Messages.getResourceURL("hoare",
			"css.rule");

	public static final URL CSS_RULE_BEAM = Messages.getResourceURL("hoare",
			"css.ruleB");

	public static final URL CSS_INPUT_NORM = Messages.getResourceURL("hoare",
			"css.input");

	public static final URL CSS_INPUT_BEAM = Messages.getResourceURL("hoare",
			"css.inputB");
}
