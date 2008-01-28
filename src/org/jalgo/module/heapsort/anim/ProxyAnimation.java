/**
 * 
 */
package org.jalgo.module.heapsort.anim;

/**
 * Subclass this if you want to decorate an existing animation.
 * 
 * @author mbue
 *
 */
public class ProxyAnimation implements Animation {
	
	protected Animation delegate;
	
	protected ProxyAnimation() {
	}
	
	public void done() {
		delegate.done();
	}

	public double getDuration() {
		return delegate.getDuration();
	}

	public void init() {
		delegate.init();
	}

	public void update(double time) {
		delegate.update(time);
	}

}
