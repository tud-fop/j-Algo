package org.jalgo.module.kmp.algorithm;

import org.jalgo.module.kmp.algorithm.phaseone.*;
import org.jalgo.module.kmp.algorithm.phasetwo.*;

/**
 * This class implements a factory for producing the steps which represent parts
 * of the original KMP algorithm
 * 
 * @author Elisa Böhl, Matthias Neubert
 */
public class StepFactory {
	private Pattern pattern;

	private String searchtext;

	private History history;

	/**
	 * This method is for phase one (production of the shifting table). It fills
	 * the History with steps which represent parts of the original KMP
	 * algorithm by adding steps produced by performNextStepP1()
	 * 
	 * @param startstep the startstep
	 */
	private void fillHistoryP1(Step startstep) {
		Step aktstep = performNextStepP1(startstep);
		while (aktstep != null) {
			history.addStep(aktstep);
			aktstep = performNextStepP1(aktstep);
		}
	}

	/**
	 * This procedure starts the algorithm Phase 2 (Patternsearching in a text)
	 * and fills the history with steps, which represent the algorithmstates
	 * 
	 * @param startstep the startstep
	 */
	private void fillHistoryP2(Step startstep) {
		Step aktstep = performNextStepP2(startstep);
		while (aktstep != null) {
			history.addStep(aktstep);
			aktstep = performNextStepP2(aktstep);
		}
	}

	/**
	 * This method initializes phase one. Called by the controller.
	 * At first it clears the history and then creates the first step
	 * (P1InitStep) with PatPos and VglInd set zero. After adding this firststep
	 * to the history fillHistoryP1() is called by using P1InitStep as a
	 * root-step
	 * 
	 * @param p the actual <code>Pattern</code>
	 * @param h the <code>History</code>
	 */
	public void startPhaseOne(Pattern p, History h) {
		pattern = p;
		history = h;
		history.clear();
		Step startstep = new P1InitStep(0, 0); // patpos==0 is interpreted as:
		// make PatPos arrow invisble
		history.addStep(startstep);
		fillHistoryP1(startstep);
	}

	/**
	 * This procedure starts with all needed objects to calculate the list of
	 * algorithmsates (called History) for Phase 2 (Pattermsearch in a Text). it
	 * sets the values, initializes the History and fills it with steps
	 * 
	 * @param p the <code>Pattern</code>
	 * @param s the searchtext
	 * @param h the <code>History</code>
	 */
	public void startPhaseTwo(Pattern p, String s, History h) {
		pattern = p;
		searchtext = s;
		history = h;
		history.clear();
		Step startstep = new P2InitStep(0, 0);
		history.addStep(startstep);
		fillHistoryP2(startstep);
	}

	/**
	 * This method produces -based on the actual step- a new step corrensponding
	 * to the original KMP algorithm. (steps represent parts or statements of
	 * the original C - sourcecode)
	 * 
	 * @param step the actual <code>Step</code>
	 * 
	 * @return Step the next <code>Step</code>
	 */
	private Step performNextStepP1(Step step) {

		if (step instanceof P1InitStep)
			return new P1BeginForStep(1, 0, false);

		if (step instanceof P1BeginForStep) {
			if (pattern.getPattern().length() == 1)	return null;
			
			if (!((P1BeginForStep) step).isLastForStep()) {

				if (pattern.getPattern().charAt(step.getPatPos()) != pattern
						.getPattern().charAt(
								((P1BeginForStep) step).getVglInd()))
					return new P1IfFalseStep(step.getPatPos(),
							((P1BeginForStep) step).getVglInd(), pattern
									.getPattern().charAt(step.getPatPos()),
							pattern.getPattern().charAt(
									((P1BeginForStep) step).getVglInd()));

				else
					return new P1IfTrueStep(step.getPatPos(),
							((P1BeginForStep) step).getVglInd(), pattern
									.getPattern().charAt(step.getPatPos()),
							pattern.getPattern().charAt(
									((P1BeginForStep) step).getVglInd()));
			}

			else return null;
		}

		if (step instanceof P1IfFalseStep)
			if ((((P1IfFalseStep) step).getVglInd() >= 0)
					&& (pattern.getPattern().charAt(step.getPatPos()) != pattern
							.getPattern().charAt(
									((P1IfFalseStep) step).getVglInd())))
				return new P1WhileStep(step.getPatPos(), pattern
						.getTblEntryAt(((P1IfFalseStep) step).getVglInd()),
						false, 0, ((P1IfFalseStep) step).getVglInd());

		if (step instanceof P1IfTrueStep)
			return new P1EndForStep(step.getPatPos(), ((P1IfTrueStep) step)
					.getVglInd() + 1);

		if (step instanceof P1WhileStep) {
			if (!((P1WhileStep) step).isLastWhileStep()) {
				if ((((P1WhileStep) step).getVglInd() >= 0)
						&& (pattern.getPattern().charAt(step.getPatPos()) != pattern
								.getPattern().charAt(
										((P1WhileStep) step).getVglInd())))
					return new P1WhileStep(step.getPatPos(), pattern
							.getTblEntryAt(((P1WhileStep) step).getVglInd()),
							false, 0, ((P1WhileStep) step).getVglInd());

				if (((P1WhileStep) step).getVglInd() < 0)
					return new P1WhileStep(step.getPatPos(),
							((P1WhileStep) step).getVglInd(), true, 1,
							((P1WhileStep) step).getVglInd());

				if (pattern.getPattern().charAt(step.getPatPos()) == pattern
						.getPattern().charAt(((P1WhileStep) step).getVglInd()))
					return new P1WhileStep(step.getPatPos(),
							((P1WhileStep) step).getVglInd(), true, 2,
							((P1WhileStep) step).getVglInd());
			}

			else return new P1EndForStep(step.getPatPos(), ((P1WhileStep) step)
						.getVglInd() + 1);
		}

		if (step instanceof P1EndForStep) {
			if (step.getPatPos() == (pattern.getPattern().length() - 1))
				return new P1BeginForStep(step.getPatPos() + 1,
						((P1EndForStep) step).getVglInd(), true);

			else return new P1BeginForStep(step.getPatPos() + 1,
						((P1EndForStep) step).getVglInd(), false);
		}

		return null;

	}

