package org.jalgo.module.c0h0.models.flowchart;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.jalgo.module.c0h0.models.ast.ASTVisitor;
import org.jalgo.module.c0h0.models.ast.Assignment;
import org.jalgo.module.c0h0.models.ast.Block;
import org.jalgo.module.c0h0.models.ast.If;
import org.jalgo.module.c0h0.models.ast.PrintfStatement;
import org.jalgo.module.c0h0.models.ast.While;

/**
 * Generates the elements of the flowchart. Has to be used with BFSIterator.
 * 
 * @author Philipp Geissler
 * 
 */
public class FcGeneratingVisitor implements ASTVisitor {

	/**
	 * Internal class to store the type of the level (while, if, ifelse, else)
	 * to work on later.
	 * 
	 */
	private class level {
		public LevelType type;
		public Vertex v, vIn;
		public Edge e;
		public List<Edge> eL;
		public boolean isBlock;
		public boolean isBlockElse;
		
		// else constructor
		/**
		 * @param type
		 * @param v
		 * @param e
		 * @param eL
		 * @param isBlock
		 */
		public level(LevelType type, Vertex v, Edge e, List<Edge> eL, boolean isBlock) {
			this.type = type;
			this.e = e;
			this.eL = eL;
			this.v = v;
			this.isBlock = isBlock;
		}
		
		// while constructor
		/**
		 * @param type
		 * @param v
		 * @param vIn
		 * @param eL
		 * @param isBlock
		 */
		public level(LevelType type, Vertex v, Vertex vIn, List<Edge> eL, boolean isBlock) {
			this.type = type;
			this.eL = eL;
			this.v = v;
			this.vIn = vIn;
			this.isBlock = isBlock;
		}
		
		// if-else constructor
		/**
		 * @param type
		 * @param v
		 * @param eL
		 * @param isBlock
		 * @param isBlockElse
		 */
		public level(LevelType type, Vertex v, List<Edge> eL, boolean isBlock, boolean isBlockElse) {
			this.type = type;
			this.v = v;
			this.eL = eL;
			this.isBlock = isBlock;
			this.isBlockElse = isBlockElse;
		}
	}

	private List<Bundle> elementList; // big list with all flowchart elements
	private List<Vertex> vertexList, lastVertexList;
	private List<Edge> edgeList, lastEdgeList;
	private Vertex v1, v2, v3;
	private Edge e1, e2, lastEdge;
	private Bundle b;
	private int depth, currentDepth, depthDifference;
	private int y; 				// current vertical position, increases after every element
	private int leftOffset; 	// offset between vertical arrows on the left side increases after every arrow (OFF)
	private int rightOffset; 	// offset between vertical arrows on the right side, increases after every arrow (OFF)
	private Stack<level> levelStack; 		// stores the type of the current level (if, while, ...)
	private Stack<List<level>> elseQueue; 	// to store arrows going out from the if-block of the ifelse-block
	private List<level> levelQueue; 		// a list of elements from the levelStack to process (if multiple brackets close)
	private List<level> elseLevelQueue; 	// the top list of the elseQueue to process
	private level l, lastWhile;
	private boolean blockStarts, whileClosed, elseAhead;

	private final int OFF = 10; 	// ARROW offset
	private final int MID = 0; 		// horizontal middle of container
	private final int LEN = 25; 	// ARROW vertical length
	private final int DIS = 70; 	// ARROW horizontal distance to middle
	private final int ADD = 6; 		// ADDRESS diameter
	private final int RHW = 90; 	// RHOMBUS width
	private final int RHH = 30; 	// RHOMBUS height
	private final int REW = 90; 	// RECTANGLE width
	private final int REH = 25; 	// RECTANGLE height
	private final int Y = 5; 		// vertical start point

