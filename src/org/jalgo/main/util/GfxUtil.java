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
	 * @param figure The <code>Figure</code> that contains the node.
	 * @param node The <code>Figure</code> in which connections you are interested in.
	 * @return A collection of all incoming (source) connections of node.
	 */
	public static Collection getSourceConnections(Figure panel, Figure node) {
		List list = panel.getChildren();
		List returnList = new ArrayList();

		Iterator it = list.iterator();

		while (it.hasNext()) {
			Object obj = it.next();
			if (obj instanceof PolylineConnection) {
				if (node.equals(((PolylineConnection) obj).getTargetAnchor().getOwner())) {
					returnList.add(obj);
				}
			}
		}

		return returnList;
	}

	/**
	 * Returns a collection of all outgoing (target) connections of a node/figure.
	 * @param figure The <code>Figure</code> that contains the node.
	 * @param node The <code>Figure</code> in which connections you are interested in.
	 * @return A collection of all outgoing (target) connections of node.
	 */
	public static Collection getTargetConnections(Figure panel, Figure node) {
		List list = panel.getChildren();
		List returnList = new ArrayList();

		Iterator it = list.iterator();

		while (it.hasNext()) {
			Object obj = it.next();
			if (obj instanceof PolylineConnection) {
				if (node.equals(((PolylineConnection) obj).getSourceAnchor().getOwner())) {
					returnList.add(obj);
				}
			}
		}

		return returnList;
	}

	/**
	 * Returns a collection of all outgoing (target) connections of a node/figure.
	 * @param figure The <code>Figure</code> that contains the node.
	 * @param node The <code>Figure</code> in which connections you are interested in.
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
