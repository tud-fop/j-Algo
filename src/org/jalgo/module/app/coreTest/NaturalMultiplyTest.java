package org.jalgo.module.app.coreTest;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.jalgo.module.app.core.dataType.DataType;
import org.jalgo.module.app.core.dataType.Infinity;
import org.jalgo.module.app.core.dataType.naturalNumber.NaturalMultiply;
import org.jalgo.module.app.core.dataType.naturalNumber.NaturalNumber;
import org.junit.Before;
import org.junit.Test;

public class NaturalMultiplyTest {
	NaturalMultiply mult;
	NaturalNumber n1,n2,nZero,nOne;
	// infinite
	NaturalNumber inf;
	List<DataType> list;
	
	@Before
	public void setUp() {
		mult = new NaturalMultiply();
		n1 = new NaturalNumber(5);
		n2 = new NaturalNumber(7);
		nZero = new NaturalNumber();
		nOne = new NaturalNumber(1);
		inf = new NaturalNumber(Infinity.POSITIVE_INFINITE);
		
		list = new LinkedList<DataType>();
	}
	
	@Test
	public void multiplyTwoNumbers() {
		//test if commutative
		assertEquals(new NaturalNumber(35),mult.op(n1,n2));
		assertEquals(new NaturalNumber(35),mult.op(n2,n1));
		
		assertEquals(new NaturalNumber(0),mult.op(n1,nZero));
		assertEquals(new NaturalNumber(7),mult.op(nOne,n2));
	}
	
	@Test
	public void multiplyListOfNumbers1() {
		list.add(n1);
		list.add(n2);
		list.add(nOne);
		assertEquals(new NaturalNumber(35),mult.op(list));
	}
	
	@Test
	public void multiplyListOfNumbers2() {
		for (int i = 0; i < 3; i++) {
			list.add(n1);
		}
		assertEquals(new NaturalNumber(125),mult.op(list));
	}
	
	@Test
	public void multiplyNothing() {
		assertEquals(new NaturalNumber(0),mult.op(nZero, nZero));
		assertEquals(new NaturalNumber(1),mult.op(list));
		
		assertEquals(new NaturalNumber(1),mult.op(nOne,nOne));
	}
	
	@Test
	public void multiplyInfinite() {
		List<DataType> l = new LinkedList<DataType>();
		l.add(inf);
		l.add(inf);
		assertEquals(new NaturalNumber(Infinity.POSITIVE_INFINITE),mult.op(inf,nZero));
		assertEquals(new NaturalNumber(Infinity.POSITIVE_INFINITE),mult.op(n1, inf));
		assertEquals(new NaturalNumber(Infinity.POSITIVE_INFINITE),mult.op(inf,inf));
		assertEquals(new NaturalNumber(Infinity.POSITIVE_INFINITE),mult.op(l));
	}
	
}
