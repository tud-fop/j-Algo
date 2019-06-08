package org.jalgo.module.levenshtein.gui.components;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jalgo.module.levenshtein.model.Action;
import org.jalgo.module.levenshtein.pattern.AlignmentClickObservable;
import org.jalgo.module.levenshtein.pattern.AlignmentClickObserver;

public class AlignPanel 
extends JPanel
implements AlignmentClickObservable {
	private static final long serialVersionUID = 63017010649735276L;

	private String source;
	private String target;
	private List<Action> alignment;
	private int number;
	
	private JLabel[][] table;
	private JLabel numberLabel;
	
	private Font standardFont;
	
	private List<AlignmentClickObserver> observers;
	
	public AlignPanel(int number, String source, String target, List<Action> alignment) {
		this.number = number;
		this.source = source;
		this.target = target;
		this.alignment = alignment;
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 20;
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 4;
		c.anchor = GridBagConstraints.CENTER;
		//c.fill = GridBagConstraints.HORIZONTAL;
		
		if (alignment.size() > 0)
			numberLabel = new JLabel(number + ")");
		else 
			numberLabel = new JLabel();
		
		add(numberLabel,c);
		
		table = new JLabel[4][alignment.size()];
		
		
		c = new GridBagConstraints();
		c.ipadx = 5;
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < alignment.size(); j++) {
				table[i][j] = new JLabel();
				if (i == 1) {
					table[i][j].setText("|");
				}
				c.gridx = j+1;
				c.gridy = i;
				add(table[i][j], c);
			}
		}
		
		
		Font font = numberLabel.getFont();
		standardFont = font.deriveFont((float)font.getSize());
		
		
		init();
		
		observers = new ArrayList<AlignmentClickObserver>();
	}
	
	private void init() {
		int j = 0;
		int i = 0;
		
		for (int k = 0; k < alignment.size(); k++) {
			Action action = alignment.get(k);
			switch (action) {
			case DELETION:
				table[0][k].setText("" + source.charAt(j));
				j++;
				table[2][k].setText("*");
				table[3][k].setText("d");
				break;
			case INSERTION:
				table[0][k].setText("*");
				table[2][k].setText("" + target.charAt(i));
				i++;
				table[3][k].setText("i");
				break;
			case SUBSTITUTION:
				table[0][k].setText("" + source.charAt(j));
				j++;
				table[2][k].setText("" + target.charAt(i));
				i++;
				table[3][k].setText("s");
				break;
			case IDENTITY:
				table[0][k].setText("" + source.charAt(j));
				j++;
				table[2][k].setText("" + target.charAt(i));
				i++;
				break;
			default:
				break;
			}
		}
	}
	
	public void onResize(int width, int height) {
		int prefWidth = getPreferredSize().width;
		double ratio = (double) width / (double) prefWidth;
		
		float maxSize = (float) (numberLabel.getFont().getSize() * ratio);
		float size = Math.min(maxSize, (float) (height / 6.0));
		
		Font newFont = standardFont.deriveFont(
				Font.PLAIN, 
				size);
		numberLabel.setFont(newFont);
		
		for (JLabel[] labels : table) {
			for (JLabel label : labels) {
				label.setFont(standardFont.deriveFont(
						Font.PLAIN, 
						size));
			}
		}
		
		revalidate();
		repaint();
	}

	public void registerAlignmentObserver(AlignmentClickObserver obs) {
		observers.add(obs);
	}
	
	public void clicked() {
		for (AlignmentClickObserver obs : observers) {
			obs.alignmentClicked(alignment);
		}
	}
	
	public void mouseEntered() {
		for (AlignmentClickObserver obs : observers) {
			obs.alignmentHoverIn(alignment);
		}
	}
	
	public void mouseExited() {
		for (AlignmentClickObserver obs : observers) {
			obs.alignmentHoverOut(alignment);
		}
	}
}
