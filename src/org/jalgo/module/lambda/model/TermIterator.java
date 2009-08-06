package org.jalgo.module.lambda.model;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class TermIterator implements Iterator<Term> {

	private Term term;
	private Term currentTerm;
	private String currentPos;
	private Set<String> visited;
	private Boolean hasnext;
	
	public TermIterator(Term term) {
		this.term = term;
		currentPos = "";
		currentTerm = term;
		hasnext = true;
		visited = new TreeSet<String>();
	}

	public boolean hasNext() {
		if(hasnext == null) {
			if(!currentPos.contains("l") && currentTerm.getChildren().size() <= 0) {
				hasnext = false;	
			}
			else if(currentTerm.getChildren().size() == 0) {
				int lastL = currentPos.lastIndexOf('l');
				if(lastL == 0) {
					currentPos = "";
					currentTerm = term;
				}
				else {
					currentPos = currentPos.substring(0, lastL);
					currentTerm = term.getSubTerm(currentPos);
				}
				hasnext = hasNext();
			}
			else if(currentTerm.getChildren().size() == 2) {
				if(currentTerm.getTermType() == ETermType.ABSTRACTION) {
					currentTerm = currentTerm.getSubTerm("a");
					currentPos += 'a';
				}
				else {
					if(!visited.contains(currentPos)) {
						visited.add(currentPos);
						currentTerm = currentTerm.getSubTerm("l");
						currentPos += 'l';	
					}
					else {
						currentTerm = currentTerm.getSubTerm("r");
						currentPos += 'r';
					}
				}
				hasnext = true;
			}
		}
		return hasnext;
	}

	public Term next() {
		if(hasNext()) {
			hasnext = null;
			return currentTerm;
		}
		return null;
	}

	public String nextPosition() {
		if(hasNext()) {
			hasnext = null;
			return currentPos;
		}
		return null;
	}
	
	public String getCurrentPos() {
		if(hasNext()) {
			return currentPos;
		}
		return null;
	}
	
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
