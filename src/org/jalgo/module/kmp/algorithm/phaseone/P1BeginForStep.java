package org.jalgo.module.kmp.algorithm.phaseone;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import org.jalgo.main.util.Settings;
import org.jalgo.module.kmp.Constants;
import org.jalgo.module.kmp.algorithm.KMPHighlighter;
import org.jalgo.module.kmp.gui.GUIConstants;

/**
 * This class implements the <code>P1BeginForStep</code>. It marks the
 * position in KMP original sourcecode when the if condition in the FOR loop
 * evaluated.
 * 
 * @author Matthias Neubert
 */
public class P1BeginForStep extends P1BigStep {
	private static final long serialVersionUID = -8505363387847297109L;

	private boolean lastforstep;

	/**
	 * The constructor.
	 * 
	 * @param pp the pattern position
	 * @param vi the compare index
	 * @param lf if this is the last for step
	 */
	public P1BeginForStep(int pp, int vi, boolean lf) {
		patpos = pp;
		vglind = vi;
		lastforstep = lf;
	}

	/**
	 * Returns status of beeing the last P1BeginForStep or not for this step
	 * 
	 * @return bool is true if this is the last P1BeginForStep
	 */
	public Boolean isLastForStep() {
		return lastforstep;
	}

	public String getDescriptionText() {
		String lang = Settings.getString("main", "Language");
		if (!lastforstep) {
			if (lang.equals("de"))
				return "Rücke nun den P-Zeiger um eins nach rechts. "
						+ "Aktuell: PatPos=" + patpos
						+ ". Das Ende des Pattern ist noch nicht erreicht.";
			else
				return "The for loop is not over, increase the patternposition.";
		} else {
			if (lang.equals("de"))
				return "Das Ende des Pattern ist nun erreicht."
						+ Constants.SEPARATOR
						+ "Die Verschiebetabelle ist fertig!";
			else
				return "The for loop is over, this is the end of the algorithm.";
		}
	}

	public List<KMPHighlighter> getKMPHighlighter() {
		if (!lastforstep) {
			List<KMPHighlighter> liste;
			liste = new LinkedList<KMPHighlighter>();
			liste.add(new KMPHighlighter(34, 45, GUIConstants.HIGHLIGHT_COLOR));
			liste.add(new KMPHighlighter(46, 74, GUIConstants.TRUE_COLOR));
			liste.add(new KMPHighlighter(75, 94, GUIConstants.HIGHLIGHT_COLOR));
			return liste;
		} else {
			List<KMPHighlighter> liste;
			liste = new LinkedList<KMPHighlighter>();
			liste.add(new KMPHighlighter(34, 45, GUIConstants.HIGHLIGHT_COLOR));
			liste.add(new KMPHighlighter(46, 74, GUIConstants.FALSE_COLOR));
			liste.add(new KMPHighlighter(75, 94, GUIConstants.HIGHLIGHT_COLOR));
			return liste;
		}
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeInt(patpos);
		out.writeInt(vglind);
		out.writeBoolean(lastforstep);
		out.writeObject(getDescriptionText());
		out.writeObject(getKMPHighlighter());
	}

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		patpos = in.readInt();
		vglind = in.readInt();
		lastforstep = in.readBoolean();
		descriptiontext = (String) in.readObject();
		kmphighlights = (List<KMPHighlighter>) in.readObject();
	}
}