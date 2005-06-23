/*
 * Created on 31.05.2005 19:24:12
 *
  */
package org.jalgo.module.dijkstraModule.actions;

import org.jalgo.module.dijkstraModule.gui.Controller;

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
    public GotoMacroStepAction(Controller ctrl, boolean bNext) throws Exception
    {
        super(ctrl, 0);
  
        setGotoStepIndex(((bNext == true)? getController().getNextMacroStepIndex(): getController().getPrevMacroStepIndex()));
        registerAndDo(true);
    }

}
