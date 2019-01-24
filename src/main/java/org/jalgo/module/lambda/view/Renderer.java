package org.jalgo.module.lambda.view;

import java.awt.FlowLayout;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.jalgo.module.lambda.Constants;
import org.jalgo.module.lambda.model.FormatString;

/**
 * Creates instances of RenderElement to display the current working term at the renderLabel.
 */
public class Renderer {
	
	private JComponent renderTarget;
	private JLabel opcode;
	
	public Renderer(JComponent rt) {
		this.renderTarget = rt;
		renderTarget.setLayout(new FlowLayout());
		((FlowLayout)renderTarget.getLayout()).setVgap(0);
		((FlowLayout)renderTarget.getLayout()).setHgap(2);
		((FlowLayout)renderTarget.getLayout()).setAlignment(FlowLayout.LEADING);
		
		opcode = new JLabel();
		opcode.setFont(GUIController.TERM_FONT);
		opcode.setText(Constants.ARROW_R + " ");
	}

	/**
	 * adds the RenderElement re to the renderTarget (i.e. renderLabel) and makes it visible.
	 * @param re the RenderElement to be displayed
	 */
	public void drawRenderElement(RenderElement re) {
		 renderTarget.add(re);
		 re.setVisible(true);
	}
	/**
	 * Break the outputString down and render it as RenderElements.
	 * 
	 * @param it iterator to the outputString
	 * @param parent GUIController to which the RenderElements should be added
	 */
	public List<RenderElement> drawTerm(Iterator<FormatString> it, GUIController parent) {
		
		renderTarget.add(opcode);
		
		List<RenderElement> l = new LinkedList<RenderElement>();
		
		// Term zerlegen und stueckweise rendern		
		RenderElement re;
		FormatString fs;
		
		while(it.hasNext()) {
			fs = it.next();
			re = new RenderElement(fs.getFormat(), fs.getText(), fs.getPosition(), parent);
			drawRenderElement(re);
			l.add(re);
		}
		
		renderTarget.validate();
		
		return l;
	}
	
	public void setOpcode(String opcodeString) {
		if (opcodeString == "" || opcodeString == "=")
			opcode.setText(opcodeString + " ");
		else
			opcode.setText(Constants.ARROW_R + opcodeString + " ");
	}
	
	/**
	 * Sets the renderTarget where the drawTerm method draws the RenderElements.
	 * @param rt the new renderTarget
	 */
	public void setRenderTarget(JComponent rt) {
		this.renderTarget = rt;
	}
	
	/**
	 * Removes all RenderElements from the renderTarget.
	 */
	public void clearRenderTarget() {
		renderTarget.removeAll();
		renderTarget.repaint();
		renderTarget.validate();
	}
}