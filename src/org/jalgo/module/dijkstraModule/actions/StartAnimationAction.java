/**
 * @author Frank Staudinger
 * 
 * Created on 09.06.2005 00:17:53
 *
 */
package org.jalgo.module.dijkstraModule.actions;

import org.jalgo.module.dijkstraModule.gui.Controller;

public class StartAnimationAction extends Action {

    protected int m_iMilliseconds;
 
    public StartAnimationAction(Controller ctrl,int iMilliseconds) throws Exception {
        super(ctrl);
        m_iMilliseconds = iMilliseconds;
        super.registerAndDo(true);
    }

    /* (non-Javadoc)
     * @see org.jalgo.module.dijkstraModule.actions.Action#doAction()
     */
    public boolean doAction() throws Exception
    {
        getController().startAnimation(m_iMilliseconds);
        return false;
    }

    /* (non-Javadoc)
     * @see org.jalgo.module.dijkstraModule.actions.Action#undoAction()
     */
    public boolean undoAction() throws Exception {
        getController().stopAnimation();
        return false;
    }

}
