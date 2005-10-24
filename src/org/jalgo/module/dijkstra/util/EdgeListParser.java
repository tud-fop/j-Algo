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
 * Created on 07.05.2005
 *
 */
package org.jalgo.module.dijkstra.util;
import org.jalgo.module.dijkstra.model.Edge;
import org.jalgo.module.dijkstra.model.Graph;
import org.jalgo.module.dijkstra.model.Node;
/**
 * The EdgeListParser class defines the functionality to create and modify a graph by an edge list.
 * 
 * @author Hannes Stra"s
 *
*/
public class EdgeListParser
{
	private String edgeList;
	private Graph newGraph;
	private Graph retGraph;
	
	/** 
	 *  Creates an EdgeListParser.
	 */
	public EdgeListParser()
	{
		edgeList = "";
		newGraph = new Graph();
	}

	private boolean setEdgeList(String newEdgeList)
	{
		edgeList = newEdgeList.trim(); // remove leading and trailing space characters after each edit
		return edgeList.equals("");
	}

	/** Parses given Edge list and adds Nodes and Edges specified in list to given Graph.
	 * @param edgeListToParse the Edge list to parse
	 * @param graphToFill the graph, to which new elements are added
	 * @return The given graph containing new elements as specified in edgeListToParse
	 * @throws ParsingException
	 */
	public Graph getParsedEdgeList(String edgeListToParse, Graph graphToFill) throws ParsingException
	{
		retGraph = (Graph)graphToFill.clone();
		setEdgeList(edgeListToParse);
		
		/*	enterEdgeList();
		 *  only needed if Edge list must start with "{"
		 */
		while (edgeList.startsWith("("))
		{
			enterEdge();
			parseEdge();
			leaveEdge();
			if (edgeList.startsWith(","))
			{
				nextEdge();
			}
		}
		/*	leaveEdgeList();
		 * 	only needed if Edge list must end with "}"
		 */
		
		if (edgeList.equals(""))
		{
			newGraph.rescale();
			retGraph.setAllChangedFlagsFalse(); // only new elements should have the changed-flag
			retGraph.synchronizeWith(newGraph, false);
			retGraph.replaceMissingNodes();	// 
			
			newGraph.reset();
			return retGraph;
		}
		throw new ParsingException("Kantenliste: Syntaxfehler im Eingabestring: " + edgeList + "!");
	}
	
	private void parseEdge() throws ParsingException
	{
		String start, end, weight;
		Node startNode, endNode;
		Edge edge;
		
		start = getStartNode();
		nextNode();
		weight = getWeight();
		nextNode();
		end = getEndNode();
		
		/**
		 * check if all node indexes are numbers between 1 and 9 using regular expressions
		 */
		if (
				start.matches("[1-9]")
				&&
				end.matches("[1-9]")
				&&
				weight.matches("[0-9][0-9]*")
			)
		{
			startNode = new Node((Integer.valueOf(start, 10)).intValue()); // creating "int"s from "String"s to the base 10
			endNode = new Node((Integer.valueOf(end, 10)).intValue());
			edge = new Edge(startNode, endNode, (Integer.valueOf(weight, 10)).intValue());

			newGraph.addNode(startNode);
			newGraph.addNode(endNode);
			newGraph.addEdge(edge);
		}
		else
		{
			// error handling
			throw new ParsingException("Syntaxfehler in der Kantenliste: (" + start + ", " + weight + ", " + end + "). Erwartet: (Startknoten, Gewicht, Endknoten).");
		}
}
	
	private void enterEdgeList() throws ParsingException
	{
		if (edgeList.startsWith("{"))
		{
			setEdgeList(edgeList.substring(1));
		}
		else
		{
			throw new ParsingException("Kantenliste muss mit \"{\" beginnen!");
		}
	}
	
	private void leaveEdgeList() throws ParsingException
	{
		if (edgeList.startsWith("}"))
		{
			setEdgeList(edgeList.substring(1));
		}
		else
		{
			throw new ParsingException("Kantenliste muss auf \"}\" enden!");
		}
	}
	
	private void enterEdge() throws ParsingException
	{
		if (edgeList.startsWith("("))
		{
			setEdgeList(edgeList.substring(1));
		}
		else
		{
			throw new ParsingException("Kante muss mit \"(\" beginnen!");
		}
	}
	
	private void leaveEdge() throws ParsingException
	{
		if (edgeList.startsWith(")"))
		{
			setEdgeList(edgeList.substring(1));
		}
		else
		{
			throw new ParsingException("Kante muss auf \"(\" enden!");
		}
	}
	
	private void nextEdge() throws ParsingException
	{
		if (edgeList.startsWith(","))
		{
			setEdgeList(edgeList.substring(1));
		}
		else
		{
			throw new ParsingException("Kanten müssen mit \",\" getrennt werden!");
		}
	}
	
	private void nextNode() throws ParsingException
	{
		if (edgeList.startsWith(","))
		{
			setEdgeList(edgeList.substring(1));
		}
		else
		{
			throw new ParsingException("Einzelne Elemente innerhalb der Kante müssen mit \",\" getrennt werden!");
		}
	}
	
	private String getStartNode() throws ParsingException
	{
		if (edgeList.equals(""))
		{
			throw new ParsingException("Unerwartetes Ende des Eingabestrings! Erwartet: Startknoten.");
		}
		
		String n = edgeList.substring(0, 1);
		setEdgeList(edgeList.substring(1));
		
		return n;
	}
	
	private String getEndNode() throws ParsingException
	{
		if (edgeList.equals(""))
		{
			throw new ParsingException("Unerwartetes Ende des Eingabestrings! Erwartet: Endknoten.");
		}
		
		String n = edgeList.substring(0, 1);
		setEdgeList(edgeList.substring(1));
		
		return n;
	}
	
	private String getWeight()
	{
		String n = "";
		
		while (!edgeList.startsWith(",") && edgeList.length() > 0)
		{
			n = n.concat(edgeList.substring(0, 1)); // weight can be longer than one character
			setEdgeList(edgeList.substring(1));
		}
		
		return n;
	}

}
