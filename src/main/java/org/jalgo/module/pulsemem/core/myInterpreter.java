package org.jalgo.module.pulsemem.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jalgo.module.pulsemem.core.exceptions.ECycleLimitReached;
import org.jalgo.module.pulsemem.core.exceptions.EIncompatibleFunctionHeaders;
import org.jalgo.module.pulsemem.core.exceptions.EIncompatibleParameterCount;
import org.jalgo.module.pulsemem.core.exceptions.EPrintfException;
import org.jalgo.module.pulsemem.core.exceptions.EReturnException;
import org.jalgo.module.pulsemem.core.exceptions.ETypeExpected;
import org.jalgo.module.pulsemem.core.exceptions.EUndefinedFunctionCalled;
import org.jalgo.module.pulsemem.core.exceptions.EUserAbort;
import c00.AST;
import c00.AST.*;


/**
 * This class is the core of the C00-Interpreter
 *
 * It interprets the given syntax tree and provides a pulsing memory chart.
 * <code>myInterpreter i = new myInterpreter();
 * i.runInterpreter(prog);
 * System.out.print(pml.toString());</code>
 *
 * @author Joachim Protze
 */
public class myInterpreter {
	private static int MAX_CYCLES = 1000000;

	private boolean continueFlag=false;
	private boolean breakFlag=false;
	private boolean returnFlag=false;
	private List<PulsMemLine> pm;
	private Map<String,VarInfo> vi;
	private List<Integer> ruecksprungMarken=new ArrayList<Integer>();
	private Stack stack;
	private List<FunctionHandler> functions;
	private IOSimulation ios;
	private int cyclus=0;
	private boolean aborting = false;

	/**
	 *
	 */
	public myInterpreter() {
		pm=new ArrayList<PulsMemLine>();
		vi=new HashMap<String,VarInfo>();
//		ruecksprungMarken=new ArrayList<Integer>();
		functions=new ArrayList<FunctionHandler>();
		stack=new Stack();
		ios=new CliIOSimulation();
	}

	/**
	 * let the Interpreter interpret a C00-program in form of a AST
	 * @param program is instance of AST.Program
	 */
	public void runInterpreter(Program program){
		clear();
		interpret(program);
	}

	/**
	 * returns an Object of type null, Boolean, int/Integer, List<Integer>, Variable, List<Variable>
	 * or List<Object> (with Object in {Integer, Variable})
	 * @param ast is leaf of the AST
	 * @return Object of type null, Boolean, int/Integer, List<Integer>, Variable, List<Variable> or List<Object> (with Object in {Integer, Variable})
	 */
	public Object interpret(AST.ASTTree ast){
		if (aborting)
			throw new EUserAbort();

		cyclus++;
		if (cyclus>MAX_CYCLES)
			throw new ECycleLimitReached(MAX_CYCLES);

		if (ast instanceof AST.Program){
			return SwitchProgram((AST.Program)ast);
		}
		if (ast instanceof AST.GlobalDeclarations){
			return SwitchGlobalDeclarations((AST.GlobalDeclarations)ast);
		}
		if (ast instanceof AST.Declarations){
			return SwitchDeclarations((AST.Declarations)ast);
		}
		if (ast instanceof AST.Declaration){
			return SwitchDeclaration((AST.Declaration)ast);
		}
		if (ast instanceof AST.ConstDeclarations){
			return SwitchConstDeclarations((AST.ConstDeclarations)ast);
		}
		if (ast instanceof AST.VarDeclarations){
			return SwitchVarDeclarations((AST.VarDeclarations)ast);
		}
		if (ast instanceof AST.FunctionImplementations){
			return SwitchFunctionImplementations((AST.FunctionImplementations)ast);
		}
		if (ast instanceof AST.FunctionHeading){
			return SwitchFunctionHeading((AST.FunctionHeading)ast);
		}
		if (ast instanceof AST.FormalParameters){
			return SwitchFormalParameters((AST.FormalParameters)ast);
		}
		if (ast instanceof AST.ParamSections){
			return SwitchParamSections((AST.ParamSections)ast);
		}
		if (ast instanceof AST.Block){
			return SwitchBlock((AST.Block)ast);
		}
		if (ast instanceof AST.StatementSequence){
			return SwitchStatementSequence((AST.StatementSequence)ast);
		}
		if (ast instanceof AST.Statement){
			return SwitchStatement((AST.Statement)ast);
		}
		if (ast instanceof AST.Assignment){
			return SwitchAssignment((AST.Assignment)ast);
		}
		if (ast instanceof AST.FunctionCall){
			return SwitchFunctionCall((AST.FunctionCall)ast);
		}
		if (ast instanceof AST.ActualParameters){
			return SwitchActualParameters((AST.ActualParameters)ast);
		}
		if (ast instanceof AST.ExpressionList){
			return SwitchExpressionList((AST.ExpressionList)ast);
		}
		if (ast instanceof AST.Expression){
			return SwitchExpression((AST.Expression)ast);
		}
		if (ast instanceof AST.FirstTerm){
			return SwitchFirstTerm((AST.FirstTerm)ast);
		}
		if (ast instanceof AST.Term){
			return SwitchTerm((AST.Term)ast);
		}
		if (ast instanceof AST.Factor){
			return SwitchFactor((AST.Factor)ast);
		}
		if (ast instanceof AST.BoolExpression){
			return SwitchBoolExpression((AST.BoolExpression)ast);
		}
		return null;
	}

