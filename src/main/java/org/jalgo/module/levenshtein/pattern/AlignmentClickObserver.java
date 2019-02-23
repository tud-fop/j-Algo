package org.jalgo.module.levenshtein.pattern;

import java.util.List;

import org.jalgo.module.levenshtein.model.Action;

public interface AlignmentClickObserver {

	public void alignmentClicked(List<Action> alignment);
	
	public void alignmentHoverIn(List<Action> alignment);
	
	public void alignmentHoverOut(List<Action> alignment);
	
}
