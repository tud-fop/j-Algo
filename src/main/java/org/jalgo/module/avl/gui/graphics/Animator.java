/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer science. It is written in Java and platform independent. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/* Created on 09.06.2005 */
package org.jalgo.module.avl.gui.graphics;

/**
 * The class <code>Animator</code> represents a thread for running some animation.
 * Subclasses have to override the <code>run()</code> method.<br>
 * At this time, animations are only supported for the generation of random trees.
 * 
 * @author Alexander Claus
 */
public abstract class Animator
extends Thread
implements GraphicsConstants {

	public static final int STOPPED = 0;
	public static final int RUNNING = 1;
	public static final int STOP_REQUESTED = 2;
	private int runningMode;

	/**
	 * Starts the animation.
	 */
	public synchronized void start() {
		runningMode = RUNNING;
		super.start();
	}
	
	/**
	 * Requests to stop the animation. Subclasses have to define, how and when to
	 * stop the thread.
	 */
	public void stopAnim() {
		runningMode = STOP_REQUESTED;
	}

	/**
	 * This method has to be invoked by subclasses, if the animation thread ends.
	 */
	protected void animStopped() {
		runningMode = STOPPED;
	}

	/**
	 * Retrieves the running state of the animation thread.
	 * 
	 * @return <code>true</code>, if the thread is running,
	 * 			<code>false</code> otherwise
	 */
	public boolean isRunning() {
		return runningMode != STOPPED;
	}

	/**
	 * Retrieves, if the thread is requested to be stopped.
	 * 
	 * @return <code>true</code>, if stop is requested, <code>false</code> otherwise
	 */
	protected boolean isStopRequested() {
		return runningMode == STOP_REQUESTED;
	}
}