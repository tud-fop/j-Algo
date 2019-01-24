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
 * This class implements the P1WhileStep. It marks the position in KMP original
 * sourcecode when the while head is getting evaluated and possibliy the while
 * body is executed.
 * 
 * @author Matthias Neubert
 */
public class P1WhileStep extends P1SmallStep {
	private static final long serialVersionUID = 2635832786142045084L;

	private boolean lastwhilestep;
	private int condition;
	private int myOldVglInd;

	/**
	 * Constructor method (differs from P1SmallStep).
	 * 
	 * @param pp Pattern position (PatPos)
	 * @param vi compare index (VglInd)
	 * @param lw boolean is true when this is the last P1WhileStep in this FOR
	 *           loop pass
	 * @param cond int which tells what condtion was the reason for escaping from
	 *             or keeping in the while loop cond is 0 if the conditions are
	 *             fullfilled, 1 if VglInd < 0 and 2 if the characters are equal
	 * @param oldVglind the old VglInd
	 */
	public P1WhileStep(int pp, int vi, boolean lw, int cond, int oldVglind) {
		patpos = pp;
		vglind = vi;
		lastwhilestep = lw;
		condition = cond;
		myOldVglInd = oldVglind;

	}

	/**
	 * Returns the occured condition in the head of the while loop.
	 * 
	 * @return int which tells what condtion was the reason for escaping from or
	 *         keeping in the while loop cond is 0 if the conditions are
	 *         fullfilled, 1 if VglInd < 0 and 2 if the characters are equal
	 */
	public int getCond() {
		return condition;
	}

	/**
	 * Returns status of beeing the last P1WhileStep or notfor this step.
	 * 
	 * @return bool is true if this is the last P1WhileStep
	 */
	public boolean isLastWhileStep() {
		return lastwhilestep;
	}

	/**
	 * Returns the VlgInd of the step on which the production of this step is
	 * based on. This is used to mark the usage of former entries in the
	 * shiftingtable with a colour.
	 * 
	 * @return int which is the former vglind
	 */
	public int getOldVglind() {
		return myOldVglInd;
	}

	public String getDescriptionText() {
		String lang = Settings.getString("main", "Language");
		if (!lastwhilestep) {
			if (lang.equals("de"))
				return "1. Prüfe, ob der V-Zeiger den Anfang des Pattern noch nicht überschritten hat."
						// + Constants.SEPARATOR
						+ " 2. Prüfe, ob der Buchstabe an Stelle V ungleich dem an Stelle P ist."
						+ Constants.SEPARATOR
						+ "Beide Bedingungen waren eben erfüllt. "
						+ "Deshalb wurde V-Zeiger auf den Indexwert, der bei ihm eben in der "
						+ "Verschiebetabelle stand, verschoben. (Also auf "
						+ vglind
						+ ")"
						+ " Wiederhole dies, bis eine der beiden Bedingungen nicht erfüllt wird.";
			else
				return "Whilestep, not the last one.";
		} else {
			if (lang.equals("de")) {
				if (condition == 1)
					return "Die 1. Bedingung (der while-Schleife) wurde nicht erfüllt."
							+ Constants.SEPARATOR
							+ "Der V-Zeiger hat den Anfang des Pattern überschritten."
							+ " Der Schleifenkörper wird nicht durchlaufen.";
				if (condition == 2)
					return "Die 2. Bedingung (der while-Schleife) wurde nicht erfüllt."
							+ Constants.SEPARATOR
							+ "Ein zu P gleicher Buchstabe wurde gefunden! Der Schleifenkörper wird nicht durchlaufen.";
			} else {
				if (condition == 1)
					return "Whilestep, the last one_reason 1.";
				if (condition == 2)
					return "Whilestep, the last one_reason 2.";
			}
		}
		return "";
	}

	public List<KMPHighlighter> getKMPHighlighter() {
		List<KMPHighlighter> liste;
		liste = new LinkedList<KMPHighlighter>();
		if (!lastwhilestep) {
			liste.add(new KMPHighlighter(231, 284, GUIConstants.TRUE_COLOR));
			liste
					.add(new KMPHighlighter(293, 320,
							GUIConstants.HIGHLIGHT_COLOR));
		} else
			liste.add(new KMPHighlighter(231, 284, GUIConstants.FALSE_COLOR));
		return liste;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeInt(patpos);
		out.writeInt(vglind);
		out.writeBoolean(lastwhilestep);
		out.writeInt(condition);
		out.writeObject(getDescriptionText());
		out.writeObject(getKMPHighlighter());
	}

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		patpos = in.readInt();
		vglind = in.readInt();
		lastwhilestep = in.readBoolean();
		condition = in.readInt();
		descriptiontext = (String) in.readObject();
		kmphighlights = (List<KMPHighlighter>) in.readObject();
	}

}