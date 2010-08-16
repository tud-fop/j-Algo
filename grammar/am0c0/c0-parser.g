// Author: Martin Morgenstern
%header {:
/* WARNING: This is generated code. DO *NOT* MODIFY IT! */
:};
%package "org.jalgo.module.am0c0.parser.c0";
%import "org.jalgo.module.am0c0.parser.ErrorEvents";
%import "org.jalgo.module.am0c0.parser.ParserUtils";
%import "org.jalgo.module.am0c0.model.c0.ast.*";
%import "org.jalgo.module.am0c0.model.c0.trans.AtomicTrans.AtomicType";

%class "GeneratedC0Parser";

%init {:
        this.report = ErrorEvents.forC0();
:};

%embed {:
    public ErrorEvents getErrorEvents() {
        return (ErrorEvents) report;
    }

    private int convert(final Symbol symbol) {
        return ParserUtils.safeSymbolToInt(symbol, (ErrorEvents) report);
    }
:};

%terminals INCLUDE, STDIO;
%terminals IF, ELSE, WHILE, MAIN, PRINTF, SCANF, RETURN, INT, CONST;
%terminals IFORMAT, DFORMAT;
%terminals COMMA, SEMICOLON, AMP, ASSIGN;
%terminals PLUS, MINUS, MULT, DIV, MOD;
%terminals EQ, NE, GE, LE, GT, LT;
%terminals LBRACKET, RBRACKET, LPAREN, RPAREN;
%terminals NUMBER, IDENT;

%typeof Factor = "Factor";
%typeof Term = "Term";
%typeof SimpleExpr = "SimpleExpr";
%typeof Statement = "Statement";
%typeof Relation = "AtomicType";
%typeof Program = "Program";
%typeof Block = "Block";
%typeof BoolExpr = "BoolExpression";
%typeof StatementSequence = "StatementSequence";
%typeof Declaration = "Declaration";
%typeof ConstDeclaration = "ConstDeclaration";
%typeof VarDeclaration = "VarDeclaration";

%typeof IDENT = "String";

// resolve shift-reduce conflict (dangling else)
%nonassoc ELSE;
%nonassoc IF;

%goal Program;

Program
    = INCLUDE STDIO INT MAIN LPAREN RPAREN Block.b
      {: return new Symbol(new Program(b)); :}
    ;

Block
    = LBRACKET Declaration.d StatementSequence.s RETURN SEMICOLON RBRACKET
      {: return new Symbol(new Block(d, s)); :}
    | LBRACKET Declaration.d RETURN SEMICOLON RBRACKET
      {: return new Symbol(new Block(d, null)); :}
    ;

StatementSequence
	= Statement.stat
	  {: return new Symbol(new StatementSequence(stat)); :}
	| StatementSequence.seq Statement.stat
	  {: seq.addStatement(stat); return new Symbol(seq); :}
	;

Declaration
    =
      {: return new Symbol(new Declaration(null, null)); :}
    | ConstDeclaration.constdecl SEMICOLON
      {: return new Symbol(new Declaration(constdecl, null)); :}
    | VarDeclaration.vardecl SEMICOLON
      {: return new Symbol(new Declaration(null, vardecl)); :}
    | ConstDeclaration.constdecl SEMICOLON VarDeclaration.vardecl SEMICOLON
      {: return new Symbol(new Declaration(constdecl, vardecl)); :}
    ;

ConstDeclaration
    = CONST IDENT.i ASSIGN NUMBER.n
      {: return new Symbol(new ConstDeclaration(new C0AST.ConstIdent(i, convert(n)))); :}
    | CONST IDENT.i ASSIGN MINUS NUMBER.n
      {: return new Symbol(new ConstDeclaration(new C0AST.ConstIdent(i, -convert(n)))); :}
    | ConstDeclaration.d COMMA IDENT.i ASSIGN NUMBER.n
      {: d.addConstant(new C0AST.ConstIdent(i, convert(n))); return new Symbol(d); :}
    | ConstDeclaration.d COMMA IDENT.i ASSIGN MINUS NUMBER.n
      {: d.addConstant(new C0AST.ConstIdent(i, -convert(n))); return new Symbol(d); :}
    ;

VarDeclaration
    = INT IDENT.i
      {: return new Symbol(new VarDeclaration(new C0AST.Ident(i))); :}
    | VarDeclaration.d COMMA IDENT.i
      {: d.addVariable(new C0AST.Ident(i)); return new Symbol(d); :}
    ;

Statement
    = IDENT.i ASSIGN SimpleExpr.e SEMICOLON
      {: return new Symbol(new Statement.AssignmentStatement(i, e)); :}
    | IF LPAREN BoolExpr.e RPAREN Statement.s
      {: return new Symbol(new Statement.IfStatement(e, s)); :}
    | IF LPAREN BoolExpr.e RPAREN Statement.s1 ELSE Statement.s2
      {: return new Symbol(new Statement.IfElseStatement(e, s1, s2)); :}
    | WHILE LPAREN BoolExpr.e RPAREN Statement.s
      {: return new Symbol(new Statement.WhileStatement(e, s)); :}
    | PRINTF LPAREN DFORMAT COMMA IDENT.i RPAREN SEMICOLON
      {: return new Symbol(new Statement.PrintfStatement(i)); :}
    | SCANF LPAREN IFORMAT COMMA AMP IDENT.i RPAREN SEMICOLON
      {: return new Symbol(new Statement.ScanfStatement(i)); :}
    | LBRACKET StatementSequence.s RBRACKET
      {: return new Symbol(new Statement.CompStatement(s)); :}
    ;

BoolExpr
    = SimpleExpr.l Relation.rel SimpleExpr.r
      {: return new Symbol(new BoolExpression(l, rel, r)); :}
    ;

Relation
    = EQ    {: return new Symbol(AtomicType.EQ); :}
    | NE    {: return new Symbol(AtomicType.NE); :}
    | LE    {: return new Symbol(AtomicType.LE); :}
    | GE    {: return new Symbol(AtomicType.GE); :}
    | LT    {: return new Symbol(AtomicType.LT); :}
    | GT    {: return new Symbol(AtomicType.GT); :}
    ;

SimpleExpr
    = Term.t
      {: return new Symbol(new SimpleExpr.UnaryPlusExpr(t)); :}
    | PLUS Term.t
      {: return new Symbol(new SimpleExpr.UnaryPlusExpr(t)); :}
    | MINUS Term.t
      {: return new Symbol(new SimpleExpr.UnaryMinusExpr(t)); :}
    | SimpleExpr.s PLUS Term.t
      {: return new Symbol(new SimpleExpr.PlusExpr(s, t)); :}
    | SimpleExpr.s MINUS Term.t
      {: return new Symbol(new SimpleExpr.MinusExpr(s, t)); :}
    ;

Term
    = Factor.f
      {: return new Symbol(new Term.FactorTerm(f)); :}
    | Term.t MULT Factor.f
      {: return new Symbol(new Term.MultTerm(t, f)); :} 
    | Term.t DIV Factor.f
      {: return new Symbol(new Term.DivTerm(t, f)); :}
    | Term.t MOD Factor.f
      {: return new Symbol(new Term.ModTerm(t, f)); :}
    ;

Factor
    = IDENT.i
      {: return new Symbol(new Factor.IdentFactor(i)); :}
    | NUMBER.n
      {: return new Symbol(new Factor.NumberFactor(convert(n))); :}
    | LPAREN SimpleExpr.s RPAREN
      {: return new Symbol(new Factor.CompExprFactor(s)); :}
    ;
