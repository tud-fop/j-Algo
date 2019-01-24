package org.jalgo.module.unifikation;

import java.awt.Dimension;
import java.awt.Font;

import org.jalgo.module.unifikation.algo.model.Formating;

public interface Constants {
	
	public static final String ALPHA="\u03B1";
	public static final String BETA="\u03B2";
	public static final String GAMMA="\u03B3";
	public static final String DELTA="\u03B4";
	public static final String EPSILON="\u03B5";
	public static final String THETA="\u03B8";
	
	//most general unifier
	public static final String PHI="\u03C6";
	public static final String UNIFIEDARROW="\u21A6";
	
	
	//Editor
	public static final Dimension ButtonSize = new Dimension(45,30);
	public static final String Placeholder="\u25A1";
	public static final int ButtonGap = 5;
	public static final Font ButtonFont = new Font("Tahoma",Font.PLAIN,14);
	public static final Font ButtonAlgoFont = new Font("Tahoma",Font.PLAIN,11);
	public static final Font TextFont = new Font("Tahoma",Font.PLAIN,14); 
	
	public static final String NBodyCSS = "body {font-family:Times New Roman;font-size : "+(Formating.activeSizeNormal)+"px; font-weight:bold;}\r\n"+
	 "a {color: black; text-decoration:none}"+
	 "sub {font-size : "+(Formating.activeSizeNormal-4)+"px; }" +
	 ".constsymb {color: "+Formating.Function+"}" +
	 ".var {color: "+Formating.Variable+"}"; 
	
	public static final String BBodyCSS = "body {font-family:Times New Roman;font-size : "+(Formating.activeSizeBeamer)+"px; font-weight:bold;}\r\n"+
	 "a{color: black; text-decoration:none}" +
	 "sub {font-size : "+(Formating.activeSizeBeamer-4)+"px; }" +
	 ".constsymb {color: "+Formating.Function+"}" +
	 ".var {color: "+Formating.Variable+"}";
	
	
	//unicode umlauts
	
	public static final String lowercasedUE = "\u00FC";
	public static final String lowercasedOE = "\u00F6";
	public static final String lowercasedAE = "\u00E4";
}
