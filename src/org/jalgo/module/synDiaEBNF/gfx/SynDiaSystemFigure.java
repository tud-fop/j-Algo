/*
 * Created on 17.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.gfx;

import java.io.Serializable;
import java.util.List;

import org.eclipse.draw2d.Figure;

/**
 * Represents the whole syntactical diagram system, including one or more syntactical diagrams.
 *  
 * @author Michael Pradel
 */
public class SynDiaSystemFigure extends Figure implements Serializable {
	
	private List synDias; // list of InitialFigure's 
	
	public SynDiaSystemFigure(List synDias){
		this.synDias=synDias;
	}

	public void addSynDia(InitialFigure newInitialFigure) {
		synDias.add(newInitialFigure);
	}
	
	/**
	 * @return
	 */
	public List getSynDias() {
		return synDias;
	}
	
	public void setSynDias(List synDias){
		this.synDias=synDias;
	}

}
