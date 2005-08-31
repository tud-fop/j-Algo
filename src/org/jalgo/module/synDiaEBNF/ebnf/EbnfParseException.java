/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * Created on 08.06.2004
 */
package org.jalgo.module.synDiaEBNF.ebnf;

import org.jalgo.main.util.Messages;

/**
 * this is a new <code>Exception</code> for the EBNF parser
 * 
 * @author Stephan Creutz
 */
public class EbnfParseException extends Exception {

	private static final long serialVersionUID = -7652140753340833433L;

	public EbnfParseException(String message) {
		super(message);
	}

	public EbnfParseException(String message, int position) {
		super(message + Messages.getString("synDiaEBNF",
			"EbnfParseException.._char___1") + position); //$NON-NLS-1$
	}
}