	/**
	 * this function returns the fallowing P2step of a given P2step. if the
	 * given step is the last step who can be created, it will be returned null
	 * 
	 * @param step the old <code>Step</code>
	 * 
	 * @return the new <code>Step</code>
	 */
	private Step performNextStepP2(Step step) {
		if (step instanceof P2InitStep) {
			if ((step.getPatPos() >= (pattern.getPattern()).length())
					|| (((P2InitStep) step).getTextPos() >= searchtext.length()))
				return new P2OuterWhileStep(step.getPatPos(),
						((P2InitStep) step).getTextPos(), true);
			else return new P2OuterWhileStep(step.getPatPos(),
						((P2InitStep) step).getTextPos(), false);
		}

		if (step instanceof P2OuterWhileStep) {
			if (((P2OuterWhileStep) step).isLastWhileStep()) {
				if (step.getPatPos() >= (pattern.getPattern()).length())
					return new P2EndStep(step.getPatPos(),
							((P2OuterWhileStep) step).getTextPos(), true);
				else return new P2EndStep(step.getPatPos(),
							((P2OuterWhileStep) step).getTextPos(), false);
			}
			else if ((step.getPatPos() >= 0)
					&& ((pattern.getPattern()).charAt(step.getPatPos()) != searchtext
							.charAt(((P2OuterWhileStep) step).getTextPos()))) {
				int tableEntry = pattern.getTblEntryAt(step.getPatPos());
				int tp = ((P2OuterWhileStep) step).getTextPos();
				return new P2InnerWhileStep(tableEntry, tp, false, step
						.getPatPos()
						- tableEntry, (pattern.getPattern()).length());

			}
			else {
				int tp = ((P2OuterWhileStep) step).getTextPos();
				return new P2InnerWhileStep(step.getPatPos(), tp, true, 0,
						(pattern.getPattern()).length());
			}
		}

		if (step instanceof P2InnerWhileStep) {
			if (((P2InnerWhileStep) step).isLastWhileStep())
				return new P2InnerSetStep(step.getPatPos() + 1,
						((P2InnerWhileStep) step).getTextPos() + 1);
			else if ((step.getPatPos() >= 0)
					&& ((pattern.getPattern()).charAt(step.getPatPos()) != searchtext
							.charAt(((P2InnerWhileStep) step).getTextPos()))) {
				int tableEntry = pattern.getTblEntryAt(step.getPatPos());
				int tp = ((P2InnerWhileStep) step).getTextPos();
				return new P2InnerWhileStep(tableEntry, tp, false, step
						.getPatPos()
						- tableEntry, (pattern.getPattern()).length());
			}
			else {
				int tp = ((P2InnerWhileStep) step).getTextPos();
				return new P2InnerWhileStep(step.getPatPos(), tp, true, 0,
						(pattern.getPattern()).length());
			}
		}

		if (step instanceof P2InnerSetStep) {
			if ((step.getPatPos() >= (pattern.getPattern()).length())
					|| (((P2InnerSetStep) step).getTextPos() >= searchtext
							.length()))
				return new P2OuterWhileStep(step.getPatPos(),
						((P2InnerSetStep) step).getTextPos(), true);

			else return new P2OuterWhileStep(step.getPatPos(),
						((P2InnerSetStep) step).getTextPos(), false);
		}

		if (step instanceof P2EndStep) return null;

		return null;
	}
}
