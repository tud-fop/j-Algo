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
		
		// step a) to b)
		n7.deleteOutgoing(e7_2);
		n7.deleteOutgoing(e7_8);
		n11.deleteOutgoing(n7);
		// HIER WEITER !!
	}
}
