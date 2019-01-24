/*
 * j-Algo - a visualization tool for algorithm runs, especially useful for
 * students and lecturers of computer science. j-Algo is written in Java and
 * thus platform independent. Development is supported by Technische Universität
 * Dresden.
 *
 * Copyright (C) 2004-2008 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jalgo.module.heapsort.renderer;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * <p>The CanvasEntity class represents an object on the virtual canvas. Each such object has
 * an affine transformation, which describes the relationship between the ("relative", or local)
 * coordinates wrt. to the entity and the ("absolute") coordinates which are local to its parent.
 * </p>
 * 
 * <p>A canvas entity keeps a log of where the image has to be redrawn, the so-called dirty region.
 * The dirty region is a rectangle expressed in absolute coordinates (which is needed in case
 * the affine transform is changed, and in particular, when the new transform has no inverse).
 * (Imagine how you would express some non-empty box in local coordinates when the transform
 * maps everything to one particular point.)
 * </p>
 * 
 * <p>Furthermore, canvas entities have a bounding box (expressed in local coordinates) and an
 * integer describing the position in the z order of the canvas. Note that, if the bounding box
 * were in absolute coordinates, it could grow every time a non-quadrant rotation is applied.
 * Besides, the transformation could not be reset, only concatenated.</p>
 * 
 * @author mbue
 */
public class CanvasEntity {
	protected Rectangle bounds = new Rectangle(0,0,-1,-1);    // nothing
	protected Rectangle dirty = new Rectangle(0,0,-1,-1);     // nothing
	protected AffineTransform trans = new AffineTransform();  // identity
	protected int zorder = 0;
	
	protected Set<CanvasEntity> children =
		new TreeSet<CanvasEntity>(CanvasEntityComparator.getInstance());
	
	// hide constructor to prevent people from forgetting to use the
	// factory provided by the renderer
	protected CanvasEntity() {
		
	}
	
	/**
	 * <p>Adds canvas entity <code>c</code> to the children of this node.
	 * Does nothing if <code>c</code> already in this hierarchy.</p>
	 * <p>Note: This will of course not stop you from adding the same canvas
	 * entity twice to some tree by adding it to some node and then
	 * to another one which is deeper in the tree. Just don't do it.</p>
	 *  
	 * @param c canvas entity to be added
	 */
	public void addChild(CanvasEntity c) {
		// dirty region need not be changed because it is
		// computed recursively on demand
		if (findParentOf(c) == null)
			children.add(c);
	}
	
	/**
	 * Finds the parent node of a given canvas entity <code>c</code>.
	 * Returns <code>null</code> iff this node is not contained in the
	 * subtrees below <code>this</code>.
	 * 
	 * @param c
	 * @return parent of <code>c</code>, or <code>null</code> if
	 * <code>c</code> not in this hierarchy
	 */
	public CanvasEntity findParentOf(CanvasEntity c) {
		// leaf node: can't be parent
		if (children.isEmpty())
			return null;
		// this is parent
		if (children.contains(c))
			return this;
		// one of the children could be parent
		CanvasEntity result = null;
		for (CanvasEntity ch: children) {
			result = ch.findParentOf(c);
			if (result != null)
				return result;
		}
		return result;
	}
	
	/**
	 * Remove <code>c</code> from the children of this node.
	 * Adjust dirty region accordingly.
	 * Does nothing if <code>c</code> is no child of this node.
	 * 
	 * @param c canvas entity to be removed
	 */
	public void removeChild(CanvasEntity c) {
		// check this for sake of failure atomicity
		if (children.contains(c)) {
			c.invalidate();
			dirty.add(transformBounds(c.dirty, trans));
			children.remove(c);
		}
	}
	
	/**
	 * Apply the visitor <code>v</code> to every canvas entity in this subtree.
	 * 
	 * @param v
	 */
	public void fold(CanvasEntityVisitor v) {
		fold(v, new AffineTransform());
	}
	
	protected void fold(CanvasEntityVisitor v, AffineTransform t) {
		// t1 = t . trans
		AffineTransform t1 = new AffineTransform(t);
		t1.concatenate(trans);
		v.invoke(this, t1, null);
		for (CanvasEntity e: children)
			e.fold(v, t1);
	}
	
	/**
	 * <p>Apply the visitor <code>v</code> to every canvas entity in this subtree, provided that
	 * they intersect with the rectangle <code>r</code>, which is regarded as being in absolute
	 * coordinates wrt. <code>this</code>.</p>
	 * 
	 * <p>As an example scenario, the rectangle <code>r</code> could be the intersection of
	 * some visible area with the dirty region of <code>this</code>.</p> 
	 * 
	 * @param v
	 * @param r
	 * @param t
	 */
	public void foldVisible(CanvasEntityVisitor v, Rectangle r) {
		foldVisible(v, r, new AffineTransform());
	}
	
	protected void foldVisible(CanvasEntityVisitor v, Rectangle r, AffineTransform t) {
		// t1 = t . trans
		AffineTransform t1 = new AffineTransform(t);
		t1.concatenate(trans);
		// transform bounding box into absolute coordinates and test intersection
		if (transformBounds(bounds, t1).intersects(r)) {
			v.invoke(this, t1, r);
			for (CanvasEntity e: children)
				e.foldVisible(v, r, t1);
		}
	}
	
	/**
	 * <p>Adds the bounding box to the dirty region causing this entity
	 * (and others underneath or above) to be redrawn.</p>
	 * 
	 * <p>NOTE that if you change the transformation, you have to call
	 * this beforehands <em>and</em> afterwards!
	 */
	public void invalidate() {
		dirty.add(transformBounds(bounds, trans));
	}
	
