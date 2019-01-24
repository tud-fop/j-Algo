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
package org.jalgo.module.heapsort.anim;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>An entity which cares about time. Similar to the way locations are handled
 * with canvas entities, time entities have a local and an absolute time, for now denoted
 * by <code>t_loc</code> and <code>t_abs</code>, respectively. The
 * defining equation is
 * 
 * <code>t_abs = scale*t_loc + offset</code>, where <code>scale</code> and
 * <code>offset</code> are members of this class and <code>scale &gt; 0</code>.</p>
 * 
 * <p>Since in any case, absolute time is given and local time is wanted, we use
 * another form of the equation: <code>t_loc = 1/scale*(t_abs - offset)</code>.
 * 
 * @author mbue
 *
 */
public class TimeEntity {
	protected TimeEntity parent = null;
	protected double offset = 0.0;
	protected double scale = 1.0;
	protected Set<TimeEntity> children = null;
	
	/**
	 * Adds <code>t</code> to the children of this time
	 * entity. Will not prevent you from adding the same
	 * entity twice into the same tree. Just don't do it.
	 * 
	 * @param t time entity to be added
	 */
	public void addChild(TimeEntity t) {
		if (children == null)
			children = new HashSet<TimeEntity>();
		children.add(t);
		t.parent = this;
	}

	/**
	 * Returns current local time, provided that the parent node does so correctly.
	 * Use a <code>TimeRoot</code> instance as root node to make this work.
	 * 
	 * @return
	 */
	public double now() {
		if (parent == null)
			return 0.0;
		else
			return 1/scale*(parent.now()-offset);
	}
	
	/**
	 * If <code>t</code> is a direct successor of this node,
	 * remove it.
	 *  
	 * @param t time entity to be removed
	 */
	public void removeChild(TimeEntity t) {
		if (t.parent == this) {
			children.remove(t);
			t.parent = null;
		}
	}
	
	/**
	 * Virtual method called when local time has changed.
	 * Override this when subclassing. The default implementation
	 * does nothing, so there is no need to call <code>super</code>.
	 *  
	 * @param tloc local time
	 */
	protected void doUpdate(double tloc) {
		
	}
	
	/**
	 * Called by the system to update the time entity wrt.
	 * to absolute time. Also implements composite pattern.
	 * Note: This method is final. When subclassing, override
	 * <code>doUpdate</code>.
	 * 
	 * @param tabs absolute time
	 */
	public final void update(double tabs) {
		double tloc = 1/scale*(tabs-offset);
		doUpdate(tloc);
		if (children != null) {
			for (TimeEntity c: children)
				c.update(tloc);
		}
	}
	
	public void setScale(double scale) {
		if (scale <= 0.0)
			throw new IllegalArgumentException("scale must be greater than 0");
		this.scale = scale;
	}
}
