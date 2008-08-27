package org.jalgo.module.hoare.control.edits;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.constants.Rule;
import org.jalgo.module.hoare.constants.Status;
import org.jalgo.module.hoare.control.UndoableEdit;
import org.jalgo.module.hoare.control.Controller;
import org.jalgo.module.hoare.model.Model;
import org.jalgo.module.hoare.model.VerificationFormula;

/**
 * a class implementing UndoableEdit which can apply any rule
 *
 * @author Johannes
 */
public class ApplyRule implements UndoableEdit {

	private Model model;
	private Controller controller;
	private int vfId;
	private Rule rule;
	private byte[] buffer;
	private boolean undo = false;
	private boolean redo = false;

	/**
	 * creates a new instance of ApplyRule
	 * 
	 * @param model
	 * 		the model, the VerificationFormula belongs to
	 * @param vfId
	 *		the ID of the VerificationFormula to apply the rule to
	 * @param rule
	 *		Rule that should be applied
	 */
	public ApplyRule(Model model, int vfId, Rule rule, Controller controller){
		this.vfId = vfId;
		this.model = model;
		this.rule = rule;
		this.controller = controller;
	}

	/**
	 * applys the rule to the model, if possible
	 */
	public void apply() throws InvalidParameterException {

		if( model == null )
			throw new InvalidParameterException( Messages.getString("hoare", "out.ruleApplyError") );

		if( model.getVerificationFormula(vfId)  == null )
			throw new InvalidParameterException(Messages.getString("hoare", "out.noValidNode"));

		if( controller == null )
			throw new InvalidParameterException(Messages.getString("hoare", "out.ruleApplyError"));

		try {
			if( !model.apply(vfId, rule) ){
				if( rule == Rule.ASSIGN ){
					model.getVerificationFormula(vfId).setStatus(Status.RESULTWRONG);
					controller.checkVfParentStatus(vfId);
					model.notifyObservers();
				}
				throw new InvalidParameterException( Messages.getString("hoare", "out.ruleApplyError") );
			}
		} catch( UnsupportedOperationException e ){
			throw new InvalidParameterException( e.getMessage() );
		}
		
		if( rule == Rule.ASSIGN ){
			model.getVerificationFormula(vfId).setStatus(Status.RESULTOK);
			controller.checkVfParentStatus(vfId);
		}
		else {
			controller.checkVfParentStatus(model.getVerificationFormula(vfId).getChildren().get(0).getId());
		}

		model.notifyObservers();
		undo = true;
		redo = false;
	}

	/**
	 * applys the rule to the model
	 */
	public void redo(){
		if( !redo )
			throw new CannotRedoException();
		
		if( rule == Rule.STRONGPRE || rule == Rule.WEAKPOST ){
			// get old tree
			try {
				ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
				ObjectInputStream input = new ObjectInputStream(bais);

				int rootId = input.readInt();
				
				Object tmp = input.readObject();
				if( !(tmp instanceof HashMap) )
					throw new IOException();
				Map<Integer,VerificationFormula> formulas = (HashMap<Integer, VerificationFormula>) tmp;

				input.close();
				bais.close();

				model.init( formulas.get(rootId));
				model.setAllChanged();
				model.notifyObservers();
			} catch (IOException e){
				//e.printStackTrace();
				throw new CannotRedoException();
			} catch (ClassNotFoundException e){
				//e.printStackTrace();
				throw new CannotRedoException();
			}
			undo = true;
			redo = false;
		}
		else {
			apply();
		}
	}

	/**
	 * undoes the application of the rule
	 */
	public void undo(){
		if( !undo )
			throw new CannotUndoException();
		
		if( rule == Rule.STRONGPRE || rule == Rule.WEAKPOST ){
			// save old tree
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream output = new ObjectOutputStream(baos);

				output.writeInt(model.getRoot().getId());
				output.writeObject(model.getFormulas());
				output.close();
				buffer = baos.toByteArray();
				baos.close();
			} catch( IOException e ){
				//e.printStackTrace();
				throw new CannotUndoException();
			}
		}
		
		model.remove(vfId);
		model.getVerificationFormula(vfId).setStatus(Status.RESULTWRONG);
		controller.checkVfParentStatus(vfId);

		model.notifyObservers();
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
