package org.jalgo.module.unifikation.junit;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.jalgo.module.unifikation.*;
import org.jalgo.module.unifikation.parser.*;

public class TestSetParser {
	
	private ISetParser parser;
	private ParserError lexerError;
	private ParserError parserError;
	private ParserError invalidArity;
	
	@Before
	public void setUp() throws Exception {
				
		parser = new SetParser();
		
		//Errors
		lexerError = new ParserError(ParserErrorType.LexerError);
		parserError = new ParserError(ParserErrorType.ParserError);
		invalidArity = new ParserError(ParserErrorType.InvalidArity);
		
	}
	
	@Test
	public void testSetParser(){
		//Test für Übergabe des Nullpointers
			assertFalse("Fehler bei Nullpointerübergabe an Parser", parser.parse(null));
		
		//Test korrekter Terme
			assertTrue("Fehler beim Parsen von M={(x1,x1)}. Expected: 'true' but was 'false'", parser.parse("M={(x1,x1)}"));
			assertTrue("Fehler beim Parsen von M={(alpha(x1,x2,gamma(x3)),theta(x1))}. Expected: 'true' but was 'false'", parser.parse("M={(" + Constants.ALPHA + "(x1,x2," + Constants.GAMMA + "(x3))," + Constants.THETA + "(x1))}"));
			assertTrue("Fehler beim Parsen von M={(x1 , alpha(x1))}. Expected: 'true' but was 'false'.", parser.parse("M={(x1," + Constants.ALPHA + "(x1))}"));
			assertTrue("Fehler beim Parsen von M={(gamma(epsilon(theta())) , alpha(beta(delta(alpha(x1)))))}. Expected: 'true' but was 'false'.", parser.parse("M={(" + Constants.GAMMA + "(" + Constants.EPSILON + "(" + Constants.THETA + "))," + Constants.ALPHA + "(" + Constants.BETA + "(" + Constants.DELTA + "(" + Constants.ALPHA + "(x1)))))}"));
			assertTrue("Fehler beim Parsen von M={(alpha(x,y,z,u,v,m),beta(x0,x17,x23,x189,x92834,x3848984)}. Expected: 'true' but was 'false'", parser.parse("M={(" + Constants.ALPHA + "(x,y,z,u,v,w)," + Constants.BETA + "(x1,x17,x23,x189,x92834,x3848984))}"));

		//Test inkorrekter Terme
			//leeres Problem
			assertFalse("Fehler beim Parsen von M={(,)}. Expected: 'false' but was 'true'. Input: leere Menge", parser.parse("M={(,)}"));
			
			//fehlendes Komma
			assertFalse("Fehler beim Parsen von M={(x1 x1)}. Expected: 'false' but was 'true'. Input: fehlendes Komma", parser.parse("M={(x1 x1)}"));
			assertFalse("Fehler beim Parsen von M={(alpha(x1,x2 gamma(x3)),theta(x1))}. Expected: 'false' but was 'true'. Input: fehlendes Komma", parser.parse("M={(" + Constants.ALPHA + "(x1,x2" + Constants.GAMMA + "(x3))," + Constants.THETA + "(x1))}"));
			
			//falsche Stelligkeit
			assertFalse("Fehler beim Parsen von M={(alpha(gamma(x1,alpha),x3) , alpha(gamma(x1,x3),x3))}. Expected: 'false' but was 'true'. Input: Term mit falscher Stelligkeit", parser.parse("M={(" + Constants.ALPHA + "(" + Constants.GAMMA + "(x1," + Constants.ALPHA + "),x3) , " + Constants.ALPHA +"(" + Constants.GAMMA + "(x1,x3),x3))}"));
			assertFalse("Fehler beim Parsen von M={(gamma(epsilon(theta())) , alpha(beta(delta(alpha()))))}. Expected: 'false' but was 'true'. Input: Term mit falscher Stelligkeit", parser.parse("M={(" + Constants.GAMMA + "(" + Constants.EPSILON + "(" + Constants.THETA + "))," + Constants.ALPHA + "(" + Constants.BETA + "(" + Constants.DELTA + "(" + Constants.ALPHA + "))))}"));
	
			//fehlende Klammer
			assertFalse("Fehler beim Parsen von M={(x1 , alpha(x1)}. Expected: 'false' but was 'true'. Input: fehlende Klamme", parser.parse("M={(x1," + Constants.ALPHA + "(x1)}"));
			assertFalse("Fehler beim Parsen von M={(alpha(x1,x2,gamma x3)),theta(x1))}. Expected: 'false' but was 'true'. Input: fehlende Klamme", parser.parse("M={(" + Constants.ALPHA + "(x1,x2," + Constants.GAMMA + "x3))," + Constants.THETA + "(x1))}"));
			assertFalse("Fehler beim Parsen von M={(x1 , alpha(x1)). Expected: 'false' but was 'true'. Input: fehlende Klamme", parser.parse("M={(x1," + Constants.ALPHA + "(x1)"));
			assertFalse("Fehler beim Parsen von M=(x1 , alpha(x1))}. Expected: 'false' but was 'true'. Input: fehlende Klamme", parser.parse("M=(x1," + Constants.ALPHA + "(x1)}"));
			
			//undefinierte Zeichen
			assertFalse("Fehler beim Parsen von M={(alpha(a,b,c,d,e,g),beta(x0,x17,x23,x189,x92834,x3848984)}. Expected: 'false' but was 'true'", parser.parse("M={(" + Constants.ALPHA + "(a,b,c,d,e,f)," + Constants.BETA + "(x1,x17,x23,x189,x92834,x3848984))}"));
			assertFalse("Fehler beim Parsen von M={(alpha(x,y,z,u,v,w),beta(x-1,x-17)}. Expected: 'false' but was 'true'", parser.parse("M={(" + Constants.ALPHA + "(x,y,z,u,v,w)," + Constants.BETA + "(x-1,x-17))}"));
			assertFalse("Fehler beim Parsen von M={(alpha(x,y,z,u,v,w),beta(xtest,xtest2)}. Expected: 'false' but was 'true'", parser.parse("M={(" + Constants.ALPHA + "(x,y,z,u,v,w)," + Constants.BETA + "(xtest,xtest2))}"));

	}
	
