/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
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
 * Created on 07.06.2005
 * $ID$
 */
package org.jalgo.module.dijkstra.gui;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;

/** As SWT denies us the possibility to create color objects without a
 * device, we need this frigging class.
 * 
 * @author Julian Stecklina
 *
 */
public class ColorFactory {
    protected Device device;
    
    /** Creates a new Color object.
     * @param r red
     * @param g green
     * @param b blue
     * @return a Color object
     */
    public Color makeColor(int r, int g, int b) {
        return new Color(device,r,g,b);
    }
    
    /** Creates a ColorFactory for the given device. 
     * @param device the device where we want to draw with these colours.
     */
    public ColorFactory(Device device) {
        this.device = device;
    }
}
