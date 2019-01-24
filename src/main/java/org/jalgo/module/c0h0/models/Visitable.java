package org.jalgo.module.c0h0.models;

import org.jalgo.module.c0h0.models.ast.ASTVisitor;

/**
 * visitor interface for visitible classes
 *
 */
public interface Visitable {
	/**
	 * accepts the visitor
	 * 
	 * @param visitor
	 */
	public void accept(ASTVisitor visitor);
}
