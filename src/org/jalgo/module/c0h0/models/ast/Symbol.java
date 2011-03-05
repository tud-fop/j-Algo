package org.jalgo.module.c0h0.models.ast;

import org.jalgo.module.c0h0.models.Visitable;

/**
 * Representing any symbol in the AST
 *
 */
public class Symbol implements Visitable {

	private String address;

	/**
	 * sets the address of this node
	 * 
	 * @param address
	 * 			
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * returns the String with the address of this node
	 * @return address
	 * 			String with the address of this node
	 */
	public String getAddress() {
		return address;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.c0h0.models.Visitable#accept(org.jalgo.module.c0h0.models.ast.ASTVisitor)
	 */
	public void accept(ASTVisitor visitor) {
		// This is a dummy. Dummies do basically nothing. Lazy dummies.
	}
}
