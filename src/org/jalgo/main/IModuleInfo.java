/*
 * Created on 24.06.2004
 */
 
package org.jalgo.main;

import org.eclipse.swt.graphics.Image;

/**
 * @author Stephan Creutz
 */
public interface IModuleInfo {
	public abstract String getName();
	public abstract String getVersion();
	public abstract String getAuthor();
	public abstract String getDescription();
	public abstract Image getLogo();
	public abstract String getLicense();
	
	/**
	 * Get the filename of the currently opened file.
	 * @return filename
	 */
	public abstract String getOpenFileName();
	
	/**
	 * Set the filename of the currently opened file.
	 * @param string filename
	 */
	public abstract void setOpenFileName(String string);
}