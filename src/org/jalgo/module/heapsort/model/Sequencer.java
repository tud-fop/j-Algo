package org.jalgo.module.heapsort.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.jalgo.module.heapsort.Subject;

/**
 * <p>The sequencer is used to construct a derivation. From a current state, it
 * derives successor states and stores them. Following some derivation strategy,
 * it will select the successor which will become the current state.</p>
 * 
 * <p>In addition, the sequencer can be persisted.</p>
 * 
 * <p>Note: ATM, no strategy is implemented. Only deterministic algorithms
 * are supported!</p>
 * 
 * @author mbue
 */
public final class Sequencer extends Subject<SequencerListener> {
	/**
	 * A data structure needed in the derivation. It stores the successors of
	 * some state along with the index of the successor chosen by the strategy
	 * the be reduced further.
	 * 
	 * @author mbue
	 */
	private static class Level {
		/**
		 * The successor which was chosen by the strategy to be reduced further.
		 */
		int current;
		/**
		 * The data for the current successor.
		 */
		Level next;
		/**
		 * Parent.
		 */
		Level prev;
		/**
		 * The list of successors.
		 */
		List<Entry<State,Action>> successors;
	}
	
	/**
	 * Some token implementation of Map.Entry, which is needed for
	 * the initial level. (See constructor of <code>Sequencer</code>).
	 * 
	 * @author mbue
	 *
	 * @param <K>
	 * @param <V>
	 */
	private static class MyEntry<K,V> implements Entry {
		
		K key;
		V value;
		
		public MyEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public K getKey() {
			return key;
		}

		public V getValue() {
			return value;
		}

		@SuppressWarnings("unchecked")
		public Object setValue(Object arg0) {
			V old = value;
			value = (V)arg0;
			return old;
		}
		
	}
	
	private Level head = null;
	private Level current = null;
	
	@SuppressWarnings("unchecked")
	public Sequencer(State initial) {
		head = new Level();
		head.current = 0;
		head.next = null;
		head.prev = null;
		head.successors = new LinkedList<Entry<State,Action>>();
		head.successors.add(new MyEntry<State,Action>(initial, null));
		current = head;
	}
	
	/**
	 * If not already done, reduce the current state. Does not change the current state,
	 * though---use <code>step</code> to achieve that. This method will trigger <code>stepAvail</code>
	 * iff derivation is possible.
	 */
	public void derive() {
		if (current.next == null) {
			Set<Entry<State,Action>> entries =
				current.successors.get(current.current).getKey().
				computeSuccessors().entrySet();
			if (!entries.isEmpty()) {
				Level tail = new Level();
				tail.next = null;
				tail.prev = current;
				tail.current = 0;
				tail.successors = new LinkedList<Entry<State,Action>>(entries);
				current.next = tail;
				notifyAll(NotifyStepAvail.getInstance());
			}
		}
	}
	
	/**
	 * Make a derivation step. Uses <code>derive()</code> if necessary, but won't trigger
	 * <code>stepAvailable</code>. If a step is actually taken, <code>step</code> will
	 * be triggered.
	 * 
	 * @return True iff some step could be taken.
	 */
	public boolean step() {
		if (current.next == null) {
			suspendUpdates();
			try {
				derive();
			}
			finally {
				resumeUpdates();
			}
		}
		if (current.next != null) {
			Entry<State,Action> e = null;
			Level old = current;
			
			current = current.next;
			
			e = current.successors.get(current.current);
			notifyAll(new NotifyStep(old.successors.get(old.current).getKey(),
					e.getValue(), e.getKey()));
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Go backwards in the derivation, triggering <code>back</code>.
	 * Return <code>false</code> iff current state already is the initial state.
	 * 
	 * @return True iff some step could be taken.
	 */
	public boolean back() {
		if (current.prev != null) {
			Entry<State,Action> e = current.successors.get(current.current);
			current = current.prev;
			
			// arguments: ex-current state, action which got us there, ex-previous state
			notifyAll(new NotifyBack(e.getKey(), e.getValue(),
					current.successors.get(current.current).getKey()));
			return true;
		}
		else
			return false;
	}

	public State getCurrentState() {
		return current.successors.get(current.current).getKey();
	}
	
	public boolean isBackPossible() {
		return current.prev != null;
	}
	
	public boolean isStepPossible() {
		return current.next != null;
	}
	
	private static class NotifyStepAvail implements Notifier<SequencerListener> {
		private static NotifyStepAvail arnie = new NotifyStepAvail();
		
		public static NotifyStepAvail getInstance() {
			return arnie;
		}
		
		public void invoke(SequencerListener l) {
			l.stepAvail();
		}
	}

	private static class NotifyStep implements Notifier<SequencerListener> {
		private State q;
		private State q1;
		private Action a;
		
		public NotifyStep(State q, Action a, State q1) {
			this.q = q;
			this.q1 = q1;
			this.a = a;
		}
		
		public void invoke(SequencerListener l) {
			l.step(q, a, q1);
		}
	}

	private static class NotifyBack implements Notifier<SequencerListener> {
		private State q;
		private State q1;
		private Action a;
		
		public NotifyBack(State q, Action a, State q1) {
			this.q = q;
			this.q1 = q1;
			this.a = a;
		}
		
		public void invoke(SequencerListener l) {
			l.back(q, a, q1);
		}
	}
}
