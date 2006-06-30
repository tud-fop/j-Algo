package org.jalgo.module.kmp.algorithm.phasetwo;

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
 * This class specifies the concrete algorithmstate test and patternshifting.
 *
 * @author Danilo Lisske, Elisa Böhl
 */
public class P2InnerWhileStep extends P2SmallStep {
	private static final long serialVersionUID = -2917571422573814032L;
	
	private boolean lastwhilestep;
	private int jumpdistance;
	private int patternlength;
	
	/**
	 * The constructor.
	 * 
	 * @param pp the pattern position
	 * @param tp the text position
	 * @param lw if this is the last while step
	 * @param jd the jump distance
	 * @param pl the pattern length
	 */
	public P2InnerWhileStep(int pp, int tp, boolean lw, int jd, int pl) {
		patpos = pp;
		textpos = tp;
		lastwhilestep = lw;
		jumpdistance = jd;
		patternlength = pl;
	}
		
	public String getDescriptionText() {
		String lang = Settings.getString("main","Language");
		if (lastwhilestep) {
			if (patpos < 0) {
				if (lang.equals("de"))
					return "Das Sichtfenster steht vor dem Pattern, es kann nichts verglichen werden.";
				else
					return "The condition of the while loop PatternPos >= 0 is not true." + Constants.SEPARATOR
							+ " So the window is ahead of the pattern, there is nothing to be compared.";			
			} else {
				if (lang.equals("de"))
					return "Die Zeichen im Sichtfenster sind identisch. Pattern und Textausschnitt sind " +
							"eventuell gleich, es müssen mehr Stellen verglichen werden.";
				else
					return "here the characters in the window are equal. The pattern is not moved "
							+ "because more positions have to be compared.";
			}
		}
		else {			
			int ersatz = patpos;
			if (ersatz<0) ersatz = 0;
			if (lang.equals("de")){
				String intro = "Die while-Bedingung ist erfüllt. " +
						"Das Pattern wurde auf PatPos " + patpos  + " (" ;
				String distance = jumpdistance+" Positionen nach rechts) verschoben. ";
				if (jumpdistance == 1) distance = "eine Position nach rechts) verschoben. ";
				String expl = "Die Zeichen waren bis an Patternposition " + (jumpdistance + patpos)
				+ " gleich, daher ist die nächste Gleichheit erst mit PatPos = " 
				+ patpos + " möglich (Anwenden der in Phase eins ermittelten Daten). ";
					
				
				if (patpos + jumpdistance < 1)
					expl = "Keine Gleichheit zwischen Pattern und Suchtext, " +
							"daher wird der Patternanfang an TextPos+1 gelegt. ";
							
				
				if (jumpdistance + ersatz -1 < 1)
					expl = expl	+ "Eingesparte Vergleiche = 0.";
				else
					expl = expl	+ "Eingesparte Vergleiche >= " + (jumpdistance + ersatz - 1) + ".";
				return intro + distance + expl;
				
			}
			else {
				String intro = "The Pattern was shifted right at PatPos " + patpos 	+ " (by " ;
				String distance = jumpdistance+" positions). ";
				if (jumpdistance == 1) distance = "one position). ";
				String expl = "Up to Patternposition " + (jumpdistance + patpos)
				+ " the characters were equal, so it is known that the next equality is possible at PatPos = " 
				+ patpos + ". This information can be derived from the structure of the pattern solely, "
				+ "And that is, what the first stage does. There was/were economized ";
				if (jumpdistance + ersatz -1 == 1)
					expl = expl	+ "at least one comparison in contrast to the naive search.";
				else
					if (jumpdistance + ersatz -1 <= 1)
						expl = expl	+ "no comparisons in contrast to the naive search.";
					else
						expl = expl	+ "at least " + (jumpdistance + ersatz - 1) + " comparisons in contrast to the naive search";
				
			
				if (patpos + jumpdistance < 1)
					expl = "As there was no equality between pattern and searchtext, " +
							"the pattern was shifted along the text to its beginning. " +
							"There were economized no comparisons in contrast to the naive search " +
							"because the calculated table could not be used successfully.";
				
				return intro + distance + expl;
			}
		}				
	}

	public List<KMPHighlighter> getKMPHighlighter() {
		List<KMPHighlighter> liste;
		liste = new LinkedList<KMPHighlighter>();
		if (lastwhilestep) 
			liste.add(new KMPHighlighter(105,164,GUIConstants.FALSE_COLOR)); //while-header
		else {
			liste.add(new KMPHighlighter(105,164,GUIConstants.TRUE_COLOR)); // while-header
			liste.add(new KMPHighlighter(173,207,GUIConstants.HIGHLIGHT_COLOR)); //true-case
		}
		return liste;
	}

	/**
	 * Returns a boolean value, which indicates, if the loop of the innerwhileloop is false (then the value is true).
	 * 
	 * @return if this is the last while step
	 */
	public boolean isLastWhileStep(){
		return lastwhilestep;		
	}
	
	/**
	 * Returns the distance, which have to be shiftet at this step.
	 * 
	 * @return the jumpdistance
	 */
	public int getJumpDistance() {
		return jumpdistance;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeInt(patpos);
		out.writeInt(textpos);
		out.writeBoolean(lastwhilestep);
		out.writeInt(jumpdistance);
		out.writeInt(patternlength);
		out.writeObject(getDescriptionText());
		out.writeObject(getKMPHighlighter());
	}
	
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    	patpos = in.readInt();
    	textpos = in.readInt();
    	lastwhilestep = in.readBoolean();
    	jumpdistance = in.readInt();
    	patternlength = in.readInt();
		descriptiontext = (String) in.readObject();
		kmphighlights = (List<KMPHighlighter>) in.readObject();
    }
}