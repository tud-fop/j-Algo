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
 * Statement factory for all jump statements.
 * 
 * @author Max Leuth&auml;user
 */
public class JumpStatementFactory implements AbstractStatementFactory {
	/**
	 * Updates the AM after calling JMC.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class Jmc extends SimulationStatement {
		private int target;

		/**
		 * @throws IllegalArgumentException
		 *             if target < 0
		 */
		public Jmc(LineAddress address, int target) {
			super(address);

			if (target < 0) {
				throw new IllegalArgumentException(
						"Jump target must not be < 0.");
			}
			this.target = target;
		}

		@Override
		public MachineConfiguration apply(MachineConfiguration configuration)
				throws IllegalArgumentException {
			if (configuration.getStack().getStackAsList().size() < 1) {
				throw new IllegalArgumentException(
						"Stack does not contain 1 or more values.");
			}
			if (configuration.getStack().pop() == 0) {
				configuration.getProgramCounter().set(target);
			} else {
				configuration.getProgramCounter().inc();
			}
			return configuration;
		}

		@Override
		public String getDescription() {
			return "if <i>d</i>=0:<i>d</i>.2: ... :<i>d</i>.n with n \u2265 1 then (<i>e</i>,<i>d</i>.2: ... :<i>d</i>.n,<i>h</i>, <i>inp</i>, <i>out</i>)<br />else (<i>m</i>+1,<i>d</i>.2: ... :<i>d</i>.n,<i>h</i>, <i>inp</i>, <i>out</i>)<br />If the first stack element is 0 (logic false), then the program counter will be set to <i>e</i>.<br />Otherwise the first stack element has an other value (logic true), then the program counter will be incremented.";
		}

		@Override
		public String getCodeText() {
			return "JMC " + target + ";";
		}
	}

	/**
	 * Updates the AM after calling JMP.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class Jmp extends SimulationStatement {
		private int target;

		/**
		 * @throws IllegalArgumentException
		 *             if target < 0
		 */
		public Jmp(LineAddress address, int target) {
			super(address);

			if (target < 0) {
				throw new IllegalArgumentException(
						"Jump target must not be < 0.");
			}
			this.target = target;
		}

		@Override
		public MachineConfiguration apply(MachineConfiguration configuration) {
			configuration.getProgramCounter().set(target);
			return configuration;
		}

		@Override
		public String getDescription() {
			return "(<i>e</i>,<i>d</i>,<i>h</i>, <i>inp</i>, <i>out</i>)";
		}

		@Override
		public String getCodeText() {
			return "JMP " + target + ";";
		}
	}

	@Override
	public SimulationStatement newStatement(Statement statement,
			StatementResource resource) {
		LineAddress address = resource.getAddress();
		int value = resource.getValue();
		switch (statement) {
		case JMC:
			return new Jmc(address, value);
		case JMP:
			return new Jmp(address, value);
		default:
			throw new AssertionError("Unknown statement type:" + statement);
		}
	}
}
