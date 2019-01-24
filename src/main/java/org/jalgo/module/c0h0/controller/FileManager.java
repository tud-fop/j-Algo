package org.jalgo.module.c0h0.controller;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.jalgo.module.c0h0.views.TerminalView;

/**
 * this class provides file opening functionality, specifically .c0 files
 * 
 * @author hendrik
 * 
 */

public class FileManager {

	private JFileChooser c0fileChooser;
	private JFileChooser h0fileChooser;
	private JFileChooser fcfileChooser;
	private JFileChooser exampleChooser;
	private String home = System.getProperty("user.dir");
	private String sep = System.getProperty("file.separator");
	private String suffix = ".c0";

	/**
	 * Checks for c0-files
	 */
	private FileFilter c0Filter = new FileFilter() {

		public String getDescription() {
			return "*" + suffix;
		}

		public boolean accept(File f) {
			if (f.getName().endsWith(suffix))
				return true;
			if (f.isDirectory())
				return true;
			return false;
		}
	};

	/**
	 * Checks for Flowchart-files
	 */
	private FileFilter fcFilter = new FileFilter() {
		String suffix = ".png";

		public String getDescription() {
			return "*" + suffix;
		}

		public boolean accept(File f) {
			if (f.getName().endsWith(suffix))
				return true;
			if (f.isDirectory())
				return true;
			return false;
		}
	};

	/**
	 * Checks for h0-files
	 */
	private FileFilter h0Filter = new FileFilter() {
		String hsuffix = ".h0";

		public String getDescription() {
			return "*" + hsuffix;
		}

		public boolean accept(File f) {
			if (f.getName().endsWith(hsuffix))
				return true;
			if (f.isDirectory())
				return true;
			return false;
		}
	};

	/**
	 * opens a file
	 * 
	 * @return the file
	 */
	public ArrayList<String> openFile() {
		if (c0fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { // parsing
			return readFile(c0fileChooser.getSelectedFile());
		}
		return new ArrayList<String>();
	}

	/**
	 * opens an example
	 * 
	 * @return the example
	 */
	public ArrayList<String> openExample() {
		if (exampleChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { // parsing
			return readFile(exampleChooser.getSelectedFile());
		}
		return new ArrayList<String>();
	}

	/**
	 * saves a file
	 * 
	 * @param code
	 * @param suffix
	 */
	public void saveFile(String code, String suffix) {
		File file;
		if (h0fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			file = h0fileChooser.getSelectedFile();
			if (!file.getName().endsWith(suffix)) {
				String path = file.getAbsolutePath();
				file = new File(path + suffix);
			}
		} else
			return;

		try {
			file.createNewFile();
		} catch (IOException e) {
			TerminalView.println("can't create file - everything alright with your filesystem??");
			e.printStackTrace();
		} // Create the empty file with default permissions, etc.

		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			TerminalView.println("just made a file but can't find it anylonger - me so stupid");
			e.printStackTrace();
		}

		try {
			fos.write(code.getBytes());
			fos.close();
		} catch (IOException e) {
			TerminalView.println("just made a file but can't write in it - having a bad day?");
			e.printStackTrace();
		}
	}

	/**
	 * save a file
	 * 
	 * @param code
	 */
	public void saveFile(List<String> code) {

		File file;
		if (c0fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			file = c0fileChooser.getSelectedFile();
			if (!file.getName().endsWith(suffix)) {
				String path = file.getAbsolutePath();
				file = new File(path + suffix);
			}
		} else
			return;

		try {
			file.createNewFile();
		} catch (IOException e) {
			TerminalView.println("can't create file - everything alright with your filesystem??");
			e.printStackTrace();
		} // Create the empty file with default permissions, etc.

		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			TerminalView.println("just made a file but can't find it anylonger - me so stupid");
			e.printStackTrace();
		}

		for (String line : code) {
			String lineString = line + "\n";
			try {
				fos.write(lineString.getBytes());
			} catch (IOException e) {
				TerminalView.println("just made a file but can't write in it - having a bad day?");
				e.printStackTrace();
			}
		}

	}

	/**
	 * Saves an image with the given suffix
	 * 
	 * @param img
	 * 			Image to be saved
	 * @param suffix
	 * 			String with the allowed suffix
	 */
	public void saveImage(Image img, String suffix) {
		File file;
		if (fcfileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			file = fcfileChooser.getSelectedFile();
			if (!file.getName().endsWith(suffix)) {
				String path = file.getAbsolutePath();
				file = new File(path + suffix);
			}
		} else
			return;
		try {
			file.createNewFile();
		} catch (IOException e) {
			TerminalView.println("can't create file - everything alright with your filesystem??");
			e.printStackTrace();
		} // Create the empty file with default permissions, etc.

		try {
			ImageIO.write((RenderedImage)img, "png", file);
		} catch (IOException e) {
			TerminalView.println("Couldn't create file");
			e.printStackTrace();
		}
	}

	public FileManager() {
		c0fileChooser = new JFileChooser();
		h0fileChooser = new JFileChooser();
		fcfileChooser = new JFileChooser();
		exampleChooser = new JFileChooser(home + sep + "examples" + sep + "c0h0");
		c0fileChooser.setFileFilter(c0Filter);
		h0fileChooser.setFileFilter(h0Filter);
		fcfileChooser.setFileFilter(fcFilter);
		exampleChooser.setFileFilter(c0Filter);
	}

	/**
	 * read a file
	 * 
	 * @param file
	 * @return the file
	 */
	private ArrayList<String> readFile(File file) {
		ArrayList<String> code = new ArrayList<String>();
		try {
			FileReader in = new FileReader(file);
			BufferedReader bReader = new BufferedReader(in);
			try {
				while (bReader.ready())
					code.add(bReader.readLine());
			} catch (IOException e) {
				TerminalView.println("can't read file - everything alright with your filesystem??");
				// e.printStackTrace();
				return new ArrayList<String>();
			}
			return code;
		} catch (FileNotFoundException e) {
			return new ArrayList<String>();
		}
	}
}
