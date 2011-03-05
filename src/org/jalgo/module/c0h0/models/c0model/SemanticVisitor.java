package org.jalgo.module.c0h0.models.c0model;

import java.util.ArrayList;

import org.jalgo.module.c0h0.models.ast.ASTVisitor;
import org.jalgo.module.c0h0.models.ast.Assignment;
import org.jalgo.module.c0h0.models.ast.Block;
import org.jalgo.module.c0h0.models.ast.If;
import org.jalgo.module.c0h0.models.ast.Var;
import org.jalgo.module.c0h0.models.ast.While;

/**
 * Checks AST for semantic Failures. This is to be used with DFSIterator.
 * 
 */
public class SemanticVisitor implements ASTVisitor {
	private boolean valid;
	private ArrayList<Integer> availVars;
	private String error;
	private Var printfVar;
	private ArrayList<Integer> notInitializedVars;

	/**
	 * Creates a new Instance of the SemanticVisitor and already checks for
	 * undefined Vars
	 * 
	 * @param availVars
	 * @param scanfVars
	 * @param printfVar
	 */
	@SuppressWarnings("unchecked")
	public SemanticVisitor(ArrayList<Var> availVars, ArrayList<Var> scanfVars,
			Var printfVar) {
		valid = true;
		this.availVars = getIndexOfVarList(availVars);
		notInitializedVars = (ArrayList<Integer>) this.availVars.clone();
		error = "";
		this.printfVar = printfVar;

		// Treten undefinierte Variablen auf?
		for (int i : getIndexOfVarList(scanfVars)) {
			if (!this.availVars.contains(i)) {
				valid = false;
				error += "x" + i + " nicht deklariert(Scanf)<br>";
				this.availVars.add(i);
			}
			notInitializedVars.remove((Object) i);
		}
		if (!this.availVars.contains(printfVar.getIndex())) {
			valid = false;
			error += printfVar + " nicht deklariert(Printf)<br>";
			this.availVars.add(printfVar.getIndex());
		}
	}

	/**
	 * @param varList
	 *            ArrayList which contains Vars
	 * @return vars ArrayList which contains the Index of the given Vars
	 */
	private ArrayList<Integer> getIndexOfVarList(ArrayList<Var> varList) {
		ArrayList<Integer> vars = new ArrayList<Integer>();
		for (Var v : varList) {
			vars.add(v.getIndex());
		}
		return vars;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jalgo.module.c0h0.models.ast.ASTVisitor#visitAssignment(org.jalgo
	 * .module.c0h0.models.ast.Assignment)
	 */
	public void visitAssignment(Assignment assignment) {
		if (!availVars.contains(assignment.getVar().getIndex())) {
			valid = false;
			error += assignment.getVar() + " nicht deklariert<br>";
		}
		for (int i : getIndexOfVarList(assignment.getTerm().getVars())) {
			if (!availVars.contains(i)) {
				valid = false;
				error += "x" + i + " nicht deklariert<br>";
				availVars.add(i);
			}
			if (notInitializedVars.contains(i)) {
				valid = false;
				error += "x" + i + " nicht definiert<br>";
				notInitializedVars.remove((Object) i);
			}
		}
		// Delete initialized Var
		notInitializedVars.remove((Object) assignment.getVar().getIndex());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jalgo.module.c0h0.models.ast.ASTVisitor#visitIf(org.jalgo.module.
	 * c0h0.models.ast.If)
	 */
	public void visitIf(If ifStatement) {
		for (int i : getIndexOfVarList(ifStatement.getRelation().getVars())) {
			if (!availVars.contains(i)) {
				valid = false;
				error += "x" + i + " nicht deklariert<br>";
				availVars.add(i);
			}
			if (notInitializedVars.contains(i)) {
				valid = false;
				error += "x" + i + " nicht definiert<br>";
				notInitializedVars.remove((Object) i);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jalgo.module.c0h0.models.ast.ASTVisitor#visitWhile(org.jalgo.module
	 * .c0h0.models.ast.While)
	 */
	public void visitWhile(While whileStatement) {
		for (int i : getIndexOfVarList(whileStatement.getRelation().getVars())) {
			if (!availVars.contains(i)) {
				valid = false;
				error += "x" + i + " nicht deklariert<br>";
				availVars.add(i);
			}
			if (notInitializedVars.contains(i)) {
				valid = false;
				error += "x" + i + " nicht definiert<br>";
				notInitializedVars.remove((Object) i);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jalgo.module.c0h0.models.ast.ASTVisitor#visitBlock(org.jalgo.module
	 * .c0h0.models.ast.Block)
	 */
	public void visitBlock(Block block) {
		// Ignore
	}

	/**
	 * @return valid boolean whether the semantic check is valid
	 */
	public boolean isValid() {
		if (notInitializedVars.contains(printfVar)) {
			valid = false;
		}
		return valid;
	}

	/**
	 * @return error String with ErrorInformations
	 */
	public String getError() {
		if (notInitializedVars.contains(printfVar)) {
			error += printfVar + " nicht definiert(Printf)<br>";
		}
		return error;
	}
}
