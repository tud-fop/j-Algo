package org.jalgo.module.c0h0.models.ast;

import java.util.ArrayList;

import org.jalgo.module.c0h0.models.ast.tools.Iterable;

/**
 * Contains all statements of the original code
 *
 */
public class Program extends Symbol implements Iterable {
	private Declaration decl;
	private ScanfSequence scanf;
	private Block statements;
	private PrintfStatement printf;

	/**
	 * @param decl
	 * @param scanf
	 * @param statements
	 * @param printf
	 */
	public Program(final Declaration decl, final ScanfSequence scanf,
			final Block statements, final PrintfStatement printf) {
		this.decl = decl;
		this.scanf = scanf;
		this.statements = statements;
		this.printf = printf;
	}

	/**
	 * returns Declaration of all Vars
	 * 
	 * @return declaration
	 * 				Declaration of all Vars
	 */
	public Declaration getDecl() {
		return decl;
	}

	/**
	 * retruns ScanfSequence of all Scanfs
	 * 
	 * @return scanfsequence
	 * 				ScanfSequence of all Scanfs
	 */
	public ScanfSequence getScanf() {
		return scanf;
	}

	/**
	 * returns Block with all statements
	 * 
	 * @return block
	 * 				Block with all statements
	 */
	public Block getBlock() {
		return statements;
	}
	
	/**
	 * returns ArrayList<Iterable> of all statements
	 * 
	 * @return sequence
	 * 				ArrayList<Iterable> of all statements
	 */
	public ArrayList<Iterable> getBlockStatements() {
		return statements.getSequence();
	}

	/**
	 * returns PrintfStatement with the Printf
	 * 
	 * @return printfstatement
	 * 				PrintfStatement with the Printf
	 */
	public PrintfStatement getPrintf() {
		return printf;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.c0h0.models.ast.tools.Iterable#getSequence()
	 */
	public ArrayList<Iterable> getSequence() {
		ArrayList<Iterable> arr = new ArrayList<Iterable>();
		arr.add(decl);
		arr.add(scanf);
		arr.add(statements);
		arr.add(printf);
		return arr;
	}
}