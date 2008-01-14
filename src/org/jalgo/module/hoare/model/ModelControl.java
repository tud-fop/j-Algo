package org.jalgo.module.hoare.model;

import java.awt.Dimension;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import org.jalgo.module.hoare.control.ProgramControl;
import org.jalgo.module.hoare.gui.GuiControl;

import c00.AST;

// import org.antlr.runtime.RecognitionException;

/**
 * Central Unit of the model
 * 
 * 
 * @author Gerald
 * 
 */

public class ModelControl extends Observable {

	private GuiControl guiControl;

	private ProgramControl programControl;

	private String code;

	private VerificationFormula root;

	private Map<Integer, VerificationFormula> nodeMap;

	public ModelControl() {
		code = "";
		nodeMap = new HashMap<Integer, VerificationFormula>();
	}

	/**
	 * returns the VerificationFormula with the given id
	 * 
	 * @param id
	 * @return the VerificationFormula with the given id
	 */
	public VerificationFormula getVF(int id) {

		if (nodeMap.containsKey(id))
			return nodeMap.get(id);
		return null;
	}

	/**
	 * returns the parentVF of a given VF
	 * 
	 * @param vf
	 *            the current VerificationFormula
	 * @return the parentVF
	 */
	public VerificationFormula getPreviousVF(VerificationFormula vf) {
		VerificationFormula currentNode = root;

		if (vf == null)
			return null;

		Set<Integer> keys = nodeMap.keySet();
		for (Integer key : keys) {
			currentNode = nodeMap.get(key);
			if (currentNode.getChildren().contains(vf))
				return currentNode;
		}

		return null;
	}

	public void setChanged() {
		super.setChanged();

	}

	/**
	 * updates the NodeMap
	 */

	public void updateNodeMap() {

		nodeMap.clear();
		if (root != null)
			nodeMap = root.collectNodes(nodeMap);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String sourceCode) {
		this.code = sourceCode;
	}

	public VerificationFormula getRoot() {
		return root;
	}

	public void setRoot(VerificationFormula root) {
		this.root = root;
		updateNodeMap();
		setChanged();
	}

	public Map<Integer, VerificationFormula> getNodeMap() {
		return nodeMap;
	}

	public void setNodeMap(Map<Integer, VerificationFormula> nodeMap) {
		this.nodeMap = nodeMap;
	}

	public GuiControl getGuiControl() {
		return guiControl;
	}

	public void setGuiControl(GuiControl guiControl) {
		this.guiControl = guiControl;
	}

	public ProgramControl getProgramControl() {
		return programControl;
	}

	public void setProgramControl(ProgramControl programControl) {
		this.programControl = programControl;
	}

	/**
	 * builds a VF-data structure of an AST-Tree
	 * 
	 * @param tree
	 *            the AST-Baum
	 * @param source
	 *            the associated sourcecode as a string
	 * @return the root VerificationFormula of the VF-data strucure
	 */
	public VerificationFormula createVF(AST.StatementSequence tree,
			String source) {
		code = source + System.getProperty("line.separator");
		root = createVerificationFormula(tree, new DummyAssertion(),
				new DummyAssertion(), null);
		updateNodeMap();
		setChanged();
		return root;
	}

	private VerificationFormula createVerificationFormula(
			AST.StatementSequence tree, Assertion pre, Assertion post,
			VerificationFormula parent) {
		VerificationFormula result = null;
		if (tree instanceof AST.PairStmSeq) {
			if (((AST.PairStmSeq) tree).stmSeq instanceof AST.EmptyStmSeq)
				result = createVerificationFormula(((AST.PairStmSeq) tree).stm,
						pre, post, parent);
			else {
				result = new StatSeqVF(pre, post, parent);
				result.setCodeStart(new Dimension(tree.startColumn,
						tree.startLine));
				result.setCodeEnd(new Dimension(tree.endColumn, tree.endLine));

				Assertion dummy = new DummyAssertion();
				VerificationFormula child = createVerificationFormula(
						((AST.PairStmSeq) tree).stm, pre, dummy, result);
				if (child != null)
					result.addChild(child);
				child = createVerificationFormula(
						((AST.PairStmSeq) tree).stmSeq, dummy, post, result);
				if (child != null)
					result.addChild(child);
			}

		}
		return result;
	}

