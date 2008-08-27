package org.jalgo.module.hoare.view;

import org.jgraph.graph.DefaultEdge;

/**
 * This a <code>DefaultEdge</code> from JGraph package extended
 * with a parentCell and a sourceCell<br>
 * <br>
 * !! take care, source and parent cell of the MyEdge is used logically swapped in WSgraph for programming reasons 
 * 
 * @author Tomas
 *
 */
public class MyEdge extends DefaultEdge{

	private static final long serialVersionUID = 3649276324756278498L;
	
	private MyCell sourceCell, targetCell;
	
	/**
	 * this is the getter for the source cell
	 * !! take care, the source cell in this case is not the cell on the beginning of the arrow,
	 * it is the lower painted cell of the both cells involved in this arrow  
	 * @return the sourceCell
	 */
	public MyCell getSourceCell() {
		return sourceCell;
	}

	/**
	 * this is the setter for the source cell
	 * !! take care, the source cell in this case is not the cell on the beginning of the arrow,
	 * it is the lower painted cell of the both cells involved in this arrow 
	 * @param sourceCell the sourceCell to set
	 */
	public void setSourceCell(MyCell sourceCell) {
		this.sourceCell = sourceCell;
	}

	/**
	  * this is the getter for the target cell
	 * !! take care, the source cell in this case is not the cell at the end of the arrow,
	 * it is the higher painted cell of the both cells involved in this arrow 
	 * @return the targetCell
	 */
	public MyCell getTargetCell() {
		return targetCell;
	}

	/**
	 * this is the setter for the target cell
	 * !! take care, the source cell in this case is not the cell at the end of the arrow,
	 * it is the higher painted cell of the both cells involved in this arrow 
	 * @param targetCell the targetCell to set
	 */
	public void setTargetCell(MyCell targetCell) {
		this.targetCell = targetCell;
	}

}
