/*
 * Created on 18.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.gui;

import java.io.Serializable;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.jalgo.main.gfx.ClickListener;
import org.jalgo.main.gui.actions.ExportAction;
import org.jalgo.main.gui.actions.PrintViewAction;
import org.jalgo.main.gui.actions.ZoomInAction;
import org.jalgo.main.gui.actions.ZoomOutAction;
import org.jalgo.main.gui.widgets.GraphViewForm;
import org.jalgo.main.util.GfxUtil;
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

	private GraphViewForm form2;
	private ModuleController mc;

	public CreateSynDiaClickGui(Composite parent, ModuleController mc)
	{
		super(parent);
		
		this.mc = mc;
		
		GfxUtil.setAppShell(parent.getShell());

		form2 = new GraphViewForm(parent, SWT.BORDER);
		
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

		ToolBar toolbar2 = toolbarMgr2.createControl(form2);

		//		Attach all to ViewForm
		form2.setTopCenter(toolbar2);
		form2.setText(Messages.getString("CreateSynDiaClickGui.Use_the_buttons_below_to_get_a_new_syntax_diagram._1")); //$NON-NLS-1$
		form2.setImage(new Image(parent.getDisplay(), "./pix/new.gif")); //$NON-NLS-1$

		new ClickListener(form2.getPanel());

	}
	
	public Figure getSynDiaPanel() {
		return (Figure) form2.getPanel();
	}

}
