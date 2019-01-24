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

import java.awt.geom.Point2D;

import org.jalgo.module.heapsort.renderer.MarkingRect;
import org.jalgo.module.heapsort.renderer.Node;
import org.jalgo.module.heapsort.renderer.SequenceElement;
import org.jalgo.module.heapsort.renderer.Text;

/**
 * A (static) factory class for common animations. There are several
 * facilities (read: classes) to compose animations: <code>ProxyAnimation</code>
 * if you just want to add some init or done code, <code>ParallelAnimation</code>
 * for animations running completely in parallel, <code>SequentialAnimation</code>
 * for a sequence of animations, and most flexible (and expensive)
 * <code>CompositeAnimation</code>, which allows for arbitrary composition.
 * 
 * @author mbue
 */
public final class Animations {
	
	public interface CrossfaderFactory {
		Animation invoke(Fadeable f1, Fadeable f2);
	}
	
	public interface CrossmoverFactory {
		Animation invoke(Moveable m1, Moveable m2, Point2D p1, Point2D p2);
	}
	
	public static final CrossfaderFactory LINEAR_FADER = new LinearCrossfaderFactory();
	public static final CrossfaderFactory SMOOTH_FADER = new SmoothCrossfaderFactory();
	
	public static final CrossmoverFactory LINEAR_MOVER = new LinearCrossmoverFactory();
	public static final CrossmoverFactory SMOOTH_MOVER = new SmoothCrossmoverFactory();
	
	/**
	 * Creates a crossfading animation. The duration of the animation
	 * will be a token 1.0, so just use your own scale.
	 *  
	 * @param f1
	 * @param f2
	 * @return
	 */
	public static Animation createCrossfader(CrossfaderFactory fac,
			Fadeable f1, Fadeable f2) {
		return fac.invoke(f1, f2);
	}
	
	public static Animation createInFader(CrossfaderFactory fac, Fadeable f) {
		return fac.invoke(null, f);
	}
	
	public static Animation createOutFader(CrossfaderFactory fac, Fadeable f) {
		return fac.invoke(f, null);
	}
	
	public static Animation createCrossmover(CrossmoverFactory fac,
			Moveable m1, Moveable m2, Point2D p1, Point2D p2) {
		return fac.invoke(m1, m2, p1, p2);
	}
	
	public static Animation createMover(CrossmoverFactory fac,
			Moveable m, Point2D p1, Point2D p2) {
		return fac.invoke(m, null, p1, p2);
	}
	
	public static Animation createZoomer(Scalable s1, Scalable s2, double factor) {
		return new Zoomer(s1, s2, factor);
	}
	
	public static Animation createCrossBlinker(Fadeable f1, Fadeable f2, double periods) {
		return new SmoothBlinker(f1, f2, periods);
	}
	
	public static Animation createBlinker(Fadeable f1, double periods) {
		return new SmoothBlinker(f1, null, periods);
	}
	
	// adapter stuff
	
	public static Animation createCrossfader(CrossfaderFactory fac,
			MarkingRect f1, MarkingRect f2) {
		return fac.invoke(new MarkingRectAdapter(f1), new MarkingRectAdapter(f2));
	}
	
	public static Animation createInFader(CrossfaderFactory fac, Text t) {
		return fac.invoke(null, new TextAdapter(t));
	}
	
	public static Animation createInFader(CrossfaderFactory fac, MarkingRect mr) {
		return fac.invoke(null, new MarkingRectAdapter(mr));
	}
	
	public static Animation createInFader(CrossfaderFactory fac, Node n) {
		return fac.invoke(null, new NodeAdapter(n));
	}
	
	public static Animation createInFader(CrossfaderFactory fac, SequenceElement s) {
		return fac.invoke(null, new SequenceElementAdapter(s));
	}
	
	public static Animation createOutFader(CrossfaderFactory fac, Text t) {
		return fac.invoke(new TextAdapter(t), null);
	}
	
	public static Animation createOutFader(CrossfaderFactory fac, MarkingRect mr) {
		return fac.invoke(new MarkingRectAdapter(mr), null);
	}
	
	public static Animation createOutFader(CrossfaderFactory fac, Node n) {
		return fac.invoke(new NodeAdapter(n), null);
	}
	
	public static Animation createOutFader(CrossfaderFactory fac, SequenceElement s) {
		return fac.invoke(new SequenceElementAdapter(s), null);
	}
	
