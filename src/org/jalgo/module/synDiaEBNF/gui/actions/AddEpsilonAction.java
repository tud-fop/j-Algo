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
import org.jalgo.module.synDiaEBNF.gfx.EmptyFigure;
import org.jalgo.module.synDiaEBNF.gfx.SynDiaException;
import org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure;

/**
 * @author Anne Kersten
 *
 */
public class AddEpsilonAction extends Action implements IClickAction {

	Figure figure;

	public AddEpsilonAction(IFigure figure) {
		this.figure = (Figure) figure;
		//this.appWindow = appWindow;
		setText(Messages.getString("AddEpsilonAction.Empty_Figure_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("AddEpsilonAction.Add_Epsilon._2")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/epsilon.gif")); //$NON-NLS-1$
	}

	public void run() {
		ClickCollector.init(1, this);

	}

	public void performAction(ArrayList items) {
		EmptyFigure epsilon = new EmptyFigure();
		IFigure help = ((Figure) items.get(0)).getParent();
		try {
			if (help instanceof CloudFigure) {
				((CompositeSynDiaFigure) help.getParent()).replace(
					(SynDiaFigure) help,
					epsilon);
					
			} else
				MessageDialog.openError(
					null,
					Messages.getString("AddEpsilonAction.Warning_4"), //$NON-NLS-1$
					Messages.getString("AddEpsilonAction.Click_on_a_cloud_to_add_a_new_element._5")); //$NON-NLS-1$
		} catch (SynDiaException e) {
			// TODO: handle e
		}

	}

}
