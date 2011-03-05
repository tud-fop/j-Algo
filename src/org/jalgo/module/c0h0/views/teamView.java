package org.jalgo.module.c0h0.views;

/**
 * allows a view to update an other view (observer pattern)
 * @author mathias.kaufmann
 */
public interface teamView {
	/**
	 * register a new team performer
	 * 
	 * @param member
	 */
	abstract public void registerTeamPerformerMember(View member);
	
	/**
	 * unregister a new team performer
	 * 
	 * @param member
	 */
	abstract public void unregisterTeamPerformerMember(View member);
	
	/**
	 * notify team performers
	 */
	abstract public void notifyTeamPerformers();
	
	/**
	 * returns the address of the team performer
	 * @return the address
	 */
	abstract public String getTeamPerformerAddress();
}