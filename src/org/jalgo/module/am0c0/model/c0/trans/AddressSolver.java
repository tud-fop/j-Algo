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
import java.util.List;
import java.util.Map;

import org.jalgo.main.util.Messages;
import org.jalgo.module.am0c0.model.AddressException;
import org.jalgo.module.am0c0.model.LineAddress;
import org.jalgo.module.am0c0.model.TreeAddress;

/**
 * Address solver which creates the TreeAddress-to-LineAddress map
 * 
 * @author Felix Schmitt
 * 
 */
public class AddressSolver {

	/**
	 * maps {@link TreeAddress}es to {@link LineAddress}es
	 * 
	 * @param transFunctions
	 *            a List of {@link TransformFunction} with {@link TreeAddress}es
	 * @return a Map of {@link TreeAddress}-Strings to {@link LineAddress}es
	 * @throws NullPointerException
	 */
	public static Map<String, LineAddress> solve(List<TransformFunction> transFunctions)
			throws NullPointerException {
		Map<String, LineAddress> solved = new HashMap<String, LineAddress>();

		int line = 1;
		int index = 0;

		while (index < transFunctions.size()) {
			TransformFunction func = transFunctions.get(index);

			if (func.getAddress() != null)
				try {
					solved.put(func.getAddress().toString(), new LineAddress(line));
				} catch (AddressException e) {
					// there can't be an AddressException here (as long as line
					// does not overflow
				}

			if (func instanceof StubTrans) {
				if (func.getAddress() == null)
					throw new NullPointerException(Messages.getString("am0c0", "AddressSolver.0")); //$NON-NLS-1$
			} else {
				line++;
			}

			index++;
		}

		return solved;
	}
}
