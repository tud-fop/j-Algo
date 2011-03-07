/**
 * AM1 Simulator - simulating am1 code in an abstract machine based on the
 * definitions of the lectures 'Programmierung' at TU Dresden.
 * Copyright (C) 2010 Max Leuthäuser
 * Contact: s7060241@mail.zih.tu-dresden.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jalgo.module.am1simulator.view;

import java.awt.Font;
import java.awt.Frame;

import org.jalgo.main.gui.JAlgoWindow;

/**
 * All constants for GUI elements are defined here.
 * 
 * @author Max Leuth&auml;user
 */
public final class GuiUtilities {
	/** The presentation font */
	public static final Font PRESENTATIONFONT = new Font("Courier", Font.BOLD,
			18);
	/** The standard font */
	public static final Font STANDARDFONT = new Font("Courier", Font.PLAIN, 12);

	public static final Font STANDARDEDITORFONT = new Font("Monospaced", Font.PLAIN, 12);
	public static final Font PRESENTATIONEDITORFONT = new Font("Monospaced", Font.PLAIN, 18);
	/**
	 * This method returns the {@link JAlgoWindow} or throw a
	 * {@link RuntimeException} if it was unable to find it.
	 * 
	 * @return the {@link JAlgoWindow}
	 */
	public static JAlgoWindow getJAlgoWindow() {
		for (Frame f : Frame.getFrames())
			if (f instanceof JAlgoWindow)
				return (JAlgoWindow) f;

		throw new RuntimeException("JAlgoWindow not found"); //$NON-NLS-1$
	}
}