	public static Animation createCrossmover(CrossmoverFactory fac,
			Node m1, Node m2, Point2D p1, Point2D p2) {
		return fac.invoke(new NodeAdapter(m1), new NodeAdapter(m2), p1, p2);
	}
	
	public static Animation createCrossmover(CrossmoverFactory fac,
			SequenceElement m1, SequenceElement m2, Point2D p1, Point2D p2) {
		return fac.invoke(new SequenceElementAdapter(m1), new SequenceElementAdapter(m2), p1, p2);
	}
	
	public static Animation createMover(CrossmoverFactory fac,
			Text m, Point2D p1, Point2D p2) {
		return fac.invoke(new TextAdapter(m), null, p1, p2);
	}
	
	public static Animation createZoomer(Node s1, Node s2, double factor) {
		return new Zoomer(new NodeAdapter(s1), new NodeAdapter(s2), factor);
	}
	
	public static Animation createZoomer(
			SequenceElement s1, SequenceElement s2, double factor) {
		return new Zoomer(new SequenceElementAdapter(s1), new SequenceElementAdapter(s2), factor);
	}
	
	public static Animation createBlinker(MarkingRect f1, double periods) {
		return new SmoothBlinker(new MarkingRectAdapter(f1), null, periods);
	}
	
	// private (implementation) stuff follows
	
	private static class LinearCrossfaderFactory implements CrossfaderFactory {
		public Animation invoke(Fadeable f1, Fadeable f2) {
			return new LinearCrossfader(f1, f2);
		}
	}
	
	private static class SmoothCrossfaderFactory implements CrossfaderFactory {
		public Animation invoke(Fadeable f1, Fadeable f2) {
			return new SmoothCrossfader(f1, f2);
		}
	}
	
	private static class LinearCrossmoverFactory implements CrossmoverFactory {
		public Animation invoke(Moveable m1, Moveable m2, Point2D p1, Point2D p2) {
			return new LinearCrossmover(m1, m2, p1, p2);
		}
	}
	
	private static class SmoothCrossmoverFactory implements CrossmoverFactory {
		public Animation invoke(Moveable m1, Moveable m2, Point2D p1, Point2D p2) {
			return new SmoothCrossmover(m1, m2, p1, p2);
		}
	}
	
	private static abstract class AbstractCrossfader implements Animation {
		protected Fadeable f1;
		protected Fadeable f2;
	
		protected AbstractCrossfader(Fadeable f1, Fadeable f2) {
			this.f1 = f1;
			this.f2 = f2;
		}

		public void done() {
			/**XX bug fix: if the animation is done, set the state
			// (necessary if update does not get called, which can happen
			f1.setState(0.0f);
			f2.setState(1.0f);*/
			// we don't want to hold the fadeables longer than necessary
			// (note that this method will be called automatically,
			// which can not be said about deleting the reference to us)
			f1 = null;
			f2 = null;
		}

		public double getDuration() {
			return 1.0;
		}

		public void init() {			
		}

	}
	
	private static final class LinearCrossfader extends AbstractCrossfader implements Animation {
		
		public LinearCrossfader(Fadeable f1, Fadeable f2) {
			super(f1, f2);
		}

		public void update(double time) {
			float f = (float)time;
			if (f1 != null)
				f1.setState(1.0f-f);
			if (f2 != null)
				f2.setState(f);
		}
		
	}
	
	private static final class SmoothCrossfader extends AbstractCrossfader implements Animation {
		
		public SmoothCrossfader(Fadeable f1, Fadeable f2) {
			super(f1, f2);
		}

		public void update(double time) {
			float f = (float)(0.5*Math.cos(Math.PI*time));
			if (f1 != null)
				f1.setState(0.5f+f);
			if (f2 != null)
				f2.setState(0.5f-f);
		}	
	}
	
	private static final class SmoothBlinker extends AbstractCrossfader implements Animation {
		private double periods;
		
		public SmoothBlinker(Fadeable f1, Fadeable f2, double periods) {
			super(f1, f2);
			this.periods = periods;
		}
		
		/*public void done() {
			f1.setState(1.0f);
			f2.setState(0.0f);
			f1 = null;
			f2 = null;
		}*/

		public void update(double time) {
			float f = (float)(0.5*Math.cos(2*periods*Math.PI*time));
			if (f1 != null)
				f1.setState(0.5f+f);
			if (f2 != null)
				f2.setState(0.5f-f);
		}	
	}
	
