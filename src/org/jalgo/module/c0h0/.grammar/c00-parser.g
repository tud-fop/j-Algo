%package "org.jalgo.module.c0h0.parser";

%import "org.jalgo.module.c0h0.models.ast.*";
%import "org.jalgo.module.c0h0.models.ast.Number";
%import "beaver.Symbol";

%class "GeneratedC00Parser";

%init {:
        this.report = ErrorEvents.createReport();
:};

%embed {:
    public ErrorEvents getErrorEvents() {
        return (ErrorEvents) report;
    }
    private int convertToInt(String str) {
        return Integer.parseInt(str);
    }
:};

%terminals INCLUDE, STDIO;
%terminals IF, ELSE, WHILE, MAIN, PRINTF, SCANF, RETURN, INT;
%terminals IFORMAT, DFORMAT;
%terminals COMMA, SEMICOLON, AMP, ASSIGN;
%terminals PLUS, MINUS, MULT, DIV, MOD;
%terminals EQ, NE, GE, LE, GT, LT;
%terminals LBRACKET, RBRACKET, LPAREN, RPAREN;
%terminals NUMBER, IDENT;

%typeof Program = "Program";
%typeof Declaration = "Declaration";
%typeof ScanfSequence = "ScanfSequence";
%typeof PrintfStatement = "PrintfStatement";
%typeof Block = "Block";
%typeof Statement = "Statement";
%typeof Term = "Term";
%typeof Relation = "Relation";
%typeof RelationType = "RelationType";
%typeof Operation = "Operation";
%typeof OperationType = "OperationType";
%typeof IDENT = "String";
%typeof NUMBER = "String";

// resolve shift-reduce conflict (dangling else)
%nonassoc ELSE;
%nonassoc IF;

%goal Program;

Program
    = INCLUDE STDIO INT MAIN LPAREN RPAREN LBRACKET Declaration.d SEMICOLON ScanfSequence.c Block.b PrintfStatement.p RETURN SEMICOLON RBRACKET
      {: return new Symbol(new Program(d, c, b, p)); :}
    | INCLUDE STDIO INT MAIN LPAREN RPAREN LBRACKET Declaration.d SEMICOLON ScanfSequence.c PrintfStatement.p RETURN SEMICOLON RBRACKET
      {: return new Symbol(new Program(d, c, null, p)); :}
    ;

Declaration
    = INT IDENT.i
      {: return new Symbol(new Declaration(new Var(i))); :}
    | Declaration.d COMMA IDENT.i
      {: d.addVariable(new Var(i)); return new Symbol(d); :}
    ;

ScanfSequence
    =
      {: return new Symbol(new ScanfSequence()); :}
    | ScanfSequence.s SCANF LPAREN IFORMAT COMMA AMP IDENT.i RPAREN SEMICOLON 
      {: s.addScanf(new Var(i)); return new Symbol(s); :}
    ;

PrintfStatement
    = PRINTF LPAREN DFORMAT COMMA IDENT.i RPAREN SEMICOLON
      {: return new Symbol(new PrintfStatement(new Var(i))); :}
    ;

Block
    = LBRACKET RBRACKET
      {: return new Symbol(new Block(true)); :}
    | LBRACKET Block.b Statement.stat RBRACKET
      {: b.addStatement(stat); return new Symbol(b); :}
    | Statement.stat
      {: return new Symbol(new Block(stat, true)); :}
    | Block.b Statement.stat
      {: b.addStatement(stat); return new Symbol(b); :}
    ;

