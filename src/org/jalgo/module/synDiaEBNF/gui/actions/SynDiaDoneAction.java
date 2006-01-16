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

package org.jalgo.module.synDiaEBNF.gui.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.main.gui.DialogConstants;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.module.synDiaEBNF.ModuleController;
import org.jalgo.module.synDiaEBNF.gfx.CloudFigure;
import org.jalgo.module.synDiaEBNF.gfx.CompositeSynDiaFigure;
import org.jalgo.module.synDiaEBNF.gfx.EmptyFigure;
import org.jalgo.module.synDiaEBNF.gfx.InitialFigure;
import org.jalgo.module.synDiaEBNF.gfx.SynDiaException;
import org.jalgo.module.synDiaEBNF.gfx.VariableFigure;

/**
 * @author Anne Kersten
 */
public class SynDiaDoneAction extends Action {

	private Figure figure;
	private ModuleController mc;

	public SynDiaDoneAction(IFigure figure, ModuleController mc) {
		this.figure = (Figure) figure;
		this.mc = mc;
		setText(Messages.getString("synDiaEBNF",
			"SynDiaDoneAction.Diagram_done_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("synDiaEBNF",
			"SynDiaDoneAction.Done_2")); //$NON-NLS-1$
		setImageDescriptor(ImageDescriptor.createFromURL(
			getClass().getResource("/ebnf_pix/finish.gif")));
	}

	public void run() {
		int a = 0, i = 0;
		List<Figure> children = new ArrayList<Figure>();
		List<String> variables = new ArrayList<String>();
		List<String> initials = new ArrayList<String>();
		children.addAll(figure.getChildren());
		if (children.isEmpty()) {
			JAlgoGUIConnector.getInstance().showWarningMessage(
				Messages.getString("synDiaEBNF",
					"SynDiaDoneAction.There_is_no_syntax_diagram._5")); //$NON-NLS-1$ //$NON-NLS-2$
			return;
		} else if ((children.size() == 1)
				&& (children.get(0) instanceof CloudFigure)) {
			JAlgoGUIConnector.getInstance().showWarningMessage(
				Messages.getString("synDiaEBNF",
					"SynDiaDoneAction.0"));
			return;
		}

		while (i < children.size()) {
			Object o = children.get(i);
			if (o != null) {
				children.addAll(((Figure) o).getChildren());
				i++;
			} else
				children.remove(i);
		}
		
		for (Figure child : children) {
			if (child instanceof CloudFigure) {
				if (a == 0) {
					if (JAlgoGUIConnector.getInstance().showConfirmDialog(
						Messages.getString("synDiaEBNF",
							"SynDiaDoneAction.There_are_still_clouds_left._Do_you_want_them_replaced_by_simple_lines__7"), //$NON-NLS-1$
						DialogConstants.YES_NO_OPTION) == DialogConstants.YES_OPTION) {
						try {
							((CompositeSynDiaFigure) ((CloudFigure) child)
									.getParent()).replace(((CloudFigure) child),
									new EmptyFigure());
						} catch (SynDiaException e) {
							System.err.println(e.getMessage());
						}
						a = 1;
					} else
						return;
				} else
					try {
						((CompositeSynDiaFigure) ((CloudFigure) child).getParent())
								.replace(((CloudFigure) child), new EmptyFigure());
					} catch (SynDiaException e) {
						System.err.println(e.getMessage());
					}
			} else if (child instanceof VariableFigure)
				variables.add(((VariableFigure)child).getText());
			else if (child instanceof InitialFigure)
				initials.add(((InitialFigure) child).getLabel());
		}
		
		for (String variable : variables) {
			if (!initials.contains(variable)) {
				JAlgoGUIConnector.getInstance().showWarningMessage(
						Messages.getString("synDiaEBNF",
							"SynDiaDoneAction.There_is_no_definition_for_9") + //$NON-NLS-1$
						variable);
				return;
			}
		}

		mc.createSynDiaFinished();
	}
}