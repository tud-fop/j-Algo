package org.jalgo.module.app.view.run;

import org.jalgo.module.app.core.Matrix;
import org.jalgo.module.app.core.step.*;
import org.jalgo.module.app.view.InterfaceConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.jalgo.module.app.controller.AlgorithmController;
import org.jalgo.module.app.core.step.AtomicStep;
import org.jalgo.module.app.core.step.GroupStep;
import org.jalgo.module.app.core.step.RootStep;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.Timer;

/**
 * Displays and updates the two matrices (the matrix from the last group step
 * including path highlighting and the current matrix with highlighting of
 * processed and unprocessed elements) during the algorithm mode in the
 * algorithm panel.
 */
public class MatrixComponent extends JPanel implements StepHighlighting {

	private static final long serialVersionUID = -1480166238292888483L;

	private MatrixDisplay leftDisplay, rightDisplay, tempDisplay, probeDisplay;
	private MatrixMoveAnimator animator;
	private Step currentStep;
	private boolean isVisible;
	private ViewMode viewMode;
	
	private RootStep rootStep;
	private Font forcedFont;
	
	private boolean beamerMode;
	
	private enum ViewMode {
		StartView, GroupStepView, AtomicStepView, EndView 
	}
	
	public MatrixComponent(AlgorithmController algoCtrl) {
		algoCtrl.addStepHighlighting(this);
		
		this.beamerMode = false;
		
		this.setBackground(InterfaceConstants.formulaBackgroundColor());
				
		leftDisplay = new MatrixDisplay(true);
		rightDisplay = new MatrixDisplay(true);
		tempDisplay = new MatrixDisplay(true);
		probeDisplay = new MatrixDisplay(true);
		
		leftDisplay.setBounds(calculateLeftOuterPosition(),0,1,1);
		rightDisplay.setBounds(calculateRightOuterPosition(),0,1,1);
		tempDisplay.setBounds(calculateRightOuterPosition(),0,1,1);
		probeDisplay.setBounds(calculateRightOuterPosition(),0,1,1);

		currentStep = null;
		
		this.setLayout(null);
		
		this.add(leftDisplay);
		this.add(rightDisplay);
		this.add(tempDisplay);
		
		forcedFont = InterfaceConstants.getMatrixFont(beamerMode);
		
		// Set separation line
		setBorder(BorderFactory.createMatteBorder(-1, -1, 1, -1,
				Color.LIGHT_GRAY));
		setBackground(Color.WHITE);

		isVisible = false;
		
		setOpaque(true);			// Needed for ubuntulooks GUI decorator
		addComponentListener(new MatrixComponentListener());
		
		setDoubleBuffered(true);
	}

	/**
	 * Toggles whether the matrix component is visible or not.
	 * 
	 */
	public void setVisible(boolean state) {
		isVisible = state;
	}

	public void updateBeamerMode(boolean beamerMode){
		this.beamerMode = beamerMode;
		leftDisplay.updateBeamerMode(beamerMode);
		rightDisplay.updateBeamerMode(beamerMode);
		tempDisplay.updateBeamerMode(beamerMode);
		probeDisplay.updateBeamerMode(beamerMode);
		forcedFont = InterfaceConstants.getMatrixFont(beamerMode);
		repaint();
		revalidate();
	}
	
	/**
	 * Calculates the height of a matrix.
	 * 
	 * @return
	 */
	private int calculateMatrixHeight() {
		return calculateDualMatrixWidth();
	}
	
	private int calculateMatrixPositionTop() {
		if (calculateMatrixHeight() > getHeight())
			return 0;
		else
			return getHeight() / 2 - calculateMatrixHeight() / 2;
	}
	
	private int calculateMatrixDisplayHeight() {
		return getHeight() - calculateMatrixPositionTop() - 10; 
	}
	
