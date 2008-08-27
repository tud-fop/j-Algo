package org.jalgo.module.hoare.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jalgo.module.hoare.constants.TextStyle;

/**
 * Represents a Variable of the verification tree.
 * This class implements methods to return information
 * about the linked {@link VarAssertion} and the 
 * {@link VerificationFormula} where the Variable is placed.
 * 
 * @author Thomas
 *
 */
public class Variable implements Comparable<Variable> {

	/**
	 * holds the id of the linked {@link VerificationFormula} which contains this variable
	 */	
	 private int id;
	 
	 /**
	  * holds the name of the variable
	  */
	 private String name;
	 
	 /**
	  * holds the c0 compatible assertion under this variable
	  */
	 private String source;
	 
	 /**
	  * holds the short form of the assertion under this variable 
	  */
	 private String text;
	 
	 /**
	  * flags if the variable is the preAssertion of the linked {@link VerificationFormula}
	  */
	 private boolean isPreAssertion;
	 
	/**
	 * holds the original assertion String
	 */
	private String original;
	 
	 /**
	  * Creates an instance of an Variable which represents a VarAssertion.
	  * 
	  * @throws NullPointerException 
	  *         if one of the Parameter is <code>null</code>
	  * @param id 
	  *        of the VerificationFormula which contains the VarAssertion.
	  * @param name
	  *        the name of the VarAssertion you get with
	  *        {@link VarAssertion#getVariableName()}        
	  * @param text
	  *        the underlying assertion you get by calling
	  *        {@link VarAssertion#toText(boolean)} with <code>true</code>
	  * @param original
	  *        the originally entered String you get by calling
	  *        {@link VarAssertion#getOriginal()}  
	  * @param source
	  *        the C0-conform underlying assertion you get by calling
	  *        {@link VarAssertion#toString()}
	  * @param isPreAssertion
	  *        should be <code>true</code> if the VarAssertion is a preAssertion
	  *        of the {@link VerificationFormula} given by its <code>id</code>
	  *        otherwise <code>false</code>
	  */
	 public Variable(int id,String name,String text,String original,
	 		              String source,boolean isPreAssertion) throws NullPointerException{
	 	if ((name==null)||(text==null)||(source==null)){
	 		throw new NullPointerException();
	 	}
	 	this.id=id;
	 	this.name=name;
	 	this.text=text;
	 	this.original=original;
	 	this.source=source;
	 	this.isPreAssertion=isPreAssertion;
	 }
	 
	 
	 /**
	  * Gives you the ID of the VerificationFormula which
	  * contains the Variable
	  * 
	  * @return an ID of the {@link VerificationFormula}
	  */
	 public int getVerificaitonId(){
	 	return id;
	 }
	 
	/**
	 * Gets the AssertionString under the Variable in
	 * the (@link TextStyle) you specify with the parameter.
	 * 
	 * @param ts 
	 *        (@link TextStyle) tells the routine how
	 *        the result should be formated         
	 * @return 	the assertion under this Variable
	 */
	public String getAssertion(TextStyle ts)	{
		switch (ts)	{
			case SOURCE	: return source;
			case SHORT 	: return name;
			case EDITOR	: return original;
			default		: return text;
			}			
		}
		
	/**
	 * Gives you the Name of the Variable.
	 * 
	 * @return the Name of the Variable
	 */
	public String getName(){
		return name;
	}
		
		
	/**
	 * This method produces a natural order for Strings that
	 * match the following RegularExpression:<br>
	 * <code>([A-Za-z]*)([0-9]+)$</code>
	 * 
	 * @param argument
	 *    the Variable to be compared.
	 * @return 
	 *    a negative integer, zero, or a positive integer as this Variable is less than,
	 *    equal to, or greater than the specified Variable.
	 *
	 * @throws NullPointerException
	 *    if the specified Variable is <code>null</code>
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Variable argument)
	  throws NullPointerException{

		if (argument==null)	{
			throw new NullPointerException("Given Variable is null and can not be compared!");
		}
		Pattern p = Pattern.compile("([A-Za-z]*)([0-9]+)$");
		Matcher m1 = p.matcher(name);
		Matcher m2 = p.matcher(argument.getName());
		if ((m1.matches()) && (m2.matches()))	{
			if (m1.group(1).compareTo(m2.group(1))==0)	{
				return (Integer.parseInt(m1.group(2))-Integer.parseInt(m2.group(2)));
			}			
		}
		return name.compareTo(argument.getName());	
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */	
	@Override
	public boolean equals(Object obj){
		if (obj==null)	{
			return false;
		}
		
		if (obj instanceof Variable)	{
			return ((Variable) obj).getName().equals(name);			
		} else	{
			return false;			
		}
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	/**
	 * Gives you the possibility to decide whether
	 * the Variable is a pre assertion or a post assertion
	 * of the Formula associated by the Id.<br>
	 * Note: Check if the Variable is pre or post assertion
	 * before you call {@link VerificationFormula#editPreAssertion}
	 * or {@link VerificationFormula#editPreAssertion}.
	 * 
	 * @return <code>true</code> if the Variable is the pre assertion
	 *         of the associated formula<br>
	 *         <code>false</code> if the Variable is the post assertion
	 *         of the associated formula
	 */
	public boolean isPreAssertion(){
		return isPreAssertion;
	}
	
}
