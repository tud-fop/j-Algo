package org.jalgo.module.app.core.dataType;

import java.io.Serializable;
import java.util.List;

/**
 * A class, which allows the execution of an operation. It is also a container
 * for their neutral elements, their <code>Notation</code> and different
 * identifications.
 * 
 */
public abstract class Operation implements Serializable {

	/**
	 * Returns the ID of the <code>Operation</code>.
	 * 
	 * @return the ID of the <code>Operation</code>.
	 */
	public abstract String getID();

	/**
	 * Returns the name of the <code>Operation</code> (e.g. Addition,
	 * Conjunction).
	 * 
	 * @return the name of the <code>Operation</code>
	 */
	public abstract String getName();

	/**
	 * Returns the symbolic representation of the <code>Operation</code>,
	 * e.g. "+", " \u2227").
	 * 
	 * @return the symbolic representation of the <code>Operation</code>.
	 */
	public abstract String getSymbolicRepresentation();

	/**
	 * An Operation can either be prefix (e.g. min{a,b,..}), infix (e.g. a + b)
	 * or postfix (just for completeness).
	 * 
	 */
	public enum Notation {
		PREFIX, INFIX, POSTFIX
	};

	/**
	 * Returns the <code>Notation</code> used for this <code>Operation</code>.
	 * 
	 * @return the used <code>Notation</code>.
	 */
	public abstract Notation getNotation();

	/**
	 * Returns the neutral element of the operation or <code>null</code> if no
	 * such element exists.
	 * 
	 * A neutral element makes no change to a operations result.
	 * 
	 * @return the neutral element
	 */
	public abstract DataType getNeutralElement();

	/**
	 * Returns a descriptive string of the neutral element of the operation or <code>null</code> if no
	 * such element exists.
	 * 
	 * @return the description of neutral element
	 */
	public String getNeutralElementDescription() {
		return getNeutralElement().toString();
	}	
	
	/**
	 * Returns the absorbing element of the operation or <code>null</code> if
	 * no such element exists.
	 * 
	 * A absorbing element consumes every operation and so is always the result
	 * of an operation when it is included in the arguments.
	 * 
	 * @return the absorbing element
	 */
	public abstract DataType getAbsorbingElement();

	/**
	 * Returns <code>true</code> if the operation is associative,
	 * <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if operation is associative
	 */
	public abstract boolean isAssociative();

	/**
	 * Returns <code>true</code> if the operation is commutative,
	 * <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if operation is commutative
	 */
	public abstract boolean isCommutative();

	/**
	 * Performs the operation using two arguments.
	 * 
	 * @return the operation result
	 */
	public abstract DataType op(DataType arg1, DataType arg2);

	/**
	 * Performs the operation using a list of arguments.
	 * 
	 * @return the operation result
	 */
	public abstract DataType op(List<DataType> args);

	/**
	 * Performs the star operation using one argument.
	 * 
	 * @return the operation result
	 */
	public abstract DataType star(DataType arg, Operation other);
}
