/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2010 j-Algo-Team, j-algo-development@lists.sourceforge.net
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
package org.jalgo.module.am0c0.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.jalgo.main.util.Messages;
import org.jalgo.module.am0c0.core.Editor;
import org.jalgo.module.am0c0.core.ILoveCandy;
import org.jalgo.module.am0c0.gui.GuiConstants;
import org.jalgo.module.am0c0.gui.View;
import org.jalgo.module.am0c0.gui.jeditor.JEditor;
import org.jalgo.module.am0c0.gui.jeditor.jedit.tokenmarker.CTokenMarker;

/**
 * This class represents the Editor in the JAlgo module C0/AM0.
 * 
 * @author Max Leuth&auml;user
 * @author Felix Schmitt
 */
public class EditorView extends View {
	private static final long serialVersionUID = 1L;
	private JPanel buttonFields, buttonPanel, buttonPanel2, contentPanel;
	public JEditor codeField;
	private JEditorPane logConsole;
	private JButton loadButton;
	private JButton saveButton;
	private JButton runButton;
	private JButton validateButton;
	public ButtonGroup codeSelector;
	public JRadioButton c0Button, am0Button;
	private JSplitPane hSplitPane;
	private ButtonHandler buttonHandler;
	private Editor editorController;
	public JTabbedPane tabPane;

	public EditorView(Editor editorController) {
		this.editorController = editorController;
		initComponents();
		initComponentAttributs();
		attachButtonHandler();
		add(contentPanel);
	}

	/**
	 * Set the text of the console.
	 * 
	 * @param text
	 *            the text which should be printed
	 */
	public void setConsoleText(String text) {
		logConsole.setText("<html><body style=\"background-color: #ffffff;\">" //$NON-NLS-1$
				+ text + "</body></html>"); //$NON-NLS-1$
	}

	/**
	 * Delete the current content of the console.
	 */
	public void clearConsoleText() {
		logConsole.setText(null);
	}

	/**
	 * @return the {@link JEditor} from this {@link EditorView}.
	 */
	public JEditor getJEditor() {
		return codeField;
	}

