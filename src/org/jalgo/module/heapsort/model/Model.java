package org.jalgo.module.heapsort.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Model {
	void deserialize(InputStream is) throws IOException;
	State getInitialState();
	void serialize(OutputStream os) throws IOException;
}