	/**
	 * Calculates the width of a matrix if two matrices are shown.
	 * @return
	 */
	private int calculateDualMatrixWidth() {
		return Math.min(getWidth()/2 - (1 * InterfaceConstants.matrixHgap()), getHeight() - 1*InterfaceConstants.matrixVgap());
	}

	/**
	 * Calculates the position of the left matrix if two matrices are shown.
	 * 
	 * @return
	 */
	private int calculateDualMatrixPositionLeft() {
		return (getWidth() / 4) - (calculateDualMatrixWidth() / 2) + InterfaceConstants.matrixHgap() / 2;
	}

	/**
	 * Calculates the position of the right matrix if two matrices are shown.
	 * 
	 * @return
	 */
	private int calculateDualMatrixPositionRight() {
		return (getWidth() * 3/4) - (calculateDualMatrixWidth() / 2) - (InterfaceConstants.matrixHgap() / 2);
	}
	
	/**
	 * Calculates the width of a single matrix.
	 * 
	 * @return
	 */
	
	private int calculateSingleMatrixWidth() {
		return calculateDualMatrixWidth();
	}
	
	/**
	 * Calculates the position of a single matrix centered to the screen.
	 * 
	 * @return
	 */
	
	private int calculateSingleMatrixPosition() {
		return getWidth() / 2 - calculateSingleMatrixWidth() / 2;
	}	
	
	/**
	 * Calculates the position of a matrix moved outside of the left side of the window.
	 * 
	 * @return
	 */	
	private int calculateRightOuterPosition() {
		return getWidth() + 10;
	}

	/**
	 * Calculates the position of a matrix moved outside of the right side of the window.
	 * 
	 * @return
	 */		
	private int calculateLeftOuterPosition() {
		return - calculateDualMatrixWidth();
	}

	/**
	 * Sets the position, width and the font of a matrix display.
	 * 
	 * @return
	 */		
	private void arrangeView(MatrixDisplay display, int x)
	{
		Rectangle rect = display.getBounds();

		rect.x = x;
		rect.y = calculateMatrixPositionTop();
		
		rect.width = calculateDualMatrixWidth();
		rect.height = calculateMatrixDisplayHeight();
		
		display.scaleTo(rect.width, rect.height);
		display.setBounds(rect);
		display.setFontForced(forcedFont);
	}
	
	/**
	 * Arranges the left and the right matrix display that
	 * the left display is shown centered and the right matrix
	 * is placed on the right outer position.
	 * 
	 */
	private void arrangeSingleViewLeft() {
		arrangeView(leftDisplay, calculateSingleMatrixPosition());
		arrangeView(rightDisplay, calculateRightOuterPosition());
	}

	/**
	 * Arranges the left and the right matrix display that
	 * the right display is shown centered and the left matrix
	 * is placed on the left outer position.
	 * 
	 */	
	private void arrangeSingleViewRight() {
		arrangeView(leftDisplay, calculateLeftOuterPosition());
		arrangeView(rightDisplay, calculateSingleMatrixPosition());
		
	}

	/**
	 * Arranges the left and the right matrix display that
	 * the both displays are shown on their usual positions.
	 * 
	 */
	private void arrangeDualView() {
		arrangeView(leftDisplay, calculateDualMatrixPositionLeft());
		arrangeView(rightDisplay, calculateDualMatrixPositionRight());
	}

	/**
	 * Prepares any matrix display to show the content of a given
	 * matrix.
	 * 
	 *  @param component
	 *  	The affected matrix display
	 *  @param matrix
	 *  	The content to show
	 */
	private void singleView(MatrixDisplay component, Matrix matrix) {
		int matrixWidth, matrixHeight;
		
		matrixHeight = calculateMatrixHeight();
		matrixWidth = calculateSingleMatrixWidth();

		component.setBounds((int)component.getBounds().getX(), (int)component.getBounds().getY(), matrixWidth, calculateMatrixDisplayHeight());

		//component.setFontForced(null);
		component.setFontForced(forcedFont);
		
		component.updateMatrix(matrix);
		
		this.repaint();
		this.revalidate();		
	}

