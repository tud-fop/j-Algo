package org.jalgo.module.am1simulator.parser;

import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.Lexer;

/**
 *
 * @author Max Leuthäuser
 */
public class AM1SyntaxKit extends DefaultSyntaxKit {
	private static final long serialVersionUID = 1L;

	public AM1SyntaxKit() {
        super(new AM1Lexer());
    }
	
	  AM1SyntaxKit(Lexer lexer) {
        super(lexer);
    }
}
