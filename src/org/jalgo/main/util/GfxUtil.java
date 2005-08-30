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
 * Created on 08.06.2004
 */
 
package org.jalgo.main.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Hauke Menges
 * @author Cornelius Hald
 * @author Babett Schaliz
 * @author Marco Zimmerling
 */
public class GfxUtil {

	/**
	 * Returns a collection of all incoming (source) connections of a node/figure.
	 * @param panel The <code>Figure</code> that contains the node.
	 * @param node The <code>Figure</code> in which connections you are interested in.
	 * @return A collection of all incoming (source) connections of node.
	 */
	public static Collection getSourceConnections(Figure panel, Figure node) {
		List list = panel.getChildren();
		List<PolylineConnection> returnList = new ArrayList<PolylineConnection>();

		Iterator it = list.iterator();

		while (it.hasNext()) {
			Object obj = it.next();
			if (obj instanceof PolylineConnection) {
				if (node.equals(((PolylineConnection) obj).getTargetAnchor().getOwner())) {
					returnList.add((PolylineConnection)obj);
				}
			}
		}

		return returnList;
	}

	/**
	 * Returns a collection of all outgoing (target) connections of a node/figure.
	 * @param panel The <code>Figure</code> that contains the node.
	 * @param node The <code>Figure</code> in which connections you are interested in.
	 * @return A collection of all outgoing (target) connections of node.
	 */
	public static Collection getTargetConnections(Figure panel, Figure node) {
		List list = panel.getChildren();
		List<PolylineConnection> returnList = new ArrayList<PolylineConnection>();

		Iterator it = list.iterator();

		while (it.hasNext()) {
			Object obj = it.next();
			if (obj instanceof PolylineConnection) {
				if (node.equals(((PolylineConnection) obj).getSourceAnchor().getOwner())) {
					returnList.add((PolylineConnection)obj);
				}
			}
		}

		return returnList;
	}

	/**
	 * Returns a collection of all outgoing (target) connections of a node/figure.
	 * @param panel The <code>Figure</code> that contains the node.
	 * @param sourceNode The <code>Figure</code> in which connections you are interested in.
	 * @return A collection of all outgoing (target) connections of node.
	 */
	public static PolylineConnection getConnection(Figure panel, Figure sourceNode, Figure targetNode) {
		//TODO eventuell auch mehrere Connections zwischen 2 Knoten -> Anpassen!!!!
		List list = panel.getChildren();
		
		Iterator it = list.iterator();

		while (it.hasNext()) {
			Object obj = it.next();
			if (obj instanceof PolylineConnection) {
				if (		sourceNode.equals(((PolylineConnection) obj).getSourceAnchor().getOwner())
					&&	targetNode.equals(((PolylineConnection) obj).getTargetAnchor().getOwner())) {
					return (PolylineConnection)obj;
				}
			}
		}

		return null;
	}
	
	private static Shell appShell;
	
	public static Shell getAppShell(){
		return appShell;
	}
	
	public static void setAppShell(Shell appShell_){
		appShell = appShell_;
	}
	
}
