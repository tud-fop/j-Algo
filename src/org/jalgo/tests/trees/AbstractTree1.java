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
 * Created on 30.05.2005
 *
 */
package org.jalgo.tests.trees;

import org.jalgo.main.trees.Tree;

/**
 * @author Michael Pradel
 *  
 */
public class AbstractTree1 {

	public static void main(String[] args) {
		Tree t11 = new Tree("11");
		Tree t7 = new Tree("7");
		Tree t2 = new Tree("2");
		Tree t8 = new Tree("8");
		Tree t1 = new Tree("1");
		Tree t5 = new Tree("5");
		Tree t4 = new Tree("4");
		Tree t14 = new Tree("14");
		Tree t15 = new Tree("15");
		
		t11.addChild(t7);
		t11.addChild(t14);
		t7.addChild(t2);
		t7.addChild(t8);
		t2.addChild(t1);
		t2.addChild(t5);
		t5.addChild(t4);
		t14.addChild(t15);
		
		System.out.println("Children of 11: " + t11.getChildren());	
		System.out.println("Parent of 4: " + t4.getParent());
		System.out.println("Parent of 2: " + t2.getParent());
		
		System.out.println("Node from t4: " + t4.getNode());
		
		System.out.println("Edge from 11 to 7: " + t11.getEdgeTo(t7));
		System.out.println("Edge from 15 to its parent: " + t15.getEdgeToParent());
		System.out.println("Edge from 14 to 15 (should be equal): " + t14.getEdgeTo(t15));
	}
}