	/**
	 * Prepares the left matrix display to shown the content of the given matrix.
	 * 
	 * @param matrix
	 */
	private void singleViewLeft(Matrix matrix) {
		singleView(leftDisplay, matrix);
	}

	/**
	 * Prepares the right matrix display to shown the content of the given matrix.
	 * 
	 * @param matrix
	 */	
	private void singleViewRight(Matrix matrix) {
		singleView(rightDisplay, matrix);
	}	
	
	/**
	 * Prepares the left and right matrix to show the content of the matrix "left"
	 * and "right.
	 * 
	 * @param left
	 * @param right
	 */	
	private void dualView(Matrix left, Matrix right) {
		int matrixWidth, matrixHeight;
		
		matrixHeight = calculateMatrixHeight();
		matrixWidth = calculateDualMatrixWidth();
		
		leftDisplay.setVisible(true);
		rightDisplay.setVisible(true);
		
		leftDisplay.setBounds((int)leftDisplay.getBounds().getX(), (int)leftDisplay.getBounds().getY(), matrixWidth, calculateMatrixDisplayHeight());
		rightDisplay.setBounds((int)rightDisplay.getBounds().getX(), (int)rightDisplay.getBounds().getY(), matrixWidth, calculateMatrixDisplayHeight());
		
		leftDisplay.setFontForced(null);
		rightDisplay.setFontForced(null);
		
		leftDisplay.updateMatrix(left);
		rightDisplay.updateMatrix(right);

		leftDisplay.setFontForced(forcedFont);
		rightDisplay.setFontForced(forcedFont);		
		
		leftDisplay.updateMatrix(left);
		rightDisplay.updateMatrix(right);	

		this.repaint();
		this.revalidate();
	}
	
	/**
	 * Prepares the left, the right and the temporary matrix to show
	 * the content of the given matrices.
	 * 
	 * @param left
	 * @param right
	 * @param temp
	 */
	private void trippleView(Matrix left, Matrix right, Matrix temp) {
		int matrixWidth, matrixHeight;
		
		matrixHeight = calculateMatrixHeight();
		matrixWidth = calculateDualMatrixWidth();
		
		leftDisplay.setVisible(true);
		rightDisplay.setVisible(true);
		tempDisplay.setVisible(true);
		
		leftDisplay.setBounds((int)leftDisplay.getBounds().getX(), (int)leftDisplay.getBounds().getY(), matrixWidth, calculateMatrixDisplayHeight());
		rightDisplay.setBounds((int)rightDisplay.getBounds().getX(), (int)rightDisplay.getBounds().getY(), matrixWidth, calculateMatrixDisplayHeight());
		tempDisplay.setBounds((int)tempDisplay.getBounds().getX(), (int)tempDisplay.getBounds().getY(), matrixWidth, calculateMatrixDisplayHeight());
		
		leftDisplay.setFontForced(null);
		rightDisplay.setFontForced(null);
		tempDisplay.setFontForced(null);
		
		leftDisplay.updateMatrix(left);
		rightDisplay.updateMatrix(right);
		tempDisplay.updateMatrix(temp);
		
		leftDisplay.setFontForced(forcedFont);
		rightDisplay.setFontForced(forcedFont);
		tempDisplay.setFontForced(forcedFont);	
		
		leftDisplay.updateMatrix(left);
		rightDisplay.updateMatrix(right);
		tempDisplay.updateMatrix(temp);
		
		this.repaint();
		this.revalidate();		
	}	

	/**
	 * Cancels a running animation.
	 * 
	 */
	private void cancelAnimations() {
			if ((animator != null) && (!animator.isCanceled())) {
				animator.cancelAnimation();
			}
	}
	
	/**
	 * Animates the switch from any step to the first step.
	 * 
	 * @param changer
	 */
	private void animateBackToStart(MatrixChanger changer) {
		leftDisplay.removePathHighlighting();

		animator = new MatrixMoveAnimator(changer);
		animator.addAnimator(leftDisplay, 1, calculateSingleMatrixPosition());
		animator.addAnimator(rightDisplay, 1, calculateRightOuterPosition()); 		
		
		animator.startAnimation();
	}
	
