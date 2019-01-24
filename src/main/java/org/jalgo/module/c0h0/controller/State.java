package org.jalgo.module.c0h0.controller;

import org.jalgo.module.c0h0.views.View;

/**
 * states of the containers
 * 
 */
public class State {
	private View left, right, bottom;
	private ViewState name;

	/**
	 * @param left
	 * @param right
	 * @param bottom
	 */
	public State(View left, View right, View bottom, ViewState name) {
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.name = name;
	}

	/**
	 * calls render on every currently visible view
	 */
	public void render() {
		left.render();
		right.render();
		bottom.render();
	}

	/**
	 * calls update on every currently visible view
	 */
	public void update() {
		left.update();
		right.update();
		bottom.update();
	}

	/**
	 * returns the left View
	 * 
	 * @return left View
	 */
	public View getLeftView() {
		return left;
	}

	/**
	 * returns the right View
	 * 
	 * @return right View
	 */
	public View getRightView() {
		return right;
	}

	/**
	 * returns the bottom View
	 * 
	 * @return bottom View
	 */
	public View getBottomView() {
		return bottom;
	}

	/**
	 * returns the name of this ViewState
	 * 
	 * @return name 
	 */
	public ViewState getName() {
		return name;
	}
}
