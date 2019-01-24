/**
 * 
 */
package org.jalgo.module.ebnf.gui.syndia.display;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jalgo.module.ebnf.gui.syndia.AbstractSDGuiController;
import org.jalgo.module.ebnf.gui.trans.RenderElemNotFoundException;
import org.jalgo.module.ebnf.gui.trans.SynDiaElemNotFoundException;
import org.jalgo.module.ebnf.gui.syndia.display.SynDiaPanel;
import org.jalgo.module.ebnf.model.syndia.ElementNotFoundException;
import org.jalgo.module.ebnf.model.syndia.SynDiaElem;
import org.jalgo.module.ebnf.model.syndia.SynDiaSystem;
import org.jalgo.module.ebnf.model.syndia.SyntaxDiagram;
import org.jalgo.module.ebnf.renderer.ReturnDiagram;
import org.jalgo.module.ebnf.renderer.SynDiaRenderer;
import org.jalgo.module.ebnf.renderer.elements.RenderElement;

/**
 * This class is an inherition of a JPanel and represents a container of all
 * SynDiaPanels
 * 
 * @author Andre
 */
@SuppressWarnings("serial")
public class DrawPanel extends JPanel implements Observer, IDrawPanel, MouseWheelListener {

	protected SynDiaRenderer renderer;
	protected List<SynDiaPanel> synDiaPanelList;
	protected Map<RenderElement, SynDiaElem> renderMap;
	protected SynDiaSystem synDiaSystem;
	protected AbstractSDGuiController controller;
	protected boolean autoSize = false;
	protected String displayName;
	protected double factor;


	/**
	 * Initializes all paramaters
	 * 
	 * @param synDiaSystem the syntax diagram system to draw
	 * @param dc the DisplayController
	 * @param synDiaRenderer a <code>SynDiaRenderer</code>
	 */
	public DrawPanel(SynDiaSystem synDiaSystem, AbstractSDGuiController dc,
			SynDiaRenderer synDiaRenderer) {

		displayName = "display";
		factor = 0.2;
		this.setLayout(null);
		this.controller = dc;
		this.renderer = synDiaRenderer;
		this.renderMap = new LinkedHashMap<RenderElement, SynDiaElem>();
		this.synDiaSystem = synDiaSystem;
		this.synDiaPanelList = new ArrayList<SynDiaPanel>();
		this.setBackground(BACKGROUND_COLOR);
		this.addMouseWheelListener(this);
		initListeners();

		this.drawSynDiaSystem(this.synDiaSystem);
		this.repaint();
		
	}

	/**
	 * This method initializes all Listeners on the GUI elements
	 */
	public void initListeners() {

		this.addComponentListener(new ComponentAdapter() {

			public void componentResized(ComponentEvent e) {

				update(null, null);

			}
		});

	}

	/**
	 * Collects all SynDiaPanels for each <code>SyntaxDiagram</code> found and
	 * places it on the DrawPanel
	 * 
	 * @param sds
	 */
	public void drawSynDiaSystem(SynDiaSystem sds) {

		int x = 30;
		int y = 20;

		this.removeAll();
		this.synDiaPanelList.clear();
		this.renderMap.clear();
		for (String s : sds.getLabelsOfVariables()) {

			try {
				SyntaxDiagram d = sds.getSyntaxDiagram(s);

				SynDiaPanel sdp;
				sdp = this.getSyntaxDiagram(d);
				sdp.repaint();
				this.synDiaPanelList.add(sdp);
				this.add(sdp);
				sdp.setVisible(true);
				sdp.setLocation(x, y);

				y += sdp.getHeight() + 2 * renderer.getRenderValues().space;
			} catch (ElementNotFoundException e) {
			}

		}
		this.repaint();

	}

	/**
	 * Calculates the dimension of a Syntax Diagram
	 * 
	 * @param sds
	 * @return The dimension of the Syntax Diagram System
	 */
	public Dimension getSynDiaSystemSize(SynDiaSystem sds) {

		int y = 20;

		int ret_width = 0;
		int ret_height = 0;

		for (String s : sds.getLabelsOfVariables()) {

			try {
				SyntaxDiagram d = sds.getSyntaxDiagram(s);

				Dimension dim = this.renderer.getDiagramSize(d);

				if (dim.width > ret_width)
					ret_width = dim.width;

				ret_height = y + dim.height;
				y += dim.height + 3 * renderer.getRenderValues().space;
			} catch (ElementNotFoundException e) {
			}

		}
		return new Dimension(ret_width + 50, ret_height + 20);

	}

