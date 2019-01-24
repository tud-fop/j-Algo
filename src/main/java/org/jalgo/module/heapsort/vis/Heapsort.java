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
package org.jalgo.module.heapsort.vis;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.jalgo.module.heapsort.anim.Animation;
import org.jalgo.module.heapsort.anim.Animations;
import org.jalgo.module.heapsort.anim.CompositeAnimation;
import org.jalgo.module.heapsort.anim.Fadeable;
import org.jalgo.module.heapsort.anim.ParallelAnimation;
import org.jalgo.module.heapsort.anim.ProxyAnimation;
import org.jalgo.module.heapsort.anim.SequentialAnimation;
import org.jalgo.module.heapsort.model.Action;
import org.jalgo.module.heapsort.model.State;
import org.jalgo.module.heapsort.model.Heapsort.*;
import org.jalgo.module.heapsort.renderer.CanvasEntity;
import org.jalgo.module.heapsort.renderer.CanvasEntityFactory;
import org.jalgo.module.heapsort.renderer.Edge;
import org.jalgo.module.heapsort.renderer.MarkingRect;
import org.jalgo.module.heapsort.renderer.Node;
import org.jalgo.module.heapsort.renderer.SequenceElement;
import org.jalgo.module.heapsort.renderer.Text;

/**
 * <p>This implements the actual Heapsort visualisation.</p>
 * 
 * <p>The concept is as follows: When setting up a state on the
 * root canvas, a so-called "state dictionary" is built which
 * tells us which semantic object is represented by which
 * canvas entity.</p>
 * 
 * <p>Animations can use that dictionary to find the objects
 * they have to modify, and they can add and remove objects
 * from the dictionary as necessary for the state transition.</p>
 * 
 * <p>This concept replaces the former one where the visualisation
 * had no access to the root entity and the dictionary was modeled
 * explicity by means of a canvas database (merely a hierarchical
 * map String-&gt;CanvasEntity), which would be used to create the
 * actual canvas entities for display. This was not to the point
 * and overly complicated.</p>
 * 
 * <p>P.S.: Don't look at the code that thoroughly.
 * In contrast to the infrastructure code, this is rather messy.
 * This is due to the fact that it just has to work, which it does.
 * You know, the beauty lies outside (in the animations to be seen
 * on screen).</p>
 * 
 * @author mbue
 *
 */
public final class Heapsort implements Visualisation {
	
	// general
	private HeapsortState current = null;
	private HeapsortState next = null;
	private CanvasEntity root;
	private CanvasEntityFactory f;
	// static appearance information
	private static final int MAX_NODES = 31; // five levels
	private static Point[] nodepos;
	private static Point[] seqpos;
	private static Point[] edgefrom;
	private static Point[] edgeto;
	private static Point lidisp = new Point(40,0); // li displacement
	private static Color recol = new Color(192,192,192);
	private static Color undercol = Color.RED;
	// state dictionary
	private List<SequenceElement> seq;
	private List<Text> seqind;
	private List<Node> nodes;
	private List<Edge> edges;
	private MarkingRect under;
	private Text li;
	private List<MarkingRect> re;
	private MarkingRect re2;
	private Text cmpres;
	private int first; // number of visible nodes
	private int last; // number of visible nodes in next step
	
	public Heapsort(CanvasEntity root, CanvasEntityFactory f) {
		this.root = root;
		this.f = f;
		// need fast indexing
		seq = new ArrayList<SequenceElement>();
		seqind = new ArrayList<Text>();
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
		re = new ArrayList<MarkingRect>();
	}
	
	// --- static stuff
	
	private static void computeNodepos(int i, int x, int y, int d) {
		nodepos[i] = new Point(x, y);
		if (i <= (MAX_NODES-3)/2) {
			computeNodepos(2*i+1, x-d, y+75, 9*d/20);
			computeNodepos(2*i+2, x+d, y+75, 9*d/20);
		}
	}
	
