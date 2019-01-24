/**
 * AM1 Simulator - simulating am1 code in an abstract machine based on the
 * definitions of the lectures 'Programmierung' at TU Dresden.
 * Copyright (C) 2010 Max Leuthäuser
 * Contact: s7060241@mail.zih.tu-dresden.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jalgo.module.am1simulator.presenter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Class which carry update events used by the communication between presenter
 * and view in the MVP-Pattern.
 * 
 * @author Max Leuth&auml;user
 */
public class UpdateEvent<E> {
	List<E> updates;

	public UpdateEvent(E value) {
		updates = new LinkedList<E>();
		updates.add(value);
	}

	public List<E> getValues() {
		return updates;
	}

	/**
	 * @param arg0
	 * @param args never seen varargs? i lol'd
	 */
	public void setValues(E arg0, E... args) {
		updates.add(arg0);
		if (args != null && args.length > 0)
			updates.addAll(Arrays.asList(args));
	}
}
