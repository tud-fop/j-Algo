package org.jalgo.module.lambda;

import org.jalgo.module.lambda.controller.Controller;
import org.jalgo.module.lambda.controller.IController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.jalgo.main.AbstractModuleConnector;
import org.jalgo.main.gui.JAlgoGUIConnector;

public class ModuleConnector extends AbstractModuleConnector {

	private IController controller;
	
	@Override
	public void init() {
		JAlgoGUIConnector.getInstance().getModuleMenu(this).setEnabled(false);
		controller = new Controller(this);
	}

	@Override
	public void run() {
		controller.run();
	}

	@Override
	public void setDataFromFile(ByteArrayInputStream data) {
        try {
			ObjectInputStream in = new ObjectInputStream(data);
			controller.loadData(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ByteArrayOutputStream getDataForFile() {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream objOut = new ObjectOutputStream(out);
			controller.saveData(objOut);
            out.close();
			return out;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void print() {
	}

}