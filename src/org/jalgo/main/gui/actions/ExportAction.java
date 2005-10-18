/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and
 * platform independant. j-Algo is developed with the help of Dresden
 * University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * Created on May 22, 2004
 */

package org.jalgo.main.gui.actions;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.ScaledGraphics;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;

/**
 * @author Cornelius Hald
 */
public class ExportAction extends Action {

	private IFigure figure;

	public ExportAction(IFigure figure) {
		this.figure = figure;

		setText(Messages.getString("main", "ui.Export")); //$NON-NLS-1$
		setToolTipText(Messages.getString("main", "ui.Export_image")); //$NON-NLS-1$
		setImageDescriptor(ImageDescriptor.createFromURL(
			getClass().getResource("/main_pix/export.gif")));
	}

	public void run() {
		// Create Image with double size to enhance quality
		if (figure.getPreferredSize().width * figure.getPreferredSize().height == 0) {
			JAlgoGUIConnector.getInstance().showErrorMessage(
				"Export failure: Nothing to export"); //$NON-NLS-1$
			return;
		}
		Image img = new Image(
			null,
			figure.getPreferredSize().width,
			figure.getPreferredSize().height);
		GC gc = new GC(img);
		SWTGraphics swtg = new SWTGraphics(gc);
		ScaledGraphics scaled = new ScaledGraphics(swtg);

		// Enter the scale factor
		scaled.scale(1);

		// Paint the figure on the scaledGraphics
		figure.paint(scaled);

		// Sets ImageData
		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] { img.getImageData()};

		// Save to file
		FileDialog filer = new FileDialog(new Shell(), SWT.SAVE);
		filer.setText(Messages.getString("main", "ui.Export_file")); //$NON-NLS-1$
		filer.setFilterExtensions(new String[] { "*.bmp" }); //$NON-NLS-1$
		filer.setFilterNames(new String[] {
			Messages.getString("main", "ExportAction.Bitmap_(*.bmp)_6") }); //$NON-NLS-1$
		String filename = filer.open();

		if (filename != null) {
			loader.save(filename, SWT.IMAGE_BMP);
		}
	}
}