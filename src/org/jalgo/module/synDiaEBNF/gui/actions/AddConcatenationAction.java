/*
 * Created on 19.06.2004
 *
 */
package org.jalgo.module.synDiaEBNF.gui.actions;

import java.util.ArrayList;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.jalgo.main.gfx.IClickAction;
import org.jalgo.main.util.ClickCollector;
import org.jalgo.main.util.GfxUtil;
import org.jalgo.module.synDiaEBNF.gfx.CloudFigure;
import org.jalgo.module.synDiaEBNF.gfx.CompositeSynDiaFigure;
import org.jalgo.module.synDiaEBNF.gfx.ConcatenationFigure;
import org.jalgo.module.synDiaEBNF.gfx.SynDiaException;
import org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure;

/**
 * @author Hauke Menges
 *
 */
public class AddConcatenationAction extends Action implements IClickAction {
	IFigure figure;

	public AddConcatenationAction(IFigure figure) {
		this.figure = figure;
		//this.appWindow = appWindow;
		setText(Messages.getString("AddConcatenationAction.Concatenation_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("AddConcatenationAction.Add_concatenation._2")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/concatenation.gif")); //$NON-NLS-1$
	}

	public void run() {
		ClickCollector.init(1, this);

	}

	public void performAction(ArrayList items) {
		try {
			IFigure help = ((Figure) items.get(0)).getParent();

			if (help instanceof CloudFigure) {
				InputDialog inDialog =
					new InputDialog(
						GfxUtil.getAppShell(),
						Messages.getString("AddConcatenationAction.Concatenation_4"), //$NON-NLS-1$
						Messages.getString("AddConcatenationAction.How_many_clouds_do_you_want_to_have__5"), //$NON-NLS-1$
						"2", //$NON-NLS-1$
						null);
				int result = 0;
				if (inDialog.open() != Window.CANCEL) {
					try {
						result =
							(Integer.valueOf(inDialog.getValue())).intValue();
					} catch (NumberFormatException e) {
						result = 2;
					}

					((CompositeSynDiaFigure) help.getParent()).replace(
						(SynDiaFigure) help,
						new ConcatenationFigure(result));
				}
			}
			else
				MessageDialog.openError(
					null,
					Messages.getString("AddConcatenationAction.Warning_7"), //$NON-NLS-1$
					Messages.getString("AddConcatenationAction.Click_on_a_cloud_to_add_a_new_element._8")); //$NON-NLS-1$

		} catch (SynDiaException e) {
			//TODO: handle e
		}

	}

}