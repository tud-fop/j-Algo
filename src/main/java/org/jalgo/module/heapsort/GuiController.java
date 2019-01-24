/*
 * j-Algo - a visualization tool for algorithm runs, especially useful for
 * students and lecturers of computer science. j-Algo is written in Java and
 * thus platform independent. Development is supported by Technische Universität
 * Dresden.
 *
 * Copyright (C) 2004-2008 j-Algo-Team, j-algo-development@lists.sourceforge.net
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
package org.jalgo.module.heapsort;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.jalgo.main.gui.components.JToolbarButton;
import org.jalgo.module.heapsort.anim.TimeEntity;
import org.jalgo.module.heapsort.model.Action;
import org.jalgo.module.heapsort.model.ModelListener;
import org.jalgo.module.heapsort.model.SequencerListener;
import org.jalgo.module.heapsort.model.State;
import org.jalgo.module.heapsort.model.Heapsort.Actions;
import org.jalgo.module.heapsort.model.Heapsort.HeapsortState;
import org.jalgo.module.heapsort.model.Heapsort.Phase12sl;
import org.jalgo.module.heapsort.renderer.CanvasEntity;
import org.jalgo.module.heapsort.renderer.Renderer;
import org.jalgo.module.heapsort.vis.Controller;
import org.jalgo.module.heapsort.vis.ControllerListener;

/**
 * @author mbue
 *
 */
public class GuiController implements ControllerListener, ModelListener {

	private JComponent rootPane;
	private JToolBar toolbar;
	private JCheckBoxMenuItem lecture;
	private GState state;
	private Controller ctrl;
	private Renderer renderer;
	private CanvasEntity root;
	private TimeEntity timeroot;
	private Thread thread;

