package org.jalgo.module.levenshtein.gui.components;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.jalgo.module.levenshtein.gui.GuiController;
import org.jalgo.module.levenshtein.gui.events.ClickOnTableCell;
import org.jalgo.module.levenshtein.model.Cell;
import org.jalgo.module.levenshtein.model.Controller;
import org.jalgo.module.levenshtein.pattern.CellClickedObservable;
import org.jalgo.module.levenshtein.pattern.CellClickedObserver;

public class TablePanel extends JPanel implements CellClickedObservable {
	private static final long serialVersionUID = -5407126107244247503L;

	private Controller controller;
	private GuiController guiController;
	
	private TableCellPanel[][] tablePanels;
	
	private List<CellClickedObserver> onClickObserver;
	
	private List<TableCellPanel> coloredCells;
	
	public void init(String source, String target,
			Controller controller) {
		this.controller = controller;
		onClickObserver = new ArrayList<CellClickedObserver>();
		coloredCells = new ArrayList<TableCellPanel>();
//		this.guiController = guiController;
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		tablePanels = new TableCellPanel[2*(source.length()+1)][2 * (target.length()+1)];
		
		for (int j = 0; j < 2*(source.length()+1); j++) {
			for (int i = 0; i < 2*(target.length()+1); i++) {
				tablePanels[j][i] = new TableCellPanel();
				if (j % 2 == 1 && i % 2 == 1)
					tablePanels[j][i].addMouseListener(new ClickOnTableCell(j, i, this));
				c.gridy = j;
				c.gridx = i;
				add(tablePanels[j][i], c);
				if (j != 0 && i != 0)
					tablePanels[j][i].setVisible(false);
			}
		}
		
		tablePanels[0][0].setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.black));
		
		for (int j = 1; j < 2*(source.length()+1); j++) {
			tablePanels[j][0].setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.black));
		}
		
		for (int i = 1; i < 2*(target.length()+1); i++) {
			tablePanels[0][i].setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
		}
		
		tablePanels[0][0].setText("d(j,i)");
		
		for (int j = 0; j < source.length(); j++) {
			tablePanels[2*j + 3][0].setText("\\text{" + source.charAt(j) + "}");
		}
		
		for (int i = 0; i < target.length(); i++) {
			tablePanels[0][2*i + 3].setText("\\text{" + target.charAt(i) + "}");
		}
		
		for (int j = 0; j <= source.length(); j++) {
			for (int i = 0; i <= target.length(); i++) {
				int tJ = 2*j + 1;
				int tI = 2*i + 1;
				Cell cell = controller.getCell(j, i);
				tablePanels[tJ][tI].setText(cell.getValue()+"");
				if (cell.outDeletion()) {
					tablePanels[tJ+1][tI].setArrow(TableCellPanel.ARROWDOWN);
				}
				if (cell.outInsertion()) {
					tablePanels[tJ][tI+1].setArrow(TableCellPanel.ARROWRIGHT);
				}
				if (cell.outSubstitution() || cell.outIdentity()) {
					tablePanels[tJ+1][tI+1].setArrow(TableCellPanel.ARROWRIGHTDOWN);
				}
			}
		}
		
		tablePanels[1][1].mark();
	}
	
	public void cellClicked(int j, int i) {
		for (TableCellPanel cell : coloredCells) {
			cell.black();
		}
		coloredCells.clear();
		
		if(tablePanels[j][i].isMarked()) {
			
			tablePanels[j][i].unmark();
			tablePanels[j][i].setVisible(true);
			
			if (j-1 > 0) {
				tablePanels[j-1][i].setVisible(true);
			}
			if (i-1 > 0) {
				tablePanels[j][i-1].setVisible(true);
			}
			if (j-1 > 0 && i-1 > 0) {
				tablePanels[j-1][i-1].setVisible(true);
			}
			
			if (j+2 < tablePanels.length) {
				if (i == 1) {
					tablePanels[j+2][1].mark();
				} else if (tablePanels[j+2][i-2].isFilled()) {
					tablePanels[j+2][i].mark();
				}
				
			} 
			if (i+2 < tablePanels[0].length) {
				if (j == 1) {
					tablePanels[1][i+2].mark();
				} else if (tablePanels[j-2][i+2].isFilled()) {
					tablePanels[j][i+2].mark();
				}
			}	
		}
		
		if(tablePanels[j][i].isFilled()) {
			tellObservers((j-1)/2, (i-1)/2);
			tablePanels[j][i].fat();
			coloredCells.add(tablePanels[j][i]);
			
			if (!(j == 1 || i == 1)) {
				if (j - 2 >= 1) {
					tablePanels[j-2][i].green();
					coloredCells.add(tablePanels[j-2][i]);
					tablePanels[j-1][i].green();
					coloredCells.add(tablePanels[j-1][i]);
				}
				if (i - 2 >= 1) {
					tablePanels[j][i-2].blue();
					coloredCells.add(tablePanels[j][i-2]);
					tablePanels[j][i-1].blue();
					coloredCells.add(tablePanels[j][i-1]);
				}
				if (j-2 >= 1 && i-2 >= 1) {
					tablePanels[j-2][i-2].red();
					coloredCells.add(tablePanels[j-2][i-2]);
					tablePanels[j-1][i-1].red();
					coloredCells.add(tablePanels[j-1][i-1]);
				}
			}
		} else {
			tellObservers(-1, -1);
		}
		
		repaint();
		revalidate();
	}
	

	public void registerObserver(CellClickedObserver obs) {
		onClickObserver.add(obs);
	}
	
	public void tellObservers(int j, int i) {
		for (CellClickedObserver obs : onClickObserver) {
			obs.cellClicked(j, i);
		}
	}
}
