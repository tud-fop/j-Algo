package org.jalgo.module.levenshtein.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ControllerTest {
	
	private Controller controller;
	
	@Before
	public void setUp() {
		controller = new Controller();
	}
	
	@Test
	public void calculationTest() {
		controller.init("bürste", "schürze", 1, 1, 1, 0);
		assertEquals(5, controller.getCell(6, 7).getValue());
	}
	
	@Test
	public void alignmentSubsTest() {
		controller.init("a", "b", 1, 1, 1, 0);
		List<List<Action>> alignments = controller.getAlignments(1, 1);
		assertEquals(1, alignments.size());
		assertEquals(1, alignments.get(0).size());
		assertEquals(Action.SUBSTITUTION, alignments.get(0).get(0));
	}
	
	@Test
	public void alignmentIdentTest() {
		controller.init("a", "a", 1, 1, 1, 0);
		List<List<Action>> alignments = controller.getAlignments(1, 1);
		assertEquals(1, alignments.size());
		assertEquals(1, alignments.get(0).size());
		assertEquals(Action.IDENTITY, alignments.get(0).get(0));
	}
	
	@Test
	public void alignmentNotIdentTest() {
		controller.init("a", "a", 1, 1, 1, 5);
		List<List<Action>> alignments = controller.getAlignments(1, 1);
		assertEquals(2, alignments.size());
	}
	
	@Test
	public void alignmentTest() {
		controller.init("bürste", "schürze", 1, 1, 1, 0);
		List<List<Action>> alignments = controller.getAlignments(6, 7);
		assertEquals(6, alignments.size());
	}
}