	/**
	 * Animates the switch from the first step to the first
	 * group or atomic step.
	 * 
	 * @param changer
	 */
	private void animateStartToStep(MatrixChanger changer) {
		animator = new MatrixMoveAnimator(changer);
		
		animator.addAnimator(leftDisplay, -1, calculateDualMatrixPositionLeft());
		animator.addAnimator(rightDisplay, -1, calculateDualMatrixPositionRight());

		animator.startAnimation();
	}
	
	/**
	 * Animates the switch to the last step.
	 * 
	 * @param changer
	 */
	private void animateReachedEnd(MatrixChanger changer) {
		animator = new MatrixMoveAnimator(changer);
		
		animator.addAnimator(leftDisplay, -1, calculateLeftOuterPosition());
		animator.addAnimator(rightDisplay, -1, calculateSingleMatrixPosition());

		animator.startAnimation();
	}
	
	/**
	 * Animates the switch from the last step to a group step or an atomic step.
	 * 
	 * @param changer
	 */
	private void animateEndToStep(MatrixChanger changer) {
		animator = new MatrixMoveAnimator(changer);
		
		animator.addAnimator(leftDisplay, 1, calculateDualMatrixPositionLeft());
		animator.addAnimator(rightDisplay, 1, calculateDualMatrixPositionRight());

		animator.startAnimation();
	}
	
	/**
	 * Animates the switch to the next group step from a group step or atomic step.
	 * 
	 * @param changer
	 */
	private void animateGroupStepNext(MatrixChanger changer) {
		animator = new MatrixMoveAnimator(changer);
		
		animator.addAnimator(leftDisplay, -1, calculateLeftOuterPosition());
		animator.addAnimator(rightDisplay,-1, calculateDualMatrixPositionLeft());
		animator.addAnimator(tempDisplay, -1, calculateDualMatrixPositionRight());

		animator.startAnimation();
	}

	/**
	 * Animates the switch to the previous group step from a group step or atomic step.
	 * 
	 * @param changer
	 */
	private void animateGroupStepPrev(MatrixChanger changer) {
		animator = new MatrixMoveAnimator(changer);
		
		animator.addAnimator(leftDisplay, 1, calculateDualMatrixPositionRight());
		animator.addAnimator(rightDisplay, 1, calculateRightOuterPosition());
		animator.addAnimator(tempDisplay, 1, calculateDualMatrixPositionLeft());

		animator.startAnimation();
	}

	/**
	 * Probes the best font for a matrix displayes in "matrix".
	 * 
	 * @param matrix
	 * @return
	 */
	private Font probeFont(Matrix matrix) {
		Font font;
		
		probeDisplay.scaleTo(calculateDualMatrixWidth(), calculateDualMatrixWidth());
		probeDisplay.setFontForced(null);
		probeDisplay.updateMatrix(rootStep.getAfterMatrix());
		
		font = probeDisplay.getCurrentFont();
		
		if (font == null)
			font = InterfaceConstants.getMatrixFont(beamerMode);
		else
			font = new Font(font.getName(), font.getStyle(), font.getSize());

		return font;
	}
	
	/**
	 * Probes the best font for the whole algorithm by testing the ideal font for
	 * the initial or the final matrix.
	 */
	private void probeForcedFont() {
		Font start, end;
		
		if (rootStep == null) {
			forcedFont = InterfaceConstants.getMatrixFont(beamerMode);
			return;
		}
		
		start = probeFont(rootStep.getBeforeMatrix());
		end = probeFont(rootStep.getAfterMatrix());
		
		if (start.getSize() > end.getSize()) {
			forcedFont = end;
		}
		else {
			forcedFont = start;
		}
	}
	
