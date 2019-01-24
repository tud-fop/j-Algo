package org.jalgo.module.unifikation.junit;

import static org.junit.Assert.*;

import org.jalgo.module.unifikation.Constants;
import org.jalgo.module.unifikation.algo.model.ConstructorSymbol;
import org.jalgo.module.unifikation.algo.model.ITerm;
import org.jalgo.module.unifikation.algo.model.Pair;
import org.jalgo.module.unifikation.algo.model.ProblemSet;
import org.jalgo.module.unifikation.algo.model.Variable;
import org.junit.Before;
import org.junit.Test;

public class TestRules {
	//already existing terms: x,y,z,u,alpha,beta,gamma,delta
	//alpha(x),alpha(z),beta(z),alpha(beta()),alpha(beta(z))
	//delta(z),beta(delta(z))
	
	private Pair substPair1;
	private Pair substPair2;
	private Pair substPair3;
	private Pair substPair4;
	private Pair substPair5;
	private Pair substPair6;
	private Pair substPair7;	
	private Pair decompPair1;
	private Pair decompPair2;
	private Pair decompPair3;
	private Pair decompPair4;
	private Pair elimPair1;
	private Pair elimPair2;
	private Pair elimPair3;
	private Pair elimPair4;
	private Pair elimPair5;
	private Pair swapPair1;
	private Pair swapPair2;
	private Pair swapPair3;
	private Pair swapPair4;
	private Pair swapPair5;
	private Pair swapPair6;
	private Pair swapPair1Result;
	private Pair swapPair2Result;
	private Pair swapPair3Result;
	private Pair swapPair4Result;
	private Pair swapPair5Result;
	private Pair swapPair6Result;
	
	private ProblemSet problemSet;
	
	private Variable varX;
	private Variable varY;
	private Variable varZ;
	private Variable varU;
	
	private ConstructorSymbol constrAlpha;
	private ConstructorSymbol constrBeta;
	
	private ITerm alphaWithX;
	private ITerm alphaWithZ;
	private ITerm betaWithZ;
	private ITerm alphaWithBeta;
	private ITerm alphaWithBetaWithZ;
	private ITerm deltaWithZ;
	private ITerm betaWithDeltaWithZ;
	
	@Before
	public void setUp() throws Exception {
		
	//term setUp
		//variables
		varX = new Variable("x");
		varY = new Variable("y");
		varZ = new Variable("z");
		varU = new Variable("u");
	
		//constructors
		constrAlpha = new ConstructorSymbol(Constants.ALPHA);
		constrBeta = new ConstructorSymbol(Constants.BETA);
	
		//terms with arguments
		alphaWithX = new ConstructorSymbol(Constants.ALPHA);
		alphaWithX.addParameter(varX);
		alphaWithZ = new ConstructorSymbol(Constants.ALPHA);
		alphaWithZ.addParameter(varZ);
		
		alphaWithBeta = new ConstructorSymbol(Constants.ALPHA);
		alphaWithBeta.addParameter(constrBeta);
		betaWithZ = new ConstructorSymbol(Constants.BETA);
		betaWithZ.addParameter(varZ);
		alphaWithBetaWithZ = new ConstructorSymbol(Constants.ALPHA);
		alphaWithBetaWithZ.addParameter(betaWithZ);
		deltaWithZ = new ConstructorSymbol(Constants.DELTA);
		deltaWithZ.addParameter(varZ);
		betaWithDeltaWithZ = new ConstructorSymbol(Constants.BETA);
		betaWithDeltaWithZ.addParameter(deltaWithZ);
			
	//pairs for rules
		//substitution
		substPair1 = new Pair(varX, alphaWithZ);
		substPair2 = new Pair(varX, alphaWithX);
		substPair3 = new Pair(alphaWithZ, varX);
		substPair4 = new Pair(varX, varX);
		substPair5 = new Pair(alphaWithBeta, alphaWithBetaWithZ);
		substPair6 = new Pair(varU, alphaWithBetaWithZ);
		substPair7 = new Pair(varU, varU);
		
		//decomposition
		decompPair1 = new Pair(varX, varX);
		decompPair2 = new Pair(alphaWithX, alphaWithZ);
		decompPair3 = new Pair(betaWithDeltaWithZ, betaWithZ);
		decompPair4 = new Pair(varX, varY);
		
		//elimination
		elimPair1 = new Pair(varX, varX);
		elimPair2 = new Pair(varX, varY);
		elimPair3 = new Pair(varX, alphaWithX);
		elimPair4 = new Pair(betaWithZ, betaWithZ);
		elimPair5 = new Pair(constrAlpha, constrAlpha);
		
		//swap
		swapPair1 = new Pair(constrAlpha, varX);
		swapPair1Result = new Pair(varX, constrAlpha);
		swapPair2 = new Pair(varX, constrAlpha);
		swapPair2Result = new Pair(varX, constrAlpha);
		swapPair3 = new Pair(varX, varX);
		swapPair3Result = new Pair(varX, varX);
		swapPair4 = new Pair(alphaWithBetaWithZ, varU);
		swapPair4Result = new Pair(varU, alphaWithBetaWithZ);
		swapPair5 = new Pair(alphaWithBeta, alphaWithX);
		swapPair5Result = new Pair(alphaWithBeta, alphaWithX);
		swapPair6 = new Pair(varX, varY);
		swapPair6Result = new Pair(varX, varY);

		problemSet = new ProblemSet();
	}
	
