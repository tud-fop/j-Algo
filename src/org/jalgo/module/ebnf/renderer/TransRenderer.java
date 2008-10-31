/**
 * 
 */
package org.jalgo.module.ebnf.renderer;

import java.awt.Dimension;
import java.awt.Font;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jalgo.module.ebnf.gui.EbnfFont;
import org.jalgo.module.ebnf.gui.FontNotInitializedException;
import org.jalgo.module.ebnf.gui.ebnf.EbnfRenderer;
import org.jalgo.module.ebnf.model.syndia.Branch;
import org.jalgo.module.ebnf.model.syndia.Concatenation;
import org.jalgo.module.ebnf.model.syndia.Repetition;
import org.jalgo.module.ebnf.model.syndia.SynDiaElem;
import org.jalgo.module.ebnf.model.syndia.SyntaxDiagram;
import org.jalgo.module.ebnf.model.syndia.TerminalSymbol;
import org.jalgo.module.ebnf.model.syndia.Variable;
import org.jalgo.module.ebnf.model.trans.TransMap;
import org.jalgo.module.ebnf.renderer.elements.RenderBase;
import org.jalgo.module.ebnf.renderer.elements.RenderElement;
import org.jalgo.module.ebnf.renderer.elements.RenderName;

/**
 * This class gets a syntax diagram, a drawing area (JComponent) and some other
 * optional params. As result you will get a drawn syntax diagram (system) on
 * the given area.
 * 
 * @author Andre Viergutz
 */
public class TransRenderer extends SynDiaRenderer {

	
	private TransMap transMap = new TransMap();
	
	/**
	 * Creates a Map containing all RenderElements to be rendered including
	 * RenderTrans
	 * 
	 * @param sd
	 *            A SyntaxDiagram
	 * @param transMap
	 *            A map where each syntax diagram element is associated to a
	 *            Term, showing if the element is already transformed or no
	 * @return A map with all elements to be rendered
	 */
	public ReturnDiagram getRenderedDiagram(SyntaxDiagram sd, TransMap transMap) {

		renderMap.clear();
		this.transMap = transMap;

		VirtualDim vdim = getMyDim(sd);

		// BASE LINE + SIZE OF DIAGRAM
		RenderBase rb;
		rb = new RenderBase();
		rb.setRenderValues(rv);
		rb.setSize(vdim.width + 2 * rv.radius, 4 * rv.radius);
		rb.setLocation(0, 0);
		rb.setVisible(true);

		renderMap.put(rb, null);

		// NAME OF DIAGRAM
		RenderName rn;
		if (sd.getName().equals(sd.getMySynDiaSystem().getStartDiagram()))
			rn = new RenderName(sd.getName(), true);
		else
			rn = new RenderName(sd.getName(), false);
		rn.setRenderValues(rv);
		rn.setSize(rv.getWidthFromString(sd.getName()), 2 * rv.radius);
		rn.setLocation(0, 0);
		rn.setVisible(true);

		renderMap.put(rn, null);

		ReturnDiagram rd = new ReturnDiagram();
		rd.renderMap = renderMap;
		rd.width = vdim.width + 2 * rv.radius;
		rd.height = vdim.virtualHeight + 2 * rv.radius;

		return rd;
	}
	
	protected VirtualDim getMyDim(SyntaxDiagram sd) {
		return getDimFromElement(sd.getRoot(), rv.radius,
				2 * rv.radius, 0, true, true, true);
	}

	/**
	 * Calculates the size of the given diagram
	 * 
	 * @param sd
	 *            A SyntaxDiagram
	 * @return A map with all elements to be rendered
	 */
	public Dimension getDiagramSize(SyntaxDiagram sd) {

		VirtualDim vdim = getDimFromElement(sd.getRoot(), rv.radius,
				2 * rv.radius, 0, true, true, false);

		Dimension dimension = new Dimension();

		dimension.width = vdim.width + 2 * rv.radius;
		dimension.height = vdim.virtualHeight + 2 * rv.radius;

		return dimension;
	}

