/**
 * 
 */
package org.jalgo.module.heapsort;

import java.net.URL;

import org.jalgo.main.util.Messages;

/**
 * @author mbue
 *
 */
public class Util {

	private static final String main = "main";
	private static final String module = "heapsort";
	
	public static String getString(String key) {
		return Messages.getString(module, key);
	}
	
	public static URL getMainResourceURL(String key) {
		return Messages.getResourceURL(main, key);
	}
	
	public static URL getResourceURL(String key) {
		return Messages.getResourceURL(module, key);
	}

}
