package org.jalgo.module.heapsort.model;

public interface SequencerListener {
	void stepAvail();
	void step(State q, Action a, State q1);
	void back(State q, Action a, State q1);
}
