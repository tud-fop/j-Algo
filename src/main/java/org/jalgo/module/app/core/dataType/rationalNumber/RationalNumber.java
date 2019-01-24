package org.jalgo.module.app.core.dataType.rationalNumber;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.jalgo.module.app.core.dataType.Infinity;
import org.jalgo.module.app.core.dataType.NumericDataType;
import org.jalgo.module.app.core.dataType.Operation;
import org.jalgo.module.app.core.dataType.MaxValues;


public class RationalNumber extends NumericDataType {

	private static final long serialVersionUID = 6771153631961177659L;

	public static Set<Operation> getOperations() {
		Set<Operation> operations;
		
		operations = new HashSet<Operation>();

		operations.add(new RationalAdd(RationalNumber.class));
		operations.add(new RationalMultiply(RationalNumber.class));
		operations.add(new RationalMinimum(RationalNumber.class));
		operations.add(new RationalMaximum(RationalNumber.class));

		return operations;
	}
	
	public static Operation getOperationByID(String id) {
		for (Operation op : getOperations()) {
			if (op.getID().equals(id))
				return op;
		}
		
		return null;
	}

	public static String getName() {
		return "Rational Number";
	}

	public static String[] getSymbolicRepresentation() {
		String[] returnValue = {"\u211d", "", ""};
	 
		return returnValue;
	}
	
	protected boolean isValidValue(float value) {
		return (value >= minimumValue() && value <= maximumValue());
	}
	
	protected float defaultValue() {
		return 0f;
	}
	
	protected float minimumValue() {
		return Float.NEGATIVE_INFINITY;
	}
	
	protected float maximumValue() {
		return Float.POSITIVE_INFINITY;
	}

	/**
	 * INSTANCE METHODES
	 */

	private float value;

	public RationalNumber()
	{
		value = defaultValue();
	}

	public RationalNumber(float val)
	{
		if (isValidValue(val)) {
			value = val;
		} else {
			throw new IllegalArgumentException();
		}
	}

	public RationalNumber(Infinity inf)
	{
		setInfinity(inf);
	}

	@Override
	public boolean setFromFloat(float val) {
		if (isValidValue(val)) {
			value = val;
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean setFromInt(int val) {
		if (isValidValue(val)) {
			value = val;
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean setFromNumeric(NumericDataType num) {
		Infinity inf;
		float val;

		val = num.toFloat();
		inf = num.getInfinity();

		if (inf != Infinity.REAL) {
			return setInfinity(inf);
		}

		if (isValidValue(val)) {
			value = val;
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public float toFloat() {
		return value;
	}

	@Override
	public int toInt() {
		return (int) value;
	}

	@Override
	public RationalNumber clone() {
		RationalNumber num = null;
		
		try {
			num = this.getClass().newInstance();
			num.setFromNumeric(this);
		}
		catch (InstantiationException e) {
		}
		catch (IllegalAccessException e) {
		}
		
		return num;
	}
	
	@Override
	public boolean setFromString(String str) {
		float val;

		// Input is infinite
		if (str.equals("\u221e")) {
			setInfinity(Infinity.POSITIVE_INFINITE);
			return true;
		}
		if (str.equals("-\u221e")) {
			setInfinity(Infinity.NEGATIVE_INFINITE);
			return true;
		}

		if (str.matches("[0-9]+\\,?[0-9]+")) {
			str = str.replace(',', '.'); 
		}
		
		// Input is real
		try {
			val = Float.parseFloat(str);
		}
		catch (NumberFormatException ex) {
			return false;
		}

		if (isValidValue(val)
			&& val >= MaxValues.getRationalNumberMinimum()
			&& val <= MaxValues.getRationalNumberMaximum()
		    ) {
			value = val;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		switch (getInfinity()) {
			case POSITIVE_INFINITE:
				return "\u221e";
			case NEGATIVE_INFINITE:
				return "-\u221e";
			default:
				return (new DecimalFormat("#####0.##", new DecimalFormatSymbols(new Locale("en")))).format(value);
		}
	}

	@Override
	public Infinity getInfinity() {
		if (value == Float.POSITIVE_INFINITY)
			return Infinity.POSITIVE_INFINITE;
		else if (value == Float.NEGATIVE_INFINITY)
			return Infinity.NEGATIVE_INFINITE;
		else
			return Infinity.REAL;
	}

	@Override
	public boolean setInfinity(Infinity inf) {
		float val;
		
		if (inf == Infinity.POSITIVE_INFINITE)
			val = maximumValue();
		else if (inf == Infinity.NEGATIVE_INFINITE)
			val = minimumValue();
		else
			val = defaultValue();
		
		value = val;
		return true;
	}

	public String[] getSpecialCharacter() {
		String[] out = {"\u221e"};
		
		return out;
	}	
}
