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
import org.jalgo.module.am1simulator.model.am1.machine.RuntimeStack;

/**
 * This class is a simple blueprint for all commands. Just a method to return
 * the type of the command is stipulated
 * 
 * @author Max Leuth&auml;user
 */
public class MemoryStatementFactory implements AbstractStatementFactory {
	/**
	 * Updates the AM after calling LOAD.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class Load extends SimulationStatement {
		private int target;
		private String location;

		public Load(LineAddress address, String location, int target) {
			super(address);
			this.target = target;
			this.location = location;
		}

		@Override
		public MachineConfiguration apply(MachineConfiguration configuration)
				throws IllegalArgumentException {
			int z;
			try {
				z = configuration.getRuntimeStack().load(location, target,
						configuration.getRef().get());
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentException("The target (" + location
						+ ", " + target + ") does not exist");
			}
			configuration.getProgramCounter().inc();
			configuration.getStack().push(z);
			return configuration;
		}

		@Override
		public String getDescription() {
			return "(<i>m</i> + 1, <i>z</i> : <i>d</i>, <i>h</i>, <i>r</i>, <i>inp</i>, <i>out</i>)"
					+ "<br />"
					+ "with <i>z</i> = <i>h</i>.(<i>adr</i>(<i>r</i>,<i>b</i>,<i>o</i>))"
					+ " and <br /><br />"
					+ "<table><tr><td valign='middle'><i>adr</i>(<i>r</i>,<i>b</i>,<i>o</i>) = </td><td><font size='6'>{</font></td><td><i>r</i> + <i>o</i><br /><i>o</i></td><td>if b=lokal;<br />if b=global</td></tr></table>";
		}

		@Override
		public String getCodeText() {
			return "LOAD(" + location.toLowerCase() + "," + target + ");";
		}
	}

	/**
	 * Updates the AM after calling LOADA.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class Loada extends SimulationStatement {
		private int target;
		private String location;

		public Loada(LineAddress address, String location, int target) {
			super(address);
			this.target = target;
			this.location = location;
		}

		@Override
		public MachineConfiguration apply(MachineConfiguration configuration)
				throws IllegalArgumentException {
			int z = RuntimeStack.adr(configuration.getRef().get(), location,
					target);
			configuration.getProgramCounter().inc();
			configuration.getStack().push(z);
			return configuration;
		}

		@Override
		public String getDescription() {
			return "(<i>m</i> + 1, <i>adr</i>(<i>r</i>,<i>b</i>,<i>o</i>) : <i>d</i>, <i>h</i>, <i>r</i>, <i>inp</i>, <i>out</i>)";
		}

		@Override
		public String getCodeText() {
			return "LOADA(" + location.toLowerCase() + "," + target + ");";
		}
	}

	/**
	 * Updates the AM after calling LOADI.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class Loadi extends SimulationStatement {
		private int target;

		public Loadi(LineAddress address, int target) {
			super(address);
			this.target = target;
		}

		@Override
		public MachineConfiguration apply(MachineConfiguration configuration)
				throws IllegalArgumentException {
			int z;
			try {
				z = configuration.getRuntimeStack().loadi(target,
						configuration.getRef().get());
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentException("The target (" + target
						+ ") does not exist");
			}
			configuration.getProgramCounter().inc();
			configuration.getStack().push(z);
			return configuration;
		}

		@Override
		public String getDescription() {
			return "(<i>m</i> + 1, <i>z</i> : <i>d</i>, <i>h</i>, <i>r</i>, <i>inp</i>, <i>out</i>)"
					+ "<br />"
					+ "with <i>z</i> = <i>h</i>.(<i>h</i>.(<i>r</i> + <i>o</i>))";
		}

		@Override
		public String getCodeText() {
			return "LOADI(" + target + ");";
		}
	}

	/**
	 * Updates the AM after calling STORE.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class Store extends SimulationStatement {
		private int target;
		private String location;

		public Store(LineAddress address, String location, int target) {
			super(address);
			this.target = target;
			this.location = location;
		}

		@Override
		public MachineConfiguration apply(MachineConfiguration configuration)
				throws IllegalArgumentException {
			int z;
			try {
				z = configuration.getStack().pop();
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentException("The stack is empty!");
			}

			if (configuration.getRuntimeStack().store(location, target,
					configuration.getRef().get(), z) == -1) {
				throw new IllegalArgumentException(
						"The value at this address does not exist!");
			}
			configuration.getProgramCounter().inc();
			return configuration;
		}

		@Override
		public String getDescription() {
			return "if <i>d</i> = <i>z</i> : <i>d'</i> then (<i>m</i> + 1, <i>d'</i>, <i>h'</i>, <i>r</i>, <i>inp</i>, <i>out</i>)"
					+ "<br />"
					+ "with <i>h'</i> = <i>h</i>[<i>adr</i>(<i>r</i>,<i>b</i>,<i>o</i>)/<i>z</i>]"
					+ " and <br /><br />"
					+ "<table><tr><td valign='middle'><i>adr</i>(<i>r</i>,<i>b</i>,<i>o</i>) = </td><td><font size='6'>{</font></td><td><i>r</i> + <i>o</i><br /><i>o</i></td><td>if b=lokal;<br />if b=global</td></tr></table>";
		}

		@Override
		public String getCodeText() {
			return "STORE(" + location + "," + target + ");";
		}
	}

	/**
	 * Updates the AM after calling STOREI.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class Storei extends SimulationStatement {
		private int target;

		public Storei(LineAddress address, int target) {
			super(address);
			this.target = target;
		}

		@Override
		public MachineConfiguration apply(MachineConfiguration configuration)
				throws IllegalArgumentException {
			int z;
			try {
				z = configuration.getStack().pop();
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentException("The stack is empty!");
			}

			if (configuration.getRuntimeStack().storei(target,
					configuration.getRef().get(), z) == -1) {
				throw new IllegalArgumentException(
						"The value at this address does not exist!");
			}
			configuration.getProgramCounter().inc();
			return configuration;
		}

		@Override
		public String getDescription() {
			return "if <i>d</i> = <i>z</i> : <i>d'</i> then (<i>m</i> + 1, <i>d'</i>, <i>h'</i>, <i>r</i>, <i>inp</i>, <i>out</i>)"
					+ "<br />"
					+ "with <i>h'</i> = <i>h</i>[<i>h</i>.(<i>r</i> + <i>o</i>)/<i>z</i>]";
		}

		@Override
		public String getCodeText() {
			return "STOREI(" + target + ");";
		}
	}

	@Override
	public SimulationStatement newStatement(Statement statement,
			StatementResource resource) {
		LineAddress address = resource.getAddress();
		String location = resource.getLocation();
		int value = resource.getValue();
		switch (statement) {
		case LOAD:
			return new Load(address, location, value);
		case LOADA:
			return new Loada(address, location, value);
		case LOADI:
			return new Loadi(address, value);
		case STORE:
			return new Store(address, location, value);
		case STOREI:
			return new Storei(address, value);
		default:
			throw new AssertionError("Unknown statement type:" + statement);
		}
	}
}
