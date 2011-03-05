package org.jalgo.module.c0h0.models.ast.tools;

import java.util.ArrayList;

import org.jalgo.module.c0h0.models.ast.ASTModel;
import org.jalgo.module.c0h0.models.ast.Program;
import org.jalgo.module.c0h0.models.ast.Symbol;
import org.jalgo.module.c0h0.models.ast.tools.Iterable;

/**
 * the abstract iterator for the depth/breadth first iterator
 *
 */
public abstract class Iterator implements java.util.Iterator<Symbol> {
	ArrayList<Iterable> list;
	java.util.Iterator<Iterable> it;
	Program prog;

	/**
	 * @param ast
	 *            the abstract syntax tree to iterate through
	 */
	public Iterator(ASTModel ast) {
		list = new ArrayList<Iterable>();
		prog = ast.getProgram();
	}

	public boolean hasNext() {
		return it.hasNext();
	}

	public Symbol next() {
		return (Symbol) it.next();
	}

	public void remove() {
		it.remove();
	}

}
