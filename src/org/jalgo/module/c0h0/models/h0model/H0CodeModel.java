package org.jalgo.module.c0h0.models.h0model;

import org.jalgo.module.c0h0.controller.Controller;
import org.jalgo.module.c0h0.models.Generator;
import org.jalgo.module.c0h0.models.Performer;
import org.jalgo.module.c0h0.models.ast.Symbol;
import org.jalgo.module.c0h0.models.ast.tools.DFSIterator;

/**
 * Generates, contains and returns Haskell<sub>0</sub> code.
 * 
 * @author Peter Schwede
 * 
 */
public class H0CodeModel implements Performer, Generator {
	private H0GeneratingVisitor visitor;
	private int step;
	private Controller controller;
	private String processedAddress;
	private int maxStep = 0;
	private boolean active = false;

	/**
	 * Generates, contains and returns Haskell<sub>0</sub> code.
	 */
	public H0CodeModel(Controller controller) {
		this.controller = controller;
		visitor = new H0GeneratingVisitor();
		step = 0;
		processedAddress = null;
	}

	/**
	 * Generates Haskell<sub>0</sub>-Code.
	 * 
	 * Internally a Visitor is used.
	 * 
	 * @see H0GeneratingVisitor
	 * 
	 */
	public void generate() {
		clear();
		int m = controller.getASTModel().getProgram().getDecl()
				.getVariableList().size();
		int k = controller.getASTModel().getProgram().getScanf().getScanfList()
				.size();
		int i = controller.getASTModel().getProgram().getPrintf().getVar()
				.getIndex();
		visitor.setMKI(m, k, i);

		for (DFSIterator bit = new DFSIterator(controller.getASTModel()); bit
				.hasNext();) {
			bit.next().accept(visitor);
		}
		maxStep = visitor.getSteps().size();
	}

	/**
	 * Delivers the contained code.
	 * 
	 * @return Complete Haskell<sub>0</sub> string with head, body and foot
	 */
	public String getCode() {
		return visitor.getPlain();
	}

	/**
	 * Returns symbol at a given line
	 * 
	 * Catches illegal line numbers
	 * 
	 * @param line
	 * @return Symbol at a given line
	 */
	public Symbol getSymbolAtLine(int line) {
		if (line > 0 && line < visitor.getSymbols().size())
			return visitor.getSymbols().get(line);
		return null;
	}

	/**
	 * Inverse of @see getSymbolAtLine
	 * 
	 * Gets the first line where an Symbol with given address is written
	 * 
	 * @param address
	 * @return line number
	 */
	public int getLineOfSymbol(String address) {
		int i = 0;
		if (address != "")
			for (Symbol s : visitor.getSymbols()) {
				if (s != null && s.getAddress() != null
						&& s.getAddress().equals(address))
					return i;
				i++;
			}
		return i;
	}

	/**
	 * @return HTML-formatted Haskell<sub>0</sub>-Code for the current step.
	 */
	public String getHTMLCode() {
		// Show nothing on the first step
		if (step == 0)
			return "";

		int lastLineOfStep = visitor.getSteps().get(Math.min(step, maxStep-1));
		Symbol sym = getSymbolAtLine(lastLineOfStep);
		if (step <= maxStep) {
			processedAddress = sym != null ? sym.getAddress() : "";
		} else {
			processedAddress = "";
		}
		return visitor.getHTML(0, lastLineOfStep, step >= maxStep);
	}

	/**
	 * Delivers the contained code in HTML.
	 * 
	 * @return HTML code
	 */
	public String getAllHTMLCode() {
		return visitor.getHTML();
	}

	/**
	 * Resets this H0CodeModel, as if it's new.
	 */
	public void clear() {
		visitor.clear();
		step = 0;
		processedAddress = null;
		maxStep = 0;
	}

	public void performStep() {
		step = Math.min(step + 1, maxStep + 1);
		controller.updateViews();
		if (step < maxStep)
			controller.markNode(processedAddress);
		else if (step == maxStep)
			controller.markNode(visitor.getPrintAddress());
		else {
			controller.markNode("");
		}
	}

	public void performAll() {
		step = maxStep + 1;
		controller.updateViews();
		controller.markNode("");
	}

	public void undoStep() {
		step = Math.max(0, step - 1);
		controller.updateViews();
		if (step < maxStep)
			controller.markNode(processedAddress);
		else if (step == maxStep)
			controller.markNode(visitor.getPrintAddress());
		else {
			controller.markNode("");
		}
	}

	public void undoAll() {
		step = 0;
		controller.updateViews();
	}

	public boolean isDone() {
		return step > maxStep;
	}

	public boolean isClear() {
		return step == 0;
	}

	public String getAddress() {
		return processedAddress;
	}

	public void setActive(boolean a) {
		active = a;
	}

	public boolean isActive() {
		return active;
	}

	public int getNumberOfLines() {
		return visitor.getNumberOfLines() + ((step <= maxStep) ? visitor.foot().split("\n").length : 0);
	}
}
