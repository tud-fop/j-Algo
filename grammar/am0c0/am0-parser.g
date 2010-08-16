// Author: Martin Morgenstern
%header {:
/* WARNING: This is generated code. DO *NOT* MODIFY IT! */
:};
%package "org.jalgo.module.am0c0.parser.am0";
%import "org.jalgo.module.am0c0.parser.ErrorEvents";
%import "org.jalgo.module.am0c0.parser.ParserUtils";
%import "org.jalgo.module.am0c0.model.LineAddress";
%import "org.jalgo.module.am0c0.model.AddressException";
%import "org.jalgo.module.am0c0.model.am0.*";

%class "GeneratedAM0Parser";

%embed {:
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
        this.report = ErrorEvents.forAm0();
:};

%terminals ADD, MUL, SUB, DIV, MOD;
%terminals EQ, NE, LT, GT, LE, GE;
%terminals LOAD, STORE, LIT;
%terminals JMP, JMC;
%terminals WRITE, READ;
%terminals EOL, SEMICOLON;
%terminals NUMBER, MINUS;

%goal Program;

%typeof CommandList, Program = "AM0Program";
%typeof Command = "SimulationStatement";
%typeof UnsignedNumber, Number = "Integer";

Program
    = CommandList
    ;

CommandList
    = Command.a SEMICOLON
    {: AM0Program program = new AM0Program(); program.add(a); return new Symbol(program); :}
    | CommandList.a EOL Command.b SEMICOLON
    {: a.add(b); return new Symbol(a); :}
    ;

Command
    = ADD.a
    {: return new Symbol(new Add(address(a))); :}
    | MUL.a
    {: return new Symbol(new Mul(address(a))); :}
    | SUB.a
    {: return new Symbol(new Sub(address(a))); :}
    | DIV.a
    {: return new Symbol(new Div(address(a))); :}
    | MOD.a
    {: return new Symbol(new Mod(address(a))); :}
    | EQ.a
    {: return new Symbol(new Equal(address(a))); :}
    | NE.a
    {: return new Symbol(new NotEqual(address(a))); :}
    | LT.a
    {: return new Symbol(new LesserThen(address(a))); :}
    | GT.a
    {: return new Symbol(new GreaterThen(address(a))); :}
    | LE.a
    {: return new Symbol(new LesserEqual(address(a))); :}
    | GE.a
    {: return new Symbol(new GreaterEqual(address(a))); :}
    | JMP.a UnsignedNumber.b
    {: return new Symbol(new Jmp(address(a), b)); :}
    | JMC.a UnsignedNumber.b
    {: return new Symbol(new Jmc(address(a), b)); :}
    | LOAD.a UnsignedNumber.b
    {: return new Symbol(new Load(address(a), b)); :}
    | STORE.a UnsignedNumber.b
    {: return new Symbol(new Store(address(a), b)); :}
    | LIT.a Number.b
    {: return new Symbol(new Lit(address(a), b)); :}
    | WRITE.a UnsignedNumber.b
    {: return new Symbol(new Write(address(a), b)); :}
    | READ.a UnsignedNumber.b
    {: return new Symbol(new Read(address(a), b)); :}
    ;

UnsignedNumber
    = NUMBER.a
    {: return new Symbol(ParserUtils.safeSymbolToInt(a, (ErrorEvents) report)); :}
    ;

Number
    = UnsignedNumber.a
    {: return new Symbol(a); :}
    | MINUS UnsignedNumber.a
    {: return new Symbol(-a); :}
    ;

