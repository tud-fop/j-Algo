/*
 * Created on 23.05.2005
 *
  */
package org.jalgo.module.dijkstraModule.actions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.jalgo.module.dijkstraModule.gui.Controller;
import org.jalgo.module.dijkstraModule.model.Graph;

/**
 * @author Frank
 *
 * Klasse zum Speichern/Laden eines Graphen im/aus dem Dateisystem
 * 
 */
public class FileAction extends GraphAction {

	
	protected boolean m_bSave;
	protected Graph m_newGraph;

	public FileAction(Controller ctrl,boolean bSave) throws Exception 
	{
		super(ctrl);
		m_bSave =bSave;
		this.registerAndDo(m_bSave == false);
	}

	public boolean doAction() throws Exception
	{
		if(m_bSave == true)
		{
			save(getController().getGraph());
			return false;
		}
		else
		{
			if(open())
				getController().setGraph(m_newGraph);
			return true;				
		}
	}

	public boolean undoAction() throws Exception
	{
		if(m_bSave == false)
			getController().setGraph(m_oldGraph);
		return (m_bSave == false);
	}
	/*
	 * Author: Steven Voigt
	 * 
	 * Speichert Graph im FS 
	 * 
	 * */
	protected boolean save(Graph g) throws Exception
	{
		Display disp = Display.getCurrent();
		Shell shell = new Shell(disp);
	
		FileDialog dialog = new FileDialog(shell,SWT.SAVE);

		final String[] FILTER = {"Graph Objekt (*.graph)","Alle Dateien (*.*)"};
		
		dialog.setFilterNames(FILTER);
		dialog.setFileName("*.gra");
		String file = dialog.open();
		if (file != null) 
		{
			ObjectOutputStream out = null;
			try
			{
				out = new ObjectOutputStream(new FileOutputStream(file));
				out.writeObject(g);
				out.flush();
				return true;
			}
			catch (IOException ioe)
			{
				throw ioe;
			}
			finally 
			{
				if (out != null)
				{
					try 
					{
						out.close();
					}
					catch (IOException ioex)
					{
						throw ioex;
					}
				}
			}			
		}
		return false;
	}
	/*
	 * Author: Steven Voigt
	 * 
	 * L�dt Graph aus Datei im FS 
	 * 
	 * */	
	protected boolean open() throws Exception
	{
		Display disp = Display.getCurrent();
		Shell shell = new Shell(disp);	
		FileDialog dialog = new FileDialog(shell,SWT.OPEN);
		final String[] FILTER = {"Graph Objekt (*.graph)","Alle Dateien (*.*)"};
		
		dialog.setFilterNames(FILTER);

		dialog.setFileName("*.gra");
			//dialog.setText("(Einkaufs)Laden");
			  final String[] Ext = {
		       "*.gra"};
		dialog.setFilterNames(Ext);	
			
		String file = dialog.open();
		m_newGraph = null;
		if (file != null)
		{
			ObjectInputStream in = null;
			try {
				in = new ObjectInputStream(new FileInputStream(file));
				m_newGraph = (Graph) in.readObject();
				return true;
			} catch (Exception ioe)
			{
				
				throw ioe;
			
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException ioex) {
						ioex.printStackTrace();
					}				}
			}
			
		}
		return false;
	}
}
