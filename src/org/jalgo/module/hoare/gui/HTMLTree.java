package org.jalgo.module.hoare.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.model.VerificationFormula;

/**
 * Fenster, welches den Beweisbaum anzeigt, mittels html
 * 
 * @author WarMuuh!!
 * 
 */

public class HTMLTree extends JTextPane implements Observer, GUIConstants {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final class Listener implements HyperlinkListener {
		public void hyperlinkUpdate(HyperlinkEvent arg0) {
			if (arg0.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				String link = arg0.getDescription();
				int nodeID = Integer.parseInt(link.substring(0, link
						.indexOf(':')));
				guiControl.setActiveNode(nodeID);
				guiControl.notifyObservers();

			}

		}
	}

	private final class MouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			/**/
		}
	};

	private GuiControl guiControl;

	private Renderer renderer;

	private JTextPane content;

	private HTMLEditorKit htmlEditKit;

	private StyleSheet cssNorm;

	private StyleSheet cssBeam;

	public HTMLTree(GuiControl guiCtrl) {
		super();
		content = new JTextPane();
		this.setLayout(new BorderLayout());
		guiControl = guiCtrl;
		guiControl.addObserver(this);
		renderer = new HTMLRenderer(guiControl);

		content.setEditable(false);
		content.addMouseListener(new MouseListener());

		// htmlNorm = new HTMLEditorKit();
		// htmlNorm.setLinkCursor(new Cursor(Cursor.HAND_CURSOR));
		// StyleSheet css1 = htmlNorm.getStyleSheet();
		// css1.importStyleSheet(CSS_TREE_NORM);
		// htmlNorm.setStyleSheet(css1);
		//		
		// htmlBeam = new HTMLEditorKit();
		// htmlBeam.setLinkCursor(new Cursor(Cursor.HAND_CURSOR));
		// StyleSheet css2 = htmlBeam.getStyleSheet();
		// css2.importStyleSheet(CSS_TREE_BEAM);
		// htmlBeam.setStyleSheet(css2);

		htmlEditKit = new HTMLEditorKit();
		htmlEditKit.setLinkCursor(new Cursor(Cursor.HAND_CURSOR));

		cssNorm = new StyleSheet();
		cssNorm.importStyleSheet(CSS_TREE_NORM);

		cssBeam = new StyleSheet();
		cssBeam.importStyleSheet(CSS_TREE_BEAM);

		htmlEditKit.setStyleSheet(cssNorm);

		content.setEditorKit(htmlEditKit);

		content.addHyperlinkListener(new Listener());

		content.setText(Messages.getString("hoare", "tree.empty"));

		JScrollPane scroll = new JScrollPane();
		JViewport view = new JViewport();
		view.setView(content);

		scroll.setViewport(view);
		this.add(scroll, BorderLayout.CENTER);

	}

	public void update(Observable arg0, Object arg1) {
		VerificationFormula root = guiControl.getRoot();
		try {
			content.getDocument().remove(0, content.getDocument().getLength());
		} catch (BadLocationException e) {
		
			e.printStackTrace();
		}
		if (root == null)
			content.setText(Messages.getString("hoare", "tree.empty"));
		else
			renderer.render(root, content);
	}

	public void setBeamer(boolean b) {

		if (b) {
			String temp = content.getText();
			htmlEditKit.setStyleSheet(cssBeam);
			content.setEditorKit(htmlEditKit);
			content.setText(temp);
		} else {
			String temp = content.getText();
			htmlEditKit.setStyleSheet(cssNorm);
			content.setEditorKit(htmlEditKit);
			content.setText(temp);
		}

	}
}
