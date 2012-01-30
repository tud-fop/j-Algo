package org.jalgo.module.em.control;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import org.jalgo.module.em.data.EMData;
import org.jalgo.module.em.data.Event;
import org.jalgo.module.em.data.Partition;
import org.jalgo.module.em.data.StartParameters;
/**
 * <code>EMModule</code> is responsible for calculating the EMSteps.<br>
 * This Module is the implementation of the EM Algorithm
 * for the Data Structure of this Project.
 * 
 * @author Kilian Gebhardt
 *
 */
public class EMModule {
	/**
	 * This function will calculate the next ProbabilityDistribution,
	 * Statistical Analyzer, the approximated Frequency for each <code>Event</code>,
	 * as well as the Probability Distribution for all SingleEvents and
	 * <code>Partition</code>s. At last it will calculation the Likelihood of this
	 * Distributions concerning the situation described in <code>StartParameters</code>.
	 * 
	 * <code>calcNextEMData</code> takes one <code>EMData</code> and produces the next <code>EMData</code>, which means, that is has to be
	 * applied on all <code>EMData</code> of one <code>EMState</code>, and the new <code>EMData</code> have to be composed to the next <code>EMState</code>
	 * 
	 * @param currentData		the predecessor of the step, that is going to be calculated
	 * @param p0Index			index of p<sub>0</sub>
	 * @param startParameters	the {@code StartParameters} object of this experiment
	 * @return	the 'next' EM Step
	 */
	public static EMData calcNextEMData(final EMData currentData, final int p0Index, final StartParameters startParameters) {
		//calculating the next Statistical Analyzer d_i(x)
		Map<Event, Double> nextStatisticalAnalyzer = new HashMap<Event, Double>();
		for (Event event : startParameters.getEvents()) {
			if (currentData.getPForPartition().get(event.getYield()) == 0.0) {
				nextStatisticalAnalyzer.put(event,
						startParameters.getP0EMState().getEMData()
						.get(p0Index).getP().get(event)
						/ startParameters.getP0EMState().getEMData()
						.get(p0Index).getPForPartition().get(event.getYield()));
			} else {
				nextStatisticalAnalyzer.put(event,
					currentData.getP().get(event) 
					/ currentData.getPForPartition().get(event.getYield()));
			}
		}

		//calculating the Frequency h(x)
		Map<Event, Double> nextEventFrequency = new HashMap<Event, Double>();
		for (Event event : startParameters.getEvents()) {
			nextEventFrequency.put(event,
					event.getYield().getFrequency()
					* nextStatisticalAnalyzer.get(event));
		}
		
		//calculating the singleEvent Probability (O(n * m))
		
		Map<Point, Double> nextSingleEventProbability = new HashMap<Point, Double>();
		for (Point singleEvent : currentData.getPForSingleEvent().keySet()) {
			Double probability = new Double(0);

			for (Event event : startParameters.getEvents()) {
				if (event.getTuple().get(singleEvent.y) == singleEvent.x) {
					probability += nextEventFrequency.get(event);
				}

			nextSingleEventProbability.put(singleEvent,
					probability
					/ startParameters.getFrequencySum());
			}
		}
		
		//calculating the singleEventProbability (O(m + n))
		/*
		int max = startParameters.getExperiment().get(0);
		for (int i = 1; i < startParameters.getObjectCount(); i++) {
			if (startParameters.getExperiment().get(i) > max) {
				max = startParameters.getExperiment().get(i);
			}
		}
		double[][] singleEventProbabilityArray = new double[max][startParameters.getObjectCount()];
		for (int x = 0; x < max; x++) {
			for (int y = 0; y < startParameters.getObjectCount(); y++) {
				singleEventProbabilityArray[x][y] = 0;
			}
		}
		Map<Point, Double> nextSingleEventProbability = new HashMap<Point, Double>();
				
		for (Event event : startParameters.getEvents()) {
			for (int objectIndex = 0; objectIndex < startParameters.getObjectCount(); objectIndex++) {
				singleEventProbabilityArray[event.getTuple().get(objectIndex) - 1][objectIndex] += nextEventFrequency.get(event);
			}
		}
		
		for (Point singleEvent : currentData.getPForSingleEvent().keySet()) {
			nextSingleEventProbability.put(singleEvent, singleEventProbabilityArray[singleEvent.x - 1][singleEvent.y]
					/ startParameters.getFrequencySum());
		}
		*/
		//calculating the Probability p(x)
		Map<Event, Double> nextEventProbability = new HashMap<Event, Double>();
		for (Event event : startParameters.getEvents()) {
			Double probability = new Double(1);

			for (int objectIndex = 0;
					objectIndex < event.getTuple().size();
					objectIndex++) {
				probability *=
						nextSingleEventProbability
						.get(new Point(event.getTuple()
								.get(objectIndex), objectIndex));
			}

			nextEventProbability.put(event, probability);
		}

		//calculating the Probability for the Partitions p'(y)
		Map<Partition, Double> nextPartitionProbability = new HashMap<Partition, Double>();
		for (Partition partition : startParameters.getObservations()) {
			Double probability = new Double(0);

			for (Event event : partition.getElements()) {
				probability += nextEventProbability.get(event);
			}

			nextPartitionProbability.put(partition, probability);
		}

		//calculating the Likelihood L
		Double likelihood = new Double(0);
		for (Partition partition : startParameters.getObservations()) {
			if (partition.getFrequency() == 0) {
				likelihood += 0;
			} else {
				likelihood += partition.getFrequency()
						* Math.log10(nextPartitionProbability.get(partition));
			}
		}

		return new EMData(nextStatisticalAnalyzer,
				nextEventFrequency,
				nextEventProbability,
				nextPartitionProbability,
				nextSingleEventProbability,
				likelihood);
		}

	/**
	 * Calculates the probability for each event and partition and 
	 * the likelihood, based on the initial ProbabilityDistribution
	 * This function is used for creating the <code>EMData</code>s for P0EMState 
	 * and for calculating the 3DPlot.
	 * 
	 * @param singleEventStartProbability	the initial probability distribution
	 * @param startParameters	the {@code StartParameters} object of this experiment
	 * @return EMData, where the Maps for 'd' and 'h' are null !
	 */
	public static EMData calcFirstStep(
			final Map<Point, Double> singleEventStartProbability,
			final StartParameters startParameters) {

		//calculating the Probability p(x)
		Map<Event, Double> eventStartProbability = new HashMap<Event, Double>();
		for (Event event : startParameters.getEvents()) {
			Double probability = new Double(1);

			for (int objectIndex = 0;
					objectIndex < event.getTuple().size();
					objectIndex++) {
				probability *=
						singleEventStartProbability.get(new Point(event.getTuple().get(objectIndex), objectIndex));
			}

			eventStartProbability.put(event, probability);
		}

		//calculating the Probability for the Partitions p'(y)
		Map<Partition, Double> partitionStartProbability = new HashMap<Partition, Double>();
		for (Partition partition : startParameters.getObservations()) {
			Double probability = new Double(0);

			for (Event event : partition.getElements()) {
				probability += eventStartProbability.get(event);
			}

			partitionStartProbability.put(partition, probability);
		}

		//calculating the Likelihood L
		Double likelihood = new Double(0);
		for (Partition partition : startParameters.getObservations()) {
			if (partition.getFrequency() == 0) {
				likelihood += 0;
			} else {
				likelihood += partition.getFrequency()
						* Math.log10(partitionStartProbability.get(partition));
			}
		}

		return new EMData(null,
				null,
				eventStartProbability,
				partitionStartProbability,
				singleEventStartProbability,
				likelihood);
	}
}