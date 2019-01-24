package org.jalgo.module.kmp.algorithm;

import org.jalgo.module.kmp.Constants;
import java.util.LinkedList;

/**
 * The <code>Pattern</code> for the algorithm.
 * 
 * @author Danilo Lisske
 */
public class Pattern {
	private String pattern;
	private int[] shiftingtable;
	private boolean ispatternset =  false;
	private boolean isexpandable = true;
	private LinkedList cycletable;
	
	/**
	 * Sets the <code>Pattern</code>.
	 * 
	 * @param pattern the pattern
	 */
	public void setPattern(String pattern) {
		if(!(pattern == "" || pattern == null)) {
			if(pattern.length() <= Constants.MAX_PAT_LENGTH) {
				this.pattern = pattern;
				ispatternset = true;
				buildShiftingTable();
				buildCycleTable();
				if(pattern.length() == Constants.MAX_PAT_LENGTH) isexpandable = false;
				else isexpandable = true;
			}
		}
		else ispatternset = false;
	}
	
	/**
	 * Returns the pattern as a <code>Pattern</code> object.
	 * 
	 * @return the <code>Pattern</code>
	 */
	public String getPattern() {
		return pattern;
	}
	
	/**
	 * Checks if the pattern is already set.
	 * 
	 * @return if the pattern is set
	 */
	public boolean isPatternSet() {
		return ispatternset;
	}
	
	/**
	 * Checks if the pattern is expandable.
	 * 
	 * @return if the pattern is expandable
	 */
	public boolean isPatternExpandable() {
		return isexpandable;
	}
	
	/**
	 * Returns the Character at the specified position.
	 * 
	 * @param index the position
	 * @return the character
	 */
	public char getCharAt(int index) {
		return pattern.charAt(index);
	}
	
	/**
	 * Returns the shiftingtable entry at the specified position.
	 * 
	 * @param index the position
	 * @return the shiftingtable entry
	 */
	public int getTblEntryAt(int index) {
		return shiftingtable[index];
	}
	
	/**
	 * Sets the shiftingtable entry at the specified position to the
	 * specified value.
	 * 
	 * @param index the position
	 * @param value the value
	 */
	public void setTblEntryAt(int index, int value) {
		shiftingtable[index] = value;
	}
	
	private void buildShiftingTable() {
		shiftingtable = new int[pattern.length()];
		shiftingtable[0] = -1;
		int vglind = 0;

		for (int patpos = 1; patpos < pattern.length(); patpos++)
		{
			if (pattern.charAt(patpos) == pattern.charAt(vglind))
				shiftingtable[patpos] = shiftingtable[vglind];
			else shiftingtable[patpos] = vglind;

			while ((vglind >= 0) && (pattern.charAt(patpos) != pattern.charAt(vglind)))
				vglind = shiftingtable[vglind];
			vglind++;
		}
	}
	
	private void buildCycleTable() {	
		cycletable = new LinkedList<int[]>();
		int[] cycledepthtable = new int[pattern.length()];
		int[] workdata;
		int vglind = 0;
		int workpos = 0;
		int i;
		
		for (int patpos = 1; patpos < pattern.length(); patpos++){
			vglind = 0;
			if (pattern.charAt(0) == pattern.charAt(patpos)){
				workpos = patpos;
				while( (workpos < pattern.length()) && (pattern.charAt(vglind) == pattern.charAt(workpos))) {
					cycledepthtable[workpos]++;
					workpos++; vglind++;					
				}			
			}
		}
		
		cycletable.clear();
		workdata = new int[1]; workdata[0] = 0;
		cycletable.add(0, workdata);
		for (int patpos = 1; patpos < pattern.length(); patpos++){
			workdata =  new int[(cycledepthtable[patpos])+1];
			for (i=0; i<(cycledepthtable[patpos])+1;i++) 
				workdata[i] = 0;			
			cycletable.add(patpos, workdata);
		}

		for (int patpos = 1; patpos < pattern.length(); patpos++){
			vglind = 0;
			if (pattern.charAt(0) == pattern.charAt(patpos)){
				workpos = patpos;
				while((workpos < pattern.length()) && (pattern.charAt(vglind) == pattern.charAt(workpos))) {
					workdata = (int[]) cycletable.get(workpos);
					i=0;
					while (workdata[i] != 0) i++;					
					workdata[i] = patpos;
					cycletable.set(workpos, workdata);
					workpos++; vglind++;					
				}
			}
		}
	}		
	
	/**
	 * Returns an intarray with the cycle data.
	 * 
	 * @param index
	 * @return an intarray with the cycle data
	 */
	public int[] getCycleDataAt(int index) {
		int[] work = new int[((int[])cycletable.get(index)).length -1];			
		System.arraycopy((int[])cycletable.get(index), 0, work, 0, ((int[])cycletable.get(index)).length -1);
		return work;
	}
	
	public String toString() {
		String shifttable = "";
		String cycletable = "";
		for (int i = 0; i < shiftingtable.length; i++) {
			shifttable += shiftingtable[i];
		}		
		for (int j = 0; j < pattern.length(); j++) {
			cycletable += "[";	
			for (int i = 0; i < (getCycleDataAt(j)).length; i++)	{
				cycletable += (int) ((int[]) (getCycleDataAt(j)))[i] + ",";
				}
			cycletable += "] ";
		}		
		return "Pattern = " + pattern + "\nVerschiebeTabelle = " + shifttable + "\nZyklenInfo = " + cycletable;
	}
}