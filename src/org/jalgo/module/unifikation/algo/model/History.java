package org.jalgo.module.unifikation.algo.model;

import java.util.LinkedList;
import java.util.List;

import org.jalgo.module.unifikation.parser.ISetParser;
import org.jalgo.module.unifikation.parser.SetParser;

/**
 * Holds all steps made for a history function
 * @author Alex
 *
 */
public class History {
	/**
	 * Steps as a String
	 */
	private List<String> steps;
	/**
	 * Used rule that made the pair
	 */
	private List<Rule> usedrules;
	/**
	 * Selected pair in each step
	 */
	private List<Integer> stepsSelPair;
	/**
	 * index of current step
	 */
	private int curStep;
	/**
	 * Show selected pair
	 */
	private boolean showSelected;
	
	
	/**
	 * sign to split lines (line end symbol)
	 */
	private static final String lineEnd="<br /><font size=\"1\"><br /></font>";
	
	public History(){
		steps=new LinkedList<String>();
		usedrules=new LinkedList<Rule>();
		stepsSelPair=new LinkedList<Integer>();
		this.reset();
	}
	
	/**
	 * Deletes all following steps from history
	 */
	private void clearFollowingSteps(){
		steps.subList(curStep+1,steps.size()).clear();
		usedrules.subList(curStep+1,usedrules.size()).clear();
		stepsSelPair.subList(curStep+1,stepsSelPair.size()).clear();
	}
	
	/**
	 * Adds a step to history, clears all following steps from the current step before
	 * @param step Set to add
	 * @return true if successfully added
	 */
	public boolean addStep(ProblemSet step){
		//remove all following steps saved, since a change is made
		clearFollowingSteps();
		//deactivate it
		boolean active=step.isActive();
		step.setActive(false);
		//store it
		usedrules.add(step.getLastUsedRule());
		boolean result=steps.add(step.getFormatText());
		if(result){
			curStep++;
			result=stepsSelPair.add(-1);
			if(!result)
			{
				steps.remove(steps.size()-1);
				usedrules.remove(usedrules.size()-1);
			}
			else{
				showSelected=false;
				//update last Step
				if(curStep>0){
					ProblemSet set=getSet(curStep-1);
					set.setSelectedPair(stepsSelPair.get(curStep-1));
					set.setActive(false);
					steps.set(curStep-1, set.getFormatText());
				}
			}
		}
		//restore active state
		step.setActive(active);
		return result;
	}
	
	/**
	 * Sets the current selected pair
	 * @param selPair index of pair currently selected
	 */
	public void setSelected(final int selPair){
		if(selPair<-1) return;
		if(curStep>=0){
			if(stepsSelPair.get(curStep)!=selPair){
				clearFollowingSteps();
				stepsSelPair.set(curStep, selPair);
			}
			showSelected=(selPair>=0);
		}
	}
	
	/**
	 * Gets the full history till current step (exclude) as a formated String
	 * @return Formated String of the history
	 */
	public String getHistory(){
		StringBuffer result=new StringBuffer();
		//for(int i=0;i<curStep;i++){
		if(curStep>0){
			for(int i=curStep-1;i>=0;i--){
				//result.append(steps.get(i)+usedrules.get(i+1)+lineEnd);
				result.insert(0, steps.get(i)+Formating.setTextSize(" \u2192 "+usedrules.get(i+1).toString(), false)+lineEnd);
				if(i<=curStep-3 && result.length()>=20000){
					result.insert(0, "<center>\u219F &nbsp; ... &nbsp; \u219F</center>"+lineEnd);
					break;
				}
			}
			result.append(lineEnd);
		}
		return result.toString();
	}
	
	/**
	 * Gets the amount of steps stored
	 * @return Amount of steps stored
	 */
	public int getStepCount(){
		return steps.size();
	}
	
	/**
	 * Gets the current step index <br>
	 * 0: first Step <br>
	 * -1: no Step
	 * @return Current step index
	 */
	public int getCurrentStep(){
		return curStep;
	}
	
	/**
	 * Gets the specified step as text
	 * @param index Index of the step
	 * @return String of the step
	 */
	public String getStepText(int index){
		if(index<0 || index>=steps.size()) return "";
		return steps.get(index);
	}
	
	/**
	 * Gets the specified step as a set
	 * @param index
	 * @return Set of the step or null for error
	 */
	public ProblemSet getSet(int index){
		if(index<0 || index>=steps.size()) return null;
		ISetParser parser=new SetParser();
		parser.parse(getStepText(index));
		ProblemSet result=parser.getResult();
		result.setLastUsedRule(usedrules.get(index));
		return result;
	}
	
	/**
	 * Gets the current step as a set
	 * @return Set of current step or null for error
	 */	
	public ProblemSet getCurrentSet(){
		ProblemSet result=getSet(curStep);
		if(showSelected) result.setSelectedPair(stepsSelPair.get(curStep));
		return result;
	}

	/**
	 * Steps a step back in history
	 * @return True if it was possible
	 */
	public boolean prev(){
		if(showSelected){
			if(curStep<0) return false;
			showSelected=false;
			return true;
		}else if(curStep>0){
			curStep--;
			showSelected=true;
			return true;
		}else return false;
	}
	
	/**
	 * Steps a full step back in history
	 * @return True if it was possible
	 */	
	public boolean prevFull(){
		if(curStep>0){
			curStep--;
			showSelected=false;
			return true;
		}else return false;
	}
	
	/**
	 * Steps all steps back in the history
	 * @return True if it was possible
	 */
	public boolean prevAll(){
		if(curStep>0){
			curStep=0;
			showSelected=false;
			return true;
		}else return false;
	}
	
	/**
	 * Steps a step forward in history
	 * @return True if it was possible
	 */
	public boolean next(){
		if(!showSelected){
			if(curStep>=0 && stepsSelPair.get(curStep)>=0){
				showSelected=true;
				return true;
			}else return false;			
		}else if(curStep<steps.size()-1){
			curStep++;
			showSelected=false;
			return true;
		}else return false;
	}
	
	/**
	 * Steps a full step forward in the history
	 * @return True if it was possible
	 */
	public boolean nextFull(){
		if(curStep<steps.size()-1){
			curStep++;
			showSelected=false;
			return true;
		}else return false;
	}
	
	/**
	 * Steps all steps forward in the history
	 * @return True if it was possible
	 */
	public boolean nextAll(){
		if(curStep<steps.size()-1){
			curStep=steps.size()-1;
			showSelected=false;
			return true;
		}else return false;
	}
	
	/**
	 * Steps to a specified step in history
	 * @param step Index of step to go to
	 * @return True if it was possible
	 */
	public boolean go(final int step){
		if(step>=0 && step<steps.size()){
			curStep=step;
			showSelected=false;
			return true;
		}else return false;
	}
	
	/**
	 * Resets the whole history
	 */
	public void reset(){
		curStep=-1;
		showSelected=false;
		steps.clear();
		usedrules.clear();
		stepsSelPair.clear();
	}
	

}