	private static final class Zoomer implements Animation {
		private Scalable s1;
		private Scalable s2;
		private double factor;
		
		public Zoomer(Scalable s1, Scalable s2, double factor) {
			this.s1 = s1;
			this.s2 = s2;
			this.factor = factor;
		}

		public void update(double time) {
			double f = factor*Math.sin(Math.PI*time);
			if (s1 != null)
				s1.setScale(1.0-f);
			if (s2 != null)
				s2.setScale(1.0+f);
		}

		public void done() {
			s1 = null;
			s2 = null;
		}

		public double getDuration() {
			return 1.0;
		}

		public void init() {
			
		}	
	}
	
	private static abstract class AbstractCrossmover implements Animation {
		protected Moveable m1;
		protected Moveable m2;
		protected Point2D p1;
		protected Point2D p2;
		
		public AbstractCrossmover(Moveable m1, Moveable m2, Point2D p1, Point2D p2) {
			this.m1 = m1;
			this.m2 = m2;
			this.p1 = p1;
			this.p2 = p2;
		}
		
		public void done() {
			m1 = null;
			m2 = null;
		}

		public double getDuration() {
			return 1.0;
		}

		public void init() {
			
		}

	}
	
	private static final class LinearCrossmover extends AbstractCrossmover implements Animation {
		
		public LinearCrossmover(Moveable m1, Moveable m2, Point2D p1, Point2D p2) {
			super(m1, m2, p1, p2);
		}

		public void update(double time) {
			double t2 = time;
			double t1 = 1.0-t2;
			if (m1 != null)
				m1.setPosition(t1*p1.getX()+t2*p2.getX(), t1*p1.getY()+t2*p2.getY());
			if (m2 != null)
				m2.setPosition(t2*p1.getX()+t1*p2.getX(), t2*p1.getY()+t1*p2.getY());
		}
		
	}
	
	private static final class SmoothCrossmover extends AbstractCrossmover implements Animation {
		
		public SmoothCrossmover(Moveable m1, Moveable m2, Point2D p1, Point2D p2) {
			super(m1, m2, p1, p2);
		}

		public void update(double time) {
			double t = 0.5*Math.cos(Math.PI*time);
			double t1 = 0.5+t;
			double t2 = 0.5-t;
			if (m1 != null)
				m1.setPosition(t1*p1.getX()+t2*p2.getX(), t1*p1.getY()+t2*p2.getY());
			if (m2 != null)
				m2.setPosition(t2*p1.getX()+t1*p2.getX(), t2*p1.getY()+t1*p2.getY());
		}
		
	}
	
	// adapters
	// note: using one adapter class for several
	// interfaces (like Moveable, Fadable) saves us keystrokes
	// ---but remember: canvas entities may offer more than one
	// property suitable for fading... and then we will need
	// more adapter classes anyway
	
	private static class TextAdapter implements Fadeable, Moveable {
		private final Text text;
		
		public TextAdapter(Text text) {
			this.text = text;
		}
		
		public void setState(float state) {
			text.setOpacity(state);
		}

		public void setPosition(double x, double y) {
			text.setPosition(x, y);
		}
	}
	
	private static class NodeAdapter implements Fadeable, Moveable, Scalable {
		private final Node node;
		
		public NodeAdapter(Node node) {
			this.node = node;
		}
		
		public void setState(float state) {
			node.setHighlight(state);
		}

		public void setPosition(double x, double y) {
			node.setPosition(x, y);
		}

		public void setScale(double scale) {
			node.setScale(scale);
		}
	}
	
	private static class SequenceElementAdapter implements Fadeable, Moveable, Scalable {
		private final SequenceElement se;
		
		public SequenceElementAdapter(SequenceElement se) {
			this.se = se;
		}
		
		public void setState(float state) {
			se.setHighlight(state);
		}

		public void setPosition(double x, double y) {
			se.setPosition(x, y);
		}

		public void setScale(double scale) {
			se.setScale(scale);
		}
	}
	
	private static class MarkingRectAdapter implements Fadeable {
		private final MarkingRect mr;
		
		public MarkingRectAdapter(MarkingRect mr) {
			this.mr = mr;
		}
		
		public void setState(float state) {
			mr.setOpacity(state);
		}
	}

}
