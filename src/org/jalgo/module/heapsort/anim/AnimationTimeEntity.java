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
 * <p>This time entity controls an animation. The animation is being run
 * for local times in the real interval [0..1]. The time transformation
 * is set up in the constructor according to the absolute <code>start</code>
 * time desired for the animation as well as the duration it reports.</p>
 * 
 * <p>This class does only support at most one listener for two reasons:
 * a) this should suffice in most cases, and otherwise, the listener
 * could act as subject to other listeners; b) it's the easiest way of
 * getting around the multiple inheritance problem.</p>
 * 
 * @author mbue
 */
public final class AnimationTimeEntity extends TimeEntity implements Animation {
	private Animation anim;
	private AnimationListener listener;
	private State state = new State1();
	// XXX maybe we should create all states at once because they will be created anyway
	
	public AnimationTimeEntity(Animation anim, double start) {
		super();
		this.anim = anim;
		this.offset = start;
		this.scale = anim.getDuration();
	}
	
	public void setListener(AnimationListener l) {
		listener = l;
	}
	
	protected void doUpdate(double tloc) {
		state.doUpdate(tloc);
	}
	
	private interface State {
		void doUpdate(double tloc);
	}
	
	/**
	 * pre-init state
	 * 
	 * @author mbue
	 */
	private class State1 implements State {
		public void doUpdate(double tloc) {
			// BUGFIX: handle tloc > 1.0
			// if this ate is used in a composite animation,
			// it might get called with -5 and then with 5
			// because of some time trouble, the outer animation is
			// only called with 0.0 and 1.0
			if (tloc >= 1.0) {
				state = new State3();
				anim.update(1.0);
				// changed to ease composition
				//done() is called outside
				//anim.done();
				if (listener != null)
					listener.animationComplete(anim);				
			}
			else if (tloc >= 0.0) {
				state = new State2();
				// changed to ease composition:
				//init() is called outside
				//anim.init();
				anim.update(0.0);
			}
		}
	}
	
	/**
	 * running animation
	 * 
	 * @author mbue
	 */
	private class State2 implements State {
		public void doUpdate(double tloc) {
			if (tloc < 1.0) {
				anim.update(tloc);
			}
			else {
				state = new State3();
				anim.update(1.0);
				// changed to ease composition
				//done() is called outside
				//anim.done();
				if (listener != null)
					listener.animationComplete(anim);
			}
		}
	}
	
	/**
	 * post-done state
	 * 
	 * @author mbue
	 */
	private class State3 implements State {
		public void doUpdate(double tloc) {
			// this animation is done!
		}
	}

	public void done() {
		anim.done();
	}

	public double getDuration() {
		return anim.getDuration();
	}

	public void init() {
		anim.init();
	}

}
