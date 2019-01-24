package org.jalgo.module.unifikation.algo.model;

//specifies functions that have to be implemented to realize a formated output
public interface IModelFormat {	
	//returns the formated representation of the element
	public String getFormatText();
	//returns the unformatted representation of the element
	public String toString();

}
