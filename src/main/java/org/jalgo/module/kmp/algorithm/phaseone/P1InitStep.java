package org.jalgo.module.kmp.algorithm.phaseone;

import java.util.LinkedList;
import java.util.List;

import org.jalgo.main.util.Settings;
import org.jalgo.module.kmp.Constants;
import org.jalgo.module.kmp.algorithm.KMPHighlighter;
import org.jalgo.module.kmp.gui.GUIConstants;

/**
 * This class implements the initial step of phase one. It marks the start of
 * the shifting table build process It is allway created with patpos==0 and
 * vglind==0 KMP usually initializes with patpos==-1 but for some reason we had
 * to select 0 and interpret it to patpos arrow is invisible.
 * 
 * @author Matthias Neubert
 */
public class P1InitStep extends P1SmallStep {
	private static final long serialVersionUID = -5512637554518269829L;

	/**
	 * The constructor.
	 * 
	 * @param pp the pattern position
	 * @param vi the compare index
	 */
	public P1InitStep(int pp, int vi) {
		patpos = pp;
		vglind = vi;
	}

	public String getDescriptionText() {
		String lang = Settings.getString("main", "Language");
		if (lang.equals("de"))
			return "Initialisierung: Der Verschiebetabelleneintrag an der Stelle 0 wird auf -1 gesetzt. "
					+ "Der Vergleichsindex wird auf 0 gesetzt.";
		else
			return "Initialisation: The shifting table entry at position 0 is set to -1. "
					+ "The compare index is set to 0."
					+ Constants.SEPARATOR
					+ "Meaning: the algorithm starts.";
	}

	public List<KMPHighlighter> getKMPHighlighter() {
		List<KMPHighlighter> liste;
		liste = new LinkedList<KMPHighlighter>();
		liste.add(new KMPHighlighter(0, 29, GUIConstants.HIGHLIGHT_COLOR));
		return liste;
	}
}
