package org.jalgo.module.app.core;

import java.io.Serializable;

import org.jalgo.module.app.core.dataType.DataType;
import org.jalgo.module.app.core.dataType.Operation;

/**
 * Represents a <code>SemiRing</code>, which is a algebraic structure
 * consisting of a <code>DataType</code>, two <code>Operation</code>s (and
 * their neutral elements, which are stored in the corresponding
 * <code>Operation</code>).
 * 
 */
public class SemiRing implements Serializable, Comparable<SemiRing> {

	/**
	 * important for load/save operations.
	 */
	private static final long serialVersionUID = -8445924133073074815L;
	private Operation plus;
	private Operation dot;
	private Class<? extends DataType> type;

	// e.g. Shortest Path Problem
	private String name;

	// description referring to the Problem
	private String description;

	// tuple containing Set, operations and their neutral elements
	private String definition;
	private String[] typeRepresentation;

	/**
	 * Constructs a Semiring from a <code>DataType</code> class and two
	 * <code>Operation</code>s.
	 * 
	 * @param type
	 *            a class inheriting from <code>DataType</code>.
	 * @param plus
	 *            the additive <code>Operation</code>.
	 * @param dot
	 *            the multiplicative <code>Operation</code>.
	 */
	public SemiRing(Class<? extends DataType> type, Operation plus,
			Operation dot) {
		this.type = type;
		this.plus = plus;
		this.dot = dot;
		this.name = "No Name";
	}

	public Operation getPlusOperation() {
		return plus;
	}

	public void setPlusOperation(Operation plus) {
		this.plus = plus;
	}

	public Operation getDotOperation() {
		return dot;
	}

	public void setDotOperation(Operation dot) {
		this.dot = dot;
	}

	public Class<? extends DataType> getType() {
		return type;
	}

	public void setType(Class<? extends DataType> type) {
		this.type = type;
	}

	/**
	 * @return the name (e.g. Shortest Path Problem).
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the description of the the Semiring (e.g..a Non-formal description
	 * of the Shortest Path Problem).
	 * 
	 * @return the description of the Semiring.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 *            the description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public void setTypeRepresentation(String[] representation) {
		this.typeRepresentation = representation;
	}
	
	public String[] getTypeRepresentation() {
		return this.typeRepresentation;
	}
	
	/**
	 * 
	 * @return a formal tuple containing the Set, the two Operations and their
	 *         neutral elements.
	 */
	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(SemiRing compareSemiring) {
		if (compareSemiring == null) {
			throw new NullPointerException();
		}
		return this.getName().compareTo(compareSemiring.getName());
	}
}
