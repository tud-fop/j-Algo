package org.jalgo.module.unifikation.parser;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;
import org.jalgo.module.unifikation.algo.model.*;

/**
 * Parser for sets for unification problems
 * @author Alex
 *
 */
public class SetParser implements ISetParser {
	private List<ParserError> errors;
	private ProblemSet resultSet;
	private FormatConstraints formatConstraints;
	
	public SetParser(){
		errors=null;
		resultSet=null;
		formatConstraints=new FormatConstraints();
	}

	public List<ParserError> getErrors() {
		return errors;
	}

	public ProblemSet getResult() {
		return resultSet;
	}
	
	public boolean parse(String input) {
		//prepare
		resultSet=null;
		errors=new LinkedList<ParserError>();
		//Clean input
		input=this.cleanString(input);
		if(input==null) return false;
		//do parsing
    	CharStream cs = new ANTLRStringStream(input);
    	//Lexer
    	UnificationSetLexerErrorReporter uLexer = new UnificationSetLexerErrorReporter(cs);
        CommonTokenStream tokens = new CommonTokenStream(uLexer);
        //Parser
        UnificationSetParserErrorReporter uParser = new UnificationSetParserErrorReporter(tokens);
        UnificationSetParserErrorReporter.unificationset_return parserResult=null;
        try {
        	parserResult=uParser.unificationset();
		} catch (RecognitionException e) {
			errors.add(new ParserError(ParserErrorType.ParserError,e));
			return false;
		}
		//Check for errors
		if(uLexer.getNumberOfSyntaxErrors()>0){
			for(RecognitionException e:uLexer.getErrors()){
				errors.add(new ParserError(ParserErrorType.LexerError,e));
			}
			return false;
		}
		if(uParser.getNumberOfSyntaxErrors()>0){
			for(RecognitionException e:uParser.getErrors()){
				errors.add(new ParserError(ParserErrorType.ParserError,e));
			}
			return false;
		}
		
		//check for arity
		Set<String> constructorSymbols = uParser.constructorArity.keySet();
	    
		for (String constructorSymbol : constructorSymbols){
			//for each symbol
			Map<Integer,List<Integer>> curArity=uParser.constructorArity.get(constructorSymbol);
			if(curArity.size()>1){
				//more than 1 arity found
				//find the one with most entries and set that to valid
				Integer rightArity=-1;
				int maxCount=-1;
				Set<Integer> arity=curArity.keySet();
				for(Integer ar : arity){
					//for each arity
					if(curArity.get(ar).size()>maxCount){
						maxCount=curArity.get(ar).size();
						rightArity=ar;
					}
				}
				//add wrong ones as errors
				for(Integer ar : arity){
					//for each arity
					if(ar.equals(rightArity)){
						for(Integer pos : curArity.get(ar)){
							errors.add(new ParserError(ParserErrorType.InvalidArity,ar.toString(),pos.intValue()));
						}
					}
				}
			}
		}
		
		Tree AST=(Tree) parserResult.getTree();
		if(AST==null){
			errors.add(new ParserError(ParserErrorType.InternalError));
			return false;
		}
		if(AST.getType()!=UnificationSetParser.SET){
			errors.add(new ParserError(ParserErrorType.InvalidToken,AST));
			return false;
		}
		resultSet=new HTMLProblemSet();
		
		//parse Pairs
		for(int i=0;i<AST.getChildCount();i++){
			Tree curChild=AST.getChild(i);
			Pair curPair=this.parsePair(curChild);
			if(curPair!=null) resultSet.addPair(curPair);
		}
		return errors.size()==0;
	}
	
	/**
	 * Gets the position after the current tree<br>
	 * approximated! does not count invalid chars or whitespace
	 * @param tree
	 * @return index of char after current tree
	 */
	private int getTreeEnd(Tree tree){
		return tree.getCharPositionInLine()+tree.getText().length();
	}
	
	/**
	 * Parses a pair and returns it
	 * @param inTree (sub-)AST-tree to parse
	 * @return Pair or null on error
	 */
	private Pair parsePair(Tree inTree){
		if(inTree.getType()!=UnificationSetParser.PAIR){
			errors.add(new ParserError(ParserErrorType.InvalidToken,inTree));
			return null;
		}
		if(inTree.getChildCount()!=2){
			if(inTree.getChildCount()<2){
				errors.add(new ParserError(ParserErrorType.MissingToken,"TERM",getTreeEnd(inTree)));
			}else{
				for(int i=2;i<inTree.getChildCount();i++){
					errors.add(new ParserError(ParserErrorType.InvalidToken,inTree.getChild(i)));
				}
			}
			return null;
		}
		//parse Terms
		ITerm firstTerm=this.parseTerm(inTree.getChild(0));
		ITerm secondTerm=this.parseTerm(inTree.getChild(1));
		if(firstTerm!=null && secondTerm!=null){
			Pair resPair=new Pair();
			resPair.addFirstTerm(firstTerm);
			resPair.addSecondTerm(secondTerm);
			return resPair;
		}else return null;
	}
	
