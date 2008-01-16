package org.jalgo.module.pulsemem.core;

import java.util.ArrayList;
import java.util.List;
import c00.AST;
import c00.AST.*;

/**
 * This class generates a formated C00-Code from the AST
 *
 * @author Joachim Protze
 */
public class CodeGenerator {
	private int TabLevel;
	private String output="";
	private String outputWithRM="";
	private int ruecksprungMarke=1;
	private String tabulator="  ";
	private List<Integer> breakpoints = new ArrayList<Integer>();

	/**
	 * Generates formated Code from the AST
	 * @param TabLevel
	 */
	public CodeGenerator(int TabLevel){
		this.TabLevel=TabLevel;
	}

	/**
	 * Generates formated Code from the AST
	 *
	 */
	public CodeGenerator(){
		this(0);
	}

	/**
	 * set tabulator-string (default "  " - 2 blankspaces)
	 * @param tabulator
	 */
	public void setTabulator(String tabulator) {
		this.tabulator = tabulator;
	}

	/**
	 * prints TabLevel*2 blackspaces
	 * @param TabLevel
	 */
	private void printTab(int TabLevel){
		for (int i=0; i<TabLevel; i++)
			myPrint(tabulator);
	}

	/**
	 * prints the formated code w/o linenumbers to the commandline
	 *
	 */
	public void realPrint(){
		System.out.print(output);
	}

	public List<Integer> getBreakpoints(){
		return breakpoints;
	}

	/**
	 * returns formated code w/o linenumbers
	 * @return
	 */
	public String getOutput(){
		return output;
	}

	/**
	 * returns formated code w/o linenumbers
	 * @return
	 */
	public String getOutputWithRM(){
		return outputWithRM;
	}

	/**
	 * returns formated code with linenumbers
	 * @return formated code with linenumbers
	 */
	public String getNumberedOutput(){
		String[] tmp=output.split("\n");
		String ret="";
		for (int i=1; i<=tmp.length; i++){
			if (i<10){ret += " ";}
			if (i<100){ret += " ";}
			ret+=i+" "+tmp[i-1]+"\n";
		}
		return ret;
	}

	/**
	 * returns formated code with linenumbers
	 * @return formated code with linenumbers
	 */
	public String getNumberedOutputWithRM(){
		String[] tmp=outputWithRM.split("\n");
		String ret="";
		for (int i=1; i<=tmp.length; i++){
			if (i<10){ret += " ";}
			if (i<100){ret += " ";}
			ret+=i+" "+tmp[i-1]+"\n";
		}
		return ret;
	}

	/**
	 * collects the formated code in the output-variable
	 * @param string
	 */
	private void myPrint(String string) {
		myPrintWithRM(string);
		output += string;
	}

	/**
	 * collects the formated code in the output-variable
	 * @param string
	 */
	private void myPrintWithRM(String string) {
		outputWithRM += string;
	}

	/**
	 * differentiate between the possible Program-instances
	 * builds formated code from the commited ASTTree
	 * @param prog
	 */
	public void SwitchProgram(AST.Program prog){
		HandleExpandProgram((AST.ExpandProgram)prog);
	}

	/**
	 * differentiate between the possible GlobalDeclarations-instances
	 * builds formated code from the commited ASTTree
	 * @param decls
	 */
	public void SwitchGlobalDeclarations(AST.GlobalDeclarations decls) {
		if (decls instanceof AST.VarConstGlobDecls){
			HandleVarConstGlobDecls((AST.VarConstGlobDecls)decls);
		}
		if (decls instanceof AST.FuncGlobDecls){
			HandleFuncGlobDecls((AST.FuncGlobDecls)decls);
		}
		if (decls instanceof AST.EmptyGlobDecls){
			HandleEmptyGlobDecls((AST.EmptyGlobDecls)decls);
		}
	}

	/**
	 * differentiate between the possible Declarations-instances
	 * builds formated code from the commited ASTTree
	 * @param decls
	 */
	public void SwitchDeclarations(Declarations decls) {
		if (decls instanceof AST.EmptyDecls){
			HandleEmptyDecls((AST.EmptyDecls)decls);
		}
		if (decls instanceof AST.PairDecls){
			HandlePairDecls((AST.PairDecls)decls);
		}
	}