	/**
	 * Sets the bounding box of this canvas entity to <code>newBounds</code>, adding to
	 * the dirty region both the old and the new bounding box.
	 * 
	 * @param newBounds
	 */
	public void setBounds(Rectangle newBounds) {
		if (!bounds.equals(newBounds)) {
			invalidate();
			bounds.setBounds(newBounds);
			invalidate();
		}
	}
	
	/**
	 * Returns the z order value.
	 * @return
	 */
	public int getZorder() {
		return zorder;
	}
	
	/**
	 * Changes the Z order of this entity. This will invalidate the current image of the entity.
	 * 
	 * @param newz
	 */
	public void setZorder(int newz) {
		if (newz != zorder) {
			zorder = newz;
			invalidate();
		}
	}
	
	/**
	 * Compute the dirty region of the whole entity subtree (starting at <code>this</code>),
	 * which is the union of the dirty regions of the entities, transformed into the absolute
	 * coordinate system wrt. <code>this</code>.
	 * 
	 * @return Union of dirty regions of subtree
	 */
    public Rectangle computeDirtyRegion() {
    	// start computation with our dirty region (this is already
    	// in absolute coordinates)
    	ComputeDirtyVisitor v = new ComputeDirtyVisitor(dirty);
    	// transform dirty regions of subtrees
    	fold(v);
    	return v.dirty;
    }
    
    /**
     * Clear the dirty regions of the whole entity subtree starting at <code>this</code>.
     *
     */
    public void clearDirtyRegion() {
    	fold(ClearDirtyVisitor.getInstance());
    }
    
    /**
     * Compute the bounding box of the rectangle which is obtained by
     * applying the AffineTransform <code>t</code> to the rectangle
     * <code>b</code>.
     * 
     * @param b
     * @param t
     * @return
     */
    public static Rectangle transformBounds(Rectangle b, AffineTransform t) {
    	if (b.width < 0 || b.height < 0)
        	return b;
    	
    	// set up data structures for transformation
    	Point2D[] s = {
    		new Point2D.Double(b.x, b.y),
    		new Point2D.Double(b.x, b.y+b.height),
    		new Point2D.Double(b.x+b.width, b.y),
    		new Point2D.Double(b.x+b.width, b.y+b.height)};
    	Point2D[] d = new Point2D.Double[4];
    	
    	// transform b by t
    	t.transform(s, 0, d, 0, 4);
    	
    	// compute bounding box
    	double minx = d[0].getX();
    	double maxx = d[0].getX();
    	double miny = d[0].getY();
    	double maxy = d[0].getY();
    	
    	for (int i = 1; i<4; i++) {
    		if (d[i].getX() < minx)
    			minx = d[i].getX();
    		
    		if (d[i].getX() > maxx)
    			maxx = d[i].getX();
    		
    		if (d[i].getY() < miny)
    			miny = d[i].getY();
    		
    		if (d[i].getY() > maxy)
    			maxy = d[i].getY();
    	}
    	// FIXME make it a bit bigger because of rounding errors/lack of accuracy 
    	return new Rectangle((int)minx-1, (int)miny-1, (int)(maxx-minx)+2, (int)(maxy-miny)+2);
    }
    
    // auxiliary class used in computeDirtyRegion()
    // this demonstrates the wordiness of Java
    private static class ComputeDirtyVisitor implements CanvasEntityVisitor {
        public Rectangle dirty = null;
        
        public ComputeDirtyVisitor() {
        	this(new Rectangle(0,0,-1,-1));
        }
        
        public ComputeDirtyVisitor(Rectangle dirty) {
        	this.dirty = dirty;
        }
        
        public void invoke(CanvasEntity e, AffineTransform t, Rectangle clip) {
        	// the dirty regions of the children are in our
        	// local coordinate space, transform into global
        	// clip can be ignored!
        	for (CanvasEntity c: e.children) {
        		Rectangle dtrans = transformBounds(c.dirty, t);
        		dirty.add(dtrans);
        	}
        }
    }

    // auxiliary class used in clearDirtyRegion()
    private static class ClearDirtyVisitor implements CanvasEntityVisitor {
    	// technical stuff: this visitor has no state and can therefore be allocated statically
    	private static ClearDirtyVisitor arnie = new ClearDirtyVisitor();
    	
        public void invoke(CanvasEntity e, AffineTransform t, Rectangle clip) {
        	// clip is ignored here as well
        	// --------------------------------------------
        	// assume we render root.computeDirtyRegion()
        	// then the whole dirty region is accounted for
        	// and intersecting with clip would be neutral
        	// --------------------------------------------
        	// assume we render some visible region
        	// then invisible parts of the dirty region
        	// would not matter because they will be marked
        	// dirty anyway when becoming visible
        	e.dirty.height = -1;
        	e.dirty.width = -1;
        }
        
        public static ClearDirtyVisitor getInstance() {
        	return arnie;
        }
    }
    
    /**
     * <p>A comparator for canvas entities.</p>
     * 
     * <p><code>c1 &lt;= c2</code> iff<br>
     * 
     * <code>c1.zorder &lt;= c2.zorder</code> or<br>
     * <code>c1.zorder == c2.zorder</code> implies <code>c1.hashCode() &lt;= c2.hashCode()</code>.</p>
     * 
     * @author mbue
     */
    private static class CanvasEntityComparator implements Comparator<CanvasEntity> {
    	private static CanvasEntityComparator comp = new CanvasEntityComparator();
    	
    	public static Comparator<CanvasEntity> getInstance() {
    		return comp;
    	}

		public int compare(CanvasEntity o1, CanvasEntity o2) {
			// lexicographic order on direct product
			// int x int
			// (zorder, hashCode())
			int res = o1.zorder-o2.zorder;
			if (res == 0)
				return o1.hashCode()-o2.hashCode();
			else
				return res;
		}
    	
    }

}
