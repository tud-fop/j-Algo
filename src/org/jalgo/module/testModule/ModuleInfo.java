/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and
 * platform independant. j-Algo is developed with the help of Dresden
 * University of Technology.
 *
 * Copyright (C) 2004 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * Created on 24.06.2004
 */
package org.jalgo.module.testModule;

import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.main.IModuleInfo;

/**
 * @author stephan
 * 24.06.2004 20:20:00
 */
public class ModuleInfo implements IModuleInfo {
	private ImageDescriptor image;
	private String openfile;

	public ModuleInfo() {
		image = ImageDescriptor.createFromFile(null, "logo.png");
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

	public ImageDescriptor getLogo() {
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
