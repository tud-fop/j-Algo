/*
 * Created on 19.04.2004
 */
 
package org.jalgo.main.util;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.jalgo.main.IModuleConnector;
import org.jalgo.main.Jalgo;

/**
 * @author Hauke Menges
 */
public class Storage {

	public static boolean load(String filename) {
		return load(filename, null);
	}

	public static boolean load(
		String filename,
		IModuleConnector currentInstance) {
		byte[] buf = null;

		// Create InputStream
		FileInputStream in = null;
		try {
			in = new FileInputStream(filename);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			// is it a valid jalgo-file?
			buf = new byte[new String("jalgo").getBytes().length]; //$NON-NLS-1$
			in.read(buf, 0, new String("jalgo").getBytes().length); //$NON-NLS-1$

			if (!(new String(buf).equals("jalgo"))) { //$NON-NLS-1$
				Shell shell = new Shell();
				MessageDialog.openError(
					shell,
					Messages.getString("Storage.Could_not_open_file_4"), //$NON-NLS-1$
					Messages.getString("Storage.File_not_valid_5")); //$NON-NLS-1$
				shell.dispose();
				return false;
			}
			buf = null;

			// Get Header length
			int nameLength = in.read();
			int versionLength = in.read();

			// Read Header	
			buf = new byte[nameLength];
			in.read(buf, 0, nameLength);
			String name = new String(buf);
			buf = new byte[versionLength];
			in.read(buf, 0, versionLength);
			String version = new String(buf);
			buf = null;

			if (currentInstance == null)
				currentInstance =
					Jalgo.getJalgoMain().newInstance(name + " " + version, false); //$NON-NLS-1$

			// Read module data
			buf = new byte[in.available()];
			in.read(buf, 0, in.available());

			// give outStream to Module
			ByteArrayInputStream outStream = new ByteArrayInputStream(buf);
			currentInstance.setDataFromFile(outStream);

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return true;
	}

	public static boolean save(String filename) {

		// Generate Headers
		int nameLength =
			Jalgo
				.getCurrentModule()
				.getModuleInfo()
				.getName()
				.getBytes()
				.length;
		int versionLength =
			Jalgo
				.getCurrentModule()
				.getModuleInfo()
				.getVersion()
				.getBytes()
				.length;

		// Create OutputStream
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filename);

		} catch (FileNotFoundException e) {
			Shell shell = new Shell();
			MessageDialog.openError(
				shell,
				Messages.getString("Storage.Could_not_write_file_7"), //$NON-NLS-1$
				Messages.getString("Storage.Could_not_write_the_file._n_8") + //$NON-NLS-1$				Messages.getString("Storage.Maybe_out_of_diskspace_9")); //$NON-NLS-1$

			e.printStackTrace();
		}

		try {
			// Write Header
			out.write((new String("jalgo")).getBytes()); //$NON-NLS-1$
			out.write(nameLength);
			out.write(versionLength);
			out.write(
				Jalgo.getCurrentModule().getModuleInfo().getName().getBytes());
			out.write(
				Jalgo
					.getCurrentModule()
					.getModuleInfo()
					.getVersion()
					.getBytes());

			// Write module data
			out.write(Jalgo.getCurrentModule().getDataForFile().toByteArray());

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return true;
	}
}
