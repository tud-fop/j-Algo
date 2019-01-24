package org.jalgo.module.em.control;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.Semaphore;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import org.jalgo.module.em.gui.output.diagram.DiagramPanel;
import org.jalgo.module.em.gui.output.table.TableViewPanel;

/**
 * Controls visual output and navigation through various em states. It can
 * switch views and activate presentation mode.
 * 
 * @Author Christian Hendel
 * @Author Tobias Nett
 * @Author Kilian Gebhardt
 */
public class MainController {

	private static final int HYBRID = 1;
	private static final int TABLE = 2;
	private static final int DIAGRAM = 0;
	private DiagramPanel diagram;
	private TableViewPanel table;
	private LogState logstate;
	private JComponent content;
	private JSplitPane hypanel = new JSplitPane();
	private boolean beamer;
	private JScrollPane tabframe;
	private int view;
	private PropertyChangeListener dividerSizeChangedListener; 
	
	//Semaphore and protected Variable
	private final Semaphore resizing = new Semaphore(1, false);
	private int windowHeight;
	
	
	/**
	 * Constructor for {@code MainController} class. Saves the values specified
	 * by {@code Controller} object.
	 * 
	 * @param logstate
	 *            reference to the {@code LogState} object
	 * @param content
	 *            the component that should hold the visible content
	 * @param beamer
	 *            specifies if presentation mode is active
	 */
	public MainController(LogState logstate, JComponent content, boolean beamer, Controller controller) {
		this.logstate = logstate;
		this.content = content;
		this.beamer = beamer;
		mainView();

	};

	/**
	 * Starts main output view for user to observe all dates. Prepares Diagram
	 * View and Table View.
	 */
	public void mainView() {
		diagram = new DiagramPanel(logstate);
		view = HYBRID;
		//MouseListerner catches MouseEvents when clicking 
		//on LikelihoodTable-Headers and resizes the SplitView
		table = new TableViewPanel(logstate, new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				hybridResize();
				super.mouseClicked(e);
			}
			
		});
		
		table.setBeamerMode(beamer);

		content.removeAll();
		table.update();
		diagram.upDate();
		tabframe = new JScrollPane(table);
		
		//Initialize View with Hybird View
		setUpHybrid();
		showHybrid();
		
		//ComponentListener, that Resizes Diagram and Table,
		//if Size of JAlgo was Changed
		hypanel.addComponentListener(new ComponentListener() {
			@Override
			public void componentResized(ComponentEvent e) {
				if (resizing.tryAcquire()) {
					hybridResize();
					windowHeight = hypanel.getHeight();
					resizing.release();
				}
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
	};

	/**
	 * one em step forward,updating all views
	 */
	public void stepForward() {
		logstate.stepForward();
		diagram.upDate();
		table.update();
		content.updateUI();

	};

	/**
	 * steps one em step backwards, updating all views
	 */
	public void stepBack() {
		logstate.stepBackward();
		diagram.upDate();
		table.update();
	};

	/**
	 * Performs a single step forward. Calculates the needed data and updates
	 * all related views.
	 */
	public void singleStepForward() {
		logstate.singleStepForward();
		diagram.upDate();
		table.update();
		table.update();
	}

	/**
	 * Performs a single step backward. Calculates the needed data and updates
	 * all related views.
	 */
	public void singleStepBackward() {
		logstate.singleStepBackward();
		diagram.upDate();
		table.update();
		table.update();
	}

	/**
	 * shows Hybrid view(diagram and Table)
	 */
	public void showHybrid() {
		view = HYBRID;
		table.setViewLike();
		hybridResize();
	};
	
	public void setUpHybrid(){
		hypanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, diagram, tabframe);
		hybridResize();
		hypanel.setOneTouchExpandable(true);

		dividerSizeChangedListener = new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				if (resizing.tryAcquire()){
					if (arg0.getPropertyName().equals("dividerLocation") && windowHeight == hypanel.getHeight()){
						if ((Integer) arg0.getNewValue() == 1) {
							view = TABLE;
						} else if ((Integer) arg0.getNewValue() > windowHeight - hypanel.getDividerSize() - 5) {
							view = DIAGRAM;
						} else if ((view == TABLE && ((Integer) arg0.getNewValue() > (Integer) arg0.getOldValue()))
								|| (view == DIAGRAM && ((Integer) arg0.getNewValue() < (Integer) arg0.getOldValue()))) {
							view = HYBRID;
						}
					}
					resizing.release();
				}
			}
		};
		hypanel.addPropertyChangeListener(dividerSizeChangedListener);

		content.add(hypanel);
		content.updateUI();
	}

	/**
	 * shows diagram only
	 */
	public void showDiagram() {
		view = DIAGRAM;
		hypanel.setDividerLocation(1.0);
	};

	/**
	 * shows table only
	 */
	public void showTable() {
		view = TABLE;
		if (logstate.getEvents().size() > 10) {
			table.setViewLikeSingle();
		} else {
			table.setViewLikeSinglePart();
		}
		hypanel.setDividerLocation(0.0);
	};

	/**
	 * Toggles the presentation mode. Updates all views that provide a
	 * presentation mode.
	 */
	public void changePresentationMode() {
		beamer = !beamer;
		table.setBeamerMode(beamer);
		table.update();
		hybridResize();
	}
	
	public void updateViews(){
		table.update();
		diagram.upDate();
	}
	
	/**
	 * Sizes the TablePanel to min{50% of the heights;
	 * preferred size in which every L-Table can be displayed}.
	 * @author kilian
	 */
	private void hybridResize() {
		//Set the number of TableColumns
		if (view != DIAGRAM) {
			if (content.getWidth() <= 900) {
				table.setShownum(2);
				table.update();
			} else if (content.getWidth() < 1043) {
				table.setShownum(3);
				table.update();
			} else {
				table.setShownum(4);
				table.update();
			}
		}
		if (view == HYBRID) {
			if (tabframe.getPreferredSize().height - hypanel.getDividerSize() > content.getSize().height / 2) {
				hypanel.setDividerLocation(0.5);
			} else {
				//If the Window is smaller than 1110, a horizontal Scrollbar 
				//is needed: this consumes extra space
				int scroll_offset = 0;
				if (hypanel.getSize().width < 1110) {
					scroll_offset = 15;
				}
				hypanel.setDividerLocation(content.getSize().height
						- tabframe.getPreferredSize().height
						- hypanel.getDividerSize()
						- scroll_offset
						- 2);
			}
		} else if (view == TABLE) {
			hypanel.setDividerLocation(0.0);
		} else {
			hypanel.setDividerLocation(1.0);
		}
	diagram.resetButtons();
	}
}
