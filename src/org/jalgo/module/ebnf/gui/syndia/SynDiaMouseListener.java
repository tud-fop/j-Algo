package org.jalgo.module.ebnf.gui.syndia;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.event.MouseInputAdapter;

import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.gui.trans.SynDiaElemNotFoundException;
import org.jalgo.module.ebnf.model.syndia.Branch;
import org.jalgo.module.ebnf.model.syndia.Concatenation;
import org.jalgo.module.ebnf.model.syndia.NullElem;
import org.jalgo.module.ebnf.model.syndia.Repetition;
import org.jalgo.module.ebnf.model.syndia.SynDiaElem;
import org.jalgo.module.ebnf.model.syndia.TerminalSymbol;
import org.jalgo.module.ebnf.model.syndia.Variable;
import org.jalgo.module.ebnf.renderer.elements.RenderElement;
import org.jalgo.module.ebnf.renderer.elements.RenderName;
import org.jalgo.module.ebnf.renderer.elements.RenderNullElem;
import org.jalgo.module.ebnf.renderer.elements.RenderTerminal;
import org.jalgo.module.ebnf.renderer.elements.RenderVariable;

/**
 * This class reacts to all sorts of mouse input. It contains information about
 * the current mode (edit, insert variable, etc.) and status of docked elements.
 * 
 * @author Michael Thiele
 * 
 */
public class SynDiaMouseListener extends MouseInputAdapter {

	final static int MODE_EDIT = 0;

	final static int MODE_TERMINAL = 1;

	final static int MODE_VARIABLE = 2;

	final static int MODE_REPETITION = 3;

	final static int MODE_BRANCH = 4;

	final static int MODE_WORDWRAP = 5;

	final static int MODE_DELETE = 6;

	private final int STATUS_NOTDOCKED = 0;

	private final int STATUS_LEFTDOCKED = 1;

	private int currentMode = MODE_EDIT;

	private int currentStatus = STATUS_NOTDOCKED;

	private NullElem leftNullElem;

	private GuiController guiController;

	private Map<RenderElement, SynDiaElem> renderMap;

	/**
	 * Initializes the <code>SynDiaMouseListener</code>.
	 * 
	 * @param guiController
	 *            the guiController for this mouse listener
	 * @param renderMap
	 *            the render map with the actual syntax diagram
	 */
	public SynDiaMouseListener(GuiController guiController,
			Map<RenderElement, SynDiaElem> renderMap) {
		this.guiController = guiController;
		this.renderMap = renderMap;
		leftNullElem = null;
	}

	private SynDiaElem getElem(Component component) {
		if (renderMap.containsKey(component)) {
			return renderMap.get(component);
		}
		return null;
	}

	/**
	 * Sets the actual mode. Current status is set to "not docked".
	 * 
	 * @param newMode
	 *            the new mode to set
	 */
	public void setMode(int newMode) {
		currentMode = newMode;
		guiController.hideTiledBranch();
		guiController.hideTiledRepetition();
		currentStatus = STATUS_NOTDOCKED;
	}

