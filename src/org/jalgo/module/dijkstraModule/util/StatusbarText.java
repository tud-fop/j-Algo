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
 * Created on 06.06.2005
 *

 */
package org.jalgo.module.dijkstraModule.util;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;

/**
 * @author Frank Staudinger
 * This class is used to provide styled text to the statusbar pane.
 */
public class StatusbarText
{
	protected StyleRange[] m_styles = null;
	protected String m_strText;
	public StatusbarText(String strText, StyleRange[] styles)
	{
		m_styles = styles;
		m_strText = strText;
	}
	
	public void setText(StyledText text)
	{
		text.setText(m_strText);
		if(m_styles != null)
			text.setStyleRanges(m_styles);
	}

}