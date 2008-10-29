package org.jalgo.module.app.coreTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.jalgo.module.app.core.dataType.Infinity;
import org.jalgo.module.app.core.dataType.Operation;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalAdd;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalMaximum;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalMinimum;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalMultiply;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalNumber;
import org.junit.Before;
import org.junit.Test;

public class RationalNumberTest {
	RationalNumber rEmpty,rZero,rStandard,rNegative,rInfinity;
	
	@Before
	public void setUp() { 
		rEmpty = new RationalNumber();
		rZero = new RationalNumber(0);
		rStandard = new RationalNumber(7.2f);
		rNegative = new RationalNumber();
		rInfinity = new RationalNumber(Infinity.POSITIVE_INFINITE);
	}
	
	/**
	 * Check if the data type has at least 2 Operations.
	 */
	@Test
	public void checkNumberOfOperations() {
		assertNotNull(RationalNumber.getOperations());
		assertNotSame(0,RationalNumber.getOperations().size());
		assertNotSame(1,RationalNumber.getOperations().size());
	}
	
	@Test
	public void checkIntPositiveNumber() {
		assertEquals(0,rEmpty.toInt());
		assertEquals(0,rZero.toInt());
		assertEquals(7,rStandard.toInt());
	}
	
	@Test
	public void createNegativeNumber() {
		assertTrue(rNegative.setFromInt(-7));
		assertTrue(rNegative.setFromFloat(-7));
		assertTrue(rNegative.setFromString("-7"));
		assertTrue(rNegative.setFromNumeric(new RationalNumber(-7.7f)));
	}
	
	@Test
	public void setFromFloat() {
		rEmpty.setFromFloat(1.0f);
		assertEquals(1.0f,rEmpty.toFloat());
		rEmpty.setFromFloat(0.1f);
		assertEquals(0.1f,rEmpty.toFloat());
		rEmpty.setFromFloat(1.9f);
		assertEquals(1.9f,rEmpty.toFloat());
	}
	
	@Test
	public void setFromInt() {
		rEmpty.setFromInt(1);
		assertEquals(1.0f,rEmpty.toFloat());
		rEmpty.setFromInt(0);
		assertEquals(0.0f,rEmpty.toFloat());
	}
	
	@Test
	public void setFromNumericNumber() {
		rEmpty.setFromNumeric(new RationalNumber(1.1f));
		assertEquals(1,rEmpty.toInt());
		rEmpty.setFromNumeric(new RationalNumber());
		assertEquals(0,rEmpty.toInt());
		rEmpty.setFromNumeric(new RationalNumber(1.9f));
		assertEquals(1,rEmpty.toInt());
	}
	
	@Test
	public void setFromString() {
		rEmpty.setFromString("3");
		assertEquals(3.0f,rEmpty.toFloat());
	}
	
	@Test
	public void setWrongStringInput1() {
		assertFalse(rEmpty.setFromString("aba"));
	}
	
	@Test
	public void setWrongStringInput2() {
		assertFalse(rEmpty.setFromString("2.3ts"));
	}
	
	
	@Test
	public void setToTypes() {
		rEmpty.setFromFloat(3.4f);
		assertEquals(3,rEmpty.toInt());
		assertEquals(3.4f,rEmpty.toFloat());
		assertEquals("3.4",rEmpty.toString());
	}
	
	@Test
	public void addNumbers() {
		rEmpty.setFromFloat(2.5f);
		rZero.setFromFloat(3.5f);
		for (Operation op : RationalNumber.getOperations()) {
			if (op instanceof RationalAdd) {	
				assertEquals(new RationalNumber(6.0f),op.op(rEmpty, rZero));
			} else if (op instanceof RationalMultiply) {
				assertEquals(new RationalNumber(8.75f),op.op(rEmpty, rZero));
			} else if (op instanceof RationalMinimum) {
				assertEquals(new RationalNumber(2.5f),op.op(rEmpty, rZero));
			} else if (op instanceof RationalMaximum) {
				assertEquals(new RationalNumber(3.5f),op.op(rEmpty, rZero));
			}
		}
	}
}
