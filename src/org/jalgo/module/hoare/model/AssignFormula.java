package org.jalgo.module.hoare.model;

import java.util.Vector;

import org.jalgo.module.hoare.constants.Rule;
import org.jalgo.module.hoare.constants.Status;
import org.jalgo.module.hoare.constants.TextStyle;
import org.jalgo.module.hoare.constants.ParserAccess;
import org.lsmp.djep.xjep.XJep;
import org.nfunk.jep.Node;

/**
 * Represents a single assignment.
 * It is the leaf of the verification tree.
 * 
 * @author Thomas, Uwe
 *
 */

public class AssignFormula extends VerificationFormula {
	
	private static final long serialVersionUID = 6878923401432559946L;

	/**
	 * Constructs a AssignFormula with the given beginning and end of the code
	 * and sets the parent and the source of
	 * the whole <code>VerificationFormula</code> tree.
	 * 
	 * @param parent the parent node in the VerificationFormula tree
	 * @param source the source of the whole VerificationFormula tree
	 * @param codeStart startIndex of the SourceCode
	 * @param codeEnd endIndex of the SourceCode
	 */
	AssignFormula(VerificationFormula parent, String source, int codeStart, int codeEnd){
		super(parent, source, codeStart,codeEnd);		
	}
	
	/**
	 * @see org.jalgo.module.hoare.model.VerificationFormula#canApply(Rule)
	 */
	boolean canApply(Rule rule)	{
		return rule.equals(Rule.ASSIGN);
	}

	/**
	 * @see org.jalgo.module.hoare.model.VerificationFormula#applyNext(VerificationFormula)
	 */
	boolean applyNext(VerificationFormula parent)
		throws UnsupportedOperationException	{

		String[] replace = new String[2];
		String pre;
		String post;
		String code;
		
		pre = parent.getPreAssertion(TextStyle.SOURCE).replaceAll("\\s", "").replaceAll(";", "").replaceAll("(?<![<>=!])=(?![=])", "==");
		post = parent.getPostAssertion(TextStyle.SOURCE).replaceAll("\\s", "").replaceAll(";", "").replaceAll("(?<![<>=!])=(?![=])", "==");
		code = parent.getCode(parent.code).replaceAll("\\s", "").replaceAll(";", "");
		replace = code.split("=", 2);

		XJep parser = ParserAccess.createParser();
		try {
			Node preTop = ParserAccess.parse(pre);//parser.parse(pre);
			Node postTop = ParserAccess.parse(post);//parser.parse(post);
			Vector<org.nfunk.jep.Variable> variables = new Vector<org.nfunk.jep.Variable>();
			variables = (Vector<org.nfunk.jep.Variable>)parser.getVarsInEquation(postTop, variables);
			String[] variableNames = new String[variables.size()];
			Node[] variableNodes = new Node[variables.size()];
			for (int i=0; i<variableNames.length; i++) {
				variableNames[i] = variables.get(i).getName();
				if (variableNames[i].equals(replace[0])) {
					variableNodes[i] = parser.parse(replace[1]);
				}
				else {
					variableNodes[i] = ParserAccess.parse(variableNames[i]);
				}
			}
			parser.substitute(postTop, variableNames, variableNodes);
			if (parser.toString(preTop).equals(parser.toString(postTop))) {
				parent.appliedRule = Rule.ASSIGN;
				parent.status = Status.RESULTOK;
				parent.setChanged(true);
				return true;
			}
			else {
				parent.status = Status.RESULTWRONG;
				parent.setChanged(true);
				return false;
			}
		}
		catch (Exception e) {
			parent.status = Status.RESULTWRONG;
			parent.setChanged(true);
			return false;
		}
		catch (Error e) {
			parent.status = Status.RESULTWRONG;
			parent.setChanged(true);
			return false;
		}
	}

	/**
	 * 
	 * @see org.jalgo.module.hoare.model.VerificationFormula#getCode(boolean)
	 */
	@Override
	public String getCode(boolean full) {
		return (full ? getCode(getCode()) : "x=r;");
	}
	
	

	/**
	 * This method performs a {@link Status} change of this
	 * <code>VerificationFormula</code><br>
	 * Note: That the Status can only be changed if the
	 * <code>VerificationFormula</code> is <code>verifiable</code>.
	 * 
	 * @param status
	 *         the {@link Status} to set
	 */
	@Override
	public void setStatus(Status status) {
		if (! this.status.equals(status)) {
			setChanged(true);
			this.status = status;
			if (status == Status.RESULTWRONG)	{
				appliedRule = null;
			}
		}
	}
}