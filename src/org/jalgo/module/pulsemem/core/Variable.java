package org.jalgo.module.pulsemem.core;

import org.jalgo.module.pulsemem.core.exceptions.EIncompatibleTypes;
import org.jalgo.module.pulsemem.core.exceptions.EMemoryError;
import org.jalgo.module.pulsemem.core.exceptions.EWriteConstant;
import org.jalgo.module.pulsemem.core.exceptions.ENotAPointerException;

/**
 * This class makes objects from our C00-Variables
 *
 * @author Joachim Protze
 */
public class Variable {
	protected String name=null;
	protected Integer value=null;
	protected Boolean constant = false;
	protected Boolean global = false;
	protected int visibilityLevel=0;
	protected VarInfo vi=null;

	/**
	 * Declare variable with initialisation
	 * @param name
	 * @param constant
	 * @param visibilityLevel
	 * @param value
	 */
	public Variable(String name, Boolean constant, int visibilityLevel, int value, VarInfo vi){
		this.name=name;
		this.visibilityLevel=visibilityLevel;
		this.value=value;
		this.constant=constant;
		this.vi=vi;
	}

	/**
	 * Declare variable w/o initialisation
	 * @param name
	 * @param constant
	 * @param visibilityLevel
	 */
	public Variable(String name, Boolean constant, int visibilityLevel, VarInfo vi){
		this.name=name;
		this.visibilityLevel=visibilityLevel;
		this.constant=constant;
		this.vi=vi;
	}

	/**
	 * setze Informationen zur Variable
	 * @param Informationen zur Variable
	 */
	public void setVi(VarInfo vi){
		this.vi=vi;
	}

	/**
	 * returns Informationen zur Variable
	 * @return Informationen zur Variable
	 */
	public VarInfo getVi(){
		return vi;
	}

	/**
	 * returns the variablen-name
	 * @return variablen-name
	 */
	public String getName() {
		return name;
	}

	/**
	 * returns the actual value of the variable
	 * @return actual value of the variable
	 */
	public Object getValue() {
		if (value==null)
			throw new EMemoryError(this.name);
		return value;
	}

	/**
	 * set the value of the variable
	 * @param value
	 */
	public void setValue(Object value) {
		if (constant)
			throw new EWriteConstant(this.name);
		if (!(value instanceof Integer))
			throw new EIncompatibleTypes("Int","Pointer",this.name);
		this.value = (Integer)value;
	}

	/**
	 * is the variable global declared?
	 * @return is the variable global declared?
	 */
	public Boolean isGlobal() {
		return global;
	}

	/**
	 * makes the variable global declared
	 * @param global
	 */
	public void setGlobal(Boolean global) {
		this.global = global;
	}

	/**
	 * returns dereferenced value
	 * @return dereferenced value
	 */
	public Object getTargetValue() {
		throw new ENotAPointerException(this.name);
	}

	/**
	 * returns referencation status
	 * @return referencation status
	 */

	public Boolean isReferenced(){
		throw new ENotAPointerException(this.name);
	}

	/**
	 * set dereferenced value
	 * @param value
	 */
	public void setTargetValue(Object value) {
		throw new ENotAPointerException(this.name);
	}

	/**
	 * returns a clone of the Object
	 * @return a clone of the Object
	 */
	public Variable clone(){
		Variable var;
		if (value==null)
			var=new Variable(this.name,this.constant,this.visibilityLevel,this.vi);
		else
			var=new Variable(this.name,this.constant,this.visibilityLevel,this.value,this.vi);
		var.setGlobal(global);
		return var;
	}

	/**
	 * returns the visibility Level
	 * @return visibility Level
	 */
	public int getVisibilityLevel() {
		return visibilityLevel;
	}
	public Boolean isConstant() {
		return constant;
	}
	public String toString(){
//		return name + "("+visibilityLevel+")"+vi+" = " + value;
		return name + "("+visibilityLevel+") = " + value;
	}
}
