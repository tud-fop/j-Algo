package org.jalgo.module.hoare.view;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jalgo.module.hoare.constants.TextStyle;
import org.jalgo.module.hoare.model.VerificationFormula;
import org.jalgo.module.hoare.view.formula.FormulaViewer;

/**
 * Provides panels for the vertices of the verification tree.
 * 
 * @author Antje, Thomas
 *
 */
public class VertexPanelFactory {
	
	/**
	 * The class is not supposed to be instanciated as there are only static methods.
	 *
	 */
	private VertexPanelFactory() {
	}
	
	/**
	 * Returns a <code>JPanel</code> for the specified cell.
	 * @param gui the gui controller
	 * @param cell the cell the <code>JPanel</code> is for
	 * @param isSelected <code>true</code> if the cell is selected, otherwise false
	 * @param isDetailed <code>true</code> if the cell is expanded, otherwise false
	 * @return a <code>JPanel</code> for the specified cell
	 */
	public static JPanel getVertexPanel(View gui, MyCell cell, boolean isSelected, boolean isDetailed) {
		if (isDetailed) {
			return getDetailedVertexPanel(gui, cell, isSelected);
		}
		else {
			return getCompactVertexPanel(gui, cell, isSelected);
		}
	}
	
	/**
	 * Returns a <code>JPanel</code> for the specified cell which is assumed not to be expanded.
	 * @param gui the gui controller
	 * @param cell the cell the <code>JPanel</code> is for
	 * @param isSelected <code>true</code> if the cell is selected, otherwise false
	 * @return a <code>JPanel</code> for the specified cell which is assumed not to be expanded.
	 */
	public static JPanel getCompactVertexPanel(View gui, MyCell cell, boolean isSelected) {
		
		int id = cell.getID();
		VerificationFormula vf = gui.getModel().getVerificationFormula(id);
		if (cell==null) throw new NullPointerException("ups! cell is null.");
		if (vf==null) throw new NullPointerException("ups! Formula is null.");
		float fontSize = (float)gui.getWorkScreen().getGraph().getFont().getSize2D();
		Font assertionFont = gui.getMainFont().deriveFont(fontSize);
		Font sourceCodeFont = gui.getSourceCodeFont().deriveFont(fontSize);
		
		JPanel p = new VertexPanel(false);
		String preLabelString = vf.getPreAssertion(TextStyle.SHORT);
		if (!vf.isImplication()) {
			preLabelString = "{" + preLabelString + "} ";
		}
		JLabel preLabel = new JLabel(preLabelString);
		preLabel.setFont(assertionFont.deriveFont(Font.BOLD));
		JLabel sourceLabel = new JLabel(vf.getCode(false));
		sourceLabel.setFont(sourceCodeFont.deriveFont(Font.ITALIC));
		String postLabelString = vf.getPostAssertion(TextStyle.SHORT);
		if (!vf.isImplication()) {
			postLabelString = " {" + postLabelString + "}";
		}
		JLabel postLabel = new JLabel(postLabelString);
		postLabel.setFont(assertionFont.deriveFont(Font.BOLD));
		
		p.add(preLabel);
		p.add(sourceLabel);
		p.add(postLabel);
		
		setBounds(p);
		
		return p;
	}
	
	/**
	 * Returns a <code>JPanel</code> for the specified cell which is assumed to be expanded.
	 * @param gui the gui controller
	 * @param cell the expanded cell the <code>JPanel</code> is for
	 * @param isSelected <code>true</code> if the cell is selected, otherwise false
	 * @return a <code>JPanel</code> for the specified cell which is assumed to be expanded.
	 */
	public static JPanel getDetailedVertexPanel(View gui, MyCell cell, boolean isSelected) {
		int id = cell.getID();
		VerificationFormula vf = gui.getModel().getVerificationFormula(id);
		float fontSize = (float)gui.getWorkScreen().getGraph().getFont().getSize2D();
		Font assertionFont = gui.getMainFont().deriveFont(fontSize);
		Font sourceCodeFont = gui.getSourceCodeFont().deriveFont(fontSize);
		
		JPanel p = new VertexPanel(true);
		// if there is still a (?) in the preAssertion do not draw
		JLabel preFV=null;
		if (vf.hasFilledPreAssertion()){
			FormulaViewer newFV = new FormulaViewer(vf.getPreAssertion(TextStyle.SOURCE), 
					assertionFont.deriveFont(Font.BOLD));
			if (!vf.isImplication()) {
				newFV.setPreString("{");
				newFV.setPostString("}");
			}
			newFV.setColor(WSVar.HIGHLIGHT_COLOR_PRE);
			newFV.initSize();
			preFV = newFV;
		}else{
			String preLabelString = vf.getPreAssertion(TextStyle.FULL);
			if (!vf.isImplication()) {
				preLabelString = "{" + preLabelString + "} ";
			}
			preFV = new JLabel(preLabelString);
			preFV.setFont(assertionFont.deriveFont(Font.BOLD));
			preFV.setForeground(WSVar.HIGHLIGHT_COLOR_PRE);
		}	

		
		JLabel sourceLabel = new JLabel(vf.getCode(true));
		sourceLabel.setFont(sourceCodeFont.deriveFont(Font.ITALIC));
		// if there is still a (?) in the postAssertion do not draw
		JLabel postFV=null;
		if (vf.hasFilledPostAssertion()){
			FormulaViewer newFV = new FormulaViewer(vf.getPostAssertion(TextStyle.SOURCE), 
				                                       assertionFont.deriveFont(Font.BOLD));
		 	if (!vf.isImplication()) {
		 		newFV.setPreString("{");
		 		newFV.setPostString("}");
		 	}
			newFV.setColor(WSVar.HIGHLIGHT_COLOR_POST);
			newFV.initSize();
			postFV=newFV;
		}else{
			String postLabelString = vf.getPostAssertion(TextStyle.FULL);
			if (!vf.isImplication()) {
				postLabelString = "{" + postLabelString + "} ";
			}
			postFV = new JLabel(postLabelString);
			postFV.setFont(assertionFont.deriveFont(Font.BOLD));
			postFV.setForeground(WSVar.HIGHLIGHT_COLOR_POST);
		}
		
		p.add(preFV);
		p.add(sourceLabel);
		p.add(postFV);
		
		setBounds(p);
		
		return p;
	}
	
