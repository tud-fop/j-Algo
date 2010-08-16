//Author: Torsten Stueber
//28.03.2007

package c00;

import java.io.IOException;

import o3b.antlr.runtime.*;
import o3b.antlr.runtime.debug.ParseTreeBuilder;
import o3b.antlr.runtime.tree.ParseTree;

import c00.parser.*;


/**
* The class AST contains the abstract syntax tree data type definition.
*/
public abstract class AST {

	/**
	* Parses a file containing a C program and returns the abstract syntax tree of the input file.
	* The start symbol of the derivation tree of the input program can be specified. Notice that the ouput tree is
	* of the generic type ASTTree and should be cast to one of its inherited classes.
	* <p>
	* Example: <code>AST.Block block =  (AST.Block)AST.parseFile("foo.c", "Block");</code>
	*
	* @param fileName    the name of the input file
	* @param startSymbol a string containing the name of the start symbol, e.g., "Program" or "StatementSequence"
	* @return            the abstract syntax tree of the input program
	* @see	 ASTTree
	*/
	public static ASTTree parseFile(String fileName, String startSymbol, StringBuffer err) throws RecognitionException {
		try {
			CharStream charStream = new ANTLRFileStream(fileName);
			C00Lexer lexer = new C00Lexer(charStream);
			CommonTokenStream tokenStream = new CommonTokenStream(lexer);
			ParseTreeBuilder builder = new ParseTreeBuilder(startSymbol);
			C00Parser parser = new C00Parser(tokenStream, builder, err, true);

			return parse(parser, startSymbol);
		}
		catch (IOException e) {
			if(err!=null) err.append("IOError");
		}

		return null;
	}