	/**
	 * register your own I/O-Simulation
	 * @param ios
	 */
	public void setIOSimulation(IOSimulation ios){
		this.ios=ios;
	}


	/**
	 * sets returnFlag
	 * @param flag
	 */
	public void setReturnFlag(boolean flag) {
		returnFlag=flag;
	}

	/**
	 * returns the resulting pulsing memory as a list of pulsing memory lines (PulsMemLine)
	 * @return the resulting pulsing memory as a list of pulsing memory lines (PulsMemLine
	 */
	public List<PulsMemLine> getPm(){
		return pm;
	}

	/**
	 * deletes all PMLines (called when new C-File is loaded)
	*/

	public void clearPm() {
		pm=new ArrayList<PulsMemLine>();
	}

	/**
	 * returns the enclosing block (Block,Program,CompoundStatement,FunctionImplementation)
	 * @param var
	 * @return the enclosing block (Block,Program,CompoundStatement,FunctionImplementation)
	 */
	private ASTTree getParentBlock(ASTTree var) {
		if (var.parent instanceof AST.Block || var.parent instanceof AST.Program || var.parent instanceof AST.Statement )
			return var.parent;
		if (var.parent instanceof AST.PairFuncImpls )
			return ((PairFuncImpls)var.parent).block;

		return getParentBlock(var.parent);
	}

	/**
	 * returns the enclosing block (Block,Program,CompoundStatement,FunctionImplementation)
	 * @param var
	 * @return the enclosing block (Block,Program,CompoundStatement,FunctionImplementation)
	 */
	private boolean isIntFunction(ASTTree var) {
		if (var.parent instanceof AST.PairFuncImpls || var.parent instanceof AST.Program){
			return !(var.parent instanceof VoidFuncHead);
		}
		return isIntFunction(var.parent);
	}

	/**
	 * returns the Boolean value of the breakFlag - it signals, that an
	 * break-Stm was found and the end of the loop is not reached yet.
	 * @return Boolean
	 */
	public boolean isBreakFlag() {
		return breakFlag;
	}

	/**
	 * returns returnFlag
	 * @return returnFlag
	 */
	public boolean isReturnFlag() {
		return returnFlag;
	}

	/**
	 * returns the value of the simulated C00-IO-Call scanf("%i",&ident);
	 * @return int
	 */
	private int myScanf(int line) {
		return ios.input(line);
	}

	/**
	 * Handles the C00-IO-Call printf("%d",ident)
	 * @param string
	 */
	private void myPrintf(String value, int line) {
		ios.output(value, line);
	}

	/**
	 * cleanup before executing a new program
	 *
	 */
	private void clear() {
		pm=new ArrayList<PulsMemLine>();
		vi=new HashMap<String,VarInfo>();
		ruecksprungMarken=new ArrayList<Integer>();
		functions=new ArrayList<FunctionHandler>();
		stack=new Stack();
		continueFlag=false;
		breakFlag=false;
		returnFlag=false;
	}

	/**
	 * returns a copy of the actual RM-Stack
	 * @return a copy of the actual RM-Stack
	 */
	private List<Integer> cloneRM(){
		List<Integer> tmp = new ArrayList<Integer>();
		for (int i : ruecksprungMarken){
			tmp.add(i);
		}
		return tmp;
	}

	/**
	 * creates a new PM-line
	 * @param line
	 */
	private void newPmLine(int line){
		pm.add(new PulsMemLine(line,cloneRM(),stack.cloneStack(), stack.getVisibilityLevel()));
	}

	/**
	 * creates a new PM-label-line
	 * @param line
	 */
	private void newPmLine(int line, int label){
		pm.add(new PulsMemLine(line,cloneRM(),stack.cloneStack(), stack.getVisibilityLevel(), label));
	}

	/**
	 * returns the VarInfo-Object for the declared var. Creates one if not already exists.
	 * @param var
	 * @return the VarInfo-Object for the declared var. Creates one if not already exists.
	 */
	private VarInfo searchForVarinfo(ASTTree var){
		ASTTree block= getParentBlock(var);
		VarInfo ret;
		if (vi.containsKey(var.startLine+"_"+var.startColumn)){
			ret=vi.get(var.startLine+"_"+var.startColumn);
			ret.addPos(new Integer [] {var.startLine,var.startColumn});
		}else{
			int offset = 0;
			if ((var instanceof AST.RefLastPrmSects) || (var instanceof AST.RefPairPrmSects))
				offset = "int *".length();
			else if ((var instanceof AST.ValueLastPrmSects) || (var instanceof AST.ValuePairPrmSects))
				offset = "int ".length();
			
			ret = new VarInfo(var.startLine,block.endLine,new Integer [] {var.startLine,var.startColumn + offset});
			vi.put(var.startLine+"_"+block.endLine, ret);
		}
		return ret;
	}

	/**
	 * returns a Functionhandler selected by name from the funtions-List
	 * @param name
	 * @return FunctionHandler
	 */
	private FunctionHandler getFunctionByName(String name){
		for (FunctionHandler i : functions){
			if (i.getName().equals(name))
				return i;
		}
		return null;
	}

