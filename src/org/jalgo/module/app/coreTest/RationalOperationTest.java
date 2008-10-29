package org.jalgo.module.app.coreTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.jalgo.module.app.core.dataType.DataType;
import org.jalgo.module.app.core.dataType.Infinity;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalAdd;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalMaximum;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalMinimum;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalMultiply;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalNumber;
import org.junit.Before;
import org.junit.Test;

public class RationalOperationTest {
	RationalNumber rEmpty,rZero,rOne,r5,r7,rNegative;
	// infinite
	RationalNumber pinf, ninf;
	RationalAdd add;
	RationalMultiply mult;
	RationalMinimum min;
	RationalMaximum max;
	
	List<DataType> list1, list2;
	
	@Before
	public void setUp() {
		add = (RationalAdd) RationalNumber.getOperationByID("add");
		mult = (RationalMultiply) RationalNumber.getOperationByID("mult");
		min = (RationalMinimum) RationalNumber.getOperationByID("min");
		max = (RationalMaximum) RationalNumber.getOperationByID("max");
		
		rEmpty = new RationalNumber();
		rZero = new RationalNumber(0);
		rOne = new RationalNumber(1.0f);
		pinf = new RationalNumber(Infinity.POSITIVE_INFINITE);
		ninf = new RationalNumber(Infinity.NEGATIVE_INFINITE);
		r5 = new RationalNumber(5.2f);
		r7 = new RationalNumber(7.2f);
		rNegative = new RationalNumber();
		
		list1 = new LinkedList<DataType>();
		list2 = new LinkedList<DataType>();
		
	}
	
	@Test
	public void checkClosure() {
		assertTrue((add.op(rEmpty, rEmpty).getClass() == RationalNumber.class));
		assertTrue((mult.op(rEmpty, rEmpty).getClass() == RationalNumber.class));
		assertTrue((min.op(rEmpty, rEmpty).getClass() == RationalNumber.class));
		assertTrue((max.op(rEmpty, rEmpty).getClass() == RationalNumber.class));
		assertTrue((rEmpty.clone().getClass() == RationalNumber.class));
	}
	
	@Test
	public void twoNumbers() {
			assertEquals(new RationalNumber(12.4f),add.op(r5,r7));
			assertEquals(new RationalNumber(12.4f),add.op(r7,r5));
			assertEquals(new RationalNumber(0.0f),add.op(rEmpty, rZero));
			
			assertEquals(new RationalNumber(37.44f),mult.op(r5,r7));
			assertEquals(new RationalNumber(37.44f),mult.op(r7,r5));
			assertEquals(new RationalNumber(0.0f),mult.op(rEmpty, rZero));
	
			assertEquals(new RationalNumber(5.2f),min.op(r5,r7));
			assertEquals(new RationalNumber(0),min.op(rZero,r5));
			assertEquals(new RationalNumber(0.0f),min.op(rEmpty, rZero));

			assertEquals(new RationalNumber(7.2f),max.op(r5,r7));
			assertEquals(new RationalNumber(7.2f),max.op(r7,rZero));
			assertEquals(new RationalNumber(0.0f),max.op(rEmpty, rZero));
	}
	
	@Test
	public void listOfNumbers1() {
		list1.add(r5);
		list1.add(r7);
		list1.add(rZero);
		
		list2.add(r5);
		list2.add(r7);
		list2.add(rOne);
		
		assertEquals(new RationalNumber(12.4f),add.op(list1));
		assertEquals(new RationalNumber(13.4f),add.op(list2));
		
		assertEquals(new RationalNumber(0.0f),mult.op(list1));
		assertEquals(new RationalNumber(37.44f),mult.op(list2));
		
		assertEquals(new RationalNumber(0.0f),min.op(list1));
		assertEquals(new RationalNumber(1.0f),min.op(list2));
		
		assertEquals(new RationalNumber(7.2f),max.op(list1));
		assertEquals(new RationalNumber(7.2f),max.op(list2));
	}
	
	@Test
	public void listOfNumbers2() {
		for (int i = 0; i < 3; i++) {
			list1.add(new RationalNumber(5.125f));
		}
		assertEquals(new RationalNumber(15.375f),add.op(list1));
		
		assertEquals(new RationalNumber(134.611328125f),mult.op(list1));
		
		assertEquals(new RationalNumber(5.125f),min.op(list1));
		
		assertEquals(new RationalNumber(5.125f),max.op(list1));
	}
	
