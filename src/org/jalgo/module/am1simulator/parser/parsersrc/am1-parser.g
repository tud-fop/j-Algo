// Author: Max Leuthäuser
%header {:
/* WARNING: This is generated code. DO *NOT* MODIFY IT! */
:};
%package "parser.am1";
%import "parser.ErrorEvents";
%import "parser.ParserUtils";
%import "model.LineAddress";
%import "model.AddressException";
%import "model.am1.*";
%import "model.am1.AbstractStatementFactory.Statement";

%class "GeneratedAM1Parser";

%embed {:
	private ArithmeticStatementFactory arithmetic = new ArithmeticStatementFactory();
	private CompareStatementFactory compare = new CompareStatementFactory();
	private IOStatementFactory io = new IOStatementFactory();
	private JumpStatementFactory jump = new JumpStatementFactory();
	private MemoryStatementFactory memory = new MemoryStatementFactory();
	private ProceduralStatementFactory procedural = new ProceduralStatementFactory();
		
    private LineAddress address(Symbol symbol) {
        try {
            return new LineAddress(Symbol.getLine(symbol.getStart()));
        } catch (AddressException e) {
            return null;
        }
    }

    public ErrorEvents getErrorEvents() {
        return (ErrorEvents) report;
    }
:};

%init {:
        this.report = ErrorEvents.forAm1();
:};

%terminals ADD, MUL, SUB, DIV, MOD;
%terminals EQ, NE, LT, GT, LE, GE;
%terminals LOAD, STORE, LIT;
%terminals LOADA, LOADI, STOREI;
%terminals JMP, JMC;
%terminals WRITE, READ;
%terminals WRITEI, READI;
%terminals PUSH, CALL, INIT, RET;
%terminals EOL, SEMICOLON, COMMA;
%terminals NUMBER, MINUS, LBRAKET, RBRAKET;
%terminals GLOBAL, LOCAL;

%goal Program;

%typeof CommandList, Program = "AM1Program";
%typeof Command = "SimulationStatement";
%typeof UnsignedNumber, Number = "Integer";
%typeof Location = "String";

Program
    = CommandList
    ;

CommandList
    = Command.a SEMICOLON
    {: AM1Program program = new AM1Program(); program.add(a); return new Symbol(program); :}
    | CommandList.a EOL Command.b SEMICOLON
    {: a.add(b); return new Symbol(a); :}
    ;

Command
    = ADD.a
    {: return new Symbol(arithmetic.newStatement(Statement.ADD, new StatementResource.Builder(address(a)).build())); :}
    | MUL.a
    {: return new Symbol(arithmetic.newStatement(Statement.MUL, new StatementResource.Builder(address(a)).build())); :}
    | SUB.a
    {: return new Symbol(arithmetic.newStatement(Statement.SUB, new StatementResource.Builder(address(a)).build())); :}
    | DIV.a
    {: return new Symbol(arithmetic.newStatement(Statement.DIV, new StatementResource.Builder(address(a)).build())); :}
    | MOD.a
    {: return new Symbol(arithmetic.newStatement(Statement.MOD, new StatementResource.Builder(address(a)).build())); :}
    | EQ.a
    {: return new Symbol(compare.newStatement(Statement.EQUAL, new StatementResource.Builder(address(a)).build())); :}
    | NE.a
    {: return new Symbol(compare.newStatement(Statement.NOTEQUAL, new StatementResource.Builder(address(a)).build())); :}
    | LT.a
    {: return new Symbol(compare.newStatement(Statement.LESSERTHEN, new StatementResource.Builder(address(a)).build())); :}
    | GT.a
    {: return new Symbol(compare.newStatement(Statement.GREATERTHEN, new StatementResource.Builder(address(a)).build())); :}
    | LE.a
    {: return new Symbol(compare.newStatement(Statement.LESSEREQUAL, new StatementResource.Builder(address(a)).build())); :}
    | GE.a
    {: return new Symbol(compare.newStatement(Statement.GREATEREQUAL, new StatementResource.Builder(address(a)).build())); :}
    | JMP.a UnsignedNumber.b
    {: return new Symbol(jump.newStatement(Statement.JMP, new StatementResource.Builder(address(a)).value(b).build())); :}
    | JMC.a UnsignedNumber.b
    {: return new Symbol(jump.newStatement(Statement.JMC, new StatementResource.Builder(address(a)).value(b).build())); :}
    | LOAD.a LBRAKET Location.b COMMA Number.c RBRAKET
    {: return new Symbol(memory.newStatement(Statement.LOAD, new StatementResource.Builder(address(a)).location(b).value(c).build())); :}
    | LOADA.a LBRAKET Location.b COMMA Number.c RBRAKET
    {: return new Symbol(memory.newStatement(Statement.LOADA, new StatementResource.Builder(address(a)).location(b).value(c).build())); :}
    | LOADI.a LBRAKET Number.b RBRAKET
    {: return new Symbol(memory.newStatement(Statement.LOADI, new StatementResource.Builder(address(a)).value(b).build())); :}
    | STORE.a LBRAKET Location.b COMMA Number.c RBRAKET
    {: return new Symbol(memory.newStatement(Statement.STORE, new StatementResource.Builder(address(a)).location(b).value(c).build())); :}
    | STOREI.a LBRAKET Number.b RBRAKET
    {: return new Symbol(memory.newStatement(Statement.STOREI, new StatementResource.Builder(address(a)).value(b).build())); :}
    | WRITE.a LBRAKET Location.b COMMA Number.c RBRAKET
    {: return new Symbol(io.newStatement(Statement.WRITE, new StatementResource.Builder(address(a)).location(b).value(c).build())); :}
    | WRITEI.a LBRAKET Number.b RBRAKET
    {: return new Symbol(io.newStatement(Statement.WRITEI, new StatementResource.Builder(address(a)).value(b).build())); :}
    | READ.a LBRAKET Location.b COMMA Number.c RBRAKET
    {: return new Symbol(io.newStatement(Statement.READ, new StatementResource.Builder(address(a)).location(b).value(c).build())); :}
    | READI.a LBRAKET Number.b RBRAKET
    {: return new Symbol(io.newStatement(Statement.READI, new StatementResource.Builder(address(a)).value(b).build())); :}
    | PUSH.a
    {: return new Symbol(procedural.newStatement(Statement.PUSH, new StatementResource.Builder(address(a)).build())); :}
    | CALL.a UnsignedNumber.b
    {: return new Symbol(procedural.newStatement(Statement.CALL, new StatementResource.Builder(address(a)).value(b).build())); :}
    | INIT.a UnsignedNumber.b
    {: return new Symbol(procedural.newStatement(Statement.INIT, new StatementResource.Builder(address(a)).value(b).build())); :}
    | RET.a UnsignedNumber.b
    {: return new Symbol(procedural.newStatement(Statement.RET, new StatementResource.Builder(address(a)).value(b).build())); :}
    | LIT.a Number.b
    {: return new Symbol(procedural.newStatement(Statement.LIT, new StatementResource.Builder(address(a)).value(b).build())); :}
    ;

UnsignedNumber
    = NUMBER.a
    {: return new Symbol(ParserUtils.safeSymbolToInt(a, (ErrorEvents) report)); :}
    ;

Location
	= LOCAL
	{: return new Symbol("lokal"); :}
	| GLOBAL
	{: return new Symbol("global"); :}
	;
	
Number
    = UnsignedNumber.a
    {: return new Symbol(a); :}
    | MINUS UnsignedNumber.a
    {: return new Symbol(-a); :}
    ;

