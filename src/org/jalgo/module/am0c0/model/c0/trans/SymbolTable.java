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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jalgo.main.util.Messages;
import org.jalgo.module.am0c0.model.am0.Ram;
import org.jalgo.module.am0c0.model.c0.trans.Symbol.SymbolType;

/**
 * Represents a C0 symbol table
 * 
 * @author Felix Schmitt
 * 
 */
public class SymbolTable implements Cloneable {
	private Map<String, Symbol> symbols;

	/**
	 * constructor
	 */
	public SymbolTable() {
		symbols = new HashMap<String, Symbol>();
	}

	@Override
	public SymbolTable clone() {
		try {
			SymbolTable result = (SymbolTable) super.clone();
			result.symbols = new HashMap<String, Symbol>(symbols);
			return result;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
	}

	/**
	 * removes all {@link Symbol}s from the table
	 */
	public void clear() {
		symbols.clear();
	}

	/**
	 * adds a new symbol to the table
	 * 
	 * @param symbol
	 *            the new symbol
	 * @throws SymbolException
	 */
	public void add(Symbol symbol) throws SymbolException {
		if (exists(symbol.getID()))
			throw new SymbolException(Messages.getString("am0c0", "SymbolTable.0") + symbol.getID() + Messages.getString("am0c0", "SymbolTable.1")); //$NON-NLS-1$ //$NON-NLS-2$

		if (symbol.getType() == SymbolType.ST_VAR)
			for (Symbol s : symbols.values())
				if (s.getType() == SymbolType.ST_VAR && s.getAddress() == symbol.getAddress())
					throw new SymbolException(Messages.getString("am0c0", "SymbolTable.2") + s.getAddress() //$NON-NLS-1$
							+ Messages.getString("am0c0", "SymbolTable.3")); //$NON-NLS-1$

		symbols.put(symbol.getID(), symbol);
	}

	/**
	 * checks if an symbol id already exists in the table
	 * 
	 * @param id
	 *            the id to check
	 * @return if the id exists in the table
	 */
	public boolean exists(String id) {
		return symbols.containsKey(id);
	}

	/**
	 * returns the number of entries in the table
	 * 
	 * @return number of table entries
	 */
	public int size() {
		return symbols.size();
	}

	/**
	 * transforms this SymbolTable into type String[][] for use with JTable
	 * 
	 * @return a String[][] representation of this SymbolTable
	 * @throws SymbolException
	 */
	public String[][] toStringTable() throws SymbolException {
		String[][] result = new String[size()][4];

		Iterator<String> iter = symbols.keySet().iterator();
		int i = 0;
		while (iter.hasNext()) {
			String key = iter.next();
			Symbol symbol = symbols.get(key);

			String tabOfId = "";

			if (symbol.getType() == SymbolType.ST_CONST) {
				tabOfId = "(const, " + symbol.getValue() + ")";
			} else {
				tabOfId = "(var, " + symbol.getAddress() + ")";
			}

			String[] row = { symbol.getID(), tabOfId };
			result[i] = row;
			i++;
		}

		return result;
	}

	/**
	 * returns a String representation of this SymbolTable. one {@link Symbol}
	 * per line
	 * 
	 * @return a String representation
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		for (Symbol s : symbols.values()) {
			result.append(s.toString());
			result.append("\n"); //$NON-NLS-1$
		}

		return result.toString();
	}

	/**
	 * returns the SymbolType of an id in the table. retrieving the type of an
	 * id not in the table raises an exception
	 * 
	 * @param id
	 *            the id to check
	 * @return the type of the symbol with this id
	 */
	public SymbolType getType(String id) throws SymbolException {
		if (!exists(id))
			throw new SymbolException(
					Messages.getString("am0c0", "SymbolTable.9")); //$NON-NLS-1$

		return symbols.get(id).getType();
	}

	/**
	 * returns the value of an id in the table. retrieving the value of an id
	 * not in the table raises an exception
	 * 
	 * @param id
	 *            the id to check
	 * @return the value of the symbol with this id
	 */
	public int getValue(String id) throws SymbolException {
		if (!exists(id))
			throw new SymbolException(
					Messages.getString("am0c0", "SymbolTable.10")); //$NON-NLS-1$

		return symbols.get(id).getValue();
	}

	/**
	 * returns the {@link Ram} address of the {@link Symbol} id in the table
	 * 
	 * @param id
	 *            identifier of the {@link Symbol}
	 * @return the {@link Ram} address of the {@link Symbol}
	 * @throws SymbolException
	 */
	public int getAddress(String id) throws SymbolException {
		if (!exists(id))
			throw new SymbolException(
					Messages.getString("am0c0", "SymbolTable.11")); //$NON-NLS-1$

		return symbols.get(id).getAddress();
	}
}
