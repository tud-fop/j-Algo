/*
 * Created on 13.06.2004
 */
 
package org.jalgo.main.util;

import java.util.ArrayList;

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
	private static IClickAction actionObject;
	private static ArrayList items;
	
	private static Point lastPoint;
	
	
	public static void init(int numberOfClicks_, IClickAction actionObject_){
		isCollecting = true;
		numberOfClicks = numberOfClicks_;
		actionObject = actionObject_;
		items = new ArrayList();
	}
	
	public static boolean addItem(ClickListener clickListener){
		if ((isCollecting == true) && (numberOfClicks > 0)){
			items.add(clickListener.getFigure());
			lastPoint = clickListener.getLastPoint();
			numberOfClicks--;
			if (numberOfClicks == 0){
				actionObject.performAction(items);
				stopCollecting();
				return true;
			}
		}
		return false;
	}
	
	public static void stopCollecting(){
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
