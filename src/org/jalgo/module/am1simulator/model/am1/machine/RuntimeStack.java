/**
 * AM1 Simulator - simulating am1 code in an abstract machine based on the
 * definitions of the lectures 'Programmierung' at TU Dresden.
 * Copyright (C) 2010 Max Leuthäuser
 * Contact: s7060241@mail.zih.tu-dresden.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jalgo.module.am1simulator.model.am1.machine;

import java.util.List;
import java.util.LinkedList;

/**
 * Class which represents the runtime stack in an abstract machine.
 * 
 * @author Max Leuth&auml;user
 */
public class RuntimeStack {
	private List<RuntimeStackEntry> values;

	/**
	 * Create a new empty runtime stack.
	 */
	public RuntimeStack() {
		values = new LinkedList<RuntimeStackEntry>();
	}

	/**
	 * Copyconstructor
	 * 
	 * @param i
	 *            {@link RuntimeStack} which should be copied.
	 */
	public RuntimeStack(RuntimeStack i) {
		values = new LinkedList<RuntimeStackEntry>();

		for (RuntimeStackEntry c : i.getRuntimeStackAsList()) {
			values.add(new RuntimeStackEntry(c));
		}
	}

	/**
	 * Create a new runtime stack based on an already existing and filled list
	 * of values.
	 * 
	 * @param v
	 *            {@link List} which already holds some values.
	 */
	public RuntimeStack(final List<RuntimeStackEntry> v) {
		values = new LinkedList<RuntimeStackEntry>();

		for (RuntimeStackEntry c : v) {
			values.add(new RuntimeStackEntry(c));
		}
	}

	/**
	 * Store a new entry in the runtime stack. (STORE)
	 * 
	 * @param b
	 * @param o
	 * @param r
	 * @param z
	 * @return -1 of the operation fails.
	 */
	public int store(final String b, final int o, final int r, final int z) {
		return exchangeAtCount(adr(r, b, o), z);
	}

	/**
	 * Store a new entry in the runtime stack. (STOREI
	 * 
	 * @param o
	 * @param r
	 * @param z
	 * @return -1 of the operation fails.
	 */
	public int storei(final int o, final int r, final int z) {
		return exchangeAtCount(getAtCount(r + o), z);
	}

	/**
	 * Returns the value at this index in the runtime stack. (LOAD)
	 * 
	 * @param b
	 * @param o
	 * @param r
	 * @return the value for the given index, <b>-1</b> if there is no entry at
	 *         this index.
	 */
	public int load(final String b, final int o, final int r) {
		return getAtCount(adr(r, b, o));
	}

	/**
	 * Returns the value at this index in the runtime stack. (LOADI)
	 * 
	 * @param o
	 * @param r
	 * @return the value for the given index, <b>-1</b> if there is no entry at
	 *         this index.
	 */
	public int loadi(final int o, final int r) {
		return getAtCount(getAtCount(r + o));
	}

	/**
	 * Pushes a new value to the runtime stack. (PUSH)
	 * 
	 * @param z
	 */
	public void push(final int z) {
		if (values.isEmpty()) {
			RuntimeStackEntry r = new RuntimeStackEntry(z, false);
			values.add(r);
			return;
		}
		RuntimeStackEntry r = values.get(values.size() - 1);
		if (r.isClosed()) {
			RuntimeStackEntry n = new RuntimeStackEntry(z, false);
			values.add(n);
		} else {
			r.add(z);
		}
	}

	/**
	 * Call new values to the runtime stack. (CALL)
	 * 
	 * @param m
	 * @param r
	 */
	public void call(final int m, final int r) {
		if (values.isEmpty()) {
			LinkedList<Integer> a = new LinkedList<Integer>();
			a.add(m);
			a.add(r);
			RuntimeStackEntry n = new RuntimeStackEntry(a, true);
			values.add(n);
			return;
		}
		RuntimeStackEntry l = values.get(values.size() - 1);
		if (l.isClosed()) {
			LinkedList<Integer> a = new LinkedList<Integer>();
			a.add(m);
			a.add(r);
			RuntimeStackEntry n = new RuntimeStackEntry(a, true);
			values.add(n);
		} else {
			l.add(m);
			l.add(r);
			l.setClosed();
		}
	}

