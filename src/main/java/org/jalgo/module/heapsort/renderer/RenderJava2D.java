/*
 * j-Algo - a visualization tool for algorithm runs, especially useful for
 * students and lecturers of computer science. j-Algo is written in Java and
 * thus platform independent. Development is supported by Technische Universität
 * Dresden.
 *
 * Copyright (C) 2004-2008 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jalgo.module.heapsort.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.awt.image.VolatileImage;

import javax.swing.JPanel;

/**
 * <p>Java2D renderer. There are some restrictions, mainly: Transparency is faked by
 * fading the color towards the background color. This is totally sufficient for
 * our purposes and much more efficient.</p>
 * 
 * <p>Currently, it uses a lightweight component for the output meaning that your nice
 * little popup menus may also be lightweight.</p>
 * 
 * @author mbue
 */
public final class RenderJava2D implements Renderer, CanvasEntityFactory, ComponentListener {
	
	// definition of RenderCanvas to be found at the end of this class
	private RenderCanvas c;
	private VolatileImage backbuffer;
	private RootJava2D root;
	
	// Renderer methods
	
	public void init(Container cc) {
		c = new RenderCanvas();
		c.addComponentListener(this);
		cc.add(c);
		backbuffer = null;
		//backbuffer = c.createVolatileImage(c.getWidth(), c.getHeight());
	}
	
	public void dispose() {
		if (backbuffer != null) {
			backbuffer.flush();
			backbuffer = null;
		}
		c.getParent().remove(c);
		c = null;
	}

	public CanvasEntityFactory createFactory() {
		return this;
	}
	
	public Rectangle getVisible() {
		return new Rectangle(0, 0, c.getWidth(), c.getHeight());
	}
	
	public boolean validate() {
		if (backbuffer == null) {
			// component was not displayable last time we tried
			// try again
			try {
				backbuffer = c.createVolatileImage(c.getWidth(), c.getHeight());
			}
			catch (IllegalArgumentException e) {
				; // XXX do nothing
			}
			return true;
		}
		else {
			if (c.getWidth() != backbuffer.getWidth() || c.getHeight() != backbuffer.getHeight()) {
				backbuffer.flush();
				backbuffer = null;
				backbuffer = c.createVolatileImage(c.getWidth(), c.getHeight());
				backbuffer.validate(c.getGraphicsConfiguration());
				return true;
			}
			else {
				int rc = backbuffer.validate(c.getGraphicsConfiguration());
				if (rc == VolatileImage.IMAGE_INCOMPATIBLE) {
					backbuffer.flush();
					backbuffer = null;
					backbuffer = c.createVolatileImage(c.getWidth(), c.getHeight());
					return true;
				}
				return rc != VolatileImage.IMAGE_OK;
			}
		}
	}
	
	public void renderVisible(CanvasEntity root, Rectangle r) {
		if (backbuffer == null)
			return; // BAIL OUT
		Graphics2D g = (Graphics2D)backbuffer.getGraphics();
		try {
			AffineTransform t = g.getTransform();
			g.setClip(r);
			root.foldVisible(new RenderVisitor(g), r, t);
			g.setTransform(t);
		}
		finally {			
			g.dispose();
		}
	}
	
	public boolean show(Rectangle r) {
		if (r == null)
			c.repaint();
		else
			c.repaint(r.x, r.y, r.width, r.height);
		return true;
	}
	
	// factory methods
	
	public CanvasEntity createRoot() {
		if (root == null)
			root = new RootJava2D();
        return root;
	}
	
	public Node createNode(Point pos, String label) {
		return new NodeJava2D(pos, label);
	}
	
	public Text createText(String text, int width, int height) {
		return new TextJava2D(text, width, height);
	}
	
	public MarkingRect createMarkingRect() {
		return new MarkingRectJava2D();
	}
	
	public SequenceElement createSequenceElement(Point pos, String label) {
		return new SequenceElementJava2D(pos, label);
	}
	
	public Edge createEdge(Point from, Point to) {
		return new EdgeJava2D(from, to);
	}
	
	// Last Action Renderer
	
	private class RenderVisitor implements CanvasEntityVisitor {
		private Graphics2D g = null;
		
		public RenderVisitor(Graphics2D graphics) {
			g = graphics;
			g.setFont(defaultfont);
			g.setRenderingHint(
            		RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
		}

		public void invoke(CanvasEntity e, AffineTransform trans, Rectangle clip) {
			if (e instanceof Renderable) {
				g.setTransform(trans);
				((Renderable)e).render(g);
			}
		}
		
	}
	
	// Canvas Entities
	
	private interface Renderable {
		void render(Graphics2D g);
	}

	private static final Font defaultfont = new Font("sans", Font.BOLD, 16);
	private static final Font regularfont = new Font("sans", 0, 14);
	private static final Stroke ndstroke = new BasicStroke(2); 
	private static final Stroke seqstroke = new BasicStroke(1); 
	
	private static class RootJava2D extends CanvasEntity implements Renderable {
		
		public void render(Graphics2D g) {
			Rectangle r = bounds;
			g.setColor(Color.WHITE);
			//int c = (int)((System.nanoTime()>>23)&0xff);
			//int c = children.size()*3;
			//g.setColor(new Color(c|c<<8|c<<16));
			g.fillRect(r.x, r.y, r.width, r.height);
		}

	}
	
