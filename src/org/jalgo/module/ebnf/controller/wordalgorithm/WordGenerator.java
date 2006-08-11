package org.jalgo.module.ebnf.controller.wordalgorithm;

import java.util.HashMap;
import java.util.Map;

import org.jalgo.module.ebnf.controller.wordalgorithm.exceptions.GenerateWordException;
import org.jalgo.module.ebnf.model.syndia.Branch;
import org.jalgo.module.ebnf.model.syndia.Concatenation;
import org.jalgo.module.ebnf.model.syndia.ElementNotFoundException;
import org.jalgo.module.ebnf.model.syndia.Repetition;
import org.jalgo.module.ebnf.model.syndia.SynDiaElem;
import org.jalgo.module.ebnf.model.syndia.SynDiaSystem;
import org.jalgo.module.ebnf.model.syndia.TerminalSymbol;
import org.jalgo.module.ebnf.model.syndia.Variable;

/**
 * Generates a random word from a given syntax diagram system.
 * 
 * @author Michael Thiele
 * 
 */
public class WordGenerator {

	private static int counter = 0;

	private SynDiaSystem sds;

	private String generatedWord;

	private Map<Repetition, Double> repetitions;

	/**
	 * 
	 * @param sds
	 *            a syntax diagram system
	 */
	public WordGenerator(SynDiaSystem sds) {
		this.sds = sds;
		fillRepetitions();
	}

	private void fillRepetitions() {
		repetitions = new HashMap<Repetition, Double>();
		for (String name : sds.getLabelsOfVariables()) {
			try {
				Concatenation root = sds.getSyntaxDiagram(name).getRoot();
				fillRepetitions(root);
			} catch (ElementNotFoundException e) {
				e.printStackTrace();
				System.exit(-1);
			}

		}
	}

	private void fillRepetitions(Concatenation concat) {
		for (int i = 0; i < concat.getNumberOfElems(); i++) {
			SynDiaElem sde = concat.getSynDiaElem(i);
			if (sde instanceof Repetition) {
				Repetition r = (Repetition) sde;
				repetitions.put(r, 0.5);
				fillRepetitions(r.getLeft());
				fillRepetitions(r.getRight());
			} else if (sde instanceof Branch) {
				Branch b = (Branch) sde;
				fillRepetitions(b.getLeft());
				fillRepetitions(b.getRight());
			}
		}
	}

	/**
	 * Starts the generator and returns a random word.
	 * 
	 * @return a random word
	 * @throws GenerateWordException
	 */
	public String start() throws GenerateWordException {
		try {
			Concatenation root = sds.getSyntaxDiagram(sds.getStartDiagram())
					.getRoot();
			this.generatedWord = "";
			counter = 0;
			this.walkThroughConcatenation(root);
			return generatedWord;
		} catch (ElementNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}

	private void walkThroughConcatenation(Concatenation concat)
			throws GenerateWordException {
		if (counter > 100 || generatedWord.length() > 120)
			throw new GenerateWordException();
		// from the beginning to the end
		for (int i = 0; i < concat.getNumberOfElems(); i++) {
			SynDiaElem sde = concat.getSynDiaElem(i);
			// TerminalSymbol - just add to generated word
			if (sde instanceof TerminalSymbol) {
				generatedWord += ((TerminalSymbol) sde).getLabel();
				counter++;
			}
			// Variable - jump to this diagram and walk through its root
			else if (sde instanceof Variable) {
				try {
					Concatenation root = sds.getSyntaxDiagram(
							((Variable) sde).getLabel()).getRoot();
					walkThroughConcatenation(root);
				} catch (ElementNotFoundException e) {
					e.printStackTrace();
					System.exit(-1);
				}
			}
			// Branch - either take upper or lower path
			else if (sde instanceof Branch) {
				// lookahead, how much branches are on this way
				double countUpper, countLower;
				double chance;
				Concatenation leftPath = ((Branch) sde).getLeft();
				Concatenation rightPath = ((Branch) sde).getRight();
				countUpper = countBranches(leftPath) + 1;
				countLower = countBranches(rightPath) + 1;
				if (countUpper > countLower)
					chance = 1 - countLower / (countUpper + countLower);
				else
					chance = countUpper / (countLower + countUpper);
				if (Math.random() < chance) {
					// upper path
					walkThroughConcatenation(leftPath);
				} else {
					// lower path
					walkThroughConcatenation(rightPath);
				}
			}
			// Repetition - take upper path and decide at the end whether to
			// take lower path
			else if (sde instanceof Repetition) {
				walkThroughRepetition((Repetition) sde);
			}
		}
	}

	private void walkThroughRepetition(Repetition r)
			throws GenerateWordException {
		Concatenation leftPath = r.getLeft();
		walkThroughConcatenation(leftPath);
		if (Math.random() < repetitions.get(r)) {
			// walk through lower path
			Concatenation rightPath = r.getRight();
			walkThroughConcatenation(rightPath);
			// set random value for repetition nearer to zero
			if (repetitions.get(r) > 0)
				repetitions.put(r, repetitions.get(r) - 0.1);
			walkThroughRepetition(r);
		}
	}

	private double countBranches(Concatenation concat) {
		double count = 0;
		for (int i = 0; i < concat.getNumberOfElems(); i++) {
			SynDiaElem sde = concat.getSynDiaElem(i);
			if (sde instanceof Branch) {
				Branch b = (Branch) sde;
				count++;
				count += countBranches(b.getLeft());
				count += countBranches(b.getRight());
			}
		}
		return count;
	}

}
