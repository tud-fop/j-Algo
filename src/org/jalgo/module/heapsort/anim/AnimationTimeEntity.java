/*
 * j-Algo - a visualization tool for algorithm runs, especially useful for
 * students and lecturers of computer sience. j-Algo is written in Java and
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
 * <p>
 * This time entity controls an animation. The animation is being run
 * for local times in the real interval [0..1]. You have to see to it
 * that the transformation of this entity yields the correct absolute
 * time values, i.e. <code>offset</code> is the start time and <code>
 * scale</code> is the duration.</p>
 * 
 * <p>
 * This class does only support at most one listener for two reasons:
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
			if (tloc >= 0.0) {
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
