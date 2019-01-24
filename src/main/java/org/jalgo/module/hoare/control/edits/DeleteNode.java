package org.jalgo.module.hoare.control.edits;

import java.security.InvalidParameterException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.constants.Status;
import org.jalgo.module.hoare.control.UndoableEdit;
import org.jalgo.module.hoare.control.Controller;
import org.jalgo.module.hoare.model.Model;
import org.jalgo.module.hoare.model.VerificationFormula;

/**
 * an UndoableEdit that can delete a subtree of a node
 *
 * @author Johannes
 */
public class DeleteNode implements UndoableEdit {

	private Model model;
	private Controller controller;
	private int vfId;
	private byte[] buffer;
	private boolean undo = false;
	private boolean redo = false;


	/**
	 * creates a new instance of DeleteNode
	 *
	 * @param model
	 *		the model, the VerificationFormula belongs to
	 * @param vfId
	 *		the Id of the VerificationFormula
	 */
	public DeleteNode(Model model, int vfId, Controller controller){
		this.model = model;
		this.vfId = vfId;
		this.controller = controller;
	}

	/**
	 * delete the node
	 */
	public void apply() throws InvalidParameterException{
		if( model == null )
			throw new InvalidParameterException(Messages.getString("hoare","out.DeleteNodeFailed"));
		if( model.getVerificationFormula(vfId) == null )
			throw new InvalidParameterException(Messages.getString("hoare","out.noValidNode"));
		if( controller == null )
			throw new InvalidParameterException(Messages.getString("hoare","out.DeleteNodeFailed"));

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
			throw new InvalidParameterException(Messages.getString("hoare","out.DeleteNodeFailed"));
		}

		// make changes
		model.remove(vfId);
		
		model.getVerificationFormula(vfId).setStatus(Status.RESULTWRONG);
		controller.checkVfParentStatus(vfId);

		model.notifyObservers();
		
		undo = true;
		redo = false;
	}

	/**
	 * delete the node again
	 */
	public void redo(){
		if( !redo )
			throw new CannotRedoException();
		
		model.remove(vfId);
		
		model.getVerificationFormula(vfId).setStatus(Status.RESULTWRONG);
		controller.checkVfParentStatus(vfId);
		
		model.notifyObservers();
		
		undo = true;
		redo = false;
	}

	/**
	 * undelete the node.
	 * Warning: If the implementation of the Map of VerificationFormulas in Model changes, this method has to be changed, too.
	 */
	public void undo(){
		if( !undo )
			throw new CannotUndoException();
		
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
			ObjectInputStream input = new ObjectInputStream(bais);

			int rootId = input.readInt();
			Object tmp = input.readObject();
			if( !(tmp instanceof HashMap) )
				throw new IOException();
			Map<Integer, VerificationFormula> formulas = (HashMap<Integer, VerificationFormula>) tmp;

			input.close();
			bais.close();

			model.init( formulas.get(rootId) );
			model.setAllChanged();
			model.notifyObservers();
		} catch (IOException e){
			e.printStackTrace();
			throw new CannotUndoException();
		} catch (ClassNotFoundException e){
			throw new CannotUndoException();
		}
		
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
