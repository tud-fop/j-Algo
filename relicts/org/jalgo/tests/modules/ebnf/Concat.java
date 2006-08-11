package org.jalgo.tests.ebnf;

import java.util.ArrayList;
import java.util.List;

import org.jalgo.module.ebnf.renderer.elements.RenderElement;


public class Concat extends SynDiaElem {

	/**
	 * 
	 */
	private static final long serialVersionUID = -377359652483309748L;
	private List<SynDiaElem> elems;
	
	private int width;
	private int height;
	
	public Concat() {
		
		elems = new ArrayList<SynDiaElem>();
		
	}
	
	public void addElem(SynDiaElem el) {
		
		elems.add(el);
		
	}
	
	public void getRenderListFromDiagram(List<RenderElement> list) {
		/*
		JPanel p = new JPanel();
		p.setSize(this.getWidth(), this.getHeight());
		list.add();
		*/
		
	}
	
	public void update() {
		

	}
	
	
	public void getWidth(int width) {
		
		this.width = width;
	}
	
	public void getHeight(int width) {
		
		this.width = width;
	}
	
	
	public String sayHello() {
		
		String ausgabe = new String("");
		ausgabe += "--> Concatenation .. \n";
		for (SynDiaElem elem : elems) {
			
			ausgabe += "\t" + elem.sayHello() + "\n";
						
		}
		return ausgabe;
		
	}
	
	public String contentToString() {
		String ausgabe = new String("");
		
		for (SynDiaElem elem : elems) {
			
			ausgabe += elem.contentToString();
						
		}
		return ausgabe;
		
		
		
	}
	
	
	

}
