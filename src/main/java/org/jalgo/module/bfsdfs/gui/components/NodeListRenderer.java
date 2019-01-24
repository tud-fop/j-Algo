package org.jalgo.module.bfsdfs.gui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * This class renders the content of the comboBox <code>startNodeChooser</code>
 * used in {@linkplain AlgoTab}. Each entry contains an {@linkplain ImageIcon}
 * which is displayed, and the underlying integer.
 * 
 * @author Florian Dornbusch
 */
@SuppressWarnings(value={"unchecked", "unused"})
class NodeListRenderer
implements ListCellRenderer{
    /**
     * This list contains the original integers of the nodes.
     * Other classes need this list to read the entries of the
     * comboBoxes because they cannot read the image icon.
     */
	List<Integer> theIntList;
	
	protected DefaultListCellRenderer defaultRenderer =
		new DefaultListCellRenderer();

	  public Component getListCellRendererComponent(JList list, Object value,
	      int index, boolean isSelected, boolean cellHasFocus) {
	    ImageIcon theIcon = null;

	    JLabel renderer = 
	    	(JLabel) defaultRenderer.getListCellRendererComponent(
	    			list, value, index, isSelected,cellHasFocus);
	    
	    // data conversion
	    if (value instanceof Object[]) {
	      Object values[] = (Object[]) value;
	      if(values[0] instanceof ImageIcon){
	    	  theIcon = (ImageIcon) values[0];
	      }
	      if(values[1] instanceof List) {
	    	  theIntList = (List<Integer>)values[1];
	      }
	    }
	    
	    if (theIcon != null) {
	      renderer.setIcon(theIcon);
	    }
	    //eliminate the text
	    renderer.setText(null);
	    return renderer;
	  }
}