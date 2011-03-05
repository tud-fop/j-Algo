package org.jalgo.module.c0h0.models.tmodel;

import java.util.HashMap;
import java.util.Stack;

import org.jalgo.module.c0h0.models.ast.ASTVisitor;
import org.jalgo.module.c0h0.models.ast.Assignment;
import org.jalgo.module.c0h0.models.ast.Block;
import org.jalgo.module.c0h0.models.ast.If;
import org.jalgo.module.c0h0.models.ast.While;

/**
 * saves the corresponding rules for the tree structured addresses
 * @author mathias.kaufmann
 */
public class TGeneratingVisitor implements ASTVisitor {
	private HashMap<String,String> ruleList;
	private String assignmentRule;
	private String ifRule;
	private String ifElseRule;
	private String blockRule;
	private String whileRule;
	private Stack<String> nesa;
	
	/**
	 * sttrans-rule-library
	 */
	public TGeneratingVisitor() {
		ruleList           = new HashMap<String,String>();
		nesa			   = new Stack<String>();
		String langBracket = "&lt;";
		String rangBracket = "&gt;";
		assignmentRule     = 	"<i>sttrans</i>(xi = <i>se</i>, a, b, <i>m</i>) :=<br>" +
								"&nbsp;&nbsp;&nbsp;&nbsp;fa :: Int -> ... -> Int (<i>m</i>-mal ->)<br>" +
								"&nbsp;&nbsp;&nbsp;&nbsp;fa x1 ... xm = fb x1 ... x(<i>i</i> − 1) <i>setrans</i>(<i>se</i>) x(<i>i</i> + 1) ... xm<br>" +
								"&nbsp;&nbsp;f&uuml;r alle <i>se</i> ∈ <i>W</i>(" + langBracket + "SimpleExpression" + rangBracket + "), a, b ∈ <i>Adr</i> und <i>i</i>,<i>m</i> ≥ 1 mit <i>i</i> ≤ <i>m</i>";
		ifRule             = 	"<i>sttrans</i>(if (<i>be</i>) <i>stat</i>, a, b, <i>m</i>) :=<br>" + 
								"&nbsp;&nbsp;&nbsp;&nbsp;fa :: Int -> ... -> Int (<i>m</i>-mal ->)<br>" +
								"&nbsp;&nbsp;&nbsp;&nbsp;fa x1 ... xm = if <i>betrans</i>(<i>be</i>) then fa1 x1 ... xm<br>" + 
								"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;else fb &nbsp;x1 ... xm<br>" +
								"&nbsp;&nbsp;&nbsp;&nbsp;<i>sttrans</i>(<i>stat</i>, a1, b, <i>m</i>)<br>" +
								"&nbsp;&nbsp;f&uuml;r alle <i>be</i> ∈ <i>W</i>(" + langBracket + "BoolExpression" + rangBracket + "), <i>stat</i> ∈ <i>W</i>(" + langBracket + "Statement" + rangBracket + "), a, b ∈ <i>Adr</i> und <i>m</i> ≥ 1";
		ifElseRule         =	"<i>sttrans</i>(if (<i>be</i>) <i>stat</i><sub>1</sub> else <i>stat</i><sub>2</sub>, a, b, <i>m</i>) :=<br>" + 
								"&nbsp;&nbsp;&nbsp;&nbsp;fa :: Int -> ... -> Int (<i>m</i>-mal ->)<br>" + 
								"&nbsp;&nbsp;&nbsp;&nbsp;fa x1 ... xm = if <i>betrans</i>(<i>be</i>) then fa1 x1 ... xm<br>" + 
								"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;else else fa2 x1 ... xm<br>" + 
								"&nbsp;&nbsp;&nbsp;&nbsp;<i>sttrans</i>(<i>stat</i><sub>1</sub>, a1, b, <i>m</i>)<br>" + 
								"&nbsp;&nbsp;&nbsp;&nbsp;<i>sttrans</i>(<i>stat</i><sub>2</sub>, a2, b, <i>m</i>)<br>" + 
								"f&uuml;r alle <i>be</i> ∈ <i>W</i>(" + langBracket + "BoolExpression" + rangBracket + "), <i>stat</i><sub>1</sub>, <i>stat</i><sub>2</sub> ∈ <i>W(" + langBracket +"Statement" + rangBracket +"), a, b ∈ <i>Adr</i> und <i>m</i> ≥ 1";
		blockRule          =	"<i>sttrans</i>({ <i>stat</i><sub>1</sub> <i>stat</i><sub>2</sub> . . . <i>stat</i><sub>n</sub> }, a, b, <i>m</i>) :=<br>" + 
								"&nbsp;&nbsp;&nbsp;&nbsp;fa :: Int -> ... -> Int (<i>m</i>-mal ->)<br>" + 
								"&nbsp;&nbsp;&nbsp;&nbsp;fa x1 ... xm = fa1 x1 ... xm<br>" + 
								"&nbsp;&nbsp;&nbsp;&nbsp;<i>sttrans</i>(<i>stat</i><sub>1</sub>, a1, a2, <i>m</i>)<br>" + 
								"&nbsp;&nbsp;&nbsp;&nbsp;<i>sttrans</i>(<i>stat</i><sub>2</sub>, a2, a3, <i>m</i>)<br>" + 
								"&nbsp;&nbsp;&nbsp;&nbsp;.&nbsp;.&nbsp;.<br>" +
								"&nbsp;&nbsp;&nbsp;&nbsp;<i>sttrans</i>(<i>stat</i><sub>n</sub>, an, b, <i>m</i>)<br>" + 
								"f&uuml;r alle <i>stat</i><sub>1</sub>, . . . , <i>stat</i><sub>n</sub> ∈ W(" + langBracket + "Statement" + rangBracket + "), a, b ∈ <i>Adr</i> und <i>m</i> ≥ 1";
		whileRule          = 	"<i>sttrans</i>(while (<i>be</i>) <i>stat</i> , a, b, <i>m</i>) :=<br>" + 
								"&nbsp;&nbsp;&nbsp;&nbsp;fa :: Int -> ... -> Int (<i>m</i>-mal ->)<br>" + 
								"&nbsp;&nbsp;&nbsp;&nbsp;fa x1 ... xm = if <i>betrans</i>(<i>be</i>) then fa1 x1 ... xm<br>" +
								"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;else else fb &nbsp;x1 ... xm<br>" + 
								"&nbsp;&nbsp;&nbsp;&nbsp;<i>sttrans</i>(<i>stat</i>, a1, a, <i>m</i>)<br>" + 
								"f&uuml;r alle <i>be</i> ∈ <i>W</i>(" + langBracket + "BoolExpression" + rangBracket + "), <i>stat</i>	∈ <i>W</i>(" + langBracket + "Statement" + rangBracket + ")," + "a, b ∈ <i>Adr</i>, <i>m</i> ≥ 1 (beachte: die 2. Adresse lautet a)";
	}
	
