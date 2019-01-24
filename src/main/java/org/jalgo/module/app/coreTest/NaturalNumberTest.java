package org.jalgo.module.app.coreTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import org.jalgo.module.app.core.dataType.Infinity;
import org.jalgo.module.app.core.dataType.Operation;
import org.jalgo.module.app.core.dataType.naturalNumber.NaturalNumber;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalNumber;
import org.junit.Before;
import org.junit.Test;

public class NaturalNumberTest {
	NaturalNumber nEmpty,nZero,nStandard,nNegative,nInfinity;
	
	@Before
	public void setUp() { 
		nEmpty = new NaturalNumber();
		nZero = new NaturalNumber(0);
		nStandard = new NaturalNumber(7);
		nNegative = new NaturalNumber();
		nInfinity = new NaturalNumber(Infinity.POSITIVE_INFINITE);
	}
	
	/**
	 * Check if the data type has at least 2 Operations.
	 */
	@Test
	public void checkNumberOfOperations() {
		assertNotNull(NaturalNumber.getOperations());
		assertNotSame(0,NaturalNumber.getOperations().size());
		assertNotSame(1,NaturalNumber.getOperations().size());
	}
	
	@Test
	public void checkIntPositiveNumber() {
		assertEquals(0,nEmpty.toInt());
		assertEquals(0,nZero.toInt());
		assertEquals(7,nStandard.toInt());
		assertEquals(Integer.MAX_VALUE,nInfinity.toInt());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void createNegativeNumber() {
		new NaturalNumber(-7);
	}

	@Test
	public void setNegativeNumber() {
		assertFalse(nNegative.setFromInt(-7));
		assertFalse(nNegative.setFromFloat(-7));
		assertFalse(nNegative.setFromString("-7"));
		assertFalse(nNegative.setFromNumeric(new RationalNumber(-7.7f)));
	}
	
	@Test
	public void setFromFloat() {
		nEmpty.setFromFloat(1.0f);
		assertEquals(1,nEmpty.toInt());
		nEmpty.setFromFloat(0.1f);
		assertEquals(0,nEmpty.toInt());
		nEmpty.setFromFloat(1.9f);
		assertEquals(1,nEmpty.toInt());
	}
	
	@Test
	public void setFromInt() {
		nEmpty.setFromInt(1);
		assertEquals(1,nEmpty.toInt());
		nEmpty.setFromInt(0);
		assertEquals(0,nEmpty.toInt());
	}
	
	@Test
	public void setFromNumericNumber() {
		nEmpty.setFromNumeric(new RationalNumber(1.1f));
		assertEquals(1,nEmpty.toInt());
		nEmpty.setFromNumeric(new RationalNumber());
		assertEquals(0,nEmpty.toInt());
		nEmpty.setFromNumeric(new RationalNumber(1.9f));
		assertEquals(1,nEmpty.toInt());
	}
	
	@Test
	public void setFromString() {
		nEmpty.setFromString("3");
		assertEquals(3,nEmpty.toInt());
	}
	
	@Test
	public void setWrongStringInput1() {
		assertFalse(nEmpty.setFromString("aba"));
		
	}
	
	@Test
	public void setWrongStringInput2() {
		assertFalse(nEmpty.setFromString("-2"));
	}
	
	@Test
	public void setWrongStringInput3() {
		assertFalse(nEmpty.setFromString("2.3"));
	}
	
	
	@Test
	public void setToTypes() {
		nEmpty.setFromInt(3);
		assertEquals(3,nEmpty.toInt());
		assertEquals(3.0f,nEmpty.toFloat());
		assertEquals("3",nEmpty.toString());
	}
	
	@Test
	public void addNumbers() {
		nEmpty.setFromInt(2);
		nZero.setFromInt(3);
		for (Operation op : NaturalNumber.getOperations()) {
			if (op instanceof org.jalgo.module.app.core.dataType.naturalNumber.NaturalAdd) {	
				assertEquals(new NaturalNumber(5),op.op(nEmpty, nZero));
			} else if (op instanceof org.jalgo.module.app.core.dataType.naturalNumber.NaturalMultiply) {
				assertEquals(new NaturalNumber(6),op.op(nEmpty, nZero));
			}
		}
	}
}
