package org.jalgo.module.unifikation.algo.controller;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;

import org.jalgo.module.unifikation.algo.view.Algo;

public class HLLPairs implements HyperlinkListener {
	private Algo theAlgo = null;

	public HLLPairs(Algo theAlgo) {
		this.theAlgo=theAlgo;
	}

	/**
	 * Handles events of Hyperlinks (MouseOver...)
	 */
	public void hyperlinkUpdate(HyperlinkEvent e) {
		Element el = e.getSourceElement(); 

		if(e.getEventType() == HyperlinkEvent.EventType.ENTERED){
			//work around for tablet PCs
			if(theAlgo.isMouseDown()) OnClick(e,el);
			else OnMouseOver(e,el);
		}else if(e.getEventType() == HyperlinkEvent.EventType.EXITED) 
			OnMouseOut(e,el);
		else if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
			OnClick(e,el);
	}
	
	/**
	 * Called when mouse enters an element
	 */
	private void OnMouseOver(HyperlinkEvent e, Element el){
		int pair=Integer.valueOf(GetLinkTarget(el)).intValue();
		if(theAlgo.getProblemSet().getHoverPair()!=pair && theAlgo.getProblemSet().getSelectedPair()!=pair){
			theAlgo.getProblemSet().setHoverPair(pair);
			theAlgo.notifyHoverChanged();
		}
	}

	/**
	 * Called when mouse leaves an element
	 */
	private void OnMouseOut(HyperlinkEvent e, Element el){
		theAlgo.getProblemSet().setHoverPair(-1);
		theAlgo.notifyHoverChanged();
	}

	/**
	 * Called when an element is clicked
	 */
	private void OnClick(HyperlinkEvent e, Element el){
		int pair=Integer.valueOf(GetLinkTarget(el)).intValue();
		if(theAlgo.getProblemSet().getSelectedPair()!=pair){
			theAlgo.getProblemSet().setSelectedPair(pair);
		}else theAlgo.getProblemSet().setSelectedPair(-1);
		theAlgo.notifySelectionChanged();
		theAlgo.setMouseDown(false);
	}
	
	/**
	 * gets the href attribute value from an element
	 * @param elem
	 * @return value of href attribute
	 */
	//Original: http://www.tutorials.de/forum/swing-java2d-3d-swt-jface/163395-jeditorpane-und-links.html
	//TODO: Bessere Variante?
    private String GetLinkTarget(Element elem) {
        AttributeSet attributeSet = elem.getAttributes();
        Object linkAttr = attributeSet.getAttribute(HTML.Tag.A);
        return (String) ((AttributeSet) linkAttr).getAttribute(HTML.Attribute.HREF);
    }
}
