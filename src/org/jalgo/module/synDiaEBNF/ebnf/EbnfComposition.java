/*
 * Created on 29.04.2004
 */
 
package org.jalgo.module.synDiaEBNF.ebnf;

import java.io.Serializable;

/**
 * Is an abstract class to have a similar name for all EbnfElement, 
 * which take part at the Composite Design Pattern. 
 * Every Compound could take part of another.
 * 
 * @author Babett Schaliz
 */
public abstract class EbnfComposition
	extends EbnfElement
	implements Serializable {

}