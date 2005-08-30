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
 * Created on 12.05.2005
 *
 */
package org.jalgo.module.dijkstraModule.util;
import org.jalgo.module.dijkstraModule.model.Graph;
import org.jalgo.module.dijkstraModule.model.Node;
/**
 * The NodeListParser class defines the functionality to create and modify a graph by a node list.
 * 
 * @author Hannes Stra"s
 *
*/
public class NodeListParser
{
	private String nodeList;
	private Graph newGraph;
	private Graph retGraph;
	
	/**
	 * Creates a NodeListParser. 
	 */
	public NodeListParser()
	{
		nodeList = "";
		newGraph = new Graph();
	}
	
	private boolean setNodeList(String newNodeList)
	{
		nodeList = newNodeList.trim(); // remove leading and trailing space characters after each edit
		return nodeList.equals("");
	}

	/** Parses given Node list and adds Nodes and Edges to given Graph.
	 * @param nodeListToParse Node list to parse
	 * @param graphToFill graph, to which specified Nodes are added
	 * @return changed graph
	 * @throws ParsingException
	 */
	public Graph getParsedNodeList(String nodeListToParse, Graph graphToFill) throws ParsingException
	{
		
		retGraph = graphToFill;
		setNodeList(nodeListToParse);
		
		/*	enterNodeList();
		 * 	only needed, if Node list must start with "{"
		 */
		
		if (!nodeList.equals(""))getNode(); // get first Node if nodeList isn't empty
		
		while(nodeList.startsWith(","))
		{
			nextNode();
			getNode(); // creation of Node-object
		}
		/*	leaveNodeList();
		 * 	only needed, if Node list must end with "}"
		 */
		
		if (nodeList.equals(""))
		{
			retGraph.setAllChangedFlagsFalse(); // only new elements should have the changed-flag
			newGraph.rescale();
			retGraph.synchronizeWith(new Graph(newGraph.getNodeList(), retGraph.getEdgeList()), true);
			
			retGraph.replaceMissingNodes();
		
			return retGraph;
		}
		throw new ParsingException("Knotenliste: Syntaxfehler im Eingabestring: \"" + nodeList + "\"");
	}
	
	private void enterNodeList() throws ParsingException
	{
		if (nodeList.startsWith("{"))
		{
			setNodeList(nodeList.substring(1));
		}
		else
		{
			throw new ParsingException("Knotenliste muss mit \"{\" beginnen!");
		}
	}
	
	private void leaveNodeList() throws ParsingException
	{
		if (nodeList.startsWith("}"))
		{
			setNodeList(nodeList.substring(1));
		}
		else
		{
			throw new ParsingException("Knotenliste muss auf \"}\" enden!");
		}
	}
	
	private void getNode() throws ParsingException
	{
		if (nodeList.equals(""))
		{
			throw new ParsingException("Unerwartetes Ende des Eingabestrings! Erwartet: Knoten.");
		}
		
		String currentNode = nodeList.substring(0, 1);
		setNodeList(nodeList.substring(1));
		
		
		if (currentNode.matches("[1-9]"))
		{
			newGraph.addNode(new Node((Integer.valueOf(currentNode, 10)).intValue()));
		}
		else
		{
			throw new ParsingException("Kein zulässiger Knoten: " + currentNode + "! Nur Dezimalzahlen von 1 - 9 erlaubt.");
		}
	}
	
	private void nextNode() throws ParsingException
	{
		if (nodeList.startsWith(","))
		{
			setNodeList(nodeList.substring(1));
		}
		else
		{
			throw new ParsingException("Knoten müssen mit \",\" getrennt werden!");
		}
	}

}