	@Test
	public void testGetErrorsParser(){
		
			//leeres Problem erwartet ParserError
			parser.parse("M={(,)}");
			assertSame("M={(,)} erwartete ParserError.", parser.getErrors().get(0).getType(),parserError.getType());
			 
			//fehlendes Komma erwartet ParserError
		 	parser.parse("M={(x1 x1)}");
		 	assertSame("M={(x1 x1)} erwartete ParserError.", parser.getErrors().get(0).getType(),parserError.getType());
		 	parser.parse("M={(" + Constants.ALPHA + "(x1,x2" + Constants.GAMMA + "(x3))," + Constants.THETA + "(x1))}");
		 	assertSame("M={(alpha(x1,x2 gamma(x3)),theta(x1))} erwartete ParserError.", parser.getErrors().get(0).getType(),parserError.getType());
		 	
			//falsche Stelligkeit erwartet InvalidArity
			parser.parse("M={(" + Constants.ALPHA + "(" + Constants.GAMMA + "(x1," + Constants.ALPHA + "),x3) , " + Constants.ALPHA +"(" + Constants.GAMMA + "(x1,x3),x3))}");
			assertSame("M={(alpha(gamma(x1,alpha),x3) , alpha(gamma(x1,x3),x3))} erwartete invalidArity.", parser.getErrors().get(0).getType(),invalidArity.getType());
			parser.parse("M={(" + Constants.GAMMA + "(" + Constants.EPSILON + "(" + Constants.THETA + "))," + Constants.ALPHA + "(" + Constants.BETA + "(" + Constants.DELTA + "(" + Constants.ALPHA + "))))}");
			assertSame("M={(gamma(epsilon(theta())) , alpha(beta(delta(alpha()))))} erwartete invalidArity.", parser.getErrors().get(0).getType(),invalidArity.getType());
			
			//fehlende Klammer erwartet ParserError
			parser.parse("M={(x1," + Constants.ALPHA + "(x1)}");
			assertSame("M={(x1, alpha(x1)} erwartete ParserError.", parser.getErrors().get(0).getType(),parserError.getType());
			parser.parse("M={(" + Constants.ALPHA + "(x1,x2," + Constants.GAMMA + "x3))," + Constants.THETA + "(x1))}");
			assertSame("M={(alpha(x1,x2,gamma x3)),theta(x1))} erwartete ParserError.", parser.getErrors().get(0).getType(),parserError.getType());
			parser.parse("M={(x1," + Constants.ALPHA + "(x1)");
			assertSame("M={(x1 , alpha(x1)) erwartete ParserError.", parser.getErrors().get(0).getType(),parserError.getType());
			parser.parse("M=(x1," + Constants.ALPHA + "(x1)}");
			assertSame("M=(x1 , alpha(x1))} erwartete ParserError.", parser.getErrors().get(0).getType(),parserError.getType());
			
			//undefinierte Zeichen erwartet LexerError
			parser.parse("M={(" + Constants.ALPHA + "(a,b,c,d,e,f)," + Constants.BETA + "(x1,x17,x23,x189,x92834,x3848984))}");
			assertSame("M={(alpha(a,b,c,d,e,g),beta(x0,x17,x23,x189,x92834,x3848984)} erwartete LexerError.", parser.getErrors().get(0).getType(),lexerError.getType());
			parser.parse("M={(" + Constants.ALPHA + "(x,y,z,u,v,w)," + Constants.BETA + "(x-1,x-17))}");
			assertSame("M={(alpha(x,y,z,u,v,w),beta(x-1,x-17)} erwartete LexerError.", parser.getErrors().get(0).getType(),lexerError.getType());
			parser.parse("M={(" + Constants.ALPHA + "(x,y,z,u,v,w)," + Constants.BETA + "(xtest,xtest2))}");
			assertSame("M={(alpha(x,y,z,u,v,w),beta(xtest,xtest2)} erwartete LexerError.", parser.getErrors().get(0).getType(),lexerError.getType());
		
	}

}