	/**
	 * differentiate between the possible Declaration-instances
	 * builds formated code from the commited ASTTree
	 * @param decl
	 */
	public void SwitchDeclaration(Declaration decl) {
		if (decl instanceof AST.VarDecl){
			HandleVarDecl((AST.VarDecl)decl);
		}
		if (decl instanceof AST.ConstDecl){
			HandleConstDecl((AST.ConstDecl)decl);
		}
	}

	/**
	 * differentiate between the possible ConstDeclarations-instances
	 * builds formated code from the commited ASTTree
	 * @param constDecls
	 */
	public void SwitchConstDeclarations(ConstDeclarations constDecls) {
		if (constDecls instanceof AST.LastConstDecls){
			HandleLastConstDecls((AST.LastConstDecls)constDecls);
		}
		if (constDecls instanceof AST.PairConstDecls){
			HandlePairConstDecls((AST.PairConstDecls)constDecls);
		}
	}

	/**
	 * differentiate between the possible VarDeclarations-instances
	 * builds formated code from the commited ASTTree
	 * @param varDecls
	 */
	public void SwitchVarDeclarations(VarDeclarations varDecls) {
		if (varDecls instanceof AST.InitVarLastDecls){
			HandleInitVarLastDecl((AST.InitVarLastDecls)varDecls);
		}
		if (varDecls instanceof AST.InitVarPairDecls){
			HandleInitVarPairDecls((AST.InitVarPairDecls)varDecls);
		}
		if (varDecls instanceof AST.RefVarLastDecls){
			HandleRefVarLastDecls((AST.RefVarLastDecls)varDecls);
		}
		if (varDecls instanceof AST.RefVarPairDecls){
			HandleRefVarPairDecls((AST.RefVarPairDecls)varDecls);
		}
		if (varDecls instanceof AST.VarLastDecls){
			HandleVarLastDecls((AST.VarLastDecls)varDecls);
		}
		if (varDecls instanceof AST.VarPairDecls){
			HandleVarPairDecls((AST.VarPairDecls)varDecls);
		}
	}

	/**
	 * differentiate between the possible FunctionImplementations-instances
	 * builds formated code from the commited ASTTree
	 * @param funcImpl
	 */
	public void SwitchFunctionImplementations(FunctionImplementations funcImpl) {
		if (funcImpl instanceof AST.EmptyFuncImpls){
			HandleEmptyFuncImpls((AST.EmptyFuncImpls)funcImpl);
		}
		if (funcImpl instanceof AST.PairFuncImpls){
			HandlePairFuncImpls((AST.PairFuncImpls)funcImpl);
		}
	}

	/**
	 * differentiate between the possible FunctionHeading-instances
	 * builds formated code from the commited ASTTree
	 * @param funcHead
	 */
	public void SwitchFunctionHeading(FunctionHeading funcHead) {
		if (funcHead instanceof AST.VoidFuncHead){
			HandleVoidFuncHead((AST.VoidFuncHead)funcHead);
		}
		if (funcHead instanceof AST.IntFuncHead){
			HandleIntFuncHead((AST.IntFuncHead)funcHead);
		}
	}

	/**
	 * differentiate between the possible ParamSections-instances
	 * builds formated code from the commited ASTTree
	 * @param prmSects
	 */
	public void SwitchParamSection(ParamSections prmSects) {
		if (prmSects instanceof AST.ValueLastPrmSects){
			HandleValueLastPrmSects((AST.ValueLastPrmSects)prmSects);
		}
		if (prmSects instanceof AST.RefLastPrmSects){
			HandleRefLastPrmSects((AST.RefLastPrmSects)prmSects);
		}
		if (prmSects instanceof AST.ValuePairPrmSects){
			HandleValuePairPrmSects((AST.ValuePairPrmSects)prmSects);
		}
		if (prmSects instanceof AST.RefPairPrmSects){
			HandleRefPairPrmSects((AST.RefPairPrmSects)prmSects);
		}
	}

	/**
	 * differentiate between the possible FormalParameters-instances
	 * builds formated code from the commited ASTTree
	 * @param formParam
	 */
	public void SwitchFormalParameters(FormalParameters formParam) {
		if (formParam instanceof AST.EmptyFormalParams){
			HandleEmptyFormalParams((AST.EmptyFormalParams)formParam);
		}
		if (formParam instanceof AST.VoidFormalParams){
			HandleVoidFormalParams((AST.VoidFormalParams)formParam);
		}
		if (formParam instanceof AST.NonVoidFormalParams){
			HandleNonVoidFormalParams((AST.NonVoidFormalParams)formParam);
		}
	}

