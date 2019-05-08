package org.jalgo.module.levenshtein.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jalgo.main.util.Messages;
import org.jalgo.module.levenshtein.gui.GuiController;
import org.jalgo.module.levenshtein.gui.LatexRenderer;
import org.jalgo.module.levenshtein.gui.events.HideGeneralFormulaAction;
import org.jalgo.module.levenshtein.gui.events.ShowGeneralFormulaAction;
import org.jalgo.module.levenshtein.model.Controller;
import org.jalgo.module.levenshtein.pattern.CellClickedObserver;
import org.jalgo.module.levenshtein.pattern.ChangeGeneralFormulaSizeObserver;
import org.jalgo.module.levenshtein.pattern.ChangeSizeObservable;

public class GFP extends JPanel implements CellClickedObserver, ChangeSizeObservable {

	private static final long serialVersionUID = -5291732915625263050L;
	private LeftPanel leftPanel;
	private RightBigPanel rightBigPanel;
	private RightSmallPanel rightSmallPanel;

	private JButton downButton;
	private JButton upButton;
	private final ImageIcon downIcon;
	private final ImageIcon upIcon;

	private GridBagConstraints leftPanelConstraints;
	private GridBagConstraints rightPanelConstraints;

	private boolean smallFormula = false;

	public GFP() {
		setLayout(new GridBagLayout());

		leftPanelConstraints = new GridBagConstraints();

		leftPanelConstraints.gridx = 0;
		leftPanelConstraints.gridy = 0;
		leftPanelConstraints.anchor = GridBagConstraints.NORTH;

		downIcon = new ImageIcon(Messages.getResourceURL("levenshtein", "Ui.Downarrow"));
		upIcon = new ImageIcon(Messages.getResourceURL("levenshtein", "Ui.Uparrow"));

		ImageIcon resDownIcon = resizeImageIcon(downIcon);
		ImageIcon resUpIcon = resizeImageIcon(upIcon);

		downButton = new JButton(resDownIcon);
		upButton = new JButton(resUpIcon);

		downButton.setOpaque(false);
		downButton.setBorderPainted(false);
		upButton.setOpaque(false);
		upButton.setBorderPainted(false);

		leftPanel = new LeftPanel();
//		leftPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		leftPanel.add(downButton);
		add(leftPanel, leftPanelConstraints);

		rightPanelConstraints = new GridBagConstraints();
		rightPanelConstraints.gridx = 1;
		rightPanelConstraints.gridy = 0;
		rightPanelConstraints.anchor = GridBagConstraints.CENTER;

		rightBigPanel = new RightBigPanel();
//		rightBigPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		add(rightBigPanel, rightPanelConstraints);

		rightSmallPanel = new RightSmallPanel();
//		rightSmallPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		
		setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
	}

	private ImageIcon resizeImageIcon(ImageIcon imgIcon) {
		Image img = imgIcon.getImage();
		int imgSize = 30;
		Image newimg = img.getScaledInstance(imgSize, imgSize, Image.SCALE_SMOOTH);
		return new ImageIcon(newimg);
	}

	public void registerObserver(ChangeGeneralFormulaSizeObserver obs) {
		downButton.addMouseListener(new HideGeneralFormulaAction(obs));
		upButton.addMouseListener(new ShowGeneralFormulaAction(obs));
	}

	public void cellClicked(int j, int i, boolean wasAlreadyFilled) {
		rightSmallPanel.cellClicked(j, i, wasAlreadyFilled);
		rightBigPanel.cellClicked(j, i, wasAlreadyFilled);
	}

	public void setController(Controller controller) {
		rightSmallPanel.setController(controller);
		rightBigPanel.setController(controller);
	}

	private void onResize() {
		Dimension dim = getPreferredSize();
		onResize(dim.width, dim.height);
	}

	public void onResize(int width, int height) {
		setPreferredSize(new Dimension(width, height));
		int padding = 5;
		int leftSize = Math.min(height - padding, 50);
		leftPanel.onResize(leftSize, leftSize);

		rightSmallPanel.onResize(width - leftSize - padding, height - padding);

		rightBigPanel.onResize(width - leftSize - padding, height - padding);
	}

