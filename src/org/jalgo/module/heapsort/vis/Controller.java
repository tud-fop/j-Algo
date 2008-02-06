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

import java.util.LinkedList;
import java.util.List;

import org.jalgo.module.heapsort.Subject;
import org.jalgo.module.heapsort.anim.Animation;
import org.jalgo.module.heapsort.anim.AnimationListener;
import org.jalgo.module.heapsort.anim.AnimationTimeEntity;
import org.jalgo.module.heapsort.anim.TimeEntity;
import org.jalgo.module.heapsort.model.Action;
import org.jalgo.module.heapsort.model.Model;
import org.jalgo.module.heapsort.model.ModelListener;
import org.jalgo.module.heapsort.model.Sequencer;
import org.jalgo.module.heapsort.model.SequencerListener;
import org.jalgo.module.heapsort.model.State;

/**
 * <p>This class coordinates the derivation of the algorithm run
 * and its animation. Thus, it links several parts of the module:
 * the model, the renderer, and the visualisation. It would be
 * legitimate to refer to it as the core of the application
 * (apart from the gui).</p>
 * 
 * <p>At any given time, the controller is in exactly one of several
 * states: Ready (for further derivation commands), being in a macro
 * step (i.e. automatically deriving until a certain point, and thus
 * not taking any commands), playing an animation (not taking any
 * commands either), or waiting for user acknowledgement (accepting
 * only the "cont(inue)" command). The behaviour of the controller
 * depends on a large part on the current state.</p>
 * 
 * <p>The controller maintains a state stack, which is initialized
 * with the ready state. When a derivation takes place, an animation
 * is scheduled and the playing state is pushed on the stack. If the
 * animation consists of several parts which require acknowledgement
 * by the user, the waiting state is pushed in between the parts.</p>
 * 
 * <p>The stack is necessary for two reasons: <strong>a)</strong> states
 * are parametrised (e.g. with the animation to be played), meaning that
 * in effect, there are a lot more states than just those (state classes)
 * named here. Hence, returning to the previous state, which is desirable
 * in many cases, requires direct knowledge of the previous state.</p>
 * 
 * <p><strong>b)</strong> Sometimes, even the state class of the previous
 * state is not determined by the current state. Playing an animation can
 * return to ready or to macro, depending on whether a macro derivation
 * is taking place or not. A stack seems a much more elegant solution
 * than variables indicating where to return to (effectively creating a
 * playing-normal and a playing-macro state).</p>
 * 
 * <p>The controller is linked to the model via the <code>SequencerListener</code>
 * interface (so to speak in a "push" manner), and it is linked to the
 * renderer by the root canvas entity, the canvas entity factory, and
 * the root time entity. Completion of animations can be recognized
 * thanks to the <code>AnimationListener</code>.</p>
 * 
 * <p>Important technical note: The controller can not be seen as some
 * kind of <code>SequencerListener</code> because the controller must own
 * the sequencer in order to prevent anyone from using it while the
 * animation is running. Therefore, the controller has in fact to act
 * as a proxy to for sequencer (even though they don't implement a common
 * interface).</p>
 * 
 * @author mbue
 */
public final class Controller extends Subject<ControllerListener> {
	
	// please find all those numerous inner classes at the end of
	// the class definition
	
	private Model model;
	private Visualisation vis;
	private TimeEntity timeroot;
	
	private Sequencer seq;
	
	private Listener listener;
	private LogListener loglistener;
	private CState currentState = null;
	private boolean lecture;
	
	public Controller(Model model, Visualisation vis, TimeEntity timeroot) {
		// the model is used to create the initial state
		this.model = model;
		// the vis is used to create state rep and action anim
		this.vis = vis;
		// use timeroot to schedule animations
		this.timeroot = timeroot;
		
		// the listeners capture events from underlying layers
		listener = new Listener();
		loglistener = new LogListener();
		
		pushState(new Initial());
	}
	
	public boolean isLecture() {
		return lecture;
	}
	
