/**
 * @author Frank Staudinger
 * 
 * Created on 13.06.2005 17:59:21
 *
 */
package org.jalgo.module.startup.gui;

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.jalgo.main.IModuleInfo;
import org.jalgo.main.Jalgo;
import org.jalgo.main.JalgoMain;
/**
 * @author Frank Staudinger
 * 
 * The default startup screen that lets the user choose between the different modules.
 * 
 */
public class StartupComposite extends Composite {

    class _ButtonSelectionAdapter extends SelectionAdapter
    {
        int m_nIndex;
        _ButtonSelectionAdapter(int nIndex)
        {
            m_nIndex = nIndex;
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
         */
        public void widgetSelected(SelectionEvent arg0) {
            try
            {
	            JalgoMain main = Jalgo.getJalgoMain();
	            main.newInstance(m_nIndex);
            }
            catch(Exception e)
            {
                
            }
            
            super.widgetSelected(arg0);
        }
    }
    /**
     * @param arg0
     * @param arg1
     */
    public StartupComposite(Composite arg0, int arg1) {
        super(arg0, arg1);
        
      	GridLayout l = new GridLayout();
    	l.marginHeight = 5;
    	l.marginWidth = 5;
    	l.numColumns = 1;
    	l.makeColumnsEqualWidth = true;
    	
    	this.setLayout(l);
 
		this.setBackground(new Color(this.getDisplay(),255,255,255));
		JalgoMain main = Jalgo.getJalgoMain();
		Iterator iter = main.getKnownModuleInfos().iterator();
		if (iter.hasNext()) iter.next(); // HS -- because you shouldn't be able to create a second startup tab
		while(iter.hasNext())
		{
		    IModuleInfo info = (IModuleInfo) iter.next();
		    Button b = new Button(this,SWT.CENTER);
		    b.setText(info.getName());
		    b.setLayoutData(new GridData(GridData.CENTER));
		    b.addSelectionListener(new _ButtonSelectionAdapter(main.getKnownModuleInfos().indexOf(info))); // HS
		}
		// This magic incantation is required so that the all Composites get
		// the right size from the start.
		this.pack();
		this.setSize(this.getSize());
    }

}
