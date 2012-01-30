package org.jalgo.module.em.control;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.jalgo.module.em.data.EMData;
import org.jalgo.module.em.data.EMState;
import org.jalgo.module.em.data.Event;
import org.jalgo.module.em.data.Partition;
import org.jalgo.module.em.data.StartParameters;

/**
 * LogState is the Class, that controls the calculation of new EM Steps, has the
 * DataStructure in which new EM steps are included and provides access to the
 * calculated data to the Output-Classes. <br>
 * LogState stores as well, up to which step, the calculated data should be
 * visualized by the Output-Classes <br>
 * <br>
 * A new instance LogState should be created, after all StartParameters were set
 * correctly. Changing the StartParameters afterwards can cause Problems!
 * 
 * @author kilian
 * 
 */
public class LogState {
	private List<EMState> emStates;
	private StartParameters startParameters;
	private int step;
	private int singleStep;
	private boolean logarithmicLikelihoodRepresentation;

	/**
	 * Creates a new Instance of LogState, sets StartParameters, the Step and
	 * the Default LikelihoodRepresentation to nonLogarithmic.
	 * 
	 * @param startParameters
	 *            Complete and valid startParameters, that should not be changed
	 *            afterwards.
	 */
	public LogState(final StartParameters startParameters) {
		this.startParameters = startParameters;
		emStates = new ArrayList<EMState>();
		emStates.add(startParameters.getP0EMState());
		step = 0;
		logarithmicLikelihoodRepresentation = false;
	}

	/**
	 * calculates the Next EMState and inserts it into the emState List.
	 */
	private void calcNextState() {
		List<EMData> nextEMDataBlocks = new ArrayList<EMData>();
		for (EMData currentData : emStates.get(emStates.size() - 1).getEMData()) {
			nextEMDataBlocks.add(EMModule.calcNextEMData(currentData, emStates
					.get(emStates.size() - 1).getEMData().indexOf(currentData),
					startParameters));
		}
		emStates.add(new EMState(nextEMDataBlocks));
	}

	/**
	 * calcUntilLimit calculates new EMSteps until the difference of
	 * Likelihood[i] and Likelihood[i - 1]) falls below limit (percentage)
	 * 
	 * @param limit
	 *            > 0, where 100% = 1.0 and 1% = 0.01
	 */
	public void calcUntilLimit(double limit) {
		boolean status;
		if (emStates.size() > 1) {
			status = false;
			for (int p0Index = 0; p0Index < getP0Count(); p0Index++) {
				if ((this.getLogarithmicL(p0Index,
						this.emStates.size() - 1)
						- this.getLogarithmicL(p0Index,
								this.emStates.size() - 2)) > Math.log10(1 + limit)) {
					status = true;
				}
			}
		} else {
			status = true;
		}
		//System.out.println(limit);
		while (status) {
			calcNextState();
			status = false;
			for (int p0Index = 0; p0Index < getP0Count(); p0Index++) {
				if ((this.getLogarithmicL(p0Index,
						this.emStates.size() - 1)
						- this.getLogarithmicL(p0Index,
								this.emStates.size() - 2)) > Math.log10(1 + limit)) {
					status = true;
				}
			}
			this.step = emStates.size() - 1;
			this.singleStep = 0;
		}
		
		int stepIndex = this.emStates.size() - 1;
		status = true;
		while ((stepIndex > 0) && status) {
			status = true;
			for (int p0Index = 0; p0Index < getP0Count(); p0Index++) {
				if ((this.getLogarithmicL(p0Index, stepIndex)
						- this.getLogarithmicL(p0Index, stepIndex - 1)) > Math.log10(1 + limit)) {
					status = false;
				}
			}
			stepIndex--;
		}
		if (stepIndex != 0 || status == false) {
			this.step = stepIndex + 2;
		} else {
			this.step = stepIndex + 1;
		}
		//this.singleStep = 0;
	}

	/**
	 * Gives direct access to the Likelihood in a certain step.
	 * 
	 * @param p0Index
	 *            for 0 <= p0 < getP0Count()
	 * @param step
	 *            for 0 <= step <= getStepCount()
	 * @return Likelihood, in Logarithmic or 'normal' Representation, depending
	 *         on <code>setLogarithmicLikelihoodRepresentation(boolean)</code>
	 * @throws IndexOutOfBoundsException
	 *             if step > the amount of calculated steps or p0 >=
	 *             getP0Count()
	 */
	public final Double getL(final int p0Index, final int step) {
		if (this.logarithmicLikelihoodRepresentation) {
			return getLogarithmicL(p0Index, step);
		} else {
			return Math.pow(10, getLogarithmicL(p0Index, step));
		}
	}

