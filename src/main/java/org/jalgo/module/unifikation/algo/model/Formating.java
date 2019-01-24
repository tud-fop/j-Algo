package org.jalgo.module.unifikation.algo.model;

/*
 * Defines Colors for formating
 */
public class Formating {
	/**
	 * Normal Chars (Brackets...) color
	 */
	public static final String Char="#777777";
	//see name
	public static final String Function="#991000";
	public static final String Variable="#0000AA";
	/**
	 * selected text color
	 */
	public static final String Selected="#FFFF33";
	/**
	 * Hovered color
	 */
	public static final String Hover="#FFFFAA";
	/**
	 * active pair size in normal mode
	 */
	public static final int activeSizeNormal=18;
	/**
	 * inactive pair size in normal mode
	 */
	public static final int inactiveSizeNormal=10;
	/**
	 * active pair size in beamer mode
	 */
	public static final int activeSizeBeamer=24;
	/**
	 * inactive pair size in beamer mode
	 */
	public static final int inactiveSizeBeamer=15;
	/**
	 * True if currently in beamerMode
	 */
	private static boolean beamerMode=false;

	/**
	 * Formats the text with a color
	 * @param text Text to format
	 * @param color
	 * @return colored text
	 */
	public static String addColor(String text, String color){
		if(text.equals("")) return "";
		return "<span style=\"color:"+color+"\">"+text+"</span>";
	}

	/**
	 * Formats the text with a background color
	 * @param text Text to format
	 * @param color
	 * @return colored text
	 */
	public static String addBackgroundColor(String text, String color){
		if(text.equals("")) return "";
		return "<span style=\"background-color:"+color+"\">"+text+"</span>";
	}
	
	/**
	 * Formats the text with a given size
	 * @param text Text to format
	 * @param size
	 * @return resized text
	 */
	public static String addSize(String text, int size){
		if(text.equals("")) return "";
		return "<span style=\"font-size: "+size+"px\">"+text+"</span>";
	}
	
	/**
	 * Gets the text size for current state and to active flag of the text
	 * @param active True if state is active
	 * @return Current text size
	 */
	public static int getTextSize(boolean active){
		if(beamerMode){
			if(active) return activeSizeBeamer;
			else return inactiveSizeBeamer;
		}else{
			if(active) return activeSizeNormal;
			else return inactiveSizeNormal;
		}
	}
	
	/**
	 * Formats the text with a size according to its state
	 * @param text Text to format
	 * @param active True if state is active
	 * @return resized text
	 */
	public static String setTextSize(String text, boolean active){
		if(text.equals("")) return "";
		return addSize(text,getTextSize(active));
	}
	
	/**
	 * Formats the text to show that it has been used
	 * @param text Text to format
	 * @return Formated text
	 */
	public static String addUsedMark(String text){
		if(text.equals("")) return "";
		return "<u>"+text+"</u>";
	}
	
	/**
	 * Checks if currently in beamer Mode
	 * @return True if in beamer mode
	 */
	public static boolean isBeamerMode(){
		return beamerMode;
	}
	
	/**
	 * Toggles beamer mode
	 * @return state of beamer mode after change
	 */
	public static boolean toggleBeamerMode(){
		beamerMode=!beamerMode;
		return beamerMode;
	}
}
