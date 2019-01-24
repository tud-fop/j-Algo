/**
 * 
 */
package org.jalgo.module.em.test;

import java.util.Vector;

import junit.framework.TestCase;

import org.jalgo.module.em.data.Event;
import org.jalgo.module.em.data.Partition;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the <code>Event</code> class. Test all constructors and methods.
 * 
 * @author Tobias Nett
 *
 */
public class EventTest extends TestCase{
	
	private Event event, eventLess, eventGreater;
	private Vector<Integer> tuple, tupleLess, tupleGreater;
	private Partition partition;

	/**
	 * Initializes the test elements. Creates a new tuple (1,2,3) and creates an <code>Event</code> out of
	 * this tuple. 
	 * Creates a new empty <code>Partition</code>.
	 */
	@Before
	public void setUp(){
		tuple = new Vector<Integer>();
		tupleLess = new Vector<Integer>();
		tupleGreater = new Vector<Integer>();
		// create an event vector: (1,2,3)
		for (int i = 1; i < 4; i++){
			tuple.add(i);
			tupleLess.add(1);
			tupleGreater.add(i+1);
		}
		// create the event
		event = new Event(tuple);
		eventLess = new Event(tupleLess);
		eventGreater = new Event(tupleGreater);
		// create the (empty) partition
		partition = new Partition();
	}
	
	protected void tearDown() throws Exception {
        super.tearDown();
    }

	/**
	 * Test method for {@link org.jalgo.module.em.data.Event#Event(java.util.Vector)}. 
	 * <p> Constructor test method. The tuple should be the passed tuple, the yield should be <code>null</code>.
	 * 
	 */
	@Test
	public void testEvent() {		
		assertEquals(tuple, event.getTuple());
		assertEquals(null, event.getYield());
	}

	/**
	 * Test method for {@link org.jalgo.module.em.data.Event#setYield(org.jalgo.module.em.data.Partition)}.
	 * <p> The event's yield should be the Partition which is transfered to the method.
	 */
	@Test
	public void testSetYield() {
		event.setYield(partition);
		assertEquals(partition, event.getYield());
	}
	
	/**
	 * Test method for {@link org.jalgo.module.em.data.Event#setYield(org.jalgo.module.em.data.Partition)}.
	 * <p> The event's yield should be null (event is not part of any partition anymore).
	 */
	@Test
	public void testSetNullYield() {
		event.setYield(null);
		assertEquals(null, event.getYield());
	}

	/**
	 * Test method for {@link org.jalgo.module.em.data.Event#clearYield()}.
	 * <p> <code>clearYield</code> should set the event's yield to null (event is not part of any partition anymore).
	 */
	@Test
	public void testClearYield() {
		event.setYield(partition);
		assertEquals(partition, event.getYield());
		event.clearYield();
		assertEquals(null, event.getYield());
	}

	/**
	 * Test method for {@link org.jalgo.module.em.data.Event#toString()}.
	 * <p> The String representation of the event should be in the form 
	 * "(x<sub>1</sub>,x<sub>2</sub>,x<sub>3</sub>,...)" with space after
	 *  each comma.
	 */
	@Test
	public void testToString() {
		String expected = "(1, 2, 3)";
		assertEquals(expected, event.toString());
	}

	/**
	 * Test method for {@link org.jalgo.module.em.data.Event#getYield()}.
	 * <p> Should return <code>null</code> after the event is created and the
	 * yielded partition after the yield is set.
	 */
	@Test
	public void testGetYield() {
		assertEquals(null, event.getYield());
		event.setYield(partition);
		assertEquals(partition, event.getYield());
	}

	/**
	 * Test method for {@link org.jalgo.module.em.data.Event#getTuple()}.
	 * <p> Should return the event represented as a tuple 
	 * (<code>Vector</code> of <code>Integer</code>).
	 */
	@Test
	public void testGetTuple() {
		assertEquals(tuple, event.getTuple());
	}
	/**
	 * Test method for {@link org.jalgo.module.em.data.Event#compareTo(Event)}.
	 * <p> Compares the event (1,2,3) to the events (1,1,1) and (2,3,4) - method
	 * to test should return 0, a value less and and a value greater 0.
	 */
	@Test
	public void testComparable(){
		assertEquals(0, event.compareTo(event));
		assertEquals(true, event.compareTo(eventGreater)<0);
		assertEquals(true, event.compareTo(eventLess)>0);
	}

}
