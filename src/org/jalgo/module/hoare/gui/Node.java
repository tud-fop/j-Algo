package org.jalgo.module.hoare.gui;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Node implements Serializable {
	/**
	 * 
	 */

	public enum Status {
		Error, ResultOK, ResultWrong, Nothing, Evaluating
	}

	private static final long serialVersionUID = 4575195779383885664L;

	private boolean visible;

	private boolean showLabel;

	private String label;

	private List<Node> children;

	private Status status;

	public Node(boolean visible, boolean showLabel, String label) {
		this.visible = visible;
		this.showLabel = showLabel;
		this.label = label;
		children = new LinkedList<Node>();
		status = Status.Nothing;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getLabel() {
		return label;
	}

	public boolean isShowLabel() {
		return showLabel;
	}

	public void setShowLabel(boolean showLabel) {
		this.showLabel = showLabel;

	}

	public void addChild(Node child) {
		this.children.add(child);
	}

	public void resetChildren() {
		this.children.clear();
	}

	public List<Node> getChildren() {
		return children;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}