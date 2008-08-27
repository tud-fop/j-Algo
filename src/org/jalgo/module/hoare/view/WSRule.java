package org.jalgo.module.hoare.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Popup;
import javax.swing.PopupFactory;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.constants.Rule;

/**
 * This is the Panel which shows the Rules you can use.
 * 
 * @author Antje
 *
 */
public class WSRule extends WSPart {
	private static final long serialVersionUID = 1L;
	
	/**
	 * The gui controller.
	 */
	protected final View gui;
	/**
	 * Collection of the rule views.
	 */
	protected Collection<RuleView> ruleViews = new ArrayList<RuleView>();

	/**
	 * Creates a new instance of <code>WSRule</code>.
	 * @param gui the gui controller
	 */
	public WSRule(View gui){
		super("rules");
		this.gui = gui;

		setLayout(new BorderLayout());
				
		Container rulesView = new JPanel();
		rulesView.setLayout(new GridLayout(8,1));
		ruleViews.add(new RuleView(Rule.ASSIGN,
				Messages.getString("hoare", "name.assign"),
				new JLabel(new ImageIcon(Messages.getResourceURL("hoare", "rule.assign")))));
		ruleViews.add(new RuleView(Rule.COMPOUND,
				Messages.getString("hoare", "name.comp"),
				new JLabel(new ImageIcon(Messages.getResourceURL("hoare", "rule.comp")))));
		ruleViews.add(new RuleView(Rule.IF,
				Messages.getString("hoare", "name.alt1"),
				new JLabel(new ImageIcon(Messages.getResourceURL("hoare", "rule.alt1")))));
		ruleViews.add(new RuleView(Rule.IFELSE,
				Messages.getString("hoare", "name.alt2"),
				new JLabel(new ImageIcon(Messages.getResourceURL("hoare", "rule.alt2")))));
		ruleViews.add(new RuleView(Rule.ITERATION,
				Messages.getString("hoare", "name.iter"),
				new JLabel(new ImageIcon(Messages.getResourceURL("hoare", "rule.iter")))));
		ruleViews.add(new RuleView(Rule.STATSEQ,
				Messages.getString("hoare", "name.sequence"),
				new JLabel(new ImageIcon(Messages.getResourceURL("hoare", "rule.sequence")))));
		ruleViews.add(new RuleView(Rule.STRONGPRE,
				Messages.getString("hoare", "name.SV"),
				new JLabel(new ImageIcon(Messages.getResourceURL("hoare", "rule.SV")))));
		ruleViews.add(new RuleView(Rule.WEAKPOST,
				Messages.getString("hoare", "name.SN"),
				new JLabel(new ImageIcon(Messages.getResourceURL("hoare", "rule.SN")))));
		for (RuleView ruleView : ruleViews) {
			rulesView.add(ruleView);
		}
		
		JScrollPane scrollPane = new JScrollPane();
		JViewport viewp = new JViewport();
		viewp.setView(rulesView);
		scrollPane.setViewport(viewp);
		
		add(title, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		
	}
	
	/**
	 * Shows a single rule.
	 * @author Antje
	 *
	 */
	protected class RuleView extends JPanel {
		
		private static final long serialVersionUID = -8641308951811997611L;
		
		private RuleButton button;
		
		/**
		 * Creates a new instance of <code>RuleView</code>.
		 * @param rule rule that can be applied by this <code>RuleView</code>
		 * @param text description of the rule
		 */
		public RuleView(Rule rule, String text) {
			init(text, new RuleButton(rule, text));
		}
		
		/**
		 * Creates a new instance of <code>RuleView</code>.
		 * @param rule rule that can be applied by this <code>RuleView</code>
		 * @param text description of the rule
		 * @param popup a more detailed preview of the rule
		 */
		public RuleView(Rule rule, String text, Component popup) {
			init(text, new RuleButton(rule, text, popup));
		}
		
		/**
		 * Inits the <code>RuleView</code>.
		 * @param name description of the rule
		 * @param button applies the rule on click
		 */
		protected void init(String name, RuleButton button) {
			this.button = button;
			setLayout(new BorderLayout());
			add(button);
			setText(name);
			setFont();
		}
		
		/**
		 * Returns the text that describes the rule.
		 * @return the text that describes the rule
		 */
		public String getText() {
			return button.getText();
		}
		
		/**
		 * Sets the text that describes the rule.
		 * @param name text that describes the rule
		 */
		public void setText(String name) {
			button.setText(name);
		}
		
		/**
		 * Sets the font to the global font of the gui.
		 *
		 */
		public void setFont() {
			Font f = gui.getMainFont();
			button.setFont(f);
		}
		
	}
	
	/**
	 * Button that applies a rule on click.
	 * @author Antje
	 *
	 */
	protected class RuleButton extends JButton {
		
		private static final long serialVersionUID = 7776645518236062545L;
		
		/**
		 * Rule that will be applied on click.
		 */
		protected final Rule rule;
		/**
		 * The instance itself
		 */
		private final RuleButton ruleButtonMirror = this;
		/**
		 * Factory for creating the popup with the rule preview
		 */
		private final PopupFactory popups;
		/**
		 * Popup with a detailed preview of the rule.
		 */
		protected Popup popup;
		/**
		 * Container for the rule preview
		 * This container together with its content will be shown in the popup.
		 */
		private Container popupContainer = new JPanel(new BorderLayout());
		
		/**
		 * Creates a new button that can be used to apply the specified rule.
		 * @param ruleToApply rule that will be applied on click
		 */
		protected RuleButton(Rule ruleToApply) {
			this.rule = ruleToApply;
			
			popups = new PopupFactory();
			
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					gui.applyRule(rule);
				}
			});
			
			addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					int posX = getPopupPosX((int)Math.round(ruleButtonMirror.getLocationOnScreen().getX())+e.getX());
					int posY = getPopupPosY((int)Math.round(ruleButtonMirror.getLocationOnScreen().getY())+e.getY());
					popup = popups.getPopup(ruleButtonMirror, popupContainer, posX, posY);
					popup.show();
				}
				public void mouseExited(MouseEvent e) {
					popup.hide();
				}
			});
            
		}
		
		/**
		 * Creates a new button that can be used to apply the specified rule.
		 * @param rule rule that will be applied on click
		 * @param text short description of the rule
		 */
		public RuleButton(Rule rule, String text) {
			this(rule);
			setText(text);
		}
		
		/**
		 * Creates a new button that can be used to apply the specified rule.
		 * @param rule rule that will be applied on click
		 * @param text short description of the rule
		 * @param popup detailed preview of the rule
		 */
		public RuleButton(Rule rule, String text, Component popup) {
			this(rule, text);
			popupContainer.add(popup);
		}
        
		/**
		 * Returns the popup position on the screen in x direction.
		 * @param mousePosX the current mouse position in x direction
		 * @return the popup position on the screen in x direction
		 */
		private int getPopupPosX(int mousePosX) {
			return mousePosX-(int)popupContainer.getPreferredSize().getWidth()-5;
		}
		
		/**
		 * Returns the popup position on the screen in y direction.
		 * @param mousePosY the current mouse position in y direction
		 * @return the popup position on the screen in y direction
		 */
		private int getPopupPosY(int mousePosY) {
			return mousePosY+5;
		}
		
	}
	
	/**
	 * Sets the font to the global font of the gui.
	 *
	 */
	public void updateFont() {
		title.setFont(gui.getMainFont().deriveFont(Font.BOLD));
		if (ruleViews!=null) {
			for (RuleView ruleView : ruleViews) {
				ruleView.setFont();
			}
		}
	}
	
}
