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
import org.jalgo.module.am1simulator.model.am1.machine.MachineConfiguration;

/**
 * Abstract base class for all AM1 simulation statements.
 * 
 * @author Max Leuth&auml;user
 */
public abstract class SimulationStatement {
	private LineAddress address;

	public SimulationStatement(LineAddress address) {
		setAddress(address);
	}

	/**
	 * @return the number of lines this statement spans over.
	 */
	public int getLinesCount() {
		return 1;
	}

	/**
	 * Returns the {@link LineAddress} associated with this statement.
	 * 
	 * @return the {@link LineAddress}
	 */
	public LineAddress getAddress() {
		return address;
	}

	/**
	 * Sets the {@link LineAddress} associated with this statement.
	 * 
	 * @param address
	 *            new {@link LineAddress}
	 */
	public void setAddress(LineAddress address) {
		this.address = address;
	}

	/**
	 * @param configuration
	 *            the {@link MachineConfiguration}
	 * @return the updated {@link MachineConfiguration} after applying this
	 *         statement
	 */
	public abstract MachineConfiguration apply(
			MachineConfiguration configuration);

	/**
	 * @return the description for this statement in HTML format
	 */
	public abstract String getDescription();

	/**
	 * Returns the statement code text as a string
	 * 
	 * @return statement code text as String
	 */
	public abstract String getCodeText();

	/**
	 * Returns the string representation (code text) and a newline added.
	 * 
	 * @return string representation
	 */
	@Override
	public String toString() {
		return getCodeText() + "\n";
	}
}