	/**
	 * Entering the algorithm mode.
	 */
	public void enterHighlightMode(RootStep step) {
		rootStep = step;
		
		arrangeSingleViewLeft();
		
		probeForcedFont();
	}

	/**
	 * Leaving the algorithm mode.
	 */
	public void leaveHighlightMode() {
		rootStep = null;
	}	
	
	public void highlightFirstStep(RootStep step) {
		MatrixChanger changer;
		ViewMode oldMode;
		
		if (!isVisible)
			return;	

		cancelAnimations();	
		oldMode = viewMode;
		changer = new StartChanger();
		changer.setStep(step);

		
		if ((oldMode == ViewMode.GroupStepView) || (oldMode == ViewMode.AtomicStepView)) {
			animateBackToStart(changer);
		}
		 else {
			changer.show();
		}
	}		
	
	public void highlightLastStep(RootStep step) {
		MatrixChanger changer;
		ViewMode oldMode;
		
		if (!isVisible)
			return;
		
		cancelAnimations();		
		oldMode = viewMode;
		changer = new EndChanger();
		changer.setStep(step);
		
		if ((oldMode == ViewMode.GroupStepView) || (oldMode == ViewMode.AtomicStepView)) {
			animateReachedEnd(changer);
		}
		 else {
			changer.show();
		}
	}
	
	public void highlightAtomicStep(AtomicStep step, boolean isForward) {
		MatrixChanger changer;		
		ViewMode oldMode;
		boolean needUpdate;
		
		if (!isVisible)
			return;

		cancelAnimations();
		
		oldMode = viewMode;
		needUpdate = (oldMode == ViewMode.StartView) || (oldMode == ViewMode.EndView) || (oldMode == ViewMode.GroupStepView) || (!isForward);
		
		changer = new AtomicStepChanger(needUpdate, isForward);
		changer.setStep(step);
		
		if (oldMode == ViewMode.StartView)
			animateStartToStep(changer);
		else if (oldMode == ViewMode.EndView)
			animateEndToStep(changer);			
		else {
			// Reverse switch from group step to atomic step
			if (   (isForward == false) 
				&& (currentStep instanceof GroupStep)
			   )
			{
				changer = new PrevGroupStepToAtomicChanger();
				changer.setStep(step);
				
				animateGroupStepPrev(changer);
			}
			else {
				changer.show();
			}
		}
	}
	
	public void highlightGroupStep(GroupStep step, boolean isForward) {
		MatrixChanger changer;		
		ViewMode oldMode = viewMode;
		
		if (!isVisible)
			return;
		
		cancelAnimations();	

		if (isForward)
			changer = new NextGroupStepChanger();
		else
			changer = new PrevGroupStepChanger();
		
		changer.setStep(step);
		
		if (oldMode == ViewMode.StartView)
			animateStartToStep(changer);
		else if (oldMode == ViewMode.EndView)
			animateEndToStep(changer);
		else if (isForward)
			animateGroupStepNext(changer);
		else {
			// Reverse switch from atomic step to group step
			if (currentStep instanceof AtomicStep) {
				changer.show();
			}
			else
				animateGroupStepPrev(changer);
		}
	}
	
	/**
	 * Used to split up the display operations during an animation. 
	 * 
	 *
	 */
	private abstract class MatrixChanger {
		/**
		 * Sets the step which will be shown after the animation.
		 * 
		 * @param step
		 */
		public abstract void setStep(Step step);
		
		/**
		 * Processes all actions needed before the start of the animation.
		 * (Like placing the matrix display to the start positions of 
		 * the animation or loading the right content).
		 * 
		 * This function is usually called by the Animator object. 
		 */
		public abstract void preAnimation();
		
		/**
		 * Processes all actions needed to finish the animation
		 * (Like placing the matrix display to the final position after
		 * the animation and setting the correct highlights and matrix name)
		 * 
		 * This function is usually called by the Animator object.
		 */
		public abstract void postAnimation();
		
