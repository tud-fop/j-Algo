package org.jalgo.tests.avl;

import java.awt.*;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * The class <code>Graphics2DRendererTest</code> shows, that it is possible to use the
 * Java2D API within the SWT framework. But, it also shows the stunning speed of the 
 * translation: An area of 400 x 400 p. takes approx. 2 seconds to be displayed. So this
 * technique is only recommended for static display contents.
 * 
 * @author Alexander Claus
 */
public class Graphics2DRendererTest {

	public static void main(String[] args) {
		final Graphics2DRenderer renderer = new Graphics2DRenderer();
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(400, 400);

		shell.open();
		shell.setText("Java2d to Draw2d");
		LightweightSystem lws = new LightweightSystem(shell);
		IFigure figure = new Figure() {
			protected void paintClientArea(org.eclipse.draw2d.Graphics graphics) {
				Dimension controlSize = getSize();
				renderer.prepareRendering(graphics);

				Graphics2D g2d = renderer.getGraphics2D();
				paintNode(g2d, 200, 210);
				paintNode(g2d, 10, 351);
				paintNode(g2d, 395, 40);
				paintNode(g2d, 100, -2);

				renderer.render(graphics);
			}
		};
		lws.setContents(figure);
		while (!shell.isDisposed()) if (!display.readAndDispatch()) display.sleep();
		renderer.dispose();
	}

	private static void paintNode(Graphics2D g, int x, int y) {
		g.setColor(Color.black);
		g.fillOval(x-11, y-11, 22, 22);
		g.setColor(Color.orange);
		g.fillOval(x-10, y-10, 20, 20);
	}
}