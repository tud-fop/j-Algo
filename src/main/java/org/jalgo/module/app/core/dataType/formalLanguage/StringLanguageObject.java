package org.jalgo.module.app.core.dataType.formalLanguage;

class StringLanguageObject extends FormalLanguageObject {

	private static final long serialVersionUID = -7090260348441367569L;
	private static final String epsilon = "\u03B5";
	private String str;

	public StringLanguageObject() {
		str = "";
	}

	public StringLanguageObject(String str) {
		setFromString(str);
	}

	/**
	 * @see FormalLanguageObject.toString
	 */
	public String toString() {
		return getString();
	}

	/**
	 * @see FormalLanguageObject.setFromString
	 */
	public void setFromString(String str) {
		setString(str);
	}

	/**
	 * @see FormalLanguageObject.clone
	 */
	public StringLanguageObject clone() {
		return new StringLanguageObject(str);
	}

	/**
	 * @see FormalLanguageObject.equals
	 */
	public boolean equals(Object other) {
		StringLanguageObject ot;

		if (!(other instanceof StringLanguageObject))
			return false;

		ot = (StringLanguageObject) other;
		return str.equals(ot.str);
	}

	public String getString() {
		return str;
	}

	public void setString(String str) {
		// "E" will be replaced by "\u03B5" in simplify()
		if (!str.matches("[\u03B5a-zE]*"))
			throw new IllegalArgumentException();

		this.str = new String(str);
	}

	/**
	 * Simplifies object by removing whitespace and epsilon occurrences in
	 * concatenation with literals.
	 * 
	 * @see FormalLanguageObject.simplify
	 */
	public FormalLanguageObject simplify() {
		String value;

		value = str;
		value = value.replaceAll(" ", "");
		value = value.replaceAll("E", "\u03B5");

		String before;
		do {
			before = value;
			//ET: "E" replaced by "\u03B5"
			value = FormalLanguage
					.findAndReplaceRegEx(value, "\u03B5[\u03B5a-z]", 2, 1, 2);
			//ET: "E" replaced by "\u03B5"
			value = FormalLanguage
					.findAndReplaceRegEx(value, "[\u03B5a-z]\u03B5", 2, 0, 1);

		} while (!value.equals(before));

		if (value.length() == 0)
			return null;
		
		if (!value.equals(str))
			return new StringLanguageObject(value);
		else
			return this;
	}
}