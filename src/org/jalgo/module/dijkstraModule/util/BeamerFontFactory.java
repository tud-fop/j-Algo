/**
 * @author Frank Staudinger
 * 
 * Created on 09.06.2005 17:49:26
 *
 */
package org.jalgo.module.dijkstraModule.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

/**
 * @author Frank
 *
 */
public class BeamerFontFactory
{
    public static Font getFont()
    {
        return new Font(Display.getCurrent(), "Verdana", 14, SWT.BOLD);
    }
}
