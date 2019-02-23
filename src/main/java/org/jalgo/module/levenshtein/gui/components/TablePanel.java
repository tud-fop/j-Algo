package org.jalgo.module.levenshtein.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.apache.commons.lang.NotImplementedException;
import org.jalgo.module.levenshtein.gui.GuiController;
import org.jalgo.module.levenshtein.gui.events.ClickOnTableCell;
import org.jalgo.module.levenshtein.model.Action;
import org.jalgo.module.levenshtein.model.Cell;
import org.jalgo.module.levenshtein.model.Controller;
import org.jalgo.module.levenshtein.pattern.AlignmentClickObserver;
import org.jalgo.module.levenshtein.pattern.CellClickedObservable;
import org.jalgo.module.levenshtein.pattern.CellClickedObserver;
import org.jalgo.module.levenshtein.pattern.ToolbarObserver;

public class TablePanel 
extends JPanel 
implements CellClickedObservable, ToolbarObserver, AlignmentClickObserver {
	private static final long serialVersionUID = -5407126107244247503L;

	private Controller controller;
	private GuiController guiController;
	
	private TableCellPanel[][] tablePanels;
	
	private List<CellClickedObserver> onClickObserver;
	
	private List<TableCellPanel> coloredCells;
	
	private int width;
	private int height;
	
	/**
	 * initializes the TablePanel
	 * @param source, the source word
	 * @param target, the target word
	 * @param controller, the controller of the levenshtein calculation
	 */
	public void init(String source, String target,
			Controller controller) {
		this.controller = controller;
		
		// manage on click observers that want to be notified if a cell is clicked on
		onClickObserver = new ArrayList<CellClickedObserver>();
		
		// save all colored cells such that they can be uncolored again
		coloredCells = new ArrayList<TableCellPanel>();
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		// create the array of tableCells
		tablePanels = new TableCellPanel[2*(source.length()+1)][2 * (target.length()+1)];
		
		// initialize the array of table cells and add the cells to the correct location
		for (int j = 0; j < 2*(source.length()+1); j++) {
			for (int i = 0; i < 2*(target.length()+1); i++) {
				tablePanels[j][i] = new TableCellPanel();
				
				// these cells will contain numbers and therefore need to be clicked on
				if (j % 2 == 1 && i % 2 == 1)
					tablePanels[j][i].addMouseListener(new ClickOnTableCell(j, i, this));
				
				// add the cell to the view
				c.gridy = j;
				c.gridx = i;
				add(tablePanels[j][i], c);
				
				// initially all cells of the grid are invisible
				if (j != 0 && i != 0)
					tablePanels[j][i].setVisible(false);
			}
		}
		
		// set the borders of the table head and first column
		tablePanels[0][0].setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.black));
		
		for (int j = 1; j < 2*(source.length()+1); j++) {
			tablePanels[j][0].setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.black));
		}
		
		for (int i = 1; i < 2*(target.length()+1); i++) {
			tablePanels[0][i].setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
		}
		
		// set the words to the correct cells
		tablePanels[0][0].setText("d(j,i)");
		
		for (int j = 0; j < source.length(); j++) {
			tablePanels[2*j + 3][0].setText("\\text{" + source.charAt(j) + "}");
		}
		
		for (int i = 0; i < target.length(); i++) {
			tablePanels[0][2*i + 3].setText("\\text{" + target.charAt(i) + "}");
		}
		
		// fill the invisible cells with their texts
		for (int j = 0; j <= source.length(); j++) {
			for (int i = 0; i <= target.length(); i++) {
				int tJ = 2*j + 1;
				int tI = 2*i + 1;
				
				// get the cell entry from the controller
				Cell cell = controller.getCell(j, i);
				
				// set the value to the corresponding cell
				tablePanels[tJ][tI].setText(cell.getValue()+"");
				
				// add arrows to the neighbor cells, if we can go that way
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
		
		// mark the initial spot where we can start filling the table 
		tablePanels[1][1].mark();
		
		width = getPreferredSize().width;
		height = getPreferredSize().height;
		
		// uncolor all cells, if clicked anywhere between
		addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent e) {}
			
			public void mousePressed(MouseEvent e) {}
			
			public void mouseExited(MouseEvent e) {}
			
			public void mouseEntered(MouseEvent e) {}
			
			public void mouseClicked(MouseEvent e) { cellClicked(-1, -1); }
		});
	}
	
	/**
	 * show the value of a cell if this cell currently can be filled,
	 * if the cell is filled either way, show the path to the cell
	 * and show how this cell is calculated, therefore the cell clicked observer
	 * are notified
	 * @param j, the vertical cell index
	 * @param i, the horizontal cell index
	 */
	public void cellClicked(int j, int i) {
		// uncolor all currently colored cells
		for (TableCellPanel cell : coloredCells) {
			cell.black();
		}
		coloredCells.clear();
		
		// if the cell is marked it need to get filled and other cells marked
		if(j > 0 && i > 0 && tablePanels[j][i].isMarked()) {
			
			// unmark and fill the cell
			tablePanels[j][i].unmark();
			tablePanels[j][i].setVisible(true);
			
			// show arrows to the cell
			if (j-1 > 0) {
				tablePanels[j-1][i].setVisible(true);
			}
			if (i-1 > 0) {
				tablePanels[j][i-1].setVisible(true);
			}
			if (j-1 > 0 && i-1 > 0) {
				tablePanels[j-1][i-1].setVisible(true);
			}
			
			// calculate, if with this cell showing up, some new cells can
			// be calculated
			// here check the cell downwards
			if (j+2 < tablePanels.length) {
				if (i == 1) {
					tablePanels[j+2][1].mark();
				} else if (tablePanels[j+2][i-2].isFilled()) {
					tablePanels[j+2][i].mark();
				}
				
			} 
			// here check the cell to the right
			if (i+2 < tablePanels[0].length) {
				if (j == 1) {
					tablePanels[1][i+2].mark();
				} else if (tablePanels[j-2][i+2].isFilled()) {
					tablePanels[j][i+2].mark();
				}
			}	
		}
		
		// if the cell is a filled cell, color cells that are
		// used to calculate this cell and show the path to
		// the cell
		if(j > 0 && i > 0 && tablePanels[j][i].isFilled()) {
			// notify observers
			tellObservers((j-1)/2, (i-1)/2);
			
			// highlight the current cell
			tablePanels[j][i].fat();
			coloredCells.add(tablePanels[j][i]);
			
			// color the cells leftwards, upwards and left-upwards
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
			
			// highlight the path to the current cell
			// therefore get alignments from the controller
			List<List<Action>> paths = controller.getAlignments((j-1)/2, (i-1)/2);
			
			for (List<Action> alignment : paths) {
				highlightAlignment(alignment);
			}
			
		} else {
			// uncolor everything
			tellObservers(-1, -1);
		}
		
		repaint();
		revalidate();
	}
	
	private void highlightAlignment(List<Action> alignment) {
		// highlight the start cell
		tablePanels[1][1].fat();
		coloredCells.add(tablePanels[1][1]);
		
		// iterate through the alignments and highlight along the actions
		
		int x = 1;
		int y = 1;
		for (Action action : alignment) {
			switch (action) {
			case DELETION:
				tablePanels[x+1][y].fat();
				coloredCells.add(tablePanels[x+1][y]);
				tablePanels[x+2][y].fat();
				coloredCells.add(tablePanels[x+2][y]);
				x+=2;
				break;
			case INSERTION:
				tablePanels[x][y+1].fat();
				coloredCells.add(tablePanels[x][y+1]);
				tablePanels[x][y+2].fat();
				coloredCells.add(tablePanels[x][y+2]);
				y+=2;
				break;
			case SUBSTITUTION:
			case IDENTITY:
				tablePanels[x+1][y+1].fat();
				coloredCells.add(tablePanels[x+1][y+1]);
				tablePanels[x+2][y+2].fat();
				coloredCells.add(tablePanels[x+2][y+2]);
				x+=2;
				y+=2;
				break;
			default:
				break;
			}
		}
		
	}
	
	/**
	 * undoes a cell, this cell should be the cell most down and most right that is filled
	 * @param j, the vertical index
	 * @param i, the horizontal index
	 */
	public void undoCell(int j, int i) {
		// uncolor everything
		cellClicked(-1, -1);
		
		// after we made the cell invisible it can be filled as the next cell, so needs 
		// to be marked
		tablePanels[j][i].setVisible(false);
		tablePanels[j][i].mark();
		
		// hide the arrows to the cell
		if (j > 1) {
			tablePanels[j-1][i].setVisible(false);
		}
		if (i > 1) {
			tablePanels[j][i-1].setVisible(false);
		}
		if (j > 1 && i > 1) {
			tablePanels[j-1][i-1].setVisible(false);
		}
		
		// unmark cells to the right and downwards
		if (j+2 < tablePanels.length) {
			tablePanels[j+2][i].unmark();
		} 
		if (i+2 < tablePanels[0].length) {
			tablePanels[j][i+2].unmark();
		}
		
		repaint();
		revalidate();
	}
	
	/**
	 * resizes the table on window size changed
	 * @param width, the allowed width of the table panel
	 * @param height, the allowed height of the table panel
	 */
	public void onResize(int width, int height) {
		// calculate the maximum possible size
		int rows = tablePanels.length;
		int cols = tablePanels[0].length;
		int maxSizeWidth = (int) (width / (cols * 1.5));
		int maxSizeHeight = (int) (height / (rows * 1.5));
		int maxSize = Math.min(maxSizeWidth, maxSizeHeight);
		
		// resize all the table cells
		for (int j = 0; j < tablePanels.length; j++) {
			for (int i = 0; i < tablePanels[0].length; i++) {
				tablePanels[j][i].resize(maxSize);
			}
		}
		
		repaint();
		revalidate();
	}

	/**
	 * register Observer
	 */
	public void registerObserver(CellClickedObserver obs) {
		onClickObserver.add(obs);
	}
	
	/**
	 * notify registered observer
	 * @param j, the source word index
	 * @param i, the target word index
	 */
	public void tellObservers(int j, int i) {
		for (CellClickedObserver obs : onClickObserver) {
			obs.cellClicked(j, i);
		}
	}

	/**
	 * searches for the next cell to fill and performs one step
	 */
	public void performStep() {
		for (int j = 1; j < tablePanels.length; j+=2) {
			for (int i = 1; i < tablePanels[0].length; i+=2) {
				if (tablePanels[j][i].isMarked()) {
					cellClicked(j, i);
					return;
				}
			}
		}
		cellClicked(-1, -1);
	}

	/**
	 * fills the table
	 */
	public void performAllSteps() {
		for (int j = 1; j < tablePanels.length; j+=2) {
			for (int i = 1; i < tablePanels[0].length; i+=2) {
				if (tablePanels[j][i].isMarked()) {
					cellClicked(j, i);
				}
			}
		}
		cellClicked(-1, -1);
	}

	/**
	 * searches for the last filled table and undoes it
	 */
	public void undoStep() {
		for (int j = tablePanels.length-1; j >= 1; j-=2) {
			for (int i = tablePanels[0].length-1; i >= 1; i-=2) {
				if (tablePanels[j][i].isFilled()) {
					undoCell(j,i);
					return;
				}
			}
		}
	}

	/**
	 * empties the table
	 */
	public void undoAll() {
		for (int j = tablePanels.length-1; j >= 1; j-=2) {
			for (int i = tablePanels[0].length-1; i >= 1; i-=2) {
				if (tablePanels[j][i].isFilled()) {
					undoCell(j,i);
				}
			}
		}
	}

	public void alignmentClicked(List<Action> alignment) {
		// uncolor all currently colored cells
		for (TableCellPanel cell : coloredCells) {
			cell.black();
		}
		coloredCells.clear();
		highlightAlignment(alignment);
	}
}
