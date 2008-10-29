package org.jalgo.module.app.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jalgo.main.util.Messages;
import org.jalgo.module.app.controller.SemiringController;
import org.jalgo.module.app.core.AvailableSemiRings;
import org.jalgo.module.app.core.SemiRing;

/**
 * This component is used for displaying a) the semiring selection at the start
 * of the module and b) the selected semiring during the graph editing and
 * algorithm mode. The component is displayed in the right panel at the top. The
 * Display mode depends on the enum <code>SemiringDisplay</code>.
 * 
 */
public class SemiringComponent extends JPanel {

	private static final long serialVersionUID = -1301296968702660788L;

	private SemiringController semiringController;

	private Map<String, SemiRing> semirings;
	private String[] semiringNameList;

	private JPanel editPanel;
	private BorderLayout editLayout;
	private JComboBox semiringBox;
	private JButton okayButton;
	private JLabel text;

	private Font normalFont, smallFont, boldFont;

	private boolean beamerMode;

	/**
	 * This enum is used for setting the displayMode of the semiring display.
	 * 
	 */
	private enum SemiringDisplay {
		TEMPLATE_SELECTION, DISPLAY
	}

	private SemiringDisplay displayMode;

	/**
	 * Instantiates the SemiRingComponent. This component is used for displaying
	 * a) the semiring selection at the start of the module and b) the selected
	 * semiring during the graph editing and algorithm mode. The component is
	 * displayed in the right panel at the top. The Display mode depends on the
	 * enum <code>SemiringDisplay</code>.
	 * 
	 * @param controller
	 *            the controller, through which access to the model is possible.
	 */
	public SemiringComponent(SemiringController controller) {

		semiringController = controller;
		setAlignmentX(Component.LEFT_ALIGNMENT);

		beamerMode = false;

		setDisplayMode(SemiringDisplay.TEMPLATE_SELECTION);
	}

	/**
	 * Initializes the dropdown box with a alphabetical sorted list of all
	 * Semirings.
	 */
	private void initSemiringDropDown() {
		semirings = AvailableSemiRings.getSemiRings();
		semiringNameList = new String[semirings.size()];

		int i = 0;
		for (SemiRing s : semirings.values()) {
			semiringNameList[i] = s.getName();
			i++;
		}
		Arrays.sort(semiringNameList);
		semiringBox = new JComboBox(semiringNameList);
	}

	private void initEditPanel() {
		initSemiringDropDown();

		editLayout = new BorderLayout();
		

		
		text = new JLabel(Messages.getString("app", "SemiringComponent.Text"));
		editPanel = new JPanel(editLayout);
		editPanel.setLayout(editLayout);

		okayButton = new JButton(Messages.getString(
				"app", "SemiringComponent.OK")); //$NON-NLS-1$ //$NON-NLS-2$

		editLayout.setVgap(7);
		editLayout.setHgap(20);
		editPanel.add(semiringBox, BorderLayout.CENTER);
		editPanel.add(okayButton, BorderLayout.EAST);
		editPanel.add(text, BorderLayout.NORTH);
		okayButton.addActionListener(new OkayButtonListener());
	}

	private void initViewPanel() {
		Dimension panelDimension = new Dimension(this.getWidth(),
				(int) (InterfaceConstants.formulaNormalFont(beamerMode)
						.getSize() * 4.5f));

		this.setMinimumSize(panelDimension);
		this.setPreferredSize(panelDimension);
		this.setMaximumSize(panelDimension);

		normalFont = InterfaceConstants.formulaNormalFont(beamerMode);
		smallFont = InterfaceConstants.formulaSmallFont(beamerMode);
		boldFont = InterfaceConstants.formulaBoldFont(beamerMode);

		repaint();
		revalidate();
	}

	private int paintString(String string, Graphics2D graphics, Font font,
			int offsetX, int offsetY) {
		graphics.setFont(font);

		graphics.drawString(string, offsetX, offsetY);
		return offsetX
				+ (int) graphics.getFontMetrics(font).getStringBounds(string,
						graphics).getWidth();
	}

	private int paintStringNormal(String string, Graphics2D graphics,
			int offsetX, int offsetY) {
		return paintString(string, graphics, normalFont, offsetX, offsetY);
	}

	private int paintStringBold(String string, Graphics2D graphics,
			int offsetX, int offsetY) {
		return paintString(string, graphics, boldFont, offsetX, offsetY);
	}

	private int paintStringSmall(String string, Graphics2D graphics,
			int offsetX, int offsetY) {
		return paintString(string, graphics, smallFont, offsetX, offsetY);
	}

