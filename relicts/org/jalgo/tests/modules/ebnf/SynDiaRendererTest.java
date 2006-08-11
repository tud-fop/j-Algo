/**
 * 
 */
package org.jalgo.tests.ebnf;

import org.jalgo.module.ebnf.model.*;
import org.jalgo.module.ebnf.renderer.*;

import java.util.List;
import java.util.ArrayList;


/**
 * @author Andre
 *
 */
public class SynDiaRendererTest {
	
	public static void main(String[] args) {
		
		SyntaxDiagram syndia = new SyntaxDiagram("S");

		Concat c1 = new Concat();
		Concat c2 = new Concat();
		
		Branch b1 = new Branch("open");
		
		Terminal t1 = new Terminal("a");
		Terminal t2 = new Terminal("a");
		Terminal t3 = new Terminal("b");
		
		c1.addElem(t1);
		c2.addElem(t2);
		c2.addElem(t3);
		
		b1.addElem(c1);
		b1.addElem(c2);
				
		
		syndia.addElem(b1);
		
		
		
		
		
		
		
		
		
		
		
	}
	
	
	
}
