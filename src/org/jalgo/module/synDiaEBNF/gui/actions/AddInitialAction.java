/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and
 * platform independant. j-Algo is developed with the help of Dresden
 * University of Technology.
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
 * Created on 18.06.2004
 */
package org.jalgo.module.synDiaEBNF.gui.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.jalgo.main.gfx.IClickAction;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.ClickCollector;
import org.jalgo.main.util.GfxUtil;
import org.jalgo.main.util.Messages;
import org.jalgo.module.synDiaEBNF.gfx.CloudFigure;
import org.jalgo.module.synDiaEBNF.gfx.InitialFigure;

/**
 * @author Hauke Menges
 */
public class AddInitialAction extends Action implements IClickAction<Figure> {

	private IFigure figure;
	private boolean firstSet;
	private ArrayList<String> labelList;

	public AddInitialAction(IFigure figure) {
		this.figure = figure;
		this.firstSet = false;
		this.labelList = new ArrayList<String>();
		setText(Messages.getString("synDiaEBNF",
			"AddInitialAction.New_Diagram_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("synDiaEBNF",
			"AddInitialAction.Add_initial_diagram._2")); //$NON-NLS-1$
		setImageDescriptor(ImageDescriptor.createFromURL(
			getClass().getResource("/ebnf_pix/newsyndia.gif")));
	}

	public void run() {
		ClickCollector.init(1, this);
	}

	public void performAction(List<Figure> items) {
		IFigure help = items.get(0).getParent();

		if (help instanceof CloudFigure)
			JAlgoGUIConnector.getInstance().showWarningMessage(
				Messages.getString("synDiaEBNF", 
					"AddInitialAction.Click_on_a_panel_to_add_a_new_initial_figure._5")); //$NON-NLS-1$
		else {
			InitialFigure startcloud;
			InputDialog inDialog =
				new InputDialog(
					GfxUtil.getAppShell(),
					Messages.getString("synDiaEBNF", "AddInitialAction.Initial_figure_6"), //$NON-NLS-1$
					Messages.getString("synDiaEBNF", "AddInitialAction.Name__7"), //$NON-NLS-1$
					"S", //$NON-NLS-1$
					null);
			if (inDialog.open() != Window.CANCEL) {
				String result = inDialog.getValue();
				boolean errorInList = false;

				Iterator it = labelList.iterator();

				while (it.hasNext()) {
					if (result.equals(it.next()))
						errorInList = true;
				}

				if (errorInList)
					JAlgoGUIConnector.getInstance().showWarningMessage(
						Messages.getString("synDiaEBNF", "AddInitialAction.EBNF_does_not_allow_multiple_diagrams_with_same_names._11")); //$NON-NLS-1$
				else if (result.equals("")) //$NON-NLS-1$
					JAlgoGUIConnector.getInstance().showWarningMessage(
						Messages.getString("synDiaEBNF", "AddInitialAction.EBNF_does_not_allow_diagrams_without_names._14")); //$NON-NLS-1$
				else {
					labelList.add(result);
					startcloud = new InitialFigure(result);
					if (!this.firstSet) {
						this.firstSet = true;
						startcloud.setStartFigure();
					}
					this.figure.add(startcloud);
					startcloud.setLocation(ClickCollector.getLastPoint());
				}
			}
		}
	}
}