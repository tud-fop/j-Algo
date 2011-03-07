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
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;

import org.jalgo.module.am1simulator.parser.AM1SyntaxKit;
import org.jalgo.module.am1simulator.presenter.Simulator;
import org.jalgo.module.am1simulator.presenter.UpdateEvent;
import org.jalgo.module.am1simulator.presenter.UpdateEventCreater;
import org.jalgo.module.am1simulator.model.am1.SimulationStatement;

import jsyntaxpane.actions.ActionUtils;
import jsyntaxpane.components.Markers;
import jsyntaxpane.components.Markers.SimpleMarker;

/**
 * Panel which is used for the editor.
 * 
 * @author Max Leuth&auml;user
 */
public class EditorPanel extends JPanel implements UpdateEventCreater<String> {
	private JEditorPane codeEditor;
	private JFileChooser fileChooser;
	private boolean modified = false;
	private Simulator simulator;

	private static final long serialVersionUID = 1L;
	private static final String EXAMPLES_PATH = "examples/am1simulator";

	public EditorPanel(final Simulator simulator) {
		this.simulator = simulator;
		setLayout(new BorderLayout());
		codeEditor = new JEditorPane();
		add(new JScrollPane(codeEditor), BorderLayout.CENTER);
		AM1SyntaxKit.initKit();
		codeEditor.setContentType("text/am1");
		codeEditor.setAutoscrolls(true);
		codeEditor.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				try {
					int line = ActionUtils.getLineNumber(codeEditor,
							codeEditor.getCaretPosition());
					if (simulator.getAM1Program() != null) {
						String s = simulator.getAM1Program().get(line)
								.getDescription();
						fireUpdateEvent(new UpdateEvent<String>(simulator
								.getAM1Program().get(line).getCodeText()
								+ "<br /><p>" + s + "</p>"));
					}
				} catch (BadLocationException e1) {
					// Ignored here if the user clicks at an invalid position!
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		codeEditor.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				setModified(true);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				setModified(true);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				setModified(true);
			}
		});
		setBorder(BorderFactory.createTitledBorder("Editor"));
		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(EXAMPLES_PATH));
		fileChooser.addChoosableFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "AM1 source code (*.am1)";
			}

			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().endsWith(".am1");
			}
		});
	}

	/**
	 * @return true if the user has modified the code, false otherwise.
	 */
	public boolean isModified() {
		return modified;
	}

	/**
	 * Set the modified flag to m.
	 * 
	 * @param m
	 */
	public void setModified(boolean m) {
		modified = m;
	}

	/**
	 * @return the editor which is used here to type in new code or load/save
	 *         and edit files.
	 */
	public JEditorPane getEditor() {
		return codeEditor;
	}

	/**
	 * @param line
	 * @return the caret position in the editor at the given line.
	 */
	private int getCaretAtLine(int line) {
		int lines = 0;
		char[] chararr = codeEditor.getText().toCharArray();
		for (int i = 0; i < chararr.length; i++) {
			if (chararr[i] == '\n') {
				lines++;
			}
			if (lines == line)
				return i;
		}
		return 0;
	}

	/**
	 * Remove all markers from the editor.
	 */
	public void removeMarkers() {
		Markers.removeMarkers(codeEditor);
	}

	/**
	 * Mark a specific {@link SimulationStatement} in the editor.
	 * 
	 * @param s
	 */
	public void markStatement(SimulationStatement s) {
		int start = getCaretAtLine(s.getAddress().getLine() - 1);
		int end = s.getCodeText().length() + 1;
		SimpleMarker a = new SimpleMarker(Color.ORANGE);
		Markers.markText(codeEditor, start, start + end, a);
		codeEditor.setCaretPosition(start + end - 1);
	}

	/**
	 * Loads new code if the user confirms the respective dialog.
	 * 
	 * @return the path to the selected file or a empty string if no file was
	 *         selected
	 */
	public String loadCode() {
		if (isModified()) {
			int saveBeforeClose = JOptionPane
					.showConfirmDialog(
							this,
							"The current code has been modified.\nDo you want to save it?",
							"Confirm", JOptionPane.YES_NO_CANCEL_OPTION);

			switch (saveBeforeClose) {
			case JOptionPane.YES_OPTION:
				saveCode();
				setModified(false);
				break;
			case JOptionPane.CANCEL_OPTION:
				return "";
			default:
				break;
			}
		}

		boolean result = fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION;

		if (result) {
			File file = fileChooser.getSelectedFile();

			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(file));

				codeEditor.setText("");
				boolean firstLine = true;

				for (String line; (line = reader.readLine()) != null;) {
					if (firstLine) {
						codeEditor.setText(line);
						firstLine = false;
					} else
						codeEditor.setText(codeEditor.getText() + "\n" + line);
				}
				reader.close();
				setModified(false);
				return fileChooser.getSelectedFile().getAbsolutePath();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(),
						"The file could not be loaded.",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				return "";
			}

		}
		return "";
	}

	/**
	 * Saves the entered code if the user confirms the respective dialog.
	 * 
	 * @return the path if the user confirmed the dialog or "" if not
	 */
	public String saveCode() {
		boolean result = fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION;
		String ret = "";
		if (result) {
			File file = fileChooser.getSelectedFile();
			ret = file.getAbsolutePath();
			PrintWriter printWriter;
			try {
				printWriter = new PrintWriter(new FileWriter(file));
				printWriter.print(codeEditor.getText());
				printWriter.close();
				setModified(false);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(),
						"File could not be saved.", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				return "";
			}
		}
		return ret;
	}

	@Override
	public void fireUpdateEvent(UpdateEvent<String> update) {
		simulator.getView().getConsolePanel().handleUpdateEvent(update);
	}
}
