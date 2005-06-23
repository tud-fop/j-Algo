/*
 * Created on 07.06.2005
 * $ID$
 */
package org.jalgo.module.dijkstraModule.gui;

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
