/*
 * Created on 18.06.2004
 */
package org.jalgo.module.synDiaEBNF.gui.actions;

import java.util.ArrayList;
import java.util.Iterator;

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
import org.jalgo.module.synDiaEBNF.gfx.InitialFigure;

/**
 * @author Hauke Menges
 */
public class AddInitialAction extends Action implements IClickAction {

	private IFigure figure;
	private boolean firstSet;
	private ArrayList labelList;

	public AddInitialAction(IFigure figure) {
		this.figure = figure;
		this.firstSet = false;
		this.labelList = new ArrayList();
		setText(Messages.getString("AddInitialAction.New_Diagram_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("AddInitialAction.Add_initial_diagram._2")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/newsyndia.gif")); //$NON-NLS-1$
	}

	public void run() {
		ClickCollector.init(1, this);

	}

	public void performAction(ArrayList items) {
		IFigure help = ((Figure) items.get(0)).getParent();

		if (help instanceof CloudFigure)
			MessageDialog.openError(
				null,
				Messages.getString("AddInitialAction.Warning_4"), //$NON-NLS-1$
				Messages.getString("AddInitialAction.Click_on_a_panel_to_add_a_new_initial_figure._5")); //$NON-NLS-1$
		else {
			InitialFigure startcloud;
			InputDialog inDialog =
				new InputDialog(
					GfxUtil.getAppShell(),
					Messages.getString("AddInitialAction.Initial_figure_6"), //$NON-NLS-1$
					Messages.getString("AddInitialAction.Name__7"), //$NON-NLS-1$
					"S", //$NON-NLS-1$
					null);
			String result = ""; //$NON-NLS-1$
			if (inDialog.open() != Window.CANCEL) {
				result = inDialog.getValue();
				boolean errorInList = false;

				Iterator it = labelList.iterator();

				while (it.hasNext()) {
					if (result.equals((String) it.next()))
						errorInList = true;
				}

				if (errorInList)
					MessageDialog.openError(
						null,
						Messages.getString("AddInitialAction.Warning_10"), //$NON-NLS-1$
						Messages.getString("AddInitialAction.EBNF_does_not_allow_multiple_diagrams_with_same_names._11")); //$NON-NLS-1$
				else if (result.equals("")) //$NON-NLS-1$
					MessageDialog.openError(
						null,
						Messages.getString("AddInitialAction.Warning_13"), //$NON-NLS-1$
						Messages.getString("AddInitialAction.EBNF_does_not_allow_diagrams_without_names._14")); //$NON-NLS-1$
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
