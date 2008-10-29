package org.jalgo.module.app.coreTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.jalgo.module.app.core.dataType.Operation;
import org.jalgo.module.app.core.dataType.booleanType.BooleanType;
import org.jalgo.module.app.core.dataType.booleanType.Conjunction;
import org.jalgo.module.app.core.dataType.booleanType.Disjunction;
import org.junit.Before;
import org.junit.Test;

public class BooleanTypeTest {
	BooleanType bEmpty, bFalse, bTrue;

	@Before
	public void setUp() throws Exception {
		bEmpty = new BooleanType();
		bFalse = new BooleanType(false);
		bTrue = new BooleanType(true);
	}
	
	@Test
	public void checkNumberOfOperations() {
		assertNotNull(BooleanType.getOperations());
		assertNotSame(0,BooleanType.getOperations().size());
		assertNotSame(1,BooleanType.getOperations().size());
	}
	
	@Test
	public void checkValue() {
		assertFalse(bEmpty.toBoolean());
		assertFalse(bFalse.toBoolean());
		assertTrue(bTrue.toBoolean());
	}
	
	@Test
	public void setFromBoolean() {
		bEmpty.setFromBoolean(false);
		assertFalse(bEmpty.toBoolean());
		bEmpty.setFromBoolean(true);
		assertTrue(bEmpty.toBoolean());
	}
	
	@Test
	public void setFromInt() {
		bEmpty.setFromInt(0);
		assertFalse(bEmpty.toBoolean());
		bEmpty.setFromInt(1);
		assertTrue(bEmpty.toBoolean());
	}
	
	@Test
	public void setFromString() {
		bEmpty.setFromString("false");
		assertFalse(bEmpty.toBoolean());
		bEmpty.setFromString("1");
		assertTrue(bEmpty.toBoolean());
	}
	
	@Test
	public void wrongStringInput() {
		assertFalse(bEmpty.setFromString("aba"));
	}
	
	@Test
	public void setToType() {
		bEmpty.setFromBoolean(false);
		assertFalse(bEmpty.toBoolean());
		assertEquals(0,bEmpty.toInt());
		assertEquals("false",bEmpty.toString());
	}
	
	@Test
	public void testSimpleOperation() {
		for (Operation op : BooleanType.getOperations()) {
			if (op instanceof Conjunction) {
				assertEquals(new BooleanType(false),op.op(bFalse, bTrue));
			} else if (op instanceof Disjunction) {
				assertEquals(new BooleanType(true),op.op(bFalse, bTrue));
			}
		}
	}

}