	/**
	 * differentiate between the possible Block-instances
	 * builds formated code from the commited ASTTree
	 * @param block
	 */
	public void SwitchBlock(Block block) {
		HandleExpandBlock((ExpandBlock)block);
	}

	/**
	 * differentiate between the possible StatementSequence-instances
	 * builds formated code from the commited ASTTree
	 * @param stmSeq
	 */
	public void SwitchStatementSequence(StatementSequence stmSeq) {
		if (stmSeq instanceof AST.EmptyStmSeq){
			HandleEmptyStmSeq((AST.EmptyStmSeq)stmSeq);
		}
		if (stmSeq instanceof AST.PairStmSeq){
			HandlePairStmSeq((AST.PairStmSeq)stmSeq);
		}
	}

	/**
	 * differentiate between the possible Statement-instances
	 * builds formated code from the commited ASTTree
	 * @param stm
	 */
	public void SwitchStatement(Statement stm) {
		printTab(TabLevel);
		if (stm instanceof AST.AssignStm){
			HandleAssignStm((AST.AssignStm)stm);
		}
		if (stm instanceof AST.PureIfStm){
			HandlePureIfStm((AST.PureIfStm)stm);
		}
		if (stm instanceof AST.ElseIfStm){
			HandleElseIfStm((AST.ElseIfStm)stm);
		}
		if (stm instanceof AST.SwitchStm){
			HandleSwitchStm((AST.SwitchStm)stm);
		}
		if (stm instanceof AST.WhileStm){
			HandleWhileStm((AST.WhileStm)stm);
		}
		if (stm instanceof AST.DoWhileStm){
			HandleDoWhileStm((AST.DoWhileStm)stm);
		}
		if (stm instanceof AST.ForStm){
			HandleForStm((AST.ForStm)stm);
		}
		if (stm instanceof AST.ContinueStm){
			HandleContinueStm((AST.ContinueStm)stm);
		}
		if (stm instanceof AST.BreakStm){
			HandleBreakStm((AST.BreakStm)stm);
		}
		if (stm instanceof AST.CompStm){
			HandleCompStm((AST.CompStm)stm);
		}
		if (stm instanceof AST.FuncCallStm){
			HandleFuncCallStm((AST.FuncCallStm)stm);
		}
		if (stm instanceof AST.EmptyReturnStm){
			HandleEmptyReturnStm((AST.EmptyReturnStm)stm);
		}
		if (stm instanceof AST.ExprReturnStm){
			HandleExprReturnStm((AST.ExprReturnStm)stm);
		}
		if (stm instanceof AST.ExprPrintfStm){
			HandleExprPrintfStm((AST.ExprPrintfStm)stm);
		}
		if (stm instanceof AST.ScanfStm){
			HandleScanfStm((AST.ScanfStm)stm);
		}
		if (stm instanceof AST.LabelStm){
			HandleLabelStm((AST.LabelStm)stm);
		}
	}

	/**
	 * differentiate between the possible Assignment-instances
	 * builds formated code from the commited ASTTree
	 * @param assign
	 */
	public void SwitchAssignment(Assignment assign) {
		if (assign instanceof AST.ValueAssign){
			HandleValueAssign((AST.ValueAssign)assign);
		}
		if (assign instanceof AST.RefAssign){
			HandleRefAssign((AST.RefAssign)assign);
		}
	}

	/**
	 * differentiate between the possible SwitchBlock-instances
	 * builds formated code from the commited ASTTree
	 * @param switchBlock
	 */
	public void SwitchSwitchblock(SwitchBlock switchBlock) {
		if (switchBlock instanceof AST.NoDefaultSwitchBlock){
			HandleNoDefaultSwitchBlock((AST.NoDefaultSwitchBlock)switchBlock);
		}
		if (switchBlock instanceof AST.DefaultSwitchBlock){
			HandleDefaultSwitchBlock((AST.DefaultSwitchBlock)switchBlock);
		}
	}

