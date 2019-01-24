package org.jalgo.module.c0h0.models.ast;

import java.util.ArrayList;

import org.jalgo.module.c0h0.models.ast.tools.Iterable;
import org.jalgo.module.c0h0.models.Visitable;

/**
 * Representing a While-statement in the AST
 *
 */
public class While extends Statement implements Visitable, Iterable {
	private Relation relation;
	private Block seq;

	/**
	 * @param r
	 * @param s
	 */
	public While(Relation r, Block s) {
		relation = r;
		seq = s;
	}

	/**
	 * Returns the Relation of the While-Statement
	 * 
	 * @return relation
	 * 			Relation of the While-Statement
	 */
	public Relation getRelation() {
		return relation;
	}

	/**
	 * Returns the Block of the While-Statement
	 * 
	 * @return block
	 * 			Block of the While-Statement
	 */
	public Block getBlock() {
		return seq;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.c0h0.models.ast.Statement#getSequence()
	 */
	public ArrayList<Iterable> getSequence() {
		ArrayList<Iterable> arr = new ArrayList<Iterable>();
		arr.add(seq);
		return arr;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.c0h0.models.ast.Symbol#accept(org.jalgo.module.c0h0.models.ast.ASTVisitor)
	 */
	public void accept(ASTVisitor visitor) {
		visitor.visitWhile(this);
	}
}
