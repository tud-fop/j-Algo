package org.jalgo.module.hoare.control;

import javax.swing.SwingUtilities;
import javax.swing.undo.UndoManager;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;
import java.security.InvalidParameterException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;

import o3b.antlr.runtime.RecognitionException;
import org.jalgo.main.util.Messages;
import org.jalgo.main.AbstractModuleConnector.SaveStatus;
import org.jalgo.module.hoare.ModuleConnector;
import org.jalgo.module.hoare.constants.Rule;
import org.jalgo.module.hoare.constants.Event;
import org.jalgo.module.hoare.constants.Status;
import org.jalgo.module.hoare.constants.TextStyle;
import org.jalgo.module.hoare.model.Model;
import org.jalgo.module.hoare.model.VerificationFormula;
import org.jalgo.module.hoare.view.View;
import org.jalgo.module.hoare.control.edits.ApplyRule;
import org.jalgo.module.hoare.control.edits.AssertionEdit;
import org.jalgo.module.hoare.control.edits.DeleteNode;

import c00.AST;
import c00.AST.ASTTree;

/**
 * This is the main controller of the Program, which manages the communication between
 * View and Model.
 *
 * @author Johannes
 */
public class Controller {

	private Model model;
	private View view;
	private ModuleConnector mc;
	private UndoManager undoManager;
	private Evaluation evaluator;
	private int lastSave;

	/**
	 * creates a new instance of Controller.
	 */
	public Controller(View view, Model model, ModuleConnector mc){
		this.view = view;
		this.model = model;
		this.mc = mc;
		undoManager = new UndoManager();
		evaluator = new JepEvaluator(50, 15, this);
		init();
	}

	/**
	 * initiates the Model and the UndoManager
	 */
	private void init(){
		lastSave = 0;
		undoManager.discardAllEdits();
		setStatusText("");
		//view.installWelcomeScreen();
		view.grayUndo(true);
		view.grayRedo(true);
	}

	/**
	 * for loading a project.
	 * should be called by ModuleConnector
	 * Warning: If the implementation of the Map of VerificationFormulas in Model changes, this method has to be changed, too.
	 * 
	 * @param input
	 *		stream to load from
	 */
	public void load(ObjectInputStream input) throws IOException, ClassNotFoundException{
		if( input == null )
			throw new IOException();
		
		int rootId = input.readInt();

		Object tmp = input.readObject();
		if( !(tmp instanceof HashMap) )
			throw new IOException();
		Map<Integer,VerificationFormula> formulas = (HashMap<Integer, VerificationFormula>) tmp;

		model.init( formulas.get(rootId) );
		view.installWorkScreen();

		model.setAllChanged();
		model.notifyObservers();
		
		if( mc != null )
			mc.setSaveStatus(SaveStatus.NO_CHANGES);
		lastSave = 0;
	}

	/**
	 * saving a project.
	 * should be called by ModuleConnector
	 *
	 * @param output
	 *		stream to save to
	 */
	public void save(ObjectOutputStream output) throws IOException {
		if( output == null )
			throw new IOException();
		
		output.writeInt(model.getRoot().getId());
		output.writeObject(model.getFormulas());
		
		if( mc != null )
			mc.setSaveStatus(SaveStatus.NO_CHANGES);
		lastSave=0;
	}

	/**
	 * internal function for everything to do at undo
	 */
	private void undo(){
		if( undoManager.canUndo() ){
			try {
				undoManager.undo();
				lastSave--;
				view.grayRedo(false);
			} catch(CannotUndoException e){
				setStatusText(Messages.getString("hoare","out.UndoError"));
			}

			if( undoManager.canUndo() ){
				view.grayUndo(false);
			}
			else {
				view.grayUndo(true);
			}
			if( mc != null ){
				if( lastSave != 0 )
					mc.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
				else
					mc.setSaveStatus(SaveStatus.NO_CHANGES);
			}
		}
		else {
			setStatusText(Messages.getString("hoare","out.UndoImpossible"));
		}
	}

