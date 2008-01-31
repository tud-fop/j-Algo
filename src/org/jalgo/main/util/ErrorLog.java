/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer science. It is written in Java and platform independent. j-Algo is developed with the help of Dresden University of Technology.
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

/* Created on 11.06.2005 */
package org.jalgo.main.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

/**
 * The class <code>ErrorLog</code> is a helper class for easy debugging, when the
 * program is running at the users computer. It writes system properties and
 * occured exceptions in a textfile. The file is created when the module starts and
 * is successive filled with exceptions. If there were no exceptions during runtime,
 * the file is deleted from the disk. That mechanism guarantees, that only useful
 * error logfiles are stored.
 * These logfiles the user can send to the programmers to fix the bugs, if there
 * were any...
 * 
 * @author Alexander Claus
 */
public class ErrorLog {

	private PrintStream stderr;
	private FileOutputStream out;
	private String fileName;
	private static final String lineSep = System.getProperty("line.separator");
	private static final String testString = "Error Log"+lineSep+lineSep;
	private boolean writeProtected = false;

	/**
	 * Constructs an <code>ErrorLog</code> object. The standard error stream is
	 * redirected to a file "error\<TimeStamp\>.log" and the available system
	 * properties are written to the file.
	 * Finally a well-defined string is written, which can be used to determine,
	 * if an exception occured.
	 */
	public ErrorLog() {
		try {
			fileName = "error"+System.currentTimeMillis()+".log";
			out = new FileOutputStream(fileName);
			stderr = System.err;
			System.setErr(new PrintStream(out));
			System.err.println("Properties:");
			System.err.println();
			for (Map.Entry property : System.getProperties().entrySet()) {
				System.err.println(property);
			}
			System.err.println();
			System.err.print(testString);
		}
		catch (FileNotFoundException e) {
			System.err.println("Konnte keine Logdatei anlegen...");
			writeProtected = true;
		}
	}

	/**
	 * Closes the created logfile and redirects the standard error stream back.
	 * Tests if there were exceptions occured, and if not, deletes the logfile.
	 * (If the program starts in write protected mode, e.g. from cd, there is
	 * nothing to close...)
	 */
	public void close() {
		if (writeProtected) return;
		try {
			out.close();
			File log = new File(fileName);
			FileReader testReader = new FileReader(log);
			testReader.skip(log.length()-testString.length());
			char[] streamEnd = new char[testString.length()];
			testReader.read(streamEnd);
			testReader.close();
			if (String.valueOf(streamEnd).equals(testString)) log.delete();
			System.setErr(stderr);
		}
		catch (IOException e) {e.printStackTrace();}
	}
}