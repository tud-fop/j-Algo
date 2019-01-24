package org.jalgo.module.lambda.model;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public interface ITermHandler {

	public void highlightPossibleBetaReductions(String position,
			boolean highligh);

	public Term getSubTerm(String position);

	public EAvailability checkPossibleBetaReduction(String position);

	public EAvailability betaReduce(String position);

	public boolean alphaConvert(String position, String newname);

	public Iterator<FormatString> getFormatString(boolean showBrackets);

	public Set<String> getPossibleBetaReductions(String position);

	public Set<String> getNeededAlphaConversions(String position);

	public boolean eliminateShortcut(String position);
	
	public boolean matchShortcut(String position);
	
	public void eliminateAllShortcuts(String position);
	
	public void makeAllShortcuts(String position);

	public String getLastOperationOutput();

	public EStepKind doLowLevelAutoStep(String position);
	
	public EStepKind doHighLevelAutoStep(String position);

	public Set<String> getAllUnusedVars();

	public boolean replaceSubTerm(String position, Term newterm);
	
	public List<Term> getAllChildren(String position);

}
