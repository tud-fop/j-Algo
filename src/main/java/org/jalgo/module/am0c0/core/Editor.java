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

import org.jalgo.main.util.Messages;
import org.jalgo.module.am0c0.model.am0.AM0Program;
import org.jalgo.module.am0c0.model.c0.C0Program;
import org.jalgo.module.am0c0.parser.Parser;
import org.jalgo.module.am0c0.parser.am0.AM0Parser;
import org.jalgo.module.am0c0.parser.c0.C0Parser;
import org.jalgo.module.am0c0.gui.EditorView;
import org.jalgo.module.am0c0.gui.jeditor.JEditor;
import org.jalgo.module.am0c0.gui.jeditor.jedit.tokenmarker.AM0TokenMarker;
import org.jalgo.module.am0c0.gui.jeditor.jedit.tokenmarker.CTokenMarker;

/**
 * The Editor SubController, containing the EditorView (with JEditor component).
 * It controls code parsing/validation through its states and can return parsed
 * code as AM0/C0Programs.
 * 
 * @author Felix Schmitt
 * @author Franz Gregor
 * 
 */
public class Editor extends Subcontroller {
	public final static int AM0 = 0;
	public final static int C0 = 1;

	/**
	 * abstract static base class for all states of this Editor controller
	 * 
	 * @author Felix Schmitt
	 * 
	 */
	public abstract static class EditorState {
		protected Parser parser;

		/**
		 * validates the current code and returns the next {@link EditorState}
		 * (which can be the last one, too) depending on the last state and the
		 * validation result.
		 * 
		 * @return the next {@link EditorState}
		 */
		public EditorState validate(String code) {
			return this;
		}

		/**
		 * Returns the parsed {@link C0Program}. Calling this method in an
		 * invalid state will cause a {@link IllegalStateException}.
		 * 
		 * @return the parsed {@link C0Program}
		 * @throws IllegalStateException
		 *             if this method is invoked on instances of
		 *             {@link StateNotValidated} and {@link StateValidatedAM0}
		 */
		public abstract C0Program getC0Program();

		/**
		 * Returns the parsed {@link AM0Program}. Calling this method in an
		 * invalid state will cause a {@link IllegalStateException}.
		 * 
		 * @return the parsed {@link AM0Program}
		 * @throws IllegalStateException
		 *             if this method is invoked on instances of
		 *             {@link StateNotValidated} and {@link StateValidatedC0}
		 */
		public abstract AM0Program getAM0Program();

		/**
		 * abstract method for returning text with current validation status
		 * 
		 * @return a String with current validation status
		 */
		public abstract String getValidationResult();
	}

	/**
	 * abstract base class for not-validated states
	 * 
	 * @author Felix Schmitt
	 * 
	 */
	public abstract static class StateNotValidated extends EditorState {

		/**
		 * parses the code in {@link JEditor} an returns the result. used by
		 * subclasses
		 * 
		 * @return the parsing result
		 */
		protected boolean isValid(String code) {
			return parser.parse(code);
		}

		/**
		 * returns the error text/validation text returned by the parser
		 * 
		 * @return a String with the error text
		 */
		@Override
		public String getValidationResult() {
			return parser.getErrorText();
		}

		/**
		 * Invoking this method on instances of this class results in a
		 * {@link IllegalStateException}.
		 */
		@Override
		public C0Program getC0Program() {
			throw new IllegalStateException();
		}

		/**
		 * Invoking this method on instances of this class results in a
		 * {@link IllegalStateException}.
		 */
		@Override
		public AM0Program getAM0Program() {
			throw new IllegalStateException();
		}
	}

	/**
	 * state with invalidated AM0 code in the editor
	 * 
	 * @author Felix Schmitt
	 * 
	 */
	public static class StateNotValidatedAM0 extends StateNotValidated {

		/**
		 * constructor
		 */
		public StateNotValidatedAM0() {
			parser = new AM0Parser();
		}

		@Override
		public EditorState validate(String code) {
			if (isValid(code))
				return new StateValidatedAM0((AM0Parser) parser);
			else
				return this;
		}
	}

	/**
	 * state with invalidated C0 code in the editor
	 * 
	 * @author Felix Schmitt
	 * 
	 */
	public static class StateNotValidatedC0 extends StateNotValidated {

		/**
		 * constructor
		 */
		public StateNotValidatedC0() {
			parser = new C0Parser();
		}

		@Override
		public EditorState validate(String code) {
			if (isValid(code))
				return new StateValidatedC0((C0Parser) parser);
			else
				return this;
		}
	}

	/**
	 * abstract base class for validated states
	 * 
	 * @author Felix Schmitt
	 * 
	 */
	public abstract static class StateValidated extends EditorState {

		/**
		 * constructor
		 * 
		 * @param parser
		 *            a {@link Parser} used for validating code
		 */
		public StateValidated(Parser parser) {
			if (parser == null)
				throw new IllegalArgumentException("Parser must not be null."); //$NON-NLS-1$
			this.parser = parser;
		}

		@Override
		public String getValidationResult() {
			return Messages.getString("am0c0", "Editor.3") //$NON-NLS-1$
					+ Messages.getString("am0c0", "Editor.4"); //$NON-NLS-1$
		}
	}

	/**
	 * state with validated AM0 code in the editor
	 * 
	 * @author Felix Schmitt
	 * 
	 */
	public static class StateValidatedAM0 extends StateValidated {

