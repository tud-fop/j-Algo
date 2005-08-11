/*
 * Created on 12.07.2005
 *
 */
package org.jalgo.main.gui;

import java.util.Iterator;
import java.util.Random;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.jalgo.main.IModuleInfo;
import org.jalgo.main.JalgoMain;

public class ModuleChooseDialog extends Dialog implements SelectionListener {

	private JalgoMain main;
	private ListViewer moduleListViewer;
	private List moduleList;
	private Button button;
	private Label logoL;
	private Label descrL;
	
	public ModuleChooseDialog(Shell shell, JalgoMain main) {
		super(shell);
		this.main = main;
	}
	
	public Control createContents(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		GridLayout allLayout = new GridLayout();
		GridLayout topLayout = new GridLayout();
		topLayout.numColumns = 3;
		topLayout.makeColumnsEqualWidth = true;
		topLayout.marginHeight = 20;
		GridLayout downLayout = new GridLayout();
				
		container.setLayout(allLayout);
		
		Composite top = new Composite(container, SWT.NONE);
		top.setLayout(topLayout);
		top.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
		
		Composite down = new Composite(container, SWT.BORDER);
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
		moduleList.setSelection(0);
		moduleListViewer.getControl().setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
	
		// logo
		logoL = new Label(top, SWT.CENTER);
		logoL.setImage(ImageDescriptor.createFromURL(getClass().getResource(
			"/main_pix/jalgo.png")).createImage());
		logoL.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_CENTER));
		
		// start button
		button = new Button(top, SWT.PUSH);
		button.setText("Starten");
		button.setToolTipText("Das ausgewählte Modul starten.");
		button.setEnabled(false);
		button.addSelectionListener(this);
		
		// module description
		descrL = new Label(down, SWT.LEFT | SWT.WRAP);
		descrL.setText("Klicken Sie auf ein Modul, um seine Beschreibung zu sehen.");
		descrL.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL
				| GridData.FILL_BOTH));		

		return container;
		
	}
	
	public Point getInitialSize() {
		return new Point(600,400);
	}
	
	/*public Point getInitialLocation(Point initialSize) {
		Point mainWindowLoc = shell.getLocation();
		System.out.println(mainWindowLoc.x + ", " + mainWindowLoc.y);
	}*/
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Modul-Auswahl");
		Rectangle displayBounds = Display.getCurrent().getBounds();
		newShell.setLocation(
			(displayBounds.width - newShell.getSize().x) / 2,
			(displayBounds.height - newShell.getSize().y) / 2);
	}
	
	// <<--------- implementation of SelectionListener
	
	public void widgetDefaultSelected(SelectionEvent e) {
		
	}
	
	public void widgetSelected(SelectionEvent e) {
		// button is clicked
		if (e.widget == button) {
			main.newInstance(moduleList.getSelectionIndex());
			close();
		} 
		// selection in list is changed
		else if (e.widget == moduleList) {
			button.setEnabled(true);
			int modNum = moduleList.getSelectionIndex();
			// set description text of module
			descrL.setText(((IModuleInfo) main.getKnownModuleInfos().get(modNum)).getName() + 
					"\n\n" + ((IModuleInfo) main.getKnownModuleInfos().get(modNum)).getDescription() +
					"\n\nVersion: " + ((IModuleInfo) main.getKnownModuleInfos().get(modNum)).getVersion() + 
					"\n\nAutoren: " + ((IModuleInfo) main.getKnownModuleInfos().get(modNum)).getAuthor() +
					"\n\nLizenz: " + ((IModuleInfo) main.getKnownModuleInfos().get(modNum)).getLicense());
			// show module's logo
			logoL.setImage(((IModuleInfo) main.getKnownModuleInfos().get(modNum)).getLogo().createImage());
		}
 	}
	
	// implementation of SelectionListener ------------------->>
	
}