	/**
	 * returns a Variable selected by name from the visible stack
	 * @param name
	 * @return Variable
	 */
	private Variable getVariableByName(String name){
		for (Variable i : stack.getVisibleStack()){
			if (i.getName().equals(name))
				return i;
		}
		return null;
	}

	/**
	 * returns null-Object
	 * @param prog
	 * @return null
	 */
	private Object SwitchProgram(AST.Program prog){
		interpretExpandProgram((AST.ExpandProgram)prog);
		return null;
	}

	/**
	 * returns declared Variables as List<Variable>
	 * @param decls
	 * @return List<Variable>
	 */
	private Object SwitchGlobalDeclarations(AST.GlobalDeclarations decls) {
		if (decls instanceof AST.VarConstGlobDecls){
			return interpretVarConstGlobDecls((AST.VarConstGlobDecls)decls);
		}
		if (decls instanceof AST.FuncGlobDecls){
			return interpretFuncGlobDecls((AST.FuncGlobDecls)decls);
		}
		if (decls instanceof AST.EmptyGlobDecls){
			return interpretEmptyGlobDecls((AST.EmptyGlobDecls)decls);
		}
		return null;
	}

	/**
	 * returns declared Variables as List<Variable>
	 * @param decls
	 * @return List<Variable>
	 */
	private Object SwitchDeclarations(Declarations decls) {
		if (decls instanceof AST.EmptyDecls){
			return interpretEmptyDecls((AST.EmptyDecls)decls);
		}
		if (decls instanceof AST.PairDecls){
			return interpretPairDecls((AST.PairDecls)decls);
		}
		return null;
	}

	/**
	 * returns declared Variables as List<Variable>
	 * @param decl
	 * @return List<Variable>
	 */
	private Object SwitchDeclaration(Declaration decl) {
		if (decl instanceof AST.VarDecl){
			return interpretVarDecl((AST.VarDecl)decl);
		}
		if (decl instanceof AST.ConstDecl){
			return interpretConstDecl((AST.ConstDecl)decl);
		}
		return null;
	}

	/**
	 * returns declared Const-Variables as List<Variable>
	 * @param constDecls
	 * @return List<Variable>
	 */
	private Object SwitchConstDeclarations(ConstDeclarations constDecls) {
		if (constDecls instanceof AST.LastConstDecls){
			return interpretLastConstDecls((AST.LastConstDecls)constDecls);
		}
		if (constDecls instanceof AST.PairConstDecls){
			return interpretPairConstDecls((AST.PairConstDecls)constDecls);
		}
		return null;
	}

	/**
	 * returns declared Variables as List<Variable>
	 * @param varDecls
	 * @return List<Variable>
	 */
	private Object SwitchVarDeclarations(VarDeclarations varDecls) {
		if (varDecls instanceof AST.InitVarLastDecls){
			return interpretInitVarLastDecl((AST.InitVarLastDecls)varDecls);
		}
		if (varDecls instanceof AST.InitVarPairDecls){
			return interpretInitVarPairDecls((AST.InitVarPairDecls)varDecls);
		}
		if (varDecls instanceof AST.RefVarLastDecls){
			return interpretRefVarLastDecls((AST.RefVarLastDecls)varDecls);
		}
		if (varDecls instanceof AST.RefVarPairDecls){
			return interpretRefVarPairDecls((AST.RefVarPairDecls)varDecls);
		}
		if (varDecls instanceof AST.VarLastDecls){
			return interpretVarLastDecls((AST.VarLastDecls)varDecls);
		}
		if (varDecls instanceof AST.VarPairDecls){
			return interpretVarPairDecls((AST.VarPairDecls)varDecls);
		}
		return null;
	}

	/**
	 * returns null-Object
	 * @param funcImpl
	 * @return null
	 */
	private Object SwitchFunctionImplementations(FunctionImplementations funcImpl) {
		if (funcImpl instanceof AST.EmptyFuncImpls){
			interpretEmptyFuncImpls((AST.EmptyFuncImpls)funcImpl);
		}
		if (funcImpl instanceof AST.PairFuncImpls){
			interpretPairFuncImpls((AST.PairFuncImpls)funcImpl);
		}
		return null;
	}

	/**
	 * returns declared Variables as List<Variable>
	 * @param funcHead
	 * @return List<Variable>
	 */
	private Object SwitchFunctionHeading(FunctionHeading funcHead) {
		if (funcHead instanceof AST.VoidFuncHead){
			return interpretVoidFuncHead((AST.VoidFuncHead)funcHead);
		}
		if (funcHead instanceof AST.IntFuncHead){
			return interpretIntFuncHead((AST.IntFuncHead)funcHead);
		}
		return null;
	}

	private Object SwitchFormalParameters(FormalParameters formParam) {
		if (formParam instanceof AST.EmptyFormalParams){
			return interpretEmptyFormalParams((AST.EmptyFormalParams)formParam);
		}
		if (formParam instanceof AST.VoidFormalParams){
			return interpretVoidFormalParams((AST.VoidFormalParams)formParam);
		}
		if (formParam instanceof AST.NonVoidFormalParams){
			return interpretNonVoidFormalParams((AST.NonVoidFormalParams)formParam);
		}
		return null;
	}

