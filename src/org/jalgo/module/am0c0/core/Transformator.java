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
/**
 * 
 */
package org.jalgo.module.am0c0.core;

import java.util.List;
import java.util.Map;

import javax.activity.InvalidActivityException;
import javax.swing.JOptionPane;

import org.jalgo.main.util.Messages;
import org.jalgo.module.am0c0.gui.TransformationView;
import org.jalgo.module.am0c0.gui.View;
import org.jalgo.module.am0c0.model.AddressException;
import org.jalgo.module.am0c0.model.C0History;
import org.jalgo.module.am0c0.model.LineAddress;
import org.jalgo.module.am0c0.model.am0.AM0Program;
import org.jalgo.module.am0c0.model.c0.C0Program;
import org.jalgo.module.am0c0.model.c0.ast.Program;
import org.jalgo.module.am0c0.model.c0.trans.AddressSolver;
import org.jalgo.module.am0c0.model.c0.trans.AtomicTrans;
import org.jalgo.module.am0c0.model.c0.trans.BlockTrans;
import org.jalgo.module.am0c0.model.c0.trans.C0TransProgram;
import org.jalgo.module.am0c0.model.c0.trans.StubTrans;
import org.jalgo.module.am0c0.model.c0.trans.SymbolTable;
import org.jalgo.module.am0c0.model.c0.trans.Trans;
import org.jalgo.module.am0c0.model.c0.trans.TransException;
import org.jalgo.module.am0c0.model.c0.trans.TransformFunction;

/**
 * Controller to handle the c0 -> am0 transformation.
 * 
 * @author Felix Schmitt
 * @author Franz Gregor
 * 
 */
public class Transformator extends Subcontroller {

	protected C0TransProgram functions;

	public static enum TransformationState {
		TS_WAITING, TS_PREVIEW, TS_FINISHED, TS_WAITING_FIRST, TS_PREVIEW_FIRST
	};

	private TransformationState state;
	private SymbolTable symbolTable;

	private C0History<C0TransProgram> functionsHistory;

	@Override
	public TransformationView getView() {
		return (TransformationView) (super.getView());
	}

	/**
	 * inits or resets the histories
	 */
	private void newHistory() {
		functionsHistory = new C0History<C0TransProgram>();
	}

	/**
	 * constructor
	 * 
	 * @param controller
	 *            the {@link Controller} for this {@link Subcontroller}
	 */
	public Transformator(Controller controller) {
		this.controller = controller;
		this.view = new TransformationView(this);
		state = TransformationState.TS_WAITING_FIRST;

		functions = new C0TransProgram();

		newHistory();
	}

	/**
	 * returns the current {@link TransformationState}
	 * 
	 * @return the current {@link TransformationState}
	 */
	public TransformationState getState() {
		return state;
	}

	/**
	 * sets the current {@link TransformationState}
	 * 
	 * @param state
	 *            new {@link TransformationState}
	 */
	public void setState(TransformationState state) {
		this.state = state;
	}

	/**
	 * returns the current {@link SymbolTable}
	 * 
	 * @return the current {@link SymbolTable}
	 */
	public SymbolTable getSymbolTable() {
		return symbolTable;
	}

