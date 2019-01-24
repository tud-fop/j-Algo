/**
 * 
 */
package org.jalgo.module.ebnf.renderer;

import java.awt.Dimension;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jalgo.module.ebnf.model.syndia.SynDiaElem;
import org.jalgo.module.ebnf.model.syndia.SyntaxDiagram;
import org.jalgo.module.ebnf.model.syndia.TerminalSymbol;
import org.jalgo.module.ebnf.model.syndia.Variable;
import org.jalgo.module.ebnf.renderer.elements.RenderBase;
import org.jalgo.module.ebnf.renderer.elements.RenderElement;
import org.jalgo.module.ebnf.renderer.elements.RenderName;

/**
 * This class gets a syntax diagram, a drawing area (JComponent) and some
 * other optional params. As result you will get a drawn syntax diagram (system) on the
 * given area.
 * 
 * @author Andre Viergutz
 */
public abstract class SynDiaRenderer {

	protected Map<RenderElement, SynDiaElem> renderMap = new  LinkedHashMap<RenderElement,SynDiaElem>();
		
	/**
	 *  All needed parameters to render a SyntaxDiagram
	 */
	protected RenderValues rv = new RenderValues();

	
	
	/** Creates a Map containing all RenderElements to be rendered including RenderTrans
	 * @param sd A SyntaxDiagram
	 * @return A map with all elements to be rendered
	 */
	public ReturnDiagram getRenderedDiagram(SyntaxDiagram sd) {
		
		renderMap.clear();
		
		VirtualDim vdim = getMyDim(sd);
		
		//NAME OF DIAGRAM
		RenderName rn;
		if (sd.getName().equals(sd.getMySynDiaSystem().getStartDiagram()))
			rn = new RenderName(sd.getName(), true);
		else
			rn = new RenderName(sd.getName(), false);
		rn.setRenderValues(rv);
		rn.setSize((int) (1.3*rv.getWidthFromString(sd.getName())) + 20, 2 * rv.radius);
		rn.setLocation(0, 0);
		rn.setVisible(true);
		
		renderMap.put(rn, null);
				
		
		//BASE LINE + SIZE OF DIAGRAM
		RenderBase rb;
		rb = new RenderBase();
		rb.setRenderValues(rv);
		rb.setSize(vdim.width + 2 * rv.radius, 4 * rv.radius);
		rb.setLocation(0, 0);
		rb.setVisible(true);
		
		renderMap.put(rb, null);
		
		
		ReturnDiagram rd = new ReturnDiagram();
		rd.renderMap = renderMap;
		rd.width = vdim.width + 2*rv.radius;
		if (rv.getWidthFromString(sd.getName()) > rd.width)
			rd.width = rv.getWidthFromString(
					sd.getName());
		rd.height = vdim.virtualHeight + 2*rv.radius;
		
		return rd;
	}
	
	/** This method is specific for the class is used as a template for getting the dimension of an element
	 * @param sd the SyntaxDiagram to render
	 * @return the VirtualDim object containing width, height as well as the virtualHeight
	 */
	protected abstract VirtualDim getMyDim(SyntaxDiagram sd);
	
	/**
	 * Calculates the size of the given diagram
	 * 
	 * @param sd A SyntaxDiagram
	 * @return A map with all elements to be rendered
	 */
	public Dimension getDiagramSize(SyntaxDiagram sd) {

		VirtualDim vdim = getDimFromElement(sd.getRoot(), rv.radius,
				2 * rv.radius, true, false);

		Dimension dimension = new Dimension();

		dimension.width = vdim.width + 2 * rv.radius;
		dimension.height = vdim.virtualHeight + 2 * rv.radius;

		return dimension;
	}
	
	
	/**
	 * Caclulates the Dimension of a given element by collecting informations
	 * from its children [and puts all elements (besides Concatenation) on the
	 * way into the renderList]
	 * 
	 * @param e A SyntaxDiagram element
	 * @param x x-position of the element
	 * @param y y-position of the element
	 * @param direction true: read left to right, false: read right to left
	 * @param render A paramter, telling the method, whether creating
	 *            RenderElements or just calculate the Dimension
	 * @return Width of e
	 */
	protected abstract VirtualDim getDimFromElement(SynDiaElem e, int x, int y, boolean direction, 
			boolean render);

	/**
	 * @param type One of the types to be found in the RenderValues
	 * @param s the SynDiaElem to be added
	 * @param x the x-position of the element
	 * @param y the y-position of the element
	 * @param width the width of the element
	 * @param height the height of the element
	 * @param label if terminal or variable, the name, otherwise null
	 */
	protected void addElemToRenderList(int type, SynDiaElem s, int x, int y, int width,
			int height, String label) {

		RenderElement renderElem = RenderElementFactory.createElement(type,
				label, null);
		
		renderElem.setRenderValues(rv);
		
		//Shadow for TerminalSymbol and Variable
// The if-branch will never be executed... (michael p, 31/10/08) 		
//		if (renderElem.getClass() == TerminalSymbol.class || renderElem.getClass() == Variable.class) {
//			renderElem.setSize(width+3, height+3);
//			renderElem.setLocation(x-1, y-1);
//		} else {
			renderElem.setSize(width, height);
			renderElem.setLocation(x, y);
//		}
		//new position for the element
		renderElem.update();
		
		//hinzufügen zur renderMap
		renderMap.put(renderElem, s);
	
	}
	
	/** This method returns the RenderValues object containing all render informations
	 * @return The RenderValues object containing all render informations
	 */
	public RenderValues getRenderValues() {
		
		return rv;
	}
}
