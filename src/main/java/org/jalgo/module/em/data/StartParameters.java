package org.jalgo.module.em.data;

import java.awt.Point;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import org.jalgo.main.util.Messages;

/**
 * StartParameters will hold all Information, that is collected during the input
 * phase. Only these values will be stored, if the StoreFunctionality is used.
 * 
 * @author kilian
 * @version Alpha
 * 
 * 
 */
public class StartParameters {

	private Set<Event> events;
	private Set<Partition> observations;
	private EMState p0EMState;
	private Vector<Integer> experiment;
	private Double frequencySum;
	private int type;
	private int par1;
	private int par2;

	/**
	 * Creates a new {@code StartParameters} object without any initialized
	 * values.
	 */
	public StartParameters() {
		type = 0;
		par1 = -1;
		par2 = -1;

	}

	/**
	 * The experiments {@code Event}s.
	 * 
	 * @return set of all events of this experiment
	 */
	public Set<Event> getEvents() {
		return events;
	}

	/**
	 * Sets the possible {@code Event}s of this experiment to the specified
	 * value.
	 * 
	 * @param events
	 *            set of all possible {@code Event}s
	 */
	public void setEvents(Set<Event> events) {
		this.events = events;
	}

	/**
	 * Sets the {@link org.jalgo.module.em.data.EMState} in the start parameters
	 * to the specified value.
	 * 
	 * @param p0EMState
	 *            the new state value
	 */
	public void setP0EMState(EMState p0EMState) {
		this.p0EMState = p0EMState;
	}

	/**
	 * The current observation set.
	 * 
	 * @return the current observation set as {@code Set<Partition>}
	 */
	public Set<Partition> getObservations() {
		return this.observations;
	}

	/**
	 * Sets the observation set to the specified value.
	 * 
	 * @param observations
	 *            the new observation set as a {@code Set<Partition>}
	 */
	public void setObservations(Set<Partition> observations) {
		this.observations = observations;
	}

	/**
	 * 
	 * @return the Sum of the Frequency from all Partitions
	 */
	public Double getFrequencySum() {
		frequencySum = .0;
		for (Partition partition : observations) {
			frequencySum += partition.getFrequency();
		}
		return frequencySum;
	}

	/**
	 * 
	 * @return Number of Objects in the Experiment Vector.
	 */
	public int getObjectCount() {
		return experiment.size();
	}

	/**
	 * 
	 * @return the Experiment Vector - {@code null} if the vector is not
	 *         initialized
	 */
	public Vector<Integer> getExperiment() {
		return this.experiment;
	}

	/**
	 * 
	 * @return the Initial EMState, with p0, p'0 and L0 for each
	 *         p0-distribution.
	 */
	public EMState getP0EMState() {
		return p0EMState;
	}

	/**
	 * 
	 * @return a set with all SingleExperiments
	 */
	public Set<Point> getSingleExperiments() {
		Set<Point> singleExperiments = new HashSet<Point>();
		for (int object = 0; object < experiment.size(); object++) {
			for (int count = 1; count <= experiment.get(object); count++) {
				singleExperiments.add(new Point(count, object));
			}
		}
		return singleExperiments;
	}

	/**
	 * Sets the experiment vector to the specified vector.
	 * 
	 * @param experiment
	 *            the new experiment vector as {@code Vector<Integer>}
	 */
	public void setExperiment(Vector<Integer> experiment) {
		this.experiment = new Vector<Integer>(experiment);
	}

	/**
	 * 
	 * @return Vector with String representations of the objects' names (like
	 *         "dice 1", "coin 1", "coin2",...)
	 */
	public String[] getObjectNames() {
		String[] objectNames = new String[experiment.size()];
		Map<Integer, Integer> map = new TreeMap<Integer, Integer>();
		for (int i = 0; i < experiment.size(); i++) {
			int sides = experiment.get(i);
			if (!(map.containsKey(sides))) {
				map.put(sides, 1);
			}
			objectNames[i] = this.ObjectName(experiment.get(i)) + " "
					+ map.get(sides);
			map.put(sides, map.get(sides) + 1);
		}
		return objectNames;
	}

	/**
	 * Returns the name of the specified object. This name is the same as in the
	 * names array returned by {@link #getObjectNames()} at position
	 * {@code index}.
	 * 
	 * @param index
	 *            the index of the object whose name is inquired
	 * @return the name of the specified object, e.g. 'coin 2', 'dice 5', ...
	 */
	public String getObjectName(int index) {
		if (index < experiment.size())
			return getObjectNames()[index];
		return null;
	}

	/**
	 * Generates a 'human readable' representation of the object (e.g. coin,
	 * dice, ...) or a symbolic representation like 'W7".
	 * 
	 * @param sides
	 *            count of object's sides
	 * 
	 * @return A 'human readable" representation of the object (e.g. coin, dice,
	 *         ...)
	 */
	private String ObjectName(Integer sides) {
		String key = "ObjectName." + sides;
		String name = Messages.getString("em", key);
		if (name.equals("!" + key + "!")) {
			name = Messages.getString("em", "ObjectName") + sides;
		}
		return name;
	}

	/**
	 * @param singleEvent
	 *            the Single Event in Point Representation
	 * @return a String of the Form " Objekt y Side x"
	 */
	public final String singleEventToString(final Point singleEvent) {
		StringBuilder eventString = new StringBuilder();
		eventString.append(getObjectName(singleEvent.y));
		eventString.append(": ");
//		eventString.append(Messages.getString("em", "StartParameters.side"));
//		eventString.append(" ");
		if (experiment.get(singleEvent.y) != 2) {
			eventString.append(String.format("%d", singleEvent.x));
		} else {
			eventString.append((singleEvent.x == 1)
					? Messages.getString("em", "StartParameters.Tail")
							: Messages.getString("em", "StartParameters.Head"));
		}
		return eventString.toString();
	}

	/**
	 * The type of the partitioning, so that it can be
	 * detected and restored.
	 * @return A length 3 Array, 
	 * 				that encodes the type of the partitioning.
	 */
	public int[] getObservationType() {
		int[] observationType = { type, par1, par2 };
		return observationType;
	}

	/**
	 * Sets the type of the partitioning, so that it can be
	 * detected when it is loaded.
	 * @param observationType A length 3 Array, 
	 * 							that encodes the type of the partitioning.
	 */
	public void setObservationType(int[] observationType) {
		this.type = observationType[0];
		this.par1 = observationType[1];
		this.par2 = observationType[2];
	}
}
