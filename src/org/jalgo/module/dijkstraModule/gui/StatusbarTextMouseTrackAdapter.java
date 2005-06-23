
/**
 * @author Frank
 * Created on 26.05.2005 18:30:24
 * MouseTrackAdapter to set the statusbar's text.
 * 
 */
package org.jalgo.module.dijkstraModule.gui;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.jalgo.module.dijkstraModule.util.*;

public class StatusbarTextMouseTrackAdapter extends MouseTrackAdapter {

    protected Controller m_ctrl;
    protected String m_strText;
    
    public StatusbarTextMouseTrackAdapter(Controller ctrl, String strText)
    {
        m_ctrl = ctrl;
        m_strText = strText;
    };
    
    /* (non-Javadoc)
     * @see org.eclipse.swt.events.MouseTrackListener#mouseEnter(org.eclipse.swt.events.MouseEvent)
     */
    public void mouseEnter(MouseEvent arg0)
    {   	
       m_ctrl.setStatusbarText(new StatusbarText(this.m_strText,null));
       super.mouseEnter(arg0);
    }
    /* (non-Javadoc)
     * @see org.eclipse.swt.events.MouseTrackListener#mouseExit(org.eclipse.swt.events.MouseEvent)
     */
    public void mouseExit(MouseEvent arg0)
    {
        m_ctrl.setStatusbarText( new StatusbarText("",null) );
        super.mouseExit(arg0);
    }
}
