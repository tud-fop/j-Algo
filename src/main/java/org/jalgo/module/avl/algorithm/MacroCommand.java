/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package org.jalgo.module.avl.algorithm;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Ulrike Fischer, Jean Christoph Jung
 * 
 * Part of the design pattern "Command/Action/Transaction" (class MacroCommand
 * of the pattern). contains a list of commands that are collected to one unit,
 * so a macrocommand object can do more complex operations by performing many
 * atomic commands.
 */
public abstract class MacroCommand
extends Command {

	protected String name;
	protected List<Command> commands;

	/** "iterator" on the commands List. */
	protected int currentPosition;

	/**
	 * Constructs a <code>MacroCommand</code>. <br>
	 * Initializes the command list, the currentPosition of the command
	 * iteration. Initializes the name of the <code>MacroCommand</code> with
	 * an default name.
	 */
	public MacroCommand() {
		commands = new LinkedList<Command>();
		currentPosition = 0;
		name = "noname"; //$NON-NLS-1$
	}

	/**
	 * @return <code>true</code>, if there is a command in the command list,
	 *         that is not yet performed, <code>false</code> otherwise
	 */
	public boolean hasNext() {
		if (commands.isEmpty() || (currentPosition >= commands.size()))
			return false;
		return true;
	}

	/**
	 * @return <code>true</code>, if there is command in the command list,
	 *         that can be undone, <code>false</code> otherwise
	 */
	public boolean hasPrevious() {
		if (commands.size() > currentPosition) {
			Command c = commands.get(currentPosition);
			if ((currentPosition == 0) && (c instanceof MacroCommand))
				return ((MacroCommand)c).hasPrevious();
			return (currentPosition > 0);
		}
		return (currentPosition > 0);
	}

	/**
	 * This methode need to be specifed by the concrete command.
	 */
	public abstract void perform();

	/**
	 * This methode need to be specifed by the concrete command.
	 */
	public abstract void undo();

	/**
	 * calls a few times <code>perform()</code>, so that logical units (block
	 * steps) are done in one step. The logical units are defined by the
	 * concrete macrocommand.
	 */
	public void performBlockStep() {
		if (!hasNext()) return;
		Command c = commands.get(currentPosition);
		if (c instanceof MacroCommand) ((MacroCommand)c).performBlockStep();
		else {
			do {
				perform();
				if (!hasNext()) return;
				c = commands.get(currentPosition);
				if (c instanceof MacroCommand) return;
			}
			while (true);
		}
	}

	/**
	 * counterpart to <code>performBlockStep</code> undoes several calls of
	 * perform, jumps back always to the start of a logical unit. The logical
	 * units are defined by the concrete macrocommand.
	 */
	public void undoBlockStep() {
		//TODO: what should this be?
		if (!hasPrevious()) return;
	}

	/**
	 * @return the name of the macrocommand
	 */
	public String getName() {
		return name;
	}

	/**
	 * cancels the algorithm. recovers the state before calling the first time
	 * perform.
	 */
	public void abort() {
		while (hasPrevious()) undo();
	}

	/**
	 * completes the execution of the algorithm. performs all commands in the
	 * commandlist
	 */
	public void finish() {
		while (hasNext()) perform();
	}
}