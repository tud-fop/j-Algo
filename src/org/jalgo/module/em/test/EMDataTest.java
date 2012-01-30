/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jalgo.module.em.test;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import junit.framework.TestCase;

import org.jalgo.module.em.data.EMData;
import org.jalgo.module.em.data.Event;
import org.jalgo.module.em.data.Partition;

/**
 *
 * @author Kilian
 */
public class EMDataTest extends TestCase {
    private EMData instance;
    private Map<Event,Double> d,h,p;
    private Map<Point,Double> pForSingleEvent;
    private Map<Partition,Double> pForPartition;
    private double likelihood;
    
    @Override
    protected void setUp() throws Exception {
    	//Creating Events
		Set<Event> events = new HashSet<Event>();
		Vector <Integer> kkEventVector = new Vector<Integer>(3);
		Vector <Integer> kzEventVector = new Vector<Integer>(3);
		Vector <Integer> zkEventVector = new Vector<Integer>(3);
		Vector <Integer> zzEventVector = new Vector<Integer>(3);
		kkEventVector.setSize(2);
		kzEventVector.setSize(2);
		zkEventVector.setSize(2);
		zzEventVector.setSize(2);
		
		kkEventVector.setElementAt(2, 0);
		kkEventVector.setElementAt(2, 1);
		kzEventVector.setElementAt(2, 0);
		kzEventVector.setElementAt(1, 1);
		zkEventVector.setElementAt(1, 0);
		zkEventVector.setElementAt(2, 1);
		zzEventVector.setElementAt(1, 0);
		zzEventVector.setElementAt(1, 1);
		
		Event kkEvent =  new Event(kkEventVector);
		Event kzEvent =  new Event(kzEventVector);
		Event zkEvent =  new Event(zkEventVector);
		Event zzEvent =  new Event(zzEventVector);
		
		events.add(zzEvent);
		events.add(zkEvent);
		events.add(kzEvent);
		events.add(kkEvent);
		
		p = new HashMap<Event,Double>();
		double pValue = 0.0;
		for (Event event : events){
			p.put(event, pValue);
			pValue += 1.0;
		}		
		
		d = new HashMap<Event,Double>();
		for (Event event : events){
			d.put(event, pValue);
			pValue += 1.0;
		}	
		
		h = new HashMap<Event,Double>();
		for (Event event : events){
			h.put(event, pValue);
			pValue += 1.0;
		}	
		
		
		Set<Event> elements1 = new HashSet<Event>();
		Set<Event> elements2 = new HashSet<Event>();
		Set<Event> elements3 = new HashSet<Event>();
		
		elements1.add(kkEvent);
		elements2.add(kzEvent);
		elements2.add(zkEvent);
		elements3.add(zzEvent);
		
		Partition partition1 = new Partition(elements1, "0 mal Zahl"); //KK; h =  2
		Partition partition2 = new Partition(elements2, "1 mal Zahl"); //KZ , ZK; h = 9
		Partition partition3 = new Partition(elements3, "2 mal Zahl"); //ZZ; h = 4
		Set<Partition> partitions = new HashSet<Partition>();
		partitions.add(partition1);
		partitions.add(partition2);
		partitions.add(partition3);
		
		pForPartition = new HashMap<Partition, Double>();
		pValue = 0.0;
		for (Partition partition : partitions){
			pForPartition.put(partition, pValue);
			pValue += 1.0;
		}	
		
		
		likelihood = 17.3;
    	
		pForSingleEvent = new HashMap<Point, Double>();
    	pForSingleEvent.put(new Point(2,0), 0.5);
		pForSingleEvent.put(new Point(1,0), 0.5);
		pForSingleEvent.put(new Point(2,1), 0.5);
		pForSingleEvent.put(new Point(1,1), 0.5);
		instance = new EMData(d, h, p, pForPartition, pForSingleEvent, likelihood);
		
    	super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getP method, of class EMData.
     */
    public void testGetP() {
        Map<Event, Double> expResult = p;
        Map<Event, Double> result = instance.getP();
        for (Event event : expResult.keySet()){
        	assertEquals(expResult.get(event), result.get(event));
        }
    }

    /**
     * Test of getD method, of class EMData.
     */
    public void testGetD() {
        Map<Event,Double> expResult = d;
        Map<Event,Double> result = instance.getD();
        assertEquals(expResult, result);
    }

    /**
     * Test of getH method, of class EMData.
     */
    public void testGetH() {
        Map<Event,Double> expResult = h;
        Map<Event,Double> result = instance.getH();
        assertEquals(expResult, result);
     }

    /**
     * Test of getPForPartition method, of class EMData.
     */
    public void testGetPForPartition() {
        Map<Partition, Double> expResult = pForPartition;
        Map<Partition, Double> result = instance.getPForPartition();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPForSingleEvent method, of class EMData.
     */
    public void testGetPForSingleEvent() {
        Map<Point,Double> expResult = pForSingleEvent;
        Map<Point,Double> result = instance.getPForSingleEvent();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLikelihood method, of class EMData.
     */
    public void testGetLikelihood() {
        Double expResult = likelihood;
        Double result = instance.getLikelihood();
        assertEquals(expResult, result);
    }
}
