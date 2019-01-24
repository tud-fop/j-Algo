package org.jalgo.module.unifikation.algo.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

import org.jalgo.module.unifikation.IAppView;
import org.jalgo.module.unifikation.algo.view.Algo;

/**
 * Creates button listeners that used for algo view
 * @author Alex
 *
 */
public class ButtonListenerFactory {
	protected final IAppView appView;
	
	public ButtonListenerFactory(IAppView appView){
		this.appView=appView;
	}
	
	/**
	 * Gets a mouse listener that handles hover with behavior specified by given class
	 * @param c Class to use for firing events to
	 * @return Mouse listener as requested
	 */
	public <T extends IButtonAction>MouseListener getHoverListener(Class<T> c){
		IButtonAction action=null;
		try {
			action=c.getConstructor(Algo.class).newInstance(appView);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return new HoverListener(action);
	}
	
	/**
	 * Gets a action listener with onClick behavior specified by given class
	 * @param c Class to use for firing events to
	 * @return Button listener as requested
	 */
	public <T extends IButtonAction>ActionListener getClickListener(Class<T> c){
		IButtonAction action=null;
		try {
			action=c.getConstructor(Algo.class).newInstance(appView);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return new ClickListener(action);
	}
	
	/**
	 * Basic hover listener
	 * @author Alex
	 *
	 */
	class HoverListener implements MouseListener{
		private final IButtonAction action;
		public HoverListener(IButtonAction action){
			this.action=action;
		}

		/**
		 * Delegates to onClick event of handler class
		 * Does nothing for now
		 */
		public void mouseClicked(MouseEvent e) {
			/*if(e.getButton()==1 && ((JButton) e.getSource()).isEnabled()==true){
				action.onClick();
			}*/
		}

		/**
		 * Sets help text as specified in handler class
		 */
		public void mouseEntered(MouseEvent e) {
			if(((JButton) e.getSource()).isEnabled()==true){
				String helpText=action.getHoverText();
				if(helpText==null) helpText="";
				appView.setHelpText(helpText);
			}
		}
		
		/**
		 * Resets help text
		 */
		public void mouseExited(MouseEvent e) {
			if(((JButton) e.getSource()).isEnabled()==true){
				appView.setHelpText("");
			}
		}
		
		public void mousePressed(MouseEvent e) {	
		}
		public void mouseReleased(MouseEvent e) {	
		}
	}
}
/**
 * Basic click listener
 * @author Alex
 *
 */
class ClickListener implements ActionListener{
	private final IButtonAction action;
	public ClickListener(IButtonAction action){
		this.action=action;
	}

	public void actionPerformed(ActionEvent arg0) {
		action.onClick();		
	}
}	


