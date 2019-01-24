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

import org.apache.commons.lang.StringUtils;
import org.jalgo.module.am0c0.model.Address;
import org.jalgo.module.am0c0.model.CodeObject;

/**
 * Represents the base of the abstract syntax tree for a parsed C0 program.
 * 
 * <p>
 * All parts of the abstract syntax tree extend this class.
 * </p>
 * 
 * <p>
 * Additionally, this class contains AST types that are used frequently by the
 * other AST types ({@link Ident} and {@link ConstIdent}).
 * </p>
 * 
 * @author Martin Morgenstern
 */
public abstract class C0AST extends CodeObject {
	public C0AST(Address address) {
		super(address);
	}

	public C0AST() {
		this(null);
	}

	/**
	 * 
	 */
	protected String compressText(final String codeText) {
		if (codeText == null) {
			throw new NullPointerException();
		}

		String[] parts = codeText.split("\n");

		if (parts.length <= 5) {
			return codeText;
		}

		final StringBuilder result = new StringBuilder();

		for (int i = 0; i < 3; i++) {
			result.append(parts[i]);
			result.append("\n");
		}

		result.append("...\n");

		for (int i = parts.length - 2; i < parts.length; i++) {
			result.append(parts[i]);
			result.append("\n");
		}

		return result.toString();
	}

	@Override
	public int getLinesCount() {
		String codeText = compressText(getCodeTextInternal());
		return StringUtils.countMatches(codeText, "\n") + 1; //$NON-NLS-1$
	}

	protected abstract String getCodeTextInternal();

	@Override
	public String getCodeText() {
		return compressText(getCodeTextInternal());
	}

	/**
	 * Represents identifiers for variables.
	 * 
	 * @author Martin Morgenstern
	 */
	static public class Ident {
		private final String name;

		/**
		 * Construct a new identifier named {@code name}.
		 * 
		 * @param name
		 *            the name of the new identifier
		 */
		public Ident(final String name) {
			this.name = name;
		}

		/**
		 * @return the name of the identifier
		 */
		public String getName() {
			return name;
		}
	}

	/**
	 * Represents identifiers for constants.
	 * 
	 * @author Martin Morgenstern
	 */
	static public class ConstIdent {
		private final String name;
		private final int value;

		/**
		 * Construct a new constant identifier {@code name} with the value
		 * {@code value} .
		 * 
		 * @param name
		 *            the name of the constant
		 * @param value
		 *            the value of the constant
		 */
		public ConstIdent(final String name, final int value) {
			this.name = name;
			this.value = value;
		}

		/**
		 * @return the name of the constant
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the value of the constant
		 */
		public int getValue() {
			return value;
		}
	}
}
