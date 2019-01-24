package org.jalgo.module.app.coreTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.jalgo.module.app.core.dataType.formalLanguage.Concatenate;
import org.jalgo.module.app.core.dataType.formalLanguage.FormalLanguage;
import org.jalgo.module.app.core.dataType.formalLanguage.Union;
import org.junit.Ignore;
import org.junit.Test;

public class FormalLanguageTest {

	@Test
	public void testMake() {
		 new FormalLanguage("(c+(ca(bca+E)*bc))");
	}
 
	@Test
	public void testToString() {
		Union union = (Union) FormalLanguage.getOperationByID("union");
		Concatenate conc = (Concatenate) FormalLanguage.getOperationByID("concat");
		
		try {
			FormalLanguage language1 = new FormalLanguage("a");
			FormalLanguage language2 = new FormalLanguage("b");
			
			FormalLanguage language3 = (FormalLanguage) union.op(language1, language2);
			assertEquals("(a+b)", language3.toString());
		
			FormalLanguage language4 = (FormalLanguage) conc.op(language3, language3);
			
			Set<String> testSet = new HashSet<String>();
			testSet.add("aa");
			testSet.add("ab");
			testSet.add("ba");
			testSet.add("bb");
			assertEquals("(a+b)(a+b)", language4.toString());

			language3 = (FormalLanguage) union.star(language3, null);
			//assertEquals("{(a|b)*}", language3.toString());
		} catch (IllegalArgumentException e) {
			fail("Illegal argument");
		}
	}

	@Test
	public void testFormalLanguage() {
		FormalLanguage language = new FormalLanguage();
		assertTrue("A new formal language has to be empty.", language.isEmpty());
	}
 
	@Test
	public void testSimplify() {
		FormalLanguage l1 = new FormalLanguage("((a|b)*)");
		FormalLanguage l2 = new FormalLanguage("((E|a|b)*)");
		FormalLanguage l3 = new FormalLanguage("((E)*)");
		FormalLanguage l4 = new FormalLanguage("(EE)");
		FormalLanguage l5 = new FormalLanguage("(EEEEE)");
		FormalLanguage l6 = new FormalLanguage("(E+(EE|(a|E*)*|baad)*+ baEbEd)");
		FormalLanguage l7 = new FormalLanguage("(a+g+d+(b|a)*+z+E)");
		FormalLanguage l8 = new FormalLanguage("(c(abc)*)");
		
		assertEquals("((a|b)*)",l1.toString());
		assertEquals("((a|b)*)",l2.toString());
		assertEquals("(E)",l3.toString());
		assertEquals("(E)",l4.toString());
		assertEquals("(E)",l5.toString());
		assertEquals("((a*|baad)*+E+babd)",l6.toString());
		assertEquals("((a|b)*+E+a+d+g+z)",l7.toString());
	}
}
