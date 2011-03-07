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

package org.jalgo.module.am1simulator.view.components;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import org.jalgo.module.am1simulator.presenter.UpdateEvent;
import org.jalgo.module.am1simulator.presenter.UpdateListener;

/**
 * Panel which is used to show some text to the user, like AM1 statement
 * descriptions and parser output.
 * 
 * @author Max Leuth&auml;user
 */
public class ConsolePanel extends JPanel implements UpdateListener<String> {
	private static final long serialVersionUID = 1L;
	private JEditorPane description, console;
	private JTabbedPane tabbet;

	public ConsolePanel() {
		setLayout(new GridLayout(1, 2));
		description = new JEditorPane();
		console = new JEditorPane();

		description.setEditable(false);
		description.setContentType("text/html");
		description.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES,
				true);
		console.setEditable(false);
		console.setContentType("text/html");
		console.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

		tabbet = new JTabbedPane();
		tabbet.add("Console", new JScrollPane(console));
		tabbet.add("Description", new JScrollPane(description));

		JPanel output = new JPanel();
		output.setLayout(new BorderLayout());
		output.setBorder(BorderFactory.createTitledBorder("Output:"));
		output.add(tabbet, BorderLayout.CENTER);

		add(output);
	}

	/**
	 * @return the {@link JEditorPane} which is used to show AM1 statement
	 *         descriptions to the user.
	 */
	public JEditorPane getDescriptionEditor() {
		return description;
	}

	/**
	 * @return the {@link JEditorPane} which is used to show the parser output
	 *         to the user.
	 */
	public JEditorPane getConsole() {
		return console;
	}

	/**
	 * Switch to the {@link JEditorPane} which is used to show the parser output
	 * to the user.
	 */
	public void switchToConsole() {
		tabbet.setSelectedIndex(0);
	}

	/**
	 * Switch to the {@link JEditorPane} which is used to show AM1 statement
	 * descriptions to the user.
	 */
	public void switchToDescription() {
		tabbet.setSelectedIndex(1);
	}

	@Override
	public void handleUpdateEvent(UpdateEvent<String> event) {
		description.setText(event.getValues().get(0));
	}
}
