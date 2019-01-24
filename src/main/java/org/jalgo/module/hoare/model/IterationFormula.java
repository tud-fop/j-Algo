/**
 * 
 */
package org.jalgo.module.hoare.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.constants.ParserAccess;
import org.jalgo.module.hoare.constants.Rule;
import org.nfunk.jep.ParseException;

/**
 * Represents the While statement with on statement (or statement sequence)
 * 
 * @author Thomas, Uwe
 *
 */

public class IterationFormula extends VerificationFormula {
	/**
	 * the serial Id of this Object
	 */
	private static final long serialVersionUID = 3879791094677408935L;
	/**
	 * holds the concrete assertion in the boolean expression in the while-statement
	 */
	private ConcreteAssertion boolExp;

	
	
	/**
	 * Constructs a IterationFormula with the given beginning and end of the code
	 * and sets the parent and the source of
	 * the whole <code>VerificationFormula</code> tree.
	 * 
	 * @param parent the parent node in the VerificationFormula tree
	 * @param source the source of the whole VerificationFormula tree
	 * @param codeStart startIndex of the SourceCode
	 * @param codeEnd endIndex of the SourceCode
	 * @param boolExp the boolean Expression of the while-Statement
	 */
	IterationFormula(VerificationFormula parent, String source, int codeStart,
			int codeEnd, ConcreteAssertion boolExp) {
		super(parent, source, codeStart, codeEnd);
		this.boolExp=boolExp;
	}

	/**
	 * @see org.jalgo.module.hoare.model.VerificationFormula#canApply(Rule)
	 */
	boolean canApply(Rule rule)	{
		return rule.equals(Rule.ITERATION);
	}
	
	/**
	 * @see org.jalgo.module.hoare.model.VerificationFormula#applyNext(VerificationFormula)
	 */
	boolean applyNext(VerificationFormula parent)
		throws UnsupportedOperationException	{

		if (! parent.hasChildren())	{
			return false;
		}
		VerificationFormula vf = parent.getChildren().get(0);

		
		if (vf.isRuleApplied())	{
			return false;
		}
		
		if (! check(parent))	{
			throw new UnsupportedOperationException(String.format(Messages.
						getString("hoare", "out.iterationCheckFailed"),
						postAssertion.toText(false),
						preAssertion.toText(false)));
		} else	{
			parent.appliedRule = Rule.ITERATION;

			vf.setApplied();
			vf.setChanged(true);
			vf.replacePreAssertion(new AndAssertion(parent.preAssertion,
										boolExp));
			vf.replacePostAssertion(parent.preAssertion);
			return true;
		}
	}
	/**
	 * @see org.jalgo.module.hoare.model.VerificationFormula#check(VerificationFormula)
	 */
	protected boolean check(VerificationFormula formula)	{
		String pre;
		String post;
		String prePost;
		String bool;
		Pattern p;
		Matcher m;
		
		
		pre = formula.preAssertion.toString().replaceAll("(?<![\\<\\>\\!\\=])\\=(?![\\=])", "==");
		post = formula.postAssertion.toString().replaceAll("(?<![\\<\\>\\!\\=])\\=(?![\\=])", "==");
		try	{
			pre = ParserAccess.parseToString(pre).replaceAll("\\.0", "");
			post = ParserAccess.parseToString(post).replaceAll("\\.0", "");
			bool = ParserAccess.parseToString(boolExp.toString()).replaceAll("\\.0", "");
		} catch (ParseException pe){
			return false;
		}		

		
		p = Pattern.compile("(.*)\\&\\&([^\\&]+)");
		m = p.matcher(post);


		if (! m.find())	{
			return false;
		}

		
		prePost = m.group(1);
		return (prePost.equals(pre) && isEqual(bool, m.group(2)));
	}
		
	/**
	 * isEqual is an internal method for checking whether neg equals NOT pos
	 * 
	 * @param pos the positive assertion as a String
	 * @param neg the negative assertion to be checked as a String
	 * @return true if neg equals NOT pos
	 */
	private boolean isEqual(String pos, String neg)	{
		Pattern p;
		Matcher m;



		if (("!(" + pos + ")").equals(neg) || ("!" + pos).equals(neg))	{
			return true;
		}		


		p = Pattern.compile("([^\\<\\>\\=\\!]+)(?:[\\<\\>\\=]\\=?|\\!\\=)([^\\<\\>\\=\\!]+)");
		m = p.matcher(pos);
		if (! m.find())	{
			return false;
		}


		if (pos.matches("([^\\<\\>\\=\\!]+)\\<([^\\<\\>\\=\\!]+)"))	{
			return ((m.group(1) + ">=" + m.group(2)).equals(neg) ? true : false);
		}

		if (pos.matches("([^\\<\\>\\=\\!]+)\\>([^\\<\\>\\=\\!]+)"))	{
			return ((m.group(1) + "<=" + m.group(2)).equals(neg) ? true : false);
		}

		if (pos.matches("([^\\<\\>\\=\\!]+)\\<\\=([^\\<\\>\\=\\!]+)"))	{
			return ((m.group(1) + ">" + m.group(2)).equals(neg) ? true : false);
		}

		if (pos.matches("([^\\<\\>\\=\\!]+)\\>\\=([^\\<\\>\\=\\!]+)"))	{
			return ((m.group(1) + "<" + m.group(2)).equals(neg) ? true : false);
		}

		if (pos.matches("([^\\<\\>\\=\\!]+)\\=\\=([^\\<\\>\\=\\!]+)"))	{
			return ((m.group(1) + "!=" + m.group(2)).equals(neg) ? true : false);
		}

		if (pos.matches("([^\\<\\>\\=\\!]+)\\!\\=([^\\<\\>\\=\\!]+)"))	{
			return ((m.group(1) + "==" + m.group(2)).equals(neg) ? true : false);
		}


		return false;
	}
	
	/**
	 * 
	 * @see org.jalgo.module.hoare.model.VerificationFormula#getCode(boolean)
	 */
	public String getCode(boolean full) {
		return (full ? getCode(getCode()) : "while (" + boolExp.toText(true)+")");
	}
	
}
