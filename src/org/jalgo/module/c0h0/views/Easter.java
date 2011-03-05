package org.jalgo.module.c0h0.views;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**     *                           *
 * some magic   *              *                   *
 *                 *                     *    *
 * @author White Rabbit    *
 *    *     *                         *                 */
@SuppressWarnings("serial")
public class Easter extends View {
	private static class Egg extends Thread {
		private boolean isRunning = true;
		private Canvas canvas;
		private BufferStrategy strategy;
		private BufferedImage background;
		private Graphics2D backgroundGraphics;
		private Graphics2D graphics;
		private JFrame frame;
		private int width = 160;
		private int height = 120;
		private int scale = 4;
		private GraphicsConfiguration config = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();

		private Random ra = new Random();
		private List<Obstacle> obs = new ArrayList<Obstacle>();
		private List<Tail> tail = new LinkedList<Tail>();
		private final int starttail = 6;
		private int tailctr = starttail;
		private boolean pause = false;

		private Player plr = new Player();

		public class Obstacle extends Thing {
			private short type;
			private int speed = 1;
			private boolean horiz;
			private Color clr;

			public Obstacle() {
				if (ra.nextInt(5) == 0)
					type = (short) ra.nextInt(5);
				else
					type = 0;

				horiz = ra.nextBoolean();

				switch (type) {
				case 1:
					clr = Color.PINK;
					break;
				case 2:
					clr = Color.GREEN;
					break;
				case 3:
					clr = Color.RED;
					break;
				case 4:
					clr = Color.CYAN;
					break;
				default:
					clr = Color.LIGHT_GRAY;
				}
				if (horiz)
					set(0, ra.nextInt(width), clr);
				else
					set(ra.nextInt(width), 0, clr);
			}

			public boolean update() {
				if (y < height && y >= 0 && x >= 0 && x < width) {
					if (horiz)
						set(x + speed, y, clr);
					else
						set(x, y + speed, clr);
				} else {
					obs.remove(this);
					return false;
				}

				for (Tail t : tail) {
					if (t.x == x && t.y == y) {
						tail.remove(t); // t.set(t.x, t.y, this.clr);
						this.speed *= -1; // obs.remove(this);
						switch (type) {
						case 1:
							tailctr -= 1;
							plr.pts += 7;
							break;
						case 2:
							tailctr += 10;
							plr.pts += 10;
							break;
						case 3:
							tailctr = 4;
							plr.pts += 100;
							break;
						case 4:
							for (Obstacle o : obs) {
								o.speed *= -1;
							}
							plr.pts += 17;
							break;
						default:
							tailctr++;
							plr.pts++;
							break;
						}
						return false;
					}
				}
				return true;
			}
		}

		public class Player extends Thing {
			protected int speed = 1;
			protected int dx = 1, dy = 0, hp = 3, pts;
			protected long counter = 100; // 1000 frames

			public Player() {
				set(width / 2, height / 2, Color.WHITE);
				pts = 0;
				tailctr = starttail;
				for (int i = tailctr; i > 0; i--)
					tail.add(new Tail(x + i, y));
			}

			public void update() {
				if (dx != 0 || dy != 0) {
					tail.add(new Tail(x, y));
					while (tail.size() > tailctr)
						tail.remove(0);
				}
				// Staying inside the field
				if (x > width-1)
					x = 1;
				if (y > height-1)
					y = 1;
				if (x < 1)
					x = width-1;
				if (y < 1)
					y = height-1;

				set(x + dx * speed, y + dy * speed, Color.WHITE);

				if (counter == 0) {
					tailctr--; // die slowly!
					counter = 100;
				}
				counter--;
			}
		}

		public class Tail extends Thing {
			public Tail(int x, int y) {
				set(x, y, Color.BLUE);
			}
		}

		public class Thing {
			protected int x, y;
			protected Color clr;

			public void set(int x, int y, Color clr) {
				this.x = x;
				this.y = y;
				this.clr = clr;
			}

			public void render(Graphics2D g) {
				g.setColor(clr);
				g.fillRect(x, y, 1, 1);
			}
		}

		// create a hardware accelerated image
		public final BufferedImage create(final int width, final int height,
				final boolean alpha) {
			return config.createCompatibleImage(width, height,
					alpha ? Transparency.TRANSLUCENT : Transparency.OPAQUE);
		}

