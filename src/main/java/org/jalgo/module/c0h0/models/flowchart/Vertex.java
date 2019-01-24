package org.jalgo.module.c0h0.models.flowchart;


/**
 * a vertex of the flow chart
 * 
 * @author Philipp
 *
 */
public class Vertex extends Element{
	private int x;
	private int y;
	private int width;
	private int height;

	/**
	 * @param label
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param style
	 */
	public Vertex(String label, int x, int y, int width, int height, String style) {
		super(style, label);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * sets x
	 * 
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * sets y
	 * 
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * sets the width
	 * 
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * sets the height
	 * 
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
	/**
	 * returns x
	 * 
	 * @return x position
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * returns y
	 * 
	 * @return y position
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * returns the width
	 * 
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * returns the height
	 * 
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	
}