	@Test	
	public void testSubstitutionCheck(){
		assertTrue(substPair1.substitutionCheck());
		assertFalse(substPair2.substitutionCheck());
		assertFalse(substPair3.substitutionCheck());
		assertFalse(substPair4.substitutionCheck());
		assertFalse(substPair5.substitutionCheck());
		assertTrue(substPair6.substitutionCheck());
	}
	
	@Test
	public void testDecompositionCheck(){
		assertFalse(decompPair1.decompositionCheck());
		assertTrue(decompPair2.decompositionCheck());
		assertTrue(decompPair3.decompositionCheck());
		assertFalse(decompPair4.decompositionCheck());
	}
	
	@Test
	public void testEliminationCheck(){
		assertTrue(elimPair1.eliminationCheck());
		assertFalse(elimPair2.eliminationCheck());
		assertFalse(elimPair3.eliminationCheck());
		assertFalse(elimPair4.eliminationCheck());
		assertFalse(elimPair5.eliminationCheck());
	}
	
	@Test
	public void testSwapCheck(){
		assertTrue(swapPair1.swapCheck());
		assertFalse(swapPair2.swapCheck());
		assertFalse(swapPair3.swapCheck());
		assertTrue(swapPair4.swapCheck());
		assertFalse(swapPair5.swapCheck());
		assertFalse(swapPair6.swapCheck());
	}

	@Test
	public void testSubstitution(){
		//{(x, alpha(z)) , (x, x)}
		problemSet.addPair(substPair1);
		problemSet.addPair(substPair4);
		problemSet.setSelectedPair(0);
		assertTrue(problemSet.substitution());
		
		//{(x, alpha(x)) , (x, x)}
		problemSet.removeAllPairs();
		problemSet.addPair(substPair2);
		problemSet.addPair(substPair4);
		problemSet.setSelectedPair(0);
		assertFalse(problemSet.substitution());
		
		//{(alpha(z), x) , (x, x)}
		problemSet.removeAllPairs();
		problemSet.addPair(substPair3);
		problemSet.addPair(substPair4);
		problemSet.setSelectedPair(0);
		assertFalse(problemSet.substitution());
		
		//{(x, x) , (x, x)}
		problemSet.removeAllPairs();
		problemSet.addPair(substPair4);
		problemSet.addPair(substPair4);
		problemSet.setSelectedPair(0);
		assertFalse(problemSet.substitution());
		
		//{(alpha(beta()), gamma(x)) , (x, x)}
		problemSet.removeAllPairs();
		problemSet.addPair(substPair5);
		problemSet.addPair(substPair4);
		problemSet.setSelectedPair(0);
		assertFalse(problemSet.substitution());
		
		//{(u, alpha(beta(z))) , (u, u)}
		problemSet.removeAllPairs();
		problemSet.addPair(substPair6);
		problemSet.addPair(substPair7);
		problemSet.setSelectedPair(0);
		assertTrue(problemSet.substitution());
	}
	
