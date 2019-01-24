package org.jalgo.module.ebnf.controller.syndia;

import org.jalgo.module.ebnf.model.syndia.SynDiaSystem;
import org.jalgo.module.ebnf.util.IAction;

/**
 * Action that adds a syntax diagram to a syntax diagram system.
 * 
 * @author Michael Thiele
 *
 */
public class AddSynDiaAction implements IAction {

	private SynDiaSystem sds;

	private String name, oldStartDiagramName;

	private boolean startDiagram;
	
	private boolean undo;

	/**
	 * 
	 * @param sds
	 *            the syntax diagram system of the actual syntax diagram
	 * @param name
	 *            the name for the new diagram
	 * @param startDiagram
	 *            is <code>true</code> if new diagram will be the new start
	 *            diagram
	 */
	public AddSynDiaAction(SynDiaSystem sds, String name, boolean startDiagram) {
		this.sds = sds;
		this.name = name;
		this.startDiagram = startDiagram;
		undo = false;
	}

	public void perform() throws Exception {
		if (!undo)
			sds.addSyntaxDiagram(name);
		else
			sds.undoRemoveSyntaxDiagram();
		if (startDiagram) {
			oldStartDiagramName = sds.getStartDiagram();
			sds.setStartDiagram(name);
		}
	}

	public void undo() throws Exception {
		undo = true;
		sds.removeSyntaxDiagram(name, false);
		if (startDiagram) {
			sds.setStartDiagram(oldStartDiagramName);
		}
	}

}