Statement
    = SEMICOLON
      {: return new Symbol(new Statement()); :}
    | IDENT.i ASSIGN Term.t SEMICOLON
      {: return new Symbol(new Assignment(new Var(i), t)); :}
    | IF LPAREN Relation.r RPAREN LBRACKET RBRACKET
      {: return new Symbol(new If(r, new Block(true), new Block(false))); :}
    | IF LPAREN Relation.r RPAREN Statement.s
      {: return new Symbol(new If(r, new Block(s, false), new Block(false))); :}
    | IF LPAREN Relation.r RPAREN LBRACKET Block.b RBRACKET
      {: return new Symbol(new If(r, b)); :}
    | IF LPAREN Relation.r RPAREN LBRACKET RBRACKET ELSE LBRACKET RBRACKET
      {: return new Symbol(new If(r, new Block(true), new Block(true))); :}
    | IF LPAREN Relation.r RPAREN LBRACKET RBRACKET ELSE Statement.s2
      {: return new Symbol(new If(r, new Block(true), new Block(s2, false))); :}
    | IF LPAREN Relation.r RPAREN LBRACKET RBRACKET ELSE LBRACKET Block.s2 RBRACKET
      {: return new Symbol(new If(r, new Block(true), s2)); :}
    | IF LPAREN Relation.r RPAREN Statement.s ELSE LBRACKET RBRACKET
      {: return new Symbol(new If(r, new Block(s, false), new Block(true))); :}
    | IF LPAREN Relation.r RPAREN LBRACKET Block.b RBRACKET ELSE LBRACKET RBRACKET
      {: return new Symbol(new If(r, b, new Block(true))); :}
    | IF LPAREN Relation.r RPAREN Statement.s1 ELSE Statement.s2
      {: return new Symbol(new If(r, new Block(s1, false), new Block(s2, false))); :}
    | IF LPAREN Relation.r RPAREN Statement.s1 ELSE LBRACKET Block.s2 RBRACKET
      {: return new Symbol(new If(r, new Block(s1, false), s2)); :}
    | IF LPAREN Relation.r RPAREN LBRACKET Block.s1 RBRACKET ELSE Statement.s2
      {: return new Symbol(new If(r, s1, new Block(s2, false))); :}
    | IF LPAREN Relation.r RPAREN LBRACKET Block.s1 RBRACKET ELSE LBRACKET Block.s2 RBRACKET
      {: return new Symbol(new If(r, s1, s2)); :}
    | WHILE LPAREN Relation.r RPAREN Statement.s
      {: return new Symbol(new While(r, new Block(s, false))); :}
    | WHILE LPAREN Relation.r RPAREN LBRACKET Block.b RBRACKET
      {: return new Symbol(new While(r, b)); :}
    | WHILE LPAREN Relation.r RPAREN LBRACKET RBRACKET
      {: return new Symbol(new While(r, new Block(true))); :}
    ;

Term
    = IDENT.i
      {: return new Symbol(new Var(i)); :}
    | NUMBER.n
      {: return new Symbol(new Number(convertToInt(n))); :}
    | LPAREN Term.t RPAREN
      {: return new Symbol(new Term(t)); :}
    | PLUS IDENT.i
      {: return new Symbol(new Var(i, UnaryType.PLUS)); :}
    | PLUS NUMBER.n
      {: return new Symbol(new Number(convertToInt(n), UnaryType.PLUS)); :}
    | PLUS LPAREN Term.t RPAREN
      {: return new Symbol(new Term(t, UnaryType.PLUS)); :} 
    | MINUS IDENT.i
      {: return new Symbol(new Var(i, UnaryType.MINUS)); :}
    | MINUS NUMBER.n
      {: return new Symbol(new Number(convertToInt(n), UnaryType.MINUS)); :}
    | MINUS LPAREN Term.t RPAREN
      {: return new Symbol(new Term(t, UnaryType.MINUS)); :} 
    | Operation.o
      {: return new Symbol(o); :}
    | Relation.r
      {: return new Symbol(r); :}
    ;

Relation
    = Term.l RelationType.rel Term.r
      {: return new Symbol(new Relation(l, rel, r)); :}
    ;

RelationType
    = EQ    {: return new Symbol(RelationType.EQ); :}
    | NE    {: return new Symbol(RelationType.NE); :}
    | LE    {: return new Symbol(RelationType.LE); :}
    | GE    {: return new Symbol(RelationType.GE); :}
    | LT    {: return new Symbol(RelationType.LT); :}
    | GT    {: return new Symbol(RelationType.GT); :}
    ;

Operation
    = Term.l OperationType.op Term.r
      {: return new Symbol(new Operation(l, op, r)); :}
    ;

OperationType
    = PLUS  {: return new Symbol(OperationType.ADD); :}
    | MINUS {: return new Symbol(OperationType.SUB); :}
    | MULT  {: return new Symbol(OperationType.MUL); :}
    | DIV   {: return new Symbol(OperationType.DIV); :}
    | MOD   {: return new Symbol(OperationType.MOD); :}
    ;