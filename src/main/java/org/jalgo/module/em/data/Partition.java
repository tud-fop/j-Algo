package org.jalgo.module.em.data;

import java.util.Set;
import java.util.TreeSet;

/**
 * Data object representing a partition of the experiment's event set (a class of a partition).
 * <p> A <code>Partition</code> consists of a set of <code>Event</code>s and a frequency, which is
 * a real number greater or equal 0.
 * 
 * @author Tobias Nett
 *
 */
public class Partition {
	
	private String name;
	private double frequency = 0;
	private Set<Event> elements;

	/**
	 * Creates a new, empty Partition object.
	 */
	public Partition(){
		elements = new TreeSet<Event>();
		name = "";
	}
	
	/**
	 * Creates a new Partition which consists of all Events in <code>elements</code>. 
	 * All the Events are yielded to this partition.
	 * 
	 * @param elements Set of Events which belong to this partition
	 */
	public Partition(Set<Event> elements) {
		if (elements == null)
			throw new NullPointerException("null argument(s)");
		this.elements = new TreeSet<Event>(elements);
		
		this.name = "";
		if (elements != null)
			yieldEvents();
	}

	/**
	 * Creates a new Partition which consists of all Events in <code>elements</code>. 
	 * The Partition's name is set to <code>name</name> (e.g. "two time head").
	 * All the Events are yielded to this partition.
	 * 
	 * @param elements Set of Events which belong to this partition
	 * @param name Name of the Partition
	 */
	public Partition(Set<Event> elements, String name) {
		if (name == null || elements == null)
			throw new NullPointerException("null argument(s)");
		this.elements = new TreeSet<Event>(elements);
		this.name = name;
		
		if (elements != null)
			yieldEvents();
	}
	
	/**
	 * Yields this partition to every Event.
	 */
	private void yieldEvents(){
		for (Event e : elements) {
			e.setYield(this);
		}
	}
	
	/**
	 * Adds an Event to this partition and sets the yield of the Event.
	 * 
	 * @param e Event to add
	 */
	public void addElement(Event e){
		if (elements == null)
			elements = new TreeSet<Event>();
		elements.add(e);
		e.setYield(this);
	}
	
	/**
	 * Removes the Event from the partition
	 * 
	 * @param e Event to remove
	 */
	public void removeElement(Event e){
		if (elements != null)
			elements.remove(e);
	}
	
	/**
	 * Clears the yield of every Event. Should be called before deleting the partition.
	 */
	public void remove(){
		for (Event e : elements) {
			e.clearYield();
		}
	}
	
	/**
	 * Returns a mathematical string representation of this partition in the form
	 * "{(1,1), (1,2), ... }".
	 * 
	 * @return mathematical representation of this partition
	 */
	public String getMathSet(){
		StringBuilder out = new StringBuilder("{ ");
		for (Event event : elements) {
			out.append(event.toString());
			out.append(", ");
		}
		out.deleteCharAt(out.lastIndexOf(", "));
		out.append("}");
		return out.toString();
	}

	/**
	 * Returns the name of the Partition, which should be better human-readable than the mathematical 
	 * representation of the element set, e.g. "3-times head" or "Sum: 4".
	 * 
	 * 
	 * @return the partition's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the partition to the given value. 
	 * 
	 * @param name the partition's name
	 */
	public void setName(String name) {
		if (name == null)
			throw new NullPointerException("no null arguments allowed");
		this.name = name;		
	}

	/**
	 * Get the partition's frequency, which often means a natural number at the beginning of an experiment. 
	 * 
	 * @return the frequency - a real number >= 0
	 */
	public double getFrequency() {
		return frequency;
	}

	/**
	 * 
	 * @param frequency the frequency to set
	 * 
	 * @throws IllegalArgumentException is raised when the argument is < 0 
	 */
	public void setFrequency(double frequency) throws IllegalArgumentException {
		if (frequency < 0)
			throw new IllegalArgumentException("Negative frequency");
		this.frequency = frequency;
	}

	/**
	 * Return a Set of all Events that belong to this Partition. <code>null</code> is returned 
	 * if there are no elements in this partition. 
	 * 
	 * @return all elements of this partition
	 */
	public Set<Event> getElements(){
		return elements;
	}
	
	/**
	 * Generates a String "{ (x<sub>1</sub>, x<sub>2</sub>,...),(x<sub>1</sub>, x<sub>2</sub>,...),..}", 
	 * where (x<sub>1</sub>, x<sub>2</sub>,...) is a Event of the partition.  
	 * 
	 * @return The String Representation of the Partition.
	 */
	@Override
	public String toString(){
		//if there are no elements in this partition
		if (elements == null || elements.isEmpty())
			return "empty";
		// otherwise, generate a mathematical representation of its Event set.
		//StringBuilder out = new StringBuilder("{ ");
		StringBuilder out = new StringBuilder("<html><b>"+name+"</b><br>");
		out.append(getMathSet());
		out.append("</html>");
		return out.toString();	
	}
}
