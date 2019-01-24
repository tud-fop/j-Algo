package org.jalgo.module.em.data;

import java.awt.Point;
import java.util.Map;

/**
 * Stores all Data of one EM Step for one StartProbability.
 * @author kilian
 *
 */
public class EMData {
	private Map<Event, Double> p;
	private Map<Event, Double> d;
	private Map<Event, Double> h;
	private Map<Partition, Double> pForPartition;
	private Map<Point,Double> pForSingleEvent;
	private Double likelihood;

	/**
	 * The Constructor sets all the Stored Values.
	 * @param d Statistical Analyzer of the Events d(x)
	 * @param h estimated Frequency of the Events h(x)
	 * @param p the Probability of the Events p(x)
	 * @param pForPartition the Probability for the Partitions p'(y)
	 * @param pForSingleEvent the Probability for
	 * the each singleEvent, given in Point representation
	 * @param likelihood the Likelihood of the
	 * ProbabilityDistribution, in Relation to the y-corpus
	 * given in global StartParameters
	 */
	public EMData(final Map<Event, Double> d,
					final Map<Event, Double> h,
					final Map<Event, Double> p,
					final Map<Partition, Double> pForPartition,
					final Map<Point, Double> pForSingleEvent,
					final Double likelihood) {
		this.d = d;
		this.h = h;
		this.p = p;
		this.pForPartition = pForPartition;
		this.pForSingleEvent = pForSingleEvent;
		this.likelihood = likelihood;
	}

	/**
	 * @return the Probability for each Event in the current Step
	 */
	public final Map<Event, Double> getP() {
		return p;
	}

	/**
	 * @return the Statistical Analyzer for each Event in the current Step
	 */
	public final Map<Event, Double> getD() {
		return d;
	}

	/**
	 * @return the Frequency for each Event in the current Step
	 */
	public final Map<Event, Double> getH() {
		return h;
	}

	/**
	 * @return the Probability for each Partition in the current Step
	 */
	public final Map<Partition, Double> getPForPartition() {
		return pForPartition;
	}

	/**
	 * @return a map, where the Keys are
	 * singleEvents in Point Representation:
	 * Point.x = Side out of [1..]
	 * Point.y = number of the Object
	 * in the Experiment Vector out of [0..n-1]
	 */
	public final Map<Point, Double> getPForSingleEvent() {
		return pForSingleEvent;
	}
	/**
	 * @return Likelihood of the current Step
	 */
	public final Double getLikelihood() {
		return likelihood;
	}
}