	public void setSmallFormula(boolean smallFormula) {
		this.smallFormula = smallFormula;
		if (smallFormula) {
			leftPanel.remove(downButton);
			leftPanel.add(upButton);
			remove(rightBigPanel);
			add(rightSmallPanel, rightPanelConstraints);
		} else {
			leftPanel.remove(upButton);
			leftPanel.add(downButton);
			remove(rightSmallPanel);
			add(rightBigPanel, rightPanelConstraints);
		}

	}

	private static class LeftPanel extends JPanel {

		private static final long serialVersionUID = -5515515279647193697L;

		public void onResize(int width, int height) {
			setPreferredSize(new Dimension(width, height));
		}
	}

	private static abstract class RightPanel extends JPanel {

		private static final long serialVersionUID = -8647164889272754307L;
		protected Controller controller;

		protected String fillTarget0Row = "d(0,i) = i \\cdot $c_{ins}$";
		protected String target0Constr = "\\forall\\ 0 \\le i \\le k";
		protected String fillSource0Col = "d(j,0) = j \\cdot $c_{del}$";
		protected String source0Constr = "\\forall\\ 0 \\le j \\le n";
		protected String formInsert = "d(j,i-1) + $c_{ins}$";
		protected String formDelete = "d(j-1,i) + $c_{del}$";
		protected String formSubs = "d(j-1,i-1) + ";
		protected String restConstr = "\\forall\\ 1 \\le j \\le n, \\\\" + "1 \\le i \\le k";
		protected String costSubstitution = "c_{sub}";
		protected String subsConstr = "\\text{" + GuiController.getString("gui.if") + "}, $w_j$ \\neq $v_i$ \\\\";
		protected String costIdentity = "$c_{id}$";
		protected String idConstr = "\\text{" + GuiController.getString("gui.otherwise") + "}";
		protected String closingBracket = "\\end{array}\\right\\}";
		protected String fillRest = "d(j,i) = \\min";
		protected String openingBracket = "\\left\\{\\begin{array}{lc}";

		protected final int padding = 5;

		protected boolean wasAlreadyFilled = false;
		protected int clickedJ = -1;
		protected int clickedI = -1;

		public void onResize(int width, int height) {
			setPreferredSize(new Dimension(width, height));
		}

		public void setController(Controller controller) {
			this.controller = controller;
		}

		public abstract void cellClicked(int j, int i, boolean wasAlreadyFilled);
	}

	public static class RightBigPanel extends RightPanel {

		private static final long serialVersionUID = -854759571508751376L;

		private JLabel lblD0i = new JLabel();
		private JLabel lbl0ik = new JLabel();
		private JLabel lblDj0 = new JLabel();
		private JLabel lbl0jn = new JLabel();
		private JLabel lblDji = new JLabel();
		private JLabel lbl1ik1jn = new JLabel();

		public static int size = 10;

		public RightBigPanel() {
			setLayout(new GridBagLayout());

			addLabels();

			setLabelSizes();
		}

		private void addLabels() {
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;

			c.gridx = 0;
			c.gridy = 0;
//			lblD0i.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
			add(lblD0i, c);

			c.gridx = 1;
			c.gridy = 0;
//			lbl0ik.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
			add(lbl0ik, c);

			c.gridx = 0;
			c.gridy = 1;
//			lblDj0.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
			add(lblDj0, c);

			c.gridx = 1;
			c.gridy = 1;
//			lbl0jn.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
			add(lbl0jn, c);

			c.gridx = 0;
			c.gridy = 2;
//			lblDji.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
			add(lblDji, c);

			c.gridx = 1;
			c.gridy = 2;
//			lbl1ik1jn.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
			add(lbl1ik1jn, c);
		}

		private void fillLabels() {
			LatexRenderer.render(fillTarget0Row, lblD0i, size);
			LatexRenderer.render(target0Constr, lbl0ik, size);

			LatexRenderer.render(fillSource0Col, lblDj0, size);
			LatexRenderer.render(source0Constr, lbl0jn, size);

			String string = fillRest + openingBracket + formDelete + ", \\\\ " + formInsert + ", \\\\" + formSubs
					+ openingBracket + costSubstitution + "&" + subsConstr + costIdentity + "&" + idConstr
					+ closingBracket + closingBracket;
			LatexRenderer.render(string, lblDji, size);
			LatexRenderer.render(restConstr, lbl1ik1jn, size);
		}

