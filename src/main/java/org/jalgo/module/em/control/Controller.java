package org.jalgo.module.em.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

import org.jalgo.main.gui.components.JToolbarButton;
import org.jalgo.main.util.Messages;
import org.jalgo.module.em.data.StartParameters;
import org.jalgo.module.em.gui.StartScreen;

/**
 * Main controller for the JAlgo module for the EM-Algorithm. The controller
 * handles the two view controllers and prepares menus and tool bars for user
 * actions.
 * 
 * @author Christian Hendel
 */
public class Controller {
	
	private OptionPane optionbox;
	private MainController output;
	private InputController input;
	private LogState logstate;
	private StartParameters params;
	private JComponent content;
	private JToolBar toolbar;
	private JMenu menu;
	private JToolbarButton wback, back, forw, wforw, save, load, presentation,
			preferences,home;
	private ActionListener lwback, lback, lforw, lwforw, lhyview, ldiaview,
			ltabview, llogmode, lsave, lload, lbeamer, lprefs,lhome;
	private JMenuItem HyView, DiaView, TabView, logmode;
	private JCheckBoxMenuItem beamer;
	private IOController IO = new IOController();
	private StartScreen startscreen;
	private ImageIcon beamerOff = new ImageIcon(Messages.getResourceURL("em",
			"ToolBarIcon.beamerOff"));
	private ImageIcon beamerOn = new ImageIcon(Messages.getResourceURL("em",
			"ToolBarIcon.beamerOn"));
	private boolean presMode;
	private double limit = -1;
	
	private JLabel stepLabel = new JLabel();

