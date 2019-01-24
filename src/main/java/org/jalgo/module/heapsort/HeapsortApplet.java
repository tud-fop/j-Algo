/**
 * 
 */
package org.jalgo.module.heapsort;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JApplet;
import javax.swing.JFrame;

import org.jalgo.module.heapsort.anim.TimeEntity;
import org.jalgo.module.heapsort.anim.TimeRoot;
import org.jalgo.module.heapsort.model.Model;
import org.jalgo.module.heapsort.renderer.CanvasEntity;
import org.jalgo.module.heapsort.renderer.CanvasEntityFactory;
import org.jalgo.module.heapsort.renderer.Renderer;
import org.jalgo.module.heapsort.vis.Controller;
import org.jalgo.module.heapsort.vis.Visualisation;

/**
 * @author mbue
 *
 */
public final class HeapsortApplet extends JApplet {

	private Renderer renderer;
	private CanvasEntity root;
	private TimeEntity timeroot;
	private Controller ctrl;
	private Thread thread;

	/**
	 * 
	 */
	private static final long serialVersionUID = 5192815393184014419L;
	
	private static final int[] sequence = { 7,15,14,8,13,18,24,9,5,16,21 };

	@Override
	public void init() {
		Model model;
		Visualisation vis;
		CanvasEntityFactory f;
		
		// connect everything...
		model = new org.jalgo.module.heapsort.model.Heapsort();
		for (int i: sequence)
			((org.jalgo.module.heapsort.model.Heapsort)model).addNumber(i);
		renderer = new org.jalgo.module.heapsort.renderer.RenderJava2D();
		f = renderer.createFactory();
		root = f.createRoot();
		vis = new org.jalgo.module.heapsort.vis.Heapsort(root, f);
		timeroot = new TimeRoot();
		ctrl = new Controller(model, vis, timeroot);
		setLayout(new BorderLayout());
		renderer.init(this);
		addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				synchronized (root) {
					if (ctrl.isBackPossible() && !ctrl.isStepPossible())
						ctrl.reset();
					else {
						if (ctrl.isMacroStepPossible()) {
							timeroot.setScale(0.7);
							ctrl.macroStep(-1);
						}
						else
							ctrl.suspend();
					}
				}
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}
			
		});
	}
	
	@Override
	public void start() {
		thread = new Thread(new UpdateThread());
		thread.start();		
	}
	
	@Override
	public synchronized void stop() {
		thread = null;
	}
	
    public static void main(String argv[]) {
        final HeapsortApplet app = new HeapsortApplet();
        app.init();
        JFrame f = new JFrame("Heapsort Demo -- click in the window to start/pause/reset");
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
            public void windowDeiconified(WindowEvent e) { app.start(); }
            public void windowIconified(WindowEvent e) { app.stop(); }
        });
        f.getContentPane().add("Center", app);
        f.pack();
        f.setSize(new Dimension(790,495));
        f.setVisible(true);
        app.start();
    }
	
	
	private class UpdateThread implements Runnable {
		
		public void run() {
			Thread me = Thread.currentThread();
			while (thread == me) {
				synchronized(root) {
					// animate...
					timeroot.update(0.000000001d*System.nanoTime());
					// display...
					// (should do nothing if nothing happened)
					Rectangle r;
					do {
						r = renderer.getVisible();
						if (!renderer.validate())
							r = r.intersection(root.computeDirtyRegion());
						renderer.renderVisible(root, r);
					} while (!renderer.show(r));
					root.clearDirtyRegion();
				}
				// breathe...
				try {
					Thread.sleep(20);
				}
				catch (InterruptedException e) {
					break;
				}
			}
			thread = null;
		}
		
	}

}