	/**
	 * differentiate between the possible CaseSequence-instances
	 * builds formated code from the commited ASTTree
	 * @param caseSeq
	 */
	public void SwitchCaseSequence(CaseSequence caseSeq) {
		if (caseSeq instanceof AST.EmptyCaseSeq){
			HandleEmptyCaseSeq((AST.EmptyCaseSeq)caseSeq);
		}
		if (caseSeq instanceof AST.NoBreakPairCaseSeq){
			HandleNoBreakPairCaseSeq((AST.NoBreakPairCaseSeq)caseSeq);
		}
	}

	/**
	 * differentiate between the possible FunctionCall-instances
	 * builds formated code from the commited ASTTree
	 * @param funcCall
	 */
	public void SwitchFunctionCall(FunctionCall funcCall){
		HandleExpandFunctionCall((ExpandFuncCall)funcCall);
	}

	/**
	 * differentiate between the possible ActualParameters-instances
	 * builds formated code from the commited ASTTree
	 * @param actParams
	 */
	public void SwitchActualParameters(ActualParameters actParams) {
		if (actParams instanceof AST.EmptyActParams){
			HandleEmptyActParams((AST.EmptyActParams)actParams);
		}
		if (actParams instanceof AST.ExprListActParams){
			HandleExprListActParams((AST.ExprListActParams)actParams);
		}
	}

	/**
	 * differentiate between the possible ExpressionList-instances
	 * builds formated code from the commited ASTTree
	 * @param exprList
	 */
	public void SwitchExpressionList(ExpressionList exprList) {
		if (exprList instanceof AST.LastExprList){
			HandleLastExprList((AST.LastExprList)exprList);
		}
		if (exprList instanceof AST.PairExprList){
			HandlePairExprList((AST.PairExprList)exprList);
		}
	}

	/**
	 * differentiate between the possible Expression-instances
	 * builds formated code from the commited ASTTree
	 * @param exp
	 */
	public void SwitchExpression(Expression exp) {
		if (exp instanceof AST.PlusExpr){
			HandlePlusExpr((AST.PlusExpr)exp);
		}
		if (exp instanceof AST.MinusExpr){
			HandleMinusExpr((AST.MinusExpr)exp);
		}
		if (exp instanceof AST.FirstTermExpr){
			HandleFirstTermExpr((AST.FirstTermExpr)exp);
		}
	}

	/**
	 * differentiate between the possible FirstTerm-instances
	 * builds formated code from the commited ASTTree
	 * @param firstTerm
	 */
	public void SwitchFirstTerm(FirstTerm firstTerm) {
		if (firstTerm instanceof AST.MultFirstTerm){
			HandleMultFirstTerm((AST.MultFirstTerm)firstTerm);
		}
		if (firstTerm instanceof AST.DivFirstTerm){
			HandleDivFirstTerm((AST.DivFirstTerm)firstTerm);
		}
		if (firstTerm instanceof AST.ModFirstTerm){
			HandleModFirstTerm((AST.ModFirstTerm)firstTerm);
		}
		if (firstTerm instanceof AST.FactorFirstTerm){
			HandleFactorFirstTerm((AST.FactorFirstTerm)firstTerm);
		}
		if (firstTerm instanceof AST.PlusFactorFirstTerm){
			HandlePlusFactorFirstTerm((AST.PlusFactorFirstTerm)firstTerm);
		}
		if (firstTerm instanceof AST.MinusFactorFirstTerm){
			HandleMinusFactorFirstTerm((AST.MinusFactorFirstTerm)firstTerm);
		}
	}

	/**
	 * differentiate between the possible Term-instances
	 * builds formated code from the commited ASTTree
	 * @param term
	 */
	public void SwitchTerm(Term term) {
		if (term instanceof AST.MultTerm){
			HandleMultTerm((AST.MultTerm)term);
		}
		if (term instanceof AST.DivTerm){
			HandleDivTerm((AST.DivTerm)term);
		}
		if (term instanceof AST.ModTerm){
			HandleModTerm((AST.ModTerm)term);
		}
		if (term instanceof AST.FactorTerm){
			HandleFactorTerm((AST.FactorTerm)term);
		}
	}