	private Object SwitchParamSections(ParamSections prmSects) {
		if (prmSects instanceof AST.ValueLastPrmSects){
			return interpretValueLastPrmSects((AST.ValueLastPrmSects)prmSects);
		}
		if (prmSects instanceof AST.RefLastPrmSects){
			return interpretRefLastPrmSects((AST.RefLastPrmSects)prmSects);
		}
		if (prmSects instanceof AST.ValuePairPrmSects){
			return interpretValuePairPrmSects((AST.ValuePairPrmSects)prmSects);
		}
		if (prmSects instanceof AST.RefPairPrmSects){
			return interpretRefPairPrmSects((AST.RefPairPrmSects)prmSects);
		}
		return null;
	}

	private Object SwitchBlock(Block block) {
		this.newPmLine(block.startLine);
		return interpretExpandBlock((ExpandBlock)block);
	}

	private Object SwitchStatementSequence(StatementSequence stmSeq) {
		if (stmSeq instanceof AST.EmptyStmSeq){
			return interpretEmptyStmSeq((AST.EmptyStmSeq)stmSeq);
		}
		if (stmSeq instanceof AST.PairStmSeq){
			return interpretPairStmSeq((AST.PairStmSeq)stmSeq);
		}
		return null;
	}

	private Object SwitchStatement(Statement stm) {
		if (continueFlag==true) return null;
		if (breakFlag==true) return null;
		if (returnFlag==true) return null;
		if (stm instanceof AST.LabelStm){
			this.newPmLine(stm.startLine, ((AST.LabelStm)stm).number);
			interpretLabelStm((AST.LabelStm)stm);
			return null;
		}
		this.newPmLine(stm.startLine);
		if (stm instanceof AST.AssignStm){
			interpretAssignStm((AST.AssignStm)stm);
		}
		if (stm instanceof AST.PureIfStm){
			interpretPureIfStm((AST.PureIfStm)stm);
		}
		if (stm instanceof AST.ElseIfStm){
			interpretElseIfStm((AST.ElseIfStm)stm);
		}
		if (stm instanceof AST.SwitchStm){
			interpretSwitchStm((AST.SwitchStm)stm);
		}
		if (stm instanceof AST.WhileStm){
			interpretWhileStm((AST.WhileStm)stm);
		}
		if (stm instanceof AST.DoWhileStm){
			interpretDoWhileStm((AST.DoWhileStm)stm);
		}
		if (stm instanceof AST.ForStm){
			interpretForStm((AST.ForStm)stm);
		}
		if (stm instanceof AST.ContinueStm){
			interpretContinueStm((AST.ContinueStm)stm);
		}
		if (stm instanceof AST.BreakStm){
			interpretBreakStm((AST.BreakStm)stm);
		}
		if (stm instanceof AST.CompStm){
			interpretCompStm((AST.CompStm)stm);
		}
		if (stm instanceof AST.FuncCallStm){
			interpretFuncCallStm((AST.FuncCallStm)stm);
		}
		if (stm instanceof AST.EmptyReturnStm){
			interpretEmptyReturnStm((AST.EmptyReturnStm)stm);
		}
		if (stm instanceof AST.ExprReturnStm){
			return interpretExprReturnStm((AST.ExprReturnStm)stm);
		}
		if (stm instanceof AST.ExprPrintfStm){
			interpretExprPrintfStm((AST.ExprPrintfStm)stm);
		}
		if (stm instanceof AST.ScanfStm){
			interpretScanfStm((AST.ScanfStm)stm);
		}
		return null;
	}

	private Object SwitchAssignment(Assignment assign) {
		if (assign instanceof AST.ValueAssign){
			interpretValueAssign((AST.ValueAssign)assign);
		}
		if (assign instanceof AST.RefAssign){
			interpretRefAssign((AST.RefAssign)assign);
		}
		return null;
	}

	private Object SwitchFunctionCall(FunctionCall funcCall){
		return interpretExpandFunctionCall((ExpandFuncCall)funcCall);
	}

	private Object SwitchActualParameters(ActualParameters actParams) {
		if (actParams instanceof AST.EmptyActParams){
			return interpretEmptyActParams((AST.EmptyActParams)actParams);
		}
		if (actParams instanceof AST.ExprListActParams){
			return interpretExprListActParams((AST.ExprListActParams)actParams);
		}
		return null;
	}

	private Object SwitchExpressionList(ExpressionList exprList) {
		if (exprList instanceof AST.LastExprList){
			return interpretLastExprList((AST.LastExprList)exprList);
		}
		if (exprList instanceof AST.PairExprList){
			return interpretPairExprList((AST.PairExprList)exprList);
		}
		return null;
	}

	private Object SwitchExpression(Expression exp) {
		if (exp instanceof AST.PlusExpr){
			return interpretPlusExpr((AST.PlusExpr)exp);
		}
		if (exp instanceof AST.MinusExpr){
			return interpretMinusExpr((AST.MinusExpr)exp);
		}
		if (exp instanceof AST.FirstTermExpr){
			return interpretFirstTermExpr((AST.FirstTermExpr)exp);
		}
		return null;
	}

