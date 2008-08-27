package org.jalgo.module.hoare.control.edits;

import java.security.InvalidParameterException;
import java.util.SortedSet;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.constants.Status;
import org.jalgo.module.hoare.constants.TextStyle;
import org.jalgo.module.hoare.constants.Rule;
import org.jalgo.module.hoare.control.Controller;
import org.jalgo.module.hoare.control.UndoableEdit;
import org.jalgo.module.hoare.control.Evaluation;
import org.jalgo.module.hoare.model.Model;
import org.jalgo.module.hoare.model.VerificationFormula;
import org.jalgo.module.hoare.model.Variable;

import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;

import org.nfunk.jep.ParseException;

/**
 * and UndoableEdit that can edit pre- and postAssertion of an VerificationFormula
 *
 * @author Johannes
 */
public class AssertionEdit implements UndoableEdit {

	private Model model;
	private Controller controller;
	private int vfId;
	private String preAssertion;
	private String postAssertion;
	private Evaluation evaluator;
	private boolean undo = false;
	private boolean redo = false;


	/**
	 * creates a new instance of AssertionEdit
	 *
	 * @param vf
	 * 		the VerificationFormula the edit will be applied to
	 * @param preAssertion
	 *		the maybe changed preAssertion of the VerificationFormula
	 * @param postAssertion
	 *		the maybe changed postAssertion of the VerificationFormula
	 * @param evaluator
	 *		the evaluator used by the Controller
	 * @param controller
	 * 		the Controler for checkVfParentStatus()
	 */
	public AssertionEdit(Model model, int vfId, String preAssertion, String postAssertion, Evaluation evaluator, Controller controller){
		this.model = model;
		this.vfId = vfId;
		this.preAssertion = preAssertion;
		this.postAssertion= postAssertion;
		this.evaluator = evaluator;
		this.controller = controller;
	}

