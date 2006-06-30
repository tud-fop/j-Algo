package org.jalgo.module.kmp.algorithm.phaseone;

import java.util.LinkedList;
import java.util.List;

import org.jalgo.main.util.Settings;
import org.jalgo.module.kmp.algorithm.KMPHighlighter;
import org.jalgo.module.kmp.gui.GUIConstants;

/**
 * This class implements the <code>P1EndForStep</code>.
 * 
 * @author Matthias Neubert
 */
public class P1EndForStep extends P1SmallStep {
	private static final long serialVersionUID = -4149353033472562689L;

	/**
	 * The constructor.
	 * 
	 * @param pp the pattern position
	 * @param vi the compare index
	 */
	public P1EndForStep(int pp, int vi) {
		patpos = pp;
		vglind = vi;
	}

	public String getDescriptionText() {
		String lang = Settings.getString("main", "Language");
		if (lang.equals("de"))
			return "Rücke den V-Zeiger um eins nach rechts. "
					+ "Aktuell: Vergleichsindex=" + vglind + ".";

		else
			return "End of for loop.";
	}

	public List<KMPHighlighter> getKMPHighlighter() {
		List<KMPHighlighter> liste;
		liste = new LinkedList<KMPHighlighter>();
		liste.add(new KMPHighlighter(324, 344, GUIConstants.HIGHLIGHT_COLOR));
		return liste;
	}
}