	private Object SwitchFirstTerm(FirstTerm firstTerm) {
		if (firstTerm instanceof AST.MultFirstTerm){
			return interpretMultFirstTerm((AST.MultFirstTerm)firstTerm);
		}
		if (firstTerm instanceof AST.DivFirstTerm){
			return interpretDivFirstTerm((AST.DivFirstTerm)firstTerm);
		}
		if (firstTerm instanceof AST.ModFirstTerm){
			return interpretModFirstTerm((AST.ModFirstTerm)firstTerm);
		}
		if (firstTerm instanceof AST.FactorFirstTerm){
			return interpretFactorFirstTerm((AST.FactorFirstTerm)firstTerm);
		}
		if (firstTerm instanceof AST.PlusFactorFirstTerm){
			return interpretPlusFactorFirstTerm((AST.PlusFactorFirstTerm)firstTerm);
		}
		if (firstTerm instanceof AST.MinusFactorFirstTerm){
			return interpretMinusFactorFirstTerm((AST.MinusFactorFirstTerm)firstTerm);
		}
		return null;
	}

	private Object SwitchTerm(Term term) {
		if (term instanceof AST.MultTerm){
			return interpretMultTerm((AST.MultTerm)term);
		}
		if (term instanceof AST.DivTerm){
			return interpretDivTerm((AST.DivTerm)term);
		}
		if (term instanceof AST.ModTerm){
			return interpretModTerm((AST.ModTerm)term);
		}
		if (term instanceof AST.FactorTerm){
			return interpretFactorTerm((AST.FactorTerm)term);
		}
		return null;
	}

	private Object SwitchFactor(Factor factor) {
		if (factor instanceof AST.IdFactor){
			return interpretIdFactor((AST.IdFactor)factor);
		}
		if (factor instanceof AST.RefIdFactor){
			return interpretRefIdFactor((AST.RefIdFactor)factor);
		}
		if (factor instanceof AST.AddIdFactor){
			return interpretAddIdFactor((AST.AddIdFactor)factor);
		}
		if (factor instanceof AST.NumFactor){
			return interpretNumFactor((AST.NumFactor)factor);
		}
		if (factor instanceof AST.FuncCallFactor){
			return interpretFuncCallFactor((AST.FuncCallFactor)factor);
		}
		if (factor instanceof AST.CompFactor){
			return interpretCompFactor((AST.CompFactor)factor);
		}
		return null;
	}

	private Object SwitchBoolExpression(BoolExpression boolExp) {
		if (boolExp instanceof AST.EqBoolExpr){
			return interpretEqBoolExpr((AST.EqBoolExpr)boolExp);
		}
		if (boolExp instanceof AST.NotEqBoolExpr){
			return interpretNotEqBoolExpr((AST.NotEqBoolExpr)boolExp);
		}
		if (boolExp instanceof AST.LessBoolExpr){
			return interpretLessBoolExpr((AST.LessBoolExpr)boolExp);
		}
		if (boolExp instanceof AST.GreatBoolExpr){
			return interpretGreatBoolExpr((AST.GreatBoolExpr)boolExp);
		}
		if (boolExp instanceof AST.LessEqBoolExpr){
			return interpretLessEqBoolExpr((AST.LessEqBoolExpr)boolExp);
		}
		if (boolExp instanceof AST.GreatEqBoolExpr){
			return interpretGreatEqBoolExpr((AST.GreatEqBoolExpr)boolExp);
		}
		return null;
	}


	private void interpretExpandProgram(AST.ExpandProgram code){
		ArrayList<Variable> globDeclList= (ArrayList<Variable>)interpret(code.globDecl);
		for (Variable i : globDeclList){
			i.setGlobal(true);
		}
		interpret(code.funcImpl);
		ArrayList<Variable> formParamList = (ArrayList<Variable>)interpret(code.formParam);
		if (formParamList.size()>0)
			throw new EIncompatibleParameterCount("0",""+formParamList.size());
		interpret(code.block);
	}

	private Object interpretEmptyGlobDecls(EmptyGlobDecls decls) {
		return new ArrayList<Variable>();
	}

	private Object interpretFuncGlobDecls(FuncGlobDecls decls) {
		ArrayList<Variable> ret=(ArrayList<Variable>)interpret(decls.globDecls);
		functions.add(new FunctionHandler(decls.funcHead, this));
		return ret;
	}

	private Object interpretVarConstGlobDecls(VarConstGlobDecls decls) {
		ArrayList<Variable> ret=(ArrayList<Variable>)interpret(decls.globDecls);
		ret.addAll((ArrayList<Variable>)interpret(decls.decl));
		return ret;
	}

	private Object interpretEmptyDecls(EmptyDecls decls) {
		return new ArrayList<Variable>();
	}

	private Object interpretPairDecls(PairDecls decls) {
		ArrayList<Variable> ret=(ArrayList<Variable>)interpret(decls.decls);
		ret.addAll(0,(ArrayList<Variable>)interpret(decls.decl));
		return ret;
	}

	private Object interpretVarDecl(VarDecl decl) {
		return (ArrayList<Variable>)interpret(decl.varDecls);
	}

	private Object interpretConstDecl(ConstDecl decl) {
		return (ArrayList<Variable>)interpret(decl.constDecls);
	}

	private Object interpretPairConstDecls(PairConstDecls decls) {
		Variable var = new Variable(decls.ident,true, stack.getVisibilityLevel(), decls.number, searchForVarinfo(decls));
		stack.addVar(var);		
		ArrayList<Variable> list=(ArrayList<Variable>)interpret(decls.constDecls);
		list.add(0,var);
		return list;
	}

