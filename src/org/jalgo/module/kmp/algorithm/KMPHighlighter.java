package org.jalgo.module.kmp.algorithm;

import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.Color;
import java.io.Serializable;
import java.io.IOException;

/**
 * The highlighters for the <code>InfoTabbedPanel</code>.
 * 
 * @author Danilo Lisske, Sebastian Patschorke
 */
public class KMPHighlighter implements Serializable {
	private static final long serialVersionUID = 4582211301082985368L;
	
	private int startpos;
	private int endpos;
	private Color hlpcolor;
	
	/**
	 * The constructor if the <code>KMPHighlighter</code>.
	 * 
	 * @param start the start position
	 * @param end the end position
	 * @param color the color of the highlight
	 */
	public KMPHighlighter(int start, int end, Color color) {
		startpos = start;
		endpos = end;
		hlpcolor = color;
	}
	
	/**
	 * Returns the start position.
	 * 
	 * @return the start position
	 */
	public int getStartPos() {
		return startpos;
	}
	
	/**
	 * Returns the end position.
	 * 
	 * @return the end position
	 */
	public int getEndPos() {
		return endpos;
	}
	
	/**
	 * Returns the <code>Highlighter.HighlightPainter</code> of this highlight.
	 * 
	 * @return the <code>Highlighter.HighlightPainter</code>
	 */
	public Highlighter.HighlightPainter getHightlightPainter() {
		return new DefaultHighlighter.DefaultHighlightPainter(hlpcolor);
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeInt(startpos);
		out.writeInt(endpos);
		out.writeObject(hlpcolor);
	}
	
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		startpos = in.readInt();
		endpos = in.readInt();
		hlpcolor = (Color)in.readObject();
	}
}