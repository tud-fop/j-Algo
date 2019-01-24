// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g 2010-06-07 19:13:24

	package org.jalgo.module.unifikation.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class UnificationSetLexer extends Lexer {
    public static final int OPENBRACE=14;
    public static final int U=7;
    public static final int DELTA=23;
    public static final int W=9;
    public static final int V=8;
    public static final int SET=16;
    public static final int INT=26;
    public static final int EPSILON=24;
    public static final int EOF=-1;
    public static final int BETA=21;
    public static final int Y=5;
    public static final int ALPHA=20;
    public static final int X=4;
    public static final int Z=6;
    public static final int LBRACKET=10;
    public static final int THETA=25;
    public static final int WS=27;
    public static final int VARIABLE=19;
    public static final int COMMA=13;
    public static final int CLOSEBRACE=15;
    public static final int STARTSET=12;
    public static final int PAIR=17;
    public static final int RBRACKET=11;
    public static final int GAMMA=22;
    public static final int CONSTRUCTOR=18;

    // delegates
    // delegators

    public UnificationSetLexer() {;} 
    public UnificationSetLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public UnificationSetLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g"; }

    // $ANTLR start "X"
    public final void mX() throws RecognitionException {
        try {
            int _type = X;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:7:3: ( 'x' )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:7:5: 'x'
            {
            match('x'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "X"

    // $ANTLR start "Y"
    public final void mY() throws RecognitionException {
        try {
            int _type = Y;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:8:3: ( 'y' )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:8:5: 'y'
            {
            match('y'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Y"

    // $ANTLR start "Z"
    public final void mZ() throws RecognitionException {
        try {
            int _type = Z;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:9:3: ( 'z' )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:9:5: 'z'
            {
            match('z'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Z"

    // $ANTLR start "U"
    public final void mU() throws RecognitionException {
        try {
            int _type = U;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:10:3: ( 'u' )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:10:5: 'u'
            {
            match('u'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "U"

    // $ANTLR start "V"
    public final void mV() throws RecognitionException {
        try {
            int _type = V;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:11:3: ( 'v' )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:11:5: 'v'
            {
            match('v'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "V"

    // $ANTLR start "W"
    public final void mW() throws RecognitionException {
        try {
            int _type = W;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:12:3: ( 'w' )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:12:5: 'w'
            {
            match('w'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "W"

    // $ANTLR start "LBRACKET"
    public final void mLBRACKET() throws RecognitionException {
        try {
            int _type = LBRACKET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:13:10: ( '(' )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:13:12: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LBRACKET"

    // $ANTLR start "RBRACKET"
    public final void mRBRACKET() throws RecognitionException {
        try {
            int _type = RBRACKET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:14:10: ( ')' )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:14:12: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RBRACKET"

    // $ANTLR start "STARTSET"
    public final void mSTARTSET() throws RecognitionException {
        try {
            int _type = STARTSET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:15:10: ( 'm=' )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:15:12: 'm='
            {
            match("m="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STARTSET"

    // $ANTLR start "COMMA"
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:16:7: ( ',' )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:16:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMA"

    // $ANTLR start "OPENBRACE"
    public final void mOPENBRACE() throws RecognitionException {
        try {
            int _type = OPENBRACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:17:11: ( '{' )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:17:13: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OPENBRACE"

    // $ANTLR start "CLOSEBRACE"
    public final void mCLOSEBRACE() throws RecognitionException {
        try {
            int _type = CLOSEBRACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:18:12: ( '}' )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:18:14: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CLOSEBRACE"

    // $ANTLR start "ALPHA"
    public final void mALPHA() throws RecognitionException {
        try {
            int _type = ALPHA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:73:8: ( '\\u03B1' | 'a' )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:
            {
            if ( input.LA(1)=='a'||input.LA(1)=='\u03B1' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ALPHA"

    // $ANTLR start "BETA"
    public final void mBETA() throws RecognitionException {
        try {
            int _type = BETA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:74:7: ( '\\u03B2' | 'b' )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:
            {
            if ( input.LA(1)=='b'||input.LA(1)=='\u03B2' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BETA"

    // $ANTLR start "GAMMA"
    public final void mGAMMA() throws RecognitionException {
        try {
            int _type = GAMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:75:8: ( '\\u03B3' | 'c' )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:
            {
            if ( input.LA(1)=='c'||input.LA(1)=='\u03B3' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GAMMA"

    // $ANTLR start "DELTA"
    public final void mDELTA() throws RecognitionException {
        try {
            int _type = DELTA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:76:8: ( '\\u03B4' | 'd' )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:
            {
            if ( input.LA(1)=='d'||input.LA(1)=='\u03B4' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DELTA"

    // $ANTLR start "EPSILON"
    public final void mEPSILON() throws RecognitionException {
        try {
            int _type = EPSILON;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:77:9: ( '\\u03B5' | 'e' )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:
            {
            if ( input.LA(1)=='e'||input.LA(1)=='\u03B5' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EPSILON"

    // $ANTLR start "THETA"
    public final void mTHETA() throws RecognitionException {
        try {
            int _type = THETA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:78:7: ( '\\u03B8' | 't' )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:
            {
            if ( input.LA(1)=='t'||input.LA(1)=='\u03B8' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "THETA"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:80:5: ( ( '0' .. '9' )+ )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:80:7: ( '0' .. '9' )+
            {
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:80:7: ( '0' .. '9' )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='0' && LA1_0<='9')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:80:7: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

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


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:83:5: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:83:9: ( ' ' | '\\t' | '\\r' | '\\n' )+
            {
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:83:9: ( ' ' | '\\t' | '\\r' | '\\n' )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='\t' && LA2_0<='\n')||LA2_0=='\r'||LA2_0==' ') ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);

            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    public void mTokens() throws RecognitionException {
        // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:1:8: ( X | Y | Z | U | V | W | LBRACKET | RBRACKET | STARTSET | COMMA | OPENBRACE | CLOSEBRACE | ALPHA | BETA | GAMMA | DELTA | EPSILON | THETA | INT | WS )
        int alt3=20;
        switch ( input.LA(1) ) {
        case 'x':
            {
            alt3=1;
            }
            break;
        case 'y':
            {
            alt3=2;
            }
            break;
        case 'z':
            {
            alt3=3;
            }
            break;
        case 'u':
            {
            alt3=4;
            }
            break;
        case 'v':
            {
            alt3=5;
            }
            break;
        case 'w':
            {
            alt3=6;
            }
            break;
        case '(':
            {
            alt3=7;
            }
            break;
        case ')':
            {
            alt3=8;
            }
            break;
        case 'm':
            {
            alt3=9;
            }
            break;
        case ',':
            {
            alt3=10;
            }
            break;
        case '{':
            {
            alt3=11;
            }
            break;
        case '}':
            {
            alt3=12;
            }
            break;
        case 'a':
        case '\u03B1':
            {
            alt3=13;
            }
            break;
        case 'b':
        case '\u03B2':
            {
            alt3=14;
            }
            break;
        case 'c':
        case '\u03B3':
            {
            alt3=15;
            }
            break;
        case 'd':
        case '\u03B4':
            {
            alt3=16;
            }
            break;
        case 'e':
        case '\u03B5':
            {
            alt3=17;
            }
            break;
        case 't':
        case '\u03B8':
            {
            alt3=18;
            }
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
            {
            alt3=19;
            }
            break;
        case '\t':
        case '\n':
        case '\r':
        case ' ':
            {
            alt3=20;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("", 3, 0, input);

            throw nvae;
        }

        switch (alt3) {
            case 1 :
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:1:10: X
                {
                mX(); 

                }
                break;
            case 2 :
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:1:12: Y
                {
                mY(); 

                }
                break;
            case 3 :
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:1:14: Z
                {
                mZ(); 

                }
                break;
            case 4 :
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:1:16: U
                {
                mU(); 

                }
                break;
            case 5 :
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:1:18: V
                {
                mV(); 

                }
                break;
            case 6 :
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:1:20: W
                {
                mW(); 

                }
                break;
            case 7 :
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:1:22: LBRACKET
                {
                mLBRACKET(); 

                }
                break;
            case 8 :
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:1:31: RBRACKET
                {
                mRBRACKET(); 

                }
                break;
            case 9 :
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:1:40: STARTSET
                {
                mSTARTSET(); 

                }
                break;
            case 10 :
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:1:49: COMMA
                {
                mCOMMA(); 

                }
                break;
            case 11 :
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:1:55: OPENBRACE
                {
                mOPENBRACE(); 

                }
                break;
            case 12 :
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:1:65: CLOSEBRACE
                {
                mCLOSEBRACE(); 

                }
                break;
            case 13 :
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:1:76: ALPHA
                {
                mALPHA(); 

                }
                break;
            case 14 :
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:1:82: BETA
                {
                mBETA(); 

                }
                break;
            case 15 :
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:1:87: GAMMA
                {
                mGAMMA(); 

                }
                break;
            case 16 :
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:1:93: DELTA
                {
                mDELTA(); 

                }
                break;
            case 17 :
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:1:99: EPSILON
                {
                mEPSILON(); 

                }
                break;
            case 18 :
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:1:107: THETA
                {
                mTHETA(); 

                }
                break;
            case 19 :
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:1:113: INT
                {
                mINT(); 

                }
                break;
            case 20 :
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:1:117: WS
                {
                mWS(); 

                }
                break;

        }

    }


 

}