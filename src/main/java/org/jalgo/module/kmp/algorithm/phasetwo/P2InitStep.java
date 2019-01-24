package org.jalgo.module.kmp.algorithm.phasetwo;

import java.util.LinkedList;
import java.util.List;

import org.jalgo.main.util.Settings;
import org.jalgo.module.kmp.algorithm.KMPHighlighter;
import org.jalgo.module.kmp.gui.GUIConstants;

/**
 * This class specifies the concrete algorithmstate initialisation of algorithm.
 *
 * @author Danilo Lisske, Elisa Böhl
 */
public class P2InitStep extends P2SmallStep {
	private static final long serialVersionUID = 4453507936499153868L;

	/**
	 * The constructor.
	 * 
	 * @param pp the pattern position
	 * @param tp the text position
	 */
	public P2InitStep(int pp, int tp) {
		patpos = pp;
		textpos = tp;
	}
	
	public String getDescriptionText() {
		String lang = Settings.getString("main","Language");
		if (lang.equals("de"))
			return "Initialisierung: PatternPos und TextPos werden auf 0 gesetzt."
					+ " Das Sichtfenster (stellt PatternPos und TextPos grafisch dar) "
					+ "wird an den Anfang von Text und Pattern gesetzt. Sichtfensterposition " +
							"(TextPos, PatPos) = (0, 0)";			
		else
			return "Initialisation: PatternPos and TextPos are set to 0." 
					+ " The window, which represents PatternPos and TextPos graphically, "
					+ "is set to the beginning of text and Pattern.";	
	}
	
	public List<KMPHighlighter> getKMPHighlighter() {
		List<KMPHighlighter> liste;
		liste = new LinkedList<KMPHighlighter>();
		liste.add(new KMPHighlighter(0,29,GUIConstants.HIGHLIGHT_COLOR));
		return liste;
	}
}