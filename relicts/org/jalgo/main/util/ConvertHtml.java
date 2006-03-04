/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and
 * platform independant. j-Algo is developed with the help of Dresden
 * University of Technology.
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
 * Created on 23.04.2004
 */

package org.jalgo.main.util;

import java.util.regex.Pattern;

/**
 * @author Benjamin Scholz
 * @author Stephan Creutz
 */
public class ConvertHtml implements ITxtConvert {
	
	public String convert(String text) {
		text = Pattern.compile("&").matcher(text).replaceAll("&amp;");
		text = Pattern.compile("<").matcher(text).replaceAll("&lt;");
		text = Pattern.compile(">").matcher(text).replaceAll("&gt;");
		text = Pattern.compile("\"").matcher(text).replaceAll("&quot;");
		text = Pattern.compile("ä").matcher(text).replaceAll("&auml;");
		text = Pattern.compile("Ä").matcher(text).replaceAll("&Auml;");
		text = Pattern.compile("ö").matcher(text).replaceAll("&ouml;");
		text = Pattern.compile("Ö").matcher(text).replaceAll("&Ouml;");
		text = Pattern.compile("ü").matcher(text).replaceAll("&uuml;");
		text = Pattern.compile("Ü").matcher(text).replaceAll("&Uuml;");
		text = Pattern.compile("ß").matcher(text).replaceAll("&szlig;");
		return "<html><head><title></title></head>\n<body>\n"
			+ text
			+ "\n</body></html>";
	}

}