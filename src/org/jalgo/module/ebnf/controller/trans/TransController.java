/**
 * 
 */

package org.jalgo.module.ebnf.controller.trans;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.jalgo.main.gui.components.JToolbarButton;
import org.jalgo.module.ebnf.MainController;
import org.jalgo.module.ebnf.ModuleConnector;
import org.jalgo.module.ebnf.gui.trans.GUIController;
import org.jalgo.module.ebnf.model.ebnf.Definition;
import org.jalgo.module.ebnf.model.ebnf.DefinitionFormatException;
import org.jalgo.module.ebnf.model.ebnf.Term;
import org.jalgo.module.ebnf.model.syndia.SynDiaElem;
import org.jalgo.module.ebnf.model.syndia.SynDiaSystem;
import org.jalgo.module.ebnf.model.trans.TransMap;
import org.jalgo.module.ebnf.util.ActionStack;

/**
 * @author Andre
 */
public class TransController {

	/**
	 * Represents the Pane given by the TransController to draw on
	 */
	private JPanel contentPane;

	/**
	 * Represents the MainController of the EBNF module
	 */
	private MainController maincontroller;

	/**
	 * Represents the ModuleConnector of the EBNF module
	 */
	private ModuleConnector connector;

	/**
	 * Represents the GuiController of the trans-algorithm
	 */
	private GUIController guicontroller;

	/**
	 * Represents the EBNF definition to transform
	 */
	private Definition ebnfDef;

	/**
	 * Represents the later created SynDiaSystem
	 */
	private SynDiaSystem synDiaSystem;

	/**
	 * Represents the Map to see whether an element is transformed (null) or not
	 */
	private TransMap transMap;

	/**
	 * Represents a dataholder for the performed steps making it possible to
	 * undo steps
	 */
	private ActionStack actionStack;
	
	private AbstractAction startAction;

