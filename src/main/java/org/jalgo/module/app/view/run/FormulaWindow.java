package org.jalgo.module.app.view.run;

import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

import javax.swing.*;

import org.jalgo.main.util.Messages;
import org.jalgo.module.app.controller.AlgorithmController;
import org.jalgo.module.app.core.step.AtomicStep;
import org.jalgo.module.app.core.step.GroupStep;
import org.jalgo.module.app.core.step.RootStep;
import org.jalgo.module.app.view.InterfaceConstants;

/**
 * Displays an enlarged version (with increased font sizes) of the
 * <code>FormulaComponent</code> in an extra window.
 */
public class FormulaWindow extends JFrame implements StepHighlighting, ComponentListener, WindowListener {
	private static final long serialVersionUID = 7712663723144407282L;
	
	private AlgorithmController algoCtrl;
	private FormulaDisplay formulaDisplay;
	private Point lowerLeftCorner;
	private boolean modifiedPosition;
	
	private boolean beamerMode;
	
	/**
	 * Displays an enlarged version (with increased font sizes) of the
	 * <code>FormulaComponent</code> in an extra window.
	 */
	public FormulaWindow(AlgorithmController algoCtrl, GraphicsConfiguration display, Point lowerLeftCorner, boolean beamerMode) {
		super(display);
		
		this.setTitle(Messages.getString("app", "FormulaWindow.Title")); //$NON-NLS-1$ //$NON-NLS-2$"
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(Messages.getResourceURL("app", //$NON-NLS-1$
																"Module_logo")));	
		
		this.lowerLeftCorner = lowerLeftCorner;
		modifiedPosition = false;
		
		this.beamerMode = beamerMode;
		this.algoCtrl = algoCtrl;
		
		algoCtrl.addStepHighlighting(this);
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setResizable(false);
		
		formulaDisplay = new FormulaDisplay(InterfaceConstants.formulaWindowNormalFont(this.beamerMode),
											InterfaceConstants.formulaWindowBoldFont(this.beamerMode),
											InterfaceConstants.formulaWindowSmallFont(this.beamerMode));

		this.add(formulaDisplay);

		this.getContentPane().setBackground(InterfaceConstants.formulaBackgroundColor());
		
		addComponentListener(this);	
	}
	
	/**
	 * Sets the operators needed to display the formula.
	 * 
	 * @param addOperator
	 *            the String representation of the add operator.
	 * @param addInfix
	 *            true, if the add operator is written infix.
	 * @param multOperator
	 *            the String representation of the multiply operator.
	 * @param multInfix
	 *            true, if the multiply operator is written infix.
	 */
	public void setOperators(String addOperator, boolean addInfix, String multOperator, boolean multInfix) {
		formulaDisplay.setOperators(addOperator, addInfix, multOperator, multInfix);
	}
	
	/**
	 * Repaints the formula for the given <code>step</code>.
	 * 
	 * @param step
	 *            the step to paint the formula for.
	 */
	public void repaintFormula(AtomicStep step) {
		int newX,newY;
		BufferedImage tempImage;
		
		if (step != null)
			formulaDisplay.setParameterDescription(step);
		
		formulaDisplay.revalidate();
		this.update(this.getGraphics());
		this.repaint();
		
		// Calculate size
		tempImage = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
		formulaDisplay.paint( tempImage.getGraphics() );

		newX = formulaDisplay.getOffsetX();
		newY = formulaDisplay.getOffsetY();
		this.setSize(newX + 10,newY - 20);
		
		if (!modifiedPosition) {
			this.setLocation(lowerLeftCorner.x, lowerLeftCorner.y - newY + 20);
		}
	}
	
	public void updateBeamerMode(boolean beamerMode){
		//FIXME resize depending on beamerMode
		int newX,newY;
		
		this.beamerMode = beamerMode;
		newX = formulaDisplay.getOffsetX();
		newY = formulaDisplay.getOffsetY();
		this.setSize(newX + 10,newY - 20);
		
		formulaDisplay.setFont(InterfaceConstants.formulaWindowNormalFont(this.beamerMode),
							   InterfaceConstants.formulaWindowBoldFont(this.beamerMode),
							   InterfaceConstants.formulaWindowSmallFont(this.beamerMode));
		
		formulaDisplay.repaint();
		formulaDisplay.revalidate();
	}

	public void enterHighlightMode(RootStep rootStep) {
	}

	public void leaveHighlightMode() {
		dispose();
	}

	public void highlightAtomicStep(AtomicStep step, boolean isForward) {
		formulaDisplay.setNoPainting(false);
		repaintFormula(step);
	}

	public void highlightGroupStep(GroupStep step, boolean isForward) {
		formulaDisplay.setNoPainting(true);
		repaintFormula((AtomicStep)step.getStep(0));

		formulaDisplay.repaint();
		formulaDisplay.revalidate();	
	}

	public void highlightLastStep(RootStep step) {
		formulaDisplay.setNoPainting(true);
		repaintFormula((AtomicStep)((GroupStep)step.getStep(0)).getStep(0));
		
		formulaDisplay.repaint();
		formulaDisplay.revalidate();	
	}

	public void highlightFirstStep(RootStep step) {
		formulaDisplay.setNoPainting(true);
		repaintFormula((AtomicStep)((GroupStep)step.getStep(0)).getStep(0));

		formulaDisplay.repaint();
		formulaDisplay.revalidate();	
	}

	public void componentHidden(ComponentEvent arg0) {
	}

	public void componentMoved(ComponentEvent arg0) {
		modifiedPosition = true;
	}

	public void componentResized(ComponentEvent arg0) {
	}

	public void componentShown(ComponentEvent arg0) {
	}

	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosed(WindowEvent e) {
		algoCtrl.formulaWindowClosed();		
	}

	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
