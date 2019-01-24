package org.jalgo.module.hoare.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Point;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.constants.Rule;
import org.jalgo.module.hoare.constants.Status;
import org.jalgo.module.hoare.constants.TextStyle;
import org.jalgo.module.hoare.model.Model;
import org.jalgo.module.hoare.model.VerificationFormula;
import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;

/**
 * This is the Panel which shows the Graph using JGraph.
 * All changes to the graph have to be in this class.
 * 
 * @author Tomas
 *
 */
public class WSGraph extends JPanel implements Observer {
	
	private static Color selectionColor = new Color(0,165,255);
	private static final long serialVersionUID = 5704113904547903732L;
	private JGraph graph = new JGraph(new DefaultGraphModel());
	private double zoom = 0;
	private MyCell selected;
	private MyCell opened;
	private HashMap<String, Double> openedPos = new HashMap<String, Double>();
	private final View gui;
	private ArrayList <VerificationFormula> neueZellen = new ArrayList <VerificationFormula>();
	
	/**
	 * Starting the JPanel containing the the Graph
	 * @param View
	 *		the controlling View to save it 
	 *
	 */
	public WSGraph(View gui){
		super();
		this.gui = gui;
		this.setLayout(new BorderLayout());
		
		graph.setCloneable(true);
     	graph.setInvokesStopCellEditing(true);
		graph.setJumpToDefaultPort(true);
		graph.setSelectionEnabled(false);
		graph.setDisconnectable(false);
		graph.setEditable(false);
		graph.setAntiAliased(true);
		graph.getGraphLayoutCache().setFactory(new MyCellViewFactory());
		graph.addKeyListener(gui);
		graph.addMouseListener(gui.getGraphMouseListener());
		graph.addMouseWheelListener(gui.getGraphMouseListener());
		graph.addMouseListener(gui.new CellPopupMenu());
		graph.setBounds(this.getBounds());
		
		this.add(graph);
		this.add(new JScrollPane(graph));
		
		
}
	/**
	 * the cell maker, which makes a cell
	 * with an color gradient, an ID, a Text and the level and positioning flag
	 * <br>
	 * the Width and Height of a Cell is set 0 because the autosize function of jgraph will size the cells
	 * 
	 * @param name
	 * 		the Text to be shown in the cell
	 * @param ID
	 * 		the ID of the Verification Formula, which the cell is representing.
	 * 		also used to find a specific cell in a Cell Array
	 * @param bg
	 * 		the background Gradient color of the cell
	 * @param pos
	 * 		the position of the cell in relation to his parent cell (1,0,-1)
	 * @param level
	 * 		the level the cell should be shown in the graph (for example: the root cell gets Level 0, the children of root Level 1)
	 * @return MyCell
	 * 		the made cell, but not inserted in jgraph yet
	 */
	private MyCell createCell(String name, int ID, Color bg, int pos, int level, boolean rounded) {
	 return createCell(name, ID, 1, 1, bg, pos, level, rounded);
	}
	
	/**
	 * the real cell maker, which makes a cell on a specific position,
	 * with an color gradient, an ID, a Text and the level and positioning flag
	 * <br>
	 * the Width and Height of a Cell is set 0 because the autosize function of jgraph will size the cells
	 * 
	 * @param name
	 * 		the Text to be shown in the cell
	 * @param ID
	 * 		the ID of the Verification Formula, which the cell is representing.
	 * 		also used to find a specific cell in a Cell Array
	 * @param x
	 * 		the X coordinate of the cells position
	 * @param y
	 *  	the Y coordinate of the cells position
	 * @param bg
	 * 		the background Gradient color of the cell
	 * @param pos
	 * 		the position of the cell in relation to his parent cell (1,0,-1)
	 * @param level
	 * 		the level the cell should be shown in the graph (for example: the root cell gets Level 0, the children of root Level 1)
	 * @return MyCell
	 * 		the made cell, but not inserted in jgraph yet
	 */
	private MyCell createCell(String name, int ID, int x, int y, Color bg, int pos, int level, boolean rounded) {

			MyCell cell = new MyCell(name, ID, pos, level, rounded);
			cell.setUserObject(VertexPanelFactory.getVertexPanel(gui, cell, getSelectedCell()==cell, false));
						
			GraphConstants.setBounds(cell.getAttributes(),new Rectangle2D.Double(x, y, 0, 0));
		
			GraphConstants.setAutoSize(cell.getAttributes(), true);
			GraphConstants.setSelectable(cell.getAttributes(),false);
			GraphConstants.setBorder(cell.getAttributes(), BorderFactory.createLineBorder(Color.black));
			GraphConstants.setBorderColor(cell.getAttributes(), Color.black);
			if (bg != null) {GraphConstants.setGradientColor(cell.getAttributes(), bg);
				GraphConstants.setOpaque(cell.getAttributes(), true);
			}

			DefaultPort port = new DefaultPort();
			cell.add(port);
			port.setParent(cell);
			
			
			return cell;
		}
	
