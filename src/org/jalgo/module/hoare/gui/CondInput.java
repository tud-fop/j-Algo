package org.jalgo.module.hoare.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.control.ProgramControl.Edit;
import org.jalgo.module.hoare.model.StatSeqVF;
import org.jalgo.module.hoare.model.StrongPreVF;
import org.jalgo.module.hoare.model.VerificationFormula;
import org.jalgo.module.hoare.model.WeakPostVF;

public class CondInput extends JPanel implements Observer, GUIConstants {

	/**
	 * Input-Mask for Pre- and Post-Conditions
	 * 
	 * @author Markus
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JToolBar toolbar;

	private JPanel mainpanel;

	private JButton jButtonAnd;

	private JButton jButtonOr;

	private JButton jButtonSigma;

	private JButton jButtonOK;

	private JButton jButtonProd;

	private JButton jButtonNot;

	private JButton jButtonSqrt;

	private JButton jButtonAbs;

	private JButton jButtonLn;

	private JButton jButtonExp;

	private JButton jButtonLequal;

	private JButton jButtonGequal;

	private JTextPane preedit;

	private JTextPane postedit;

	private JTextPane code;

	private int focused = 0;

	private StyleSheet cssNorm;

	private StyleSheet cssBeam;

	private HTMLEditorKit htmlEditKit;

	private GuiControl gui;
	
	/**
	 * Constructs a <code>CondInput</code> object with the given
	 * reference.
	 * 
	 * @param gui
	 *            the <code>GUIController</code> instance
	 */
	