		/**
		 * Executes the pre- and post operations of the animation. This
		 * is needed if an action should be executed without any animation.
		 */
		public void show() {
			preAnimation();
			postAnimation();
		}
	}
	
	/**
	 * Used to setup the display of any group step.
	 *
	 */	
	private abstract class GroupMatrixChanger extends MatrixChanger {
		protected GroupStep step;
		
		public void setStep(Step step) {
			this.step = (GroupStep)step;
		}
	}
	
	/**
	 * Used to setup the switching to an atomic step.
	 *
	 */	
	private abstract class AtomicMatrixChanger extends MatrixChanger {
		protected AtomicStep step;
		
		public void setStep(Step step) {
			this.step = (AtomicStep)step;
		}
	}	

	/**
	 * Used to show the switching to any group step 
	 *
	 */	
	private class GenericGroupStepChanger extends GroupMatrixChanger {
		public void preAnimation() {
			leftDisplay.setMatrixName(true, ((AtomicStep)step.getStep(0)).getK() - 1);
			rightDisplay.setMatrixName(true, ((AtomicStep)step.getStep(0)).getK());					 
		}
		
		public void postAnimation() {
			arrangeDualView();
			
			leftDisplay.setMatrixName(true, ((AtomicStep)step.getStep(0)).getK());
			rightDisplay.setMatrixName(true, ((AtomicStep)step.getStep(0)).getK() + 1);

			currentStep = step;
			viewMode = ViewMode.GroupStepView;
		}
	}	
	
	/**
	 * Used to show a group step without switching.
	 *
	 */	
	private class GroupStepChanger extends GenericGroupStepChanger {
	}
	
	/**
	 * Used to setup the switching to the initial matrix.
	 *
	 */	
	private class StartChanger extends GroupMatrixChanger {
		public void preAnimation() {
			leftDisplay.setVisible(true);
			rightDisplay.setVisible(true);
			tempDisplay.setVisible(false);			
			singleViewLeft(step.getBeforeMatrix());
			
			leftDisplay.setMatrixName(true, 0);
		}
		
		public void postAnimation() {
			arrangeSingleViewLeft();
			leftDisplay.setMatrixName(false, 0);
			
			currentStep = step;
			viewMode = ViewMode.StartView;
		}
	}

	/**
	 * Used to setup the switching to the final matrix.
	 *
	 */	
	private class EndChanger extends GroupMatrixChanger {
		public void preAnimation() {
			leftDisplay.setVisible(true);
			rightDisplay.setVisible(true);
			tempDisplay.setVisible(false);
			dualView(step.getBeforeMatrix(), step.getAfterMatrix());
		}
		
		public void postAnimation() {
			arrangeSingleViewRight();
			leftDisplay.setMatrixName(false, 0);
			rightDisplay.removeChangeHighlighting();
			
			currentStep = step;
			viewMode = ViewMode.EndView;
		}
	}	
	
	/**
	 * Used to setup the switching to a next group step.
	 *
	 */	
	private class NextGroupStepChanger extends GenericGroupStepChanger {
		public void preAnimation() {
			if (viewMode == ViewMode.EndView) {
				leftDisplay.setVisible(true);
				rightDisplay.setVisible(true);
				tempDisplay.setVisible(true);
				 
				dualView(step.getBeforeMatrix(), step.getBeforeMatrix());
			}
			 else
			{
				 leftDisplay.setVisible(true);
				 rightDisplay.setVisible(true);
				 tempDisplay.setVisible(true);
				 
				 arrangeView(tempDisplay, calculateRightOuterPosition());
				 trippleView(step.getBeforeMatrix(), step.getBeforeMatrix(), step.getBeforeMatrix());
				 tempDisplay.setMatrixName(true, ((AtomicStep)step.getStep(0)).getK() + 1);
			}
				
			super.preAnimation();			
		}
		