	/**
	 * differentiate between the possible Factor-instances
	 * builds formated code from the commited ASTTree
	 * @param factor
	 */
	public void SwitchFactor(Factor factor) {
		if (factor instanceof AST.IdFactor){
			HandleIdFactor((AST.IdFactor)factor);
		}
		if (factor instanceof AST.RefIdFactor){
			HandleRefIdFactor((AST.RefIdFactor)factor);
		}
		if (factor instanceof AST.AddIdFactor){
			HandleAddIdFactor((AST.AddIdFactor)factor);
		}
		if (factor instanceof AST.NumFactor){
			HandleNumFactor((AST.NumFactor)factor);
		}
		if (factor instanceof AST.FuncCallFactor){
			HandleFuncCallFactor((AST.FuncCallFactor)factor);
		}
		if (factor instanceof AST.CompFactor){
			HandleCompFactor((AST.CompFactor)factor);
		}
	}

	/**
	 * differentiate between the possible BoolExpression-instances
	 * builds formated code from the commited ASTTree
	 * @param boolExp
	 */
	public void SwitchBoolExpression(BoolExpression boolExp) {
		if (boolExp instanceof AST.EqBoolExpr){
			HandleEqBoolExpr((AST.EqBoolExpr)boolExp);
		}
		if (boolExp instanceof AST.NotEqBoolExpr){
			HandleNotEqBoolExpr((AST.NotEqBoolExpr)boolExp);
		}
		if (boolExp instanceof AST.LessBoolExpr){
			HandleLessBoolExpr((AST.LessBoolExpr)boolExp);
		}
		if (boolExp instanceof AST.GreatBoolExpr){
			HandleGreatBoolExpr((AST.GreatBoolExpr)boolExp);
		}
		if (boolExp instanceof AST.LessEqBoolExpr){
			HandleLessEqBoolExpr((AST.LessEqBoolExpr)boolExp);
		}
		if (boolExp instanceof AST.GreatEqBoolExpr){
			HandleGreatEqBoolExpr((AST.GreatEqBoolExpr)boolExp);
		}
	}

	private void HandleExpandProgram(AST.ExpandProgram code){
		myPrint("#include <stdio.h>\n");
		SwitchGlobalDeclarations(code.globDecl);
		SwitchFunctionImplementations(code.funcImpl);
		myPrint("\nint main (");
		SwitchFormalParameters(code.formParam);
		myPrint(")\n");
		SwitchBlock(code.block);
		myPrint("\n");
	}

	private void HandleEmptyGlobDecls(EmptyGlobDecls decls) {
//		myPrint(" /*epsilon*/ ");
	}

	private void HandleFuncGlobDecls(FuncGlobDecls decls) {
		SwitchFunctionHeading(decls.funcHead);
		myPrint(";\n");
		SwitchGlobalDeclarations(decls.globDecls);
	}

	private void HandleVarConstGlobDecls(VarConstGlobDecls decls) {
		SwitchDeclaration(decls.decl);
		SwitchGlobalDeclarations(decls.globDecls);
	}

	private void HandleEmptyDecls(EmptyDecls decls) {
//		myPrint(" /*epsilon*/ ");
	}

	private void HandlePairDecls(PairDecls decls) {
		SwitchDeclaration(decls.decl);
		SwitchDeclarations(decls.decls);
	}

	private void HandleVarDecl(VarDecl decl) {
		printTab(TabLevel);
		myPrint("int ");
		SwitchVarDeclarations(decl.varDecls);
		myPrint(";\n");
	}

	private void HandleConstDecl(ConstDecl decl) {
		printTab(TabLevel);
		myPrint("const int ");
		SwitchConstDeclarations(decl.constDecls);
		myPrint(";\n");
	}

	private void HandlePairConstDecls(PairConstDecls decls) {
		myPrint(decls.ident + " = " + decls.number + ", ");
		SwitchConstDeclarations(decls.constDecls);
	}

	private void HandleLastConstDecls(LastConstDecls decls) {
		myPrint(decls.ident + " = " + decls.number);
	}

	private void HandleVarPairDecls(VarPairDecls decls) {
		myPrint(decls.ident+", ");
		SwitchVarDeclarations(decls.varDecls);
	}

	private void HandleVarLastDecls(VarLastDecls decls) {
		myPrint(decls.ident);
	}

	private void HandleRefVarPairDecls(RefVarPairDecls decls) {
		myPrint("*" + decls.ident + ", ");
		SwitchVarDeclarations(decls.varDecls);
	}

	private void HandleRefVarLastDecls(RefVarLastDecls decls) {
		myPrint("*" + decls.ident);
	}

	private void HandleInitVarPairDecls(InitVarPairDecls decls) {
		myPrint(decls.ident + " = " + decls.number + ", ");
		SwitchVarDeclarations(decls.varDecls);
	}