	/**
	 * the Edgemaker, which gets two cells and the label name and retruns the cell.
	 * the Edges will have a link to the parent and the child cell saved inside the edge.
	 * 
	 * @param label
	 * 		the Edge Label written in the middle of an edge (keep empty if you don't want a label)
	 * @param source
	 * 		the Source Cell (parent) of the type MyCell
	 * @param target
	 * 		the Target Cell (child) of the type MyCell
	 * @return MyEdge
	 * 	 	the made edge, but not inserted in jgraph yet	
	 */
	private MyEdge createEdge(MyCell source, MyCell target){
	
		// Richtung ist ge�ndert
		
		MyEdge edge= new MyEdge(); 
		// source - target gewechselt
		edge.setSource(target.addPort());	
		edge.setTarget(source.addPort());
		// �nderung ende
		edge.setSourceCell(source);
		edge.setTargetCell(target);
		GraphConstants.setLineEnd(edge.getAttributes(), GraphConstants.ARROW_SIMPLE);
		edge.setUserObject(getEdgeText(source,target,TextStyle.SHORT));
		
		GraphConstants.setOffset(edge.getAttributes(), new Point2D.Double(20, 0));
		
		return edge;
	}
	/**
	 * this function was is supposed to give the EdgeText for an edge with two Cells of the MyCell type.
	 * The text will be the rule which was used.
	 * 
	 * The function also finds cells which should be not rounded in the view and changes the preferences of the cell.
	 * 
	 * @param source
	 * @param target
	 * @param style
	 * @return
	 */
	private String getEdgeText(MyCell source, MyCell target, TextStyle ts){
		VerificationFormula ziel =gui.getModel().getVerificationFormula(target.getID());
		return getEdgeText( ziel.getParent(), ziel , ts);
		
	}
	
	/**
	 * this function was is supposed to give the EdgeText for an edge with two Verification formulars.
	 * The text will be the rule which was used.
	 * 
	 * The function also finds cells which should be not rounded in the view and changes the preferences of the cell.
	 * 
	 * @param source
	 * @param target
	 * @param style
	 * @return
	 */
	private String getEdgeText(VerificationFormula source, VerificationFormula target, TextStyle style){
		String label="";
		
		if (source!= null && target!= null){
		Rule rule = source.getAppliedRule();
		label = translateRule(rule);
		}
		
		return label;
				
	}
	
	/**
	 * zooming the graph with e^(value/5) as function
	 * 
	 * @param value
	 * 		zooming value is supposed to be 1 or -1
	 * @param p
	 * 		point to which it should zoom
	 * @return true
	 * 		returns the success of the operation
	 */
	public boolean zoom(double value, Point2D p){
		this.zoom = zoom + (value/5);
		double newScale = Math.exp(zoom);
		
		if ((newScale>=20 && value > 0) || (newScale<=0.2 && value < 0)) {
			gui.setStatusText(Messages.getString("hoare", "view.zoomError"));
			this.zoom = zoom - (value/5);
			return false;
		} else {
		gui.setStatusText("");	
		graph.setScale(newScale, p);
		}
		return true;
		
	}
	
	/**
	 * getter for Jgraph
	 * @return JGraph
	 */
	public JGraph getGraph(){
		return graph;
	}
	
