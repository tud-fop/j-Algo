package org.jalgo.module.bfsdfs.gui;

import java.util.Collection;

import javax.swing.JOptionPane;

import org.jalgo.module.bfsdfs.gui.graphview.VisualEdge;
import org.jalgo.module.bfsdfs.gui.graphview.VisualGraphElement;
import org.jalgo.module.bfsdfs.gui.graphview.VisualNode;

/**
 * Interface with GUI test methods.
 * Can be removed later.
 * @author Anselm Schmidt
 *
 */
public abstract class GUITest {
	/**
	 * Show a simple message box with a simple message. 
	 * @param string Message to show.
	 * @author Anselm Schmidt
	 */
	public static void msgBox(String string) {
		JOptionPane.showMessageDialog(null, string, "GUITest", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Write a line into System.out or System.err.
	 * @param string Text to write.
	 * @param err If it is <code>True</code>,
	 * the line will be written into System.err.
	 * Otherwise, it will be written into System.out.
	 * @author Anselm Schmidt
	 */
	public static void write(String string, boolean err) {
		if(err) {
			System.err.println(string);
		}
		else {
			System.out.println(string);
		}
	}
	
	/**
	 * Write a collection of <code>VisualGraphElement</code> into System.out or
	 * System.err.
	 * @param collection Collection to write.
	 * @param err If it is <code>True</code>,
	 * the line will be written into System.err.
	 * Otherwise, it will be written into System.out.
	 * @author Anselm Schmidt
	 */
	public static void write(Collection<VisualGraphElement> collection, boolean err) {
		for(VisualGraphElement element : collection) {
			String string = "";
			
			if(element instanceof VisualNode) {
				string = ((VisualNode) element).toString();
			}
			else if(element instanceof VisualEdge) {
				string = ((VisualEdge) element).toString();
			}
			
			if(err) {
				System.err.println(string);
			}
			else {
				System.out.println(string);
			}
		}
	}
}
