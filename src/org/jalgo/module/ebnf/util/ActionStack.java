package org.jalgo.module.ebnf.util;

import java.util.Stack;
import java.util.Observable;
import java.util.Observer;

/**
 * The <code>ActionStack</code> implements two Stacks. One Stack saves the
 * operations performed, the other Stack saves the operations undone. Each
 * operation must be an implemented IAction.
 * 
 * The <code>ActionStack</code> is observable. He must have at least one
 * observer, the MainController. Therefore you have to give the Adress of the
 * MainController to the ActionStack if you create one.
 * 
 * The <code>ActionStack</code> will inform the MainController if redo or undo
 * is possible and if one of these statuses has changed.
 * 
 * The <code>ActionStack</code> will throw <code>Exception</code>s
 * implemented by the Action performed, if <code>perform()</code>,
 * <code>undo()</code> or <code>redo()</redo>
 * fails.
 * 
 * @author Claas Wilke
 *
 */
public class ActionStack extends Observable {

	/*
	 * Every IAction that was performed is going to be pushed onto the undoList.
	 */
	private Stack<IAction> undoList;

	/*
	 * Each IAction the user has made undon is going to be pushed onto the
	 * redoList.
	 */
	private Stack<IAction> redoList;

	/**
	 * The constructor constructs a new ActionStack with an empty undoList and
	 * an empty redoList.
	 * 
	 * @param observer
	 *            An Observer the ActionStack will notify if its undo or redo
	 *            status will change. The observer will be a MainController in a
	 *            future version. It is any class which implements
	 *            java.Util.Observer by now. TODO change param type to
	 *            MainController.
	 */
	public ActionStack(Observer observer) {
		undoList = new Stack<IAction>();
		redoList = new Stack<IAction>();
		addObserver(observer);
	}

	public ActionStack() {
		undoList = new Stack<IAction>();
		redoList = new Stack<IAction>();

	}

	/**
	 * Returns true if an undoOperation is possible. An undoOperation is
	 * possible, if the undoList is not empty.
	 * 
	 * @return True if undo() is possible.
	 */
	public boolean isUndoPossible() {
		return !undoList.empty();
	}

	/**
	 * Returns true if an redoOperation is possible. An redoOperation is
	 * possible, if the redoList is not empty.
	 * 
	 * @return True if redo() is possible.
	 */
	public boolean isRedoPossible() {
		return !redoList.empty();
	}

	/**
	 * Makes an IAction undone, if there is an IAction in the undoList.
	 * 
	 * Throws <code>Exception</code>s implemented by the <code>IAction</code>
	 * which should be redone.
	 * 
	 * @return True if step was undone
	 */
	public boolean undo() throws Exception {
		if (isUndoPossible()) {
			/*
			 * If redoList is empty, the status will change to full. (Used at
			 * the end of the method to (optionaly) inform the Observers.
			 */
			try {
				boolean statusWillChange = !isRedoPossible();

				IAction anAction = undoList.pop();
				anAction.undo();
				/* The Action undone can now be redone. */
				redoList.push(anAction);

				if (statusWillChange || !isUndoPossible()) {
					setChanged();
					notifyObservers(this);
				}
				
				return true;
			} catch (Exception e) {
				throw e;
			}

		} else {
			// System.out.println("Undo was not possible.");
			return false;
		}
	}

	/**
	 * Makes an IAction redone, if there is an IAction in the redoList.
	 * 
	 * Throws <code>Exception</code>s implemented by the <code>IAction</code>
	 * which should be redone.
	 * 
	 * @return True if step was redone
	 */
	public boolean redo() throws Exception {
		if (isRedoPossible()) {
			try {
				boolean statusWillChange = !isUndoPossible();

				IAction anAction = redoList.pop();
				anAction.perform();
				/* The Action can now be undone again */
				undoList.push(anAction);

				if (statusWillChange || !isRedoPossible()) {
					setChanged();
					notifyObservers(this);
				}
				
				return true;
				
			} catch (Exception e) {
				throw e;
			}

		} else {
			// System.out.println("Redo was not possible.");
			return false;
		}
	}

	/**
	 * Performs an IAction an pushes the <code>IAction</code> onto the
	 * undoList.
	 * 
	 * Throws <code>Exception</code>s implemented by the <code>IAction</code>
	 * which should be performed.
	 * 
	 * @param anAction
	 *            IAction which should be performed.
	 */
	public void perform(IAction anAction) throws Exception {
		/*
		 * If undoList is empty, the status will change to full. (Used at the
		 * end of the method to (optionaly) inform the Observers.
		 */
		try {
			Boolean statusWillChange = !isUndoPossible();
			anAction.perform();
			undoList.push(anAction);
			/*
			 * If redoList is not empty it has to be cleared. Every IAction
			 * undone before can not be redone any longer.
			 */
			if (isRedoPossible()) {
				redoList.clear();
				/*
				 * Redo is not possible any longer. The Class has to notify its
				 * Observers.
				 */
				setChanged();
				notifyObservers(this);
			}
			/*
			 * If the undoList was empty before the IAction was performed, the
			 * undoList has changed its status. The Class has to notify its
			 * Observers (if not done before).
			 */
			else if (statusWillChange) {
				setChanged();
				notifyObservers(this);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * This method empty the undo and the redo list and makes every undo or redo
	 * operation impossible. Be carefull!
	 * 
	 */
	public void clear() {
		undoList.clear();
		redoList.clear();
		setChanged();
		notifyObservers(this);
	}
	
	public String toString() {
		String s = "undoList:\n";
		for(IAction action:undoList) {
			s += action.getClass() + "\n";
		}
		s += "\nredoList:\n";
		for(IAction action:redoList) {
			s += action.getClass() + "\n";
		}
		return s;
	}
}
