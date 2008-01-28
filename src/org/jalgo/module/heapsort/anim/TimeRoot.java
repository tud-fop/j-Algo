/**
 * 
 */
package org.jalgo.module.heapsort.anim;


/**
 * @author mbue
 *
 */
public class TimeRoot extends TimeEntity {
	private double now_;
	
	@Override
	public double now() {
		return now_;
	}
	
	@Override
	protected void doUpdate(double tloc) {
		// that is totally correct:
		// now has to be expressed in local time
		// (absolute wrt. children)
		now_ = tloc;
	}
	
	/**
	 * Set scale of this time root. Will reset offset such that
	 * there will be no seam wrt. now().
	 * 
	 * @see org.jalgo.module.heapsort.anim.TimeEntity#setScale(double)
	 */
	@Override
	public void setScale(double scale) {
		/* Ansatz:
		 * absolute time and local time don't change
		 * 
		 * s1*tloc + o1 = s2*tloc + o2
		 * --> o2 = (s1-s2)*tloc + 1
		 */
		double s1 = this.scale;
		super.setScale(scale);
		offset += (s1-scale)*now_;
	}
}
