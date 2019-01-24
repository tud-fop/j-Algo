package org.jalgo.module.c0h0.views;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;
import javax.swing.event.EventListenerList;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;

import org.jalgo.module.c0h0.controller.Controller;
import org.jalgo.module.c0h0.controller.InterfaceConstants;
import org.jalgo.module.c0h0.controller.ViewManager;

/**
 * view which displays the H0 code
 * 
 * @author Peter Schwede
 * 
 */
public class H0View extends View implements teamView {
	private static final long serialVersionUID = -657432685963819871L;
	private static final String BODYFONTWIDTHMARGIN = "body {font-family: Courier, \"Courier New\", monospace; width:100%; padding: 3px; margin: 0; font-size: ";
	private TextEditor textEditor;
	private JScrollPane scrollpane;
	private Controller controller;
	private String currentlyMarked = null;
	private String currentlyEntered = "";
	private EventListenerList team;

	private final String markedPrefix = ".";
	private StringBuilder bob = new StringBuilder();

	/**
	 * @param controller
	 * @param viewManager
	 */
	public H0View(final Controller controller, ViewManager viewManager) {
		this.controller = controller;

		textEditor = new TextEditor();
		textEditor.setContentType("text/html");
		textEditor.setEditable(false);

		this.team = new EventListenerList();

		// setup CSS
		StyleSheet ss = ((HTMLDocument) textEditor.getDocument())
				.getStyleSheet();
		ss
				.addRule("body {width:100%; font-family: Courier, \"Courier New\", monospace; padding: 3px; margin: 0;}");
		ss.addRule("table td {width: 100%; margin: 0; padding: 0;}");
		ss
				.addRule("table {width: 100%; border-collapse: collapse; padding: 0; margin: 0;}");
		ss
				.addRule(".greyed {color:" + InterfaceConstants.GRAYED_CONTENT
						+ ";}");
		ss.addRule(".head {color: " + InterfaceConstants.GRAYED_CONTENT + ";}");
		ss.addRule(".foot {color: " + InterfaceConstants.GRAYED_CONTENT + ";}");
		ss.addRule("a {color: #000000; text-decoration: none;}");

		scrollpane = new JScrollPane(textEditor);
		setLayout(new BorderLayout());
		add(scrollpane, BorderLayout.CENTER);

		// Listener for touch display work-around
		MouseAdapter mouseListener = new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (!currentlyEntered.equals("")) {
					textEditor.fireHyperlinkUpdate(new HyperlinkEvent(this,
							HyperlinkEvent.EventType.ACTIVATED, null,
							currentlyEntered));
				}
			}
		};
		HyperlinkListener hyperListener = new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					if (!currentlyMarked.equals(currentlyEntered)) {
						controller.markNode(currentlyEntered);
					}
				} else if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {
					currentlyEntered = e.getDescription();
				} else {
					currentlyEntered = "";
				}
			}
		};
		textEditor.addMouseListener(mouseListener);
		textEditor.addHyperlinkListener(hyperListener);

		doLayout();
	}

	/*
	 * (non-Javadoc) This method is implicitly called in Controller.markNode()
	 * It handles all visible, non-text.
	 * 
	 * @see org.jalgo.module.c0h0.views.View#render()
	 */
	public boolean render() {
		StyleSheet ss = ((HTMLDocument) textEditor.getDocument())
				.getStyleSheet();

		ss.removeStyle(markedPrefix + currentlyMarked);
		currentlyMarked = controller.getASTModel().getMarkedNode();
		StringBuilder bob = new StringBuilder();
		bob.append(markedPrefix);
		bob.append(currentlyMarked);
		bob.append(" { background-color: ");
		if (controller.getH0CodeModel().isActive())
			bob.append(InterfaceConstants.MARKEDCOLOR_ACTIVE);
		else
			bob.append(InterfaceConstants.MARKEDCOLOR_INACTIVE);
		bob.append(";}");
		ss.addRule(bob.toString());

		textEditor.scrollToReference(currentlyMarked);
		return true;
	}

	/*
	 * (non-Javadoc) This handles all content
	 * 
	 * @see org.jalgo.module.c0h0.views.View#update()
	 */
	public boolean update() {
		if (controller.getASTModel().isValid()) {
			// fetch current code
			textEditor.setText(controller.getH0CodeModel().getHTMLCode());
		}
		
		if(!controller.getASTModel().getMarkedNode().equals(currentlyMarked))
			render();

		StyleSheet ss = ((HTMLDocument) textEditor.getDocument())
				.getStyleSheet();

		bob.setLength(0);
		if (controller.getViewManager().isBeamerMode()) {
			bob.append(BODYFONTWIDTHMARGIN);
			bob.append(InterfaceConstants.BEAMERMODE_FONTSIZE);
			bob.append("pt; background-color: ");
			bob.append(controller.getViewManager().backgroundColor(
					controller.getH0CodeModel().isActive()));
			bob.append(";}");
		} else {
			bob.append(BODYFONTWIDTHMARGIN);
			bob.append(InterfaceConstants.NORMAL_FONTSIZE);
			bob.append("pt; background-color: ");
			bob.append(controller.getViewManager().backgroundColor(
					controller.getH0CodeModel().isActive()));
			bob.append(";}");
		}
		ss.addRule(bob.toString());
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jalgo.module.c0h0.views.teamView#registerTeamPerformerMember(org.
	 * jalgo.module.c0h0.views.View)
	 */
	public void registerTeamPerformerMember(View member) {
		team.add(View.class, member);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jalgo.module.c0h0.views.teamView#unregisterTeamPerformerMember(org
	 * .jalgo.module.c0h0.views.View)
	 */
	public void unregisterTeamPerformerMember(View member) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.module.c0h0.views.teamView#notifyTeamPerformers()
	 */
	public void notifyTeamPerformers() {
		for (View view : team.getListeners(View.class)) {
			view.teamUpdate();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.module.c0h0.views.teamView#getTeamPerformerAddress()
	 */
	public String getTeamPerformerAddress() {
		return controller.getH0CodeModel().getAddress();
	}
}
