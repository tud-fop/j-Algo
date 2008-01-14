package org.jalgo.module.hoare.gui.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.gui.GuiControl;

public class LoadCNullProg extends AbstractAction {
	/**
	 * loads C0-Program from .c-file
	 *
	 * @author:Markus
	 */
	
	private static final long serialVersionUID = 1L;

	private GuiControl gui;

	public LoadCNullProg(GuiControl gui) {
		this.gui = gui;

		this.putValue(NAME, Messages.getString("hoare", "name.openC0Program"));
		// this.putValue(SHORT_DESCRIPTION, Messages.getString("hoare",
		// "ttt.openC0Program"));
		this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.openC0Program")));
	}

	public void actionPerformed(ActionEvent arg0) {

		JFileChooser fc = new JFileChooser(System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "examples"
				+ System.getProperty("file.separator") + "hoare");
		fc.setFileFilter(new FileFilter() {

			@Override
			public boolean accept(File f) {
				if (f.getName().endsWith("c"))
					return true;
				if (f.isDirectory())
					return true;
				return false;
			}

			@Override
			public String getDescription() {
				return "*.c";
			}

		});
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			if (gui.init(fc.getSelectedFile())) // parsing succeeded
				gui.installWorkScreen();
		}

	}

}
