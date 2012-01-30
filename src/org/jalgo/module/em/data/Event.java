package org.jalgo.module.em.data;

import java.util.Vector;

import org.jalgo.main.util.Messages;

/**
 * Represents an event, that consists of multiple SingleEvents.
 * @author Kilian Gebhardt
 * @author Tobias Nett
 */
public class Event implements Comparable<Event> {
	private Vector<Integer> tuple, experimentVector;
	private Partition yield;

	/**
	 * @param tuple
	 *            The tuple with SingleEvents.
	 * @param experimentVector The experimentVector.
	 */
	public Event(final Vector<Integer> tuple, final Vector<Integer> experimentVector) {
		this.tuple = tuple;
		this.yield = null;
		this.experimentVector = experimentVector;
	}

	/**
	 * @param tuple
	 *            The tuple with SingleEvents.
	 */
	public Event(final Vector<Integer> tuple) {
		this.tuple = tuple;
		this.yield = null;
		this.experimentVector = null;
	}

	/**
	 * @param yield
	 *            The Partition to which the event belongs.
	 */
	public final void setYield(final Partition yield) {
		this.yield = yield;
	}

	/**
	 * Clears this event's yield by setting it to <code>null</code>.
	 */
	public final void clearYield() {
		this.yield = null;
	}

	/**
	 * Generates a String "(x<sub>1</sub>,x<sub>2</sub>,x<sub>3</sub>,...)",
	 * where x<sub>i</sub> is either a number in case of a dice or "K" or "Z"
	 * (German) or "H" or "T" (English) in case of a coin.
	 * 
	 * @return The String Representation of the Event.
	 */
	@Override
	public final String toString() {
		StringBuilder output = new StringBuilder();
		output.append("(");
		for (int i = 0; i < tuple.size(); i++) {
			if (experimentVector != null){
				output.append((experimentVector.get(i) == 2)
						? ((tuple.get(i) == 1)
								? Messages.getString("em", "StartParameters.Tail") + ", "
								: Messages.getString("em", "StartParameters.Head") + ", ")
						: tuple.get(i) + ", ");
			} else {
				output.append(tuple.get(i) + ", ");
			}
		}
		output.delete(output.lastIndexOf(", "), output.length());
		output.append(")");
		return output.toString();
	}

	/**
	 * @return Partition of the Event.
	 */
	public final Partition getYield() {
		return yield;
	}

	/**
	 * @return Events tuple with SingleEvents
	 */
	public final Vector<Integer> getTuple() {
		return tuple;
	}

	/**
	 * Compares this event with the specified event for order. The tuple entries
	 * have a descending priority and the vector elements will be compared until
	 * they are not equal.
	 * 
	 * @param e
	 *            the Event to compare
	 * @return a negative integer, zero, or a positive integer as this object is
	 *         less than, equal to, or greater than the specified object.
	 */
	@Override
	public final int compareTo(final Event e) {
		// get the length of the shorter event
		int size = Math.min(e.getTuple().size(), this.tuple.size());
		int cmp = 0;
		for (int i = 0; i < size; i++) {
			cmp = this.tuple.get(i).compareTo(e.getTuple().get(i));
			if (cmp != 0) {
				break;
			}
		}
		return cmp;
	}

	@Override
	public final boolean equals(final Object e) {
		if (e instanceof Event)
			return ((Event) e).getTuple().equals(tuple);
		return false;
	}

	@Override
	public final int hashCode() {
		return tuple.hashCode();
	}

}
