package org.jalgo.module.kmp.algorithm;

import java.io.Serializable;
import java.util.List;

/**
 * This class is the interface of all Stepclasses
 * 
 * @author Danilo Lisske
 */
public interface Step extends Serializable {
	/**
	 * Returns the pattern position.
	 * 
	 * @return the pattern position
	 */
	public int getPatPos();
	
	/**
	 * Returns a descriptionstring, who is specialy for each Stepclass with its parameters
	 * 
	 * @return the descriptiontext
	 */
	public String getDescriptionText();
	
	/**
	 * Returns a list of Highlightinformations, which is specialy for each stepclass with its parameters
	 * 
	 * @return a <code>List</code> of <code>KMPHighlighter</code>
	 */
	public List<KMPHighlighter> getKMPHighlighter();
}