	private static class NodeJava2D extends Node implements Renderable {
		public NodeJava2D(Point pos, String label) {
			super(pos, label);
		}
		
		public void render(Graphics2D g) {
			float f = getHighlight();
			float f1 = 1.0f-f;
			FontMetrics fm = g.getFontMetrics();
			int w = fm.stringWidth(getLabel());
			int h = fm.getAscent();
			g.setStroke(ndstroke);
			g.setColor(new Color(f1+f*(244.0f/255), f1+f*(184.0f/255), f1));
			g.fillOval(bounds.x, bounds.y, bounds.width-1, bounds.height-1);
			g.setColor(Color.BLUE);
			g.drawOval(bounds.x+1, bounds.y+1, bounds.width-3-1, bounds.height-3-1);
			g.setColor(Color.BLACK);
			//g.drawRect(bounds.x+(bounds.width-w)/2, bounds.y+(bounds.height-h)/2-2,
			//		w-1, h-1);
			g.drawString(getLabel(), bounds.x+(bounds.width-w)/2, bounds.y+(bounds.height+h)/2-2);
		}		
	}
	
	private static class TextJava2D extends Text implements Renderable {
		public TextJava2D(String text, int width, int height) {
			super(text, width, height);
		}

		public void render(Graphics2D g) {
			// XXX this is NOT failure atomic
			if (isRegular())
				g.setFont(regularfont);
			float f = 1.0f-getOpacity();
			FontMetrics fm = g.getFontMetrics();
			int w = fm.stringWidth(getText());
			int h = fm.getAscent();
			g.setColor(new Color(f, f, f));
			if (isRegular()) {
				g.drawString(getText(), bounds.x+bounds.width-w, bounds.y+(bounds.height+h)/2-2);
				g.setFont(defaultfont);
			}
			else
				g.drawString(getText(), bounds.x+(bounds.width-w)/2, bounds.y+(bounds.height+h)/2-2);
		}
	}
	
	private static class MarkingRectJava2D extends MarkingRect implements Renderable {

		public void render(Graphics2D g) {
			Color c1 = getColor();
			float f1 = getOpacity();
			//float f = 1.0f-0.3f*f1;
			g.setStroke(seqstroke);
			//g.setColor(new Color(f, f, f));
			g.setColor(new Color(
					(float)1.0f+(c1.getRed()/255.0f-1.0f)*f1,
					1.0f+(c1.getGreen()/255.0f-1.0f)*f1,
					1.0f+(c1.getBlue()/255.0f-1.0f)*f1));
			g.fillRect(bounds.x, bounds.y, bounds.width-1, bounds.height-1);
		}		
	}
	
	private static class SequenceElementJava2D extends SequenceElement implements Renderable {
		public SequenceElementJava2D(Point pos, String label) {
			super(pos, label);
		}
		
		public void render(Graphics2D g) {
			float f = getHighlight();
			float f1 = 1.0f-f;
			FontMetrics fm = g.getFontMetrics();
			int w = fm.stringWidth(getLabel());
			int h = fm.getAscent();
			g.setStroke(seqstroke);
			g.setColor(new Color(f1+f*(244.0f/255), f1+f*(184.0f/255), f1));
			g.fillRect(bounds.x, bounds.y, bounds.width-1, bounds.height-1);
			g.setColor(Color.GRAY);
			g.drawRect(bounds.x, bounds.y, bounds.width-1, bounds.height-1);
			g.setColor(Color.BLACK);
			g.drawString(getLabel(), bounds.x+(bounds.width-w)/2, bounds.y+(bounds.height+h)/2-2);
		}		
	}
	
	private static class EdgeJava2D extends Edge implements Renderable {
		public EdgeJava2D(Point from, Point to) {
			super(from, to);
		}
		
		public void render(Graphics2D g) {
			float f = 1.0f-0.7f*getOpacity();
			g.setStroke(ndstroke);
			g.setColor(new Color(f, f, f));
			g.drawLine(from.x, from.y, to.x, to.y);
		}		
	}

	// component listener methods

	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		// setting bounds to visible works
		// as long as root.trans is identity
		// FIXME we center the output here
		// this is quick AND dirty!
		synchronized (root) {
			if (root != null) {
				Rectangle r = getVisible();
				int dx = r.width/2;
				root.trans.setToTranslation(dx, 0);
				root.setBounds(new Rectangle(-dx, 0, r.width, r.height));
			}
		}
	}

	public void componentShown(ComponentEvent e) {
		//if (root != null)
		//	root.setBounds(getVisible());
		componentResized(e);
	}
	
	/**
	 * A <code>Canvas</code> descendant responsible for
	 * providing the backbuffer (via <code>createVolatileImage</code>)
	 * and updating its contents on the primary buffer.
	 * 
	 * @author mbue
	 */
	@SuppressWarnings("serial")
	private class RenderCanvas extends JPanel {
		@Override
		public void paint(Graphics g) {
			if (backbuffer != null) {
				if (!backbuffer.contentsLost())
					g.drawImage(backbuffer, 0, 0, null);
				else
					root.invalidate();
			}
		}
		
		/*@Override
		public void update(Graphics g) {
			// we override update because in the default implementation
			// update would clear the surface before calling paint
			paint(g);
		}*/
	}

}