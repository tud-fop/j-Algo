/*
 * Created on 19.04.2004
 */
 
package org.jalgo.main.util;

/**
 * A Timer especially used for animations.
 * 
 * @author Hauke Menges
 */
public class Timer {
	private long msStart, msEnd;

	/**
	 * Initializes the timer.
	 * 
	 * @param msOffset	Offset in milliseconds
	 * @param msLength	Length/duration in milliseconds
	 */
	public Timer(int msOffset, int msLength) {
	}

	/**
	 * Calculates the new position.
	 * 
	 * @return	The new position (from 0.0 to 1.0)
	 */
	public double getPosition() {
		return 0.0;
	}
	
}
