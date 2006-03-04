package org.jalgo.main.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Alexander Claus
 */
public class Settings {

	private static String fileSep = System.getProperty("file.separator");
	private static String path =
		System.getProperty("user.home")+fileSep+".jalgo"+fileSep+"main.prefs";
	private static Properties properties;

	/**
	 * Creates settings file with default settings.
	 */
	static {
		properties = new Properties();
		try {
			if (!new File(path).exists()) {
				properties.put("ShowSplashOnStartup", "true");
				properties.put("ShowModuleChooserOnStartup", "true");
				properties.put("MaximizeWindowOnStartup", "true");
				properties.put("Language", "de");
				store();
			}
			else properties.load(new FileInputStream(path));
		}
		catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static boolean getBoolean(String key) {
		return Boolean.parseBoolean((String)properties.get(key));
	}

	public static void setBoolean(String key, boolean value) {
		properties.put(key, String.valueOf(value));
		store();
	}

	public static String getString(String key) {
		return properties.getProperty(key);
	}

	public static void setString(String key, String value) {
		properties.put(key, value);
		store();
	}

	private static void store() {
		try {
			FileOutputStream out = new FileOutputStream(path); 
			properties.store(out, "j-Algo Settings");
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