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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import jsyntaxpane.actions.CaretMonitor;

import org.jalgo.module.am1simulator.view.components.MainPanel;
import org.jalgo.module.am1simulator.view.components.ConsolePanel;
import org.jalgo.module.am1simulator.view.components.EditorPanel;
import org.jalgo.module.am1simulator.view.components.TablePanel;
import org.jalgo.module.am1simulator.view.components.Toolbar;

/**
 * Class which contains all view elements and regulate the access on them. This
 * class uses a builder pattern. Null arguments are <b>not</b> allowed and
 * causes an {@link IllegalArgumentException}.
 * 
 * @author Max Leuth&auml;user
 */
public class ViewContainer {
	private final ConsolePanel console;
	private final EditorPanel editor;
	private final TablePanel table;
	private final Toolbar toolbar;
	private final MainPanel mainPanel;
	private final JLabel position, file;

	public static class Builder {
		// Required Parameters
		private final MainPanel mainPanel;

		// Optional parameters - not initialized
		private ConsolePanel console;
		private EditorPanel editor;
		private TablePanel table;
		private Toolbar toolbar;
		private JLabel position, file;

		public Builder(MainPanel mainPanel) {
			if (mainPanel == null) {
				throw new IllegalArgumentException(
						"Null arguments are not allowed!");
			}
			this.mainPanel = mainPanel;
		}

		public Builder console(ConsolePanel con) {
			if (con == null) {
				throw new IllegalArgumentException(
						"Null arguments are not allowed!");
			}
			console = con;
			return this;
		}

		public Builder editor(EditorPanel edit) {
			if (edit == null) {
				throw new IllegalArgumentException(
						"Null arguments are not allowed!");
			}
			editor = edit;
			return this;
		}

		public Builder table(TablePanel tab) {
			if (tab == null) {
				throw new IllegalArgumentException(
						"Null arguments are not allowed!");
			}
			table = tab;
			return this;
		}

		public Builder toolbar(Toolbar tool) {
			if (tool == null) {
				throw new IllegalArgumentException(
						"Null arguments are not allowed!");
			}
			toolbar = tool;
			return this;
		}

		public Builder postion(JLabel pos) {
			if (pos == null) {
				throw new IllegalArgumentException(
						"Null arguments are not allowed!");
			}
			position = pos;
			return this;
		}

		public Builder file(JLabel f) {
			if (f == null) {
				throw new IllegalArgumentException(
						"Null arguments are not allowed!");
			}
			file = f;
			return this;
		}

		public ViewContainer build() {
			return new ViewContainer(this);
		}
	}

	private ViewContainer(Builder builder) {
		if (builder == null) {
			throw new IllegalArgumentException(
					"Null arguments are not allowed!");
		}
		mainPanel = builder.mainPanel;
		console = builder.console;
		editor = builder.editor;
		table = builder.table;
		toolbar = builder.toolbar;
		position = builder.position;
		file = builder.file;

		assembleInterface();
	}

	/**
	 * Build the interface of this application.
	 */
	private void assembleInterface() {
		editor.getEditor().addCaretListener(
				new CaretMonitor(editor.getEditor(), position));

		JSplitPane vSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				true, editor, table);
		JSplitPane hSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,
				vSplitPane, console);
		hSplitPane.setOneTouchExpandable(true);
		vSplitPane.setOneTouchExpandable(true);

		table.add(toolbar, BorderLayout.NORTH);
		mainPanel.add(hSplitPane, BorderLayout.CENTER);

		JPanel statusBar = new JPanel(new BorderLayout());
		statusBar.add(position, BorderLayout.WEST);
		statusBar.add(file, BorderLayout.EAST);
		statusBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		mainPanel.add(statusBar, BorderLayout.SOUTH);

		mainPanel.doLayout();
	}

	/**
	 * Write the filename of the currently opened file at the statusbar.
	 * 
	 * @param path
	 */
	public void writeFileName(String path) {
		file.setText(path);
		file.setToolTipText(path);
	}

	/**
	 * Change to look of the statusbar to show the user the file he has opened
	 * was edited.
	 * 
	 * @param m
	 */
	public void setStatusbarModified(boolean m) {
		if (m) {
			file.setForeground(Color.RED);
			file.setFont(file.getFont().deriveFont(Font.ITALIC));
		} else {
			file.setForeground(Color.BLACK);
			file.setFont(file.getFont().deriveFont(Font.PLAIN));
		}
	}

	/**
	 * @return the {@link MainPanel}
	 */
	public MainPanel getMainPanel() {
		return mainPanel;
	}

	/**
	 * @return the {@link EditorPanel}
	 */
	public EditorPanel getEditorPanel() {
		return editor;
	}

	/**
	 * @return the {@link ConsolePanel}
	 */
	public ConsolePanel getConsolePanel() {
		return console;
	}

	/**
	 * @return the {@link TablePanel}
	 */
	public TablePanel getTablePanel() {
		return table;
	}

	/**
	 * @return the {@link Toolbar}
	 */
	public Toolbar getToolbar() {
		return toolbar;
	}
}