	/**
	 * This method checks whether the given element is already transformed or
	 * not. In case of an element is to transform a RenderTrans is created,
	 * otherwise the other fitting element
	 * 
	 * @param e
	 *            a <code>SynDiaElem></code> to be calculated
	 * @param x
	 *            the start position
	 * @param y
	 *            the start position
	 * @param min_width
	 *            if the width of a parental element is greater than the element
	 *            itself, the min_width is used
	 * @param direction
	 *            changes its value on each repetition
	 * @param stairs
	 *            boolean, telling wether to render or without stairs
	 * @param render
	 *            indicates whether RenderElements should be created or not.
	 * @return a virtual dimension, containing the real coordinates of a
	 *         Component and the virtual dimension of the syntax diagram element
	 */
	private VirtualDim getDimFromElement(SynDiaElem e, int x, int y,
			int min_width, boolean direction, boolean stairs, boolean render) {

		VirtualDim dim = new VirtualDim();

		// Vergleich ob trans(...) oder Element
		if (transMap.get(e) != null) {

			//
			// TRANSELEMENT
			//

			Font ebnfFont = null;
			try {
				ebnfFont = EbnfFont.getFont().deriveFont(Font.ITALIC,
						rv.font.getSize());
			} catch (FontNotInitializedException ex) {

				ex.printStackTrace();
			}

			int width = rv.getWidthFromString("trans("
					+ EbnfRenderer.toRenderString(transMap.get(e), true, "")
					+ ")", ebnfFont)
					+ (int) Math.round(1.3 * rv.radius);

			int height = 2 * rv.radius;

			if (width < height)
				width = height;

			dim = new VirtualDim(width, height, height);
			if (render)
				addElemToRenderList(RenderValues.TRANSTERM, e, x, y, width,
						height, "trans("
								+ EbnfRenderer.toRenderString(transMap.get(e),
										true, "") + ")", EbnfRenderer
								.toRenderStringList(transMap.get(e), true));

			return dim;

		} else {

			dim = this.getDimFromElem(e, x, y, min_width, direction, stairs,
					render);

		}

		return dim;

	}

	/**
	 * Never used!!!
	 */
	protected VirtualDim getDimFromElement(SynDiaElem e, int x, int y,
			boolean direction, boolean render) {

		return null;

	}

