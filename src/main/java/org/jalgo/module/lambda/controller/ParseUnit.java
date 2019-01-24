package org.jalgo.module.lambda.controller;

import java.util.LinkedList;
import java.util.Observer;

import org.jalgo.module.lambda.ShortcutHandler;
import org.jalgo.module.lambda.model.Abstraction;
import org.jalgo.module.lambda.model.Application;
import org.jalgo.module.lambda.model.Atom;
import org.jalgo.module.lambda.model.Term;

import org.antlr2.runtime.*;
import org.antlr2.runtime.tree.Tree;

import org.jalgo.module.lambda.model.*;
import org.jalgo.main.util.Messages;

/**
 * The ParseUnit translates a given String into a Lambda Term Structure
 * 
 *
 */
public class ParseUnit implements IParseUnit {

	private LinkedList<Integer> klammers;
	private LinkedList<Integer> lambda;
	
	/**
	 * translates a given String into a Lambda Term Structure and puts it into an ITermHandler
	 * @param s the String to translate
	 * @param o the view that should observe the ITermHandler
	 * @return the ITermHandler object
	 * @throws RecognitionException 
	 */
	public ITermHandler parseString(String s, Observer o) throws LambdaException {
		Term term;
		ITermHandler termHandler;
		term = parseString(s);
		termHandler = new TermHandler(term, o);
		return termHandler;
	}
	
	/**
	 * translates a given String into a Lambda Term Structure
	 * @param s the String to translate
	 * @return the Term structure
	 * @throws RecognitionException throws an ANTLR RecognitionException if there was an error during the parsing process
	 */
	public Term parseString(String s) throws LambdaException {
		isValid(s);
		this.klammers = new LinkedList<Integer>();
		this.lambda = new LinkedList<Integer>();
		s = preProcessing(s);
        if(s == null) {
        	throw new LambdaException("Unbekannter Syntaxfehler!"/*Messages.getString("lambda", "ltxt.recognitionEx")*/);
        }
		LambdaCalculusLexer lex = new LambdaCalculusLexer(new ANTLRStringStream(s));
      	CommonTokenStream tokens = new CommonTokenStream(lex);
       	LambdaCalculusParser parser = new LambdaCalculusParser(tokens);
       	try {
    		LambdaCalculusParser.start_return start = parser.start();
			Term t = translateAST((Tree)start.getTree());
			return t;
       	} catch (RecognitionException e){       		
       		if(e instanceof MismatchedTokenException){
       			int index;
       			if(e.charPositionInLine==-1)
       				 index = decreaseIndex(e.index);
       			else
       				index = decreaseIndex(e.charPositionInLine); 
       			String expected = nameToken(((MismatchedTokenException) e).expecting); 
       			String found = nameToken(e.c);
       			String msg = Messages.getString("lambda","ltxt.recognitionEx2");
       			msg = msg.replaceFirst("X", ""+index).replaceFirst("Y", expected).replaceFirst("Z",found);
       			throw new LambdaException(msg,index);
       		}
       		else{
       			int index;
       			if(e.charPositionInLine==-1)
      				 index = decreaseIndex(e.index);
      			else
      				index = decreaseIndex(e.charPositionInLine); 
       			throw new LambdaException(Messages.getString("lambda","ltxt.recognitionEx").replaceFirst("X", ""+index),index);
       		}
		}
	}
	/**
	 * Store the index of all the parenthesis add in the term
	 * @param list the list of the token
	 * @param index the index in the term list
	 */
	private void addIndexKlammer(LinkedList<String []>list, int index){
		int n=-1;
		for(int i=0;i<list.size() && i<=index;i++){
			n+=list.get(i)[1].length();
		}
		for(int i = 0;i<this.klammers.size();i++){
			if(n<=this.klammers.get(i))
				this.klammers.set(i, this.klammers.get(i)+1);
		}
		for(int i = 0;i<this.lambda.size();i++){
			if(n<=this.lambda.get(i))
				this.lambda.set(i, this.lambda.get(i)+1);
		}
		this.klammers.add(n);
	}
	/**
	 * Transform some given index of the vollklammer term, to the index given for
	 * the user
	 * @param index in the vollklammer term
	 * @return index in the user term
	 */
	private int decreaseIndex(int index){
		int count=0;
		for(int i = 0;i<this.klammers.size();i++){
			if(index>=this.klammers.get(i))
				count++;
		}
		for(int i = 0;i<this.lambda.size();i++){
			if(index>=this.lambda.get(i))
				count+=2;
		}
		return index-count;
		
	}
	/**
	 * Given some token return the name of the token in a Human readable form 
	 * @param token the token
	 * @return the token's name
	 */
	private String nameToken(int token) {
		switch (token) {
			case LambdaCalculusLexer.ABSTRACTION: return Messages.getString("lambda","ltxt.errorAbstraction");
			case LambdaCalculusLexer.APPLICATION: return Messages.getString("lambda","ltxt.errorApplication");
			case LambdaCalculusLexer.LAMBDA: return "\u03BB";
			case LambdaCalculusLexer.POPEN: return "(";
			case LambdaCalculusLexer.PCLOSE: return ")";
			case LambdaCalculusLexer.SPCLOSE: return "<";
			case LambdaCalculusLexer.SPOPEN: return ">";
			case LambdaCalculusLexer.ATOM: return Messages.getString("lambda","ltxt.errorAtom"); 
			case LambdaCalculusLexer.LETTER: return Messages.getString("lambda","ltxt.errorLetter");
			case LambdaCalculusLexer.CONS: return Messages.getString("lambda","ltxt.errorCons");
			case LambdaCalculusLexer.VAR: return Messages.getString("lambda","ltxt.errorLetter");
			case LambdaCalculusLexer.SHORTCUT: return Messages.getString("lambda","ltxt.errorShortcut1");
			case LambdaCalculusLexer.SHORTCUTID: return Messages.getString("lambda","ltxt.errorShortcut1");
			case LambdaCalculusLexer.POINT: return Messages.getString("lambda","ltxt.errorPoint");
			case LambdaCalculusLexer.EOF: return Messages.getString("lambda","ltxt.errorEof");
			default: return ""+token;
		}
	}
	
