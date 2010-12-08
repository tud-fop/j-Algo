package org.jalgo.module.bfsdfs.controller;

import org.jalgo.module.bfsdfs.graph.ObservableGraph;
import org.jalgo.module.bfsdfs.undo.Step;

/**
 * DesignStep implements {@link Step} from from undo package.
 * It represent a step in the design mode,which can do undo or redo.
 * @author Johannes Siegert
 */
abstract class DesignStep implements Step{

	private ObservableGraph observableGraph;
	
	/**
	 * @param observableGraph the observable graph on which the method will be called
	 * @throws NullPointerException if <code>observableGraph</code> is <code>null</code>
	 */
	public DesignStep(ObservableGraph observableGraph) throws NullPointerException{
		if(observableGraph==null){
			throw new NullPointerException();
		}
		this.observableGraph = observableGraph;
	}

	public ObservableGraph getObservableGraph() {
		return observableGraph;
	}
}
