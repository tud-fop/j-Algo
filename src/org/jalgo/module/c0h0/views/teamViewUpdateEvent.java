package org.jalgo.module.c0h0.views;

import java.util.EventObject;

/**
 * to update an View from another View a teamViewUpdateEvent is sent from source to sink
 * 
 * @author mathias.kaufmann
 */
public class teamViewUpdateEvent extends EventObject {

	private static final long serialVersionUID = 8518681423550314908L;

	private String Address;
	
	/**
	 * @param source
	 * @param Address
	 */
	public teamViewUpdateEvent(Object source,String Address) {
		super(source);
		this.Address = Address;
	}
	/**
	 * returns the address of the corresponding Symbol which triggered the update
	 * 
	 * @return the address
	 */
	public String getAddress() {
		return this.Address;
	}
}
