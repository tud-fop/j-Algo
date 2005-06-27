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
 * Created on 14.05.2005
 *
 */
package org.jalgo.tests.avl.algorithm;

import org.jalgo.module.avl.datastructure.*;
import org.jalgo.module.avl.*;

/**
 * @author Matthias Schmidt
 *
 */
public class CreateRandomTreeTest{

	//TODO: change this to a jUnit test
	public static void main(String[] args) {
		
		SearchTree st = new SearchTree();
		Controller cont = new Controller(st);

		cont.setAVLMode(false);
		cont.createRandomTree(10);
		
		try {while (cont.algorithmHasNextStep()) cont.perform();}
		catch (NoActionException ex) {ex.printStackTrace();}

		// der zufällige Baum inOrder
		System.out.println("Der Baum inOrder!");
		SearchTree tree = new SearchTree();
		tree.printToConsole();

		// der zufällige Baum inLevelOrder
		System.out.println("Der Baum inLevelOrder!");
		System.out.println(tree.exportLevelOrder());
	}
}