	{
		nodepos = new Point[MAX_NODES];
		seqpos = new Point[MAX_NODES];
		edgefrom = new Point[MAX_NODES];
		edgeto = new Point[MAX_NODES];
		//computeNodepos(0, 350, 30, 175);
		// assume 0 is center of screen
		computeNodepos(0, 0, 30, 175+15+30);
		for (int ind = 0; ind < MAX_NODES; ind++)
			seqpos[ind] = new Point(-270+ind*40, 400);
		for (int ind = 1; ind < MAX_NODES; ind++) {
			edgefrom[ind] = nodepos[(ind-1)/2];
			edgeto[ind] = nodepos[ind];
			int dx = edgefrom[ind].x-edgeto[ind].x;
			int dy = edgefrom[ind].y-edgeto[ind].y;
			double oo = 1/Math.sqrt(dx*dx+dy*dy);
			edgefrom[ind] = new Point(
					(int)(edgefrom[ind].x-30*oo*dx),
					(int)(edgefrom[ind].y-30*oo*dy));
			edgeto[ind] = new Point(
					(int)(edgeto[ind].x+30*oo*dx),
					(int)(edgeto[ind].y+30*oo*dy));
		}
	}
	
	// --- state stuff

	/* (non-Javadoc)
	 * @see org.jalgo.module.heapsort.vis.Visualisation#setupState(org.jalgo.module.heapsort.model.State)
	 */
	public void setupState(State q) {
		if (q != current) {
			if (q instanceof HeapsortState)
				setupState((HeapsortState)q);
			else // XXX being lazy: postponing exception (will get null pointer exception)
				teardown();
		}
	}
	
	private void setupState(HeapsortState q) {
		teardown();
		current = q;
		setup();
	}
	
	/* the following methods fill the dictionary
	 * but they don't add anything to the canvas
	 * this allows us to use them in animations
	 * especially in composite ones
	 */
	
	private void addLiPointer(int li) {
		Text t = f.createText("\u2190", 20, 20);
		Point p = nodepos[li];
		t.setPosition(new Point(p.x+lidisp.x, p.y+lidisp.y));
		t.setScale(1.7);
		t.setZorder(1000);
		this.li = t;
	}

	private void addReMarking(int re, int n) {
		// go backwards through the nodes 
		re++; // --> point at the first entry to be marked
		n--;  // --> point at the last entry to be marked
		this.re.clear();
		while (re <= n) {			
			// get position of last entry (of current line)
			Point p1 = nodepos[n];
			// find first position of this line
			Point p2 = p1; // will be the wanted first position
			Point p3 = p1; // will be the last position on the line above
			while ((re <= n) && (p3.y == p1.y)) {
				n--;
				p2 = p3;
				p3 = nodepos[n];				
			}
			MarkingRect mr = f.createMarkingRect();
			mr.setColor(recol);
			mr.setPosition(new Point(p2.x-30,p2.y-30));
			mr.setWidth(p1.x-p2.x+60);
			mr.setHeight(p1.y-p2.y+60);
			this.re.add(mr);
		}
	}
	
	private int addCmpRes() {
		Phase12sl q = (Phase12sl)current;
		int j = 2*q.i+1;
		if (j+1 <= q.re) {
			String s;
			if (q.sequence.get(j) < q.sequence.get(j+1)) {
				s = "<";
				j++;
			}
			else
				s = ">";
			// XXX uncomment the "if" if you want the comparison in any case
			if (q.sequence.get(q.i) < q.sequence.get(j)) {
				cmpres = f.createText(s, 20, 20);
				cmpres.setPosition(new Point(nodepos[q.i].x, nodepos[2*q.i+1].y));
				cmpres.setScale(1.7);
			}
		}
		
		if (q.sequence.get(q.i) < q.sequence.get(j))
			return j;
		else
			return -1;
	}
	
