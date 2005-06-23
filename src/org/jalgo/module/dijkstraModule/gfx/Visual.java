/*
 * Created on 07.05.2005
 */

package org.jalgo.module.dijkstraModule.gfx;

import org.jalgo.module.dijkstraModule.gfx.GraphParent;
import org.jalgo.module.dijkstraModule.gui.Controller;

/**
 * Semi-abstract class that serves as a foundation for visual representation objects.
 * It mainly provides flags that indicate state. Visuals are wrapper objects which contain 
 * other actual drawing elements that are displayed on a {@link GraphParent}.
 * <br><br>The flags are interpreted by the update() method, which is implemented differently 
 * by each visual subclass and changes the appearance of the drawing elements.
 * Most flags are not exclusive but can be interpreted in a hierarchic manner.
 * For example, the ACTIVE flag overrides all other flags, CHANGED has second priority, 
 * and so on. In addition, some flags have different meanings depending on the mode
 * (editing or algorithm) or may have no meaning at all in one of the modes.
 * @author Martin Winter
 */

public abstract class Visual {
	
	protected GraphParent parent;		// Needed for mouse dragging and for access to the controller.

	/**
	 * Default state.
	 * Used in editing and algorithm mode.
	 */
	public static final int NONE = 0;
	
	/**
	 * The element has the immediate focus.
	 * Used in editing and algorithm mode.
	 */
	public static final int ACTIVE = 1;
	
	/**
	 * The element is in the border set.
	 * Used in algorithm mode only.
	 */
	public static final int BORDER = 2;
	
	/**
	 * The element has just been changed.
	 * Used in editing mode only.
	 */
	public static final int CHANGED = 4;
	
	/**
	 * The element is in the chosen set.
	 * Used in algorithm mode only.
	 */
	public static final int CHOSEN = 8;
	
	/**
	 * The element is an edge in conflict with another edge.
	 * Used in algorithm mode only.
	 */
	public static final int CONFLICT = 16;
	
	/**
	 * The element has been touched by the mouse cursor.
	 * Used in editing mode only.
	 */
	public static final int HIGHLIGHTED = 32;
	
	/**
	 * The element is the start node.
	 * Used in algorithm mode only.
	 */
	public static final int START = 64;
	
	/**
	 * Boolean flags corresponding to the integer flags above.
	 */
	private boolean isActive;
	private boolean isBorder;
	private boolean isChanged;
	private boolean isChosen;
	private boolean isConflict;
	private boolean isHighlighted;
	private boolean isStart;
	
	/**
	 * Creates a new visual and sets the graph parent.
	 * @param parent the graph parent that the drawing elements should appear on
	 */
	public Visual(GraphParent parent) {
		setParent(parent);
	}

	/**
	 * Adds the actual drawing elements to the graph parent.
	 * @param parent the graph parent that the drawing elements should appear on
	 */
	public abstract void addToParent(GraphParent parent);
	
	/**
	 * Sets the graph parent.
	 * @param parent the graph parent that the drawing elements should appear on
	 */
	public void setParent(GraphParent parent) {
		this.parent = parent;
	}
	
	/**
	 * Returns the graph parent.
	 * @return the graph parent that the drawing elements appear on
	 */
	public GraphParent getParent() {
		return parent;
	}
	
	/**
	 * Returns the controller of the graph parent.
	 * @return the controller of the graph parent or <code>null</code> if parent is <code>null</code>
	 */
	public Controller getController() {
		if (parent != null) {
			return parent.getController();
		} else {
			return null;
		}
	}
	
	/**
	 * Returns the mode of the graph parent's controller.
	 * @return the mode of the graph parent's controller or -1 if parent or controller are <code>null</code>
	 */
	public int getControllerMode() {
		if ( (parent != null) && (parent.getController() != null) ) {
			return parent.getController().getEditingMode();
		} else {
			return -1;
		}
	}
	
	/**
	 * Returns <code>true</code> if the graph parent's controller is in editing mode.
	 * @return <code>true</code> if the graph parent's controller is in editing mode or if parent is <code>null</code>
	 */
	public boolean isInEditingMode() {
		if (parent != null) {
			return (getControllerMode() != Controller.MODE_ALGORITHM);
		} else {
			return true;
		}
	}

	
	/* Flag accessors. */
	
	/**
	 * Sets flags from a single integer value.
	 * It sets the boolean flags by using logical AND on <code>flags</code>.
	 */
	public void setFlags(int flags) {
		isActive = ((flags & ACTIVE) > 0);
		isBorder = ((flags & BORDER) > 0);
		isChanged = ((flags & CHANGED) > 0);
		isChosen = ((flags & CHOSEN) > 0);
		isConflict = ((flags & CONFLICT) > 0);
		isHighlighted = ((flags & HIGHLIGHTED) > 0);
		isStart = ((flags & START) > 0);
	}
	
	/**
	 * Gets flags as a single integer value.
	 * It does so by combining the boolean flags with a logical OR.
	 * @return flags as a single integer value
	 */
	public int getFlags() {
		int flags = NONE;
		if (isActive) flags |= ACTIVE;
		if (isBorder) flags |= BORDER;
		if (isChanged) flags |= CHANGED;
		if (isChosen) flags |= CHOSEN;
		if (isConflict) flags |= CONFLICT;
		if (isHighlighted) flags |= HIGHLIGHTED;
		if (isStart) flags |= START;
		return flags;
	}
	
	/**
	 * Sets the active flag.
	 * @param isActive the active flag as a boolean
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	/**
	 * Gets the active flag.
	 * @return the active flag as a boolean
	 */
	public boolean isActive() {
		return isActive;
	}
	
	/**
	 * Sets the border flag.
	 * @param isBorder
	 */
	public void setBorder(boolean isBorder) {
		this.isBorder = isBorder;
	}
	
	/**
	 * Gets the border flag.
	 * @return the border flag as a boolean
	 */
	public boolean isBorder() {
		return isBorder;
	}
	
	/**
	 * Sets the changed flag.
	 * @param isChanged
	 */
	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}
	
	/**
	 * Gets the changed flag.
	 * @return the changed flag as a boolean
	 */
	public boolean isChanged() {
		return isChanged;
	}
	
	/**
	 * Sets the chosen flag.
	 * @param isChosen
	 */
	public void setChosen(boolean isChosen) {
		this.isChosen = isChosen;
	}
	
	/**
	 * Gets the chosen flag.
	 * @return the chosen flag as a boolean
	 */
	public boolean isChosen() {
		return isChosen;
	}
	
	/**
	 * Sets the conflict flag.
	 * @param isConflict
	 */
	public void setConflict(boolean isConflict) {
		this.isConflict = isConflict;
	}
	
	/**
	 * Gets the conflict flag.
	 * @return the conflict flag as a boolean
	 */
	public boolean isConflict() {
		return isConflict;
	}
	
	/**
	 * Sets the highlighted flag.
	 * @param isHighlighted
	 */
	public void setHighlighted(boolean isHighlighted) {
		this.isHighlighted = isHighlighted;
	}
	
	/**
	 * Gets the highlighted flag.
	 * @return the highlighted flag as a boolean
	 */
	public boolean isHighlighted() {
		return isHighlighted;
	}
	
	/**
	 * Sets the start flag.
	 * @param isStart
	 */
	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}
	
	/**
	 * Gets the start flag.
	 * @return the start flag as a boolean
	 */
	public boolean isStart() {
		return isStart;
	}
	
	
	/**
	 * Update appearance according to flags.
	 * Call this method after modifying any flags.
	 */
	public abstract void update();
}
