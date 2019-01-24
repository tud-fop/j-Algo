package org.jalgo.module.c0h0.models.ast.tools;



import org.jalgo.module.c0h0.models.ast.ASTModel;
import org.jalgo.module.c0h0.models.ast.Program;
import org.jalgo.module.c0h0.models.ast.tools.Iterator;
import org.jalgo.module.c0h0.models.ast.tools.Iterable;

/**
 * Iterator through a syntax tree with depth first search method.
 * 
 * @author Peter Schwede
 * 
 */
public class DFSIterator extends Iterator {
	/**
	 * @param ast
	 *            the abstract syntax tree to iterate through
	 * @see DFSIterator
	 */
	public DFSIterator(ASTModel ast) {
		super(ast);
		if(prog != null)
			for(Iterable item : prog.getBlockStatements()) {
				dfs(item);
			}
		it = list.iterator();
	}

	/**
	 * @param ast
	 * @param complete
	 */
	public DFSIterator(ASTModel ast, boolean complete) {
		super(ast);
		Program prog = ast.getProgram();
		if (complete) {
			dfs(prog.getDecl());
			dfs(prog.getScanf());
		}
		dfs(prog.getBlock());
		if (complete)
			dfs(prog.getPrintf());
		it = list.iterator();
	}

	/**
	 * chop AST using DFS and line it up in an ArrayList
	 * @param symbol
	 */
	private void dfs(Iterable symbol) {
		if (symbol != null) {
			list.add(symbol);
			for (Iterable s : symbol.getSequence()) {
				dfs(s);
			}
		}
	}

}