	private void HandleInitVarLastDecl(InitVarLastDecls decls) {
		myPrint(decls.ident + " = " + decls.number);
	}

	private void HandlePairFuncImpls(PairFuncImpls impls) {
		myPrint("\n");
		SwitchFunctionHeading(impls.funcHead);
		myPrint("\n");
		SwitchBlock(impls.block);
		SwitchFunctionImplementations(impls.funcImpls);
	}

	private void HandleEmptyFuncImpls(EmptyFuncImpls impls) {
//		myPrint(" /*epsilon*/ ");
	}

	private void HandleIntFuncHead(IntFuncHead head) {
		myPrint("int " + head.ident + " (");
		SwitchFormalParameters(head.formParams);
		myPrint(")");
	}

	private void HandleVoidFuncHead(VoidFuncHead head) {
		myPrint("void " + head.ident + " (");
		SwitchFormalParameters(head.formParams);
		myPrint(")");
	}

	private void HandleEmptyFormalParams(EmptyFormalParams params) {
//		myPrint(" /*epsilon*/ ");
	}

	private void HandleVoidFormalParams(VoidFormalParams params) {
		myPrint(" void ");
	}

	private void HandleNonVoidFormalParams(NonVoidFormalParams params) {
		SwitchParamSection(params.prmSects);
	}

	private void HandleValueLastPrmSects(ValueLastPrmSects sects) {
		myPrint("int "+sects.ident);
	}

	private void HandleRefLastPrmSects(RefLastPrmSects sects) {
		myPrint("int *"+sects.ident);
	}

	private void HandleValuePairPrmSects(ValuePairPrmSects sects) {
		myPrint("int "+sects.ident+", ");
		SwitchParamSection(sects.prmSects);
	}

	private void HandleRefPairPrmSects(RefPairPrmSects sects) {
		myPrint("int *"+sects.ident+", ");
		SwitchParamSection(sects.prmSects);
	}

	private void HandleExpandBlock(ExpandBlock block) {
		printTab(TabLevel);
		myPrint("{\n");
		TabLevel++;
		SwitchDeclarations(block.decls);
		SwitchStatementSequence(block.stmSeq);
		TabLevel--;
		printTab(TabLevel);
		myPrint("}\n");
	}

	private void HandlePairStmSeq(PairStmSeq seq) {
		SwitchStatement(seq.stm);
		SwitchStatementSequence(seq.stmSeq);
	}

	private void HandleEmptyStmSeq(EmptyStmSeq seq) {
//		myPrint(" /*epsilon*/ ");
	}

	private void HandleAssignStm(AssignStm stm) {
		SwitchAssignment(stm.assign);
		myPrint(";\n");
	}

	private void HandlePureIfStm(PureIfStm stm) {
		myPrint("if (");
		SwitchBoolExpression(stm.boolExp);
		myPrint(")\n");
		if (!(stm.stm instanceof AST.CompStm)) TabLevel++;
		SwitchStatement(stm.stm);
		if (!(stm.stm instanceof AST.CompStm)) TabLevel--;
	}

	private void HandleElseIfStm(ElseIfStm stm) {
		myPrint("if (");
		SwitchBoolExpression(stm.boolExp);
		myPrint(")\n");
		if (!(stm.stm1 instanceof AST.CompStm)) TabLevel++;
		SwitchStatement(stm.stm1);
		if (!(stm.stm1 instanceof AST.CompStm)) TabLevel--;
		printTab(TabLevel);
		myPrint("else\n");
		if (!(stm.stm2 instanceof AST.CompStm)) TabLevel++;
		SwitchStatement(stm.stm2);
		if (!(stm.stm2 instanceof AST.CompStm)) TabLevel--;
	}

	private void HandleSwitchStm(SwitchStm stm) {
		myPrint("switch (");
		SwitchExpression(stm.exp);
		myPrint(")\n");
		SwitchSwitchblock(stm.switchBlock);
	}

	private void HandleWhileStm(WhileStm stm) {
		myPrint("while (");
		SwitchBoolExpression(stm.boolExp);
		myPrint(")\n");
		if (!(stm.stm instanceof AST.CompStm)) TabLevel++;
		SwitchStatement(stm.stm);
		if (!(stm.stm instanceof AST.CompStm)) TabLevel--;
	}

