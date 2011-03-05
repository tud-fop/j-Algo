package org.jalgo.module.c0h0.controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JToolBar;
import javax.swing.JMenu;

import org.jalgo.module.c0h0.gui.ModuleContainer;
import org.jalgo.module.c0h0.gui.ButtonManager;

import org.jalgo.module.c0h0.models.Performer;
import org.jalgo.module.c0h0.models.ast.ASTModel;
import org.jalgo.module.c0h0.models.c0model.C0CodeModel;
import org.jalgo.module.c0h0.models.flowchart.FlowChartModel;
import org.jalgo.module.c0h0.models.h0model.H0CodeModel;
import org.jalgo.module.c0h0.models.tmodel.TRuleModel;

/**
 * Central Controller Class
 * 
 * Organizes communication between Models and Views
 * 
 * @author hendrik
 * 
 */
public class Controller {
	private ViewManager viewManager;
	private ButtonManager buttonManager;
	private C0CodeModel c0CodeModel;
	private ASTModel model;
	private FlowChartModel fcModel;
	private H0CodeModel h0CodeModel;
	private TRuleModel tRuleModel;
	private FileManager fileManager;
	private int cPerf;
	private List<Performer> performers;

	/**
	 * @param container
	 * @param toolbar
	 * @param menu
	 */
	public Controller(ModuleContainer container, JToolBar toolbar, JMenu menu) {
		fileManager = new FileManager();

		// create Models first
		model = new ASTModel();
		c0CodeModel = new C0CodeModel(this);
		h0CodeModel = new H0CodeModel(this);
		fcModel = new FlowChartModel(this);
		tRuleModel = new TRuleModel(this);

		// the views come later, because the views need the models
		viewManager = new ViewManager(this, container);
		buttonManager = new ButtonManager(this, viewManager, toolbar, menu);
		buttonManager.toggleTransView(false);
	}

	/**
	 * Saves current content of codeModel into a file using the FileManager
	 * Class
	 * 
	 */
	public void saveFileC0() {
		List<String> code = c0CodeModel.getPlainC0Code();
		fileManager.saveFile(code);
	}

	/**
	 * Saves current content of codeModel into a file using the FileManager
	 * Class
	 * 
	 */
	public void saveFileH0() {
		String code = h0CodeModel.getCode();
		fileManager.saveFile(code, ".h0");
	}

	/**
	 * Saves the flowchart into an image
	 */
	public void saveFileFlowchart() {
		fileManager.saveImage(viewManager.getFlowChartImage(), ".png");
	}
	/**
	 * Toggles to edit mode
	 */
	public void newCode() {
		setState(ViewState.EDIT);
		updateViews();
	}

	/**
	 * Loads a .c0 File using the FileManager Class
	 * 
	 */
	public void openFile() {
		ArrayList<String> file = fileManager.openFile();
		if (file.size() > 0) {
			c0CodeModel.loadCode(file);
			if (viewManager.getState() != ViewState.WELCOME)
				returnToEditMode();
			else
				setState(ViewState.EDIT);
		}
	}

	/**
	 * Same as openFile() - only it starts in a different directory
	 * 
	 */
	public void openExample() {
		ArrayList<String> example = fileManager.openExample();
		if (example.size() > 0) {
			c0CodeModel.loadCode(example);
			if (viewManager.getState() != ViewState.WELCOME)
				returnToEditMode();
			else
				setState(ViewState.EDIT);
		}
	}

	/**
	 * 
	 * Instructs the Model to generate the AST from the c0 code and enters
	 * RUN-state
	 * 
	 */
	public void runTransformation() {
		// Run Button pressed

		// Nun einen AST erstellen

		// Vorerst den Code im CodeModel ablegen
		String code = viewManager.getC0Code();
		c0CodeModel.setCode(code);
		model.create(code);

		// AST erstellt oder etwa doch nicht?
		if (model.isValid()) {
			buttonManager.setUndoAllEnabled(false);
			buttonManager.setUndoStepEnabled(false);
			c0CodeModel.generate();
			h0CodeModel.generate();
			fcModel.generate();
			tRuleModel.generate();
			initPerformers();
			setState(ViewState.RUN);
			setArrows(0);
		} else {
			viewManager.updateErrorInformation(model.getErrorText());
			buttonManager.toggleTransView(true);
		}
		viewManager.updateViews();
	}

	/**
	 * Leaves the RUN-state
	 * 
	 */
	public void returnToEditMode() {
		// Edit Button pressed
		c0CodeModel.clear();
		c0CodeModel.setActive(true);
		h0CodeModel.setActive(false);
		fcModel.setActive(false);
		h0CodeModel.clear();
		fcModel.clear();
		model.setMarkedNode("");
		viewManager.updateErrorInformation("");
		viewManager.renderAllViews();
		setState(ViewState.EDIT);
	}

