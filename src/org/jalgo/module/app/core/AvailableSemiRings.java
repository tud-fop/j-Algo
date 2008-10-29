package org.jalgo.module.app.core;

import java.util.HashMap;
import java.util.Map;

import org.jalgo.main.util.Messages;
import org.jalgo.module.app.core.dataType.DataType;
import org.jalgo.module.app.core.dataType.Operation;
import org.jalgo.module.app.core.dataType.booleanType.BooleanType;
import org.jalgo.module.app.core.dataType.formalLanguage.FormalLanguage;
import org.jalgo.module.app.core.dataType.naturalNumber.NaturalNumber;
import org.jalgo.module.app.core.dataType.rationalNumber.PercentageType;
import org.jalgo.module.app.core.dataType.rationalNumber.PositiveRationalNumber;

/**
 * Provides static methods to get supported Semirings.
 */
public class AvailableSemiRings {

	public static String AVAILABILITY_PROBLEM_ID = "availability";
	public static String CAPACITY_PROBLEM_ID = "capacity";
	public static String PROCESS_PROBLEM_ID = "process";
	public static String RELIABILITY_PROBLEM_ID = "reliability";
	public static String SHORTEST_PATH_PROBLEM_ID = "shortestPath";

	private static Map<String, SemiRing> semiRings = null;

	/**
	 * Delivers all supported semirings.
	 * @return The map whose values are semirings and keys are the problem-IDs.
	 */
	public static Map<String, SemiRing> getSemiRings() {
		if (semiRings == null)
			buildSemiRings();

		return semiRings;
	}

	/**
	 * @param semiringID
	 *            The ID of the semiring. This is a string and is available as a
	 *            static field of AvailableSemiRings.
	 * @return The semiring with the given ID.
	 */
	public static SemiRing getSemiRing(String semiringID) {
		if (semiRings == null)
			buildSemiRings();

		return semiRings.get(semiringID);
	}

	/**
	 * Creates a new Map of all supported semirings. The keys are the
	 * problem-ids which are hold as static fields. For each value is a new
	 * semiring created.
	 * 
	 * This method defines the specific properties of each semiring, like name,
	 * description and definition.
	 */
	private static void buildSemiRings() {
		semiRings = new HashMap<String, SemiRing>();
		SemiRing ring;
		String definition;

		// FIXME: Remove definition
		definition = "(" + PositiveRationalNumber.getSymbolicRepresentation();
		definition += "," + PositiveRationalNumber.getOperationByID("min").getSymbolicRepresentation();
		definition += "," + PositiveRationalNumber.getOperationByID("add").getSymbolicRepresentation();
		definition += ",\u221E";
		definition += ",0)";
		ring = newSemiRing(PositiveRationalNumber.class, 
						   PositiveRationalNumber.getOperationByID("min"),
						   PositiveRationalNumber.getOperationByID("add"), 
						   Messages.getString("app","AvailableSemiRings.ShortestPathName"), 
						   definition,
						   Messages.getString("app","AvailableSemiRings.ShortestPathDescription"),
						   PositiveRationalNumber.getSymbolicRepresentation()
						  );
		semiRings.put(SHORTEST_PATH_PROBLEM_ID, ring);

		definition = "(" + NaturalNumber.getSymbolicRepresentation();
		definition += "," + NaturalNumber.getOperationByID("max").getSymbolicRepresentation();
		definition += "," + NaturalNumber.getOperationByID("min").getSymbolicRepresentation();
		definition += ",0";
		definition += ",\u221E)";
		ring = newSemiRing(NaturalNumber.class, 
						   NaturalNumber.getOperationByID("max"), 
						   NaturalNumber.getOperationByID("min"), 
						   Messages.getString("app","AvailableSemiRings.CapacityName"),  
						   definition, 
						   Messages.getString("app","AvailableSemiRings.CapacityDescription"),  
						   NaturalNumber.getSymbolicRepresentation()
						  );
		semiRings.put(CAPACITY_PROBLEM_ID, ring);

		definition = "(" + BooleanType.getSymbolicRepresentation();
		definition += "," + BooleanType.getOperationByID("disjunct").getSymbolicRepresentation();
		definition += "," + BooleanType.getOperationByID("conjunct").getSymbolicRepresentation();
		definition += ",false";
		definition += ",true)";
		ring = newSemiRing(BooleanType.class, 
						   BooleanType.getOperationByID("disjunct"), 
						   BooleanType.getOperationByID("conjunct"), 
						   Messages.getString("app","AvailableSemiRings.AvailabilityName"), 
						   definition, 
						   Messages.getString("app","AvailableSemiRings.AvailabilityDescription"), 
						   BooleanType.getSymbolicRepresentation()
						  );
		semiRings.put(AVAILABILITY_PROBLEM_ID, ring);

		definition = "(" + PercentageType.getSymbolicRepresentation();
		definition += "," + PercentageType.getOperationByID("max").getSymbolicRepresentation();
		definition += "," + PercentageType.getOperationByID("mult").getSymbolicRepresentation();
		definition += ",0";
		definition += ",1)";
		ring = newSemiRing(PercentageType.class, 
						   PercentageType.getOperationByID("max"), 
						   PercentageType.getOperationByID("mult"), 
						   Messages.getString("app","AvailableSemiRings.ReliabilityName"), 
						   definition,
						   Messages.getString("app","AvailableSemiRings.ReliabilityDescription"),
						   PercentageType.getSymbolicRepresentation()
						  );
		semiRings.put(RELIABILITY_PROBLEM_ID, ring);

		definition = "(" + FormalLanguage.getSymbolicRepresentation();
		definition += "," + FormalLanguage.getOperationByID("union").getSymbolicRepresentation();
		definition += "," + FormalLanguage.getOperationByID("concat").getSymbolicRepresentation();
		definition += ",\u2205";
		definition += ",(\u03B5))";
		ring = newSemiRing(FormalLanguage.class, 
						   FormalLanguage.getOperationByID("union"), 
						   FormalLanguage.getOperationByID("concat"), 
						   Messages.getString("app","AvailableSemiRings.ProcessName"),
						   definition,
						   Messages.getString("app","AvailableSemiRings.ProcessDescription"),
						   FormalLanguage.getSymbolicRepresentation()
						  );
		semiRings.put(PROCESS_PROBLEM_ID, ring);
	}

	/**
	 * @param cl
	 *            The class the DataType.
	 * @param plus
	 *            The plus operation.
	 * @param dot
	 *            The dot operation.
	 * @param name
	 *            The name as a string.
	 * @param definiton
	 *            The formal definition in form of a tuple.
	 * @param description
	 *            The description in a non formal speech.
	 * @return An Object form type SemiRing with the given properties.
	 */
	private static SemiRing newSemiRing(Class<? extends DataType> cl, 
									    Operation plus, 
									    Operation dot, 
									    String name,
									    String definiton, 
									    String description,
									    String[] representation
									   ) 
	{
		SemiRing ring;

		if (plus == null || dot == null)
			throw new NullPointerException();

		ring = new SemiRing(cl, plus, dot);
		ring.setName(name);
		ring.setDefinition(definiton);
		ring.setDescription(description);
		ring.setTypeRepresentation(representation);

		return ring;
	}

}