	private void paintSemiringDefinition(Graphics2D graphics,
			SemiRing semiring, int offsetX, int offsetY, int lineHeight) {
		int lowerOffset, upperOffset;

		String[] symbolicRepresentation;
		String plusOperator, dotOperator, plusNeutral, dotNeutral;

		symbolicRepresentation = semiring.getTypeRepresentation();
		plusOperator = semiring.getPlusOperation().getSymbolicRepresentation();
		dotOperator = semiring.getDotOperation().getSymbolicRepresentation();
		plusNeutral = semiring.getPlusOperation().getNeutralElementDescription();
		dotNeutral = semiring.getDotOperation().getNeutralElementDescription();

		// (
		offsetX = paintStringNormal("(", graphics, offsetX, offsetY);

		// Paint symbolic representation of type
		offsetX = paintStringBold(symbolicRepresentation[0], graphics, offsetX,
				offsetY);
		lowerOffset = paintStringSmall(symbolicRepresentation[1], graphics,
				offsetX, offsetY - lineHeight / 4);
		upperOffset = paintStringSmall(symbolicRepresentation[2], graphics,
				offsetX, offsetY + lineHeight / 4);

		offsetX = Math.max(lowerOffset, upperOffset);
		offsetX = paintStringNormal(", ", graphics, offsetX, offsetY);

		// Paint operators
		offsetX = paintStringBold(plusOperator, graphics, offsetX, offsetY);
		offsetX = paintStringNormal(", ", graphics, offsetX, offsetY);

		offsetX = paintStringBold(dotOperator, graphics, offsetX, offsetY);
		offsetX = paintStringNormal(", ", graphics, offsetX, offsetY);

		// Paint neutrals elements
		offsetX = paintStringBold(plusNeutral, graphics, offsetX, offsetY);
		offsetX = paintStringNormal(", ", graphics, offsetX, offsetY);

		offsetX = paintStringBold(dotNeutral, graphics, offsetX, offsetY);

		// )
		offsetX = paintStringNormal(")", graphics, offsetX, offsetY);
	}

	private void paintView(Graphics2D graphics) {
		int fontHeight, lineHeight, offsetX, offsetY;
		SemiRing semiring;

		semiring = semiringController.getSemiRing();

		// Draw background
		graphics.setColor(this.getBackground());
		graphics.fill(new Rectangle(this.getSize()));

		// Initialize painting
		graphics.setColor(InterfaceConstants.formulaForegroundColor());
		graphics.setStroke(new BasicStroke(1));

		fontHeight = (int) graphics.getFontMetrics(normalFont).getStringBounds(
				"D", graphics).getHeight();
		lineHeight = (int) (fontHeight * 1.5);
		offsetX = lineHeight / 2;
		offsetY = 0;

		graphics.setFont(normalFont);

		// Paint name of Semiring
		offsetY += lineHeight;
		graphics.drawString(semiring.getName(), offsetX, offsetY);

		// Paint description of Semiring
		/*
		 * offsetY += lineHeight; graphics.drawString(semiring.getDescription(),
		 * offsetX, offsetY);
		 */

		// Paint formal definition of Semiring
		offsetY += lineHeight;
		paintSemiringDefinition(graphics, semiring, offsetX, offsetY,
				lineHeight);
	}

	public void paint(Graphics graphics) {
		Graphics2D g;

		g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		if (displayMode == SemiringDisplay.TEMPLATE_SELECTION)
			super.paint(graphics);
		else
			this.paintView(g);
	}

	/**
	 * Gets the current DisplayMode of the Semiring (either TEMPLATE_SELECTION
	 * or DISPLAY).
	 * 
	 * @return the mode.
	 */
	public SemiringDisplay getDisplayMode() {
		return displayMode;
	}

	/**
	 * Sets the current DisplayMode of the Semiring to either TEMPLATE_SELECTION
	 * or DISPLAY.
	 * 
	 * @param mode
	 */
	public void setDisplayMode(SemiringDisplay mode) {
		this.displayMode = mode;

		switch (this.displayMode) {
		case TEMPLATE_SELECTION:
			if (editPanel == null)
				initEditPanel();

			add(editPanel);
			break;

		case DISPLAY:
			initViewPanel();

			if (editPanel != null)
				remove(editPanel);

			break;
		}
	}

	/**
	 * @return the Semiring depending on selection in drop-down box.
	 */
	public SemiRing getSelectedSemiring() {
		String semiringName = semiringNameList[semiringBox.getSelectedIndex()];

		for (SemiRing s : semirings.values()) {
			if (s.getName().equals(semiringName)) {
				return s;
			}
		}

		return null;
	}

	public void updateDisplay(SemiRing semiring) {
		if (semiring != null) {
			setDisplayMode(SemiringDisplay.DISPLAY);

			repaint();
		} else {
			setDisplayMode(SemiringDisplay.TEMPLATE_SELECTION);
		}
	}

	public void updateBeamerMode(boolean beamerMode) {
		this.beamerMode = beamerMode;
		this.boldFont = InterfaceConstants.formulaBoldFont(beamerMode);
		this.normalFont = InterfaceConstants.formulaNormalFont(beamerMode);
		this.smallFont = InterfaceConstants.formulaSmallFont(beamerMode);
		initViewPanel();
	}

	class OkayButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			semiringController.setSemiRing(getSelectedSemiring());
		}
	}

}