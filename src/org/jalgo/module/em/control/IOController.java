package org.jalgo.module.em.control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Writer;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.jalgo.main.util.Messages;
import org.jalgo.module.em.data.StartParameters;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;

/**
 * The {@code IOController} handles the input and output user actions.
 * 
 * @author Tom Schumann, Tobias Nett
 */

public class IOController {

	/**
	 * creates an {@code IOController} object.
	 */
	public IOController() {

	}

	/**
	 * Generates and opens a dialog for choosing a path to a directory or file.
	 * Returns the path, or null if the selection was canceled.
	 * 
	 * @param String to identify the chooser
	 * 
	 * @return the path of the selected file as {@code String}, {@code null} if
	 *         selection was canceled
	 */
	private String open(String type) {
		final JFileChooser chooser = new JFileChooser(Messages.getString("em",
				"IOController.FileChooser"));
		if(type.equals("SAVE")){
			chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		}
		else{
			chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		}
		
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		// the folder that appears at file chooser startup
		final File file = new File("examples/em/");
		chooser.setCurrentDirectory(file);
		chooser.setVisible(true);
		
		if(type.equals("OPEN")){
			final int result = chooser.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				File pathToFile = chooser.getSelectedFile();
				// only accepts *.em files
				if (pathToFile.getName().toLowerCase().endsWith("em")) {
					String inputVerzStr = pathToFile.getPath();
					return inputVerzStr;
				}
			}
		}
		if(type.equals("SAVE")){
			chooser.showSaveDialog(null);
			File fileread = chooser.getSelectedFile();
			chooser.setVisible(false);
			return fileread.getAbsolutePath();
		}
		return null;
	}

	/**
	 * Stores the specified {@code StartParameters} object into a file.
	 * 
	 * @param params
	 *            the {@code StartParameters} object to be stored
	 */
	public void write(StartParameters params) {
		String pfad = this.open("SAVE")+".em";
		if (pfad != null) {
			Writer out = null;
			XStream xstream = new XStream();
			ObjectOutputStream outstream = null;
			try {
				out = new BufferedWriter(new FileWriter(pfad));
			} catch (IOException e) {
				System.out.println("Error by creating" + e);
			}
			try {
				outstream = xstream.createObjectOutputStream(out);
			} catch (IOException e) {
				System.out.println("Error by creating" + e);
			}
			try {
				outstream.writeObject(params);
			} catch (IOException e) {
				System.out.println("Error by writing" + e);
			}
			try {
				outstream.flush();
				out.flush();
			} catch (Exception e) {
				System.out.println("Error while flushing" + e);
			}
			try {
				outstream.close();
				System.out.println("XOutputStream has been closed");
			} catch (Exception e) {
				System.out.println("Error while closing XOutputStream" + e);
			}

			try {

				out.close();
				System.out.println("Writer has been closed");

			} catch (Exception e) {
				System.out.println("Error while closing Writer" + e);
			}
		}

	}

	/**
	 * Opens a dialog to choose a file and reads this file into a
	 * {@code StartParameters} object that can be visualized by the module.
	 * 
	 * @return {@code StartParameters} object read out of the specified file
	 */
	public StartParameters read(){
		String path = this.open("OPEN");
		Reader read = null;
		XStream xstream = new XStream();
		StartParameters obj = null;
		ObjectInputStream input = null;
		if (path != null) {
			try {
				read = new BufferedReader(new FileReader(path));
			} catch (FileNotFoundException e) {
//				System.out.println("Error while opening the File" + e);

				JOptionPane.showMessageDialog(null, Messages.getString(
						"em", "StartScreen.LoadErrorDescription"), Messages
						.getString("em", "StartScreen.LoadErrorTitle"),
						JOptionPane.ERROR_MESSAGE);
			}
			try {
				input = xstream.createObjectInputStream(read);
			} catch (IOException e) {
//				System.out.println("Error while creating the InputStream" + e);

				JOptionPane.showMessageDialog(null, Messages.getString(
						"em", "StartScreen.LoadErrorDescription"), Messages
						.getString("em", "StartScreen.LoadErrorTitle"),
						JOptionPane.ERROR_MESSAGE);
			} catch (ArrayIndexOutOfBoundsException e) {
				JOptionPane.showMessageDialog(null, Messages.getString(
						"em", "StartScreen.LoadErrorDescription"), Messages
						.getString("em", "StartScreen.LoadErrorTitle"),
						JOptionPane.ERROR_MESSAGE);
			} catch (StreamException e) {
//				System.out.println("Error while creating the InputStream" + e);

				JOptionPane.showMessageDialog(null, Messages.getString(
						"em", "StartScreen.LoadErrorDescription"), Messages
						.getString("em", "StartScreen.LoadErrorTitle"),
						JOptionPane.ERROR_MESSAGE);
			}
			if (input != null) {
				try {
					obj = (StartParameters) input.readObject();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, Messages.getString(
							"em", "StartScreen.LoadErrorDescription"), Messages
							.getString("em", "StartScreen.LoadErrorTitle"),
							JOptionPane.ERROR_MESSAGE);
				}
			}
			if (read != null) {
				try {
					read.close();
				} catch (IOException e) {
//					System.out.println("Error while closing reader" + e);
				}
			}
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
//					System.out.println("Error while closing XStream" + e);
				} 
			}
			return obj;
		}
		return null;
	}

}
