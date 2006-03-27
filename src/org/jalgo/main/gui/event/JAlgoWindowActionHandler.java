package org.jalgo.main.gui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;

import org.jalgo.main.JAlgoMain;
import org.jalgo.main.gui.JAlgoWindow;
import org.jalgo.main.gui.components.AboutFrame;
import org.jalgo.main.gui.components.ModuleChooseDialog;
import org.jalgo.main.gui.components.PreferencesDialog;

/**
 * This class is responsible for handling action events of the j-Algo main
 * program.
 * 
 * @author Alexander Claus
 */
public class JAlgoWindowActionHandler
extends AbstractAction
implements ActionListener {

	private static final long serialVersionUID = -4399928482802216570L;
	private final JAlgoWindow appWin;

	public JAlgoWindowActionHandler(JAlgoWindow appWin) {
		this.appWin = appWin;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().startsWith("new"))
			JAlgoMain.getInstance().newInstanceByName(
				e.getActionCommand().substring(3));
		else if (e.getActionCommand().equals("ui.New") ||
			(e.getSource() instanceof AbstractButton &&
			((AbstractButton)e.getSource()).getActionCommand().equals("ui.New")))
			ModuleChooseDialog.open(appWin);
		else if (e.getActionCommand().equals("ui.Open_file"))
			appWin.showOpenDialog(true, false);
		else if (e.getActionCommand().equals("ui.Save_file"))
			JAlgoMain.getInstance().saveFile();
		else if (e.getActionCommand().equals("ui.Save_as"))
			appWin.showSaveDialog(true);
		else if (e.getActionCommand().equals("ui.Close"))
			appWin.tabClosed();
		else if (e.getActionCommand().equals("ui.Help_contents"))
			System.out.println("help requested");
		else if (e.getActionCommand().equals("ui.About"))
			AboutFrame.openAboutJAlgoFrame(appWin);
		else if (e.getActionCommand().equals("ui.About_module"))
			AboutFrame.openAboutModuleFrame(appWin);
		else if (e.getActionCommand().equals("ui.Prefs"))
			PreferencesDialog.open(appWin);
		else if (e.getActionCommand().equals("ui.Exit"))
			appWin.handleCloseEvent();
	}
}