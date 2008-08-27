package org.jalgo.module.hoare.constants;

import org.lsmp.djep.xjep.XJep;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;
import org.jalgo.main.util.Messages;

/**
 * This class consists exclusively of static methods that use the XJep Framework. 
 * 
 * @author Uwe
 *
 */
public class ParserAccess {


	private static XJep parser = initParser(new XJep());


	
	/**
	 * Initializes the given instance of XJep.
	 * 
	 * @param parser instance of XJep
	 * @return the initialized instance of XJep
	 */
	private static XJep initParser(XJep parser)	{
		parser = new XJep();
		parser.addStandardFunctions();
		parser.setAllowAssignment(true);
		parser.setAllowUndeclared(true);
		return parser;
	}

	/**
	 * Parses the given string expression with the static parser
	 * and some additional internal criteria
	 * and returns the top node of the created XJep expression tree.
	 * 
	 * @param s the expression to be parsed
	 * @return the top node of the created XJep expression tree
	 * @throws ParseException if there are some grammatical errors in the expression
	 */
	public static Node parse(String s) throws ParseException	{
		return parse(parser, s);
	}
	
	/**
	 * Parses the given string expression with the static parser
	 * and some additional internal criteria
	 * and returns the top node of the created XJep expression tree.
	 * 
	 * @param s the expression to be parsed
	 * @return the top node of the created XJep expression tree
	 * @throws ParseException if there are some grammatical errors in the expression
	 */
	public static String parseToString(String s) throws ParseException	{
		return parser.toString(parse(s));
	}
	
	/**
	 * Parses the given string expression with the given parser
	 * and some additional internal criteria
	 * and returns the top node of the created XJep expression tree.
	 * 
	 * @param parser the parser that should be used to parse the string expression
	 * @param s the expression to be parsed
	 * @return the top node of the created XJep expression tree
	 * @throws ParseException if there are some grammatical errors in the expression
	 */
	public static Node parse(XJep parser, String s) throws ParseException	{
		Node result = parser.parse(s);
		isCorrect(s);
		return result;
	}
	
	/**
	 * Returns a string representation of the XJep node tree
	 * represented by the given top node.
	 * 
	 * @param node top node of the XJep node tree
	 * @return the string representation of the given node
	 */
	public static String getString(Node node)	{
		return parser.toString(node);
	}
	
	/**
	 * Returns a new and initialized instance of XJep.
	 * 
	 * @return an initialized instance of XJep
	 */
	public static XJep createParser()	{
		return initParser(new XJep());
	}
	
	/**
	 * Internal method which checks some additional internal
	 * criteria of the given expression
	 * 
	 * @param s the expression to be checked
	 * @throws ParseException if there are some grammatical errors in the expression
	 */
	private static void isCorrect(String s) throws ParseException	{
		if (s.matches(".*((([<>=]|!)=)|[<>=])[^\\&\\|<>=]+((([<>=]|!)=)|[<>=]).*"))	{
			throw new ParseException(Messages.getString("hoare", "parserAccess.newParseException"));
		}
	}

}
