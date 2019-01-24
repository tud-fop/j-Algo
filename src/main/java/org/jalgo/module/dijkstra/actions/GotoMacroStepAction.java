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
 * Created on 31.05.2005 19:24:12
 *
 */
package org.jalgo.module.dijkstra.actions;

import org.jalgo.module.dijkstra.gui.Controller;

/**
 *  This class moves to the naxt/previous macro step in the Dijkstra
 * @author Frank Staudinger
 *
 */
public class GotoMacroStepAction extends GotoStepAction {

	/**
	 * @param ctrl
	 * @param bNext true if you want to go forward
	 */
	public GotoMacroStepAction(Controller ctrl, boolean bNext) throws ActionException {
		super(ctrl, 0);

		setGotoStepIndex(((bNext == true) ? getController().getNextMacroStepIndex() : getController()
				.getPrevMacroStepIndex()));
		registerAndDo(true);
	}

}