	private VerificationFormula createVerificationFormula(AST.Statement tree,
			Assertion pre, Assertion post, VerificationFormula parent) {
		VerificationFormula result = null;
		String label = null;

		if (tree instanceof AST.AssignStm) // da refAssign nich erlaubt, gleich
		// ValueAssign
		{
			label = "x = r;";
			result = new AssignVF(pre, post, parent);
		} else if (tree instanceof AST.PureIfStm) {
			String PI = getPI(((AST.PureIfStm) tree).boolExp);
			label = "if(" + PI + ")";
			result = new IfVF(pre, post, parent);
			PI = PI.replaceAll("==", "=");
			PI = PI.replaceAll("\\s", "");
			((IfVF) result).setPi(new ConcreteAssertion(PI));
			VerificationFormula child = createVerificationFormula(
					((AST.PureIfStm) tree).stm, (new AndAssertion(pre,
							((IfVF) result).getPi())), post, result);
			if (child != null)
				result.addChild(child);
		} else if (tree instanceof AST.ElseIfStm) {
			String PI = getPI(((AST.ElseIfStm) tree).boolExp);
			label = "if (" + PI + ") ... else ... ";
			result = new IfElseVF(pre, post, parent);
			PI = PI.replaceAll("==", "=");
			PI = PI.replaceAll("\\s", "");
			((IfElseVF) result).setPi(new ConcreteAssertion(PI));
			VerificationFormula child = createVerificationFormula(
					((AST.ElseIfStm) tree).stm1, (new AndAssertion(pre,
							((IfElseVF) result).getPi())), post, result);
			if (child != null)
				result.addChild(child);
			child = createVerificationFormula(((AST.ElseIfStm) tree).stm2,
					(new AndAssertion(pre, new NotAssertion(((IfElseVF) result)
							.getPi()))), post, result);
			if (child != null)
				result.addChild(child);
		} else if (tree instanceof AST.WhileStm) {
			String PI = getPI(((AST.WhileStm) tree).boolExp);
			label = "while (" + PI + ")";
			result = new IterationVF(pre, post, parent);
			PI = PI.replaceAll("==", "=");
			PI = PI.replaceAll("\\s", "");
			((IterationVF) result).setPi(new ConcreteAssertion(PI));
			VerificationFormula child = createVerificationFormula(
					((AST.WhileStm) tree).stm, (new AndAssertion(pre,
							((IterationVF) result).getPi())), pre, result);
			if (child != null)
				result.addChild(child);
		} else if (tree instanceof AST.CompStm) {
			label = "{..}";
			result = new CompoundVF(pre, post, parent);
			VerificationFormula child = createVerificationFormula(
					((AST.CompStm) tree).stmSeq, pre, post, result); // Declarations
			// ignored!
			if (child != null)
				result.addChild(child);
		}

		if (result == null)
			return null;

		result.setCodeStart(new Dimension(tree.startColumn, tree.startLine));
		result.setCodeEnd(new Dimension(tree.endColumn, tree.endLine));

		return result;
	}

	/**
	 * 
	 * @param codePosition
	 *            the code position in the source code file
	 * @return index in the code-string
	 */
	public int getIndex(Dimension codePosition) {
		int index = 0; // Index im String
		// solange i < Zeilenzahl, suche nächsten Zeilenumbruch:
		for (int i = 0; i < (int) codePosition.getHeight() - 1; i++) {
			index = code.indexOf(System.getProperty("line.separator"),
					index + 1); // System.getProperty("line.separator").length();
		}

		index += codePosition.getWidth();
		if (codePosition.getHeight() > 1)
			index += System.getProperty("line.separator").length();

		return index;
	}

	/**
	 * 
	 * @param id
	 *            id of the VF
	 * @return the associated segment of the source code
	 */
	public String getCodeSegment(int id) {
		VerificationFormula vf = getVF(id); // hole zugehörige VFx

		return code.substring(getIndex(vf.getCodeStart()), getIndex(vf
				.getCodeEnd()) + 1);
	}

	/**
	 * 
	 * @param bool
	 *            a BoolExpression of the AST-tree
	 * @return BoolExpression as a string
	 */
	public String getPI(AST.BoolExpression bool) {
		return code.substring(getIndex(new Dimension(bool.startColumn,
				bool.startLine)), getIndex(new Dimension(bool.endColumn,
				bool.endLine)) + 1);
	}

	@Override
	public void notifyObservers() {
		this.updateNodeMap();
		this.setChanged();
		super.notifyObservers();
	}

	public void load(ObjectInputStream ios) throws IOException,
			ClassNotFoundException {
		int rootID = ios.readInt();
		nodeMap = (HashMap<Integer, VerificationFormula>) ios.readObject();
		root = nodeMap.get(rootID);
		code = (String) ios.readObject();
		VerificationFormula.setNextID(ios.readInt());
		this.setChanged();
	}

	public void save(ObjectOutputStream oos) throws IOException {
		oos.writeInt(root.getID());
		oos.writeObject(nodeMap);
		oos.writeObject(code);
		oos.writeInt(VerificationFormula.getNextID());
	}

}
