// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g 2010-06-07 19:13:24

	package org.jalgo.module.unifikation.parser;
	import java.util.Map;
	import java.util.HashMap;
	import java.util.TreeMap;
	import java.util.List;
	import java.util.ArrayList;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class UnificationSetParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "X", "Y", "Z", "U", "V", "W", "LBRACKET", "RBRACKET", "STARTSET", "COMMA", "OPENBRACE", "CLOSEBRACE", "SET", "PAIR", "CONSTRUCTOR", "VARIABLE", "ALPHA", "BETA", "GAMMA", "DELTA", "EPSILON", "THETA", "INT", "WS"
    };
    public static final int OPENBRACE=14;
    public static final int U=7;
    public static final int DELTA=23;
    public static final int W=9;
    public static final int V=8;
    public static final int SET=16;
    public static final int INT=26;
    public static final int EPSILON=24;
    public static final int BETA=21;
    public static final int EOF=-1;
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


        public UnificationSetParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public UnificationSetParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return UnificationSetParser.tokenNames; }
    public String getGrammarFileName() { return "C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g"; }


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


    public static class unificationset_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "unificationset"
    // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:57:1: unificationset : set ;
    public final UnificationSetParser.unificationset_return unificationset() throws RecognitionException {
        UnificationSetParser.unificationset_return retval = new UnificationSetParser.unificationset_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        UnificationSetParser.set_return set1 = null;



        try {
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:57:16: ( set )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:57:18: set
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_set_in_unificationset177);
            set1=set();

            state._fsp--;

            root_0 = (CommonTree)adaptor.becomeRoot(set1.getTree(), root_0);

            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "unificationset"

    public static class set_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "set"
    // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:58:1: set : STARTSET OPENBRACE ws ( pair ( ws COMMA ws pair )* ws )? CLOSEBRACE -> ^( SET ( pair )* ) ;
    public final UnificationSetParser.set_return set() throws RecognitionException {
        UnificationSetParser.set_return retval = new UnificationSetParser.set_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token STARTSET2=null;
        Token OPENBRACE3=null;
        Token COMMA7=null;
        Token CLOSEBRACE11=null;
        UnificationSetParser.ws_return ws4 = null;

        UnificationSetParser.pair_return pair5 = null;

        UnificationSetParser.ws_return ws6 = null;

        UnificationSetParser.ws_return ws8 = null;

        UnificationSetParser.pair_return pair9 = null;

        UnificationSetParser.ws_return ws10 = null;


        CommonTree STARTSET2_tree=null;
        CommonTree OPENBRACE3_tree=null;
        CommonTree COMMA7_tree=null;
        CommonTree CLOSEBRACE11_tree=null;
        RewriteRuleTokenStream stream_OPENBRACE=new RewriteRuleTokenStream(adaptor,"token OPENBRACE");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_CLOSEBRACE=new RewriteRuleTokenStream(adaptor,"token CLOSEBRACE");
        RewriteRuleTokenStream stream_STARTSET=new RewriteRuleTokenStream(adaptor,"token STARTSET");
        RewriteRuleSubtreeStream stream_pair=new RewriteRuleSubtreeStream(adaptor,"rule pair");
        RewriteRuleSubtreeStream stream_ws=new RewriteRuleSubtreeStream(adaptor,"rule ws");
        try {
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:58:5: ( STARTSET OPENBRACE ws ( pair ( ws COMMA ws pair )* ws )? CLOSEBRACE -> ^( SET ( pair )* ) )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:58:7: STARTSET OPENBRACE ws ( pair ( ws COMMA ws pair )* ws )? CLOSEBRACE
            {
            STARTSET2=(Token)match(input,STARTSET,FOLLOW_STARTSET_in_set185);  
            stream_STARTSET.add(STARTSET2);

            OPENBRACE3=(Token)match(input,OPENBRACE,FOLLOW_OPENBRACE_in_set187);  
            stream_OPENBRACE.add(OPENBRACE3);

            pushFollow(FOLLOW_ws_in_set189);
            ws4=ws();

            state._fsp--;

            stream_ws.add(ws4.getTree());
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:58:29: ( pair ( ws COMMA ws pair )* ws )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==LBRACKET) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:58:30: pair ( ws COMMA ws pair )* ws
                    {
                    pushFollow(FOLLOW_pair_in_set192);
                    pair5=pair();

                    state._fsp--;

                    stream_pair.add(pair5.getTree());
                    // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:58:35: ( ws COMMA ws pair )*
                    loop1:
                    do {
                        int alt1=2;
                        int LA1_0 = input.LA(1);

                        if ( (LA1_0==WS) ) {
                            int LA1_1 = input.LA(2);

                            if ( (LA1_1==COMMA) ) {
                                alt1=1;
                            }


                        }
                        else if ( (LA1_0==COMMA) ) {
                            alt1=1;
                        }


                        switch (alt1) {
                    	case 1 :
                    	    // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:58:37: ws COMMA ws pair
                    	    {
                    	    pushFollow(FOLLOW_ws_in_set196);
                    	    ws6=ws();

                    	    state._fsp--;

                    	    stream_ws.add(ws6.getTree());
                    	    COMMA7=(Token)match(input,COMMA,FOLLOW_COMMA_in_set198);  
                    	    stream_COMMA.add(COMMA7);

                    	    pushFollow(FOLLOW_ws_in_set200);
                    	    ws8=ws();

                    	    state._fsp--;

                    	    stream_ws.add(ws8.getTree());
                    	    pushFollow(FOLLOW_pair_in_set202);
                    	    pair9=pair();

                    	    state._fsp--;

                    	    stream_pair.add(pair9.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop1;
                        }
                    } while (true);

                    pushFollow(FOLLOW_ws_in_set206);
                    ws10=ws();

                    state._fsp--;

                    stream_ws.add(ws10.getTree());

                    }
                    break;

            }

            CLOSEBRACE11=(Token)match(input,CLOSEBRACE,FOLLOW_CLOSEBRACE_in_set210);  
            stream_CLOSEBRACE.add(CLOSEBRACE11);



            // AST REWRITE
            // elements: pair
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 58:72: -> ^( SET ( pair )* )
            {
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:58:75: ^( SET ( pair )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(SET, "SET"), root_1);

                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:58:81: ( pair )*
                while ( stream_pair.hasNext() ) {
                    adaptor.addChild(root_1, stream_pair.nextTree());

                }
                stream_pair.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "set"

    public static class pair_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "pair"
    // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:60:1: pair : LBRACKET ws first= term COMMA second= term ws RBRACKET -> ^( PAIR $first $second) ;
    public final UnificationSetParser.pair_return pair() throws RecognitionException {
        UnificationSetParser.pair_return retval = new UnificationSetParser.pair_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LBRACKET12=null;
        Token COMMA14=null;
        Token RBRACKET16=null;
        UnificationSetParser.term_return first = null;

        UnificationSetParser.term_return second = null;

        UnificationSetParser.ws_return ws13 = null;

        UnificationSetParser.ws_return ws15 = null;


        CommonTree LBRACKET12_tree=null;
        CommonTree COMMA14_tree=null;
        CommonTree RBRACKET16_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        RewriteRuleSubtreeStream stream_ws=new RewriteRuleSubtreeStream(adaptor,"rule ws");
        try {
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:60:6: ( LBRACKET ws first= term COMMA second= term ws RBRACKET -> ^( PAIR $first $second) )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:60:8: LBRACKET ws first= term COMMA second= term ws RBRACKET
            {
            LBRACKET12=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_pair227);  
            stream_LBRACKET.add(LBRACKET12);

            pushFollow(FOLLOW_ws_in_pair229);
            ws13=ws();

            state._fsp--;

            stream_ws.add(ws13.getTree());
            pushFollow(FOLLOW_term_in_pair233);
            first=term();

            state._fsp--;

            stream_term.add(first.getTree());
            COMMA14=(Token)match(input,COMMA,FOLLOW_COMMA_in_pair235);  
            stream_COMMA.add(COMMA14);

            pushFollow(FOLLOW_term_in_pair239);
            second=term();

            state._fsp--;

            stream_term.add(second.getTree());
            pushFollow(FOLLOW_ws_in_pair241);
            ws15=ws();

            state._fsp--;

            stream_ws.add(ws15.getTree());
            RBRACKET16=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_pair243);  
            stream_RBRACKET.add(RBRACKET16);



            // AST REWRITE
            // elements: second, first
            // token labels: 
            // rule labels: retval, second, first
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_second=new RewriteRuleSubtreeStream(adaptor,"rule second",second!=null?second.tree:null);
            RewriteRuleSubtreeStream stream_first=new RewriteRuleSubtreeStream(adaptor,"rule first",first!=null?first.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 60:61: -> ^( PAIR $first $second)
            {
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:60:63: ^( PAIR $first $second)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(PAIR, "PAIR"), root_1);

                adaptor.addChild(root_1, stream_first.nextTree());
                adaptor.addChild(root_1, stream_second.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "pair"

    public static class term_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "term"
    // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:62:1: term : ( constructor | variable );
    public final UnificationSetParser.term_return term() throws RecognitionException {
        UnificationSetParser.term_return retval = new UnificationSetParser.term_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        UnificationSetParser.constructor_return constructor17 = null;

        UnificationSetParser.variable_return variable18 = null;



        try {
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:62:6: ( constructor | variable )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( ((LA3_0>=ALPHA && LA3_0<=THETA)) ) {
                alt3=1;
            }
            else if ( ((LA3_0>=X && LA3_0<=W)) ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:62:8: constructor
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_constructor_in_term262);
                    constructor17=constructor();

                    state._fsp--;

                    adaptor.addChild(root_0, constructor17.getTree());

                    }
                    break;
                case 2 :
                    // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:62:22: variable
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_variable_in_term266);
                    variable18=variable();

                    state._fsp--;

                    adaptor.addChild(root_0, variable18.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "term"

    public static class constructor_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "constructor"
    // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:64:1: constructor : symbol= constructorsymbol ( ws LBRACKET ws (ids+= term ( ws COMMA ws ids+= term )* ws )? RBRACKET )? -> ^( CONSTRUCTOR constructorsymbol ( term )* ) ;
    public final UnificationSetParser.constructor_return constructor() throws RecognitionException {
        UnificationSetParser.constructor_return retval = new UnificationSetParser.constructor_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LBRACKET20=null;
        Token COMMA23=null;
        Token RBRACKET26=null;
        List list_ids=null;
        UnificationSetParser.constructorsymbol_return symbol = null;

        UnificationSetParser.ws_return ws19 = null;

        UnificationSetParser.ws_return ws21 = null;

        UnificationSetParser.ws_return ws22 = null;

        UnificationSetParser.ws_return ws24 = null;

        UnificationSetParser.ws_return ws25 = null;

        RuleReturnScope ids = null;
        CommonTree LBRACKET20_tree=null;
        CommonTree COMMA23_tree=null;
        CommonTree RBRACKET26_tree=null;
        RewriteRuleTokenStream stream_LBRACKET=new RewriteRuleTokenStream(adaptor,"token LBRACKET");
        RewriteRuleTokenStream stream_RBRACKET=new RewriteRuleTokenStream(adaptor,"token RBRACKET");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        RewriteRuleSubtreeStream stream_constructorsymbol=new RewriteRuleSubtreeStream(adaptor,"rule constructorsymbol");
        RewriteRuleSubtreeStream stream_ws=new RewriteRuleSubtreeStream(adaptor,"rule ws");
        try {
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:64:13: (symbol= constructorsymbol ( ws LBRACKET ws (ids+= term ( ws COMMA ws ids+= term )* ws )? RBRACKET )? -> ^( CONSTRUCTOR constructorsymbol ( term )* ) )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:64:15: symbol= constructorsymbol ( ws LBRACKET ws (ids+= term ( ws COMMA ws ids+= term )* ws )? RBRACKET )?
            {
            pushFollow(FOLLOW_constructorsymbol_in_constructor277);
            symbol=constructorsymbol();

            state._fsp--;

            stream_constructorsymbol.add(symbol.getTree());
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:64:40: ( ws LBRACKET ws (ids+= term ( ws COMMA ws ids+= term )* ws )? RBRACKET )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==WS) ) {
                int LA6_1 = input.LA(2);

                if ( (LA6_1==LBRACKET) ) {
                    alt6=1;
                }
            }
            else if ( (LA6_0==LBRACKET) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:64:41: ws LBRACKET ws (ids+= term ( ws COMMA ws ids+= term )* ws )? RBRACKET
                    {
                    pushFollow(FOLLOW_ws_in_constructor280);
                    ws19=ws();

                    state._fsp--;

                    stream_ws.add(ws19.getTree());
                    LBRACKET20=(Token)match(input,LBRACKET,FOLLOW_LBRACKET_in_constructor282);  
                    stream_LBRACKET.add(LBRACKET20);

                    pushFollow(FOLLOW_ws_in_constructor284);
                    ws21=ws();

                    state._fsp--;

                    stream_ws.add(ws21.getTree());
                    // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:64:56: (ids+= term ( ws COMMA ws ids+= term )* ws )?
                    int alt5=2;
                    int LA5_0 = input.LA(1);

                    if ( ((LA5_0>=X && LA5_0<=W)||(LA5_0>=ALPHA && LA5_0<=THETA)) ) {
                        alt5=1;
                    }
                    switch (alt5) {
                        case 1 :
                            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:64:57: ids+= term ( ws COMMA ws ids+= term )* ws
                            {
                            pushFollow(FOLLOW_term_in_constructor289);
                            ids=term();

                            state._fsp--;

                            stream_term.add(ids.getTree());
                            if (list_ids==null) list_ids=new ArrayList();
                            list_ids.add(ids.getTree());

                            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:64:67: ( ws COMMA ws ids+= term )*
                            loop4:
                            do {
                                int alt4=2;
                                int LA4_0 = input.LA(1);

                                if ( (LA4_0==WS) ) {
                                    int LA4_1 = input.LA(2);

                                    if ( (LA4_1==COMMA) ) {
                                        alt4=1;
                                    }


                                }
                                else if ( (LA4_0==COMMA) ) {
                                    alt4=1;
                                }


                                switch (alt4) {
                            	case 1 :
                            	    // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:64:68: ws COMMA ws ids+= term
                            	    {
                            	    pushFollow(FOLLOW_ws_in_constructor292);
                            	    ws22=ws();

                            	    state._fsp--;

                            	    stream_ws.add(ws22.getTree());
                            	    COMMA23=(Token)match(input,COMMA,FOLLOW_COMMA_in_constructor294);  
                            	    stream_COMMA.add(COMMA23);

                            	    pushFollow(FOLLOW_ws_in_constructor296);
                            	    ws24=ws();

                            	    state._fsp--;

                            	    stream_ws.add(ws24.getTree());
                            	    pushFollow(FOLLOW_term_in_constructor301);
                            	    ids=term();

                            	    state._fsp--;

                            	    stream_term.add(ids.getTree());
                            	    if (list_ids==null) list_ids=new ArrayList();
                            	    list_ids.add(ids.getTree());


                            	    }
                            	    break;

                            	default :
                            	    break loop4;
                                }
                            } while (true);

                            pushFollow(FOLLOW_ws_in_constructor305);
                            ws25=ws();

                            state._fsp--;

                            stream_ws.add(ws25.getTree());

                            }
                            break;

                    }

                    RBRACKET26=(Token)match(input,RBRACKET,FOLLOW_RBRACKET_in_constructor309);  
                    stream_RBRACKET.add(RBRACKET26);


                    }
                    break;

            }

            AddConstructorArity((symbol!=null?input.toString(symbol.start,symbol.stop):null),list_ids==null?0:list_ids.size(),(symbol!=null?((Token)symbol.start):null));


            // AST REWRITE
            // elements: term, constructorsymbol
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 64:185: -> ^( CONSTRUCTOR constructorsymbol ( term )* )
            {
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:64:187: ^( CONSTRUCTOR constructorsymbol ( term )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(CONSTRUCTOR, "CONSTRUCTOR"), root_1);

                adaptor.addChild(root_1, stream_constructorsymbol.nextTree());
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:64:219: ( term )*
                while ( stream_term.hasNext() ) {
                    adaptor.addChild(root_1, stream_term.nextTree());

                }
                stream_term.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "constructor"

    public static class constructorsymbol_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "constructorsymbol"
    // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:66:1: constructorsymbol : ( ALPHA | BETA | GAMMA | DELTA | EPSILON | THETA );
    public final UnificationSetParser.constructorsymbol_return constructorsymbol() throws RecognitionException {
        UnificationSetParser.constructorsymbol_return retval = new UnificationSetParser.constructorsymbol_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set27=null;

        CommonTree set27_tree=null;

        try {
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:66:19: ( ALPHA | BETA | GAMMA | DELTA | EPSILON | THETA )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set27=(Token)input.LT(1);
            if ( (input.LA(1)>=ALPHA && input.LA(1)<=THETA) ) {
                input.consume();
                adaptor.addChild(root_0, (CommonTree)adaptor.create(set27));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "constructorsymbol"

    public static class variable_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "variable"
    // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:68:1: variable : varid ( INT )? -> ^( VARIABLE varid ( INT )? ) ;
    public final UnificationSetParser.variable_return variable() throws RecognitionException {
        UnificationSetParser.variable_return retval = new UnificationSetParser.variable_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token INT29=null;
        UnificationSetParser.varid_return varid28 = null;


        CommonTree INT29_tree=null;
        RewriteRuleTokenStream stream_INT=new RewriteRuleTokenStream(adaptor,"token INT");
        RewriteRuleSubtreeStream stream_varid=new RewriteRuleSubtreeStream(adaptor,"rule varid");
        try {
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:68:11: ( varid ( INT )? -> ^( VARIABLE varid ( INT )? ) )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:68:13: varid ( INT )?
            {
            pushFollow(FOLLOW_varid_in_variable350);
            varid28=varid();

            state._fsp--;

            stream_varid.add(varid28.getTree());
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:68:19: ( INT )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==INT) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:68:19: INT
                    {
                    INT29=(Token)match(input,INT,FOLLOW_INT_in_variable352);  
                    stream_INT.add(INT29);


                    }
                    break;

            }



            // AST REWRITE
            // elements: INT, varid
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 68:24: -> ^( VARIABLE varid ( INT )? )
            {
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:68:26: ^( VARIABLE varid ( INT )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(VARIABLE, "VARIABLE"), root_1);

                adaptor.addChild(root_1, stream_varid.nextTree());
                // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:68:43: ( INT )?
                if ( stream_INT.hasNext() ) {
                    adaptor.addChild(root_1, stream_INT.nextNode());

                }
                stream_INT.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "variable"

    public static class varid_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "varid"
    // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:69:1: varid : ( X | Y | Z | U | V | W );
    public final UnificationSetParser.varid_return varid() throws RecognitionException {
        UnificationSetParser.varid_return retval = new UnificationSetParser.varid_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set30=null;

        CommonTree set30_tree=null;

        try {
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:69:7: ( X | Y | Z | U | V | W )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set30=(Token)input.LT(1);
            if ( (input.LA(1)>=X && input.LA(1)<=W) ) {
                input.consume();
                adaptor.addChild(root_0, (CommonTree)adaptor.create(set30));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "varid"

    public static class ws_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ws"
    // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:71:1: ws : ( WS )? ;
    public final UnificationSetParser.ws_return ws() throws RecognitionException {
        UnificationSetParser.ws_return retval = new UnificationSetParser.ws_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token WS31=null;

        CommonTree WS31_tree=null;

        try {
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:71:5: ( ( WS )? )
            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:71:7: ( WS )?
            {
            root_0 = (CommonTree)adaptor.nil();

            // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:71:7: ( WS )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==WS) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // C:\\Users\\Alex\\Desktop\\java\\unifikationset\\UnificationSet.g:71:7: WS
                    {
                    WS31=(Token)match(input,WS,FOLLOW_WS_in_ws389); 
                    WS31_tree = (CommonTree)adaptor.create(WS31);
                    adaptor.addChild(root_0, WS31_tree);


                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "ws"

    // Delegated rules


 

    public static final BitSet FOLLOW_set_in_unificationset177 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STARTSET_in_set185 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_OPENBRACE_in_set187 = new BitSet(new long[]{0x0000000008008400L});
    public static final BitSet FOLLOW_ws_in_set189 = new BitSet(new long[]{0x0000000000008400L});
    public static final BitSet FOLLOW_pair_in_set192 = new BitSet(new long[]{0x000000000800A000L});
    public static final BitSet FOLLOW_ws_in_set196 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_COMMA_in_set198 = new BitSet(new long[]{0x0000000008000400L});
    public static final BitSet FOLLOW_ws_in_set200 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_pair_in_set202 = new BitSet(new long[]{0x000000000800A000L});
    public static final BitSet FOLLOW_ws_in_set206 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_CLOSEBRACE_in_set210 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_pair227 = new BitSet(new long[]{0x000000000BF003F0L});
    public static final BitSet FOLLOW_ws_in_pair229 = new BitSet(new long[]{0x000000000BF003F0L});
    public static final BitSet FOLLOW_term_in_pair233 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_COMMA_in_pair235 = new BitSet(new long[]{0x000000000BF003F0L});
    public static final BitSet FOLLOW_term_in_pair239 = new BitSet(new long[]{0x0000000008000800L});
    public static final BitSet FOLLOW_ws_in_pair241 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_RBRACKET_in_pair243 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constructor_in_term262 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variable_in_term266 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constructorsymbol_in_constructor277 = new BitSet(new long[]{0x0000000008000402L});
    public static final BitSet FOLLOW_ws_in_constructor280 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_LBRACKET_in_constructor282 = new BitSet(new long[]{0x000000000BF00BF0L});
    public static final BitSet FOLLOW_ws_in_constructor284 = new BitSet(new long[]{0x000000000BF00BF0L});
    public static final BitSet FOLLOW_term_in_constructor289 = new BitSet(new long[]{0x0000000008002800L});
    public static final BitSet FOLLOW_ws_in_constructor292 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_COMMA_in_constructor294 = new BitSet(new long[]{0x000000000BF003F0L});
    public static final BitSet FOLLOW_ws_in_constructor296 = new BitSet(new long[]{0x000000000BF003F0L});
    public static final BitSet FOLLOW_term_in_constructor301 = new BitSet(new long[]{0x0000000008002800L});
    public static final BitSet FOLLOW_ws_in_constructor305 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_RBRACKET_in_constructor309 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_constructorsymbol0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_varid_in_variable350 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_INT_in_variable352 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_varid0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WS_in_ws389 = new BitSet(new long[]{0x0000000000000002L});

}