	public FcGeneratingVisitor() {
		vertexList = new ArrayList<Vertex>();
		lastVertexList = new ArrayList<Vertex>();
		edgeList = new ArrayList<Edge>();
		lastEdgeList = new ArrayList<Edge>();
		elementList = new ArrayList<Bundle>();
		y = Y + LEN;
		depth = 1;
		leftOffset = 0;
		rightOffset = 0;
		blockStarts = true;
		levelStack = new Stack<level>();
		elseQueue = new Stack<List<level>>();
	}

	/**
	 * returns a list of Bundles with lists of flowchart elements
	 * @return list of bundles
	 */
	public List<Bundle> getElementList(PrintfStatement printf) {

		// add start arrow
		v1 = new Vertex(null, MID, Y, 0, 0, "ANKER");
		v2 = new Vertex(null, MID, Y + LEN, 0, 0, "ANKER");
		e1 = new Edge(null, v1, v2, "ARROW");
		elementList.get(0).getEdges().add(e1);

		// process left stack
		
		if (levelStack.size() > 0 && depth > 0) {
			checkLevel("");
		}
		
		// add print
		
		edgeList = new ArrayList<Edge>();

		v1 = new Vertex(printf.getAddress(), MID - ADD / 2, y, ADD, ADD, 
				"ADDRESS;spacingLeft="+addSpacing(printf.getAddress()));
		y += ADD + LEN;
		v2 = new Vertex("print(" + printf.getVar().toString()+")", MID - REW / 2, y, REW, REH, "RECTANGLE");

		e1 = new Edge(null, v1, v2, "ARROW");
		edgeList.add(e1);		
		
		b = new Bundle(new ArrayList<Vertex>(), edgeList, printf.getAddress());
		elementList.add(b);
		
		return elementList;
	}

	/**
	 * Creates the arrows, if a level ends.
	 * 
	 * @param address
	 *            of the current symbol
	 */
	private void checkLevel(String address) {
		currentDepth = address.length() - 1;
		depthDifference = depth - currentDepth;
		
		// check if a higher level is reached
		if (depthDifference > 0 && levelStack.size() > 0) {

			levelQueue = new LinkedList<level>();

			/* create levelQueue element for every closing bracket
			 * 
			 * that is necessary to check if a higher while exists
			 * (if that is the case outgoing arrows have to be drawn upwards)
			 */			
			while (depthDifference > 0 && !levelStack.isEmpty()) {
				depthDifference -= 2;
				if(!levelStack.peek().isBlock){
					depthDifference++; // if no brackets around block
				}
				levelQueue.add(levelStack.pop());
			}

			whileClosed = false;

			// search for else
			elseAhead = false;
			if (levelQueue.get(levelQueue.size() - 1).type == LevelType.IFELSE) {
				elseAhead = true;
				elseQueue.add(new LinkedList<level>());
			}

			// process every level in queue (for every closing bracket)
			while (!levelQueue.isEmpty()) {

				l = levelQueue.get(0);
				levelQueue.remove(0);

				// search for last while
				lastWhile = null;
				for (int i = levelQueue.size() - 1; i >= 0; i--)
					if (levelQueue.get(i).type == LevelType.WHILE)
						lastWhile = levelQueue.get(i);
				
				/* process level
				 * 
				 * NOTE: to store the vertices is not necessary because the flowchart is drawn by the edges
				 * (the edges always have a start and an end vertex which will be drawn)
				 */
				switch(l.type){
				case WHILE:
					createIfOrWhile();
					break;
				case IF:
					createIfOrWhile();
					break;
				case IFELSE:
					createIfOrWhile();
					levelStack.push(new level(LevelType.ELSE, lastEdge.getFrom(), lastEdge, lastEdgeList, l.isBlockElse));
					if (!whileClosed && !elseQueue.isEmpty())
						elseQueue.peek().add(new level(LevelType.ELSE, lastEdge.getFrom(), lastEdge, lastEdgeList, true));
					break;
				case ELSE:
					if(!elseQueue.isEmpty())
						createElse();
					break;
				}
			}
		}
	}