	/**
	 * Gives direct access to the Likelihood in a certain step.
	 * 
	 * @param p0Index
	 *            for 0 <= p0 < getP0Count()
	 * @param step
	 *            for 0 <= step <= getStepCount()
	 * @return Likelihood in Logarithmic Representation
	 * @throws IndexOutOfBoundsException
	 *             if step > the amount of calculated steps or p0 >=
	 *             getP0Count()
	 */
	private final Double getLogarithmicL(final int p0Index, final int step) {
		return emStates.get(step).getEMData().get(p0Index).getLikelihood();
	}

	/**
	 * gives direct access to the Probability of a SingleEvent in a certain step
	 * 
	 * @param p0Index
	 *            for 0 <= p0 < getP0Count()
	 * @param step
	 *            for 1 <= step <= getStepCount()
	 * @param singleEvent
	 *            the SinlgeEvent in Point Representation
	 * @return the probability of the singleEvent
	 * @throws IndexOutOfBoundsException
	 *             if step > the amount of calculated steps or p0 >=
	 *             getP0Count()
	 */
	public final Double getPSingleEvent(final int p0Index, final int step,
			final Point singleEvent) {
		return emStates.get(step).getEMData().get(p0Index).getPForSingleEvent()
				.get(singleEvent);
	}

	/**
	 * gives direct access to the Statistical Analyzer of an <code>Event</code>
	 * in a certain step.
	 * 
	 * @param p0Index
	 *            for 0 <= p0 < getP0Count()
	 * @param step
	 *            for 1 <= step <= getStepCount()
	 * @param event
	 *            the {@code Event} whose statistic analyzer is questioned
	 * @return the statistic analyzer of an event
	 * @throws NullPointerException
	 *             if step = 0
	 * @throws IndexOutOfBoundsException
	 *             if step > the amount of calculated steps or p0 >=
	 *             getP0Count()
	 * 
	 */
	public final Double getD(final int p0Index, final int step,
			final Event event) throws NullPointerException,
			IndexOutOfBoundsException {
		return emStates.get(step).getEMData().get(p0Index).getD().get(event);
	}

	/**
	 * gives direct access to the Frequency of an <code>Event</code> in a
	 * certain step.
	 * 
	 * @param p0Index
	 *            for 0 <= p0 < getP0Count()
	 * @param step
	 *            for 1 <= step <= getStepCount()
	 * @param event the {@code Event} whose frequency is questioned
	 * @return the estimated frequency of an event
	 * @throws NullPointerException
	 *             if step = 0
	 * @throws IndexOutOfBoundsException
	 *             if step > the amount of calculated steps or p0 >=
	 *             getP0Count()
	 */
	public final Double getH(final int p0Index, final int step,
			final Event event) {
		return emStates.get(step).getEMData().get(p0Index).getH().get(event);
	}

	/**
	 * gives direct access to the Probability of an <code>Event</code> in a
	 * certain step
	 * 
	 * @param p0Index
	 *            for 0 <= p0 < getP0Count()
	 * @param step
	 *            for 0 <= step <= getStepCount()
	 * @param event
	 *            the {@code Event} whose probability is questioned
	 * @return the probability of an event
	 * @throws IndexOutOfBoundsException
	 *             if step > the amount of calculated steps or p0 >=
	 *             getP0Count()
	 */
	public Double getP(int p0Index, int step, Event event) {
		return emStates.get(step).getEMData().get(p0Index).getP().get(event);
	}

	/**
	 * Calculates the Likelihood for a 2-Coin Experiment from given
	 * probabilities for "K" - Side of each Coin. The probability of the "Z" -
	 * Side is implicated ( 1 - p1(K) ).
	 * 
	 * @param p1K
	 *            = p1(K) out of [0 .. 1]
	 * @param p2K
	 *            = p2(K) out of [0 .. 1]
	 * @return Likelihood
	 */
	public final double getL3DGraph(final double p1K, final double p2K) {
		HashMap<Point, Double> singleEventProbability = new HashMap<Point, Double>();
		singleEventProbability.put(new Point(2, 0), p1K);
		singleEventProbability.put(new Point(1, 0), 1 - p1K);
		singleEventProbability.put(new Point(2, 1), p2K);
		singleEventProbability.put(new Point(1, 1), 1 - p2K);
		if (! isLogarithmicLikelihoodRepresentationEnabled()) {
			return Math.pow(10,
					EMModule.calcFirstStep(singleEventProbability, startParameters)
							.getLikelihood());
		} else {
			return EMModule.calcFirstStep(singleEventProbability, startParameters)
							.getLikelihood();
		}
		
	}

