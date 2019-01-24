package org.jalgo.module.pulsemem.core;

import java.util.ArrayList;
import java.util.List;


public class bufferedIOSimulation implements IOSimulation{

	private List<Integer> inputBuffer=new ArrayList<Integer>();
	private List<String> outputBuffer=new ArrayList<String>();
	
	/**
	 * List-buffered I/O-Simulation (FIFO) - intended for testing purpose
	 * @param inputBuffer
	 */
	public bufferedIOSimulation(List<Integer> inputBuffer){
		this.inputBuffer=inputBuffer;
	}
	public bufferedIOSimulation(){
	}
	
	public int input(int line) {
		return inputBuffer.remove(0);
	}

	public void output(String output, int line) {
		outputBuffer.add(output);
	}

	/**
	 * returns the inputbuffer
	 * @return inputbuffer
	 */
	public List<Integer> getInputBuffer() {
		return inputBuffer;
	}

	/**
	 * set the inputbuffer
	 * @param inputBuffer
	 */
	public void setInputBuffer(List<Integer> inputBuffer) {
		this.inputBuffer = inputBuffer;
	}
	
	/**
	 * fill the inputbuffer
	 * @param inputBuffer
	 */
	public void addInputBuffer(Integer inputBuffer) {
		this.inputBuffer.add(inputBuffer);
	}

	/**
	 * returns the bufferd output
	 * @return the bufferd output
	 */
	public List<String> getOutputBuffer() {
		return outputBuffer;
	}
	
	/**
	 * delete all entries on output
	 *
	 */
	public void clearOutput(){
		outputBuffer=new ArrayList<String>();
	}
}
