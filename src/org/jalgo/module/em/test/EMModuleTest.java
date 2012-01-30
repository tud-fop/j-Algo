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
import org.junit.Test;

/**
 * Test class for the {@code EMModule} class.
 * 
 */
public class EMModuleTest extends TestCase {
	private StartParameters startParameters;
	private EMState p0EMState;
	private Event kkEvent;
	private Event kzEvent;
	private Event zkEvent;
	private Event zzEvent;
	private Map<Point, Double> singleEventStartProbability;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		startParameters = new StartParameters();

		// Creating Events
		Set<Event> events = new HashSet<Event>();
		Vector<Integer> kkEventVector = new Vector<Integer>(3);
		Vector<Integer> kzEventVector = new Vector<Integer>(3);
		Vector<Integer> zkEventVector = new Vector<Integer>(3);
		Vector<Integer> zzEventVector = new Vector<Integer>(3);
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

		kkEvent = new Event(kkEventVector);
		kzEvent = new Event(kzEventVector);
		zkEvent = new Event(zkEventVector);
		zzEvent = new Event(zzEventVector);

		events.add(zzEvent);
		events.add(zkEvent);
		events.add(kzEvent);
		events.add(kkEvent);

		Vector<Integer> experimentVector = new Vector<Integer>();
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

		// Creating Partitions
		Partition partition1 = new Partition(elements1, "0 mal Zahl"); // KK; h
																		// = 2
		Partition partition2 = new Partition(elements2, "1 mal Zahl"); // KZ ,
																		// ZK; h
																		// = 9
		Partition partition3 = new Partition(elements3, "2 mal Zahl"); // ZZ; h
																		// = 4

		partition1.setFrequency(2);
		partition2.setFrequency(9);
		partition3.setFrequency(4);

		Set<Partition> observations = new HashSet<Partition>();
		observations.add(partition1);
		observations.add(partition2);
		observations.add(partition3);
		startParameters.setObservations(observations);

		// Set Yield
		for (Partition partition : startParameters.getObservations()) {
			for (Event event : partition.getElements()) {
				event.setYield(partition);
			}
		}

		List<EMData> p0DataList = new ArrayList<EMData>();

		// Creating p0 Distributions
		// p0 No. 1
		singleEventStartProbability = new HashMap<Point, Double>();
		singleEventStartProbability.put(new Point(2, 0), 0.5);
		singleEventStartProbability.put(new Point(1, 0), 0.5);
		singleEventStartProbability.put(new Point(2, 1), 0.5);
		singleEventStartProbability.put(new Point(1, 1), 0.5);
		EMData p0Data = EMModule.calcFirstStep(singleEventStartProbability,
				startParameters);

		p0DataList.add(p0Data);

		// p0 No. 2
		Map<Point, Double> singleEventStartProbability2 = new HashMap<Point, Double>();
		singleEventStartProbability2.put(new Point(2, 0), 0.3);
		singleEventStartProbability2.put(new Point(1, 0), 0.7);
		singleEventStartProbability2.put(new Point(2, 1), 0.4);
		singleEventStartProbability2.put(new Point(1, 1), 0.6);
		EMData p0Data2 = EMModule.calcFirstStep(singleEventStartProbability2,
				startParameters);
		p0DataList.add(p0Data2);

		// p0 No. 3
		Map<Point, Double> singleEventStartProbability3 = new HashMap<Point, Double>();
		singleEventStartProbability3.put(new Point(2, 0), 0.1);
		singleEventStartProbability3.put(new Point(1, 0), 0.9);
		singleEventStartProbability3.put(new Point(2, 1), 0.2);
		singleEventStartProbability3.put(new Point(1, 1), 0.8);
		EMData p0Data3 = EMModule.calcFirstStep(singleEventStartProbability3,
				startParameters);
		p0DataList.add(p0Data3);

		p0EMState = new EMState(p0DataList);
		startParameters.setP0EMState(p0EMState);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for
	 * {@link org.jalgo.module.em.control.EMModule#calcNextEMData(EMData, int, StartParameters)}
	 * .
	 */
	@Test
	public void testCalcNextEMData() {
		EMData data0 = EMModule.calcNextEMData(p0EMState.getEMData().get(0), 0,
				startParameters);
		// Testing Calculation of D
		double[] dArray = { 1.0, 0.5, 0.5, 1.0 };
		assertEquals(dArray[0], data0.getD().get(kkEvent));
		assertEquals(dArray[1], data0.getD().get(kzEvent));
		assertEquals(dArray[2], data0.getD().get(zkEvent));
		assertEquals(dArray[3], data0.getD().get(zzEvent));

		// Testing Calculation of H
		double[] hArray = { 2.0, 4.5, 4.5, 4.0 };
		assertEquals(hArray[0], data0.getH().get(kkEvent));
		assertEquals(hArray[1], data0.getH().get(kzEvent));
		assertEquals(hArray[2], data0.getH().get(zkEvent));
		assertEquals(hArray[3], data0.getH().get(zzEvent));

		// Testing Calculation of P
		double[] pArray = { (6.5 * 6.5) / 225.0, 6.5 * 8.5 / 225.0,
				6.5 * 8.5 / 225.0, 8.5 * 8.5 / 225.0 };
		assertEquals(true,
				Math.abs(pArray[0] - data0.getP().get(kkEvent)) < 0.0001);
		assertEquals(true,
				Math.abs(pArray[1] - data0.getP().get(kzEvent)) < 0.0001);
		assertEquals(true,
				Math.abs(pArray[2] - data0.getP().get(zkEvent)) < 0.0001);
		assertEquals(true,
				Math.abs(pArray[3] - data0.getP().get(zzEvent)) < 0.0001);

		// Testing Calculation of Likelihood
		assertEquals(
				true,
				Math.abs(6.2306e-007 - Math.pow(10, data0.getLikelihood())) < 0.00000000001);

	}

	/**
	 * Test method for
	 * {@link org.jalgo.module.em.control.EMModule#calcFirstStep(Map, StartParameters)}
	 * .
	 */
	@Test
	public void testCalcFirstStep() {
		EMData data0 = EMModule.calcFirstStep(singleEventStartProbability,
				startParameters);
		// Testing Calculation of P
		double[] pArray = { 0.25, 0.25, 0.25, 0.25 };
		assertEquals(true,
				Math.abs(pArray[0] - data0.getP().get(kkEvent)) < 0.0001);
		assertEquals(true,
				Math.abs(pArray[1] - data0.getP().get(kzEvent)) < 0.0001);
		assertEquals(true,
				Math.abs(pArray[2] - data0.getP().get(zkEvent)) < 0.0001);
		assertEquals(true,
				Math.abs(pArray[3] - data0.getP().get(zzEvent)) < 0.0001);

		// Testing Calculation of Likelihood
		assertEquals(
				true,
				Math.abs(4.76837158e-007 - Math.pow(10, data0.getLikelihood())) < 0.000000000000001);

	}
}
