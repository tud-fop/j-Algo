/*
 * Created on 22.04.2005
 */
package org.jalgo.module.avl.algorithm;
import java.util.Random;
import java.util.*;
import org.jalgo.module.avl.*;
import org.jalgo.module.avl.datastructure.*;

/**
 * @author Matthias Schmidt
 *
 * The class <code>CreateRandomTree</code> creates a random binary tree with a given
 * number of notes. There is the posibility to create an avl tree as well.
 */
public class CreateRandomTree extends MacroCommand implements Constants{

	int finalNodeNumber;
	Random rand;
	List<Integer> keyList;
	private SearchTree tree;
	private WorkNode wn;
	private boolean avl;

/**
* Constructs the <code>CreateRandomTree</code> instance for the current tree generation. <br>
* Initializes the name of the working algorithm which is dependent on the avl mode.
* Initializes the command list with the first <code>Insert or InsertAVL</code> object. 
*
* @param nodes number of nodes the <code>SearchTree</code> will have
* @param st <code>SearchTree</code> which ought to be builded.
* @param wn <code>WorkNode</code> which is always use by the <code>MacroCommand</code>s
*/
	public CreateRandomTree(int nodes, SearchTree st, WorkNode wn, boolean avl){
		super();
		if (avl) name = "AVL-Baum Erstellen";
		else 	 name = "Suchbaum Erstellen";
		tree = st;
		this.wn = wn;
		this.avl = avl;
		if(nodes>0){
			
			rand = new Random();
			finalNodeNumber = nodes;
			keyList = new ArrayList<Integer>();

			int firstKey = rand.nextInt(MAX_KEY)+MIN_KEY;
			
			changeWorkNode(firstKey);
			if (avl)	commands.add(0,CommandFactory.createInsertAVL(wn,tree));
			else		commands.add(0,CommandFactory.createInsert(wn,tree));
			commands.add(1,CommandFactory.createNoOperation());
			keyList.add(0,firstKey);

			results.add(0,getName()+" mit "+finalNodeNumber+" Knoten");
			results.add(1,"1");
			results.add(2,WORKING);
		}
		else{
			results.add(0,"zufälliger Suchbaum ist fertig");
			results.add(1," ");
			results.add(2,DONE);
			
		}
	}
//	 Changes the work node.	
	private void changeWorkNode(int key){
		wn.setKey(key);
		wn.setNextToMe(tree.getRoot());
		wn.setVisualizationStatus(Visualizable.NORMAL);
	}
	
//	 Stores the log description and the section from a command.	
	private void storeLogAndSectionFrom(Command c){
		results.clear();
		results.add(c.getResult(0));
		results.add(c.getResult(1));
		results.add(c.getResult(2));
	}
	
//	 Offer the necessary commands for the next insertation.	
	private void calculateNextInsertation(){
		currentPosition++;
		results.clear();	
			// test: Are there some commands in the commandlist which has not been performed?
			if (currentPosition>=commands.size()){

				// test: Is it necessary to add one more node?
				// each node needs one Insert and one NoOperation object
				if (currentPosition/2<finalNodeNumber){			
						int newkey=0;
						while(keyList.contains(newkey = rand.nextInt(MAX_KEY)+MIN_KEY)) {}
					
						changeWorkNode(newkey);
						if (avl) 	commands.add(CommandFactory.createInsertAVL(wn,tree));
						else 		commands.add(CommandFactory.createInsert(wn,tree));
						commands.add(CommandFactory.createNoOperation());
						keyList.add(newkey);
					
						storeLogAndSectionFrom(commands.get(currentPosition));
						//results.set(0,"NoOp");
					
						return;
				}
				results.add(0,"zufälliger Suchbaum ist fertig");
				results.add(1," ");
				results.add(2,DONE);
				
				wn.setNextToMe(tree.getRoot());
			}
			else {	
				
				results.add(0,"");
				results.add(1,"1");
				results.add(2,WORKING);
				
				wn.setNextToMe(tree.getRoot());
				wn.setKey(keyList.get(currentPosition/2));
				wn.setVisualizationStatus(Visualizable.NORMAL);
				tree.getRoot().setVisualizationStatus(Visualizable.FOCUSED);
			}

	}

/**
* Calculates one step. <br> 	
* The result list gets WORKING at the index 2 if the final number of <code>Node</code>s is not reached, 
* DONE if the <code>SearchTree</code> is completed
*/
	public void perform() {
		
/*		Command com = commands.get(currentPosition);

		if (!(com instanceof NoOperation)) {			
			if (avl) {
				InsertAVL ins = (InsertAVL) com;
				if (ins.hasNext()) {
					ins.perform();
					storeLogAndSectionFrom(com);
					if (!ins.hasNext())
						currentPosition++;
					return;
				}
				currentPosition++;
			}
			else {
				Insert ins = (Insert) com;
				if (ins.hasNext()) {
					ins.perform();
					storeLogAndSectionFrom(com);
					if (!ins.hasNext())
						currentPosition++;
					return;
				}
				currentPosition++;
			}		
		}
		
		Command noOp = commands.get(currentPosition);
		noOp.perform();	
		calculateNextInsertation();
		*/
		Command c = commands.get(currentPosition);
		if (c instanceof NoOperation) {
			if (keyList.size()>=finalNodeNumber) {currentPosition++; return; }
			int newkey=0;
			while(keyList.contains(newkey = rand.nextInt(MAX_KEY)+MIN_KEY)) {}
			keyList.add(newkey);
			changeWorkNode(newkey);
			if (avl)
				commands.set(0, CommandFactory.createInsertAVL(wn, tree));
			else
				commands.set(0, CommandFactory.createInsert(wn, tree));
			currentPosition = 0;
		}
		else {
			MacroCommand m = (MacroCommand)c;
			if (m.hasNext()) {
				m.perform();
				storeLogAndSectionFrom(m);
				if (!m.hasNext())
					currentPosition++;
			}
		}
}

/**
* Calculates one block step. <br>
* The result list gets WORKING at the index 2 if the final number of <code>Node</code>s is not reached, 
* DONE if the <code>SearchTree</code> is completed
*/
	public void performBlockStep() {
		Command c = commands.get(currentPosition);
		if (c instanceof NoOperation) {
			if (keyList.size()>=finalNodeNumber) {currentPosition++; return; }
			int newkey=0;
			while(keyList.contains(newkey = rand.nextInt(MAX_KEY)+MIN_KEY)) {}
			keyList.add(newkey);
			changeWorkNode(newkey);
			if (avl)
				commands.set(0, CommandFactory.createInsertAVL(wn, tree));
			else
				commands.set(0, CommandFactory.createInsert(wn, tree));
			currentPosition = 0;
		}
		else {
			MacroCommand m = (MacroCommand)c;
			while (m.hasNext()) {
				m.perform();
				storeLogAndSectionFrom(m);
				if (!m.hasNext())
					currentPosition++;
			}
			if (keyList.size() >= finalNodeNumber) {currentPosition = 2; return; }
		}
		
		
		
/*		Command com = commands.get(currentPosition);
		if (!(com instanceof NoOperation)) {
			if (avl) {
				InsertAVL ins = (InsertAVL) com;
				while(ins.hasNext())
					ins.perform();
				storeLogAndSectionFrom(ins);
				results.set(0,keyList.get(currentPosition/2)+ " Einfügen beendet");		
				currentPosition++;
				return;
			}
			else{
				Insert ins = (Insert)com;
				while(ins.hasNext())
					ins.perform();
				storeLogAndSectionFrom(ins);
				results.set(0,keyList.get(currentPosition/2)+ " Einfügen beendet");		
				currentPosition++;
				return;
			}
		}
		else
			currentPosition++; */
	}

/**
* Calculates one undo.
*
*/
	public void undo() {
		
		if (!hasPrevious())
			return;
		
		MacroCommand mc = (MacroCommand) commands.get(0);
		mc.undo();
		currentPosition=0;
		
/*		if (this.hasPrevious()){
			//test: first time, that undo() is running?
			if (currentPosition>=commands.size())
				currentPosition--;
			Command com = (Command) commands.get(currentPosition);
			
			if (!(com instanceof NoOperation)){
				if (avl){
					InsertAVL ins = (InsertAVL)com;
					if (ins.hasPrevious()){
						ins.undo();
						storeLogAndSectionFrom(ins);
						return;
					}
					tree.getRoot().setVisualizationStatus(Visualizable.NORMAL);
					currentPosition--;	
				}
				else {
					Insert ins = (Insert)com;
					if (ins.hasPrevious()){
						ins.undo();
						storeLogAndSectionFrom(ins);
						return;
					}
					tree.getRoot().setVisualizationStatus(Visualizable.NORMAL);
					currentPosition--;
				}
			}		
			com.undo();
			//wn.getNextToMe().setVisualizationStatus(Visualizable.FOCUSED);
			results.clear();
			results.add("");
			results.add("2");
			results.add(WORKING);
			
//			commands.remove(currentPosition);
//			commands.remove(currentPosition+1);
			
			currentPosition--;
			int preKey = keyList.get(finalNodeNumber);
			wn.setNextToMe(tree.getNodeFor(preKey));
			wn.setKey(preKey);
			wn.setVisualizationStatus(Visualizable.INVISIBLE);		
		} */
	}

/**
* Calculates one block undo.
*
*/
	public void undoBlockStep() {

		if (!hasPrevious())
			return;
		
		currentPosition = 0;
		MacroCommand mc = (MacroCommand) commands.get(0);
		while (mc.hasPrevious())
			mc.undo();
		
/*		if (this.hasPrevious()){
			//test: first time, that undo() is running?
			if (currentPosition>=commands.size())
				currentPosition--;			
			Command com = commands.get(currentPosition);
			
			
			if (!(com instanceof NoOperation)){
				if(avl){
					InsertAVL ins = (InsertAVL)com;
					if (ins.hasPrevious()){
						while (ins.hasPrevious())
							ins.undo();
						storeLogAndSectionFrom(ins);
						if (hasPrevious())
							currentPosition--;
						return;
					}
					tree.getRoot().setVisualizationStatus(Visualizable.NORMAL);
					currentPosition--;	
				}		
				else {
					Insert ins = (Insert)com;
					if (ins.hasPrevious()){
						while(ins.hasPrevious())
							ins.undo();
						storeLogAndSectionFrom(ins);
						if (this.hasPrevious())
							currentPosition--;
				    return;
					}
					tree.getRoot().setVisualizationStatus(Visualizable.NORMAL);
					currentPosition--;
				}
			}
			
			com.undo();
			results.clear();
			results.add("");
			results.add("2");
			results.add(WORKING);
			
//			commands.remove(currentPosition);
//			commands.remove(currentPosition+1);
						
			currentPosition--;
			int preKey = keyList.get(finalNodeNumber);
			wn.setNextToMe(tree.getNodeFor(preKey));
			wn.setKey(preKey);
			wn.setVisualizationStatus(Visualizable.INVISIBLE);
			tree.getRoot().setVisualizationStatus(Visualizable.NORMAL);
		} */
	}
	
/**
* 
* Terminates the tree generation. After that, the tree has it's current number of nodes.
*/
	public void abort(){
		if (!this.hasPrevious())
			return;	
		Command com = commands.get(currentPosition);
		if (com instanceof NoOperation){
			currentPosition++;			
		}	
		if (currentPosition<commands.size()){
			if (avl) {	
				InsertAVL ins = (InsertAVL)commands.get(currentPosition);
				while (ins.hasPrevious())
					ins.undo();
			}
			else {
				Insert ins = (Insert)commands.get(currentPosition);
				while (ins.hasPrevious())
					ins.undo();
			}
		}		
		commands = commands.subList(0,currentPosition);
		keyList = keyList.subList(0,currentPosition/2);
		finalNodeNumber=keyList.size();	
		results.clear();
		results.add("Suchbaum fertig");
		results.add(" ");
		results.add(DONE);
	}
}