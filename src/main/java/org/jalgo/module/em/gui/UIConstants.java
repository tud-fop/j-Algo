package org.jalgo.module.em.gui;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import org.jalgo.main.util.Messages;

/**
 * The class {@code UIConstants} holds UI constants for the em module, such as
 * different fonts for presentation mode and normal mode.
 *
 * @author Tobias Nett
 * 
 */
public interface UIConstants {

	/**
	 * Big presentation mode font with pixel size 32 in the systems default
	 * font.
	 */
	public static final Font BEAMER_FONT = new Font(UIManager.getDefaults()
			.getFont("TextField.font").getFontName(), Font.PLAIN, 32);
	/**
	 * Bold presentation mode font with pixel size 32 in the systems default
	 * font.
	 */
	public static final Font BEAMER_FONT_BOLD = new Font(UIManager
			.getDefaults().getFont("TextField.font").getFontName(), Font.BOLD,
			32);

	/**
	 * Smaller presentation mode font with pixel size 16
	 */
	public static final Font PRESENTATION_FONT = new Font(UIManager
			.getDefaults().getFont("TextField.font").getFontName(), Font.PLAIN,
			16);
	/**
	 * Smaller bold presentation mode font with pixel size 16
	 */
	public static final Font PRESENTATION_FONT_BOLD = new Font(UIManager
			.getDefaults().getFont("TextField.font").getFontName(), Font.BOLD,
			16);

	/**
	 * Medium sized presentation mode font for partition table with pixel size 24.
	 */
	public static final Font PRESENTATION_PARTITION_FONT = new Font(UIManager
			.getDefaults().getFont("TextField.font").getFontName(), Font.PLAIN,
			24);
	/**
	 * Medium sized bold presentation mode font for partition table with pixel size 24.
	 */
	public static final Font PRESENTATION_PARTITION_FONT_BOLD = new Font(
			UIManager.getDefaults().getFont("TextField.font").getFontName(),
			Font.BOLD, 24);

	/**
	 * Default system font.
	 */
	public static final Font DEFAULT_FONT = UIManager.getDefaults().getFont(
			"Label.font");
	/**
	 * Default bold system font.
	 */
	public static final Font DEFAULT_FONT_BOLD = new Font(
			DEFAULT_FONT.getFontName(), Font.BOLD, DEFAULT_FONT.getSize());
	
	////////////////////////////////////
	/////////  Icon constants  /////////
	////////////////////////////////////
	
	/**
	 * Forward icon for input panels in presentation mode. Right arrow with rounded edges.
	 */
	public static final ImageIcon FORWARD_ICON = new ImageIcon(Messages.getResourceURL("em",
			"ButtonIcon.bForward"));
	/**
	 * Forward icon for input panels in standard mode. Right arrow with rounded edges.
	 */
	public static final ImageIcon FORWARD_ICON_SMALL = new ImageIcon(Messages.getResourceURL("em",
			"ButtonIcon.bForward.small"));
	
	/**
	 * Back icon for input panels in presentation mode. Left arrow with rounded edges.
	 */
	public static final ImageIcon BACK_ICON = new ImageIcon(Messages.getResourceURL("em",
			"ButtonIcon.bBack"));
	/**
	 * Back icon for input panels in standard mode. Left arrow with rounded edges.
	 */
	public static final ImageIcon BACK_ICON_SMALL = new ImageIcon(Messages.getResourceURL("em",
			"ButtonIcon.bBack.small"));
	
	/**
	 * Minus icon used for <i>remove</i> buttons. Icon for presentation mode.
	 */
	public static final ImageIcon REMOVE_ICON = new ImageIcon(Messages.getResourceURL("em",
			"ButtonIcon.bRemove"));
	
	/**
	 * Minus icon used for <i>remove</i> buttons. Icon for standard mode.
	 */
	public static final ImageIcon REMOVE_ICON_SMALL = new ImageIcon(Messages.getResourceURL("em",
			"ButtonIcon.bRemove.small"));
	
	/**
	 * Plus icon used for <i>add<i> buttons. Icon for presentation mode.
	 */
	public static final ImageIcon PLUS_ICON = new ImageIcon(Messages.getResourceURL("em",
			"ButtonIcon.bNew"));
	
	/**
	 * Plus icon used for <i>add<i> buttons. Icon for normal mode.
	 */
	public static final ImageIcon PLUS_ICON_SMALL = new ImageIcon(Messages.getResourceURL("em",
			"ButtonIcon.bNew.small"));
	
	/**
	 * Shield icon used for <i>fill</i> buttons. Icon for presentation mode.
	 */
	public static final ImageIcon SHIELD_ICON = new ImageIcon(Messages.getResourceURL("em",
			"ButtonIcon.bFill"));
	
	/**
	 * Shield icon used for <i>fill</i> buttons. Icon for normal mode.
	 */
	public static final ImageIcon SHIELD_ICON_SMALL = new ImageIcon(Messages.getResourceURL("em",
			"ButtonIcon.bFill.small"));
}
