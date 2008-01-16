package org.jalgo.module.pulsemem.core;

import java.util.ArrayList;
import java.util.List;

import org.jalgo.module.pulsemem.core.exceptions.EMemoryError;


/**
 * This class provides the Variables Stack
 *
 * @author Joachim Protze
 */
public class Stack {
	protected List<Variable> stack;
	protected int visibilityLevel;

	/**
	 * Class Stack contains a variable stack and offers various functions get a filtered list of them
	 *
	 */
	public Stack(){
		stack=new ArrayList<Variable>();
		visibilityLevel=0;
	}

	/**
	 * returns a filtered List of actual visible variables
	 * @return a filtered List of actual visible variables
	 */
	public List<Variable> getVisibleStack(){
		List<Variable> ret=new ArrayList<Variable>();
		for (Variable i : stack)
			if(i.getVisibilityLevel()>=visibilityLevel || i.isGlobal()){
				Variable k=null;
				for (Variable j : ret){
					if (i.getName().equals(j.getName())){
						k=j;
						break;
					}
				}
				ret.remove(k);
				ret.add(i);
			}
		return ret;
	}

	/**
	 * returns a filtered List of actual via Pointers visible Variables
	 * @return a filtered List of actual via Pointers visible Variables
	 */
	public List<Variable> getPointedStack(){
		List<Variable> ret=new ArrayList<Variable>();
		for (Variable i : getVisibleStack())
			if(i instanceof Zeiger && i.isReferenced()){
				ret.add((Variable)i.getValue());
			}
		return ret;
	}

	/**
	 * returns a cloned list of all variables currently on the stack
	 * @return a cloned list of all variables currently on the stack
	 */
	public List<Variable> cloneStack(){
		List<Variable> ret=new ArrayList<Variable>();
		for (Variable i : stack)
			if (i instanceof Zeiger){
				if (!((Zeiger)i).isReferenced()){
					((Zeiger)i).clone();
				}else{
					Variable k=null;
					for (Variable j : ret){
						if (j.getName().equals(((Zeiger)i).getTarget().getName()) && j.getVisibilityLevel()==((Zeiger)i).getTarget().getVisibilityLevel()){
							k=j;
							break;
						}
					}
					ret.add(((Zeiger)i).clone(k));
				}
			}else
				ret.add(i.clone());
		return ret;
	}

	/**
	 * returns a list of all variables currently on the stack
	 * @return a list of all variables currently on the stack
	 */
	public List<Variable> getStack() {
		return stack;
	}

	/**
	 * returns the visibilitylevel for the variables
	 * @return the visibilitylevel
	 */
	public int getVisibilityLevel() {
		return visibilityLevel;
	}

	/**
	 * sets the visibilitylevel for the variables
	 * @param visibilityLevel
	 */
	public void setVisibilityLevel(int visibilityLevel) {
		this.visibilityLevel = visibilityLevel;
	}

	/**
	 * increases the visibilitylevel for the variables
	 *
	 */
	public void increaseVisibilityLevel() {
		this.visibilityLevel++;
	}

	/**
	 * decreases the visibilitylevel for the variables
	 *
	 */
	public void decreaseVisibilityLevel() {
		this.visibilityLevel--;
	}

	/**
	 * puts a variable to the stack
	 * @param var
	 */
	public void addVar(Variable var){
		stack.add(var);
	}

	/**
	 * removes a variable from the stack
	 * @param var
	 */
	public void removeVar(Variable var){
		if (var.getVisibilityLevel()<this.visibilityLevel)
			throw new EMemoryError(var.name);
		stack.remove(var);
	}

	/**
	 * removes all actual visible local variables from the stack
	 * intended for cleaning after return from a methode
	 */
	public void removeAllToplevelVar(){
		List<Variable> tmp=new ArrayList<Variable>();
		for (Variable i : stack){
			if(i.getVisibilityLevel()>=visibilityLevel)
				tmp.add(i);
		}
		for (Variable i : tmp){
			stack.remove(i);
		}
		decreaseVisibilityLevel();
	}




}