	/**
	 * creates arrows from if or while
	 */
	private void createIfOrWhile() {
		
		// close while loop if not already closed
		if (!whileClosed && l.type==LevelType.WHILE) {
			lastEdge.setStyle("HORIZONTAL");

			v1 = new Vertex(null, MID - DIS - leftOffset, lastEdge.getFrom().getY()
					+ lastEdge.getFrom().getHeight() / 2, 0, 0, "ANKER");
			lastVertexList.add(v1);
			lastEdge.setTo(v1);

			v2 = new Vertex(null, MID - DIS - leftOffset, l.vIn.getY() + l.vIn.getHeight() / 2, 0, 0, "ANKER");
			lastVertexList.add(v2);

			e1 = new Edge(null, v1, v2, "VERTICAL");
			lastEdgeList.add(e1);

			v3 = new Vertex(null, MID - l.vIn.getWidth() / 2, l.vIn.getY() + l.vIn.getHeight() / 2, 0, 0, "ANKER");
			e2 = new Edge(null, v2, v3, "ARROW");
			lastEdgeList.add(e2);

			leftOffset += OFF;

			whileClosed = true;
		}
		
		/*
		 * - in while loop
		 * 
		 * => "nein"-arrow goes upwards and leads over the last while
		 */
		if (lastWhile != null){
			
			createArrowUp(l, -1);	
		}
		
		/*
		 * - in if-block of an if-else-construct
		 * - not in while loop
		 * - not leading to an else block
		 * 
		 * => "nein"-arrow will be stored in elseQueue to be drawn later (after else)
		 */
		else if (elseAhead && l.type!=LevelType.IFELSE) {
 
			elseQueue.peek().add(new level(LevelType.WHILE, l.v, l.eL, true, false));
		}
		
		/*
		 * - not in while loop
		 * - not in if-block of an if-else-construct
		 * 
		 * => "nein"-arrow leads normally downwards around the while-block
		 */
		else {
			
			createArrowDown(l, +1);	
		} 
	}

	/**
	 * creates the arrow going around the else-block and the arrows going out of the if-block
	 */
	private void createElse() {
		
		// delete the arrow leading directly to the else-block
		if (l.e.getStyle() != "HORIZONTAL")
			l.e.setStyle("HORIZONTAL;opacity=0");

		// create all arrows from the if-block of the ifelse-block

		elseLevelQueue = elseQueue.pop();

		for (level r : elseLevelQueue) {

			/*
			 * - in while loop
			 * 
			 * => arrow goes upwards and leads over the last while
			 */
			if (lastWhile != null){
				createArrowUp(r, -1);
			}
			
			/*
			 * - not in while loop
			 * - else ahead not possible
			 * 
			 * => arrow leads normally downwards
			 */
			else{
				createArrowDown(r, -1);
			}
		}
	}
	
	/**
	 * creates arrow leading upwards over the last while
	 * 
	 * @param k
	 * @param side
	 * 			-1 arrow on left side
	 * 			+1 arrow on right side
	 */
	private void createArrowUp(level k, int side){
		int offset;
		if(side==-1) offset = -DIS - leftOffset;
		else offset = DIS + rightOffset;
		
		v1 = new Vertex(null, MID + offset, k.v.getY() + k.v.getHeight() / 2, 0, 0, "ANKER");
		
		if (k.type == LevelType.ELSE) // if the arrow is going around the else-block (then no label)
			e1 = new Edge(null, k.v, v1, "HORIZONTAL");
		else
			e1 = new Edge("nein", k.v, v1, "HORIZONTAL");
		
		k.eL.add(e1);

		v2 = new Vertex(null, MID + offset, lastWhile.vIn.getY() + lastWhile.vIn.getHeight() / 2, 0, 0,
				"ANKER");
		e1 = new Edge(null, v1, v2, "VERTICAL");
		k.eL.add(e1);
		
		v3 = new Vertex(null, MID + side*(lastWhile.vIn.getWidth() / 2),
				lastWhile.vIn.getY() + lastWhile.vIn.getHeight() / 2, 0, 0, "ANKER");
		e1 = new Edge(null, v2, v3, "ARROW");
		k.eL.add(e1);
		
		if(side==-1) leftOffset += OFF;
		else rightOffset += OFF;
	}
	
