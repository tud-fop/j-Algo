package org.jalgo.module.ebnf.model.syndia;

/**
 * Creates syntax diagram systems
 * 
 * @author MichA
 * 
 */
public class SynDiaSystemLibrary {

	static {
		setUpSyntaxDiagrams();
	}

	private static SynDiaSystem sds1, sds2;

	private static void setUpSyntaxDiagrams() {
		// sds1 aus Skript S.152
		sds1 = new SynDiaSystem("S");
		sds1.addSyntaxDiagram("A");
		// Wichtig: bis auf StartVariable müssen ALLE Variablen in das sds "per
		// Hand" eingefügt werden!!!
		sds1.addVariable("A");
		// sds2 frei erfunden...
		sds2 = new SynDiaSystem("Start");
		sds2.addSyntaxDiagram("Diagram1");
		sds2.addVariable("Diagram1");
		sds2.addSyntaxDiagram("Diagram2");
		sds2.addVariable("Diagram2");
		sds2.addSyntaxDiagram("Diagram3");
		sds2.addVariable("Diagram3");
		// Füllen von Syntaxdiagrammsystem 1
		try {
			sds1.getSyntaxDiagram("S").getRoot().addRepetition(0, 0);
			((Repetition) sds1.getSyntaxDiagram("S").getRoot().getSynDiaElem(1))
					.getRight().addTerminalSymbol(0, "c");
			sds1.getSyntaxDiagram("S").getRoot().addVariable(2, "A");
			sds1.addVariable("A");
			// ----
			sds1.getSyntaxDiagram("A").getRoot().addBranch(0, 0);
			((Branch) sds1.getSyntaxDiagram("A").getRoot().getSynDiaElem(1))
					.getLeft().addTerminalSymbol(0, "a");
			((Branch) sds1.getSyntaxDiagram("A").getRoot().getSynDiaElem(1))
					.getLeft().addTerminalSymbol(2, "b");
			((Branch) sds1.getSyntaxDiagram("A").getRoot().getSynDiaElem(1))
					.getLeft().addVariable(2, "A");
			sds1.addVariable("A");
			((Branch) sds1.getSyntaxDiagram("A").getRoot().getSynDiaElem(1))
					.getRight().addTerminalSymbol(0, "a");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoNullElemException e) {
			e.printStackTrace();
		} catch (ElementNotFoundException e) {
			e.printStackTrace();
		}
		// Füllen von Syntaxdiagrammsystem 2
		try {
			sds2.getSyntaxDiagram("Start").getRoot().addTerminalSymbol(0, "a");
			sds2.getSyntaxDiagram("Start").getRoot().addVariable(2, "Diagram1");
			sds2.addVariable("Diagram1");
			sds2.getSyntaxDiagram("Start").getRoot().addRepetition(4, 4);
			sds2.getSyntaxDiagram("Start").getRoot().addTerminalSymbol(6, "c");
			((Repetition) sds2.getSyntaxDiagram("Start").getRoot()
					.getSynDiaElem(5)).getLeft().addTerminalSymbol(0, "b");
			((Repetition) sds2.getSyntaxDiagram("Start").getRoot()
					.getSynDiaElem(5)).getRight().addVariable(0, "Diagram2");
			sds2.addVariable("Diagram2");
			// ---- 2
			sds2.getSyntaxDiagram("Diagram1").getRoot().addTerminalSymbol(0,
					"a");
			sds2.getSyntaxDiagram("Diagram1").getRoot().addTerminalSymbol(2,
					"b");
			sds2.getSyntaxDiagram("Diagram1").getRoot().addVariable(4,
					"Diagram3");
			sds2.addVariable("Diagram3");
			sds2.getSyntaxDiagram("Diagram1").getRoot().addTerminalSymbol(6,
					"b");
			sds2.getSyntaxDiagram("Diagram1").getRoot().addTerminalSymbol(8,
					"c");
			sds2.getSyntaxDiagram("Diagram1").getRoot().addBranch(2, 8);
			((Branch) sds2.getSyntaxDiagram("Diagram1").getRoot()
					.getSynDiaElem(3)).getRight().addTerminalSymbol(0, "a");
			// ---- 3
			sds2.getSyntaxDiagram("Diagram2").getRoot().addTerminalSymbol(0,
					"e");
			sds2.getSyntaxDiagram("Diagram2").getRoot().addVariable(2,
					"Diagram2");
			sds2.addVariable("Diagram2");
			sds2.getSyntaxDiagram("Diagram2").getRoot().addBranch(2, 4);
			sds2.getSyntaxDiagram("Diagram2").getRoot().addRepetition(2, 4);
			((Repetition) sds2.getSyntaxDiagram("Diagram2").getRoot()
					.getSynDiaElem(3)).getRight().addTerminalSymbol(0, "g");
			((Repetition) sds2.getSyntaxDiagram("Diagram2").getRoot()
					.getSynDiaElem(3)).getRight().addTerminalSymbol(2, "f");
			((Repetition) sds2.getSyntaxDiagram("Diagram2").getRoot()
					.getSynDiaElem(3)).getRight().addRepetition(2, 2);
			((Repetition) ((Repetition) sds2.getSyntaxDiagram("Diagram2")
					.getRoot().getSynDiaElem(3)).getRight().getSynDiaElem(3))
					.getRight().addTerminalSymbol(0, "a");
			((Repetition) ((Repetition) sds2.getSyntaxDiagram("Diagram2")
					.getRoot().getSynDiaElem(3)).getRight().getSynDiaElem(3))
					.getRight().addTerminalSymbol(2, "b");
			// ---- 4
			sds2.getSyntaxDiagram("Diagram3").getRoot().addBranch(0, 0);
			((Branch) sds2.getSyntaxDiagram("Diagram3").getRoot()
					.getSynDiaElem(1)).getRight().addBranch(0, 0);
			((Branch) sds2.getSyntaxDiagram("Diagram3").getRoot()
					.getSynDiaElem(1)).getRight().addTerminalSymbol(2, "b");
			((Branch) ((Branch) sds2.getSyntaxDiagram("Diagram3").getRoot()
					.getSynDiaElem(1)).getRight().getSynDiaElem(1)).getRight()
					.addTerminalSymbol(0, "c");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoNullElemException e) {
			e.printStackTrace();
		} catch (ElementNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return syntax diagram system 1
	 */
	public static SynDiaSystem getSynDiaSystem1() {
		return sds1;
	}

	/**
	 * 
	 * @return syntax diagram system 2
	 */
	public static SynDiaSystem getSynDiaSystem2() {
		// System.out.println("sds2: " + sds2);
		// System.out.println("sds2: " + sds2.printNullElems());
		return sds2;
	}
}
