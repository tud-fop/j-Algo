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
package org.jalgo.module.heapsort;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Some super-sick, weird, and fucked up implementation of the Observer pattern.
 * Sorry but it's all Java's fault. (However, this not as sick as
 * <code>java.lang.Observable</code>.)</p>
 * 
 * <p>Usage: Define your Listener interface, e.g. <code>MyListener</code>.
 * Derive your subject class from <code>Subject&lt;MyListener&gt;</code>.
 * In your subject, implement a <code>Notifier&lt;MyListener&gt;</code> for each
 * of <code>MyListener</code>'s methods. That enables you to use
 * <code>notifyAll</code>.</p>
 * 
 * <p>See? Java is a big monster.</p>
 * 
 * <p>BTW, this shit is not thread-safe! TODO Need to find a suspendUpdates semantics
 * suitable for threading.</p>
 * 
 * @author mbue
 *
 */
public class Subject<Listener> {
	protected List<Listener> listeners;
	private int update = 0; // only notify if <= 0
	
	public void addListener(Listener l) {
		if (listeners == null)
			listeners = new ArrayList<Listener>();
		listeners.add(l);
	}
	
	public void removeListener(Listener l) {
		if (listeners != null)
			listeners.remove(l);
	}
	
	protected void notifyAll(Notifier<Listener> n) {
		if ((listeners != null) && (update <= 0)) {
			for (Listener l: listeners)
				n.invoke(l);
		}
	}
	
	protected void suspendUpdates() {
		update++;
	}
	
	protected void resumeUpdates() {
		update--;
	}
	
	protected interface Notifier<Listener> {
		void invoke(Listener l);
	}
}
