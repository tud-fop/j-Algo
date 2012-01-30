/**
 * 
 */
package org.jalgo.module.em.test;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import junit.framework.TestCase;

import org.jalgo.module.em.data.Event;
import org.jalgo.module.em.data.Partition;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Tobias Nett
 *
 */
public class PartitionTest extends TestCase {
	
	private static final double DELTA = 0.001;
	
	private Partition partition;
	private Set<Event> elements;
	private String name;
	private Event kk,kz,zk,zz;

	@Before
	public void setUp(){
		elements = new HashSet<Event>();
		// create Events
		Vector<Integer> v = new Vector<Integer>();
		v.add(1); v.add(1);
		kk = new Event(v);
		v = new Vector<Integer>();
		v.add(1); v.add(2);
		kz = new Event(v);
		v = new Vector<Integer>();
		v.add(2); v.add(1);
		zk = new Event(v);
		v = new Vector<Integer>();
		v.add(2); v.add(2);
		zz = new Event(v);
		
		elements.add(kk);
		elements.add(kz);
		elements.add(zk);
		elements.add(zz);
		
	}
	
	protected void tearDown() throws Exception {
        super.tearDown();
    }

	/**
	 * Test method for {@link org.jalgo.module.em.data.Partition#Partition()}.
	 */
	@Test
	public void testPartition() {
		partition = new Partition();
		assertEquals("Eine leere Partition ohne Namen soll erzeugt werden", 
				"" , partition.getName());
		assertEquals("Eine leere Partition mit Häufigkeit 0 soll erzeugt werden", 
				0, partition.getFrequency(), DELTA);
		assertEquals("Eine leere Partition ohne zugeordnete Elemente soll erzeugt werden", 
				true, partition.getElements().isEmpty());
	}

	/**
	 * Test method for {@link org.jalgo.module.em.data.Partition#Partition(java.util.Set)}.
	 */
	@Test
	public void testPartitionSetOfEvent() {
		partition = new Partition(elements);
		assertEquals("Eine Partition ohne Namen soll erzeugt werden", 
				"" , partition.getName());
		assertEquals("Eine Partition mit Häufigkeit 0 soll erzeugt werden", 
				0, partition.getFrequency(), DELTA);
		assertEquals("Die Partition soll die übergebenen Elemente enhalten.", 
				elements, partition.getElements());
		
		try {
			partition = new Partition(null);
			fail("null argument should raise NPE");
		} catch (NullPointerException e) { }
	}

	/**
	 * Test method for {@link org.jalgo.module.em.data.Partition#Partition(java.util.Set, java.lang.String)}.
	 */
	@Test
	public void testPartitionSetOfEventString() {
		name = "TestName";
		partition = new Partition(elements, name);
		assertEquals("Eine Partition mit Name und Elementen soll erzeugt werden", 
				name , partition.getName());
		assertEquals("Eine Partition mit Häufigkeit 0 soll erzeugt werden", 
				0, partition.getFrequency(), DELTA);
		assertEquals("Die Partition soll die übergebenen Elemente enhalten.", 
				elements, partition.getElements());
		// if the name is null, a null pointer exception should be raised
		try {
			partition = new Partition(elements, null);
			fail("null argument should raise NPE");
		} catch (NullPointerException e) { }		
	}

	/**
	 * Test method for {@link org.jalgo.module.em.data.Partition#addElement(org.jalgo.module.em.data.Event)}.
	 */
	@Test
	public void testAddElement() {
		partition = new Partition();
		partition.addElement(zk);
		// adding an element to the partition
		assertEquals("Die Partition muss das hinzugefügte Element enhalten.", true, partition.getElements().contains(zk));
		Set<Event> rest = partition.getElements();
		rest.remove(zk);
		// only one element should be added to the partition
		assertEquals("Die Partition sollte keine weiteren Elemente enthalten", true, rest.isEmpty());
		// an element could only once be added to a partition
		partition = new Partition();
		partition.addElement(zk);
		partition.addElement(zk);
		assertEquals("Event darf nur einmal hinzugefügt werden", 1, partition.getElements().size());
	}

