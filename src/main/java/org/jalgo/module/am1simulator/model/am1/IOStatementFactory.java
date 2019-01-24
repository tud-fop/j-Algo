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
 * Statement factory for all I/O statements.
 * 
 * @author Max Leuth&auml;user
 */
public class IOStatementFactory implements AbstractStatementFactory {
	/**
	 * Updates the AM after calling READ.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class Read extends SimulationStatement {
		private int target;
		private String location;

		public Read(LineAddress address, String location, int target) {
			super(address);
			this.target = target;
			this.location = location;
		}

		@Override
		public MachineConfiguration apply(MachineConfiguration configuration)
				throws IllegalArgumentException {
			int z;
			try {
				z = configuration.getInputStream().read();
			} catch (NoSuchElementException e) {
				throw new IllegalArgumentException("The input stream is empty!");
			}
			if (configuration.getRuntimeStack().store(location, target,
					configuration.getRef().get(), z) == -1) {
				throw new IllegalArgumentException(
						"This value in the runtime stack does not exist!");
			}
			configuration.getProgramCounter().inc();
			return configuration;
		}

		@Override
		public String getDescription() {
			return "if <i>inp</i> = <i>z</i> : <i>inp'</i>, <i>z</i> \u220A \u2124 then (<i>m</i> + 1, <i>d</i>, <i>h'</i>, <i>r</i>, <i>inp'</i>, <i>out</i>)"
					+ "<br />"
					+ "with <i>h'</i> = <i>h</i>[<i>adr</i>(<i>r</i>,<i>b</i>,<i>o</i>)/<i>z</i>]"
					+ " and <br /><br />"
					+ "<table><tr><td valign='middle'><i>adr</i>(<i>r</i>,<i>b</i>,<i>o</i>) = </td><td><font size='6'>{</font></td><td><i>r</i> + <i>o</i><br /><i>o</i></td><td>if b=lokal;<br />if b=global</td></tr></table>";
		}

		@Override
		public String getCodeText() {
			return "READ(" + location + "," + target + ");";
		}
	}

	/**
	 * Updates the AM after calling READI.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class Readi extends SimulationStatement {
		private int target;

		/**
		 * @throws IllegalArgumentException
		 *             if target < 0
		 */
		public Readi(LineAddress address, int target) {
			super(address);
			this.target = target;
		}

		@Override
		public MachineConfiguration apply(MachineConfiguration configuration)
				throws IllegalArgumentException {
			int z;
			try {
				z = configuration.getInputStream().read();
			} catch (NoSuchElementException e) {
				throw new IllegalArgumentException("The input stream is empty!");
			}
			if (configuration.getRuntimeStack().storei(target,
					configuration.getRef().get(), z) == -1) {
				throw new IllegalArgumentException(
						"The value in the runtime stack does not exist!");
			}
			configuration.getProgramCounter().inc();
			return configuration;
		}

		@Override
		public String getDescription() {
			return "if <i>inp</i> = <i>z</i> : <i>inp'</i>, <i>z</i> \u220A \u2124 then (<i>m</i> + 1, <i>d</i>, <i>h'</i>, <i>r</i>, <i>inp'</i>, <i>out</i>)"
					+ "<br />"
					+ "with <i>h'</i> = <i>h</i>[<i>h</i>.(<i>r</i> + <i>o</i>)/<i>z</i>]";
		}

		@Override
		public String getCodeText() {
			return "READI(" + target + ");";
		}
	}

	/**
	 * Updates the AM after calling WRITE.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class Write extends SimulationStatement {
		private int target;
		private String location;

		public Write(LineAddress address, String location, int target) {
			super(address);
			this.target = target;
			this.location = location;
		}

		@Override
		public MachineConfiguration apply(MachineConfiguration configuration)
				throws IllegalArgumentException {

			int z = configuration.getRuntimeStack().load(location, target,
					configuration.getRef().get());

			if (z == -1) {
				throw new IllegalArgumentException("The target (" + location
						+ ", " + target + ") does not exist");
			} else {
				configuration.getProgramCounter().inc();
				configuration.getOutputStream().write(z);
			}
			return configuration;
		}

		@Override
		public String getDescription() {
			return "(<i>m</i> + 1, <i>d</i>, <i>h</i>, <i>r</i>, <i>inp</i>, <i>out</i> : <i>z</i>)"
					+ "<br />"
					+ "with <i>z</i> = <i>h</i>.(<i>adr</i>(<i>r</i>,<i>b</i>,<i>o</i>))"
					+ " and <br /><br />"
					+ "<table><tr><td valign='middle'><i>adr</i>(<i>r</i>,<i>b</i>,<i>o</i>) = </td><td><font size='6'>{</font></td><td><i>r</i> + <i>o</i><br /><i>o</i></td><td>if b=lokal;<br />if b=global</td></tr></table>";
		}

		@Override
		public String getCodeText() {
			return "WRITE(" + location + "," + target + ");";
		}
	}

	/**
	 * Updates the AM after calling WRITEI.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class Writei extends SimulationStatement {
		private int target;

		public Writei(LineAddress address, int target) {
			super(address);
			this.target = target;
		}

		@Override
		public MachineConfiguration apply(MachineConfiguration configuration)
				throws IllegalArgumentException {
			int z = configuration.getRuntimeStack().loadi(target,
					configuration.getRef().get());
			if (z == -1) {
				throw new IllegalArgumentException("The target (" + target
						+ ") does not exist");
			} else {
				configuration.getProgramCounter().inc();
				configuration.getOutputStream().write(z);
			}
			return configuration;
		}

		@Override
		public String getDescription() {
			return "(<i>m</i> + 1, <i>d</i>, <i>h</i>, <i>r</i>, <i>inp</i>, <i>out</i> : <i>z</i>)"
					+ "<br />"
					+ "with <i>z</i> = <i>h</i>.(<i>h</i>.(<i>r</i> + <i>o</i>))";
		}

		@Override
		public String getCodeText() {
			return "WRITEI(" + target + ");";
		}
	}

	@Override
	public SimulationStatement newStatement(Statement statement,
			StatementResource resource) {
		LineAddress address = resource.getAddress();
		String location = resource.getLocation();
		int value = resource.getValue();
		switch (statement) {
		case READ:
			return new Read(address, location, value);
		case READI:
			return new Readi(address, value);
		case WRITE:
			return new Write(address, location, value);
		case WRITEI:
			return new Writei(address, value);
		default:
			throw new AssertionError("Unknown statement type:" + statement);
		}
	}
}
