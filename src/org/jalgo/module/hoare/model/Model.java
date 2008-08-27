package org.jalgo.module.hoare.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.constants.Rule;
import org.jalgo.module.hoare.constants.TextStyle;

import c00.AST;

/**
 * This is the master control program of the hole data structure.<br> 
 * <br>
 * To initialize a verification tree you have two possibilities<br>      
 * 1) call the <code>init</code>-method to create a new verification tree
 * from a parsed C0-source code<br>
 * 2) call the overloaded <code>init</code>-method to create
 * a new verification tree from a given root-{@link VerificaitonFormula}<br>
 * To save an existing verification tree you have to perform two steps<br> 
 * 1) collect all the {@link VerificationFormulas} with
 * the <code>getFormulas</code>-method<br>
 * 2) get the index of the root-{@link VerificaitonFormula} by calling
 *  <code>getRoot().getId()</code><br>
 *
 * 
 * @author Thomas
 */
public class Model extends Observable {

	private VerificationFormula root;
	private Map<Integer,VerificationFormula> formulas;
	private SortedSet<Variable> variables;
	private int varCount;
	private int piCount;
	
	/**
	 * Creates a new instance of the Model.
	 */
	public Model() {
		root=null;
		formulas=new HashMap<Integer,VerificationFormula>();
		variables=new TreeSet<Variable>();
		
		varCount=0;
		piCount=0;
	}

	/**
	 * Sets the root of the Verification tree to the given VerificationFormula
	 * Note: This method calls the method <code>updateFormulas()</code> to
	 * update the map of Formulas. So you do not have to call this method after
	 * using the <code>setRoot</code>-operation.
	 * 
	 * @param vf the VerificationFormula to be set as root 
	 */
	private void setRoot(VerificationFormula vf){
		if (vf!=null) {
			root=vf;
			updateFormulas();
		}
	}
	
	/**
	 * Gives you the root of the Verification tree
	 * 
	 * @return the root 
	 */
	public VerificationFormula getRoot(){
		return root;
	}

	/**
	 * Returns the {@link VerificationFormula} specified by the Id. 
	 * Every {@link VerificationFormula} could have one or two children.
	 * 
	 * @param id
	 *        the Index of a {@link VerificationFormula}
	 * @return the selected {@link VerificationFormula} and
	 *         <code>null</code> if no {@link VerificationFormula} was found 
	 */
	public VerificationFormula getVerificationFormula(int id){
		VerificationFormula result=null;
		
		if (formulas!=null) {
			result=formulas.get(new Integer(id));
		}
		
		return result;
	}
	
	/**
	 * This method to performs a recursive collection  
	 * of VerificationFormulas and there IDs.
	 * This routine is for internal use, and could  
	 * cause a leak of performance if you use it to often.
	 * 
	 */
	public void updateFormulas(){
		formulas.clear();
		variables.clear();
		varCount=0;
	
		if (root!=null) {
			root.getFormulaMap(formulas);
			Variable var;
			SortedSet<Variable> vars=new TreeSet<Variable>();
   
			for (VerificationFormula vf : formulas.values())	{
				if (vf.canEditPreAssertion()){
					var=new Variable(vf.getId(),
							vf.getPreAssertion(TextStyle.SHORT),
							vf.getPreAssertion(TextStyle.FULL),
							vf.getPreAssertion(TextStyle.EDITOR),                      
							vf.getPreAssertion(TextStyle.SOURCE),
							true);
					if (vf.isRuleApplied())	{
						variables.add(var);
					}
					vars.add(var);
				}
				
				if (vf.canEditPostAssertion())	{
					var=new Variable(vf.getId(),
							vf.getPostAssertion(TextStyle.SHORT),
							vf.getPostAssertion(TextStyle.FULL),
							vf.getPostAssertion(TextStyle.EDITOR),
							vf.getPostAssertion(TextStyle.SOURCE),
							false);
					if (vf.isRuleApplied()){
						variables.add(var);
					}
					vars.add(var);
				}				
			}
			Pattern p = Pattern.compile("([A-Za-z]*)([0-9]+)$");
			Matcher m = p.matcher(vars.last().getName());
			if (m.matches())	{
				varCount=Integer.parseInt(m.group(2));
			} else	{
				varCount=vars.size();
			}
			vars.clear();
			vars=null;
		}
	}
	
