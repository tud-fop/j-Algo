package org.jalgo.module.c0h0.views;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLDocument;

import org.jalgo.module.c0h0.controller.Controller;
import org.jalgo.module.c0h0.controller.InterfaceConstants;
import org.jalgo.module.c0h0.controller.ViewManager;

/**
 * displayes the sstrans-rule provided by the TRuleModel
 * @author mathias.kaufmann
 */
public class TransView extends View {
	private static final long serialVersionUID = 6788651920838700007L;
	private TextEditor texteditor;
	private JScrollPane scrollPane;
	private ViewManager viewManager;
	private Controller controller;
	
	/**
	 * @param controller
	 * @param viewManager
	 */
	public TransView(final Controller controller, ViewManager viewManager){
		this.viewManager = viewManager;
		this.controller = controller;
		texteditor = new TextEditor();
		scrollPane = new JScrollPane(texteditor);
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
		texteditor.setEditable(false);
		texteditor.setContentType("text/html");
        String bodyRule = "body {  font-family: helvetica, arial, 'lucida console'; font-size: " + InterfaceConstants.NORMAL_FONTSIZE  + "pt; }";
        ((HTMLDocument)texteditor.getDocument()).getStyleSheet().addRule(bodyRule);
        render();
	}

	/**
	 * Renders the view.
	 * @return success
	 */
	public boolean render() {
		controller.getTRuleModel().setSTTrans();
		texteditor.setText(controller.getTRuleModel().getSTTrans());
		return true;
	}
	
	public boolean update() {
		String bodyRule;
		if (viewManager.isBeamerMode()){
	        bodyRule = "body {  font-family: monospace; " +
	        	"font-size: " + InterfaceConstants.BEAMERMODE_FONTSIZE  + "pt; }";
	    }
		else {
			bodyRule = "body {  font-family: monospace; " +
    			"font-size: " + InterfaceConstants.NORMAL_FONTSIZE  + "pt; }";
		}
		
        ((HTMLDocument)texteditor.getDocument()).getStyleSheet().removeStyle("body");
        ((HTMLDocument)texteditor.getDocument()).getStyleSheet().addRule(bodyRule);
		render();
		scrollPane.updateUI();
		return true;
	}
	
	public void teamUpdate() {
		controller.getTRuleModel().setSTTrans(viewManager.h0View.getTeamPerformerAddress());
		texteditor.setText(controller.getTRuleModel().getSTTrans());
	}

	/**
	 * register this view
	 * @param h0View
	 */
	public void registerToView(H0View h0View) {
		h0View.registerTeamPerformerMember(this);
	}	
}
