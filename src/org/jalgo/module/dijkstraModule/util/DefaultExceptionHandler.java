/*
 * Created on 30.05.2005 09:24:47
 *
  */
package org.jalgo.module.dijkstraModule.util;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

/**
 * @author Frank
 *
 */

public class DefaultExceptionHandler 
{
	
	public DefaultExceptionHandler(Exception e)
	{
	    e.printStackTrace();
		MessageBox fault =new MessageBox(Display.getCurrent().getActiveShell());
		if (e.getMessage() == null)
		{
		    return;
			
		}
		fault.setMessage(e.getMessage());
		fault.open();
	}
}
