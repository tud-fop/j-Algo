package org.jalgo.module.hoare.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jalgo.module.hoare.constants.CodeDimension;
import org.jalgo.module.hoare.constants.ParserAccess;
import org.jalgo.module.hoare.constants.Rule;
import org.jalgo.module.hoare.constants.Status;
import org.jalgo.module.hoare.constants.TextStyle;
import org.nfunk.jep.ASTFunNode;
import org.nfunk.jep.ASTStart;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

/**
 * Represents a node of a verification tree.
 * 
 * @author Uwe,Thomas
 * 
 */

public abstract class VerificationFormula implements Serializable {

	protected int id;
	private static int nextId = 0;
	private String source;
	protected Rule appliedRule;
	protected boolean ruleApplied;
	protected boolean changed;
	protected Status status;
	protected CodeDimension code;
	protected VerificationFormula parent;
	protected List<VerificationFormula> children;
	protected AbstractAssertion preAssertion;
	protected AbstractAssertion postAssertion;

	/**
	 * Constructs a VerificationFormula with the given source code of the
	 * verification and the beginning and the end index of the part of the
	 * source code represented by this Formula.<br>
	 * The preAssertion and postAssertion will be set to <code>null</code>.
	 * The attributes changed and ruleApplied will be set to <code>false</code>. 
	 * 
	 * @param parent
	 *         the parent node in the VerificationFormula tree
	 * @param source
	 *         the whole source code of this verification        
	 * @param codeStart
	 *         startIndex of the SourceCode
	 * @param codeEnd
	 *         endIndex of the SourceCode
	 */
	VerificationFormula(VerificationFormula parent, String source, int codeStart, int codeEnd) {
		children = new ArrayList<VerificationFormula>();
		this.parent = parent;
		this.changed = false;
		this.ruleApplied = false;
		this.status = Status.RESULTWRONG;
		this.id = nextId++;
		this.source = source;
		code = new CodeDimension();
		code.start = codeStart;
		code.end = codeEnd;
		preAssertion = null;
		postAssertion = null;
	}

	/**
	 * Gets the source code of this VerificationFormula tree
	 * 
	 * @return {@link String} which contains the source code
	 */
	String getSource() {
		return source;
	}

	/**
	 * Gets a code segment from the source code Note: This method is needed to show
	 * you a great example of the java class overloading to produce the
	 * <code>getCode(getCode())</code> procedure call in the
	 * <code>getCode(boolean)</code> method.
	 * 
	 * @param indices
	 *         {@link CodeDimension} with the beginning and end of the code segment
	 * @return {@link String} representing the code segment
	 */
	protected String getCode(CodeDimension indices) {
		return (indices.start < source.length() && indices.end < source.length())
				? new String(source.substring(indices.start, indices.end + 1))
				: null;
	}

	/**
	 * Gets the beginning and end of this code segment.
	 * 
	 * @return CodeDimension containing startindex and endindex of the String
	 */
	public CodeDimension getCode() {
		return code;
	}

	/**
	 * Gives you the part of the source code which belongs to this rule. If
	 * parameter full is <code>true</code> the result will be the text segment in
	 * his full length. In the other case the result will be a simplified version,
	 * that represents the <code>VerificationFormula</code>
	 * 
	 * @param full
	 *         <code>true</code> will cause the result to be a full source code
	 *         segment
	 * @return the source code in a version you decided by the parameter
	 *         <code>full</code>
	 * 
	 */
	public abstract String getCode(boolean full);

	/**
	 * Sets the new parent <code>VerificationFormula</code>.
	 * 
	 * @param parent
	 *         the new parent VerificationFormula
	 */
	protected void setParent(VerificationFormula parent) {
		this.parent = parent;
	}

	/**
	 * Gets the parent of this <code>VerificationFormula</code>.
	 * 
	 * @return parent of the <code>VerificationFormula</code>
	 */
	public VerificationFormula getParent() {
		return parent;
	}

	/**
	 * Checks whether this <code>VerificationFormula</code> has children.
	 * 
	 * @return <code>true</code> if this <code>VerificationFormula</code> has
	 *         children
	 */
	public boolean hasChildren() {
		return !children.isEmpty();
	}

