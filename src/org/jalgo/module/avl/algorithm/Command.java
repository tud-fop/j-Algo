package org.jalgo.module.avl.algorithm;
import java.util.*;

/**
 * @author Ulrike Fischer
 * Part of the design pattern "Command/Action/Transaction" (class Command of the pattern).
 * the abstract methods <code> perform</code> and <code> undo</code>
 * make possible to do/undo operations stepwise (implemented in the inherited classes).
 * the other methods are necessary to communicate between different command objects,
 * e.g. to set parameters and get the results of a command. 
 *  
 */

public abstract class Command {
	
//	protected String section;
	protected List results;
	protected List parameters;

/**
 * Constructs a <code>Command</code>. <br>
 * Initializes the result and the parameters list.
 *
 */
	public Command() {
		results = new LinkedList();
		parameters = new LinkedList();
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
 * Sets the parameters of the <code>Command</code>.
 * @param p <code>List</code> of parameters.
 */
	public void setParameters(List p) {
		parameters = new LinkedList(p);
	}

/**
 * Retrieves the whole result list of the <code>Command</code>
 * @return <code>List</code> list of results.
 */	
	public List getResults() {
		return results;
	}

/**
 * Retrieves the entry of the result list from the given index.
 * @param index position of the wanted result
 * @return <code>Object</code> the result
 */
	public Object getResult(int index){
		if (index<results.size())
			return results.get(index);
		return null;
	}
	
}
