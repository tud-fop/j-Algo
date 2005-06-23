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