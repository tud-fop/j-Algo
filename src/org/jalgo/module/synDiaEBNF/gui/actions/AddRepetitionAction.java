/*
 * Created on 18.06.2004
 *
 */
package org.jalgo.module.synDiaEBNF.gui.actions;

import java.util.ArrayList;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.main.gfx.IClickAction;
import org.jalgo.main.util.ClickCollector;
import org.jalgo.module.synDiaEBNF.gfx.CloudFigure;
import org.jalgo.module.synDiaEBNF.gfx.CompositeSynDiaFigure;
import org.jalgo.module.synDiaEBNF.gfx.RepetitionFigure;
import org.jalgo.module.synDiaEBNF.gfx.SynDiaException;
import org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure;

/**
 * @author Cornelius Hald
 * @author Anne Kersten
 *
 */
public class AddRepetitionAction extends Action implements IClickAction {
	
	IFigure figure;
	
	public AddRepetitionAction(IFigure figure){
		this.figure = figure;
		//this.appWindow = appWindow;
		setText(Messages.getString("AddRepetitionAction.Repetition_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("AddRepetitionAction.Add_repetition_2")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/repetition.gif")); //$NON-NLS-1$
	}
	
	public void run() {
		ClickCollector.init(1, this);

	}

	public void performAction(ArrayList items) {
		try {
			IFigure help = ((Figure) items.get(0)).getParent();
		
			if (help instanceof CloudFigure){
				((CompositeSynDiaFigure) help.getParent()).replace((SynDiaFigure) help, new RepetitionFigure());
			}
			else
				MessageDialog.openError(
					null,
					Messages.getString("AddRepetitionAction.Warning_4"), //$NON-NLS-1$
					Messages.getString("AddRepetitionAction.Click_on_a_cloud_to_add_a_new_element._5")); //$NON-NLS-1$
			

		} catch (SynDiaException e) {
			//TODO: handle e
		}

	}

}