	/**
	 * internal function for everything to do at redo
	 */
	private void redo(){
		if( undoManager.canRedo() ){
			try {
				undoManager.redo();
				lastSave++;
				view.grayUndo(false);
			} catch(CannotRedoException e){
				setStatusText(Messages.getString("hoare", "out.RedoError"));
			}
			if( mc != null ){
				if( lastSave != 0 )
					mc.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
				else
					mc.setSaveStatus(SaveStatus.NO_CHANGES);
			}

			if( undoManager.canRedo() )
				view.grayRedo(false);
			else
				view.grayRedo(true);
		}
		else {
			setStatusText(Messages.getString("hoare", "out.RedoImpossible"));
		}
	}

	/**
	 * applys a rule to the tree
	 *
	 * @param id
	 *		index of the node/VerificationFormula to edit
	 * @param type
	 *		Rule to apply
	 */
	private void applyRule(int id, Rule type) throws InvalidParameterException {
		UndoableEdit edit;
		boolean everythingsFine = true;

		switch(type){
			case ASSIGN:
			case IF:
			case IFELSE:
			case COMPOUND:
			case STATSEQ:
			case ITERATION:
			case STRONGPRE:
			case WEAKPOST:
				edit = new ApplyRule( model, id, type, this );
				break;
			default:
				throw new InvalidParameterException(Messages.getString("hoare", "control.unknownRule"));
		}

		try {
			edit.apply();
		} catch( InvalidParameterException e ){
			everythingsFine = false;
			setStatusText(e.getMessage());
		}

		if( everythingsFine ){
			undoManager.addEdit( edit );
			view.grayUndo(false);
			if( mc != null )
				mc.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
			lastSave++;
		}
	}

	/**
	 * edits Assertion, given from the View
	 * 
	 * @param id
	 *		index of the node/VerificationFormula to edit
	 * @param preAssertion
	 *		String to set as preAssertion
	 * @param postAssertion
	 *		String to set as postAssertion
	 */
	private void editAssertion(int id, String preAssertion, String postAssertion){
		UndoableEdit edit = new AssertionEdit( model, id, preAssertion, postAssertion, evaluator, this);
		boolean everythingsFine = true;

		try {
			edit.apply();
		} catch( InvalidParameterException e ){
			everythingsFine = false;
			setStatusText(e.getMessage());
		}

		if( everythingsFine ){
			undoManager.addEdit( edit );
			view.grayUndo(false);
			if( mc != null )
				mc.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
			lastSave++;
		}
	}

	/**
	 * deletes a node from the model
	 *
	 * @param id
	 *		index of the node/VerificationFormula to delete
	 */
	private void deleteNode(int id){
		UndoableEdit edit = new DeleteNode(model, id, this);
		boolean everythingsFine = true;

		try {
			edit.apply();
		} catch(InvalidParameterException e) {
			setStatusText(e.getMessage());
			everythingsFine = false;
		}

		if( everythingsFine ){
			undoManager.addEdit( edit );
			view.grayUndo(false);
			if( mc != null )
				mc.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
			lastSave++;
		}
	}

	/**
	 * gets the reports from the evaluator and takes according actions.
	 * sets result in model and sends errormessages to view
	 *
	 * @param id
	 *		index of the node/VerificationFormula that was evaluated
	 * @param result
	 *		result of the evaluation, errors and incorrect are false
	 * @param message
	 *		String to send to View on error or incorrect
	 */
	public void report(int id, boolean result, String message){
		if( model.getVerificationFormula(id) == null )
			return;
		
		while( model.getVerificationFormula(id).hasChanged() ){
			try {
				Thread.sleep(10);
			} catch( InterruptedException e ){
			}
		}

		// set result in model
		if( result ){
			model.getVerificationFormula(id).setStatus(Status.RESULTOK);
		}
		else {
			model.getVerificationFormula(id).setStatus(Status.RESULTWRONG);

			if( message != null && !message.equals("") )
				setStatusText(message);
		}

		checkVfParentStatus(id);
		
		SwingUtilities.invokeLater(new Runnable()	{
			public void run() {
				model.notifyObservers();
			}
		});
		//model.notifyObservers();
		if( mc != null )
			mc.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
	}

