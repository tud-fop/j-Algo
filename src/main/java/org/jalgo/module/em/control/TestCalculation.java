package org.jalgo.module.em.control;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.jalgo.module.em.data.EMData;
import org.jalgo.module.em.data.EMState;
import org.jalgo.module.em.data.Event;
import org.jalgo.module.em.data.Partition;
import org.jalgo.module.em.data.StartParameters;

/**
 * Class for testing usage. You can use this to get a <i>hard coded</i> example.
 */
public class TestCalculation {

	/**
	 * You can use this for Testing the output-Views. You have to use the
	 * stepForward function, to calculate new Steps. For an example look at
	 * TestCalculation.main().
	 * 
	 * @return initialized LogState that has StartParameters for the Coin
	 *         Example and 3 StartProbabilityDistributions
	 */
	public static LogState createCoinExample() {
		StartParameters startParameters = createCoinExampleStartParams();

		return new LogState(startParameters);
	}

	/**
	 * Creates a test object of start parameters that can be used for testing
	 * the output views.
	 * <p>
	 * The example is very similar to the example given in the lecture.
	 * 
	 * @return a complete {@code StartParameters} object for test usage
	 */
	public static StartParameters createCoinExampleStartParams() {
		StartParameters startParameters = new StartParameters();

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

		Event kkEvent = new Event(kkEventVector);
		Event kzEvent = new Event(kzEventVector);
		Event zkEvent = new Event(zkEventVector);
		Event zzEvent = new Event(zzEventVector);

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
		elements3.add(zkEvent);
		elements1.add(zzEvent);

		// Creating Partitions
		Partition partition1 = new Partition(elements1, "0 mal Zahl"); // KK; h
																		// = 2
		Partition partition2 = new Partition(elements2, "1 mal Zahl"); // KZ ,
																		// ZK; h
																		// = 9
		Partition partition3 = new Partition(elements3, "2 mal Zahl"); // ZZ; h
																		// = 4

		partition1.setFrequency(9);
		partition2.setFrequency(2);
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
		Map<Point, Double> singleEventStartProbability = new HashMap<Point, Double>();
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

		EMState p0EMState = new EMState(p0DataList);
		startParameters.setP0EMState(p0EMState);

		return startParameters;
	}
	
	
	
	/**
	 * @param args
	 */
	
	
	public static void main(String[] args) {
		System.out.println(Double.MIN_VALUE);
		LogState myLogState = createCoinExample();
		//myLogState.setLogarithmicLikelihoodRepresentation(true);
		//Calculating 20 EMStates and setting the StepCount to 19
		//for (int i = 0; i<20; i++) myLogState.stepForward();
		myLogState.calcUntilLimit(0.01);
		System.out.println("P0 Count  " + myLogState.getP0Count());
		System.out.println("step Count" + myLogState.getStepCount());
		for (int p0 = 0; p0<myLogState.getP0Count(); p0++){
			//TableHeader
			System.out.println("Likelihood-Developement for p0 No. " + (p0+1));
			System.out.print(" Step:  Likelihood:          ");
			for(Event event: myLogState.getEvents()){
				String space = String.format("%"+ (8) + "s", "");
				System.out.print("p("+event.toString()+")" + space);
			}
			System.out.println();
			//TableRow
			for (int step = 0; step<= myLogState.getStepCount(); step++){
				System.out.format("  %03d   %.15f   ",step,myLogState.getL(p0,step));
				double p1K = myLogState.getPSingleEvent(p0, step, new Point(2,0));
				double p2K = myLogState.getPSingleEvent(p0, step, new Point(2,1));
				System.out.format(" %.15f   ",myLogState.getL3DGraph(p1K, p2K));
				for (Event event : myLogState.getEvents()){
					System.out.format(" %.15f",myLogState.getP(p0, step, event));
				}
				System.out.println();
				
			}
				System.out.println("\n");
				
		}
		for (int i = 0; i<90; i++) myLogState.stepBackward();
		
		System.out.println("P0 Count  " + myLogState.getP0Count());
		System.out.println("step Count" + myLogState.getStepCount());
		
		for (int p0 = 0; p0<myLogState.getP0Count(); p0++){
			//TableHeader
			System.out.println("Likelihood-Developement for p0 No. " + (p0+1));
			System.out.print(" Step:  Likelihood:          ");
			for(Event event: myLogState.getEvents()){
				String space = String.format("%"+ (8) + "s", "");
				System.out.print("p("+event.toString()+")" + space);
			}
			System.out.println();
			//TableRow
			for (int step = 0; step<= myLogState.getStepCount(); step++){
				System.out.format("  %03d   %.15f   ",step,myLogState.getL(p0,step));
				for (Event event : myLogState.getEvents()){
					System.out.format(" %.15f",myLogState.getP(p0, step, event));
				}
				System.out.println();
				
			}
				System.out.println("\n");
				
		}
		//for (int i = 0; i<63; i++) myLogState.singleStepForward();
		
		System.out.println("P0 Count  " + myLogState.getP0Count());
		System.out.println("step Count" + myLogState.getStepCount());
		
		
		for (int p0 = 0; p0<myLogState.getP0Count(); p0++){
			//TableHeader
			System.out.println("Likelihood-Developement for p0 No. " + (p0+1));
			System.out.print(" Step:  Likelihood:          ");
			for(Event event: myLogState.getEvents()){
				String space = String.format("%"+ (8) + "s", "");
				System.out.print("p("+event.toString()+")" + space);
			}
			System.out.println();
			//TableRow
			for (int step = 0; step<= myLogState.getStepCount(); step++){
				System.out.format("  %03d   %.15f   ",step,myLogState.getL(p0,step));
				for (Event event : myLogState.getEvents()){
					System.out.format(" %.15f",myLogState.getP(p0, step, event));
				}
				System.out.println();
				
			}
				System.out.println("\n");
				
		}
		//System.out.println(myLogState.getL(0, 25));
		
		for(int p0 = 0; p0<myLogState.getP0Count(); p0++){
			for(int iterationStep = 0; iterationStep < myLogState.getStepCount();iterationStep++){
				System.out.print(myLogState.getPSingleEvent(p0, iterationStep, new Point(2,0)));
				System.out.print(" ");
				System.out.print(myLogState.getPSingleEvent(p0, iterationStep, new Point(2,1)));
				System.out.print(" ");
				System.out.println(myLogState.getL(p0, iterationStep));
			}
			System.out.println();
		}
		System.out.println(Math.log10(0.0)*0);
	}

}
