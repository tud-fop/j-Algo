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
