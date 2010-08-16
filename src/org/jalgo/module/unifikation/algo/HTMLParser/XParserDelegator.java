package org.jalgo.module.unifikation.algo.HTMLParser;

import java.lang.reflect.Field;

import javax.swing.text.html.parser.DTD;
import javax.swing.text.html.parser.ParserDelegator;

public class XParserDelegator extends ParserDelegator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8977568244066045484L;

	public XParserDelegator() {
		try {
			Field f = javax.swing.text.html.parser.ParserDelegator.class.getDeclaredField("dtd");
	        f.setAccessible(true);
	        DTD dtd=(DTD)f.get(null);
	        javax.swing.text.html.parser.Element div=dtd.getElement("div");
	        dtd.defineElement("ora", div.getType(), true, true,div.getContent(),null, null,div.getAttributes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*@Override
	public void parse(Reader r, ParserCallback cb, boolean ignoreCharSet)
			throws IOException {
		new DocumentParser(getDTD()).parse(r, cb, ignoreCharSet);
	}

	public DTD getDTD() {
		DTD dtd=null;
		try {
			dtd = DTD.getDTD("html32");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Element span = dtd.getElement("span");
		dtd.defineElement("ora", span.getType(), true, true, span.getContent(), null, null, span.getAttributes());
		return dtd;/*
		if ( dtd == null ) {
			String nm = "html32.bdtd";
			DTD _dtd = new XDTD(nm);
			_dtd = createDTD(_dtd, nm);
			dtd = _dtd;
		}
		return dtd;
	}*/

}
