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
		String currentNode;
		
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
		else
		{
			throw new ParsingException("Knotenliste: Syntaxfehler im Eingabestring: \"" + nodeList + "\"");
		}
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
