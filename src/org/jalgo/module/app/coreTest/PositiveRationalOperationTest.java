package org.jalgo.module.app.coreTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jalgo.module.app.core.dataType.DataType;
import org.jalgo.module.app.core.dataType.Infinity;
import org.jalgo.module.app.core.dataType.Operation;
import org.jalgo.module.app.core.dataType.rationalNumber.PositiveRationalNumber;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalAdd;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalMaximum;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalMinimum;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalMultiply;
import org.junit.Before;
import org.junit.Test;


public class PositiveRationalOperationTest {
	PositiveRationalNumber rEmpty,rZero,rOne,r5,r7,rNegative;
	// infinite
	PositiveRationalNumber pinf, ninf;
	RationalAdd add;
	RationalMultiply mult;
	RationalMinimum min;
	RationalMaximum max;
	
	List<DataType> list1,list2;
	
	@Before
	public void setUp() {
		Set<Operation> ops;
		
		ops = PositiveRationalNumber.getOperations();
		for (Operation op : ops) {
			if (op instanceof RationalAdd)
				add = (RationalAdd)op;
			if (op instanceof RationalMultiply)
				mult = (RationalMultiply)op;
			if (op instanceof RationalMinimum)
				min = (RationalMinimum)op;
			if (op instanceof RationalMaximum)
				max = (RationalMaximum)op;	
		}
		
		rEmpty = new PositiveRationalNumber();
		rZero = new PositiveRationalNumber(0);
		rOne = new PositiveRationalNumber(1.0f);
		pinf = new PositiveRationalNumber(Infinity.POSITIVE_INFINITE);
		ninf = new PositiveRationalNumber(Infinity.NEGATIVE_INFINITE);
		r5 = new PositiveRationalNumber(5.2f);
		r7 = new PositiveRationalNumber(7.2f);
		rNegative = new PositiveRationalNumber();
		
		list1 = new LinkedList<DataType>();
		list2 = new LinkedList<DataType>();
		
	}
	
	@Test
	public void checkClosure() {
		assertTrue((add.op(rEmpty, rEmpty).getClass() == PositiveRationalNumber.class));
		assertTrue((mult.op(rEmpty, rEmpty).getClass() == PositiveRationalNumber.class));
		assertTrue((min.op(rEmpty, rEmpty).getClass() == PositiveRationalNumber.class));
		assertTrue((max.op(rEmpty, rEmpty).getClass() == PositiveRationalNumber.class));
		assertTrue((rEmpty.clone().getClass() == PositiveRationalNumber.class));
	}
	
	@Test
	public void checkInvalidNumbers() {
		assertFalse(rNegative.setFromFloat(-2.2f));
	}
	@Test
	public void twoNumbers() {
			assertEquals(new PositiveRationalNumber(12.4f),add.op(r5,r7));
			assertEquals(new PositiveRationalNumber(12.4f),add.op(r7,r5));
			assertEquals(new PositiveRationalNumber(0.0f),add.op(rEmpty, rZero));
			
			assertEquals(new PositiveRationalNumber(37.44f),mult.op(r5,r7));
			assertEquals(new PositiveRationalNumber(37.44f),mult.op(r7,r5));
			assertEquals(new PositiveRationalNumber(0.0f),mult.op(rEmpty, rZero));
	
			assertEquals(new PositiveRationalNumber(5.2f),min.op(r5,r7));
			assertEquals(new PositiveRationalNumber(0),min.op(rZero,r5));
			assertEquals(new PositiveRationalNumber(0.0f),min.op(rEmpty, rZero));

			assertEquals(new PositiveRationalNumber(7.2f),max.op(r5,r7));
			assertEquals(new PositiveRationalNumber(7.2f),max.op(r7,rZero));
			assertEquals(new PositiveRationalNumber(0.0f),max.op(rEmpty, rZero));
	}
	
