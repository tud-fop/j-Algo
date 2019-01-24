package org.jalgo.module.app.core.dataType.formalLanguage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jalgo.module.app.core.dataType.DataType;
import org.jalgo.module.app.core.dataType.Operation;

public class FormalLanguage extends DataType {

	/*
	 * (non-Javadoc) CLASS METHODS
	 */

	private static final long serialVersionUID = 4065198973755158572L;

	public static Set<Operation> getOperations() {
		Set<Operation> operations;

		operations = new HashSet<Operation>();

		operations.add(new Union());
		operations.add(new Concatenate());

		return operations;
	}

	public static Operation getOperationByID(String id) {
		for (Operation op : getOperations()) {
			if (op.getID().equals(id))
				return op;
		}

		return null;
	}

	public static String getName() {
		return "Formal Language";
	}

	public static String[] getSymbolicRepresentation() {
		String[] returnValue = { "P(\u03A3*)", "", "" };

		return returnValue;
	}

	/*
	 * (non-Javadoc) INSTANCE METHODS
	 */

	private FormalLanguageObject language;

	/**
	 * Sets the default value for a formal language of the set with epsilon.
	 */
	public FormalLanguage() {
		language = emptyLanguage();
	}

	public FormalLanguage(String value) {
		this();
		setFromString(value);
	}

	public FormalLanguage(FormalLanguage other) {
		if (other.language != null)
			language = other.language.clone();
	}

	/**
	 * Sets the language by parsing the given string.
	 * 
	 * @param str
	 *            the string to parse
	 * @return <code>true</code> if successful, <code>false</code> otherwise
	 */
	public boolean setFromString(String str) {
		if (str == "\u2205")
			str = "";

		try {
			language = languageFromString(str);

			simplify();

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isEmpty() {
		return (language == null);
	}

	public String toString() {
		if (language != null)
			return language.toString();
		else
			return "\u2205";
	}

	public boolean equals(Object ot) {
		FormalLanguage other;

		if (ot == null || !(ot instanceof FormalLanguage)) {
			return false;
		}

		other = (FormalLanguage) ot;
		if (language == null) {
			if (other.language == null)
				return true;
			else
				return false;
		} else {
			return (language.equals(other.language));
		}
	}

	public FormalLanguage clone() {
		return new FormalLanguage(this);
	}

	/**
	 * Simplifies the whole language.
	 */
	private void simplify() {
		if (language == null)
			return;

		/*
		 * Simplify language
		 */
		FormalLanguageObject newLanguage;
		boolean change;

		do {
			newLanguage = language.simplify();
			change = (newLanguage != null && !newLanguage.equals(language));
			language = newLanguage;
		} while (change);

		/*
		 * Simplify root element
		 */
		if ((language instanceof OptionLanguageObject)
				&& ((OptionLanguageObject) language).getOptions().size() == 1) {
			language = ((OptionLanguageObject) language).getOptions().first();
		}
		if ((language instanceof ConcatenationLanguageObject)
				&& ((ConcatenationLanguageObject) language).getParts().size() == 1) {
			language = ((ConcatenationLanguageObject) language).getParts().get(
					0);
		}
	}

	/**
	 * Creates empty <code>FormalLanguageObject</code>.
	 * 
	 * @return a empty formal language object
	 */
	static FormalLanguageObject emptyLanguage() {
		return null;
	}

	/**
	 * Creates a <code>FormalLanguageObject</code> from the given string.
	 * 
	 * @param str
	 *            the formal language as string
	 * @return a formal language object
	 */
	static FormalLanguageObject languageFromString(String str) {
		List<String> parts;

		parts = splitLanguageString(str);

		if (parts.size() == 0)
			return emptyLanguage();
		if (parts.size() >= 2)
			return new ConcatenationLanguageObject(parts);

		// Single Object
		if (str.charAt(str.length() - 1) == '*')
			return new StarLanguageObject(str);
		else if (str.charAt(0) == '(' && str.charAt(str.length() - 1) == ')')
			return new OptionLanguageObject(str);
		else
			return new StringLanguageObject(str);
	}

	/**
	 * Splits <code>str</code> by <code>divider</code>, retaining the
	 * parenthesis structure of the contained elements
	 * 
	 * @param str
	 *            the string to split
	 * @param divider
	 *            the character to split by
	 * @return the split string or <code>null</code> if parenthesis do not
	 *         match
	 */
	public static List<String> sensitiveSplit(String str, char divider) {
		int count, index, begin;
		List<String> parts;

		index = begin = 0;
		count = 0;
		parts = new ArrayList<String>();

		while (index < str.length()) {
			char character;

			character = str.charAt(index);
			if (character == '(')
				count++;
			if (character == ')')
				count--;
			if (character == divider && count == 0) {
				if (begin < index)
					parts.add(str.substring(begin, index));
				begin = index + 1;
			}

			index++;
		}

		if (begin < index)
			parts.add(str.substring(begin, index));

		if (count != 0)
			return null;
		else
			return parts;
	}

	/**
	 * Combination of <code>String.indexOf</code>,
	 * <code>String.matches</code> and <code>String.replace</code>. Runs
	 * through <code>string</code> and matches at every position for
	 * <code>reg</code>. If found, the occurrence is replaced by a substring
	 * from the offset <code>newStart</code> to <code>newEnd</code>.
	 * 
	 * @param string
	 *            the string to search in
	 * @param reg
	 *            the regular expression to match with
	 * @param origLen
	 *            the fixed length of all possible matches to <code>reg</code>
	 * @param newStart
	 *            the replacement substring start index
	 * @param newEnd
	 *            the replacement substring end index
	 * @return the string with all replacements
	 */
	public static String findAndReplaceRegEx(String string, String reg,
			int origLen, int newStart, int newEnd) {
		int index;

		for (index = 0; index + origLen <= string.length(); index++) {
			String part;

			part = string.substring(index, index + origLen);
			if (!part.matches(reg))
				continue;

			string = string.replace(part, string.substring(index + newStart,
					index + newEnd));
			index--;
		}

		return string;
	}

	/**
	 * Splits up the given string into parts, so that each part is again a
	 * formal language, with special attention parenthesis pairs and star
	 * operations.
	 * 
	 * @param str
	 *            the string to split
	 * @return a <code>List<code> of <code>String</code>s
	 */
	public static List<String> splitLanguageString(String str) {
		int count, index, begin;

		List<String> strParts;

		index = begin = 0;
		count = 0;
		strParts = new ArrayList<String>();

		while (index < str.length()) {
			char character;

			character = str.charAt(index);

			if (character == '(') {
				if (count == 0 && begin < index) {
					strParts.add(str.substring(begin, index));
					begin = index;
				}

				count++;
			}

			if (character == ')') {
				count--;

				if (count == 0 && begin < index) {
					strParts.add(str.substring(begin, index + 1));
					begin = index + 1;
				}
			}

			if (character == '*' && count == 0) {
				int max;

				if (begin < index)
					strParts.add(str.substring(begin, index));

				max = strParts.size() - 1;
				if (max >= 0)
					strParts.set(max, strParts.get(max) + "*");
				else
					throw new IllegalArgumentException();

				begin = index + 1;
			}

			index++;
		}

		if (begin < index)
			strParts.add(str.substring(begin, index));

		return strParts;
	}

	public String[] getSpecialCharacter() {
		String[] out = { "\u03b5" };

		return out;
	}
}