	@Test
	public void testDecomposition(){
		problemSet.removeAllPairs();
		problemSet.addPair(decompPair1);
		problemSet.setSelectedPair(0);
		assertFalse(problemSet.decomposition());
		
		problemSet.removeAllPairs();
		problemSet.addPair(decompPair2);
		problemSet.setSelectedPair(0);
		assertTrue(problemSet.decomposition());
		
		problemSet.removeAllPairs();
		problemSet.addPair(decompPair3);
		problemSet.setSelectedPair(0);
		assertTrue(problemSet.decomposition());
		
		problemSet.removeAllPairs();
		problemSet.addPair(decompPair4);
		problemSet.setSelectedPair(0);
		assertFalse(problemSet.decomposition());
	}
	
	@Test
	public void testElimination(){
		problemSet.removeAllPairs();
		problemSet.addPair(elimPair1);
		problemSet.setSelectedPair(0);
		assertTrue(problemSet.elimination());
		
		problemSet.removeAllPairs();
		problemSet.addPair(elimPair2);
		problemSet.setSelectedPair(0);
		assertFalse(problemSet.elimination());
		
		problemSet.removeAllPairs();
		problemSet.addPair(elimPair3);
		problemSet.setSelectedPair(0);
		assertFalse(problemSet.elimination());
		
		problemSet.removeAllPairs();
		problemSet.addPair(elimPair4);
		problemSet.setSelectedPair(0);
		assertFalse(problemSet.elimination());
		
		problemSet.removeAllPairs();
		problemSet.addPair(elimPair5);
		problemSet.setSelectedPair(0);
		assertFalse(problemSet.elimination());
	}
	
	@Test
	public void testSwap(){
		problemSet.removeAllPairs();
		problemSet.addPair(swapPair1);
		problemSet.setSelectedPair(0);
		assertTrue(problemSet.swap());
		
		problemSet.removeAllPairs();
		problemSet.addPair(swapPair2);
		problemSet.setSelectedPair(0);
		assertFalse(problemSet.swap());
		
		problemSet.removeAllPairs();
		problemSet.addPair(swapPair3);
		problemSet.setSelectedPair(0);
		assertFalse(problemSet.swap());
		
		problemSet.removeAllPairs();
		problemSet.addPair(swapPair4);
		problemSet.setSelectedPair(0);
		assertTrue(problemSet.swap());
		
		problemSet.removeAllPairs();
		problemSet.addPair(swapPair5);
		problemSet.setSelectedPair(0);
		assertFalse(problemSet.swap());
		
		problemSet.removeAllPairs();
		problemSet.addPair(swapPair6);
		problemSet.setSelectedPair(0);
		assertFalse(problemSet.swap());
	}
	
	@Test
	public void testSwapByComparison(){
		swapPair1.swap();
		assertEquals(swapPair1.toString(), swapPair1Result.toString());
		
		swapPair2.swap();
		assertEquals(swapPair2.toString(), swapPair2Result.toString());
		
		swapPair3.swap();
		assertEquals(swapPair3.toString(), swapPair3Result.toString());
		
		swapPair4.swap();
		assertEquals(swapPair4.toString(), swapPair4Result.toString());
		
		swapPair5.swap();
		assertEquals(swapPair5.toString(), swapPair5Result.toString());
		
		swapPair6.swap();
		assertEquals(swapPair6.toString(), swapPair6Result.toString());
	}


}
