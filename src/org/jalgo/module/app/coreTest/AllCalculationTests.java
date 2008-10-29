package org.jalgo.module.app.coreTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
 
@RunWith(Suite.class)
@Suite.SuiteClasses({
  SimpleCalculationTest.class,
  DijkstraComparisonTest.class,
  AvailabilityProblemTest.class,
  CapacityProblemTest.class,
  FormalLanguageTest.class
})
public class AllCalculationTests {
    // the class remains completely empty, 
    // being used only as a holder for the above annotations
	
}