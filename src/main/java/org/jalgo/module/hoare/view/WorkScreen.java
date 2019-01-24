package org.jalgo.module.hoare.view;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Settings;
import org.jalgo.module.hoare.view.util.SplitPaneWithWorkingDividerLocationSetter;

/**
 * This is the master Panel of the workscreen which keeps the smaller Panels within.
 * It also edits the size relations between the Panels.
 * 
 * @author Tomas, Antje, Johannes
 *
 */
public class WorkScreen extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;
	
	/**
	 * The gui controller.
	 */
	protected final View gui;
	/**
	 * Shows all rules.
	 */
	private WSRule rules;
	/**
	 * Shows the verification tree.
	 */
	private WSGraph graph;
	/**
	 * Shows the source code.
	 */
	private WSSource source;
	/**
	 * Shows a list of all variables.
	 */
	private WSVar var;
	/**
	 * Seperates the <code>graph</code> from the <code>rules</code>, the <code>source</code> and the <code>var</code>.
	 */
	private JSplitPane mainSplitter, rightUpndownSplitter, rightLeftRightSplitter;
	private boolean init = true;
 /**
  * Represents the visibility of the mainSplitter
  */
	private boolean isVisible = true;
 /**
  * saves the relation of the mainSplitter
  */
	private double mainRelation = 0.8;
	
	/**
	 * opens the JPanel
	 * @param gui
	 */
	public WorkScreen(View gui) {
		super();
		this.gui = gui;
		
		this.rules = new WSRule(gui);
		this.graph = new WSGraph(gui);
		this.source = new WSSource(gui);
		this.var = new WSVar(gui);
		
		this.setLayout(new BorderLayout());
		
		rightLeftRightSplitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rules, source);
		rightUpndownSplitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT, rightLeftRightSplitter, var);
		
		// width of splitter-line
		rightLeftRightSplitter.setDividerSize(3);
		rightUpndownSplitter.setDividerSize(3);

		// position of splitter-line
		int leftRightLocation = 240;
		int upndownLocation = 195;
		try {
			leftRightLocation = Integer.valueOf(Settings.getString("hoare", "workScreen.rightLeftRightSplitterLocation"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		try {
			upndownLocation = Integer.valueOf(Settings.getString("hoare", "workScreen.rightUpndownSplitterLocation"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		rightUpndownSplitter.setDividerLocation(upndownLocation);
		rightLeftRightSplitter.setDividerLocation(leftRightLocation);
		
		rightLeftRightSplitter.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange (PropertyChangeEvent changeEvent) {
				if (changeEvent.getPropertyName().equals(JSplitPane.DIVIDER_LOCATION_PROPERTY)) {
					int location = rightLeftRightSplitter.getDividerLocation();
					try {
						Settings.setString("hoare", "workScreen.rightLeftRightSplitterLocation", String.valueOf(location));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		rightUpndownSplitter.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange (PropertyChangeEvent changeEvent) {
				if (changeEvent.getPropertyName().equals(JSplitPane.DIVIDER_LOCATION_PROPERTY)) {
					int location = rightUpndownSplitter.getDividerLocation();
					try {
					 Settings.setString("hoare", "workScreen.rightUpndownSplitterLocation", String.valueOf(location));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		

		mainSplitter = new SplitPaneWithWorkingDividerLocationSetter(JSplitPane.HORIZONTAL_SPLIT, this.graph, rightUpndownSplitter);
		
		mainSplitter.setDividerSize(3);
		
		this.add(mainSplitter);
		
		
		double relation = 0.8;
		try {
			relation = Double.valueOf(Settings.getString("hoare", "workScreen.mainSplitterRelation"));//0.8;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		mainSplitter.setDividerLocation(relation);
		mainSplitter.setResizeWeight(relation);
		
		int graphweite = (int)Math.round(JAlgoGUIConnector.getInstance().getModuleComponent(gui.getMC()).getSize().getWidth()*(relation-0.1));
		int graphhoehe = (int)Math.round(JAlgoGUIConnector.getInstance().getModuleComponent(gui.getMC()).getSize().getHeight());
		graph.setSize(graphweite, graphhoehe);
		
		mainSplitter.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange (PropertyChangeEvent changeEvent) {
				if (changeEvent.getPropertyName().equals(JSplitPane.DIVIDER_LOCATION_PROPERTY)) {
					double relation = (Double.valueOf(mainSplitter.getDividerLocation()))/(mainSplitter.getWidth()-mainSplitter.getDividerSize());
					try {
						if (isVisible){
							//Sets this property only if the Splitter is visible
						 Settings.setString("hoare", "workScreen.mainSplitterRelation", String.valueOf(relation));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	
		init = false;
	}
	
	/**
	 * Rules Screen Getter
	 * @return the rules
	 */
	public WSRule getRules() {
		return rules;
	}

	/**
	 * Rules Screen Setter
	 * @param rules the rules to set
	 */
	public void setRules(WSRule rules) {
		this.rules = rules;
	}

	/**
	 * Graph Screen Getter
	 * @return the graph
	 */
	public WSGraph getGraph() {
		return graph;
	}

	/**
	 * Graph Screen Setter
	 * @param graph
	 */
	public void setGraph(WSGraph graph) {
		this.graph = graph;
	}

	/**
	 * Source Screen Getter
	 * @return the source
	 */
	public WSSource getSource() {
		return source;
	}

	/**
	 * Source Screen Setter
	 * @param source 
	 */
	public void setSource(WSSource source) {
		this.source = source;
	}

	/**
	 * Variable Screen Getter
	 * @return the var
	 */
	public WSVar getVar() {
		return var;
	}

	/**
	 * Variable Screen Setter
	 * @param var 
	 */
	public void setVar(WSVar var) {
		this.var = var;
	}
	/**
	 * update method to give events from view to Screens below (Variable, Source, Graph, Rules Screen)
	 */
	public void update(Observable o, Object arg) {
		try {
   getRules().update(o, arg);
		}
		catch (RuntimeException e) {
			e.printStackTrace();
		}
		try {
			getSource().update(o, arg);
		}
		catch (RuntimeException e) {
			e.printStackTrace();
		}
		try {
			getVar().update(o, arg);
		}
			catch (RuntimeException e) {
			e.printStackTrace();
		}
		try {
			getGraph().update(o, arg);
		}
		catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates the font in the Screens below (Variable, Source, Graph, Rules Screen).
	 */
	public void updateFont() {
		if (!init) {
			getSource().updateFont();
			getVar().updateFont();
			getRules().updateFont();
			getGraph().updateFont();
		}
	}
	
	/**
	 * This Method changes the visibility of the 
	 * Rules, Source and Variable view by toggling
	 * between visible and invisible.<br>
	 * Note: This Method saves the old position
	 * of the Splitter and resets this position.
	 */
	public void toggleSplitter(){
		isVisible=!isVisible;
		rightUpndownSplitter.setVisible(isVisible);
		if (isVisible){
			mainSplitter.setDividerLocation(mainRelation);
			mainSplitter.setResizeWeight(mainRelation);
		} else {
			mainRelation=(Double.valueOf(mainSplitter.getDividerLocation()))/(mainSplitter.getWidth()-mainSplitter.getDividerSize());
			mainSplitter.setDividerLocation(0);
		}
		//UpdateBorderLayout
		this.validate();
	}
	
}



