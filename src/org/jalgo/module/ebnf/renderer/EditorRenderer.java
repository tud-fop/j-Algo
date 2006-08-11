/**
 * 
 */
package org.jalgo.module.ebnf.renderer;

import org.jalgo.module.ebnf.model.syndia.Branch;
import org.jalgo.module.ebnf.model.syndia.Concatenation;
import org.jalgo.module.ebnf.model.syndia.NullElem;
import org.jalgo.module.ebnf.model.syndia.Repetition;
import org.jalgo.module.ebnf.model.syndia.SynDiaElem;
import org.jalgo.module.ebnf.model.syndia.SyntaxDiagram;
import org.jalgo.module.ebnf.model.syndia.TerminalSymbol;
import org.jalgo.module.ebnf.model.syndia.Variable;

/**
 * This class gets a syntax diagram, a drawing area (JComponent) and some
 * other optional params. As result you will get a drawn syntax diagram (system) on the
 * given area.
 * 
 * @author Andre Viergutz
 */
public class EditorRenderer extends SynDiaRenderer{

	
	
	/**
	 * Decreases the size of the SPACE between render elements
	 */
	public EditorRenderer() {
		
		super();
		this.rv.space = super.rv.space / 2;
	}
	
	protected VirtualDim getMyDim(SyntaxDiagram sd) {
		
		return getDimFromElement(sd.getRoot(), rv.radius,
				2 * rv.radius, true, true);
		
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
	protected VirtualDim getDimFromElement(SynDiaElem e, int x, int y, boolean direction, 
			boolean render) {
		
		/*
		 * TERMINAL
		 */
		if (e.getClass() == TerminalSymbol.class) {
			TerminalSymbol t = (TerminalSymbol) e;

			int width = rv.getWidthFromString(t.getLabel())
					+ (int) Math.round(1.3 * rv.radius);
			int height = 2 * rv.radius;

			if (width < height)
				width = height;

			VirtualDim dim = new VirtualDim(width, height, height);
			if (render)
				addElemToRenderList(RenderValues.TERMINAL, t, x, y, width,
						height, t.getLabel());

			return dim;
			/*
			 * VARIABLE
			 */
		}else if (e.getClass() == Variable.class) {

			Variable v = (Variable) e;

			int width = rv.getWidthFromString(v.getLabel())
					+ (int) Math.round(1.3 * rv.radius);
			int height = 2 * rv.radius;

			if (width < height)
				width = height;

			VirtualDim dim = new VirtualDim(width, height, height);

			if (render)
				addElemToRenderList(RenderValues.VARIABLE, v, x, y, width,
						height, v.getLabel());

			return dim;
			/*
			 * BRANCH
			 */
		} else if (e.getClass() == Branch.class) {

			// width
			Branch b = (Branch) e;
			VirtualDim dim_left = getDimFromElement(b.getLeft(), x + 3
					* rv.radius, y, direction, render);

			VirtualDim dim_right = getDimFromElement(b.getRight(), x + 3
					* rv.radius, y + dim_left.height + rv.space, direction,
					render);

			VirtualDim dim = dim_left;

			if (dim_right.width > dim.width)
				dim.width = dim_right.width;

			dim.width += 6 * rv.radius;

			// height
			dim.height = dim_left.height + rv.space + 2 * rv.radius;

			dim.virtualHeight = dim_left.virtualHeight + rv.space
					+ dim_right.virtualHeight;

			if (render)
				addElemToRenderList(RenderValues.BRANCH, b, x, y, dim.width,
						dim.height, "");

			return dim;
			/*
			 * CONCATENATION
			 */
		} else if (e.getClass() == Concatenation.class) {

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

				dim_tmp = getDimFromElement(sde, x, y, direction, render);
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
		} else if (e.getClass() == Repetition.class) {

			// width
			Repetition r = (Repetition) e;
			VirtualDim dim_left = getDimFromElement(r.getLeft(), x + 2
					* rv.radius, y, direction, render);

			VirtualDim dim_right = getDimFromElement(r.getRight(), x + 2
					* rv.radius, y + dim_left.height + rv.space, !direction,
					render);
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
						dim.width, dim.height, "");

			return dim;
			/*
			 * NULL ELEM
			 */
		} else if (e.getClass() == NullElem.class) {

			NullElem ne = (NullElem) e;

			int width = 2 * rv.radius;
			int height = 2 * rv.radius;

			VirtualDim dim = new VirtualDim(width, height, height);

			if (render)
				addElemToRenderList(RenderValues.NULLELEM, ne, x, y, width,
						height, "");

			return dim;

		}
		/*
		 * OTHERS
		 */
		return new VirtualDim(0, 0, 0);
	}

	
}
