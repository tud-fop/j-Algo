package org.jalgo.module.heapsort.vis;

public interface ControllerListener {
	/**
	 * Called when the state of the controller changed.
	 * This amounts to the possibility of going forwards or backwards and
	 * lecture mode.
	 */
	void stateChanged();
}