	/**
	 * creates arrow leading downwards
	 * 
	 * @param k
	 * @param side
	 * 			-1 arrow on left side
	 * 			+1 arrow on right side
	 */
	private void createArrowDown(level k, int side){
		int offset;
		if(side==-1) offset = -DIS - leftOffset;
		else offset = DIS + rightOffset;
		
		v1 = new Vertex(null, MID + offset, k.v.getY() + k.v.getHeight() / 2, 0, 0, "ANKER");
		
		if (k.type == LevelType.ELSE) // if the arrow is going around the else-block (then no label)
			e1 = new Edge(null, k.v, v1, "HORIZONTAL");
		else
			e1 = new Edge("nein", k.v, v1, "HORIZONTAL");
		
		k.eL.add(e1);

		v2 = new Vertex(null, MID + offset, y + ADD / 2, 0, 0, "ANKER");
		e1 = new Edge(null, v1, v2, "VERTICAL");
		k.eL.add(e1);

		v3 = new Vertex(null, MID + side*(ADD / 2), y + ADD / 2, 0, 0, "ANKER");
		e1 = new Edge(null, v2, v3, "ARROW");
		k.eL.add(e1);

		if(side==-1) leftOffset += OFF;
		else rightOffset += OFF;
	}
	
	/**
	 * positions the addresses dynamically
	 * 
	 * @param address
	 * @return address spacing
	 */
	private String addSpacing(String address){	
		Integer addressSpacing = address.length()*4;
		return addressSpacing.toString();
	}

	public void visitBlock(Block block) {
		
		if(!block.getStatementList().isEmpty()){	// if no empty else block
			if (!block.isBlock()) {	// if no brackets around block
				
				if (!blockStarts){
					
					// resolve if-else-anomaly
					if(!levelStack.peek().isBlock && !levelStack.peek().isBlockElse)
						checkLevel(block.getAddress().substring(2));
					else checkLevel(block.getAddress().substring(1));
				}
			}
			
			else {	// if brackets around block
				vertexList = new ArrayList<Vertex>();
				edgeList = new ArrayList<Edge>();
		
				if (!blockStarts){ // else block
					// resolve if-else-anomaly
					if(levelStack.peek().isBlock)
						checkLevel(block.getAddress());
					else checkLevel(block.getAddress().substring(1));
				}
	
				v1 = new Vertex(block.getAddress(), MID - ADD / 2, y, ADD, ADD, 
						"LEFTADDRESS;spacingRight="+addSpacing(block.getAddress()));
				v2 = new Vertex(null, MID, y, 0, 0, "ANKER");
	
				vertexList.add(v1);
				vertexList.add(v2);
	
				e1 = new Edge(null, v1, v2, "opacity=0");
				edgeList.add(e1);
	
				b = new Bundle(vertexList, edgeList, block.getAddress());
				elementList.add(b);
			}
			blockStarts = false;
			depth = 1;	// to prevent a false level-up event caused by if-else-anomaly
		}
	}

	public void visitAssignment(Assignment assignment) {

		vertexList = new ArrayList<Vertex>();
		edgeList = new ArrayList<Edge>();

		checkLevel(assignment.getAddress()); // check if a bracket has closed

		v1 = new Vertex(assignment.getAddress(), MID - ADD / 2, y, ADD, ADD, 
				"ADDRESS;spacingLeft="+addSpacing(assignment.getAddress()));
		y += ADD + LEN;
		v2 = new Vertex(assignment.getVar().toString() + " = " + assignment.getTerm().toString(), MID - REW / 2, y,
				REW, REH, "RECTANGLE");
		y += REH + LEN;
		v3 = new Vertex(null, MID, y, 0, 0, "ANKER");

		vertexList.add(v1);
		vertexList.add(v2);
		vertexList.add(v3);

		e1 = new Edge(null, v1, v2, "ARROW");
		edgeList.add(e1);

		e1 = new Edge(null, v2, v3, "ARROW");
		edgeList.add(e1);

		b = new Bundle(vertexList, edgeList, assignment.getAddress());
		elementList.add(b);

		lastEdge = new Edge(null, null, null, null);
		lastVertexList = new ArrayList<Vertex>();
		lastEdgeList = new ArrayList<Edge>();

		lastEdge = e1;
		lastVertexList = vertexList;
		lastEdgeList = edgeList;

		blockStarts = false;

		depth = assignment.getAddress().length()-1;

	}

