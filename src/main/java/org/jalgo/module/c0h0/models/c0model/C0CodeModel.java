package org.jalgo.module.c0h0.models.c0model;

import java.util.ArrayList;
import java.util.LinkedList;

import org.jalgo.module.c0h0.controller.Controller;
import org.jalgo.module.c0h0.models.Generator;
import org.jalgo.module.c0h0.models.Performer;
import org.jalgo.module.c0h0.models.ast.Var;
import org.jalgo.module.c0h0.models.ast.tools.DFSIterator;
import org.jalgo.module.c0h0.views.TerminalView;

/**
 * Generates, contains and returns C<sub>0</sub> code.
 * 
 */
public class C0CodeModel implements Performer, Generator {
	private ArrayList<String> editCode = new ArrayList<String>();
	private String code;
	private StringBuilder addresses;
	private LinkedList<String> addressList = new LinkedList<String>();
	private int currentStep, addressCount;
	private Controller controller;
	private boolean active = true;
	private boolean actual = false;

	public C0CodeModel(Controller controller) {
		this.controller = controller;
		code = "";
		addresses = new StringBuilder();
		currentStep = 0;
	}

	/**
	 * @return formatted C-Code of generateCodeFragment
	 */
	public String getCodeForm() {
		String result = "";
		for (String l : editCode) {
			result += l + "\n";
		}
		return result;
	}

	/**
	 * Returns if the View got the last address
	 * 
	 * @return Boolean if createAddresses has been used
	 */
	public boolean isActual() {
		return actual;
	}

	/**
	 * creates Addresses for the left JEditPane of the C0View
	 */
	private void createAddresses() {
		int i = 0;
		actual = false;
		addresses = new StringBuilder();
		addresses.append("<table>");
		for (int s = 0; s < currentStep; s++) {
			while (i < addressList.size() && "br".equals(addressList.get(i))) {
				addresses.append("<tr><td></td></tr>");
				i++;
			}
			if (i < addressList.size())
				addresses.append("<tr><td><a href=\"" + addressList.get(i)
						+ "\">" + addressList.get(i) + "</a></td></tr>");
			i++;
		}
		if (i > 0 && i < addressList.size() + 1) {
			controller.markNode(addressList.get(i - 1));
		} else {
			controller.markNode("");
		}
		addresses.append("</table>");
	}

	/**
	 * Returns the line of a given address
	 * 
	 * @param address
	 *            String with the address
	 * @return Integer with the line
	 */
	public int getLineOfAddress(String address) {
		int line = 0;
		if (addressList.contains(address)) {
			line = addressList.indexOf(address);
		}
		return line;
	}

	/**
	 * Returns the total amount of lines
	 * 
	 * @return Integer with the amount
	 */
	public int getTotalLines() {
		return addressList.size() + 1;
	}

