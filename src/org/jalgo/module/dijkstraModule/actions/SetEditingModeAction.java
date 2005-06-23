/*
 * Created on 30.05.2005 18:26:35
 *
  */
package org.jalgo.module.dijkstraModule.actions;

import org.jalgo.module.dijkstraModule.gui.Controller;

/**
 * @author Frank
 *
  */
public class SetEditingModeAction extends Action {

    protected int m_iMode;
    protected int m_iOldMode;
    /**
     * @return Returns the m_iMode.
     */
    protected int getMode() {
        return m_iMode;
    }
    /**
     * @param mode The m_iMode to set.
     */
    protected void setMode(int mode) {
        m_iMode = mode;
    }
 
    /**
     * @param ctrl	Controller for this action
     * @param iMode new Editing mode
     * @throws Exception
     */
    public SetEditingModeAction(Controller ctrl, int iMode)throws Exception
    {
        this(ctrl,iMode,false);
    }

    public SetEditingModeAction(Controller ctrl, int iMode, boolean bRegister)throws Exception
    {
        super(ctrl);
        m_iMode = iMode;
        this.registerAndDo(bRegister);
    }

    
    /* (non-Javadoc)
     * @see org.jalgo.module.dijkstraModule.actions.Action#doAction()
     */
    public boolean doAction() throws Exception {
        m_iOldMode = getController().getEditingMode();        
        getController().setEditingMode(m_iMode);
        getController().getGraph().setAllChangedFlagsFalse(); // HS
        return true;
    }

    /* (non-Javadoc)
     * @see org.jalgo.module.dijkstraModule.actions.Action#undoAction()
     */
    public boolean undoAction() throws Exception {
        getController().setEditingMode(m_iOldMode);
        return true;
    }

}
