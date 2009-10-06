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
package org.jalgo.module.heapsort.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;

import org.jalgo.module.heapsort.Subject;

/**
 * <p>This class models the Heapsort algorithm.</p>
 * 
 * <p>The states are:</p>
 * <ul>
 *    <li>InitialState</li>
 *    <li>Phase0: Building the tree</li>
 *    <li>Phase1a: Before Sinkenlassen</li>
 *    <li>Phase1b: After Sinkenlassen</li>
 *    <li>Phase2a: Before swapping</li>
 *    <li>Phase2b: After swapping/before Sinkenlassen</li>
 *    <li>Phase12sla: Sinkenlassen before comparison</li>
 *    <li>Phase12slb: Sinkenlassen after comparison/before swapping</li>
 *    <li>Done</li>
 *  </ul>
 *  
 *  <p>The actions are:</p>
 *  <ul>
 *    <li>Startphase0: used in InitialState when Phase0 is launched</li>
 *    <li>Startphase1: used in Phase0 when Phase1a is launched</li>
 *    <li>Startphase2: used in Phase0 when Phase2s is launched (happens if sequence has length 1)</li>
 *    <li>Startsl: used in Phase1a and Phase2b when Phase12sla is launched</li>
 *    <li>Returnphase1: used in Phase12sl* when returning to Phase1b</li>
 *    <li>Returnphase2: used in Phase12sl* when returning to Phase2a</li>
 *    <li>Extend: used in Phase0 when level is increased</li>
 *    <li>Compare: used in Phase12sla when going to Phase12slb</li>
 *    <li>Swap: used in Phase12slb when going to Phase12sla (swapping nodes)</li>
 *    <li>Decli: used in Phase1b when going to Phase1a (decrementing pointer "Li")</li>
 *    <li>SwapDecre: used in Phase2a when going to Phase2b</li>
 *    <li>Finish: used in Phase12sl* and others when going to Done</li>
 * </ul>
 * 
 * @author mbue
 *
 */
public class Heapsort extends Subject<ModelListener> implements Model {
	
	private List<Integer> sequence;
	
	public Heapsort() {
		sequence = new ArrayList<Integer>();
	}
	
	public void addNumber(int n) {
		// this might be slow, but speed is not relevant here
		for (Integer i: sequence)
			if (i.intValue() == n)
				throw new IllegalArgumentException();
		sequence.add(Integer.valueOf(n));
		notifyAll(ModelChangedNotifier.getInstance());
	}
	
	public State getInitialState() {
		return new InitialState(sequence);
	}

	@SuppressWarnings("unchecked")
	public void deserialize(InputStream is) throws IOException {
		ObjectInputStream ois = new ObjectInputStream(is);
		try {
			sequence = (List<Integer>)ois.readObject();
		}
		catch (ClassNotFoundException e) {
			; // XXX fail silently
			// a) shouldn't happen anyway
			// b) no big deal
		}
		notifyAll(ModelChangedNotifier.getInstance());
	}