		// Setup
		public Egg() {
			// JFrame
			frame = new JFrame();
			frame.addWindowListener(new FrameClose());
			frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			frame.setSize(width * scale, height * scale);
			frame.setVisible(true);

			// Canvas
			canvas = new Canvas(config);
			canvas.setSize(width * scale, height * scale);
			frame.add(canvas, 0);

			// Background & Buffer
			background = create(width, height, false);
			canvas.createBufferStrategy(2);
			do {
				strategy = canvas.getBufferStrategy();
			} while (strategy == null);
			start();

			frame.addKeyListener(new FrameKey());
		}

		private class FrameClose extends WindowAdapter {
			@Override
			public void windowClosing(final WindowEvent e) {
				isRunning = false;
			}
		}

		private class FrameKey implements KeyListener {

			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
				case KeyEvent.VK_W:
					plr.dy = -1;
					plr.dx = 0;
					break;
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_S:
					plr.dy = 1;
					plr.dx = 0;
					break;
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_A:
					plr.dx = -1;
					plr.dy = 0;
					break;
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_D:
					plr.dx = 1;
					plr.dy = 0;
					break;
				case KeyEvent.VK_Q:
				case KeyEvent.VK_ESCAPE:
					isRunning = false;
					break;
				case KeyEvent.VK_ENTER:
					pause = !pause;
					break;
				case KeyEvent.VK_P:
					pause = true;
					break;
				}
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}

		}

		// Screen and buffer stuff
		private Graphics2D getBuffer() {
			if (graphics == null) {
				try {
					graphics = (Graphics2D) strategy.getDrawGraphics();
				} catch (IllegalStateException e) {
					return null;
				}
			}
			return graphics;
		}

		private boolean updateScreen() {
			graphics.dispose();
			graphics = null;
			try {
				strategy.show();
				Toolkit.getDefaultToolkit().sync();
				return (!strategy.contentsLost());

			} catch (NullPointerException e) {
				return true;

			} catch (IllegalStateException e) {
				return true;
			}
		}

		public void run() {
			backgroundGraphics = (Graphics2D) background.getGraphics();
			long fpsWait = (long) (1.0 / 30 * 1000);
			main: while (isRunning) {
				long renderStart = System.nanoTime();
				updateGame();

				// Update Graphics
				do {
					Graphics2D bg = getBuffer();
					if (!isRunning) {
						break main;
					}
					renderGame(backgroundGraphics); // this calls your draw
					if (scale != 1) {
						bg.drawImage(background, 0, 0, width * scale, height
								* scale, 0, 0, width, height, null);
					} else {
						bg.drawImage(background, 0, 0, null);
					}
					bg.dispose();
				} while (!updateScreen());

				// Better do some FPS limiting here
				long renderTime = (System.nanoTime() - renderStart) / 1000000;
				try {
					Thread.sleep(Math.max(0, fpsWait - renderTime));
				} catch (InterruptedException e) {
					Thread.interrupted();
					break;
				}
				renderTime = (System.nanoTime() - renderStart) / 1000000;

			}
			frame.dispose();
		}

		public void updateGame() {
			if (pause) {
				return;
			}

			plr.update();

			if (ra.nextInt((int) (5 + Math.pow(2, -width * height / 30 / 30
					/ 100 / 100 / (plr.pts + 1)))) == 0)
				obs.add(new Obstacle());
			int o = 0;

			while (o < obs.size()) {
				if (obs.get(o).update()) {
					o++;
				}
			}

			if (tail.size() == 0) {
				if (plr.hp > 0) {
					int hp = plr.hp;
					int pts = plr.pts;
					plr = new Player();
					plr.hp = hp - 1;
					plr.pts = pts;
					tailctr = 5;
					obs.clear();
					pause = true;
				} else {
					isRunning = false; // GAME OVER
				}
			}
		}

		public void renderGame(Graphics2D g) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
			
			if (pause) {
				g.setColor(Color.WHITE);
				int size = 12;
				g.setFont(new Font("Monospace", Font.PLAIN, size));
				g.drawString("REM LIFES: " + plr.hp, 2,
						height / 2 - size);
				g.drawString("SCORE: " + plr.pts, 2, height / 2);
				g.drawString("CONTINUE? [ENTER]", 2,
						height / 2 + size);
				return;
			}
			
			for (Tail t : tail)
				t.render(g);
			for (Obstacle o : obs)
				o.render(g);
			plr.render(g);
		}
	}

	public Easter() {
		new Egg();
	}

	public static void main(final String args[]) {
		new Egg();
	}
}
