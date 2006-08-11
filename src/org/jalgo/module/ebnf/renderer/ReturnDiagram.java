/**
 * 
 */
package org.jalgo.module.ebnf.renderer;

import java.util.Map;
import java.util.LinkedHashMap;
import org.jalgo.module.ebnf.model.syndia.SynDiaElem;
import org.jalgo.module.ebnf.renderer.elements.RenderElement;

/**
 * This class is a data container and holds a renderMap, the width and height of
 * a syntax diagram and is used by render method
 * 
 * @author Andre
 */
public class ReturnDiagram {

	/**
	 * A Map where each rendered element is associated to a syntax diagram
	 * element in the model
	 */
	public Map<RenderElement, SynDiaElem> renderMap = new LinkedHashMap<RenderElement, SynDiaElem>();

	/**
	 * the width of the syntax diagram
	 */
	public int width = 0;

	/**
	 * the height of a syntax diagram
	 */
	public int height = 0;

}
