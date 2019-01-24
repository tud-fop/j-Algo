package org.jalgo.module.pulsemem.core;

import java.util.HashSet;
import java.util.Set;

/**
 * Saves nice-to-know infos about a variable
 *
 * @author Joschim Protze
 */
public class VarInfo {

	private int [] scope;
	private Set<Integer[]> appearance;
	
	/**
	 * 
	 * @param begin of the scope
	 * @param end of the scope
	 * @param position of the variable
	 */
	public VarInfo(int begin, int end, Integer[] pos){
		scope=new int [] {begin,end};
		appearance=new HashSet<Integer[]>();
		appearance.add(pos);
	}

	public void addPos(Integer[] pos){
		appearance.add(pos);
	}
	
	public String toString(){
		String ret="["+scope[0]+","+scope[1]+", ";
		for (Integer[] i : appearance){
			ret+= "["+i[0]+","+i[1]+"]";
		}
		return ret+"]";
	}
	
	/**
	 * returns the begin of the scope interval
	 * @return the begin of the scope interval
	 */
	public int getScopeBegin(){
		return scope[0];
	}

	/**
	 * returns the end of the scope interval
	 * @return the end of the scope interval
	 */
	public int getScopeEnd(){
		return scope[1];
	}

	/**
	 * returns the scope-interval as an int[2]
	 * begin=getScope()[0]
	 * end=getScope()[1]
	 * @return scope interval
	 */
	public int[] getScope(){
		return scope;
	}

	/**
	 * for (Integer[] i : getAppearance()){
	 * 	positionLine=i[0];
	 * 	positionColumn=i[1];
	 * 	//...
	 * }
	 * @return a Set<{Line,Col}> of the Variable Appearence
	 */
	public Set<Integer[]> getAppearance(){
		return appearance;
	}

}
