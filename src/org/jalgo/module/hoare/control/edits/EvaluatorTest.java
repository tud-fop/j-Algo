package org.jalgo.module.hoare.control.edits;

import org.jalgo.module.hoare.control.Evaluation;
import org.jalgo.module.hoare.control.JepEvaluator;
import org.jalgo.module.hoare.model.AndAssertion;
import org.jalgo.module.hoare.model.Assertion;
import org.jalgo.module.hoare.model.ConcreteAssertion;

public class EvaluatorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Evaluation eval = new JepEvaluator(100,100);
		Assertion ass1 = new AndAssertion(	new ConcreteAssertion("s=Sum(j^2,j,1,i-1)"),
										  	new AndAssertion(	new ConcreteAssertion("i<=n+1"),
										  						new ConcreteAssertion("i<=n")));
		
		Assertion ass2 = new AndAssertion(	new ConcreteAssertion("s+i^2=Sum(j^2,j,1,i)"),
			  								new ConcreteAssertion("i<=n"));
		
		eval.evaluate(1, ass1, ass2);

	}

}