	private void addUnder(int first, int last) {
		if (first < last) {
			Point p = seqpos[first];
			Point p1 = seqpos[last-1];
			under = f.createMarkingRect();
			under.setColor(undercol);
			under.setPosition(new Point(p.x-15, p.y+18));
			under.setWidth(p1.x-p.x+31);
			under.setHeight(3);
		}
	}
	
	private void setup() {
		// first: fill dictionary
		
		// sequence elements, their indices, nodes, edges 
		for (int ind = 0; ind < current.sequence.size(); ind++) {
			SequenceElement se = f.createSequenceElement(
					seqpos[ind],
					String.valueOf(current.sequence.get(ind)));
			se.setZorder(100+ind);
			seq.add(se);
			Text sei = f.createText(String.valueOf(ind), 16, 10);
			sei.setPosition(seqpos[ind].x+6, seqpos[ind].y+30);
			sei.setOpacity(0.5f);
			sei.setRegular(true);
			seqind.add(sei);
			// XXX create node at seqpos...
			Node nd = f.createNode(seqpos[ind], current.sequence.get(ind).toString());
			nd.setZorder(102+ind); // 102 instead of 100 to be on top of seq (extend anim)
			nodes.add(nd);
			if (ind > 0) {
				Edge e = f.createEdge(edgefrom[ind], edgeto[ind]);
				e.setZorder(1);
				edges.add(e);
			}
		}
		
		// compute which nodes have to be shown
		if (current instanceof InitialState)
			first = 0;
		else if (current instanceof Phase0) {
			int level = ((Phase0)current).level;
			first = 0;
			while (level > 0) {
				first = 2*first+1;
				level--;
			}
			if (first > current.sequence.size())
				first = current.sequence.size();
		}
		else
			first = current.sequence.size();
		last = Math.min(2*first+1, current.sequence.size());
		
		if (current instanceof Phase0)
			addUnder(first, last);
		// li pointer and re marker
		if (current instanceof Phase12) {
			int li = ((Phase12)current).li;
			int re = ((Phase12)current).re;
			
			if (re == current.sequence.size()-1) {
				if (!(current instanceof Phase2))
					addLiPointer(li);
			}
			else {
				addReMarking(re, current.sequence.size());
				re2 = f.createMarkingRect();
				re2.setColor(recol);
				re2.setPosition(new Point(seqpos[re+1].x-20, seqpos[re+1].y-20));
				re2.setHeight(40);
				re2.setWidth(seqpos[current.sequence.size()-1].x-seqpos[re+1].x+40);
			}
		}
		// highlights and stuff 
		if (current instanceof Phase12sl) {
			int i = ((Phase12sl)current).i;
			nodes.get(i).setHighlight(1.0f);
			seq.get(i).setHighlight(1.0f);
			if (current instanceof Phase12slb) {
				int j = addCmpRes();
				if (j != -1) {
					nodes.get(j).setHighlight(1.0f);
					seq.get(j).setHighlight(1.0f);
				}
			}
		}
		
		// second: put on screen
		for (int ind = 0; ind < first; ind++) {
			nodes.get(ind).setPosition(nodepos[ind]);
			root.addChild(nodes.get(ind));
		}
		for (int ind = 1; ind < first; ind++)
			root.addChild(edges.get(ind-1));
		for (SequenceElement s: seq)
			root.addChild(s);
		for (Text s: seqind)
			root.addChild(s);
		for (MarkingRect m: re)
			root.addChild(m);
		if (under != null)
			root.addChild(under);
		if (re2 != null)
			root.addChild(re2);
		if (li != null)
			root.addChild(li);
		if (cmpres != null)
			root.addChild(cmpres);
	}
	
