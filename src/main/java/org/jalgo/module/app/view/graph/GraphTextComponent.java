package org.jalgo.module.app.view.graph;

import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import org.jalgo.module.app.controller.GraphActionListener;
import org.jalgo.module.app.controller.GraphController;
import org.jalgo.module.app.controller.GraphObserver;
import org.jalgo.module.app.core.dataType.DataType;
import org.jalgo.module.app.core.graph.Edge;
import org.jalgo.module.app.core.graph.Graph;
import org.jalgo.module.app.core.graph.Node;
import org.jalgo.module.app.view.InterfaceConstants;

public class GraphTextComponent extends JPanel implements GraphObserver {
	private Graph graph;
	private GraphActionListener graphActionListener;
	
	private List<Object> mistakeHighlights;
	private List<Object> selectionHighlights;
	private Map<UpdateOperation, List<EdgeDescriptor>> actionMap;

	private List<EdgeDescriptor> currentEdgeDescriptors;
	private GraphController graphCtrl;
	
	private boolean beamerMode;
	
	private boolean isEditing;
	
	private class EdgeDescriptor {
		public DataType weight;
		public int start, end;

		public ParsedTuple parserInput;
				
		public EdgeDescriptor(int start, int end, DataType weight) {
			this.weight = weight;
			this.start = start;
			this.end = end;
		}

		public EdgeDescriptor(int start, int end, DataType weight, ParsedTuple parserInput) {
			this(start, end, weight);
			this.parserInput = parserInput;
		}		
		
		public boolean equals(Object o) {
			if ((o != null) && (o instanceof EdgeDescriptor)) {
				EdgeDescriptor edge = (EdgeDescriptor)o; 
				
				return (   (this.start == edge.start) 
						&& (this.end == edge.end)
				   	    );
			}
			
			return false;
		}
	}
	
	private class ParsedTuple {
		public boolean isMistake;
		public boolean seemsValid;
		
		public int startPos, endPos;
		
		public ParsedTuple() {
			isMistake = true;
			seemsValid = false;
			startPos = 0;
			endPos = 0;
		}
		
		public ParsedTuple(int startPos, int endPos) {
			isMistake = false;
			seemsValid = true;
			this.startPos = startPos;
			this.endPos = endPos;
		}
	}
	
	private enum UpdateOperation {
		NEW_EDGE,
		ALTER_WEIGHT,
		KEEP_EDGE,
		DELETE_EDGE
	}
	
	/**
	 *
	 */
	private static final long serialVersionUID = 3641739084413985176L;

	private JTextArea textArea;
	
	private Highlighter highlighter;
    private	Highlighter.HighlightPainter mistakePainter;
    private Highlighter.HighlightPainter selectionPainter;
	
	
	public GraphTextComponent(GraphController graphCtrl) {
		mistakeHighlights = new ArrayList<Object>();
		selectionHighlights = new ArrayList<Object>();
		currentEdgeDescriptors = new ArrayList<EdgeDescriptor>();
		this.graphCtrl = graphCtrl;
		
		beamerMode = false;
		
		this.setLayout(new GridLayout(1,1));
				
		highlighter = new DefaultHighlighter();
		mistakePainter = new UnderliningHighlightPainter(InterfaceConstants.wrongInputColor());
		selectionPainter = new BoxingHighlightPainter(InterfaceConstants.textViewEdgeLineColor(), InterfaceConstants.textViewEdgeFillColor());
		
		textArea = new JTextArea();
		JScrollPane scrollpane = new JScrollPane(textArea);
		
		textArea.setHighlighter(highlighter);
		textArea.setMargin(new Insets(3,3,3,3));
		textArea.getDocument().addDocumentListener(new TextAreaListener());
		textArea.addKeyListener(new TextAreaListener());
		
		textArea.addCaretListener(new TextAreaListener());
		
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setFont(InterfaceConstants.GraphTextComponentFont(beamerMode));
		
		this.add(scrollpane);
		
		isEditing = false;
		
	}

	public GraphActionListener getGraphActionListener() {
		return graphActionListener;
	}

