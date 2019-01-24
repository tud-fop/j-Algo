package org.jalgo.module.pulsemem;

import java.net.*;
import org.jalgo.main.util.*;

public abstract class Admin {
	private static final String ModuleName = "pulsemem";
	
	public static String getLanguageString(String name)
	{
		return Messages.getString(ModuleName, name);
	}
	
	public static URL getResourceURL(String name)
	{
		return Messages.getResourceURL(ModuleName, name);
	}
}
