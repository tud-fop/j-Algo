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
 * Statement factory for all compare statements.
 * 
 * @author Max Leuth&auml;user
 */
public class CompareStatementFactory implements AbstractStatementFactory {
	/**
	 * Updates the AM after calling EQ.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class Equal extends SimulationStatement {
		public Equal(LineAddress address) {
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
			if (arg1 == arg0) {
				configuration.getStack().push(1);
			} else
				configuration.getStack().push(0);
			return configuration;
		}

		@Override
		public String getDescription() {
			return "if <i>d</i>=<i>d</i>.1:<i>d</i>.2:<i>d</i>.3: ... :<i>d</i>.n with n \u2265 2 then (<i>m</i>+1,b:<i>d</i>.3: ... :<i>d</i>.n,<i>h</i>, <i>inp</i>, <i>out</i>)<br />and <i>b</i>=1, if <i>d</i>.2=<i>d</i>.1, and <i>b</i>=0, if <i>d</i>.2\\!=<i>d</i>.1,<br />that means for the value true (or false respectively) is 1 (or 0 respectively) pushed to the stack.";
		}

		@Override
		public String getCodeText() {
			return "EQ;";
		}
	}

	/**
	 * Updates the AM after calling GE.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class GreaterEqual extends SimulationStatement {
		public GreaterEqual(LineAddress address) {
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
			if (arg1 >= arg0) {
				configuration.getStack().push(1);
			} else
				configuration.getStack().push(0);
			return configuration;
		}

		@Override
		public String getDescription() {
			return "if <i>d</i>=<i>d</i>.1:<i>d</i>.2:<i>d</i>.3: ... :<i>d</i>.n with n \u2265 2 then (<i>m</i>+1,b:<i>d</i>.3: ... :<i>d</i>.n,<i>h</i>, <i>inp</i>, <i>out</i>)<br />and <i>b</i>=1, if <i>d</i>.2\u2265<i>d</i>.1, and <i>b</i>=0, if <i>d</i>.2<<i>d</i>.1,<br />that means for the value true (or false respectively) is 1 (or 0 respectively) pushed to the stack.";
		}

		@Override
		public String getCodeText() {
			return "GE;";
		}
	}

	/**
	 * Updates the AM after calling GT.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class GreaterThen extends SimulationStatement {
		public GreaterThen(LineAddress address) {
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
			if (arg1 > arg0) {
				configuration.getStack().push(1);
			} else
				configuration.getStack().push(0);
			return configuration;
		}

		@Override
		public String getDescription() {
			return "if <i>d</i>=<i>d</i>.1:<i>d</i>.2:<i>d</i>.3: ... :<i>d</i>.n with n \u2265 2 then (<i>m</i>+1,b:<i>d</i>.3: ... :<i>d</i>.n,<i>h</i>, <i>inp</i>, <i>out</i>)<br />and <i>b</i>=1, if <i>d</i>.2><i>d</i>.1, and <i>b</i>=0, if <i>d</i>.2\u2264<i>d</i>.1,<br />that means for the value true (or false respectively) is 1 (or 0 respectively) pushed to the stack.";
		}

		@Override
		public String getCodeText() {
			return "GT;";
		}
	}

	/**
	 * Updates the AM after the calling LE.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class LesserEqual extends SimulationStatement {
		public LesserEqual(LineAddress address) {
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
			if (arg1 <= arg0) {
				configuration.getStack().push(1);
			} else
				configuration.getStack().push(0);
			return configuration;
		}

		@Override
		public String getDescription() {
			return "if <i>d</i>=<i>d</i>.1:<i>d</i>.2:<i>d</i>.3: ... :<i>d</i>.n with n \u2265 2 then (<i>m</i>+1,b:<i>d</i>.3: ... :<i>d</i>.n,<i>h</i>, <i>inp</i>, <i>out</i>)<br />and <i>b</i>=1, if <i>d</i>.2\u2264<i>d</i>.1, and <i>b</i>=0, if <i>d</i>.2><i>d</i>.1,<br />that means for the value true (or false respectively) is 1 (or 0 respectively) pushed to the stack.";
		}

		@Override
		public String getCodeText() {
			return "LE;";
		}
	}

	/**
	 * Updates the AM after calling LT.
	 * 
	 * @author Max Leuth&auml;user
	 */
	public class LesserThen extends SimulationStatement {
		public LesserThen(LineAddress address) {
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
			if (arg1 < arg0) {
				configuration.getStack().push(1);
			} else
				configuration.getStack().push(0);
			return configuration;
		}

		@Override
		public String getDescription() {
			return "if <i>d</i>=<i>d</i>.1:<i>d</i>.2:<i>d</i>.3: ... :<i>d</i>.n with n \u2265 2 then (<i>m</i>+1,b:<i>d</i>.3: ... :<i>d</i>.n,<i>h</i>, <i>inp</i>, <i>out</i>)<br />and <i>b</i>=1, if <i>d</i>.2<<i>d</i>.1, and <i>b</i>=0, if <i>d</i>.2\u2265<i>d</i>.1,<br />that means for the value true (or false respectively) is 1 (or 0 respectively) pushed to the stack.";
		}

		@Override
		public String getCodeText() {
			return "LT;";
		}
	}

	/**
	 * Updates the AM after calling NE.
	 * 
	 * @author Max Leuth&auml;user
	 */
	public class NotEqual extends SimulationStatement {
		public NotEqual(LineAddress address) {
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
			if (arg1 != arg0) {
				configuration.getStack().push(1);
			} else
				configuration.getStack().push(0);
			return configuration;
		}

		@Override
		public String getDescription() {
			return "if <i>d</i>=<i>d</i>.1:<i>d</i>.2:<i>d</i>.3: ... :<i>d</i>.n with n \u2265 2 then (<i>m</i>+1,b:<i>d</i>.3: ... :<i>d</i>.n,<i>h</i>, <i>inp</i>, <i>out</i>)<br />and <i>b</i>=1, if <i>d</i>.2\\!=<i>d</i>.1, and <i>b</i>=0, if <i>d</i>.2=<i>d</i>.1,<br />that means for the value true (or false respectively) is 1 (or 0 respectively) pushed to the stack.";
		}

		@Override
		public String getCodeText() {
			return "NE;";
		}
	}

	@Override
	public SimulationStatement newStatement(Statement statement,
			StatementResource resource) {
		LineAddress address = resource.getAddress();
		switch (statement) {
		case EQUAL:
			return new Equal(address);
		case GREATERTHEN:
			return new GreaterThen(address);
		case GREATEREQUAL:
			return new GreaterEqual(address);
		case LESSEREQUAL:
			return new LesserEqual(address);
		case LESSERTHEN:
			return new LesserThen(address);
		case NOTEQUAL:
			return new NotEqual(address);
		default:
			throw new AssertionError("Unknown statement type:" + statement);
		}
	}
}
