package org.jalgo.module.hoare.constants;

/**
 * Representation of the Status of a verification node<br>
 * UNCHANGED  : shows that a verification node has not
 *              been checked after the node has changed<br>
 * WAITING    : shows that a verification node waits
 *              for the result of an evaluation             
 * RESULTWRONG: marks the verification node as tested
 *              but not correct<br>
 * RESULTOK   : marks the verification node as tested
 *              and correct<br>
 * 
 * 
 * @author Uwe
 *
 */
public enum Status {

	UNCHANGED,
	WAITING,
	RESULTWRONG,
	RESULTOK
	
}