	/**
	 * Gives you a Map of all VerificationFormulas
	 * selected by their ID.
	 * 
	 * @return the Map of ID and VerificationFormula
	 */
	public Map<Integer,VerificationFormula> getFormulas(){
		return formulas;
	}
	
	/**
	 * Gives you a Set of all variables which the user can edit.
	 * The {@link Variable}-Object knows the id of the VerificationFormula
	 * where the {@link VarAssertion} is placed, the name of the variable and
	 * the assertion text behind this variable.
	 * Note: That the set is sorted by the name of the variable.
	 * 
	 * @return a {@link SortedSet} of variables
	 */
	public SortedSet<Variable> getVariables(){
		return variables;
	}
	
	/**
	 * Gives you the whole source code to get verified as String.
	 * If the Model was not initialized yet you will get an
	 * empty String
	 * 
	 * @return the source code if the Model was initiated
	 */
	public String getSource(){
		return (root == null ? "" : root.getSource());		
	}

	/**
	 * Applies a Rule to the {@link VerificationFormula} specified by the id parameter.
	 * Returns true if the Rule could be applied to this VerificationFormula.
	 * Expect that the Rules strong predecessor and weak successor could be
	 * applied everywhere in the verification tree.    
	 * 
	 * @param id
	 *     the Index of the VerificationFormula
	 * @param rule
	 *     the {@link Rule} which should be applied
	 * @return
	 *     only <code>true</code> if the {@link Rule} has been applied
	 *     successful
	 * @throws IllegalArgumentException
	 *     if the given <code>id</code> does not belong to a
	 *     {@link VerificationFormula} in the Model	 *     
	 *  
	 */
	public boolean apply(int id,Rule rule) throws IllegalArgumentException
	{
		boolean result=false;
		VerificationFormula vf=getVerificationFormula(id);
		if (vf==null){
			throw new IllegalArgumentException(Messages.getString("hoare",
					                                 "out.noValidNode"));
		}
	
		if (rule.equals(Rule.STRONGPRE)) {
			varCount++;
			result=vf.createStrongPre("P"+varCount);
			if (! result) varCount--;
		} else {
			if (rule.equals(Rule.WEAKPOST)) {
				varCount++;
				result=vf.createWeakPost("P"+varCount);
				if (! result) varCount--;
			} else {
				if (vf.canApply(rule)) {
					result=vf.applyNext(vf);
				}
			}
		}
		if (result) updateFormulas();
		return result;
	}
	
	/**
	 * Removes all children of this {@link VerificationFormula}.
	 * This operation do not really delete the selected
	 * part of the verification tree, but sets all following
	 * VerificationFormulas as not visible.<br>
	 * Note that strong predecessors and weak successors will be
	 * really removed from the verification tree. 
	 * 
	 * @param id
	 *    of the selected {@link VerificationFormula}
	 * @return 
	 *    <code>true</code> if the {@link VerificationFormula} exists
	 */
	public boolean remove(int id) {
		VerificationFormula vf=getVerificationFormula(id);
		if (vf==null) return false;
		vf.deleteChildren();
		updateFormulas();
		//Shows View that the whole tree has changed
		getRoot().setChanged(true);
		return true;
	}
	
	
	/**
	 * This method applies a given verification tree to the model.
	 * The verification tree will be build up by the given root.
	 * 
	 * @param root
	 *        the root of the verification tree
	*/
	public void init(VerificationFormula root){
		setRoot(root);
		//Shows View that the whole tree has changed
		root.setChanged(true);
		VerificationFormula.setNextId(root);
	}
	
	/**
	 * Builds a VF-data structure of an AST-Tree.
	 * Note: The given source code have to be a single line string.
	 * In other cases there will be Errors during the String selection!
	 * 
	 * @param tree
	 *            the AST-Tree
	 * @param source
	 *            the associated sourcecode as a string
	 * @return the root VerificationFormula of the VF-data strucure
	 */
	public void init(AST.StatementSequence tree,String source) {
		varCount=2;
		piCount=0;
		root=initTree(tree,null, source);
		root.replacePreAssertion(new VarAssertion("P1"));
		root.replacePostAssertion(new VarAssertion("P2"));
		root.setApplied();
		root.setChanged(true);
		updateFormulas();
	}
	
