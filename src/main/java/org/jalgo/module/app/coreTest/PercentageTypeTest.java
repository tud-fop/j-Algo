package org.jalgo.module.app.coreTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.jalgo.module.app.core.dataType.DataType;
import org.jalgo.module.app.core.dataType.Infinity;
import org.jalgo.module.app.core.dataType.rationalNumber.PercentageType;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalAdd;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalMaximum;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalMinimum;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalMultiply;
import org.junit.Before;
import org.junit.Test;

public class PercentageTypeTest {
	PercentageType rEmpty,rZero,rOne,r5,r2,rNegative;
	// infinite
	PercentageType pinf, ninf;
	RationalAdd add;
	RationalMultiply mult;
	RationalMinimum min;
	RationalMaximum max;
	
	List<DataType> list1,list2;
	
	@Before
	public void setUp() {
		add = (RationalAdd) PercentageType.getOperationByID("add");
		mult = (RationalMultiply) PercentageType.getOperationByID("mult");
		min = (RationalMinimum) PercentageType.getOperationByID("min");
		max = (RationalMaximum) PercentageType.getOperationByID("max");
		
		rEmpty = new PercentageType();
		rZero = new PercentageType(0);
		rOne = new PercentageType(1.0f);
		pinf = new PercentageType(Infinity.POSITIVE_INFINITE);
		ninf = new PercentageType(Infinity.NEGATIVE_INFINITE);
		rNegative = new PercentageType();
		r5 = new PercentageType(0.5f);
		r2 = new PercentageType(0.25f);
		list1 = new LinkedList<DataType>();
		list2 = new LinkedList<DataType>();
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void checkInvalidNumbers() {
		rNegative = new PercentageType(1.2f);
	}
	
	@Test
	public void checkClosure() {
		assertTrue((add.op(rEmpty, rEmpty).getClass() == PercentageType.class));
		assertTrue((mult.op(rEmpty, rEmpty).getClass() == PercentageType.class));
		assertTrue((min.op(rEmpty, rEmpty).getClass() == PercentageType.class));
		assertTrue((max.op(rEmpty, rEmpty).getClass() == PercentageType.class));
		assertTrue((rEmpty.clone().getClass() == PercentageType.class));
	}
	
	@Test
	public void twoNumbers() {
			assertEquals(new PercentageType(0.75f),add.op(r5,r2));
			assertEquals(new PercentageType(0.75f),add.op(r2,r5));
			assertEquals(new PercentageType(0.0f),add.op(rEmpty, rZero));
			
			assertEquals(new PercentageType(0.125f),mult.op(r5,r2));
			assertEquals(new PercentageType(0.125f),mult.op(r2,r5));
			assertEquals(new PercentageType(0.0f),mult.op(rEmpty, rZero));
	
			assertEquals(new PercentageType(0.25f),min.op(r5,r2));
			assertEquals(new PercentageType(0),min.op(rZero,r5));
			assertEquals(new PercentageType(0.0f),min.op(rEmpty, rZero));

			assertEquals(new PercentageType(0.5f),max.op(r5,r2));
			assertEquals(new PercentageType(0.25f),max.op(r2,rZero));
			assertEquals(new PercentageType(0.0f),max.op(rEmpty, rZero));
	}
	
	@Test
	public void listOfNumbers1() {
		list1.add(r5);
		list1.add(r2);
		list1.add(rZero);
		
		list2.add(r5);
		list2.add(r2);
		list2.add(r2);
		
		assertEquals(new PercentageType(0.75f),add.op(list1));
		assertEquals(new PercentageType(1.0f),add.op(list2));
		
		assertEquals(new PercentageType(0.0f),mult.op(list1));
		assertEquals(new PercentageType(0.03125f),mult.op(list2));
		
		assertEquals(new PercentageType(0.0f),min.op(list1));
		assertEquals(new PercentageType(0.25f),min.op(list2));
		
		assertEquals(new PercentageType(0.5f),max.op(list1));
		assertEquals(new PercentageType(0.5f),max.op(list2));
	}
	
	@Test
	public void listOfNumbers2() {
		for (int i = 0; i < 3; i++) {
			list1.add(new PercentageType(0.25f));
		}
		assertEquals(new PercentageType(0.75f),add.op(list1));
		
		assertEquals(new PercentageType(0.015625f),mult.op(list1));
		
		assertEquals(new PercentageType(0.25f),min.op(list1));
		
		assertEquals(new PercentageType(0.25f),max.op(list1));
	}
	
	@Test
	public void testNeutralElement() {
		assertEquals(new PercentageType(0),add.op(rZero,rZero));
		assertEquals(new PercentageType(0),add.op(list1));
		
		assertEquals(new PercentageType(1),mult.op(rOne,rOne));
		assertEquals(new PercentageType(1),mult.op(list1));
		
		assertEquals(new PercentageType(Infinity.POSITIVE_INFINITE),min.op(pinf,pinf));
		assertEquals(new PercentageType(Infinity.POSITIVE_INFINITE),min.op(list1));
		
		assertEquals(new PercentageType(Infinity.NEGATIVE_INFINITE),max.op(ninf,ninf));
		assertEquals(new PercentageType(Infinity.NEGATIVE_INFINITE),max.op(list1));
	}
	
	@Test
	public void testNegativeInfinite() {
		assertEquals(new PercentageType(Infinity.NEGATIVE_INFINITE), new PercentageType(0f));
	}
	
	@Test
	public void testPositiveInfinite() {
		assertEquals(new PercentageType(Infinity.POSITIVE_INFINITE), new PercentageType(1f));
	}
}
