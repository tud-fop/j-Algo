package org.jalgo.module.unifikation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import org.jalgo.main.AbstractModuleConnector.SaveStatus;
import org.jalgo.main.util.Messages;
import org.jalgo.module.unifikation.algo.model.Formating;
import org.jalgo.module.unifikation.algo.view.Algo;
import org.jalgo.module.unifikation.editor.Editor;
import org.jalgo.module.unifikation.parser.ISetParser;
import org.jalgo.module.unifikation.parser.SetParser;
import org.jalgo.module.unifikation.WelcomeScreen;

/**
 * the main application
 * @author Alex
 *
 */
public class Application {

	private ApplicationState state;
	public JComponent contentPane;
	public JToolBar toolbar;	
	public JMenu menu;
	private String problem;
	private IAppView currentView;
	private ModuleConnector moduleConnector;
	private JCheckBoxMenuItem menubeamer;
	private WelcomeScreen welcomeScreen;
	
	// Toolbar buttons
	public JButton undoAll;
	public JButton undoBlockStep;
	public JButton undoStep;
	public JButton performAll;
	public JButton performBlockStep;
	public JButton performStep;
	
	
	/**
	 * installs the Welcomescreen
	 * @param contentPane
	 * @param toolbar
	 * @param menu
	 * @param moduleConnector2
	 */
	public void startWelcomescreen(JComponent contentPane, JToolBar toolbar, JMenu menu, ModuleConnector moduleConnector) {
		this.contentPane=contentPane;
		this.toolbar=toolbar;
		this.menu=menu;
		this.moduleConnector=moduleConnector;
		
		menubeamer = new JCheckBoxMenuItem("Pr" + Constants.lowercasedAE + "sentationsmodus");
		menubeamer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Formating.toggleBeamerMode();
				currentView.updateSetView();
			}
		});
		menu.add(menubeamer);
		

		contentPane.removeAll();
		welcomeScreen = new WelcomeScreen(this);

        contentPane.add(welcomeScreen);
	}
	
	/**
	 * initializes the application
	 * @param moduleConnector 
	 */
	public void init(){
		
		contentPane.removeAll();
				
		this.state=ApplicationState.Preview;
		

		setFinished("M={(,)}");
		
	}

	public String getProblem() {
		return problem;
	}
	
	public String getActualHTMLProblem() {
		problem = null;
		if(state.equals(ApplicationState.Editor))
			problem = ((Editor) currentView).editWorkspace.getText();
		return problem;
	}

	/**
	 * should be called by a view, when its work is done
	 * this will switch to next view
	 */
	public void setFinished(String problem){
		this.setProblem(problem);
		contentPane.removeAll();
		toolbar.removeAll();
		
		switch(state){
		case Preview:
		case Algo:
			currentView=new Editor();
			state=ApplicationState.Editor;
			this.enableSaving();
			break;
		case Editor:
			currentView=new Algo();
			state=ApplicationState.Algo;
			//menubeamer.setEnabled(true);
			this.disableSaving();
			break;
		}
		
		currentView.setApplication(this);
		currentView.setContentPane(contentPane);
		
		//Repaint UI - really needed!
		toolbar.updateUI();
		contentPane.updateUI();
	}

	public void setProblem(String problem) {
		//cut off html tags
		int iStart=problem.lastIndexOf("<body>");
		int iEnd=problem.lastIndexOf("</body>");
		if(iStart>=0 && iEnd>iStart){
			problem=problem.substring(iStart+6,iEnd);
		}
		//cut off control chars
		this.problem = problem.trim();
	}
	
	public IAppView getCurrentView() {
		return currentView;
	}
	
	/**
	 * Save Problem to File
	 */
	public ByteArrayOutputStream saveFile() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ISetParser parser=new SetParser();
		String text=this.getActualHTMLProblem();
		if(parser.parse(text)==false)
		{
			JOptionPane.showMessageDialog(null, "Keine g�ltige Menge", "JAlgo(Unifikation) -Fehler", JOptionPane.ERROR_MESSAGE);
			notifyChange();
		}
		try {
			ObjectOutputStream objOut = new ObjectOutputStream(out);
			objOut.writeObject(text);
            out.close();
			return out;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Changes SaveState to CHANGES_TO_SAVE
	 */
	public void notifyChange(){
		ISetParser parser=new SetParser();
		String text=this.getActualHTMLProblem();
		if(parser.parse(text))
			moduleConnector.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
		else
			moduleConnector.setSaveStatus(SaveStatus.NOTHING_TO_SAVE);
	}
	
	
	/**
	 * Enables Saving
	 */
	public void enableSaving(){
		moduleConnector.setSavingBlocked(false);
	}
	
	/**
	 * Disables Saving
	 */
	public void disableSaving(){
		moduleConnector.setSavingBlocked(true);
		moduleConnector.setSaveStatus(SaveStatus.NOTHING_TO_SAVE);
	}
	/**
	 * Load Problem from File
	 */
	public void loadFile(ByteArrayInputStream data) {
		try {
			ObjectInputStream in = new ObjectInputStream(data);
			try {
				ISetParser parser=new SetParser();
				String text=(String) in.readObject();
				if(parser.parse(text))
				{
					//do this to force switch to editor
					this.state=ApplicationState.Algo;
					//switch
					this.setFinished(text);
					this.enableSaving();
				}
				else
					JOptionPane.showMessageDialog(null, "Keine g" + Constants.lowercasedUE + "ltige Menge", "JAlgo(Unifikation) -Fehler", JOptionPane.ERROR_MESSAGE);
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Installs the toolbar
	 */
	public void installToolbar() {
		undoAll = new JButton (new ImageIcon(Messages.getResourceURL("main", "Icon.Undo_all")));
		undoAll.setToolTipText("zur" + Constants.lowercasedUE + "ck zur Ausgangsmenge");
		undoBlockStep = new JButton (new ImageIcon(Messages.getResourceURL("main", "Icon.Undo_blockstep")));
		undoBlockStep.setToolTipText("Komplettschritt zur" + Constants.lowercasedUE + "ck");
		undoStep = new JButton (new ImageIcon(Messages.getResourceURL("main", "Icon.Undo_step")));
		undoStep.setToolTipText("Einzelschritt zur" + Constants.lowercasedUE + "ck");
		performStep = new JButton (new ImageIcon(Messages.getResourceURL("main", "Icon.Perform_step")));
		performStep.setToolTipText("Einzelschritt vor");
		performBlockStep = new JButton (new ImageIcon(Messages.getResourceURL("main", "Icon.Perform_blockstep")));
		performBlockStep.setToolTipText("Komplettschritt vor");
		performAll = new JButton (new ImageIcon(Messages.getResourceURL("main", "Icon.Perform_all")));
		performAll.setToolTipText("Durchl" + Constants.lowercasedAE + "uft Algorithmus bis zum Ende");

		toolbar.add(undoAll);
		toolbar.add(undoBlockStep);
		toolbar.add(undoStep);
		toolbar.add(performStep);
		toolbar.add(performBlockStep);
		toolbar.add(performAll);
		toolbar.addSeparator();
	}
	
	/**
	 * Enables the toolbar buttons
	 */
	public void enableToolbar(){
		undoAll.setEnabled(true);
		undoBlockStep.setEnabled(true);
		undoStep.setEnabled(true);
		performStep.setEnabled(true);
		performBlockStep.setEnabled(true);
		performAll.setEnabled(true);
	}
	
	/**
	 * Disables the toolbar buttons 
	 */
	public void disableToolbar(){
		undoAll.setEnabled(false);
		undoBlockStep.setEnabled(false);
		undoStep.setEnabled(false);
		performStep.setEnabled(false);
		performBlockStep.setEnabled(false);
		performAll.setEnabled(false);
	}

}
