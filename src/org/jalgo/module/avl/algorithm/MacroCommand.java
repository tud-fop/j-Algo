package org.jalgo.module.avl.algorithm;
import java.util.*;

/**
 * @author Ulrike Fischer, Jean Christoph Jung
 * 
 * Part of the design pattern "Command/Action/Transaction" (class MacroCommand of the pattern).
 * contains a list of commands that are collected to one unit, so a macrocommand object can do 
 * more complex operations by performing many atomic commands.
 *
 */
public abstract class MacroCommand extends Command {

	protected String name;
	protected List<Command> commands;
	
	/**
	 * "iterator" on the commands List. 
	 */
	protected int currentPosition;

/**
 * Constructs a <code>MacroCommand</code>. <br>
 * Initializes the command list, the currentPosition of the command iteration.
 * Initializes the name of the <code>MacroCommand</code> with an default name.
 */
	public MacroCommand() {
		commands = new LinkedList<Command>();
		currentPosition = 0;
		name = "noname";
	}
	
	/**
	 * @return true: if there is a command in the command list, that is not yet performed
	 * false: else
	 */
	
	public boolean hasNext() {
		if (commands.isEmpty() || (currentPosition>=commands.size()))
			return false;
		else
			return true;
	}
	
	
/**
 * @return: true, if there is command in the command list, that can be undone.
 * false: else
 */
	public boolean hasPrevious() {
		if (commands.size()>currentPosition) {
			Command c = commands.get(currentPosition);
			if ((currentPosition==0) && (c instanceof MacroCommand)) {
				return ((MacroCommand)c).hasPrevious();
			}
			else
				return (currentPosition>0) ;
		}
		else return (currentPosition>0);
	}

	
/**
 * This methode need to be specifed by the concrete command.
 *
 */
	public abstract void perform();

/**
 * This methode need to be specifed by the concrete command.
 *
 */
	public abstract void undo();

/**
 *  calls a few times <code>perform()</code>, so that logical units (block steps) 
 *  are done in one step. The logical units are defined by the concrete macrocommand.
 */
	public void performBlockStep() {
		if (!hasNext()) return;
		Command c = commands.get(currentPosition);
		if (c instanceof MacroCommand) {
			((MacroCommand)c).performBlockStep();
		}
		else {
			do {
				perform();
				if (!hasNext()) return;
				c = commands.get(currentPosition);
				if (c instanceof MacroCommand) return;
			} while (true);
		}
	}

	/**
	 * counterpart to <code>performBlockStep</code>  
	 * undoes several calls of perform, jumps back always to the start of a logical unit.
	 *  The logical units are defined by the concrete macrocommand.
	 */
	public void undoBlockStep() {
		if (!hasPrevious()) return;
	}
	
	/**
	 * @return: the name of the macrocommand
	 */
	public String getName(){
		return name;
	}
	
	
	/**
	 * cancels the algorithm. recovers the state before calling the first time perform.
	 */
	public void abort() {
		while (hasPrevious()) undo();
	}
	
	/**
	 * completes the execution of the algorithm. performs all commands in the commandlist
	 */
	public void finish() {
		while (hasNext()) perform();
	}
}
