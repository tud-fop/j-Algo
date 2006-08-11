package org.jalgo.module.ebnf.controller.syndia;

import org.jalgo.module.ebnf.model.syndia.SyntaxDiagram;

/**
 * Action that renames an existing syntax diagram.
 * 
 * @author Michael Thiele
 *
 */
public class RenameSyntaxDiagramAction extends RenameElementAction {

	private SyntaxDiagram sd;

	private boolean setStartDiagram;

	private String oldStartDiagram;

	/**
	 * 
	 * @param sd
	 *            the syntax diagram to rename
	 * @param newLabel
	 *            the new label for this syntax diagram
	 * @param setStartDiagram
	 *            is <code>true</code> if this diagram will be new start
	 *            diagram
	 */
	public RenameSyntaxDiagramAction(SyntaxDiagram sd, String newLabel,
			boolean setStartDiagram) {
		super(newLabel);
		this.sd = sd;
		this.setStartDiagram = setStartDiagram;
		oldStartDiagram = sd.getMySynDiaSystem().getStartDiagram();
		oldLabel = sd.getName();
	}

	public void perform() throws Exception {
		sd.setName(newLabel);
		if (setStartDiagram)
			sd.getMySynDiaSystem().setStartDiagram(sd.getName());
	}

	public void undo() throws Exception {
		sd.setName(oldLabel);
		if (setStartDiagram)
			sd.getMySynDiaSystem().setStartDiagram(oldStartDiagram);
	}

}
