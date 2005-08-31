/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

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
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.jalgo.main.JAlgoGUIConnector;
import org.jalgo.main.gfx.IClickAction;
import org.jalgo.main.util.ClickCollector;
import org.jalgo.main.util.GfxUtil;
import org.jalgo.main.util.Messages;
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
		setText(Messages.getString("synDiaEBNF",
			"AddConcatenationAction.Concatenation_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("synDiaEBNF",
			"AddConcatenationAction.Add_concatenation._2")); //$NON-NLS-1$
		setImageDescriptor(ImageDescriptor.createFromURL(
			getClass().getResource("/ebnf_pix/concatenation.gif")));
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
						Messages.getString("synDiaEBNF",
							"AddConcatenationAction.Concatenation_4"), //$NON-NLS-1$
						Messages.getString("synDiaEBNF",
							"AddConcatenationAction.How_many_clouds_do_you_want_to_have__5"), //$NON-NLS-1$
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
				JAlgoGUIConnector.getInstance().showWarningMessage(
					Messages.getString("synDiaEBNF", 
						"AddConcatenationAction.Click_on_a_cloud_to_add_a_new_element._8")); //$NON-NLS-1$

		} catch (SynDiaException e) {
			//TODO: handle e
		}
	}
}