	/**
	 * previews the results of applying the {@link TransformFunction} at index
	 * 
	 * @param index
	 *            index of the {@link TransformFunction} to preview
	 * @return if the preview could be generated successfully
	 */
	public boolean previewStatement(int index) throws IllegalStateException {
		if (state == TransformationState.TS_FINISHED)
			throw new IllegalStateException("Preview is not permitted in state FINISHED."); //$NON-NLS-1$

		TransformFunction func = functions.get(index);

		if (func instanceof StubTrans)
			return false;

		if (func instanceof AtomicTrans) {
			JOptionPane.showMessageDialog(getView(),
					Messages.getString("am0c0", "Transformator.1"), //$NON-NLS-1$
					Messages.getString("am0c0", "Transformator.2"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
			return false;
		}

		try {
			getView().setPreviewText(getPreviewText(index));
			getView().setRuleText(getRuleText(index));
			if (getState() == TransformationState.TS_WAITING
					|| getState() == TransformationState.TS_PREVIEW) {
				setState(TransformationState.TS_PREVIEW);
			} else {
				setState(TransformationState.TS_PREVIEW_FIRST);
			}
			return true;
		} catch (TransException e) {
			JOptionPane.showMessageDialog(getView(), e.getMessage(), Messages.getString("am0c0", "Transformator.0"), //$NON-NLS-1$
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	/*
	 * protected boolean internalTranslateNextStatement() { int nextIndex = -1;
	 * for (int i = 0; i < functions.size(); i++) { TransformFunction func =
	 * functions.get(i); if (!((func instanceof AtomicTrans) || (func instanceof
	 * StubTrans))) { nextIndex = i; break; } }
	 * 
	 * if (nextIndex == -1) { return false; } else { try { applyFunc(nextIndex);
	 * return true; } catch (TransException e) { return false; } } }
	 */

	/**
	 * translates the {@link TransformFunction} at index and applies the results
	 * to the {@link C0TransProgram}
	 * 
	 * @param index
	 *            index of the {@link TransformFunction} to translate/apply
	 * @return if the translation was successful
	 */
	public boolean translateStatement(int index) throws IllegalStateException {
		if (state == TransformationState.TS_FINISHED)
			throw new IllegalStateException("Translation is not permitted in state FINISHED."); //$NON-NLS-1$

		try {
			TransformFunction func = functions.get(index);

			if (func instanceof AtomicTrans || func instanceof StubTrans) {
				JOptionPane.showMessageDialog(getView(),
						Messages.getString("am0c0", "Transformator.5"), //$NON-NLS-1$
						Messages.getString("am0c0", "Transformator.6"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
				return false;
			}

			applyFunc(index);
		} catch (TransException e) {
			JOptionPane.showMessageDialog(getView(), e.getMessage(), Messages.getString("am0c0", "Transformator.7"), //$NON-NLS-1$
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		getView().updateModel(functions);
		if (transformationFinished()) {
			setState(TransformationState.TS_FINISHED);
			JOptionPane.showMessageDialog(getView(), Messages.getString("am0c0", "Transformator.8"), //$NON-NLS-1$
					Messages.getString("am0c0", "Transformator.9"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
		} else
			setState(TransformationState.TS_WAITING);
		return true;

	}

	/**
	 * translates the next available {@link TransformFunction} and applies the
	 * results to the {@link C0TransProgram}
	 * 
	 * @param updateView
	 *            if the {@link View} should be updated after translation
	 * @return if the translation was successful. returns false if there was no
	 *         next available {@link TransformFunction}
	 */
	public boolean translateNextStatement(boolean updateView) throws IllegalStateException {
		if (state == TransformationState.TS_FINISHED)
			throw new IllegalStateException("Translation is not permitted in state FINISHED."); //$NON-NLS-1$

		int nextIndex = -1;
		for (int i = 0; i < functions.size(); i++) {
			TransformFunction func = functions.get(i);
			if (!((func instanceof AtomicTrans) || (func instanceof StubTrans))) {
				nextIndex = i;
				break;
			}
		}

		if (nextIndex == -1) {
			return false;
		} else
			try {
				applyFunc(nextIndex);
			} catch (TransException e) {
				JOptionPane.showMessageDialog(getView(), e.getMessage(), Messages.getString("am0c0", "Transformator.11"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
				return false;
			}

		if (updateView)
			getView().updateModel(functions);

		if (transformationFinished()) {
			setState(TransformationState.TS_FINISHED);
			JOptionPane.showMessageDialog(getView(), Messages.getString("am0c0", "Transformator.12"), //$NON-NLS-1$
					Messages.getString("am0c0", "Transformator.13"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
			return false;

		} else {
			setState(TransformationState.TS_WAITING);
			return true;
		}
	}

	/**
	 * translates the complete {@link C0TransProgram} and updated the
	 * {@link View} afterwards
	 */
	public void translateAll() throws IllegalStateException {
		while (translateNextStatement(false)) {
		}
		;
		getView().updateModel(functions);
	}

	/**
	 * creates a new {@link C0TransProgram} using program. The current
	 * {@link C0TransProgram}, {@link SymbolTable} and {@link View} will be
	 * cleared
	 * 
	 * @param program
	 *            the new {@link C0Program}. must not be null
	 */
	public void setC0Program(C0Program program) throws IllegalArgumentException {
		if (program != null) {
			functions.clear();
			symbolTable = new SymbolTable();
			functions.add(new Trans((Program) program.get(0), null));
			setState(TransformationState.TS_WAITING_FIRST);
			getView().updateModel(functions);
			getView().updateSymbolTable();
			newHistory();
		} else
			throw new IllegalArgumentException("C0Program must not be null."); //$NON-NLS-1$
	}

	/**
	 * returns the current {@link C0TransProgram}
	 * 
	 * @return the current {@link C0TransProgram}
	 */
	public C0TransProgram getC0TransProgram() {
		return functions;
	}

	/**
	 * checks if the transformation is complete. returns true if there are only
	 * {@link AtomicTrans} or {@link StubTrans} functions remaining
	 * 
	 * @return whether the transformation is finished
	 */
	protected boolean transformationFinished() {
		boolean result = true;
		for (TransformFunction func : functions)
			if (!((func instanceof AtomicTrans) || (func instanceof StubTrans)))
				return false;

		return result && functions.size() != 0;
	}

	/**
	 * applies the {@link TransformFunction} at index by inserting the results
	 * to the list and updating symbolTable
	 * 
	 * @param index
	 *            the index of the functions to apply
	 * @throws TransException
	 */
	protected void applyFunc(int index) throws TransException {
		List<TransformFunction> resultFunctions = null;
		try {
			resultFunctions = getResultFunctions(index, false);
		} catch (IllegalArgumentException e) {
			throw new TransException(e.getMessage());
		}

		functionsHistory.add(functions.clone());
		functions.remove(index);
		functions.addAll(index, resultFunctions);
	}

	/**
	 * goes one step back in the transformation
	 * 
	 * @throws InvalidActivityException
	 */
	public void stepBack() throws InvalidActivityException {
		if (!functionsHistory.hasPrevious())
			throw new InvalidActivityException();

		functions = functionsHistory.previous();
		getView().updateModel(functions);

		if (functions.get(0) instanceof BlockTrans)
			symbolTable.clear();

		if (functionsHistory.hasPrevious()) {
			setState(TransformationState.TS_WAITING);
		} else {
			setState(TransformationState.TS_WAITING_FIRST);
		}
	}

	/**
	 * returns the list of resulting functions when applying
	 * {@link TransformFunction} at index
	 * 
	 * @param index
	 *            the index of the function to apply
	 * @param preview
	 *            set true if results should be previewed but not inserted into
	 *            the list
	 * @return the list of resulting {@link TransformFunction}s
	 * @throws TransException
	 */
	protected List<TransformFunction> getResultFunctions(int index, boolean preview)
			throws TransException {
		TransformFunction func = functions.get(index);

		if (func instanceof AtomicTrans || func instanceof StubTrans)
			throw new TransException(
					Messages.getString("am0c0", "Transformator.15") //$NON-NLS-1$
							+ func.getCodeText());

		if (preview && func instanceof BlockTrans)
			return ((BlockTrans) func).applyPreview();
		else
			try {
				return func.apply(symbolTable);
			} catch (TransException e) {
				throw new TransException(e.getMessage() + "\n" + func.getCodeText()); //$NON-NLS-1$
			}
	}

	/**
	 * returns the preview text associated with the {@link TransformFunction} at
	 * index
	 * 
	 * @param index
	 *            the index of the function
	 * @return the preview text
	 * @throws TransException
	 */
	protected String getPreviewText(int index) throws TransException {
		StringBuilder resultText = new StringBuilder();

		List<TransformFunction> resultFunctions = getResultFunctions(index, true);

		for (TransformFunction resultFunc : resultFunctions) {
			resultText.append(resultFunc.getCodeText());
			resultText.append("\n");
		}

		return resultText.toString();
	}

	/**
	 * returns the rule text associated with the {@link TransformFunction} at
	 * index (their description)
	 * 
	 * @param index
	 *            the index of the function
	 * @return the rule text
	 * @throws IllegalArgumentException
	 */
	protected String getRuleText(int index) throws IllegalArgumentException {
		if (index >= 0 && index < functions.size())
			return functions.get(index).getDescription();
		else
			throw new IllegalArgumentException(Messages.getString("am0c0", "Transformator.3") + index); //$NON-NLS-1$
	}

	public AM0Program getAM0Program() throws NullPointerException, AddressException {
		Map<String, LineAddress> addresses;

		addresses = AddressSolver.solve(getC0TransProgram());

		AM0Program am0program = new AM0Program();

		int line = 1;
		for (TransformFunction func : getC0TransProgram()) {
			if (!(func instanceof StubTrans)) {
				AtomicTrans atomFunc = (AtomicTrans) func;

				am0program.add(atomFunc.getStatement(new LineAddress(line), addresses));

				line++;
			}
		}

		return am0program;
	}
}
