package org.jalgo.module.ebnf.controller.syndia;

import org.jalgo.module.ebnf.model.syndia.SyntaxDiagram;
import org.jalgo.module.ebnf.util.IAction;

/**
 * Action that deletes a syntax diagram.
 * 
 * @author Michael Thiele
 * 
 */
public class DeleteSyntaxDiagramAction implements IAction {

	private SyntaxDiagram syntaxDiagram;

	/**
	 * 
	 * @param syntaxDiagram
	 *            the syntax diagram to delete
	 */
	public DeleteSyntaxDiagramAction(SyntaxDiagram syntaxDiagram) {
		this.syntaxDiagram = syntaxDiagram;
	}

	public void perform() throws Exception {
		syntaxDiagram.getMySynDiaSystem().removeSyntaxDiagram(
				syntaxDiagram.getName(), true);
	}

	public void undo() throws Exception {
		syntaxDiagram.getMySynDiaSystem().undoDeleteSyntaxDiagram();
	}

}
