/*
 * Created on 18.06.2004
 *
 */
package org.jalgo.module.synDiaEBNF.gui.actions;

import java.util.ArrayList;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.main.gfx.IClickAction;
import org.jalgo.main.util.ClickCollector;
import org.jalgo.module.synDiaEBNF.gfx.AlternativeFigure;
import org.jalgo.module.synDiaEBNF.gfx.CloudFigure;
import org.jalgo.module.synDiaEBNF.gfx.CompositeSynDiaFigure;
import org.jalgo.module.synDiaEBNF.gfx.ConcatenationFigure;
import org.jalgo.module.synDiaEBNF.gfx.SynDiaException;
import org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure;

/**
 * @author Hauke Menges
 *
 */
public class AddCloudAction extends Action implements IClickAction {
	IFigure figure;

	public AddCloudAction(IFigure figure) {
		this.figure = figure;
		setText(Messages.getString("AddCloudAction.Cloud_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("AddCloudAction.Add_cloud._2")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/rectangle.gif")); //$NON-NLS-1$
	}

	public void run() {
		ClickCollector.init(1, this);

	}

	public void performAction(ArrayList items) {
		ConcatenationFigure cloud;
		try {
			cloud = new ConcatenationFigure(3);
			IFigure help = ((Figure) items.get(0)).getParent();
			
			if (help instanceof CloudFigure)
				((CompositeSynDiaFigure) help.getParent()).replace((SynDiaFigure) help, new AlternativeFigure(2));
		} catch (SynDiaException e) {
			//TODO: handle e
		}

	}

}