	public void serialize(OutputStream os) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(sequence);
		oos.flush();
		oos = null;
		oos.close();
	}
	
	public static void main(String[] args) {
		int[] testdata = {7,15,14,8,13,18,24,9,5,16,21};
		//int[] testdata = {1, 2, 3, 4, 5};
		
		Heapsort m = new Heapsort();
		for (int i: testdata)
			m.addNumber(i);
		
		State s = m.getInitialState();
		while (s != null) {
			Map<State, Action> succ = s.computeSuccessors();
			Iterator<Entry<State,Action>> i = succ.entrySet().iterator();
			System.out.println(s.toString());
			if (i.hasNext()) {
				Entry<State,Action> e = i.next();
				s = e.getKey();
				System.out.println(e.getValue());
			}
			else
				s = null;
		}
	}
	
	// auxiliary methods
	
	/**
	 * Computes the number of levels of the binary tree
	 * corresponding to some list of length <code>n</code>
	 */
	public static int getLevels(int n) {
		int l = 0;
		while (n > 0) {
			n >>= 1;
			l++;
		}
		return l;  
	}
	
	// actions (the sick way of doing enum)
	public static final class Actions {
		// Initial->Phase0
		private static final Action startphase0 = new Action() {
			public String toString() {
				return "Start Phase 0";
			}
		};
		// Phase0->Phase0
		private static final Action extend = new Action() {
			public String toString() {
				return "Extend";
			}
		};
		// Phase0->Phase1a
		private static final Action startphase1 = new Action() {
			public String toString() {
				return "Start Phase 1";
			}
		};
		// Phase0->Phase2a, Phase1a->Phase2a
		private static final Action startphase2 = new Action() {
			public String toString() {
				return "Start Phase 2";
			}
		};
		// Phase1a->Phase12sla, Phase2b->Phase12sla
		private static final Action startsl = new Action() {
			public String toString() {
				return "Start Sinkenlassen";
			}
		};
		// Phase12sla->Phase1b, Phase12slb->Phase1b
		private static final Action returnphase1 = new Action() {
			public String toString() {
				return "Return to Phase 1";
			}
		};
		// Phase12sla->Phase2a, Phase12slb->Phase2a
		private static final Action returnphase2 = new Action() {
			public String toString() {
				return "Return to Phase 2";
			}
		};
		// Phase1b->Phase1a
		private static final Action decli = new Action() {
			public String toString() {
				return "Decrement li";
			}
		};
		// Phase2a->Phase2b
		private static final Action swap_decre = new Action() {
			public String toString() {
				return "Swap a[0], a[re] and decrement re";
			}
		};
		// Phase12slb->Phase12sla
		private static final Action swap = new Action() {
			public String toString() {
				return "Swap a[i], a[j]";
			}
		};
		// Phase12sla->Phase12slb
		private static final Action compare = new Action() {
			public String toString() {
				return "Compare";
			}
		};
		// Phase12sla->Done, Phase12slb->Done
		private static final Action finish = new Action() {
			public String toString() {
				return "Finish";
			}
		};
		
		public static Action getExtend() {
			return extend;
		}
		
		public static Action getStartphase0() {
			return startphase0;
		}
		
		public static Action getStartphase1() {
			return startphase1;
		}
		
		public static Action getStartphase2() {
			return startphase2;
		}
		
		public static Action getStartsl() {
			return startsl;
		}
		
		public static Action getReturnphase1() {
			return returnphase1;
		}
		
		public static Action getReturnphase2() {
			return returnphase2;
		}
		
		public static Action getDecli() {
			return decli;
		}
		
		public static Action getSwapDecre() {
			return swap_decre;
		}
		
		public static Action getCompare() {
			return compare;
		}
		
		public static Action getSwap() {
			return swap;
		}
		
		public static Action getFinish() {
			return finish;
		}
		
		public static boolean isExtend(Action a) {
			return a == extend;
		}
		
		public static boolean isStartphase0(Action a) {
			return a == startphase0;
		}		
		
		public static boolean isStartphase1(Action a) {
			return a == startphase1;
		}
		
		public static boolean isStartphase2(Action a) {
			return a == startphase2;
		}

		public static boolean isStartsl(Action a) {
			return a == startsl;
		}

		public static boolean isReturnphase1(Action a) {
			return a == returnphase1;
		}
		
		public static boolean isReturnphase2(Action a) {
			return a == returnphase2;
		}
		
		public static boolean isDecli(Action a) {
			return a == decli;
		}
		
		public static boolean isSwapDecre(Action a) {
			return a == swap_decre;
		}
		
		public static boolean isCompare(Action a) {
			return a == compare;
		}
		
		public static boolean isSwap(Action a) {
			return a == swap;
		}
		
		public static boolean isFinish(Action a) {
			return a == finish;
		}
	}
	
	// states
	public static class HeapsortState {
		public final List<Integer> sequence;
		
		protected HeapsortState(List<Integer> sequence) {
			this.sequence = sequence;
		}
	}
	
	public static class InitialState extends HeapsortState implements State {
		@SuppressWarnings("unchecked")
		public InitialState() {
			this(Collections.EMPTY_LIST);
		}
		
		public InitialState(List<Integer> sequence) {
			super(sequence);
		}

		public Map<State, Action> computeSuccessors() {
			Map<State,Action> result = new HashMap<State,Action>();
			if (sequence.size() > 1)
				result.put(new Phase0(sequence), Actions.getStartphase0());
			else
				result.put(new Done(sequence), Actions.getFinish());
			return result;
		}

		public int getDetailLevel() {
			return 0;
		}
	}
	
	// phase 0: artificially inserted
	public static class Phase0 extends HeapsortState implements State {
		public final int level;
		
		public Phase0(List<Integer> sequence) {
			this(sequence, 0);
		}
		
		public Phase0(List<Integer> sequence, int level) {
			super(sequence);
			this.level = level;
		}
		
		public int getDetailLevel() {
			return 1;
		}
		
		public Map<State,Action> computeSuccessors() {
			Map<State,Action> result = new HashMap<State,Action>();
			if (level < getLevels(sequence.size()))
				result.put(new Phase0(sequence, level+1), Actions.getExtend());
			else {
				if (sequence.size() > 1)
					result.put(new Phase1a(sequence), Actions.getStartphase1());
				else
					result.put(new Phase2a(sequence, 0, sequence.size()-1), Actions.getStartphase2());
			}
			return result;
		}
		
		public String toString() {
			return String.format("Phase 0: level %d, sequence %s", level, sequence.toString());
		}
	}
	
	public static final class Done extends HeapsortState implements State {
		
		public Done(List<Integer> sequence) {
			super(sequence);
		}
		
		public int getDetailLevel() {
			return 0;
		}
		
		public Map<State,Action> computeSuccessors() {
			return new HashMap<State,Action>();
		}
		
		public String toString() {
			return String.format("Done: sequence %s", new Object[] {sequence.toString()});
		}		
	}
	
	public static class Phase12 extends HeapsortState {
		public final int li;
		public final int re;
		
		protected Phase12(List<Integer> sequence, int li, int re) {
			super(sequence);
			this.li = li;
			this.re = re;
		}
		
		public String toString() {
			return String.format("%s: li %d, re %d, sequence %s", getClass()
					.getName().substring(
							"org.jalgo.module.heapsort.model.Heapsort$"
									.length()), li, re, sequence.toString());
		}
	}
	
	// --gophase1-> phase1 --startsl-> phase12sl --compare-> phase12sla (--swap-> phase12sl) --returnphase1-> phase1a --decli-> phase1
	// macro:         x                                        (x)                                                                x
	
	public static class Phase1 extends Phase12 {
		protected Phase1(List<Integer> sequence, int li, int re) {
			super(sequence, li, re);
		}
	}
	
	public static final class Phase1a extends Phase1 implements State {
		
		public Phase1a(List<Integer> sequence) {
			super(sequence, sequence.size() / 2 - 1, sequence.size()-1);
		}
		
		public Phase1a(List<Integer> sequence, int li, int re) {
			super(sequence, li, re);
		}
		
		public int getDetailLevel() {
			return 0;
		}
		
		public Map<State,Action> computeSuccessors() {
			Map<State,Action> result = new HashMap<State,Action>();
			result.put(new Phase12sla(sequence, li, re), Actions.getStartsl());
			return result;
		}
		
	}
	
	public static final class Phase1b extends Phase1 implements State {

		public Phase1b(List<Integer> sequence, int li, int re) {
			super(sequence, li, re);
		}
		
		public int getDetailLevel() {
			return 2;
		}
		
		public Map<State,Action> computeSuccessors() {
			Map<State,Action> result = new HashMap<State,Action>();
			result.put(new Phase1a(sequence, li-1, re), Actions.getDecli());
			return result;
		}

	}
	
	// --gophase2-> phase2 --swap_decre-> phase2a --startsl-> ... --returnphase2-> phase2
	// macro:          x                     x
	
	public static class Phase2 extends Phase12 {
		protected Phase2(List<Integer> sequence, int li, int re) {
			super(sequence, li, re);
		}
	}
	
	public static final class Phase2a extends Phase2 implements State {
		
		public Phase2a(List<Integer> sequence, int li, int re) {
			super(sequence, li, re);
		}
		
		public int getDetailLevel() {
			return 0;
		}
		
		public Map<State,Action> computeSuccessors() {
			Map<State,Action> result = new HashMap<State,Action>();
			ArrayList<Integer> sequenceprime = new ArrayList<Integer>(sequence);
			int a_0 = sequenceprime.get(0);
			int a_re = sequenceprime.get(re);
			sequenceprime.set(0, a_re);
			sequenceprime.set(re, a_0);
			result.put(new Phase2b(sequenceprime, li, re-1), Actions.getSwapDecre());
			return result;
		}
		
	}
	
	public static final class Phase2b extends Phase2 implements State {

		public Phase2b(List<Integer> sequence, int li, int re) {
			super(sequence, li, re);
		}
		
		public int getDetailLevel() {
			return 0;
		}
		
		public Map<State,Action> computeSuccessors() {
			Map<State,Action> result = new HashMap<State,Action>();
			result.put(new Phase12sla(sequence, li, re), Actions.getStartsl());
			return result;
		}

	}
	
	public static class Phase12sl extends Phase12 {
		public final int i;
		
		protected Phase12sl(List<Integer> sequence, int li, int re, int i) {
			super(sequence, li, re);
			this.i = i;
		}
		
		protected void addReturnState(Map<State,Action> m) {
			if (li > 0)
				m.put(new Phase1b(sequence, li, re), Actions.getReturnphase1());
			else {
				if (re > 0) {
					m.put(new Phase2a(sequence, li, re), Actions.getReturnphase2());
				}
				else
					m.put(new Done(sequence), Actions.getFinish());
			}		
		}
	}
	
	public static final class Phase12sla extends Phase12sl implements State {
		
		public Phase12sla(List<Integer> sequence, int li, int re) {
			this(sequence, li, re, li);
		}
		
		public Phase12sla(List<Integer> sequence, int li, int re, int i) {
			super(sequence, li, re, i);
		}
		
		public int getDetailLevel() {
			return 2;
		}
		
		public Map<State,Action> computeSuccessors() {
			Map<State,Action> result = new HashMap<State,Action>();
			if (2*i+1 > re)
			    addReturnState(result);
			else
				result.put(new Phase12slb(sequence, li, re, i), Actions.getCompare());
			return result;
		}
		
	}
	
	public static final class Phase12slb extends Phase12sl implements State {
		
		public Phase12slb(List<Integer> sequence, int li, int re, int i) {
			super(sequence, li, re, i);
		}
		
		public int getDetailLevel() {
			return 1;
		}
		
		public Map<State,Action> computeSuccessors() {
			Map<State,Action> result = new HashMap<State,Action>();
			int j = 2*i+1;
			if ((j+1 <= re) && (sequence.get(j)<sequence.get(j+1)))
				j++;
			if (sequence.get(i) < sequence.get(j)) {
				ArrayList<Integer> sequenceprime = new ArrayList<Integer>(sequence);
				int a_i = sequenceprime.get(i);
				int a_j = sequenceprime.get(j);
				sequenceprime.set(i, a_j);
				sequenceprime.set(j, a_i);
				result.put(new Phase12sla(sequenceprime, li, re, j), Actions.getSwap());
			}
			else
				addReturnState(result);
			return result;
		}
		
	}

	public static class ModelChangedNotifier implements Notifier<ModelListener> {
		private static final ModelChangedNotifier arnie = new ModelChangedNotifier();

		public static Notifier<ModelListener> getInstance() {
			return arnie;
		}

		public void invoke(ModelListener l) {
			l.modelChanged();
		}
	}
}