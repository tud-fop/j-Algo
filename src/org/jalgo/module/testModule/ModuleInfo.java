/*
 * Created on 24.06.2004
 */
package org.jalgo.module.testModule;

import org.eclipse.swt.graphics.Image;
import org.jalgo.main.IModuleInfo;

/**
 * @author stephan
 * 24.06.2004 20:20:00
 */
public class ModuleInfo implements IModuleInfo {
	private Image image;
	private String openfile;
	
	public ModuleInfo() {
		image = new Image(null, "logo.png");
	}
	public String getName() {
		return "Sample Module Name";
	}

	public String getVersion() {
		return "1.1.2";
	}

	public String getAuthor() {
		return "Module programmers name";
	}

	public String getDescription() {
		return "This is a sample module which demonstrates how program a module";
	}

	public Image getLogo() {
		// supported fileformats are GIF, BMP, JPEG, PNG and TIFF
		return image;
	}

	public String getLicense() {
		return "my license";
	}

	public String getOpenFileName() {
		return this.openfile;
	}

	public void setOpenFileName(String file) {
		this.openfile = file;
	}

}
