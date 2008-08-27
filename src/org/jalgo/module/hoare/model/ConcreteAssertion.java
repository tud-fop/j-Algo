package org.jalgo.module.hoare.model;

/**
 * Represents a concrete term as the leaf of the assertion tree.
 * Assertion strings my not contain any Not And or Or symbols.
 * 
 * @author Thomas
 *
 */
class ConcreteAssertion extends AbstractAssertion {

	/**
	 * the serial Id of this Object
	 */
	private static final long serialVersionUID = -6773213821777206709L;
	/**
	 * holds the concrete assertion as a string in c0
	 */
	private String term;
	/**
	 * holds the converted string with included Unicode symbols
	 */
	private String text;
	/**
	 * holds the index of the PI 
	 */
	private int index;
	
	/**
	 * creates an ConcreteAssertion with an C0 Assertion string.
	 * Every ConcreteAssertion gets his own Pi-identifier.
	 * 
	 * @param assertion
	 *        the concrete assertion string
	 * @param index
	 *        the Number behind the Pi
	 */
	public ConcreteAssertion(String assertion,int index){		
		this.index=index;
		this.term=assertion;
		this.text=new String(assertion);
	
		// some symbols will be replaced by Unicode symbols
        text = text.replaceAll("==", "=");
        text = text.replaceAll("<=", "\u2264");
        text = text.replaceAll(">=", "\u2265");
        text = text.replaceAll("<", "\u003C");
        text = text.replaceAll(">", "\u003E");
        text = text.replaceAll("sqrt", "\221A");
        text = text.replaceAll("Sum", "\u2211");
        text = text.replaceAll("Product", "\220F");
	}
	
	/**
	 * @see org.jalgo.module.hoare.model.AbstractAssertion#includesVariable()
	 */
	@Override
	public boolean verifiable() {
		return true;
	}

	/**
	 * @see org.jalgo.module.hoare.model.AbstractAssertion#toString()
	 */
	@Override
	public String toString() {
		return "("+term+")";
	}

	/**
	 * @see org.jalgo.module.hoare.model.AbstractAssertion#toText(boolean)
	 */
	@Override
	public String toText(boolean full) {
		return (full ? text : "\u03C0" + index);
	}
	
}