	/**
	 * Checks, whether the Experiment consists of 2 coins, and therefore a
	 * 3D-Graph can be plotted. => <code>double getL3DGraph(..)</code>
	 * 
	 * @return true if a 3D Graph can be plotted, or false instead.
	 */
	public final boolean activate3D() {
		if ((startParameters.getObjectCount() == 2)
				&& (startParameters.getExperiment().get(0) == 2)
				&& (startParameters.getExperiment().get(1) == 2)) {
			return true;
		}
		return false;
	}

	/**
	 * @return Number of StartProbability-Distributions
	 */
	public final int getP0Count() {
		return startParameters.getP0EMState().getEMData().size();
	}

	/**
	 * Returns the yield of the specified event.
	 * 
	 * @param event
	 *            the {@code Event} whose yield is questioned
	 * @return <code>Partition</code> in which the <code>Event</code> is
	 *         located.
	 */
	public final Partition getYield(final Event event) {
		return event.getYield();
	}

	/**
	 * @return the Experiment Vector
	 */
	public final Vector<Integer> getExperiment() {
		return startParameters.getExperiment();
	}

	/**
	 * generates a Set with SingleExperiments.
	 * 
	 * @return all SingleExperiments
	 */
	public final Set<Point> getSingleEvents() {
		return startParameters.getSingleExperiments();
	}

	/**
	 * @return a Set with all <code>Event</code>s
	 */
	public final Set<Event> getEvents() {
		return startParameters.getEvents();
	}

	/**
	 * @return the step, up to which the Data shall be displayed (inclusive
	 *         step)
	 */
	public final int getStepCount() {
		return step;
	}

	/**
	 * @return the amount singleSteps, that have to be added to the "full" step
	 *         => range is [0 , 2] - where 3 SingleSteps are 1 "full Step" =
	 *         "step"
	 */
	public final int getSingleStepCount() {
		return singleStep;
	}

	/**
	 * @return the step, up to which the Data shall be displayed after the
	 *         increase
	 */
	public final int singleStepForward() {
		singleStep++;
		if (singleStep == 3) {
			singleStep = 0;
			step++;
		}
		if (emStates.size() < step + 5) {
			calcNextState();
		}
		return singleStep;
	}

	/**
	 * @return the singleStep, up to which the Data shall be displayed after the
	 *         decrease
	 */
	public final int singleStepBackward() {
		singleStep--;
		if (singleStep == -1) {
			if (step > 0) {
				singleStep = 2;
				step--;
			} else {
				singleStep = 0;
			}
		}
		return singleStep;
	}

	/**
	 * @return the step, up to which the Data shall be displayed after the
	 *         decrease
	 */
	public final int stepForward() {
		step++;
		if (emStates.size() < step + 5) {
			calcNextState();
		}
		return step;
	}

	/**
	 * @return the step, up to which the Data shall be displayed after decrease
	 */
	public final int stepBackward() {
		step--;
		if (step < 0) {
			step = 0;
			singleStep = 0;
		}
		return step;
	}

	/**
	 * The Likelihood is naturally in range of 0..1 and has the tendency to
	 * become very small. Therefore all operations are performed on the
	 * logarithm of the Likelihood.<br>
	 * If this Option is set, <code>getL(int p0, int step)</code> will return
	 * the Logarithm of the Likelihood.
	 * 
	 * @param enable
	 *            <code>true</code>, if it the Option should be enabled,
	 *            <code>false</code> otherwise
	 */
	public final void setLogarithmicLikelihoodRepresentation(
			final boolean enable) {
		logarithmicLikelihoodRepresentation = enable;
	}

	/**
	 * The Likelihood is naturally in range of 0..1 and has the tendency to
	 * become very small Therefore all operations are performed on the logarithm
	 * of the Likelihood. <br>
	 * If <code>setLogarithmicLikelihoodRepresentation(boolean enable)</code>
	 * was set, <code>getL(int p0, int step)</code> will return the Logarithm of
	 * the Likelihood.
	 * 
	 * @return <code>true</code>, if LogarithmicLikelihoodRepresentation is
	 *         enabled, false <code>otherwise</code>
	 */
	public final boolean isLogarithmicLikelihoodRepresentationEnabled() {
		return logarithmicLikelihoodRepresentation;
	}

	/**
	 * 
	 * @return Vector with String representations of the objects' names (like
	 *         "dice 1", "coin 1", "coin2",...)
	 */
	public final String[] getObjectNames() {
		return startParameters.getObjectNames();
	}
	
	/**
	 * @param singleEvent
	 *            the Single Event in Point Representation
	 * @return a String of the Form " Objekt y Side x"
	 */
	public final String singleEventToString(Point singleEvent){
		return startParameters.singleEventToString(singleEvent);
	}
}
