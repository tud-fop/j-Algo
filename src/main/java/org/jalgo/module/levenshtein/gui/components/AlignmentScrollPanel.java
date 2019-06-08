package org.jalgo.module.levenshtein.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jalgo.module.levenshtein.gui.events.AlignmentClick;
import org.jalgo.module.levenshtein.model.Action;
import org.jalgo.module.levenshtein.model.Controller;
import org.jalgo.module.levenshtein.pattern.AlignmentClickObservable;
import org.jalgo.module.levenshtein.pattern.AlignmentClickObserver;
import org.jalgo.module.levenshtein.pattern.CellClickedObserver;

public class AlignmentScrollPanel 
extends JScrollPane 
implements CellClickedObserver, AlignmentClickObservable {
	private static final long serialVersionUID = 1670629543439950694L;

	private String source;
	private String target;
	private Controller controller;
	
	private JPanel content;
	
	private int width;
	private int height;
	
	private List<AlignPanel> panels;
	
	private List<AlignmentClickObserver> observers;
	
	public AlignmentScrollPanel(String source, String target, Controller controller) {

		this.source = source;
		this.target = target;
		this.controller = controller;
		
		panels = new ArrayList<AlignPanel>();

		content = new JPanel();
		content.setLayout(new GridBagLayout());
		
		setBorder(null);
		
		setViewportView(content);
		
		observers = new ArrayList<AlignmentClickObserver>();
	}
	
	public void cellClicked(int j, int i, boolean wasAlreadyFilled) {
		content.removeAll();
		panels.clear();
		
		if (j > -1 && i > -1) {
			GridBagConstraints c = new GridBagConstraints();
			c.ipady = 20;
			c.ipadx = 5;
			//c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.WEST;
			
			
			List<List<Action>> alignments = controller.getAlignments(j, i);
			
			for (int k = 0; k < alignments.size(); k++) {
				c.gridx = 0;
				c.gridy = k;
				AlignPanel panel = new AlignPanel(k+1,source, target, alignments.get(k));
				for ( AlignmentClickObserver obs : observers) {
					panel.registerAlignmentObserver(obs);
				}
				panel.addMouseListener(new AlignmentClick(panel));
				if (k > 0) {
					panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
					//panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
				}
				panel.onResize(Math.max(0, width-30), height / 4);
				panels.add(panel);
				content.add(panel, c);
			}
		}
		
		revalidate();
		repaint();
	}

	public void onResize(int width, int height) {
		this.width = width;
		this.height = height;
		for (AlignPanel p : panels) {
			p.onResize(Math.max(0, width-30), height / 4);
		}
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

	public void registerAlignmentObserver(AlignmentClickObserver obs) {
		observers.add(obs);
	}
	
	
}
