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
 * Created on 13.06.2004
 */
 
package org.jalgo.main.util;

import java.util.ArrayList;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.geometry.Point;
import org.jalgo.main.gfx.ClickListener;
import org.jalgo.main.gfx.IClickAction;

/**
 * The ClickCollector collects figures that are reported by a figures 
 * ClickListener.
 * It receives a list of numberOfClicks swt-figures and the last
 * position where a click occured. 
 * It then calls actionObject.performAction(items).
 * 
 * @author Hauke Menges
 */
public class ClickCollector {
	
	private static boolean isCollecting;
	private static int numberOfClicks;
	private static IClickAction<Figure> actionObject;
	private static ArrayList<Figure> items;
	
	private static Point lastPoint;
	
	public static void init(int numberOfClicks_, IClickAction actionObject_) {
		isCollecting = true;
		numberOfClicks = numberOfClicks_;
		actionObject = actionObject_;
		items = new ArrayList<Figure>();
	}
	
	public static boolean addItem(ClickListener clickListener) {
		if ((isCollecting == true) && (numberOfClicks > 0)) {
			items.add(clickListener.getFigure());
			lastPoint = clickListener.getLastPoint();
			numberOfClicks--;
			if (numberOfClicks == 0) {
				actionObject.performAction(items);
				stopCollecting();
				return true;
			}
		}
		return false;
	}
	
	public static void stopCollecting() {
		isCollecting = false;
		numberOfClicks = 0;
		actionObject = null;
		items = null;
	}

	public static Point getLastPoint(){
		return lastPoint;
	}
	
	public static boolean isCollecting(){
		return isCollecting;
	}
}