	/**
	 * Test method for {@link org.jalgo.module.em.data.Partition#removeElement(org.jalgo.module.em.data.Event)}.
	 */
	@Test
	public void testRemoveElement() {
		partition = new Partition(elements);
		partition.removeElement(zk);
		assertEquals("Anzahl der Elemente muss kleiner werden", 3, partition.getElements().size());
		assertEquals("Entferntes Element darf nich mehr enthalten sein", false, partition.getElements().contains(zk));
	}

	/**
	 * Test method for {@link org.jalgo.module.em.data.Partition#getName()}.
	 */
	@Test
	public void testGetName() {
		partition = new Partition();
		assertEquals("", partition.getName());
		partition.setName("TestName");
		assertEquals("TestName", partition.getName());
	}

	/**
	 * Test method for {@link org.jalgo.module.em.data.Partition#setName(java.lang.String)}.
	 */
	@Test
	public void testSetName() {
		partition = new Partition();
		String name = "TestName";
		partition.setName(name);
		assertEquals(name, partition.getName());
		try {
			partition.setName(null);
			fail("null argument should raise NPE");
		} catch (NullPointerException e) { }
	}

	/**
	 * Test method for {@link org.jalgo.module.em.data.Partition#getFrequency()}.
	 */
	@Test
	public void testGetFrequency() {
		partition = new Partition();
		assertEquals(0, partition.getFrequency(), DELTA);
		partition.setFrequency(Double.MAX_VALUE);
		assertEquals(Double.MAX_VALUE, partition.getFrequency(), DELTA);
	}

	/**
	 * Test method for {@link org.jalgo.module.em.data.Partition#setFrequency(double)}.
	 */
	@Test
	public void testSetFrequency() {
		
		partition = new Partition();
		partition.setFrequency(Double.MAX_VALUE);
		assertEquals(Double.MAX_VALUE, partition.getFrequency(), DELTA);
		
		partition.setFrequency(0);
		assertEquals(0, partition.getFrequency(), DELTA);
	}

	/**
	 * Test method for {@link org.jalgo.module.em.data.Partition#setFrequency(double)}.
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testNegativeFrequency() {
		final int NEGATIVE_FREQUENCY = -3;
		partition = new Partition();
		try {
			partition.setFrequency(NEGATIVE_FREQUENCY);
			fail("Negative amount should raise IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			
		}
		
		
	}

	/**
	 * Test method for {@link org.jalgo.module.em.data.Partition#getElements()}.
	 */
	@Test
	public void testGetElements() {
		partition = new Partition(elements);
		assertEquals(elements, partition.getElements());
		partition = new Partition(new HashSet<Event>());
		assertEquals(true, partition.getElements().isEmpty());
	}

	/**
	 * Test method for {@link org.jalgo.module.em.data.Partition#toString()}.
	 */
	@Test
	public void testToString() {
		partition = new Partition();
		assertEquals("empty", partition.toString());
		partition = new Partition(elements, "TestName");
		
		String expected = "<html><b>TestName</b><br>{ (1, 1), (1, 2), (2, 1), (2, 2) }</html>";
		assertEquals(expected, partition.toString());
		
		assertEquals(true, partition.toString().startsWith("<html>"));
		assertEquals(true, partition.toString().endsWith("</html>"));
		
		assertEquals(true, partition.toString().contains("{ ("));
		assertEquals(true, partition.toString().contains(") }"));	
		
		assertEquals(true, partition.toString().contains(partition.getName()));
		
		assertEquals(true, partition.toString().contains("(1, 1)"));
		assertEquals(true, partition.toString().contains("(1, 2)"));
		assertEquals(true, partition.toString().contains("(2, 1)"));
		assertEquals(true, partition.toString().contains("(2, 2)"));
		
		assertEquals(66, partition.toString().length());
	}
	
	/**
	 * Test method for {@link org.jalgo.module.em.data.Partition#getMathSet()}.
	 */
	public void testGetMathSet(){
		partition = new Partition(elements);
		
		assertEquals(true, partition.getMathSet().startsWith("{ "));
		assertEquals(true, partition.getMathSet().endsWith(" }"));
		
		assertEquals(true, partition.toString().contains("(1, 1)"));
		assertEquals(true, partition.toString().contains("(1, 2)"));
		assertEquals(true, partition.toString().contains("(2, 1)"));
		assertEquals(true, partition.toString().contains("(2, 2)"));
		
		assertEquals(34, partition.getMathSet().length());
	}
}

