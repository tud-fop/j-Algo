package org.jalgo.main.gui.components;


import java.awt.event.ActionListener;
import java.net.URL;

import javax.help.*;

/**
 * 
 * @author Matthias Schmidt
 */
public class HelpSystem {


	/**
	 * Retrieves an JavaHelp spezific ActionListener which launches the JavaHelpDialog.
	 * @return an ActionListener 
	 */
	public ActionListener getJavaHelpActionListener(){
		//Find the HelpSet file and create the HelpSet object:
		String helpHS = "test.hs";
		ClassLoader cl = HelpSystem.class.getClassLoader();
		HelpSet hs;
		try {
			URL hsURL = HelpSet.findHelpSet(cl, helpHS);
			hs = new HelpSet(null, hsURL);
		} catch (Exception ee) {
			//	 Say what the exception really is
			System.out.println( "HelpSet " + ee.getMessage());
			System.out.println("HelpSet "+ helpHS +" not found");
			return null;
		}
	//Create a HelpBroker object:
	HelpBroker hb = hs.createHelpBroker();
	//Create a "Help" menu item to trigger the help viewer:
	return ((ActionListener) new CSH.DisplayHelpFromSource(hb));
	}	
	
	// TODO eine Methode schreiben, die die HelpSets der einzelnen Module dynamisch mergt - mgl. nur einmal!
	// TODO die einzelnen HelpSets für die Module erstellen
	// TODO ein einheitliches Format für die Hilfe erstellen
}