		/**
		 * constructor
		 * 
		 * @param parser
		 *            see {@link StateValidated }
		 */
		public StateValidatedAM0(AM0Parser parser) {
			super(parser);
		}

		/**
		 * returns the {@link AM0Program } when parsing finished successfully,
		 * null otherwise
		 * 
		 * @return the {@link AM0Program } or null
		 */
		@Override
		public AM0Program getAM0Program() {
			return ((AM0Parser) parser).getProgram();
		}

		/**
		 * Invoking this method on instances of this class results in a
		 * {@link IllegalStateException}.
		 */
		@Override
		public C0Program getC0Program() {
			throw new IllegalStateException();
		}
	}

	/**
	 * state with validated C0 code in the editor
	 * 
	 * @author Felix Schmitt
	 * 
	 */
	public static class StateValidatedC0 extends StateValidated {

		/**
		 * constructor
		 * 
		 * @param parser
		 *            see {@link StateValidated }
		 */
		public StateValidatedC0(C0Parser parser) {
			super(parser);
		}

		/**
		 * returns the {@link C0Program } when parsing finished successfully,
		 * null otherwise
		 * 
		 * @return the {@link C0Program } or null
		 */
		@Override
		public C0Program getC0Program() {
			return ((C0Parser) parser).getProgram();
		}

		/**
		 * Invoking this method on instances of this class results in a
		 * {@link IllegalStateException}.
		 */
		@Override
		public AM0Program getAM0Program() {
			throw new IllegalStateException();
		}
	}

	@SuppressWarnings("unused")
	private boolean autoValidate;
	private EditorState state;

	/**
	 * constructor
	 * 
	 * @param controller
	 *            the {@link Controller } for this {@link Subcontroller}
	 */
	public Editor(Controller controller) {
		this.controller = controller;
		view = new EditorView(this);
		setAutoValidate(false);
	}

	@Override
	public EditorView getView() {
		return (EditorView) (super.getView());
	}

	/**
	 * Loads code file to the {@link JEditor} component and sets the editor
	 * state corresponding to the file extension.
	 * 
	 * @return the path of the file loaded or an empty String the user aborted
	 *         the load dialog.
	 */
	public String loadCode() {
		String filepath = ((EditorView) view).getJEditor().loadCode();

		if (filepath.endsWith(".am0")) { //$NON-NLS-1$
			setState(new StateNotValidatedAM0());
		}

		if (filepath.endsWith(".c0")) { //$NON-NLS-1$
			setState(new StateNotValidatedC0());
		}

		return filepath;
	}

	/**
	 * saves code from {@link JEditor } to a file
	 * 
	 * @return the absolut patch if the user confirmed the save dialog or "" if not
	 */
	public String saveCode() {
		return ((EditorView) view).getJEditor().saveCode();
	}

	/**
	 * sets the {@link EditorState }
	 * 
	 * @param state
	 *            the new {@link EditorState}. must not be null
	 */
	public void setState(EditorState state) throws IllegalArgumentException {
		if (state == null)
			throw new IllegalArgumentException("EditorState must not be null"); //$NON-NLS-1$

		if (state instanceof StateNotValidatedAM0
				|| state instanceof StateValidatedAM0) {
			getView().codeField.setTokenMarker(new AM0TokenMarker());
			getView().codeField.repaint();
			getView().am0Button.setSelected(true);
		}

		if (state instanceof StateNotValidatedC0
				|| state instanceof StateValidatedC0) {
			getView().codeField.setTokenMarker(new CTokenMarker());
			getView().codeField.repaint();
			getView().c0Button.setSelected(true);
		}

		this.state = state;
	}

	/**
	 * returns the current {@link EditorState }
	 * 
	 * @return the current {@link EditorState }
	 */
	public EditorState getState() {
		return state;
	}

	/**
	 * returns text with current validation status
	 * 
	 * @return a String with validation status
	 */
	public String getValidationResult() {
		return state.getValidationResult();
	}

	/**
	 * calls validation and updated the {@link EditorState }. the validation
	 * result will be logged to the GUI
	 * 
	 * @param type
	 *            either {@link Editor#AM0} or {@link Editor#C0}.
	 * @return true if the code was successfully validated (new state is a
	 *         {@link StateValidated })
	 */
	public boolean validate(final int type) {
		if (type == AM0) {
			setState(new StateNotValidatedAM0());
		}
		if (type == C0) {
			setState(new StateNotValidatedC0());
		}

		setState(state
				.validate(((EditorView) getView()).getJEditor().getText()));

		((EditorView) view).setConsoleText(getValidationResult());

		return getState() instanceof StateValidatedAM0
				|| getState() instanceof StateValidatedC0;
	}

	/**
	 * calls validation and updated the {@link EditorState }.
	 */
	@SuppressWarnings("unused")
	private void autoValidate() {
		setState(state
				.validate(((EditorView) getView()).getJEditor().getText()));
	}

	/**
	 * sets auto validation. if autoValidate is true, a timer will try to
	 * validate the code in {@link JEditor } in intervals
	 * 
	 * @param autoValidate
	 *            new value for auto-validation
	 */
	public void setAutoValidate(boolean autoValidate) {
		this.autoValidate = autoValidate;
	}

	public void setAM0Program(AM0Program am0program) {
		getView().codeField.updateModel(am0program);
		getView().tabPane.setTitleAt(0, Messages.getString("am0c0", "Editor.5"));
		setState(new StateNotValidatedAM0());
	}
}
