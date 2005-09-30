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
 
package org.jalgo.module.synDiaEBNF.gui;

import java.io.Serializable;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jalgo.main.gfx.ClickListener;
import org.jalgo.main.gui.actions.ExportAction;
import org.jalgo.main.gui.actions.PrintViewAction;
import org.jalgo.main.gui.actions.ZoomInAction;
import org.jalgo.main.gui.actions.ZoomOutAction;
import org.jalgo.main.gui.widgets.GraphViewForm;
import org.jalgo.main.util.GfxUtil;
import org.jalgo.main.util.Messages;
import org.jalgo.module.synDiaEBNF.ModuleController;
import org.jalgo.module.synDiaEBNF.gui.actions.AddAlternativeAction;
import org.jalgo.module.synDiaEBNF.gui.actions.AddConcatenationAction;
import org.jalgo.module.synDiaEBNF.gui.actions.AddEpsilonAction;
import org.jalgo.module.synDiaEBNF.gui.actions.AddInitialAction;
import org.jalgo.module.synDiaEBNF.gui.actions.AddRepetitionAction;
import org.jalgo.module.synDiaEBNF.gui.actions.AddSynVariAction;
import org.jalgo.module.synDiaEBNF.gui.actions.AddTerminalAction;
import org.jalgo.module.synDiaEBNF.gui.actions.SynDiaDoneAction;

/**
 * @author Babett Schaliz
 * @author Anne Kersten
 * @author Hauke Menges
 */
public class CreateSynDiaClickGui extends Gui implements Serializable {

	private static final long serialVersionUID = -557820845220356421L;
	private GraphViewForm form2;
	private ClickListener form2ClickListener;

	public CreateSynDiaClickGui(Composite parent, ModuleController mc)
	{
		super(parent);
		
		GfxUtil.setAppShell(parent.getShell());

		form2 = new GraphViewForm(parent, SWT.BORDER);
		
		// TODO is this dead code needed anymore? 
		/*
		BorderFlowLayout flowlayout = new BorderFlowLayout(false);
		flowlayout.setMajorAlignment(BorderFlowLayout.ALIGN_LEFTTOP);
		flowlayout.setMajorSpacing(0); // Y-Spacing
		flowlayout.setMinorSpacing(50); // X-Spacing
		flowlayout.setStretchMinorAxis(false);

		form2.getPanel().setLayoutManager(flowlayout);
		*/
		
		ToolbarLayout layout = new ToolbarLayout();
		layout.setSpacing(50);
		form2.getPanel().setLayoutManager(layout);
//		MarginBorder b = new MarginBorder(10, 10, 0, 0);
//		form2.getPanel().setBorder(b);

		//		Create Toolbar
		ToolBarManager toolbarMgr2 = new ToolBarManager(SWT.FLAT);
		/*
		toolbarMgr2.add(new AddCircleAction(form2.getPanel()));
		toolbarMgr2.add(new AddRectangleAction(form2.getPanel()));
		toolbarMgr2.add(new AddRoundedRectangleAction(form2.getPanel()));
		toolbarMgr2.add(new Separator());
		toolbarMgr2.add(new AddLineAction());
		toolbarMgr2.add(new AddCurvedLineAction());
		toolbarMgr2.add(new Separator());
		*/


		toolbarMgr2.add(new ZoomInAction(form2));
		toolbarMgr2.add(new ZoomOutAction(form2));
		toolbarMgr2.add(new Separator());
		toolbarMgr2.add(new PrintViewAction(form2.getPanel()));
		toolbarMgr2.add(new Separator());
		toolbarMgr2.add(new ExportAction(form2.getPanel()));
		toolbarMgr2.add(new Separator());		
		toolbarMgr2.add(new AddInitialAction(form2.getPanel()));
		toolbarMgr2.add(new AddRepetitionAction(form2.getPanel()));
		toolbarMgr2.add(new AddAlternativeAction(form2.getPanel()));
		toolbarMgr2.add(new AddConcatenationAction(form2.getPanel()));
		toolbarMgr2.add(new AddTerminalAction(form2.getPanel()));
		toolbarMgr2.add(new AddSynVariAction(form2.getPanel()));
		toolbarMgr2.add(new AddEpsilonAction(form2.getPanel()));
		toolbarMgr2.add(new Separator());	
		toolbarMgr2.add(new SynDiaDoneAction(form2.getPanel(), mc));

		// Attach all to ViewForm
		form2.setTopCenter(toolbarMgr2.createControl(form2));
		form2.setText(Messages.getString("synDiaEBNF",
			"CreateSynDiaClickGui.Use_the_buttons_below_to_get_a_new_syntax_diagram._1")); //$NON-NLS-1$
		form2.setImage(ImageDescriptor.createFromURL(
			getClass().getResource("/main_pix/new.gif")).createImage());
		
		/* - the original code was "new ClickListener(form2.getPanel());" without an assignment
		 * - didn't understand this because in my way of thinking this would be thrownaway by the
		 * garbage collector. please enlight me!!!
		 * Stephan
		 */
		form2ClickListener = new ClickListener(form2.getPanel());
	}
	
	public Figure getSynDiaPanel() {
		return (Figure) form2.getPanel();
	}
}
