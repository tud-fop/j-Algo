/* WARNING: This is generated code. DO *NOT* MODIFY IT! */
package org.jalgo.module.am0c0.parser.c0;

import beaver.Symbol;
import beaver.Scanner;
import org.jalgo.module.am0c0.parser.ParserConstants;
import org.jalgo.module.am0c0.parser.c0.GeneratedC0Parser.Terminals;

%%

%class C0Scanner
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
Identifier = [:jletter:][:jletterdigit:]*
Number = 0 | [1-9][0-9]*
Comment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
Whitespace = {LineTerminator} | [\t\f ]

%%

{Comment}       { /* ignore */ }
{Whitespace}+   { /* ignore */ }
"#include"      { return symbol(Terminals.INCLUDE, "#include"); }
"<stdio.h>"     { return symbol(Terminals.STDIO, "<stdio.h>"); }
"int"           { return symbol(Terminals.INT, "int"); }
"main"          { return symbol(Terminals.MAIN, "main"); }
"("             { return symbol(Terminals.LPAREN, "("); }
")"             { return symbol(Terminals.RPAREN, ")"); }
"{"             { return symbol(Terminals.LBRACKET, "{"); }
"}"             { return symbol(Terminals.RBRACKET, "}"); }
"="             { return symbol(Terminals.ASSIGN, "="); }
","             { return symbol(Terminals.COMMA, ","); }
";"             { return symbol(Terminals.SEMICOLON, ";"); }
"&"             { return symbol(Terminals.AMP, "&"); }
"const"         { return symbol(Terminals.CONST, "const"); }
"if"            { return symbol(Terminals.IF, "if"); }
"else"          { return symbol(Terminals.ELSE, "else"); }
"while"         { return symbol(Terminals.WHILE, "while"); }
"printf"        { return symbol(Terminals.PRINTF, "printf"); }
"scanf"         { return symbol(Terminals.SCANF, "scanf"); }
"\"%i\""        { return symbol(Terminals.IFORMAT, "\"%i\""); }
"\"%d\""        { return symbol(Terminals.DFORMAT, "\"%d\""); }
"return" {Whitespace}+ "0"  { return symbol(Terminals.RETURN, "return 0"); }
"+"             { return symbol(Terminals.PLUS, "+"); }
"-"             { return symbol(Terminals.MINUS, "-"); }
"/"             { return symbol(Terminals.DIV, "/"); }
"*"             { return symbol(Terminals.MULT, "*"); }
"%"             { return symbol(Terminals.MOD, "%"); }
"=="            { return symbol(Terminals.EQ, "=="); }
"!="            { return symbol(Terminals.NE, "!="); }
">="            { return symbol(Terminals.GE, ">="); }
"<="            { return symbol(Terminals.LE, "<="); }
">"             { return symbol(Terminals.GT, ">"); }
"<"             { return symbol(Terminals.LT, "<"); }
{Identifier}    { return symbol(Terminals.IDENT, yytext()); }
{Number}        { return symbol(Terminals.NUMBER, yytext()); }
.|\n            { throw new Scanner.Exception(yyline + 1, yycolumn + 1, yytext()); }
