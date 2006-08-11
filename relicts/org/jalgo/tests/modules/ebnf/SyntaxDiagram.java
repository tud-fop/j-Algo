package org.jalgo.tests.ebnf;

import javax.swing.JPanel;

import org.jalgo.module.ebnf.renderer.*;
import org.jalgo.module.ebnf.renderer.elements.RenderElement;

import java.util.List;

public class SyntaxDiagram extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3573499574510961338L;
	public final static int NULLLENGTH = 100;
	public final static int ELEMSPACE = 20;
	
	private Concat concat;
	private String name;
	private int x = 10;
	private int y = 10;
	
	public SyntaxDiagram(String name)   {
		
		setSize(1280, 1024);
		setVisible(true);
		setLayout(null);
		
		concat = new Concat();
		this.name = name;
	}
	
	public void addElem(SynDiaElem el) {
		
		concat.addElem(el);
		
		this.add(el);
		
		if (x + el.getLength() > this.getSize().getWidth()) {
			x = 10;
			y += 50;
		}
		
		
		el.setLocation(x, y);
		el.setVisible(true);
		
		System.out.println(x);
		x += el.getLength() + 10;
	
	}
	
	public void getRenderListFromDiagram(List<RenderElement> list) {
		
		concat.getRenderListFromDiagram(list);
		
	}
	
	public String sayHello() {
		
		return concat.sayHello();
		
	}
	public String contentToString() {
		
		return concat.contentToString();
		
	}
}
