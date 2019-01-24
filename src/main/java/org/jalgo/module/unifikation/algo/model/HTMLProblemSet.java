package org.jalgo.module.unifikation.algo.model;

/**
 * Problem set with HTML formating
 * @author Alex
 *
 */
public class HTMLProblemSet extends ProblemSet {
	public String getFormatText(boolean finished){
		StringBuffer result=new StringBuffer("M="+formatBracket("{ "));
		int remaining=pairs.size();
		int i=0;
	    for (Pair pair : pairs){
	    	String sPair=pair.getFormatText();
	    	if(isActive()){
	    		if(finished){
	    			if(!this.isPairInFinalForm(i))
	    				sPair=Formating.addUsedMark(sPair);
	    		}
	    		else{
		    		sPair="<a href=\""+i+"\">"+sPair+"</a>";
		    		 if(getSelectedPair()==i){
			    			sPair=Formating.addBackgroundColor(sPair, Formating.Selected);
			    	}else if(getHoverPair()==i){
		    			sPair=Formating.addBackgroundColor(sPair, Formating.Hover);
		    		}
	    		}
	    	}else if(getSelectedPair()==i){
	    		sPair=Formating.addUsedMark(sPair);
	    	}
	    	result.append(sPair);
	    	remaining--;
	    	if(remaining>0) result.append(formatChar(", &nbsp; &nbsp; "));
	    	i++;
	    }
	    result.append(formatChar(" }"));
	    return result.toString();
	}
}
