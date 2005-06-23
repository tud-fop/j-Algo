package org.jalgo.module.avl.algorithm;

import org.jalgo.module.avl.datastructure.*;
import org.jalgo.module.avl.Constants;
import java.util.*;

/**
 * Realizes the insertion of a key into a given searchtree by holding
 * the searchtree property
 * @author Jean Christoph Jung
 */

public class Insert extends MacroCommand implements Constants {
	private WorkNode wn;
	private SearchTree tree;
	
	
	/**
	 * @param w: Reference to the the current Node; the key of the worknode is the key that 
	 * will be inserted into the serchtree st.
	 * @param st: the tree, the algorithm is working on.
	 */
	public Insert(WorkNode w, SearchTree st){
		super();
		name = "Einfügen";
		wn = w;
		tree = st;
		if (wn.getNextToMe()!=null) {    // Tree not empty
			results.add(0, new String("Einfügen von " + wn.getKey()+ " gestartet"));
			results.add(1, "insert1");
			commands.add(CommandFactory.createSearch(w));
		}
		else {                           // Tree empty, search not necessary
			results.add(0, new String("Einfügen von " + wn.getKey() + " gestartet"));
			results.add(1, "insert2");
			commands.add(CommandFactory.createCreateNode(wn,tree));
		}
		results.add(2,WORKING);
	}
	
	/**
	 * <code> perform </code> does one step in the insertion of a new node.
	 * writes into the resultlist: DONE, if Insert is finished, WORKING if not finished,
	 * FOUND if key already exists
	 */
	public void perform() {
		Command c = commands.get(currentPosition);
		c.setParameters(parameters);
		c.perform();
		results.clear();
		List r = c.getResults();
		if (c instanceof Search) {
			int searchresult = (Integer) r.get(2);
			switch (searchresult) {
				case FOUND:
					currentPosition++;
					results.add(0, r.get(0) + "\nSchlüssel muss nicht eingefügt werden");
					results.add(1, "insert1");
					results.add(2, FOUND);
					break;	
				case WORKING:
					results.add(0, r.get(0));
					results.add(1, "insert1");
					results.add(2, WORKING);
					break;
				case NOTFOUND:
					Integer child = (Integer) r.get(3);
					if (child==LEFT) {
						results.add(0,r.get(0) + " --> links einfügen");
						results.add(1, "insert2");
						wn.setVisualizationStatus(Visualizable.NORMAL | Visualizable.LEFT_ARROW);
					}
					else {
						results.add(0,r.get(0) + " --> rechts einfügen");
						results.add(1,"insert2");
						wn.setVisualizationStatus(Visualizable.NORMAL | Visualizable.RIGHT_ARROW);
					}
					currentPosition++;
					if (commands.size()<2)
						commands.add(1, CommandFactory.createCreateNode(wn,tree));
					parameters.add(0, child);
					results.add(2, WORKING);
					break;
				default: results.add(2,WORKING); break;
			}
		}
		if (c instanceof CreateNode) {
			Command ch = CommandFactory.createCalcHeight(tree.getRoot());
			ch.perform();
			
			results.add(0,"Einfügen beendet");
			results.add(1,"insert2");
			results.add(2, DONE);
			currentPosition++;
			wn.setVisualizationStatus(Visualizable.INVISIBLE);
		}
	}

	
	/**
	 *  First blockstep: the search
	 *  Second blockstep: insert of the new node
	 */
	
	public void performBlockStep() {
		if (!hasNext()) return;
		Command c = commands.get(currentPosition);
		results.clear();
		results.add("Absatznummer");
		if (c instanceof Search)
			while (((Search)c).hasNext()) {
				perform();
			}
		else perform();
	}
		
	
	/**
	 * recovers the state before the last call of <code> perform </code>
	 */
	public void undo() {
		results.clear();
		results.add(0,"Schritt rückgängig gemacht");
		if (commands.size()<=currentPosition)
			currentPosition--;
		else if (currentPosition==1) currentPosition--;
		
		Command c = commands.get(currentPosition);

		if (c instanceof Search) {
			results.add(1, "insert1");
			if (((Search)c).hasPrevious()) {
				c.undo();
				results.add(2, DONE);
			}
			else {
				results.add(2, DONE);
			}
		}
		if (c instanceof CreateNode) {
			c.undo();
			results.add(1,"insert2");
			results.add(2,DONE);
		}
	}
	
	/**
	 *  First blockstep: the search
	 *  Second blockstep: insert of the new node
	 */
	public void undoBlockStep() {
		if ((commands.size()<=currentPosition) || (currentPosition==1)) 
			currentPosition--;
		
		Command c = commands.get(currentPosition);
		if (c instanceof Search)
			while (((Search)c).hasPrevious()) {
				undo();
			}
		else
			undo();
	}
}
