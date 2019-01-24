// $ANTLR 3.1.3 Mar 18, 2009 10:09:25 /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g 2009-06-09 21:31:03

package org.jalgo.module.lambda.controller;
import org.jalgo.module.lambda.model.*;

import org.antlr2.runtime.*;
import org.antlr2.runtime.tree.*;

public class LambdaCalculusParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "LAMBDA", "POINT", "POPEN", "PCLOSE", "SPOPEN", "SPCLOSE", "ABSTRACTION", "APPLICATION", "ATOM", "SHORTCUT", "VAR", "CONS", "SHORTCUTID", "LETTER", "WHITESPACE", "COMMENT", "LINE_COMMENT"
    };
    public static final int SPOPEN=8;
    public static final int ABSTRACTION=10;
    public static final int LINE_COMMENT=20;
    public static final int POPEN=6;
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


        public LambdaCalculusParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public LambdaCalculusParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return LambdaCalculusParser.tokenNames; }
    public String getGrammarFileName() { return "/usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g"; }




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
       
        throw new MismatchedTokenException(ttype, input);
    }   




    public static class start_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "start"
    // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:73:1: start : term EOF ;
    public final LambdaCalculusParser.start_return start() throws RecognitionException {
        LambdaCalculusParser.start_return retval = new LambdaCalculusParser.start_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token EOF2=null;
        LambdaCalculusParser.term_return term1 = null;


        Object EOF2_tree=null;

        try {
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:73:7: ( term EOF )
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:73:9: term EOF
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_term_in_start135);
            term1=term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, term1.getTree());
            EOF2=(Token)match(input,EOF,FOLLOW_EOF_in_start137); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            EOF2_tree = (Object)adaptor.create(EOF2);
            adaptor.addChild(root_0, EOF2_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
            throw e;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "start"

    public static class term_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "term"
    // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:74:1: term : ( atom | abstraction | application | shortcut | ( POPEN atom PCLOSE )=> atom );
    public final LambdaCalculusParser.term_return term() throws RecognitionException {
        LambdaCalculusParser.term_return retval = new LambdaCalculusParser.term_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        LambdaCalculusParser.atom_return atom3 = null;

        LambdaCalculusParser.abstraction_return abstraction4 = null;

        LambdaCalculusParser.application_return application5 = null;

        LambdaCalculusParser.shortcut_return shortcut6 = null;

        LambdaCalculusParser.atom_return atom7 = null;



        try {
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:74:6: ( atom | abstraction | application | shortcut | ( POPEN atom PCLOSE )=> atom )
            int alt1=5;
            switch ( input.LA(1) ) {
            case VAR:
                {
                int LA1_1 = input.LA(2);

                if ( (true) ) {
                    alt1=1;
                }
                else if ( (synpred1_LambdaCalculus()) ) {
                    alt1=5;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 1, 1, input);

                    throw nvae;
                }
                }
                break;
            case CONS:
                {
                int LA1_2 = input.LA(2);

                if ( (true) ) {
                    alt1=1;
                }
                else if ( (synpred1_LambdaCalculus()) ) {
                    alt1=5;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 1, 2, input);

                    throw nvae;
                }
                }
                break;
            case POPEN:
                {
                int LA1_3 = input.LA(2);

                if ( (LA1_3==LAMBDA) ) {
                    alt1=2;
                }
                else if ( (LA1_3==POPEN||(LA1_3>=VAR && LA1_3<=SHORTCUTID)) ) {
                    alt1=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 1, 3, input);

                    throw nvae;
                }
                }
                break;
            case SHORTCUTID:
                {
                alt1=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }

            switch (alt1) {
                case 1 :
                    // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:74:8: atom
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_atom_in_term144);
                    atom3=atom();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, atom3.getTree());

                    }
                    break;
                case 2 :
                    // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:75:5: abstraction
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_abstraction_in_term150);
                    abstraction4=abstraction();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, abstraction4.getTree());

                    }
                    break;
                case 3 :
                    // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:76:5: application
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_application_in_term157);
                    application5=application();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, application5.getTree());

                    }
                    break;
                case 4 :
                    // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:77:5: shortcut
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_shortcut_in_term164);
                    shortcut6=shortcut();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, shortcut6.getTree());

                    }
                    break;
                case 5 :
                    // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:79:5: ( POPEN atom PCLOSE )=> atom
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_atom_in_term183);
                    atom7=atom();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, atom7.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
            throw e;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "term"

    public static class atom_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "atom"
    // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:83:1: atom : ( variable | constant );
    public final LambdaCalculusParser.atom_return atom() throws RecognitionException {
        LambdaCalculusParser.atom_return retval = new LambdaCalculusParser.atom_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        LambdaCalculusParser.variable_return variable8 = null;

        LambdaCalculusParser.constant_return constant9 = null;



        try {
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:83:7: ( variable | constant )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==VAR) ) {
                alt2=1;
            }
            else if ( (LA2_0==CONS) ) {
                alt2=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:83:9: variable
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_variable_in_atom199);
                    variable8=variable();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, variable8.getTree());

                    }
                    break;
                case 2 :
                    // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:84:5: constant
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_constant_in_atom206);
                    constant9=constant();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, constant9.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
            throw e;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "atom"

    public static class variable_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "variable"
    // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:86:1: variable : v= VAR -> ^( ATOM $v) ;
    public final LambdaCalculusParser.variable_return variable() throws RecognitionException {
        LambdaCalculusParser.variable_return retval = new LambdaCalculusParser.variable_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token v=null;

        Object v_tree=null;
        RewriteRuleTokenStream stream_VAR=new RewriteRuleTokenStream(adaptor,"token VAR");

        try {
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:86:9: (v= VAR -> ^( ATOM $v) )
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:86:11: v= VAR
            {
            v=(Token)match(input,VAR,FOLLOW_VAR_in_variable216); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_VAR.add(v);



            // AST REWRITE
            // elements: v
            // token labels: v
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_v=new RewriteRuleTokenStream(adaptor,"token v",v);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 86:17: -> ^( ATOM $v)
            {
                // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:86:20: ^( ATOM $v)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(ATOM, "ATOM"), root_1);

                adaptor.addChild(root_1, stream_v.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
            throw e;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "variable"

    public static class constant_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "constant"
    // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:87:1: constant : c= CONS -> ^( ATOM $c) ;
    public final LambdaCalculusParser.constant_return constant() throws RecognitionException {
        LambdaCalculusParser.constant_return retval = new LambdaCalculusParser.constant_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token c=null;

        Object c_tree=null;
        RewriteRuleTokenStream stream_CONS=new RewriteRuleTokenStream(adaptor,"token CONS");

        try {
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:87:9: (c= CONS -> ^( ATOM $c) )
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:87:11: c= CONS
            {
            c=(Token)match(input,CONS,FOLLOW_CONS_in_constant233); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_CONS.add(c);



            // AST REWRITE
            // elements: c
            // token labels: c
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_c=new RewriteRuleTokenStream(adaptor,"token c",c);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 87:18: -> ^( ATOM $c)
            {
                // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:87:21: ^( ATOM $c)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(ATOM, "ATOM"), root_1);

                adaptor.addChild(root_1, stream_c.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
            throw e;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "constant"

    public static class abstraction_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "abstraction"
    // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:89:1: abstraction : POPEN LAMBDA aa= variable POINT at= term PCLOSE -> ^( ABSTRACTION $aa $at) ;
    public final LambdaCalculusParser.abstraction_return abstraction() throws RecognitionException {
        LambdaCalculusParser.abstraction_return retval = new LambdaCalculusParser.abstraction_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token POPEN10=null;
        Token LAMBDA11=null;
        Token POINT12=null;
        Token PCLOSE13=null;
        LambdaCalculusParser.variable_return aa = null;

        LambdaCalculusParser.term_return at = null;


        Object POPEN10_tree=null;
        Object LAMBDA11_tree=null;
        Object POINT12_tree=null;
        Object PCLOSE13_tree=null;
        RewriteRuleTokenStream stream_PCLOSE=new RewriteRuleTokenStream(adaptor,"token PCLOSE");
        RewriteRuleTokenStream stream_LAMBDA=new RewriteRuleTokenStream(adaptor,"token LAMBDA");
        RewriteRuleTokenStream stream_POPEN=new RewriteRuleTokenStream(adaptor,"token POPEN");
        RewriteRuleTokenStream stream_POINT=new RewriteRuleTokenStream(adaptor,"token POINT");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        RewriteRuleSubtreeStream stream_variable=new RewriteRuleSubtreeStream(adaptor,"rule variable");
        try {
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:90:2: ( POPEN LAMBDA aa= variable POINT at= term PCLOSE -> ^( ABSTRACTION $aa $at) )
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:90:4: POPEN LAMBDA aa= variable POINT at= term PCLOSE
            {
            POPEN10=(Token)match(input,POPEN,FOLLOW_POPEN_in_abstraction251); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_POPEN.add(POPEN10);

            LAMBDA11=(Token)match(input,LAMBDA,FOLLOW_LAMBDA_in_abstraction253); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LAMBDA.add(LAMBDA11);

            pushFollow(FOLLOW_variable_in_abstraction257);
            aa=variable();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_variable.add(aa.getTree());
            POINT12=(Token)match(input,POINT,FOLLOW_POINT_in_abstraction259); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_POINT.add(POINT12);

            pushFollow(FOLLOW_term_in_abstraction263);
            at=term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_term.add(at.getTree());
            PCLOSE13=(Token)match(input,PCLOSE,FOLLOW_PCLOSE_in_abstraction265); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_PCLOSE.add(PCLOSE13);



            // AST REWRITE
            // elements: aa, at
            // token labels: 
            // rule labels: retval, at, aa
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_at=new RewriteRuleSubtreeStream(adaptor,"rule at",at!=null?at.tree:null);
            RewriteRuleSubtreeStream stream_aa=new RewriteRuleSubtreeStream(adaptor,"rule aa",aa!=null?aa.tree:null);

            root_0 = (Object)adaptor.nil();
            // 90:50: -> ^( ABSTRACTION $aa $at)
            {
                // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:90:53: ^( ABSTRACTION $aa $at)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(ABSTRACTION, "ABSTRACTION"), root_1);

                adaptor.addChild(root_1, stream_aa.nextTree());
                adaptor.addChild(root_1, stream_at.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
            throw e;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "abstraction"

    public static class application_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "application"
    // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:91:1: application : POPEN t1= term t2= term PCLOSE -> ^( APPLICATION $t1 $t2) ;
    public final LambdaCalculusParser.application_return application() throws RecognitionException {
        LambdaCalculusParser.application_return retval = new LambdaCalculusParser.application_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token POPEN14=null;
        Token PCLOSE15=null;
        LambdaCalculusParser.term_return t1 = null;

        LambdaCalculusParser.term_return t2 = null;


        Object POPEN14_tree=null;
        Object PCLOSE15_tree=null;
        RewriteRuleTokenStream stream_PCLOSE=new RewriteRuleTokenStream(adaptor,"token PCLOSE");
        RewriteRuleTokenStream stream_POPEN=new RewriteRuleTokenStream(adaptor,"token POPEN");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        try {
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:92:2: ( POPEN t1= term t2= term PCLOSE -> ^( APPLICATION $t1 $t2) )
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:92:4: POPEN t1= term t2= term PCLOSE
            {
            POPEN14=(Token)match(input,POPEN,FOLLOW_POPEN_in_application286); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_POPEN.add(POPEN14);

            pushFollow(FOLLOW_term_in_application290);
            t1=term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_term.add(t1.getTree());
            pushFollow(FOLLOW_term_in_application294);
            t2=term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_term.add(t2.getTree());
            PCLOSE15=(Token)match(input,PCLOSE,FOLLOW_PCLOSE_in_application296); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_PCLOSE.add(PCLOSE15);



            // AST REWRITE
            // elements: t2, t1
            // token labels: 
            // rule labels: t2, retval, t1
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_t2=new RewriteRuleSubtreeStream(adaptor,"rule t2",t2!=null?t2.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_t1=new RewriteRuleSubtreeStream(adaptor,"rule t1",t1!=null?t1.tree:null);

            root_0 = (Object)adaptor.nil();
            // 92:33: -> ^( APPLICATION $t1 $t2)
            {
                // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:92:36: ^( APPLICATION $t1 $t2)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(APPLICATION, "APPLICATION"), root_1);

                adaptor.addChild(root_1, stream_t1.nextTree());
                adaptor.addChild(root_1, stream_t2.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
            throw e;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "application"

    public static class shortcut_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "shortcut"
    // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:93:1: shortcut : s= SHORTCUTID -> ^( SHORTCUT $s) ;
    public final LambdaCalculusParser.shortcut_return shortcut() throws RecognitionException {
        LambdaCalculusParser.shortcut_return retval = new LambdaCalculusParser.shortcut_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token s=null;

        Object s_tree=null;
        RewriteRuleTokenStream stream_SHORTCUTID=new RewriteRuleTokenStream(adaptor,"token SHORTCUTID");

        try {
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:93:9: (s= SHORTCUTID -> ^( SHORTCUT $s) )
            // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:93:11: s= SHORTCUTID
            {
            s=(Token)match(input,SHORTCUTID,FOLLOW_SHORTCUTID_in_shortcut316); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_SHORTCUTID.add(s);



            // AST REWRITE
            // elements: s
            // token labels: s
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_s=new RewriteRuleTokenStream(adaptor,"token s",s);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 93:24: -> ^( SHORTCUT $s)
            {
                // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:93:27: ^( SHORTCUT $s)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SHORTCUT, "SHORTCUT"), root_1);

                adaptor.addChild(root_1, stream_s.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException e) {
            throw e;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "shortcut"

    // $ANTLR start synpred1_LambdaCalculus
    public final void synpred1_LambdaCalculus_fragment() throws RecognitionException {   
        // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:79:5: ( POPEN atom PCLOSE )
        // /usr/users/sonstige/s9527146/Desktop/trunk/grammar/LambdaCalculus.g:79:6: POPEN atom PCLOSE
        {
        match(input,POPEN,FOLLOW_POPEN_in_synpred1_LambdaCalculus175); if (state.failed) return ;
        pushFollow(FOLLOW_atom_in_synpred1_LambdaCalculus177);
        atom();

        state._fsp--;
        if (state.failed) return ;
        match(input,PCLOSE,FOLLOW_PCLOSE_in_synpred1_LambdaCalculus179); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred1_LambdaCalculus

    // Delegated rules

    public final boolean synpred1_LambdaCalculus() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_LambdaCalculus_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


 

    public static final BitSet FOLLOW_term_in_start135 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_start137 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atom_in_term144 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_abstraction_in_term150 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_application_in_term157 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_shortcut_in_term164 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atom_in_term183 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variable_in_atom199 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constant_in_atom206 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_variable216 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONS_in_constant233 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_POPEN_in_abstraction251 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_LAMBDA_in_abstraction253 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_variable_in_abstraction257 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_POINT_in_abstraction259 = new BitSet(new long[]{0x000000000001C040L});
    public static final BitSet FOLLOW_term_in_abstraction263 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_PCLOSE_in_abstraction265 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_POPEN_in_application286 = new BitSet(new long[]{0x000000000001C040L});
    public static final BitSet FOLLOW_term_in_application290 = new BitSet(new long[]{0x000000000001C040L});
    public static final BitSet FOLLOW_term_in_application294 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_PCLOSE_in_application296 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SHORTCUTID_in_shortcut316 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_POPEN_in_synpred1_LambdaCalculus175 = new BitSet(new long[]{0x000000000000C000L});
    public static final BitSet FOLLOW_atom_in_synpred1_LambdaCalculus177 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_PCLOSE_in_synpred1_LambdaCalculus179 = new BitSet(new long[]{0x0000000000000002L});

}
