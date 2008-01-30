/*
 * j-Algo - a visualization tool for algorithm runs, especially useful for
 * students and lecturers of computer science. j-Algo is written in Java and
 * thus platform independent. Development is supported by Technische Universität
 * Dresden.
 *
 * Copyright (C) 2004-2008 j-Algo-Team, j-algo-development@lists.sourceforge.net
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
package org.jalgo.module.heapsort.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>This interface is used for modelling algorithms and their runs.
 * We adopt some kind of reduction semantics, meaning that we
 * start in some initial state and iteratively derive successor
 * states until we reach a final state. Implementing classes
 * should offer the possibility to set the input data considered
 * in the run starting with the state returned as initial.</p>
 * 
 * <p>Implementing classes should be derived from
 * <code>Subject&lt;ModelListener&gt;</code> and notify listeners
 * of changes in the input data.</p>
 *  
 * @author mbue
 */
public interface Model {
	void deserialize(InputStream is) throws IOException;
	State getInitialState();
	void serialize(OutputStream os) throws IOException;
}
