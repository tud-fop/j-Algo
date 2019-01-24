/**
 * 
 */
package org.jalgo.module.ebnf.model.trans;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Observable;

import org.jalgo.module.ebnf.model.ebnf.Term;
import org.jalgo.module.ebnf.model.syndia.SynDiaElem;


/**
 * @author Andre
 */
public class TransMap extends Observable {

	/**
	 * Represents the transMap
	 */
	private Map<SynDiaElem, Term> observableLinkedHashMap;

	/**
	 * Initializes the LinkedHashMap
	 */
	public TransMap() {

		observableLinkedHashMap = new LinkedHashMap<SynDiaElem, Term>();

	}

	/**
	 * This method calls the put()-method of the LinkedHashMap and informs all
	 * observers, that the Map has changed
	 * 
	 * @param key a SynDiaElem
	 * @param value a Term
	 */
	public void put(SynDiaElem key, Term value) {

		observableLinkedHashMap.put(key, value);
		this.setChanged();
	

	}

	/**
	 * This method calls the get()-method of the LinkeHashMap
	 * 
	 * @param key a SyntaxDiagram element
	 * @return The Term associated to SyntaxDiagram element
	 */
	public Term get(SynDiaElem key) {

		return observableLinkedHashMap.get(key);

	}

	/**
	 * This method calls the keySet()-method of the LinkedHashMap
	 * 
	 * @return A set containing all keys
	 */
	public Set<SynDiaElem> keySet() {

		return observableLinkedHashMap.keySet();

	}
	
	/**
	 * This method checks whether the Map is completely tranformed or not
	 * 
	 * @return True, if the Map is completely transformed
	 */
	public boolean isTransformed() {

		for (Term t : observableLinkedHashMap.values()) {

			if (t != null)
				return false;

		}
		return true;

	}

	/** This method checks if NO step was performed
	 * @return True, if no element was transformed yet
	 */
	public boolean isEbnf() {

		for (Term t : observableLinkedHashMap.values()) {

			if (t == null)
				return false;

		}
		return true;

	}

}