	private Object interpretLastConstDecls(LastConstDecls decls) {
		ArrayList<Variable> list=new ArrayList<Variable>();
		Variable var = new Variable(decls.ident,true, stack.getVisibilityLevel(), decls.number,searchForVarinfo(decls));
		stack.addVar(var);
		list.add(var);
		return list;
	}

	private Object interpretVarPairDecls(VarPairDecls decls) {
		Variable var = new Variable(decls.ident,false, stack.getVisibilityLevel(), searchForVarinfo(decls));
		stack.addVar(var);
		ArrayList<Variable> list=(ArrayList<Variable>)interpret(decls.varDecls);		
		list.add(0,var);
		return list;
	}

	private Object interpretVarLastDecls(VarLastDecls decls) {
		ArrayList<Variable> list=new ArrayList<Variable>();
		Variable var = new Variable(decls.ident,false, stack.getVisibilityLevel(),searchForVarinfo(decls));
		stack.addVar(var);
		list.add(var);
		return list;
	}

	private Object interpretRefVarPairDecls(RefVarPairDecls decls) {
		Variable var = new Zeiger(decls.ident, stack.getVisibilityLevel(),searchForVarinfo(decls));
		stack.addVar(var);
		ArrayList<Variable> list=(ArrayList<Variable>)interpret(decls.varDecls);
		list.add(0,var);
		return list;
	}

	private Object interpretRefVarLastDecls(RefVarLastDecls decls) {
		ArrayList<Variable> list=new ArrayList<Variable>();
		Variable var = new Zeiger(decls.ident, stack.getVisibilityLevel(),searchForVarinfo(decls));
		stack.addVar(var);
		list.add(var);
		return list;
	}

	private Object interpretInitVarPairDecls(InitVarPairDecls decls) {
		Variable var = new Variable(decls.ident,false, stack.getVisibilityLevel(), decls.number,searchForVarinfo(decls));
		stack.addVar(var);
		ArrayList<Variable> list=(ArrayList<Variable>)interpret(decls.varDecls);
		list.add(0,var);
		return list;
	}

	private Object interpretInitVarLastDecl(InitVarLastDecls decls) {
		ArrayList<Variable> list=new ArrayList<Variable>();
		Variable var = new Variable(decls.ident,false, stack.getVisibilityLevel(), decls.number, searchForVarinfo(decls));
		stack.addVar(var);
		list.add(var);
		return list;
	}

	private void interpretPairFuncImpls(PairFuncImpls impls) {
		String name;
		if (impls.funcHead instanceof AST.IntFuncHead){
			name=((IntFuncHead)impls.funcHead).ident;
		}else{
			name=((VoidFuncHead)impls.funcHead).ident;
		}
		FunctionHandler existing = getFunctionByName(name);
		if (existing == null){
			functions.add(new FunctionHandler(impls.funcHead, impls.block, this));
		}else{
			if (! existing.equalHeads(impls.funcHead))
				throw new EIncompatibleFunctionHeaders(impls.funcHead);
			existing.setBlock(impls.block);
		}
		interpret(impls.funcImpls);
	}

	private void interpretEmptyFuncImpls(EmptyFuncImpls impls) {
	}

	private Object interpretIntFuncHead(IntFuncHead head) {
		return interpret(head.formParams);
	}

	private Object interpretVoidFuncHead(VoidFuncHead head) {
		return interpret(head.formParams);
	}

	private Object interpretEmptyFormalParams(EmptyFormalParams params) {
		return new ArrayList<Variable>();
	}

	private Object interpretVoidFormalParams(VoidFormalParams params) {
		return new ArrayList<Variable>();
	}

	private Object interpretNonVoidFormalParams(NonVoidFormalParams params) {
		return interpret(params.prmSects);
	}

	private Object interpretValueLastPrmSects(ValueLastPrmSects sects) {
		ArrayList<Variable> list=new ArrayList<Variable>();
		Variable var = new Variable(sects.ident,false, stack.getVisibilityLevel(), searchForVarinfo(sects));
		stack.addVar(var);
		list.add(var);
		return list;
	}

	private Object interpretRefLastPrmSects(RefLastPrmSects sects) {
		ArrayList<Variable> list=new ArrayList<Variable>();
		Variable var = new Zeiger(sects.ident, stack.getVisibilityLevel(),searchForVarinfo(sects));
		stack.addVar(var);
		list.add(var);
		return list;
	}

	private Object interpretValuePairPrmSects(ValuePairPrmSects sects) {
		Variable var = new Variable(sects.ident,false, stack.getVisibilityLevel(),searchForVarinfo(sects));
		stack.addVar(var);
		ArrayList<Variable> list=(ArrayList<Variable>)interpret(sects.prmSects);		
		list.add(0,var);
		return list;
	}

	private Object interpretRefPairPrmSects(RefPairPrmSects sects) {
		Variable var = new Zeiger(sects.ident, stack.getVisibilityLevel(),searchForVarinfo(sects));
		stack.addVar(var);
		ArrayList<Variable> list=(ArrayList<Variable>)interpret(sects.prmSects);		
		list.add(0,var);
		return list;
	}

	private Object interpretExpandBlock(ExpandBlock block) {
		ArrayList<Variable> list=(ArrayList<Variable>)interpret(block.decls);
		Object ret = interpret(block.stmSeq);
		for (Variable i : list){
			stack.removeVar(i);
		}
		return ret;
	}

