package org.jalgo.module.hoare.view;

import org.jgraph.graph.DefaultGraphCell;

/**
 * This a <code>DefaultGraphCell</code> from JGraph package extended
 * with an ID, a level in the graph and a position (left , right, middle) over the parent cell
 * 
 * @author tomas
 *
 */
public class MyCell extends DefaultGraphCell{
	private static final long serialVersionUID = 3982472968049935711L;
	private int ID;
	private int level;
	private int pos;
	private double ratio;
	private boolean rounded;
		
	public static int setLeft(){return -1;}
	public static int setRight(){return 1;}
	public static int setMiddle(){return 0;}
	
	
	/**
	 * here you can set name, ID, position, level, and rounded right in the constructor
	 * @param name
	 * @param ID
	 * @param pos
	 * @param level
	 * @param rounded
	 */
	public MyCell(String name, int ID, int pos, int level, boolean rounded){
		super(name);
		this.ID = ID;
		this.level = level;
		this.pos = pos;
		this.rounded = rounded;
	}	
	/**
	 * getter for the Cell ID
	 * @return the iD
	 */
	public int getID() {
		return ID;
	}

	/**
	 * setter for the Cell ID
	 * @param id the iD to set
	 */
	public void setID(int id) {
		ID = id;
	}
	/**
	 * getter for the Level the cell is supposed to be painted in the graph
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}
	/**
	 * setter for the Level the cell is supposed to be painted in the graph
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	/**
	 * getter for the Position the cell should be in relation to the "parent cell", left of it = -1, middle = 0, right = 1
	 * @return the position (1 or 0 or -1)
	 */
	public int getPos(){ return pos;}
	/**
	 * getter for the Ratio the child cells are positioned with
	 * @return
	 */
	public double getRatio() {
		return ratio;
	}
	/**
	 * setter for the Ratio the child cells are positioned with
	 * @param d
	 */
	public void setRatio(double d) {
		this.ratio = d;
	}
	/**
	 * getter for isRounded feature to know if the cell is supposed to be painted round or not
	 * @return
	 */
	public boolean isRounded() {
		return rounded;
	}
	/**
	 * setter for isRounded feature to know if the cell is supposed to be painted round or not
	 * @param rounded
	 */
	public void setRounded(boolean rounded) {
		this.rounded = rounded;
	}
			
	
	
}
