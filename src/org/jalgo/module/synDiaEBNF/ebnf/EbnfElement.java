/*
 * Created on 29.04.2004
 */
 
package org.jalgo.module.synDiaEBNF.ebnf;

import java.io.Serializable;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

/**
 * Abstract to have a same type (name) for all composite members in an EBNF term.
 * Declaration of some colors which are used in conjunction with EBNF terms.
 * 
 * @author Babett Schaliz
 * @author Stephan Creutz
 */
public abstract class EbnfElement implements Serializable {
	protected final int ALTERNATIVE_COLOR = SWT.COLOR_BLUE;
	protected final int CONCATENATION_COLOR = SWT.COLOR_BLUE;
	protected final int OPTION_COLOR = SWT.COLOR_BLUE;
	protected final int PRECEDENCE_COLOR = SWT.COLOR_BLUE;
	protected final int REPETITION_COLOR = SWT.COLOR_BLUE;

	public abstract int render(Shell shell, List styleList, int pos);
}