	@Test
	public void testNeutralElement() {
		assertEquals(new RationalNumber(0),add.op(rZero,rZero));
		assertEquals(new RationalNumber(0),add.op(list1));
		
		assertEquals(new RationalNumber(1),mult.op(rOne,rOne));
		assertEquals(new RationalNumber(1),mult.op(list1));
		
		assertEquals(new RationalNumber(Infinity.POSITIVE_INFINITE),min.op(pinf,pinf));
		assertEquals(new RationalNumber(Infinity.POSITIVE_INFINITE),min.op(list1));
		
		assertEquals(new RationalNumber(Infinity.NEGATIVE_INFINITE),max.op(ninf,ninf));
		assertEquals(new RationalNumber(Infinity.NEGATIVE_INFINITE),max.op(list1));
	}
	
	@Test
	public void testNegativeInfinite() {
		List<DataType> l = new LinkedList<DataType>();
		l.add(ninf);
		l.add(ninf);
		l.add(ninf);
		
		assertEquals("-∞ + 0", new RationalNumber(Infinity.NEGATIVE_INFINITE),add.op(ninf,rZero));
		assertEquals("1 + -∞", new RationalNumber(Infinity.NEGATIVE_INFINITE),add.op(rOne, ninf));
		assertEquals("-∞ + -∞", new RationalNumber(Infinity.NEGATIVE_INFINITE),add.op(ninf,ninf));
		assertEquals("+{-∞, -∞, -∞}", new RationalNumber(Infinity.NEGATIVE_INFINITE),add.op(l));
		
		assertEquals("-∞ * 0", new RationalNumber(Infinity.NEGATIVE_INFINITE),mult.op(ninf,rZero));
		assertEquals("1 * -∞", new RationalNumber(Infinity.NEGATIVE_INFINITE),mult.op(rOne, ninf));
		assertEquals("-∞ * -∞", new RationalNumber(Infinity.NEGATIVE_INFINITE),mult.op(ninf,ninf));
		assertEquals("*{-∞, -∞, -∞}", new RationalNumber(Infinity.NEGATIVE_INFINITE),mult.op(l));
		
		assertEquals("min -∞ 0", new RationalNumber(Infinity.NEGATIVE_INFINITE),min.op(ninf,rZero));
		assertEquals("min 1 -∞", new RationalNumber(Infinity.NEGATIVE_INFINITE),min.op(rOne, ninf));
		assertEquals("min -∞ -∞", new RationalNumber(Infinity.NEGATIVE_INFINITE),min.op(ninf,ninf));
		assertEquals("min{-∞, -∞, -∞}", new RationalNumber(Infinity.NEGATIVE_INFINITE),min.op(l));
		
		assertEquals("max -∞ 0", new RationalNumber(0),max.op(ninf,rZero));
		assertEquals("max 1 -∞", new RationalNumber(1),max.op(rOne, ninf));
		assertEquals("max -∞ -∞", new RationalNumber(Infinity.NEGATIVE_INFINITE),max.op(ninf,ninf));
		assertEquals("max{-∞, -∞, -∞}", new RationalNumber(Infinity.NEGATIVE_INFINITE),max.op(l));
	}
	
	@Test
	public void testPositiveInfinite() {
		List<DataType> l = new LinkedList<DataType>();
		l.add(pinf);
		l.add(pinf);
		l.add(pinf);
		
		assertEquals(new RationalNumber(Infinity.POSITIVE_INFINITE),add.op(pinf,rZero));
		assertEquals(new RationalNumber(Infinity.POSITIVE_INFINITE),add.op(rOne, pinf));
		assertEquals(new RationalNumber(Infinity.POSITIVE_INFINITE),add.op(pinf,pinf));
		assertEquals(new RationalNumber(Infinity.POSITIVE_INFINITE),add.op(l));
		
		assertEquals(new RationalNumber(Infinity.POSITIVE_INFINITE),mult.op(pinf,rZero));
		assertEquals(new RationalNumber(Infinity.POSITIVE_INFINITE),mult.op(rOne, pinf));
		assertEquals(new RationalNumber(Infinity.POSITIVE_INFINITE),mult.op(pinf,pinf));
		assertEquals(new RationalNumber(Infinity.POSITIVE_INFINITE),mult.op(l));
		
		assertEquals(new RationalNumber(0),min.op(pinf,rZero));
		assertEquals(new RationalNumber(1),min.op(rOne, pinf));
		assertEquals(new RationalNumber(Infinity.POSITIVE_INFINITE),min.op(pinf,pinf));
		assertEquals(new RationalNumber(Infinity.POSITIVE_INFINITE),min.op(l));
		
		assertEquals(new RationalNumber(Infinity.POSITIVE_INFINITE),max.op(pinf,rZero));
		assertEquals(new RationalNumber(Infinity.POSITIVE_INFINITE),max.op(rOne, pinf));
		assertEquals(new RationalNumber(Infinity.POSITIVE_INFINITE),max.op(pinf,pinf));
		assertEquals(new RationalNumber(Infinity.POSITIVE_INFINITE),max.op(l));
	}
}