	/**
	 * applys the edit to the specified VerificationFormula
	 */
	public void apply() throws InvalidParameterException {
		if( model == null )
			throw new InvalidParameterException(Messages.getString("hoare","out.EditAssertionFailed"));
		if( preAssertion == null || postAssertion == null )
			throw new InvalidParameterException(Messages.getString("hoare","out.EditAssertionFailed"));
		if( model.getVerificationFormula(vfId) == null )
			throw new InvalidParameterException(Messages.getString("hoare","out.noValidNode"));
		if( evaluator == null )
			throw new InvalidParameterException(Messages.getString("hoare","out.EditAssertionFailed"));
		if( controller == null )
			throw new InvalidParameterException(Messages.getString("hoare","out.EditAssertionFailed"));
		
		
		String tempPre, tempPost;
		VerificationFormula vf = model.getVerificationFormula(vfId);

		tempPre = vf.getPreAssertion(TextStyle.SOURCE);
		tempPost = vf.getPostAssertion(TextStyle.SOURCE);

		if( !tempPre.equals(preAssertion) && vf.canEditPreAssertion() ){
			// edit preAssertion (changes Variable)
			try {
				vf.editPreAssertion(preAssertion);
			} catch( Exception e ){
				throw new InvalidParameterException(e.getMessage());
			}
			model.updateFormulas(); //Performs the Update of the Variable-List

			// search for Variable
			SortedSet<Variable> vars = model.getVariables();
			Variable var = null;
			for( Variable tmpVar : vars){
				if( tmpVar.isPreAssertion() && tmpVar.getName().equals(vf.getPreAssertion(TextStyle.SHORT)) ){
					var = tmpVar;
					break;
				}
			}
			if( var == null ){
				// change it back
				try {
					vf.editPreAssertion(tempPre);
				} catch( Exception e ){
					throw new InvalidParameterException(e.getMessage());
				}
				model.updateFormulas();  //Performs the Update of the Variable-List
				throw new InvalidParameterException(Messages.getString("hoare","out.EditAssertionFailed"));
			}
			
			// evaluate all VFs containing this Variable and update the state of the tree
			for( VerificationFormula tmpVF : model.getFormulasByVariable(var.getName()) ){
				if( tmpVF.isRuleApplied() ){ // isVisible
					tmpVF.setChanged(true);
					if( tmpVF.verifiable() ){
						tmpVF.setStatus(Status.WAITING);
						evaluator.evaluate(tmpVF);
					}
					else {
						if( tmpVF.hasFilledPreAssertion() && tmpVF.hasFilledPostAssertion() && tmpVF.getAppliedRule() != null && tmpVF.isCorrect() ){
							if( tmpVF.getAppliedRule() == Rule.ASSIGN ){
								tmpVF.setStatus(Status.RESULTWRONG);
							}
							
							if( tmpVF.hasChildren() && tmpVF.getChildren().get(0).isRuleApplied() )
								controller.checkVfParentStatus(tmpVF.getChildren().get(0).getId());
							else if(!tmpVF.hasChildren())
								controller.checkVfParentStatus(tmpVF.getId());
						}
						else {
							if( tmpVF.isImplication() ){
								tmpVF.setStatus(Status.RESULTWRONG);
								controller.checkVfParentStatus(tmpVF.getId());
							}
							if( tmpVF.getAppliedRule() == Rule.ITERATION ){
								tmpVF.setStatus(Status.RESULTWRONG);
								controller.checkVfParentStatus(tmpVF.getId());
								if( !tmpVF.isCorrect() )
									controller.setStatusText(Messages.getString("hoare", "out.IterationChangedToWrong"));
							}
						}
					}
				}
			}
			
			preAssertion = tempPre;
		}
		if( !tempPost.equals(postAssertion) && vf.canEditPostAssertion() ){
			// edit postAssertion (changes Variable)
			try {
				vf.editPostAssertion(postAssertion);
			} catch( Exception e ){
				throw new InvalidParameterException(e.getMessage());
			}
			model.updateFormulas();  //Performs the Update of the Variable-List
			
			// search for Variable
			SortedSet<Variable> vars = model.getVariables();
			Variable var = null;
			for( Variable tmpVar : vars){
				if( !(tmpVar.isPreAssertion()) && tmpVar.getName().equals(vf.getPostAssertion(TextStyle.SHORT)) ){
					var = tmpVar;
					break;
				}
			}
			if( var == null ){
				// change it back
				try {
					vf.editPostAssertion(tempPost);
				} catch( Exception e ){
					throw new InvalidParameterException(e.getMessage());
				}
				model.updateFormulas();  		//Performs the Update of the Variable-List
				throw new InvalidParameterException(Messages.getString("hoare","out.EditAssertionFailed"));
			}
			
			// evaluate all VFs containing this Variable which are WEAKPOST
			for( VerificationFormula tmpVF : model.getFormulasByVariable(var.getName()) ){
				if( tmpVF.isRuleApplied() ){ // isVisible
					tmpVF.setChanged(true);
					if( tmpVF.verifiable() ){
						tmpVF.setStatus(Status.WAITING);
						evaluator.evaluate(tmpVF);
					}
					else {
						if( tmpVF.hasFilledPreAssertion() && tmpVF.hasFilledPostAssertion() && tmpVF.getAppliedRule() != null && tmpVF.isCorrect() ){
							if( tmpVF.getAppliedRule() == Rule.ASSIGN ){
								tmpVF.setStatus(Status.RESULTWRONG);
							}
							
							if( tmpVF.hasChildren() && tmpVF.getChildren().get(0).isRuleApplied() )
								controller.checkVfParentStatus(tmpVF.getChildren().get(0).getId());
							else if(!tmpVF.hasChildren())
								controller.checkVfParentStatus(tmpVF.getId());
						}
						else {
							if( tmpVF.isImplication() ){
								tmpVF.setStatus(Status.RESULTWRONG);
								controller.checkVfParentStatus(tmpVF.getId());
							}
							if( tmpVF.getAppliedRule() == Rule.ITERATION ){
								tmpVF.setStatus(Status.RESULTWRONG);
								controller.checkVfParentStatus(tmpVF.getId());
								if( !tmpVF.isCorrect() )
									controller.setStatusText(Messages.getString("hoare", "out.IterationChangedToWrong"));
							}
						}
					}
				}
			}
			
			postAssertion = tempPost;
		}
		
		model.notifyObservers();

		if( undo == redo ){
			undo = true;
			redo = false;
		}
	}

	/**
	 * redoes the edit
	 */
	public void redo(){
		if( !redo )
			throw new CannotRedoException();
		apply();
		undo = true;
		redo = false;
	}

	/**
	 * undoes the edit
	 */
	public void undo(){
		if( !undo )
			throw new CannotUndoException();
		apply();
		undo = false;
		redo = true;
	}

	/**
	 * not needed, just to complete the interface
	 */
	public boolean canRedo(){
		return redo;
	}

	/**
	 * not needed, just to complete the interface
	 */
	public boolean canUndo(){
		return undo;
	}

	/**
	 * not needed, just to complete the interface
	 */
	public String getPresentationName(){
		return null;
	}

	/**
	 * not needed, just to complete the interface
	 */
	public String getRedoPresentationName(){
		return null;
	}

	/**
	 * not needed, just to complete the interface
	 */
	public String getUndoPresentationName(){
		return null;
	}

	/**
	 * not needed, just to complete the interface
	 */
	public boolean addEdit(javax.swing.undo.UndoableEdit arg0){
		return false;
	}

	/**
	 * not needed, just to complete the interface
	 */
	public void die(){
	 // do nothing
	}

	/**
	 * not needed, just to complete the interface
	 */
	public boolean isSignificant(){
		return true;
	}

	/**
	 * not needed, just to complete the interface
	 */
	public boolean replaceEdit(javax.swing.undo.UndoableEdit arg0){
		return false;
	}

}
