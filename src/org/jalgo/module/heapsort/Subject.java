/**
 * 
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
	List<Listener> listeners;
	int update = 0; // only notify if <= 0
	
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
	
	protected static interface Notifier<Listener> {
		void invoke(Listener l);
	}
}