	/**
	 * function to select a cell, change the border color and notify the variable and source screen
	 * @param cell
	 * 		cell which should be selected
	 */
	public void setSelected(MyCell cell){
		if (selected != cell){
			if (selected != null) {setUnselected(); }
			contractCell();
			selected=cell;
			GraphConstants.setBorderColor(cell.getAttributes(), selectionColor);
			GraphConstants.setBorder(cell.getAttributes(), BorderFactory.createLineBorder(selectionColor,1));
			
			graph.getGraphLayoutCache().editCell(cell, cell.getAttributes());
			VertexPanelFactory.setSelected((JPanel)cell.getUserObject());
			gui.getWorkScreen().getVar().newCellSelected(cell);
			gui.getWorkScreen().getSource().newCellSelected(cell);
		} else {
			expandCell(cell);
		}
		beautifier();
	}
	
	/**
	 * the selected Cell getter
	 * @return
	 */
	public MyCell getSelectedCell(){
	return selected;
	}
	
	/**
	 * function to deselect a cell and reset all properies (border color, ...),
	 * notifies the variable and source screen
	 */
	public void setUnselected(){
		if (selected != null){
			GraphConstants.setBorder(selected.getAttributes(), BorderFactory.createLineBorder(Color.black));
			GraphConstants.setBorderColor(selected.getAttributes(), Color.black);
			graph.getGraphLayoutCache().editCell(selected, selected.getAttributes());
			contractCell();
			VertexPanelFactory.setNotSelected((JPanel)selected.getUserObject());
			selected = null;
			gui.getWorkScreen().getSource().cellsUnselected();
			gui.getWorkScreen().getVar().cellsUnselected();
		}
	}
	
	/**
	 * function to bring cell in an order on the screen.
	 * <br>
	 * function is used everytime when something was changed on the graph.<br>
	 * it works is this order: <br>
	 * 1. finding out how many levels there are and which one the root cell is (Level = 0)<br>
	 * 2. getting maximum Width using the getMaxWidth function<br>
	 * 3. painting the cells <br>
	 */
	protected void beautifier(){
				
		int minLevelHeight = 100, levelCount=0;
		MyCell root = null;
		Object[] cells = graph.getGraphLayoutCache().getCells(false, true, false, false);
		
		for (Object obj : cells) {
			MyCell cell = (MyCell)obj;
			levelCount = Math.max(cell.getLevel(), levelCount);
			if (cell.getLevel() == 0) { root = cell;}
		}
		
		if (root!= null){
		levelCount++;
		int height = Math.max(this.getHeight(), minLevelHeight*(levelCount+1));
		double maxWidth= getMaxWidth(root)+30;
		graph.getGraphLayoutCache().edit(setCellBounds(root, 0, Math.max(this.getWidth(), maxWidth), height, Math.round(height/(levelCount+1)),null));
		}
		
	}
	
	/**
	 * this function is used by the beautifier to get max Width <br>
	 * you can give the root Cell and it will call itself in depth first (left - right - self) to get the width of the children,
	 * set a ratio, sum the Width and return the max(width of the cell or sum of width of the children)
	 * @param cell
	 * 		MyCell which should be the root
	 * @return
	 * 		the Max Width needed to paint the cell with all the children (without a cell overlaying an other)
	 */
	private double getMaxWidth(MyCell cell){
		
		Object[] edges = DefaultGraphModel.getEdges(graph.getModel(), cell, true);
		
		switch(edges.length) {
		case 1: 
			return Math.max(GraphConstants.getBounds(cell.getAttributes()).getWidth(), getMaxWidth(((MyEdge)edges[0]).getTargetCell()));
		case 2:
				double width1 = 0, width2 =0; 
				if (((MyEdge)edges[0]).getTargetCell().getPos() > 0){
					width1 = getMaxWidth(((MyEdge)edges[0]).getTargetCell());
					width2 = getMaxWidth(((MyEdge)edges[1]).getTargetCell());
				} else {
					width2 = getMaxWidth(((MyEdge)edges[0]).getTargetCell());
					width1 = getMaxWidth(((MyEdge)edges[1]).getTargetCell());	
				}
				cell.setRatio(Math.round(100/((width1+width2)/width2))); 
				return Math.max(GraphConstants.getBounds(cell.getAttributes()).getWidth(), width1+width2);
		}
		return GraphConstants.getBounds(cell.getAttributes()).getWidth();
	}
	
