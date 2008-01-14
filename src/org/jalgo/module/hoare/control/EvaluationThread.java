package org.jalgo.module.hoare.control;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.model.Assertion;
import org.lsmp.djep.xjep.XJep;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

public class EvaluationThread extends Thread {

	public interface ErrorReporter {
		public void report(Exception message);
	}

	private XJep evaluator;

	private String string_source;

	private String string_target;

	private Assertion assertion_source, assertion_target;

	private int base = 50;

	private JepEvaluator father = null;

	private Integer myId = null;

	private Integer timeToLive = 10000;

	private Set<String> bound_parameters = new HashSet<String>();

	private Set<String> unbound_parameters = new HashSet<String>();

	private Map<String, Integer> fixed_parameters = new HashMap<String, Integer>();

	private Node left;

	private Node right;

	private Set<String> currentVars = new HashSet<String>();
	private boolean stop = false;

	public EvaluationThread(Assertion assertion_source,
			Assertion assertion_target, JepEvaluator father, int myId,
			int time, int base) {

		this.timeToLive = time * 1000;
		this.myId = myId;
		this.father = father;
		this.assertion_source = assertion_source;
		this.assertion_target = assertion_target;
		this.base = base;
		evaluator = new XJep();
		initJep();

	}

	private void initJep() {

		evaluator.addStandardConstants();
		evaluator.addStandardFunctions();
		evaluator.setAllowUndeclared(true);
		evaluator.setImplicitMul(true);
		evaluator.setAllowAssignment(true);
		evaluator.removeVariable("e");
		evaluator.removeVariable("pi");
	}

	public void run() {
		int count = 0; // count the number of runs

		// Apply syntactic modifications and convert into String
		try {
			string_source = translate(assertion_source);
			string_target = translate(assertion_target);
		} catch (Exception e) {
			father.reportError(myId, Messages.getString("hoare",
					"out.evalParserError"));
			return;
		}

		double max = Math.pow(base, unbound_parameters.size()); // max number of
																// tests

		// set values of variables
		for (String var : fixed_parameters.keySet()) {
			evaluator.addVariable(var.toString(), Double
					.valueOf(fixed_parameters.get(var)));
		}
		for (String var : unbound_parameters) {
			evaluator.addVariable(var, Math.round(Math.random() * base
					- (base / 2)));

		}

		// parse expressions
		try {
			left = evaluator.parse(string_source);
			right = evaluator.parse(string_target);
		} catch (ParseException e1) {
			father.reportError(myId, Messages.getString("hoare",
					"out.evalParserError"));
			return;
		}

		// Save the thread's start time
		double startTime = System.currentTimeMillis();

		while (count <= max) {
			if(stop) return;
			// Give the system the chance to switch between threads every 500
			// steps
			if (count % 500 == 0) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {

				}
			}

			// Set variables to random values for testing
			for (String var : unbound_parameters) {
				evaluator.setVarValue(var, Math.round(Math.random() * base
						- (base / 2)));
			}
			
			//System.out.println(evaluator.getSymbolTable());

			try {
				
				if (evaluator.evaluate(left).equals(1.0)
						&& evaluator.evaluate(right).equals(0.0)) {

					for (String var : unbound_parameters) {
						currentVars.add(evaluator.getVar(var).toString());
					}
					father.putResult(myId, false, currentVars);
					return;
				}
			} catch (ParseException ee) {
				father.reportError(myId, Messages.getString("hoare",
							"out.evalParserError"));
					return;
			}

			count++;

			// Check every 5 steps if the time is up and the thread should be
			// terminated
			if (count % 5 == 0) {
				if (startTime + timeToLive < System.currentTimeMillis()) {
					break;
				}
			}
		}
		//System.out.println(true);
		father.putResult(myId, true, currentVars);
	}

	/**
	 * Translates an assertion into a Jeval-usable syntax
	 * 
	 * @param input -
	 *            assertion to translate
	 * @return a String of the translated assertion
	 */
	private String translate(Assertion input) {
		/*
		 * This function realizes the translation of assertion-data-format into
		 * the Jeval-format. The main task is to find and classify parameters in
		 * the assertions.
		 */

		String tmp = input.toString(); // will be returned after 'translation'

		// create search patterns for parameters
		Pattern p_bound_variables = Pattern
				.compile("[A-Za-z]+\\([^,]*,(?:([a-zA-Z][a-zA-Z0-9]*)),[^,],[^,]*");
		Pattern p_unbound_variables = Pattern
				.compile("(?:([a-zA-Z][a-zA-Z0-9]*\\b))([^\\(])");
		Pattern p_fixed_variables = Pattern
				.compile("\\((?:([a-zA-Z][a-zA-Z0-9]*\\b))=([0-9]+)\\)");
		Matcher matcher = null;

		// find bounded variables in assertions
		matcher = p_bound_variables.matcher(tmp);
		while (matcher.find()) {
			bound_parameters.add(matcher.group(1));
		}

		// find unbounded variables in assertions
		matcher = p_unbound_variables.matcher(tmp);
		while (matcher.find()) {
			if (!bound_parameters.contains(matcher.group(1))
					&& !fixed_parameters.containsKey(matcher.group(1)))
				unbound_parameters.add(matcher.group(1));
		}

		// find fixed variables (like "(x=0)")
		matcher = p_fixed_variables.matcher(tmp);
		while (matcher.find()) {

			// if a parameter is fixed several times then take the first
			// occurance
			if (!fixed_parameters.containsKey(matcher.group(1)))
				fixed_parameters.put(matcher.group(1), Integer.valueOf(matcher
						.group(2)));
			unbound_parameters.remove(matcher.group(1));
		}

		// -- replace all "=" with "=="
		Pattern p_equal = Pattern.compile("(?<=[^<>])=");
		matcher = p_equal.matcher(tmp);
		while (matcher.find()) {
			tmp = matcher.replaceAll("==");
		}

		return tmp;
	}

	public void end(){
		this.stop = true;
	}
}
