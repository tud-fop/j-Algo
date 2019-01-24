// $ANTLR 3.1.3 Mar 18, 2009 10:09:25 /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g 2009-06-09 21:31:03


package org.jalgo.module.lambda.controller;

import org.antlr2.runtime.*;


public class LambdaCalculusLexer extends Lexer {
    public static final int SPOPEN=8;
    public static final int ABSTRACTION=10;
    public static final int POPEN=6;
    public static final int LINE_COMMENT=20;
    public static final int POINT=5;
    public static final int APPLICATION=11;
    public static final int LETTER=17;
    public static final int SHORTCUTID=16;
    public static final int WHITESPACE=18;
    public static final int ATOM=12;
    public static final int EOF=-1;
    public static final int SHORTCUT=13;
    public static final int PCLOSE=7;
    public static final int CONS=15;
    public static final int VAR=14;
    public static final int LAMBDA=4;
    public static final int COMMENT=19;
    public static final int SPCLOSE=9;

    // delegates
    // delegators

    public LambdaCalculusLexer() {;} 
    public LambdaCalculusLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public LambdaCalculusLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "/usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g"; }

    // $ANTLR start "LAMBDA"
    public final void mLAMBDA() throws RecognitionException {
        try {
            int _type = LAMBDA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:10:8: ( '\\u03BB' )
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:10:10: '\\u03BB'
            {
            match('\u03BB'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LAMBDA"

    // $ANTLR start "POINT"
    public final void mPOINT() throws RecognitionException {
        try {
            int _type = POINT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:11:7: ( '.' )
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:11:9: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "POINT"

    // $ANTLR start "POPEN"
    public final void mPOPEN() throws RecognitionException {
        try {
            int _type = POPEN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:12:7: ( '(' )
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:12:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "POPEN"

    // $ANTLR start "PCLOSE"
    public final void mPCLOSE() throws RecognitionException {
        try {
            int _type = PCLOSE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:13:8: ( ')' )
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:13:10: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PCLOSE"

    // $ANTLR start "SPOPEN"
    public final void mSPOPEN() throws RecognitionException {
        try {
            int _type = SPOPEN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:14:8: ( '<' )
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:14:10: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SPOPEN"

    // $ANTLR start "SPCLOSE"
    public final void mSPCLOSE() throws RecognitionException {
        try {
            int _type = SPCLOSE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:15:9: ( '>' )
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:15:11: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SPCLOSE"

    // $ANTLR start "VAR"
    public final void mVAR() throws RecognitionException {
        try {
            int _type = VAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:98:6: ( ( 'k' .. 'z' ) )
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:98:8: ( 'k' .. 'z' )
            {
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:98:8: ( 'k' .. 'z' )
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:98:9: 'k' .. 'z'
            {
            matchRange('k','z'); 

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "VAR"

    // $ANTLR start "CONS"
    public final void mCONS() throws RecognitionException {
        try {
            int _type = CONS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:99:6: ( ( 'a' .. 'j' | '+' | '-' | '*' | '/' | '%' ) )
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:99:8: ( 'a' .. 'j' | '+' | '-' | '*' | '/' | '%' )
            {
            if ( input.LA(1)=='%'||(input.LA(1)>='*' && input.LA(1)<='+')||input.LA(1)=='-'||input.LA(1)=='/'||(input.LA(1)>='a' && input.LA(1)<='j') ) {
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
    // $ANTLR end "CONS"

    // $ANTLR start "SHORTCUTID"
    public final void mSHORTCUTID() throws RecognitionException {
        try {
            int _type = SHORTCUTID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:102:2: ( SPOPEN ( LETTER )+ SPCLOSE )
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:102:4: SPOPEN ( LETTER )+ SPCLOSE
            {
            mSPOPEN(); 
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:102:10: ( LETTER )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='0' && LA1_0<='9')||(LA1_0>='A' && LA1_0<='Z')||(LA1_0>='a' && LA1_0<='z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:102:11: LETTER
            	    {
            	    mLETTER(); 

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

            mSPCLOSE(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SHORTCUTID"

    // $ANTLR start "LETTER"
    public final void mLETTER() throws RecognitionException {
        try {
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:104:2: ( ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' ) )
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:104:4: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' )
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "LETTER"

    // $ANTLR start "WHITESPACE"
    public final void mWHITESPACE() throws RecognitionException {
        try {
            int _type = WHITESPACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:105:12: ( ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )+ )
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:105:15: ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )+
            {
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:105:15: ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='\t' && LA2_0<='\n')||(LA2_0>='\f' && LA2_0<='\r')||LA2_0==' ') ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||(input.LA(1)>='\f' && input.LA(1)<='\r')||input.LA(1)==' ' ) {
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

             _channel = HIDDEN; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WHITESPACE"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:108:5: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:108:9: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:108:14: ( options {greedy=false; } : . )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0=='*') ) {
                    int LA3_1 = input.LA(2);

                    if ( (LA3_1=='/') ) {
                        alt3=2;
                    }
                    else if ( ((LA3_1>='\u0000' && LA3_1<='.')||(LA3_1>='0' && LA3_1<='\uFFFF')) ) {
                        alt3=1;
                    }


                }
                else if ( ((LA3_0>='\u0000' && LA3_0<=')')||(LA3_0>='+' && LA3_0<='\uFFFF')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:108:42: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            match("*/"); 

            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMENT"

    // $ANTLR start "LINE_COMMENT"
    public final void mLINE_COMMENT() throws RecognitionException {
        try {
            int _type = LINE_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:111:5: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' )
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:111:7: '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
            {
            match("//"); 

            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:111:12: (~ ( '\\n' | '\\r' ) )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='\u0000' && LA4_0<='\t')||(LA4_0>='\u000B' && LA4_0<='\f')||(LA4_0>='\u000E' && LA4_0<='\uFFFF')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:111:12: ~ ( '\\n' | '\\r' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:111:26: ( '\\r' )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='\r') ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:111:26: '\\r'
                    {
                    match('\r'); 

                    }
                    break;

            }

            match('\n'); 
            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LINE_COMMENT"

    public void mTokens() throws RecognitionException {
        // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:1:8: ( LAMBDA | POINT | POPEN | PCLOSE | SPOPEN | SPCLOSE | VAR | CONS | SHORTCUTID | WHITESPACE | COMMENT | LINE_COMMENT )
        int alt6=12;
        alt6 = dfa6.predict(input);
        switch (alt6) {
            case 1 :
                // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:1:10: LAMBDA
                {
                mLAMBDA(); 

                }
                break;
            case 2 :
                // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:1:17: POINT
                {
                mPOINT(); 

                }
                break;
            case 3 :
                // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:1:23: POPEN
                {
                mPOPEN(); 

                }
                break;
            case 4 :
                // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:1:29: PCLOSE
                {
                mPCLOSE(); 

                }
                break;
            case 5 :
                // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:1:36: SPOPEN
                {
                mSPOPEN(); 

                }
                break;
            case 6 :
                // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:1:43: SPCLOSE
                {
                mSPCLOSE(); 

                }
                break;
            case 7 :
                // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:1:51: VAR
                {
                mVAR(); 

                }
                break;
            case 8 :
                // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:1:55: CONS
                {
                mCONS(); 

                }
                break;
            case 9 :
                // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:1:60: SHORTCUTID
                {
                mSHORTCUTID(); 

                }
                break;
            case 10 :
                // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:1:71: WHITESPACE
                {
                mWHITESPACE(); 

                }
                break;
            case 11 :
                // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:1:82: COMMENT
                {
                mCOMMENT(); 

                }
                break;
            case 12 :
                // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:1:90: LINE_COMMENT
                {
                mLINE_COMMENT(); 

                }
                break;

        }

    }


    protected DFA6 dfa6 = new DFA6(this);
    static final String DFA6_eotS =
        "\5\uffff\1\13\2\uffff\1\12\6\uffff";
    static final String DFA6_eofS =
        "\17\uffff";
    static final String DFA6_minS =
        "\1\11\4\uffff\1\60\2\uffff\1\52\6\uffff";
    static final String DFA6_maxS =
        "\1\u03bb\4\uffff\1\172\2\uffff\1\57\6\uffff";
    static final String DFA6_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\uffff\1\6\1\7\1\uffff\1\12\1\10\1\5\1"+
        "\11\1\13\1\14";
    static final String DFA6_specialS =
        "\17\uffff}>";
    static final String[] DFA6_transitionS = {
            "\2\11\1\uffff\2\11\22\uffff\1\11\4\uffff\1\12\2\uffff\1\3\1"+
            "\4\2\12\1\uffff\1\12\1\2\1\10\14\uffff\1\5\1\uffff\1\6\42\uffff"+
            "\12\12\20\7\u0340\uffff\1\1",
            "",
            "",
            "",
            "",
            "\12\14\7\uffff\32\14\6\uffff\32\14",
            "",
            "",
            "\1\15\4\uffff\1\16",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA6_eot = DFA.unpackEncodedString(DFA6_eotS);
    static final short[] DFA6_eof = DFA.unpackEncodedString(DFA6_eofS);
    static final char[] DFA6_min = DFA.unpackEncodedStringToUnsignedChars(DFA6_minS);
    static final char[] DFA6_max = DFA.unpackEncodedStringToUnsignedChars(DFA6_maxS);
    static final short[] DFA6_accept = DFA.unpackEncodedString(DFA6_acceptS);
    static final short[] DFA6_special = DFA.unpackEncodedString(DFA6_specialS);
    static final short[][] DFA6_transition;

    static {
        int numStates = DFA6_transitionS.length;
        DFA6_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA6_transition[i] = DFA.unpackEncodedString(DFA6_transitionS[i]);
        }
    }

    class DFA6 extends DFA {

        public DFA6(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 6;
            this.eot = DFA6_eot;
            this.eof = DFA6_eof;
            this.min = DFA6_min;
            this.max = DFA6_max;
            this.accept = DFA6_accept;
            this.special = DFA6_special;
            this.transition = DFA6_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( LAMBDA | POINT | POPEN | PCLOSE | SPOPEN | SPCLOSE | VAR | CONS | SHORTCUTID | WHITESPACE | COMMENT | LINE_COMMENT );";
        }
    }
 

}