	public void setLecture(boolean lecture) {
		this.lecture = lecture;
		notifyAll(StateChangedNotifier.getInstance());
	}

	public State getCurrentState() {
		return seq.getCurrentState();
	}

	public void derive() {
		seq.derive();
	}
	
	public boolean isBackPossible() {
		return currentState.isBackPossible();
	}
	
	public boolean isStepPossible() {
		return currentState.isStepPossible();
	}
	
	public boolean isMacroStepPossible() {
		return currentState.isMacroStepPossible();
	}
	
	public boolean isSuspendPossible() {
		return currentState.isSuspendPossible();
	}
	
	public boolean isResetPossible() {
		return canModelChange();
	}
	
	public boolean canModelChange() {
		return currentState.canModelChange();
	}
	
	public Model getModel() {
		return model;
	}
	
	/**
	 * Public method corresponding to reset button
	 * ("go right to the beginning")
	 *
	 */
	public void reset() {
		// This is one way of doing it.
		currentState.modelChanged();
	}

	/**
	 * Public method corresponding to button "&lt;"
	 *
	 */
	public void back() {
		currentState.back();
	}
	
	/**
	 * Public method corresponding to button "&gt;"
	 *
	 */
	public void cont() {
		currentState.cont();
	}
	
	/**
	 * Public method corresponding to button "&lt;&lt;"
	 * (<code>detailLevel</code> would be an application constant)
	 * 
	 * @param detailLevel
	 */
	public void macroBack(int detailLevel) {
		currentState.macroBack(detailLevel);
	}
	
	/**
	 * Public method corresponding to button "&gt;&gt;"
	 * (<code>detailLevel</code> would be an application constant)
	 * 
	 * @param detailLevel
	 */
	public void macroStep(int detailLevel) {
		currentState.macroStep(detailLevel);
	}
	
	public void suspend() {
		currentState.suspend();
	}
	
	public void addListener(SequencerListener l) {
		loglistener.addListener(l);
	}
	
	public void removeListener(SequencerListener l) {
		loglistener.removeListener(l);
	}
	
	private int inpushpopstate = 0;
	
	private synchronized void pushState(CState s) {
		inpushpopstate++;
		try {
			s.prev = currentState;
			currentState = s;
			currentState.init();
		}
		finally {
			inpushpopstate--;
		}
		if (inpushpopstate == 0)
			notifyAll(StateChangedNotifier.getInstance());
	}
	
	private synchronized void popState() {
		inpushpopstate++;
		try {
			CState s = currentState;
			currentState = s.prev;
			s.done();
			currentState.ret();
		}
		finally {
			inpushpopstate--;
		}
		if (inpushpopstate == 0)
			notifyAll(StateChangedNotifier.getInstance());
	}
	
	/// the rest (inner classes)

	/**
	 * Convenience interface to allow uniform handling of
	 * both directions: forward and backwards. This is used
	 * in the macro steps and it is only concerned about
	 * the derivation, not the animation.
	 * 
	 * @author mbue
	 */
	private interface Direction {
		void go();
		boolean canGo();
	}
	
	private class Forward implements Direction {
		public boolean canGo() {
			return seq.isStepPossible();
		}

		public void go() {
			// don't call cont() here because this would
			// call currentState.cont(), which would in turn
			// eventually call this one
			seq.step();
			// XXX think about this call
			seq.derive();
		}		
	}
	
	private class Backwards implements Direction {
		public boolean canGo() {
			return seq.isBackPossible();
		}

		public void go() {
			seq.back();
		}
	}
	
	/**
	 * Controller state, e.g. being ready or playing an animation.
	 * Contains methods which depend on the current state.
	 * States will be managed on a stack, i.e. the "ready" state
	 * might spawn a "playing" state, which might then return
	 * to the ready state.
	 * 
	 * @author mbue
	 */
	private class CState {
		public CState prev;
		
