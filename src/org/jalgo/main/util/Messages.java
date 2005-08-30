/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and platform
 * independant. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

/*
 * Created on Jun 29, 2004
 */

package org.jalgo.main.util;

import java.net.URL;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Alexander Claus
 */
public abstract class Messages {

	private final static HashMap<String,ResourceBundle> RESOURCE_BUNDLES =
		new HashMap<String,ResourceBundle>();
	static {
		registerResourceBundle("main", "org.jalgo.main.de");
	}

	/**
	 * Loads and registers a resource bundle with the given path name. The
	 * resource bundle will be accessible under the given key calling<br>
	 * <code>Messages.getString(bundleKey, messageKey)</code>.<br>
	 * If the key is already assigned, an <code>IllegalArgumentException</code>
	 * is thrown.
	 * 
	 * @param key the access key for the resource bundle
	 * @param bundlePath the file path name of the bundle data base
	 */
	public static void registerResourceBundle(String key, String bundlePath) {
		if (RESOURCE_BUNDLES.containsKey(key) ||
			RESOURCE_BUNDLES.containsKey(key+"_res"))
			throw new IllegalArgumentException("Key already assigned: "+key);
		RESOURCE_BUNDLES.put(key, ResourceBundle.getBundle(bundlePath));
		RESOURCE_BUNDLES.put(key+"_res", ResourceBundle.getBundle(
			bundlePath.substring(0, bundlePath.lastIndexOf("."))+".res"));
	}

	/**
	 * Retrieves an externalized string message with the given key from the
	 * specified resource bundle.
	 *  
	 * @param bundleKey the key for accessing the resource bundle
	 * @param messageKey the key identifying the string message
	 * 
	 * @return the externalized string message, if it was found.
	 */
	public static String getString(String bundleKey, String messageKey) {
		try {
			return RESOURCE_BUNDLES.get(bundleKey).getString(messageKey);
		}
		catch (MissingResourceException e) {
			return '!' + messageKey + '!';
		}
	}

	/**
	 * Retrieves an <code>URL</code> object backed by an externalized string of
	 * the interesting path name. A call to this method will have the same
	 * effect as<br>
	 * <code>getClass().getResource(Messages.getString(bundleKey+"_res", key))
	 * </code>
	 * 
	 * @param bundleKey the key to the resource bundle
	 * @param key the key to the externalized string
	 * 
	 * @return the requested <code>URL</code> object, if it exists,
	 * 			<code>null</code> otherwise
	 */
	public static URL getResourceURL(String bundleKey, String key) {
		try {
			return Messages.class.getResource(
				RESOURCE_BUNDLES.get(bundleKey+"_res").getString(key));
		}
		catch (MissingResourceException ex) {
			return null;
		}
	}
}