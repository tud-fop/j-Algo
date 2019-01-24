// $ANTLR 3.0b6 C00.g 2007-05-21 11:30:10

package c00.parser;


import o3b.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class C00Lexer extends Lexer {
    public static final int T14=14;
    public static final int T29=29;
    public static final int T36=36;
    public static final int T35=35;
    public static final int T45=45;
    public static final int T20=20;
    public static final int T34=34;
    public static final int Ident=4;
    public static final int T25=25;
    public static final int T18=18;
    public static final int T37=37;
    public static final int EscapeSequence=8;
    public static final int T26=26;
    public static final int T32=32;
    public static final int T17=17;
    public static final int T51=51;
    public static final int T46=46;
    public static final int T16=16;
    public static final int DIGIT=10;
    public static final int T38=38;
    public static final int T41=41;
    public static final int T24=24;
    public static final int T19=19;
    public static final int T39=39;
    public static final int T21=21;
    public static final int T44=44;
    public static final int LETTER=9;
    public static final int T33=33;
    public static final int T11=11;
    public static final int T22=22;
    public static final int T50=50;
    public static final int T43=43;
    public static final int T12=12;
    public static final int T23=23;
    public static final int T28=28;
    public static final int T42=42;
    public static final int T40=40;
    public static final int T13=13;
    public static final int WHITESPACE=7;
    public static final int String=6;
    public static final int T48=48;
    public static final int T15=15;
    public static final int EOF=-1;
    public static final int T47=47;
    public static final int Tokens=53;
    public static final int T31=31;
    public static final int T49=49;
    public static final int T27=27;
    public static final int T52=52;
    public static final int Number=5;
    public static final int T30=30;
    public C00Lexer() {;} 
    public C00Lexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "C00.g"; }

    // $ANTLR start T11
    public void mT11() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T11;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:6:7: ( '#include' )
            // C00.g:6:7: '#include'
            {
            match("#include"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T11

    // $ANTLR start T12
    public void mT12() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T12;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:7:7: ( '<stdio.h>' )
            // C00.g:7:7: '<stdio.h>'
            {
            match("<stdio.h>"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T12

    // $ANTLR start T13
    public void mT13() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T13;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:8:7: ( 'int' )
            // C00.g:8:7: 'int'
            {
            match("int"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T13

    // $ANTLR start T14
    public void mT14() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T14;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:9:7: ( 'main' )
            // C00.g:9:7: 'main'
            {
            match("main"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T14

    // $ANTLR start T15
    public void mT15() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T15;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:10:7: ( '(' )
            // C00.g:10:7: '('
            {
            match('('); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T15

    // $ANTLR start T16
    public void mT16() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T16;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:11:7: ( ')' )
            // C00.g:11:7: ')'
            {
            match(')'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T16

    // $ANTLR start T17
    public void mT17() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T17;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:12:7: ( ';' )
            // C00.g:12:7: ';'
            {
            match(';'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T17

    // $ANTLR start T18
    public void mT18() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T18;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:13:7: ( 'const' )
            // C00.g:13:7: 'const'
            {
            match("const"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T18

    // $ANTLR start T19
    public void mT19() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T19;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:14:7: ( '=' )
            // C00.g:14:7: '='
            {
            match('='); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T19

    // $ANTLR start T20
    public void mT20() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T20;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:15:7: ( ',' )
            // C00.g:15:7: ','
            {
            match(','); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T20

    // $ANTLR start T21
    public void mT21() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T21;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:16:7: ( '*' )
            // C00.g:16:7: '*'
            {
            match('*'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T21

    // $ANTLR start T22
    public void mT22() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T22;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:17:7: ( 'void' )
            // C00.g:17:7: 'void'
            {
            match("void"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T22

    // $ANTLR start T23
    public void mT23() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T23;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:18:7: ( '{' )
            // C00.g:18:7: '{'
            {
            match('{'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T23

    // $ANTLR start T24
    public void mT24() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T24;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:19:7: ( '}' )
            // C00.g:19:7: '}'
            {
            match('}'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T24

    // $ANTLR start T25
    public void mT25() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T25;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:20:7: ( 'if' )
            // C00.g:20:7: 'if'
            {
            match("if"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T25

    // $ANTLR start T26
    public void mT26() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T26;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:21:7: ( 'switch' )
            // C00.g:21:7: 'switch'
            {
            match("switch"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T26

    // $ANTLR start T27
    public void mT27() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T27;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:22:7: ( 'while' )
            // C00.g:22:7: 'while'
            {
            match("while"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T27

    // $ANTLR start T28
    public void mT28() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T28;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:23:7: ( 'do' )
            // C00.g:23:7: 'do'
            {
            match("do"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T28

    // $ANTLR start T29
    public void mT29() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T29;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:24:7: ( 'for' )
            // C00.g:24:7: 'for'
            {
            match("for"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T29

    // $ANTLR start T30
    public void mT30() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T30;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:25:7: ( 'continue' )
            // C00.g:25:7: 'continue'
            {
            match("continue"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T30

    // $ANTLR start T31
    public void mT31() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T31;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:26:7: ( 'break' )
            // C00.g:26:7: 'break'
            {
            match("break"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T31

    // $ANTLR start T32
    public void mT32() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T32;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:27:7: ( 'return' )
            // C00.g:27:7: 'return'
            {
            match("return"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T32

    // $ANTLR start T33
    public void mT33() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T33;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:28:7: ( 'printf' )
            // C00.g:28:7: 'printf'
            {
            match("printf"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T33

    // $ANTLR start T34
    public void mT34() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T34;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:29:7: ( 'scanf' )
            // C00.g:29:7: 'scanf'
            {
            match("scanf"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T34

    // $ANTLR start T35
    public void mT35() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T35;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:30:7: ( '\"%i\"' )
            // C00.g:30:7: '\"%i\"'
            {
            match("\"%i\""); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T35

    // $ANTLR start T36
    public void mT36() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T36;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:31:7: ( '&' )
            // C00.g:31:7: '&'
            {
            match('&'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T36

    // $ANTLR start T37
    public void mT37() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T37;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:32:7: ( '/*label' )
            // C00.g:32:7: '/*label'
            {
            match("/*label"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T37

    // $ANTLR start T38
    public void mT38() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T38;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:33:7: ( '*/' )
            // C00.g:33:7: '*/'
            {
            match("*/"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T38

    // $ANTLR start T39
    public void mT39() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T39;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:34:7: ( 'else' )
            // C00.g:34:7: 'else'
            {
            match("else"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T39

    // $ANTLR start T40
    public void mT40() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T40;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:35:7: ( 'default' )
            // C00.g:35:7: 'default'
            {
            match("default"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T40

    // $ANTLR start T41
    public void mT41() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T41;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:36:7: ( ':' )
            // C00.g:36:7: ':'
            {
            match(':'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T41

    // $ANTLR start T42
    public void mT42() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T42;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:37:7: ( 'case' )
            // C00.g:37:7: 'case'
            {
            match("case"); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T42

    // $ANTLR start T43
    public void mT43() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T43;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:38:7: ( '+' )
            // C00.g:38:7: '+'
            {
            match('+'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T43

    // $ANTLR start T44
    public void mT44() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T44;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:39:7: ( '-' )
            // C00.g:39:7: '-'
            {
            match('-'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T44

    // $ANTLR start T45
    public void mT45() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T45;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:40:7: ( '/' )
            // C00.g:40:7: '/'
            {
            match('/'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T45

    // $ANTLR start T46
    public void mT46() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T46;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:41:7: ( '%' )
            // C00.g:41:7: '%'
            {
            match('%'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T46

    // $ANTLR start T47
    public void mT47() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T47;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:42:7: ( '==' )
            // C00.g:42:7: '=='
            {
            match("=="); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T47

    // $ANTLR start T48
    public void mT48() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T48;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:43:7: ( '!=' )
            // C00.g:43:7: '!='
            {
            match("!="); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T48

    // $ANTLR start T49
    public void mT49() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T49;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:44:7: ( '<' )
            // C00.g:44:7: '<'
            {
            match('<'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T49

    // $ANTLR start T50
    public void mT50() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T50;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:45:7: ( '>' )
            // C00.g:45:7: '>'
            {
            match('>'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T50

    // $ANTLR start T51
    public void mT51() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T51;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:46:7: ( '<=' )
            // C00.g:46:7: '<='
            {
            match("<="); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T51

    // $ANTLR start T52
    public void mT52() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T52;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:47:7: ( '>=' )
            // C00.g:47:7: '>='
            {
            match(">="); 


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T52

    // $ANTLR start WHITESPACE
    public void mWHITESPACE() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = WHITESPACE;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:493:14: ( ( ('\\t'|' '|'\\r'|'\\n'|'\\u000C'))+ )
            // C00.g:493:14: ( ('\\t'|' '|'\\r'|'\\n'|'\\u000C'))+
            {
            // C00.g:493:14: ( ('\\t'|' '|'\\r'|'\\n'|'\\u000C'))+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);
                if ( ((LA1_0>='\t' && LA1_0<='\n')||(LA1_0>='\f' && LA1_0<='\r')||LA1_0==' ') ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C00.g:493:16: ('\\t'|' '|'\\r'|'\\n'|'\\u000C')
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||(input.LA(1)>='\f' && input.LA(1)<='\r')||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);

             _channel = HIDDEN; 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end WHITESPACE

    // $ANTLR start String
    public void mString() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = String;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:495:10: ( '\"' ( EscapeSequence | ~ ('\\\\'|'\"'))* '\"' )
            // C00.g:495:10: '\"' ( EscapeSequence | ~ ('\\\\'|'\"'))* '\"'
            {
            match('\"'); 
            // C00.g:495:14: ( EscapeSequence | ~ ('\\\\'|'\"'))*
            loop2:
            do {
                int alt2=3;
                int LA2_0 = input.LA(1);
                if ( (LA2_0=='\\') ) {
                    alt2=1;
                }
                else if ( ((LA2_0>='\u0000' && LA2_0<='!')||(LA2_0>='#' && LA2_0<='[')||(LA2_0>=']' && LA2_0<='\uFFFE')) ) {
                    alt2=2;
                }


                switch (alt2) {
            	case 1 :
            	    // C00.g:495:16: EscapeSequence
            	    {
            	    mEscapeSequence(); 

            	    }
            	    break;
            	case 2 :
            	    // C00.g:495:33: ~ ('\\\\'|'\"')
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFE') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            match('\"'); 

            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end String

    // $ANTLR start Ident
    public void mIdent() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = Ident;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:496:10: ( LETTER ( LETTER | DIGIT )* )
            // C00.g:496:10: LETTER ( LETTER | DIGIT )*
            {
            mLETTER(); 
            // C00.g:496:17: ( LETTER | DIGIT )*
            loop3:
            do {
                int alt3=3;
                int LA3_0 = input.LA(1);
                if ( ((LA3_0>='A' && LA3_0<='Z')||(LA3_0>='a' && LA3_0<='z')) ) {
                    alt3=1;
                }
                else if ( ((LA3_0>='0' && LA3_0<='9')) ) {
                    alt3=2;
                }


                switch (alt3) {
            	case 1 :
            	    // C00.g:496:18: LETTER
            	    {
            	    mLETTER(); 

            	    }
            	    break;
            	case 2 :
            	    // C00.g:496:27: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end Ident

    // $ANTLR start Number
    public void mNumber() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = Number;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C00.g:497:10: ( ( DIGIT )+ )
            // C00.g:497:10: ( DIGIT )+
            {
            // C00.g:497:10: ( DIGIT )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);
                if ( ((LA4_0>='0' && LA4_0<='9')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C00.g:497:11: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);


            }



                    if ( token==null && ruleNestingLevel==1 ) {
                        emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                    }

                        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end Number

    // $ANTLR start DIGIT
    public void mDIGIT() throws RecognitionException {
        try {
            ruleNestingLevel++;
            // C00.g:499:27: ( '0' .. '9' )
            // C00.g:499:27: '0' .. '9'
            {
            matchRange('0','9'); 

            }

        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end DIGIT

    // $ANTLR start LETTER
    public void mLETTER() throws RecognitionException {
        try {
            ruleNestingLevel++;
            // C00.g:500:27: ( ('A'..'Z'|'a'..'z'))
            // C00.g:500:27: ('A'..'Z'|'a'..'z')
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }


            }

        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end LETTER

    // $ANTLR start EscapeSequence
    public void mEscapeSequence() throws RecognitionException {
        try {
            ruleNestingLevel++;
            // C00.g:501:27: ( '\\\\' ('t'|'n'|'r'|'\\\"'|'\\''|'\\\\'))
            // C00.g:501:27: '\\\\' ('t'|'n'|'r'|'\\\"'|'\\''|'\\\\')
            {
            match('\\'); 
            if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }


            }

        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end EscapeSequence

    public void mTokens() throws RecognitionException {
        // C00.g:1:10: ( T11 | T12 | T13 | T14 | T15 | T16 | T17 | T18 | T19 | T20 | T21 | T22 | T23 | T24 | T25 | T26 | T27 | T28 | T29 | T30 | T31 | T32 | T33 | T34 | T35 | T36 | T37 | T38 | T39 | T40 | T41 | T42 | T43 | T44 | T45 | T46 | T47 | T48 | T49 | T50 | T51 | T52 | WHITESPACE | String | Ident | Number )
        int alt5=46;
        switch ( input.LA(1) ) {
        case '#':
            alt5=1;
            break;
        case '<':
            switch ( input.LA(2) ) {
            case '=':
                alt5=41;
                break;
            case 's':
                alt5=2;
                break;
            default:
                alt5=39;}

            break;
        case 'i':
            switch ( input.LA(2) ) {
            case 'n':
                int LA5_38 = input.LA(3);
                if ( (LA5_38=='t') ) {
                    int LA5_64 = input.LA(4);
                    if ( ((LA5_64>='0' && LA5_64<='9')||(LA5_64>='A' && LA5_64<='Z')||(LA5_64>='a' && LA5_64<='z')) ) {
                        alt5=45;
                    }
                    else {
                        alt5=3;}
                }
                else {
                    alt5=45;}
                break;
            case 'f':
                int LA5_39 = input.LA(3);
                if ( ((LA5_39>='0' && LA5_39<='9')||(LA5_39>='A' && LA5_39<='Z')||(LA5_39>='a' && LA5_39<='z')) ) {
                    alt5=45;
                }
                else {
                    alt5=15;}
                break;
            default:
                alt5=45;}

            break;
        case 'm':
            int LA5_4 = input.LA(2);
            if ( (LA5_4=='a') ) {
                int LA5_40 = input.LA(3);
                if ( (LA5_40=='i') ) {
                    int LA5_66 = input.LA(4);
                    if ( (LA5_66=='n') ) {
                        int LA5_82 = input.LA(5);
                        if ( ((LA5_82>='0' && LA5_82<='9')||(LA5_82>='A' && LA5_82<='Z')||(LA5_82>='a' && LA5_82<='z')) ) {
                            alt5=45;
                        }
                        else {
                            alt5=4;}
                    }
                    else {
                        alt5=45;}
                }
                else {
                    alt5=45;}
            }
            else {
                alt5=45;}
            break;
        case '(':
            alt5=5;
            break;
        case ')':
            alt5=6;
            break;
        case ';':
            alt5=7;
            break;
        case 'c':
            switch ( input.LA(2) ) {
            case 'o':
                int LA5_41 = input.LA(3);
                if ( (LA5_41=='n') ) {
                    switch ( input.LA(4) ) {
                    case 't':
                        int LA5_83 = input.LA(5);
                        if ( (LA5_83=='i') ) {
                            int LA5_98 = input.LA(6);
                            if ( (LA5_98=='n') ) {
                                int LA5_111 = input.LA(7);
                                if ( (LA5_111=='u') ) {
                                    int LA5_120 = input.LA(8);
                                    if ( (LA5_120=='e') ) {
                                        int LA5_125 = input.LA(9);
                                        if ( ((LA5_125>='0' && LA5_125<='9')||(LA5_125>='A' && LA5_125<='Z')||(LA5_125>='a' && LA5_125<='z')) ) {
                                            alt5=45;
                                        }
                                        else {
                                            alt5=20;}
                                    }
                                    else {
                                        alt5=45;}
                                }
                                else {
                                    alt5=45;}
                            }
                            else {
                                alt5=45;}
                        }
                        else {
                            alt5=45;}
                        break;
                    case 's':
                        int LA5_84 = input.LA(5);
                        if ( (LA5_84=='t') ) {
                            int LA5_99 = input.LA(6);
                            if ( ((LA5_99>='0' && LA5_99<='9')||(LA5_99>='A' && LA5_99<='Z')||(LA5_99>='a' && LA5_99<='z')) ) {
                                alt5=45;
                            }
                            else {
                                alt5=8;}
                        }
                        else {
                            alt5=45;}
                        break;
                    default:
                        alt5=45;}

                }
                else {
                    alt5=45;}
                break;
            case 'a':
                int LA5_42 = input.LA(3);
                if ( (LA5_42=='s') ) {
                    int LA5_68 = input.LA(4);
                    if ( (LA5_68=='e') ) {
                        int LA5_85 = input.LA(5);
                        if ( ((LA5_85>='0' && LA5_85<='9')||(LA5_85>='A' && LA5_85<='Z')||(LA5_85>='a' && LA5_85<='z')) ) {
                            alt5=45;
                        }
                        else {
                            alt5=32;}
                    }
                    else {
                        alt5=45;}
                }
                else {
                    alt5=45;}
                break;
            default:
                alt5=45;}

            break;
        case '=':
            int LA5_9 = input.LA(2);
            if ( (LA5_9=='=') ) {
                alt5=37;
            }
            else {
                alt5=9;}
            break;
        case ',':
            alt5=10;
            break;
        case '*':
            int LA5_11 = input.LA(2);
            if ( (LA5_11=='/') ) {
                alt5=28;
            }
            else {
                alt5=11;}
            break;
        case 'v':
            int LA5_12 = input.LA(2);
            if ( (LA5_12=='o') ) {
                int LA5_47 = input.LA(3);
                if ( (LA5_47=='i') ) {
                    int LA5_69 = input.LA(4);
                    if ( (LA5_69=='d') ) {
                        int LA5_86 = input.LA(5);
                        if ( ((LA5_86>='0' && LA5_86<='9')||(LA5_86>='A' && LA5_86<='Z')||(LA5_86>='a' && LA5_86<='z')) ) {
                            alt5=45;
                        }
                        else {
                            alt5=12;}
                    }
                    else {
                        alt5=45;}
                }
                else {
                    alt5=45;}
            }
            else {
                alt5=45;}
            break;
        case '{':
            alt5=13;
            break;
        case '}':
            alt5=14;
            break;
        case 's':
            switch ( input.LA(2) ) {
            case 'c':
                int LA5_48 = input.LA(3);
                if ( (LA5_48=='a') ) {
                    int LA5_70 = input.LA(4);
                    if ( (LA5_70=='n') ) {
                        int LA5_87 = input.LA(5);
                        if ( (LA5_87=='f') ) {
                            int LA5_102 = input.LA(6);
                            if ( ((LA5_102>='0' && LA5_102<='9')||(LA5_102>='A' && LA5_102<='Z')||(LA5_102>='a' && LA5_102<='z')) ) {
                                alt5=45;
                            }
                            else {
                                alt5=24;}
                        }
                        else {
                            alt5=45;}
                    }
                    else {
                        alt5=45;}
                }
                else {
                    alt5=45;}
                break;
            case 'w':
                int LA5_49 = input.LA(3);
                if ( (LA5_49=='i') ) {
                    int LA5_71 = input.LA(4);
                    if ( (LA5_71=='t') ) {
                        int LA5_88 = input.LA(5);
                        if ( (LA5_88=='c') ) {
                            int LA5_103 = input.LA(6);
                            if ( (LA5_103=='h') ) {
                                int LA5_114 = input.LA(7);
                                if ( ((LA5_114>='0' && LA5_114<='9')||(LA5_114>='A' && LA5_114<='Z')||(LA5_114>='a' && LA5_114<='z')) ) {
                                    alt5=45;
                                }
                                else {
                                    alt5=16;}
                            }
                            else {
                                alt5=45;}
                        }
                        else {
                            alt5=45;}
                    }
                    else {
                        alt5=45;}
                }
                else {
                    alt5=45;}
                break;
            default:
                alt5=45;}

            break;
        case 'w':
            int LA5_16 = input.LA(2);
            if ( (LA5_16=='h') ) {
                int LA5_50 = input.LA(3);
                if ( (LA5_50=='i') ) {
                    int LA5_72 = input.LA(4);
                    if ( (LA5_72=='l') ) {
                        int LA5_89 = input.LA(5);
                        if ( (LA5_89=='e') ) {
                            int LA5_104 = input.LA(6);
                            if ( ((LA5_104>='0' && LA5_104<='9')||(LA5_104>='A' && LA5_104<='Z')||(LA5_104>='a' && LA5_104<='z')) ) {
                                alt5=45;
                            }
                            else {
                                alt5=17;}
                        }
                        else {
                            alt5=45;}
                    }
                    else {
                        alt5=45;}
                }
                else {
                    alt5=45;}
            }
            else {
                alt5=45;}
            break;
        case 'd':
            switch ( input.LA(2) ) {
            case 'o':
                int LA5_51 = input.LA(3);
                if ( ((LA5_51>='0' && LA5_51<='9')||(LA5_51>='A' && LA5_51<='Z')||(LA5_51>='a' && LA5_51<='z')) ) {
                    alt5=45;
                }
                else {
                    alt5=18;}
                break;
            case 'e':
                int LA5_52 = input.LA(3);
                if ( (LA5_52=='f') ) {
                    int LA5_74 = input.LA(4);
                    if ( (LA5_74=='a') ) {
                        int LA5_90 = input.LA(5);
                        if ( (LA5_90=='u') ) {
                            int LA5_105 = input.LA(6);
                            if ( (LA5_105=='l') ) {
                                int LA5_116 = input.LA(7);
                                if ( (LA5_116=='t') ) {
                                    int LA5_122 = input.LA(8);
                                    if ( ((LA5_122>='0' && LA5_122<='9')||(LA5_122>='A' && LA5_122<='Z')||(LA5_122>='a' && LA5_122<='z')) ) {
                                        alt5=45;
                                    }
                                    else {
                                        alt5=30;}
                                }
                                else {
                                    alt5=45;}
                            }
                            else {
                                alt5=45;}
                        }
                        else {
                            alt5=45;}
                    }
                    else {
                        alt5=45;}
                }
                else {
                    alt5=45;}
                break;
            default:
                alt5=45;}

            break;
        case 'f':
            int LA5_18 = input.LA(2);
            if ( (LA5_18=='o') ) {
                int LA5_53 = input.LA(3);
                if ( (LA5_53=='r') ) {
                    int LA5_75 = input.LA(4);
                    if ( ((LA5_75>='0' && LA5_75<='9')||(LA5_75>='A' && LA5_75<='Z')||(LA5_75>='a' && LA5_75<='z')) ) {
                        alt5=45;
                    }
                    else {
                        alt5=19;}
                }
                else {
                    alt5=45;}
            }
            else {
                alt5=45;}
            break;
        case 'b':
            int LA5_19 = input.LA(2);
            if ( (LA5_19=='r') ) {
                int LA5_54 = input.LA(3);
                if ( (LA5_54=='e') ) {
                    int LA5_76 = input.LA(4);
                    if ( (LA5_76=='a') ) {
                        int LA5_92 = input.LA(5);
                        if ( (LA5_92=='k') ) {
                            int LA5_106 = input.LA(6);
                            if ( ((LA5_106>='0' && LA5_106<='9')||(LA5_106>='A' && LA5_106<='Z')||(LA5_106>='a' && LA5_106<='z')) ) {
                                alt5=45;
                            }
                            else {
                                alt5=21;}
                        }
                        else {
                            alt5=45;}
                    }
                    else {
                        alt5=45;}
                }
                else {
                    alt5=45;}
            }
            else {
                alt5=45;}
            break;
        case 'r':
            int LA5_20 = input.LA(2);
            if ( (LA5_20=='e') ) {
                int LA5_55 = input.LA(3);
                if ( (LA5_55=='t') ) {
                    int LA5_77 = input.LA(4);
                    if ( (LA5_77=='u') ) {
                        int LA5_93 = input.LA(5);
                        if ( (LA5_93=='r') ) {
                            int LA5_107 = input.LA(6);
                            if ( (LA5_107=='n') ) {
                                int LA5_118 = input.LA(7);
                                if ( ((LA5_118>='0' && LA5_118<='9')||(LA5_118>='A' && LA5_118<='Z')||(LA5_118>='a' && LA5_118<='z')) ) {
                                    alt5=45;
                                }
                                else {
                                    alt5=22;}
                            }
                            else {
                                alt5=45;}
                        }
                        else {
                            alt5=45;}
                    }
                    else {
                        alt5=45;}
                }
                else {
                    alt5=45;}
            }
            else {
                alt5=45;}
            break;
        case 'p':
            int LA5_21 = input.LA(2);
            if ( (LA5_21=='r') ) {
                int LA5_56 = input.LA(3);
                if ( (LA5_56=='i') ) {
                    int LA5_78 = input.LA(4);
                    if ( (LA5_78=='n') ) {
                        int LA5_94 = input.LA(5);
                        if ( (LA5_94=='t') ) {
                            int LA5_108 = input.LA(6);
                            if ( (LA5_108=='f') ) {
                                int LA5_119 = input.LA(7);
                                if ( ((LA5_119>='0' && LA5_119<='9')||(LA5_119>='A' && LA5_119<='Z')||(LA5_119>='a' && LA5_119<='z')) ) {
                                    alt5=45;
                                }
                                else {
                                    alt5=23;}
                            }
                            else {
                                alt5=45;}
                        }
                        else {
                            alt5=45;}
                    }
                    else {
                        alt5=45;}
                }
                else {
                    alt5=45;}
            }
            else {
                alt5=45;}
            break;
        case '\"':
            int LA5_22 = input.LA(2);
            if ( (LA5_22=='%') ) {
                int LA5_57 = input.LA(3);
                if ( (LA5_57=='i') ) {
                    int LA5_79 = input.LA(4);
                    if ( (LA5_79=='\"') ) {
                        alt5=25;
                    }
                    else if ( ((LA5_79>='\u0000' && LA5_79<='!')||(LA5_79>='#' && LA5_79<='\uFFFE')) ) {
                        alt5=44;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("1:1: Tokens : ( T11 | T12 | T13 | T14 | T15 | T16 | T17 | T18 | T19 | T20 | T21 | T22 | T23 | T24 | T25 | T26 | T27 | T28 | T29 | T30 | T31 | T32 | T33 | T34 | T35 | T36 | T37 | T38 | T39 | T40 | T41 | T42 | T43 | T44 | T45 | T46 | T47 | T48 | T49 | T50 | T51 | T52 | WHITESPACE | String | Ident | Number );", 5, 79, input);

                        throw nvae;
                    }
                }
                else if ( ((LA5_57>='\u0000' && LA5_57<='h')||(LA5_57>='j' && LA5_57<='\uFFFE')) ) {
                    alt5=44;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("1:1: Tokens : ( T11 | T12 | T13 | T14 | T15 | T16 | T17 | T18 | T19 | T20 | T21 | T22 | T23 | T24 | T25 | T26 | T27 | T28 | T29 | T30 | T31 | T32 | T33 | T34 | T35 | T36 | T37 | T38 | T39 | T40 | T41 | T42 | T43 | T44 | T45 | T46 | T47 | T48 | T49 | T50 | T51 | T52 | WHITESPACE | String | Ident | Number );", 5, 57, input);

                    throw nvae;
                }
            }
            else if ( ((LA5_22>='\u0000' && LA5_22<='$')||(LA5_22>='&' && LA5_22<='\uFFFE')) ) {
                alt5=44;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("1:1: Tokens : ( T11 | T12 | T13 | T14 | T15 | T16 | T17 | T18 | T19 | T20 | T21 | T22 | T23 | T24 | T25 | T26 | T27 | T28 | T29 | T30 | T31 | T32 | T33 | T34 | T35 | T36 | T37 | T38 | T39 | T40 | T41 | T42 | T43 | T44 | T45 | T46 | T47 | T48 | T49 | T50 | T51 | T52 | WHITESPACE | String | Ident | Number );", 5, 22, input);

                throw nvae;
            }
            break;
        case '&':
            alt5=26;
            break;
        case '/':
            int LA5_24 = input.LA(2);
            if ( (LA5_24=='*') ) {
                alt5=27;
            }
            else {
                alt5=35;}
            break;
        case 'e':
            int LA5_25 = input.LA(2);
            if ( (LA5_25=='l') ) {
                int LA5_61 = input.LA(3);
                if ( (LA5_61=='s') ) {
                    int LA5_80 = input.LA(4);
                    if ( (LA5_80=='e') ) {
                        int LA5_96 = input.LA(5);
                        if ( ((LA5_96>='0' && LA5_96<='9')||(LA5_96>='A' && LA5_96<='Z')||(LA5_96>='a' && LA5_96<='z')) ) {
                            alt5=45;
                        }
                        else {
                            alt5=29;}
                    }
                    else {
                        alt5=45;}
                }
                else {
                    alt5=45;}
            }
            else {
                alt5=45;}
            break;
        case ':':
            alt5=31;
            break;
        case '+':
            alt5=33;
            break;
        case '-':
            alt5=34;
            break;
        case '%':
            alt5=36;
            break;
        case '!':
            alt5=38;
            break;
        case '>':
            int LA5_31 = input.LA(2);
            if ( (LA5_31=='=') ) {
                alt5=42;
            }
            else {
                alt5=40;}
            break;
        case '\t':
        case '\n':
        case '\f':
        case '\r':
        case ' ':
            alt5=43;
            break;
        case 'A':
        case 'B':
        case 'C':
        case 'D':
        case 'E':
        case 'F':
        case 'G':
        case 'H':
        case 'I':
        case 'J':
        case 'K':
        case 'L':
        case 'M':
        case 'N':
        case 'O':
        case 'P':
        case 'Q':
        case 'R':
        case 'S':
        case 'T':
        case 'U':
        case 'V':
        case 'W':
        case 'X':
        case 'Y':
        case 'Z':
        case 'a':
        case 'g':
        case 'h':
        case 'j':
        case 'k':
        case 'l':
        case 'n':
        case 'o':
        case 'q':
        case 't':
        case 'u':
        case 'x':
        case 'y':
        case 'z':
            alt5=45;
            break;
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            alt5=46;
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("1:1: Tokens : ( T11 | T12 | T13 | T14 | T15 | T16 | T17 | T18 | T19 | T20 | T21 | T22 | T23 | T24 | T25 | T26 | T27 | T28 | T29 | T30 | T31 | T32 | T33 | T34 | T35 | T36 | T37 | T38 | T39 | T40 | T41 | T42 | T43 | T44 | T45 | T46 | T47 | T48 | T49 | T50 | T51 | T52 | WHITESPACE | String | Ident | Number );", 5, 0, input);

            throw nvae;
        }

        switch (alt5) {
            case 1 :
                // C00.g:1:10: T11
                {
                mT11(); 

                }
                break;
            case 2 :
                // C00.g:1:14: T12
                {
                mT12(); 

                }
                break;
            case 3 :
                // C00.g:1:18: T13
                {
                mT13(); 

                }
                break;
            case 4 :
                // C00.g:1:22: T14
                {
                mT14(); 

                }
                break;
            case 5 :
                // C00.g:1:26: T15
                {
                mT15(); 

                }
                break;
            case 6 :
                // C00.g:1:30: T16
                {
                mT16(); 

                }
                break;
            case 7 :
                // C00.g:1:34: T17
                {
                mT17(); 

                }
                break;
            case 8 :
                // C00.g:1:38: T18
                {
                mT18(); 

                }
                break;
            case 9 :
                // C00.g:1:42: T19
                {
                mT19(); 

                }
                break;
            case 10 :
                // C00.g:1:46: T20
                {
                mT20(); 

                }
                break;
            case 11 :
                // C00.g:1:50: T21
                {
                mT21(); 

                }
                break;
            case 12 :
                // C00.g:1:54: T22
                {
                mT22(); 

                }
                break;
            case 13 :
                // C00.g:1:58: T23
                {
                mT23(); 

                }
                break;
            case 14 :
                // C00.g:1:62: T24
                {
                mT24(); 

                }
                break;
            case 15 :
                // C00.g:1:66: T25
                {
                mT25(); 

                }
                break;
            case 16 :
                // C00.g:1:70: T26
                {
                mT26(); 

                }
                break;
            case 17 :
                // C00.g:1:74: T27
                {
                mT27(); 

                }
                break;
            case 18 :
                // C00.g:1:78: T28
                {
                mT28(); 

                }
                break;
            case 19 :
                // C00.g:1:82: T29
                {
                mT29(); 

                }
                break;
            case 20 :
                // C00.g:1:86: T30
                {
                mT30(); 

                }
                break;
            case 21 :
                // C00.g:1:90: T31
                {
                mT31(); 

                }
                break;
            case 22 :
                // C00.g:1:94: T32
                {
                mT32(); 

                }
                break;
            case 23 :
                // C00.g:1:98: T33
                {
                mT33(); 

                }
                break;
            case 24 :
                // C00.g:1:102: T34
                {
                mT34(); 

                }
                break;
            case 25 :
                // C00.g:1:106: T35
                {
                mT35(); 

                }
                break;
            case 26 :
                // C00.g:1:110: T36
                {
                mT36(); 

                }
                break;
            case 27 :
                // C00.g:1:114: T37
                {
                mT37(); 

                }
                break;
            case 28 :
                // C00.g:1:118: T38
                {
                mT38(); 

                }
                break;
            case 29 :
                // C00.g:1:122: T39
                {
                mT39(); 

                }
                break;
            case 30 :
                // C00.g:1:126: T40
                {
                mT40(); 

                }
                break;
            case 31 :
                // C00.g:1:130: T41
                {
                mT41(); 

                }
                break;
            case 32 :
                // C00.g:1:134: T42
                {
                mT42(); 

                }
                break;
            case 33 :
                // C00.g:1:138: T43
                {
                mT43(); 

                }
                break;
            case 34 :
                // C00.g:1:142: T44
                {
                mT44(); 

                }
                break;
            case 35 :
                // C00.g:1:146: T45
                {
                mT45(); 

                }
                break;
            case 36 :
                // C00.g:1:150: T46
                {
                mT46(); 

                }
                break;
            case 37 :
                // C00.g:1:154: T47
                {
                mT47(); 

                }
                break;
            case 38 :
                // C00.g:1:158: T48
                {
                mT48(); 

                }
                break;
            case 39 :
                // C00.g:1:162: T49
                {
                mT49(); 

                }
                break;
            case 40 :
                // C00.g:1:166: T50
                {
                mT50(); 

                }
                break;
            case 41 :
                // C00.g:1:170: T51
                {
                mT51(); 

                }
                break;
            case 42 :
                // C00.g:1:174: T52
                {
                mT52(); 

                }
                break;
            case 43 :
                // C00.g:1:178: WHITESPACE
                {
                mWHITESPACE(); 

                }
                break;
            case 44 :
                // C00.g:1:189: String
                {
                mString(); 

                }
                break;
            case 45 :
                // C00.g:1:196: Ident
                {
                mIdent(); 

                }
                break;
            case 46 :
                // C00.g:1:202: Number
                {
                mNumber(); 

                }
                break;

        }

    }
}