		private void fat() {
			LatexRenderer.render(LatexRenderer.fat(fillTarget0Row), lblD0i, size);
			LatexRenderer.render(LatexRenderer.fat(target0Constr), lbl0ik, size);

			LatexRenderer.render(LatexRenderer.fat(fillSource0Col), lblDj0, size);
			LatexRenderer.render(LatexRenderer.fat(source0Constr), lbl0jn, size);

			String string = fillRest + openingBracket + formDelete + ", \\\\ " + formInsert + ", \\\\" + formSubs
					+ openingBracket + costSubstitution + "&" + subsConstr + costIdentity + "&" + idConstr
					+ closingBracket + closingBracket;
			LatexRenderer.render(LatexRenderer.fat(string), lblDji, size);
			LatexRenderer.render(LatexRenderer.fat(restConstr), lbl1ik1jn, size);
		}

		private void setLabelSizes() {

			lblD0i.setPreferredSize(null);
			lbl0ik.setPreferredSize(null);
			lblDj0.setPreferredSize(null);
			lbl0jn.setPreferredSize(null);
			lblDji.setPreferredSize(null);
			lbl1ik1jn.setPreferredSize(null);

			fat();

			lblD0i.setPreferredSize(new Dimension(lblD0i.getPreferredSize().width + padding,
					lblD0i.getPreferredSize().height + padding));

			lbl0ik.setPreferredSize(new Dimension(lbl0ik.getPreferredSize().width + padding,
					lbl0ik.getPreferredSize().height + padding));

			lblDj0.setPreferredSize(new Dimension(lblDj0.getPreferredSize().width + padding,
					lblDj0.getPreferredSize().height + padding));

			lbl0jn.setPreferredSize(new Dimension(lbl0jn.getPreferredSize().width + padding,
					lbl0jn.getPreferredSize().height + padding));

			lblDji.setPreferredSize(new Dimension(lblDji.getPreferredSize().width + padding,
					lblDji.getPreferredSize().height + padding));

			lbl1ik1jn.setPreferredSize(new Dimension(lbl1ik1jn.getPreferredSize().width + padding,
					lbl1ik1jn.getPreferredSize().height + padding));

			fillLabels();
		}

		public void onResize(int width, int height) {
			width -= padding;
			height -= padding;

			size = 50;
			setLabelSizes();
			
			setPreferredSize(null);

			Dimension currentDimension = getPreferredSize();

			double widthRatio = (double) width / (double) (currentDimension.getWidth() + padding);
			double heightRatio = (double) height / (double) (currentDimension.getHeight() + padding);
			double ratio = Math.min(widthRatio, heightRatio);

			int maxSize = (int) (ratio * size);

			do {
				size = maxSize;
				setLabelSizes();
				maxSize--;
				currentDimension = getPreferredSize();
			} while (width < currentDimension.getWidth() + padding || height < currentDimension.getHeight() + padding);

			setPreferredSize(new Dimension(width, height));

			cellClicked(clickedJ, clickedI, wasAlreadyFilled);
		}