	/**
	 * Add 'count' times zero to the runtime stack (INIT)
	 * 
	 * @param count
	 */
	public void init(final int count) {
		if (count > 0) {
			if (values.isEmpty()) {
				LinkedList<Integer> a = new LinkedList<Integer>();
				for (int i = 0; i < count; i++) {
					a.add(0);
				}
				RuntimeStackEntry n = new RuntimeStackEntry(a, true);
				values.add(n);
				return;
			}
			RuntimeStackEntry l = values.get(values.size() - 1);
			if (l.isClosed()) {
				for (int i = 0; i < count; i++) {
					l.add(0);
				}
			} else {
				for (int i = 0; i < count; i++) {
					l.add(0);
				}
				l.setClosed();
			}
		}
	}

	/**
	 * Removes the last {@link RuntimeStackEntry}. (RET) Calling this will
	 * change the result e.g. for getLength().
	 */
	public void ret() {
		values.remove(values.size() - 1);
	}

	/**
	 * @return the length of the runtime stack
	 */
	public int getLength() {
		int result = 0;
		for (RuntimeStackEntry r : values) {
			result += r.size();
		}
		return result;
	}

	/**
	 * Returns the value at this count in the runtime stack.
	 * 
	 * @param count
	 * @return the value for the given count
	 * @throws IndexOutOfBoundsException
	 *             if there is no entry at this index
	 */
	public int getAtCount(int count) throws IndexOutOfBoundsException {
		if (count > getLength() || count == -1)
			throw new IndexOutOfBoundsException();
		else {
			LinkedList<Integer> t = new LinkedList<Integer>();
			for (RuntimeStackEntry r : values) {
				t.addAll(r);
			}
			return t.get(count - 1);
		}
	}

	/**
	 * Exchanges a value at this count in the runtime stack.
	 * 
	 * @param count
	 * @param value
	 * @return <b>-1</b> if there is no entry at this index, <b>0</b> otherwise.
	 */
	public int exchangeAtCount(int count, int value) {
		if (getAtCount(count) == -1)
			return -1;
		else {
			LinkedList<Integer> t = new LinkedList<Integer>();
			for (RuntimeStackEntry r : values) {
				t.addAll(r);
			}
			t.set(count - 1, value);
			int start = 0;
			int end = 0;
			for (RuntimeStackEntry r : values) {
				end = start + r.size() - 1;
				r.clear();
				r.addAll(t.subList(start, end + 1));
				start = end + 1;
			}
		}
		return 0;
	}

	/**
	 * @return the ram as list.
	 */
	public List<RuntimeStackEntry> getRuntimeStackAsList() {
		return values;
	}

	/**
	 * adr(r,b,o) = r + o if b='lokal', o otherwise.
	 * 
	 * @param r
	 * @param b
	 * @param o
	 * @return the result of the function 'adr' as defined by the script.
	 */
	public static int adr(int r, String b, int o) {
		if (b.equals("lokal")) {
			return r + o;
		}
		if (b.equals("global")) {
			return o;
		}
		throw new IllegalArgumentException("b must be 'lokal or 'global'!");
	}

	/**
	 * @return the runtime stack as String
	 */
	@Override
	public String toString() {
		if (values.isEmpty()) {
			return "Ɛ";
		}
		StringBuilder result = new StringBuilder();
		for (RuntimeStackEntry i : values) {
			result.append(i);
			result.append(":");
		}
		return result.toString().substring(0, result.toString().length() - 1);
	}

	/**
	 * Print a List containing {@link RuntimeStackEntry}s and format them like a
	 * real {@link RuntimeStack} using {@link RuntimeStack#toString()}.
	 * 
	 * @param in
	 * @return a String that looks like {@link RuntimeStack#toString()}.
	 */
	public static String printAListAsRuntimeStack(List<RuntimeStackEntry> in) {
		RuntimeStack r = new RuntimeStack(in);
		return r.toString();
	}

}
