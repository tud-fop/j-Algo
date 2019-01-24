package org.jalgo.module.hoare.control;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jalgo.main.util.Messages;
import org.lsmp.djep.xjep.XJep;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;
import org.jalgo.module.hoare.constants.ParserAccess;

/**
 * an Thread for evaluating the correctness of pre- and postAssertions.
 * it reports errors and results through his father.
 *
 * @author Johannes, Uwe
 */
public class EvaluationThread extends Thread {

	private int myId;
	private int maxEvalTime;
	private int base;
	private boolean run;
	private JepEvaluator father;
	private String leftSide;
	private String rightSide;


	private XJep evaluator;

	/**
	 * creates a new instance of EvaluationThread
	 *
	 * @param myId
	 * 		index of the node/VerificationFormula - also the ThreadId
	 * @param maxEvalTime
	 * 		maxEvalTime given in seconds
	 * @param base
	 * 		number of ciphers to test
	 * @param father
	 *		father of the thread, will get results reported
	 * @param leftSide
	 * 		left side of the equation
	 * @param rightSide
	 *		right side of the equation
	 */
	public EvaluationThread(int myId, int maxEvalTime, int base, JepEvaluator father, String leftSide, String rightSide){
		this.myId = myId;
		this.maxEvalTime = maxEvalTime * 1000;
		this.base = base;
		this.father = father;
		this.leftSide = leftSide;
		this.rightSide = rightSide;
		run = true;

		evaluator = ParserAccess.createParser();
	}


	/**
	 * the main working function of the thread.
	 * evaluates the assertions an report any result or error to the father
	 */
	public void run(){
		int count = 0;
		double max = 0.0;
		double startTime = 0.0;
		Node leftNode;
		Node rightNode;
		Set<Object> unbound = new HashSet<Object>();
		Map<String, Double> fixed = new HashMap<String, Double>();
		Random random = new Random();

		
		if (!(leftSide.matches(".*\\|\\|.*")) && !(rightSide.matches(".*\\|\\|.*")))	{
			leftSide = prepareForXJep(leftSide, true);
			rightSide = prepareForXJep(rightSide, false);
			fixed = getFixed(leftSide);
			
			for (String s : fixed.keySet())	{
				evaluator.addVariable(s, fixed.get(s));
			}
		}
		
		

		try	{
			leftNode = evaluator.parse(leftSide);
			rightNode = evaluator.parse(rightSide);
		} catch (ParseException pe)	{
			father.report(myId, false, Messages.getString("hoare", "out.evalError") + pe.getErrorInfo());
			return;
		}

		for (Object o : evaluator.getSymbolTable().keySet())	{
			if (evaluator.getVarValue((String)o) == null
				&& !(leftSide.matches(".*(Sum|Product)\\([^,]*," + o + ",[^,]*,[^,]*\\).*"))
				&& !(leftSide.matches(".*(\\&|\\||\\()" + o + "=(Sum|Product).*"))
				&& !(rightSide.matches(".*(Sum|Product)\\([^,]*," + o + ",[^,]*,[^,]*\\).*")))	{
				unbound.add(o);
			}
		}
		

		max = Math.pow(base, unbound.size());
		startTime = System.currentTimeMillis();
		run = true;
		

		while ((run) && (count < max))	{
			
			for (Object o : unbound){
				evaluator.setVarValue((String)o, (random.nextInt() % (base/2)));
			}
			
			try	{
				if (evaluator.evaluate(leftNode).equals(1.0)
						&& evaluator.evaluate(rightNode).equals(0.0))	{
					father.report(myId, false, Messages.getString("hoare", "out.evaluationWrong")
													+ "\n" + evaluator.getSymbolTable().toString());
					return;
				}				
			} catch (ParseException pe){
				father.report(myId, false, Messages.getString("hoare", "out.evalError") + pe.getErrorInfo());
				return;
			}
			count++;
			
			if ((count % 100) == 0)	{
				if ((System.currentTimeMillis() - startTime) > maxEvalTime)	{
					break;
				}				
			}
			
		}// while
		
		if (!run)	{
			return;
		}
		
		father.report(myId, true, Messages.getString("hoare", "out.evaluationOK"));
	}

	/**
	 * will stop the thread
	 */
	public void end(){
		run = false;
	}

	/**
	 * Prepares the assertion for XJep.
	 * Equals were replaced by double equals except for variable=Sum|Product
	 * and variable=double value.
	 *
	 * @param assertion
	 *		String of assertion to translate
	 * @return the reworked String representing the assertion
	 */
	private String prepareForXJep(String assertion, boolean prepareSum){
		
		Pattern p1;
		Matcher matcher;		
		
		
		assertion = assertion.replaceAll("(?<![<>=\\!])=(?!=)", "==");
		

		
		if (prepareSum)	{
			p1 = Pattern.compile("((?:\\&|\\||\\A)(?:\\(*)([a-zA-Z][a-zA-Z]*\\d*))==((?:Sum|Product)\\([^,]*,[^,]*,[^,]*,[^\\)]*\\)[^\\&\\|]*)");
			
			matcher = p1.matcher(assertion);
			while (matcher.find())	{
				assertion = assertion.replace(matcher.group(1) + "==" + matcher.group(3),
								matcher.group(1) + "=" + matcher.group(3) + "==" + matcher.group(2));
			}	
		}
		
		
		return assertion;
	}
	
	/**
	 * Creates a Map with the fixed variables (variable=double value).
	 * 
	 * @param assertion the String representing the assertion
	 * @return Map with the fixed variables and their values
	 */
	private Map<String, Double> getFixed(String assertion){
		
		HashMap<String, Double> fixed = new HashMap<String, Double>();
		Pattern p2;
		Matcher matcher;		
		
		
		p2 = Pattern.compile("((?:\\(|\\&|\\||\\A)([a-zA-Z][a-zA-Z]*\\d*\\b))==((\\d*(\\.\\d*)?)(?:\\)|\\||\\&|\\z))");

		matcher = p2.matcher(assertion);
		while (matcher.find())	{
			try	{
				fixed.put(matcher.group(2), Double.valueOf(matcher.group(4)));
			} catch (NumberFormatException nfe)	{
				// do not add as fixed Variable
				// this Exception can't be raised
				// because only proper Double values matches the regex
			}
		}
		
		return fixed;
	}

}
