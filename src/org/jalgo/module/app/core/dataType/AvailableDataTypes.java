package org.jalgo.module.app.core.dataType;

import java.util.HashSet;
import java.util.Set;

import org.jalgo.module.app.core.dataType.booleanType.BooleanType;
import org.jalgo.module.app.core.dataType.formalLanguage.FormalLanguage;
import org.jalgo.module.app.core.dataType.naturalNumber.NaturalNumber;
import org.jalgo.module.app.core.dataType.rationalNumber.PercentageType;
import org.jalgo.module.app.core.dataType.rationalNumber.PositiveRationalNumber;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalNumber;

/**
 * Provides static methods to get supported Data Types.
 * 
 */
public class AvailableDataTypes {

	private static Set<Class<? extends DataType>> dataTypes = null;

	/**
	 * Returns all available <code>DataType</code>s as a set of classes.
	 * 
	 * @return the available <code>DataType</code>s.
	 */
	public static Set<Class<? extends DataType>> getDataTypes() {
		if (dataTypes == null) {
			dataTypes = new HashSet<Class<? extends DataType>>();

			dataTypes.add(BooleanType.class);
			dataTypes.add(FormalLanguage.class);
			dataTypes.add(NaturalNumber.class);
			dataTypes.add(RationalNumber.class);
			dataTypes.add(PositiveRationalNumber.class);
			dataTypes.add(PercentageType.class);
		}

		return dataTypes;
	}
}
