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

/**
 * @author Frank Staudinger
 * 
 * Created on 09.06.2005 16:30:44
 *
 */
package org.jalgo.module.dijkstraModule.util;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.widgets.Control;
import org.jalgo.module.dijkstraModule.gui.Controller;

/**
 * @author Frank Staudinger
 *

 */
public class AlgoModeButtonAniObserver implements Observer {

    protected Control m_control;
    /**
     * 
     */
    public AlgoModeButtonAniObserver(Control control) {
        super();
        m_control = control;
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable arg0, Object arg1) {
        Controller ctrl =((Controller)arg0);
        if(ctrl.getAnimationMillis() > 0 && ctrl.getEditingMode() == Controller.MODE_ALGORITHM)
            m_control.setEnabled(false);
        else
            m_control.setEnabled(true);

    }

}