	/**
	 * this function is used by the beautifier to paint the cells, <br>
	 * it paints the cell in the middle of the (from-to) region, uses the ratio to divide the area and calls itself with the children Cells 
	 * @param cell
	 * 		MyCell which should be the root
	 * @param from
	 * 		Point on the X axis where the area for this cell starts
	 * @param to
	 * 		Point on the X axis where the area for this cell ends
	 * @param height
	 * 		the height of the whole cell painting area
	 * @param levelHeight
	 * 		the level height ( area between the cells in Y Axis)
	 * @return
	 *		a Map with all the changes on the Graph !!! the content of the map has to be given to Jgraph after calling this method,
	 *		this method doesn't change the cell position in the graph directly - it just write the changes in the Map
	 */
	private Map<MyCell, AttributeMap> setCellBounds(MyCell cell, double from, double to, int height, int levelHeight, Map<String, Double> special){
		HashMap<MyCell, AttributeMap> nested = new HashMap<MyCell, AttributeMap>();
		
		long minusX = Math.round(GraphConstants.getBounds(cell.getAttributes()).getWidth()/2);
		long minusY = Math.round(GraphConstants.getBounds(cell.getAttributes()).getHeight()/2);
		
		if (special==null){		
		GraphConstants.setBounds(cell.getAttributes(), new Rectangle2D.Double((from + Math.round((to-from)/2))-minusX, (height-(levelHeight*(cell.getLevel()+1)))-minusY,0,0));
		} 
		nested.put(cell, cell.getAttributes());
				
		
		Object[] edges = DefaultGraphModel.getEdges(graph.getModel(), cell, true);
		switch(edges.length) {
		case 1: 
			nested.putAll(setCellBounds(((MyEdge)edges[0]).getTargetCell(), from, to, height, levelHeight, null));
		case 2:
			int mitte = (int) Math.round(from + ((to-from)*cell.getRatio()/100));
		
			HashMap<String, Double> specialNeu = null;
									
			for (Object edge : edges){
				MyCell zelle = ((MyEdge)edge).getTargetCell();
				if (zelle.getPos() == -1) nested.putAll(setCellBounds(zelle, from, mitte , height, levelHeight, specialNeu));
				if (zelle.getPos() == 1) nested.putAll(setCellBounds(zelle, mitte ,to, height, levelHeight, specialNeu));
			}
			
		default: break;
		}
		
		
		
		return nested;
	}
	
	/**
	 * paints the graph new (supposed to be called when the root VerificationFormula was changed)
	 * @param root
	 * 		VerificationFormula root
	 */
	private void paintNew(VerificationFormula root) { 
		
		graph.setGraphLayoutCache(new GraphLayoutCache());
		graph.getGraphLayoutCache().setFactory(new MyCellViewFactory());
		if (root.isRuleApplied()){
		graph.getGraphLayoutCache().insert(cellPaint(root, 0, MyCell.setMiddle()).toArray());
		
		MyCell se = selected;
		MyCell ex = opened;
		
		setUnselected();
			
			if (se != null) {
				MyCell cell = getCell(se.getID());
				if (cell != null){
					setSelected(cell);
					
					if (ex != null && ex.equals(se)) setSelected(cell);
				}
			}
			
		}
	}
	
	/**
	 * support function of paintNew(VerificationFormula) for recursive calls to paint the cells
	 * @param vf
	 * 		VerificationFormula on which the cell is based
	 * @param Level
	 * 		the Level the cell should be painted (root = 0, children of root =1, their children =2,...)
	 * @param seite
	 * 		the Position relative to the parent cell (-1 = left, 0 = middle , 1 = right)
	 * @return
	 * 		ArrayList of Cells which should be inserted in the graph (this function doesn't insert them into the graph)
	 */
	private ArrayList<DefaultGraphCell> cellPaint(VerificationFormula vf, int Level, int seite){
		ArrayList<DefaultGraphCell> list = new ArrayList<DefaultGraphCell>();
	    MyCell cell = createCell(getCellText(vf, false), vf.getId(), getNeededColor(vf), seite, Level, !vf.isImplication());
		list.add(cell);
		
		if (vf.hasChildren()){
			if (vf.getChildren().size()>1) {
				if (vf.getChildren().get(0).isRuleApplied()){
				List<DefaultGraphCell> a = cellPaint(vf.getChildren().get(0), Level+1, MyCell.setLeft());
				list.addAll(a);
				list.add(createEdge(cell,(MyCell) a.get(0)));
				
				}
				if (vf.getChildren().get(1).isRuleApplied()){
				List<DefaultGraphCell> b = cellPaint(vf.getChildren().get(1), Level+1, MyCell.setRight());
				list.addAll(b);
				list.add(createEdge(cell,(MyCell) b.get(0)));
				
				}
			} else {
				if (vf.getChildren().get(0).isRuleApplied()){
				List<DefaultGraphCell> c = cellPaint(vf.getChildren().get(0), Level+1, MyCell.setMiddle());
				list.addAll(c);
				list.add(createEdge( cell,(MyCell) c.get(0)));
				
				}
			}
		}
		
		return list;
	}
	
