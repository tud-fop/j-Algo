package org.jalgo.module.app.coreTest;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jalgo.module.app.core.dataType.Infinity;
import org.jalgo.module.app.core.dataType.Operation;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalAdd;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalMaximum;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalMinimum;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalMultiply;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalNumber;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalOperation;
import org.junit.Before;
import org.junit.Test;

public class RationalStarOperationTest {
	RationalNumber rZero, rOne, rNInf, rPInf;
	RationalOperation add, mult, min, max;
	List<RationalNumber> rItems;

	@Before
	public void setUp() {
		Set<Operation> ops;

		ops = RationalNumber.getOperations();
		for (Operation op : ops) {
			if (op instanceof RationalAdd)
				add = (RationalAdd) op;
			if (op instanceof RationalMultiply)
				mult = (RationalMultiply) op;
			if (op instanceof RationalMinimum)
				min = (RationalMinimum) op;
			if (op instanceof RationalMaximum)
				max = (RationalMaximum) op;
		}

		rItems = new ArrayList<RationalNumber>();
		// Regular numbers
		rItems.add(new RationalNumber(16.92f));
		rItems.add(new RationalNumber(39.55f));
		rItems.add(new RationalNumber(37.47f));
		rItems.add(new RationalNumber(123.23f));
		rItems.add(new RationalNumber(-16.92f));
		rItems.add(new RationalNumber(-39.55f));
		rItems.add(new RationalNumber(-37.47f));
		rItems.add(new RationalNumber(-123.23f));
		
		// Small numbers close to or lesser than 1
		rItems.add(new RationalNumber(1.01f));
		rItems.add(new RationalNumber(0.23f));
		rItems.add(new RationalNumber(0.01f));
		rItems.add(new RationalNumber(0.99f));
		rItems.add(new RationalNumber(-1.01f));
		rItems.add(new RationalNumber(-0.23f));
		rItems.add(new RationalNumber(-0.01f));
		rItems.add(new RationalNumber(-0.99f));
		
		// Standard numbers
		rItems.add(new RationalNumber(0.0f));
		rItems.add(new RationalNumber(1.0f));
		rItems.add(new RationalNumber(2.0f));
		rItems.add(new RationalNumber(-1.0f));
		rItems.add(new RationalNumber(-2.0f));

		// Fixed values
		rZero = new RationalNumber(0f);
		rOne = new RationalNumber(1f);
		rNInf = new RationalNumber(Infinity.NEGATIVE_INFINITE);
		rPInf = new RationalNumber(Infinity.POSITIVE_INFINITE);
	}

	@Test
	public void starWithAdd() {
		for (RationalNumber item : rItems) {
			assertEquals("+min "+item, rPInf, add.star(item, min));
			assertEquals("+max "+item, rNInf, add.star(item, max));
			
			if (item.toFloat() > 0) {
				assertEquals("++ "+item, rPInf, add.star(item, add));
				assertEquals("+* "+item, rPInf, add.star(item, mult));
			}
			else if (item.toFloat() == 0) {
				assertEquals("++ "+item, rZero, add.star(item, add));
				assertEquals("+* "+item, rOne, add.star(item, mult));
			}
			else { // (item.toFloat() < -0)
				assertEquals("++ "+item, rNInf, add.star(item, add));
				
				try {
					assertEquals("+* "+item, null, add.star(item, mult));
				}
				catch (UnsupportedOperationException e) {}
			}
		}

		assertEquals("++ "+rPInf, rPInf, add.star(rPInf, add));
		assertEquals("+* "+rPInf, rPInf, add.star(rPInf, mult));
		assertEquals("+min "+rPInf, rPInf, add.star(rPInf, min));

		assertEquals("++ "+rNInf, rNInf, add.star(rNInf, add));
		assertEquals("+max "+rNInf, rNInf, add.star(rNInf, max));
	}

	@Test
	public void starWithMult() {
		for (RationalNumber item : rItems) {
			assertEquals("*min "+item, rPInf, mult.star(item, min));
			assertEquals("*max "+item, rNInf, mult.star(item, max));
			assertEquals("*+ "+item, rZero, mult.star(item, add));
			
			if (item.toFloat() > 1) {
				assertEquals("** "+item, rPInf, mult.star(item, mult));
			}
			else if (item.toFloat() == 1) {
				assertEquals("** "+item, rOne, mult.star(item, mult));
			}
			else if (item.toFloat() > 0) {
				assertEquals("** "+item, rZero, mult.star(item, mult));
			}
			else if (item.toFloat() == 0) {
				assertEquals("** "+item, rZero, mult.star(item, mult));
			}
			else {
				try {
					assertEquals("** "+item, null, mult.star(item, mult));
				}
				catch (UnsupportedOperationException e) {}
			}
		}
	}

	@Test
	public void starWithMin() {
		for (RationalNumber item : rItems) {
			assertEquals("min min "+item, item, min.star(item, min));
			assertEquals("min max "+item, rNInf, min.star(item, max));
			
			if (item.toFloat() >= 1) {
				assertEquals("min+ "+item, rZero, min.star(item, add));
				assertEquals("min* "+item, rOne, min.star(item, mult));
			}
			else if (item.toFloat() > 0) {
				assertEquals("min+ "+item, rZero, min.star(item, add));
				assertEquals("min* "+item, rZero, min.star(item, mult));
			}
			else if (item.toFloat() == 0) {
				assertEquals("min+ "+item, rZero, min.star(item, add));
				assertEquals("min* "+item, rZero, min.star(item, mult));
			}
			else if (item.toFloat() >= -1) {
				assertEquals("min+ "+item, rNInf, min.star(item, add));
				assertEquals("min* "+item, item, min.star(item, mult));
			}
			else {
				assertEquals("min+ "+item, rNInf, min.star(item, add));

				try {
					assertEquals("min* "+item, null, min.star(item, mult));
				}
				catch (UnsupportedOperationException e) {}
			}
		}

	}

	@Test
	public void starWithMax() {
		for (RationalNumber item : rItems) {
			assertEquals("max min "+item, rPInf, max.star(item, min));
			assertEquals("max max "+item, item, max.star(item, max));

			if (item.toFloat() > 1) {
				assertEquals("max+ "+item, rPInf, max.star(item, add));
				assertEquals("max* "+item, rPInf, max.star(item, mult));
			}
			else if (item.toFloat() > 0) {
				assertEquals("max+ "+item, rPInf, max.star(item, add));
				assertEquals("max* "+item, rOne, max.star(item, mult));
			}
			else if (item.toFloat() == 0) {
				assertEquals("max+ "+item, rZero, max.star(item, add));
				assertEquals("max* "+item, rOne, max.star(item, mult));
			}
			else if (item.toFloat() >= -1) {
				assertEquals("max+ "+item, rZero, max.star(item, add));
				assertEquals("max* "+item, rOne, max.star(item, mult));
			}
			else {
				assertEquals("max+ "+item, rZero, max.star(item, add));
				
				try {
					assertEquals("max* "+item, null, max.star(item, mult));
				}
				catch (UnsupportedOperationException e) {}
			}
		}
	}
}