	private Object interpretPairStmSeq(PairStmSeq seq) {
		Integer ret1,ret2,ret=null;
		ret1= (Integer)interpret(seq.stm);
		ret2= (Integer)interpret(seq.stmSeq);
		if (ret1!=null)
			ret=ret1;
		if (ret2!=null)
			ret=ret2;
		return ret;
	}

	private Object interpretEmptyStmSeq(EmptyStmSeq seq) {
		return null;
	}

	private void interpretAssignStm(AssignStm stm) {
		interpret(stm.assign);
	}

	private void interpretPureIfStm(PureIfStm stm) {
		if ((Boolean)interpret(stm.boolExp))
			interpret(stm.stm);
	}

	private void interpretElseIfStm(ElseIfStm stm) {
		if ((Boolean)interpret(stm.boolExp))
			interpret(stm.stm1);
		else
			interpret(stm.stm2);
	}

	private void interpretSwitchStm(SwitchStm stm) {
		SwitchHandler sh= new SwitchHandler((Integer)interpret(stm.exp),stm.switchBlock,this);
		sh.callSwitch();
		if (breakFlag==true){breakFlag=false;}
	}

	private void interpretWhileStm(WhileStm stm) {
		while( (Boolean)interpret(stm.boolExp) ){
			if (breakFlag==true){breakFlag=false; break;}
			if (continueFlag==true){continueFlag=false;}
			interpret(stm.stm);
		}
	}

	private void interpretDoWhileStm(DoWhileStm stm) {
		do{
			if (breakFlag==true){breakFlag=false; break;}
			if (continueFlag==true){continueFlag=false;}
			interpret(stm.stm);
		}while( (Boolean)interpret(stm.boolExp) );
	}

	private void interpretForStm(ForStm stm) {
		for (interpret(stm.assign1); (Boolean)interpret(stm.boolExp); interpret(stm.assign2)){
			if (breakFlag==true){breakFlag=false; break;}
			if (continueFlag==true){continueFlag=false;}
			interpret(stm.stm);
		}
	}

	private void interpretContinueStm(ContinueStm stm) {
		continueFlag=true;
	}

	private void interpretBreakStm(BreakStm stm) {
		breakFlag=true;
	}

	private void interpretCompStm(CompStm stm) {
		ArrayList<Variable> list=(ArrayList<Variable>)interpret(stm.decls);
		interpret(stm.stmSeq);
		for (Variable i : list){
			stack.removeVar(i);
		}
	}

	private void interpretFuncCallStm(FuncCallStm stm) {
		interpret(stm.funcCall);
	}

	private Object interpretEmptyReturnStm(EmptyReturnStm stm) {
		if (isIntFunction(stm)){
			throw new EReturnException("void","int",stm.startLine);
		}
		returnFlag=true;
		return null;
	}

	private Object interpretExprReturnStm(ExprReturnStm stm) {
		Object ret=interpret(stm.expr);
		if (! isIntFunction(stm)){
			throw new EReturnException(ret.getClass().getName(),"Void",stm.expr.startLine);
		}
		if (ret instanceof Variable)
			throw new EReturnException("Variable","int",stm.expr.startLine);
		returnFlag=true;
		return (Integer)ret;
	}

	private void interpretExprPrintfStm(ExprPrintfStm stm) {
//		getVariableByName(stm.ident).getVi().addPos(new Integer [] {stm.startLine,stm.startColumn});
		String output=stm.string.replace("\\n", "\n");
		output=output.replace("\\t","\t");
		List<Object> varlist = (List<Object>)interpret(stm.exprList);
		for (Object i : varlist){
			if (!(i instanceof Integer)){
				throw new ETypeExpected("Integer","Pointer",stm.startLine);
			}
			if (output.contains("%d")){
				output=output.replaceFirst("%d", ""+i);
			}else{
				throw new EPrintfException(stm.startLine);
			}
		}
		if (output.contains("%d")){
			throw new EPrintfException(stm.startLine);
		}
		myPrintf(output, stm.startLine);
//		myPrintf((Integer)getVariableByName(stm.ident).getValue());
	}

	private void interpretScanfStm(ScanfStm stm) {
		getVariableByName(stm.ident).getVi().addPos(new Integer [] {stm.startLine,stm.startColumn + "scanf(\"%i\", &".length()});
		getVariableByName(stm.ident).setValue(myScanf(stm.startLine));
	}

	private void interpretLabelStm(LabelStm stm) {
	}

	private void interpretValueAssign(ValueAssign assign) {
		getVariableByName(assign.ident).getVi().addPos(new Integer [] {assign.startLine,assign.startColumn});
		getVariableByName(assign.ident).setValue((Integer)interpret(assign.expr));
	}

	private void interpretRefAssign(RefAssign assign) {
		getVariableByName(assign.ident).getVi().addPos(new Integer [] {assign.startLine,assign.startColumn + "*".length()});
		getVariableByName(assign.ident).setTargetValue((Integer)interpret(assign.expr));
	}

