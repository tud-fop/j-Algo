package org.jalgo.module.c0h0.models.ast;

import org.jalgo.module.c0h0.models.ast.tools.BFSIterator;
import org.jalgo.module.c0h0.models.ast.tools.DFSIterator;
import org.jalgo.module.c0h0.models.c0model.AddressVisitor;
import org.jalgo.module.c0h0.models.c0model.SemanticVisitor;
import org.jalgo.module.c0h0.parser.C00Parser;

/**
 * the AST model
 *
 */
public class ASTModel {
	private boolean valid = false;
	private ErrorInformation errorInformation;
	private String errorText;
	private Program program = null;
	private String markedNode = "";
	private int maxdepth = 0;

	public ASTModel() {
		// nothing... will create later, if the mighty controller is in mood to

	}

	/**
	 * Generates the AST-Model itself
	 * 
	 * @param text
	 */
	public void create(String text) {
		C00Parser parser = new C00Parser();
		valid = parser.parse(text);
		program = parser.getProgram();

		// Ein leeres Programm ist auch nicht valide!
		valid &= program != null &&  program.getBlock() != null;

		if (valid) {
			// Tiefe zuruecksetzen
			maxdepth = 0;

			// Adressen hinzufuegen und Semantik ueberpruefen
			BFSIterator iterator = new BFSIterator(this);
			DFSIterator dfsiter = new DFSIterator(this);
			SemanticVisitor semVisitor = new SemanticVisitor(program.getDecl()
					.getVariableList(), program.getScanf().getScanfList(),
					program.getPrintf().getVar());
			AddressVisitor addVisitor = new AddressVisitor();
			int printfAddress = 1;
			Symbol nextStep;
			while (iterator.hasNext()) {
				nextStep = iterator.next();
				nextStep.accept(addVisitor);

				// Leider hier in dem While
				if (!String.valueOf(nextStep.getAddress().charAt(1)).equals("f")) {
					if (printfAddress < Integer.parseInt(String.valueOf(nextStep.getAddress().charAt(1))) + 1) {
						printfAddress = Integer.parseInt(String.valueOf(nextStep.getAddress().charAt(1))) + 1;
					}
				}
				if (nextStep.getAddress().length() > maxdepth) {
					maxdepth = nextStep.getAddress().length();
				}
			}
			while (dfsiter.hasNext()) {
				dfsiter.next().accept(semVisitor);
			}
			program.getPrintf().setAddress("f" + printfAddress);
			valid = valid && semVisitor.isValid();
			errorText += "Semantischer Fehler:<br>" + semVisitor.getError();
		} else {
			if (program == null || program.getBlock() == null) {
				errorText = "Bitte geben Sie ein Programm ein!";
			}
			if (parser.getErrorText() != "") {
				// TODO: Richtige Fehlerausgabe
				// parser.getErrorText();
				errorText = "Syntax Fehler<br>";
			}
		}
	}

	/**
	 * Sets the currently marked / highlighted Symbol
	 * 
	 * @param address as String
	 */
	public void setMarkedNode(String address) {
		markedNode = address;
	}

	/**
	 * Returns Parser error
	 * 
	 * @return the error information
	 */
	public ErrorInformation getErrorInformation() {
		return errorInformation;
	}

	/**
	 * Returns Parser/Semantic error as a String
	 * 
	 * @return the error text
	 */
	public String getErrorText() {
		return errorText;
	}

	/**
	 * returns the validity of the AST
	 * 
	 * @return if valid
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * returns the program
	 * 
	 * @return program
	 */
	public Program getProgram() {
		return program;
	}

	/**
	 * returns the marked node
	 * 
	 * @return the marked node
	 */
	public String getMarkedNode() {
		return markedNode;
	}

	/**
	 * Returns the length of the longest address
	 * 
	 * @return  Integer with the depth
	 */
	public int getMaxDepth() {
		return maxdepth;
	}
}
