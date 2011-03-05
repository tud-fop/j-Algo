package org.jalgo.module.c0h0.models.ast;

/**
 * visitor interface for the AST visitors
 *
 */
public interface ASTVisitor {
	/**
	 * visit assignment
	 * @param assignment
	 */
	public void visitAssignment(Assignment assignment);

	/**
	 * visit block
	 * @param block
	 */
	public void visitBlock(Block block);

	/**
	 * visit while
	 * @param whileStatement
	 */
	public void visitWhile(While whileStatement);

	/**
	 * visit if
	 * @param ifStatement
	 */
	public void visitIf(If ifStatement);
}
