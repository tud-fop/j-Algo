/* WARNING: This is generated code. DO *NOT* MODIFY IT! */
/* Author: Max Leuthäuser */

package parser.am1;

import beaver.Symbol;
import beaver.Scanner;
import parser.ParserConstants;
import parser.am1.GeneratedAM1Parser.Terminals;

%%

%class AM1Scanner
%extends Scanner
%function nextToken
%type Symbol
%yylexthrow Scanner.Exception
%eofval{
    return symbol(Terminals.EOF, ParserConstants.EOF);
%eofval}
%unicode
%line
%column

%{
    private Symbol symbol(short id, Object value)
    {
        return new Symbol(id, yyline + 1, yycolumn + 1, yylength(), value);
    }
%}

LineTerminator = \n|\r|\r\n
Number = 0 | [1-9][0-9]*
Whitespace = [\t\f ]

%%

{Whitespace}+       { /* ignore */ }
{LineTerminator}    { return symbol(Terminals.EOL, ParserConstants.EOL); }
";"                 { return symbol(Terminals.SEMICOLON, ";"); }
"-"                 { return symbol(Terminals.MINUS, "-"); }
","                 { return symbol(Terminals.COMMA, ","); }
"("                 { return symbol(Terminals.LBRAKET, "("); }
")"                 { return symbol(Terminals.RBRAKET, ")"); }
"lokal"				{ return symbol(Terminals.LOCAL, "lokal"); }
"global"			{ return symbol(Terminals.GLOBAL, "global"); }
{Number}            { return symbol(Terminals.NUMBER, yytext()); }
"ADD"               { return symbol(Terminals.ADD, "ADD"); }
"MUL"               { return symbol(Terminals.MUL, "MUL"); }
"SUB"               { return symbol(Terminals.SUB, "SUB"); }
"DIV"               { return symbol(Terminals.DIV, "DIV"); }
"MOD"               { return symbol(Terminals.MOD, "MOD"); }
"EQ"                { return symbol(Terminals.EQ, "EQ"); }
"NE"                { return symbol(Terminals.NE, "NE"); }
"LT"                { return symbol(Terminals.LT, "LT"); }
"GT"                { return symbol(Terminals.GT, "GT"); }
"LE"                { return symbol(Terminals.LE, "LE"); }
"GE"                { return symbol(Terminals.GE, "GE"); }
"LOAD"              { return symbol(Terminals.LOAD, "LOAD"); }
"LOADA"             { return symbol(Terminals.LOADA, "LOADA"); }
"LOADI"             { return symbol(Terminals.LOADI, "LOADI"); }
"STORE"             { return symbol(Terminals.STORE, "STORE"); }
"STOREI"            { return symbol(Terminals.STOREI, "STOREI"); }
"LIT"               { return symbol(Terminals.LIT, "LIT"); }
"JMP"               { return symbol(Terminals.JMP, "JMP"); }
"JMC"               { return symbol(Terminals.JMC, "JMC"); }
"WRITE"             { return symbol(Terminals.WRITE, "WRITE"); }
"WRITEI"            { return symbol(Terminals.WRITEI, "WRITEI"); }
"READ"              { return symbol(Terminals.READ, "READ"); }
"READI"             { return symbol(Terminals.READI, "READI"); }
"PUSH"              { return symbol(Terminals.PUSH, "PUSH"); }
"CALL"              { return symbol(Terminals.CALL, "CALL"); }
"INIT"              { return symbol(Terminals.INIT, "INIT"); }
"RET"               { return symbol(Terminals.RET, "RET"); }
.|\n                { throw new Scanner.Exception(yyline + 1, yycolumn + 1, yytext()); }