	public GuiController(JComponent rootPane, JToolBar toolbar, JMenu menu,
			Controller controller, Renderer renderer, CanvasEntity root,
			TimeEntity timeroot) {
		this.rootPane = rootPane;
		this.toolbar = toolbar;
		this.ctrl = controller;
		this.renderer = renderer;
		this.root = root;
		this.timeroot = timeroot;
		// create everything visible
		rootPane.setLayout(new BorderLayout());
		// renderer will be in the center area
		renderer.init(this.rootPane);
		lecture = new JCheckBoxMenuItem(Util.getString("Gui.Menu_lecture"));
		lecture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrl.setLecture(!ctrl.isLecture());
			}
		});
		menu.add(lecture);
		state = new InputState();
		// add listeners
		this.ctrl.addListener(this);
		((Subject<ModelListener>)this.ctrl.getModel()).addListener(this);
		// default: lecture mode
		this.ctrl.setLecture(true);
	}
	
	public void run() {
		// start thread
		thread = new Thread(new UpdateThread());
		thread.start();
	}
	
	public void dispose() {
		state.dispose();
		thread = null;
		renderer.dispose();
	}

	public void stateChanged() {
		lecture.setSelected(ctrl.isLecture());
		state.updateButtons();
	}

	public void modelChanged() {
		state.modelChanged();
	}
	
	private void setState(GState s) {
		state.dispose();
		state = null;
		state = s;
	}
	
	private interface GState {
		void updateButtons();
		void modelChanged();
		void dispose();
	}
	
	private class InputState implements GState {
		
		private JPanel inputPane;
		private JLabel label;
		private JTextField edit;
		private JButton addButton;
		private JButton startButton;
		private JTextArea hints;
		private boolean waserror = false;
		
		public InputState() {
			inputPane = new JPanel(new GridBagLayout());
			inputPane.setBorder(BorderFactory.createTitledBorder(Util.getString("Gui.Input_title")));
			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(1,1,2,1);
			
			label = new JLabel(Util.getString("Gui.Input_label"));
			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 2;
			inputPane.add(label, c);
			
			edit = new JTextField(2);
			edit.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (!edit.getText().equals("")) {
						addButton.doClick();
						edit.setText("");
					}
					else
						startButton.doClick();
				}
				
			});
			c.gridx = 2;
			c.gridy = 0;
			c.gridwidth = 1;
			inputPane.add(edit, c);
			
			addButton = new JButton(Util.getString("Gui.Input_add"));
			addButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					synchronized(root) {
						try {
							int i = Integer.parseInt(edit.getText());
							if ((i > 0) && (i < 100))
								((org.jalgo.module.heapsort.model.Heapsort)ctrl.getModel()).addNumber(i);
							if (waserror) {
								hints.setText("");
								waserror = false;
							}
						}
						catch (NumberFormatException e1) {
							; // just ignore non-numeric input
						}
						catch (IllegalArgumentException e1) {
							hints.setText(Util.getString("Gui.Hint_double"));
							waserror = true;
						}
					}
				}
				
			});
			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = GridBagConstraints.REMAINDER;
			inputPane.add(addButton, c);
			
			startButton = new JButton(Util.getString("Gui.Input_start"));
			startButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					synchronized(root) {
						ctrl.cont();
						setState(new DerivationState());
					}							
				}
				
			});
			c.gridx = 0;
			c.gridy = 2;
			inputPane.add(startButton, c);
			
			hints = new JTextArea(17, 10);
			hints.setBackground(inputPane.getBackground());
			hints.setFont(label.getFont());
			hints.setEditable(false);
			hints.setLineWrap(true);
			hints.setWrapStyleWord(true);
			hints.setText(Util.getString("Gui.Hint_instructions"));
			c.gridx = 0;
			c.gridy = 3;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.insets = new Insets(12, 1, 1, 1);
			inputPane.add(hints, c);
			
			rootPane.add(inputPane, BorderLayout.EAST);
			edit.requestFocus();
		}

		public void dispose() {
			rootPane.remove(inputPane);
			inputPane.removeAll();
			inputPane = null;
			label = null;
			edit = null;
			addButton = null;
			startButton = null;
			hints = null;
			rootPane.validate();
		}

		public void updateButtons() {
			addButton.setEnabled(ctrl.canModelChange() &&
					(((HeapsortState)ctrl.getCurrentState()).sequence.size() < 31));
			startButton.setEnabled(ctrl.isStepPossible());
			if (!ctrl.isStepPossible())
				edit.requestFocus();
		}

		public void modelChanged() {
			if (((HeapsortState)ctrl.getCurrentState()).sequence.size() >= 31) {
				hints.setText(Util.getString("Gui.Hint_limit"));
				addButton.setEnabled(false);
			}
		}
		
	}
	
	private class DerivationState implements GState, SequencerListener {
		
		private JToolbarButton reset;
		private JToolbarButton macroback;
		private JToolbarButton back;
		private JToolbarButton suspend;
		private JToolbarButton cont;
		private JToolbarButton macrostep;
		private JToolbarButton finish;
		private JPanel logPane;
		private JList logList;
		private DefaultListModel logger;
		
		public DerivationState() {
			reset = new JToolbarButton(
					new ImageIcon(Util.getMainResourceURL("Icon.Undo_all")),
					Util.getString("Gui.reset"), "");
			reset.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					synchronized(root) {
						ctrl.reset();
						logger.clear(); // FIXME should be done in some (not yet existing) listener
					}
				}
			});			
			
			macroback = new JToolbarButton(
					new ImageIcon(Util.getMainResourceURL("Icon.Undo_blockstep")),
					Util.getString("Gui.macroback"), "");
			macroback.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					synchronized(root) {
						timeroot.setScale(1);
						ctrl.macroBack(0);
					}
				}
			});
			
			back = new JToolbarButton(
					new ImageIcon(Util.getMainResourceURL("Icon.Undo_step")),
					Util.getString("Gui.back"), "");
			back.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					synchronized(root) {
						timeroot.setScale(1);
						ctrl.back();
					}
				}
			});
			
			suspend = new JToolbarButton(
					new ImageIcon(Util.getResourceURL("Icon.Suspend")),
					Util.getString("Gui.suspend"), "");
			suspend.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					synchronized(root) {
						ctrl.suspend();
					}
				}
			});
			
			cont = new JToolbarButton(
					new ImageIcon(Util.getMainResourceURL("Icon.Perform_step")),
					Util.getString("Gui.step"), "");
			cont.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					synchronized(root) {
						System.gc(); // XXX let's see whether this helps
						timeroot.setScale(1);
						ctrl.cont();
					}
				}
			});
			
			macrostep = new JToolbarButton(
					new ImageIcon(Util.getMainResourceURL("Icon.Perform_blockstep")),
					Util.getString("Gui.macrostep"), "");
			macrostep.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					synchronized(root) {
						System.gc(); // XXX let's see whether this helps
						timeroot.setScale(1);
						ctrl.macroStep(0);
					}
				}
			});

			finish = new JToolbarButton(
					new ImageIcon(Util.getMainResourceURL("Icon.Finish_algorithm")),
					Util.getString("Gui.finish"), "");
			finish.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					synchronized(root) {
						System.gc(); // XXX let's see whether this helps
						timeroot.setScale(0.7);
						ctrl.macroStep(-1);
					}
				}
			});
			
			logPane = new JPanel(new BorderLayout());
			logPane.setBorder(BorderFactory.createTitledBorder(Util.getString("Gui.Log_title")));
			logPane.add(new JLabel("<html>"+
					Util.getString("Gui.Log_header")+"<br>"+
					Util.getString("Gui.Log_header2")+"</html>"),
					BorderLayout.NORTH);
			logger = new DefaultListModel();
			logList = new JList(logger);
			logList.setLayoutOrientation(JList.VERTICAL);
			logList.setVisibleRowCount(-1);
			logList.setFixedCellWidth(50);
			JScrollPane sp = new JScrollPane(logList);
			logPane.add(sp, BorderLayout.CENTER);
			logPane.setVisible(!ctrl.isLecture());
			
			toolbar.add(reset);
			toolbar.add(macroback);
			toolbar.add(back);
			toolbar.add(suspend);
			toolbar.add(cont);
			toolbar.add(macrostep);
			toolbar.add(finish);
			toolbar.validate();
			rootPane.add(logPane, BorderLayout.EAST);
			rootPane.validate();
			
			ctrl.addListener(this);
		}

		public void dispose() {
			ctrl.removeListener(this);
			toolbar.remove(reset);
			toolbar.remove(macroback);
			toolbar.remove(back);
			toolbar.remove(suspend);
			toolbar.remove(cont);
			toolbar.remove(macrostep);
			toolbar.remove(finish);
			rootPane.remove(logPane);
			logPane = null;
			logger = null;
		}

		public void updateButtons() {
			ctrl.derive();
			reset.setEnabled(ctrl.isResetPossible());
			macroback.setEnabled(ctrl.isBackPossible());
			back.setEnabled(ctrl.isBackPossible());
			suspend.setEnabled(ctrl.isSuspendPossible());
			cont.setEnabled(ctrl.isStepPossible());
			macrostep.setEnabled(ctrl.isMacroStepPossible());
			finish.setEnabled(ctrl.isMacroStepPossible());
			if (logPane.isVisible() == ctrl.isLecture()) {
				logPane.setVisible(!ctrl.isLecture());
				rootPane.validate();
			}
		}

		public void modelChanged() {
			assert(false); // not expected to happen
		}

		public void back(State q, Action a, State q1) {
			if (logger.getSize() > 0) {
				ListItem li = (ListItem)logger.get(logger.getSize()-1);
				if (li.q == q1)
					logger.remove(logger.getSize()-1);
			}
		}

		public void step(State q, Action a, State q1) {
			if (Actions.isSwap(a)) {
				ListItem li = new ListItem();
				li.q = q;
				li.i = ((Phase12sl)q).i;
				li.j = ((Phase12sl)q1).i;
				logger.addElement(li);
				logList.ensureIndexIsVisible(logger.getSize()-1);
			}
		}

		public void stepAvail() {
		}
		
	}
	
	// this describes a sinkenlassen step
	private static class ListItem {
		public State q;
		public int i;
		public int j;
		
		@Override
		public String toString() {
			return String.format("%d/%d", i, j);
		}
	}
	
	private class UpdateThread implements Runnable {
		
		public void run() {
			Thread me = Thread.currentThread();
			while (thread == me) {
				synchronized(root) {
					// animate...
					timeroot.update(0.000000001d*System.nanoTime());
					// display...
					// (should do nothing if nothing happened)
					Rectangle r;
					do {
						r = renderer.getVisible();
						if (!renderer.validate())
							r = r.intersection(root.computeDirtyRegion());
						renderer.renderVisible(root, r);
					} while (!renderer.show(r));
					root.clearDirtyRegion();
				}
				// breathe...
				try {
					Thread.sleep(20);
				}
				catch (InterruptedException e) {
					break;
				}
			}
			thread = null;
		}
		
	}
	
}