	public CondInput(final GuiControl gui) {

		super();
		gui.addObserver(this);
		this.gui = gui;

		this.setLayout(new BorderLayout());

		toolbar = new JToolBar();
		this.add(toolbar, BorderLayout.NORTH);
		toolbar.setFloatable(false);

		jButtonAnd = new JButton();
		jButtonOr = new JButton();
		jButtonSigma = new JButton();
		jButtonProd = new JButton();
		jButtonNot = new JButton();
		jButtonSqrt = new JButton();
		jButtonAbs = new JButton();
		jButtonLn = new JButton();
		jButtonExp = new JButton();
		jButtonLequal = new JButton();
		jButtonGequal = new JButton();

		toolbar.add(jButtonAnd);
		jButtonAnd.setText("\u2227");
		jButtonAnd.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (focused == 1) {
					preedit.replaceSelection("\u2227");
					preedit.requestFocus();
				}
				if (focused == 2) {
					postedit.replaceSelection("\u2227");
					postedit.requestFocus();
				}

			}

		});
		toolbar.add(jButtonOr);
		jButtonOr.setText("\u2228");
		jButtonOr.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (focused == 1) {
					preedit.replaceSelection("\u2228");
					preedit.requestFocus();
				}
				if (focused == 2) {
					postedit.replaceSelection("\u2228");
					postedit.requestFocus();
				}

			}

		});
		toolbar.add(jButtonNot);
		jButtonNot.setText("\u00AC");
		jButtonNot.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (focused == 1) {
					preedit.replaceSelection("\u00AC");
					preedit.requestFocus();
				}
				if (focused == 2) {
					postedit.replaceSelection("\u00AC");
					postedit.requestFocus();
				}

			}

		});

		toolbar.add(jButtonSigma);
		jButtonSigma.setText("\u2211");
		jButtonSigma.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (focused == 1) {
					preedit
							.replaceSelection("\u2211("
									+ Messages.getString("hoare",
											"out.function")
									+ ","
									+ Messages.getString("hoare",
											"out.controlVariable") + ","
									+ Messages.getString("hoare", "out.from")
									+ ","
									+ Messages.getString("hoare", "out.to")
									+ ")");
					preedit.requestFocus();
				}
				if (focused == 2) {
					postedit
							.replaceSelection("\u2211("
									+ Messages.getString("hoare",
											"out.function")
									+ ","
									+ Messages.getString("hoare",
											"out.controlVariable") + ","
									+ Messages.getString("hoare", "out.from")
									+ ","
									+ Messages.getString("hoare", "out.to")
									+ ")");
					postedit.requestFocus();
				}

			}

		});
		toolbar.add(jButtonProd);
		jButtonProd.setText("\u220F");
		jButtonProd.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (focused == 1) {
					preedit
							.replaceSelection("\u220F("
									+ Messages.getString("hoare",
											"out.function")
									+ ","
									+ Messages.getString("hoare",
											"out.controlVariable") + ","
									+ Messages.getString("hoare", "out.from")
									+ ","
									+ Messages.getString("hoare", "out.to")
									+ ")");
					preedit.requestFocus();
				}
				if (focused == 2) {
					postedit
							.replaceSelection("\u220F("
									+ Messages.getString("hoare",
											"out.function")
									+ ","
									+ Messages.getString("hoare",
											"out.controlVariable") + ","
									+ Messages.getString("hoare", "out.from")
									+ ","
									+ Messages.getString("hoare", "out.to")
									+ ")");
					postedit.requestFocus();
				}

			}

		});

		toolbar.add(jButtonAbs);
		jButtonAbs.setText("|x|");
		jButtonAbs.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (focused == 1) {
					preedit.replaceSelection("abs(x)");
					preedit.requestFocus();
				}
				if (focused == 2) {
					postedit.replaceSelection("abs(x)");
					postedit.requestFocus();
				}

			}

		});

		toolbar.add(jButtonLn);
		jButtonLn.setText("ln x");
		jButtonLn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (focused == 1) {
					preedit.replaceSelection("ln(x)");
					preedit.requestFocus();
				}
				if (focused == 2) {
					postedit.replaceSelection("ln(x)");
					postedit.requestFocus();
				}

			}

		});

		toolbar.add(jButtonExp);
		jButtonExp.setText("x\u00AA");
		jButtonExp.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (focused == 1) {
					preedit.replaceSelection("x^a");
					preedit.requestFocus();
				}
				if (focused == 2) {
					postedit.replaceSelection("x^a");
					postedit.requestFocus();
				}
			}

		});

		toolbar.add(jButtonSqrt);
		jButtonSqrt.setText("\u221A");
		jButtonSqrt.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (focused == 1) {
					preedit.replaceSelection("\u221A(x)");
					preedit.requestFocus();
				}
				if (focused == 2) {
					postedit.replaceSelection("\u221A(x)");
					postedit.requestFocus();
				}
			}

		});

		toolbar.add(jButtonLequal);
		jButtonLequal.setText("\u2264");
		jButtonLequal.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (focused == 1) {
					preedit.replaceSelection("\u2264");
					preedit.requestFocus();
				}
				if (focused == 2) {
					postedit.replaceSelection("\u2264");
					postedit.requestFocus();
				}
			}

		});

		toolbar.add(jButtonGequal);
		jButtonGequal.setText("\u2265");
		jButtonGequal.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (focused == 1) {
					preedit.replaceSelection("\u2265");
					preedit.requestFocus();
				}
				if (focused == 2) {
					postedit.replaceSelection("\u2265");
					postedit.requestFocus();
				}
			}

		});

		mainpanel = new JPanel();
		JScrollPane scrollPane = new JScrollPane();
		JViewport viewp = new JViewport();
		viewp.setView(mainpanel);
		scrollPane.setViewport(viewp);
		this.add(scrollPane, BorderLayout.CENTER);

		mainpanel.setLayout(new BorderLayout());

		this.htmlEditKit = new HTMLEditorKit();
		htmlEditKit.setLinkCursor(new Cursor(Cursor.HAND_CURSOR));

		this.cssNorm = new StyleSheet();
		cssNorm.importStyleSheet(CSS_INPUT_NORM);

		this.cssBeam = new StyleSheet();
		cssBeam.importStyleSheet(CSS_INPUT_BEAM);

		htmlEditKit.setStyleSheet(cssNorm);

		preedit = new JTextPane();
		preedit.setMaximumSize(new java.awt.Dimension(400, 22));
		preedit
				.setBorder(BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED));
		preedit.setEditorKit(htmlEditKit);
		preedit.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent arg0) {
				focused = 1;

			}

			public void focusLost(FocusEvent arg0) {
			}

		});
		preedit.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					jButtonOK.doClick();
					preedit.setText(preedit.getText().replaceAll(
							System.getProperty("line.separator"), ""));
				}
			}

			public void keyReleased(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					jButtonOK.doClick();
					preedit.setText(preedit.getText().replaceAll(
							System.getProperty("line.separator"), ""));
				}

			}

			public void keyTyped(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					preedit.setText(preedit.getText().replaceAll(
							System.getProperty("line.separator"), ""));
			}

		});

		code = new JTextPane();
		code.setMaximumSize(new java.awt.Dimension(400, 22));
		code.setOpaque(false);
		code.setEditable(false);
		code.setEditorKit(htmlEditKit);

		postedit = new JTextPane();
		postedit.setBorder(BorderFactory
				.createEtchedBorder(BevelBorder.LOWERED));
		postedit.setMaximumSize(new java.awt.Dimension(400, 22));
		postedit.setEditorKit(htmlEditKit);
		postedit.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent arg0) {
				focused = 2;

			}

			public void focusLost(FocusEvent arg0) {
			}

		});
		postedit.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent arg0) {
				// TODO Automatisch erstellter Methoden-Stub
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					jButtonOK.doClick();
					postedit.setText(postedit.getText().replaceAll(
							System.getProperty("line.separator"), ""));
				}
			}

			public void keyReleased(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					jButtonOK.doClick();
					postedit.setText(postedit.getText().replaceAll(
							System.getProperty("line.separator"), ""));
				}
			}

			public void keyTyped(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					postedit.setText(postedit.getText().replaceAll(
							System.getProperty("line.separator"), ""));
			}

		});

		jButtonOK = new JButton();
		jButtonOK.setText("OK");
		jButtonOK.setPreferredSize(new java.awt.Dimension(150, 25));
		ActionListener oki = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO: BUG: wenn leere pre/post eingegeben wird!
				if (gui.getActiveNode() < 0)
					return;

				int index1 = preedit.getText().indexOf("<div class=");
				int index2 = preedit.getText().indexOf("</div>");

				String pre = new String("");

				if ((index1 != -1) && (index2 != -1)) {

					pre = preedit.getText();

					index1 = index1 + ((pre.indexOf(">", index1)) - index1) + 1;
					pre = pre.substring(index1, index2);

					// index1 =
					// pre.indexOf(System.getProperty("line.separator"),
					// index1)+1;
					// pre = pre.substring(index1);
					// index1 =
					// pre.indexOf(System.getProperty("line.separator"));
					// pre = pre.substring(0,index1);
					pre = pre.replaceAll("&nbsp", "");
					pre = pre.replaceAll("\\s", "");
					pre = pre.replaceAll("&#8804;", "<=");
					pre = pre.replaceAll("&#8805;", ">=");
					pre = pre.replaceAll("&lt;", "<");
					pre = pre.replaceAll("&gt;", ">");
					pre = pre.replaceAll("&#8730;", "sqrt");
					pre = pre.replaceAll("&#8743;", "&&");
					pre = pre.replaceAll("&#8744;", "||");
					pre = pre.replaceAll("&#172;", "!");
					pre = pre.replaceAll("&#8721;", "Sum");
					pre = pre.replaceAll("&#8719;", "Product");

				}

				int index3 = postedit.getText().indexOf("<div class=");
				int index4 = postedit.getText().indexOf("</div>");

				String post = new String("");

				if ((index3 != -1) && (index4 != -1)) {

					post = postedit.getText();

					index3 = index3 + ((post.indexOf(">", index3)) - index3)
							+ 1;
					post = post.substring(index3, index4);

					post = post.replaceAll("&nbsp", "");
					post = post.replaceAll("\\s", "");
					post = post.replaceAll("&#8804;", "<=");
					post = post.replaceAll("&#8805;", ">=");
					post = post.replaceAll("&lt;", "<");
					post = post.replaceAll("&gt;", ">");
					post = post.replaceAll("&#8730;", "sqrt");
					post = post.replaceAll("&#8743;", "&&");
					post = post.replaceAll("&#8744;", "||");
					post = post.replaceAll("&#172;", "!");
					post = post.replaceAll("&#8721;", "Sum");
					post = post.replaceAll("&#8719;", "Product");

				}

				gui.applyTreeEdit(Edit.AssertionEdit, gui.getActiveNode(), pre,
						post);

			}

		};
		jButtonOK.addActionListener(oki);
		jButtonOK.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "released");

		Box box = Box.createVerticalBox();
		box.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 5),
				new Dimension(0, 20)));
		box.add(preedit);
		preedit.setAlignmentX(Component.CENTER_ALIGNMENT);

		box.add(code);
		code.setAlignmentX(Component.CENTER_ALIGNMENT);

		box.add(postedit);
		postedit.setAlignmentX(Component.CENTER_ALIGNMENT);

		box.add(jButtonOK);
		jButtonOK.setAlignmentX(Component.CENTER_ALIGNMENT);

		mainpanel.add(box, BorderLayout.CENTER);
	}

	public void update(Observable o, Object arg) {

		if (gui.getActiveNode() >= 0) {
			String codeseg = gui.getCodeSegment(gui.getActiveNode());
			codeseg = codeseg.replaceAll("<", "&lt;");
			codeseg = codeseg.replaceAll(">", "&gt;");
			try {
				code.getDocument().remove(0, code.getDocument().getLength());
				preedit.getDocument().remove(0, preedit.getDocument().getLength());
				postedit.getDocument().remove(0, postedit.getDocument().getLength());
			} catch (BadLocationException e) {
				/**/
			}
			
			code.setText("<center><div class='codeseg'>" + codeseg
					+ "</div></center>");

			preedit.setEnabled(false);
			postedit.setEnabled(false);

			VerificationFormula vf = gui.getVF(gui.getActiveNode());
			if ((vf instanceof WeakPostVF)
					|| (vf == gui.getRoot())
					|| (vf.getParent() != null && !(vf instanceof StrongPreVF)
							&& vf.getParent().getType().equals(StatSeqVF.class) && vf
							.getParent().getChildren().get(0) == vf))
				postedit.setEnabled(true);

			if ((vf instanceof StrongPreVF)
					|| (vf == gui.getRoot())
					|| (vf.getParent() != null && !(vf instanceof WeakPostVF)
							&& vf.getParent().getType().equals(StatSeqVF.class) && vf
							.getParent().getChildren().get(1) == vf))
				preedit.setEnabled(true);

		} else
			code.setText("");

		if (gui.getActiveNode() >= 0) {
			preedit.setText("<center><div class='cond'>" + gui.getPre()
					+ "</div></center>");
			postedit.setText("<center><div class='cond'>" + gui.getPost()
					+ "</div></center>");
		} else {
			preedit.setText("<center><div class='cond'>"
					+ Messages.getString("hoare", "out.preCond") + ": "
					+ "</div></center>");
			postedit.setText("<center><div class='cond'>"
					+ Messages.getString("hoare", "out.postCond") + ": "
					+ "</div></center>");
		}

	}
	/**
	 *	sets the Beamer-Mode either active or inactive  
	 * 
	 * @param boolean b
	 */
	public void setBeamer(boolean b) {

		String temp1 = preedit.getText();
		String temp2 = postedit.getText();
		String temp3 = code.getText();

		if (b) {
			htmlEditKit.setStyleSheet(cssBeam);
			preedit.setMaximumSize(new Dimension(400, 30));
			postedit.setMaximumSize(new Dimension(400, 30));
			code.setMaximumSize(new Dimension(400, 30));
		} else {
			htmlEditKit.setStyleSheet(cssNorm);
			preedit.setMaximumSize(new java.awt.Dimension(400, 22));
			postedit.setMaximumSize(new java.awt.Dimension(400, 22));
			code.setMaximumSize(new java.awt.Dimension(400, 22));
		}

		preedit.setEditorKit(htmlEditKit);
		preedit.setText(temp1);
		postedit.setEditorKit(htmlEditKit);
		postedit.setText(temp2);
		code.setEditorKit(htmlEditKit);
		code.setText(temp3);

	}

}