	public void visitAssignment(Assignment assignment) {
		ruleList.put(assignment.getAddress(), assignmentRule);
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.c0h0.models.ast.ASTVisitor#visitBlock(org.jalgo.module.c0h0.models.ast.Block)
	 * Block-Statement ist nur bei Else (ohne Block) besonders.
	 * Auf dem Stack liegt die naechste Else-BSA (falls moeglich)
	 * 
	 */
	public void visitBlock(Block block) {
		if (!nesa.isEmpty())
			if (block.getAddress() == nesa.lastElement()) {
				ruleList.put(block.getAddress(), ifElseRule);
				nesa.pop();
				return;
			}
		ruleList.put(block.getAddress(), blockRule);
	}

	public void visitWhile(While whileStatement) {
		ruleList.put(whileStatement.getAddress(), whileRule);
	}

	public void visitIf(If ifStatement) {
		if (ifStatement.getElseSequence().getSequence().isEmpty()) {
			ruleList.put(ifStatement.getAddress(), ifRule);
		} else {
			ruleList.put(ifStatement.getAddress(), ifElseRule);
			nesa.push(ifStatement.getElseSequence().getAddress());
		}
	}

	/**
	 * returns the rule assigned to an address
	 * 
	 * @param markedNode
	 * @return the rule
	 */
	public String getRuleByAddress(String markedNode) {
		return this.ruleList.get(markedNode);
	}
}