	public static ASTTree parseFile(String fileName, String startSymbol) throws RecognitionException {
		return parseFile(fileName, startSymbol, null);
	}
	/**
	* Parses a string containing a C program and returns the abstract syntax tree of the input string.
	* The start symbol of the derivation tree of the input program can be specified. Notice that the ouput tree is
	* of the generic type ASTTree and should be cast to one of its inherited classes.
	* <p>
	* Example: <code>AST.Block block = (AST.Block)AST.parseString("{int i=4; i=3-i*2;}", "Block");</code>
	*
	* @param input       the input string
	* @param startSymbol a string containing the name of the start symbol, e.g., "Program" or "StatementSequence"
	* @return            the abstract syntax tree of the input program
	* @see	 ASTTree
	*/
	public static ASTTree parseString(String input, String startSymbol, StringBuffer err, boolean ignoreExceptions) throws RecognitionException {
		CharStream charStream = new ANTLRStringStream(input);
		C00Lexer lexer = new C00Lexer(charStream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		ParseTreeBuilder builder = new ParseTreeBuilder(startSymbol);
		C00Parser parser = new C00Parser(tokenStream, builder, err, ignoreExceptions);
		
		return parse(parser, startSymbol);
	}

	public static ASTTree parseString(String input, String startSymbol, StringBuffer err) throws RecognitionException {
		return parseString(input, startSymbol, err, true);
	}
	
	public static ASTTree parseString(String input, String startSymbol) throws RecognitionException {
		return parseString(input, startSymbol, null);
	}
	
	private static ASTTree parse(C00Parser parser, String startSymbol) throws RecognitionException {
		if (startSymbol.equals("Program"))
			return parser.program().astTree;
		
		if (startSymbol.equals("GlobalDeclarations"))
			return parser.globalDeclarations().astTree;

		if (startSymbol.equals("Declarations"))
			return parser.declarations().astTree;

		if (startSymbol.equals("Declaration"))
			return parser.declaration().astTree;

		if (startSymbol.equals("ConstDeclarations"))
			return parser.constDeclarations().astTree;

		if (startSymbol.equals("VarDeclarations"))
			return parser.varDeclarations().astTree;

		if (startSymbol.equals("FunctionImplementations"))
			return parser.functionImplementations().astTree;

		if (startSymbol.equals("FunctionHeading"))
			return parser.functionHeading().astTree;

		if (startSymbol.equals("FormalParameters"))
			return parser.formalParameters().astTree;

		if (startSymbol.equals("ParamSections"))
			return parser.paramSections().astTree;

		if (startSymbol.equals("Block"))
			return parser.block().astTree;

		if (startSymbol.equals("StatementSequence"))
			return parser.statementSequence().astTree;

		if (startSymbol.equals("Statement"))
			return parser.statement().astTree;

		if (startSymbol.equals("Assignment"))
			return parser.assignment().astTree;

		if (startSymbol.equals("SwitchBlock"))
			return parser.switchBlock().astTree;

		if (startSymbol.equals("CaseSequence"))
			return parser.caseSequence().astTree;

		if (startSymbol.equals("FunctionCall"))
			return parser.functionCall().astTree;

		if (startSymbol.equals("ActualParameters"))
			return parser.actualParameters().astTree;

		if (startSymbol.equals("EpxressionList"))
			return parser.expressionList().astTree;

		if (startSymbol.equals("Expression"))
			return parser.expression().astTree;

		if (startSymbol.equals("FirstTerm"))
			return parser.firstTerm().astTree;

		if (startSymbol.equals("Term"))
			return parser.term().astTree;

		if (startSymbol.equals("Factor"))
			return parser.factor().astTree;

		if (startSymbol.equals("BoolExpression"))
			return parser.boolExpression().astTree;
		
		return null;
	}

	/**
	* This is the base class of all abstract syntax trees.
	*/
	public static class ASTTree {
		
		/**
		* Constructor of the class ASTTree
		*/
		public ASTTree() {
			parent = null;
			extra = null;
		}
		
		/**
		* Reference to the parent node of the ASTTree
		*/
		public ASTTree parent;
		
		/**
		* Reference to an additional memory object.
		*/
		public Object extra;
		
		/**
		* The code fragment that this astract syntax tree stands for starts in this line number.
		* Line numbers run from 1, 2, ...
		*/
		public int startLine;

		/**
		* The first symbol of the associated code fragment starts is in this column in line {@link startLine}.
		* Column numbers run from 0, 1, ...
		*/
		public int startColumn;

		/**
		* The code fragment that this astract syntax tree stands for ends in this line number.
		* Line numbers run from 1, 2, ...
		*/
		public int endLine;

		/**
		* This is the column number of the last symbol of the associated code fragment in {@link endLine}.
		* Column numbers run from 0, 1, ...
		*/
		public int endColumn;
	}


	public static abstract class Program extends ASTTree {}
	
	/**
	* 'Program' -> #include <stdio.h> 'GlobalDeclarations' 'FunctionImplementations' int main('FormalParameters') 'Block'
	*/
	public static class ExpandProgram extends Program {
		public GlobalDeclarations globDecl;
		public FunctionImplementations funcImpl;
		public FormalParameters formParam;
		public Block block;
		public ExpandProgram(GlobalDeclarations globDecl, FunctionImplementations funcImpl,
				FormalParameters formParam, Block block) {
			super();
			this.globDecl = globDecl; globDecl.parent = this;
			this.funcImpl = funcImpl; funcImpl.parent = this;
			this.formParam = formParam; formParam.parent = this;
			this.block = block; block.parent = this;
		}
	}
	
	
	public static abstract class GlobalDeclarations extends ASTTree {}
	
	/**
	* 'GlobalDeclarations' -> epsilon
	*/
	public static class EmptyGlobDecls extends GlobalDeclarations {
		public EmptyGlobDecls() {
			super();
		}
	}
	
	/**
	* 'GlobalDeclarations' -> 'Declaration' 'GlobalDeclarations'
	*/
	public static class VarConstGlobDecls extends GlobalDeclarations {
		public Declaration decl;
		public GlobalDeclarations globDecls;
		public VarConstGlobDecls(Declaration decl, GlobalDeclarations globDecls) {
			super();
			this.decl = decl; decl.parent = this;
			this.globDecls = globDecls; globDecls.parent = this;
		}
	}
	
	/**
	* 'GlobalDeclarations' -> 'FunctionHeading'; 'GlobalDeclarations'
	*/
	public static class FuncGlobDecls extends GlobalDeclarations {
		public FunctionHeading funcHead;
		public GlobalDeclarations globDecls;
		public FuncGlobDecls(FunctionHeading funcHead, GlobalDeclarations globDecls) {
			super();
			this.funcHead = funcHead; funcHead.parent = this;
			this.globDecls = globDecls; globDecls.parent = this;
		}
	}
	
	
	public static abstract class Declarations extends ASTTree {}
	
	/**
	* 'Declarations' -> epsilon
	*/
	public static class EmptyDecls extends Declarations {
		public EmptyDecls() {
			super();
		}
	}
	
	/**
	* 'Declarations' -> 'Declaration' 'Declarations'
	*/
	public static class PairDecls extends Declarations {
		public Declaration decl;
		public Declarations decls;
		public PairDecls(Declaration decl, Declarations decls) {
			super();
			this.decl = decl; decl.parent = this;
			this.decls = decls; decls.parent = this;
		}
	}
	
	
	public static abstract class Declaration extends ASTTree  {}
	
	/**
	* 'Declaration' -> const int 'ConstDeclarations';
	*/
	public static class ConstDecl extends Declaration {
		public ConstDeclarations constDecls;
		public ConstDecl(ConstDeclarations constDecls) {
			super();
			this.constDecls = constDecls; constDecls.parent = this;
		}
	}
	
	/**
	* 'Declaration' -> int 'VarDeclarations';
	*/
	public static class VarDecl extends Declaration {
		public VarDeclarations varDecls;
		public VarDecl(VarDeclarations varDecls) {
			super();
			this.varDecls = varDecls; varDecls.parent = this;
		}
	}
	
	
	public static abstract class ConstDeclarations extends ASTTree  {}
	
	/**
	* 'ConstDeclarations' -> 'Ident' = 'Number'
	*/
	public static class LastConstDecls extends ConstDeclarations {
		public String ident;
		public int number;
		public LastConstDecls(String ident, int number) {
			super();
			this.ident = ident; 
			this.number = number;
		}
	}
	
	/**
	* 'ConstDeclarations' -> 'Ident' = 'Number', 'ConstDeclarations'
	*/
	public static class PairConstDecls extends ConstDeclarations {
		public String ident;
		public int number;
		public ConstDeclarations constDecls;
		public PairConstDecls(String ident, int number, ConstDeclarations constDecls) {
			super();
			this.ident = ident;
			this.number = number;
			this.constDecls = constDecls; constDecls.parent = this;
		}
	}
	
	
	public static abstract class VarDeclarations extends ASTTree  {}
	
	/**
	* 'VarDeclarations' -> 'Ident'
	*/
	public static class VarLastDecls extends VarDeclarations {
		public String ident;
		public VarLastDecls(String ident) {
			super();
			this.ident = ident;
		}
	}

	/**
	* 'VarDeclarations' -> *'Ident'
	*/
	public static class RefVarLastDecls extends VarDeclarations {
		public String ident;
		public RefVarLastDecls(String ident) {
			super();
			this.ident = ident;
		}
	}
	
	/**
	* 'VarDeclarations' -> 'Ident' = 'Number'
	*/
	public static class InitVarLastDecls extends VarDeclarations {
		public String ident;
		public int number;
		public InitVarLastDecls(String ident, int number) {
			super();
			this.ident = ident;
			this.number = number;
		}
	}
	
	/**
	* 'VarDeclarations' -> 'Ident', 'VarDeclarations'
	*/
	public static class VarPairDecls extends VarDeclarations {
		public String ident;
		public VarDeclarations varDecls;
		public VarPairDecls(String ident, VarDeclarations varDecls) {
			super();
			this.ident = ident;
			this.varDecls = varDecls; varDecls.parent = this;
		}
	}

	/**
	* 'VarDeclarations' -> *'Ident', 'VarDeclarations'
	*/
	public static class RefVarPairDecls extends VarDeclarations {
		public String ident;
		public VarDeclarations varDecls;
		public RefVarPairDecls(String ident, VarDeclarations varDecls) {
			super();
			this.ident = ident;
			this.varDecls = varDecls; varDecls.parent = this;
		}
	}
	
	/**
	* 'VarDeclarations' -> 'Ident' = 'Number', 'VarDeclarations'
	*/
	public static class InitVarPairDecls extends VarDeclarations {
		public String ident;
		public int number;
		public VarDeclarations varDecls;
		public InitVarPairDecls(String ident, int number, VarDeclarations varDecls) {
			super();
			this.ident = ident;
			this.number = number;
			this.varDecls = varDecls; varDecls.parent = this;
		}
	}
	
	
	public static abstract class FunctionImplementations extends ASTTree  {}
	
	/**
	* 'FunctionImplementations' -> epsilon
	*/
	public static class EmptyFuncImpls extends FunctionImplementations {
		public EmptyFuncImpls() {
			super();
		}
	}
	
	/**
	* 'FunctionImplementations' -> 'FunctionHeading' 'Block' 'FunctionImplementations'
	*/
	public static class PairFuncImpls extends FunctionImplementations {
		public FunctionHeading funcHead;
		public Block block;
		public FunctionImplementations funcImpls;
		public PairFuncImpls(FunctionHeading funcHead, Block block, FunctionImplementations funcImpls) {
			super();
			this.funcHead = funcHead; funcHead.parent = this;
			this.block = block; block.parent = this;
			this.funcImpls = funcImpls; funcImpls.parent = this;
		}
	}
	
	
	public static abstract class FunctionHeading extends ASTTree  {}
	
	/**
	* 'FunctionHeading' -> void 'Ident'('FormalParameters')
	*/
	public static class VoidFuncHead extends FunctionHeading {
		public String ident;
		public FormalParameters formParams;
		public VoidFuncHead(String ident, FormalParameters formParams) {
			super();
			this.ident = ident;
			this.formParams = formParams; formParams.parent = this;
		}
	}
	
	/**
	* 'FunctionHeading' -> int 'Ident'('FormalParameters')
	*/
	public static class IntFuncHead extends FunctionHeading {
		public String ident;
		public FormalParameters formParams;
		public IntFuncHead(String ident, FormalParameters formParams) {
			super();
			this.ident = ident;
			this.formParams = formParams; formParams.parent = this;
		}
	}
	
	
	public static abstract class FormalParameters extends ASTTree  {}
	
	/**
	* 'FormalParameters' -> epsilon
	*/
	public static class EmptyFormalParams extends FormalParameters {
		public EmptyFormalParams() {
			super();
		}
	}

	/**
	* 'FormalParameters' -> void
	*/
	public static class VoidFormalParams extends FormalParameters {
		public VoidFormalParams() {
			super();
		}
	}
	
	/**
	* 'FormalParameters' -> 'ParamSections'
	*/
	public static class NonVoidFormalParams extends FormalParameters {
		public ParamSections prmSects;
		public NonVoidFormalParams(ParamSections prmSects) {
			super();
			this.prmSects = prmSects; prmSects.parent = this;
		}
	}
	
	
	public static abstract class ParamSections extends ASTTree  {}
	
	/**
	* 'ParamSections' -> int 'Ident'
	*/
	public static class ValueLastPrmSects extends ParamSections {
		public String ident;
		public ValueLastPrmSects(String ident) {
			super();
			this.ident = ident;
		}
	}
	
	/**
	* 'ParamSections' -> int *'Ident'
	*/
	public static class RefLastPrmSects extends ParamSections {
		public String ident;
		public RefLastPrmSects(String ident) {
			super();
			this.ident = ident;
		}
	}
	
	/**
	* 'ParamSections' -> int 'Ident', 'ParamSections'
	*/
	public static class ValuePairPrmSects extends ParamSections {
		public String ident;
		public ParamSections prmSects;
		public ValuePairPrmSects(String ident, ParamSections prmSects) {
			super();
			this.ident = ident;
			this.prmSects = prmSects; prmSects.parent = this;
		}
	}
	
	/**
	* 'ParamSections' -> int *'Ident', 'ParamSections'
	*/
	public static class RefPairPrmSects extends ParamSections {
		public String ident;
		public ParamSections prmSects;
		public RefPairPrmSects(String ident, ParamSections prmSects) {
			super();
			this.ident = ident;
			this.prmSects = prmSects; prmSects.parent = this;
		}
	}
	
	
	public static abstract class Block extends ASTTree  {}
	
	/**
	* 'Block' -> {'Declarations' 'StatementSequence'}
	*/
	public static class ExpandBlock extends Block {
		public Declarations decls;
		public StatementSequence stmSeq;
		public ExpandBlock(Declarations decls, StatementSequence stmSeq) {
			super();
			this.decls = decls; decls.parent = this;
			this.stmSeq = stmSeq; stmSeq.parent = this;
		}
	}
	
	
	public static abstract class StatementSequence extends ASTTree  {}
	
	/**
	* 'StatementSequence' -> epsilon
	*/
	public static class EmptyStmSeq extends StatementSequence {
		public EmptyStmSeq() {
			super();
		}
	}
	
	/**
	* 'StatementSequence' -> 'Statement' 'StatementSequence'
	*/
	public static class PairStmSeq extends StatementSequence {
		public Statement stm;
		public StatementSequence stmSeq;
		public PairStmSeq(Statement stm, StatementSequence stmSeq) {
			super();
			this.stm = stm; stm.parent = this;
			this.stmSeq = stmSeq; stmSeq.parent = this;
		}
	}
	
	
	public static abstract class Statement extends ASTTree  {}
	
	/**
	* 'Statement' -> 'Assignment'
	*/
	public static class AssignStm extends Statement {
		public Assignment assign;
		public AssignStm(Assignment assign) {
			super();
			this.assign = assign; assign.parent = this;
		}
	}
	
	/**
	* 'Statement' -> if ('BoolExpression') 'Statement'
	*/
	public static class PureIfStm extends Statement {
		public BoolExpression boolExp;
		public Statement stm;
		public PureIfStm(BoolExpression boolExp, Statement stm) {
			super();
			this.boolExp = boolExp; boolExp.parent = this;
			this.stm = stm; stm.parent = this;
		}
	}
	
	/**
	* 'Statement' -> if ('BoolExpression') 'Statement' else 'Statement'
	*/
	public static class ElseIfStm extends Statement {
		public BoolExpression boolExp;
		public Statement stm1, stm2;
		public ElseIfStm(BoolExpression boolExp, Statement stm1, Statement stm2) {
			super();
			this.boolExp = boolExp; boolExp.parent = this;
			this.stm1 = stm1; stm1.parent = this;
			this.stm2 = stm2; stm2.parent = this;
		}
	}
	
	/**
	* 'Statement' -> switch ('Expression') 'SwitchBlock'
	*/
	public static class SwitchStm extends Statement {
		public Expression exp;
		public SwitchBlock switchBlock;
		public SwitchStm(Expression exp, SwitchBlock switchBlock) {
			super();
			this.exp = exp; exp.parent = this;
			this.switchBlock = switchBlock; switchBlock.parent = this;
		}
	}
	
	/**
	* 'Statement' -> while ('BoolExpression') 'Statement'
	*/
	public static class WhileStm extends Statement {
		public BoolExpression boolExp;
		public Statement stm;
		public WhileStm(BoolExpression boolExp, Statement stm) {
			super();
			this.boolExp = boolExp; boolExp.parent = this;
			this.stm = stm; stm.parent = this;
		}
	}
	
	/**
	* 'Statement' -> do 'Statement' while ('BoolExpression')
	*/
	public static class DoWhileStm extends Statement {
		public Statement stm;
		public BoolExpression boolExp;
		public DoWhileStm(Statement stm, BoolExpression boolExp) {
			super();
			this.stm = stm; stm.parent = this;
			this.boolExp = boolExp; boolExp.parent = this;
		}
	}
	
	/**
	* 'Statement' -> for ('Assignment'; 'BoolExpression'; 'Assignment') 'Statement'
	*/
	public static class ForStm extends Statement {
		public Assignment assign1;
		public BoolExpression boolExp;
		public Assignment assign2;
		public Statement stm;
		public ForStm(Assignment assign1, BoolExpression boolExp, Assignment assign2, Statement stm) {
			super();
			this.assign1 = assign1; assign1.parent = this;
			this.boolExp = boolExp; boolExp.parent = this;
			this.assign2 = assign2; assign2.parent = this;
			this.stm = stm; stm.parent = this;
		}
	}
	
	/**
	* 'Statement' -> continue;
	*/
	public static class ContinueStm extends Statement {
		public ContinueStm() {
			super();
		}
	}
	
	/**
	* 'Statement' -> break;
	*/
	public static class BreakStm extends Statement {
		public BreakStm() {
			super();
		}
	}
	
	/**
	* 'Statement' -> {'Declarations' 'StatementSequence'}
	*/
	public static class CompStm extends Statement {
		public Declarations decls;
		public StatementSequence stmSeq;
		public CompStm(Declarations decls, StatementSequence stmSeq) {
			super();
			this.decls = decls; decls.parent = this;
			this.stmSeq = stmSeq; stmSeq.parent = this;
		}
	}
	
	/**
	* 'Statement' -> 'FunctionCall';
	*/
	public static class FuncCallStm extends Statement {
		public FunctionCall funcCall;
		public FuncCallStm(FunctionCall funcCall) {
			super();
			this.funcCall = funcCall; funcCall.parent = this;
		}
	}
	
	/**
	* 'Statement' -> return;
	*/
	public static class EmptyReturnStm extends Statement {
		public EmptyReturnStm() {
			super();
		}
	}
	
	/**
	* 'Statement' -> return 'Expression';
	*/
	public static class ExprReturnStm extends Statement {
		public Expression expr;
		public ExprReturnStm(Expression expr) {
			super();
			this.expr = expr; expr.parent = this;
		}
	}

	/**
	* 'Statement' -> printf('String');
	*/
	public static class PurePrintfStm extends Statement {
		public String string;
		public PurePrintfStm(String string) {
			super();
			this.string = string;
		}
	}
	
	/**
	* 'Statement' -> printf('String', 'ExpressionList');
	*/
	public static class ExprPrintfStm extends Statement {
		public String string;
		public ExpressionList exprList;
		public ExprPrintfStm(String string, ExpressionList exprList) {
			super();
			this.string = string;
			this.exprList = exprList;
		}
	}

	/**
	* 'Statement' -> scanf("%i", &'Ident');
	*/
	public static class ScanfStm extends Statement {
		public String ident;
		public ScanfStm(String ident) {
			super();
			this.ident = ident;
		}
	}
	
	/**
	* 'Statement' -> /<code></code>*label 'Number'*<code></code>/
	*/
	public static class LabelStm extends Statement {
		public int number;
		public LabelStm(int number) {
			super();
			this.number = number;
		}
	}
	
	
	public static abstract class Assignment extends ASTTree  {}
	
	/**
	* 'Assignment' -> 'Ident' = 'Expression'
	*/
	public static class ValueAssign extends Assignment {
		public String ident;
		public Expression expr;
		public ValueAssign(String ident, Expression expr) {
			super();
			this.ident = ident;
			this.expr = expr; expr.parent = this;
		}
	}
	
	/**
	* 'Assignment' -> *'Ident' = 'Expression'
	*/
	public static class RefAssign extends Assignment {
		public String ident;
		public Expression expr;
		public RefAssign(String ident, Expression expr) {
			super();
			this.ident = ident;
			this.expr = expr; expr.parent = this;
		}
	}
	
	
	public static abstract class SwitchBlock extends ASTTree  {}
	
	/**
	* 'SwitchBlock' -> {'CaseSequence'}
	*/
	public static class NoDefaultSwitchBlock extends SwitchBlock {
		public CaseSequence caseSeq;
		public NoDefaultSwitchBlock(CaseSequence caseSeq) {
			super();
			this.caseSeq = caseSeq; caseSeq.parent = this;
		}
	}
	
	/**
	* 'SwitchBlock' -> {'CaseSequence' default: 'StatementSequence'}
	*/
	public static class DefaultSwitchBlock extends SwitchBlock {
		public CaseSequence caseSeq;
		public StatementSequence stmSeq;
		public DefaultSwitchBlock(CaseSequence caseSeq, StatementSequence stmSeq) {
			super();
			this.caseSeq = caseSeq; caseSeq.parent = this;
			this.stmSeq = stmSeq; stmSeq.parent = this;
		}
	}
	
	
	public static abstract class CaseSequence extends ASTTree  {}
	
	/**
	* 'CaseSequence' -> epsilon
	*/
	public static class EmptyCaseSeq extends CaseSequence {
		public EmptyCaseSeq() {
			super();
		}
	}
	
	/**
	* 'CaseSequence' -> case 'Number': 'StatementSequence' 'CaseSequence'
	*/
	public static class NoBreakPairCaseSeq extends CaseSequence {
		public int number;
		public StatementSequence stmSeq;
		public CaseSequence caseSeq;
		public NoBreakPairCaseSeq(int number, StatementSequence stmSeq, CaseSequence caseSeq) {
			super();
			this.number = number;
			this.stmSeq = stmSeq; stmSeq.parent = this;
			this.caseSeq = caseSeq; caseSeq.parent = this;
		}
	}
	
	
	public static abstract class FunctionCall extends ASTTree  {}
	
	/**
	* 'FunctionCall' -> 'Ident'('ActualParameters')
	*/
	public static class ExpandFuncCall extends FunctionCall {
		public String ident;
		public ActualParameters actParams;
		public ExpandFuncCall(String ident, ActualParameters actParams) {
			super();
			this.ident = ident;
			this.actParams = actParams; actParams.parent = this;
		}
	}
	
	
	public static abstract class ActualParameters extends ASTTree  {}
	
	/**
	* 'ActualParameters' -> epsilon
	*/
	public static class EmptyActParams extends ActualParameters {
		public EmptyActParams() {
			super();
		}
	}
	
	/**
	* 'ActualParameters' -> 'ExpressionList'
	*/
	public static class ExprListActParams extends ActualParameters {
		public ExpressionList exprList;
		public ExprListActParams(ExpressionList exprList) {
			super();
			this.exprList = exprList; exprList.parent = this;
		}
	}


	public static abstract class ExpressionList extends ASTTree  {}
	
	/**
	* 'ExpressionList' -> 'Expression'
	*/
	public static class LastExprList extends ExpressionList {
		public Expression expr;
		public LastExprList(Expression expr) {
			super();
			this.expr = expr; expr.parent = this;
		}
	}

	/**
	* 'ExpressionList' -> 'Expression', 'ExpressionList'
	*/
	public static class PairExprList extends ExpressionList {
		public Expression expr;
		public ExpressionList exprList;
		public PairExprList(Expression expr, ExpressionList exprList) {
			super();
			this.expr = expr; expr.parent = this;
			this.exprList = exprList; exprList.parent = this;
		}
	}

	
	public static abstract class Expression extends ASTTree  {}
	
	/**
	* 'Expression' -> 'Expression' + 'Term'
	*/
	public static class PlusExpr extends Expression {
		public Expression expr;
		public Term term;
		public PlusExpr(Expression expr, Term term) {
			super();
			this.expr = expr; expr.parent = this;
			this.term = term; term.parent = this;
		}
	}
	
	/**
	* 'Expression' -> 'Expression' - 'Term'
	*/
	public static class MinusExpr extends Expression {
		public Expression expr;
		public Term term;
		public MinusExpr(Expression expr, Term term) {
			super();
			this.expr = expr; expr.parent = this;
			this.term = term; term.parent = this;
		}
	}
	
	/**
	* 'Expression' -> 'FirstTerm'
	*/
	public static class FirstTermExpr extends Expression {
		public FirstTerm firstTerm;
		public FirstTermExpr(FirstTerm firstTerm) {
			super();
			this.firstTerm = firstTerm; firstTerm.parent = this;
		}
	}


	public static abstract class FirstTerm extends ASTTree  {}
	
	/**
	* 'FirstTerm' -> 'FirstTerm' * 'Factor'
	*/
	public static class MultFirstTerm extends FirstTerm {
		public FirstTerm firstTerm;
		public Factor factor;
		public MultFirstTerm(FirstTerm firstTerm, Factor factor) {
			super();
			this.firstTerm = firstTerm; firstTerm.parent = this;
			this.factor = factor; factor.parent = this;
		}
	}
	
	/**
	* 'FirstTerm' -> 'FirstTerm' / 'Factor'
	*/
	public static class DivFirstTerm extends FirstTerm {
		public FirstTerm firstTerm;
		public Factor factor;
		public DivFirstTerm(FirstTerm firstTerm, Factor factor) {
			super();
			this.firstTerm = firstTerm; firstTerm.parent = this;
			this.factor = factor; factor.parent = this;
		}
	}

	/**
	* 'FirstTerm' -> 'FirstTerm' % 'Factor'
	*/
	public static class ModFirstTerm extends FirstTerm {
		public FirstTerm firstTerm;
		public Factor factor;
		public ModFirstTerm(FirstTerm firstTerm, Factor factor) {
			super();
			this.firstTerm = firstTerm; firstTerm.parent = this;
			this.factor = factor; factor.parent = this;
		}
	}
	
	/**
	* 'FirstTerm' -> 'Factor'
	*/
	public static class FactorFirstTerm extends FirstTerm {
		public Factor factor;
		public FactorFirstTerm(Factor factor) {
			super();
			this.factor = factor; factor.parent = this;
		}
	}

	/**
	* 'FirstTerm' -> +'Factor'
	*/
	public static class PlusFactorFirstTerm extends FirstTerm {
		public Factor factor;
		public PlusFactorFirstTerm(Factor factor) {
			super();
			this.factor = factor; factor.parent = this;
		}
	}

	/**
	* 'FirstTerm' -> -'Factor'
	*/
	public static class MinusFactorFirstTerm extends FirstTerm {
		public Factor factor;
		public MinusFactorFirstTerm(Factor factor) {
			super();
			this.factor = factor; factor.parent = this;
		}
	}

	
	public static abstract class Term extends ASTTree  {}
	
	/**
	* 'Term' -> 'Term' * 'Factor'
	*/
	public static class MultTerm extends Term {
		public Term term;
		public Factor factor;
		public MultTerm(Term term, Factor factor) {
			super();
			this.term = term; term.parent = this;
			this.factor = factor; factor.parent = this;
		}
	}
	
	/**
	* 'Term' -> 'Term' / 'Factor'
	*/
	public static class DivTerm extends Term {
		public Term term;
		public Factor factor;
		public DivTerm(Term term, Factor factor) {
			super();
			this.term = term; term.parent = this;
			this.factor = factor; factor.parent = this;
		}
	}

	/**
	* 'Term' -> 'Term' % 'Factor'
	*/
	public static class ModTerm extends Term {
		public Term term;
		public Factor factor;
		public ModTerm(Term term, Factor factor) {
			super();
			this.term = term; term.parent = this;
			this.factor = factor; factor.parent = this;
		}
	}
	
	/**
	* 'Term' -> 'Factor'
	*/
	public static class FactorTerm extends Term {
		public Factor factor;
		public FactorTerm(Factor factor) {
			super();
			this.factor = factor; factor.parent = this;
		}
	}
	

	public static abstract class Factor extends ASTTree  {}
	
	/**
	* 'Factor' -> 'Ident'
	*/
	public static class IdFactor extends Factor {
		public String ident;
		public IdFactor(String ident) {
			super();
			this.ident = ident;
		}
	}
	
	/**
	* 'Factor' -> *'Ident'
	*/
	public static class RefIdFactor extends Factor {
		public String ident;
		public RefIdFactor(String ident) {
			super();
			this.ident = ident;
		}
	}
	
	/**
	* 'Factor' -> &'Ident'
	*/
	public static class AddIdFactor extends Factor {
		public String ident;
		public AddIdFactor(String ident) {
			super();
			this.ident = ident;
		}
	}
	
	/**
	* 'Factor' -> 'Number'
	*/
	public static class NumFactor extends Factor {
		public int number;
		public NumFactor(int number) {
			super();
			this.number = number;
		}
	}
	
	/**
	* 'Factor' -> 'FunctionCall'
	*/
	public static class FuncCallFactor extends Factor {
		public FunctionCall funcCall;
		public FuncCallFactor(FunctionCall funcCall) {
			super();
			this.funcCall = funcCall; funcCall.parent = this;
		}
	}
	
	/**
	* 'Factor' -> ('Expression')
	*/
	public static class CompFactor extends Factor {
		public Expression expr;
		public CompFactor(Expression expr) {
			super();
			this.expr = expr; expr.parent = this;
		}
	}
	
	
	public static abstract class BoolExpression extends ASTTree  {}
	
	/**
	* 'BoolExpression' -> 'Expression' == 'Expression'
	*/
	public static class EqBoolExpr extends BoolExpression {
		public Expression expr1, expr2;
		public EqBoolExpr(Expression expr1, Expression expr2) {
			super();
			this.expr1 = expr1; expr1.parent = this;
			this.expr2 = expr2; expr2.parent = this;
		}
	}
	
	/**
	* 'BoolExpression' -> 'Expression' != 'Expression'
	*/
	public static class NotEqBoolExpr extends BoolExpression {
		public Expression expr1, expr2;
		public NotEqBoolExpr(Expression expr1, Expression expr2) {
			super();
			this.expr1 = expr1; expr1.parent = this;
			this.expr2 = expr2; expr2.parent = this;
		}
	}
	
	/**
	* 'BoolExpression' -> 'Expression' < 'Expression'
	*/
	public static class LessBoolExpr extends BoolExpression {
		public Expression expr1, expr2;
		public LessBoolExpr(Expression expr1, Expression expr2) {
			super();
			this.expr1 = expr1; expr1.parent = this;
			this.expr2 = expr2; expr2.parent = this;
		}
	}
	
	/**
	* 'BoolExpression' -> 'Expression' > 'Expression'
	*/
	public static class GreatBoolExpr extends BoolExpression {
		public Expression expr1, expr2;
		public GreatBoolExpr(Expression expr1, Expression expr2) {
			super();
			this.expr1 = expr1; expr1.parent = this;
			this.expr2 = expr2; expr2.parent = this;
		}
	}
	
	/**
	* 'BoolExpression' -> 'Expression' <= 'Expression'
	*/
	public static class LessEqBoolExpr extends BoolExpression {
		public Expression expr1, expr2;
		public LessEqBoolExpr(Expression expr1, Expression expr2) {
			super();
			this.expr1 = expr1; expr1.parent = this;
			this.expr2 = expr2; expr2.parent = this;
		}
	}
	
	/**
	* 'BoolExpression' -> 'Expression' >= 'Expression'
	*/
	public static class GreatEqBoolExpr extends BoolExpression {
		public Expression expr1, expr2;
		public GreatEqBoolExpr(Expression expr1, Expression expr2) {
			super();
			this.expr1 = expr1; expr1.parent = this;
			this.expr2 = expr2; expr2.parent = this;
		}
	}

}