	private ITerm parseTerm(Tree inTree){
		if(inTree.getType()==UnificationSetParser.VARIABLE){
			return this.parseVariable(inTree);
		}else if(inTree.getType()==UnificationSetParser.CONSTRUCTOR){
			return this.parseConstructor(inTree);
		}else{
			errors.add(new ParserError(ParserErrorType.InvalidToken,inTree));
			return null;
		}
	}
	
	private Variable parseVariable(Tree inTree){
		if(inTree.getChildCount()==0){
			//should not happen
			errors.add(new ParserError(ParserErrorType.MissingToken,"",inTree.getCharPositionInLine()));
			return null;
		}
		String varName=inTree.getChild(0).getText();
		String varIndex="";
		if(inTree.getChildCount()>1) varIndex=inTree.getChild(1).getText();
		return new ColorVariable(varName+varIndex);
	}
	
	private ConstructorSymbol parseConstructor(Tree inTree){
		if(inTree.getChildCount()==0){
			//should not happen
			errors.add(new ParserError(ParserErrorType.MissingToken,"",inTree.getCharPositionInLine()));
			return null;
		}
		ConstructorSymbol result=new ColorConstructorSymbol(inTree.getChild(0).getText());
		for(int i=1;i<inTree.getChildCount();i++){
			ITerm curTerm=this.parseTerm(inTree.getChild(i));
			if(curTerm!=null) result.addParameter(curTerm);
		}
		return result;
	}
	
	/**
	 * Cleans up a String for parsing
	 * @param input source string
	 * @return Cleaned up String; null=error
	 */
	private String cleanString(final String input){
		if(input==null) return null;
		//Force lower case and remove special chars from start/end
		String Result=input.toLowerCase().trim();
		//remove whitespace
		Result=Result.replaceAll(" ", "");
		//remove line breaks
		Result=Result.replaceAll("\r", "");
		Result=Result.replaceAll("\n", "");
		//remove HTML
		Result=removeHTML(Result);
		return Result;
	}
	
	private String getSpecialChar(String in){
		if(in==null) return "";
		if(in.startsWith("#")){
			//parse unicode chars
			char res=(char) Integer.parseInt(in.substring(1));
			return res+"";
		}
		//ignore all others
		return "";
	}
	
	/**
	 * Removes the HTML-Tags from the given input-String
	 * @param HTML input
	 * @return Same string but w/o HTML tags or null for an error
	 */
	private String removeHTML(String HTML){
		StringBuffer Result=new StringBuffer();
		
		//get part between <body></body>
		int iStart=HTML.indexOf("<body");
		int iEnd;
		if(iStart>=0){
			iEnd=HTML.indexOf("</body>");
			if(iEnd<0){
				errors.add(new ParserError(ParserErrorType.InvalidBodyTag));
				return null;
			}
			HTML=HTML.substring(iStart+"<body>".length(),iEnd).trim();
		}
		
		//Loop through all remaining tags
		iStart=HTML.indexOf('<');
		iEnd=0;
		while(iStart>=0){
			//Append everything in front of tag 
			Result.append(HTML.substring(iEnd,iStart));
			//Get tag
			iEnd=HTML.indexOf('>',iStart+1);
			if(iEnd<0) return null;
			String tag=HTML.substring(iStart,iEnd+1);
			//validate tag
			if(!formatConstraints.isTagValid(tag)){
				errors.add(new ParserError(ParserErrorType.InvalidTag,tag));
				return null;
			}
			iEnd++;
			//getNext
			iStart=HTML.indexOf('<',iEnd);
		}
		Result.append(HTML.substring(iEnd));
		StringBuffer SBres = new StringBuffer();
		//Handle entities ("&xxxx;")
		Pattern specialPattern = Pattern.compile("&([^;]{3,5});");
		Matcher specialMatcher = specialPattern.matcher(Result);
		while (specialMatcher.find()) {
			specialMatcher.appendReplacement(SBres, getSpecialChar(specialMatcher.group(1)));
		}
		specialMatcher.appendTail(SBres);
		return SBres.toString();
	}
}
