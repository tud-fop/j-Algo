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

import java.util.NoSuchElementException;

import org.jalgo.module.am1simulator.model.LineAddress;
import org.jalgo.module.am1simulator.model.am1.machine.MachineConfiguration;

/**
 * Statement factory for all procedural statements.
 * 
 * @author Max Leuth&auml;user
 */
public class ProceduralStatementFactory implements AbstractStatementFactory {
	/**
	 * Updates the AM after calling CALL.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class Call extends SimulationStatement {
		private int target;

		public Call(LineAddress address, int target) {
			super(address);
			this.target = target;
		}

		@Override
		public MachineConfiguration apply(MachineConfiguration configuration)
				throws IllegalArgumentException {

			int pc = configuration.getProgramCounter().get();
			int ref = configuration.getRef().get();

			configuration.getProgramCounter().set(target);
			configuration.getRef().set(
					configuration.getRuntimeStack().getLength() + 2);

			configuration.getRuntimeStack().call(pc + 1, ref);
			return configuration;
		}

		@Override
		public String getDescription() {
			return "(<i>adr</i>, <i>d</i>, <i>h</i> : (<i>m</i> + 1) : <i>r</i>, <i>length</i>(<i>h</i>) + 2, <i>inp</i>, <i>out</i>)";
		}

		@Override
		public String getCodeText() {
			return "CALL " + target + ";";
		}
	}

	/**
	 * Updates the AM after calling INIT.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class Init extends SimulationStatement {
		private int amount;

		public Init(LineAddress address, int amount) {
			super(address);
			this.amount = amount;
		}

		@Override
		public MachineConfiguration apply(MachineConfiguration configuration)
				throws IllegalArgumentException {

			configuration.getRuntimeStack().init(amount);
			configuration.getProgramCounter().inc();
			return configuration;
		}

		@Override
		public String getDescription() {
			return "(<i>m</i> + 1, <i>d</i>, <i>h</i> : 0{n}, <i>r</i>, <i>inp</i>, <i>out</i>)";
		}

		@Override
		public String getCodeText() {
			return "INIT " + amount + ";";
		}
	}

	/**
	 * Updates the AM after calling PUSH.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class Push extends SimulationStatement {
		public Push(LineAddress address) {
			super(address);
		}

		@Override
		public MachineConfiguration apply(MachineConfiguration configuration)
				throws IllegalArgumentException {
			int z;
			try {
				z = configuration.getStack().pop();
			} catch (NoSuchElementException e) {
				throw new IllegalArgumentException("The stack is empty!");
			}
			configuration.getRuntimeStack().push(z);
			configuration.getProgramCounter().inc();
			return configuration;
		}

		@Override
		public String getDescription() {
			return "if <i>d</i> = <i>z</i> : <i>d'</i> then (<i>m</i> + 1, <i>d'</i>, <i>h</i> : <i>z</i>, <i>r</i>, <i>inp</i>, <i>out</i>)";
		}

		@Override
		public String getCodeText() {
			return "PUSH;";
		}
	}

	/**
	 * Updates the AM after calling RET.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class Ret extends SimulationStatement {
		private int target;

		public Ret(LineAddress address, int target) {
			super(address);
			this.target = target;
		}

		@Override
		public MachineConfiguration apply(MachineConfiguration configuration)
				throws IllegalArgumentException {
			final int oldref = configuration.getRef().get();
			final int oldPc = configuration.getProgramCounter().get();
			configuration.getRef().set(
					configuration.getRuntimeStack().getAtCount(oldref));
			configuration.getProgramCounter().set(
					configuration.getRuntimeStack().getAtCount(oldref - 1));

			try {
				configuration.getRuntimeStack().ret();
			} catch (IndexOutOfBoundsException e) {
				configuration.getRef().set(oldref);
				configuration.getProgramCounter().set(oldPc);
				throw new IllegalArgumentException(
						"The runtime stack is empty!");
			}
			return configuration;
		}

		@Override
		public String getDescription() {
			return "(<i>h</i>.(<i>r</i> - 1),<i>d</i>,<i>h</i>.1 : ... : <i>h</i>.(<i>r</i> - 2 - <i>n</i>),<i>h</i>.<i>r</i>,<i>inp</i>,<i>out</i>)";
		}

		@Override
		public String getCodeText() {
			return "RET " + target + ";";
		}
	}

	/**
	 * Updates the AM after calling LIT.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class Lit extends SimulationStatement {
		private int target;

		public Lit(LineAddress address, int target) {
			super(address);
			this.target = target;
		}

		@Override
		public MachineConfiguration apply(MachineConfiguration configuration) {
			configuration.getProgramCounter().inc();
			configuration.getStack().push(target);
			return configuration;
		}

		@Override
		public String getDescription() {
			return "(<i>m</i>+1,<i>z</i>:<i>d</i>,<i>h</i>, <i>inp</i>, <i>out</i>)";
		}

		@Override
		public String getCodeText() {
			return "LIT " + target + ";";
		}
	}

	@Override
	public SimulationStatement newStatement(Statement statement,
			StatementResource resource) {
		LineAddress address = resource.getAddress();
		int value = resource.getValue();
		switch (statement) {
		case CALL:
			return new Call(address, value);
		case INIT:
			return new Init(address, value);
		case PUSH:
			return new Push(address);
		case RET:
			return new Ret(address, value);
		case LIT:
			return new Lit(address, value);
		default:
			throw new AssertionError("Unknown statement type:" + statement);
		}
	}
}