	/**
	 * translates an abstract synthax tree into a Term structure
	 * @param t the tree to translate
	 * @return the Term structure
	 */
	private Term translateAST(Tree t) throws LambdaException {
		if(t.toString() == "nil") {
			return translateAST(t.getChild(0));
		}
		else if(t.toString() == "SHORTCUT") {
			Term term;
			try{
				term = ShortcutHandler.getInstance().getShortcutCloneByRepresentation(t.getChild(0).toString());
				return term;
			}
			catch(IllegalArgumentException ex){
				String msg = Messages.getString("lambda","ltxt.errorShortcut").replaceFirst("X", t.getChild(0).toString());
				throw new LambdaException(msg);
			}
			
		}
		else if(t.toString() == "ATOM") {
			return new Atom(t.getChild(0).toString());
		}
		else if(t.toString() == "APPLICATION") {
			if(t.getChildCount() == 2){
				Term rterm = translateAST(t.getChild(0));
				Term lterm = translateAST(t.getChild(1));
				Application application = new Application(rterm,lterm);
				rterm.setParent(application);
				lterm.setParent(application);
				return application;
			}
			else
				throw new LambdaException(Messages.getString("lambda","ltxt.errorApplication"));
		}
		else if(t.toString() == "ABSTRACTION") {
			if(t.getChildCount() == 2 && t.getChild(0).toString() == "ATOM"){
				Atom var = (Atom)translateAST(t.getChild(0));
				Term child = translateAST(t.getChild(1));
				Abstraction abstraction = new Abstraction(var, child);
				var.setParent(abstraction);
				child.setParent(abstraction);
				return abstraction;
			}
			else
				throw new LambdaException(Messages.getString("lambda","ltxt.errorAbstraction"));
		}
		return null;
	}
	/**
	 * verify if the term given is valid
	 * @param s term to validate
	 * @return True if term is valid, false otherwise
	 */
	private boolean isValid(String s) throws LambdaException {
		//return true; 
		String allowed = new String ("+-*/%().<>Y"); //allowed character
		allowed+='\u03BB';
		for(int i=0; i<s.length(); i++)
		{
			if( !(s.charAt(i) >= 'a' && s.charAt(i) <= 'z') &&!(s.charAt(i) >= '0' && s.charAt(i) <= '9') && !(allowed.contains(""+s.charAt(i))) )
				throw new LambdaException(Messages.getString("lambda","ltxt.errorForbidden"),i);
		}
		
		return true;
	}
	/**
	 * PreProcessing parser: the PreProcessing transform the term given for the user and
	 * transform it to a "vollklammer" term 
	 * @param input term to Parser
	 * @return a list with the tokens found. 
	 */
    private LinkedList<String []> spliter(String input){
		try{
			int j;
			LinkedList<String []> list = new LinkedList<String []>();
			
			for(int i=0; i < input.length(); i++){
				switch (input.charAt(i)){
					case ' ':
						break;
					case '(':
						String [] s = {EToken.POPEN.toString() ,input.charAt(i)+""};
						list.add(s);
						break;
					case')':
						String [] s1 = {EToken.PCLOSE.toString(),input.charAt(i)+""};
						list.add(s1);
						break;
					case '\u03BB':
						String aux="";
						for(j=i;j<input.length() && input.charAt(j)!= '.';j++)
							aux+=input.charAt(j)+"";
						if(j<input.length())
							aux+=input.charAt(j)+"";
						j-=i;
						i+=j;
						String [] s2 = {EToken.LAMBDA.toString(),aux};
						list.add(s2);
						break;
					case '<':
						String s4="";
						for(j=i;j<input.length() && input.charAt(j)!= '>';j++){
							s4+=input.charAt(j)+"";
						}
						if(j<input.length())
							s4+=input.charAt(j)+"";
						j-=i;
						i+=j;
						String [] s5 = {EToken.SHORTCUT.toString(), s4};
						list.add(s5);
						break;
					default:
						String [] s3 = {EToken.OTHER.toString(), input.charAt(i)+""};
						list.add(s3);
						break;
						
				}
			}
			return list;
		}catch(Exception e){
			
		}
		return null;
		
	}
    private LinkedList<String[]> separateLambda(LinkedList<String[]> list, int index){
		
		if(!list.get(index)[0].equals(EToken.LAMBDA.toString()))
			return list;
		int count = 0;
		String [] neue, aux;
		String tmp;
		aux = list.get(index);
		String lambda = aux[1]; 
		
		while(lambda.length()>3){
			tmp = lambda.charAt(2)+"";
			lambda = lambda.substring(0, 2) + lambda.substring(2+1, lambda.length());
			aux[1] = lambda;
			list.set(index, aux);
			
			neue = new String [2];
			neue[0] = EToken.LAMBDA.toString();
			neue[1] = '\u03BB' + tmp+".";
			
			list.add(index+1+count, neue);
			count+=1;
			
			int n=-1;
			for(int i=0;i<list.size() && i<=index+1+count;i++){
				n+=list.get(i)[1].length();
			}
			this.lambda.add(n);
		}
	
		
		return list;
	}
    private LinkedList<String []> insertBeforLambda(LinkedList<String []> list, int index){
		String [] current;
		int countP=0;
		String [] POpen = {EToken.POPEN.toString() ,"("};
		String [] PClose = {EToken.PCLOSE.toString(),")"};
		list.add(index, POpen);
		
		for(int i=index+1;i < list.size(); i++){
			current = list.get(i);
			if(current[0].equals(EToken.POPEN.toString())){
				countP++;
			}
			else if(current[0].equals(EToken.PCLOSE.toString())){
				countP--;
			}
			if(countP < 0){
				addIndexKlammer(list, index);
				addIndexKlammer(list, i);
                    		list.add(i, PClose);
                    		break;  
			}
				
		}
			
		
		return list;
	}
    /**
	 * Insert the parenthesis in a founded application
	 * @param list The list with the Tokens
	 * @param index the index where the application was found.
	 * @return a list with the tokens
	 */
	private LinkedList<String[]>  insertP(LinkedList<String[]> list, int index){
        String [] current;
        int i, countP=0;
		String [] POpen = {EToken.POPEN.toString() ,"("};
		String [] PClose = {EToken.PCLOSE.toString(),")"};
		addIndexKlammer(list, index+1);
		list.add(index+1, PClose);
		for(i=index; i>0 ;i--){
			current = list.get(i);
			if(current[0].equals(EToken.POPEN.toString())){
				countP--;
			}
			else if(current[0].equals(EToken.PCLOSE.toString())){
				countP++;
			}
			if(countP < 0)
				break;
		}
		addIndexKlammer(list, i);
		list.add(i, POpen);
        
		return list;
	}
	/**
	 * Insert the parenthesis in a founded Abstraction
	 * @param list The list with the Tokens
	 * @param index the index where the application was found.
	 * @return a list with the tokens
	 */
	private LinkedList<String[]> insertPInAbstraction(LinkedList<String[]> list, int index){
		separateLambda(list,index);
		
		String [] current, befor, next;
		int countP=0;
		int count=0;
		String [] POpen = {EToken.POPEN.toString() ,"("};
		String [] PClose = {EToken.PCLOSE.toString(),")"};
		
        if(index+1<list.size())
        	next = list.get(index+1);
        else
        	next = null;
        
		list.add(index+1, POpen);
		
		for(int i=index+2;i < list.size(); i++){
			current = list.get(i);
             if(i>0)
                befor = list.get(i-1);
            else
                befor= null;
			if(!current[0].equals(EToken.PCLOSE.toString()))
				count++;
			if(current[0].equals(EToken.POPEN.toString())){
				countP++;
			}
			else if(current[0].equals(EToken.PCLOSE.toString())){
				countP--;
			}
			if(countP < 0 || i+1 >= list.size()){
				if((count>1 && befor != null &&!befor[0].equals(EToken.PCLOSE.toString()))
						|| (next!=null && !next[0].equals(EToken.POPEN.toString()) && count >1)){
					addIndexKlammer(list, index+1);
					addIndexKlammer(list, i);
					list.add(i, PClose);
				}
				else
					list.remove(index+1);
				break;
			}
			
			
		}
		return list;
		
	}
	/**
	 * Transform the term given for the user to a expression with all 
	 * the parenthesis needed for the Lexer and the Parser(this will would 
	 * name it "vollklammer term").
	 * @param list The list with the Tokens
	 * @return a "vollklammer" term
	 */
	private String vollklammer(LinkedList<String []> list){
		int index;
		int count=0;
		String [] current;
		String next, befor, first, last="";
		String [] POpen = {EToken.POPEN.toString() ,"("};
		String [] PClose = {EToken.PCLOSE.toString(),")"};
		
		first = list.get(0)[0];
		if(list.size()>0)
			last = list.get(list.size()-1)[0];
			
		// Check if the term have the first and the last parenthesis.
		if((first!= EToken.POPEN.toString() || last != EToken.PCLOSE.toString()) && list.size()>1){
			list.add(0, POpen);
			addIndexKlammer(list, 0);			
			list.add(list.size(), PClose);
			addIndexKlammer(list, list.size());
		}
		
		for(index=0; index < list.size(); index++){
			current = list.get(index);
			if(index-1 >0)
				befor = list.get(index-1)[0];
			else
				befor = null;
			if(index+1 < list.size())
				next = list.get(index+1)[0];
			else
				next = null;
			
			if(current[0].equals(EToken.LAMBDA.toString())){
				if(befor!= null &&!befor.equals(EToken.POPEN.toString())){
					insertBeforLambda(list, index);
					index++;
				}
				list = insertPInAbstraction(list, index);
				
			}else if(current[0].equals(EToken.OTHER.toString())){
				count++;
			}
			else if(current[0].equals(EToken.SHORTCUT.toString()))
				count++;
			else
				count=0;
 				
			if((befor!= null && befor.equals(EToken.PCLOSE.toString()) && count >=1  && next != null && !next.equals(EToken.PCLOSE.toString()) )
					||(count==2 && next != null && !next.equals(EToken.PCLOSE.toString()))){
				list = insertP(list, index);
                index++;
                count--;
			}
		}
		String fin="";
		String [] a;
		for(int i = 0; i< list.size();i++)
		{
			a= list.get(i);
			fin += a[1];
		}
		return fin;
	}
	/**
	 * Transform the term given for the user to a expression with all 
	 * the parenthesis needed for the Lexer and the Parser
	 * @param input the term given for the user
	 * @return the term with all the parenthesis
	 */
	public String preProcessing(String input){
		LinkedList<String []> list = this.spliter(input);
		if(list == null)
			return null;

		return this.vollklammer(list);
	}
	

}