	public void setGraphActionListener(GraphActionListener action_listener) {
		graphActionListener = action_listener;				
		graphActionListener.addGraphObserver(this);
		
		graph = graphActionListener.getGraph();
		
		initEdgeList();
	}
	
	public void setTextAreaFont(){
			textArea.setFont(InterfaceConstants.GraphTextComponentFont(beamerMode));
			repaint();
			revalidate();
	}

	/**
	 *  
	 */
	private String edgeToString(Edge edge)
	{
		String weight, start, end;
		
		weight = edge.getWeight().toString();
		start = Integer.toString(edge.getBegin().getId() + 1);
		end = Integer.toString(edge.getEnd().getId() + 1);
		
		return "("+start+","+weight+","+end+")";
	}
	
	/**
	 * 
	 */
	private EdgeDescriptor stringToEdge(String input)
	{
		int start, end;
		DataType weight;
		String[] split_input;
		
		// Initial parsing
		input = input.replaceAll("\\s", "");

		if (!input.startsWith("(") || !input.endsWith(")"))
			return null;

		input = input.substring(1, input.length() - 1);
		split_input = input.split(",");
		
		if (split_input.length != 3)
			return null;
				
		// Create new instance of DataType
		try {
			weight = this.graphActionListener.getDataType().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			/* FIXME */
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			
			/* FIXME */
			return null;
		}

		// Set and test values
		try {
			start = Integer.parseInt(split_input[0].trim());
			end = Integer.parseInt(split_input[2].trim());
		} catch (NumberFormatException e) {
			return null;
		}

		// Invalid node number
		if ((start == 0) || (end == 0))
			return null;
		
		// Syntax error in start / end values
		if (    (!Integer.toString(start).equals(split_input[0].trim()))
			 || (!Integer.toString(end).equals(split_input[2].trim()))
		   )
		{
			return null;
		}

		// Reflexive edges are not allowed
		if (start == end)
			return null;
		
		// Illegal weight
		if (weight.setFromString(split_input[1].trim()) == false) {
			return null;
		}
		
		return new EdgeDescriptor(start - 1, end - 1, weight);
	}
	
	/**
	 *  
	 *  
	 */
	private Edge getEdge(EdgeDescriptor descriptor) {
		
		for (Edge edge : graph.getEdges() ) {
			if (   edge.getBegin().getId() == descriptor.start 
				&& edge.getEnd().getId() == descriptor.end
			   )
			{
				return edge;
			}
		}
		
		return null;
	}
	
	/**
	 *
	 *
	 */
	private void determineAction(EdgeDescriptor descriptor) {
		List<EdgeDescriptor> operationList;
		Edge edge;
		
		edge = graph.getEdge(descriptor.start, descriptor.end);
		
		if (edge == null) {

			operationList = actionMap.get(UpdateOperation.NEW_EDGE);
			operationList.add(descriptor);
		}
		 else {
			if (edge.getWeight().equals(descriptor.weight)) {
				operationList = actionMap.get(UpdateOperation.KEEP_EDGE);
				operationList.add(descriptor);
			}
			else {
				operationList = actionMap.get(UpdateOperation.ALTER_WEIGHT);
				operationList.add(descriptor);
			}
		}
	}

