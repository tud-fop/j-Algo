package org.jalgo.module.hoare.view.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.view.View;

/**
 * The class <code>BeamerMode</code> defines a checkbox menuitem for
 * switching between beamer mode and pc mode.<br>
 * Here a special design is implemented, in order that this setting
 * takes global effect for all open instances of the Hoare module.<br>
 * 
 * @author Thomas
 */
public class BeamerMode extends JCheckBoxMenuItem implements ActionListener {
	private static final long serialVersionUID = 5523818288828234644L;

	private static boolean isAktiv=false;

	private static Set<BeamerMode> beamerModes=new HashSet<BeamerMode>();
	
	private View view;

	/**
	 * Creates a new <code>BeamerModeAction</code> which provides the
	 * <code>isAktiv</code> state with all views who have an instance
	 * of this action.
	 * 
	 * @param view the {@link View} which includes this action
	 */
	public BeamerMode(View view) {
		super(Messages.getString("hoare", "Beamer_mode"), new ImageIcon(
				Messages.getResourceURL("main", "Icon.Beamer_mode")), false);
		this.view=view;
		//isAktiv=aktiv;//?????
		this.setSelected(isAktiv);
		addActionListener(this);
		beamerModes.add(this);
	}
	
	/**
	 * Performs the action for all views which have created an instance. 
	 */
	public void actionPerformed(ActionEvent e) {
		isAktiv=this.isSelected();
		for (BeamerMode bm : beamerModes){ 
		 bm.setSelected(isAktiv);
		 bm.changeView();
		}
	}
	
	/**
	 * Performs the call of the <code>setBeamer</code>-method of the
	 * assigned {@link View}
	 */
	private void changeView(){
		if (view!=null){
			view.setBeamer(isAktiv);
		}
	}
	
	/**
	 * Returns the state of the BeamerMode
	 * 
	 * @return <code>true</code> if the BeamerMode is aktiv
	 */
	public boolean isAktiv(){
		return isAktiv;
	}
	
}