	/**
	 * Initializes the TransController
	 * 
	 * @param maincontroller the MainController of the module
	 * @param connector the ModuleConnector of the module
	 * @param contentPane the Panel to draw on
	 * @param ebnfdef an Ebnf definition
	 */
	public TransController(MainController maincontroller,
			ModuleConnector connector, JPanel contentPane, Definition ebnfdef) {

		this.maincontroller = maincontroller;
		this.connector = connector;
		this.contentPane = contentPane;

		this.actionStack = new ActionStack();

		try {
			this.ebnfDef =  ebnfdef.getStrict();
		} catch (DefinitionFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
				
		this.transMap = new TransMap();
		
		this.synDiaSystem = TransAlgorithm.transformEbnf(ebnfDef, transMap);
		
		guicontroller = new GUIController(this, connector);
		
		transMap.addObserver(guicontroller.getDrawPanel());
		transMap.addObserver(guicontroller.getControlPanel());
	}
	
	
	/**
	 * Creates a <code>JButton</code> object without border and text, which
	 * can be used in <code>JToolBar</code>s
	 * 
	 * @return a <code>JButton</code> instance with the given
	 *         <code>Action</code>
	 */
	public JButton createToolbarButton(Action a) {
		JToolbarButton button = new JToolbarButton((Icon) a
				.getValue(Action.SMALL_ICON), null, null);
		button.setAction(a);
		button.setText("");
		return button;
	}
	
	
	/**
	 * A method to set another EBNF definition to transform. In standard case
	 * never used, but perhaps.. later... or so... or not
	 * 
	 * @param ebnfdef
	 */
	public void setEbnfDefinition(Definition ebnfdef) {

		try {
			this.ebnfDef = ebnfdef.getStrict();
		} catch (DefinitionFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.synDiaSystem = new SynDiaSystem(ebnfdef.getStartVariable()
				.getName());

		this.synDiaSystem = TransAlgorithm.transformEbnf(ebnfDef, transMap);

	}
	
	public void switchToAlgorithm() {
		
		guicontroller.switchToAlgorithm();		
		
	}
	
	public void switchToSynDiaDisplay() {
		
		maincontroller.setSynDiaDisplayMode(synDiaSystem);
		
	}
	
	public void setEbnfInputMode() {
		
		maincontroller.setEbnfInputMode(true);
		
	}
	
	
	/**
	 * A method just to get the contentPane
	 * 
	 * @return The contentPane of the module
	 */
	public JPanel getContentPane() {

		return this.contentPane;

	}

	/**
	 * A method just to get the syntax diagram system
	 * 
	 * @return A SynDiaSystem
	 */
	public SynDiaSystem getSynDiaSystem() {

		return this.synDiaSystem;

	}

	/**
	 * A method just to get the transMap
	 * 
	 * @return A Map containing all associations from a syntax diagram element
	 *         to the referred term
	 */
	public TransMap getTransMap() {

		return this.transMap;

	}

	
	
	/** Checks if there's at least one more step to perform
	 * @return True, if it is possible to perform at least one step
	 */
	public boolean hasNextStep() {
		
		for (SynDiaElem sde : transMap.keySet())
			if (transMap.get(sde) != null) return true; 
	
		return false;
	}
	
	/** This method searches for the next transformable term and returns it
	 * @return A Term, which is the next to be transformed
	 */
	public Term getTermFromNextStep() {
		
		for (SynDiaElem sde : transMap.keySet())
			
			if (transMap.get(sde) != null) {
				
				return transMap.get(this.getLastPossibleParent(sde)); 
			}
	
		return null;
	}
	
	/** This method searches for the next transformable term and returns it
	 * @return A Term, which is the next to be transformed
	 */
	public SynDiaElem getSdeFromNextStep() {
		
		for (SynDiaElem sde : transMap.keySet())
			
			if (transMap.get(sde) != null) {
				
				return this.getLastPossibleParent(sde); 
			}
	
		return null;
	}
	
	/**
	 * Searches for the next possible transformation step and performs it. If a
	 * redo is possible on the ActionStack, this step will be performed.
	 *
	 * @return True, if it was possible to perform the found step
	 */
	public boolean performNextStep() {
		
		for (SynDiaElem sde : transMap.keySet()) {

				if (transMap.get(sde) != null) {

					SynDiaElem newsde = getLastPossibleParent(sde);

					TransStep ts = new TransStep(newsde, transMap);
					try {
						
						this.actionStack.perform(ts);
						transMap.notifyObservers();
						
					}
					catch (Exception e) {
						
						System.out.println("es konnte kein redo durchgeführt werden");
					}
					return true;

				}
			//}
		}
		return false;
	}
	
	/**
	 * @return true, if all steps are done
	 * 
	 */
	public boolean performAllSteps() {
		
		while (actionStack.isRedoPossible()) {
			
			try {
				actionStack.redo();
			}
			catch (Exception e) {
				
				System.out.println("es konnte kein redo durchgeführt werden");
			}
		} 
		
		while (this.hasNextStep()) {
			for (SynDiaElem sde : transMap.keySet()) {
	
					if (transMap.get(sde) != null) {
	
						SynDiaElem newsde = getLastPossibleParent(sde);
	
						TransStep ts = new TransStep(newsde, transMap);
						try {
							this.actionStack.perform(ts);
							
						}
						catch (Exception e) {
							
							System.out.println("es konnte kein redo durchgeführt werden");
						}
						break;
	
					}
			}
		}
		transMap.notifyObservers();
		return true;
			
	}
	
	
	/**
	 * This function looks for the last parent of a syntax
	 * diagram element, which is not transformed
	 * 
	 * @param sde the child syntax diagram element
	 * @return the last possible not transformed parent
	 */
	public SynDiaElem getLastPossibleParent(SynDiaElem sde) {

		SynDiaElem parent = sde.getParent();
		if (transMap.get(parent) != null)
			return getLastPossibleParent(parent);

		else
			return sde;

	}

	/** Transforms the given syntax diagram element
	 * @param sde A syntax diagram element, which is to be transformed
	 * @return true, if the tranformation was possible
	 */
	public boolean performChosenStep(SynDiaElem sde) {

		TransStep ts = new TransStep(sde, transMap);
		try {
			this.actionStack.perform(ts);
			transMap.notifyObservers();
		}
		catch (Exception e) {
			
			System.out.println("es konnte kein perform durchgeführt werden");
		}
		return true;
	}
	
	public boolean redo() {
		
		if (actionStack.isRedoPossible()) {
		
			try {
				actionStack.redo();
				transMap.notifyObservers();
			}
			catch (Exception e) {
				
				System.out.println("es konnte kein redo durchgeführt werden");
			}
				
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * Undo the last TransStep which is to find on the ActionStack
	 * @return true
	 */
	public boolean undoStep() {

		try {
			this.actionStack.undo();
			transMap.notifyObservers();
		}
		catch (Exception e) {
			
			System.out.println("es konnte kein redo durchgeführt werden");
		}
		return true;

	}
	
	/**
	 * Undoes all TransSteps which are to find on the ActionStack
	 * @return true
	 */
	public boolean undoAllSteps() {
		
		try {
			while (actionStack.isUndoPossible())
				this.actionStack.undo();
			transMap.notifyObservers();
		}
		catch (Exception e) {
			
			System.out.println("es konnte kein redo durchgeführt werden");
		} 
		
		return true;

	}
	

}
