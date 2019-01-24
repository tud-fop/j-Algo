package org.jalgo.module.pulsemem.core;

import java.io.Serializable;
import java.util.List;

import org.jalgo.module.pulsemem.core.exceptions.EPMLException;

/**
 * This class provides a pulsing memory line.
 * Contains number of the codeline, returnlabel-stack and variable-stack
 *
 * @author Joachim Protze
 */
public class PulsMemLine extends Stack implements Serializable{
	private int codeLine;
	private List<Integer> ruecksprungMarken;
	private int label=0;
	/**
	 *
	 * @param codeLine
	 * @param ruecksprungMarken
	 * @param stack
	 */
	public PulsMemLine(int codeLine, List<Integer> ruecksprungMarken, List<Variable> stack, int visibilityLevel){
		this.codeLine=codeLine;
		this.ruecksprungMarken=ruecksprungMarken;
		this.stack=stack;
		this.visibilityLevel=visibilityLevel;
	}
	/**
	 *
	 * @param codeLine
	 * @param ruecksprungMarken
	 * @param stack
	 * @param label
	 */
	public PulsMemLine(int codeLine, List<Integer> ruecksprungMarken, List<Variable> stack, int visibilityLevel, int label){
		this.codeLine=codeLine;
		this.ruecksprungMarken=ruecksprungMarken;
		this.stack=stack;
		this.visibilityLevel=visibilityLevel;
		this.label=label;
	}
	/**
	 * returns the C00-Codelinenumber of the PML
	 * @return C00-Codelinenumber of the PML
	 */
	public int getCodeLine() {
		return codeLine;
	}
	/**
	 * returns the returnlabel-stack
	 * @return returnlabel-stack
	 */
	public List<Integer> getRuecksprungMarken() {
		return ruecksprungMarken;
	}
	/**
	 * formated CLI-like String of the PML
	 * z.B.: Zeile: 7 | a(0) = 5 | i(0) = null | a(1) = 5 | *j(1) = 5 | i(1) = null |
	 */
	public String toString(){
		String ret="Zeile: "+codeLine+" | ";
		for (int i : ruecksprungMarken){
			ret += " $" + i + " ";
		}
		for (Variable i : stack){
			ret += "| " + i.toString() + " ";
		}
		ret += "|\n";
		return ret;
	}

	/**
	 *
	 * @return
	 */
	public Boolean isLabel(){
		return label != 0;
	}

	/**
	 * returns the Labelnumber of this PulsMemLine
	 * @return
	 */
	public int getLabel(){
		return label;
	}

	/**
	 * formated CLI-like String of the PML
	 * z.B.: Zeile: 7 | a(0) = 5 | i(0) = null | a(1) = 5 | *j(1) = 5 | i(1) = null |
	 */
	public String visibleToString(){
		String ret="Zeile: "+codeLine+" | ";
		for (int i : ruecksprungMarken){
			ret += " $" + i + " ";
		}
		for (Variable i : getVisibleStack()){
			ret += "| " + i.toString() + " ";
		}
		ret += "||";
		for (Variable i : getPointedStack()){
			ret += "| " + i.toString() + " ";
		}
		ret += "|\n";
		return ret;
	}
	@Override
	public void addVar(Variable var) {
		throw new EPMLException();
	}
	@Override
	public void decreaseVisibilityLevel() {
		throw new EPMLException();
	}
	@Override
	public void increaseVisibilityLevel() {
		throw new EPMLException();
	}
	@Override
	public void removeAllToplevelVar() {
		throw new EPMLException();
	}
	@Override
	public void removeVar(Variable var) {
		throw new EPMLException();
	}
	@Override
	public void setVisibilityLevel(int visibilityLevel) {
		throw new EPMLException();
	}

}
