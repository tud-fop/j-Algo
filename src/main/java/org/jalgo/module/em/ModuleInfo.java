package org.jalgo.module.em;

import java.net.URL;

import org.jalgo.main.IModuleInfo;
import org.jalgo.main.util.Messages;

/**
 * The information class for the j-Algo module. 
 * 
 * @author tobias
 *
 */
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

	@Override
	public String getName() {
		return Messages.getString("em", "ModuleInfo.name");
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getAuthor() {
		return "Meinhardt Branig, Kilian Gebhardt, Christian Hendel, Tobias Nett, Tom Schumann.";
	}

	@Override
	public String getDescription() {
		return Messages.getString("em", "ModuleInfo.info");
	}

	@Override
	public URL getLogoURL() {
		return Messages.getResourceURL("em","EM.ICON");
	}

	@Override
	public String getLicense() {
		return Messages.getString("em", "ModuleInfo.license");
	}
	
	@Override
	public URL getHelpSetURL(){
		return Messages.getResourceURL("em","HelpSet_Name");
	}
}