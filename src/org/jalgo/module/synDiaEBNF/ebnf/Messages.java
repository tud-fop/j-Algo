/*
 * Created on Jun 29, 2004
 */

package org.jalgo.module.synDiaEBNF.ebnf;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Cornelius Hald
 */
public class Messages {

	private static final String BUNDLE_NAME = "org.jalgo.main.de"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE =
		ResourceBundle.getBundle(BUNDLE_NAME);

	/**
	 * 
	 */
	private Messages() {

		// TODO Auto-generated constructor stub
	}
	/**
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		// TODO Auto-generated method stub
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
