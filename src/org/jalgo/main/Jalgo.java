/*
 * Created on 25.05.2004
 */
 
package org.jalgo.main;

/**
 * @author Christopher Friedrich
 */
public class Jalgo {

	private static JalgoMain jalgo;

	public static void main(String[] args) {

		jalgo = new JalgoMain();
		jalgo.createGUI();
	}

	public static IModuleConnector getCurrentModule() {
		return jalgo.getCurrentInstance();
	}

	public static JalgoMain getJalgoMain() {
		return jalgo;
	}

}
