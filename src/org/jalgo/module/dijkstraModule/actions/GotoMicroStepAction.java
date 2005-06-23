/*
 * Created on 31.05.2005 19:30:10
 *
  */
package org.jalgo.module.dijkstraModule.actions;

import org.jalgo.module.dijkstraModule.gui.Controller;

/**
 * @author Frank Staudinger
 *
 */
public class GotoMicroStepAction extends GotoStepAction {

    /**
     * @param ctrl
     * @param bNext true if you want to go forward
     */
    public GotoMicroStepAction(Controller ctrl, boolean bNext) throws Exception
    {
        super(ctrl, 0);
     
        setGotoStepIndex(((bNext == true)? getController().getNextStepIndex(): getController().getPrevStepIndex()));
        registerAndDo(true);
    }
}
