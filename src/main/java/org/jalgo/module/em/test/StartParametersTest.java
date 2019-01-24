package org.jalgo.module.em.test;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import junit.framework.TestCase;

import org.jalgo.module.em.control.EMModule;
import org.jalgo.module.em.data.EMData;
import org.jalgo.module.em.data.EMState;
import org.jalgo.module.em.data.Event;
import org.jalgo.module.em.data.Partition;
import org.jalgo.module.em.data.StartParameters;

/**
 *
 * @author Kilian
 */
public class StartParametersTest extends TestCase {
	private StartParameters startParameters;
    private Set<Event> events;
    private EMState p0EMState;
    private Set<Partition> observations;
    private Vector<Integer> experimentVector;
    
    @Override
    protected void setUp() throws Exception {
    	startParameters = new StartParameters();
		
		//Creating Events
		events = new HashSet<Event>();
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
		
		experimentVector = new Vector<Integer>();
		experimentVector.setSize(2);
		experimentVector.set(0, 2);
		experimentVector.set(1, 2);
		startParameters.setExperiment(experimentVector);
	
		startParameters.setEvents(events);
		Set<Event> elements1 = new HashSet<Event>();
		Set<Event> elements2 = new HashSet<Event>();
		Set<Event> elements3 = new HashSet<Event>();
		
		elements1.add(kkEvent);
		elements2.add(kzEvent);
		elements2.add(zkEvent);
		elements3.add(zzEvent);
		
		//Creating Partitions
		Partition partition1 = new Partition(elements1, "0 mal Zahl"); //KK; h =  2
		Partition partition2 = new Partition(elements2, "1 mal Zahl"); //KZ , ZK; h = 9
		Partition partition3 = new Partition(elements3, "2 mal Zahl"); //ZZ; h = 4
		
		partition1.setFrequency(2);
		partition2.setFrequency(9);
		partition3.setFrequency(4);		
		
		observations = new HashSet<Partition>();
		observations.add(partition1);
		observations.add(partition2);
		observations.add(partition3);
		startParameters.setObservations(observations);
		
		//Set Yield
		for(Partition partition : startParameters.getObservations()){
			for (Event event : partition.getElements()){
				event.setYield(partition);
			}
		}
		
		
		
		
		List<EMData> p0DataList = new ArrayList<EMData>();
		
		//Creating p0 Distributions
		// p0 No. 1
		Map<Point,Double> singleEventStartProbability = new HashMap<Point, Double>();
		singleEventStartProbability.put(new Point(2,0), 0.5);
		singleEventStartProbability.put(new Point(1,0), 0.5);
		singleEventStartProbability.put(new Point(2,1), 0.5);
		singleEventStartProbability.put(new Point(1,1), 0.5);
		EMData p0Data = EMModule.calcFirstStep(singleEventStartProbability, startParameters);
		
		p0DataList.add(p0Data);
		
		//p0 No. 2
		Map<Point,Double> singleEventStartProbability2 = new HashMap<Point, Double>();
		singleEventStartProbability2.put(new Point(2,0), 0.3);
		singleEventStartProbability2.put(new Point(1,0), 0.7);
		singleEventStartProbability2.put(new Point(2,1), 0.4);
		singleEventStartProbability2.put(new Point(1,1), 0.6);
		EMData p0Data2 = EMModule.calcFirstStep(singleEventStartProbability2, startParameters);
		p0DataList.add(p0Data2);
		
		//p0 No. 3
		Map<Point,Double> singleEventStartProbability3 = new HashMap<Point, Double>();
		singleEventStartProbability3.put(new Point(2,0), 0.1);
		singleEventStartProbability3.put(new Point(1,0), 0.9);
		singleEventStartProbability3.put(new Point(2,1), 0.2);
		singleEventStartProbability3.put(new Point(1,1), 0.8);
		EMData p0Data3 = EMModule.calcFirstStep(singleEventStartProbability3, startParameters);
		p0DataList.add(p0Data3);
				
		
		p0EMState = new EMState(p0DataList);
		startParameters.setP0EMState(p0EMState);
		
    	super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getEvents method, of class StartParameters.
     */
    public void testGetEvents() {
//        System.out.println("getEvents");
        StartParameters instance = new StartParameters();
        Set<Event> expResult = null;
        Set<Event> result = instance.getEvents();
        assertEquals(expResult, result);
        
        instance = startParameters;
        expResult = events;
        result = instance.getEvents();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of setEvents method, of class StartParameters.
     */
    public void testSetEvents() {
//        System.out.println("setEvents");
        Set<Event> events = this.events;
        StartParameters instance = new StartParameters();
        instance.setEvents(events);
        assertEquals(events, instance.getEvents());
     }

    /**
     * Test of setP0EMState method, of class StartParameters.
     */
    public void testSetP0EMState() {
//        System.out.println("setP0EMState");
        EMState p0EMState = this.p0EMState;
        StartParameters instance = new StartParameters();
        instance.setP0EMState(p0EMState);
        assertEquals(p0EMState, instance.getP0EMState());
    }

    /**
     * Test of getObservations method, of class StartParameters.
     */
    public void testGetObservations() {
//        System.out.println("getObservations");
        StartParameters instance = startParameters;
        Set<Partition> expResult = observations;
        Set<Partition> result = instance.getObservations();
        assertEquals(expResult, result);
    }

    /**
     * Test of setObservations method, of class StartParameters.
     */
    public void testSetObservations() {
//        System.out.println("setObservations");
        Set<Partition> observations = this.observations;
        StartParameters instance = new StartParameters();
        instance.setObservations(observations);
        assertEquals(observations, instance.getObservations());
    }

    /**
     * Test of getFrequencySum method, of class StartParameters.
     */
    public void testGetFrequencySum() {
//        System.out.println("getFrequencySum");
        StartParameters instance = startParameters;
        double expResult = 0;
        for (Partition partition : observations){
        	expResult += partition.getFrequency();
        }
        Double result = instance.getFrequencySum();
        assertEquals(new Double(expResult), result);
        
    }

    /**
     * Test of getObjectCount method, of class StartParameters.
     */
    public void testGetObjectCount() {
//        System.out.println("getObjectCount");
        StartParameters instance = startParameters;
        int expResult = experimentVector.size();
        int result = instance.getObjectCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of getExperiment method, of class StartParameters.
     */
    public void testGetExperiment() {
//        System.out.println("getExperiment");
        StartParameters instance = this.startParameters;
        Vector<Integer> expResult = this.experimentVector;
        Vector<Integer> result = instance.getExperiment();
        assertEquals(expResult, result);
    }

    /**
     * Test of getP0EMState method, of class StartParameters.
     */
    public void testGetP0EMState() {
//        System.out.println("getP0EMState");
        StartParameters instance = this.startParameters;
        EMState expResult = this.p0EMState;
        EMState result = instance.getP0EMState();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSingleExperiments method, of class StartParameters.
     */
    public void testGetSingleExperiments() {
//        System.out.println("getSingleExperiments");
        StartParameters instance = this.startParameters;
        Set<Point> expResult = new HashSet<Point>();
        expResult.add(new Point(2,0));
		expResult.add(new Point(1,0));
		expResult.add(new Point(2,1));
		expResult.add(new Point(1,1));
        Set<Point> result = instance.getSingleExperiments();
        assertEquals(expResult, result);
    }

    /**
     * Test of setExperiment method, of class StartParameters.
     */
    public void testSetExperiment() {
//        System.out.println("setExperiment");
        Vector<Integer> experiment = this.experimentVector;
        StartParameters instance = new StartParameters();
        instance.setExperiment(experiment);
        assertEquals(experiment, instance.getExperiment());
    }

    /**
     * Test of getObjectNames method, of class StartParameters.
     */
    public void testGetObjectNames() {
//        System.out.println("getObjectNames");
        StartParameters instance = startParameters;
        String[] expResult = {"!ObjectName!2 1", "!ObjectName!2 2"};
        String[] result = instance.getObjectNames();
        for (int i = 0; i<expResult.length; i++){
        	assertEquals(true, expResult[i].equals(result[i]));
        }
    }
}
