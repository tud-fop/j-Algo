/*
 * Created on 02.06.2004
 */
 
package org.jalgo.main;

import java.io.FileWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Michael Pradel
 *
 */
public class InternalErrorException extends IllegalArgumentException {  

	public InternalErrorException(String s) {

		//super(s);
		try {
			FileWriter errorFile = new FileWriter("error.log", true);  // append to file //$NON-NLS-1$
			Calendar calendar = new GregorianCalendar();
			errorFile.write(calendar.getTime() +  ": " + s + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
			errorFile.close();
		} catch (Exception e) {
			System.err.println(e);
		}

	}

}
