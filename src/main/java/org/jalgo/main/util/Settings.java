package org.jalgo.main.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * This class defines some methods for the handling of persistent settings.
 * For the j-Algo main program and for each module, a separate map of setting-
 * value-pairs is held. This guarantees, that no naming conflicts between
 * setting keys of different program units occur.<br>
 * To support this, the main program and each module, which should support
 * persistent settings, has to offer a file where the default values of this
 * settings are declared. This file must follow the naming conventions described
 * in the developers manual of j-Algo!
 * 
 * @author Alexander Claus
 */
public class Settings {

	private static String fileSep = System.getProperty("file.separator");
	private static Map<String, Properties> PROPERTIES =
		new HashMap<String, Properties>();
	private static String prefsPath =
		System.getProperty("user.home")+fileSep+".jalgo"+fileSep;

	static {
		registerSettingsBundle("main", "/main.prefs");
	}

	/**
	 * Loads and registers a settings bundle with the given resource path. The
	 * settings bundle will be accessible under the given key.<br>
	 * If the key is already assigned, an <code>IllegalArgumentException</code>
	 * is thrown.<br>
	 * If a module doesn't support persistent settings, this call will be
	 * ignored. If no user settings file is saved or the version of the default
	 * settings file changes, the default settings are saved in a user settings
	 * file.
	 * 
	 * @param key the access key for the settings bundle
	 * @param path the path name of the default settings file
	 */
	public static void registerSettingsBundle(String key, String path) {
		if (PROPERTIES.containsKey(key))
			throw new IllegalArgumentException("Key already assigned: "+key);
		// if module does not support persistent settings, ignore this call
		if (Settings.class.getResourceAsStream(path) == null) return;

		// load defaults
		Properties defaults = new Properties();
		try {defaults.load(Settings.class.getResourceAsStream(path));}
		catch (IOException ex) {ex.printStackTrace();}
		String defaultVersion = defaults.getProperty("Version");
		
		Properties properties;
		// check if user settings file exists, load user settings
		if (!new File(prefsPath+key+".prefs").exists()) properties = defaults;
		else {
			properties = new Properties(defaults);
			try {properties.load(new FileInputStream(prefsPath+key+".prefs"));}
			catch (IOException ex) {ex.printStackTrace();}
			if (!properties.getProperty("Version").equals(defaultVersion))
				properties = defaults;
		}

		// store possible changes in user settings file
		PROPERTIES.put(key, properties);
		store(key);
	}

	/**
	 * Retrieves the setting with the given <code>settingKey</code> from the
	 * settings bundle with the given <code>resourceKey</code> as boolean value.
	 * If the <code>settingKey</code> not exists or it doesn't represent a
	 * boolean value, <code>false</code> is returned.
	 * 
	 * @param resourceKey the key to the settings bundle
	 * @param settingKey the key of the setting
	 * @return the value of the setting
	 */
	public static boolean getBoolean(String resourceKey, String settingKey) {
		return Boolean.parseBoolean(
			(String)PROPERTIES.get(resourceKey).get(settingKey));
	}

	/**
	 * Sets the value of the setting with the given <code>settingKey</code> from
	 * the settings bundle with the given <code>resourceKey</code> to the given
	 * <code>value</code>.
	 * 
	 * @param resourceKey the key to the settings bundle
	 * @param key the key of the setting
	 * @param value the new value
	 */
	public static void setBoolean(String resourceKey, String key, boolean value) {
		PROPERTIES.get(resourceKey).put(key, String.valueOf(value));
		store(resourceKey);
	}

	/**
	 * Retrieves the setting with the given <code>settingKey</code> from the
	 * settings bundle with the given <code>resourceKey</code> as string.
	 * If the <code>settingKey</code> not exists, <code>null</code> is returned.
	 * 
	 * @param resourceKey the key to the settings bundle
	 * @param settingKey the key of the setting
	 * @return the value of the setting
	 */
	public static String getString(String resourceKey, String key) {
		return PROPERTIES.get(resourceKey).getProperty(key);
	}

	/**
	 * Sets the value of the setting with the given <code>settingKey</code> from
	 * the settings bundle with the given <code>resourceKey</code> to the given
	 * <code>value</code>.
	 * 
	 * @param resourceKey the key to the settings bundle
	 * @param key the key of the setting
	 * @param value the new value
	 */
	public static void setString(String resourceKey, String key, String value) {
		PROPERTIES.get(resourceKey).put(key, value);
		store(resourceKey);
	}

	/**
	 * Stores the settings with the given <code>resourceKey</code> in a user
	 * settings file. Each settings bundle is saved in a separate user file.
	 * 
	 * @param resourceKey the key to the settings bundle
	 */
	private static void store(String resourceKey) {
		try {
			File folder = new File(prefsPath); 
			if (!folder.exists()) folder.mkdir();
			FileOutputStream out =
				new FileOutputStream(prefsPath+resourceKey+".prefs");
			String comment;
			if (resourceKey.equals("main")) comment = "j-Algo Settings";
			else comment = "Settings for the "+resourceKey+" module";
			PROPERTIES.get(resourceKey).store(out, comment);
			out.close();
		}
		catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}		
	}
}