	private void HandleDoWhileStm(DoWhileStm stm) {
		myPrint("do \n");
		if (!(stm.stm instanceof AST.CompStm)) TabLevel++;
		SwitchStatement(stm.stm);
		if (!(stm.stm instanceof AST.CompStm)) TabLevel--;
		myPrint(" while (");
		SwitchBoolExpression(stm.boolExp);
		myPrint(")\n");
	}

	private void HandleForStm(ForStm stm) {
		myPrint("for (");
		SwitchAssignment(stm.assign1);
		myPrint("; ");
		SwitchBoolExpression(stm.boolExp);
		myPrint("; ");
		SwitchAssignment(stm.assign2);
		myPrint(")\n");
		if (!(stm.stm instanceof AST.CompStm)) TabLevel++;
		SwitchStatement(stm.stm);
		if (!(stm.stm instanceof AST.CompStm)) TabLevel--;
	}

	private void HandleContinueStm(ContinueStm stm) {
		myPrint("continue; \n");
	}

	private void HandleBreakStm(BreakStm stm) {
		myPrint("break; \n");
	}

	private void HandleCompStm(CompStm stm) {
//		printTab(TabLevel);
		myPrint("{\n");
		TabLevel++;
		SwitchDeclarations(stm.decls);
//		myPrint(" ");
		SwitchStatementSequence(stm.stmSeq);
		TabLevel--;
		printTab(TabLevel);
		myPrint("}\n");
	}

	private void HandleFuncCallStm(FuncCallStm stm) {
		SwitchFunctionCall(stm.funcCall);
		myPrint("; \n");
	}

	private void HandleEmptyReturnStm(EmptyReturnStm stm) {
		myPrint("return; \n");
	}

	private void HandleExprReturnStm(ExprReturnStm stm) {
		myPrint("return ");
		SwitchExpression(stm.expr);
		myPrint(";\n");
	}

	private void HandleExprPrintfStm(ExprPrintfStm stm) {
		myPrint("printf(\"" + stm.string + "\", ");
		this.SwitchExpressionList(stm.exprList);
		myPrint(");\n");
	}

	private void HandleScanfStm(ScanfStm stm) {
		myPrint("scanf(\"%i\", &" + stm.ident + ");\n");
	}

	private void HandleLabelStm(LabelStm stm) {
		breakpoints.add(stm.startLine);
		myPrint("/*label " + stm.number + "*/\n");
	}

	private void HandleValueAssign(ValueAssign assign) {
		myPrint(assign.ident +" = ");
		SwitchExpression(assign.expr);
	}

	private void HandleRefAssign(RefAssign assign) {
		myPrint("*" + assign.ident +" = ");
		SwitchExpression(assign.expr);
	}



	private void HandleNoDefaultSwitchBlock(NoDefaultSwitchBlock block) {
		printTab(TabLevel);
		myPrint("{\n");
		TabLevel++;
		SwitchCaseSequence(block.caseSeq);
		TabLevel--;
		printTab(TabLevel);
		myPrint("}\n");
	}

	private void HandleDefaultSwitchBlock(DefaultSwitchBlock block) {
		printTab(TabLevel);
		myPrint("{\n");
		TabLevel++;
		SwitchCaseSequence(block.caseSeq);
		myPrint("default: \n");
		SwitchStatementSequence(block.stmSeq);
		TabLevel--;
		printTab(TabLevel);
		myPrint("}\n");
	}

	private void HandleEmptyCaseSeq(EmptyCaseSeq seq) {
//		myPrint("/*epsilon*/");
	}

	private void HandleNoBreakPairCaseSeq(NoBreakPairCaseSeq seq) {
		myPrint("case "+ seq.number + ": \n");
		SwitchStatementSequence(seq.stmSeq);
		SwitchCaseSequence(seq.caseSeq);
	}

	private void HandleExpandFunctionCall(ExpandFuncCall funcCall) {
		myPrint(funcCall.ident + "(");
		SwitchActualParameters(funcCall.actParams);
		myPrint(")");
		myPrintWithRM("/* $"+ruecksprungMarke+" */");
		funcCall.extra=ruecksprungMarke;
		ruecksprungMarke++;
	}


	private void HandleEmptyActParams(EmptyActParams params) {
//		myPrint("/*epsilon*/");
	}