	/**
	 * Gives you the C0 conform source code of the given BoolExpression. 
	 * 
	 * @param bool
	 *        a BoolExpression in an AST-tree 
	 * @param source
	 *        the source code which contains the BoolExpression
	 * @return the BoolExpression as C0 conform source code
	 */
	private String getBoolExpression(AST.BoolExpression bool, String source){
		return (bool.startColumn<source.length() && bool.endColumn+1<source.length()) ? 
				new String(source.substring(bool.startColumn, bool.endColumn+1)) : null;
 }
	
	/**
	 * This method creates a part of the verification tree by
	 * recursive calling it self.    
	 * Note: This is one of two overloaded methods to handle
	 * either StatementSequences or Statements in a dynamic way. 
	 * 
	 * @param tree
	 *    a StatementSequence in an AST-tree     
	 * @param parent
	 *    of the new created VerificationFormulas 
	 * @return
	 *    the root {@link VerificationFormula} created by this method
	 */
	private VerificationFormula initTree(AST.StatementSequence tree,
			                             VerificationFormula parent, String source) {
		VerificationFormula result = null;
		
		if (tree instanceof AST.PairStmSeq) {
			
			if (((AST.PairStmSeq) tree).stmSeq instanceof AST.EmptyStmSeq) {
				result=initTree(((AST.PairStmSeq) tree).stm,parent,source);
			}	else {
				result=new StatSeqFormula(parent,source,tree.startColumn,tree.endColumn);
	   
				varCount++;
				AbstractAssertion variable=new VarAssertion("P"+varCount);
				
				List<VerificationFormula> newChildren=new ArrayList<VerificationFormula>(2);
				VerificationFormula child=initTree(((AST.PairStmSeq) tree).stm,result,source);
				if (child!=null){
					child.replacePostAssertion(variable);
					newChildren.add(child);
				}
				child=initTree(((AST.PairStmSeq) tree).stmSeq,result,source);
				if (child!=null){
					child.replacePreAssertion(variable);
					newChildren.add(child);
				}
				result.setChildren(newChildren);
			}
			
		}
		return result;
	}
	
	/**
	 * This method creates a part of the verification tree by
	 * recursive calling it self.    
	 * Note: This is one of two overloaded methods to handle
	 * either StatementSequences or Statements in a dynamic way. 
	 * 
	 * @param tree
	 *    a Statement in an AST-tree     
	 * @param parent
	 *    of the new created VerificationFormulas 
	 * @return
	 *    the root {@link VerificationFormula} created by this method
	 */
	private VerificationFormula initTree(AST.Statement tree,
			                             VerificationFormula parent, String source) {
		VerificationFormula result=null;
		VerificationFormula child=null;

		if (tree instanceof AST.AssignStm){
			result=new AssignFormula(parent,source,tree.startColumn,tree.endColumn);
		} else {
			if (tree instanceof AST.PureIfStm) {
				String boolExp=getBoolExpression(((AST.PureIfStm) tree).boolExp,source);
				piCount++;
				ConcreteAssertion pi=new ConcreteAssertion(boolExp,piCount);
				result=new IfFormula(parent,source,tree.startColumn,tree.endColumn,pi);
				
				List<VerificationFormula> newChildren=new ArrayList<VerificationFormula>(2);
				child=initTree(((AST.PureIfStm) tree).stm,result,source);
				if (child!=null)	{
					newChildren.add(child);
				}
				child=new ImplicationFormula(result,tree.startColumn,tree.endColumn,null,null);
				newChildren.add(child);
				result.setChildren(newChildren);
			} else {
				if (tree instanceof AST.ElseIfStm) {
					String boolExp=getBoolExpression(((AST.ElseIfStm) tree).boolExp,source);
					piCount++;
					ConcreteAssertion pi=new ConcreteAssertion(boolExp,piCount);
					result=new IfElseFormula(parent,source,tree.startColumn,tree.endColumn,pi);
		 	 
					List<VerificationFormula> newChildren=new ArrayList<VerificationFormula>(2);
					child=initTree(	((AST.ElseIfStm) tree).stm1,result,source);
					if (child!=null)	{
						newChildren.add(child);
					}
     
					child=initTree( ((AST.ElseIfStm) tree).stm2,result,source);
					if (child!=null)	{
						newChildren.add(child);
					}
					result.setChildren(newChildren);
				} else {
					if (tree instanceof AST.WhileStm)	{
						String boolExp = getBoolExpression(((AST.WhileStm) tree).boolExp,source);
						piCount++;
						ConcreteAssertion pi=new ConcreteAssertion(boolExp,piCount);			   
						result=new IterationFormula(parent,source,tree.startColumn,tree.endColumn,pi);
	   
						List<VerificationFormula> newChildren=new ArrayList<VerificationFormula>(2);
						child=initTree( ((AST.WhileStm) tree).stm,result,source) ;
						if (child!=null)	{
							newChildren.add(child);
						}
						result.setChildren(newChildren);
					} else {
						if (tree instanceof AST.CompStm) {
							result=new CompoundFormula(parent,source,tree.startColumn,tree.endColumn);
							
							List<VerificationFormula> newChildren=new ArrayList<VerificationFormula>(2);
							child=initTree(((AST.CompStm) tree).stmSeq,result,source); 
							// Declarations ignored!
							if (child!=null)	{
								newChildren.add(child);
							}
							result.setChildren(newChildren);			   
						}
					}					
				}			
			}						
		}
		return result;
	}
	
