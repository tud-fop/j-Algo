/*
 * Created on 13.04.2004
*/
package org.jalgo.module.synDiaEBNF;

import org.eclipse.swt.graphics.Image;
import org.jalgo.main.IModuleInfo;

/**
 * Gives information about the module.
 * 
 * @author Michael Pradel
 * @author Benjamin Scholz
 * @author Marco Zimmerling
 * @author Babett Schaliz
 * @author Stephan Creutz
 */
public class ModuleInfo implements IModuleInfo {

	private String openFileName;

	/**
	 * @see IModuleInfo#getName()
	 */
	public String getName() {
		return Messages.getString("ModuleInfo.EBNF_und_Syntaxdiagramme_1"); //$NON-NLS-1$
	}

	/**
	 * @see IModuleInfo#getVersion()
	 */
	public String getVersion() {
		return "0.42"; //$NON-NLS-1$
	}

	/**
	 * @see IModuleInfo#getAuthor()
	 */
	public String getAuthor() {
		return Messages.getString("ModuleInfo.Authors_3"); //$NON-NLS-1$
	}

	/**
	 * @see IModuleInfo#getDescription()
	 */
	public String getDescription() {
		return Messages.getString("ModuleInfo.Description_1_4") //$NON-NLS-1$
			+ Messages.getString("ModuleInfo.Description_2_5") //$NON-NLS-1$
			+ Messages.getString("ModuleInfo.Description_3_6") //$NON-NLS-1$
			+ Messages.getString("ModuleInfo.Description_4_7") //$NON-NLS-1$
			+ Messages.getString("ModuleInfo.Description_5_8") //$NON-NLS-1$
			+ Messages.getString("ModuleInfo.Description_6_9"); //$NON-NLS-1$
	}

	/**
	 * @see IModuleInfo#getLogo()
	 */
	public Image getLogo() { // TODO: design logo and provide it here as an org.eclipse.swt.graphics.Image
		return null;
	}

	/**
	 * @see IModuleInfo#getLicense()
	 */
	public String getLicense() {
		return Messages.getString("ModuleInfo.GNU_General_Public_License_10"); //$NON-NLS-1$
	}

	/**
	 * @see IModuleInfo#getOpenFileName()
	 */
	public String getOpenFileName() {
		return openFileName;
	}

	/**
	 * @see IModuleInfo#setOpenFileName(String)
	 */
	public void setOpenFileName(String string) {
		openFileName = string;
	}
}
