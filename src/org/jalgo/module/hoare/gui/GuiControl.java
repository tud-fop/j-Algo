package org.jalgo.module.hoare.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JToolBar;
import javax.swing.JToolBar.Separator;

import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.ModuleConnector;
import org.jalgo.module.hoare.control.ProgramControl;
import org.jalgo.module.hoare.control.ProgramControl.Edit;
import org.jalgo.module.hoare.gui.actions.BeamerMode;
import org.jalgo.module.hoare.gui.actions.DelNode;
import org.jalgo.module.hoare.gui.actions.LoadCNullProg;
import org.jalgo.module.hoare.gui.actions.ReInit;
import org.jalgo.module.hoare.gui.actions.Redo;
import org.jalgo.module.hoare.gui.actions.ToggleDescription;
import org.jalgo.module.hoare.gui.actions.TreeEval;
import org.jalgo.module.hoare.gui.actions.Undo;
import org.jalgo.module.hoare.model.ModelControl;
import org.jalgo.module.hoare.model.VerificationFormula;

/**
 * The GUI-Controller
 * 
 * @author Peter, Markus
 * 
 */

public class GuiControl extends Observable implements Observer, GUIConstants {

	private int activeNode;

	private HashMap<Integer, Node> nodes;

	private boolean beamer;

	private WelcomeScreen startScreen;

	private WorkScreen workScreen;

	JComponent rootComponent;

	JToolBar toolbar;

	JMenu menu;

	ModelControl modelControl;

	ProgramControl programControl;

	private ReInit reInit;

	private LoadCNullProg loadCNullProg;

	private Undo undo;

	private Redo redo;

	private ToggleDescription toggleDescription;

	private TreeEval eval;

	private DelNode delNode;

	private BeamerMode beamerMode;

	ModuleConnector connector;

	public interface ErrorEmitter {
		public void emitError(String message);

		public void emitInfo(String message);
	}

	private ErrorEmitter errorEmitter;
	/**
	 *	constructs the Gui-Control Object  
	 * 
	 * @param c
	 * 			the <code>ModuleConnector</code>
	 */
	public GuiControl(ModuleConnector c) {
		connector = c;
		beamer = false;
		errorEmitter = new ErrorEmitter() {
			public void emitError(String message) {
				JAlgoGUIConnector.getInstance().showErrorMessage(message);
			}

			public void emitInfo(String message) {
				JAlgoGUIConnector.getInstance().showInfoMessage(message);
			};
		};

		activeNode = -1;
		nodes = new HashMap<Integer, Node>();

		rootComponent = JAlgoGUIConnector.getInstance().getModuleComponent(c);
		rootComponent.removeAll();
		rootComponent.setLayout(new BorderLayout());

		toolbar = JAlgoGUIConnector.getInstance().getModuleToolbar(c);
		menu = JAlgoGUIConnector.getInstance().getModuleMenu(c);

	}

	/**
	 * initializes the Welcome-Screen
	 * 
	 * @author Peter
	 * 
	 */
	public void installWelcomeScreen() {

		// später:
		if (startScreen == null) // TODO: oder im constr?
			startScreen = new WelcomeScreen(this);

		rootComponent.removeAll();
		rootComponent.add(startScreen);
		startScreen.updateUI();
		update(null, null);
	}

	/**
	 * initializes the Work-Screen
	 * 
	 * @author Peter
	 * 
	 */

