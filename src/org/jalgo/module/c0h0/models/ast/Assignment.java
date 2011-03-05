package org.jalgo.module.c0h0.models.ast;

/**
 * Representing an Assignment in the AST
 *
 */
public class Assignment extends Statement {
	private Var var;
	private Term term;

	/**
	 * @param var
	 * @param term
	 */
	public Assignment(Var var, Term term) {
		this.var = var;
		this.term = term;
	}

	/**
	 * Returns the Var of the Assignment
	 * 
	 * @return var
	 * 			Var of the Assignment
	 */
	public Var getVar() {
		return var;
	}

	/**
	 * Returns the Term of the Assignment
	 * 
	 * @return term
	 * 			Term of the Assignment
	 */
	public Term getTerm() {
		return term;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.c0h0.models.ast.Symbol#accept(org.jalgo.module.c0h0.models.ast.ASTVisitor)
	 */
	public void accept(ASTVisitor visitor) {
		visitor.visitAssignment(this);
	}
}