		public void postAnimation() {
			if (viewMode != ViewMode.EndView) {
				MatrixDisplay buffer;

				buffer = leftDisplay;
				leftDisplay = rightDisplay;
				rightDisplay = tempDisplay;
				tempDisplay = buffer;
				
				tempDisplay.setVisible(false);
			}

			super.postAnimation();
		}
	}
	
	/**
	 * Used to setup the switching to a previous group step.
	 *
	 */
	private class PrevGroupStepChanger extends GenericGroupStepChanger {
		public void preAnimation() {
			if (viewMode == ViewMode.StartView) {
				 leftDisplay.setVisible(true);
				 rightDisplay.setVisible(true);
				 tempDisplay.setVisible(true);

				dualView(step.getBeforeMatrix(), step.getBeforeMatrix());
			}
			 else
			{
				 leftDisplay.setVisible(true);
				 rightDisplay.setVisible(true);
				 tempDisplay.setVisible(true);
				 
				 arrangeView(tempDisplay, calculateLeftOuterPosition());
				 trippleView(step.getBeforeMatrix(), step.getBeforeMatrix(), step.getBeforeMatrix());
				 tempDisplay.setMatrixName(true, ((AtomicStep)step.getStep(0)).getK());
			}
				
			super.preAnimation();
		}
		
		public void postAnimation() {
			if (viewMode != ViewMode.EndView) {
				MatrixDisplay buffer;

				buffer = rightDisplay;
				rightDisplay = leftDisplay;
				leftDisplay = tempDisplay;
				tempDisplay = buffer;
				
				tempDisplay.setVisible(false);
			}

			super.postAnimation();
		}
	}	
	
	/**
	 * Used to setup an atomic step.
	 *
	 */
	private class AtomicStepChanger extends AtomicMatrixChanger {
		private boolean needUpdate;
		private boolean isForward;
		
		public AtomicStepChanger() {
			this.needUpdate = true;
		}
		
		public AtomicStepChanger(boolean needUpdate, boolean isForward) {
			this.needUpdate = needUpdate;
			this.isForward = isForward;
		}
		
		public void preAnimation() {
			if (needUpdate)
				dualView(step.getGroupStep().getBeforeMatrix(), step.getAfterMatrix());
			else if (    (isForward && !(step.getGroupStep().getBeforeMatrix().getValueAt(step.getU(), step.getV()).equals(step.getAfterMatrix().getValueAt(step.getU(), step.getV()))))
			          || (!isForward && (step.getGroupStep().getBeforeMatrix().getValueAt(step.getU(), step.getV()).equals(step.getAfterMatrix().getValueAt(step.getU(), step.getV()))))
			        )
			{
				rightDisplay.updateMatrix(step.getBeforeMatrix());
				rightDisplay.repaint();
				rightDisplay.revalidate();
			}
				
				
			leftDisplay.setMatrixName(true, step.getK());
			rightDisplay.setMatrixName(true, step.getK() + 1);		
		}
		
		public void postAnimation() {
			arrangeDualView();
			
			leftDisplay.setPathHighlighting(step.getU(), step.getK(), step.getV());
			rightDisplay.setChangeHighlighting(step.getU(), step.getV());
			
			currentStep = step;
			viewMode = ViewMode.AtomicStepView;
		}
	}	

	private class PrevGroupStepToAtomicChanger extends AtomicStepChanger {
		public void preAnimation() {
			 leftDisplay.setVisible(true);
			 rightDisplay.setVisible(true);
			 tempDisplay.setVisible(true);
				 
			 arrangeView(tempDisplay, calculateLeftOuterPosition());
			 trippleView(step.getGroupStep().getAfterMatrix(), step.getGroupStep().getAfterMatrix(), step.getGroupStep().getBeforeMatrix());
			 tempDisplay.setMatrixName(true, step.getK());
		}		

		public void postAnimation() {
			AtomicStepChanger changer;
			MatrixDisplay buffer;

			buffer = rightDisplay;
			rightDisplay = leftDisplay;
			leftDisplay = tempDisplay;
			tempDisplay = buffer;
			
			tempDisplay.setVisible(false);
			
			super.postAnimation();
		}
	}	
	
