grammar LambdaCalculus;

	
options {
	output = AST;
	}
	
tokens {
	LAMBDA = '\u03BB';
	POINT = '.';
	POPEN = '(';
	PCLOSE = ')';
	SPOPEN = '<';
	SPCLOSE = '>';
	ABSTRACTION;
	APPLICATION;
	ATOM;
	SHORTCUT;
	}
	
@header {
  package parser;
  import model.*;

}

@lexer::header {

  package parser;
  import model.*;

}

@members {


	Term t;
protected void mismatch(IntStream input, int ttype, BitSet follow)
    throws RecognitionException
{
    System.out.println("aki1");
    throw new MismatchedTokenException(ttype, input);

}
protected Object recoverFromMismatchedToken(IntStream input,
                                            int ttype,
                                            BitSet follow)
    throws RecognitionException
{   
       System.out.println("aki2");
    throw new MismatchedTokenException(ttype, input);
}   


}



// Alter code generation so catch-clauses get replace with
// this action.
@rulecatch {
catch (RecognitionException e) {
    throw e;
}
}
// END:override 	LAMBDA = '\u03BB';


/*------------------------------------------------------------------
 * PARSER RULES  
 *------------------------------------------------------------------*/
 
start	:	term EOF;
term	:	atom
		| abstraction 
		| application 
		| shortcut
		/*| (POPEN term PCLOSE)=> term  /* FIXME: this should go */
		| (POPEN atom PCLOSE)=> atom  /*FIXME: this should go */
		;


atom 	:	variable 
	| 	constant
	;
variable:	v=VAR -> ^(ATOM $v);
constant:	c=CONS -> ^(ATOM $c);

abstraction
	:	POPEN LAMBDA aa=variable POINT at=term PCLOSE -> ^(ABSTRACTION $aa $at) ;
application
	:	POPEN t1=term t2=term PCLOSE -> ^(APPLICATION $t1 $t2);
shortcut:	s=SHORTCUTID -> ^(SHORTCUT $s);
/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

VAR 	:	('k'..'z');
CONS	:	('a'..'j' | '+' | '-' | '*' | '/' | '%' );

SHORTCUTID	
	:	SPOPEN(LETTER)+SPCLOSE;
fragment LETTER
	:	('a'..'z'|'A'..'Z'|'0'..'9');
WHITESPACE : 	( '\t' | ' ' | '\r' | '\n'| '\u000C' )+ 	{ $channel = HIDDEN; } ;

COMMENT
    :   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;}
    ;
LINE_COMMENT
    : '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    ;	
