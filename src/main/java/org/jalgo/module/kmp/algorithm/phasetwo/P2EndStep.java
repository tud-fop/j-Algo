package org.jalgo.module.kmp.algorithm.phasetwo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import org.jalgo.main.util.Settings;
import org.jalgo.module.kmp.algorithm.KMPHighlighter;
import org.jalgo.module.kmp.gui.GUIConstants;

/**
 * This class specifies the concrete algorithmstate end of algorithm.
 *
 * @author Danilo Lisske, Elisa Böhl
 */
public class P2EndStep extends P2SmallStep {
	private static final long serialVersionUID = -5517573495563514487L;
	
	private boolean found;
	
	/**
	 * The constructor.
	 * 
	 * @param pp the pattern position
	 * @param tp the text position
	 * @param f if the pattern was found
	 */
	public P2EndStep(int pp, int tp, boolean f) {
		patpos = pp;
		textpos = tp;
		found = f;
	}

    /**
	 * Returns a boolean var, which descripes, if the Pattern was discovered (true).
	 * 
	 * @return if the pattern was found
	 */
	public boolean isPatternFound() {
		return found;
	}
	
	public String getDescriptionText() {
		String lang = Settings.getString("main","Language");
		if (found) {
			if (lang.equals("de")) 
				return "PatternPos ist größer als die Länge des Patterns. "
						+ "Das wurde Pattern gefunden an Textposition "
						+ (textpos - patpos) + ".";				
			else
				return "PatternPos is greater in number then the length of the pattern."
						+ " So the Pattern was found at Textposition " + (textpos - patpos) + ".";	
		}
		else {
			if (lang.equals("de"))
				return "Nur die Textlänge wurde überschritten, " +
						"das Pattern wurde nicht gefunden.";
			else
				return "The length of the text was exceeded." + "So the Pattern was not found.";
		}		
	}
	
	public List<KMPHighlighter> getKMPHighlighter() {
		List<KMPHighlighter> liste;
		liste = new LinkedList<KMPHighlighter>();
		if (found) {
			liste.add(new KMPHighlighter(274,301,GUIConstants.TRUE_COLOR));
			liste.add(new KMPHighlighter(307,376,GUIConstants.HIGHLIGHT_COLOR));
		}
		else {
			liste.add(new KMPHighlighter(274,301,GUIConstants.FALSE_COLOR));
			liste.add(new KMPHighlighter(382,415,GUIConstants.HIGHLIGHT_COLOR));
		}
		return liste;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeInt(patpos);
		out.writeInt(textpos);
		out.writeBoolean(found);
		out.writeObject(getDescriptionText());
		out.writeObject(getKMPHighlighter());
	}
	
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    	patpos = in.readInt();
    	textpos = in.readInt();
    	found = in.readBoolean();
		descriptiontext = (String) in.readObject();
		kmphighlights = (List<KMPHighlighter>) in.readObject();
    }
}