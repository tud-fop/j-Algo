package org.jalgo.module.c0h0.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import javax.swing.text.DefaultCaret;
import javax.swing.text.html.HTMLDocument;

import org.jalgo.module.c0h0.controller.Controller;
import org.jalgo.module.c0h0.controller.InterfaceConstants;
import org.jalgo.module.c0h0.controller.ViewManager;

/**
 * C0View which contains formatted C0-Code after successfully parsing the Code
 * 
 */
public class C0View extends View {
	private static final long serialVersionUID = 1724259710236167118L;
	private TextEditor addressbar, textEditor;
	private Controller controller;
	private ViewManager viewManager;
	private JScrollPane scrollPane;

	private String lastFocusedElement;
	private String currentFocus;

	/**
	 * @param controller
	 *            Controller
	 * @param viewManager
	 *            ViewManager
	 */
	public C0View(final Controller controller, ViewManager viewManager) {
		// Anlegen der Variablen
		this.controller = controller;
		this.viewManager = viewManager;
		textEditor = new TextEditor();
		addressbar = new TextEditor();
		lastFocusedElement = "";
		currentFocus = "";

		// Aufbau des Grundskeletts
		textEditor.setContentType("text/html");
		addressbar.setContentType("text/html");
		textEditor.setEditable(false);
		addressbar.setEditable(false);
		textEditor.setBackground(Color.WHITE);

		addressbar.setPreferredSize(new Dimension(80, getHeight()));
		addressbar.setBackground(new Color(238, 238, 255));

		scrollPane = new JScrollPane();
		JPanel innerPane = new JPanel();
		innerPane.setLayout(new BorderLayout());
		innerPane.add(addressbar, BorderLayout.WEST);
		innerPane.add(textEditor, BorderLayout.CENTER);
		innerPane.doLayout();
		scrollPane.getViewport().setView(innerPane);

		// Layout
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
		doLayout();

		// Auto-Scrolling off
		((DefaultCaret) textEditor.getCaret())
				.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

		// Setting Scrollspeed
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);

