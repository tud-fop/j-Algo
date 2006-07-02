/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
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

/* Created on 01.05.2005 */
package org.jalgo.tests.avl.gui;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import org.jalgo.main.JAlgoMain;

public class TestGUI {

	private static Robot robot;

	public static void performTest() {
		new Thread() {
		    @SuppressWarnings("synthetic-access")
			public void run() {
				try {
					//resizing the window
				    Thread.sleep(4000);
				    robot = new Robot();
					robot.mouseMove(300, 110);
					click();
					click();
					robot.mouseMove(300, 10);
					click();
					click();
					//open avl module
					robot.mouseMove(120, 138);
					click();
					robot.mouseMove(120, 158);
				    Thread.sleep(20);
					robot.mouseMove(300, 158);
				    Thread.sleep(20);
					robot.mouseMove(300, 180);
					click();
					//wait for loading module
					Thread.sleep(2500);
/*					//switch to standard layout
					robot.mouseMove(500, 370);
					Thread.sleep(500);
*/
/*					//open file
					robot.mouseMove(400, 370);
					click();
					Thread.sleep(4000);
					//type in filename
					type("randomtree.jalgo");
					robot.mouseMove(750, 580);
					click();
*/					
					//create random tree
					robot.mouseMove(600, 370);
					click();
					Thread.sleep(50);
					type(KeyEvent.VK_DELETE);
					type(KeyEvent.VK_DELETE);
					type(KeyEvent.VK_1);
					type(KeyEvent.VK_5);
					type(KeyEvent.VK_ENTER);
				}
				catch (InterruptedException e) {e.printStackTrace();}
				catch (AWTException e) {e.printStackTrace();}
		    }
		}.start();
	}

	protected static void type(int keycode) {
		robot.keyPress(keycode);
		robot.keyRelease(keycode);
	}

	@SuppressWarnings("unused")
	private static void type(String s) {
		for (byte c : s.getBytes()) {
			if (c == KeyEvent.VK_PERIOD) c = (byte)(c-'A'+'a');
			robot.keyPress(c-'a'+'A');
			robot.keyRelease(c-'a'+'A');
		}
	}
	
	private static void click() {
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}

	public static void main(String[] args) {
		performTest();
		JAlgoMain.main(args);
	}
}