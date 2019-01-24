/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

/*
 * Created on 12.05.2005
 * 
 */
package org.jalgo.module.dijkstra.util;

import org.jalgo.main.util.Messages;
import org.jalgo.module.dijkstra.model.Graph;
import org.jalgo.module.dijkstra.model.Node;


/**
 * The NodeListParser class defines the functionality to create and modify a
 * graph by a node list.
 * 
 * @author Hannes Straß
 */
public class NodeListParser {

	private String nodeList;
	private Graph newGraph;
	private Graph retGraph;

	/**
	 * Creates a NodeListParser.
	 */
	public NodeListParser() {
		nodeList = ""; //$NON-NLS-1$
		newGraph = new Graph();
	}

	private boolean setNodeList(String newNodeList) {
		nodeList = newNodeList.trim(); // remove leading and trailing space
		// characters after each edit
		return nodeList.equals(""); //$NON-NLS-1$
	}

	/**
	 * Parses given Node list and adds Nodes and Edges to given Graph.
	 * 
	 * @param nodeListToParse Node list to parse
	 * @param graphToFill graph, to which specified Nodes are added
	 * @return changed graph
	 * @throws ParsingException
	 */
	public Graph getParsedNodeList(String nodeListToParse, Graph graphToFill)
	throws ParsingException {

		retGraph = graphToFill;
		setNodeList(nodeListToParse);

		/*
		 * enterNodeList(); only needed, if Node list must start with "{"
		 */

		// get first Node if nodeList isn't empty
		if (!nodeList.equals("")) getNode(); // //$NON-NLS-1$

		while (nodeList.startsWith(",")) { //$NON-NLS-1$
			nextNode();
			getNode(); // creation of Node-object
		}
		/*
		 * leaveNodeList(); only needed, if Node list must end with "}"
		 */

		if (nodeList.equals("")) { //$NON-NLS-1$
			// only new elements should have the changed-flag
			retGraph.setAllChangedFlagsFalse();
			newGraph.rescale();
			retGraph.synchronizeWith(new Graph(
				newGraph.getNodeList(), retGraph.getEdgeList()), true);

			retGraph.replaceMissingNodes();

			return retGraph;
		}
		throw new ParsingException(Messages.getString(
			"dijkstra", "NodeListParser.Syntax_error_1") + nodeList + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	private void enterNodeList()
	throws ParsingException {
		if (nodeList.startsWith("{")) setNodeList(nodeList.substring(1)); //$NON-NLS-1$
		else throw new ParsingException(Messages.getString(
			"dijkstra", "NodeListParser.Syntax_hint_1")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private void leaveNodeList()
	throws ParsingException {
		if (nodeList.startsWith("}")) setNodeList(nodeList.substring(1)); //$NON-NLS-1$
		else throw new ParsingException(Messages.getString(
			"dijkstra", "NodeListParser.Syntax_hint_2")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private void getNode()
	throws ParsingException {
		if (nodeList.equals("")) throw new ParsingException( //$NON-NLS-1$
			Messages.getString("dijkstra", "NodeListParser.Syntax_error_2")); //$NON-NLS-1$ //$NON-NLS-2$

		String currentNode = nodeList.substring(0, 1);
		setNodeList(nodeList.substring(1));

		if (currentNode.matches("[1-9]")) //$NON-NLS-1$
			newGraph.addNode(
				new Node((Integer.valueOf(currentNode, 10)).intValue()));
		else throw new ParsingException(Messages.getString(
			"dijkstra", "NodeListParser.Syntax_error_3") + currentNode //$NON-NLS-1$ //$NON-NLS-2$
			+ Messages.getString("dijkstra", "NodeListParser.Syntax_hint_3")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private void nextNode()
	throws ParsingException {
		if (nodeList.startsWith(",")) setNodeList(nodeList.substring(1)); //$NON-NLS-1$
		else throw new ParsingException(Messages.getString(
			"dijkstra", "NodeListParser.Syntax_hint_4")); //$NON-NLS-1$ //$NON-NLS-2$
	}
}