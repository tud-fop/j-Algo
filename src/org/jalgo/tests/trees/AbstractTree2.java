/*
 * Created on 02.06.2005
 *
 */
package org.jalgo.tests.trees;

import org.jalgo.main.trees.Edge;
import org.jalgo.main.trees.Node;

/**
 * @author Michael Pradel
 *
 */
public class AbstractTree2 {

	public static void main(String[] args) {
		Node n1 = new Node();
		Node n2 = new Node();
		Node n4 = new Node();
		Node n5 = new Node();
		Node n7 = new Node();
		Node n8 = new Node();
		Node n11 = new Node();
		Node n14 = new Node();
		Node n15 = new Node();

		Edge e11_7 = new Edge(n11, n7);
		Edge e2_1 = new Edge(n2, n1);
		Edge e2_5 = new Edge(n2, n5);
		Edge e5_4 = new Edge(n5, n4);
		Edge e7_2 = new Edge(n7, n2);
		Edge e7_8 = new Edge(n7, n8);
		Edge e11_14 = new Edge(n11,n14);
		Edge e14_15 = new Edge(n14,n15);

		/* RIGHT ROTATION */
		
		// from a) to b)
		n7.deleteOutgoing(e7_2);
		n7.deleteOutgoing(e7_8);
		n2.getParent().setParent(null);
		n8.getParent().setParent(null);
		n11.deleteOutgoing(e11_7);
		Edge e7_11 = new Edge(n7,n11);
		
		// step b)
		System.out.println("step b)");
		System.out.println("Parent from 2: " + n2.getParent());
		System.out.println("Parent from parent from 2: " + n2.getParent().getParent());
		System.out.println("Node 11: " + n11);
		System.out.println("Edge between 7 and 11: " + e7_11);
		System.out.println("Children of 7: " + n7.getOutgoing());
		
		// from b) to c)
		Edge e11_8 = new Edge(n11,n8);
		
		// step c)
		System.out.println("step c)");
		System.out.println("Parent from 8: " + n8.getParent());
		System.out.println("Parent from parent from 8: " + n8.getParent().getParent());
		System.out.println("Children from 11: " + n11.getOutgoing());
		System.out.println("Edge between 11 and 8: " + e11_8);
		
		// from c) to d)
		e7_2 = new Edge(n7,n2);
		
		// step d)
		System.out.println("Node 7: " + n7);
		System.out.println("Parent from parent from 2: " + n2.getParent().getParent());
	}
}