	/**
	 * Generates a C-Code mask with main, declarations, scanfs and
	 * printf-Statement
	 * 
	 * @param m
	 * @param k
	 * @param i
	 * @return the C-Code mask
	 */
	public int generateCodeFragement(int m, int k, int i) {
		int caret = 2;
		// First empty List
		editCode.clear();

		// Head first
		editCode.add("#include <stdio.h>");
		editCode.add("");
		editCode.add("int main()");
		editCode.add("{");
		String vars = "  int";
		for (int j = 1; j <= m; j++) {
			vars += " x" + j;
			if (j < m) {
				vars += ",";
			} else {
				vars += ";";
			}
		}
		editCode.add(vars);
		for (int j = 1; j <= k; j++) {
			editCode.add("  scanf(\"%i\", &x" + j + ");");
		}

		// Move Caret
		for (String line : editCode)
			caret += line.length() + 1;

		// body empty
		editCode.add("  ");

		// foot last
		editCode.add("  printf(\"%d\", x" + i + ");");
		editCode.add("  return 0;");
		editCode.add("}");

		return caret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.module.c0h0.models.Generator#generate()
	 */
	public void generate() {
		C0GeneratingVisitor visitor = new C0GeneratingVisitor();
		DFSIterator iter = new DFSIterator(controller.getASTModel());
		while (iter.hasNext()) {
			iter.next().accept(visitor);
		}
		addressCount = visitor.getAddressCount() + 1;

		// include
		addressList.add("br");
		// Leerzeichen
		addressList.add("br");
		// Main
		addressList.add("br");
		// Declaration
		addressList.add("br");

		code = "<table class=\"grayed\"><tr><td>";
		code += "<table><tr><td>#include</td><td>&nbsp;&lt;stdio.h></td></tr></table>";
		code += "</td></tr>";
		code += "<tr><td>";
		code += "</td></tr>";
		code += "<tr><td>";
		code += "int main() {";
		code += "</td></tr>";
		code += "<tr><td>";
		code += "&nbsp;&nbsp;int ";
		ArrayList<Var> vars = controller.getASTModel().getProgram().getDecl()
				.getVariableList();
		for (int i = 0; i < vars.size(); i++) {
			code += vars.get(i).toString();
			if (i != vars.size() - 1) {
				code += ", ";
			} else {
				code += ";";
			}
		}
		code += "</td></tr>";

		code += "<tr><td>";
		for (Var v : controller.getASTModel().getProgram().getScanf()
				.getScanfList()) {
			code += "&nbsp;&nbsp;scanf(\"%i\", &amp;" + v
					+ ");</td></tr><tr><td>";
			addressList.add("br");
		}
		code += "</td></tr></table>";
		addressList.add("br");
		code += visitor.getCode();
		addressList.addAll(visitor.getAddressList());
		addressList.add(controller.getASTModel().getProgram().getPrintf()
				.getAddress());
		addressCount++;
		code += "<tr class=\"" + addressList.getLast() + "\"><td>";
		code += "&nbsp;&nbsp;<a href=\"" + addressList.getLast()
				+ "\">printf(\"%d\", "
				+ controller.getASTModel().getProgram().getPrintf().getVar()
				+ ");</a>";
		code += "</td></tr>";
		code += "</table><br><table class=\"grayed\">";
		code += "<tr><td>";
		code += "&nbsp;&nbsp;return 0;";
		code += "</td></tr>";
		code += "<tr><td>";
		code += "}";
		code += "</td></tr></table>";
	}

	/**
	 * Saves the given Code to the editCode
	 * 
	 * @param code
	 */
	public void setCode(String code) {
		editCode.clear();
		for (String s : code.split("\n")) {
			editCode.add(s);
		}
	}

	/**
	 * @param code
	 *            saves the ArrayList<String> which normally comes from loading
	 *            .c0-Code to the local editCode
	 */
	public void loadCode(ArrayList<String> code) {
		clear();
		TerminalView.println("c0 clear");
		this.editCode = code;
	}

	/**
	 * @return editCode String which contains at least a C0Mask from
	 *         generateCodeFragement
	 */
	public ArrayList<String> getPlainC0Code() {
		return editCode;
	}

	/**
	 * @return code String with HTML-formatted C0-Code for the C0View
	 */
	public String getFormattedC0Code() {
		return code;
	}

	/**
	 * @return addresses String with HTML-formatted Addresses for the C0View
	 */
	public String getAddresses() {
		actual = true;
		return addresses.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.module.c0h0.models.Performer#clear()
	 */
	public void clear() {
		currentStep = 0;
		addressCount = 0;
		addressList.clear();
		code = "";
		actual = false;
		addresses = new StringBuilder();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.module.c0h0.models.Performer#performAll() /* (non-Javadoc)
	 * 
	 * @see org.jalgo.module.c0h0.models.Performer#isDone()
	 */
	public void performAll() {
		currentStep = addressCount;
		createAddresses();
		TerminalView.println("c0 : " + currentStep + " of " + addressCount);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.module.c0h0.models.Performer#performStep()
	 * 
	 * 
	 * /* (non-Javadoc)
	 * 
	 * @see org.jalgo.module.c0h0.models.Performer#isClear()
	 */
	public void performStep() {
		if (addressCount > currentStep)
			currentStep++;
		createAddresses();
		TerminalView.println("c0 : " + currentStep + " of " + addressCount);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.module.c0h0.models.Performer#undoStep()
	 */
	public void undoStep() {
		if (currentStep > 0) {
			currentStep--;
		}
		createAddresses();
		TerminalView.println("c0 : " + currentStep + " of " + addressCount);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.module.c0h0.models.Performer#undoAll()
	 */
	public void undoAll() {
		currentStep = 0;
		createAddresses();
		TerminalView.println("c0 : " + currentStep + " of " + addressCount);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.module.c0h0.models.Performer#isClear()
	 */
	public boolean isClear() {
		return currentStep == 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.module.c0h0.models.Performer#isDone()
	 */
	public boolean isDone() {
		return currentStep == addressCount;
	}

	public void setActive(boolean a) {
		this.active = a;
	}

	public boolean isActive() {
		return active;
	}
}
