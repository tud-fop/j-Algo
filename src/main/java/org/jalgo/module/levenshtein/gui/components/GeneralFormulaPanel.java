package org.jalgo.module.levenshtein.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.jalgo.main.util.Messages;
import org.jalgo.module.levenshtein.gui.events.HideGeneralFormulaAction;
import org.jalgo.module.levenshtein.gui.events.ShowGeneralFormulaAction;
import org.jalgo.module.levenshtein.model.Controller;
import org.jalgo.module.levenshtein.pattern.CellClickedObserver;
import org.jalgo.module.levenshtein.pattern.ChangeGeneralFormulaSizeObserver;
import org.jalgo.module.levenshtein.pattern.ChangeSizeObservable;

public class GeneralFormulaPanel 
extends JPanel 
implements CellClickedObserver, ChangeSizeObservable {
	private static final long serialVersionUID = -3969386447472499860L;
	
	private Controller controller;
	
	private int width;
	private int height;
	
	public static int size = 13;
	
	private int clickedJ = -1;
	private int clickedI = -1;
	
	private boolean wasAlreadyFilled = true;
	
	private List<ChangeGeneralFormulaSizeObserver> observers;
	
	private GeneralFormula expandedFormulaPanel;
	private GeneralFormula smallFormulaPanel;
	
	private JButton downButton;
	private JButton upButton;
	private final ImageIcon downIcon;
	private final ImageIcon upIcon;
	
	private boolean smallFormula = false;
	
	private final GridBagConstraints buttonConstraints;
	private final GridBagConstraints formulaConstraints;
	
	public GeneralFormulaPanel() {
		setLayout(new GridBagLayout());
		
		buttonConstraints = new GridBagConstraints();
		buttonConstraints.gridx = 0;
		buttonConstraints.gridy = 0;
		buttonConstraints.fill = GridBagConstraints.NONE;
		
		downIcon = new ImageIcon(Messages.getResourceURL("levenshtein", "Ui.Downarrow"));
		upIcon = new ImageIcon(Messages.getResourceURL("levenshtein", "Ui.Uparrow"));
		
		ImageIcon resDownIcon = resizeImageIcon(downIcon);
		ImageIcon resUpIcon = resizeImageIcon(upIcon);
		
		downButton = new JButton(resDownIcon);
		upButton = new JButton(resUpIcon);
		
		downButton.setOpaque(false);
		downButton.setBorderPainted(false);
		upButton.setOpaque(false);
		upButton.setBorderPainted(false);
		
		add(downButton, buttonConstraints);
		
		
		formulaConstraints = new GridBagConstraints();
		formulaConstraints.gridx = 1;
		formulaConstraints.gridy = 0;
		formulaConstraints.fill = GridBagConstraints.HORIZONTAL;
//		formulaConstraints.anchor = GridBagConstraints.CENTER;

		expandedFormulaPanel = new ExpandedGeneralFormula();
		
		smallFormulaPanel = new SmallGeneralFormula();
		
		add(expandedFormulaPanel, formulaConstraints);
		
		observers = new ArrayList<ChangeGeneralFormulaSizeObserver>();

		expandedFormulaPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		smallFormulaPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
	}
	
	
	private ImageIcon resizeImageIcon(ImageIcon imgIcon) {
		Image img = imgIcon.getImage();
		int imgSize = 30;
		Image newimg = img.getScaledInstance(imgSize, imgSize, Image.SCALE_SMOOTH);
		return new ImageIcon(newimg);
	}
	
	/**
	 * initializes the GerneralFormulaPanel by writing the formulas of the levenshtein
	 * distance into the Panel
	 */
	public void init(Controller controller) {
		expandedFormulaPanel.setController(controller);
		smallFormulaPanel.setController(controller);
	}

	/**
	 * marks the interesting details of the formula with colors such that
	 * its easier to understand, what's going on
	 */
	public void cellClicked(int j, int i, boolean wasAlreadyFilled) {
		smallFormulaPanel.cellClicked(j, i, wasAlreadyFilled);
		expandedFormulaPanel.cellClicked(j, i, wasAlreadyFilled);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}
	
	
	
	/**
	 * calculates the new size of the latex formulas as the window size changes
	 * @param width, the allowed width of the GeneralFormulaPanel
	 * @param height, the allowed height of the GeneralFormulaPanel
	 */
	public void onResize(int width, int height) {
		this.width = width;
		this.height = height;
		
		double buttonWidth = 0;
		
		setPreferredSize(new Dimension(width, height));
		
		if (smallFormula) {
			buttonWidth = upButton.getSize().getWidth();
			smallFormulaPanel.onResize((int) (width - buttonWidth), height);
		} else {
			buttonWidth = downButton.getSize().getWidth();
			expandedFormulaPanel.onResize((int) (width - buttonWidth), height);
		}
		
		
		
		
		cellClicked(clickedJ, clickedI, wasAlreadyFilled);
		
		repaint();
		revalidate();
	}

	public void registerObserver(ChangeGeneralFormulaSizeObserver obs) {
		downButton.addMouseListener(new HideGeneralFormulaAction(obs));
		upButton.addMouseListener(new ShowGeneralFormulaAction(obs));
		
	}
	
	
	
	public void setSmallFormula(boolean small) {
		smallFormula = small;
		if (small) {
			remove(downButton);
			add(upButton, buttonConstraints);
			remove(expandedFormulaPanel);
			add(smallFormulaPanel, formulaConstraints);
		} else {
			remove(upButton);
			add(downButton, buttonConstraints);
			remove(smallFormulaPanel);
			add(expandedFormulaPanel, formulaConstraints);
		}
		
		repaint();
		revalidate();
	}
}
