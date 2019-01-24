package org.jalgo.module.pulsemem.core;

import c00.AST;
import c00.AST.CaseSequence;
import c00.AST.DefaultSwitchBlock;
import c00.AST.EmptyCaseSeq;
import c00.AST.NoBreakPairCaseSeq;
import c00.AST.NoDefaultSwitchBlock;
import c00.AST.SwitchBlock;

/**
 * This class is for handling switchblocks 
 *
 * @author jooschi
 */
public class SwitchHandler {

	private int switchValue;
	private SwitchBlock switchBlock;
	private myInterpreter interpreter; 
	private Boolean run=false;
	private Boolean useDefault=true;
	
	/**
	 * Handles a C00-switchblock. 
	 * @param switchValue is the value of the expression, the cases are tested against
	 * @param switchBlock is the AST.switchBlock from the ASTTree.
	 * @param interpreter is the instanciating Interpreter, to interprete the leafes of the case-sequences
	 */
	public SwitchHandler(int switchValue, SwitchBlock switchBlock, myInterpreter interpreter){
		this.switchValue=switchValue;
		this.switchBlock=switchBlock;
		this.interpreter=interpreter;
	}
	
	/**
	 * invoke the Switchhandler
	 *
	 */
	public void callSwitch(){
		SwitchSwitchBlock(switchBlock);
	}

	/**
	 * differentiate between the possible switchBlocks
	 * @param switchBlock
	 * @return null
	 */
	private Object SwitchSwitchBlock(SwitchBlock switchBlock) {
		if (switchBlock instanceof AST.NoDefaultSwitchBlock){
			interpretNoDefaultSwitchBlock((AST.NoDefaultSwitchBlock)switchBlock);
		}
		if (switchBlock instanceof AST.DefaultSwitchBlock){
			interpretDefaultSwitchBlock((AST.DefaultSwitchBlock)switchBlock);
		}
		return null;
	}

	/**
	 * differentiate between the possible caseSequences
	 * @param caseSeq
	 * @return
	 */
	private Object SwitchCaseSequence(CaseSequence caseSeq) {
		if (caseSeq instanceof AST.EmptyCaseSeq){
			interpretEmptyCaseSeq((AST.EmptyCaseSeq)caseSeq);
		}
		if (caseSeq instanceof AST.NoBreakPairCaseSeq){
			interpretNoBreakPairCaseSeq((AST.NoBreakPairCaseSeq)caseSeq);
		}
		return null;
	}

	/**
	 * execute the switchBlock w/o default
	 * @param block
	 */
	private void interpretNoDefaultSwitchBlock(NoDefaultSwitchBlock block) {
		SwitchCaseSequence(block.caseSeq);
	}

	/**
	 * execute the switchBlock with default
	 * @param block
	 */
	private void interpretDefaultSwitchBlock(DefaultSwitchBlock block) {
		SwitchCaseSequence(block.caseSeq);
		if (useDefault || run)
			interpreter.interpret(block.stmSeq);
	}

	/**
	 * 
	 * @param seq
	 */
	private void interpretEmptyCaseSeq(EmptyCaseSeq seq) {
	}

	/**
	 * 
	 * @param seq
	 */
	private void interpretNoBreakPairCaseSeq(NoBreakPairCaseSeq seq) {
		if (switchValue==seq.number){
			useDefault=false;
			run=true;
		}
		if (run==true){
			interpreter.interpret(seq.stmSeq);
			if (interpreter.isBreakFlag())
				run=false;
		}
		SwitchCaseSequence(seq.caseSeq);
	}
}
