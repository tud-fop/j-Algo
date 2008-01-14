package org.jalgo.module.hoare.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import org.jalgo.main.util.Messages;

public class EventLog extends JPanel implements Observer, GUIConstants {

	private static final long serialVersionUID = 1L;

	private JTextPane log;

	private JTextPane label;

	private StringBuffer logBuffer;

	private StyleSheet cssNorm;

	private StyleSheet cssBeam;

	private HTMLEditorKit htmlEditKit;

	/**
	 * Textpanel which is supposed to display user-oriented outputs such as
	 * errors, notices, etc
	 * 
	 * @author Markus
	 * @param gui
	 *            the <code>GUIController</code> instance
	 */

	public EventLog(GuiControl gui) {

		super();
		gui.addObserver(this);

		this.setLayout(new BorderLayout());
		log = new JTextPane();
		log.setEditable(false);
		logBuffer = new StringBuffer();
		this.label = new JTextPane();

		htmlEditKit = new HTMLEditorKit();
		htmlEditKit.setLinkCursor(new Cursor(Cursor.HAND_CURSOR));

		cssNorm = new StyleSheet();
		cssNorm.importStyleSheet(CSS_EVENT_NORM);

		cssBeam = new StyleSheet();
		cssBeam.importStyleSheet(CSS_EVENT_BEAM);

		htmlEditKit.setStyleSheet(cssNorm);

		log.setEditorKit(htmlEditKit);

		label.setOpaque(false);
		label.setEditable(false);
		label.setFont(STANDARD_FONT);
		label.setText("Event-Log:");

		JButton eraseButton = new JButton(new Action() {
			public void addPropertyChangeListener(
					PropertyChangeListener listener) {
			}

			public Object getValue(String key) {
				return null;
			}

			public boolean isEnabled() {
				return true;
			}

			public void putValue(String key, Object value) {
			}

			public void removePropertyChangeListener(
					PropertyChangeListener listener) {
			}

			public void setEnabled(boolean b) {
			}

			public void actionPerformed(ActionEvent e) {
				log.setText("");
				logBuffer = new StringBuffer();
			}

		});
		eraseButton.setText(Messages.getString("hoare", "name.eraseLog"));

		JScrollPane scrollPane = new JScrollPane();
		JViewport viewp = new JViewport();
		viewp.setView(log);
		scrollPane.setViewport(viewp);
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(label, BorderLayout.NORTH);
		this.add(eraseButton, BorderLayout.SOUTH);

		gui.setErrorEmitter(new GuiControl.ErrorEmitter() {
			public void emitError(String message) {
				addError(message);
			};

			public void emitInfo(String message) {
				addInfo(message);
			};
		});

	}

	public void update(Observable arg0, Object arg1) {

	}

	private void addError(String message) {
		Calendar c = Calendar.getInstance();

		logBuffer.append("<p class='error'>" + c.get(Calendar.HOUR_OF_DAY)
				+ ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND)
				+ " " + message + "</p>");
		log.setText(logBuffer.toString());

	}

	private void addInfo(String message) {
		Calendar c = Calendar.getInstance();
		logBuffer.append("<p class='info'>" + c.get(Calendar.HOUR_OF_DAY) + ":"
				+ c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND) + " "
				+ message + "</p>");
		log.setText(logBuffer.toString());

	}
	/**
	 *	sets the Beamer-Mode either active or inactive  
	 * 
	 * @param boolean b
	 */
	public void setBeamer(boolean b) {

		if (b) {
			String temp = log.getText();
			htmlEditKit.setStyleSheet(cssBeam);
			log.setEditorKit(htmlEditKit);
			log.setText(temp);
		} else {
			String temp = log.getText();
			htmlEditKit.setStyleSheet(cssNorm);
			log.setEditorKit(htmlEditKit);
			log.setText(temp);
		}

	}

}