	/**
	 * 
	 */
	private void calculateUnrequiredEdges() {
		List<EdgeDescriptor> operationList;
		
		for (Edge edge : graph.getEdges()) {
			EdgeDescriptor edgeDescriptor = new EdgeDescriptor(edge.getBegin().getId(), edge.getEnd().getId(), null);
			
			if (   !actionMap.get(UpdateOperation.KEEP_EDGE).contains(edgeDescriptor)
				&& !actionMap.get(UpdateOperation.ALTER_WEIGHT).contains(edgeDescriptor)
			   )
			{
				operationList = actionMap.get(UpdateOperation.DELETE_EDGE);
				operationList.add(edgeDescriptor);	
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private void createActionMap() {
		actionMap = new HashMap();
		
		actionMap.put(UpdateOperation.NEW_EDGE, new ArrayList<EdgeDescriptor>());
		actionMap.put(UpdateOperation.KEEP_EDGE, new ArrayList<EdgeDescriptor>());
		actionMap.put(UpdateOperation.ALTER_WEIGHT, new ArrayList<EdgeDescriptor>());
		actionMap.put(UpdateOperation.DELETE_EDGE, new ArrayList<EdgeDescriptor>());
	}
	
	private void addEdge(EdgeDescriptor descriptor) {
		while (graph.getNode(descriptor.start) == null) {
			graphActionListener.addNode();
		}

		while (graph.getNode(descriptor.end) == null) {
			graphActionListener.addNode();
		}
					
		graphActionListener.addEdge(graph.getNode(descriptor.start), graph.getNode(descriptor.end));
		graphActionListener.alterEdgeWeight(graph.getEdge(descriptor.start, descriptor.end), descriptor.weight);	
	}
	
	private void deleteEdge(EdgeDescriptor descriptor) {
		graphActionListener.removeEdge(graph.getEdge(descriptor.start, descriptor.end));
	}
	
	private void alterEdge(EdgeDescriptor descriptor) {
		graphActionListener.alterEdgeWeight(graph.getEdge(descriptor.start, descriptor.end), descriptor.weight);
	}
	
	/**
	 * 
	 */
	private void processActions() {
		List<EdgeDescriptor> operationList;
				
		graphActionListener.beginEditing();
		
		// Delete unneeded edges
		operationList = actionMap.get(UpdateOperation.DELETE_EDGE);
		for (EdgeDescriptor edge : operationList) {
			deleteEdge(edge);
		}
		
		// Create new edges
		operationList = actionMap.get(UpdateOperation.NEW_EDGE);
		for (EdgeDescriptor edge : operationList) {
			addEdge(edge);
		}		

		// Alter weight
		operationList = actionMap.get(UpdateOperation.ALTER_WEIGHT);
		for (EdgeDescriptor edge : operationList) {
			alterEdge(edge);
		}
		
		graphActionListener.endEditing();
	}
	
	/**
	 * 
	 * 
	 */
	private List<EdgeDescriptor> calculateInvalidEdges() {
		SortedSet<Integer> nodeIds, faultyIds;
		List<EdgeDescriptor> operationList, faultyEdges;
		
		int lastId, delta;
		
		faultyEdges = new ArrayList<EdgeDescriptor>();
		
		faultyIds = new TreeSet<Integer>();
		nodeIds = new TreeSet<Integer>();

		// Collect all node IDs
		for (UpdateOperation operation : UpdateOperation.values()) {
			if (operation == UpdateOperation.DELETE_EDGE)
				continue;
			
			operationList = actionMap.get(operation);
			for (EdgeDescriptor edge : operationList) {
				nodeIds.add(new Integer(edge.start));
				nodeIds.add(new Integer(edge.end));
			}
		}

		if (nodeIds.isEmpty())
			return faultyEdges;
		
		// Compute holes between node IDs and collect faulty node IDs 
		lastId = nodeIds.first() + 1;
		delta = 0;
		
		for (int nodeId : nodeIds) {
			delta += nodeId - lastId - 1;
			
			if (delta >= 10)
				faultyIds.add(new Integer(nodeId));
		}
		
		// Determine faulty edges
		for (UpdateOperation operation : UpdateOperation.values()) {
			if (operation == UpdateOperation.DELETE_EDGE)
				continue;
			
			operationList = actionMap.get(operation);
			for (EdgeDescriptor edge : operationList) {
				if (    faultyIds.contains(new Integer(edge.start))
					 || faultyIds.contains(new Integer(edge.end))
				   )
				{
					faultyEdges.add(edge);
				}
			}
		}
		
		return faultyEdges;
	}	

	
	
	/**
	 * 
	 * 
	 */
	private ParsedTuple findNextEdgeString(String list, int start) {
		ParsedTuple parsedTuple; 
		int nestedParenthesis, firstOpeningParenthesis, lastClosingParenthesis, lastAccepted;
		int pos;
		
		nestedParenthesis = 0;
		firstOpeningParenthesis = -1;
		lastClosingParenthesis = -1;
		
		parsedTuple = new ParsedTuple();
		parsedTuple.startPos = start;
		parsedTuple.endPos = start;
		parsedTuple.isMistake = true;
		parsedTuple.seemsValid = false;
		
		for (pos = start; pos < list.length(); pos ++) {
			parsedTuple.endPos = pos;
			
			// Ignore all white space charracters
			if (    list.charAt(pos) == ' '
				 || list.charAt(pos) == '\t'
			     || list.charAt(pos) == '\n'
			     || list.charAt(pos) == '\r'
			   )
				continue;
			
			if (list.charAt(pos) == '(') {

				// Found first parenthesis
				if (nestedParenthesis == 0) {
					
					// Missing ","
					if (firstOpeningParenthesis != -1) {
						parsedTuple.startPos = lastClosingParenthesis;
						
						return parsedTuple;
					}
					
					firstOpeningParenthesis = pos;
					parsedTuple.startPos = firstOpeningParenthesis;
				}
				
				nestedParenthesis ++;
			}
			
			// Found end of tuple
			if (list.charAt(pos) == ')') {
				nestedParenthesis --;
				
				if (nestedParenthesis == 0) {
					lastClosingParenthesis = pos;
				}
				
				// There are more parenthesis closing than opening
				if (nestedParenthesis < 0)
					return parsedTuple;
				else
					continue;
			}
			
			// Ignore all characters inside a tuple
			if (nestedParenthesis > 0)
				continue;
			
			// Found end of tuple
			if (   (list.charAt(pos) == ',') 
				&& (nestedParenthesis == 0)
			   ) 
			{
				parsedTuple.isMistake = false;
				parsedTuple.seemsValid = true;
				return parsedTuple;
			}
			
					
			// Invalid character outside a parenthesis, try to find next "("
			if (lastClosingParenthesis == -1)
				parsedTuple.startPos = start;
			else
				parsedTuple.startPos = lastClosingParenthesis;
			
			for (parsedTuple.endPos += 1; parsedTuple.endPos < list.length(); parsedTuple.endPos ++) {
				if (list.charAt(parsedTuple.endPos) == '(') 
					break;
			}
			
			return parsedTuple;
		}
		
		parsedTuple.endPos = pos;
				
		// Input is only valid, if all parenthesis are closing
		if (nestedParenthesis == 0) {
			parsedTuple.seemsValid = true;
			parsedTuple.isMistake = false;
		}
		
		return parsedTuple;
	}
	
	/**
	 * 
	 */
	private void highlightText(ParsedTuple parserOutput, HighlightPainter painter, List<Object> highlightList) {
		int startPos = parserOutput.startPos;
		int endPos = parserOutput.endPos;
		
		try {
			highlightList.add(highlighter.addHighlight(startPos, endPos, painter));
		}
	 	 catch (BadLocationException e) {
	 		 return;
	 	 }
	}
	
	private void highlightMistake(ParsedTuple parserOutput) {
		highlightText(parserOutput, mistakePainter, mistakeHighlights);
	}

	private void highlightSelection(ParsedTuple parserOutput) {
		highlightText(parserOutput, selectionPainter, selectionHighlights);
	}
	
	
	/**
	 * 
	 * 
	 */
	private void parseEdgeList() {
		String edgeStringList;
		List<EdgeDescriptor> faultyEdges;
		
		createActionMap();
		
		// Remove existing highlights of mistakes
		for (Object highlight : mistakeHighlights)
			highlighter.removeHighlight(highlight);

		mistakeHighlights.clear();

		currentEdgeDescriptors.clear();
		edgeStringList = this.textArea.getText();

		for (int pos = 0; pos < edgeStringList.length(); ) {
			ParsedTuple parserOutput;
			EdgeDescriptor descriptor;

			// Parse edge list
			parserOutput = findNextEdgeString(edgeStringList, pos);
			descriptor = null;
			
			if (!parserOutput.isMistake && parserOutput.seemsValid) {
				descriptor = stringToEdge(edgeStringList.substring(parserOutput.startPos, parserOutput.endPos));
				
				if (descriptor != null) {
					if (currentEdgeDescriptors.contains(descriptor)) {
						parserOutput.isMistake = true;
					}
					else {
						currentEdgeDescriptors.add(descriptor);
						descriptor.parserInput = parserOutput;
					}
				}
			}
			
			// Markup mistakes
			if ((parserOutput.isMistake == true) || (descriptor == null && parserOutput.seemsValid)) {
				highlightMistake(parserOutput);
			}
			 else if (descriptor != null)
			{
				 determineAction(descriptor);
			}
			
			pos = parserOutput.endPos + 1;
		}
		
		faultyEdges = calculateInvalidEdges();
		
		if (faultyEdges.size() > 0) {
			for (EdgeDescriptor edge : faultyEdges) {
				highlightMistake(edge.parserInput);
			}
		}
		
		if (mistakeHighlights.size() == 0) {
			 calculateUnrequiredEdges();
			 processActions();
		 }
		
	}
	
	private void updateOutput(String output) {
		int caretPosition;

		if (isEditing)
			return;
		
		isEditing = true;
		
		if (output.endsWith(", "))
			output = output.substring(0, output.length() - 2);
		
		// Update text and restore caret position
		caretPosition = textArea.getCaretPosition();
		textArea.setText(output);
		
		if (caretPosition < 0)
			caretPosition = 0;
		if (caretPosition > output.length())
			caretPosition = output.length();
		
		textArea.setCaretPosition(caretPosition);
		
		updateSelection();
		
		isEditing = false;
	}
	
	private void updateEdgeDescriptors(int startPos, int update) {
		for (EdgeDescriptor descriptor : currentEdgeDescriptors) {
			if (descriptor.parserInput.startPos > startPos) {
				descriptor.parserInput.startPos += update;
				descriptor.parserInput.endPos += update;
			}
		}
	}
	
	public void nodeAdded(Graph graph, Node node) {
		
	}

	public void nodeAltered(Graph graph, Node node) {
	}

	public void nodeRemoved(Graph graph, Node node) {
	}

	public void edgeAdded(Graph graph, Edge edge) {
		String output;
		EdgeDescriptor descriptor;
		ParsedTuple parsedTuple;
		String edgeString;

		if (getSimilarEdgeDescriptor(edge) != null)
			return;
		
		edgeString = edgeToString(edge);
		output = textArea.getText();
		
		if (currentEdgeDescriptors.size() > 0)
			output += ", ";
			
		// Create a new edge descriptor
		parsedTuple = new ParsedTuple(output.length(), output.length() + edgeString.length());
		
		descriptor = new EdgeDescriptor(edge.getBegin().getId(),
										edge.getEnd().getId(),
										edge.getWeight(),
										parsedTuple
									   );
			
		output += edgeString;
				
		currentEdgeDescriptors.add(descriptor);		
		updateOutput(output);
	}

	public void edgeAltered(Graph graph, Edge edge) {
		String newOutput, output;
		int oldLength;
		EdgeDescriptor descriptor;
		int updateOffset;
		String edgeString;
		
		edgeString = edgeToString(edge);
		descriptor = getSimilarEdgeDescriptor(edge);
		
		oldLength = descriptor.weight.toString().length();
		
		output = textArea.getText();
		
		// Update, if old descriptor has changes
		descriptor.weight = edge.getWeight();
		
		newOutput = output.substring(0, descriptor.parserInput.startPos);
		newOutput += edgeString;
		newOutput += output.substring(descriptor.parserInput.endPos);
		
		updateOffset = descriptor.weight.toString().length() - oldLength;				
		
		descriptor.parserInput.endPos = descriptor.parserInput.startPos + edgeString.length();
		
		updateEdgeDescriptors(descriptor.parserInput.startPos, updateOffset);
		updateOutput(newOutput);
	}

	public void edgeRemoved(Graph graph, Edge edge) {
		String newOutput, output;
		EdgeDescriptor descriptor;
		int updateOffset;
		int commaOffset;
		
		updateOffset = 0;
		
		output = textArea.getText();
		descriptor = getSimilarEdgeDescriptor(edge);
		if (descriptor == null)
			return;
		
		if (currentEdgeDescriptors.lastIndexOf(descriptor) == currentEdgeDescriptors.size() - 1)
			commaOffset = 0;
		else
			commaOffset = 2;				
		
		newOutput =  output.substring(0, descriptor.parserInput.startPos + updateOffset);
		newOutput += output.substring(descriptor.parserInput.endPos + updateOffset + commaOffset);
				
		updateOffset -= descriptor.parserInput.endPos - descriptor.parserInput.startPos + commaOffset;
		
		currentEdgeDescriptors.remove(descriptor);
		
		updateEdgeDescriptors(descriptor.parserInput.startPos, updateOffset);
		updateOutput(newOutput);		
	}

	private void updateSelection() {
		Edge graphEdge;
		
		// Remove old selections
		for (Object o : selectionHighlights)
			highlighter.removeHighlight(o);
		
		// Detect new
		graphEdge = graphActionListener.getSelectedEdge();
		if (graphEdge == null)
			return;
		
		for (EdgeDescriptor localEdge : currentEdgeDescriptors) {
			if (    (localEdge.start == graphEdge.getBegin().getId())
				 && (localEdge.end == graphEdge.getEnd().getId())
			   ) 
			{
				highlightSelection(localEdge.parserInput);
				return;
			}
		}
	}
	
	private EdgeDescriptor getSimilarEdgeDescriptor(Edge edge) {
		for (EdgeDescriptor descriptor : currentEdgeDescriptors) {
			if (   (descriptor.start == edge.getBegin().getId())
				&& (descriptor.end == edge.getEnd().getId())
			   ) 
			{
				return descriptor;				
			}
		}
		
		return null;
	}
	
	public void graphUpdated() {

	}
	
	private void initEdgeList() {
		for (Edge edge : graph.getEdges()) {
			this.edgeAdded(graph, edge);
		}
	}
	
	public void graphSelectionChanged() {
		updateSelection();
	}

	private class TextAreaListener implements DocumentListener, CaretListener, KeyListener {
		public void changedUpdate(DocumentEvent event) {
			if (isEditing)
				return;
				
			isEditing = true;
			parseEdgeList();
			isEditing = false;
			
			updateSelection();
		}

		public void insertUpdate(DocumentEvent event) {
			changedUpdate(event);
		}

		public void removeUpdate(DocumentEvent event) {
			changedUpdate(event);
		}

		public void caretUpdate(CaretEvent event) {
			int position = event.getDot();
			
			if (!textArea.hasFocus())
				return;
			
			if (currentEdgeDescriptors == null)
				return;
		
			for (EdgeDescriptor edge : currentEdgeDescriptors) {
				ParsedTuple parsed = edge.parserInput; 
				
				if ((parsed.startPos <= position) && (parsed.endPos >= position)) {
					Edge graphEdge = graph.getEdge(edge.start, edge.end);
					
					if (graphEdge != null) {
						graphActionListener.setSelectedEdge(graphEdge);	
					}
				}
			}
		}

		public void keyPressed(KeyEvent event) {
			if (event.getKeyChar() == '\n')
				event.consume();
			
			return;
		}

		public void keyReleased(KeyEvent event) {
			if (event.getKeyChar() == '\n')
				event.consume();
			
		}

		public void keyTyped(KeyEvent event) {
			if (event.getKeyChar() == '\n')
				event.consume();			
		}
		
	}
	
	public void nodeAltered(Graph graph, NodeElement node) {
		
	}

	public void nodeAlteredPosition(Graph graph, Node node) {
	}

	public void updateBeamerMode(boolean beamerMode) {
		this.beamerMode = beamerMode;
		setTextAreaFont();
	}

}
