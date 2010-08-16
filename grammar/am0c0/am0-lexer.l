/* WARNING: This is generated code. DO *NOT* MODIFY IT! */
package org.jalgo.module.am0c0.parser.am0;

import beaver.Symbol;
import beaver.Scanner;
import org.jalgo.module.am0c0.parser.ParserConstants;
import org.jalgo.module.am0c0.parser.am0.GeneratedAM0Parser.Terminals;

%%

%class AM0Scanner
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
"STORE"             { return symbol(Terminals.STORE, "STORE"); }
"LIT"               { return symbol(Terminals.LIT, "LIT"); }
"JMP"               { return symbol(Terminals.JMP, "JMP"); }
"JMC"               { return symbol(Terminals.JMC, "JMC"); }
"WRITE"             { return symbol(Terminals.WRITE, "WRITE"); }
"READ"              { return symbol(Terminals.READ, "READ"); }
.|\n                { throw new Scanner.Exception(yyline + 1, yycolumn + 1, yytext()); }

