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
 * This class specifies the concrete algorithmstate test of algorithm end.
 *
 * @author Danilo Lisske, Elisa Böhl
 */
public class P2OuterWhileStep extends P2SmallStep {
	private static final long serialVersionUID = -2597475389206219552L;
	
	private boolean lastwhilestep;
	
	/**
	 * The constructor.
	 * 
	 * @param pp the pattern position
	 * @param tp the text position
	 * @param lw if this is the last outer while step
	 */
	public P2OuterWhileStep(int pp, int tp, boolean lw) {
		patpos = pp;
		textpos = tp;
		lastwhilestep = lw;
	}
	
	public String getDescriptionText() {
		String lang = Settings.getString("main","Language");
		if (lastwhilestep) {
			if (lang.equals("de"))
				return "Es gibt hier nichts mehr zu Vergleichen. "
						+ "Es muss noch festgestellt werden, warum der Algorithmus beendet wurde.";
			else
				return "There is nothing left to be compared. "
						+ "Now it has to be determined, why the algorithm was aborted.";
		}
		else {
			if (lang.equals("de"))
				return "Text- oder Patternlänge wurden nicht überschritten, "
						+ "vergleiche einfach weiter.";
			else
				return "Text- or Patternlength were not exceeded, "
						+ "compare next signs.";
		}			
	}
	
	public List<KMPHighlighter> getKMPHighlighter() {
		List<KMPHighlighter> liste;
		liste = new LinkedList<KMPHighlighter>();
		if (lastwhilestep) liste.add(new KMPHighlighter(36,90,GUIConstants.FALSE_COLOR)); //while-header
		else liste.add(new KMPHighlighter(36,90,GUIConstants.TRUE_COLOR)); //while-header
		return liste;
	}	

	/**
	 * Returns a boolean value, which indicates, if the loop of the innerwhileloop is false (then the value is true).
	 * 
	 *  @return if this is the last outer while step
	 */
	public boolean isLastWhileStep() {
		return lastwhilestep;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeInt(patpos);
		out.writeInt(textpos);
		out.writeBoolean(lastwhilestep);
		out.writeObject(getDescriptionText());
		out.writeObject(getKMPHighlighter());
	}
	
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    	patpos = in.readInt();
    	textpos = in.readInt();
    	lastwhilestep = in.readBoolean();
		descriptiontext = (String) in.readObject();
		kmphighlights = (List<KMPHighlighter>) in.readObject();
    }
}