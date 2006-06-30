package org.jalgo.module.kmp.algorithm.phasetwo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.jalgo.module.kmp.algorithm.KMPHighlighter;

/**
 *This class specifies an abstract algorithmstate.
 *All classes, which implements this class, represents a normal state of the algorithm.
 *
 * @author Danilo Lisske, Elisa Böhl
 */

public abstract class P2SmallStep implements P2Step {
	/** The pattern position. */
	public int patpos;
	/** The text position. */
	public int textpos;
	/** The descriptiontext. */
	public String descriptiontext;
	/** The list filled with <code>KMPHighlighter</code>. */
	public List<KMPHighlighter> kmphighlights;
	
	public int getPatPos() {
		return patpos;
	}
	
	public int getTextPos() {
		return textpos;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeInt(patpos);
		out.writeInt(textpos);
		out.writeObject(getDescriptionText());
		out.writeObject(getKMPHighlighter());
	}

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    	patpos = in.readInt();
    	textpos = in.readInt();
    	descriptiontext = (String)in.readObject();
		kmphighlights = (List<KMPHighlighter>) in.readObject();
    }
}