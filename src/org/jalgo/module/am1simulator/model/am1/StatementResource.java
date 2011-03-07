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

package org.jalgo.module.am1simulator.model.am1;

import org.jalgo.module.am1simulator.model.LineAddress;

/**
 * Class which holds the necessary and optional values for all kinds of
 * {@link SimulationStatement}. Implementation is characterized through the
 * builder pattern. Null arguments are <b>not</b> allowed and causes an
 * {@link IllegalArgumentException}.
 * 
 * @author Max Leuth&auml;user
 */
public class StatementResource {
	private final LineAddress address;
	private final String location;
	private final int value;

	public static class Builder {
		// always required:
		private LineAddress address;
		// optional - initialized to default values:
		private String location = "";
		private int value = 0;

		public Builder(LineAddress address) {
			if (address == null) {
				throw new IllegalArgumentException(
						"Null arguments are not allowed!");
			}
			this.address = address;
		}

		public Builder location(String loc) {
			if (loc == null) {
				throw new IllegalArgumentException(
						"Null arguments are not allowed!");
			}
			location = loc;
			return this;
		}

		public Builder value(int val) {
			value = val;
			return this;
		}

		public StatementResource build() {
			return new StatementResource(this);
		}
	}

	private StatementResource(Builder builder) {
		address = builder.address;
		location = builder.location;
		value = builder.value;
	}

	public LineAddress getAddress() {
		return address;
	}

	public String getLocation() {
		return location;
	}

	public int getValue() {
		return value;
	}
}
