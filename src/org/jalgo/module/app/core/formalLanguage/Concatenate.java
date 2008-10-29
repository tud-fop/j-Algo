package org.jalgo.module.app.core.dataType.formalLanguage;

import java.util.List;

import org.jalgo.module.app.core.dataType.DataType;
import org.jalgo.module.app.core.dataType.Operation;


public class Concatenate extends Operation {
	
	private static final long serialVersionUID = -803641693937107202L;

	public String getID() {
		return "concat";
	}

	public String getName() {
		return "Concatenate";
	}

	public String getSymbolicRepresentation() {
		return "\u2218";
	}
	
	public Notation getNotation() {
		return Notation.INFIX;
	}

	public DataType getNeutralElement() {
		return new FormalLanguage("\u03b5");
	}
	
	/**
	 * @see Operation#getNeutralElementDescription()
	 */
	public String getNeutralElementDescription() {
		return "{"+getNeutralElement().toString()+"}";
	}		

	public DataType getAbsorbingElement() {
		return new FormalLanguage();
	}

	public boolean isAssociative() {
		return true;
	}

	public boolean isCommutative() {
		//not commutative !!!
		return false;
	}

	public DataType op(DataType arg1, DataType arg2) {
		FormalLanguage first, second;
		String result;
		
		first = (FormalLanguage)arg1;
		second = (FormalLanguage)arg2;
		
		if (first.isEmpty() || second.isEmpty())
			return getAbsorbingElement();
		
		if (first.toString().equals("\u03b5"))
			result = second.toString();
		else if (second.toString().equals("\u03b5"))
			result = first.toString();
		else
			result = first.toString()+second.toString();
		
		return new FormalLanguage(result);
	}

	public DataType op(List<DataType> args) {
		DataType newSet = getNeutralElement();
		
		for (DataType dt : args) {
			newSet = op(newSet, dt);
		}
		
		return newSet;
	}

	public DataType star(DataType arg, Operation other) {
		throw new UnsupportedOperationException();
	}

}
