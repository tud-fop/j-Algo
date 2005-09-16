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
 * Created on 25.05.2004
 */
 
package org.jalgo.main;

import org.jalgo.main.util.ErrorLog;

/**
 * @author Christopher Friedrich
 */
public class Jalgo {

	private static JalgoMain jalgo;

	/**
	 * When releasing the product, start the program with the flag "errorLogOn".
	 * So an error log file could be created for easy debugging.
	 * 
	 * @param args the program arguments
	 * 
	 * @see ErrorLog
	 */
	public static void main(String[] args) {
		//saves exceptions to file
		ErrorLog errorLog = null;
		if (args.length > 0 && args[0].equalsIgnoreCase("errorlogon"))
			errorLog = new ErrorLog();

		jalgo = new JalgoMain();
		jalgo.createGUI();
		
		if (errorLog != null) errorLog.close();
	}

	public static AbstractModuleConnector getCurrentModule() {
		return jalgo.getCurrentInstance();
	}

	public static JalgoMain getJalgoMain() {
		return jalgo;
	}

}
