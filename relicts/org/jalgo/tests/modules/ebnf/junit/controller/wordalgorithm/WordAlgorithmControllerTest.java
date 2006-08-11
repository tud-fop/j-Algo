package org.jalgo.tests.ebnf.junit.controller.wordalgorithm;

import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.controller.wordalgorithm.WordAlgorithmController;
import org.jalgo.module.ebnf.model.syndia.Branch;
import org.jalgo.module.ebnf.model.syndia.Repetition;
import org.jalgo.module.ebnf.model.syndia.SynDiaSystem;
import org.jalgo.module.ebnf.model.syndia.SynDiaSystemLibrary;
import org.jalgo.module.ebnf.model.syndia.SyntaxDiagram;
import org.jalgo.module.ebnf.model.syndia.TerminalSymbol;
import org.jalgo.module.ebnf.model.syndia.Variable;
import org.jalgo.module.ebnf.model.wordalgorithm.WordAlgoModel;

import junit.framework.TestCase;

public class WordAlgorithmControllerTest extends TestCase {
	
	//FIXME
	private static String EXPL_BEFORE_START = Messages.getString("ebnf",
	"WordAlgo.Explanation_BeforeStart");
	private static String EXPL_ALGORITHM_START = Messages.getString("ebnf",
	"WordAlgo.Explanation_AlgorithmStart");
	private static String EXPL_CONTINUEINDIAGRAM = Messages.getString("ebnf",
	"WordAlgo.Explanation_ContinueDiagram");
	private static String EXPL_TERMINAL = Messages.getString("ebnf",
	"WordAlgo.Explanation_Terminal");
	private static String EXPL_VARIABLE = Messages.getString("ebnf",
	"WordAlgo.Explanation_Variable");
	private static String EXPL_JUMPTODIAGRAM = Messages.getString("ebnf",
	"WordAlgo.Explanation_JumpToDiagram");
	private static String EXPL_LEAVEDIAGRAMM_WITHADRESS = Messages.getString("ebnf",
	"WordAlgo.Explanation_LeaveDiagramWithAdress");
	private static String EXPL_RETURNTODIAGRAM = Messages.getString("ebnf",
	"WordAlgo.Explanation_ReturnToDiagram");
	
	private static String WARNING_TERMINAL = Messages.getString("ebnf",
	"WordAlgo.Warning_Terminal");
	private static String WARNING_JUMPTODIAGRAM = Messages.getString("ebnf",
	"WordAlgo.Warning_JumpToDiagram");
	
	SynDiaSystem synDiaSystem1;

	SynDiaSystem synDiaSystem2;

	SyntaxDiagram diaS;

	SyntaxDiagram diaA;

	WordAlgoModel myModel;

	WordAlgorithmController myController;

	Repetition rep1;

	TerminalSymbol terminalCinS;

	Variable varAinS;

	Branch bra1;

	TerminalSymbol terminalAinAleft;

	TerminalSymbol terminalBinA;

	Variable varAinA;

	TerminalSymbol terminalAinAright;

