grammar UnificationSet;
options {
	output=AST;
	ASTLabelType=CommonTree;
	}

tokens {
	X	= 'x' ;
	Y	= 'y' ;
	Z	= 'z' ;
	U	= 'u' ;
	V	= 'v' ;
	W	= 'w' ;
	LBRACKET = '(';
	RBRACKET = ')';
	
	STARTSET = 'm=';
	COMMA = ',';
	OPENBRACE = '{';
	CLOSEBRACE = '}';
	SET;	
	PAIR;
	CONSTRUCTOR;
	VARIABLE;
}

@lexer::header{
	package org.jalgo.module.unifikation.parser;
}

@parser::header{
	package org.jalgo.module.unifikation.parser;
	import java.util.Map;
	import java.util.HashMap;
	import java.util.TreeMap;
	import java.util.List;
	import java.util.ArrayList;
}

@parser::members{
	//Constructorsymbol->arity->position
	public Map<String,Map<Integer,List<Integer>>> constructorArity=new HashMap<String,Map<Integer,List<Integer>>>();
	private void AddConstructorArity(String constructorSymbol, int arity, Token tPos){
		int pos=-1;
		if(tPos!=null) pos=tPos.getCharPositionInLine();
		if(!constructorArity.containsKey(constructorSymbol)){
			constructorArity.put(constructorSymbol, new TreeMap<Integer,List<Integer>>());
		}
		Map<Integer,List<Integer>> arityMap=constructorArity.get(constructorSymbol);
		if(!arityMap.containsKey(arity)){
			arityMap.put(arity, new ArrayList<Integer>());
		}
		arityMap.get(arity).add(pos);
	}
}

unificationset	:	set^;
set	:	STARTSET OPENBRACE ws (pair ( ws COMMA ws pair)* ws)? CLOSEBRACE -> ^(SET pair*);

pair	:	LBRACKET ws first=term	COMMA second=term ws RBRACKET ->^(PAIR $first $second);

term	:	constructor | variable ;

constructor	:	symbol=constructorsymbol (ws LBRACKET ws (ids+=term (ws COMMA ws  ids+=term)* ws)? RBRACKET)? {AddConstructorArity($symbol.text,$ids==null?0:$ids.size(),$symbol.start);} ->^(CONSTRUCTOR constructorsymbol term*);

constructorsymbol	:	ALPHA|BETA|GAMMA|DELTA|EPSILON|THETA;

variable 	:	varid INT? ->^(VARIABLE varid INT?);
varid	:	X|Y|Z|U|V|W;

ws!	:	WS?;

ALPHA 	: '\u03B1'|'a' ;
BETA 	: '\u03B2'|'b' ;
GAMMA 	: '\u03B3'|'c' ;
DELTA 	: '\u03B4'|'d' ;
EPSILON	: '\u03B5'|'e' ;
THETA	: '\u03B8'|'t' ;

INT :	'0'..'9'+ ;


WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        )+ {$channel=HIDDEN;}
    ;

