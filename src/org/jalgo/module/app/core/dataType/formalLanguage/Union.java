package org.jalgo.module.app.core.dataType.formalLanguage;

import java.util.List;

import org.jalgo.module.app.core.dataType.DataType;
import org.jalgo.module.app.core.dataType.Operation;


public class Union extends Operation {
	
	private static final long serialVersionUID = -102320253876107809L;

	public String getID() {
		return "union";
	}

	public String getName() {
		return "Union";
	}

	public String getSymbolicRepresentation() {
		return "\u222A";
	}
	
	public Notation getNotation() {
		return Notation.INFIX;
	}

	public DataType getNeutralElement() {
		return new FormalLanguage();
	}
	
	public DataType getAbsorbingElement() {
		return null;
	}

	public boolean isAssociative() {
		return true;
	}

	public boolean isCommutative() {
		return true;
	}

	public DataType op(DataType arg1, DataType arg2) {
		FormalLanguage first, second;
		String result;
		
		first = (FormalLanguage)arg1;
		second = (FormalLanguage)arg2;
		
		if (first.isEmpty())
			return second;
		if (second.isEmpty())
			return first;
		
		result = "("+first.toString()+"+"+second.toString()+")";
		return new FormalLanguage(result);
	}

	public DataType op(List<DataType> args) {
		String result;
		
		result = "(";
		for (DataType dt : args) {
			if (!((FormalLanguage)dt).isEmpty())
				result += (FormalLanguage)dt + "+";	
		}
		
		if (result.length() > 1)
			result = result.substring(0, result.length()-1);
		result += ")";
		
		return new FormalLanguage(result);
	}

	/**
	 * The star operation on formal languages.
	 */
	public DataType star(DataType arg, Operation other) {
		FormalLanguage lang;
		
		lang = (FormalLanguage)arg;
		
		if (lang.isEmpty()) {
			return new FormalLanguage("\u03b5");
		}
		
		String result = "("+lang+")*";
		return new FormalLanguage(result);
	}

}