	/**
	 * might be called by View if neither id nor parameter is needed for an Event
	 *
	 * @see handleEvent(Event, Object, int)
	 */
	public void handleEvent(Event event) throws InvalidParameterException {
		handleEvent(event, null, -1);
	}

	/**
	 * might be called by View if one does not need an id
	 *
	 * @param event
	 *		Event to act on
	 * @param parameter
	 *		data needed for the specific Event, will be casted accordingly
	 * @see handleEvent(Event, Object, int)
	 */
	public void handleEvent(Event event, Object parameter) throws InvalidParameterException {
		handleEvent(event, parameter, -1);
	}

	/**
	 * might be called by View if one does not need a parameter
	 *
	 * @param event
	 *		Event to act on
	 * @param id
	 *		Id of the node/VerfificationFormula, the Event may work on
	 * @see handleEvent(Event, Object, int)
	 */
	public void handleEvent(Event event, int id) throws InvalidParameterException {
		handleEvent(event, null, id);
	}

	/**
	 * called by View for any action, that needs more than GUI-internals.
	 *
	 * @param event
	 *		Event to act on
	 * @param parameter
	 *		data accepted for the specific Event - will be casted accordingly
	 *		<ul>
	 *		<li> Rule for APPLYRULE
	 *		<li> String for EDITPREASSERTION, EDITPOSTASSERTION, PARSECODE
	 *		<li> File for PARSECODE
	 *		<li> not needed for any other
	 *		</ul>
	 * @param id
	 *		Id of the node/VerfificationFormula, the Event may work on -
	 *		may be -1 if not needed
	 */
	public void handleEvent(Event event, Object parameter, int id ) throws InvalidParameterException {
		if( event == null )
			throw new InvalidParameterException(Messages.getString("hoare", "control.handleEvent.noSuchEvent"));
		
		// view likes to have this ...
		view.bleechStatusText();

		switch(event){
			case UNDO: 
				undo();
				break;
			case REDO:
				redo();
				break;
			case APPLYRULE:
				if( parameter == null || ! (parameter instanceof Rule) ){
					throw new InvalidParameterException(Messages.getString("hoare", "control.handleEvent.ParameterError"));
				}
				if( id == -1 || model.getVerificationFormula(id) == null ){
					throw new InvalidParameterException(Messages.getString("hoare", "control.handleEvent.IdError"));
				}
				applyRule( id, (Rule)parameter );
				break;
			case EDITPREASSERTION:
				if( parameter == null || ! (parameter instanceof String) ){
					throw new InvalidParameterException(Messages.getString("hoare", "control.handleEvent.ParameterError"));
				}
				if( id == -1 || model.getVerificationFormula(id) == null ){
					throw new InvalidParameterException(Messages.getString("hoare", "control.handleEvent.IdError"));
				}
				editAssertion( id, (String)parameter, model.getVerificationFormula(id).getPostAssertion(TextStyle.SOURCE) );
				break;
			case EDITPOSTASSERTION:
				// 
				if( parameter == null || ! (parameter instanceof String) ){
					throw new InvalidParameterException(Messages.getString("hoare", "control.handleEvent.ParameterError"));
				}
				if( id == -1 || model.getVerificationFormula(id) == null ){
					throw new InvalidParameterException(Messages.getString("hoare", "control.handleEvent.IdError"));
				}

				editAssertion( id, model.getVerificationFormula(id).getPreAssertion(TextStyle.SOURCE), (String)parameter );
				break;
			case DELETENODE:
				if( id == -1 || model.getVerificationFormula(id) == null ){
					throw new InvalidParameterException(Messages.getString("hoare", "control.handleEvent.IdError"));
				}
				deleteNode( id );
				break;
			case PARSECODE:
				if( parameter == null || !(parameter instanceof String) && !(parameter instanceof File) ){
					throw new InvalidParameterException(Messages.getString("hoare", "control.handleEvent.ParameterError"));
				}
				if( parameter instanceof File ){
					String code = new String();
					BufferedReader reader;
					
					try {
						reader = new BufferedReader(new FileReader((File)parameter) );
						while( reader.ready() ){
							code += reader.readLine();
						}
					} catch( IOException e ){
						setStatusText(Messages.getString("hoare","control.handleEvent.FileReadError"));
						throw new InvalidParameterException(Messages.getString("hoare","control.handleEvent.FileReadError"));
					}
					// save source without whitespace
					parseCode( code );
				}
				else {
					parseCode( (String)parameter );
				}

				if( mc != null )
					mc.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
				lastSave++;
				
				//model.setAllChanged();
				model.notifyObservers();
				break;
			case REINIT:
				init();
				model.clear();
				model.notifyObservers();
				break;
			default:
				throw new InvalidParameterException(Messages.getString("hoare", "control.handleEvent.noSuchEvent"));
		}
	}

