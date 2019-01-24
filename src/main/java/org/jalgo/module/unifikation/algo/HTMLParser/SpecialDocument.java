package org.jalgo.module.unifikation.algo.HTMLParser;

import java.net.URL;

import javax.swing.text.Document;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

public class SpecialDocument extends HTMLDocument {
	/**
	 * 
	 */
	private static final long serialVersionUID = -154851356706035436L;
	
	public static final HTML.Tag FIXTAG = new HTML.UnknownTag("fixtag");

    public SpecialDocument(StyleSheet styles) {
        super(styles);
    }

    public HTMLEditorKit.ParserCallback getReader(int pos) {
        Object desc = getProperty(Document.StreamDescriptionProperty);
        if (desc instanceof URL) {
            setBase((URL)desc);
        }
        return new MyHTMLReader(pos);
    }

    class MyHTMLReader extends HTMLDocument.HTMLReader {
        public MyHTMLReader(int offset) {
            super(offset);
            registerTag(FIXTAG, new SpecialAction());
        }
    }

}
