package org.jalgo.module.c0h0.models.ast;

import java.util.ArrayList;

import org.jalgo.module.c0h0.models.Visitable;
import org.jalgo.module.c0h0.models.ast.tools.Iterable;

/**
 * Representing an If-statement in the AST
 *
 */
public class If extends Statement implements Visitable {
	private Relation relation;
	private Block ifBlock;
	private Block elseBlock = new Block(false);

	/**
	 * @param r
	 * @param s
	 */
	public If(Relation r, Block s) {
		relation = r;
		ifBlock = s;
	}

	/**
	 * @param das
	 * @param ist
	 * @param hahahaah
	 */
	public If(Relation das, Block ist, Block hahahaah) {
		relation = das;
		ifBlock = ist;
		elseBlock = hahahaah;// !
	}

	/**
	 * Returns the Relation of the If-Statement
	 * 
	 * @return relation
	 * 			Relation of the If-Statement
	 */
	public Relation getRelation() {
		return relation;
	}

	/**
	 * Returns the if-Block of the If-Statement
	 * 
	 * @return block
	 * 			Block of the If-Statement
	 */
	public Block getIfSequence() {
		return ifBlock;
	}

	/**
	 * Returns the else-Block of the If-Statement
	 * 
	 * @return block
	 * 			Block of the if-Statement
	 */
	public Block getElseSequence() {
		return elseBlock;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.c0h0.models.ast.Symbol#accept(org.jalgo.module.c0h0.models.ast.ASTVisitor)
	 */
	public void accept(ASTVisitor visitor) {
		visitor.visitIf(this);
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.c0h0.models.ast.Statement#getSequence()
	 */
	public ArrayList<Iterable> getSequence() {
		ArrayList<Iterable> res = new ArrayList<Iterable>();
		res.add(getIfSequence());
		res.add(getElseSequence());
		return res;
	}
}
