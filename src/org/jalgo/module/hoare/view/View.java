package org.jalgo.module.hoare.view;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.JToolBar.Separator;
import javax.swing.filechooser.FileFilter;

import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.main.util.Settings;
import org.jalgo.module.hoare.ModuleConnector;
import org.jalgo.module.hoare.constants.Event;
import org.jalgo.module.hoare.constants.Rule;
import org.jalgo.module.hoare.constants.TextStyle;
import org.jalgo.module.hoare.control.Controller;
import org.jalgo.module.hoare.model.Model;
import org.jalgo.module.hoare.model.Variable;
import org.jalgo.module.hoare.model.VerificationFormula;
import org.jalgo.module.hoare.view.formula.ConcreteFormulaEditor;
import org.jalgo.module.hoare.view.formula.FormulaEditor;
import org.jalgo.module.hoare.view.formula.FormulaEditorObserver;
import org.jalgo.module.hoare.view.formula.FormulaViewer;
import org.jalgo.module.hoare.view.formula.Enten;
import org.jalgo.module.hoare.view.toolbar.BeamerMode;
import org.jalgo.module.hoare.view.toolbar.DelPart;
import org.jalgo.module.hoare.view.toolbar.LoadCNullProg;
import org.jalgo.module.hoare.view.toolbar.NormalMouse;
import org.jalgo.module.hoare.view.toolbar.ReInit;
import org.jalgo.module.hoare.view.toolbar.Redo;
import org.jalgo.module.hoare.view.toolbar.ResetZoom;
import org.jalgo.module.hoare.view.toolbar.ToggleSplitter;
import org.jalgo.module.hoare.view.toolbar.Undo;
import org.jalgo.module.hoare.view.toolbar.ZoomIn;
import org.jalgo.module.hoare.view.toolbar.ZoomOut;
import org.jgraph.graph.GraphConstants;

/**
 *
 * This is the master class of the Graphical User Interface
 * and the connection to contol and model
 * @author tomas, Antje
 *
 */

public class View implements Observer, KeyListener{
	
	/**
	 * font size if the beamer mode is activated
	 */
	protected static final int fontSizeBeamerMode = 19;
	/**
	 * standard font size
	 */
	protected static final int fontSizeNormalMode = 14;
	/**
	 * font size if the beamer mode is activated
	 */
	protected static final String mainFontFileName = "DejaVuSans.ttf";
	/**
	 * standard font size
	 */
	protected static final String sourceCodeFontFileName = "DejaVuSansMono.ttf";
	
	/**
	 * Represents a zoom mode:
	 * NO - no zoom
	 * IN - zoom in
	 * OUT -zoom out
	 * @author Antje
	 *
	 */
	public enum ZoomMode { NO, IN, OUT }
	
	/**
	 * The screen that is shown on startup.
	 */
	private WelcomeScreen startScreen;
	/**
	 * The screen that is shown when working with the modul, after the welcome screen.
	 */
	private WorkScreen workScreen;
	/**
	 * Root component that is the container for all components of the gui.
	 */
	private JComponent rootComponent;
	/**
	 * Tool bar of the gui.
	 */
	private JToolBar toolbar;
	/**
	 * Connects the module with j-Algo.
	 */
	private ModuleConnector mc;
	/**
	 * The current zoom mode.
	 */
	private ZoomMode zoomMode = ZoomMode.NO;
	/**
	 * Label that displays the current error message.
	 */
	private JLabel errorLabel;
	/**
	 * Controller.
	 */
	private Controller control;
	/**
	 * Model for the gui.
	 */
	private Model model;
	/**
	 * <code>FormulaEditor</code>s that are currently open.
	 */
	private Map<String,ConcreteFormulaEditor> editors = new HashMap<String,ConcreteFormulaEditor>();
	/**
	 * Global font of the gui.
	 */
	private Font mainFont;
	
	/**
	 * Font for source code.
	 */
	private Font sourceCodeFont;
	/**
	 * Listens to mouse events in <code>WSGraph</code>.
	 */
	private GraphListener graphMouseListener = new GraphListener();
	/**
	 * <code>true</code> for beamer mode (viewing options optimized for using a beamer), <code>false</code> for normal mode.
	 */
	private boolean isBeamerMode = false;
	private Undo undo;
	private Redo redo;
	private JToolBar westToolbar;
	
