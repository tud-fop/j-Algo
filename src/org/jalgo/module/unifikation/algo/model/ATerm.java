package org.jalgo.module.unifikation.algo.model;

public abstract class ATerm implements ITerm {
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof ITerm)) return false;
		return this.toString().equalsIgnoreCase(obj.toString());
	}
	
	@Override
	public int hashCode(){
		return super.hashCode();
	}	
	
	public boolean equalsName(ITerm other){
		return getName(false).equalsIgnoreCase(other.getName(false)); 
	}

}
