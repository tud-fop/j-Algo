package org.jalgo.module.bfsdfs;

import java.net.URL;

import org.jalgo.main.IModuleInfo;
import org.jalgo.main.util.Messages;

public class ModuleInfo implements IModuleInfo {
	private static IModuleInfo instance;


	private ModuleInfo() {}

	/**
	 * Retrieves a singleton instance of <code>IModuleInfo</code>
	 * @return the singleton instance
	 */
	public static IModuleInfo getInstance() {
		if (instance == null) instance = new ModuleInfo();
		return instance;
	}

	public String getName() {
		return "BFS / DFS";
	}

	public String getVersion() {
		return "0.9";
	}

	public String getAuthor() {
		return "Florian Dornbusch, Thomas Görres, Anselm Schmidt, Johannes Siegert, Ephraim Zimmer";
	}

	public String getDescription() {
		return Messages.getString("bfsdfs", "ModuleInfo.description");
	}

	public URL getLogoURL() {
		return Messages.getResourceURL("bfsdfs", "ui.Logo_small");
	}

	public String getLicense() {
		return "Public Domain. Welcome Screen Icons CC Oxygen (www.oxygen-icons.org)";
	}
	
	public URL getHelpSetURL(){
		return Messages.getResourceURL("bfsdfs","HelpSet_Name");
	}
}