	/**
	 * Caclulates the Dimension of a given element by collecting informations
	 * from its children [and puts all elements (besides Concatenation) on the
	 * way into the renderList]
	 * 
	 * @param e
	 *            A SyntaxDiagram element
	 * @param x
	 *            x-position of the element
	 * @param y
	 *            y-position of the element
	 * @param min_width
	 *            if the width of a parental element is greater than the element
	 *            itself, the min_width is used
	 * @param direction
	 *            true: read left to right, false: read right to left
	 * @param stairs
	 *            boolean, telling wether to render or without stairs
	 * @param render
	 *            A paramter, telling the method, whether creating
	 *            RenderElements or just calculate the Dimension
	 * @return Width of e
	 */
	private VirtualDim getDimFromElem(SynDiaElem e, int x, int y,
			int min_width, boolean direction, boolean stairs, boolean render) {

		/*
		 * TERMINAL
		 */
		if (e instanceof TerminalSymbol) {
			TerminalSymbol t = (TerminalSymbol) e;

			int width = rv.getWidthFromString(t.getLabel())
					+ (int) Math.round(1.3 * rv.radius);
			int height = 2 * rv.radius;

			if (width < height)
				width = height;

			VirtualDim dim = new VirtualDim(width, height, height);
			if (render)
				addElemToRenderList(RenderValues.TERMINAL, t, x, y, width,
						height, t.getLabel(), null);

			return dim;
			/*
			 * VARIABLE
			 */
		} else if (e instanceof Variable) {

			Variable v = (Variable) e;

			int width = rv.getWidthFromString(v.getLabel())
					+ (int) Math.round(1.3 * rv.radius);
			int height = 2 * rv.radius;

			if (width < height)
				width = height;

			VirtualDim dim = new VirtualDim(width, height, height);

			if (render)
				addElemToRenderList(RenderValues.VARIABLE, v, x, y, width,
						height, v.getLabel(), null);

			return dim;
			/*
			 * BRANCH
			 */
		} else if (e instanceof Branch) {

			// width
			Branch b = (Branch) e;
			VirtualDim dim_left = getDimFromElement(b.getLeft(), x + 2
					* rv.radius, y, min_width, direction, true, render);
			VirtualDim dim = dim_left;
			VirtualDim dim_right = null;

			Concatenation right = b.getRight();
			if (dim_left.width > min_width)
				min_width = dim_left.width;

			if (right.getNumberOfElems() == 1
					&& right.getSynDiaElem(0) instanceof Branch
					&& !rv.withStairs) {
				if (transMap.get(right.getSynDiaElem(0)) != null) {
					dim_right = getDimFromElement(right, x + 2 * rv.radius, y
							+ dim_left.height + rv.space, min_width, direction,
							false, render);
				} else {
					dim_right = getDimFromElement(right, x, y + dim_left.height
							+ rv.space, min_width, direction, false, render);
				}

			} else {
				dim_right = getDimFromElement(right, x + 2 * rv.radius, y
						+ dim_left.height + rv.space, 0, direction,
						true, render);
			}

			if (dim_right.width > dim.width)
				dim.width = dim_right.width;

			if (dim.width < min_width && !rv.withStairs)
				dim.width = min_width;

			if (b.getRight().getNumberOfElems() == 1
					&& b.getRight().getSynDiaElem(0) instanceof Branch
					&& !rv.withStairs) {
				if (transMap.get(b.getRight().getSynDiaElem(0)) != null)
					dim.width += 4 * rv.radius;

			} else {

				dim.width += 4 * rv.radius;
			}

			// height
			dim.height = dim_left.height + rv.space + 2 * rv.radius;

			dim.virtualHeight = dim_left.virtualHeight + rv.space
					+ dim_right.virtualHeight;

			if (render) {

				if (stairs || rv.withStairs)
					addElemToRenderList(RenderValues.BRANCH, b, x, y,
							dim.width, dim.height, "", null);
				else
					addElemToRenderList(RenderValues.BRANCHNOSTAIRS, b, x, y,
							dim.width, dim.height, "", null);
			}

			return dim;
			/*
			 * CONCATENATION
			 */
		} else if (e instanceof Concatenation) {

			Concatenation c = (Concatenation) e;

			// width & height
			VirtualDim dim = new VirtualDim();
			VirtualDim dim_tmp = new VirtualDim();

			int i = 0;
			SynDiaElem sde;
			for (i = 0; i < c.getNumberOfElems(); i++) {

				if (direction)
					sde = c.getSynDiaElem(i);
				else
					sde = c.getSynDiaElem(c.getNumberOfElems() - i - 1);

				dim_tmp = getDimFromElement(sde, x, y, min_width, direction,
						(stairs || rv.withStairs), render);

				dim.width += dim_tmp.width;

				if (dim_tmp.virtualHeight > dim.virtualHeight)
					dim.virtualHeight = dim_tmp.virtualHeight;

				x += dim_tmp.width + rv.space;

			}

			if (i > 0)
				dim.width += (i - 1) * rv.space;

			if (dim.virtualHeight < 2 * rv.radius) {

				dim.virtualHeight = 2 * rv.radius;

			}
			dim.height = dim.virtualHeight;

			return dim;
			/*
			 * REPETITION
			 */
		} else if (e instanceof Repetition) {

			// width
			Repetition r = (Repetition) e;
			VirtualDim dim_left = getDimFromElement(r.getLeft(), x + 2
					* rv.radius, y, min_width, direction, true, render);

			VirtualDim dim_right = getDimFromElement(r.getRight(), x + 2
					* rv.radius, y + dim_left.height + rv.space, min_width,
					!direction, true, render);
			VirtualDim dim = dim_left;

			if (dim_right.width > dim.width)
				dim.width = dim_right.width;

			dim.width += 4 * rv.radius;

			// height
			dim.height = dim_left.height + rv.space + 2 * rv.radius;

			dim.virtualHeight = dim_left.virtualHeight + rv.space
					+ dim_right.virtualHeight;

			if (render)
				addElemToRenderList(RenderValues.REPETITION, r, x, y,
						dim.width, dim.height, "", null);

			return dim;

		}
		/*
		 * OTHERS
		 */
		return new VirtualDim(0, 0, 0);
	}

	/**
	 * Adds an element to the render List
	 * 
	 * @param type
	 *            to be found in the RenderValues
	 * @param s
	 *            the syntax diagram element itself
	 * @param x
	 *            the position of the element
	 * @param y
	 *            the position of the element
	 * @param width
	 *            the width of that element
	 * @param height
	 *            the height of that element
	 * @param label
	 *            optionally the name (or null) of the element
	 * @param stringList
	 *            the List of all substrings the label consists of
	 */
	private void addElemToRenderList(int type, SynDiaElem s, int x, int y,
			int width, int height, String label, List<String> stringList) {

		RenderElement renderElem = RenderElementFactory.createElement(type,
				label, stringList);

		renderElem.setRenderValues(rv);

		// Shadow for TerminalSymbol and Variable
// The if-branch will never be executed... (michael p, 31/10/08)
//		if (renderElem.getClass() == TerminalSymbol.class
//				|| renderElem.getClass() == Variable.class) {
//
//			renderElem.setSize(width + 3, height + 3);
//			renderElem.setLocation(x - 1, y - 1);
//		} else {

			renderElem.setSize(width, height);
			renderElem.setLocation(x, y);
//		}
		// new position for the element
		renderElem.update();

		// hinzufügen zur renderMap
		renderMap.put(renderElem, s);

	}

	/**
	 * @return the associated RenderValues
	 */
	public RenderValues getRenderValues() {

		return rv;
	}
}