		// we provide empty bodies here because this
		// saves us many keystrokes
		/**
		 * initialize state -- called after pushing on stack
		 * (do it afterwards so pushStack can be called in here)
		 */
		public void init() {
			
		}
		/**
		 * tidy up -- called after popping from stack
		 */
		public void done() {
			
		}
		/**
		 * notification that state becomes active again
		 */
		public void ret() {
			
		}
		
		public Animation[] createAnim(State q, Action a, State q1) {
			// this time: don't factor everything out to use
			// polymorphism instead of if-clauses... destroys locality
			if (isLecture())
				return new Animation[] { vis.setupAnimationLecture(q, a, q1)};
			else
				return vis.setupAnimation(q, a, q1);
		}
		
		public boolean isBackPossible() {
			return false;
		}
		
		public boolean isStepPossible() {
			return false;
		}
		
		public boolean isMacroStepPossible() {
			return false;
		}
		
		public boolean isSuspendPossible() {
			// go up in the stack (must be overridden by Ready)
			return prev.isSuspendPossible();
		}
		
		public void back() {
			
		}
		
		public void cont() {
			
		}
		
		public void macroStep(int detailLevel) {
			
		}
		
		public void macroBack(int detailLevel) {
			
		}
		
		public void suspend() {
			// go up in the stack (must be overridden by Ready)
			prev.suspend();
		}
		
		public void modelChanged() {
			
		}
		
		/**
		 * Returns whether it is possible to change the model.
		 * Changing the model means restarting the derivation
		 * from scratch.
		 * 
		 * @return
		 */
		public boolean canModelChange() {
			return false;
		}
	}
	
	private final class Initial extends CState {
		
		@Override
		public void init() {
			modelChanged();
			((Subject<ModelListener>)model).addListener(listener);
		}
		
		@Override
		public void modelChanged() {
			seq = new Sequencer(model.getInitialState());
			// XXX think about this call
			seq.derive();
			seq.addListener(listener);
			seq.addListener(loglistener);
			pushState(new Ready());
		}
		
		@Override
		public boolean canModelChange() {
			return true;
		}
		
		@Override
		public void ret() {
			modelChanged();
		}
	}
	
	/**
	 * Controller is ready.
	 * 
	 * @author mbue
	 */
	private final class Ready extends CState {
		
		@Override
		public void init() {
			vis.setupState(getCurrentState());
		}
		
		@Override
		public void back() {
			seq.back();
			
		}

		@Override
		public void cont() {
			seq.step();
			// XXX think about this call
			seq.derive();
		}
		
		private void macro(int detailLevel, Direction dir) {
			// the first step of a macro step is always taken independent of the detail level
			// otherwise, a macro step could result in a "no-step"
			if (dir.canGo()) {
				pushState(new MacroState(detailLevel, dir));
				dir.go();
			}			
		}
		
		@Override
		public void macroStep(int detailLevel) {
			macro(detailLevel, new Forward());
		}
		
		@Override
		public void macroBack(int detailLevel) {
			macro(detailLevel, new Backwards());			
		}

		@Override
		public boolean isBackPossible() {
			return seq.isBackPossible();
		}

		@Override
		public boolean isStepPossible() {
			return seq.isStepPossible();
		}
		
		@Override
		public boolean isMacroStepPossible() {
			return seq.isStepPossible();
		}
		
		@Override
		public boolean isSuspendPossible() {
			return false;
		}
		
		@Override
		public void suspend() {
			
		}
		
		@Override
		public void modelChanged() {
			popState();
		}
		
		@Override
		public boolean canModelChange() {
			return true;
		}
		
	}
	
	/**
	 * Playing animation until a certain detail level is reached.
	 * 
	 * @author mbue
	 */
	private final class MacroState extends CState {
		private int detailLevel;
		private Direction direction;
		private boolean suspended = false;
		
		public MacroState(int detailLevel, Direction direction) {
			this.detailLevel = detailLevel;
			this.direction = direction;
		}
		
		public Animation[] createAnim(State q, Action a, State q1) {
			return new Animation[] { vis.setupAnimationMacro(q, a, q1)};
		}
		
