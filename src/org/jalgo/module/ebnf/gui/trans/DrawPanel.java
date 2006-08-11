package org.jalgo.module.ebnf.gui.trans;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import org.jalgo.module.ebnf.gui.GUIConstants;
import org.jalgo.module.ebnf.gui.syndia.display.IDrawPanel;
import org.jalgo.module.ebnf.gui.syndia.display.SynDiaPanel;
import org.jalgo.module.ebnf.gui.trans.event.TransListener;
import org.jalgo.module.ebnf.model.syndia.ElementNotFoundException;
import org.jalgo.module.ebnf.model.syndia.SynDiaElem;
import org.jalgo.module.ebnf.model.syndia.SynDiaSystem;
import org.jalgo.module.ebnf.model.syndia.SyntaxDiagram;
import org.jalgo.module.ebnf.model.trans.TransMap;
import org.jalgo.module.ebnf.renderer.ReturnDiagram;
import org.jalgo.module.ebnf.renderer.TransRenderer;
import org.jalgo.module.ebnf.renderer.elements.RenderElement;
import org.jalgo.module.ebnf.renderer.elements.RenderTrans;

/**
 * This class is an inherition of a JPanel and represents a container of all
 * SynDiaPanels
 * 
 * @author Andre Viergutz
 */
@SuppressWarnings("serial")
public class DrawPanel extends JPanel implements Observer, IDrawPanel {

	private TransRenderer renderer;

	private List<SynDiaPanel> synDiaPanelList;

	private Map<RenderElement, SynDiaElem> renderMap;

	private TransMap transMap;

	private SynDiaSystem synDiaSystem;

	// private TransListener translistener;

	private GUIController guicontroller;

	private boolean autoSize = false;

	/**
	 * Initializes all paramaters
	 * 
	 * @param synDiaSystem the syntax diagram system to draw
	 * @param transMap the map conatining the informatin whether a syntax
	 *            diagram element is transformed or not
	 * @param controller the GUIController which aggregates this class
	 */
	public DrawPanel(SynDiaSystem synDiaSystem, TransMap transMap,
			GUIController controller) {

		this.setLayout(null);
		this.renderer = new TransRenderer();
		this.renderMap = new LinkedHashMap<RenderElement, SynDiaElem>();
		this.transMap = transMap;
		this.synDiaSystem = synDiaSystem;
		this.synDiaPanelList = new ArrayList<SynDiaPanel>();
		this.guicontroller = controller;

		this.setBackground(GUIConstants.STANDARD_COLOR_BACKGROUND);

		initListeners();

		this.drawSynDiaSystem(this.synDiaSystem, transMap);
		this.repaint();

	}

	/**
	 * This method initializes all Listeners on the GUI elements
	 */
	public void initListeners() {

		this.addComponentListener(new ComponentAdapter() {

			public void componentResized(ComponentEvent e) {
				if (autoSize)
					update(null, null);
				else
					repaint();
			}
		});

	}

	/**
	 * Collects all SynDiaPanels for each <code>SyntaxDiagram</code> found and
	 * places it on the DrawPanel
	 * 
	 * @param sds
	 * @param transMap
	 */
	public void drawSynDiaSystem(SynDiaSystem sds, TransMap transMap) {

		int x = 30;
		int y = 20;

		this.removeAll();
		this.synDiaPanelList.clear();
		this.renderMap.clear();
		for (String s : sds.getLabelsOfVariables()) {

			try {
				SyntaxDiagram d = sds.getSyntaxDiagram(s);

				SynDiaPanel sdp;

				sdp = this.getSyntaxDiagram(d, transMap);
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
	 * @param transMap
	 * @return The dimension of the Syntax Diagram System
	 */
	public Dimension getSynDiaSystemSize(SynDiaSystem sds, TransMap transMap) {

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
	 * @param transMap the transMap
	 * @return A JPanel containing the drawn SyntaxDiagram
	 */
	private SynDiaPanel getSyntaxDiagram(SyntaxDiagram sd, TransMap transMap) {

		SynDiaPanel p = new SynDiaPanel();
		ReturnDiagram rd = this.renderer.getRenderedDiagram(sd, transMap);

		for (RenderElement renderElem : rd.renderMap.keySet()) {

			if (renderElem instanceof RenderTrans) {
				renderElem.addMouseListener(new TransListener(guicontroller,
						transMap.get(rd.renderMap.get(renderElem))));
			}

			p.add(renderElem);
			renderElem.setVisible(true);
			renderElem.repaint();

		}

		int width = rd.width;
		if (this.renderer.getRenderValues().getWidthFromString(sd.getName()) > width)
			width = this.renderer.getRenderValues().getWidthFromString(
					sd.getName());

		p.setSize(width, rd.height);

		this.renderMap.putAll(rd.renderMap);

		return p;

	}

	/**
	 * @param re the render element from that you want to get the mapped render
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
	 * @param sde a SynDiaElem finding itself in the RenderMap
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
	 * @param autosize true, if the diagram size shall automatically fit to the
	 *            screen size
	 */
	public void setAutoSize(boolean autosize) {

		this.autoSize = autosize;
	}

	/**
	 * A function instead of an Observer to rearrange the system by size
	 * 
	 * @param size the size of the Font associated to the syntax diaram
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
			while ((this.getSynDiaSystemSize(synDiaSystem, transMap).width < this
					.getParent().getWidth() || this.getSynDiaSystemSize(
					synDiaSystem, transMap).height < this.getParent()
					.getHeight())
					&& size < 50) {

				size++;
				renderer.getRenderValues().setFontSize(size);

			}

			while ((this.getSynDiaSystemSize(synDiaSystem, transMap).width > this
					.getParent().getWidth() || this.getSynDiaSystemSize(
					synDiaSystem, transMap).height > this.getParent()
					.getHeight())
					&& size > 10) {

				size--;
				renderer.getRenderValues().setFontSize(size);

			}

			guicontroller.getControlPanel().setZoomerValue(size);

		}

		this.drawSynDiaSystem(synDiaSystem, transMap);
		this.setPreferredSize(this.getSynDiaSystemSize(synDiaSystem, transMap));
		guicontroller.validateComponents();
		if (guicontroller.isExplanationShown() && !transMap.isTransformed()) {

			guicontroller.setExplanationShow(true);

		} else {

			guicontroller.showExplanation(null);

		}

	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		int fsize = (int) Math.round(0.3*this.getParent().getWidth());
		Font trans = new Font("Courier", Font.BOLD, fsize);

		g2d.setFont(trans);
		g2d.setColor(GUIConstants.STANDARD_COLOR_BACKGROUND_TEXT);
		int y = this.getParent().getHeight() + (int) Math.round(0.2*fsize);
		g2d.rotate(-Math.PI * 7/180);
		g2d.drawString("trans",
				this.getParent().getWidth()
						- renderer.getRenderValues().getWidthFromString(
								"trans", trans), y );
		g2d.rotate(Math.PI * 7/180);
	}

}