	/**
	 * Used for generic animation tasks.
	 *
	 */
	private abstract class Animator extends TimerTask {
		private Timer localTimer;	
		private boolean canceled;	
		private MatrixChanger changer;
		
		/**
		 * Initializes an animation object.
		 * 
		 * @param changer
		 * 		A MatrixChanger object which contains callback
		 * 		functions which should be called before and after
		 * 		the animation.
		 * 
		 */
		public Animator(MatrixChanger changer) {
			this.changer = changer;
			this.canceled = false;			
		}
		
		public boolean isCanceled() {
			return this.canceled;
		}
		
		public void startAnimation() {
			changer.preAnimation();
			
			localTimer = new Timer();
			
			localTimer.schedule(this, 0, 4);
		}
		
		public void cancelAnimation() {
			if (canceled)
				return;
			
			localTimer.cancel();
			localTimer.purge();

			canceled = true;
			
			if (changer != null)
				changer.postAnimation();
		}		
	}
	
	private class MatrixMoveAnimator extends Animator {
		private List<AnimationTask>	tasks;
		
		private class AnimationTask {
			MatrixDisplay display;
			float		  step;
			int			  dest;
			
			public AnimationTask(MatrixDisplay display, float step, int dest) {
				this.display = display;
				this.step = step;
				this.dest = dest;
			}
		}

		private float calculateAnimationStep() {
			return getWidth() / 100;
		}
				
		public MatrixMoveAnimator(MatrixChanger changer) {
			super(changer);
			
			tasks = new ArrayList<AnimationTask>();
		}
		
		/**
		 * Adds an animation task to the animator.
		 * 
		 * @param component
		 * 		The component which should be animated
		 * 
		 * @param step
		 * 		The step which should be shown after the animation
		 * 
		 * @param destX
		 * 		The destination coordinates of the animation
		 * 
		 */
		public void addAnimator(MatrixDisplay component, float step, int destX)
		{
			tasks.add(new AnimationTask(component, step, destX));
		}

		/**
		 * Executes all move actions of one particular animation task.
		 * 
		 * @param task
		 * @return
		 */
		private boolean moveTask(AnimationTask task) {
			Rectangle rect = task.display.getBounds();
			float step;
			
			step = task.step * calculateAnimationStep();
			
			if (    ((rect.x + step >= task.dest) && (task.step > 0))
				 || ((rect.x + step <= task.dest) && (task.step < 0))
			   )
				return true;
			
			rect.x += step;
			
			task.display.setBounds(rect);
			
			return false;
		}
		
		/**
		 * Does one animation step of all animation tasks.
		 * 
		 */
		@Override
		public void run() {
			boolean allTerminated = true;
			
			for (AnimationTask task : tasks) {
				allTerminated &= moveTask(task);
			}
			
			if (allTerminated)
				cancelAnimation();
			
			repaint();
			revalidate();
		}
	}
	
	private class MatrixComponentListener implements ComponentListener {

		public void componentHidden(ComponentEvent arg0) {
			componentResized(arg0);			
		}

		public void componentMoved(ComponentEvent arg0) {
			componentResized(arg0);			
		}

		public void componentResized(ComponentEvent event) {
			if (!isVisible)
				return;
			
			probeForcedFont();
			
			switch (viewMode) {
				case StartView :
					arrangeSingleViewLeft();
					
					break;
				
				case EndView :
					arrangeSingleViewRight();
					
					break;
				
				case GroupStepView :
					arrangeDualView();

					break;
					
				case AtomicStepView :
					arrangeDualView();

					break;
					
			}

			leftDisplay.redrawCurrentMatrix();
			rightDisplay.redrawCurrentMatrix();
			tempDisplay.redrawCurrentMatrix();			
			
			repaint();
			revalidate();
		}
		
		public void componentShown(ComponentEvent arg0) {
			componentResized(arg0);			
		}
	}
}
