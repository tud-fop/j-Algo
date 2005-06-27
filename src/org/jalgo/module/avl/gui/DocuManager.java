/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * Created on 26.04.2005
 * 
 */
package org.jalgo.module.avl.gui;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jalgo.module.avl.Controller;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Jean Christoph Jung
 * 
 * The DocuManager parses an XML-file, that must have a certain form:
 * <!DOCTYPE Kommentarliste [
 * <!ELEMENT Kommentarliste (Schritt)*>
 * <!ELEMENT Schritt (Key,Text)>
 * <!ELEMENT Key (#PCDATA)*>
 * <!ELEMENT Text Line*>
 * <!ELEMENT Line (#PCDATA)*>
 * ]>
 * 
 * the constructor gets the name of the file and does all the parsing
 * and save the content in a treemap <key,value>.
 * calling the methods <code> getMapOfElements </code> or <code>getValue</code> you can access
 * that map. 
 */
public class DocuManager
implements GUIConstants {

	/**
	 * @author Jean Christoph Jung
	 * internal class of DocuManager
	 * this class extends the DefaultHandler, and overwrites the methods
	 * <code> characters, startelement, endelement </code>. 
	 */
	class XmlHandler extends DefaultHandler {
		private final int ALGDESC = -1;
		private final int SCHRITT = 0;
		private final int KEY = 1;
		private final int TEXT = 2;
		private final int LINE = 3;
		
		private Map<String,String> mapOfElements;
		private int flag = ALGDESC;
		private String key;
		private String value = "";
		private String line = "";
		private String startstring = "", endstring = "";

		public XmlHandler() {
			super();
			mapOfElements = new TreeMap<String,String>();
		}
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attr) throws SAXException {
			if (qName=="AlgorithmDescription")
				flag = ALGDESC;
			else if (qName=="Schritt")
				flag = SCHRITT;
			else if (qName=="Key")
				flag = KEY;
			else if (qName=="Text") 
				flag = TEXT;
			else if (qName=="Line")
				flag= LINE;
			
			if (attr!=null) {
				for (int i=0; i<attr.getLength(); i++) {
					String attribute = attr.getQName(i);
					String temp = attr.getValue(i);
					int k=-2;
					
					if (attribute.equals("startswith")) {
						if (temp.contains("tab")) startstring = startstring.concat("\t");
						if (temp.contains("nl")) startstring = startstring.concat("\n");
						k = temp.indexOf("sp");
						if (k>=0) {
							char whitespaces = temp.charAt(k+2);
							while (whitespaces-->'0') {
								startstring = startstring.concat(" ");
							}
						}
					}
					if (attribute.equals("endswith")) {
						if (temp.contains("tab")) endstring = endstring.concat("\t");
						if (temp.contains("nl")) endstring = endstring.concat("\n");
						k = temp.indexOf("sp");
						if (k>=0) {
							char whitespaces = temp.charAt(k+2);
							while (whitespaces-->'0') {
								endstring = endstring.concat(" ");
							}
						}					
					}
				}
			}
		}
		
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (qName=="Schritt") {
				mapOfElements.put(key, value);
				key = null;
				value = "";
			}
			if (qName=="Line") {
				value = value+startstring+line+endstring;
				line = "";
				startstring = "";
				endstring = "";
			}
			flag--;
		}
		
		public void characters(char[] ch, int start, int length) throws SAXException {
			String s = new String(ch, start, length);
			s = s.trim();
			if (flag==KEY)
				key = s;
			else if (flag==LINE) {
//				if (line==null)
//					line = s;
//				else {
					line = line+s;
//				}
			}
		}			
		
		public Map<String,String> getMapOfElements() {
			return mapOfElements;
		}
	}
	
	private Map<String, Map<String,String>> mapOfAlgorithms;
	private Map<String, Map<String, Integer>> offsets;
	private Map<String, Map<String, Integer>> lengths;
	
	private final Controller controller;

	public DocuManager(Controller controller) {
		this.controller = controller;
		loadAlgorithmDescription();
	}

	
	/**
	 * creates the Algorithm description out of a xml-file
	 * - puts the description of the single algorithms in single maps
	 * - calculates the beginning and the length (in chars) of every section
	 */
	private void loadAlgorithmDescription() {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();	
			SAXParser saxParser = factory.newSAXParser();
			XmlHandler xmlhandler = new XmlHandler();
			String path = "res:module:avl:";
			String fileseparator = System.getProperty("file.separator");
			path = path.replace(":", fileseparator);				
			saxParser.parse(new File(path+ALGORITHM_TEXT_BASE), xmlhandler);
//TODO: enable this, when switching to plugin structure
//			saxParser.parse(getClass().getResourceAsStream(
//				"/res/module/avl/"+ALGORITHM_TEXT_BASE), xmlhandler);
			Map<String,String> mapOfElements = xmlhandler.getMapOfElements();

			mapOfAlgorithms = new TreeMap<String, Map<String, String>>();
			offsets = new TreeMap<String, Map<String, Integer>>();
			lengths = new TreeMap<String, Map<String, Integer>>();
			
			mapOfAlgorithms.put("search", new TreeMap<String, String>());
			mapOfAlgorithms.put("insert", new TreeMap<String, String>());
			mapOfAlgorithms.put("avlinsert", new TreeMap<String, String>());
			mapOfAlgorithms.put("remove", new TreeMap<String, String>());
			mapOfAlgorithms.put("avlremove", new TreeMap<String, String>());

			for (String key : mapOfElements.keySet()) {
				if (key.startsWith("search"))
					mapOfAlgorithms.get("search").put(key, mapOfElements.get(key));
				if (key.startsWith("insert"))
					mapOfAlgorithms.get("insert").put(key, mapOfElements.get(key));
				if (key.startsWith("avlinsert"))
					mapOfAlgorithms.get("avlinsert").put(key, mapOfElements.get(key));
				if (key.startsWith("remove"))
					mapOfAlgorithms.get("remove").put(key, mapOfElements.get(key));
				if (key.startsWith("avlremove"))
					mapOfAlgorithms.get("avlremove").put(key, mapOfElements.get(key));
			}
			

			for (String key : mapOfAlgorithms.keySet()) {
				Map<String, String> m = mapOfAlgorithms.get(key);

				Map<String, Integer> offsetmap = new TreeMap<String, Integer>();
				Map<String, Integer> lengthmap = new TreeMap<String, Integer>();
				int offset = 0;
				int length = 0;
				
				for (String key1 : m.keySet()) {
					String text = mapOfElements.get(key1);
					length = text.length();
					offsetmap.put(key1, offset);
					lengthmap.put(key1, length);
					offset += length;
				}
				offsets.put(key, offsetmap);
				lengths.put(key, lengthmap);
			}
		}	
		catch (Throwable t) {
			System.err.println("Xml-Datei nicht gefunden oder anderer Fehler");
			t.printStackTrace();
		}		
	}
	
	
	/**
	 * @return the offset of the current section, that means the position of the
	 * character, the section starts with
	 */
	public int getCurrentStepOffset() {
		String key = getKey(controller.getAlgoName());
		String section = controller.getSection();

		try {return offsets.get(key).get(section);}
		catch (NullPointerException ex) {return -1;}
	}

	/**
	 * @return the length of the current section, that means the section consists of
	 * how many characters
	 */
	public int getCurrentStepLength() {
		String key = getKey(controller.getAlgoName());
		String section = controller.getSection();
		
		try {return lengths.get(key).get(section);}
		catch (NullPointerException ex) {return -1;}
	}

	/**
	 * @return the description of the currentAlgorithm
	 */
	public Map<String, String> getCurrentAlgorithmDescription() {
		String name = controller.getAlgoName();
		String key = getKey(name);
		if (key==null)
			return null;
		return mapOfAlgorithms.get(key);
	}
	
	
	private String getKey(String name) {
		if (name.equals("Suchen"))
			return "search";
		if (name.equals("Einfügen") || name.equals("Suchbaum Erstellen"))
			return "insert";
		if (name.equals("AVL-Einfügen") || name.equals("AVL-Baum Erstellen"))
			return "avlinsert";
		if (name.equals("Löschen"))
			return "remove";
		if (name.equals("AVL-Löschen"))
			return "avlremove";
		if (name.equals("AVL-Test"))
			return "avltest";
		return null;
	}
}