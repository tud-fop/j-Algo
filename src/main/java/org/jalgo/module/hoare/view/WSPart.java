package org.jalgo.module.hoare.view;

import java.awt.Dimension;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jalgo.main.util.Messages;
import org.jalgo.main.util.Settings;

/**
 * Abstract super class of parts of the gui.
 * It provides some useful functions.
 * 
 * @author Antje
 */
public abstract class WSPart extends JPanel implements Observer {
	
	/**
	 * ID for getting settings and external strings.
	 */
	protected final String ID;
	/**
	 * <code>JLabel</code> displaying the title
	 */
	protected final JLabel title;
	
	/**
	 * Creates a new instance of WSPart.
	 * @param id for getting settings and external strings.
	 */
	public WSPart(String id) {
		ID = id;
		title = new JLabel(Messages.getString("hoare", "name."+ID));
		title.setMinimumSize(new Dimension(0,0));
	}
	
	/**
	 * Updates the gui.
	 * This method is called when there is a change in the <code>Model</code>.
	 * @param o
	 * 		supposed to be the <code>Model</code>.
	 * @param arg
	 * 		supposed to be <code>null</code>.
	 */
	public void update(Observable o, Object arg) {
		
	}
	
	/**
	 * Returns the setting specified by the <code>key</code>.
	 * The actual key used is <code>"view." + ID + key</code>.
	 * @param key the string that identifies the setting.
	 * @return the setting as a string.
	 */
	protected String getSettingsString(String key) {
		if (ID!=null) {
			String value = "";
			try {
				value = Settings.getString("hoare", "view."+ID+"."+key);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return value;
		}
		return null;
	}
	
	/**
	 * Sets the setting specified by the <code>key</code> to the specified <code>value</code>.
	 * @param key the string that identifies the setting.
	 * @param value the new value for the setting.
	 */
	protected void setSettingsString(String key, String value) {
		if (ID!=null) {
			try {
				Settings.setString("hoare", "view."+ID+"."+key, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Returns the setting specified by the key.
	 * The actual key used is <code>"view." + ID + key</code>.
	 * @param key the string that identifies the setting.
	 * @return the setting as a boolean.
	 */
	protected boolean getSettingsBoolean(String key) {
		if (ID!=null) {
			boolean value = false;
			try {
				value = Settings.getBoolean("hoare", "view."+ID+"."+key);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return value;
		}
		return false;
	}
	
	/**
	 * Sets the setting specified by the <code>key</code> to the specified <code>value</code>.
	 * @param key the string that identifies the setting.
	 * @param value the new value for the setting.
	 */
	protected void setSettingsBoolean(String key, boolean value) {
		if (ID!=null) {
			try {
				Settings.setBoolean("hoare", "view."+ID+"."+key, value);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Returns the message specified by the key.
	 * The actual key used is <code>"view." + ID + key</code>.
	 * @param key the string that identifies the message.
	 * @return the message.
	 */
	protected String getMessage(String key) {
		if (ID!=null) {
			return Messages.getString("hoare", "view."+ID+"."+key);
		}
		return null;
	}

	/**
	 * Returns the url specified by the key.
	 * The actual key used is <code>"view." + ID + key</code>.
	 * @param key the string that identifies the message.
	 * @return the url.
	 */
	protected URL getURL(String key) {
		if (ID!=null) {
			return Messages.getResourceURL("hoare", "view."+ID+"."+key);
		}
		return null;
	}
	
}
