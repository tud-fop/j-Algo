package org.jalgo.module.synDiaEBNF.gui.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
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

	Figure figure;
	ModuleController mc;

	public SynDiaDoneAction(IFigure figure, ModuleController mc) {
		this.figure = (Figure) figure;
		this.mc=mc;
		//this.appWindow = appWindow;
		setText(Messages.getString("SynDiaDoneAction.Diagram_done_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("SynDiaDoneAction.Done_2")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/finish.gif")); //$NON-NLS-1$
	}

	public void run() {
		int a = 0, i = 0, j;
		List children = new ArrayList();
		List variables = new ArrayList();
		List initial = new ArrayList();
		children.addAll(figure.getChildren());
		if(children.isEmpty()){
			MessageDialog.openError(new Shell(),Messages.getString("SynDiaDoneAction.Warning_4"), Messages.getString("SynDiaDoneAction.There_is_no_syntax_diagram._5")); //$NON-NLS-1$ //$NON-NLS-2$
			return;
		}

		Object o;

		while (i < children.size()) {
			o = children.get(i);
			if (!(o.equals(null))) {
				children.addAll(((Figure) o).getChildren());
				i++;
			}
			else children.remove(i);
			
		}

		Iterator it = children.iterator();
		while (it.hasNext()) {
			o = it.next();
			if (o instanceof CloudFigure) {
				if (a == 0) {
					if (MessageDialog
						.openQuestion(
							new Shell(),
							Messages.getString("SynDiaDoneAction.Question_6"), //$NON-NLS-1$
							Messages.getString("SynDiaDoneAction.There_are_still_clouds_left._Do_you_want_them_replaced_by_simple_lines__7"))) { //$NON-NLS-1$
						try {
							(
								(CompositeSynDiaFigure) ((CloudFigure) o)
									.getParent())
									.replace(
								((CloudFigure) o),
								new EmptyFigure());
						} catch (SynDiaException e) {
							// TODO Automatisch erstellter Catch-Block
						}
						a = 1;
					} else
						return;
				} else
					try {
						(
							(CompositeSynDiaFigure) ((CloudFigure) o)
								.getParent())
								.replace(
							((CloudFigure) o),
							new EmptyFigure());
					} catch (SynDiaException e) {
						// TODO Automatisch erstellter Catch-Block
					}
			}
			else
			if(o instanceof VariableFigure)	variables.add(((VariableFigure)o).getText());
			else if (o instanceof InitialFigure) initial.add(((InitialFigure)o).getLabel());
			
		}
		
		for(i=0; i<variables.size();i++)
		{
			a=0;
			for(j=0; j<initial.size();j++)
				if (initial.get(j).equals(variables.get(i))) ;
				else a++;
			if(a==initial.size()){
				MessageDialog.openError(new Shell(),Messages.getString("SynDiaDoneAction.Warning_8"),Messages.getString("SynDiaDoneAction.There_is_no_definition_for_9")+variables.get(i)); //$NON-NLS-1$ //$NON-NLS-2$
				return; 
			}
		}
		
		mc.createSynDiaFinished();
	}
}
