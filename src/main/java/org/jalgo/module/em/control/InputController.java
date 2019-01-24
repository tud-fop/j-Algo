package org.jalgo.module.em.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import org.jalgo.module.em.data.StartParameters;
import org.jalgo.module.em.gui.input.P0InputPanel;
import org.jalgo.module.em.gui.input.PartitionPanel;
import org.jalgo.module.em.gui.input.XInputPanel;
import org.jalgo.module.em.gui.input.YFrequencyPanel;

/**
 * Class <code>InputController</code> is a controller for input screens used to
 * collect data to create <code>StartParameters</code>. It is able to load and
 * save presets and finally initializes the main calculations.
 * 
 * @author Christian Hendel, Tobias Nett
 * 
 */
public class InputController {

	private JComponent content;
	private Controller control;

	private StartParameters params;

	// input views
	private XInputPanel xInputView;
	private PartitionPanel partitionView;
	private YFrequencyPanel yView;
	private P0InputPanel p0View;
	private boolean beamer;

	/**
	 * Creates a new <code>InputControlloer</code> object that handles input
	 * views and user actions.
	 * 
	 * @param content
	 *            the main GUI component of JAlgo
	 * @param controller
	 *            main controller of this j-Algo module
	 * @param params
	 *            <code>StartParameters</code> object whose values are to be
	 *            edited
	 * @param presMode
	 *            the current state of presentation mode
	 */

	public InputController(final JComponent content,
			final Controller controller, StartParameters params,
			boolean presMode) {
		this.content = content;
		this.control = controller;
		this.params = params;
		this.beamer = presMode;
		createXInputView();
	}

	/**
	 * Creates a new <code>YFrequencyPanel</code> object and adds action
	 * listeners to the back/forward buttons. The created panel is directly
	 * shown in the content pane.
	 */
	public void createFrequencyView() {
		yView = new YFrequencyPanel(params, beamer);

		yView.getForwardButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createP0View();
			}
		});
		yView.getBackButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setView(partitionView);
			}
		});
		setView(yView);
	}

	/**
	 * Creates a new <code>P0InputPanel</code> object and adds action listeners
	 * to the back/forward buttons. The created panel is directly shown in the
	 * content pane.
	 */
	public void createP0View() {
		p0View = new P0InputPanel(params, beamer);

		p0View.getForwardButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				p0View.writeP0ToStartParameters();
				// Creates a new Window, in which a TestCalculation printed
				/*
				org.jalgo.module.testModule.ConsoleCalculation.outputCalcSteps(
						params, org.jalgo.module.testModule.ConsoleCalculation
								.createConsoe(), 20);
				*/
				control.output();
			}
		});
		p0View.getBackButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				p0View.writeTempP0ToStartParameters();
				setView(yView);
			}
		});
		setView(p0View);
	}

	/**
	 * Creates a new <code>PartitionPanel</code> object and adds action
	 * listeners to the back/forward buttons. The created panel is directly
	 * shown in the content pane.
	 */
	public void createPartitionView() {
		partitionView = new PartitionPanel(params, beamer);

		partitionView.getForwardButton().addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						partitionView.writeObservation();
						createFrequencyView();
					}
				});
		partitionView.getBackButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setView(xInputView);
			}
		});
		// content.add(partitionView, BorderLayout.CENTER);
		setView(partitionView);
	}

	/**
	 * Creates a new <code>XInputPanel</code> object and adds action listeners
	 * to the forward button. The created panel is directly shown in the content
	 * pane.
	 */
	public void createXInputView() {
		xInputView = new XInputPanel(params, beamer);
		xInputView.getForwardButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				xInputView.writeExperiment();
				createPartitionView();
			}
		});
		setView(xInputView);
	}

	/**
	 * Returns the current <code>StartParameters</code> object.
	 * 
	 * @return returns the current <code>StartParameters</code> object
	 */
	public StartParameters getParams() {
		return params;
	}

	/**
	 * Returns {@code true} if the module runs in presentation mode,
	 * {@code false} otherwise.
	 * 
	 * @return {@code true} if the module is currently in presentation mode,
	 *         {@code false} otherwise.
	 */
	public boolean isBeamer() {
		return beamer;
	}

	/**
	 * Starts presentation mode on {@code true} argument, ends presentation mode
	 * on {@code false} argument. The change takes effect on every view of this
	 * module.
	 * 
	 * @param beamer
	 *            {@code true} to start presentation mode, {@code false} to end
	 *            presentation mode.
	 */
	public void setBeamer(boolean beamer) {
		this.beamer = beamer;

		if (xInputView != null)
			xInputView.setBeamerMode(this.beamer);
		if (partitionView != null)
			partitionView.setBeamerMode(this.beamer);
		if (yView != null)
			yView.setBeamerMode(this.beamer);
		if (p0View != null) {
			p0View.setBeamerMode(this.beamer);
		}

	}

	/**
	 * Sets the view to the specified component. <code>view</code> should extend
	 * <code>JPanel</code> and implement <code>InputPanel</code>, but this is
	 * not necessary.
	 * <p>
	 * At first, all other components are removed from the content pane, then
	 * <code>view</code> is added and the UI is updated.
	 * 
	 * @param view
	 *            the <code>JComponent</code> that should be displayed
	 */
	private void setView(final JComponent view) {
		content.removeAll();
		content.add(view);
		content.updateUI();
	}
	
	public void backToInputView() {
		setView(p0View);
	}
}
