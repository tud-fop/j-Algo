/**
 *	Class FormatString 
 *	
 * @author Joshua Peschke
 * @author Nico Braunisch  
 * @version 1.0
 */
package org.jalgo.module.lambda.model;

import java.util.Iterator;
import java.util.Set;

public class FormatString {

	private String text;
	private Set<Format> format;
	private String position;
	private Iterator<Format> formatIterator; 
	
	/**
	 * 
	 * @param text plaintext of subterm
	 * @param format format of subterm
	 * @param position position of subterm
	 */
	public FormatString(String text, Set<Format> format, String position) {
		this.text = text;
		this.format = format;
		this.position = position;
		this.formatIterator=format.iterator();
	}
	/**
	 * 
	 * @return plaintext of subterm
	 */
	public String getText() {
		return this.text;
	}
	/**
	 * 
	 * @return returns absolut position of current term from root
	 */
	public String getPosition() {
		return this.position;
	}
	/**
	 * 
	 * @return set of format
	 */
	public Set<Format> getFormat() {
		return this.format;
	}

	/**
	 * 
	 * @return true if one more format is avaibal
	 */
	public boolean hasNext(){
		return formatIterator.hasNext();
	}
	/**
	 * 
	 * @return next format
	 */
	public Format next(){
		return formatIterator.next();
	}
	/**
	 * 
	 * @return plaintext of subterm
	 */
	public String toString(){
		return text;
	}
}
