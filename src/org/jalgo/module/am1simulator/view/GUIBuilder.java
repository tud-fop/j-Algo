/**
 * AM1 Simulator - simulating am1 code in an abstract machine based on the
 * definitions of the lectures 'Programmierung' at TU Dresden.
 * Copyright (C) 2010 Max Leuthäuser
 * Contact: s7060241@mail.zih.tu-dresden.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jalgo.module.am1simulator.view;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.jalgo.main.util.Messages;
import org.jalgo.module.am1simulator.presenter.Simulator;
import org.jalgo.module.am1simulator.view.components.ConsolePanel;
import org.jalgo.module.am1simulator.view.components.EditorPanel;
import org.jalgo.module.am1simulator.view.components.MainPanel;
import org.jalgo.module.am1simulator.view.components.TablePanel;
import org.jalgo.module.am1simulator.view.components.Toolbar;

/**
 * Class which builds the complete GUI with all its elements. Implementation is
 * characterized through static factory methods.
 * 
 * @author Max Leuth&auml;user
 */
public final class GUIBuilder {
	private GUIBuilder() {
		throw new AssertionError("This class is not ment to be initilized!");
	}

	public static MainPanel newMainPanel() {
		return new MainPanel();
	}

	public static JLabel newDefaultJLabel() {
		return new JLabel();
	}

	public static JLabel newFileJLabel() {
		JLabel result = new JLabel(new ImageIcon(Messages.getResourceURL(
				"am1simulator", "file_Icon")));
		result.setText("New file");
		return result;
	}

	public static ConsolePanel newConsolePanel() {
		ConsolePanel result = new ConsolePanel();
		result.setMaximumSize(new Dimension(800, 200));
		result.setMinimumSize(new Dimension(800, 180));
		return result;
	}

	public static TablePanel newTablePanel(Simulator simulator) {
		TablePanel result = new TablePanel(simulator);
		result.setMinimumSize(new Dimension(500, 370));
		return result;
	}

	public static EditorPanel newEditorPanel(Simulator simulator) {
		EditorPanel result = new EditorPanel(simulator);
		result.setMaximumSize(new Dimension(180, 370));
		result.setMinimumSize(new Dimension(220, 370));
		return result;
	}

	public static Toolbar newToolbar() {
		return new Toolbar();
	}
}
