package org.jalgo.module.app.core.dataType.rationalNumber;

import java.util.ArrayList;
import java.util.List;

import org.jalgo.module.app.core.dataType.DataType;
import org.jalgo.module.app.core.dataType.Infinity;
import org.jalgo.module.app.core.dataType.Operation;


public abstract class RationalOperation extends Operation {
	
	Class<? extends RationalNumber> numClass;
	
	public RationalOperation(Class<? extends RationalNumber> aClass) {
		numClass = aClass;
	}
	
	protected DataType newNumber(float value) {
		try {
			return numClass.getConstructor(new Class[] {float.class}).newInstance(new Object[] {value});
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	protected DataType newNumber(Infinity inf) {
		try {
			return numClass.getConstructor(new Class[] {Infinity.class}).newInstance(new Object[] {inf});
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public boolean isAssociative() {
		return true;
	}

	public boolean isCommutative() {
		return true;
	}

	public DataType star(DataType arg, Operation other) {
		RationalNumber v1, v2, v3;
		
		v1 = (RationalNumber) star(arg, other, 1);
		v2 = (RationalNumber) star(arg, other, 2);
		v3 = (RationalNumber) star(arg, other, 3);
		
		// Constant value
		if (v1.equals(v2) && v2.equals(v3))
			return v1;
		else if (v1.toFloat() < v2.toFloat() && v2.toFloat() < v3.toFloat()) {
			if (v1.toFloat() >= 0)
				return newNumber(Infinity.POSITIVE_INFINITE);
			else if (v1.toFloat() >= -1 && v1.toFloat() < 0 && v2.toFloat() < 0 && v3.toFloat() < 0)
				return newNumber(0.0f);
			else
				throw new UnsupportedOperationException();
		}
		else if (v1.toFloat() > v2.toFloat() && v2.toFloat() > v3.toFloat()) {
			if (v1.toFloat() <= 0)
				return newNumber(Infinity.NEGATIVE_INFINITE);
			else if (v1.toFloat() <= 1 && v1.toFloat() > 0 && v2.toFloat() > 0 && v3.toFloat() > 0)
				return newNumber(0.0f);
			else
				throw new UnsupportedOperationException();
		}
		else
			throw new UnsupportedOperationException();
	}

	protected DataType star(DataType arg, Operation other, int count) {
		List<DataType> ops1, ops2;
		
		ops1 = new ArrayList<DataType>();
		ops2 = new ArrayList<DataType>();
		
		for (int i=0; i<=count; i++) {
			ops1.add(other.op(ops2));
			ops2.add(arg);
		}
		
		return op(ops1);
	}
}
