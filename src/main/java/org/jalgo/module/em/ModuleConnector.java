package org.jalgo.module.em;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JToolBar;

import org.jalgo.main.AbstractModuleConnector;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.module.em.control.Controller;

/** Connection between main program and the em module.
 * 
 * @author Christian Hendel
 */
public class ModuleConnector extends AbstractModuleConnector {
	//private Controller cont;
	private JComponent content;
	private JMenu menu;
	private JToolBar toolbar;
	
	

	@Override
	public void init() {
		content = JAlgoGUIConnector.getInstance().getModuleComponent(this);
		menu = JAlgoGUIConnector.getInstance().getModuleMenu(this);
		toolbar =JAlgoGUIConnector.getInstance().getModuleToolbar(this);
		//cont = new Controller(content,menu,toolbar);
		new Controller(content, menu, toolbar);
	}

	@Override
	public void run() {
	}

	@Override
	public void setDataFromFile(ByteArrayInputStream data) {

	}

	@Override
	public ByteArrayOutputStream getDataForFile() {
		return null;
	}

	@Override
	public void print() {
		// here is no action performed in test module
	}

}
