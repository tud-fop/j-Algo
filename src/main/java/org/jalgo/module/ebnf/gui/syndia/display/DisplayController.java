package org.jalgo.module.ebnf.gui.syndia.display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jalgo.main.AbstractModuleConnector.SaveStatus;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.MainController;
import org.jalgo.module.ebnf.ModuleConnector;
import org.jalgo.module.ebnf.gui.syndia.AbstractSDGuiController;
import org.jalgo.module.ebnf.model.syndia.SynDiaSystem;
import org.jalgo.module.ebnf.renderer.WordAlgoRenderer;

/**
 * This class simply controls the display of a syntax diagram system as well as
 * saving a definition.
 * 
 * @author Andre Viergutz
 */
public class DisplayController extends AbstractSDGuiController {

	private MainController maincontroller;
	private ModuleConnector connector;
	protected SynDiaSystem synDiaSystem;
	protected JPanel contentPane;

	/**
	 * This constructor initializes alle the GuiComponents
	 * 
	 * @param controller The TransController which created this
	 * @param connector The ModuleConnector for saving actions
	 * @param contentP The pane to draw on
	 * @param sds The <code>SynDiaSystem</code> to draw
	 */

	public DisplayController(MainController controller,
			ModuleConnector connector, JPanel contentP, SynDiaSystem sds) {

		this.maincontroller = controller;
		this.connector = connector;
		this.contentPane = contentP;
		this.synDiaSystem = sds;

		drawScrollPane = new JScrollPane();
		drawPanel = new DrawPanel(synDiaSystem, this, new WordAlgoRenderer());
		drawPanel.addComponentListener(new ComponentAdapter() {

			public void componentMoved(ComponentEvent e) {

				drawPanel.repaint();

			}

		});

		controlPanel = new ControlPanel(this, this.connector);

		// setting
		contentPane.setBackground(Color.WHITE);
		drawScrollPane.setBackground(Color.WHITE);
		drawScrollPane.setViewportBorder(javax.swing.BorderFactory
				.createLoweredBevelBorder());
		drawScrollPane.setViewportView(drawPanel);
		drawScrollPane.setPreferredSize(new Dimension(Short.MAX_VALUE,
				contentPane.getHeight()
						- controlPanel.getPreferredSize().height));
		drawScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, Messages.getString("ebnf", "Border_Draw"),
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Tahoma", 0, 12)));

		controlPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, controlPanel
				.getPreferredSize().height));

		contentPane.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				drawScrollPane.setPreferredSize(new Dimension(Short.MAX_VALUE,
						contentPane.getHeight()
								- controlPanel.getPreferredSize().height));
			}
		});

		switchToDisplay();

	}

	/**
	 * Builds the Gui for the display of a <code>SynDiaSystem</code>
	 */
	public void switchToDisplay() {

		// layout
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));// layout);

		contentPane.add(controlPanel);
		contentPane.add(drawScrollPane);

		contentPane.validate();
		contentPane.repaint();

		connector.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);

	}

	/**
	 * Switches to the syntax diagram editor
	 */
	public void switchToSynDiaInput() {

		synDiaSystem.fillWithNullElems();
		maincontroller.setSynDiaInputMode(synDiaSystem);

	}

	/**
	 * Switches to the word algorithm
	 */
	public void setWordAlgoMode() {

		maincontroller.setWordAlgoMode(synDiaSystem);

	}

	/** Saves a <code>SynDiaSystem</code>
	 * @return the ByteArrayOutputStream of the saved system
	 */
	public ByteArrayOutputStream saveSystem() {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objOut = new ObjectOutputStream(out);
			objOut.writeBoolean(false);
			objOut.writeObject(synDiaSystem);
			objOut.close();
			connector.setSaveStatus(SaveStatus.NO_CHANGES);
		} catch (IOException ex) {
				JAlgoGUIConnector.getInstance().showErrorMessage(Messages.getString(
					"ebnf", "SynDia.Error.SaveError")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		return out;
	}

}