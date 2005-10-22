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


package org.jalgo.main.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/*
 * Created on Okt 15, 2005
 */

/**
 * Enables the work with a file implementing Input and OutputStreams.
 * 
 * @author Matthias Schmidt
 */
public class FileActivity {
	
	/**
	 * Stores a <code>boolean</code> into a file.
	 * 
	 * @param file path and file name
	 * @param value <code>boolean</code> to be stored
	 */
	public static void writeBooleanTo(String file, boolean value){
		try{
			FileOutputStream iniOut = new FileOutputStream(file);
			ObjectOutputStream valueOut = new ObjectOutputStream(iniOut);
			valueOut.writeBoolean(value);
			valueOut.close();
			iniOut.close();		
		}
		catch(IOException ex){
			System.err.println(ex);
		}
	}
	
	/**
	 * Retrieves a <code>boolean</code> from a file.
	 * 
	 * @param file path and file name
	 * @return the stored value
	 */
	public static boolean readBooleanFrom(String file){
		try{
			boolean value;
			FileInputStream iniIn = new FileInputStream(file);
			ObjectInputStream valueIn = new ObjectInputStream(iniIn);
			value = valueIn.readBoolean();
			valueIn.close();
		 	iniIn.close();
		 	return value;
		}
		catch(FileNotFoundException ex){
			System.out.println("file was not found!");
			return false;
		}
		catch(IOException ex){
			System.out.println("file couldnot be read!");
			return false;
		}

	}
}