	public void installWorkScreen() {
		if (workScreen == null)
			workScreen = new WorkScreen(this);

		rootComponent.removeAll();
		rootComponent.add(workScreen);
		workScreen.updateUI();
		update(null, null);
	}
	/**
	 * initializes the toolbar
	 * 
	 * @author Markus
	 * 
	 */
	public void installToolbar() {

		reInit = new ReInit(this);
		loadCNullProg = new LoadCNullProg(this);
		undo = new Undo(this);
		redo = new Redo(this);
		toggleDescription = new ToggleDescription(this);
		eval = new TreeEval(this);
		delNode = new DelNode(this);
		beamerMode = BeamerMode.getInstance(this);
		beamerMode.setState(false);

		reInit.putValue(AbstractAction.NAME, "");
		loadCNullProg.putValue(AbstractAction.NAME, "");
		undo.putValue(AbstractAction.NAME, "");
		redo.putValue(AbstractAction.NAME, "");
		toggleDescription.putValue(AbstractAction.NAME, "");
		eval.putValue(AbstractAction.NAME, "");
		delNode.putValue(AbstractAction.NAME, "");

		Separator sep = new Separator();
		sep.setEnabled(true);

		toolbar.add(new JToolButton(new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.reinit")), this, Messages.getString("hoare",
				"ttt.reinit"), reInit));
		toolbar.add(new JToolButton(new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.openC0Program")), this, Messages.getString(
				"hoare", "ttt.openC0Program"), loadCNullProg));
		toolbar.add(new JToolButton(new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.undo")), this, Messages.getString("hoare",
				"ttt.undo"), undo));
		toolbar.add(new JToolButton(new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.redo")), this, Messages.getString("hoare",
				"ttt.redo"), redo));
		//		
		toolbar.add(sep);
		//		
		toolbar.add(new JToolButton(new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.zoom")), this, Messages.getString("hoare",
				"ttt.toggleDesc"), toggleDescription));
		toolbar.add(new JToolButton(new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.delNode")), this, Messages.getString("hoare",
				"ttt.delNode"), delNode));
		//		
		toolbar.add(sep);
		//		
		toolbar.add(new JToolButton(new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.evalTree")), this, Messages.getString("hoare",
				"ttt.evalTree"), eval));

		toolbar.add(sep);

	}
	/**
	 * initializes the Menu
	 * 
	 * @author Markus
	 * 
	 */
	public void installMenu() {
		JMenu menu = JAlgoGUIConnector.getInstance().getModuleMenu(connector);

		menu.add(new ReInit(this));
		menu.add(new LoadCNullProg(this));
		menu.add(new Separator());
		menu.add(new Undo(this));
		menu.add(new Redo(this));
		menu.add(new Separator());
		menu.add(new TreeEval(this));
		menu.add(new DelNode(this));
		menu.add(new Separator());
		menu.add(beamerMode);
	};
	/**
	 * sets the active Node
	 * 
	 * @author Peter
	 * 
	 */
	public void setActiveNode(int id) {
		this.activeNode = id;
		this.setChanged();
	}
	/**
	 * resets the active Node to -1
	 * 
	 * @author Peter
	 * 
	 */
	public void resetActiveNode() {
		this.activeNode = -1;
		this.setChanged();
	}
	/**
	 * returns the active Node
	 * 
	 * @author Peter
	 * 
	 */
	public int getActiveNode() {
		return this.activeNode;
	}
	/**
	 * sets the Program Control
	 * 
	 * @author Peter
	 * 
	 */
	public void setProgramControl(ProgramControl programControl) {
		this.programControl = programControl;
	}
	/**
	 * sets the Model Control
	 * 
	 * @author Peter
	 * 
	 */
	public void setModelControl(ModelControl modelControl) {
		this.modelControl = modelControl;
		modelControl.addObserver(this);
	}
	/**
	 * registers an new Node
	 * 
	 * @author Peter
	 * @param id
	 * @param label
	 * @param visible
	 * @param showlabel
	 */
	public void registerNode(int id, String label, boolean visible,
			boolean showLabel) {
		// visible/ShowLabel als param?
		nodes.put(id, new Node(visible, showLabel, label));
	}
	/**
	 * returns the default label
	 * 
	 * @author Peter
	 * 
	 */
	public String getDefaultLabel(VerificationFormula vf) {
		return Messages.getString("hoare", "rule." + vf + ".label");
	}
	/**
	 * returns the Node which belong to the id
	 * 
	 * @author Peter
	 * @param id
	 */
	public Node getNode(int id) {
		// TODO:nur, damit notifyObservers mögl is...andere lösung?
		this.setChanged();

		return nodes.get(new Integer(id));

	}

	public void update(Observable arg0, Object arg1) {

		linkNodes(modelControl.getRoot());
		if (modelControl.getRoot() != null) {
			getNode(modelControl.getRoot().getID()).setVisible(true);
			connector.setSaveStatus(true);
		}

		redo.setEnabled(programControl.canRedo());
		undo.setEnabled(programControl.canUndo());

		this.setChanged();
		this.notifyObservers();
		
	}
	/**
	 * links Nodes
	 * 
	 * @author Peter
	 * 
	 */
	private void linkNodes(VerificationFormula vf) {
		if (vf == null)
			return;
		if (!(nodes.containsKey(vf.getID())))
			nodes.put(vf.getID(), new Node(false, true, getDefaultLabel(vf)));

		getNode(vf.getID()).resetChildren();
		for (VerificationFormula child : vf.getChildren()) {
			linkNodes(child);
			getNode(vf.getID()).addChild(getNode(child.getID()));
		}

	}

	public void reportError(String message) {
		if (errorEmitter != null)
			errorEmitter.emitError(message);
	}

	public void reportInfo(String message) {
		if (errorEmitter != null)
			errorEmitter.emitInfo(message);
	}

	public VerificationFormula getVF(int id) {
		return modelControl.getVF(id);
	}

	public VerificationFormula getRoot() {
		return modelControl.getRoot();
	}

	public String getCodeSegment(int id) {
		return modelControl.getCodeSegment(id);
	}

	public String getCode() {
		return modelControl.getCode();
	}

	public int getIndex(Dimension codePosition) {
		return modelControl.getIndex(codePosition);
	}

	// TODO: prorgamCtrl anpassen
	public boolean applyTreeEdit(Edit e, int nodeId) {
		return programControl.applyTreeEdit(e, nodeId);
	}

	public boolean applyTreeEdit(Edit e, int nodeId, String preAssertion,
			String postAssertion) {
		return programControl.applyTreeEdit(e, nodeId, preAssertion,
				postAssertion);
	}

	public void init() {
		installWorkScreen();
		workScreen.updateUI();
		activeNode = -1;
		programControl.init();

	}

	public boolean init(String code) {
		activeNode = -1;
		return programControl.init(code);
	}

	public boolean init(File file) {
		activeNode = -1;
		return programControl.init(file);
	}

	public void undo() {
		programControl.undo();
		this.setChanged();
	}

	public void redo() {
		programControl.redo();
		this.setChanged();
	}

	public String getPre() {

		return modelControl.getVF(activeNode).getPreAssertion().getHTMLString();
	}

	public String getPost() {

		return modelControl.getVF(activeNode).getPostAssertion()
				.getHTMLString();
	}

	/**
	 * sets the maximum Evaluation-Time
	 * 
	 * @param int
	 *            value - the Slider-Value
	 * @author Markus
	 * 
	 */
	public void setMaxEvalTime(int value) {
		programControl.setMaxEvalTime(value);
	}

	public int getEvalAmount() {

		return programControl.getEvalAmount();
	}

	public void startTreeEval() {

		programControl.evaluateTree(modelControl.getRoot());
	}

	public void setErrorEmitter(ErrorEmitter errorEmitter) {
		this.errorEmitter = errorEmitter;
	}

	public void setStatusMessage(String msg) {
		JAlgoGUIConnector.getInstance().setStatusMessage(msg);
	}

	public String getFileName() {
		// TODO:Dateiname vom Controller holen
		return null;
	}

	public void load(ObjectInputStream ios) throws IOException,
			ClassNotFoundException {
	
		nodes = (HashMap<Integer, Node>) ios.readObject();
		this.setChanged();
	}

	public void save(ObjectOutputStream oos) throws IOException {
		
		oos.writeObject(nodes);
	}

	public void setBuildTree(boolean b) {

		// TODO:einstellen, ob baum selber aufgebaut werden soll

	}

	public boolean getBeamer() {
		return beamer;
	}
	/**
	 * sets whether beamer mode is active or inactive
	 * 
	 * @author Markus
	 * 
	 */
	public void setBeamer(boolean b) {
		beamer = b;
		workScreen.setBeamer(b);
	}

}