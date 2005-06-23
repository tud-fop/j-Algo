package org.jalgo.module.avl.algorithm;
import org.jalgo.module.avl.datastructure.*;
import org.jalgo.module.avl.*;

/**
 * @author Ulrike Fischer
 * 
 * The class <code>Search</codes> searches a key in the tree. 
 */

public class Search extends MacroCommand implements Constants{
	
	private WorkNode wn;
	private boolean firstundo=false;
	
	/**
	 * @param wn reference to the position in the tree, holds the search key
	 */
	
	public Search(WorkNode wn) {
		super();
		name = "Suchen";
		this.wn = wn;
		
		// checks if the tree is empty
		if (wn.getNextToMe()==null) {
			results.add(0,"Baum ist leer, Schlüssel nicht gefunden");
			results.add(1, "search1");
			results.add(2,NOTFOUND);
			wn.setVisualizationStatus(Visualizable.INVISIBLE);
		}
		else {
			commands.add(CommandFactory.createCompareKey(wn));
			results.add(0,wn.getKey()+ " suchen");
			results.add(1,"search1");
			results.add(2,WORKING);
			wn.getNextToMe().setVisualizationStatus(Visualizable.FOCUSED);
		}
	}
	
	/**
	 * Searches the key of the WorkNode in the tree, returns FOUND if the key was found, 
	 * WORKING if the algorithm isn't yet finished or NOTFOUND if the key was not found.
	 * In the last case Search also returns the position to insert (LEFT or RIGHT).
	 */
	
	public void perform() {
		results.clear();
		Command c = (Command) commands.get(currentPosition);
		c.perform();
		currentPosition++;
		
		if (c instanceof NoOperation) {
			results.add(0,"Schlüssel gefunden");
			results.add(1,"search1");
			results.add(2,FOUND);
			wn.getNextToMe().setVisualizationStatus(Visualizable.NORMAL);
			return;
		}
		
		Integer compareresult = (Integer) c.getResults().get(0);
		
		switch (compareresult) {
		
			case 0: {
				results.add(0,wn.getKey() + " = " + wn.getNextToMe().getKey());
				results.add(1,"search1");
				results.add(2,WORKING);
				wn.setVisualizationStatus(Visualizable.INVISIBLE);
				setNodesTo(wn.getNextToMe(),Visualizable.NORMAL);
				wn.getNextToMe().setVisualizationStatus(Visualizable.FOCUSED | Visualizable.LINE_NORMAL);
				if (currentPosition==commands.size())
					commands.add(CommandFactory.createNoOperation());	
				firstundo=true;
				break;
				// key found
			}
		
			case -1: {
				if (wn.getNextToMe().getLeftChild()==null) {
					results.add(0,wn.getKey() + " < " + wn.getNextToMe().getKey() + "\nnicht gefunden");
					results.add(1,"search1");
					results.add(2,NOTFOUND);
					results.add(3,LEFT);
					wn.setVisualizationStatus(Visualizable.INVISIBLE);
					setNodesTo(wn.getNextToMe(),Visualizable.NORMAL);
					firstundo=true;
					// end of Search, key not found
				}
				else {
					results.add(0,wn.getKey() + " < " + wn.getNextToMe().getKey() + " --> nach links gehen");
					results.add(1,"search1");
					results.add(2,WORKING);
					wn.setNextToMe(wn.getNextToMe().getLeftChild());
					wn.getNextToMe().setVisualizationStatus(Visualizable.FOCUSED);
					if (currentPosition==commands.size())
						commands.add(CommandFactory.createCompareKey(wn));	
				}
				break;
			}
		
			case 1: {
				if (wn.getNextToMe().getRightChild()==null) {
					results.add(0,wn.getKey() + " > " + wn.getNextToMe().getKey() + "\nnicht gefunden");
					results.add(1,"search1");
					results.add(2,NOTFOUND);
					results.add(3,RIGHT);
					wn.setVisualizationStatus(Visualizable.INVISIBLE);
					setNodesTo(wn.getNextToMe(),Visualizable.NORMAL);
					firstundo=true;
					//end of Search, key not found
				}
				else {
					results.add(0,wn.getKey() + " > " + wn.getNextToMe().getKey() + " --> nach rechts gehen");
					results.add(1,"search1");
					results.add(2,WORKING);
					wn.setNextToMe(wn.getNextToMe().getRightChild());
					wn.getNextToMe().setVisualizationStatus(Visualizable.FOCUSED);
					if (currentPosition==commands.size())
						commands.add(CommandFactory.createCompareKey(wn));	
				}
				break;
			}
		}	
	}
	
	
	/**
	 * Realizes undo by changing the position of the WorkNode
	 */
	
	public void undo() {			
		results.clear();
		results.add("Schritt rückgängig gemacht");
		results.add("search1");
		
		currentPosition--;
		Command c = (Command) commands.get(currentPosition);
		
		if (c instanceof NoOperation) {
			wn.getNextToMe().setVisualizationStatus(Visualizable.FOCUSED | Visualizable.LINE_NORMAL);
		}
		
		else if (firstundo) {
			wn.setVisualizationStatus(Visualizable.NORMAL);
			setNodesTo(wn.getNextToMe(),Visualizable.FOCUSED);
			firstundo=false;
		}
		
		else {
			wn.getNextToMe().setVisualizationStatus(Visualizable.NORMAL);
			wn.setNextToMe(wn.getNextToMe().getParent());
		}		
	}
	
	/**
	 * Performs all single steps.
	 */
	
	public void performBlockStep() {
		while (this.hasNext()) {
			perform();
		}
		
	}
	
	/**
	 * Realizes undo for all performs.
	 */
	
	public void undoBlockStep() {
		while (this.hasPrevious()) {
			undo();
		}
	}
	
	private void setNodesTo(Node n,int status) {
		while (n!=null) {
			n.setVisualizationStatus(status);
			n = n.getParent();
		}
	}
	
}