		@Override
		public boolean isSuspendPossible() {
			return true;
		}
		
		@Override
		public void suspend() {
			suspended = true;
		}
		
		/**
		 * playing animation has finished, so make a step
		 * or be done
		 */
		@Override
		public void ret() {
			// citing State#getDetailLevel Javadoc:
			/* The program will allow the user to automatically
			 * derive up to the next state which has at most a
			 * certain detail level. */
			if (!suspended && (getCurrentState().getDetailLevel() > detailLevel) && direction.canGo()) {
				direction.go();
			}
			else
				popState();
		}
		
	}
	
	/**
	 * Playing a sequence of animations.
	 * 
	 * @author mbue
	 */
	private final class PlayAnimState extends CState implements AnimationListener {
		private Animation[] anim;
		private int current;
		private AnimationTimeEntity cate; // current animation time entity
		
		public PlayAnimState(Animation[] anim) {
			this.anim = anim;
			current = 0;
		}
		
		//@Override // unlike 1.6, Java 1.5 does not like this
		public void animationComplete(Animation a) {
			/*
			 * this will most likely be executed in a different thread
			 * but there is no concurrency involved because
			 * the normal thread won't do anything (significant)
			 */
			if (a == anim[current]) {
				a.done();
				timeroot.removeChild(cate);
				cate = null;
				current++;
				if (current < anim.length)
					pushState(new AckWaitState());
				else
					popState();
			}
		}
		
		@Override
		public void init() {
			startAnim();
		}
		
		@Override
		public void ret() {
			startAnim();
		}
		
		private void startAnim() {
			assert(cate == null);
			
			Animation aa = anim[current];
			cate = new AnimationTimeEntity(aa, timeroot.now());
			cate.setListener(this);
			aa.init();
			aa.update(0.0); // XXX this is fucking depressing!
			timeroot.addChild(cate);
		}
		
	}
	
	/**
	 * Waiting for acknowledgement.
	 * 
	 * @author mbue
	 */
	private final class AckWaitState extends CState {
		@Override
		public void cont() {
			popState();
		}
		
		@Override
		public boolean isStepPossible() {
			return true;
		}
	}

	private final class Listener implements SequencerListener, ModelListener {

		public void back(State q, Action a, State q1) {
			Animation aa = vis.setupAnimationBack(q, a, q1);
			pushState(new PlayAnimState(new Animation[] { aa }));
		}

		public void step(State q, Action a, State q1) {
			Animation[] aa = currentState.createAnim(q, a, q1);
			//System.out.println(a.toString());
			pushState(new PlayAnimState(aa));
		}

		public void stepAvail() {
			// just ignore it
			// we will just call step (not derive) and be done
			
		}

		public void modelChanged() {
			currentState.modelChanged();
		}
		
	}
	
	private static final class LogListener implements SequencerListener {

		private List<SequencerListener> loggers;

		public void addListener(SequencerListener l) {
			if (loggers == null)
				loggers = new LinkedList<SequencerListener>();
			loggers.add(l);
		}
		
		public void removeListener(SequencerListener l) {
			if (loggers != null)
				loggers.remove(l);
		}
		
		public void back(State q, Action a, State q1) {
			if (loggers != null)
				for (SequencerListener l: loggers)
					l.back(q, a, q1);
		}

		public void step(State q, Action a, State q1) {
			if (loggers != null)
				for (SequencerListener l: loggers)
					l.step(q, a, q1);
		}

		public void stepAvail() {
			if (loggers != null)
				for (SequencerListener l: loggers)
					l.stepAvail();
		}
		
	}
	
	private static final class StateChangedNotifier implements Notifier<ControllerListener> {
		private static StateChangedNotifier arnie = new StateChangedNotifier();
		
		public static StateChangedNotifier getInstance() {
			return arnie;
		}

		public void invoke(ControllerListener l) {
			l.stateChanged();
		}
		
	}

}
