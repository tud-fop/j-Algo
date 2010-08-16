package org.jalgo.module.unifikation.parser;

import java.util.ArrayList;
import java.util.List;


/**
 * Defines constraints for the input format of a set
 * @author Alex
 *
 */
public class FormatConstraints {
	private List<String> ValidTags;
	
	public FormatConstraints(){
		ValidTags=new ArrayList<String>();
		ValidTags.add("sub");
		ValidTags.add("span");
		ValidTags.add("font");
		ValidTags.add("br");
		ValidTags.add("u");
	}
	
	/**
	 * Checks if a HTML tag is valid for parsing
	 * @return true if tag is valid
	 */
	public boolean isTagValid(String tag){
		//to short tags
		if(tag.length()<2) return false;
		//remove surrounding <>
		if(tag.charAt(0)=='<' && tag.charAt(tag.length()-1)=='>'){
			tag=tag.substring(1,tag.length()-1).trim();
			//check for and remove delimiter / at beginning
			if(tag.length()>0 && tag.charAt(0)=='/'){
				tag=tag.substring(1,tag.length()).trim();
			}
			//check for and remove delimiter / at end
			if(tag.length()>0 && tag.charAt(tag.length()-1)=='/'){
				tag=tag.substring(0,tag.length()-1).trim();
			}
		}
		for(String s: ValidTags){
			if(tag.startsWith(s)) return true;
		}
		return false;
	}
}