	/* (non-Javadoc)
	 * @see org.jalgo.module.heapsort.vis.Visualisation#teardown()
	 */
	public void teardown() {
		if (current != null) {
			for (Node n: nodes)
				root.removeChild(n);
			nodes.clear();
			first = 0;
			for (Edge e: edges)
				root.removeChild(e);
			edges.clear();
			for (SequenceElement s: seq)
				root.removeChild(s);
			seq.clear();
			for (Text s: seqind)
				root.removeChild(s);
			seqind.clear();
			for (MarkingRect m: re)
				root.removeChild(m);
			re.clear();
			if (under != null) {
				root.removeChild(under);
				under = null;
			}
			if (re2 != null) {
				root.removeChild(re2);
				re2 = null;
			}
			if (li != null) {
				root.removeChild(li);
				li = null;
			}
			if (cmpres != null) {
				root.removeChild(cmpres);
				cmpres = null;
			}
			current = null;
		}
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.heapsort.vis.Visualisation#createActionAnimation(org.jalgo.module.heapsort.vis.CanvasDb, org.jalgo.module.heapsort.renderer.CanvasEntityFactory, org.jalgo.module.heapsort.model.State, org.jalgo.module.heapsort.model.Action, org.jalgo.module.heapsort.model.State)
	 */
	public Animation[] setupAnimation(State q, Action a, State q1) {
		return new Animation [] { setupAnimationLecture(q, a, q1) };
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.heapsort.vis.Visualisation#createActionAnimationBack(org.jalgo.module.heapsort.vis.CanvasDb, org.jalgo.module.heapsort.renderer.CanvasEntityFactory, org.jalgo.module.heapsort.model.State, org.jalgo.module.heapsort.model.Action, org.jalgo.module.heapsort.model.State)
	 */
	public Animation setupAnimationBack(State q, Action a, State q1) {
		setupState(q);
		next = (HeapsortState)q1;
		return new BackAnim();
	}
	
	/* (non-Javadoc)
	 * @see org.jalgo.module.heapsort.vis.Visualisation#createActionAnimationLecture(org.jalgo.module.heapsort.vis.CanvasDb, org.jalgo.module.heapsort.renderer.CanvasEntityFactory, org.jalgo.module.heapsort.model.State, org.jalgo.module.heapsort.model.Action, org.jalgo.module.heapsort.model.State)
	 */
	public Animation setupAnimationLecture(State q, Action a, State q1) {
		// this call won't harm because state should not have changed
		setupState(q);
		next = (HeapsortState)q1;
		Animation result = null;
		// don't tell me that this looks ugly...
		// it works, that's enough -- don't want any more "invoke"
		if (Actions.isExtend(a))
			result = new ExtendAnim();
		if (Actions.isStartphase0(a))
			result = new Startphase0Anim();
		if (Actions.isStartphase1(a))
			result = new Startphase1Anim();
		if (Actions.isDecli(a))
			result = new DecliAnim();
		if (Actions.isReturnphase1(a) || Actions.isReturnphase2(a))
			result = new ReturnAnim();
		if (Actions.isStartphase2(a))
			result = new Startphase2Anim();
		if (Actions.isSwapDecre(a))
			result = new SwapDecreAnim();
		if (Actions.isFinish(a))
			result = new FinishAnim();
		if (Actions.isStartsl(a))
			result = new StartslAnim();
		if (Actions.isCompare(a))
			result = new CompareAnim();
		if (Actions.isSwap(a))
			result = new TheSwapAnim();
		assert(result != null);
		result = new ProxyAnimation(result) {
			public void done() {
				super.done();
				current = next;
				next = null;
			}
		};
		return result;
	}

	// ignore macro, this is the same as lecture!
	/* (non-Javadoc)
	 * @see org.jalgo.module.heapsort.vis.Visualisation#createActionAnimationMacro(org.jalgo.module.heapsort.vis.CanvasDb, org.jalgo.module.heapsort.renderer.CanvasEntityFactory, org.jalgo.module.heapsort.model.State, org.jalgo.module.heapsort.model.Action, org.jalgo.module.heapsort.model.State)
	 */
	public Animation setupAnimationMacro(State q, Action a, State q1) {
		return setupAnimationLecture(q, a, q1);
	}

	
	// --- animations (backwards)
	
	private class BackAnim implements Animation {

		public void done() {
			setupState(next);
			next = null;
		}

		public double getDuration() {
			return 0.1;
		}

		public void init() {
		}

		public void update(double time) {
		}
		
	}
	
	
	// --- animations (Lecture)

	// startphase0: --> show under
	private class Startphase0Anim extends ProxyAnimation {
		
		public Startphase0Anim() {
			addUnder(first, last);
			delegate = Animations.createInFader(Animations.SMOOTH_FADER, under);
		}
		
		public void init() {
			root.addChild(under);
			super.init();
		}
		
		public double getDuration() {
			return 0.4;
		}

	}
	
	// extend: --> "copy" sequence elements as nodes
	private class ExtendAnim extends SequentialAnimation {
		private MarkingRect oldunder;
		private int newlast = Math.min(2*last+1, current.sequence.size());
		
		public ExtendAnim() {
			oldunder = under;
			under = null;
			addUnder(last, newlast);
			// move in nodes
			add(new ProxyAnimation(Animations.createOutFader(
					Animations.SMOOTH_FADER, new Fadeable() {
						public void setState(float state) {
							float f1 = state;
							float f2 = 1.0f - state;
							for (int ind = first; ind < last; ind++) {
								Point p1 = seqpos[ind];
								Point p2 = nodepos[ind];
								nodes.get(ind).setPosition(
										f1 * p1.x + f2 * p2.x,
										f1 * (p1.y - 15) + f2 * p2.y);
							}
						}
					})) {
				public void init() {
					for (int ind = first; ind < last; ind++)
						root.addChild(nodes.get(ind));
				}
			}, 1.5);
			// fade in edges
			if (first > 0)
				add(new ProxyAnimation(Animations.createInFader(
						Animations.SMOOTH_FADER, new Fadeable() {
							public void setState(float state) {
								for (int ind = first; ind < last; ind++)
									edges.get(ind - 1).setOpacity(state);
							}
						})) {
					public void init() {
						for (int ind = first; ind < last; ind++)
							root.addChild(edges.get(ind - 1));
					}
				}, 0.5);
			// show underbars
			if (under == null)
				add(Animations.createOutFader(Animations.SMOOTH_FADER, oldunder), 0.5);
			else {
				under.setOpacity(0.0f);
				add(Animations.createCrossfader(Animations.SMOOTH_FADER, oldunder, under), 0.5);
				add(Animations.createBlinker(under, 2), 0.4);
			}
		}

		public void init() {
			if (under != null) {
				under.setOpacity(0.0f);
				root.addChild(under);
			}
			super.init();
		}
		
		@Override
		public void done() {
			super.done();
			root.removeChild(oldunder);
			first = last;
			last = newlast;
		}
		
	}

	// startphase1: --> fade in li pointer
	private class Startphase1Anim extends ProxyAnimation {
		
		public Startphase1Anim() {
			addLiPointer(((Phase1)next).li);
			delegate = Animations.createInFader(Animations.SMOOTH_FADER, li);
		}

		public double getDuration() {
			return 0.4;
		}

		public void init() {
			root.addChild(li);
			super.init();
		}
	}

	// startphase2: --> fade out li pointer
	private class Startphase2Anim extends ProxyAnimation {
		
		public Startphase2Anim() {
			delegate = Animations.createOutFader(Animations.SMOOTH_FADER, li);
		}

		public void done() {
			super.done();
			root.removeChild(li);
			li = null;
		}

		public double getDuration() {
			return 0.4;
		}
	}

	// startsl --> fade in highlight
	private class StartslAnim extends ParallelAnimation {
		
		public StartslAnim() {
			super(1.0);
			int i = ((Phase12sl)next).i;
			add(Animations.createInFader(Animations.SMOOTH_FADER, nodes.get(i)));
			add(Animations.createInFader(Animations.SMOOTH_FADER, seq.get(i)));
		}
		
	}

	// returnphase* --> fade out highlight
	private class ReturnAnim extends ParallelAnimation {
		public ReturnAnim() {
			this(((Phase12sl)current).i);
		}
		
		public ReturnAnim(int i) {
			super(1.0);
			add(Animations.createOutFader(Animations.SMOOTH_FADER, nodes.get(i)));
			add(Animations.createOutFader(Animations.SMOOTH_FADER, seq.get(i)));
			if ((next instanceof Phase2) && (li != null))
				add(new Startphase2Anim());
		}
	}

	// decli: --> move li pointer
	private class DecliAnim extends ProxyAnimation {
		public DecliAnim() {
			Point p1 = nodepos[((Phase12)current).li];
			Point p2 = nodepos[((Phase12)next).li];
			delegate = Animations.createMover(Animations.SMOOTH_MOVER, li,
					new Point(p1.x+lidisp.x, p1.y+lidisp.y),
					new Point(p2.x+lidisp.x, p2.y+lidisp.y));
		}

		public double getDuration() {
			return 1;
		}
	}
	
	// finish --> fade out marking
	private class FinishAnim extends ParallelAnimation {
		private List<MarkingRect> re_;
		private MarkingRect re2_;
		
		public FinishAnim() {
			super(1.0);
			re_ = new ArrayList<MarkingRect>(re);
			re.clear();
			add(Animations.createOutFader(Animations.SMOOTH_FADER, new Fadeable() {				
				public void setState(float state) {
					for (MarkingRect m: re_)
						m.setOpacity(state);				
				}
			}));
			if (re2 != null) {
				re2_ = re2;
				add(Animations.createOutFader(Animations.SMOOTH_FADER, re2_));
			}
			if (current instanceof Phase12sl) {
				int i = ((Phase12sl)current).i;
				add(Animations.createOutFader(Animations.SMOOTH_FADER, nodes.get(i)));
				add(Animations.createOutFader(Animations.SMOOTH_FADER, seq.get(i)));
			}
			
		}
		
		public void done() {
			super.done();
			for (MarkingRect m: re_)
				root.removeChild(m);
			if (re2_ != null)
				root.removeChild(re2_);
		}
	}

	//
	private class SwapAnim extends ParallelAnimation {
		private int i;
		private int j;
		private Node ndi;
		private Node ndj;
		private SequenceElement sei;
		private SequenceElement sej;
		
		public SwapAnim(int i, int j) {
			super(1.0);
			this.i = i;
			this.j = j;
			ndi = nodes.get(i);
			ndj = nodes.get(j);
			sei = seq.get(i);
			sej = seq.get(j);
			add(Animations.createCrossmover(Animations.SMOOTH_MOVER, ndi, ndj, nodepos[i], nodepos[j]));
			add(Animations.createCrossmover(Animations.SMOOTH_MOVER, sei, sej, seqpos[i], seqpos[j]));
			add(Animations.createZoomer(ndi, ndj, 0.6));
			add(Animations.createZoomer(sei, sej, 0.6));
		}

		public void done() {
			super.done();
			// changing zorder --> we have to remove
			root.removeChild(ndi);
			root.removeChild(ndj);
			root.removeChild(sei);
			root.removeChild(sej);
			nodes.set(i, ndj);
			nodes.set(j, ndi);
			ndi.setZorder(100+j);
			ndj.setZorder(100+i);
			seq.set(i, sej);
			seq.set(j, sei);
			sei.setZorder(100+j);
			sej.setZorder(100+i);
			root.addChild(ndi);
			root.addChild(ndj);
			root.addChild(sei);
			root.addChild(sej);
		}
	}
	
	// decre: ...
	private class DecreAnim implements Animation {
		private Point p = new Point();
		private Point np; // nodepos[re]
		private Point sp; // seqpos[re]
		private Point npi; // initial position of re
		private Point spi; // initial position of re2
		private int nwi;   // initial width of re
		private int swi;   // initial width of re2

		public void done() {
		}

		public double getDuration() {
			return 1;
		}

		public void init() {
			int re_ = ((Phase12)current).re;
			np = nodepos[re_];
			sp = seqpos[re_];
			if ((re.size() == 0) ||
					(re.get(re.size()-1).getPosition().y != np.y-30)) {
				p.setLocation(np.x+30, np.y-30);
				MarkingRect mr = f.createMarkingRect();
				mr.setColor(recol);
				mr.setPosition(p);
				mr.setHeight(60);
				mr.setWidth(0);
				re.add(mr);
				root.addChild(mr);
			}
			if (re2 == null) {
				re2 = f.createMarkingRect();
				p.setLocation(sp.x+20, sp.y-20);
				re2.setColor(recol);
				re2.setPosition(p);
				re2.setHeight(40);
				re2.setWidth(0);
				root.addChild(re2);
			}
			npi = new Point(re.get(re.size()-1).getPosition());
			nwi = re.get(re.size()-1).getWidth();
			spi = new Point(re2.getPosition());
			swi = re2.getWidth();
		}

		public void update(double time) {
			double t1 = 1.0-time;
			double t2 = time;
			p.setLocation(t1*npi.x+t2*(np.x-30),t1*npi.y+t2*(np.y-30));
			MarkingRect mr = re.get(re.size()-1);
			mr.setPosition(p);
			mr.setWidth(npi.x-p.x+nwi);
			p.setLocation(t1*spi.x+t2*(sp.x-20),t1*spi.y+t2*(sp.y-20));
			re2.setPosition(p);
			re2.setWidth(spi.x-p.x+swi);
		}
	}
	
	// swap_decre: --> swap, extend marker
	private class SwapDecreAnim extends SequentialAnimation {
		
		public SwapDecreAnim() {
			add(new SwapAnim(0, ((Phase12)current).re), 1);
			add(new DecreAnim(), 1);
		}
		
	}

	// compare: --> fade in cmp_result
	private class CompareAnim extends ParallelAnimation {
		private int j;
		
		public CompareAnim() {
			super(1.0);
			j = addCmpRes();
			if (cmpres != null)
				add(Animations.createInFader(Animations.SMOOTH_FADER, cmpres));
			if (j != -1) {
				add(Animations.createInFader(Animations.SMOOTH_FADER, nodes.get(j)));
				add(Animations.createInFader(Animations.SMOOTH_FADER, seq.get(j)));
			}
		}

		public void init() {
			if (cmpres != null)
				root.addChild(cmpres);
			super.init();
		}
	}

	// swap --> swap, take care of highlight
	private class TheSwapAnim extends CompositeAnimation {
		
		public TheSwapAnim() {
			int i = ((Phase12sl)current).i;
			int j = ((Phase12sl)next).i;
			add(new SwapAnim(i, j), 0, 1);
			// use next.i because of swapping
			add(Animations.createOutFader(Animations.SMOOTH_FADER, nodes.get(j)), 1, 0.5);
			add(Animations.createOutFader(Animations.SMOOTH_FADER, seq.get(j)), 1, 0.5);			
			if (cmpres != null)
				add(new ProxyAnimation() {
						private Text cmpres_ = cmpres;
						
						{
							cmpres = null;
							delegate = Animations.createOutFader(Animations.SMOOTH_FADER, cmpres_);
						}
						
						public void done() {
							root.removeChild(cmpres_);
							super.done();
						}
					}, 1, 0.5);			
		}

		public double getDuration() {
			return 1.5;
		}
	}
	
}