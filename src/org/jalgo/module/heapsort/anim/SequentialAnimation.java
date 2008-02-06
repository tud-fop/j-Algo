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
 * Subclass this if your animation consists of a sequence of animations.
 * Don't forget the super calls!
 * 
 * @author mbue
 */
public class SequentialAnimation implements Animation {
	// Actually, I hate this implementation...
	// I wanted it to be more efficient than using CompositeAnimation with lots of
	// AnimationTimeEntities, so I had to pass on the idea of using too many inner
	// classes, even if we have quite some state involved
	
	private static class AnimItem {
		public Animation a;
		public double start;
		public double dur;
		public AnimItem next;
	}
	
	private AnimItem anim;
	private AnimItem head;
	private AnimItem cur;
	private double duration = 0.0;
	
	public SequentialAnimation() {
		anim = null;
	}
	
	/**
	 * Add an animation to this sequential composition.
	 * <code>dur</code> must be greater 0!
	 * 
	 * @param a
	 * @param dur
	 */
	protected void add(Animation a, double dur) {
		if (head == null) {
			head = new AnimItem();
			anim = head;
		}
		else {
			head.next = new AnimItem();
			head = head.next;
		}
		head.a = a;
		head.dur = dur;
		head.start = duration;
		duration += dur;
	}

	public void done() {
		if (cur != null) {
			cur.a.update(1.0);
			cur.a.done();
		}
	}

	public double getDuration() {
		return duration;
	}

	public void init() {
		cur = anim;
		if (cur != null) {
			cur.a.init();
			cur.a.update(0.0);
		}
	}

	public void update(double time) {
		// ok folks, we are going to use the BREAK statement here, so fasten seatbelts
		// (the loop is necessary because we are not garuanteed to be called often enough
		// to bring every animation to 1.0, which is required.)
		while (cur != null) {
			double tloc = (time*duration-cur.start)/cur.dur;
			if (tloc < 1.0) {
				cur.a.update(tloc);
				break; // <--- HERE IT IS
			}
			else {
				cur.a.update(1.0);
				cur.a.done();
				cur = cur.next;
				if (cur != null) {
					cur.a.init();
					cur.a.update(0.0);
				}
			}
		}
	}

}
