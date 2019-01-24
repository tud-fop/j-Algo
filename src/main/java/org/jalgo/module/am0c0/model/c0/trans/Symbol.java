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

import org.jalgo.main.util.Messages;

/**
 * Immutable representation of a C0 symbol, which may be a variable or constant.
 * Both types have an identifier and additionally,
 * <ul>
 * <li>a variable symbol has a memory address and</li>
 * <li>a constant symbol has a value.</li>
 * </ul>
 * Use the static factory methods {@link #varSymbol(String, int)} or
 * {@link #constSymbol(String, int)} to get a new instance of a variable or
 * constant symbol, respectively. They were chosen in favor of the standard
 * constructor because of their clearer names and easier usage.
 * 
 * @author Felix Schmitt
 * @author Martin Morgenstern
 */
public class Symbol {
	/**
	 * Enumeration that defines the type of a {@link Symbol}.
	 */
	public static enum SymbolType {
		/** Constant symbols */
		ST_CONST,
		/** Variable symbols */
		ST_VAR
	}

	private final String id;
	private final int value;
	private final SymbolType type;
	private final int address;

	/** The minimum value for an address. */
	private final static int ADDRESS_MIN = 1;

	/**
	 * Constant that is used to make clear that a value is actually not needed,
	 * but has to be initialized.
	 */
	private final static int UNUSED = -1;

	/**
	 * Static factory method which returns a new constant symbol.
	 * 
	 * @param id
	 *            the id (name) of the constant symbol
	 * @param value
	 *            the value of the constant symbol
	 * @return a new constant symbol
	 */
	public static Symbol constSymbol(String id, int value) {
		return new Symbol(id, UNUSED, SymbolType.ST_CONST, value);
	}

	/**
	 * Static factory method which returns a new variable symbol.
	 * 
	 * @param id
	 *            the id (name) of the variable symbol
	 * @param address
	 *            the memory address of the variable symbol
	 * @return a new variable symbol
	 * @throws IllegalArgumentException
	 *             if address < {@value #ADDRESS_MIN}
	 */
	public static Symbol varSymbol(String id, int address) {
		if (address < ADDRESS_MIN) {
			throw new IllegalArgumentException(Messages.getString(
					"am0c0", "Symbol.0") + ADDRESS_MIN); //$NON-NLS-1$
		}

		return new Symbol(id, address, SymbolType.ST_VAR, UNUSED);
	}

	/**
	 * Constructs a new symbol. This is internal, please use the easier to use
	 * and read static factories {@link #constSymbol(String, int)} and
	 * {@link #varSymbol(String, int)} to get instances of this class.
	 * 
	 * @param id
	 *            the name of the symbol
	 * @param address
	 *            the address of the symbol (use {@link #UNUSED} for constant
	 *            symbols)
	 * @param type
	 *            the type (const or var) of the symbol
	 * @param value
	 *            the value of the symbol (use {@link #UNUSED} for variable
	 *            symbols)
	 * @throws NullPointerException
	 *             if {@code id} is {@code null}
	 */
	private Symbol(String id, int address, SymbolType type, int value) {
		if (null == id) {
			throw new NullPointerException("id must not be null");
		}

		this.id = id;
		this.address = address;
		this.value = value;
		this.type = type;
	}

	/**
	 * returns the name of the symbol
	 * 
	 * @return the symbol's name/id
	 */
	public String getID() {
		return id;
	}

	/**
	 * returns the integer value of the symbol retrieving the value of an
	 * uninitialized symbol raises an exception
	 * 
	 * @return the symbol's value
	 * @throws SymbolException
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Get the address of this symbol.
	 * 
	 * @return address of the symbol
	 * @throws SymbolException
	 *             if the symbol is a constant symbol
	 */
	public int getAddress() throws SymbolException {
		if (type == SymbolType.ST_CONST)
			throw new SymbolException(Messages.getString("am0c0", "Symbol.1")); //$NON-NLS-1$

		return address;
	}

	/**
	 * returns the type of the symbol (const or var)
	 * 
	 * @return the symbol's type
	 */
	public SymbolType getType() {
		return type;
	}

	/**
	 * returns the string representation of the symbol consisting of its id and
	 * value
	 * 
	 * @return a string representation
	 */
	@Override
	public String toString() {
		String result = id;

		if (type == SymbolType.ST_CONST) {
			result += " = " + value; //$NON-NLS-1$
		}

		return result;
	}
}
