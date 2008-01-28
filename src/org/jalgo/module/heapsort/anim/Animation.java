/**
 * 
 */
package org.jalgo.module.heapsort.anim;


/**
 * <p>This interface models animations on a canvas.
 * Required canvas entities can be set up on <code>init</code>,
 * they can be disposed of in <code>done</code>,
 * and animated entities can be updated on <code>update</code>.</p>
 * 
 * <p>Time values presented to the animation for <code>update</code>
 * are in the closed real interval [0..1]. The reason for this is that
 * the boundaries of that interval are contained in the floating point
 * domain. The animation is garuanteed to be called with these exact
 * values by the AnimationTimeEntity.</p>
 * 
 * <p>It is assumed that the implementing class is able
 * to gain access to the necessary canvas entities. This has
 * several reasons: a) a root canvas entity (as an argument
 * to the methods declared here) might not suffice
 * because the animation could rely on a naming service;
 * b) animations are rather short-lived objects, so storing
 * the extra pointer should not be harmful.</p>
 * 
 * @author mbue
 *
 */
public interface Animation {
	void init();
	void done();
	void update(double time);
	double getDuration();
}
