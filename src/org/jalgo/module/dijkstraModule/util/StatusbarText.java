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
	};
	
	public void setText(StyledText text)
	{
		text.setText(m_strText);
		if(m_styles != null)
			text.setStyleRanges(m_styles);
	}

};