	public void mouseClicked(MouseEvent e) {
		// if the right mouse button is pressed, the ChangeEditModeMenu pops
		// up
		if (e.getButton() == MouseEvent.BUTTON3) {
			// set ChangeElemListener back if we were in edit mode
			if (currentMode == MODE_EDIT) {
				guiController.setChangeElemListenerBack();
				// if it pops up over a terminal symbol or variable you have to
				// remove the color of this element
				if (e.getComponent() instanceof RenderTerminal
						|| e.getComponent() instanceof RenderVariable) {
					((RenderElement) e.getComponent())
							.setColor(RenderElement.STANDARD_FILL_COLOR);
				}
			}
			if (!(e.getComponent() instanceof RenderTerminal)
					|| !(e.getComponent() instanceof RenderVariable)) {
				// set ColorListener back if there are some red now
				if ((currentMode == MODE_BRANCH || currentMode == MODE_REPETITION)
						&& currentStatus == STATUS_LEFTDOCKED) {
					guiController.setNullElemColorListenerBack();
				}
				// set DeleteElemListener back if we were in delete mode
				if (currentMode == MODE_DELETE) {
					guiController.setDeleteElemListenerBack();
				}
				// if popup is over RenderElement compute new position
				if (e.getSource() instanceof RenderElement) {
					guiController.showChangeEditModeMenu(e.getComponent()
							.getX()
							+ e.getX() + e.getComponent().getParent().getX(), e
							.getComponent().getY()
							+ e.getY() + e.getComponent().getParent().getY());
				} else {
					guiController.showChangeEditModeMenu(e.getX(), e.getY());
				}
			}
		}
		// a left-click
		if (e.getButton() == MouseEvent.BUTTON1) {
			Component currentComponent = e.getComponent();
			// MODE_EDIT
			if (currentMode == MODE_EDIT) {
				if (currentComponent instanceof RenderTerminal) {
					guiController.showEditTextField(currentComponent.getX()
							+ currentComponent.getParent().getX(),
							currentComponent.getY()
									+ currentComponent.getParent().getY(),
							getElem(currentComponent));
				} else if (currentComponent instanceof RenderVariable) {
					guiController.showEditTextField(currentComponent.getX()
							+ currentComponent.getParent().getX(),
							currentComponent.getY()
									+ currentComponent.getParent().getY(),
							getElem(currentComponent));
				} else if (currentComponent instanceof RenderName) {
					// getCompontent(0) always has to be NullElem!!!
					guiController.showEditSynDiaName(((SynDiaElem) renderMap
							.get(currentComponent.getParent().getComponent(0)))
							.getMySyntaxDiagram(), 20, e.getY()
							+ currentComponent.getParent().getY(), Messages
							.getString("ebnf", "SynDiaEditor.NameOfDiagram"));
				}
			}
			// MODE_TERMINAL
			else if (currentMode == MODE_TERMINAL) {
				if (currentComponent instanceof RenderNullElem) {
					guiController.showNewTextField(currentComponent.getX()
							+ currentComponent.getParent().getX(),
							currentComponent.getY()
									+ currentComponent.getParent().getY(),
							currentMode, currentComponent);
				}
			}
			// MODE_VARIABLE
			else if (currentMode == MODE_VARIABLE) {
				if (currentComponent instanceof RenderNullElem) {
					guiController.showNewTextField(currentComponent.getX()
							+ currentComponent.getParent().getX(),
							currentComponent.getY()
									+ currentComponent.getParent().getY(),
							currentMode, currentComponent);
				}
			}
			// MODE_WORDWRAP
			else if (currentMode == MODE_WORDWRAP) {
				if (currentComponent instanceof RenderNullElem) {
					guiController.getSynDiaController().addWordWrap(
							(NullElem) getElem(currentComponent));
				}
			}
			// MODE_BRANCH
			else if (currentMode == MODE_BRANCH) {
				if (currentComponent instanceof RenderNullElem) {
					// if left side is already fixed, try to fix right side
					if (currentStatus == STATUS_LEFTDOCKED) {
						if (leftNullElem.getLine() == ((NullElem) getElem(e
								.getComponent())).getLine()) {
							NullElem rightNullElem = (NullElem) getElem(currentComponent);
							if (leftNullElem.getIndex() <= rightNullElem
									.getIndex()) {
								guiController.getSynDiaController().addBranch(
										leftNullElem, rightNullElem);
							} else {
								guiController.getSynDiaController().addBranch(
										rightNullElem, leftNullElem);
							}
							guiController.hideTiledBranch();
							guiController.setCursor("branch");
							currentStatus = STATUS_NOTDOCKED;
						}
					}
					// if this is the first click, save the left NullElem and
					// set status to left docked
					else if (currentStatus == STATUS_NOTDOCKED) {
						currentStatus = STATUS_LEFTDOCKED;
						leftNullElem = (NullElem) getElem(currentComponent);
						guiController.setCursor("branchRight");
						guiController.setNullElemColorListener(leftNullElem);
						guiController.showTiledBranch(currentComponent);
					}
				}
			}
			// MODE_REPETITION
			else if (currentMode == MODE_REPETITION) {
				if (currentComponent instanceof RenderNullElem) {
					// if left side is already fixed, try to fix right side
					if (currentStatus == STATUS_LEFTDOCKED) {
						if (leftNullElem.getLine() == ((NullElem) getElem(e
								.getComponent())).getLine()) {
							NullElem rightNullElem = (NullElem) getElem(currentComponent);
							if (leftNullElem.getIndex() <= rightNullElem
									.getIndex()) {
								guiController.getSynDiaController()
										.addRepetition(leftNullElem,
												rightNullElem);
							} else {
								guiController.getSynDiaController()
										.addRepetition(rightNullElem,
												leftNullElem);
							}
							guiController.hideTiledRepetition();
							guiController.setCursor("repetition");
							currentStatus = STATUS_NOTDOCKED;
						}
					}
					// if this is the first click, save the left NullElem and
					// set status to left docked
					else if (currentStatus == STATUS_NOTDOCKED) {
						currentStatus = STATUS_LEFTDOCKED;
						leftNullElem = (NullElem) getElem(currentComponent);
						guiController.setCursor("repetitionRight");
						guiController.setNullElemColorListener(leftNullElem);
						guiController.showTiledRepetition(currentComponent);
					}
				}
			}
			// MODE_DELETE
			else if (currentMode == MODE_DELETE) {
				if (currentComponent instanceof RenderTerminal) {
					guiController.getSynDiaController().removeTerminal(
							(TerminalSymbol) getElem(currentComponent));
				} else if (currentComponent instanceof RenderVariable) {
					guiController.getSynDiaController().removeVariable(
							(Variable) getElem(currentComponent));
				}
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
		Component currentComponent = e.getComponent();
		if (currentComponent instanceof RenderNullElem) {
			if (currentMode == MODE_TERMINAL) {
				// guiController.setToolTipTextForNullElems("terminal");
			}
			if (currentStatus == STATUS_LEFTDOCKED) {
				// test, if it is possible to move right end of branch or
				// repetition to this position
				if (leftNullElem.getLine() == ((NullElem) getElem(e
						.getComponent())).getLine()) {
					int begin = leftNullElem.getIndex();
					int end = ((NullElem) getElem(currentComponent)).getIndex();
					if (begin > end) {
						int h = begin;
						begin = end;
						end = h;
					}
					int height = guiController.getRenderRadius() * 4;
					if (begin != end) {
						Concatenation concat = (Concatenation) getElem(
								currentComponent).getParent();
						for (int i = begin + 1; i < end; i++) {
							height = (int) Math.max(height,
									getHeightFromElement(concat
											.getSynDiaElem(i))
											+ 1.5
											* guiController.getRenderRadius());
						}
					}
					if (currentMode == MODE_BRANCH) {
						try {
							guiController.setNewTiledBranchEnd(getReFromSde(
									leftNullElem).getX(), e.getComponent()
									.getX());
						} catch (SynDiaElemNotFoundException e1) {
							e1.printStackTrace();
						}
						guiController.setNewTiledBranchHeight(height);
					} else {
						try {
							guiController.setNewTiledRepetitionEnd(
									getReFromSde(leftNullElem).getX(), e
											.getComponent().getX());
						} catch (SynDiaElemNotFoundException e1) {
							e1.printStackTrace();
						}
						guiController.setNewTiledRepetitionHeight(height);
					}
				} else {
					try {
						if (currentMode == MODE_BRANCH) {
							guiController
									.showTiledBranch(getReFromSde(leftNullElem));
						} else {
							guiController
									.showTiledRepetition(getReFromSde(leftNullElem));
						}
					} catch (SynDiaElemNotFoundException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

	public void mouseExited(MouseEvent e) {
		if (currentStatus == STATUS_LEFTDOCKED) {
			try {
				if (currentMode == MODE_BRANCH) {
					guiController.showTiledBranch(getReFromSde(leftNullElem));
				} else {
					guiController
							.showTiledRepetition(getReFromSde(leftNullElem));
				}
			} catch (SynDiaElemNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Computes the height of a syntax diagram element.
	 * 
	 * @param s
	 *            the syntax diagram element
	 * @return the height of the element
	 */
	private int getHeightFromElement(SynDiaElem s) {
		Component component = null;
		int additionalHeight = 0;
		int height = 0;
		/*
		 * if the element is a branch or repetition there could be additional
		 * height through elements in the right side of these
		 * branches/repetitions
		 */
		if (s instanceof Branch || s instanceof Repetition) {
			Concatenation c = null;
			if (s instanceof Branch)
				c = ((Branch) s).getRight();
			else if (s instanceof Repetition)
				c = ((Repetition) s).getRight();
			for (int i = 0; i < c.getNumberOfElems(); i++) {
				additionalHeight = Math.max(additionalHeight,
						getHeightFromElement(c.getSynDiaElem(i)) - 2
								* guiController.getRenderRadius());
			}
		}
		try {
			component = getReFromSde(s);
		} catch (IndexOutOfBoundsException e1) {
			e1.printStackTrace();
		} catch (SynDiaElemNotFoundException e1) {
			e1.printStackTrace();
		}
		height = Math.max(height, component.getHeight()) + additionalHeight;
		return height;
	}

	/**
	 * @param sde
	 *            a SynDiaElem finding itself in the RenderMap
	 * @return the render element representing the syntax diagram element in the
	 *         model
	 * @throws SynDiaElemNotFoundException
	 */
	private RenderElement getReFromSde(SynDiaElem sde)
			throws SynDiaElemNotFoundException {
		if (renderMap.containsValue(sde)) {
			for (RenderElement re : renderMap.keySet()) {
				if (sde == renderMap.get(re))
					return re;
			}
		}
		throw new SynDiaElemNotFoundException(
				"RenderElement existiert nicht in der Map");
	}

	/**
	 * Is used to set the render map to actual render map.
	 * 
	 * @param renderMap
	 *            the render map to update
	 */
	public void setRenderMap(Map<RenderElement, SynDiaElem> renderMap) {
		this.renderMap = renderMap;
	}
}
