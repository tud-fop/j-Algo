package org.jalgo.module.c0h0.models.flowchart;


/**
 * an element of the flow chart
 * 
 * @author Philipp
 *
 */
public abstract class Element {

	private String style;
	private String label;
	
	/**
	 * @param style
	 * @param label
	 */
	public Element(String style, String label) {
		this.style = style;
		this.label = label;
	}
	
	/**
	 * returns the style
	 * 
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}
	
	/**
	 * returns the label
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * sets the style
	 * 
	 * @param style
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * sets the label
	 * 
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}
}
