/**
 * 
 */
package org.jalgo.module.heapsort;

import java.net.URL;

import org.jalgo.main.IModuleInfo;

/**
 * As demanded by j-Algo specification.
 * 
 * @author mbue
 *
 */
public class ModuleInfo implements IModuleInfo {
	
	static IModuleInfo instance = null;
	
	private ModuleInfo() {
		super();
	}
	
	public static IModuleInfo getInstance() {
		if (instance == null)
			instance = new ModuleInfo();
		return instance;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.main.IModuleInfo#getAuthor()
	 */
	public String getAuthor() {
		return Util.getString("Module_authors");
	}

	/* (non-Javadoc)
	 * @see org.jalgo.main.IModuleInfo#getDescription()
	 */
	public String getDescription() {
		return Util.getString("Module_description");
	}

	/* (non-Javadoc)
	 * @see org.jalgo.main.IModuleInfo#getHelpSetURL()
	 */
	public URL getHelpSetURL() {
		return Util.getResourceURL("HelpSet_Name");
	}

	/* (non-Javadoc)
	 * @see org.jalgo.main.IModuleInfo#getLicense()
	 */
	public String getLicense() {
		return Util.getString("Module_license");
	}

	/* (non-Javadoc)
	 * @see org.jalgo.main.IModuleInfo#getLogoURL()
	 */
	public URL getLogoURL() {
		return Util.getResourceURL("Module_logo");
	}

	/* (non-Javadoc)
	 * @see org.jalgo.main.IModuleInfo#getName()
	 */
	public String getName() {
		return Util.getString("Module_name");
	}

	/* (non-Javadoc)
	 * @see org.jalgo.main.IModuleInfo#getVersion()
	 */
	public String getVersion() {
		return Util.getString("Module_version");
	}

}
