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
 * This class implements the <code>P1IfFalseStep</code>. It marks the
 * position in KMP original sourcecode when the if condition in the FOR loop is
 * not fullfilled so the two letters are different.
 * 
 * @author Matthias Neubert
 */
public class P1IfFalseStep extends P1SmallStep {
	private static final long serialVersionUID = 7621445479791563738L;

	char myV;

	char myP;

	/**
	 * Constructor method (differs from P1SmallStep).
	 * 
	 * @param pp Pattern position (PatPos)
	 * @param vi compare index (VglInd)
	 * @param p character at postion of PatPos
	 * @param v character at position if VglInd
	 */
	public P1IfFalseStep(int pp, int vi, char p, char v) {
		patpos = pp;
		vglind = vi;
		myP = p;
		myV = v;
	}

	public String getDescriptionText() {
		String lang = Settings.getString("main", "Language");
		if (lang.equals("de"))
			return "Vergleiche die Buchstaben an den Stellen V und P: "
					+ Constants.SEPARATOR
					+ "'"
					+ myV
					+ "' ungleich '"
					+ myP
					+ "'."
					+ " Deshalb schreibe den Indexwert von Stelle V in den Tabelleneintrag an Stelle P."
					+ " Beachte den Tabelleneintrag an der Stelle V."
					+ " Auf diese Position wird der V-Zeiger im nächsten Schritt verschoben";
		else
			return "The if-condition is false.";
	}

	public List<KMPHighlighter> getKMPHighlighter() {
		List<KMPHighlighter> liste;
		liste = new LinkedList<KMPHighlighter>();
		liste.add(new KMPHighlighter(106, 140, GUIConstants.FALSE_COLOR));
		liste.add(new KMPHighlighter(194, 219, GUIConstants.HIGHLIGHT_COLOR));
		return liste;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeInt(patpos);
		out.writeInt(vglind);
		out.writeChar(myP);
		out.writeChar(myV);
		out.writeObject(getDescriptionText());
		out.writeObject(getKMPHighlighter());
	}

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		patpos = in.readInt();
		vglind = in.readInt();
		myP = in.readChar();
		myV = in.readChar();
		descriptiontext = (String) in.readObject();
		kmphighlights = (List<KMPHighlighter>) in.readObject();
	}
}