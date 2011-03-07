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
import org.jalgo.module.am1simulator.model.am1.StatementResource;
import org.jalgo.module.am1simulator.model.am1.machine.MachineConfiguration;

/**
 * Statement factory for all arithmetic statements.
 * 
 * @author Max Leuth&auml;user
 */
public class ArithmeticStatementFactory implements AbstractStatementFactory {
	/**
	 * Updates the AM after calling ADD.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class Add extends SimulationStatement {
		public Add(LineAddress address) {
			super(address);
		}

		@Override
		public MachineConfiguration apply(MachineConfiguration configuration)
				throws IllegalArgumentException {
			if (configuration.getStack().getStackAsList().size() < 2) {
				throw new IllegalArgumentException(
						"Stack does not contain 2 or more values.");
			}
			configuration.getProgramCounter().inc();
			int arg0 = configuration.getStack().pop();
			int arg1 = configuration.getStack().pop();
			// this way round because this is the way its
			// written in the script
			configuration.getStack().push(arg1 + arg0);
			return configuration;
		}

		@Override
		public String getDescription() {
			return "if <i>d</i>=<i>d</i>.1:<i>d</i>.2:<i>d</i>.3: ... :<i>d</i>.n with n \u2265 2 then (<i>m</i>+1,(<i>d</i>.2+<i>d</i>.1):<i>d</i>.3: ... :<i>d</i>.n,<i>h</i>, <i>inp</i>, <i>out</i>)";
		}

		@Override
		public String getCodeText() {
			return "ADD;";
		}
	}

	/**
	 * Updates the AM after calling DIV.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class Div extends SimulationStatement {
		public Div(LineAddress address) {
			super(address);
		}

		@Override
		public MachineConfiguration apply(MachineConfiguration configuration)
				throws IllegalArgumentException, ArithmeticException {
			if (configuration.getStack().getStackAsList().size() < 2) {
				throw new IllegalArgumentException(
						"Stack does not contain 2 or more values.");
			}
			configuration.getProgramCounter().inc();
			int arg0 = configuration.getStack().pop();
			int arg1 = configuration.getStack().pop();
			// this way round because this is the way its
			// written in the script

			if (arg0 == 0) {
				throw new ArithmeticException("Division by zero.");
			}

			configuration.getStack().push(arg1 / arg0);
			return configuration;
		}

		@Override
		public String getDescription() {
			return "if <i>d</i>=<i>d</i>.1:<i>d</i>.2:<i>d</i>.3: ... :<i>d</i>.n with n \u2265 2 then (<i>m</i>+1,(<i>d</i>.2/<i>d</i>.1):<i>d</i>.3: ... :<i>d</i>.n,<i>h</i>, <i>inp</i>, <i>out</i>)";
		}

		@Override
		public String getCodeText() {
			return "DIV;";
		}
	}

	/**
	 * Updates the AM after calling MOD.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class Mod extends SimulationStatement {
		public Mod(LineAddress address) {
			super(address);
		}

		@Override
		public MachineConfiguration apply(MachineConfiguration configuration)
				throws IllegalArgumentException {
			if (configuration.getStack().getStackAsList().size() < 2) {
				throw new IllegalArgumentException(
						"Stack does not contain 2 or more values.");
			}
			configuration.getProgramCounter().inc();
			int arg0 = configuration.getStack().pop();
			int arg1 = configuration.getStack().pop();
			// this way round because this is the way its
			// written in the script

			configuration.getStack().push(arg1 % arg0);
			return configuration;
		}

		@Override
		public String getDescription() {
			return "if <i>d</i>=<i>d</i>.1:<i>d</i>.2:<i>d</i>.3: ... :<i>d</i>.n with n \u2265 2 then (<i>m</i>+1,(<i>d</i>.2%<i></i>.1):<i>d</i>.3: ... :<i>d</i>.n,<i>h</i>, <i>inp</i>, <i>out</i>)";
		}

		@Override
		public String getCodeText() {
			return "MOD;";
		}
	}

	/**
	 * Updates the AM after calling MUL.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class Mul extends SimulationStatement {
		public Mul(LineAddress address) {
			super(address);
		}

		@Override
		public MachineConfiguration apply(MachineConfiguration configuration)
				throws IllegalArgumentException {
			if (configuration.getStack().getStackAsList().size() < 2) {
				throw new IllegalArgumentException(
						"Stack does not contain 2 or more values.");
			}
			configuration.getProgramCounter().inc();
			int arg0 = configuration.getStack().pop();
			int arg1 = configuration.getStack().pop();
			// this way round because this is the way its
			// written in the script

			configuration.getStack().push(arg1 * arg0);
			return configuration;
		}

		@Override
		public String getDescription() {
			return "if <i>d</i>=<i>d</i>.1:<i>d</i>.2:<i>d</i>.3: ... :<i>d</i>.n with n \u2265 2 then (<i>m</i>+1,(<i>d</i>.2*<i>d</i>.1):<i>d</i>.3: ... :<i>d</i>.n,<i>h</i>, <i>inp</i>, <i>out</i>)";
		}

		@Override
		public String getCodeText() {
			return "MUL;";
		}
	}

	/**
	 * Updates the AM after calling SUB.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class Sub extends SimulationStatement {
		public Sub(LineAddress address) {
			super(address);
		}

		@Override
		public MachineConfiguration apply(MachineConfiguration configuration)
				throws IllegalArgumentException {
			if (configuration.getStack().getStackAsList().size() < 2) {
				throw new IllegalArgumentException(
						"Stack does not contain 2 or more values.");
			}
			configuration.getProgramCounter().inc();
			int arg0 = configuration.getStack().pop();
			int arg1 = configuration.getStack().pop();
			// this way round because this is the way its
			// written in the script
			configuration.getStack().push(arg1 - arg0);
			return configuration;
		}

		@Override
		public String getDescription() {
			return "if <i>d</i>=<i>d</i>.1:<i>d</i>.2:<i>d</i>.3: ... :<i>d</i>.n with n \u2265 2 then (<i>m</i>+1,(<i>d</i>.2-<i>d</i>.1):<i>d</i>.3: ... :<i>d</i>.n,<i>h</i>, <i>inp</i>, <i>out</i>)";
		}

		@Override
		public String getCodeText() {
			return "SUB;";
		}
	}

	@Override
	public SimulationStatement newStatement(Statement statement,
			StatementResource resource) {
		LineAddress address = resource.getAddress();
		switch (statement) {
		case ADD:
			return new Add(address);
		case DIV:
			return new Div(address);
		case MOD:
			return new Mod(address);
		case MUL:
			return new Mul(address);
		case SUB:
			return new Sub(address);
		default:
			throw new AssertionError("Unknown statement type:" + statement);
		}
	}
}
