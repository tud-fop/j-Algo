package org.jalgo.module.c0h0.models.ast;

import java.util.ArrayList;

import org.jalgo.module.c0h0.models.Visitable;
import org.jalgo.module.c0h0.models.ast.tools.Iterable;

/**
 * Representing a Block in the AST
 * 
 */
public class Block extends Symbol implements Visitable, Iterable {
	private ArrayList<Statement> stats = new ArrayList<Statement>();
	private boolean isBlock;

	/**
	 * @param isBlock
	 */
	public Block(boolean isBlock) {
		this.isBlock = isBlock;
	}

	/**
	 * @param s
	 * @param isBlock
	 */
	public Block(Statement s, boolean isBlock) {
		stats.add(s);
		this.isBlock = isBlock;
	}

	/**
	 * adds a statement
	 * 
	 * @param statement
	 *            Statement which is to be added to the Declaration
	 */
	public void addStatement(Statement statement) {
		stats.add(statement);
	}

	/**
	 * If index is invalid, null is being returned
	 * 
	 * @param index
	 * @return the statement
	 */
	public Statement getStatement(int index) {
		if (index < 0 || index >= stats.size())
			return null;
		return stats.get(index);
	}

	/**
	 * returns last statement
	 * 
	 * @return the last statement
	 */
	public Statement getLastStatement() {
		if (stats.size() > 0)
			return stats.get(stats.size() - 1);
		else
			return null;
	}

	/**
	 * returns the statement list
	 * 
	 * @return the statement list
	 */
	public ArrayList<Statement> getStatementList() {
		return stats;
	}

	/**
	 * checks if Block is a Block with Brackets
	 * 
	 * @return isBlock boolean if Block is a Block with Brackets
	 */
	public boolean isBlock() {
		return isBlock;
	}

	/**
	 * checks if Block is not a Block with Brackets
	 * 
	 * @return !isBlock boolean if Block is not a Block with Brackets
	 */
	public boolean isALie() {
		return !isBlock;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jalgo.module.c0h0.models.ast.Symbol#accept(org.jalgo.module.c0h0.
	 * models.ast.ASTVisitor)
	 */
	public void accept(ASTVisitor visitor) {
		visitor.visitBlock(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.module.c0h0.models.ast.tools.Iterable#getSequence()
	 */
	public ArrayList<Iterable> getSequence() {
		return new ArrayList<Iterable>(stats);
	}
}
