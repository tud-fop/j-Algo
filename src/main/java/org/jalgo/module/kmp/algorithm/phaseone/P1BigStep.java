package org.jalgo.module.kmp.algorithm.phaseone;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.List;
import org.jalgo.module.kmp.algorithm.KMPHighlighter;

/**
 * This abstract class implements the interface P1Step partially Only
 * P1BeginForStep is an big step.(P1BigStep) This marks the P1BeginForStep as an
 * big step like a flag but this solution is more potent to control movement on
 * the history.
 * 
 * @author Matthias Neubert
 */
public abstract class P1BigStep implements P1Step {
	/** The pattern position. */
	public int patpos;
	/** The compare index. */
	public int vglind;
	/** The descriptiontext. */
	public String descriptiontext;
	/** The list filled with <code>KMPHighlighter</code>. */
	public List<KMPHighlighter> kmphighlights;

	/**
	 * Returns the actual value of the pattern position (PatPos) of this step.
	 * 
	 * @return value of pattern position of this step
	 */
	public int getPatPos() {
		return patpos;
	}

	/**
	 * Returns the actual value of the comparing position (Vergleichsindex) of
	 * this step.
	 * 
	 * @return value of comparing position (Vergleichsindex) of this step
	 */
	public int getVglInd() {
		return vglind;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeInt(patpos);
		out.writeInt(vglind);
		out.writeObject(getDescriptionText());
		out.writeObject(getKMPHighlighter());
	}

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		patpos = in.readInt();
		vglind = in.readInt();
		descriptiontext = (String) in.readObject();
		kmphighlights = (List<KMPHighlighter>) in.readObject();
	}
}