		// Listener
		MouseAdapter realListener = new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (!currentFocus.equals("")) {
					textEditor.fireHyperlinkUpdate(new HyperlinkEvent(this,
							HyperlinkEvent.EventType.ACTIVATED, null,
							currentFocus));
				}
			}
		};
		HyperlinkListener listener = new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					if (!lastFocusedElement.equals(currentFocus)) {
						controller.markNode(currentFocus);
					}
				} else if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {
					currentFocus = e.getDescription();
				} else {
					currentFocus = "";
				}
			}
		};
		textEditor.addMouseListener(realListener);
		textEditor.addHyperlinkListener(listener);
		addressbar.addMouseListener(realListener);
		addressbar.addHyperlinkListener(listener);

		setCSS();

	}

	/**
	 * Sets all relevant CSS-Rules(is only to be used once in the Constructor!)
	 */
	private void setCSS() {
		TerminalView.println("css set "
				+ String.valueOf(InterfaceConstants.NORMAL_FONTSIZE));
		// Farbe der Tokens
		String tokenRule = ".token { color: #0000FF; }";
		// Einzelne Zellen haengen aneinander
		String tableRule = "table { border-collapse: collapse; }";
		// Der Abstand zwischen den Zellen ist 0
		String paddingRule = "table td { padding: 0px; }";
		// Schriftart ist monospace
		String fontRule = "body { font-family: 'Courier New'; font-size: "
				+ InterfaceConstants.NORMAL_FONTSIZE + " pt; }";
		// Links ohne typischem Linkstyle
		String aRule = "a { color: #000000; text-decoration: none; }";
		// Head und Foot eingrauen
		String grayedRule = "td.grayed { color: #"
				+ InterfaceConstants.GRAYED_CONTENT + "; }";

		// Regeln muessen auch in den Editor eingefuegt werden
		((HTMLDocument) textEditor.getDocument()).getStyleSheet().addRule(
				tokenRule);
		((HTMLDocument) textEditor.getDocument()).getStyleSheet().addRule(
				tableRule);
		((HTMLDocument) textEditor.getDocument()).getStyleSheet().addRule(
				paddingRule);
		((HTMLDocument) textEditor.getDocument()).getStyleSheet().addRule(
				fontRule);
		((HTMLDocument) textEditor.getDocument()).getStyleSheet()
				.addRule(aRule);
		((HTMLDocument) textEditor.getDocument()).getStyleSheet().addRule(
				grayedRule);

		((HTMLDocument) addressbar.getDocument()).getStyleSheet().addRule(
				fontRule);
		((HTMLDocument) addressbar.getDocument()).getStyleSheet().addRule(
				tableRule);
		((HTMLDocument) addressbar.getDocument()).getStyleSheet().addRule(
				paddingRule);
		((HTMLDocument) addressbar.getDocument()).getStyleSheet()
				.addRule(aRule);
	}

	/*
	 * (non-Javadoc) This method is implicitly called in Controller.markNode()
	 * 
	 * @see org.jalgo.module.c0h0.views.View#render()
	 */
	public boolean render() {
		// MarkedNode bekommt eigene Farbe
		((HTMLDocument) textEditor.getDocument()).getStyleSheet().removeStyle(
				"." + lastFocusedElement);
		((HTMLDocument) textEditor.getDocument()).getStyleSheet().addRule(
				"." + controller.getASTModel().getMarkedNode()
						+ " { background-color: #"
						+ InterfaceConstants.MARKEDCOLOR_ACTIVE + "; }");
		lastFocusedElement = controller.getASTModel().getMarkedNode();

		// Hole die neuen Adressen
		if (controller.getC0CodeModel().isActive() && !controller.getC0CodeModel().isActual())
			addressbar.setText(controller.getC0CodeModel().getAddresses());

		// Scroll zum Knoten
		textEditor.scrollToReference(lastFocusedElement);

		updateUI();
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.module.c0h0.views.View#update()
	 */
	public boolean update() {
		render();
		((HTMLDocument) textEditor.getDocument()).getStyleSheet().removeStyle(
				"body");
		((HTMLDocument) addressbar.getDocument()).getStyleSheet().removeStyle(
				"body");
		if (viewManager.isBeamerMode()) {
			((HTMLDocument) textEditor.getDocument()).getStyleSheet().addRule(
					"body { font-family: 'Courier New'; font-size: "
							+ InterfaceConstants.BEAMERMODE_FONTSIZE
							+ " pt; background-color:"
							+ viewManager.backgroundColor(controller
									.getC0CodeModel().isActive()) + ";}");
			((HTMLDocument) addressbar.getDocument()).getStyleSheet()
					.addRule(
							"body { font-family: 'Courier New'; font-size: "
									+ InterfaceConstants.BEAMERMODE_FONTSIZE
									+ " pt; }");
			addressbar.setPreferredSize(new Dimension(controller.getASTModel()
					.getMaxDepth()
					* InterfaceConstants.BEAMERMODE_FONTSIZE * 5 / 8 + 5,
					getHeight() - 20));
		} else {
			((HTMLDocument) textEditor.getDocument()).getStyleSheet().addRule(
					"body { font-family: 'Courier New'; font-size: "
							+ InterfaceConstants.NORMAL_FONTSIZE
							+ " pt; background-color:"
							+ viewManager.backgroundColor(controller
									.getC0CodeModel().isActive()) + ";}");
			((HTMLDocument) addressbar.getDocument()).getStyleSheet().addRule(
					"body { font-family: 'Courier New'; font-size: "
							+ InterfaceConstants.NORMAL_FONTSIZE + " pt; }");
			addressbar.setPreferredSize(new Dimension(controller.getASTModel()
					.getMaxDepth()
					* InterfaceConstants.NORMAL_FONTSIZE * 5 / 8 + 5,
					getHeight() - 20));
		}
		if (controller.getASTModel().isValid()) {
			// Hole den Code
			textEditor
					.setText(controller.getC0CodeModel().getFormattedC0Code());
		} else
			return false;
		updateUI();
		return true;
	}

}
