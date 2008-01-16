package org.jalgo.module.pulsemem.core;

/**
 * Interface to Simulate the <stdio.h> Functions printf and scanf
 * the methods should simulate the i/o stream
 *
 * @author Joachim Protze
 */
public interface IOSimulation {

	/**
	 * accepts linenumber of the scanf-statement
	 * returns input in form of integers
	 * @return input in form of integers
	 */
	public int input(int line);
	/**
	 * accepts output-String, linenumber of the printf-statement
	 * @param output-String
	 */
	public void output(String output, int line);

}
