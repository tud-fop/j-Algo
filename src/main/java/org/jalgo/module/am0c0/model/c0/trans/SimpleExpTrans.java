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
package org.jalgo.module.am0c0.model.c0.trans;

import java.util.ArrayList;
import java.util.List;

import org.jalgo.main.util.Messages;
import org.jalgo.module.am0c0.model.TreeAddress;
import org.jalgo.module.am0c0.model.c0.ast.*;
import org.jalgo.module.am0c0.model.c0.ast.Factor.*;
import org.jalgo.module.am0c0.model.c0.ast.SimpleExpr.*;
import org.jalgo.module.am0c0.model.c0.ast.Term.*;
import org.jalgo.module.am0c0.model.c0.trans.AtomicTrans.AtomicType;
import org.jalgo.module.am0c0.model.c0.trans.Symbol.SymbolType;

/**
 * Implementation of the C0 {@code simpleexptrans} function.
 * 
 * @author Felix Schmitt
 * @author Martin Morgenstern
 */
public class SimpleExpTrans extends TransformFunction {

	private SimpleExpr simpleExpression;

	public SimpleExpTrans(SimpleExpr token, TreeAddress address) {
		super(token, address);
		this.simpleExpression = token;

		description = Messages.getString("am0c0", "SimpleExpTrans.0"); //$NON-NLS-1$
	}

	/**
	 * Transform the SimpleExpression into a (semantic) equivalent list of
	 * TransformFunctions, in this case actually a list of AtomicTrans.
	 * 
	 * <p>
	 * As the lecture notes have no formal rules for transformation of
	 * SimpleExpressions, this transformation is done in one step (using private
	 * helper methods for transformation of Factors and Terms).
	 * </p>
	 * 
	 * @return a list of equivalent AtomicTrans objects
	 * @throws TransException
	 */
	@Override
	public List<TransformFunction> apply(SymbolTable symbolTable) throws TransException {
		List<TransformFunction> result = new ArrayList<TransformFunction>();
		try {
			if (simpleExpression instanceof UnaryPlusExpr) {
				result.addAll(termTrans(((UnaryPlusExpr) simpleExpression).getTerm(), symbolTable));
			} else if (simpleExpression instanceof UnaryMinusExpr) {
				result.addAll(termTrans(((UnaryMinusExpr) simpleExpression).getTerm(), symbolTable));
				result.add(new AtomicTrans(null, null, AtomicType.LIT, -1));
				result.add(new AtomicTrans(null, null, AtomicType.MUL, 0));
			} else if (simpleExpression instanceof PlusExpr) {
				PlusExpr plusExpr = (PlusExpr) simpleExpression;
				SimpleExpTrans simpleExpTrans = new SimpleExpTrans(plusExpr.getLeft(),
						null);

				result.addAll(simpleExpTrans.apply(symbolTable));
				result.addAll(termTrans(plusExpr.getRight(), symbolTable));
				result.add(new AtomicTrans(null, null, AtomicType.ADD, 0));
			} else if (simpleExpression instanceof MinusExpr) {
				MinusExpr minusExpr = (MinusExpr) simpleExpression;
				SimpleExpTrans simpleExpTrans = new SimpleExpTrans(minusExpr.getLeft(),
						null);

				result.addAll(simpleExpTrans.apply(symbolTable));
				result.addAll(termTrans(minusExpr.getRight(), symbolTable));
				result.add(new AtomicTrans(null, null, AtomicType.SUB, 0));
			} else {
				throw new IllegalStateException("Illegal form of SimpleExpression."); //$NON-NLS-1$
			}

		} catch (SymbolException e) {
			throw new TransException(e.getMessage());
		}
		return result;
	}

	/**
	 * Transform Factors into a (semantic) equivalent list of AtomicTrans.
	 * 
	 * <ul>
	 * <li>IdentFactors are translated into a {@code LOAD addr} AtomicTrans
	 * object, where {@code addr} is the corresponding address of the identifier
	 * (determined by looking up the symbol table).</li>
	 * 
	 * <li>NumberFactors are translated into a {@code LIT z} AtomicTrans object,
	 * where {@code z} is the constant number (determined by the value of the
	 * token).</li>
	 * 
	 * <li>Factors that contain SimpleExpressions in parentheses just transform
	 * the contained SimpleExpression.</li>
	 * </ul>
	 * 
	 * @param factor
	 *            the factor that is to be transformed
	 * @return a list of equivalent AtomicTrans objects
	 * @throws SymbolException
	 * @throws TransException
	 */
	private List<TransformFunction> factorTrans(Factor factor, SymbolTable symbolTable) throws SymbolException,
			TransException {
		List<TransformFunction> result = new ArrayList<TransformFunction>();

		if (factor instanceof IdentFactor) {
			String ident = ((IdentFactor) factor).getIdentName();

			if (symbolTable.getType(ident) == SymbolType.ST_CONST) {
				int value = 0;
				value = symbolTable.getValue(ident);

				result.add(new AtomicTrans(null, null, AtomicType.LIT, value));
			} else {
				int address = symbolTable.getAddress(ident);
				result.add(new AtomicTrans(null, null, AtomicType.LOAD, address));
			}
		} else if (factor instanceof NumberFactor) {
			int value = ((NumberFactor) factor).getNumber();
			result.add(new AtomicTrans(null, null, AtomicType.LIT, value));
		} else if (factor instanceof CompExprFactor) {
			SimpleExpr simpleExpr = ((CompExprFactor) factor).getSimpleExpr();
			SimpleExpTrans simpleExpTrans = new SimpleExpTrans(simpleExpr, null);
			result.addAll(simpleExpTrans.apply(symbolTable));
		} else {
			throw new TransException("Illegal form of Factor."); //$NON-NLS-1$
		}

		return result;
	}

	/**
	 * Transform Terms into a (semantic) equivalent list of AtomicTrans objects.
	 * 
	 * <ul>
	 * <li>FactorTerms are transformed by transforming the contained Factor.</li>
	 * <li>All other terms, which have two operands (a Term on the left side, a
	 * Factor on the right side) are transformed by transforming the Term, then
	 * the Factor and finally adding the corresponding AM0 operation (i.e. an
	 * AtomicTrans object for {@code MUL}, {@code DIV} or {@code MOD}).</li>
	 * </ul>
	 * 
	 * @param term
	 *            the Term to be transformed
	 * @return an equivalent list of AtomicTrans objects
	 * @throws SymbolException
	 * @throws TransException
	 */
	private List<TransformFunction> termTrans(Term term, SymbolTable symbolTable) throws SymbolException, TransException {
		List<TransformFunction> result = new ArrayList<TransformFunction>();

		if (term instanceof FactorTerm) {
			result.addAll(factorTrans(((FactorTerm) term).getFactor(), symbolTable));
		} else if (term instanceof BinTerm) {
			BinTerm multTerm = (BinTerm) term;
			result.addAll(termTrans(multTerm.getLeft(), symbolTable));
			result.addAll(factorTrans(multTerm.getRight(), symbolTable));

			if (term instanceof MultTerm) {
				result.add(new AtomicTrans(null, null, AtomicType.MUL, 0));
			} else if (term instanceof DivTerm) {
				result.add(new AtomicTrans(null, null, AtomicType.DIV, 0));
			} else if (term instanceof ModTerm) {
				result.add(new AtomicTrans(null, null, AtomicType.MOD, 0));
			}
		} else {
			throw new TransException("Illegal form of Term."); //$NON-NLS-1$
		}

		return result;
	}

	@Override
	public String getCodeText() {
		return "simpleexptrans(" + simpleExpression.getCodeText() + ", tab)"; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