	/**
	 * Creates a new instance of the view controller.
	 * @param c the connector with j-algo
	 */
	public View(ModuleConnector c) {
		if( c == null )
			return;
		mc=c;
		rootComponent = JAlgoGUIConnector.getInstance().getModuleComponent(c);
		toolbar = JAlgoGUIConnector.getInstance().getModuleToolbar(c);
						
		rootComponent.removeAll();
		rootComponent.setLayout(new BorderLayout());
		try {
			mainFont = Font.createFont(Font.TRUETYPE_FONT, new URL(Messages.getResourceURL("hoare", "fontFolder").toString()+mainFontFileName).openStream());
		}
		catch (Exception e) {
			e.printStackTrace();
			mainFont = rootComponent.getFont();
		}
		try {
			sourceCodeFont = Font.createFont(Font.TRUETYPE_FONT, new URL(Messages.getResourceURL("hoare", "fontFolder").toString()+sourceCodeFontFileName).openStream());
		}
		catch (Exception e) {
			e.printStackTrace();
			sourceCodeFont = mainFont;
		}
		try {
			isBeamerMode = Settings.getBoolean("hoare", "isBeamerMode");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		this.installMenu();
		this.installToolbar(true);
	}

	/**
	 * Model setter
	 * @param m
	 */
	public void setModel(Model m){
		model=m;
	}
	
	/**
	 * Model getter
	 * @return the model
	 */
	public Model getModel(){
		return model;
	}
	
	/**
	 * Control setter
	 * @param c controller
	 */
	public void setConroller(Controller c){
		control = c;		
	}

	/**
	 * Returns the controller.
	 * @return the controller
	 */
	private Controller getController() {
		return control;
	}
	
	/**
	 * installs the welcomescreen
	 */
	public void installWelcomeScreen() {
		
    		if (startScreen == null) 
    			startScreen = new WelcomeScreen(this);
    	
    	rootComponent.removeAll();
    	rootComponent.add(startScreen);
    	startScreen.revalidate();
	}
	
	/**
	 * install workscreen will install the workscreen,
	 * this function is supposed to be used by controller only
	 * otherwise use init()
	 */
	public void installWorkScreen() {
		installToolbar(false);
			
		if (workScreen == null) 
			workScreen = new WorkScreen(this);
		
		rootComponent.removeAll();
		rootComponent.add(workScreen);
		
		updateFont();
		setBeamer(isBeamerMode);
		
		workScreen.revalidate();
	}
	
	/**
	 * changes the error label in the toolbar and makes it red
	 * @param text
	 * 		labeltext
	 */
	public void setStatusText(String text){
		int textSize=14;
		errorLabel.setForeground(Color.red);
		errorLabel.setText("<html><div style=\"font-size:"+textSize+"pt\">"+text+"</div><html>");
	}
	
	/**
	 * sets the error label to an other "bleeched" color
	 */
	public void bleechStatusText(){
		errorLabel.setForeground(Color.decode("#a68067"));
	}
	
	/**
	 * changes the zoom mode
	 * which changes the cursor to zoomIn, ZoomOut, or don't zoom
	 * 
	 * you won't zoom if you use this function, use instead zoom(int Point)
	 * 
	 * @param mode the new zoom mode
	 */
	public void setZoomMode(ZoomMode mode) {
		zoomMode = mode;
		workScreen.getGraph().setCursor(getMyCursor(mode));
	}
	
	/**
	 * the current zoom mode
	 * @return the corrent zoom mode
	 */
	public ZoomMode getZoomMode() {
		return zoomMode;
	}
	
	/**
	 * makes a new workscreen (not for reinit button to use)
	 */
	public void init() {
				
		installToolbar(false);
		
		workScreen = new WorkScreen(this);
		rootComponent.removeAll();
		rootComponent.add(workScreen);
		
		updateFont();
		setBeamer(isBeamerMode);
		
		workScreen.revalidate();
		
	}
	
	/**
	 * clears the workscreen by performing a reinit
	 */
	public void reinit(){
		//k�nnte Exception werfen
		try{
			if (JOptionPane.showConfirmDialog(workScreen, Messages.getString("hoare", "wannaReinit"),  Messages.getString("hoare","ttt.reinit"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				getController().handleEvent(Event.REINIT);
			}
		} catch (Exception e){ 
			getController().handleEvent(Event.REINIT);
		}
		
		}

	/**
	 * interface method to get updates from Model
	 * gives the Observable to the workscreen for further use
	 * @param o
	 * 		supposed to be the Model
	 * @param arg
	 * 		supposed to be null
	 */
	synchronized public void update(Observable o, Object arg) {
		workScreen.update(model, arg);
	}
	
	/**
	 * function to set up the beamer mode
	 * @param beamer if <code>true</code> the view is set to beamer mode, otherwise it's set to normal mode
	 */
	public void setBeamer(boolean beamer){
		isBeamerMode = beamer;
		try {
			Settings.setBoolean("hoare", "isBeamerMode", beamer);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		int fontSize = 14;
		if (beamer){
			fontSize = fontSizeBeamerMode;
			if (workScreen!=null)
			 workScreen.getGraph().setAbsolutZoomValueToMiddle(3);
		} else {
			fontSize = fontSizeNormalMode;
			if (workScreen!=null)
			 workScreen.getGraph().setAbsolutZoomValueToMiddle(0);
		}	
		setFontSize(fontSize);
		
	}
	
	/**
	 * installs the Toolbar with buttons and errorLabel
	 * if boolean initToolbar is true, the function will init the toolbar without buttons
	 * if it is false it will init the buttons (therefor the toolbar should be inited before)
	 * 
	 * @param initToolbar
	 */
	public void installToolbar(boolean initToolbar) {
	
	if (initToolbar){	
			toolbar.removeAll();
		
		    westToolbar = new JToolBar();;
			westToolbar.setFloatable(false);
			westToolbar.setRollover(true);	
						
			errorLabel = new JLabel("");
			errorLabel.setForeground(Color.red);
						
			toolbar.setLayout(new BorderLayout());
			
			toolbar.add(westToolbar, BorderLayout.WEST);
			toolbar.add(errorLabel, BorderLayout.EAST);
			
			
			
	} else {	
		westToolbar.removeAll();
		
		undo = new Undo(this);
		redo = new Redo(this);
		
		ResetZoom rZ = new ResetZoom(this);
		ReInit reInit = new ReInit(this);
		LoadCNullProg loadCNullProg = new LoadCNullProg(this);
		DelPart del = new DelPart(this);
		ZoomIn in = new ZoomIn(this);
		ZoomOut out = new ZoomOut(this);
		NormalMouse nM = new NormalMouse(this);
		ToggleSplitter tS= new ToggleSplitter(this);
		
		del.putValue(AbstractAction.NAME, "");
		reInit.putValue(AbstractAction.NAME, "");
		loadCNullProg.putValue(AbstractAction.NAME, "");
		undo.putValue(AbstractAction.NAME, "");
		redo.putValue(AbstractAction.NAME, "");
		in.putValue(AbstractAction.NAME, "");
		out.putValue(AbstractAction.NAME, "");
		nM.putValue(AbstractAction.NAME, "");
		rZ.putValue(AbstractAction.NAME, "");
		
				
		westToolbar.add(new JToolButton(new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.undo")), this, Messages.getString("hoare",
				"ttt.undo"), undo));
		
		westToolbar.add(new JToolButton(new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.redo")), this, Messages.getString("hoare",
				"ttt.redo"), redo));
		
		westToolbar.addSeparator();
		
		westToolbar.add(new JToolButton(new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.reinit")), this, Messages.getString("hoare",
				"ttt.reinit"), reInit));
		
		westToolbar.add(new JToolButton(new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.openC0Program")), this, Messages.getString(
				"hoare", "ttt.openC0Program"), loadCNullProg));
		
		westToolbar.add(new JToolButton(new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.delNode")), this, Messages.getString(
				"hoare", "ttt.delNode"), del));
		
		westToolbar.addSeparator();
		
		westToolbar.add(new JToolButton(new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.resetZoom")), this, Messages.getString("hoare",
				"ttt.resetZoom"), rZ));
		