		/**
		 * marks the interesting details of the formula with colors such that its easier
		 * to understand, what's going on
		 */
		public void cellClicked(int j, int i, boolean wasAlreadyFilled) {
			this.wasAlreadyFilled = wasAlreadyFilled;
			fillLabels();
			clickedJ = j;
			clickedI = i;
			if (j == 0) {
				String string1 = LatexRenderer.fat(fillTarget0Row);
				LatexRenderer.render(string1, lblD0i, size);
				String string2 = LatexRenderer.fat(target0Constr);
				LatexRenderer.render(string2, lbl0ik, size);
			} else if (i == 0) {
				String string1 = LatexRenderer.fat(fillSource0Col);
				LatexRenderer.render(string1, lblDj0, size);
				String string2 = LatexRenderer.fat(source0Constr);
				LatexRenderer.render(string2, lbl0jn, size);
			} else if (i > 0 && j > 0 && wasAlreadyFilled) {
				String string1 = fillRest + openingBracket + formDelete + ", \\\\ " + formInsert + ", \\\\" + formSubs
						+ openingBracket + costSubstitution + "&" + subsConstr + costIdentity + "&" + idConstr
						+ closingBracket + closingBracket;
				LatexRenderer.render(LatexRenderer.fat(string1), lblDji, size);

				String string2 = LatexRenderer.fat(restConstr);
				LatexRenderer.render(string2, lbl1ik1jn, size);

			} else if (i > 0 && j > 0 && !wasAlreadyFilled) {
				String string1 = LatexRenderer.fat(fillRest) + openingBracket;
				string1 += LatexRenderer.green(formDelete) + ", \\\\ ";
				string1 += LatexRenderer.blue(formInsert) + ", \\\\";
				string1 += LatexRenderer.red(formSubs) + openingBracket;
				if (!controller.sameCharAt(j, i)) {
					string1 += LatexRenderer.red(costSubstitution) + "&" + subsConstr;
					string1 += costIdentity + "&" + idConstr;
				} else {
					string1 += costSubstitution + "&" + subsConstr;
					string1 += LatexRenderer.red(costIdentity) + "&" + idConstr;
				}
				string1 += closingBracket + closingBracket;

				LatexRenderer.render(string1, lblDji, size);

				String string2 = LatexRenderer.fat(restConstr);
				LatexRenderer.render(string2, lbl1ik1jn, size);

			}
		}

	}

	public static class RightSmallPanel extends RightPanel {

		private static final long serialVersionUID = -4296458538072933410L;

		private JLabel label = new JLabel();
		private String currentString = "";

		public static int size = 10;

		public RightSmallPanel() {
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			add(label, c);
		}

		@Override
		public void cellClicked(int j, int i, boolean wasAlreadyFilled) {
			this.clickedI = i;
			this.clickedJ = j;
			this.wasAlreadyFilled = wasAlreadyFilled;

			label.removeAll();

			if (j == 0) {
				currentString = fillTarget0Row;
			} else if (i == 0) {
				currentString = fillSource0Col;
			} else if (i > 0 && j > 0 && wasAlreadyFilled) {
				String costSubs;
				if (controller.sameCharAt(j, i)) {
					costSubs = costIdentity;
				} else {
					costSubs = costSubstitution;
				}

				String s = fillRest + openingBracket + formDelete + "\\text{,  }" + formInsert + "\\text{,  }"
						+ formSubs + costSubs + closingBracket;

				currentString = s;
			} else if (j > 0 && i > 0 && !wasAlreadyFilled) {
				String costSubs;
				if (controller.sameCharAt(j, i)) {
					costSubs = costIdentity;
				} else {
					costSubs = costSubstitution;
				}

				String s = fillRest + openingBracket + LatexRenderer.green(formDelete) + "\\text{,  }"
						+ LatexRenderer.blue(formInsert) + "\\text{,  }" + LatexRenderer.red(formSubs + costSubs)
						+ closingBracket;

				currentString = s;
			} else {
				currentString = "";
			}

			LatexRenderer.render(currentString, label, size);
		}

		public void onResize(int width, int height) {
			setPreferredSize(null);

			String s = fillRest + openingBracket + formDelete + "," + formInsert + "," + formSubs + costSubstitution
					+ closingBracket;

			size = 500;
			
			LatexRenderer.render(s, label, size);

			Dimension currentDimension = getPreferredSize();

			double widthRatio = (double) width / (double) (currentDimension.getWidth() + padding);
			double heightRatio = (double) height / (double) (currentDimension.getHeight() + padding);
			double ratio = Math.min(widthRatio, heightRatio);

			int maxSize = (int) (ratio * size);

			do {
				size = maxSize;
				LatexRenderer.render(s, label, size);
				maxSize--;
				currentDimension = getPreferredSize();
			} while (width < currentDimension.getWidth() + 5 * padding
					|| height < currentDimension.getHeight() + padding);

			cellClicked(clickedJ, clickedI, wasAlreadyFilled);

			setPreferredSize(new Dimension(width, height));
		}

	}

}
