package org.jalgo.module.c0h0.models.ast.tools;

import java.util.LinkedList;

import org.jalgo.module.c0h0.models.ast.ASTModel;

/**
 * Iterator through a syntax tree with breadth first search method.
 *
 */
public class BFSIterator extends Iterator {

	/**
	 * @param ast
	 *            the abstract syntax tree to iterate through
	 */
	public BFSIterator(ASTModel ast) {
		super(ast);
		if(prog != null)
			bfs(prog.getBlock());
		it = list.iterator();
	}

	/**
	 * @param ast
	 *            the abstract syntax tree to iterate through
	 * @param complete
	 *            boolean wether the program should be iterated completely with
	 *            declarations, scanfs and printfs or not
	 */
	public BFSIterator(ASTModel ast, boolean complete) {
		super(ast);
		if(prog != null)
			bfs(prog);
		it = list.iterator();
	}

	/**
	 * Chops AST using BFS and lines it up in an ArrayList
	 * 
	 * @param symbol 
	 * 				Iterable Symbol of the AST
	 */
	private void bfs(Iterable symbol) {
		if (symbol != null) {
			LinkedList<Iterable> queue = new LinkedList<Iterable>();
			queue.add(symbol);
			while (!queue.isEmpty()) {
				symbol = queue.pop();
				for (Iterable s : symbol.getSequence()) {
					list.add(s);
					queue.add(s);
				}
			}
		}
	}

}