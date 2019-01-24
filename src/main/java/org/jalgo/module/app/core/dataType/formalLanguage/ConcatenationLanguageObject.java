package org.jalgo.module.app.core.dataType.formalLanguage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class ConcatenationLanguageObject extends FormalLanguageObject {

	private static final long serialVersionUID = 544559843466005098L;
	private List<FormalLanguageObject> parts;

	public ConcatenationLanguageObject() {
		parts = new ArrayList<FormalLanguageObject>();
	}

	public ConcatenationLanguageObject(String str) {
		this();
		setFromString(str);
	}

	public ConcatenationLanguageObject(List<String> strParts) {
		this();
		setFromParts(strParts);
	}

	public ConcatenationLanguageObject(Collection<FormalLanguageObject> parts) {
		this();
		this.parts.addAll(parts);
	}

	public ConcatenationLanguageObject(ConcatenationLanguageObject other) {
		this();

		for (FormalLanguageObject obj : other.parts)
			parts.add(obj.clone());
	}

	public ConcatenationLanguageObject clone() {
		return new ConcatenationLanguageObject(this.toString());
	}

	public boolean equals(Object other) {
		ConcatenationLanguageObject ot;

		if (!(other instanceof ConcatenationLanguageObject))
			return false;

		ot = (ConcatenationLanguageObject) other;
		return parts.equals(ot.parts);
	}

	public String toString() {
		String out;

		out = "";
		for (FormalLanguageObject obj : parts)
			out += obj;

		return out;
	}

	public void setFromString(String str) {
		List<String> strParts;

		strParts = FormalLanguage.splitLanguageString(str);
		setFromParts(strParts);
	}

	public void setFromParts(List<String> strParts) {
		parts.clear();

		for (String strPart : strParts)
			parts.add(FormalLanguage.languageFromString(strPart));
	}

	public List<FormalLanguageObject> getParts() {
		return parts;
	}

	public void setParts(List<FormalLanguageObject> parts) {
		this.parts = parts;
	}
	
	private enum StarAlignment {
		STAR_ALIGNMENT_LEFT,
		STAR_ALIGNMENT_RIGHT
	}

	public FormalLanguageObject simplify() {
		List<FormalLanguageObject> newParts;
		StarAlignment alignment;
		boolean change;

		alignment = StarAlignment.STAR_ALIGNMENT_RIGHT;
		
		do {
			change = false;

			/*
			 * Simplify all parts separately
			 */
			newParts = new ArrayList<FormalLanguageObject>();

			for (FormalLanguageObject obj : parts) {
				FormalLanguageObject ret;

				ret = obj.simplify();
				if (ret != null)
					newParts.add(ret);
			}

			change = !(parts.equals(newParts));
			parts = newParts;

			/*
			 * Simplify combinations
			 */
			for (int i = 1; i < parts.size(); i++) {
				FormalLanguageObject prev, curr, child, other;

				prev = parts.get(i - 1);
				curr = parts.get(i);

				/*
				 * ALIGNMENT_LEFT:
				 * Match for pattern ...a(...a)*...
				 * Reduce to ...(a...)*a...
				 */
				if (alignment == StarAlignment.STAR_ALIGNMENT_LEFT
						&& (prev instanceof StringLanguageObject)
						&& (curr instanceof StarLanguageObject)
						
						&& (other = prev) != null
						&& ((StringLanguageObject) other).getString().length() > 0
						
						&& (child = ((StarLanguageObject) curr).child) != null
						&& (child instanceof OptionLanguageObject)
						&& ((OptionLanguageObject)child).getOptions().size() == 1
						&& (((OptionLanguageObject)child).getOptions().toArray()[0] instanceof StringLanguageObject)
					) {
					StringLanguageObject otherObj, childObj;
					String otherString, childString;
					char c;
					
					otherObj = (StringLanguageObject)other;
					childObj = (StringLanguageObject)((OptionLanguageObject)child).getOptions().toArray()[0];
					
					otherString = otherObj.getString();
					childString = childObj.getString();
					c = otherString.charAt(otherString.length() - 1);
					
					if (c == childString.charAt(childString.length() - 1)) {
						FormalLanguageObject next;
						
						/*
						 * Finally matches our pattern
						 */ 
						
						otherString = otherString.substring(0, otherString.length() - 1);
						childString = c + childString.substring(0, childString.length() - 1);
						
						otherObj.setString(otherString);
						childObj.setString(childString);
						
						if (i < parts.size() - 1)
							next = parts.get(i+1);
						else
							next = null;
							
						if (next != null && next instanceof StringLanguageObject) {
							String nextString;
							
							nextString = ((StringLanguageObject)next).getString();
							nextString = c + nextString;
							((StringLanguageObject)next).setString(nextString);
						}
						else {
							next = new StringLanguageObject(c + "");
							parts.add(i+1, next);
						}
						
						change = true;
						continue;
					}
				}
				
				/*
				 * ALIGNMENT_RIGHT:
				 * Match for pattern ...(a...)*a...
				 * Reduce to ...a(...a)*...
				 */
				
				if (alignment == StarAlignment.STAR_ALIGNMENT_RIGHT
							&& (curr instanceof StringLanguageObject)
							&& (prev instanceof StarLanguageObject)
							
							&& (other = curr) != null
							&& ((StringLanguageObject) other).getString().length() > 0
							
							&& (child = ((StarLanguageObject) prev).child) != null
							&& (child instanceof OptionLanguageObject)
							&& ((OptionLanguageObject)child).getOptions().size() == 1
							&& (((OptionLanguageObject)child).getOptions().toArray()[0] instanceof StringLanguageObject)
					) {
					StringLanguageObject otherObj, childObj;
					String otherString, childString;
					char c;
					
					otherObj = (StringLanguageObject)other;
					childObj = (StringLanguageObject)((OptionLanguageObject)child).getOptions().toArray()[0];
					
					otherString = otherObj.getString();
					childString = childObj.getString();
					c = otherString.charAt(0);
					
					if (c == childString.charAt(0)) {
						FormalLanguageObject prePrev;
						
						/*
						 * Finally matches our pattern
						 */ 
						
						otherString = otherString.substring(1, otherString.length());
						childString = childString.substring(1, childString.length()) + c;
						
						otherObj.setString(otherString);
						childObj.setString(childString);
						
						if (i > 1)
							prePrev = parts.get(i-2);
						else
							prePrev = null;
							
						if (prePrev != null && prePrev instanceof StringLanguageObject) {
							String nextString;
							
							nextString = ((StringLanguageObject)prePrev).getString();
							nextString = nextString + c;
							((StringLanguageObject)prePrev).setString(nextString);
						}
						else {
							prePrev = new StringLanguageObject("" + c);
							parts.add(i-1, prePrev);
						}
						
						change = true;
						continue;
					}
				}
				
				/*
				 * Match for pattern ...(x+E)(x)*... and ...(x)*(x+E)...
				 * Reduce to ...(x)*...
				 */
				if ((	// (z)y*
						((prev instanceof OptionLanguageObject)
						&& (curr instanceof StarLanguageObject)
						&& (other = prev) != null
						&& (child = ((StarLanguageObject) curr).child) != null)
					||	// y*(z)
						((curr instanceof OptionLanguageObject)
							&& (prev instanceof StarLanguageObject)
							&& (other = curr) != null
							&& (child = ((StarLanguageObject) prev).child) != null))
						// y == (x)
					&& ((child instanceof OptionLanguageObject)
						&& ((OptionLanguageObject)child).getOptions().size() == 1
						&& (((OptionLanguageObject)child).getOptions().toArray()[0] instanceof StringLanguageObject))) {
					StringLanguageObject childStringObject;
					OptionLanguageObject optionObject;
					
					childStringObject = (StringLanguageObject) ((OptionLanguageObject)child).getOptions().toArray()[0];
					optionObject = (OptionLanguageObject)other;
					
					if (optionObject.getOptions().size() == 2
							&& optionObject.getOptions().contains(childStringObject)
							&& optionObject.getOptions().contains(new StringLanguageObject("\uu03b5"))) {
						parts.remove(optionObject);
						
						i--;
						change = true;
						continue;
					}
				}
			}

			/*
			 * Remove special parts
			 */
			if (parts.size() > 1)
				parts.remove(new StringLanguageObject("\u03b5"));

			/*
			 * Check parts
			 */

			/*
			 * If nothing changed switch the alignment
			 */
			if (!change && alignment != StarAlignment.STAR_ALIGNMENT_LEFT) {
				alignment = StarAlignment.STAR_ALIGNMENT_LEFT;
				change = true;
			}
		} while (change);

		if (parts.size() == 0)
			return null;
		if (parts.size() == 1)
			return parts.get(0);

		return this;
	}
}