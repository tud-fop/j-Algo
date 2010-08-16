package org.jalgo.module.unifikation.parser;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;

/**
 * Represents an error occurred during parsing
 * @author Alex
 *
 */
public class ParserError{
	private ParserErrorType type=null;
	private String data=null;
	private int position=-1;
	
	public ParserError(){
	}
	
	public ParserError(ParserErrorType type){
		this.type=type;
	}
	
	public ParserError(ParserErrorType type, String data){
		this.type=type;
		this.data=data;
	}
	
	public ParserError(ParserErrorType type, String data, int position){
		this.type=type;
		this.data=data;
		this.position=position;
	}
	
	public ParserError(ParserErrorType type, Tree elem){
		this.type=type;
		if(elem!=null){
			this.data=UnificationSetParser.tokenNames[elem.getType()];
			this.position=elem.getCharPositionInLine();
		}
	}
	
	public ParserError(ParserErrorType type, RecognitionException e){
		this.type=type;
		if(e!=null){
			this.data=e.getLocalizedMessage();
			this.position=e.charPositionInLine;
		}
	}
	
	public String toString(){
		String res=""+type;
		if(position>=0) res+=" an Position "+position;
		if(data!=null) res+=" :'"+data+"'";
		return res;
	}
	
	/**
	 * 
	 * @return type of error
	 */
	public ParserErrorType getType(){
		return type;
	}
	
	/**
	 * 
	 * @return data of error, if available; else: null
	 */
	public String getData(){
		return data;
	}
	
	/**
	 * Get the position in the input String (w/o HTML) where the error occurred
	 * @return position of error, if available; else: -1
	 */
	public int getPosition(){
		return position;
	}
	
	/**
	 * Adds an offset to the position (for relative positions)
	 * @param offset
	 */
	public void addPosition(int offset){
		position+=offset;
	}
}