	/**
	 * shows a cell in big with detailed information, might call the beautifier if the big cell moves out of the left border of the screen
	 * @param cell
	 * 		the cell which should be expanded
	 */
	public void expandCell(MyCell cell){
	
		if (opened == null){
		opened=cell;
		double openedH2 = GraphConstants.getBounds(opened.getAttributes()).getHeight()/2;
		double openedW2 = GraphConstants.getBounds(opened.getAttributes()).getWidth()/2;
		opened.setUserObject(VertexPanelFactory.getVertexPanel(gui, cell, (cell==getSelectedCell()), true));
		graph.getGraphLayoutCache().editCell(opened, opened.getAttributes());
			
		openedPos.put("altX", GraphConstants.getBounds(opened.getAttributes()).getX());
		openedPos.put("neuX", (openedPos.get("altX"))-(GraphConstants.getBounds(opened.getAttributes()).getWidth()/2)+openedW2);
		openedPos.put("altY", GraphConstants.getBounds(opened.getAttributes()).getY());
		openedPos.put("neuY", ((openedPos.get("altY"))-GraphConstants.getBounds(opened.getAttributes()).getHeight()/2)+openedH2);
		
		GraphConstants.setBounds(opened.getAttributes(), new Rectangle2D.Double(openedPos.get("neuX"),openedPos.get("neuY"), 0, 0));
		graph.getGraphLayoutCache().editCell(opened, opened.getAttributes());
		
		
		
		beautifier(); 
		}
		
	}
	
	/**
	 * shows the graph in "normal-mode" if the cell was expanded before
	 * if the beautifier was used in expanding a Cell this function will use the beautifier as well
	 */
	public void contractCell(){
	
		if (opened != null){
			VerificationFormula vf = gui.getModel().getVerificationFormula(opened.getID());
			opened.setUserObject(VertexPanelFactory.getVertexPanel(gui, opened, opened==getSelectedCell(), false));
			double x =openedPos.get("altX")+(GraphConstants.getBounds(opened.getAttributes()).getX()-openedPos.get("neuX"));
			double y =openedPos.get("altY")+(GraphConstants.getBounds(opened.getAttributes()).getY()-openedPos.get("neuY"));
			GraphConstants.setBounds(opened.getAttributes(), new Rectangle2D.Double(x,y, 0, 0));
			graph.getGraphLayoutCache().editCell(opened, opened.getAttributes());
			beautifier();
			openedPos.clear();
			Object[] edges = DefaultGraphModel.getEdges(graph.getModel(), opened, false);
			if (edges.length>0)	{
				((MyEdge)edges[0]).setUserObject(getEdgeText(vf.getParent(),vf, TextStyle.SHORT));
				graph.getGraphLayoutCache().editCell(((MyEdge)edges[0]), ((MyEdge)edges[0]).getAttributes());
			}
			opened = null; 
		}
	
	}
	
	/**
	 * return the color, the VerificationFormula is supposed to be
	 * @param vf
	 * 		VerificationFormula that color we want to have
	 * @return
	 * 		Color of the VerificationFormula
	 */
	private Color getNeededColor(VerificationFormula vf){
		if (vf.getStatus() == Status.WAITING) {return Color.YELLOW;}
		if (vf.getStatus() == Status.RESULTOK) {return Color.GREEN;}
		if (vf.getStatus() == Status.RESULTWRONG) {return new Color(255,112,64);}
		return Color.LIGHT_GRAY;
	}
	