	@Test
	public void listOfNumbers1() {
		list1.add(r5);
		list1.add(r7);
		list1.add(rZero);
		
		list2.add(r5);
		list2.add(r7);
		list2.add(rOne);
		
		assertEquals(new PositiveRationalNumber(12.4f),add.op(list1));
		assertEquals(new PositiveRationalNumber(13.4f),add.op(list2));
		
		assertEquals(new PositiveRationalNumber(0.0f),mult.op(list1));
		assertEquals(new PositiveRationalNumber(37.44f),mult.op(list2));
		
		assertEquals(new PositiveRationalNumber(0.0f),min.op(list1));
		assertEquals(new PositiveRationalNumber(1.0f),min.op(list2));
		
		assertEquals(new PositiveRationalNumber(7.2f),max.op(list1));
		assertEquals(new PositiveRationalNumber(7.2f),max.op(list2));
	}
	
	@Test
	public void listOfNumbers2() {
		for (int i = 0; i < 3; i++) {
			list1.add(new PositiveRationalNumber(5.125f));
		}
		assertEquals(new PositiveRationalNumber(15.375f),add.op(list1));
		
		assertEquals(new PositiveRationalNumber(134.611328125f),mult.op(list1));
		
		assertEquals(new PositiveRationalNumber(5.125f),min.op(list1));
		
		assertEquals(new PositiveRationalNumber(5.125f),max.op(list1));
	}
	
	@Test
	public void testNeutralElement() {
		assertEquals(new PositiveRationalNumber(0),add.op(rZero,rZero));
		assertEquals(new PositiveRationalNumber(0),add.op(list1));
		
		assertEquals(new PositiveRationalNumber(1),mult.op(rOne,rOne));
		assertEquals(new PositiveRationalNumber(1),mult.op(list1));
		
		assertEquals(new PositiveRationalNumber(Infinity.POSITIVE_INFINITE),min.op(pinf,pinf));
		assertEquals(new PositiveRationalNumber(Infinity.POSITIVE_INFINITE),min.op(list1));
		
		assertEquals(new PositiveRationalNumber(Infinity.NEGATIVE_INFINITE),max.op(ninf,ninf));
		assertEquals(new PositiveRationalNumber(Infinity.NEGATIVE_INFINITE),max.op(list1));
	}
	
	@Test
	public void testNegativeInfinite() {
		assertEquals(new PositiveRationalNumber(Infinity.NEGATIVE_INFINITE), new PositiveRationalNumber(0f));
	}
	
	@Test
	public void testPositiveInfinite() {
		List<DataType> l = new LinkedList<DataType>();
		l.add(pinf);
		l.add(pinf);
		l.add(pinf);
		
		assertEquals(new PositiveRationalNumber(Infinity.POSITIVE_INFINITE),add.op(pinf,rZero));
		assertEquals(new PositiveRationalNumber(Infinity.POSITIVE_INFINITE),add.op(rOne, pinf));
		assertEquals(new PositiveRationalNumber(Infinity.POSITIVE_INFINITE),add.op(pinf,pinf));
		assertEquals(new PositiveRationalNumber(Infinity.POSITIVE_INFINITE),add.op(l));
		
		assertEquals(new PositiveRationalNumber(Infinity.POSITIVE_INFINITE),mult.op(pinf,rZero));
		assertEquals(new PositiveRationalNumber(Infinity.POSITIVE_INFINITE),mult.op(rOne, pinf));
		assertEquals(new PositiveRationalNumber(Infinity.POSITIVE_INFINITE),mult.op(pinf,pinf));
		assertEquals(new PositiveRationalNumber(Infinity.POSITIVE_INFINITE),mult.op(l));
		
		assertEquals(new PositiveRationalNumber(0),min.op(pinf,rZero));
		assertEquals(new PositiveRationalNumber(1),min.op(rOne, pinf));
		assertEquals(new PositiveRationalNumber(Infinity.POSITIVE_INFINITE),min.op(pinf,pinf));
		assertEquals(new PositiveRationalNumber(Infinity.POSITIVE_INFINITE),min.op(l));
		
		assertEquals(new PositiveRationalNumber(Infinity.POSITIVE_INFINITE),max.op(pinf,rZero));
		assertEquals(new PositiveRationalNumber(Infinity.POSITIVE_INFINITE),max.op(rOne, pinf));
		assertEquals(new PositiveRationalNumber(Infinity.POSITIVE_INFINITE),max.op(pinf,pinf));
		assertEquals(new PositiveRationalNumber(Infinity.POSITIVE_INFINITE),max.op(l));
	}
}
