package org.jalgo.module.pulsemem.core;

import org.jalgo.module.pulsemem.core.exceptions.ENullPointer;
import org.jalgo.module.pulsemem.core.exceptions.EPointerTargetException;

/**
 * This class makes objects from our C00-Pointers
 *
 * @author Joachim Protze
 */
public class Zeiger extends Variable {
	protected Variable target=null;

	/**
	 * Declare Pointer with initialisation of destination target
	 * @param name
	 * @param target
	 * @param visibilityLevel
	 */
	public Zeiger(String name, Variable target, int visibilityLevel, VarInfo vi){
		super(name, false, visibilityLevel, vi);
		this.target=target;
	}

	/**
	 * Declare Pointer w/o initialisation of destination target
	 * @param name
	 * @param visibilityLevel
	 */
	public Zeiger(String name, int visibilityLevel, VarInfo vi){
		super(name, false, visibilityLevel,vi);
	}

	/**
	 * returns the destination target
	 * @return destination target
	 */
	public Variable getTarget() {
		if (target == null)
			throw new ENullPointer(this.name);
		return target;
	}

	/**
	 * returns referencation status
	 * @return referencation status
	 */

	public Boolean isReferenced(){
		return (target != null);
	}
	/**
	 * set the destination target
	 * @param target
	 */

	public void setTarget(Variable target) {
		this.target = target;
	}

	/**
	 * returns target Variable
	 * @return target Variable
	 */

	public Object getValue() {
		if (target == null)
			throw new ENullPointer(this.name);
		return target;
	}

	/**
	 * returns dereferenced value
	 * @return dereferenced value
	 */
	public Object getTargetValue() {
		if (target == null)
			throw new ENullPointer(this.name);
		return target.getValue();
	}

	/**
	 * set dereferenced value
	 * @param value
	 */
	public void setValue(Object value) {
		if (!(value instanceof Variable))
			throw new EPointerTargetException(this.name);
		target=(Variable)value;
	}

	/**
	 * set dereferenced value
	 * @param value
	 */
	public void setTargetValue(Object value) {
		if (target == null)
			throw new ENullPointer(this.name);
		target.setValue(value);
	}

	/**
	 * Clones the object with setting new destination target
	 * @param target
	 * @return cloned Pointer
	 */
	public Variable clone(Variable target) {
		Zeiger var=new Zeiger(this.name,target,this.visibilityLevel,this.vi);
		var.setGlobal(global);
		return var;
	}

	/**
	 * Clones the object w/o setting new destination target
	 * @return cloned Pointer
	 */
	public Variable clone() {
		Zeiger var=new Zeiger(this.name,this.target,this.visibilityLevel,this.vi);
		var.setGlobal(global);
		return var;
	}

	/**
	 * returns a String "*name(visibilityLevel) = value"
	 * z.B *j(1) = 5
	 */
	public String toString(){
//		String ret="*" +name + "("+visibilityLevel+")"+vi+" = ";
		String ret="*" +name + "("+visibilityLevel+") = ";
		if (isReferenced()){
			ret+=target.toString();
		}else{
			ret+="n/a";
		}
		return ret;
	}
}
