package org.jalgo.module.lambda;

import java.net.URL;

import org.jalgo.main.IModuleInfo;
import org.jalgo.main.util.Messages;

public class ModuleInfo implements IModuleInfo {

	/** The singleton instance */
	private static IModuleInfo instance;

	/**
	 * The only constructor is unusable from outside this class. This is part of
	 * the singleton design pattern.
	 */
	private ModuleInfo() {
	// unusable from outside
	}

	/**
	 * Retrieves the singleton instance of <code>IModuleInfo</code>.
	 * 
	 * @return the singleton instance
	 */
	public static IModuleInfo getInstance() {
		if (instance == null) instance = new ModuleInfo();
		return instance;
	}
	
	public String getName() {
		return Messages.getString("lambda", "Module_name");
	}

	public String getVersion() {
		return Messages.getString("lambda", "Module_version");
	}

	public String getAuthor() {
		return Messages.getString("lambda", "Module_authors");
	}

	public String getDescription() {
		return Messages.getString("lambda", "Module_description_1");
	}

	public URL getLogoURL() {
		return Messages.getResourceURL("lambda", "Module_logo");
	}

	public String getLicense() {
		return Messages.getString("lambda", "Module_license");
	}

	public URL getHelpSetURL() {
		return Messages.getResourceURL("lambda","HelpSet_Name");
	}
	
}