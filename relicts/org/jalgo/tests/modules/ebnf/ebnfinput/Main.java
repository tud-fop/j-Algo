/*
 * Main.java
 *
 * Created on 13. April 2006, 20:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */



package org.jalgo.tests.ebnf.ebnfinput;

import javax.swing.*;

/**
 *
 * @author jm
 */
public class Main {
    
    /** Creates a new instance of Main */
    public Main() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        GUI gui = new GUI();
        try {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        SwingUtilities.updateComponentTreeUI(gui);
        } catch (Exception e){}
        gui.setVisible(true);
    }
    
}
