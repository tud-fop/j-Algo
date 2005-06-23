/*
 * Created on 30.05.2005 10:39:37
 */
package org.jalgo.module.dijkstraModule.actions;

import org.jalgo.module.dijkstraModule.gui.Controller;
import org.jalgo.module.dijkstraModule.util.StatusbarText;

/**
 * @author Frank
 *
 */
public class ShowEditPageAction extends SetEditingModeAction {

    /**
     * @param ctrl
     * @throws Exception
     */
    public ShowEditPageAction(Controller ctrl) throws Exception {
        super(ctrl, Controller.MODE_NO_TOOL_ACTIVE,true);
    
    }
    
    public boolean doAction() throws Exception
    {
        super.doAction();
        getController().setStatusbarText(new StatusbarText("",null));
        getController().showEditingPage();
        return true;
    }

    /* (non-Javadoc)
     * @see org.jalgo.module.dijkstraModule.actions.Action#Undo()
     */
    public boolean undoAction() throws Exception 
    {
        super.undoAction();
        getController().showAlgorithmPage();
        return true;
    }
}