	private void HandleExprListActParams(ExprListActParams params) {
		SwitchExpressionList(params.exprList);
	}

	private void HandleLastExprList(LastExprList list) {
		SwitchExpression(list.expr);
	}

	private void HandlePairExprList(PairExprList list) {
		SwitchExpression(list.expr);
		myPrint(", ");
		SwitchExpressionList(list.exprList);
	}

	private void HandlePlusExpr(PlusExpr expr) {
		SwitchExpression(expr.expr);
		myPrint(" + ");
		SwitchTerm(expr.term);
	}

	private void HandleMinusExpr(MinusExpr expr) {
		SwitchExpression(expr.expr);
		myPrint(" - ");
		SwitchTerm(expr.term);
	}

	private void HandleFirstTermExpr(FirstTermExpr expr) {
		SwitchFirstTerm(expr.firstTerm);
	}

	private void HandleMultFirstTerm(MultFirstTerm term) {
		SwitchFirstTerm(term.firstTerm);
		myPrint(" * ");
		SwitchFactor(term.factor);
	}

	private void HandleDivFirstTerm(DivFirstTerm term) {
		SwitchFirstTerm(term.firstTerm);
		myPrint(" / ");
		SwitchFactor(term.factor);
	}

	private void HandleModFirstTerm(ModFirstTerm term) {
		SwitchFirstTerm(term.firstTerm);
		myPrint(" % ");
		SwitchFactor(term.factor);
	}

	private void HandleFactorFirstTerm(FactorFirstTerm term) {
		SwitchFactor(term.factor);
	}

	private void HandlePlusFactorFirstTerm(PlusFactorFirstTerm term) {
		myPrint("+");
		SwitchFactor(term.factor);
	}

	private void HandleMinusFactorFirstTerm(MinusFactorFirstTerm term) {
		myPrint("-");
		SwitchFactor(term.factor);
	}

	private void HandleMultTerm(MultTerm term) {
		SwitchTerm(term.term);
		myPrint(" * ");
		SwitchFactor(term.factor);
	}

	private void HandleDivTerm(DivTerm term) {
		SwitchTerm(term.term);
		myPrint(" / ");
		SwitchFactor(term.factor);
	}

	private void HandleModTerm(ModTerm term) {
		SwitchTerm(term.term);
		myPrint(" % ");
		SwitchFactor(term.factor);
	}

	private void HandleFactorTerm(FactorTerm term) {
		SwitchFactor(term.factor);
	}

	private void HandleIdFactor(IdFactor factor) {
		myPrint(factor.ident);
	}

	private void HandleRefIdFactor(RefIdFactor factor) {
		myPrint("*" + factor.ident);
	}

	private void HandleAddIdFactor(AddIdFactor factor) {
		myPrint("&" + factor.ident);
	}

	private void HandleNumFactor(NumFactor factor) {
		myPrint(""+factor.number);
	}

	private void HandleFuncCallFactor(FuncCallFactor factor) {
		SwitchFunctionCall(factor.funcCall);
	}

	private void HandleCompFactor(CompFactor factor) {
		myPrint("(");
		SwitchExpression(factor.expr);
		myPrint(")");
	}

	private void HandleEqBoolExpr(EqBoolExpr expr) {
		SwitchExpression(expr.expr1);
		myPrint(" == ");
		SwitchExpression(expr.expr2);
	}

	private void HandleNotEqBoolExpr(NotEqBoolExpr expr) {
		SwitchExpression(expr.expr1);
		myPrint(" != ");
		SwitchExpression(expr.expr2);
	}

	private void HandleLessBoolExpr(LessBoolExpr expr) {
		SwitchExpression(expr.expr1);
		myPrint(" < ");
		SwitchExpression(expr.expr2);
	}

	private void HandleGreatBoolExpr(GreatBoolExpr expr) {
		SwitchExpression(expr.expr1);
		myPrint(" > ");
		SwitchExpression(expr.expr2);
	}

	private void HandleLessEqBoolExpr(LessEqBoolExpr expr) {
		SwitchExpression(expr.expr1);
		myPrint(" <= ");
		SwitchExpression(expr.expr2);
	}

	private void HandleGreatEqBoolExpr(GreatEqBoolExpr expr) {
		SwitchExpression(expr.expr1);
		myPrint(" >= ");
		SwitchExpression(expr.expr2);
	}


}
