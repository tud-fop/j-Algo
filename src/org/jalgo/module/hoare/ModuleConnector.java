/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jalgo.module.hoare;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.jalgo.main.AbstractModuleConnector;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.control.ProgramControl;
import org.jalgo.module.hoare.gui.GuiControl;
import org.jalgo.module.hoare.model.ModelControl;

public class ModuleConnector extends AbstractModuleConnector {

	private GuiControl guiCtrl;

	private ModelControl mCtrl;

	private ProgramControl progCtrl;

	private boolean saveStatus;

	@Override
	public void init() {
		mCtrl = new ModelControl();
		guiCtrl = new GuiControl(this);
		progCtrl = new ProgramControl();

		guiCtrl.setModelControl(mCtrl);
		guiCtrl.setProgramControl(progCtrl);

		mCtrl.setGuiControl(guiCtrl);
		mCtrl.setProgramControl(progCtrl);

		progCtrl.setModelControl(mCtrl);
		progCtrl.setGuiControl(guiCtrl);

	}

	@Override
	public void run() {
		guiCtrl.installToolbar();
		guiCtrl.installMenu();

		guiCtrl.installWelcomeScreen();
	}

	@Override
	public void setDataFromFile(ByteArrayInputStream data) {
		try {
			ObjectInputStream ois = new ObjectInputStream(data);
			guiCtrl.load(ois);
			mCtrl.load(ois);
			ois.close();
		} catch (Exception e) {
			guiCtrl.reportError(Messages.getString("hoare", "out.loadError"));
		}
		guiCtrl.installWorkScreen();
		mCtrl.notifyObservers();
	}

	@Override
	public ByteArrayOutputStream getDataForFile() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {

			ObjectOutputStream output = new ObjectOutputStream(baos);
			guiCtrl.save(output);
			mCtrl.save(output);
			output.close();
		} catch (IOException e) {
			guiCtrl.reportError(Messages.getString("hoare", "out.saveError"));
		}
		return baos;
	}

	@Override
	public void print() {
		// here is no action performed in test module
	}

	public void setSaveStatus(boolean status) {
		if (status)
			this.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
		else
			this.setSaveStatus(SaveStatus.NO_CHANGES);

		if (saveStatus != status) {
			JAlgoGUIConnector.getInstance().saveStatusChanged(this);
			saveStatus = status;
		}
		;
	}
}