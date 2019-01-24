package org.jalgo.module.app.core.dataType.formalLanguage;

import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

class OptionLanguageObject extends FormalLanguageObject {
	private static final long serialVersionUID = 36142298841024703L;
	SortedSet<FormalLanguageObject> options;

	public OptionLanguageObject() {
		options = new TreeSet<FormalLanguageObject>();
	}

	public OptionLanguageObject(String str) {
		this();
		setFromString(str);
	}

	public OptionLanguageObject(OptionLanguageObject other) {
		this();

		for (FormalLanguageObject obj : other.options)
			options.add(obj.clone());
	}

	public OptionLanguageObject clone() {
		return new OptionLanguageObject(this.toString());
	}

	public boolean equals(Object other) {
		OptionLanguageObject ot;

		if (!(other instanceof OptionLanguageObject))
			return false;

		ot = (OptionLanguageObject) other;
		return options.equals(ot.options);
	}

	public SortedSet<FormalLanguageObject> getOptions() {
		return options;
	}

	public void setOptions(SortedSet<FormalLanguageObject> options) {
		this.options = options;
	}

	public String toString() {
		String out;

		out = "(";

		for (FormalLanguageObject obj : options)
			out += obj + "+";

		if (out.length() > 1)
			out = out.substring(0, out.length() - 1);
		out += ")";

		return out;
	}

	public void setFromString(String str) {
		List<String> strings;

		if (str.charAt(0) != '(' || str.charAt(str.length() - 1) != ')')
			throw new IllegalArgumentException();

		str = str.substring(1, str.length() - 1);

		strings = FormalLanguage.sensitiveSplit(str, '+');
		options.clear();

		for (String aStr : strings)
			options.add(FormalLanguage.languageFromString(aStr));
	}

	public FormalLanguageObject simplify() {
		SortedSet<FormalLanguageObject> newOptions;
		boolean change;

		do {
			change = false;

			/*
			 * Simplify all options separately
			 */
			newOptions = new TreeSet<FormalLanguageObject>();
			
			for (FormalLanguageObject obj : options) {
				FormalLanguageObject ret;

				ret = obj.simplify();
				if (ret != null) {
					if (ret instanceof OptionLanguageObject)
						newOptions.addAll(((OptionLanguageObject)ret).getOptions());
					else
						newOptions.add(ret);
				}
			}
			
			change = !(options.equals(newOptions));
			options = newOptions;
			
			/*
			 * Simplify combinations
			 */
			Iterator<FormalLanguageObject> it;
			
			it = options.iterator();
			
			while (it.hasNext()) {
				FormalLanguageObject curr, child;

				curr = it.next();

				/*
				 * Match for pattern (a(x)*xb, aEb) and (a(x)*b, aEb)
				 * Reduce to (a(x)*b)
				 */
				if (curr instanceof ConcatenationLanguageObject) {
					FormalLanguageObject pre, post, conc;
					FormalLanguageObject p1, p2;
					ConcatenationLanguageObject obj;
					String innerString, outerString;
					int i;
					
					obj = (ConcatenationLanguageObject)curr;
					pre = post = null;
					p1 = p2 = child = null;
					innerString = outerString = null;
					
					for (i=0; i<obj.getParts().size()-1; i++) {
						
						p1 = obj.getParts().get(i);
						p2 = obj.getParts().get(i+1);
						
						if (!(p1 instanceof StarLanguageObject) || !(p2 instanceof StringLanguageObject))
							continue;
						
						innerString = null;
						
						// Case where the * contains a single char string
						child = ((StarLanguageObject)p1).getChild();
						if (child instanceof StringLanguageObject)
							innerString = ((StringLanguageObject)child).getString();

						// Case where the * contains a multiple char string
						if ((child instanceof OptionLanguageObject)
								&& ((OptionLanguageObject)child).getOptions().size() == 1
								&& (((OptionLanguageObject)child).getOptions().toArray()[0] instanceof StringLanguageObject))
							innerString = ((StringLanguageObject)((OptionLanguageObject)child).getOptions().toArray()[0]).getString();
						
						if (innerString == null)
							continue;
						
						// Look for match
						outerString = ((StringLanguageObject)p2).getString();
						
						if (!outerString.startsWith(innerString))
							outerString = null;
						
						break;
					}
					
					// No match found
					if (i == obj.getParts().size()-1)
						continue;
					
					if (i > 0)
						pre = new ConcatenationLanguageObject(obj.getParts().subList(0, i));
					if (obj.getParts().size() > i+2)
						post = new ConcatenationLanguageObject(obj.getParts().subList(i+2, obj.getParts().size()));
					if (outerString == null && obj.getParts().size() > i+1)
						post = new ConcatenationLanguageObject(obj.getParts().subList(i+1, obj.getParts().size()));
					
					if (outerString != null && !outerString.equals(innerString)) {
						StringLanguageObject remainder;
						
						remainder = new StringLanguageObject(outerString.substring(innerString.length()));
						
						if (post != null)
							((ConcatenationLanguageObject)post).getParts().add(0, remainder);
						else
							post = remainder;
					}

					if (pre == null && post == null)
						pre = new StringLanguageObject("\u03b5");
					
					if (pre == null)
						conc = post;
					else if (post == null)
						conc = pre;
					else {
						conc = new ConcatenationLanguageObject();
						((ConcatenationLanguageObject)conc).getParts().add(pre);
						((ConcatenationLanguageObject)conc).getParts().add(post);
						conc.simplify();
					}
					
					if (options.contains(conc)) {
						options.remove(conc);
						if (outerString != null)
							((StringLanguageObject)p2).setString(outerString.substring(innerString.length()));
						
						it = options.iterator();
						change = true;
					}
				}
				

				/*
				 * Match for pattern (..., (x)*[, x][, E], ...)
				 * Reduce to (..., (x)*, ...)
				 */
				if (curr instanceof StarLanguageObject
						&& (
								((((StarLanguageObject)curr).getChild() instanceof StringLanguageObject)
								&& (child = ((StarLanguageObject)curr).getChild()) != null)
							||
								((((StarLanguageObject)curr).getChild() instanceof OptionLanguageObject)
								&& (child = ((StarLanguageObject)curr).getChild()) != null
								&& ((OptionLanguageObject)child).getOptions().size() == 1
								&& (((OptionLanguageObject)child).getOptions().toArray()[0] instanceof StringLanguageObject)
								&& (child = (FormalLanguageObject)((OptionLanguageObject)child).getOptions().toArray()[0]) != null
								)
							)) {
					if (options.contains(child)) {
						options.remove(child);
						change = true;
						it = options.iterator();
					}
					if (options.contains(new StringLanguageObject("\u03b5"))) {
						options.remove(new StringLanguageObject("\u03b5"));
						change = true;
						it = options.iterator();
					}
				}
			}
		} while (change);

		if (options.size() == 0)
			return null;

		return this;
	}
}