/*
 * Created on 13.06.2004
 */

package org.jalgo.main.gfx;

import java.util.ArrayList;

/**
 * A class that implements IClickAction can be called by the ClickCollector
 * which then informs it about a list of all clicked objects.
 * 
 * @author Hauke Menges
 */
public interface IClickAction {
	public void performAction(ArrayList items);

}