		westToolbar.add(new JToolButton(new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.zoomIn")), this, Messages.getString("hoare",
				"ttt.zoomIn"), in));
		
		westToolbar.add(new JToolButton(new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.zoomOut")), this, Messages.getString("hoare",
				"ttt.zoomOut"), out));
		
		westToolbar.add(new JToolButton(new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.normalMouse")), this, Messages.getString("hoare",
				"ttt.normalMouse"),nM));
		
		westToolbar.add(new JToolButton(new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.toggleSplitter")), this, Messages.getString("hoare",
				"ttt.toggleSplitter"),tS));
		
		
		
		
		
		}
		
		
	}

	/**
	 * installs the Menu of the Hoare module
	 */
	public void installMenu() {
			
		JMenu menu = JAlgoGUIConnector.getInstance().getModuleMenu(mc);
		menu.removeAll();
		menu.add(new ReInit(this));
		menu.add(new LoadCNullProg(this));
		menu.add(new Separator());
		menu.add(new Undo(this));
		menu.add(new Redo(this));
		menu.add(new Separator());
		BeamerMode beamerMode = new BeamerMode(this);
	 if (beamerMode.isAktiv() || isBeamerMode) {
	  beamerMode.setSelected(beamerMode.isAktiv() || isBeamerMode);
	 }
	 menu.add(beamerMode);
		
	}

	/**
	 * workscreen getter
	 * @return the workScreen
	 */
	public WorkScreen getWorkScreen() {
		return workScreen;
	}
	
	/**
	 * Message setter for welcome Screen (for the label in the welcome screen)
	 * @param s
	 */
	public void setWelcomeMessage(String s){
		JAlgoGUIConnector.getInstance().setStatusMessage(s);
		
	}
	
	/**
	 * calls handle Event in controller with the Event Redo
	 */
	public void redo(){
		getController().handleEvent(Event.REDO);
	}
	
	/**
	 * calls handle Event in controller with the Event Undo
	 */
	public void undo(){
		getController().handleEvent(Event.UNDO);
	}
	
	/**
	 * Shows the formula editor for a variable when the mouse is clicked or an action is performed.
	 * Can be used as <code>MouseListener</code> or <code>ActionListener</code>.
	 * @author Antje
	 *
	 */
	protected class EditVariableListener extends MouseAdapter implements ActionListener {
		
		private String variableName;
		
		/**
		 * Creates a new listener for the specified variable.
		 * @param variableName name of the variable that will be edited
		 */
		public EditVariableListener(String variableName) {
			this.variableName = variableName;
		}

		/**
		 * Creates a new listener for the specified variable.
		 * @param verificationFormulaID id of the <code>VerificationFormula</code> that uses the variable that will be edited
		 * @param isPreAssertion <code>true</code> if the variable is part of the pre assertion, false if it is part of the post assertion
		 */
		public EditVariableListener(int verificationFormulaID, boolean isPreAssertion) {
			VerificationFormula vf = getModel().getVerificationFormula(verificationFormulaID);
			variableName = getVariable(vf, isPreAssertion).getName();
		}
		
		public void mouseClicked(MouseEvent e) {
			showFormulaEditor(variableName);
		}
		
		public void actionPerformed(ActionEvent e) {
			showFormulaEditor(variableName);
		}
		
	}
	
	/**
	 * <code>MouseListener</code> and <code>MouseWheelListener</code> for <code>WSGraph</code>
	 * @author Tomas
	 *
	 */
	protected class GraphListener extends MouseAdapter implements MouseWheelListener {
		
		public void mouseClicked(MouseEvent e) {
//			if (e.getClickCount()>2){
//				setStatusText("asdhfbvhsdbfjvhbfsjvbjlfsdbvlfsdbvbvlbfkljbydfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffasdhfbvhsdbfjvhbfsjvbjlfsdbvlfsdbvbvlbfkljbydfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffasdhfbvhsdbfjvhbfsjvbjlfsdbvlfsdbvbvlbfkljbydfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffasdhfbvhsdbfjvhbfsjvbjlfsdbvlfsdbvbvlbfkljbydfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffasdhfbvhsdbfjvhbfsjvbjlfsdbvlfsdbvbvlbfkljbydfffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
//			}
			
			
			if (getZoomMode()==ZoomMode.NO) {
				MyCell cell = null;
				try {
				cell = (MyCell) workScreen.getGraph().getGraph().getFirstCellForLocation(e.getX(), e.getY());
				} catch (Exception Ex) {}
				if (cell == null) {
					workScreen.getGraph().setUnselected();
				}
				else if (cell==workScreen.getGraph().getSelectedCell()) {
					 if (e.getButton()==MouseEvent.BUTTON1) {
						
					// Formular Editor �ffnen	 
						if (cell==workScreen.getGraph().getOpened()) {
							Rectangle2D rect = GraphConstants.getBounds(cell.getAttributes());
							double scale = workScreen.getGraph().getGraph().getScale();
							Rectangle2D rec = new Rectangle2D.Double(0, rect.getY()*scale, 0, rect.getHeight()*scale);
							
							double y = e.getY() - rec.getY();
							double third = rec.getHeight()/3;
							
							if ( y <= third){
								EditVariableListener evl = new EditVariableListener(workScreen.getGraph().getSelectedCell().getID(),true);
								evl.actionPerformed(new ActionEvent( evl, 0 ,"Pre"));
								
							} else {
							if (y>= (third*2)){
								EditVariableListener evl = new EditVariableListener(workScreen.getGraph().getSelectedCell().getID(),false);
								evl.actionPerformed(new ActionEvent( evl, 0 ,"Post"));
								
							}}
						}
					// Zelle Selectieren oder Expanden	
						else { workScreen.getGraph().setSelected(cell);}
					
					 
					 }
				}
				else {
					
					
					workScreen.getGraph().setSelected(cell);
				}
			}
			else {
				if (e.getButton()==MouseEvent.BUTTON3){
					setZoomMode(ZoomMode.NO);
					e.consume();
				} else {
				
				zoom(getZoomMode(), e.getPoint()); }
			}
			
		}
		
		/**
		 * MouseWheel with Alt pressed as zoom
		 */
		public void mouseWheelMoved(MouseWheelEvent e) {
		
			if (e.isAltDown()){
				if (e.getWheelRotation()>0) {
					zoom(ZoomMode.OUT, e.getPoint());
				}
				if (e.getWheelRotation()<0) {
					zoom(ZoomMode.IN, e.getPoint());
				}
			}
		}
		
	}
	
	/**
	 * Listener that generates popup menus for <code>WSGraph</code>.
	 * @author Tomas
	 *
	 */
	protected class CellPopupMenu extends MouseAdapter {
		
		public void mouseClicked(MouseEvent e) {

			if (e.getButton() == MouseEvent.BUTTON3 && workScreen.getGraph().getSelectedCell() != null && !e.isConsumed()){
				
				String pre = getVariableForAssertion(model.getVerificationFormula(workScreen.getGraph().getSelectedCell().getID()).getPreAssertion(TextStyle.SHORT)).getName();
				String post= getVariableForAssertion(model.getVerificationFormula(workScreen.getGraph().getSelectedCell().getID()).getPostAssertion(TextStyle.SHORT)).getName();
				
				
				JPopupMenu popmen = new JPopupMenu(); 
				
				popmen.add(new JLabel ("edit:"));
				popmen.addSeparator();
				if (pre!=null){
				JMenuItem vor = new JMenuItem("{ "+pre+" }");
				vor.addActionListener(new EditVariableListener(
						workScreen.getGraph().getSelectedCell().getID(),
						true)
				);
				popmen.add(vor);
				}
				if (post!=null && !post.equals(pre)) {
				JMenuItem nach = new JMenuItem("{ "+post+" }");
				nach.addActionListener(new EditVariableListener(
						workScreen.getGraph().getSelectedCell().getID(),
						false)
				); 
				popmen.add(nach);
				}
								
				popmen.show( e.getComponent(), e.getX(), e.getY() );
			}
			
		}
		
	}

	/**
	 * zooming function
	 * 
	 * @param mode
	 * 		Zoommode
	 * @param point
	 * 		point to zoom to
	 */
	public void zoom (ZoomMode mode, Point2D point){
		switch(mode) {
		case IN:
			workScreen.getGraph().zoom(1, point);
			break;
		case OUT:
			workScreen.getGraph().zoom(-1, point);
			break;
		}
	}
	
	/**
	 * the Cursor changer for the zoom function
	 * the int value should be: -1 for zoomOut Cursor, 1 for zoomInCursor or 0 for default Cursor
	 * @param zoomMode
	 * 		ZoomMode (if zoom out or zoom in)
	 * @return Cursor
	 */
	public static Cursor getMyCursor(ZoomMode zoomMode)
    {
		Cursor defaultCursor = null;
		if (zoomMode!=ZoomMode.NO){
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			URL cursorUrl = null;
			if (zoomMode==ZoomMode.OUT) {cursorUrl = Messages.getResourceURL("hoare", "icon.zoomOut");}
			else {cursorUrl = Messages.getResourceURL("hoare", "icon.zoomIn");}
			
			Image cursorImage = new ImageIcon(cursorUrl).getImage();
	        defaultCursor = toolkit.createCustomCursor(cursorImage, new Point(1, 1),"AA_Cursor");
		} else {
			defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
		}
		        
        return defaultCursor;
    }
	
	/**
	 * ModuleConnector getter 
	 * @return the modul connector
	 */
	public ModuleConnector getMC(){
		return mc;
	}
	
	/**
	 * Applies the rule to the selected cell if possible.
	 * @param rule
	 * 		rule which should be applied
	 */
	public void applyRule(Rule rule) {
		MyCell cell = workScreen.getGraph().getSelectedCell();
		if (!(cell==null)) {
    		int nodeID = workScreen.getGraph().getSelectedCell().getID();
	    	getController().handleEvent(Event.APPLYRULE, rule, nodeID);
		}
		else {
			setStatusText(Messages.getString("hoare", "view.cellSelectionError"));
		}
    }
	
	/**
	 * Returns the variable with the specified name.
	 * @param name name of the variable
	 * @return variable specified by the name
	 */
	protected Variable getVariable(String name) {
		for (Variable v : getModel().getVariables()) {
			if (v.getName().equals(name)) {
				return v;
			}
		}
		return null;
	}
	
	/**
	 * Returns the two variables belonging to the specified <code>VerifictaionFormula</code>.
	 * @param vf the <code>VerificationFormula</code>
	 * @return an array variables with variables[0] being the variable of the pre assertion and variables[1] being the variable of the post assertion of the specified <code>VerificationFormula</code>
	 */
	protected Variable[] getVariables(VerificationFormula vf) {
		if (vf==null) return new Variable[2];
		
		
		Variable[] variables = new Variable[2];
		variables[0] = getVariableForAssertion(vf.getPreAssertion(TextStyle.SHORT));
		variables[1] = getVariableForAssertion(vf.getPostAssertion(TextStyle.SHORT));
		return variables;
	}
	
	/**
	 * Returns the variable belonging to the pre or post assertion of the speicified <code>VerificationFormula</code>. 
	 * @param vf the <code>VerificationFormula</code>
	 * @param isPreAssertion if true the variable of the pre assertion is returned, if false the variable of the post assertion is returned
	 * @return the variable belonging to the pre or post assertion of the speicified <code>VerificationFormula</code>.
	 */
	protected Variable getVariable(VerificationFormula vf, boolean isPreAssertion) {
		if (isPreAssertion) {
			return getVariables(vf)[0];
		}
		else {
			return getVariables(vf)[1];
		}
	}
	
	/**
	 * Returns the variable that is contained in the specified assertion
	 * @param assertionShort assertion <code>String</code> in the form that contains the variable name.
	 * @return the variable of the assertion
	 */
	protected Variable getVariableForAssertion(String assertionShort) {
		for (Variable v : getVariablesSortedByLength()) {
			if (assertionShort.indexOf(v.getName())!=-1) {
				return v;
			}
		}
		return null;
	}
	
	/**
	 * Sorts all variables by length.
	 * @return the sorted collection of variables.
	 */
	private Collection<Variable> getVariablesSortedByLength() {
		
		class VariableLengthComparator implements Comparator<Variable> {

			public int compare(Variable o1, Variable o2) {
				return o1.getName().length()-o2.getName().length();
			}
			
		}
		
		List<Variable> var = new ArrayList<Variable>(getModel().getVariables());
		Collections.sort(var, Collections.reverseOrder(new VariableLengthComparator()));
		return var;
	}
	
	/**
	 * KeyListener for DELETE Treeparts
	 */
	public void keyPressed(KeyEvent e) {
		if ((e.getKeyCode()==KeyEvent.VK_DELETE || e.getKeyCode()==KeyEvent.VK_BACK_SPACE)) {
			deletePart();
		}
	}
	
	/**
	 * deletes a part of the tree, used by Keyevents and the DeletePart button
	 */
	public void deletePart(){
		if (workScreen.getGraph().getSelectedCell()!=null) {
			getController().handleEvent(Event.DELETENODE, workScreen.getGraph().getSelectedCell().getID());	
			getWorkScreen().getVar().initVariablesView();
		} else {
			errorLabel.setText(Messages.getString("hoare","out.DeleteNodeFailed"));
		}		
	}
	
	/**
	 * Is called when a key is released.
	 * @param e the key event
	 */
	public void keyReleased(KeyEvent e) {
		
	}
	
	/**
	 * Is called when a key is typed.
	 * @param e the key event
	 */
	public void keyTyped(KeyEvent e) {
		
	}

	/**
	 * Shows the {@link ConcreteFormulaEditor} to edit the specified {@link Variable}.
	 * @param variableName the {@link Variable} that is edited
	 */
	public void showFormulaEditor(final String variableName) {
		if (editors.containsKey(variableName)) {
			editors.get(variableName).toFront();
		}
		else {
			final Variable v = getVariable(variableName);
			FormulaEditorObserver observer = new FormulaEditorObserver() {
				class WarteThread extends Thread {
					boolean run = true;
					long time;
					FormulaEditor editor;
					public WarteThread(long time){
						this.time = time;
						this.start();
					}
					public void run(){
						try {
							sleep(time);
						} catch(InterruptedException e){
							return;
						}
						if( editor == null )
							return;
						Enten enten = new Enten();
						if( enten == null )
							return;
						((ConcreteFormulaEditor)editor).setFormulaPreview(enten);
						
						while( run ){
							enten.repaint();
							try {
								Thread.sleep(50);
							} catch(InterruptedException e){
								return;
							}
						}
					}
					public void setEditor(FormulaEditor editor){
						this.editor = editor;
					}
					public void end(){
						run = false;
					}
				}
				WarteThread warteThread = new WarteThread(15*60000);

				public void applyFormulaChange(FormulaEditor editor) {
					ConcreteFormulaEditor e = (ConcreteFormulaEditor)editor;
					int id = v.getVerificaitonId();
					VerificationFormula vf = getModel().getVerificationFormula(id);
					if (vf.getPreAssertion(TextStyle.SHORT).indexOf(v.getName())!=-1) {
						getController().handleEvent(Event.EDITPREASSERTION, e.getFormula(), id);
					}
					else {
						getController().handleEvent(Event.EDITPOSTASSERTION, e.getFormula(), id);
					}
				}
				public void formulaChanged(FormulaEditor editor) {
					// do nothing
				}
				public void formulaEditorClosed(FormulaEditor editor) {
					warteThread.interrupt();
					warteThread.end();
					editors.remove(v.getName());
					workScreen.getRules().validate();
				}
				public void receiveParseMessage(FormulaEditor editor, String message) {
					if( message == null ){
						warteThread.setEditor(editor);
						return;
					}
					if (message == FormulaEditor.MESSAGE_OKAY) {
						editor.removeParseMessage();
					}
					else if (message == FormulaEditor.MESSAGE_ERROR) {
						editor.setParseMessage(Messages.getString("hoare", "view.parseError"));
					}
					else {
						editor.setParseMessage(message);
					}
				}
			};
			ConcreteFormulaEditor editor = new ConcreteFormulaEditor(v.getName(), v.getAssertion(TextStyle.EDITOR));
			observer.receiveParseMessage(editor, null);
			editor.setFont(getMainFont());
			editor.addFormulaEditorObserver(observer);

			FormulaViewer viewer = new FormulaViewer(editor.getFont(), v.getName());
			editor.setFormulaPreview(viewer);
			viewer.setFormulaEditor(editor);
			editors.put(variableName, editor);
		}
	}
	
	/**
	 * Parses the speicified source code.
	 * To be used by other classes belonging to the view.
	 * @param sourceCode the source code as a string
	 */
	public void parseSourceCode(String sourceCode) {
		getWorkScreen().getGraph().setUnselected();
		getWorkScreen().getSource().makeNotEditable();
		try {
			getController().handleEvent(Event.PARSECODE, sourceCode);
		}
		catch (Exception e) { // parsing failed
			getWorkScreen().getSource().makeEditable();
		}
	}

	/**
	 * Parses the speicified source code.
	 * To be used by other classes belonging to the view.
	 * @param sourceCodeFile file that contains the source code
	 */
	public void parseSourceCode(File sourceCodeFile) {
		getWorkScreen().getGraph().setUnselected();
		getWorkScreen().getSource().makeNotEditable();
		try {
			getController().handleEvent(Event.PARSECODE, sourceCodeFile);
		}
		catch (Exception e) { // parsing failed
			getWorkScreen().getSource().makeEditable();
		}
	}

	/**
	 * Loads source code from a file that will be specified by the user.
	 */
	public void loadSourceCode() {
		JFileChooser fc = new JFileChooser(System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "examples"
				+ System.getProperty("file.separator") + "hoare");
		fc.setFileFilter(new FileFilter() {
			
			@Override
			public boolean accept(File f) {
				if (f.getName().endsWith("c"))
					return true;
				if (f.isDirectory())
					return true;
				return false;
			}
			
			@Override
			public String getDescription() {
				return "*.c";
			}
			
		});
		
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { // parsing succeeded
			parseSourceCode(fc.getSelectedFile());
		}
	}
	
	/**
	 * Loads a previously saved verification tree from a file that will be specified by the user.
	 */
	public void openTree() {
		JAlgoGUIConnector.getInstance().showOpenDialog(true, true);
	}
	
	/**
	 * Changes the global font size for the gui.
	 * @param fontSize the new font size
	 */
	public void setFontSize(int fontSize) {
		mainFont = mainFont.deriveFont((float)fontSize);
		sourceCodeFont = sourceCodeFont.deriveFont((float)fontSize);
		String settingsString;
		if (isBeamerMode) {
			settingsString = "fontSize.beamer";
		}
		else {
			settingsString = "fontSize.normal";
		}
		try {
			Settings.setString("hoare", settingsString, String.valueOf(fontSize));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		updateFont();
	}
	
	/**
	 * Updates the font of all children.
	 *
	 */
	public void updateFont() {
		if (workScreen!=null) {
			workScreen.updateFont();
		}
		for (ConcreteFormulaEditor e : editors.values()) {
			e.setFont(mainFont);
		}
	}
	
	/**
	 * Returns the global font for the gui.
	 * @return the global font for the gui.
	 */
	public Font getMainFont() {
		return mainFont;
	}

	/**
	 * Returns the source code font for the gui.
	 * @return the source code font for the gui.
	 */
	public Font getSourceCodeFont() {
		return sourceCodeFont;
	}
	/**
	 * getter for graphMouseListener
	 * @return the graphMouseListener
	 */
	public GraphListener getGraphMouseListener(){
		return graphMouseListener;
	}
	/**
	 * shows success dialog
	 */
	@SuppressWarnings("deprecation")
	public void showProveFinished() {
		JOptionPane pane = new JOptionPane(Messages.getString("hoare", "allsuccessful"));
		JDialog dialog = pane.createDialog(workScreen, "Success");
	    dialog.show();
	}
	/**
	 * functions grays the undo button or makes it colorful again
	 * @param gray
	 */
	public void grayUndo(boolean gray){
		if (undo!= null){
		undo.grayButton(gray);
		}
		}
	/**
	 * functions grays the redo button or makes it colorful again
	 * @param gray
	 */
	public void grayRedo(boolean gray){
		if (undo!= null){
		redo.grayButton(gray);
		}
	}
	
	/**
	 * This Method performs the splitter to toggle between
	 * the visible and the invisible State.
	 * Note: The state is by default visible! 
	 */
	public void toggleSplitter(){
		if (workScreen!=null){
		 workScreen.toggleSplitter();
		 workScreen.getGraph().beautifier();
		}
	}
	/**
	 * this function will reset the graph to the initial zoom
	 * it will test if the beamermode is on and ajust zoom to this
	 */
	public void resetZoom(){
		if(isBeamerMode) {
			this.getWorkScreen().getGraph().setAbsolutZoomValueToMiddle(3);
		} else {
			this.getWorkScreen().getGraph().setAbsolutZoomValueToMiddle(0);
		}
		this.setZoomMode(ZoomMode.NO);
	}
	
	
}