	protected void setUp() {
		// synDiaSystem1:
		// S :== {c}A
		// A :== (aAb|a)
		synDiaSystem1 = SynDiaSystemLibrary.getSynDiaSystem1();
		synDiaSystem1.removeNullElems();

		// Getting references to all SynDiaElems in System1
		// DiagramS:
		diaS = null;
		// DiagramA:
		diaA = null;
		try {
			diaS = synDiaSystem1.getSyntaxDiagram("S");
			diaA = synDiaSystem1.getSyntaxDiagram("A");
		} catch (Exception e) {
			System.out.println("Exception thrown: " + e.toString());
		}
		// DiagramS:
		rep1 = (Repetition) diaS.getRoot().getSynDiaElem(0);
		terminalCinS = (TerminalSymbol) rep1.getRight().getSynDiaElem(0);
		varAinS = (Variable) diaS.getRoot().getSynDiaElem(1);
		// DiagramA:
		bra1 = (Branch) diaA.getRoot().getSynDiaElem(0);
		terminalAinAleft = (TerminalSymbol) bra1.getLeft().getSynDiaElem(0);
		terminalBinA = (TerminalSymbol) bra1.getLeft().getSynDiaElem(2);
		varAinA = (Variable) bra1.getLeft().getSynDiaElem(1);
		terminalAinAright = (TerminalSymbol) bra1.getRight().getSynDiaElem(0);

		try {
			myModel = new WordAlgoModel(synDiaSystem1);
			myController = new WordAlgorithmController(null, null,
					synDiaSystem1);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public void testActionsWithDiagram1() {
		System.out
				.println("Begin WordAlgorithmController Test **********************************");

		// Check if each Variable got an different AdressNumber
		assertTrue(myController.getAdressNumber(varAinS) != null);
		assertTrue(myController.getAdressNumber(varAinA) != null);
		assertTrue(myController.getAdressNumber(varAinA) != myController
				.getAdressNumber(varAinS));

		// Test if all Informations are correct before starting algorithm.
		assertEquals("", myController.getWord());
		assertEquals("", myController.getOutput());
		assertEquals(EXPL_BEFORE_START,
				myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(diaS.getRoot() == myController.getPosition());
		assertTrue("[]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Set word to cccaaabb
		myController.setWord("cccaaabb");
		assertEquals("cccaaabb", myController.getWord());

		// Start Algorithm
		myController.startAlgorithm();
		assertTrue("".equals(myController.getOutput()));
		assertEquals(EXPL_ALGORITHM_START + "\n" + EXPL_CONTINUEINDIAGRAM,
				myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(rep1 == myController.getPosition());
		assertTrue("[]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Click on c in Diagram S
		myController.gotoTerminal(terminalCinS);
		// Check results
		assertTrue("c".equals(myController.getOutput()));
		assertEquals(EXPL_TERMINAL + "\n" + EXPL_CONTINUEINDIAGRAM,
				myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(rep1.getLeft() == myController.getPosition());
		assertTrue("[]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Click on A in Diagram S
		myController.gotoVariable(varAinS);
		// Check results
		assertTrue("c".equals(myController.getOutput()));
		assertEquals(EXPL_VARIABLE, myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(varAinS == myController.getPosition());
		System.out.println(myController.getAdressNumbersFromStack());
		assertTrue("[1]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Undo Click on Var A
		try {
			myController.undo();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// Check results
		assertTrue("c".equals(myController.getOutput()));
		assertEquals(EXPL_TERMINAL + "\n" + EXPL_CONTINUEINDIAGRAM,
				myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(rep1.getLeft() == myController.getPosition());
		assertTrue("[]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Click on c in Diagram S
		myController.gotoTerminal(terminalCinS);
		// Check results
		assertTrue("cc".equals(myController.getOutput()));
		assertEquals(EXPL_TERMINAL + "\n" + EXPL_CONTINUEINDIAGRAM,
				myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(rep1.getLeft() == myController.getPosition());
		assertTrue("[]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Click on c in Diagram S
		myController.gotoTerminal(terminalCinS);
		// Check results
		assertTrue("ccc".equals(myController.getOutput()));
		assertEquals(EXPL_TERMINAL + "\n" + EXPL_CONTINUEINDIAGRAM,
				myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(rep1.getLeft() == myController.getPosition());
		assertTrue("[]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Click on c in Diagram S
		myController.gotoTerminal(terminalCinS);
		// Check results
		assertTrue("cccc".equals(myController.getOutput()));
		assertEquals(EXPL_TERMINAL + "\n" + EXPL_CONTINUEINDIAGRAM,
				myController.getExplanation());
		assertEquals(
				WARNING_TERMINAL, myController.getWarning());
		assertTrue(rep1.getLeft() == myController.getPosition());
		assertTrue("[]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Undo Click on c in S
		try {
			myController.undo();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// Check results
		assertTrue("ccc".equals(myController.getOutput()));
		assertEquals(EXPL_TERMINAL + "\n" + EXPL_CONTINUEINDIAGRAM,
				myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(rep1.getLeft() == myController.getPosition());
		assertTrue("[]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Redo Click on c in S
		try {
			myController.redo();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// Check results
		assertTrue("cccc".equals(myController.getOutput()));
		assertEquals(EXPL_TERMINAL + "\n" + EXPL_CONTINUEINDIAGRAM,
				myController.getExplanation());
		assertEquals(WARNING_TERMINAL, myController.getWarning());
		assertTrue(rep1.getLeft() == myController.getPosition());
		assertTrue("[]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Undo Click on c in S
		try {
			myController.undo();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// Check results
		assertTrue("ccc".equals(myController.getOutput()));
		assertEquals(EXPL_TERMINAL + "\n" + EXPL_CONTINUEINDIAGRAM,
				myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(rep1.getLeft() == myController.getPosition());
		assertTrue("[]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Click on a in A
		// Model will not change because a in A in unreachable!
		myController.gotoTerminal(terminalAinAleft);
		// Check results
		assertTrue("ccc".equals(myController.getOutput()));
		assertEquals(EXPL_TERMINAL + "\n" + EXPL_CONTINUEINDIAGRAM,
				myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(rep1.getLeft() == myController.getPosition());
		assertTrue("[]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Click on A in Diagram S
		myController.gotoVariable(varAinS);
		// Check results
		assertTrue("ccc".equals(myController.getOutput()));
		assertEquals(EXPL_VARIABLE, myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(varAinS == myController.getPosition());
		System.out.println(myController.getAdressNumbersFromStack());
		assertTrue("[1]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Return to A in Diagram S
		// Model will not change because S was not already left.
		myController.returnToDiagram(varAinS);
		// Check results
		assertTrue("ccc".equals(myController.getOutput()));
		assertEquals(EXPL_VARIABLE, myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(varAinS == myController.getPosition());
		System.out.println(myController.getAdressNumbersFromStack());
		assertTrue("[1]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// JumpToDiagram S
		// The model will warn
		myController.jumpToDiagram(diaS);
		// Check results
		assertTrue("ccc".equals(myController.getOutput()));
		assertEquals(EXPL_VARIABLE, myController.getExplanation());
		assertEquals(WARNING_JUMPTODIAGRAM,
				myController.getWarning());
		assertTrue(varAinS == myController.getPosition());
		System.out.println(myController.getAdressNumbersFromStack());
		assertTrue("[1]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// JumpToDiagramA
		myController.jumpToDiagram(diaA);
		// Check results
		assertTrue("ccc".equals(myController.getOutput()));
		assertEquals(EXPL_JUMPTODIAGRAM, myController.getExplanation());
		assertEquals("", myController.getWarning());
		System.out.println("Actual position: " + myController.getPosition());
		assertTrue(diaA.getRoot().getSynDiaElem(0) == myController.getPosition());
		System.out.println(myController.getAdressNumbersFromStack());
		assertTrue("[1]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Click on a in Diagram A right part of Branch
		myController.gotoTerminal(terminalAinAright);
		// Check results
		assertTrue("ccca".equals(myController.getOutput()));
		assertEquals(EXPL_TERMINAL + "\n" + EXPL_LEAVEDIAGRAMM_WITHADRESS,
				myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(bra1 == myController.getPosition());
		assertTrue("[1]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Undo click on a in Diagram A right part of Branch
		try {
			myController.undo();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// Check results
		assertTrue("ccc".equals(myController.getOutput()));
		assertEquals(EXPL_JUMPTODIAGRAM, myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(diaA.getRoot().getSynDiaElem(0) == myController.getPosition());
		System.out.println(myController.getAdressNumbersFromStack());
		assertTrue("[1]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Click on a in Diagram A left part of Branch
		myController.gotoTerminal(terminalAinAleft);
		// Check results
		assertTrue("ccca".equals(myController.getOutput()));
		assertEquals(EXPL_TERMINAL + "\n" + EXPL_CONTINUEINDIAGRAM,
				myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(varAinA == myController.getPosition());
		assertTrue("[1]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Click on A in Diagram A
		myController.gotoVariable(varAinA);
		// Check results
		assertTrue("ccca".equals(myController.getOutput()));
		assertEquals(EXPL_VARIABLE, myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(varAinA == myController.getPosition());
		System.out.println(myController.getAdressNumbersFromStack());
		assertTrue("[1, 2]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// JumpToDiagramA
		myController.jumpToDiagram(diaA);
		// Check results
		assertTrue("ccca".equals(myController.getOutput()));
		assertEquals(EXPL_JUMPTODIAGRAM, myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(diaA.getRoot().getSynDiaElem(0) == myController.getPosition());
		System.out.println(myController.getAdressNumbersFromStack());
		assertTrue("[1, 2]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Click on a in Diagram A left part of Branch
		myController.gotoTerminal(terminalAinAleft);
		// Check results
		assertTrue("cccaa".equals(myController.getOutput()));
		assertEquals(EXPL_TERMINAL+ "\n" + EXPL_CONTINUEINDIAGRAM,
				myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(varAinA == myController.getPosition());
		assertTrue("[1, 2]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Click on A in Diagram A
		myController.gotoVariable(varAinA);
		// Check results
		assertTrue("cccaa".equals(myController.getOutput()));
		assertEquals(EXPL_VARIABLE, myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(varAinA == myController.getPosition());
		System.out.println(myController.getAdressNumbersFromStack());
		assertTrue("[1, 2, 2]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// JumpToDiagramA
		myController.jumpToDiagram(diaA);
		// Check results
		assertTrue("cccaa".equals(myController.getOutput()));
		assertEquals(EXPL_JUMPTODIAGRAM, myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(diaA.getRoot().getSynDiaElem(0) == myController.getPosition());
		System.out.println(myController.getAdressNumbersFromStack());
		assertTrue("[1, 2, 2]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Click on a in Diagram A right part of Branch
		myController.gotoTerminal(terminalAinAright);
		// Check results
		assertTrue("cccaaa".equals(myController.getOutput()));
		assertEquals(EXPL_TERMINAL + "\n" + EXPL_LEAVEDIAGRAMM_WITHADRESS,
				myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(bra1 == myController.getPosition());
		assertTrue("[1, 2, 2]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Return to diagram A
		myController.returnToDiagram(varAinA);
		// Check results
		assertTrue("cccaaa".equals(myController.getOutput()));
		assertEquals(EXPL_RETURNTODIAGRAM + "\n" + EXPL_CONTINUEINDIAGRAM,
				myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(terminalBinA == myController.getPosition());
		assertTrue("[1, 2]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Click on b in Diagram A
		myController.gotoTerminal(terminalBinA);
		// Check results
		assertTrue("cccaaab".equals(myController.getOutput()));
		assertEquals(EXPL_TERMINAL + "\n" + EXPL_LEAVEDIAGRAMM_WITHADRESS,
				myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(bra1 == myController.getPosition());
		assertTrue("[1, 2]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Return to diagram A
		myController.returnToDiagram(varAinA);
		// Check results
		assertTrue("cccaaab".equals(myController.getOutput()));
		assertEquals(EXPL_RETURNTODIAGRAM + "\n" + EXPL_CONTINUEINDIAGRAM,
				myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(terminalBinA == myController.getPosition());
		assertTrue("[1]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Click on b in Diagram A
		myController.gotoTerminal(terminalBinA);
		// Check results
		assertTrue("cccaaabb".equals(myController.getOutput()));
		assertEquals(EXPL_TERMINAL + "\n" + EXPL_LEAVEDIAGRAMM_WITHADRESS,
				myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(bra1 == myController.getPosition());
		assertTrue("[1]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Return to diagram S
		myController.returnToDiagram(varAinS);
		// Check results
		assertTrue("cccaaabb".equals(myController.getOutput()));
		assertEquals(EXPL_RETURNTODIAGRAM,
				myController.getExplanation());
		//assertEquals(EXPL_LEAVEDIAGRAMM_FINISHWITHSUCCESS, myController.getWarning());
		assertTrue(varAinS == myController.getPosition());
		assertTrue("[]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		System.out
				.println("End WordAlgorithmController Test ************************************");
	}

	public void testReset() {
		System.out
				.println("Begin WordAlgorithmControllerReset Test ************************************");
		// Start Algorithm
		myController.startAlgorithm();
		assertTrue("".equals(myController.getOutput()));
		assertEquals(EXPL_ALGORITHM_START + "\n" + EXPL_CONTINUEINDIAGRAM,
				myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(diaS.getRoot().getSynDiaElem(0) == myController.getPosition());
		assertTrue("[]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Click on c in Diagram S
		myController.gotoTerminal(terminalCinS);
		// Check results
		assertTrue("c".equals(myController.getOutput()));
		assertEquals(EXPL_TERMINAL + "\n" + EXPL_CONTINUEINDIAGRAM,
				myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(rep1.getLeft() == myController.getPosition());
		assertTrue("[]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Click on A in Diagram S
		myController.gotoVariable(varAinS);
		// Check results
		assertTrue("c".equals(myController.getOutput()));
		assertEquals(EXPL_VARIABLE, myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(varAinS == myController.getPosition());
		System.out.println(myController.getAdressNumbersFromStack());
		assertTrue("[1]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Undo Click on Var A
		try {
			myController.undo();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// Check results
		assertTrue("c".equals(myController.getOutput()));
		assertEquals(EXPL_TERMINAL  + "\n" + EXPL_CONTINUEINDIAGRAM,
				myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(rep1.getLeft() == myController.getPosition());
		assertTrue("[]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Reset algorithm
		// The test perform the same Actions again.
		// That must work!
		try {
			myController.resetAlgorithm();
		} catch (Exception e) {

		}
		// Test if all Informations are correct before starting algorithm.
		assertEquals("", myController.getWord());
		assertEquals("", myController.getOutput());
		assertEquals(EXPL_BEFORE_START,
				myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(diaS.getRoot() == myController.getPosition());
		assertTrue("[]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Start Algorithm
		myController.startAlgorithm();
		assertTrue("".equals(myController.getOutput()));
		assertEquals(EXPL_ALGORITHM_START + "\n" + EXPL_CONTINUEINDIAGRAM,
				myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(diaS.getRoot().getSynDiaElem(0) == myController.getPosition());
		assertTrue("[]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Click on c in Diagram S
		myController.gotoTerminal(terminalCinS);
		// Check results
		assertTrue("c".equals(myController.getOutput()));
		assertEquals(EXPL_TERMINAL + "\n" + EXPL_CONTINUEINDIAGRAM,
				myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(rep1.getLeft() == myController.getPosition());
		assertTrue("[]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Click on A in Diagram S
		myController.gotoVariable(varAinS);
		// Check results
		assertTrue("c".equals(myController.getOutput()));
		assertEquals(EXPL_VARIABLE, myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(varAinS == myController.getPosition());
		System.out.println(myController.getAdressNumbersFromStack());
		assertTrue("[1]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		// Undo Click on Var A
		try {
			myController.undo();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// Check results
		assertTrue("c".equals(myController.getOutput()));
		assertEquals(EXPL_TERMINAL + "\n" + EXPL_CONTINUEINDIAGRAM,
				myController.getExplanation());
		assertEquals("", myController.getWarning());
		assertTrue(rep1.getLeft() == myController.getPosition());
		assertTrue("[]".equals(myController.getAdressNumbersFromStack()
				.toString()));
		printStatus();

		System.out
				.println("End WordAlgorithmControllerReset Test ************************************");
	}

	private void printStatus() {
//		System.out.println();
//		System.out.println("Word:         " + myController.getWord());
//		System.out.println("Output:       " + myController.getOutput());
//		System.out.println("Explanation : " + myController.getExplanation());
//		System.out.println("Warning:      " + myController.getWarning());
//		System.out.println("Position:     " + myController.getPosition());
//		System.out.println("Vars on Stack:"
//				+ myController.getAdressNumbersFromStack().toString());
//		System.out.println();
	}
}
