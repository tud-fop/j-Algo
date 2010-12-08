/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2010 j-Algo-Team, j-algo-development@lists.sourceforge.net
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
package org.jalgo.module.am0c0.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.jalgo.main.util.Messages;
import org.jalgo.module.am0c0.gui.EditorView;

public class ILoveCandy extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private EditorView editorView;
	private Dimension d;
	private Font smallfont = new Font("Helvetica", Font.BOLD, 14);

	@SuppressWarnings("unused")
	private FontMetrics fmsmall, fmlarge;
	private Image ii;
	private Color dotcolor = new Color(192, 192, 0);
	private Color mazecolor;

	private boolean ingame = false;
	private boolean dying = false;

	private final int blocksize = 24;
	private final int nrofblocks = 15;
	private final int scrsize = nrofblocks * blocksize;
	private final int pacanimdelay = 2;
	private final int pacmananimcount = 4;
	private final int maxghosts = 12;
	private final int pacmanspeed = 6;

	private int pacanimcount = pacanimdelay;
	private int pacanimdir = 1;
	private int pacmananimpos = 0;
	private int nrofghosts = 6;
	private int pacsleft, score;
	@SuppressWarnings("unused")
	private int deathcounter;
	private int[] dx, dy;
	private int[] ghostx, ghosty, ghostdx, ghostdy, ghostspeed;

	private Image ghost;
	private Image pacman1, pacman2up, pacman2left, pacman2right, pacman2down;
	private Image pacman3up, pacman3down, pacman3left, pacman3right;
	private Image pacman4up, pacman4down, pacman4left, pacman4right;

	private int pacmanx, pacmany, pacmandx, pacmandy;
	private int reqdx, reqdy, viewdx, viewdy;
	/*private final short level1data[] = { 19, 26, 26, 22, 9, 12, 19, 26, 22, 9,
			12, 19, 26, 26, 22, 37, 11, 14, 17, 26, 26, 20, 15, 17, 26, 26, 20,
			11, 14, 37, 17, 26, 26, 20, 11, 6, 17, 26, 20, 3, 14, 17, 26, 26,
			20, 21, 3, 6, 25, 22, 5, 21, 7, 21, 5, 19, 28, 3, 6, 21, 21, 9, 8,
			14, 21, 13, 21, 5, 21, 13, 21, 11, 8, 12, 21, 25, 18, 26, 18, 24,
			18, 28, 5, 25, 18, 24, 18, 26, 18, 28, 6, 21, 7, 21, 7, 21, 11, 8,
			14, 21, 7, 21, 7, 21, 03, 4, 21, 5, 21, 5, 21, 11, 10, 14, 21, 5,
			21, 5, 21, 1, 12, 21, 13, 21, 13, 21, 11, 10, 14, 21, 13, 21, 13,
			21, 9, 19, 24, 26, 24, 26, 16, 26, 18, 26, 16, 26, 24, 26, 24, 22,
			21, 3, 2, 2, 6, 21, 15, 21, 15, 21, 3, 2, 2, 06, 21, 21, 9, 8, 8,
			4, 17, 26, 8, 26, 20, 1, 8, 8, 12, 21, 17, 26, 26, 22, 13, 21, 11,
			2, 14, 21, 13, 19, 26, 26, 20, 37, 11, 14, 17, 26, 24, 22, 13, 19,
			24, 26, 20, 11, 14, 37, 25, 26, 26, 28, 3, 6, 25, 26, 28, 3, 6, 25,
			26, 26, 28 };*/

	private final short leveldata[] = { 19, 26, 26, 26, 18, 18, 18, 18, 18,
			18, 18, 18, 18, 18, 22, 21, 0, 0, 0, 17, 16, 16, 16, 16, 16, 16,
			16, 16, 16, 20, 21, 0, 0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16,
			16, 20, 21, 0, 0, 0, 17, 16, 16, 24, 16, 16, 16, 16, 16, 16, 20,
			17, 18, 18, 18, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 20, 17, 16,
			16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 16, 24, 20, 25, 16, 16, 16,
			24, 24, 28, 0, 25, 24, 24, 16, 20, 0, 21, 1, 17, 16, 20, 0, 0, 0,
			0, 0, 0, 0, 17, 20, 0, 21, 1, 17, 16, 16, 18, 18, 22, 0, 19, 18,
			18, 16, 20, 0, 21, 1, 17, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16,
			20, 0, 21, 1, 17, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 20, 0, 21,
			1, 17, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 20, 0, 21, 1, 17,
			16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0, 21, 1, 25, 24, 24,
			24, 24, 24, 24, 24, 24, 16, 16, 16, 18, 20, 9, 8, 8, 8, 8, 8, 8, 8,
			8, 8, 25, 24, 24, 24, 28 };

	private final int validspeeds[] = { 1, 2, 3, 4, 6, 8 };
	private final int maxspeed = 6;

	private int currentspeed = 3;
	private short[] screendata;
	private Timer timer;
	private JButton end;

	public ILoveCandy(EditorView e) {
		editorView = e;
		end = new JButton("Stop");
		end.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				timer.stop();
				ingame = false;
				editorView.stopCandy();
			}
		});
		add(end);
		getImages();

		addKeyListener(new TAdapter());

		screendata = new short[nrofblocks * nrofblocks];
		mazecolor = new Color(5, 100, 5);
		setFocusable(true);

		d = new Dimension(400, 400);

		setBackground(Color.black);
		setDoubleBuffered(true);

		ghostx = new int[maxghosts];
		ghostdx = new int[maxghosts];
		ghosty = new int[maxghosts];
		ghostdy = new int[maxghosts];
		ghostspeed = new int[maxghosts];
		dx = new int[4];
		dy = new int[4];
		timer = new Timer(40, this);
		timer.start();
	}

	@Override
	public void addNotify() {
		super.addNotify();
		gameInit();
	}

	public void doAnim() {
		pacanimcount--;
		if (pacanimcount <= 0) {
			pacanimcount = pacanimdelay;
			pacmananimpos = pacmananimpos + pacanimdir;
			if (pacmananimpos == (pacmananimcount - 1) || pacmananimpos == 0)
				pacanimdir = -pacanimdir;
		}
	}

	public void playGame(Graphics2D g2d) {
		if (dying) {
			death();
		} else {
			movePacMan();
			drawPacMan(g2d);
			moveGhosts(g2d);
			checkMaze();
		}
	}

	public void showIntroScreen(Graphics2D g2d) {

		g2d.setColor(new Color(0, 32, 48));
		g2d.fillRect(50, scrsize / 2 - 30, scrsize - 100, 50);
		g2d.setColor(Color.white);
		g2d.drawRect(50, scrsize / 2 - 30, scrsize - 100, 50);

		String s = "Press s to start.";
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics metr = this.getFontMetrics(small);

		g2d.setColor(Color.white);
		g2d.setFont(small);
		g2d.drawString(s, (scrsize - metr.stringWidth(s)) / 2, scrsize / 2);
	}

	public void drawScore(Graphics2D g) {
		int i;
		String s;

		g.setFont(smallfont);
		g.setColor(new Color(96, 128, 255));
		s = "Score: " + score;
		g.drawString(s, scrsize / 2 + 96, scrsize + 16);
		for (i = 0; i < pacsleft; i++) {
			g.drawImage(pacman3left, i * 28 + 8, scrsize + 1, this);
		}
	}

	public void checkMaze() {
		short i = 0;
		boolean finished = true;

		while (i < nrofblocks * nrofblocks && finished) {
			if ((screendata[i] & 48) != 0)
				finished = false;
			i++;
		}

		if (finished) {
			score += 50;

			if (nrofghosts < maxghosts)
				nrofghosts++;
			if (currentspeed < maxspeed)
				currentspeed++;
			levelInit();
		}
	}

	public void death() {

		pacsleft--;
		if (pacsleft == 0)
			ingame = false;
		levelContinue();
	}

	public void moveGhosts(Graphics2D g2d) {
		short i;
		int pos;
		int count;

		for (i = 0; i < nrofghosts; i++) {
			if (ghostx[i] % blocksize == 0 && ghosty[i] % blocksize == 0) {
				pos = ghostx[i] / blocksize + nrofblocks
						* (int) (ghosty[i] / blocksize);

				count = 0;
				if ((screendata[pos] & 1) == 0 && ghostdx[i] != 1) {
					dx[count] = -1;
					dy[count] = 0;
					count++;
				}
				if ((screendata[pos] & 2) == 0 && ghostdy[i] != 1) {
					dx[count] = 0;
					dy[count] = -1;
					count++;
				}
				if ((screendata[pos] & 4) == 0 && ghostdx[i] != -1) {
					dx[count] = 1;
					dy[count] = 0;
					count++;
				}
				if ((screendata[pos] & 8) == 0 && ghostdy[i] != -1) {
					dx[count] = 0;
					dy[count] = 1;
					count++;
				}

				if (count == 0) {
					if ((screendata[pos] & 15) == 15) {
						ghostdx[i] = 0;
						ghostdy[i] = 0;
					} else {
						ghostdx[i] = -ghostdx[i];
						ghostdy[i] = -ghostdy[i];
					}
				} else {
					count = (int) (Math.random() * count);
					if (count > 3)
						count = 3;
					ghostdx[i] = dx[count];
					ghostdy[i] = dy[count];
				}

			}
			ghostx[i] = ghostx[i] + (ghostdx[i] * ghostspeed[i]);
			ghosty[i] = ghosty[i] + (ghostdy[i] * ghostspeed[i]);
			drawGhost(g2d, ghostx[i] + 1, ghosty[i] + 1);

			if (pacmanx > (ghostx[i] - 12) && pacmanx < (ghostx[i] + 12)
					&& pacmany > (ghosty[i] - 12) && pacmany < (ghosty[i] + 12)
					&& ingame) {

				dying = true;
				deathcounter = 64;

			}
		}
	}

	public void drawGhost(Graphics2D g2d, int x, int y) {
		g2d.drawImage(ghost, x, y, this);
	}

	public void movePacMan() {
		int pos;
		short ch;

		if (reqdx == -pacmandx && reqdy == -pacmandy) {
			pacmandx = reqdx;
			pacmandy = reqdy;
			viewdx = pacmandx;
			viewdy = pacmandy;
		}
		if (pacmanx % blocksize == 0 && pacmany % blocksize == 0) {
			pos = pacmanx / blocksize + nrofblocks
					* (int) (pacmany / blocksize);
			ch = screendata[pos];

			if ((ch & 16) != 0) {
				screendata[pos] = (short) (ch & 15);
				score++;
			}

			if (reqdx != 0 || reqdy != 0) {
				if (!((reqdx == -1 && reqdy == 0 && (ch & 1) != 0)
						|| (reqdx == 1 && reqdy == 0 && (ch & 4) != 0)
						|| (reqdx == 0 && reqdy == -1 && (ch & 2) != 0) || (reqdx == 0
						&& reqdy == 1 && (ch & 8) != 0))) {
					pacmandx = reqdx;
					pacmandy = reqdy;
					viewdx = pacmandx;
					viewdy = pacmandy;
				}
			}

			// Check for standstill
			if ((pacmandx == -1 && pacmandy == 0 && (ch & 1) != 0)
					|| (pacmandx == 1 && pacmandy == 0 && (ch & 4) != 0)
					|| (pacmandx == 0 && pacmandy == -1 && (ch & 2) != 0)
					|| (pacmandx == 0 && pacmandy == 1 && (ch & 8) != 0)) {
				pacmandx = 0;
				pacmandy = 0;
			}
		}
		pacmanx = pacmanx + pacmanspeed * pacmandx;
		pacmany = pacmany + pacmanspeed * pacmandy;
	}

	public void drawPacMan(Graphics2D g2d) {
		if (viewdx == -1)
			drawPacManLeft(g2d);
		else if (viewdx == 1)
			drawPacManRight(g2d);
		else if (viewdy == -1)
			drawPacManUp(g2d);
		else
			drawPacManDown(g2d);
	}

	public void drawPacManUp(Graphics2D g2d) {
		switch (pacmananimpos) {
		case 1:
			g2d.drawImage(pacman2up, pacmanx + 1, pacmany + 1, this);
			break;
		case 2:
			g2d.drawImage(pacman3up, pacmanx + 1, pacmany + 1, this);
			break;
		case 3:
			g2d.drawImage(pacman4up, pacmanx + 1, pacmany + 1, this);
			break;
		default:
			g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
			break;
		}
	}

	public void drawPacManDown(Graphics2D g2d) {
		switch (pacmananimpos) {
		case 1:
			g2d.drawImage(pacman2down, pacmanx + 1, pacmany + 1, this);
			break;
		case 2:
			g2d.drawImage(pacman3down, pacmanx + 1, pacmany + 1, this);
			break;
		case 3:
			g2d.drawImage(pacman4down, pacmanx + 1, pacmany + 1, this);
			break;
		default:
			g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
			break;
		}
	}

	public void drawPacManLeft(Graphics2D g2d) {
		switch (pacmananimpos) {
		case 1:
			g2d.drawImage(pacman2left, pacmanx + 1, pacmany + 1, this);
			break;
		case 2:
			g2d.drawImage(pacman3left, pacmanx + 1, pacmany + 1, this);
			break;
		case 3:
			g2d.drawImage(pacman4left, pacmanx + 1, pacmany + 1, this);
			break;
		default:
			g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
			break;
		}
	}

	public void drawPacManRight(Graphics2D g2d) {
		switch (pacmananimpos) {
		case 1:
			g2d.drawImage(pacman2right, pacmanx + 1, pacmany + 1, this);
			break;
		case 2:
			g2d.drawImage(pacman3right, pacmanx + 1, pacmany + 1, this);
			break;
		case 3:
			g2d.drawImage(pacman4right, pacmanx + 1, pacmany + 1, this);
			break;
		default:
			g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
			break;
		}
	}

	public void drawMaze(Graphics2D g2d) {
		short i = 0;
		int x, y;

		for (y = 0; y < scrsize; y += blocksize) {
			for (x = 0; x < scrsize; x += blocksize) {
				g2d.setColor(mazecolor);
				g2d.setStroke(new BasicStroke(2));

				if ((screendata[i] & 1) != 0) // draws left
				{
					g2d.drawLine(x, y, x, y + blocksize - 1);
				}
				if ((screendata[i] & 2) != 0) // draws top
				{
					g2d.drawLine(x, y, x + blocksize - 1, y);
				}
				if ((screendata[i] & 4) != 0) // draws right
				{
					g2d.drawLine(x + blocksize - 1, y, x + blocksize - 1, y
							+ blocksize - 1);
				}
				if ((screendata[i] & 8) != 0) // draws bottom
				{
					g2d.drawLine(x, y + blocksize - 1, x + blocksize - 1, y
							+ blocksize - 1);
				}
				if ((screendata[i] & 16) != 0) // draws point
				{
					g2d.setColor(dotcolor);
					g2d.fillRect(x + 11, y + 11, 2, 2);
				}
				i++;
			}
		}
	}

	public void gameInit() {
		pacsleft = 3;
		score = 0;
		levelInit();
		nrofghosts = 6;
		currentspeed = 3;
	}

	public void levelInit() {
		int i;
		/*if (currentspeed == 4 || currentspeed == 6) {
			for (i = 0; i < nrofblocks * nrofblocks; i++)
				screendata[i] = level1data[i];
		} else {
			for (i = 0; i < nrofblocks * nrofblocks; i++)
				screendata[i] = level2data[i];
		}*/
		for (i = 0; i < nrofblocks * nrofblocks; i++)
			screendata[i] = leveldata[i];
		levelContinue();
	}

	public void levelContinue() {
		short i;
		int dx = 1;
		int random;

		for (i = 0; i < nrofghosts; i++) {
			ghosty[i] = 4 * blocksize;
			ghostx[i] = 4 * blocksize;
			ghostdy[i] = 0;
			ghostdx[i] = dx;
			dx = -dx;
			random = (int) (Math.random() * (currentspeed + 1));
			if (random > currentspeed)
				random = currentspeed;
			ghostspeed[i] = validspeeds[random];
		}

		pacmanx = 7 * blocksize;
		pacmany = 11 * blocksize;
		pacmandx = 0;
		pacmandy = 0;
		reqdx = 0;
		reqdy = 0;
		viewdx = -1;
		viewdy = 0;
		dying = false;
	}

	public void getImages() {

		ghost = new ImageIcon(Messages.getResourceURL("am0c0", "ghost"))
				.getImage();
		pacman1 = new ImageIcon(Messages.getResourceURL("am0c0", "pacman"))
				.getImage();
		pacman2up = new ImageIcon(Messages.getResourceURL("am0c0", "up1"))
				.getImage();
		pacman3up = new ImageIcon(Messages.getResourceURL("am0c0", "up2"))
				.getImage();
		pacman4up = new ImageIcon(Messages.getResourceURL("am0c0", "up3"))
				.getImage();
		pacman2down = new ImageIcon(Messages.getResourceURL("am0c0", "down1"))
				.getImage();
		pacman3down = new ImageIcon(Messages.getResourceURL("am0c0", "down2"))
				.getImage();
		pacman4down = new ImageIcon(Messages.getResourceURL("am0c0", "down3"))
				.getImage();
		pacman2left = new ImageIcon(Messages.getResourceURL("am0c0", "left1"))
				.getImage();
		pacman3left = new ImageIcon(Messages.getResourceURL("am0c0", "left2"))
				.getImage();
		pacman4left = new ImageIcon(Messages.getResourceURL("am0c0", "left3"))
				.getImage();
		pacman2right = new ImageIcon(Messages.getResourceURL("am0c0", "right1"))
				.getImage();
		pacman3right = new ImageIcon(Messages.getResourceURL("am0c0", "right2"))
				.getImage();
		pacman4right = new ImageIcon(Messages.getResourceURL("am0c0", "right3"))
				.getImage();

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, d.width, d.height);

		drawMaze(g2d);
		drawScore(g2d);
		doAnim();
		if (ingame)
			playGame(g2d);
		else
			showIntroScreen(g2d);

		g.drawImage(ii, 5, 5, this);
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	class TAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();

			if (ingame) {
				if (key == KeyEvent.VK_LEFT) {
					reqdx = -1;
					reqdy = 0;
				} else if (key == KeyEvent.VK_RIGHT) {
					reqdx = 1;
					reqdy = 0;
				} else if (key == KeyEvent.VK_UP) {
					reqdx = 0;
					reqdy = -1;
				} else if (key == KeyEvent.VK_DOWN) {
					reqdx = 0;
					reqdy = 1;
				} else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
					ingame = false;
				} else if (key == KeyEvent.VK_PAUSE || key == KeyEvent.VK_P) {
					if (timer.isRunning())
						timer.stop();
					else
						timer.start();
				}
			} else {
				if (key == 's' || key == 'S') {
					ingame = true;
					gameInit();
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();

			if (key == Event.LEFT || key == Event.RIGHT || key == Event.UP
					|| key == Event.DOWN) {
				reqdx = 0;
				reqdy = 0;
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}
}
