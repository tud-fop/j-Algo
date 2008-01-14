package org.jalgo.module.hoare.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.control.ProgramControl.Edit;

/**
 * Dialoge to choose Verification-Rule which is supposed to be applied
 * 
 * @author Peter
 * 
 */

public class RuleView extends JPanel implements Observer, GUIConstants {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTextPane ruleview;

	private String selectedRule;

	private Map<String, Edit> ruleMap;

	private GuiControl gui;

	private StyleSheet cssNorm;

	private StyleSheet cssBeam;

	private HTMLEditorKit htmlEditKit;

	public RuleView(GuiControl gui) {

		super();
		this.gui = gui;
		gui.addObserver(this);

		this.setLayout(new BorderLayout());
		ruleview = new JTextPane();

		ruleview.setEditable(false);

		htmlEditKit = new HTMLEditorKit();
		htmlEditKit.setLinkCursor(new Cursor(Cursor.HAND_CURSOR));

		cssNorm = new StyleSheet();
		cssNorm.importStyleSheet(CSS_RULE_NORM);

		cssBeam = new StyleSheet();
		cssBeam.importStyleSheet(CSS_RULE_BEAM);

		htmlEditKit.setStyleSheet(cssNorm);

		ruleview.setEditorKit(htmlEditKit);

		ruleview.setOpaque(false);

		JScrollPane scrollPane = new JScrollPane();
		JViewport viewp = new JViewport();
		viewp.setView(ruleview);
		scrollPane.setViewport(viewp);
		this.add(scrollPane, BorderLayout.CENTER);

		ruleview.addHyperlinkListener(new RuleViewListener());

		ruleMap = new LinkedHashMap<String, Edit>();
		ruleMap.put("assign", Edit.AssignVFApply);
		ruleMap.put("sequence", Edit.StatSeqVFApply);
		ruleMap.put("comp", Edit.CompoundVFApply);
		ruleMap.put("alt1", Edit.IfVFApply);
		ruleMap.put("alt2", Edit.IfElseVFApply);
		ruleMap.put("iter", Edit.IterationVFApply);
		ruleMap.put("SV", Edit.StrongPreVFApply);
		ruleMap.put("SN", Edit.WeakPostVFApply);

		selectedRule = "assign";

		update(null, null);

	}

	public void update(Observable arg0, Object arg1) {
		try {
			ruleview.getDocument().remove(0, ruleview.getDocument().getLength());
		} catch (BadLocationException e) {
		
			e.printStackTrace();
		}
		StringBuffer content = new StringBuffer();
		content
				.append("<table width='100%' class='rules'><tr><td class='title' width='25%'>");
		int i = 0;
		for (String rule : ruleMap.keySet()) {
			if (rule.equals(selectedRule))
				content.append("<a href='"
						+ rule
						+ "' class='activeRule'>"
						+ Messages
								.getString("hoare", "rule." + rule + ".title")
						+ "</a><br>");
			else
				content.append("<a href='"
						+ rule
						+ "' class='rule'>"
						+ Messages
								.getString("hoare", "rule." + rule + ".title")
						+ "</a><br>");

			++i;
			if (i == ruleMap.size() / 2)
				content.append("</td><td class='title' width='25%'>");
		}
		content.append("</td><td class='content' width='50%'>"
				+ Messages.getString("hoare", "rule." + selectedRule
						+ ".content") + "</td></tr></table>");

		ruleview.setText(content.toString());

	}

	public class RuleViewListener implements HyperlinkListener {

		public void hyperlinkUpdate(HyperlinkEvent arg0) {
			if (arg0.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				if (gui.applyTreeEdit(ruleMap.get(arg0.getDescription()), gui
						.getActiveNode()))
					gui.update(null, null);
			} else if (arg0.getEventType() == HyperlinkEvent.EventType.ENTERED) {
				selectedRule = arg0.getDescription();
				update(null, null);
			}

		}

	}
	/**
	 * sets whether beamer mode is active or inactive
	 * 
	 * @author Markus
	 * @param b
	 */
	public void setBeamer(boolean b) {

		if (b) {
			String temp = ruleview.getText();
			htmlEditKit.setStyleSheet(cssBeam);
			ruleview.setEditorKit(htmlEditKit);
			ruleview.setText(temp);
		} else {
			String temp = ruleview.getText();
			htmlEditKit.setStyleSheet(cssNorm);
			ruleview.setEditorKit(htmlEditKit);
			ruleview.setText(temp);
		}

	}
}
