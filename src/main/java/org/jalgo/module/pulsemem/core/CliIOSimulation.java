package org.jalgo.module.pulsemem.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class CliIOSimulation implements IOSimulation{

	public CliIOSimulation(){
		
	}
	public int input(int line) {
		System.out.print("erwarte Eingabe: ");
		InputStreamReader isr = new InputStreamReader ( System.in );
		BufferedReader br = new BufferedReader ( isr );
		String s = null;
		try {
		   while ( (s = br.readLine ()) != null ) {
			   if (s.split("\n").length>0) break;
		   }
		}
		catch ( IOException ioe ) {
		}
		System.out.print(" - OK\n");
		return (new Integer(s)).intValue();
	}

	public void output(String output, int line) {
		System.out.println("Ausgabe durch printf: "+output);
	}
	

}
