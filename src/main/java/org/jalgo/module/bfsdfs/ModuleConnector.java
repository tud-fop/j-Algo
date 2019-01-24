package org.jalgo.module.bfsdfs;

import java.io.*;
import java.util.Properties;

import org.jalgo.main.AbstractModuleConnector;
import org.jalgo.main.JAlgoMain;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.module.bfsdfs.algorithms.BFS;
import org.jalgo.module.bfsdfs.algorithms.DFS;
import org.jalgo.module.bfsdfs.controller.GraphController;
import org.jalgo.module.bfsdfs.gui.GUIController;
/**
 * Interface for j-Algo. See j-Algo developer documentation for further details.
 * @author Thomas G&ouml;rres
 *
 */
public class ModuleConnector extends AbstractModuleConnector {	
	/** Name of the file for module-specific properties */
	private static final String PROPFILE_NAME		= ".bfsdfs.prop";
	
	/** Comment for the module's property file */
	private static final String PROPFILE_COMMENT	= "Properties for the j-Algo module BFS/DFS";
	
	/** Property that contains the name of the last opened file*/
	private static final String PROPERTY_LAST_FILE	= "last.file";
	
	/** Stores the full file name of the currently opened file */
	private static String currentFileName;
	
	/**
	 * Initializes the module
	 */
	private static GUIController guiController;
	private static GraphController graphController;
	private static BFS bfs;
	private static DFS dfs;
	
	@Override public void init() {
		JAlgoGUIConnector.getInstance().getModuleMenu(this).setEnabled(false);
		ModuleConnector.graphController = new GraphController(this);
		ModuleConnector.bfs = new BFS(graphController);
		ModuleConnector.dfs = new DFS(graphController);
		ModuleConnector.guiController = new GUIController(this, bfs, dfs, graphController);
	}
	
	/**
	 * Runs the module
	 */
	@Override public void run() {
		ModuleConnector.guiController.installWelcomeScreen();
	}

	public void setDataFromFile(ByteArrayInputStream data) {
		graphController.setDataFromFile(data);
		
	}

	
	public ByteArrayOutputStream getDataForFile() {
		return graphController.getDataForFile();
	}
	
	
	/**
	 * Not implemented
	 */
	@Override public void print() {}

	public static GUIController getGuiController() {
		return guiController;
	}

	public static GraphController getGraphController() {
		return graphController;
	}

	public static BFS getBfs() {
		return bfs;
	}

	public static DFS getDfs() {
		return dfs;
	}

	/**
	 * Saves the current filename before closing
	 */
	@Override
	public boolean close() {
		saveCurrentFilename();		
		return super.close();
	}
	
	/**
	 * Loads the graph from the specified file. The file has to be in j-Algo file format.
	 * @param filename name of the file that contains the graph
	 * @author Thomas G&ouml;rres
	 */
	public static void loadFromFile(String filename) {
		if (null == filename)
			return;
		
		currentFileName = filename;
		
		try {
			JAlgoMain.getInstance().openFile(filename, true);
		} catch (Exception e) {
			JAlgoGUIConnector.getInstance().showErrorMessage(Messages.getString("bfsdfs", "ModuleConnector.Loading_error"));
		}
	}

	/**
	 * Loads the graph from the last opened file. If the last opened file is not known, calling this
	 * method has no effect
	 * @author Thomas G&ouml;rres
	 */
	public static void loadFromLastFile() {
		loadFromFile(loadLastFilename());
	}
	
	/**
	 * Stores the name of the currently loaded file in the {@linkplain #PROPFILE_NAME module's property file}.
	 * If no file is opened, calling this method has no effect.
	 * 
	 * @author Thomas G&ouml;rres
	 */
	private void saveCurrentFilename() {
		if (null == currentFileName)
			currentFileName = getOpenFileName();
		
		if (null == currentFileName || "" == currentFileName)
			return;
		
		Properties fileHistory = new Properties();
		fileHistory.put(PROPERTY_LAST_FILE, currentFileName);
		
		try {
			fileHistory.store(new FileOutputStream(PROPFILE_NAME), PROPFILE_COMMENT);
		} catch (Exception e) {
			/* non-critical error, is ignored */
		}
	}
	
	/**
	 * Loads the name of the last loaded file from the {@linkplain #PROPFILE_NAME module's property file}.
	 * @return name of the last loaded file or <code>null</code>, if it could not be loaded
	 * @author Thomas G&ouml;rres
	 */
	private static String loadLastFilename() {		
		Properties fileHistory = new Properties();
		try {
			fileHistory.load(new FileInputStream(PROPFILE_NAME));
	    } catch (IOException e) {
	    	return null;
	    }
	    return fileHistory.get(PROPERTY_LAST_FILE).toString();
	}
	
	/**
	 * Checks whether the last loaded file is known. If so it may be loaded by calling
	 * {@link #loadFromLastFile()}
	 * @return <code>true</code> if the last loaded file is known 
	 * @author Thomas G&ouml;rres
	 */
	public static boolean islastOpenedFileKnown() {
		return loadLastFilename() != null;
	}
	
}