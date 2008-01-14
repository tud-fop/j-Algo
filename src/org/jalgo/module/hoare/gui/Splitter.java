package org.jalgo.module.hoare.gui;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JSplitPane;

public class Splitter extends JSplitPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Splitter() {
		super();
	}

	public Splitter(int arg0, boolean arg1, Component arg2, Component arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public Splitter(int arg0, boolean arg1) {
		super(arg0, arg1);
	}

	public Splitter(int arg0, Component arg1, Component arg2) {
		super(arg0, arg1, arg2);
	}

	public Splitter(int arg0) {
		super(arg0);
	}

	boolean isPainted = false;

	boolean hasProportionalLocation = false;

	double proportionalLocation = 0;

	public void setDividerLocation(double proportionalLocation) {
		if (!isPainted) {
			hasProportionalLocation = true;
			this.proportionalLocation = proportionalLocation;
		} else
			super.setDividerLocation(proportionalLocation);
	}

	public void paint(Graphics g) {
		if (!isPainted) {
			if (hasProportionalLocation)
				super.setDividerLocation(proportionalLocation);
			isPainted = true;
		}
		super.paint(g);
	}
}
