/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
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
 * Created on Apr 18, 2004
 */
 
package org.jalgo.main.util;

//import java.io.DataOutputStream;
import java.util.Collection;

import org.eclipse.draw2d.Panel;

/**
 * @author Cornelius Hald
 * @author Benjamin Scholz
 * @author Stephan Creutz
 */
public class Exporter {

	//private static String[] knownTxtConverters = { "txt_Lf", "txt_Cr", "txt_LfCr", "html" };
	//private static String[] knownGfxConverters = { "png", "jpg" };
	private IConverter converter;
	
	private Panel panel;

	public Exporter() {
	}

	public Exporter(Panel panel) {
		this.panel = panel;
	}

	public void setCanvas(Panel panel) {
	}

	/**
	 * @return Returns a Collection of Strings like "gif" or "txt"
	 */
	public Collection getFileFormats() {
		return null;
	}

	public void setConverter(IConverter converter) {
		this.converter = converter;
	}

	/**
	 * manages the whole export process
	 * calls: getData(), convert() and getFilename()
	 */
	public void export() {
	}
	
	/**
	 * sets the Canvas and calls export()
	 */
	public void export(Panel panel) {
			this.panel = panel;
	}

	/**
	 * sets the Canvas, and calls setConverter and export
	 * @param converter
	 */
	public void export(Panel panel, IConverter converter) {
			this.panel = panel;
	}

	/*
	private void getData() {
	}
	*/

	/**
	 * @return Returns converted DataStream. This Stream may be written to harddisk.
	 */
	/*
	private DataOutputStream convert() {
		//IConverter converter;  either txt or gfxConverter will be created
		return null;
	}
	
	private String getFilename() {
		// opens the Filechooser Dialog
		return null;
	}
	*/

}
