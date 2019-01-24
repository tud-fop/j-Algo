/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2010 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jalgo.module.am0c0.model.c0.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * AST representation for the syntactic variable {@code StatementSequence}. A
 * statement sequence is a non-empty list of statements.
 * 
 * @author Martin Morgenstern
 */
public class StatementSequence extends C0AST {
	private final List<Statement> statementList = new ArrayList<Statement>();

	/**
	 * Construct a new statement sequence with the first element {@code stat}.
	 * 
	 * @param stat
	 *            the first statement to add
	 */
	public StatementSequence(Statement stat) {
		statementList.add(stat);
	}

	/**
	 * Get a copy of the list of statements. The copy is made to ensure that the
	 * internal list representation is only changed by the {@code addStatement}
	 * method.
	 * 
	 * @return list of statements
	 */
	public List<Statement> getStatements() {
		return new ArrayList<Statement>(statementList);
	}

	/**
	 * Add statements to the list.
	 * 
	 * @param stat the statement to add
	 */
	public void addStatement(Statement stat) {
		statementList.add(stat);
	}

	/*@Override
	protected String compressText(String codeText) {
		if (statementList.size() <= 4)
			return codeText;
		else {
			String result = "";
			for (int i = 0; i < 2; i++)
				result += statementList.get(i).getCodeText() + "\n";
			result += "...\n";
			for (int i = statementList.size() - 2; i < statementList.size(); i++)
				result += statementList.get(i).getCodeText() + "\n";
			return result;
		}
	}*/

	@Override
	protected String getCodeTextInternal() {
		StringBuilder codeText = new StringBuilder();

		for (Statement stat : statementList) {
			codeText.append(stat.getCodeText());
			codeText.append("\n");
		}

		return codeText.toString();
	}
}
