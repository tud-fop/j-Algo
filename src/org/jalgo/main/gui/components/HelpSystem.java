/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and platform
 * independant. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

/*
 * Created on Feb 15, 2006
 */
package org.jalgo.main.gui.components;


import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;

import javax.help.*;

import org.jalgo.main.IModuleInfo;
import org.jalgo.main.JAlgoMain;
import org.jalgo.main.util.Messages;

/**
 * 
 * @author Matthias Schmidt
 */
public class HelpSystem {
	
	private LinkedList<IModuleInfo> knownModuleInfos; 

	
	public HelpSystem(){
		knownModuleInfos = JAlgoMain.getInstance().getKnownModuleInfos();
	}
	
	
	/**
	 * Retrieves an JavaHelp specific ActionListener which launches the JavaHelpDialog.
	 * @return an ActionListener 
	 */
	public ActionListener getJavaHelpActionListener(){
		
		//Initialise the HelpSet with the help data from JAlgoMain
		HelpSet mainHS;		
		mainHS = getHSByURL(Messages.getResourceURL("main","Main_HelpSet"));
			
		//Merge the Module HelpSets
		IModuleInfo element;
		URL path = null;
		for (Iterator<IModuleInfo> it = knownModuleInfos.iterator(); it.hasNext();) {
			element = it.next();
			path = element.getHelpSetURL();
			if (path == null)
				System.out.println("HelpSetURL from "+element.getName()+" is null!");
			else mainHS.add(getHSByURL(path));	
		}	
		
		//Create a HelpBroker object
		HelpBroker hb = mainHS.createHelpBroker();
		
		return (ActionListener) new CSH.DisplayHelpFromSource(hb);
	}	
	
	private HelpSet getHSByURL(URL path){
		HelpSet hs;
		try {
			hs = new HelpSet(null, path);	
		} catch (Exception ee) {
			//Say what the exception really is
			System.err.println("HelpSet " + ee.getMessage());
			return null;
		}
		return hs;
	}
	
	
	// TODO Mergen statisch:
	//  d.h. HelpSystem testet, ob sich die Zusammensetzung der Module verändert hat.
	//  	--> Wenn ja: Verändern der Datei, die die HelpSet Liste der Module hält und
	//					 Aktualisieren der Modulliste (gespeichert in Properties)
	// TODO den ModulChooseDialog entwerfen
}


