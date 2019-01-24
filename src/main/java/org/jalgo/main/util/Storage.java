/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

/*
 * Created on 19.04.2004
 */

package org.jalgo.main.util;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.jalgo.main.AbstractModuleConnector;
import org.jalgo.main.IModuleInfo;
import org.jalgo.main.JAlgoMain;
import org.jalgo.main.gui.JAlgoGUIConnector;


/**
 * @author Hauke Menges, Michael Pradel
 */
public class Storage {

	public static boolean load(String filename,
		AbstractModuleConnector currentInstance) {
		byte[] buf = null;

		// Create InputStream
		FileInputStream in = null;
		try {
			in = new FileInputStream(filename);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			// is it a valid jalgo-file?
			buf = new byte["jalgo".getBytes().length]; //$NON-NLS-1$
			in.read(buf, 0, "jalgo".getBytes().length); //$NON-NLS-1$
			if (!(new String(buf).equals("jalgo"))) { //$NON-NLS-1$
				JAlgoGUIConnector.getInstance().showErrorMessage(
					Messages.getString("main", "Storage.Could_not_open_file_4") + //$NON-NLS-1$
					System.getProperty("line.separator") +
					Messages.getString("main", "Storage.File_not_valid_5")); //$NON-NLS-1$
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
			buf = null;

			// get modNumber of module corresponding to this file
			int modNumber = -1;
			List moduleInfos =
				JAlgoMain.getInstance().getKnownModuleInfos();
			for (int i = 0; i < moduleInfos.size(); i++) {
				if (((IModuleInfo)moduleInfos.get(i)).getName().equals(name))
					modNumber = i;
			}
			if (modNumber < 0) {
				JAlgoGUIConnector.getInstance().showErrorMessage(
					Messages.getString("main", "Storage.Could_not_open_file_4") + //$NON-NLS-1$
					System.getProperty("line.separator") +
					Messages.getString("main", "Storage.Module_not_present")); //$NON-NLS-1$
				return false;
			}

			if (currentInstance == null)
				currentInstance = JAlgoMain.getInstance().newInstance(modNumber); //$NON-NLS-1$

			// Read module data
			buf = new byte[in.available()];
			in.read(buf, 0, in.available());

			// give outStream to Module
			ByteArrayInputStream outStream = new ByteArrayInputStream(buf);
			if (currentInstance == null)
				throw new NullPointerException("currentInstance is null");
			currentInstance.setDataFromFile(outStream);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				in.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	public static boolean save(String filename) {
		// Generate Headers
		int nameLength = JAlgoMain.getInstance().getCurrentInstance().
			getModuleInfo().getName().getBytes().length;
		int versionLength = JAlgoMain.getInstance().getCurrentInstance().
			getModuleInfo().getVersion().getBytes().length;

		// Create OutputStream
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filename);
		}
		catch (FileNotFoundException e) {
			JAlgoGUIConnector.getInstance().showErrorMessage(
				Messages.getString(
					"main", "Storage.Could_not_write_the_file._8") + //$NON-NLS-1$
					System.getProperty("line.separator") +
					Messages.getString("main", "Storage.Maybe_out_of_diskspace_9")); //$NON-NLS-1$
			e.printStackTrace();
		}

		try {
			// Write Header
			out.write("jalgo".getBytes()); //$NON-NLS-1$
			out.write(nameLength);
			out.write(versionLength);
			out.write(JAlgoMain.getInstance().getCurrentInstance().
				getModuleInfo().getName().getBytes());
			out.write(JAlgoMain.getInstance().getCurrentInstance().
				getModuleInfo().getVersion().getBytes());

			// Write module data
			out.write(JAlgoMain.getInstance().getCurrentInstance().
				getDataForFile().toByteArray());

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				out.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		return true;
	}
}