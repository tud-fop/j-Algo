/*
 * Created on Apr 23, 2004
 */
 
package org.jalgo.main.gui.actions;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Shell;
import org.jalgo.main.util.PrintDialogNext;
import org.jalgo.main.util.PrintScaledFigureOperation;

/**
 * @author Cornelius Hald
 * @author Anne Kersten
 */
public class PrintViewAction extends Action {

	private IFigure figure;

	public PrintViewAction(IFigure figure) {
		this.figure = figure;
		setText(Messages.getString("PrintViewAction.Print_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("PrintViewAction.Print_this_view_2")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/print.gif")); //$NON-NLS-1$
	}

	public void run() {
		Shell shell =new Shell();

		// New Print-Dialog
		PrintDialog printDialog = new PrintDialog(shell);
		printDialog.setText(Messages.getString("PrintViewAction.Choose_printer_4")); //$NON-NLS-1$
		PrinterData printerData = printDialog.open();
		if(printerData==null) return;
		
		
		
		Printer printer = new Printer(printerData);
		PrintScaledFigureOperation operation = new PrintScaledFigureOperation(printer,figure);
		operation.setPrintMargin(new Insets(0, 0, 0, 0));
		PrintDialogNext dialog=new PrintDialogNext(shell);
		switch(dialog.open())
		{
			case PrintScaledFigureOperation.TILE: 
				operation.run("JAlgo"); //$NON-NLS-1$
				break;
			case PrintScaledFigureOperation.FIT_PAGE: 
				operation.setPrintMode(PrintScaledFigureOperation.FIT_PAGE);
				operation.run("JAlgo"); //$NON-NLS-1$
				break;
			case PrintScaledFigureOperation.FIT_WIDTH: 
				operation.setPrintMode(PrintScaledFigureOperation.FIT_WIDTH);
				operation.run("JAlgo"); //$NON-NLS-1$
				break;
			case PrintScaledFigureOperation.FIT_HEIGHT:
				operation.setPrintMode(PrintScaledFigureOperation.FIT_HEIGHT);
				operation.run("JAlgo"); //$NON-NLS-1$
				break;
			case PrintScaledFigureOperation.USE_LAYOUT:
				operation.setPrintMode(PrintScaledFigureOperation.USE_LAYOUT);
				operation.run("JAlgo"); //$NON-NLS-1$
				break;
			default: ;
		}

		printer.dispose();
	}

}
