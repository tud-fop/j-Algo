package org.jalgo.module.lambda.view;

import java.awt.Dimension;
import javax.swing.JPanel;

/**
 * wrapper class for a JLabel, fixing the setMinimumSize bug
 * 
 * @author ben
 *
 */

public class RenderLabel extends JPanel {
	static final long serialVersionUID = 1L;

	public void clear() {
		this.removeAll();
		this.repaint();
	}
	
	public Dimension getPreferredSize()
    {
      Dimension pSize = super.getPreferredSize();
      Dimension mSize = getMinimumSize();
      int wid, ht;
      
      wid = pSize.width < mSize.width  ? mSize.width : pSize.width;
      ht = pSize.height < mSize.height ? mSize.height: pSize.height;
      return new Dimension(wid, ht);
    }
}
