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

/**
 * Base class for all statement factories. Provides all available
 * statements as enumeration.
 * 
 * @author Max Leuth&auml;user
 */
public interface AbstractStatementFactory {
	/**
	 * Enumeration that holds all available statement types.
	 * 
	 * @author Max Leuth&auml;user
	 */
	public static enum Statement {
		ADD, CALL, DIV, EQUAL, GREATEREQUAL, GREATERTHEN, INIT, JMC, JMP, LESSEREQUAL, LESSERTHEN, LIT, LOAD, LOADA, LOADI, MOD, MUL, NOTEQUAL, PUSH, READ, READI, REF, RET, STORE, STOREI, SUB, WRITE, WRITEI
	}

	/**
	 * Build a new {@link SimulationStatement} based on the value
	 * {@link Statement} and the {@link StatementResource} resource.
	 * 
	 * @param statement
	 * @param resource
	 * @return a new {@link SimulationStatement}
	 */
	SimulationStatement newStatement(Statement statement,
			StatementResource resource);
}
