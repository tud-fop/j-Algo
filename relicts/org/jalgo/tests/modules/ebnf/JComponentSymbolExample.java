package org.jalgo.tests.ebnf;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import org.jalgo.module.ebnf.renderer.*;



//<<<<<<< JComponentSymbolExample.java

/* Test, ob ich was überschreiben kann*/
/* nein, kannst du nicht *g* - war hier nur noch alles rot...
 public class JComponentSymbolExample extends JPanel implements KeyListener, MouseMotionListener, MouseListener, ActionListener {
 =======
 import java.util.List;
 import java.util.ArrayList;
 /* Test, ob ich was ï¿½berschreiben kann*/
public class JComponentSymbolExample extends JPanel implements
		MouseMotionListener, MouseListener, ActionListener {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPopupMenu pMenu;

	private int diff_x = 0;

	private int diff_y = 0;

	private Point pMouse;

	private JMenuItem termitem = new JMenuItem("Terminalsymbol");

	private JMenuItem varitem = new JMenuItem("Variable");

	private JMenuItem branchitem = new JMenuItem("Verzweigung");

	private JTextField editText;

	private SynDiaElem lastElem;

	private SyntaxDiagram syndia;

	/**
	 * 
	 */
	public JComponentSymbolExample() {

		this.setLayout(null);

		

		//System.out.println(syndia.sayHello());
		//System.out.println(syndia.contentToString());
		//System.out.println(b1.contentToString());
		//System.out.println(c2.contentToString());

		editText = new JTextField();
		editText.addActionListener(this);
		add(editText);

		addMouseListener(this);

		this.setBackground(Color.WHITE);

		termitem.addActionListener(this);
		varitem.addActionListener(this);
		branchitem.addActionListener(this);

		pMenu = new JPopupMenu();
		pMenu.add(termitem);
		pMenu.add(varitem);
		pMenu.add(branchitem);

		pMouse = new Point();
		pMouse.setLocation(50, 50);

	}

	public void actionPerformed(ActionEvent event) {

		/*
		 * Das Abfangen des Enterdrucks bei Ãƒâ€žnderung eines Symbols des
		 * Syntaxdiagrammes
		 */
		if (editText.isVisible()) {

			editText.setVisible(false);
			if (lastElem != null && !editText.getText().equals(""))
				lastElem.setText(editText.getText());
			lastElem = null;

			repaint();
			editText.setText("");

		}
		if (event.getSource() == termitem)
			reactToMenuSelection("terminal");
		else if (event.getSource() == varitem)
			reactToMenuSelection("variable");
		else if (event.getSource() == branchitem)
			reactToMenuSelection("branch");

	}

	public void reactToMenuSelection(String elem) {

		System.out.println("Creating new " + elem + "-Symbol-Object [JPanel]");
		SynDiaElem el = SymbolFactory.createElement(elem);
		el.addMouseListener(this);
		el.addMouseMotionListener(this);
		el.setLocation(pMouse.x, pMouse.y);
		this.add(el);
		el.setVisible(true);

		setComponentZOrder(el, 0);
		el.repaint();

		System.out.println(el.getText());

		if (elem != "branch")
			this.showEditText((Component) el);

		System.out.println("got HashCode: " + el.hashCode());
	}

	/*
	 * public void reactToVarMenuSelection() {
	 * 
	 * 
	 * System.out.println("Creating new Var-Symbol-Object [JPanel]"); Variable
	 * var = new Variable(varlist[random.nextInt(15)]);
	 * var.addMouseListener(this); var.addMouseMotionListener(this);
	 * var.setLocation(pMouse.x, pMouse.y); this.add(var); var.setVisible(true);
	 * 
	 * setComponentZOrder(var, 0); var.repaint(); }
	 * 
	 * public void keyPressed(KeyEvent e) {
	 * 
	 * if (e.getKeyChar() == 10) {
	 * 
	 * System.out.println("jipppie"); } System.out.println(e.getKeyChar()); }
	 * 
	 * public void keyTyped(KeyEvent e) {} public void keyReleased(KeyEvent e) {}
	 */

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {

		if (e.getComponent().getClass() != JComponentSymbolExample.class)
			e.getComponent().setLocation(
					e.getComponent().getX() - (diff_x - e.getX()),
					e.getComponent().getY() - (diff_y - e.getY()));

		setComponentZOrder((SynDiaElem) e.getComponent(), 0);

		/*
		 * Zur Neusetzung der Panelgröße, fürs Scrollen
		 */
		Component[] comps = getComponents();
		int x = 0;
		int y = 0;

		for (Component c : comps) {

			if (c.getX() + c.getWidth() > x)
				x = c.getX() + c.getWidth();
			if (c.getY() + c.getHeight() > y)
				y = c.getY() + c.getHeight();
		}

		setSize(x, y);
		revalidate();

		// System.out.println(this.getWidth()+" x "+this.getHeight());
		System.out.println("HashCode: " + e.getComponent().hashCode());
		repaint();
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		diff_x = e.getX();
		diff_y = e.getY();
	}

	public void showEditText(Component c) {

		int x = 50;
		if (c.getWidth() > 65)
			x = c.getWidth() - 15;
		editText.setBounds(c.getX() + 6, c.getY() + 5, x, 20);

		editText.setText(((SynDiaElem) c).getText());
		editText.setVisible(true);
		setComponentZOrder(editText, 0);
		editText.requestFocus(true);
		editText.selectAll();
		lastElem = (SynDiaElem) c;

	}

	public void mouseClicked(MouseEvent e) {

		System.out.println(e.getComponent().getClass().toString());

		if (e.getClickCount() == 2
				&& e.getComponent().getClass().getGenericSuperclass() == SynDiaElem.class) {

			this.showEditText(e.getComponent());
		} else if (e.getClickCount() == 1 && editText.isVisible()) {

			editText.setVisible(false);
			if (lastElem != null && !editText.getText().equals(""))
				lastElem.setText(editText.getText());
			lastElem = null;

			repaint();
			editText.setText("");

		}
	}

	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {

			pMenu.show(e.getComponent(), e.getX(), e.getY());
			pMouse.setLocation(e.getX(), e.getY());
		}

	}

	public static void main(String[] args) {

		JFrame f = new JFrame("moin");
		f.setSize(800, 600);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		f.add(new JComponentSymbolExample());

		f.setVisible(true);
	}

}