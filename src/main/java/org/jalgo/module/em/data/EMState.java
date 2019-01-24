package org.jalgo.module.em.data;

import java.util.List;

/**
 * The <code>EMState</code> holds all the <code>EMData</code> for the different
 * StartProbabilityDistributions that belong to one EMStep.
 * 
 * @author kilian
 */
public class EMState {
	/**
	 * The <code>EMData</code> that belong to the EMStep, ordered according to
	 * the Index of their StartProbabilityDistribution.
	 */
	private List<EMData> data;

	/**
	 * Creates the <code>EMState</code>, by setting the <code>EMData</code>. The
	 * <code>EMData</code> have to be ordered in the List according to the index
	 * of the StartProbabilityDistribution they belong to.
	 * 
	 * @param data the ordered list of {@code EMData}
	 */
	public EMState(final List<EMData> data) {
		this.data = data;
	}

	/**
	 * @return a List with <code>EMData</code> that belong to this EMSTep,
	 *         ordered by StartProbabilityIndex.
	 */
	public final List<EMData> getEMData() {
		return data;
	}
}