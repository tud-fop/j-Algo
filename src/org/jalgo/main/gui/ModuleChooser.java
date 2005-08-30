/*
 * Created on 29.06.2005
 *
 */
package org.jalgo.main.gui;

import java.util.Iterator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.jalgo.main.IModuleInfo;
import org.jalgo.main.JalgoMain;

public class ModuleChooser extends Composite implements SelectionListener {
	
	private JalgoMain main;
	private CTabItem cti;
	private ListViewer moduleListViewer;
	private List moduleList;
	private Button button;
	private Label logoL;
	private Label descrL;
	
	public ModuleChooser(JalgoMain main, CTabItem cti, Composite parent, int style) {
		super(parent, SWT.NONE);
		
		this.main = main;
		this.cti = cti;
		
		GridData allGD = new GridData();
		allGD.grabExcessHorizontalSpace = true;
		allGD.grabExcessVerticalSpace = true;
		setLayoutData(allGD);
		
		GridLayout allLayout = new GridLayout();
		GridLayout topLayout = new GridLayout();
		topLayout.numColumns = 3;
		topLayout.makeColumnsEqualWidth = true;
		GridLayout downLayout = new GridLayout();
				
		setLayout(allLayout);
		
		Composite top = new Composite(this, SWT.NONE);
		top.setLayout(topLayout);
		top.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
		
		Composite down = new Composite(this, SWT.BORDER);
		down.setLayout(downLayout);
		down.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL
				| GridData.FILL_BOTH));
		
		moduleList = new List(top, SWT.BORDER | SWT.SIMPLE);
		moduleListViewer = new ListViewer(moduleList);
		for (Iterator modIt = main.getKnownModuleInfos().iterator(); modIt.hasNext();) {
			IModuleInfo modInfo = (IModuleInfo) modIt.next();
			moduleList.add(modInfo.getName());
		}
		moduleList.addSelectionListener(this);
		moduleListViewer.getControl().setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
		
		
		// logo
		logoL = new Label(top, SWT.CENTER);
		logoL.setImage(ImageDescriptor.createFromURL(getClass().getResource(
			"/main_pix/jalgo.png")).createImage());
		logoL.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
		
		// start button
		button = new Button(top, SWT.PUSH);
		button.setText("Starten");
		button.setToolTipText("Das ausgew�hlte Modul starten.");
		button.setEnabled(false);
		button.addSelectionListener(this);
		
		// module description
		descrL = new Label(down, SWT.LEFT | SWT.WRAP);
		descrL.setText("Klicken Sie auf ein Modul, um seine Beschreibung zu sehen.");
		descrL.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL
				| GridData.FILL_BOTH));		
		
		
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
	// this method has no effect		
	}
	
	public void widgetSelected(SelectionEvent e) {
		// button is clicked
		if (e.widget == button) {
			main.newInstance(moduleList.getSelectionIndex());
			cti.dispose();
		} 
		// selection in list is changed
		else if (e.widget == moduleList) {
			button.setEnabled(true);
			int modNum = moduleList.getSelectionIndex();
			// set description text of module
			descrL.setText(main.getKnownModuleInfos().get(modNum).getName() + 
					"\n\n" + main.getKnownModuleInfos().get(modNum).getDescription() +
					"\n\nVersion: " + main.getKnownModuleInfos().get(modNum).getVersion() + 
					"\n\nAutoren: " + main.getKnownModuleInfos().get(modNum).getAuthor() +
					"\n\nLizenz: " + main.getKnownModuleInfos().get(modNum).getLicense());
			// show module's logo
			logoL.setImage(main.getKnownModuleInfos().get(modNum).getLogo().createImage());
		}
 	}
	
	
}
