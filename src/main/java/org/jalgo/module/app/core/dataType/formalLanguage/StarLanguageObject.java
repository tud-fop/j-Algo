package org.jalgo.module.app.core.dataType.formalLanguage;

import java.util.SortedSet;

class StarLanguageObject extends FormalLanguageObject {

	private static final long serialVersionUID = -3034912517665116911L;
	FormalLanguageObject child;

	public StarLanguageObject() {
		child = null;
	}

	public StarLanguageObject(FormalLanguageObject child) {
		this.child = child;
	}

	public StarLanguageObject(String str) {
		setFromString(str);
	}

	public StarLanguageObject clone() {
		return new StarLanguageObject(this.toString());
	}

	public boolean equals(Object other) {
		StarLanguageObject ot;

		if (!(other instanceof StarLanguageObject))
			return false;

		ot = (StarLanguageObject) other;
		return child.equals(ot.child);
	}

	public FormalLanguageObject getChild() {
		return child;
	}

	public void setChild(FormalLanguageObject child) {
		this.child = child;
	}

	public String toString() {
		return child + "*";
	}

	public void setFromString(String str) {
		if (str.charAt(str.length() - 1) != '*')
			throw new IllegalArgumentException();

		child = FormalLanguage.languageFromString(str.substring(0,
				str.length() - 1));
	}

	public FormalLanguageObject simplify() {
		FormalLanguageObject newChild;
		boolean change;

		if (child == null)
			return new StringLanguageObject("\u03b5");

		change = false;

		do {
			/*
			 * Remember that something changed
			 */
			change = false;

			/*
			 * Simplify child
			 */
			newChild = child.simplify();
			change = !newChild.equals(child);
			child = newChild;
			
			if (child == null)
				return new StringLanguageObject("\u03b5");

			/*
			 * Check child
			 */
			if (child instanceof StarLanguageObject)
				child = ((StarLanguageObject) child).child;

			if (child instanceof OptionLanguageObject) {
				SortedSet<FormalLanguageObject> options;

				options = ((OptionLanguageObject) child).getOptions();

				if (options.size() == 1) {
					FormalLanguageObject baby;

					baby = options.first();

					if ((baby instanceof StringLanguageObject)
							&& ((StringLanguageObject) baby).getString()
									.length() == 1) {
						child = baby;
						change = true;
						continue;
					}
					if ((baby instanceof StarLanguageObject)) {
						child = ((StarLanguageObject) baby).getChild();
						change = true;
						continue;
					}
				}

				options.remove(new StringLanguageObject("\u03b5"));
			}

			if (child instanceof StringLanguageObject) {
				StringLanguageObject strObject;

				strObject = (StringLanguageObject) child;
				if (strObject.getString().equals("\u03b5"))
					return strObject;
			}

		} while (change);

		return this;
	}
}