	/**
	 * Arranges the components of the specified <code>JPanel</code>.
	 * It is assumed that the <code>JPanel</code> was generated by a method of this class, otherwise this method will probably not work.
	 * @param p the <code>JPanel</code> whose components will be arranged
	 */
	public static void setBounds(JPanel p) {
		if (p instanceof VertexPanel) {
			boolean detailed = ((VertexPanel)p).detailed;
			int dist = 5;
			int x = dist;
			int y = dist;
			int maxX = 0;
			int maxY = 0;
			if (detailed) {
				for (int i=0; i<3; i++) {
					Component c = p.getComponent(i);
					int width = (int)Math.ceil(c.getPreferredSize().getWidth());
					int height = (int)Math.ceil(c.getPreferredSize().getHeight());
					// only the baseline width is computed, therefore extra width has to be added for overhanging letters in italic style
					if ((c instanceof JLabel)&(c.getFont().isItalic())) {
						width = width+(int)Math.ceil(c.getFontMetrics(c.getFont()).getAscent()/2);
					}
					c.setBounds(x, y, width, height);
					y = y + height;
					y = y + dist;
					if (width>maxX) maxX = width;
				}
				maxX = maxX + 2*dist;
				maxY = y;
				for (int i=0; i<3; i++) {
					Component c = p.getComponent(i);
					c.setBounds((maxX-c.getWidth())/2, c.getY(), c.getWidth(), c.getHeight());
				}
			}
			else {
				for (int i=0; i<3; i++) {
					Component c = p.getComponent(i);
					int width = (int)Math.ceil(c.getPreferredSize().getWidth());
					int height = (int)Math.ceil(c.getPreferredSize().getHeight());
					// only the baseline width is computed, therefore extra width has to be added for overhanging letters in italic style
					if ((c instanceof JLabel)&(c.getFont().isItalic())) {
						width = width+(int)Math.ceil(c.getFontMetrics(c.getFont()).getAscent()/2);
					}
					c.setBounds(x, y, width, height);
					x = x + width;
					x = x + dist;
					if (height>maxY) maxY = height;
				}
				maxX = x;
				maxY = maxY + 2*dist;
			}
			p.setSize(maxX, maxY);
			p.setPreferredSize(p.getSize());
			p.setMinimumSize(p.getSize());
			p.setMaximumSize(p.getSize());
		}
	}
	
	/**
	 * Returns the component displaying the pre assertion
	 * @param p the <code>JPanel</code> displaying the vertex
	 * @return the pre assertion component
	 */
	public static Component getPreAssertionComponent(JPanel p) {
		return p.getComponent(0);
	}
	
	/**
	 * Returns the component displaying the source code part
	 * @param p the <code>JPanel</code> displaying the vertex
	 * @return the source code part component
	 */
	public static Component getSourceCodePartComponent(JPanel p) {
		return p.getComponent(1);
	}
	
	/**
	 * Returns the component displaying the post assertion
	 * @param p the <code>JPanel</code> displaying the vertex
	 * @return the post assertion component
	 */
	public static Component getPostAssertionComponent(JPanel p) {
		return p.getComponent(2);
	}
	
	/**
	 * Changes the <code>JPanel</code> to displaying a selected vertex.
	 * @param p the <code>JPanel</code> displaying the vertex
	 */
	public static void setSelected(JPanel p) {
		getPreAssertionComponent(p).setForeground(WSVar.HIGHLIGHT_COLOR_PRE);
		getPostAssertionComponent(p).setForeground(WSVar.HIGHLIGHT_COLOR_POST);
	}
	
	/**
	 * Changes the <code>JPanel</code> to displaying a vertex that is not selected.
	 * @param p the <code>JPanel</code> displaying the vertex
	 */
	public static void setNotSelected(JPanel p) {
		getPreAssertionComponent(p).setForeground(WSVar.NO_HIGHLIGHT_COLOR);
		getPostAssertionComponent(p).setForeground(WSVar.NO_HIGHLIGHT_COLOR);
	}
	
	/**
	 * <code>JPanel</code> that remembers wether it shows a detailed or a compact view and that is not drawn itself (but its children are).
	 * 
	 * @author antje
	 *
	 */
	protected static class VertexPanel extends JPanel {
		private static final long serialVersionUID = 5581424971403601373L;
		
		/**
		 * If <code>true</code> the <code>VertexPanel</code> shows a detailed view, otherwise it shows a compact view.
		 */
		protected boolean detailed;
		
		/**
		 * Creates a new instance of <code>VertexPanel</code>.
		 * @param detailed must be <code>true</code> if the <code>VertexPanel</code> shows a detailed view, otherwise it must be <code>false</code>
		 */
		public VertexPanel(boolean detailed)  {
			this.detailed = detailed;
		}
		
		/**
		 * The border of the panel is not drawn.
		 */
		protected void paintBorder(Graphics g) {
			// do nothing
		}

		/**
		 * The panel itself is not drawn.
		 */
		protected void paintComponent(Graphics g) {
			// do nothing
		}
		
	}
	
}
