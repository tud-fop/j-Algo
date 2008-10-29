package org.jalgo.module.app.view.run;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import org.jalgo.main.util.Messages;
import org.jalgo.module.app.controller.AlgorithmController;
import org.jalgo.module.app.core.step.AtomicStep;
import org.jalgo.module.app.core.step.GroupStep;
import org.jalgo.module.app.core.step.RootStep;
import org.jalgo.module.app.view.InterfaceConstants;

/**
 * Displays the recursive formula (using variables and its current values and
 * the result) incl. highlighting on the algorithm panel during the algorithm
 * mode.
 */
public class FormulaComponent extends JPanel implements StepHighlighting,MouseListener {

	private static final long serialVersionUID = -5258259352683364934L;

	private AlgorithmController algoCtrl;
	
	private FormulaDisplay formulaDisplay;
	private JPanel enlargePanel;
	private Dimension panelDimension;
	private Cursor handCursor, defaultCursor;
	
	private boolean beamerMode;
	
	/**
	 * Displays the recursive formula (using variables and its current values and
	 * the result) incl. highlighting on the algorithm panel during the algorithm
	 * mode.
	 */
	public FormulaComponent(AlgorithmController algoCtrl) {
		this.algoCtrl = algoCtrl;
		algoCtrl.addStepHighlighting(this);
		
		this.beamerMode = false;
		
		setLayout(null);

		formulaDisplay = new FormulaDisplay(InterfaceConstants.formulaNormalFont(beamerMode),
											InterfaceConstants.formulaBoldFont(beamerMode),
											InterfaceConstants.formulaSmallFont(beamerMode));
		enlargePanel = new JPanel();
		enlargePanel.setBackground(Color.white);
		enlargePanel.setToolTipText(Messages.getString("app","FormulaComponent.enlargePanel")); //$NON-NLS-1$ //$NON-NLS-2$
		JLabel magnifier;
		magnifier = new JLabel(new ImageIcon(Messages.getResourceURL("app", "zoom"))); //$NON-NLS-1$ //$NON-NLS-2$
		magnifier.setBounds(0, 0, 16, 16);
		enlargePanel.add(magnifier);
		
		panelDimension = new Dimension(100, InterfaceConstants.formulaNormalFont(beamerMode).getSize() * 8);
		
		this.setMinimumSize(panelDimension);
		this.setPreferredSize(panelDimension);
		this.setMaximumSize(panelDimension);
		
		this.add(formulaDisplay);
		this.add(enlargePanel);
		
		this.setComponentZOrder(formulaDisplay, 1);
		this.setComponentZOrder(enlargePanel, 0);
		
		this.addComponentListener(new FormulaComponentListener());
		this.setBackground(InterfaceConstants.formulaBackgroundColor());
		this.setOpaque(true);
		
		enlargePanel.addMouseListener(this);
		handCursor = new Cursor(Cursor.HAND_CURSOR);
		defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	}	

	/**
	 * Sets the current operators.
	 * 
	 * @param addOperator
	 *            the multiplicative operator in its symbolic representation.
	 * @param addInfix
	 *            if <code>true</code>, the add operator must be printed
	 *            infix, otherwise prefix.
	 * @param multOperator
	 *            the multiplicative operator in its symbolic representation.
	 * @param multInfix
	 *            if <code>true</code>, the multiply operator must be printed
	 *            infix, otherwise prefix.
	 */
	public void setOperators(String addOperator, boolean addInfix, String multOperator, boolean multInfix) {
		formulaDisplay.setOperators(addOperator, addInfix, multOperator, multInfix);
	}

	public void updateBeamerMode(boolean beamerMode){
		this.beamerMode = beamerMode;
		formulaDisplay.updateBeamerMode(beamerMode);
		panelDimension = new Dimension(100, InterfaceConstants.formulaNormalFont(beamerMode).getSize() * 8);
		this.setMinimumSize(panelDimension);
		this.setPreferredSize(panelDimension);
		this.setMaximumSize(panelDimension);
		
	}
	
	public void enterHighlightMode(RootStep rootStep) {
		formulaDisplay.setNoPainting(true);
		
		repaint();
		revalidate();		
	}

	public void leaveHighlightMode() {
		formulaDisplay.setNoPainting(true);
		
		repaint();
		revalidate();		
	}	
	
	public void highlightAtomicStep(AtomicStep step, boolean isForward) {
		formulaDisplay.setNoPainting(false);
		
		formulaDisplay.setParameterDescription(step);
		
		repaint();
		revalidate();
	}

	public void highlightGroupStep(GroupStep step, boolean isForward) {
		formulaDisplay.setNoPainting(true);
		
		repaint();
		revalidate();		
	}

	public void highlightLastStep(RootStep step) {
		formulaDisplay.setNoPainting(true);
		
		repaint();
		revalidate();		
	}

	public void highlightFirstStep(RootStep step) {
		formulaDisplay.setNoPainting(true);
		
		repaint();
		revalidate();		
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		this.setCursor(handCursor);
	}

	public void mouseExited(MouseEvent e) {
		this.setCursor(defaultCursor);
	}
	
	public void mousePressed(MouseEvent e) {
			algoCtrl.initFormulaWindow();
	}
	
	public void mouseReleased(MouseEvent e) {
	}
	
	/**
	 * Used to resize a formula.
	 */
	private class FormulaComponentListener implements ComponentListener {

		public void componentHidden(ComponentEvent arg0) {
		}

		public void componentMoved(ComponentEvent arg0) {
			componentResized(arg0);
		}

		public void componentResized(ComponentEvent arg0) {
			formulaDisplay.setBounds(0, 0, getWidth() - 20, getHeight());
			enlargePanel.setBounds(getWidth() - 20, getHeight() - 25, 20, 20);	
			
			repaint();
			revalidate();
		}

		public void componentShown(ComponentEvent arg0) {
			componentResized(arg0);
			
		}
		
	}

}
