package org.jalgo.module.hoare.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.html.HTMLEditorKit;

import org.jalgo.main.util.Messages;

/**
 * Slider which adjusts the maximum evaluation time
 * 
 * @author Markus
 * @param gui
 *            the <code>GUIController</code> instance
 */

public class EvalSlider extends JPanel implements Observer, GUIConstants {

	private static final long serialVersionUID = -1129184396383659962L;

	private GuiControl gui;

	private JSlider evalslider;

	private JTextPane anzahl;

	private JTextPane dauer;

	public EvalSlider(final GuiControl gui) {

		super();
		gui.addObserver(this);
		this.gui = gui;
		this.setLayout(new BorderLayout());

		this.evalslider = new JSlider(0, 160);
		evalslider.setMajorTickSpacing(1);
		evalslider.setValue(30);

		dauer = new JTextPane();
		dauer.setEditable(false);
		dauer.setOpaque(false);

		if (gui.getBeamer()) {
			dauer.setFont(BEAMER_FONT);
		} else {
			dauer.setFont(TOOLBAR_FONT);
		}

		dauer.setText(Messages.getString("hoare", "out.maxEvalTime") + " "
				+ evalslider.getValue() + " "
				+ Messages.getString("hoare", "out.seconds"));

		HTMLEditorKit htmlEditKit = new HTMLEditorKit();
		htmlEditKit.setLinkCursor(new Cursor(Cursor.HAND_CURSOR));

		evalslider.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {

				int value = evalslider.getValue();
				value = ((int) Math.round(0.15 * (value * value)));

				if (value < 60) {
					dauer.setText(Messages
							.getString("hoare", "out.maxEvalTime")
							+ " "
							+ value
							+ " "
							+ Messages.getString("hoare", "out.seconds"));
					evalslider.repaint();
				}
				if ((value >= 60) && (value < 3600)) {

					int value2 = (int) Math.round(value / 60);
					dauer.setText(Messages
							.getString("hoare", "out.maxEvalTime")
							+ " "
							+ value2
							+ " "
							+ Messages.getString("hoare", "out.minutes"));
					evalslider.repaint();
				}
				if (value >= 3600) {
					int value2 = 3600;
					dauer.setText(Messages
							.getString("hoare", "out.maxEvalTime")
							+ " "
							+ (value2 / 3600)
							+ " "
							+ Messages.getString("hoare", "out.hour"));
					evalslider.repaint();

				}

				gui.setMaxEvalTime(value);
			}

		});

		this.anzahl = new JTextPane();
		anzahl.setEditable(false);
		anzahl.setOpaque(false);

		if (gui.getBeamer()) {
			anzahl.setFont(BEAMER_FONT);
		} else {
			anzahl.setFont(TOOLBAR_FONT);
		}

		anzahl.setText(Messages.getString("hoare", "out.evalAmount") + ": ");
		anzahl.setAlignmentY(CENTER_ALIGNMENT);

		Box box1 = Box.createHorizontalBox();
		box1.add(evalslider);
		box1.add(Box.createHorizontalStrut(5));
		box1.add(dauer);
		box1.add(Box.createHorizontalStrut(5));
		box1.add(anzahl);
		this.add(box1, BorderLayout.WEST);
	}

	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		anzahl.setText(Messages.getString("hoare", "out.evalAmount") + ": "
				+ gui.getEvalAmount());

	}
}
