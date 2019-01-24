package org.jalgo.module.app.coreTest;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.jalgo.module.app.core.dataType.DataType;
import org.jalgo.module.app.core.dataType.Infinity;
import org.jalgo.module.app.core.dataType.naturalNumber.NaturalMaximum;
import org.jalgo.module.app.core.dataType.naturalNumber.NaturalNumber;
import org.junit.Before;
import org.junit.Test;

public class NaturalMaximumTest {
	NaturalMaximum max;
	NaturalNumber n1,n2,nZero,nOne;
	// infinite
	NaturalNumber inf;
	List<DataType> list;
	
	@Before
	public void setUp() {
		max = new NaturalMaximum();
		n1 = new NaturalNumber(5);
		n2 = new NaturalNumber(7);
		nZero = new NaturalNumber();
		nOne = new NaturalNumber(1);
		inf = new NaturalNumber(Infinity.POSITIVE_INFINITE);
		
		list = new LinkedList<DataType>();
	}
	
	@Test
	public void maxTwoNumbers() {
		//test if commutative
		assertEquals(new NaturalNumber(7),max.op(n1,n2));
		assertEquals(new NaturalNumber(7),max.op(n2,n1));
		
		assertEquals(new NaturalNumber(5),max.op(n1,nZero));
		assertEquals(new NaturalNumber(7),max.op(nOne,n2));
	}
	
	@Test
	public void maxListOfNumbers1() {
		list.add(n1);
		list.add(n2);
		list.add(nOne);
		assertEquals(new NaturalNumber(7),max.op(list));
	}
	
	@Test
	public void maxListOfNumbers2() {
		for (int i = 0; i < 3; i++) {
			list.add(n1);
		}
		assertEquals(new NaturalNumber(5),max.op(list));
	}
	
	@Test
	public void maxNothing() {
		assertEquals(new NaturalNumber(0),max.op(nZero, nZero));
		assertEquals(new NaturalNumber(0),max.op(list));
		
		assertEquals(new NaturalNumber(1),max.op(nOne,nOne));
	}
	
	@Test
	public void maxInfinite() {
		List<DataType> l = new LinkedList<DataType>();
		l.add(inf);
		l.add(inf);
		assertEquals(new NaturalNumber(Infinity.POSITIVE_INFINITE),max.op(inf,nZero));
		assertEquals(new NaturalNumber(Infinity.POSITIVE_INFINITE),max.op(n1, inf));
		assertEquals(new NaturalNumber(Infinity.POSITIVE_INFINITE),max.op(inf,inf));
		assertEquals(new NaturalNumber(Infinity.POSITIVE_INFINITE),max.op(l));
	}
	
}