	/**
	 * returns the name of the differnet Rules how it is supposed to be shown
	 * @param rule
	 * 		the Enum rule we wnt to show
	 * @return
	 * 		String with the rule name (in english or german)
	 */
	private String translateRule(Rule rule){
		if (rule == null) return "";
		
		switch(rule){
		case ASSIGN: 	return Messages.getString("hoare","rule.assign.desc");
		case IF: 		return Messages.getString("hoare","rule.alt1.desc");
		case IFELSE: 	return Messages.getString("hoare","rule.alt2.desc");
		case COMPOUND: 	return Messages.getString("hoare","rule.comp.desc");
		case STATSEQ: 	return Messages.getString("hoare","rule.sequence.desc");
		case ITERATION: return Messages.getString("hoare","rule.iter.desc");
		case STRONGPRE: return Messages.getString("hoare","rule.SV.desc");
		case WEAKPOST: 	return Messages.getString("hoare","rule.SN.desc");
		default: 		return "";
		}
		
	}
	
	/**
	 * finds the cell to an ID or returns null if Cell not found
	 * @param ID
	 * 		int ID we want to find (most times the ID of a VerificationFormula)
	 * @return
	 * 		MyCell with the ID or null
	 */
	private MyCell getCell(int ID){
		MyCell zelle = null;
		Object[] cells = graph.getGraphLayoutCache().getCells(false, true, false, false);
		
		for (Object cell : cells){
			if (((MyCell)cell).getID() == ID) { zelle = (MyCell)cell; break;}
		}
		return zelle;
	}
		
	/**
	 * changes cells properties or builds a List of cells which should be paint
	 * @param verificationFormula
	 * 		VerificationFormula which was changed
	 */
	private void changeCell(VerificationFormula vf) {
		MyCell cell = getCell(vf.getId());
		if (cell != null){
		cell.setUserObject(VertexPanelFactory.getVertexPanel(gui, cell, cell==getSelectedCell(), cell==opened));//getCellText(vf, false));
		GraphConstants.setGradientColor(cell.getAttributes(), getNeededColor(vf));
		graph.getGraphLayoutCache().editCell(cell, cell.getAttributes());
		} else  {
			if (vf.isRuleApplied()){
				neueZellen.add(vf);
			}		  
		}
	}
	
	/**
	 * paints new cells of the base of the List made in changeCell
	 * so, just call this function if you called changeCell(VerificationFormula ) before
	 */
	private void makeNewCells(){
		
		ArrayList<DefaultGraphCell> neu = new ArrayList<DefaultGraphCell>();
		
		while (!neueZellen.isEmpty()) {
			
			VerificationFormula vf= neueZellen.get(0);
			VerificationFormula root= vf.getParent();
			if (root==null) throw new NullPointerException("root should not be null");
			MyCell rootCell = getCell(root.getId());
			if (rootCell==null) throw new NullPointerException("rootCell should not be null");
			
			int position= 0;
 		if (root.getChildren().size()<2) {position = MyCell.setMiddle();} 
 		else {
 			if (root.getChildren().get(0).equals(vf)) {
 				position = MyCell.setLeft();
 			}	else {
	 			position = MyCell.setRight();
	 		}
		 }
 		MyCell cell = createCell( getCellText(vf, false), vf.getId(), getNeededColor(vf), position, rootCell.getLevel()+1, !vf.isImplication());
			neu.add(cell);
			neu.add(createEdge(rootCell, cell));

			neueZellen.remove(0);			
		}
		if (!neu.isEmpty()){
		 graph.getGraphLayoutCache().insert(neu.toArray());
		 if (gui.getModel().getVerificationFormula(((MyCell) neu.get(0)).getID()).isImplication() && neu.size() > 2) { 
			 setSelected((MyCell) neu.get(2));
		 } else { setSelected((MyCell) neu.get(0)); }
		}
		neueZellen.clear();
	}
	
