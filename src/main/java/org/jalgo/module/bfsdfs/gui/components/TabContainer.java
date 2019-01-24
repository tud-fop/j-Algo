package org.jalgo.module.bfsdfs.gui.components;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.jalgo.main.util.Messages;
import org.jalgo.module.bfsdfs.algorithms.BFS;
import org.jalgo.module.bfsdfs.algorithms.DFS;
import org.jalgo.module.bfsdfs.controller.GraphController;
import org.jalgo.module.bfsdfs.gui.ComponentUtility;
import org.jalgo.module.bfsdfs.gui.GUIConstants;
import org.jalgo.module.bfsdfs.gui.GUIController;
import org.jalgo.module.bfsdfs.gui.StatusMouseAdapter;
import org.jalgo.module.bfsdfs.gui.event.BeamerAction;

/**
 * This class represents a container. It consists of three windows which can be
 * shown by clicking on their corresponding buttons. The three windows are:
 * <ul>
 * <li>{@linkplain DesignTab}: design and edit a graph</li>
 * <li>{@linkplain BFSTab}: visualize the breadth first search algorithm</li>
 * <li>{@linkplain DFSTab}: visualize the depth first search algorithm</li>
 * </ul>
 * There is also a beamer mode button available.
 * 
 * @author Anselm Schmidt,
 * @author Florian Dornbusch
 */
