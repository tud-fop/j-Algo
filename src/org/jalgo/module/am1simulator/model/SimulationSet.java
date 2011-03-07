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

package org.jalgo.module.am1simulator.model;

import org.jalgo.module.am1simulator.view.components.AM1InputDialog;
import org.jalgo.module.am1simulator.model.am1.machine.MachineConfiguration;

/**
 * Represents an data set for a new simulation. This set is created by
 * {@link AM1InputDialog}.
 * 
 * @author Max Leuth&auml;user
 */
public class SimulationSet {
	private int steps;
	private MachineConfiguration m;

	public SimulationSet(MachineConfiguration m, int steps) {
		this.m = m;
		this.steps = steps;
	}

	/**
	 * @return the {@link MachineConfiguration} which is stored here.
	 */
	public MachineConfiguration getMachineConfiguration() {
		return m;
	}

	/**
	 * @return the maximum number of steps the user wants to simulate at once.
	 */
	public int getSteps() {
		return steps;
	}

}