	/**
	 * Creates a controller and sets action listener for all buttons.
	 * 
	 * @param content
	 *            the main panel which should hold the content
	 * @param menu
	 *            the pull-down menu
	 * @param toolbar
	 *            the JAlgo tool bar
	 */
	public Controller(JComponent content, JMenu menu, JToolBar toolbar) {
		params = new StartParameters();
		presMode = false;
		this.menu = menu;
		this.content = content;
		this.toolbar = toolbar;
		lhome = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				home.setEnabled(true);
				logmode.setEnabled(false);
				HyView.setEnabled(false);
				wback.setEnabled(false);
				back.setEnabled(false);
				forw.setEnabled(false);
				wforw.setEnabled(false);
				DiaView.setEnabled(false);
				TabView.setEnabled(false);
				save.setEnabled(false);
				preferences.setEnabled(false);
				stepLabel.setVisible(false);
				input.backToInputView();
			}
		};
		lprefs = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				options();
			}
		};
		lwback = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				output.stepBack();
				stepLabel.setText(logstate.getStepCount()+"."+logstate.getSingleStepCount());
			}
		};
		lback = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				output.singleStepBackward();
				stepLabel.setText(logstate.getStepCount()+"."+logstate.getSingleStepCount());
			}
		};
		lforw = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				output.singleStepForward();
				stepLabel.setText(logstate.getStepCount()+"."+logstate.getSingleStepCount());
			}
		};
		lwforw = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				output.stepForward();
				stepLabel.setText(logstate.getStepCount()+"."+logstate.getSingleStepCount());
			}
		};
		lhyview = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				output.showHybrid();
			}
		};
		ldiaview = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				output.showDiagram();
			}
		};
		ltabview = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				output.showTable();
			}
		};
		llogmode = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (logstate.isLogarithmicLikelihoodRepresentationEnabled()) {
					logstate.setLogarithmicLikelihoodRepresentation(false);
				} else {
					logstate.setLogarithmicLikelihoodRepresentation(true);
				}
				output.updateViews();
			}
		};
		lsave = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IO.write(params);

			}
		};
		lload = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				params = IO.read();
				if (params != null) {
					setStartParameters(params);
					home.setEnabled(false);
					logmode.setEnabled(false);
					HyView.setEnabled(false);
					wback.setEnabled(false);
					back.setEnabled(false);
					forw.setEnabled(false);
					wforw.setEnabled(false);
					DiaView.setEnabled(false);
					TabView.setEnabled(false);
					save.setEnabled(false);
					load.setEnabled(true);
					preferences.setEnabled(false);
					input();
				}
			}
		};
		lbeamer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				presMode = !presMode;
				if (input != null) {
					input.setBeamer(!input.isBeamer());
				}
				if (output != null) {
					output.changePresentationMode();
				}
				if (presMode) {
					presentation.setIcon(beamerOn);
				} else {
					presentation.setIcon(beamerOff);
				}
				beamer.setSelected(presMode);
				getToolbar().updateUI();
			}
		};

		init();
		
		 optionbox = new OptionPane(this, stepLabel);
	}

	/**
	 * initializes the menu bar and tool bar and assigns action listener to all
	 * items.
	 */
	public void init() {
		clear();
		save = new JToolbarButton(new ImageIcon(Messages.getResourceURL("em", "ToolBarIcon.save")),
				Messages.getString("em", "ToolBar.save"), "");
		toolbar.add(save);
		load = new JToolbarButton(new ImageIcon(Messages.getResourceURL("em", "ToolBarIcon.load")),
				Messages.getString("em", "ToolBar.load"), "");
		toolbar.add(load);
		toolbar.addSeparator();

		preferences = new JToolbarButton(new ImageIcon(Messages.getResourceURL(
				"em", "ToolBarIcon.run")), Messages.getString("em",
				"ToolBar.run"), "");
		toolbar.add(preferences);

		wback = new JToolbarButton(new ImageIcon(Messages.getResourceURL("em",
				"ToolBarIcon.wback")),
				Messages.getString("em", "ToolBar.wback"), "");
		toolbar.add(wback);

		back = new JToolbarButton(new ImageIcon(Messages.getResourceURL("em",
				"ToolBarIcon.back")), Messages.getString("em", "ToolBar.back"),
				"");
		toolbar.add(back);

		forw = new JToolbarButton(new ImageIcon(Messages.getResourceURL("em",
				"ToolBarIcon.forw")), Messages.getString("em", "ToolBar.forw"),
				"");
		toolbar.add(forw);
		
		home = new JToolbarButton(new ImageIcon(Messages.getResourceURL("em",
				"ToolBarIcon.home")), Messages.getString("em", "ToolBar.home"),
				"");

		wforw = new JToolbarButton(new ImageIcon(Messages.getResourceURL("em",
				"ToolBarIcon.wforw")),
				Messages.getString("em", "ToolBar.wforw"), "");
		toolbar.add(wforw);
		toolbar.addSeparator();
		toolbar.add(home);
		toolbar.addSeparator();
		presentation = new JToolbarButton(beamerOff, Messages.getString("em",
				"Controller.togglePMode"), null);
		toolbar.add(presentation);
		toolbar.addSeparator();
		toolbar.add(Box.createHorizontalGlue());
		toolbar.add(stepLabel);
		toolbar.add(Box.createHorizontalStrut(10));
		
		// add action listeners
		wback.addActionListener(lwback);
		wforw.addActionListener(lwforw);
		save.addActionListener(lsave);
		load.addActionListener(lload);
		presentation.addActionListener(lbeamer);
		forw.addActionListener(lforw);
		back.addActionListener(lback);
		preferences.addActionListener(lprefs);
		home.addActionListener(lhome);
		
		// init menu entries
		HyView = new JMenuItem(Messages.getString("em", "Menu.hybrid"));
		HyView.addActionListener(lhyview);
		DiaView = new JMenuItem(Messages.getString("em", "Menu.diagram"));
		DiaView.addActionListener(ldiaview);
		TabView = new JMenuItem(Messages.getString("em", "Menu.table"));
		TabView.addActionListener(ltabview);
		logmode = new JMenuItem(Messages.getString("em", "Menu.log"));
		logmode.addActionListener(llogmode);
		beamer = new JCheckBoxMenuItem(Messages.getString("em", "Menu.presentation"));
		beamer.addActionListener(lbeamer);
		// add menu items to the dropdown menu
		menu.add(HyView);
		menu.add(DiaView);
		menu.add(TabView);
		menu.add(logmode);
		menu.add(beamer);
		// init button states
		home.setEnabled(false);
		logmode.setEnabled(false);
		HyView.setEnabled(false);
		wback.setEnabled(false);
		back.setEnabled(false);
		forw.setEnabled(false);
		wforw.setEnabled(false);
		DiaView.setEnabled(false);
		TabView.setEnabled(false);
		save.setEnabled(false);
		load.setEnabled(true);
		preferences.setEnabled(false);
		// init start screen and add it to the panel
		startscreen = new StartScreen(this, IO, params);
		content.add(startscreen);
	};

	/**
	 * Clears the content panel and updates the UI.
	 */
	public void clear() {
		content.removeAll();
		content.updateUI();
	};

	/**
	 * Starts the first input view to offer the user the ability to enter new
	 * data.
	 */
	public void input() {
		// params = startscreen.getSparams();options
		clear();
		stepLabel.setVisible(false);
		input = new InputController(content, this, params, presMode);
	};

	/**
	 * Starts the output process of visualization the EM experiment.
	 */
	public void output() {
		logstate = new LogState(params);
		
		
		preferences.setEnabled(true); 
		save.setEnabled(true);
		logmode.setEnabled(true);
		HyView.setEnabled(true);
		wback.setEnabled(true);
		back.setEnabled(true);
		forw.setEnabled(true);
		wforw.setEnabled(true);
		DiaView.setEnabled(true);
		TabView.setEnabled(true);
		home.setEnabled(true);


		clear();

		if (input != null) {
			output = new MainController(logstate, content, input.isBeamer(),this);
		} else {
			output = new MainController(logstate, content, false,this);
		}
		stepLabel.setText(logstate.getStepCount()+"."+logstate.getSingleStepCount());
		stepLabel.setVisible(true);
	};

	public JToolBar getToolbar() {
		return toolbar;
	}

	/**
	 * Overwrites the current <code>StartParameters</code> Object.
	 * 
	 * @param startParameters
	 *            the new <code>StartParameters</code> Object
	 */
	public void setStartParameters(StartParameters startParameters) {
		this.params = startParameters;
	}
	public void options(){

	    optionbox.setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE );
	    optionbox.setSize( 450, 150 );
	    optionbox.setVisible( true );
	}
	public void setLimit(double d){ 
		this.limit=d;
	}
	
	public double getLimit(){return limit;}
	
	public LogState getLogState(){return logstate;}
	
	public MainController getMC(){return output;}
	
	
}