	@Override
	protected void initComponents() {
		buttonHandler = new ButtonHandler();
		loadButton = new JButton(Messages.getString("am0c0", "EditorView.2")); //$NON-NLS-1$
		loadButton.setIcon(new ImageIcon(Messages.getResourceURL("am0c0", //$NON-NLS-1$
				GuiConstants.OPEN_ICON)));
		saveButton = new JButton(Messages.getString("am0c0", "EditorView.4")); //$NON-NLS-1$
		saveButton.setIcon(new ImageIcon(Messages.getResourceURL("am0c0", //$NON-NLS-1$
				GuiConstants.SAVE_ICON)));

		validateButton = new JButton(
				Messages.getString("am0c0", "EditorView.6")); //$NON-NLS-1$

		runButton = new JButton(Messages.getString("am0c0", "EditorView.7")); //$NON-NLS-1$
		runButton.setIcon(new ImageIcon(Messages.getResourceURL("am0c0", //$NON-NLS-1$
				GuiConstants.RUN_ICON)));
		runButton.setEnabled(false);

		codeSelector = new ButtonGroup();
		c0Button = new JRadioButton(Messages.getString("am0c0", "EditorView.9")); //$NON-NLS-1$
		c0Button.setSelected(true);
		am0Button = new JRadioButton(Messages.getString(
				"am0c0", "EditorView.10")); //$NON-NLS-1$

		buttonPanel = new JPanel();
		buttonPanel2 = new JPanel();

		buttonFields = new JPanel();

		codeField = new JEditor(new Observer() {
			public void update(Observable arg0, Object arg1) {
				if (((JEditor) arg1).isModified()) {
					tabPane.setForeground(Color.RED);
					tabPane.setFont(tabPane.getFont().deriveFont(Font.ITALIC));
					if (c0Button.isSelected())
						editorController.setState(new Editor.StateNotValidatedC0());
					else
						editorController
								.setState(new Editor.StateNotValidatedAM0());
					runButton.setEnabled(false);
				}
				;
			}
		}, null);
		codeField.setTokenMarker(new CTokenMarker());
		codeField.setCaretVisible(false);

		codeField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent arg0) {
				int cPos = codeField.getCaretPosition();
				int lineNumber = codeField.getLineOfOffset(cPos);
				int startOffset = codeField.getLineStartOffset(lineNumber);
				String str = codeField.getText(startOffset,
						(cPos - startOffset));
				int len = str.length() + 1;

				editorController.getController().writeOnStatusbar(
						Messages.getString("am0c0", "EditorView.23")
								+ (lineNumber + 1)
								+ Messages.getString("am0c0", "EditorView.24")
								+ len);
			}
		});

		logConsole = new JEditorPane("text/html", null); //$NON-NLS-1$
		logConsole.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
		logConsole.setFont(GuiConstants.STANDARDFONT_SERIF);
		JTabbedPane tab = new JTabbedPane();
		JScrollPane logScrollPane = getScrollPane(logConsole);
		logScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		tab.addTab(Messages.getString("am0c0", "EditorView.12"), logScrollPane); //$NON-NLS-1$
		tabPane = new JTabbedPane();
		tabPane.addTab(Messages.getString("am0c0", "EditorView.25"), codeField);
		hSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, tabPane,
				tab);

		contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(hSplitPane, BorderLayout.CENTER);
		contentPanel.add(buttonFields, BorderLayout.EAST);
	}

	@Override
	protected void attachButtonHandler() {
		loadButton.addActionListener(buttonHandler);
		saveButton.addActionListener(buttonHandler);
		runButton.addActionListener(buttonHandler);
		validateButton.addActionListener(buttonHandler);
		am0Button.addActionListener(buttonHandler);
		c0Button.addActionListener(buttonHandler);

		loadButton.addMouseListener(buttonHandler);
		saveButton.addMouseListener(buttonHandler);
		runButton.addMouseListener(buttonHandler);
		validateButton.addMouseListener(buttonHandler);
		am0Button.addMouseListener(buttonHandler);
		c0Button.addMouseListener(buttonHandler);
	}

	@Override
	protected void initComponentAttributs() {
		setLayout(new BorderLayout());

		codeSelector.add(am0Button);
		codeSelector.add(c0Button);

		buttonPanel.setLayout(new BorderLayout());
		buttonPanel.add(loadButton, BorderLayout.NORTH);
		buttonPanel.add(saveButton, BorderLayout.CENTER);
		JPanel p = new JPanel();
		p.add(c0Button);
		p.add(am0Button);
		buttonPanel.add(p, BorderLayout.SOUTH);

		buttonFields.setLayout(new BorderLayout());
		buttonFields.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.gray),
				Messages.getString("am0c0", "EditorView.13"))); //$NON-NLS-1$

		buttonPanel2.setLayout(new BorderLayout());
		buttonPanel2.add(this.validateButton, BorderLayout.NORTH);
		buttonPanel2.add(this.runButton, BorderLayout.SOUTH);

		buttonFields.add(buttonPanel, BorderLayout.PAGE_START);
		buttonFields.add(buttonPanel2, BorderLayout.PAGE_END);

		logConsole.setEditable(false);

		hSplitPane.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.gray),
				Messages.getString("am0c0", "EditorView.14"))); //$NON-NLS-1$

		hSplitPane
				.setDividerLocation(GuiConstants.getJAlgoWindow().getSize().height / 2);

		hSplitPane.setOneTouchExpandable(true);
	}

	private void startCandy() {
		loadButton.setEnabled(false);
		saveButton.setEnabled(false);
		runButton.setEnabled(false);
		validateButton.setEnabled(false);
		hSplitPane.setLeftComponent(new ILoveCandy(this));
		hSplitPane
				.setDividerLocation(GuiConstants.getJAlgoWindow().getHeight() / 2);
		hSplitPane.getLeftComponent().requestFocus();
	}

	public void stopCandy() {
		loadButton.setEnabled(true);
		saveButton.setEnabled(true);
		validateButton.setEnabled(true);
		hSplitPane.setLeftComponent(codeField);
	}

	private class ButtonHandler implements ActionListener, MouseListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == loadButton) {
				String s = editorController.loadCode();
				if (!s.isEmpty()) {
					tabPane.setForeground(Color.BLACK);
					tabPane.setFont(tabPane.getFont().deriveFont(Font.PLAIN));
					tabPane.setTitleAt(0, s);
				}
				runButton.setEnabled(false);
			}
			if (e.getSource() == saveButton) {
				String s = editorController.saveCode();
				if (!s.isEmpty()) {
					if (c0Button.isSelected())
						editorController
								.setState(new Editor.StateNotValidatedC0());
					else
						editorController
								.setState(new Editor.StateNotValidatedAM0());
					tabPane.setForeground(Color.BLACK);
					tabPane.setFont(tabPane.getFont().deriveFont(Font.PLAIN));
					tabPane.setTitleAt(0, s);
				}
				runButton.setEnabled(false);
			}

			if (e.getSource() == validateButton) {
				if (codeField.getText().toLowerCase().equals("ilovecandy")) {
					startCandy();
				} else {
					clearConsoleText();
					if (!codeField.getText().trim().isEmpty()) {
						if (codeSelector.isSelected(am0Button.getModel())) {
							runButton.setEnabled(editorController
									.validate(Editor.AM0));
						}

						if (codeSelector.isSelected(c0Button.getModel())) {
							runButton.setEnabled(editorController
									.validate(Editor.C0));
						}
					}
				}
			}

			if (e.getSource() == runButton) {
				if (!codeField.getText().trim().isEmpty()) {
					if (codeSelector.isSelected(am0Button.getModel())) {
						editorController
								.getController()
								.getSimulator()
								.setAM0Program(
										editorController.getState()
												.getAM0Program());
						editorController.getController().showSimulator();
					}

					if (codeSelector.isSelected(c0Button.getModel())) {
						if (codeField.isModified()) {
							int choice = JOptionPane.NO_OPTION;
							choice = JOptionPane
									.showConfirmDialog(
											editorController.getView(),
											Messages.getString(
													"am0c0", "EditorView.15"), //$NON-NLS-1$
											Messages.getString(
													"am0c0", "EditorView.16"), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$
							if (choice == JOptionPane.YES_OPTION) {
								codeField.saveCode();
							}
						}
						editorController
								.getController()
								.getTransformator()
								.setC0Program(
										editorController.getState()
												.getC0Program());

						editorController.getController().showTransformator();
					}
				}
			}
			if (e.getSource() == am0Button) {
				runButton.setEnabled(false);
				editorController.setState(new Editor.StateNotValidatedAM0());
			}
			if (e.getSource() == c0Button) {
				runButton.setEnabled(false);
				editorController.setState(new Editor.StateNotValidatedC0());
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			if (e.getSource() == loadButton) {
				editorController.getController().writeOnStatusbar(
						Messages.getString("am0c0", "EditorView.17")); //$NON-NLS-1$
			}
			if (e.getSource() == saveButton) {
				editorController.getController().writeOnStatusbar(
						Messages.getString("am0c0", "EditorView.18")); //$NON-NLS-1$
			}
			if (e.getSource() == runButton) {
				editorController.getController().writeOnStatusbar(
						Messages.getString("am0c0", "EditorView.19")); //$NON-NLS-1$

			}
			if (e.getSource() == validateButton) {
				editorController.getController().writeOnStatusbar(
						Messages.getString("am0c0", "EditorView.20")); //$NON-NLS-1$
			}
			if (e.getSource() == am0Button) {
				editorController.getController().writeOnStatusbar(
						Messages.getString("am0c0", "EditorView.21")); //$NON-NLS-1$
			}
			if (e.getSource() == c0Button) {
				editorController.getController().writeOnStatusbar(
						Messages.getString("am0c0", "EditorView.22")); //$NON-NLS-1$
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			editorController.getController().writeOnStatusbar(""); //$NON-NLS-1$
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}
	}

	@Override
	public void setPresentationMode(boolean presentationMode) {
		if (presentationMode) {
			codeField.setFont(GuiConstants.PRESENTATIONFONT);
			logConsole.setFont(GuiConstants.PRESENTATIONFONT_SERIF);
		} else {
			codeField.setFont(GuiConstants.STANDARDFONT);
			logConsole.setFont(GuiConstants.STANDARDFONT_SERIF);
		}
	}
}