	/**
	 * This methods notifies all observers which have been registered.
	 * Note: This method has been overridden to provide the
	 * notification of the observers without previously performing the 
	 * <code>setChanged</code>-routine!
	 * 
	 */
	@Override
	public void notifyObservers(){
		setChanged();
		super.notifyObservers();
		setAllUnchanged();		
	}
	
	/**
	 * This method removes the changed flags from all 
	 * verification formulas in the verification tree.
	 */
	public void setAllUnchanged(){
		for (VerificationFormula vf: formulas.values()){
			vf.setChanged(false);
		}
	}
	
	/**
	 * This method sets all visible {@link VerificationFormula}s
	 * as changed.<br>
	 * Note: Use this method after big changes in the verification tree! 
	 */
	public void setAllChanged(){
		for (VerificationFormula vf: formulas.values()){
			if (vf.ruleApplied){
				vf.setChanged(true);
			}
		}
	}
	
	/**
	 * Gives you a list of the <b>visible</b> {@link VerificationFormula}s
	 * that contains the {@link Variable} with the given name
	 * as pre or post assertion.<br>
	 * Formulas are visible if {@link VerificationFormula#isRuleApplied()}
	 * returns <code>true</code>
	 * Note: That this method uses {@link String#indexOf(int)} to
	 * collect the VerificationFormulas and causes a leak of performance.
	 * 
	 * @param name
	 *        of the Variable to look for 
	 * @return a list of {@link VerificationFormula}s which contain
	 *         the given Variable
	 * @throws NullPointerException
	 *         if the parameter <code>name</code> is <code>null</code>
	 */
	public List<VerificationFormula> getFormulasByVariable(String name)
	  throws NullPointerException{

		if (name==null)	{
			throw new NullPointerException("name must not be null");
		}
		List<VerificationFormula> result=new ArrayList<VerificationFormula>();
		for (VerificationFormula vf : formulas.values())	{
			if ((vf.getPreAssertion(TextStyle.SHORT).matches(".*"+name+"\\b.*")) 
				   || (vf.getPostAssertion(TextStyle.SHORT).matches(".*"+name+"\\b.*"))){
				result.add(vf);
			}
		}		
		return result;
	}
	
	/**
	 * Clear the verification represented by this Model.<br>
	 * Note: You do not need to call <code>clear</code> before an
	 * <code>init</code>-Operation. 
	 */
	public void clear(){
		root=null;
		formulas.clear();
		variables.clear();
	}
		
}
