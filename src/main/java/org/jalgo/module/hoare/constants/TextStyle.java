package org.jalgo.module.hoare.constants;

/**
 * This <code>enum</code> is used for the styling of Text<br> 
 * SHORT : Performs the Text to be shown in a short form with many variables<br>
 * FULL  : Performs the Text to be shown in a full version.
 *         Every variable will be resolved.<br>
 * SOURCE: Performs the Text to be shown in a full an C0-conform style.<br> 
 * EDITOR: Returns the Text to the original given style
 * 
 * @author Thomas
 *
 */
public enum TextStyle {

	SHORT,
	FULL,
	SOURCE,
	EDITOR
}
