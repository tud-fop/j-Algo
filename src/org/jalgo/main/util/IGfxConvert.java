/*
 * Created on 23.04.2004
 */
 
package org.jalgo.main.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * @author Benjamin Scholz
 */
public interface IGfxConvert extends IConverter {

	public DataOutputStream convert(DataInputStream input);
	// TODO type needs to be proved

}