	private Object interpretExpandFunctionCall(ExpandFuncCall funcCall) {
		ArrayList<Object> params= (ArrayList<Object>)interpret(funcCall.actParams);
		ruecksprungMarken.add((Integer)funcCall.extra);
		stack.increaseVisibilityLevel();
		FunctionHandler function=getFunctionByName(funcCall.ident);
		if (function.getScope()[0]>funcCall.startLine || function.getScope()[1]<funcCall.startLine){
			throw new EUndefinedFunctionCalled(funcCall.startLine);
		}

		Object ret=function.callFunction(params);
		stack.removeAllToplevelVar();
		ruecksprungMarken.remove(ruecksprungMarken.size()-1);
		return ret;
	}


	private Object interpretEmptyActParams(EmptyActParams params) {
		return new ArrayList<Object>();
	}

	private Object interpretExprListActParams(ExprListActParams params) {
		return interpret(params.exprList);
	}

	private Object interpretLastExprList(LastExprList list) {
		ArrayList<Object> liste=new ArrayList<Object>();
		liste.add(interpret(list.expr));
		return liste;
	}

	private Object interpretPairExprList(PairExprList list) {
		ArrayList<Object> liste=(ArrayList<Object>)interpret(list.exprList);
		liste.add(0, (Object)interpret(list.expr));
		return liste;
	}

	private Object interpretPlusExpr(PlusExpr expr) {
		return (Integer)(((Integer)interpret(expr.expr)) + ((Integer)interpret(expr.term)));
	}

	private Object interpretMinusExpr(MinusExpr expr) {
		return (Integer)(((Integer)interpret(expr.expr)) - ((Integer)interpret(expr.term)));
	}

	private Object interpretFirstTermExpr(FirstTermExpr expr) {
		return interpret(expr.firstTerm);
	}

	private Object interpretMultFirstTerm(MultFirstTerm term) {
		return (Integer)(((Integer)interpret(term.firstTerm)) * ((Integer)interpret(term.factor)));
	}

	private Object interpretDivFirstTerm(DivFirstTerm term) {
		return (Integer)(((Integer)interpret(term.firstTerm)) / ((Integer)interpret(term.factor)));
	}

	private Object interpretModFirstTerm(ModFirstTerm term) {
		return (Integer)(((Integer)interpret(term.firstTerm)) % ((Integer)interpret(term.factor)));
	}

	private Object interpretFactorFirstTerm(FactorFirstTerm term) {
		return interpret(term.factor);
	}

	private Object interpretPlusFactorFirstTerm(PlusFactorFirstTerm term) {
		return interpret(term.factor);
	}

	private Object interpretMinusFactorFirstTerm(MinusFactorFirstTerm term) {
		return (Integer)(-((Integer)interpret(term.factor)));
	}

	private Object interpretMultTerm(MultTerm term) {
		return (Integer)(((Integer)interpret(term.term)) * ((Integer)interpret(term.factor)));
	}

	private Object interpretDivTerm(DivTerm term) {
		return (Integer)(((Integer)interpret(term.term)) / ((Integer)interpret(term.factor)));
	}

	private Object interpretModTerm(ModTerm term) {
		return (Integer)(((Integer)interpret(term.term)) % ((Integer)interpret(term.factor)));
	}

	private Object interpretFactorTerm(FactorTerm term) {
		return interpret(term.factor);
	}

	private Object interpretIdFactor(IdFactor factor) {
		getVariableByName(factor.ident).getVi().addPos(new Integer [] {factor.startLine,factor.startColumn});
		return getVariableByName(factor.ident).getValue();
	}

	private Object interpretRefIdFactor(RefIdFactor factor) {
		getVariableByName(factor.ident).getVi().addPos(new Integer [] {factor.startLine,factor.startColumn});
		return getVariableByName(factor.ident).getTargetValue();
	}

	private Object interpretAddIdFactor(AddIdFactor factor) {
		getVariableByName(factor.ident).getVi().addPos(new Integer [] {factor.startLine,factor.startColumn + "&".length()});
		return getVariableByName(factor.ident);
	}

	private Object interpretNumFactor(NumFactor factor) {
		return factor.number;
	}

	private Object interpretFuncCallFactor(FuncCallFactor factor) {
		return interpret(factor.funcCall);
	}

	private Object interpretCompFactor(CompFactor factor) {
		return interpret(factor.expr);
	}

	private Object interpretEqBoolExpr(EqBoolExpr expr) {
		return ((Integer)interpret(expr.expr1)).intValue() == ((Integer)interpret(expr.expr2)).intValue();
	}

	private Object interpretNotEqBoolExpr(NotEqBoolExpr expr) {
		return ((Integer)interpret(expr.expr1)).intValue() != ((Integer)interpret(expr.expr2)).intValue();
	}

	private Object interpretLessBoolExpr(LessBoolExpr expr) {
		return ((Integer)interpret(expr.expr1)).intValue() < ((Integer)interpret(expr.expr2)).intValue();
	}

	private Object interpretGreatBoolExpr(GreatBoolExpr expr) {
		return ((Integer)interpret(expr.expr1)).intValue() > ((Integer)interpret(expr.expr2)).intValue();
	}

	private Object interpretLessEqBoolExpr(LessEqBoolExpr expr) {
		return ((Integer)interpret(expr.expr1)).intValue() <= ((Integer)interpret(expr.expr2)).intValue();
	}

	private Object interpretGreatEqBoolExpr(GreatEqBoolExpr expr) {
		return ((Integer)interpret(expr.expr1)).intValue() >= ((Integer)interpret(expr.expr2)).intValue();
	}

	public void abort() {
		aborting = true;
	}

}