public class TabContainer
extends JPanel
implements GUIConstants {
	private static final long serialVersionUID = -5942603844775439386L;
	
	// general references
	private GUIController gui;
	private DesignTab designTab;
	private BFSTab bfsTab;
	private DFSTab dfsTab;
	
	// components
	private JPanel tabArea;
	private JPanel buttonArea;
	private JPanel leftButtons;
	private JPanel rightButtons;	
	private JToggleButton designButton;
	private JToggleButton bfsButton;
	private JToggleButton dfsButton;
	private JToggleButton beamerButton;
	private CardLayout layout;
	private Action beamerAction;
	
	/**
	 * Creates the Tab Container and its components.
	 * @author Florian Dornbusch
	 * @author Anselm Schmidt
	 */
	public TabContainer(GUIController gui, BFS bfs, DFS dfs,
			GraphController graphController) {
		
		this.gui = gui;
		
		// create the tabs
		designTab = new DesignTab(gui, graphController);
		dfsTab = new DFSTab(gui, graphController, dfs);
		bfsTab = new BFSTab(gui, graphController, bfs);
		
		// create Panels
		buttonArea = new JPanel();
		tabArea = new JPanel();
		leftButtons = new JPanel();
		rightButtons = new JPanel();
		
		// create the buttons
		designButton = setupButton("designButton");
		designButton.setSelected(true);
		bfsButton = setupButton("bfsButton");
		dfsButton = setupButton("dfsButton");
		beamerAction = new BeamerAction(gui);
		beamerButton = setupButton("beamerButton");
		
		// add the tab buttons to the left
		leftButtons.add(designButton);
		leftButtons.add(dfsButton);
		leftButtons.add(bfsButton);
		
		// add the beamer button to the right
		rightButtons.add(beamerButton);
		
		// add buttons to the area
		buttonArea.setLayout(new BorderLayout());
		buttonArea.setPreferredSize(new Dimension(0,40));
		buttonArea.add(leftButtons, BorderLayout.WEST);
		buttonArea.add(rightButtons, BorderLayout.EAST);
		
		// set up the tab area
		layout = new CardLayout();
		tabArea.setLayout(layout);
		tabArea.add(designTab, "designTab");
		tabArea.add(bfsTab, "bfsTab");
		tabArea.add(dfsTab, "dfsTab");
		
		// add the areas to the container
		setLayout(new BorderLayout());
		add(buttonArea, BorderLayout.NORTH);
		add(tabArea, BorderLayout.CENTER);
			
		toggleBeamerMode();
		
		handleEvents();
	}	
	
	/**
	 * Returns the {@linkplain DesignTab}.
	 * @author Florian Dornbusch
	 */
	public DesignTab getDesignTab() {
		return designTab;
	}
	
	/**
	 * Returns the {@linkplain BFSTab}.
	 * @author Florian Dornbusch
	 */
	public BFSTab getBFSTab() {
		return bfsTab;
	}
	
	/**
	 * Returns the {@linkplain DFSTab}.
	 * @author Florian Dornbusch
	 */
	public DFSTab getDFSTab() {
		return dfsTab;
	}
	
	/**
	 * Returns the used {@linkplain BeamerAction}.
	 * @author Florian Dornbusch
	 */
	public Action getBeamerAction() {
		return beamerAction;
	}
	
	/**
	 * This method changes the content of this component depending on
	 * {@linkplain ComponentUtility#BEAMER_MODE}
	 * @author Florian Dornbusch
	 */
	public void toggleBeamerMode() {
		Font font;
		if(ComponentUtility.BEAMER_MODE) font = BEAMER_WRITING_FONT;
		else font = WRITING_FONT;
		designButton.setFont(font);
		bfsButton.setFont(font);
		dfsButton.setFont(font);
		beamerButton.setFont(font);
	}
	
	
	private void handleEvents() {
		designButton.addActionListener(new ActionListener() {
			/**
			 * This method is invoked if the user clicks on the design button.
			 * The position of the viewport in the old tab is used to set the
			 * new viewport properly.
			 * @author Florian Dornbusch
			 */
			public void actionPerformed(ActionEvent event) {
				layout.show(tabArea, "designTab");
				
				if(bfsButton.isSelected())
					bfsTab.saveViewportPosition();
				else dfsTab.saveViewportPosition();
				designTab.loadViewportPosition();
				
				designButton.setSelected(true);
				dfsButton.setSelected(false);
				bfsButton.setSelected(false);
				
				gui.switchToDesign();
			}	
		});
		
		dfsButton.addActionListener(new ActionListener() {
			/**
			 * This method is called if the user clicks on the DFS button.
			 * The position of the viewport of the old tab is used to set the
			 * new viewport properly.
			 * @author Florian Dornbusch
			 */
			public void actionPerformed(ActionEvent event) {
				layout.show(tabArea, "dfsTab");
				
				if(designButton.isSelected())
					designTab.saveViewportPosition();
				else bfsTab.saveViewportPosition();
				dfsTab.loadViewportPosition();
				
				designButton.setSelected(false);
				dfsButton.setSelected(true);
				bfsButton.setSelected(false);
				
				gui.switchToDFS();
			}
		});
		
		bfsButton.addActionListener(new ActionListener() {
			/**
			 * This method is called if the user clicks on the BFS button.
			 * The position of the viewport of the old tab is used to set the
			 * new viewport properly.
			 * @author Florian Dornbusch
			 */
			public void actionPerformed(ActionEvent event) {
				layout.show(tabArea, "bfsTab");
				
				if(designButton.isSelected())
					designTab.saveViewportPosition();
				else dfsTab.saveViewportPosition();
				bfsTab.loadViewportPosition();
				
				designButton.setSelected(false);
				dfsButton.setSelected(false);
				bfsButton.setSelected(true);
				
				gui.switchToBFS();
			}
		});
	}
	
	/**
	 * Creates a new button for the <code>TabContainer</code>.
	 * @param name : The name of the corresponding entry in the properties
	 * 				 files.
	 * @return The created button.
	 * @author Florian Dornbusch
	 */
	private JToggleButton setupButton(String name) {
		JToggleButton b = new JToggleButton();
		if(name == "beamerButton") b.setAction(beamerAction);
		b.setText(Messages.getString("bfsdfs",
				"tab."+name));
		b.addMouseListener(new StatusMouseAdapter(
				"tab."+name+"_tooltip"));
		b.setToolTipText(Messages.getString("bfsdfs",
				"tab."+name+"_tooltip"));
		b.setIcon(new ImageIcon(Messages.getResourceURL("bfsdfs",
				"tab."+name)));
		return b;
	}
}