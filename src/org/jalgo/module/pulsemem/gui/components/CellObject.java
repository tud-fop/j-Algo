/**
 * 
 */
package org.jalgo.module.pulsemem.gui.components;

import javax.swing.*;

/**
 * @author Karsten
 *
 */
public class CellObject extends JLabel {
	
	//private int alignment = SwingConstants.CENTER;
	
	public CellObject(String text) {
		super("<html>"+text+"</html>");
		setVerticalAlignment(SwingConstants.CENTER);
		setHorizontalAlignment(SwingConstants.CENTER);
		setOpaque(true);
	}
	
	public CellObject(String text,int alignment) {
		super("<html>"+text+"</html>");
		setVerticalAlignment(SwingConstants.CENTER);
		setHorizontalAlignment(alignment);
		setOpaque(true);
	}
	
	public CellObject(String line1,String line2) {
		super("<html>"+line1+"<br>"+line2+"</html>");
		setVerticalAlignment(SwingConstants.CENTER);
		setHorizontalAlignment(SwingConstants.CENTER);
		setOpaque(true);
	}
	
	public CellObject(String line1,String line2, int alignment) {
		super("<html>"+line1+"<br>"+line2+"</html>");
		setVerticalAlignment(SwingConstants.CENTER);
		setHorizontalAlignment(alignment);
		setOpaque(true);
	}
	
/*	public int getAlignment() {
		return alignment;
	}
*/
}
