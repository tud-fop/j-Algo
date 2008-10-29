package org.jalgo.module.app.coreTest;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jalgo.module.app.core.dataType.DataType;
import org.jalgo.module.app.core.dataType.Operation;
import org.jalgo.module.app.core.dataType.booleanType.BooleanType;
import org.jalgo.module.app.core.dataType.booleanType.Conjunction;
import org.jalgo.module.app.core.dataType.booleanType.Disjunction;
import org.junit.Before;
import org.junit.Test;

public class BooleanOperationsTest {
	BooleanType bFalse,bTrue1,bTrue2;
	Disjunction or;
	Conjunction and;
	
	List<DataType> list;
	
	@Before
	public void setUp() throws Exception {
		bFalse = new BooleanType();
		bTrue1 = new BooleanType(true);
		bTrue2 = new BooleanType(true);
		
		Set<Operation> ops;
		
		ops = BooleanType.getOperations();
		for (Operation op : ops) {
			if (op instanceof Conjunction)
				and = (Conjunction)op;
			if (op instanceof Disjunction)
				or = (Disjunction)op;
		}
		
		list = new LinkedList<DataType>();
	}
	
	@Test
	public void twoVariables() {
		assertEquals(new BooleanType(true),or.op(bFalse,bTrue1));
		assertEquals(new BooleanType(false),and.op(bFalse,bTrue1));
		assertEquals(new BooleanType(true),and.op(bTrue1, bTrue2));
	}
	
	@Test
	public void listOfNumbers1() {
		list.add(bFalse);
		list.add(bTrue1);
		list.add(bTrue2);
		
		assertEquals(new BooleanType(true),or.op(list));
		assertEquals(new BooleanType(false),and.op(list));
	}
	
	@Test
	public void listOfNumbers2() {
		for(int i = 0; i < 3; i++) {
			list.add(bTrue1);
		}
		assertEquals(new BooleanType(true),or.op(list));
		assertEquals(new BooleanType(true),and.op(list));
	}

}
