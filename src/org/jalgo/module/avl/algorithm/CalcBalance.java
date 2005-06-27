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

package org.jalgo.module.avl.algorithm;
import org.jalgo.module.avl.datastructure.*;

/**
 * @author Ulrike Fischer
 * 
 * The class <code>CalcBalance</code> calculates the balance of a node.
 */

public class CalcBalance extends Command {
	
	private WorkNode wn;
	private AVLNode n;
	private int oldBalance;
	private String oldsection;
	
	/**
	 * @param wn reference to the position in the tree
	 */
	
	public CalcBalance(WorkNode wn) {
		super();
		this.wn=wn;
		results.add(0, "log");
		results.add(1, "2");
	}
	
	/**
	 * Takes the <code>nextToMe</code> node from the WorkNode and calculates the 
	 * balance of this node.
	 */
	
	public void perform() {
		oldsection = (String) results.get(1);
		results.clear();
		
		int rightheight = 0, leftheight = 0;
		n = (AVLNode) wn.getNextToMe();
		if (n.getRightChild()!=null) rightheight = n.getRightChild().getHeight();
		if (n.getLeftChild()!=null) leftheight = n.getLeftChild().getHeight();		
		int balance = rightheight - leftheight;
		oldBalance = n.getBalance();
		n.setBalance(balance);
		n.setVisualizationStatus(Visualizable.BALANCE | Visualizable.NORMAL);
		results.add(0,"log");
		if (oldBalance>balance) {
			results.add(1, "3a");
			results.add(2, balance);
		}
		else if (oldBalance<balance) {
			results.add(1, "3b");
			results.add(2, balance);
			
		}
		else {
			results.add(1,"2");
			results.add(2, balance);
		}

	}
	
	/**
	 * Changes the balance to the one before <code>perform</code>.
	 */
	
	public void undo() {
		n.setBalance(oldBalance);
		wn.setNextToMe(n);
		n.setVisualizationStatus(Visualizable.NORMAL);
		results.add("log");
		results.add(oldsection);
	}
}
