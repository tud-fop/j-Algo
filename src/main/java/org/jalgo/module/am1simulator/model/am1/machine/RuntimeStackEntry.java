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

import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a runtime stack entry which is used in the following
 * abstract machine element:<li>{@link RuntimeStack}</li>
 * 
 * @author Max Leuth&auml;user
 */
public class RuntimeStackEntry extends LinkedList<Integer> {
	private static final long serialVersionUID = 1L;
	private boolean isClosed = false;

	/**
	 * Create a new {@link RuntimeStackEntry} with an already filled List of
	 * Integer and a flag (isClosed) which specifies if this stack entry is
	 * closed or not.
	 * 
	 * @param in
	 * @param isClosed
	 */
	public RuntimeStackEntry(List<Integer> in, boolean isClosed) {
		this.isClosed = isClosed;
		for (int i : in) {
			add(i);
		}
	}

	/**
	 * Create a new {@link RuntimeStackEntry} with only the first value (in) and
	 * a flag (isClosed) which specifies if this stack entry is closed or not.
	 * 
	 * @param in
	 * @param isClosed
	 */
	public RuntimeStackEntry(int in, boolean isClosed) {
		this.isClosed = isClosed;
		add(in);
	}

	/**
	 * Copyconstructor
	 * 
	 * @param in
	 */
	public RuntimeStackEntry(RuntimeStackEntry in) {
		this.isClosed = in.isClosed();
		for (int i : in) {
			add(i);
		}
	}

	/**
	 * @return true if this {@link RuntimeStackEntry} is closed, false
	 *         otherwise.
	 */
	public boolean isClosed() {
		return isClosed;
	}

	/**
	 * Close this {@link RuntimeStackEntry} finally. There is no way back.
	 */
	public void setClosed() {
		isClosed = true;
	}

	/**
	 * Return a String representation for this {@link RuntimeStackEntry}.<br />
	 * <br />
	 * <ul>
	 * This uses the following rules:
	 * <p>
	 * <li><i>The {@link RuntimeStackEntry} is empty:</i> return an empty String
	 * </li>
	 * <li><i>The {@link RuntimeStackEntry} contains one or more values:</i>
	 * return this values separated with ":", leading with an open parentheses.</li>
	 * <li>The String ends with a closed parentheses if the
	 * {@link RuntimeStackEntry} is closed.</li>
	 * </p>
	 * </ul>
	 * <br />
	 * 
	 * @return a String formatted as written above.
	 */
	@Override
	public String toString() {
		if (isEmpty()) {
			return "";
		} else {
			StringBuilder b = new StringBuilder();
			b.append("(");
			for (int i : this) {
				b.append(i);
				b.append(":");
			}
			String result = b.toString()
					.substring(0, b.toString().length() - 1);
			if (isClosed) {
				result += ")";
			}
			return result;
		}
	}

	@Override
	public int hashCode() {
		throw new AssertionError(
				"This class is not ment to be used in an hash data structure!");
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof RuntimeStackEntry) {
			return ((RuntimeStackEntry) o).toString().equals(toString());
		}
		return false;
	}
}
