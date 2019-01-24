/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer science. It is written in Java and platform independent. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * Created on 18.05.2005
 *
 */
package org.jalgo.module.dijkstra.model;

import java.io.Serializable;

import org.jalgo.module.dijkstra.gfx.Visual;

/**
 * @author Hannes Strass, Martin Winter
 * 
 * Abstract superclass of Node and Edge. Needed to define static flags.
 *
 */
public abstract class GraphElement
implements Serializable {

	public static final int NONE = 0;
	public static final int ACTIVE = 1;
	public static final int BORDER = 2;
	public static final int CHANGED = 4;
	public static final int CHOSEN = 8;
	public static final int CONFLICT = 16;
	public static final int HIGHLIGHTED = 32;
	public static final int START = 64;	
	
	private boolean isActive;
	private boolean isBorder;
	private boolean isChanged;
    /**
     * @see java.lang.Object#toString()
     * @author Frank Staudinger
     */
    public String toString()
    {
        return "Flags: "+new Integer(getFlags())+";"+super.toString(); //$NON-NLS-1$ //$NON-NLS-2$
    }
	private boolean isChosen;
	private boolean isConflict;
	private boolean isHighlighted;
	private boolean isStart;

	private int flags;

	protected Visual visual;

	/** Gets flags as integer.
	 * @return flags
	 */
	public int getFlags()
	{
		flags = NONE;
		if (isActive) flags |= ACTIVE;
		if (isBorder) flags |= BORDER;
		if (isChanged) flags |= CHANGED;
		if (isChosen) flags |= CHOSEN;
		if (isConflict) flags |= CONFLICT;
		if (isHighlighted) flags |= HIGHLIGHTED;
		if (isStart) flags |= START;
		return flags;
	}
	
	/** Sets the flags as integer.
	 * @param newFlags the new flags to set
	 */
	public void setFlags(int newFlags)
	{
		isActive = ((newFlags & ACTIVE) > 0);
		isBorder = ((newFlags & BORDER) > 0);
		isChanged = ((newFlags & CHANGED) > 0);
		isChosen = ((newFlags & CHOSEN) > 0);
		isConflict = ((newFlags & CONFLICT) > 0);
		isHighlighted = ((newFlags & HIGHLIGHTED) > 0);
		isStart = ((newFlags & START) > 0);
		
		flags = newFlags;
	}
	
	/** Checks whether the given element is "active".
	 */
	public boolean isActive()
	{
		return isActive;
	}
	
	/** Checks whether the element belongs to the border.
	 */
	public boolean isBorder()
	{
		return isBorder;
	}
	
	/** 
	 * @return true if the element was recently changed.
	 */
	public boolean isChanged()
	{
		return isChanged;
	}

	/**
	 * @return true if the element is a chosen one.
	 */
	public boolean isChosen()
	{
		return isChosen;
	}
	
	/**
	 * @return true if the element is in conflict with something.
	 */
	public boolean isConflict()
	{
		return isConflict;
	}
	
	/**
	 * @return true if the element shall be highlighted.
	 */
	public boolean isHighlighted()
	{
		return isHighlighted;
	}
	
	/**
	 * @return true if the element is the start node.
	 */
	public boolean isStart()
	{
		return isStart;
	}
	
	/** Sets the active property of the element.
	 * @param newActive
	 */
	public void setActive(boolean newActive)
	{
		isActive = newActive;
	}
	
	/** Sets the border property of the element.
	 * @param newBorder
	 */
	public void setBorder(boolean newBorder)
	{
		isBorder = newBorder;
	}
	
	/** Marks an object as recently changed or not.
	 * @param newChanged
	 */
	public void setChanged(boolean newChanged)
	{
		isChanged = newChanged;
	}
	
	/** Sets the chosen property of the element.
	 * @param newChosen
	 */
	public void setChosen(boolean newChosen)
	{
		isChosen = newChosen;
	}
	
	/** Sets the conflict property of the element.
	 * @param newConflict
	 */
	public void setConflict(boolean newConflict)
	{
		isConflict = newConflict;
	}
	
	/** Marks the element as highlighted or normal.
	 * @param newHighlighted
	 */
	public void setHighlighted(boolean newHighlighted)
	{
		isHighlighted = newHighlighted;
	}
	
	/** Marks the element as the start node or not.
	 * @param newStart
	 */
	public void setStart(boolean newStart)
	{
		isStart = newStart;
	}

	/**
	 * Since all <code>GraphElement</code>s are going to be displayed, each of
	 * them has a <code>Visual</code> component.
	 * 
	 * @return the <code>Visual</code> component for this <code>GraphElement</code>
	 */
	public Visual getVisual() {
		return visual;
	}
}