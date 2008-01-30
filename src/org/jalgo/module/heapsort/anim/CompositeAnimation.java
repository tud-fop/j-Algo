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

/**
 * <p>This class can be used to compose a new animation out of existing ones.
 * Stakeholder: Animation designer...</p>
 * 
 * <p>The composition capability is obtained by subclassing <code>TimeEntity</code>.
 * In terms of the time entity, the closed real interval [0..1], which is the
 * local time domain for the animation, marks the absolute time values(!). This
 * makes it a bit hard to understand the influence of the time transformation
 * applied by <code>this</code>. Except in the rare case that you want to shift
 * or stretch the whole resulting composite animation (without changing its
 * actual duration), there will be no need nor use to change it from being
 * identity.</p>
 * 
 * <p>Use <code>addChild(TimeEntity, double, double)</code> if you want to
 * specify everything in absolute measures wrt. to the animation, not the time
 * entity (recommended). E.g. if the animation has a duration of 12, and you want
 * to add a time entity <code>t</code> at position 2 for a duration of 3, you just
 * call <code>addChild(t, 2, 3)</code>, and the <code>offset</code> and
 * <code>scale</code> of <code>t</code> will be set to 2/12 and 3/12, respectively.
 * (This example assumes that the time transformation of <code>this</code> is
 * identity.)</p>
 * 
 * <p>Naturally, to add animations to this entity, you would use
 * <code>AnimationTimeEntity</code> instances.</p>
 * 
 * @author mbue
 */
public abstract class CompositeAnimation extends TimeEntity implements Animation {

	/**
	 * Call <code>done()</code> on the children. Override this to implement
	 * your own disposal procedures, but don't forget to call <call>super()</code>.
	 * 
	 * @see org.jalgo.module.heapsort.anim.Animation#done()
	 */
	public void done() {
		for (TimeEntity e: children) {
			if (e instanceof Animation)
				((Animation)e).done();
		}
	}

	/**
	 * Call <code>init()</code> on the children. Override this to implement
	 * your own initialisation procedures, but don't forget to call <code>super()</code>.
	 * 
	 * @see org.jalgo.module.heapsort.anim.Animation#init()
	 */
	public void init() {
		for (TimeEntity e: children) {
			if (e instanceof Animation)
				((Animation)e).init();
		}
	}
	
	/**
	 * <p>Adds <code>t</code> to the children of this
	 * animation. Sets <code>offset</code> and <code>scale
	 * </code> values of <code>t</code> such that
	 * an absolute time (wrt. to the animation) of
	 * <code>start</code> will be transformed to the
	 * local time 0.0 (wrt. to <code>t</code>), and respectively
	 * for <code>start+dur</code> and 1.0.</p>
	 * 
	 * <p>Note: If <code>t</code> is an <code>AnimationTimeEntity</code>,
	 * its scale will most likely be set according to its animation's
	 * duration. However, in most cases this duration is not relevant
	 * for composition.</p> 
	 * 
	 * <p>Restrictions of <code>addChild(TimeEntity)</code> apply.</p>
	 * 
	 * @param t
	 * @param start
	 * @param dur
	 */
	public void addChild(TimeEntity t, double start, double dur) {
		/* we can just pretend that this.scale
		 * would also account for the duration
		 * 
		 * two equations, two variables
		 * s1 is this.scale, s2 is t.scale
		 * o1 is this.offset, o2 is t.offset
		 * use transformation equations for
		 * start,0 and start+dur,1
		 * 
		 * start     = s1*s2*0+s1*o2+o1   (1)
		 * start+dur = s1*s2*1+s1*o2+o1   (2)
		 * 
		 *     (1) --> o2 = (start-o1)/s1
		 * (2)-(1) --> s2 = dur/s1
		 */ 
		double oneover = 1/(scale*getDuration());
		addChild(t);
		t.offset = oneover*(start-offset);
		t.scale = oneover*dur;
	}
	
	public void add(Animation a, double start, double dur) {
		addChild(new AnimationTimeEntity(a, 0), start, dur);
	}

}