	/**
	 * Calculates a SynDiaPanel with the help of the TransRenderer
	 * 
	 * @param sd A syntax diagram
	 * @return A JPanel containing the drawn SyntaxDiagram
	 */
	private SynDiaPanel getSyntaxDiagram(SyntaxDiagram sd) {

		SynDiaPanel p = new SynDiaPanel();
		ReturnDiagram rd = this.renderer.getRenderedDiagram(sd);

		for (RenderElement renderElem : rd.renderMap.keySet()) {

			// addListener(renderElem, new ColorChangeListener());
			p.add(renderElem);
			renderElem.setVisible(true);
			renderElem.repaint();

		}
		int width = rd.width;
		if (1.3*this.renderer.getRenderValues().getWidthFromString(sd.getName()) > width)
			width = (int) (1.3*this.renderer.getRenderValues().getWidthFromString(
					sd.getName()));

		p.setSize(width, rd.height);
	
		this.renderMap.putAll(rd.renderMap);
		
		return p;

	}

	/**
	 * @param re
	 *            the render element from that you want to get the mapped render
	 *            element
	 * @return the syntax diagram element representing the render element in the
	 *         model
	 * @throws RenderElemNotFoundException
	 */
	public SynDiaElem getSdeFromRe(RenderElement re)
			throws RenderElemNotFoundException {

		if (renderMap.containsKey(re)) {
			return renderMap.get(re);
		} else {
			throw new RenderElemNotFoundException(
					"RenderElement existiert nicht in der Map");
		}
	}

	/**
	 * @param sde
	 *            a SynDiaElem finding itself in the RenderMap
	 * @return the render element representing the syntax diagram element in the
	 *         model
	 * @throws SynDiaElemNotFoundException
	 */
	public RenderElement getReFromSde(SynDiaElem sde)
			throws SynDiaElemNotFoundException {

		if (renderMap.containsValue(sde)) {

			for (RenderElement re : renderMap.keySet()) {

				if (sde == renderMap.get(re))
					return re;

			}
			return null;

		} else {
			throw new SynDiaElemNotFoundException(
					"RenderElement existiert nicht in der Map");
		}
	}

	/**
	 * This method sets the paramater autoSize deciding, whether the diagram
	 * size shall automatically fit to the screen size
	 * 
	 * @param autosize
	 *            true, if the diagram size shall automatically fit to the
	 *            screen size
	 */
	public void setAutoSize(boolean autosize) {

		this.autoSize = autosize;
	}

	/**
	 * A function instead of an Observer to rearrange the system by size
	 * 
	 * @param size
	 *            the size of the Font associated to the syntax diaram
	 */
	public void resizeSystem(int size) {

		renderer.getRenderValues().setFontSize(size);

		this.update(null, null);

	}

	/**
	 * This method returns the actual font size set in the RenderValues
	 * 
	 * @return The actual font size set in the RenderValues
	 */
	public int getFontSize() {

		return this.renderer.getRenderValues().font.getSize();
	}

	/** Tells the Panel whether to render wi
	 * @param withStairs true, if stairs shall be displayed
	 */
	public void setStairs(boolean withStairs) {

		renderer.getRenderValues().withStairs = withStairs;

	}

	/**
	 * @param o
	 * @param arg
	 */
	public void update(Observable o, Object arg) {
		
		if (autoSize) {

			int size = renderer.getRenderValues().font.getSize();
			while ((this.getSynDiaSystemSize(synDiaSystem).width < this
					.getParent().getWidth() || this
					.getSynDiaSystemSize(synDiaSystem).height < this
					.getParent().getHeight())
					&& size < 50) {

				size++;
				renderer.getRenderValues().setFontSize(size);

			}

			while ((this.getSynDiaSystemSize(synDiaSystem).width > this
					.getParent().getWidth() || this
					.getSynDiaSystemSize(synDiaSystem).height > this
					.getParent().getHeight())
					&& size > 10) {

				size--;
				renderer.getRenderValues().setFontSize(size);

			}

			controller.setZoomerValue(size);

		}

		this.drawSynDiaSystem(synDiaSystem);
		this.setPreferredSize(this.getSynDiaSystemSize(synDiaSystem));
		this.getParent().validate();

	}

	/** This method returns a <code>List</code> containing all panels which represent a <code>SyntaxDiagram</code>
	 * @return a <code>List</code> containing all panels which represent a <code>SyntaxDiagram</code>
	 */
	public List<SynDiaPanel> getSynDiaPanelList() {
		return synDiaPanelList;
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		javax.swing.JScrollBar scrollbar = ((JScrollPane)this.getParent().getParent()).getVerticalScrollBar();
		
		if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
	          int totalScrollAmount =
	              e.getUnitsToScroll() * 5;
	          scrollbar.setValue(scrollbar.getValue() + totalScrollAmount);
	      }
	}
	
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		int fontSize = (int) Math.round(factor * this.getParent().getWidth());
		Font f = new Font("Courier", Font.BOLD, fontSize);

		g2d.setFont(f);
		g2d.setColor(FONT_COLOR);
		int y = this.getParent().getHeight() + (int) Math.round(0.2 * fontSize);
		g2d.rotate(-Math.PI * 7 / 180);
		g2d.drawString(displayName, (int) (this.getParent().getWidth()
				- renderer.getRenderValues().getWidthFromString(displayName, f)
				- factor * 0.2 * this.getParent().getWidth()), y);
		g2d.rotate(Math.PI * 7 / 180);
	}
}