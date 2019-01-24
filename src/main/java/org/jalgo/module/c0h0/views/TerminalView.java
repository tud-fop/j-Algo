package org.jalgo.module.c0h0.views;

/**
 * this is not realy a view
 * consider this rather to be a development tool :D
 *
 * @author hendrik
 *	
 */
public class TerminalView {
	static boolean showlog = true;
	
	/**
	 * prints line number for development purposes
	 * @param line
	 */
	public static void println(String line){
		if(showlog)
			System.out.println("TerminalView: "+line);
	}

}
