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

/**
 * AST representation of the syntactic variable {@code Program}. A program
 * contains a {@code Block}.
 * 
 * @author Martin Morgenstern
 */
public class Program extends C0AST {
	private final Block block;
	private final String codeText;

	public Program(final Block block) {
		this.block = block;
		codeText = "\n#include <stdio.h>\n\nint main()\n" + block.getCodeText();
	}

	public Block getBlock() {
		return block;
	}

	@Override
	protected String getCodeTextInternal() {
		return codeText;
	}
}