	/**
	 * Gets the children as List, whether it has children or not.
	 * 
	 * @return the {@link List} of children
	 */
	public List<VerificationFormula> getChildren() {
		return children; // this should never return null
	}

	/**
	 * Sets the list of children to the given list of children.
	 * 
	 * @param vfList
	 *         a {@link List} containing the new children
	 */
	void setChildren(List<VerificationFormula> vfList) {
		this.children = new ArrayList<VerificationFormula>(vfList);
	}

	/**
	 * Gets the preAssertion of this <code>VerificationFormula</code> as String.
	 * Note: If no preAssertion was assigned to the Formula you get an empty
	 * string!
	 * 
	 * @param ts kind of <code>String</code> you want to get back
	 * @return the preAssertion of this <code>VerificationFormula</code>
	 */
	public String getPreAssertion(TextStyle ts) {
		if (preAssertion == null) return "";
		switch (ts) {
		case SHORT:
			return preAssertion.toText(false);
		case SOURCE:
			return preAssertion.toString();
		case EDITOR:
			return preAssertion.getOrginal();
		default:
			return preAssertion.toText(true);
		}
	}

	/**
	 * Gets the postAssertion of this <code>VerificationFormula</code> as String.
	 * Note: If no preAssertion was assigned to the Formula you get an empty
	 * string!
	 * 
	 * @param ts
	 *         kind of <code>String</code> you want to get back
	 * @return the postAssertion of this <code>VerificationFormula</code>
	 */
	public String getPostAssertion(TextStyle ts) {
		if (postAssertion == null) return "";
		switch (ts) {
		case SHORT:
			return postAssertion.toText(false);
		case SOURCE:
			return postAssertion.toString();
		case EDITOR:
			return postAssertion.getOrginal();
		default:
			return postAssertion.toText(true);
		}
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id of this <code>VerificationFormula</code>
	 */
	public int getId() {
		return id;
	}

	/**
	 * Checks whether the Rule is already applied.
	 * 
	 * @return <code>true</code> if the rule is already applied
	 */
	public boolean isRuleApplied() {
		return ruleApplied;
	}

	/**
	 * Sets this VerificationFormula applied.
	 */
	public void setApplied() {
		ruleApplied = true;
	}

	/**
	 * Returns the {@link Rule} that was Applied to this
	 * <code>VerificationFormula</code>.
	 * 
	 * @return <code>Rule</code> that was Applied
	 */
	public Rule getAppliedRule()	{
		return appliedRule;
	}

	/**
	 * Replaces the preAssertion by a new one.
	 * 
	 * @param preAssertion
	 *         the new preAssertion
	 */
	void replacePreAssertion(AbstractAssertion preAssertion) {
		this.preAssertion = preAssertion;
		setChanged(true);
	}

	/**
	 * Replaces the postAssertion by a new one.
	 * 
	 * @param postAssertion
	 *         the new postAssertion
	 */
	void replacePostAssertion(AbstractAssertion postAssertion) {
		this.postAssertion = postAssertion;
		setChanged(true);
	}

	/**
	 * Deletes all {@link StrongPreFormula} and {@link WeakPostFormula} and set all
	 * other <code>VerificationFormula</code>s invisible. That means that
	 * ruleApplied is set to false for all following
	 * <code>VerificationFormula</code>s.
	 */
	void deleteChildren() {
		for (VerificationFormula vf : children) {
			if (vf instanceof WeakPostFormula || vf instanceof StrongPreFormula) {
				vf.deleteChildren();
				this.children = vf.getChildren();
				for (VerificationFormula ch : vf.getChildren())	{
					ch.setParent(this);
				}
			} else	{
				vf.ruleApplied = false;
				vf.deleteChildren();
			}
		}
	}

	/**
	 * This method builds an assertion tree from an {@link String}.
	 * The method uses XJep to parse the String into a tree of
	 * assertions.<br>
	 * Note: This method returns <code>null</code> if the given
	 * assertion is <code>null</code> or an empty String
	 * 
	 * @param assertion
	 *         The {@link String} which should be transformed.
	 * @return the assertion tree as an {@link AbstractAssertion} or
	 *         <code>null<code> if the assertion is empty
	 * @throws ParseException
	 *         if the String can not be parsed by XJep
	 */
	private AbstractAssertion buildAssertion(String assertion)
			throws ParseException {

		if ((assertion == null) || (assertion.length() == 0)) {
			return null;
		} else {
			return build(ParserAccess.parse(assertion));
		}
	}
	
	/**
	 * This method builds an assertion tree from an {@link Node}.
	 * The method goes recursive through the AST-tree by calling
	 * it self for each node.<br>
	 * Note: This method returns <code>null</code> if the given
	 * tree is <code>null</code>
	 * 
	 * @param tree
	 *         The {@link Node} representing an AST-tree.
	 * @return the assertion tree as an {@link AbstractAssertion} or
	 *         <code>null<code> if the tree is <code>null</code>
	 */
	private AbstractAssertion build(Node tree)	{
		// What to do when the Node is empty
		if (tree==null) return null;
		
		if (tree instanceof ASTStart)	{
			return build(tree.jjtGetChild(0));
		} else	{
			if (tree instanceof ASTFunNode)	{
				String foo=((ASTFunNode) tree).getName();
				
				if (foo.equals("&&")) {
					//AndAssertion
					return new AndAssertion( build(tree.jjtGetChild(0)),
							                       build(tree.jjtGetChild(1)) );
				} else if (foo.equals("||")){
					//OrAssertion
					return new OrAssertion( build(tree.jjtGetChild(0)),
				 		                      build(tree.jjtGetChild(1)) );
				} else if (foo.equals("!")){
					//NotAssertion
					return new NotAssertion(build(tree.jjtGetChild(0)));							
				} else {
					//ConcreteAssertion
					String code=ParserAccess.getString(tree).replaceAll("\\.0","");
					return new ConcreteAssertion(code,0);
				}
			} else {
				//not a FunNode or a Start what to do now???
				//For Example Variables like (a)
				//->Use this Section if you want to implement usage of
				//  Variables in an Assertion
				String code=ParserAccess.getString(tree);
				return new ConcreteAssertion(code,0);
			}
		}
	}

	/**
	 * Checks if the <code>PreAssertion</code> is a Variable and can be edited.
	 * Note: Editing means that a User can define an underlying assertion to this
	 * Variable.
	 * 
	 * @return true if the <code>PreAssertion</code> is a {@link VarAssertion}
	 */
	public boolean canEditPreAssertion() {
		return (preAssertion != null ? preAssertion.isVariable() : false);
	}

	/**
	 * Edits the <code>PreAssertion</code> of this VerificationFormula and sets
	 * the given {@link AbstractAssertion} as the underlying assertion to the
	 * <code>PreAssertion</code>. Note: This is only possible if the
	 * <code>PreAssertion</code> is a reference to an {@link VarAssertion}.
	 * 
	 * @param assertion
	 *         that should become the underlying assertion of the
	 *         <code>PreAssertion</code>
	 * @return <code>true</code> if the editing was successful
	 * @throws ParseException 
	 *          if the String can not be parsed by XJep
	 * @throws UnsupportedOperationException
	 *          if the <code>PreAssertion</code> has been frozen 
	 */
	public boolean editPreAssertion(String assertion)
			throws ParseException, UnsupportedOperationException {

		boolean result = false;
		if ((canEditPreAssertion()) && (preAssertion instanceof VarAssertion)) {
			VarAssertion variable=(VarAssertion) preAssertion;
			variable.setVariable(buildAssertion(assertion));
			variable.setOriginal(assertion);
			//status = Status.UNCHANGED;
			setChanged(true);
			result = true;
		}
		
		return result;
	}

	/**
	 * Checks if the <code>PostAssertion</code> is a Variable and can be edited.
	 * Note: Editing means that a User can define an underlying assertion to this
	 * Variable.
	 * 
	 * @return true if the <code>PostAssertion</code> is a {@link VarAssertion}
	 */
	public boolean canEditPostAssertion() {
		return (postAssertion != null ? postAssertion.isVariable() : false);
	}

	/**
	 * Edits the <code>PostAssertion</code> of this VerificationFormula and sets
	 * the given {@link AbstractAssertion} as the underlying assertion to the
	 * <code>POstAssertion</code>. Note: This is only possible if the
	 * <code>PostAssertion</code> is a reference to an {@link VarAssertion}.
	 * 
	 * @param assertion
	 *         that should become the underlying assertion of the
	 *         <code>PostAssertion</code>
	 * @return <code>true</code> if the editing was successful
	 * @throws ParseException 
	 *          if the String can not be parsed by XJep
	 * @throws UnsupportedOperationException
	 *          if the <code>PreAssertion</code> has been frozen
	 */
	public boolean editPostAssertion(String assertion)
	  throws ParseException,UnsupportedOperationException {

		boolean result = false;
		if ((canEditPostAssertion()) && (postAssertion instanceof VarAssertion)) {
			VarAssertion variable=(VarAssertion) postAssertion;
			variable.setVariable(buildAssertion(assertion));
			variable.setOriginal(assertion);			
			//status = Status.UNCHANGED;
			setChanged(true);
			result = true;
		}
		
		return result;
	}

	/**
	 * Creates a StrongPreFormula.
	 * 
	 * @param varName
	 *         the name of the created Variable (Px)
	 */
	boolean createStrongPre(String varName) {
		for (VerificationFormula vf : children) {
			if (vf.isRuleApplied()) {
				return false;
			}
		}
		
		AbstractAssertion variable=new VarAssertion(varName);
		VerificationFormula implication=new ImplicationFormula(this,code.start, code.end,
  		                                                     preAssertion,variable);
		implication.setChanged(true);
		implication.setApplied();
		VerificationFormula strongpre=new StrongPreFormula(this, source, code.start,
  		                                                 code.end,variable,
  		                                                 postAssertion);
		// illegal object state
		if (children.size() > 2) {
			return false;
		}
		if (hasChildren()) {
			for (VerificationFormula vf : children) {
				vf.setParent(strongpre);
			}
			strongpre.setChildren(this.getChildren());
			children.clear();
		}
		children.add(implication);
		children.add(strongpre);
		return true;
	}

	/**
	 * Creates a WeakPostFormula.
	 * 
	 * @param varName
	 *         the name of the created Variable (Px)
	 */
	boolean createWeakPost(String varName) {
		for (VerificationFormula vf : children) {
			if (vf.isRuleApplied()) {
				return false;
			}
		}

		AbstractAssertion variable=new VarAssertion(varName);
		VerificationFormula weakpost=new WeakPostFormula(this, source, code.start, code.end,
  		                                                preAssertion,variable);
		VerificationFormula implication=new ImplicationFormula(this, code.start, code.end, variable, 
  		                                                     postAssertion);
		implication.setChanged(true);
		implication.setApplied();

		// illegal object state
		if (children.size() > 2) {
			return false;
		}
		if (hasChildren()) {
			for (VerificationFormula vf : children) {
				vf.setParent(weakpost);
			}
			weakpost.setChildren(this.getChildren());
			children.clear();
		}
		children.add(weakpost);
		children.add(implication);
		return true;
	}

	/**
	 * Applies the next <code>Rule</code>.
	 * 
	 * @param vf
	 *         the parent of the <code>VerificationFormula</code> to be created
	 * @return <code>true</code> if a <code>VerificationFormula</code> was
	 *         created successfully
	 * @throws UnsupportedOperationException if something important failed
	 */
	abstract boolean applyNext(VerificationFormula parent)
		throws UnsupportedOperationException;

	/**
	 * Checks whether the Rule can be applied to this
	 * <code>VerificationFormula</code>.
	 * 
	 * @param rule
	 *         the Rule to be applied
	 * @return <code>true</code> if the Rule can be applied to this
	 *         VerificationFormula
	 */
	abstract boolean canApply(Rule rule);

	/**
	 * This Method performs a recursive collection of all the children and it self
	 * into a Map. This Map uses the Id of the <code>VerificationFormula</code>
	 * as Key.
	 * 
	 * @param formulas
	 *         reference of the concrete Map
	 */
	void getFormulaMap(Map<Integer, VerificationFormula> formulas) {
		for (VerificationFormula vf : children) {
			vf.getFormulaMap(formulas);
		}
		formulas.put(id, this);
	}

	/**
	 * Gives you the information whether this <code>VerificationFormula</code>
	 * was changed or not Note: That it is your opportunity to reset the Value.
	 * 
	 * @return if the <code>VerificationFormula</code> has been changed
	 */
	public boolean hasChanged() {
		return changed;
	}

	/**
	 * Sets the value of <code>isChanged</code> to the given value
	 * 
	 * @param changed
	 *         the value that <code>isChanged</code> should have
	 */
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	/**
	 * This method provides the verifiable check for <code>preAssertion</code>.
	 * This check returns only <code>true</code> if all {@link VarAssertions} in
	 * the pre assertion tree have underlying assertions.
	 * 
	 * @return <code>true</code> if the pre assertion can be verified
	 */
	public boolean hasFilledPreAssertion() {
		return preAssertion.verifiable();
	}

	/**
	 * This method provides the verifiable check for <code>postAssertion</code>.
	 * This check returns only <code>true</code> if all {@link VarAssertions} in
	 * the post assertion tree have underlying assertions.
	 * 
	 * @return <code>true</code> if the post assertion can be verified
	 */
	public boolean hasFilledPostAssertion() {
		return postAssertion.verifiable();
	}

	/**
	 * This method provides the verifiable check of this Formula.
	 * Every Formula returns by default <code>false</code> and
	 * should be overridden by Subclasses which can be verified.<br>
	 * Note: {@link StrongPreFormula},{@link WeakPostFormula} and
	 * {@link AssignFormula} override this method.
	 *  
	 * @return <code>false</code> by default<br>
	 *         Subclasses sometimes implement there own check. 
	 */
	public boolean verifiable() {
		return false;
	}

	/**
	 * Gives you the status of this <code>VerificationFormula</code>
	 * 
	 * @return the current {@link Status}
	 */
	public Status getStatus() {
		return status;
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
	public void setStatus(Status status) {
		if (! this.status.equals(status)) {
			setChanged(true);
			this.status = status;
		}
	}

	/**
	 * Gives you <code>true</code> whether the Formula is an implication or
	 * <code>false</code> if not.<br>
	 * Note: This method will be overridden by the {@link ImplicationFormula} to
	 * perform this task. 
	 * 
	 * @return <code>true</code> if the Formula is a {@link ImplicationFormula}
	 */
	public boolean isImplication(){
		return false;
	}
	
	/**
	 * Gives by default <code>true</code> but in case of an
	 * {@link IterationFormula} it returns only <code>true</code>
	 * if the post assertion has the correct from.<br>
	 * <br>
	 * {P1} while(pi) {P2}<br>
	 * P2 must have the form (P1 && !(pi))
	 * 
	 * @return <code>true</code> by default but is overridden by
	 *         {@link IterationFormula}
	 */
	public boolean isCorrect(){
		return ((appliedRule == Rule.ITERATION) ? check(this) : true);
	}

	/**
	 * Tests for weak post, strong pre and iteration formulas
	 * if the post assertion has the correct form:
	 * {P1} while(pi) {P2}<br>
	 * P2 must have the form (P1 && !(pi)) 
	 * 
	 * @param formula the VerificationFormula to check
	 * @return
	 * 		true if post assertion consists of
	 * 		pre assertion AND NOT boolean expression
	 */
	protected boolean check(VerificationFormula formula) {
		return true;
	}
	
	/**
	 * Searches for the highest id in this <code>VerificationFormula</code> tree.
	 * 
	 * @return the highest id in this <code>VerificationFormula</code> tree
	 */
	private int getHighestId()	{
		int result = this.id;
		int help;
		
		for (VerificationFormula vf : getChildren())	{
			help = vf.getHighestId();
			result = ((help > result) ? help : result);
		}

		return result;
	}
	
	/**
	 * Sets the nextId to the value which represents the highest id in the
	 * <code>VerificationFormula</code> tree increased by 1.
	 * 
	 * @param root the root of the <code>VerificationFormula</code> tree
	 * @return true if nextId is now the highestID+1 of the
	 *  <code>VerificationFormula</code> tree
	 */
	static boolean setNextId(VerificationFormula root)	{
		if (root == null)	{
			return false;
		}

		if (root.parent == null)	{
			nextId = root.getHighestId()+1;
			return true;
		} else	{
			return false;
		}
	}
	
	
}
