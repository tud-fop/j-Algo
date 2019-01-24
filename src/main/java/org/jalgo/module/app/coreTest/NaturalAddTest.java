package org.jalgo.module.app.coreTest;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.jalgo.module.app.core.dataType.DataType;
import org.jalgo.module.app.core.dataType.Infinity;
import org.jalgo.module.app.core.dataType.naturalNumber.NaturalAdd;
import org.jalgo.module.app.core.dataType.naturalNumber.NaturalNumber;
import org.junit.Before;
import org.junit.Test;

public class NaturalAddTest {
	NaturalAdd add;
	NaturalNumber n1,n2,nZero;
	// infinite
	NaturalNumber inf;
	List<DataType> list;
	
	@Before
	public void setUp() {
		add = new NaturalAdd();
		n1 = new NaturalNumber(5);
		n2 = new NaturalNumber(7);
		nZero = new NaturalNumber();
		inf = new NaturalNumber(Infinity.POSITIVE_INFINITE);
		
		list = new LinkedList<DataType>();
	}
	
	@Test
	public void addTwoNumbers() {
		assertEquals(new NaturalNumber(12),add.op(n1,n2));
		assertEquals(new NaturalNumber(12),add.op(n2,n1));
		
		assertEquals(new NaturalNumber(5),add.op(n1,nZero));
		assertEquals(new NaturalNumber(7),add.op(nZero,n2));
	}
	
	@Test
	public void addListOfNumbers1() {
		list.add(n1);
		list.add(n2);
		list.add(nZero);
		assertEquals(new NaturalNumber(12),add.op(list));
	}
	
	@Test
	public void addListOfNumbers2() {
		for (int i = 0; i < 3; i++) {
			list.add(n1);
		}
		assertEquals(new NaturalNumber(15),add.op(list));
	}
	
	@Test
	public void addNothing() {
		assertEquals(new NaturalNumber(0),add.op(nZero, nZero));
		assertEquals(new NaturalNumber(0),add.op(list));
	}
	
	@Test
	public void addInfinite() {
		List<DataType> l = new LinkedList<DataType>();
		l.add(inf);
		l.add(inf);
		
		assertEquals(new NaturalNumber(Infinity.POSITIVE_INFINITE),add.op(inf,nZero));
		assertEquals(new NaturalNumber(Infinity.POSITIVE_INFINITE),add.op(n1, inf));
		assertEquals(new NaturalNumber(Infinity.POSITIVE_INFINITE),add.op(inf,inf));
		assertEquals(new NaturalNumber(Infinity.POSITIVE_INFINITE),add.op(l));
	}
	
}
