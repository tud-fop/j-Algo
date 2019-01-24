package org.jalgo.module.kmp.algorithm.phasetwo;

import java.util.LinkedList;
import java.util.List;

import org.jalgo.main.util.Settings;
import org.jalgo.module.kmp.algorithm.KMPHighlighter;
import org.jalgo.module.kmp.gui.GUIConstants;

/**
 * This class specifies the concrete algorithmstate increment of the intern values.
 *
 * @author Danilo Lisske, Elisa Böhl
 */
public class P2InnerSetStep extends P2BigStep {
	private static final long serialVersionUID = -7963450147338716845L;

	/**
	 * The constructor.
	 * 
	 * @param pp the pattern position
	 * @param tp the text position
	 */
	public P2InnerSetStep(int pp, int tp) {
		patpos = pp;
		textpos = tp;
	}
	
	public String getDescriptionText() {
		String lang = Settings.getString("main","Language");
		if (lang.equals("de"))
			return "Das Sichtfenster wurde nach rechts auf die Position ("
					+ textpos + ", " + patpos + ") verschoben.";				
		else
			return "The window was shifted left to position ("
					+ textpos + ", " + patpos + ").";
	}
	
	public List<KMPHighlighter> getKMPHighlighter() {
		List<KMPHighlighter> liste;
		liste = new LinkedList<KMPHighlighter>();
		liste.add(new KMPHighlighter(211,234,GUIConstants.HIGHLIGHT_COLOR)); //set: inc(patpos); 
		liste.add(new KMPHighlighter(238,267,GUIConstants.HIGHLIGHT_COLOR)); //set: inc(textpos);
		return liste;
	}
}