	/**
	 * Tells the Model to set a node in the AST as marked
	 * 
	 * @param addr
	 */
	public void markNode(String addr) {
		model.setMarkedNode(addr);
		viewManager.renderViews();
	}

	/**
	 * Initializes performers
	 */
	public void initPerformers() {
		performers = new ArrayList<Performer>();
		performers.add(c0CodeModel);
		performers.add(fcModel);
		performers.add(h0CodeModel);
		cPerf = 0;
	}

	/**
	 * Undo a single step
	 */
	public void undoStep() {
		performers.get(cPerf).undoStep();
		setArrows(cPerf);
	}

	/**
	 * Perform a single step
	 */
	public void performStep() {
		performers.get(cPerf).performStep();
		setArrows(cPerf);
	}

	/**
	 * Undo all steps
	 */
	public void undoAll() {
		Performer current = performers.get(cPerf);
		// switch view if neccessary
		if (cPerf == 1 && current.isClear() && viewManager.getState() == ViewState.FLOWH0) {
			setState(ViewState.C0FLOW);
			cPerf -= 1;
			current.setActive(false);
			performers.get(cPerf).setActive(true);
			updateViews();

		} else {
			// move through steps
			if (current.isClear() && cPerf > 0) {
				cPerf -= 1;
				current.setActive(false);
				performers.get(cPerf).setActive(true);
				updateViews();
			} else
				current.undoAll();
		}
		setArrows(cPerf);
	}

	/**
	 * Perform all steps not the smallest possible solution - but clear
	 */
	public void performAll() {
		Performer current = performers.get(cPerf);
		// switch view if neccessary
		if (cPerf == 1 && current.isDone() && viewManager.getState() == ViewState.C0FLOW) {
			setState(ViewState.FLOWH0);
			cPerf += 1;
			current.setActive(false);
			performers.get(cPerf).setActive(true);
			updateViews();
		} else {
			// move through steps
			if (current.isDone() && cPerf < performers.size() - 1) {
				cPerf += 1;
				current.setActive(false);
				performers.get(cPerf).setActive(true);
				updateViews();

			} else {
				current.performAll();
			}
		}
		setArrows(cPerf);
	}

	/**
	 * Adjusts Arrowbuttons to fit situation
	 * 
	 * @param cPerf
	 */
	private void setArrows(int cPerf) {
		Performer current = performers.get(cPerf);
		buttonManager.setUndoStepEnabled(!current.isClear());
		buttonManager.setPerformStepEnabled(!current.isDone());

		buttonManager.setUndoAllEnabled(!(cPerf == 0 && current.isClear()));
		buttonManager.setPerformAllEnabled(!(cPerf == performers.size() - 1 && current.isDone()));
	}

	/**
	 * Updates views
	 */
	public void updateViews() {
		viewManager.updateViews();
	}

	/**
	 * Calls setState in ViewManager and resets the combobox
	 * 
	 * @param newState
	 */
	public void setState(ViewState newState) {
		viewManager.setState(newState);
		buttonManager.setState(newState);
	}

	/**
	 * Returns the code header corresponding to the parameters
	 * 
	 * @param m
	 *            number of variables
	 * @param k
	 *            number of variables scanned
	 * @param i
	 *            index of the output variable
	 * @return code header
	 */
	public int readMKI(int m, int k, int i) {
		int headLines = c0CodeModel.generateCodeFragement(m, k, i);
		viewManager.updateViews();
		viewManager.focusCaret(headLines);
		return headLines;
	}

	/**
	 * Toggles the beamer mode in the view manager
	 */
	public void toggleBeamerMode() {
		c0CodeModel.setCode(viewManager.getC0Code());
		viewManager.setBeamerMode(!viewManager.isBeamerMode());
	}

	/**
	 * Returns viewManager
	 * 
	 * @return viewManager
	 */
	public ViewManager getViewManager() {
		return this.viewManager;
	}

	/**
	 * Returns C0CodeModel
	 * 
	 * @return C0CodeModel
	 */
	public C0CodeModel getC0CodeModel() {
		return c0CodeModel;
	}

	/**
	 * Returns the AST model
	 * 
	 * @return ASTModel
	 */
	public ASTModel getASTModel() {
		return model;
	}

	/**
	 * Returns the fcModel
	 * 
	 * @return fcModel
	 */
	public FlowChartModel getFcmodel() {
		return fcModel;
	}

	/**
	 * Returns the h0CodeModel
	 * 
	 * @return h0CodeModel
	 */
	public H0CodeModel getH0CodeModel() {
		return h0CodeModel;
	}

	/**
	 * Returns the tRuleModel
	 * 
	 * @return tRuleModel
	 */
	public TRuleModel getTRuleModel() {
		return tRuleModel;
	}

	/**
	 * Returns the buttonManager
	 * 
	 * @return buttonManager
	 */
	public ButtonManager getButtonManager() {
		return this.buttonManager;
	}
}
