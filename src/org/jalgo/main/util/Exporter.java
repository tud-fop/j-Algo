/*
 * Created on Apr 18, 2004
 */
 
package org.jalgo.main.util;

import java.io.DataOutputStream;
import java.util.Collection;

import org.eclipse.draw2d.Panel;

/**
 * @author Cornelius Hald
 * @author Benjamin Scholz
 * @author Stephan Creutz
 */
public class Exporter {

	private static String[] knownTxtConverters = { "txt_Lf", "txt_Cr", "txt_LfCr", "html" };
	private static String[] knownGfxConverters = { "png", "jpg" };
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

	public void setConverter(IConverter converter){
	}

	/**
	 * manages the whole export process
	 * calls: getData(), convert() and getFilename()
	 */
	public void export() {
	}
	
	/**
	 * sets the Canvas and calls export()
	 * @param algoCanvas
	 */
	public void export(Panel panel) {
			this.panel = panel;
	}

	/**
	 * sets the Canvas, and calls setConverter and export
	 * @param algoCanvas
	 * @param converter
	 */
	public void export(Panel panel, IConverter converter) {
			this.panel = panel;
	}

	private void getData() {
	}

	/**
	 * @return Returns converted DataStream. This Stream may be written to harddisk.
	 */
	private DataOutputStream convert() {
		IConverter converter; // either txt or gfxConverter will be created

		return null;
	}
	
	private String getFilename() {
		// opens the Filechooser Dialog
		return null;
	}


}