	/**
	 * will parse in the code an fill the model with it
	 *
	 * @param code
	 *		a String that contains the source-code
	 */
	private void parseCode(String code){
		init();

		// scanf und printf entfernen:
		code = Pattern.compile("(scanf|printf).*?;").matcher(code).replaceAll("");
		
		// jedes WhiteSpace Zeichen entfernen  
		code = code.replaceAll("\\s", "");
		//Problem bei if-else ohne {} bzw. if oder while
		code = code.replaceAll("else([^{])","else $1");
		code = code.replaceAll("(if|while)","$1 ");
		
		try {
			StringBuffer errorBuffer = new StringBuffer();
			ASTTree astTree = AST.parseString(code, "StatementSequence", errorBuffer);
			
			if (astTree != null) {
				model.init((AST.StatementSequence)astTree, code);
			}	else {
				setStatusText(Messages.getString("hoare","control.ParseCodeError"));
				// the exception tells the view the parsing failed
				throw new InvalidParameterException();
			}
		} catch (RecognitionException e) {
			setStatusText(e.getMessage());
			// the exception tells the view the parsing failed
			throw new InvalidParameterException();
		}
	}
	
	/**
	 * checks for a VerificationFormula if its parent must change its status because of its own status changes
	 * 
	 * @param vfId
	 * 		vfId that changed its status and whose parents have to be checked
	 */
	public void checkVfParentStatus(int vfId){
		if( model.getVerificationFormula(vfId) == null ){
			return;
		}
		
		if( model.getVerificationFormula(vfId).getParent() == null ){
			// wir sind am Wurzelknoten angelangt
			if( model.getVerificationFormula(vfId).getStatus() == Status.RESULTOK ){
				model.notifyObservers();
				view.showProveFinished();
			}
			return;
		}

		
		
		VerificationFormula parent = model.getVerificationFormula(vfId).getParent();
		boolean selfOk = true;
		Status state = Status.RESULTOK;
		if( !parent.hasFilledPreAssertion() || !parent.hasFilledPostAssertion() || parent.getAppliedRule() == null || !parent.isCorrect() )
			selfOk = false;
		if( parent.isImplication() && (parent.getStatus() == Status.RESULTWRONG || parent.getStatus() == Status.WAITING) )
				selfOk = false;
		
		if( !selfOk ){
			state = Status.RESULTWRONG;
		}
		else {
			for( VerificationFormula childVf : parent.getChildren() ){
				if( childVf.getStatus() == Status.UNCHANGED || childVf.getStatus() == Status.RESULTWRONG || childVf.getStatus() == Status.WAITING ){
					state = Status.UNCHANGED;
				}
			}

		}

		if( ! parent.getStatus().equals(state) ){
			parent.setStatus(state);
			checkVfParentStatus(parent.getId());
		}
		return;
	}
	
	/**
	 * shows a Message in View by setting the status text
	 * @param text
	 * 		the text to set
	 */
	public void setStatusText(String text){
		if( text == null )
			text = "";
		view.setStatusText(text);
	}
}