	/**	
	 * gives a String which is supposed to be shown in the Cell of this VerificationFormula
	 * the boolean full discripes if the text should be in short or full mode
	 * @param vf
	 * 		VerificationFormula we want to show in the cell
	 * @param full
	 * 		true means the full text , false means the short form of the text
	 * @return
	 * 		String with the text to put into the cell User Object
	 */
	private String getCellText(VerificationFormula vf, boolean full){
		if (vf==null) throw new NullPointerException("Formula is null!");
		String userObject="";
		String resultPre="";
		String resultPost="";
		String format;
		TextStyle style;
		if (full){
			format="<html><div style=\"padding:10pt;font-size:20pt;text-align:center;\">%s"+
          " <b>{%s}</b><br><i>%s</i><br><b>{%s}</b> %s</div></html>";
			style=TextStyle.FULL;
		}else{
			format="<html><div style=\"padding:5pt;\">%s"+
          " <b>{%s}</b> <i>%s</i> <b>{%s}</b> %s</div></html>";
			style=TextStyle.SHORT;			
		}
		if (vf.getParent()!=null){
			Rule rule=vf.getParent().getAppliedRule();
			
			if (Rule.IF.equals(rule)){
				if (full) {
					resultPost="("+vf.getPreAssertion(style)+
		            ") => "+vf.getParent().getPostAssertion(style)+")";
					resultPost=resultPost.replace("<", "&lt;").replace(">", "&gt;").replace("\u2227 ", "\u2227 \u00ac(");
					resultPost="<br>"+resultPost;} 
				else{
					resultPost="("+vf.getPreAssertion(style)+
		            " => "+vf.getParent().getPostAssertion(style)+")";
					resultPost=resultPost.replace("<", "&lt;").replace(">", "&gt;");
					resultPost=resultPost.replace("\u03C0", "\u00ac\u03C0");}
				
				
			}
			
		}
				
  String pre=vf.getPreAssertion(style);
  pre=pre.replace("<", "&lt;").replace(">", "&gt;");
  String post=vf.getPostAssertion(style);
  post=post.replace("<", "&lt;").replace(">", "&gt;");
  String source=vf.getCode(full);
  source=source.replace("<", "&lt;").replace(">", "&gt;");
  userObject=String.format(format,resultPre,pre,source,post,resultPost);
		return userObject;
	
	}
	
	/**
	 * function that gets information from View if something changed in the Model<br>
	 * it splits the event : if root was changed then paintnew else change cells and make new Cells
	 */
	public void update(Observable o, Object arg) {
		
		VerificationFormula root = ((Model)o).getRoot();
		
		if (root != null){
		
		 if (root.hasChanged()) {
		  paintNew(root); 
		 } else {
				ArrayList<VerificationFormula> changes = getChanges(root);
				for (VerificationFormula vf : changes){
		 		changeCell(vf);
		 	}
			 makeNewCells();
		 }
	 
		beautifier();
		} else {
			graph.setGraphLayoutCache(new GraphLayoutCache());
			graph.getGraphLayoutCache().setFactory(new MyCellViewFactory());
			this.setUnselected();
		}
		
		}
	
	/**
	 * function to get a list of changed VerificationFormula
	 * @param root
	 * 		the root VerificationFormula
	 * @return
	 * 		ArrayList of VerificationFormulas which been changed
	 */
	private ArrayList<VerificationFormula> getChanges(VerificationFormula test){
		ArrayList<VerificationFormula> changes = new ArrayList<VerificationFormula>();
		if (test.getChildren()!=null){
		for (VerificationFormula a : test.getChildren()){
			changes.addAll(getChanges(a));
		}
		}
		if (test.hasChanged()&&test.isRuleApplied()) {changes.add(test);}
		return changes;
	}
	
	/**
	 * empty function, the graph is not supposed to get changed font
	 */
	public void updateFont() {
	}
	
	/**
	 * with this function you can set the absolute zoom value "SCALE" of the jgraph
	 * as point it uses the middle of the graphArea
	 * @param newValue
	 */
	public void setAbsolutZoomValueToMiddle(double newValue){
		Point p = new Point(this.getWidth()/2, this.getHeight()/2);
		
			this.zoom = 0 + (newValue/5);
			graph.setScale(Math.exp(zoom), p);	
		
	}
	
	/**
	 * returns the actional font used in the graph
	 */
	public Font getFont() {
		if (graph!=null) {
			return graph.getFont();
		}
		else {
			return super.getFont();
		}
	}
	
	/**
	 * getter for the expanded Cell
	 * @return
	 */
	public MyCell getOpened() {
		return opened;
	}
	
	/**
	 * setter for the expanded Cell but doesn't expand a cell itself, for this case you should use expandCell(MyCell)
	 * @param opened
	 */
	public void setOpened(MyCell opened) {
		this.opened = opened;
	}
	
	
	
}
