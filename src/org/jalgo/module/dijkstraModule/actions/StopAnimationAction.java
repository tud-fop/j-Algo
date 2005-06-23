/**
 * @author Frank Staudinger
 * 
 * Created on 09.06.2005 09:55:38
 *
 */
package org.jalgo.module.dijkstraModule.actions;

import org.jalgo.module.dijkstraModule.gui.Controller;

/**
 * @author Frank Staudinger
 *
 
 */
public class StopAnimationAction extends Action {

    protected int m_iMilliseconds;
   
    public StopAnimationAction(Controller ctrl)throws Exception {
        super(ctrl);
        this.m_iMilliseconds = ctrl.getAnimationMillis();
        super.registerAndDo(true);
    }

    /* (non-Javadoc)
     * @see org.jalgo.module.dijkstraModule.actions.Action#doAction()
     */
    public boolean doAction() throws Exception {
        getController().stopAnimation();
        return true;
    }

    /* (non-Javadoc)
     * @see org.jalgo.module.dijkstraModule.actions.Action#undoAction()
     */
    public boolean undoAction() throws Exception {
        getController().startAnimation(this.m_iMilliseconds);
        return true;
    }

}
