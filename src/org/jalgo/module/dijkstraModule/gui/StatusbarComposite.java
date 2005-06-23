/**
 * @author Frank Staudinger
 * 
 * The StatusbarComposite provides the Dijkstra module's statusbar.
 *
 
 */
package org.jalgo.module.dijkstraModule.gui;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.jalgo.module.dijkstraModule.actions.GotoStepAction;
import org.jalgo.module.dijkstraModule.actions.ShowAlgorithmPageAction;
import org.jalgo.module.dijkstraModule.actions.ShowEditPageAction;
import org.jalgo.module.dijkstraModule.util.AlgoModeButtonAniObserver;
import org.jalgo.module.dijkstraModule.util.BeamerFontFactory;
import org.jalgo.module.dijkstraModule.util.DefaultExceptionHandler;
import org.jalgo.module.dijkstraModule.util.StatusbarText;

public class StatusbarComposite extends ControllerComposite {

    protected class StatusbarObserver implements Observer
    {
        protected StyledText m_cmpStatusbar;
        public StatusbarObserver(StyledText cmp)
        {
            this.m_cmpStatusbar = cmp;
        }
        
         /* (non-Javadoc)
         * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
         */
        public void update(Observable arg0, Object arg1)
        {
  
           try
           {
               Controller ctrl = (Controller)arg1;
               StatusbarText txt = ctrl.getStatusbarText();
               txt.setText(this.m_cmpStatusbar);
               
           }
           catch(Exception e)
           {
               
           }
        }
}
 

    protected class AlgorithmButtonSelectionAdapter extends SelectionAdapter
    {
        private Controller m_Ctrl;
        boolean m_bEditMode;
        public AlgorithmButtonSelectionAdapter(Controller ctrl, boolean bEditMode)
        {
            m_Ctrl = ctrl;
            m_bEditMode = bEditMode;
        }
        
		public void widgetSelected(SelectionEvent e)
		{
		    try
		    {
		        if(m_bEditMode == true)
		            new ShowAlgorithmPageAction(m_Ctrl);
		        else
		            new ShowEditPageAction(m_Ctrl);
		            
		    }
		    catch(Exception exc)
		    {	
		        new DefaultExceptionHandler(exc);

		    }
		}
  
    }
    protected class AlgorithmRestartButtonSelectionAdapter extends SelectionAdapter
    {
        private Controller m_Ctrl;
        public AlgorithmRestartButtonSelectionAdapter(Controller ctrl)
        {
            m_Ctrl = ctrl;
            
        }
        
		public void widgetSelected(SelectionEvent e)
		{
		    try
		    {
		       new GotoStepAction(m_Ctrl,0, true);
		            
		    }
		    catch(Exception exc)
		    {	
		        new DefaultExceptionHandler(exc);

		    }
		}
  
    }    


    boolean m_bEditMode;
	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstraModule.gui.ControllerComposite
	 */
    public StatusbarComposite(Controller ctrl, Composite cmpParent, int nStyle, boolean bEditMode)
    {      
        super(ctrl, cmpParent, nStyle);
        
        m_bEditMode = bEditMode;
        
		GridLayout l = new GridLayout();
    	l.verticalSpacing = 5;
    	l.marginHeight = 5;
    	l.marginWidth = 5;
    	l.numColumns = 2;
    	if(m_bEditMode == false)
    		l.numColumns = 3;
    	l.makeColumnsEqualWidth = false;
    	
		super.setLayout(l);
		StyledText cmp = new StyledText(this,SWT.WRAP|SWT.MULTI);


		GridData g = new GridData(GridData.FILL_BOTH);
		cmp.setLayoutData(g);
		cmp.setEnabled(true);
		cmp.setBackground(this.getBackground());
		
		Button b = new Button(this, SWT.CENTER);
		if(m_bEditMode == true)
		{
		    b.setText("Algorithmus starten");
			b.addSelectionListener(new AlgorithmButtonSelectionAdapter(getController(),m_bEditMode));
			b.setLayoutData(new GridData(GridData.END|GridData.VERTICAL_ALIGN_CENTER));				    
		}
		else
		{
		    b.setText("Graph bearbeiten");
			b.addSelectionListener(new AlgorithmButtonSelectionAdapter(getController(),m_bEditMode));
			b.setLayoutData(new GridData(GridData.END|GridData.VERTICAL_ALIGN_BEGINNING));
			
			getController().addObserver(new AlgoModeButtonAniObserver(b));
			
			b = new Button(this,SWT.CENTER);
			b.setText("Algorithmus neu starten");
			b.addSelectionListener(new AlgorithmRestartButtonSelectionAdapter(getController()));
			b.setLayoutData(new GridData(GridData.END|GridData.VERTICAL_ALIGN_BEGINNING));
			
			cmp.setFont(BeamerFontFactory.getFont());
			getController().addObserver(new AlgoModeButtonAniObserver(b));
		}
		getController().registerStatusbarObserver(new StatusbarObserver(cmp));	
		

	

    }

}