	public void visitIf(If ifStatement) {

		vertexList = new ArrayList<Vertex>();
		edgeList = new ArrayList<Edge>();

		checkLevel(ifStatement.getAddress()); // check if a bracket has closed

		v1 = new Vertex(ifStatement.getAddress(), MID - ADD / 2, y, ADD, ADD,
				"ADDRESS;spacingLeft="+addSpacing(ifStatement.getAddress()));
		y += ADD + LEN;
		v2 = new Vertex(ifStatement.getRelation().toString(), MID - RHW / 2, y, RHW, RHH, "RHOMBUS");
		y += RHH + LEN;
		v3 = new Vertex(null, MID, y, 0, 0, "ANKER");

		vertexList.add(v1);
		vertexList.add(v2);
		vertexList.add(v3);

		e1 = new Edge(null, v1, v2, "ARROW");
		edgeList.add(e1);

		e1 = new Edge("ja", v2, v3, "ARROW");
		edgeList.add(e1);

		b = new Bundle(vertexList, edgeList, ifStatement.getAddress());
		elementList.add(b);

		if (ifStatement.getElseSequence().getStatementList().size() == 0)
			levelStack.push(new level(LevelType.IF, v2, edgeList, ifStatement.getIfSequence().isBlock(), false));
		else
			levelStack.push(new level(LevelType.IFELSE, v2, edgeList,
					ifStatement.getIfSequence().isBlock(), ifStatement.getElseSequence().isBlock()));	

		lastEdge = new Edge(null, null, null, null);
		lastVertexList = new ArrayList<Vertex>();
		lastEdgeList = new ArrayList<Edge>();

		lastEdge = e1;
		blockStarts = true;
		lastVertexList = vertexList;
		lastEdgeList = edgeList;
		depth = ifStatement.getAddress().length()-1;
		
	}

	public void visitWhile(While whileStatement) {
		
		vertexList = new ArrayList<Vertex>();
		edgeList = new ArrayList<Edge>();

		checkLevel(whileStatement.getAddress()); // check if a bracket has closed
		
		v1 = new Vertex(whileStatement.getAddress(), MID - ADD / 2, y, ADD, ADD,
				"ADDRESS;spacingLeft="+addSpacing(whileStatement.getAddress()));
		y += ADD + LEN;
		v2 = new Vertex(whileStatement.getRelation().toString(), MID - RHW / 2, y, RHW, RHH, "RHOMBUS");
		y += RHH + LEN;
		v3 = new Vertex(null, MID, y, 0, 0, "ANKER");

		vertexList.add(v1);
		vertexList.add(v2);
		vertexList.add(v3);

		e1 = new Edge(null, v1, v2, "ARROW");
		edgeList.add(e1);

		e1 = new Edge("ja", v2, v3, "ARROW");
		edgeList.add(e1);

		b = new Bundle(vertexList, edgeList, whileStatement.getAddress());

		elementList.add(b);

		levelStack.push(new level(LevelType.WHILE, v2, v1, edgeList, whileStatement.getBlock().isBlock()));

		lastEdge = new Edge(null, null, null, null);
		lastVertexList = new ArrayList<Vertex>();
		lastEdgeList = new ArrayList<Edge>();

		lastEdge = e1;
		blockStarts = true;
		lastVertexList = vertexList;
		lastEdgeList = edgeList;
		depth = whileStatement.getAddress().length()-1;

	}

}