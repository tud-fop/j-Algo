/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and
 * platform independant. j-Algo is developed with the help of Dresden
 * University of Technology.
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
 * Created on 03.10.2005
 */
package org.jalgo.module.synDiaEBNF;

/**
 * @author Stephan Creutz
 */
public enum ModeEnum {
	NO_MODE_SET,
	START_WITH_WIZARD,
	NORMAL_VIEW_EMPTY,
	CREATE_SYNDIA,
	EBNF_INPUT,
	TRANS_ALGO,
	RECOGNIZE_WORD_ALGO,
	GENERATE_WORD_ALGO,
	NORMAL_VIEW_EBNF,
	NORMAL_VIEW_SYNDIA
}
