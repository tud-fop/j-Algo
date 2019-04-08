package org.jalgo.module.levenshtein.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jalgo.main.gui.JAlgoWindow;
import org.jalgo.module.levenshtein.Consts;
import org.jalgo.module.levenshtein.gui.GuiController;
import org.jalgo.module.levenshtein.gui.events.Abort;
import org.jalgo.module.levenshtein.gui.events.InputListener;
import org.jalgo.module.levenshtein.gui.events.ResizeListener;
import org.jalgo.module.levenshtein.gui.events.StartAction;

public class SetUpPanel extends JPanel implements ResizeComponent {

	private JAlgoWindow appWin;

	private JLabel lblSource;
	private JTextField inputSource;
	private JLabel lblTarget;
	private JTextField inputTarget;

	private JLabel lblCostFunction;
	private JLabel lblDeletion;
	private JTextField inputDeletion;
	private JLabel lblInsertion;
	private JTextField inputInsertion;
	private JLabel lblSubstitution;
	private JTextField inputSubstitution;
	private JLabel lblIdentity;
	private JTextField inputIdentity;

	private JButton btnAbort;
	private JButton btnStart;

	private JPanel contentPanel;
	private JPanel sourcePanel;
	private JPanel targetPanel;
	private JPanel costInputs;
	private JPanel buttonPanel;

	private int hgap = 0;
	private int vgap = 0;

	private int columnsWord = 20;
	private int columnsCostFunction = 5;

	private boolean designDebug = false;

	public void init(Abort abort, StartAction startAction) {

		setLayout(new GridBagLayout());

		contentPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// init the Components of the View
		lblSource = new JLabel(GuiController.getString("input.source"));
		lblTarget = new JLabel(GuiController.getString("input.target"));
		lblCostFunction = new JLabel(GuiController.getString("input.costfunction"));
		lblDeletion = new JLabel(GuiController.getString("input.deletion"));
		lblInsertion = new JLabel(GuiController.getString("input.insertion"));
		lblSubstitution = new JLabel(GuiController.getString("input.substitution"));
		lblIdentity = new JLabel(GuiController.getString("input.identity"));

		inputSource = new JTextField(columnsWord);
		inputTarget = new JTextField(columnsWord);
		inputDeletion = new JTextField(Integer.toString(Consts.deletionCosts), columnsCostFunction);
		inputDeletion.addKeyListener(new InputListener(inputDeletion));
		inputInsertion = new JTextField(Integer.toString(Consts.insertionCosts), columnsCostFunction);
		inputInsertion.addKeyListener(new InputListener(inputInsertion));
		inputSubstitution = new JTextField(Integer.toString(Consts.substitutionCosts), columnsCostFunction);
		inputSubstitution.addKeyListener(new InputListener(inputSubstitution));
		inputIdentity = new JTextField(Integer.toString(Consts.identityCosts), columnsCostFunction);
		inputIdentity.addKeyListener(new InputListener(inputIdentity));

		btnAbort = new JButton(GuiController.getString("button.abort"));
		btnAbort.addActionListener(abort);
		btnStart = new JButton(GuiController.getString("button.start"));
		btnStart.addActionListener(startAction);

		// padding object
		Insets insets = new Insets(0, 0, 0, 0);
		c.insets = insets;
		c.fill = GridBagConstraints.HORIZONTAL;

		// create a component for the source word label and input field
		sourcePanel = new JPanel(new BorderLayout(hgap, vgap));
		sourcePanel.add(lblSource, BorderLayout.WEST);
		sourcePanel.add(inputSource, BorderLayout.EAST);

		// add the component to the GridBagLayout as first component
		c.gridx = 0;
		c.gridy = 0;
		contentPanel.add(sourcePanel, c);

		// create a component for the target word label and input field
		targetPanel = new JPanel(new BorderLayout(hgap, vgap));
		targetPanel.add(lblTarget, BorderLayout.WEST);
		targetPanel.add(inputTarget, BorderLayout.EAST);

		// add the component to the GridBagLayout as second component
		c.gridx = 0;
		c.gridy = 1;
		contentPanel.add(targetPanel, c);

		// add the cost function label as third component to the GridBagLayout
		// here some padding to the top is added
		insets.top = 20;
		c.gridx = 0;
		c.gridy = 2;
		contentPanel.add(lblCostFunction, c);
		insets.top = 0;

		// create a new panel for the cost function input fields
		costInputs = new JPanel(new GridBagLayout());

		// add the deletion label
		c.gridx = 0;
		c.gridy = 0;
		costInputs.add(lblDeletion, c);

		// add the deletion input with padding
		insets.right = 20;
		c.gridx = 1;
		c.gridy = 0;
		costInputs.add(inputDeletion, c);
		insets.right = 0;

		// add the insertion label
		c.gridx = 2;
		c.gridy = 0;
		costInputs.add(lblInsertion, c);

		// add the insertion input
		c.gridx = 3;
		c.gridy = 0;
		costInputs.add(inputInsertion, c);

		// add the substitution label
		c.gridx = 0;
		c.gridy = 1;
		costInputs.add(lblSubstitution, c);

		// add the substitution input with padding
		insets.right = 20;
		c.gridx = 1;
		c.gridy = 1;
		costInputs.add(inputSubstitution, c);
		insets.right = 0;

		// add the identity label
		c.gridx = 2;
		c.gridy = 1;
		costInputs.add(lblIdentity, c);

		// add the identity input
		c.gridx = 3;
		c.gridy = 1;
		costInputs.add(inputIdentity, c);

		// add all the cost function input fields as fourth component to the
		// content GridBagLayout
		c.gridx = 0;
		c.gridy = 3;
		contentPanel.add(costInputs, c);

		// create a component for the buttons
		buttonPanel = new JPanel(new BorderLayout());
//		buttonPanel.add(btnAbort, BorderLayout.WEST);
		buttonPanel.add(btnStart, BorderLayout.EAST);

		// add the button panel to fifth component
		insets.top = 20;
		c.gridx = 0;
		c.gridy = 4;
		contentPanel.add(buttonPanel, c);
		insets.top = 0;

		// add the GridBagLayout to the contentPane centered.
		// add(contentPanel, BorderLayout.CENTER);
		c.gridx = 0;
		c.gridy = 0;
		add(contentPanel, c);

		if (designDebug) {
			contentPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
			lblSource.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
			lblTarget.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
			lblCostFunction.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
			lblDeletion.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
			lblInsertion.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
			lblSubstitution.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
			lblIdentity.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));

			inputSource.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
			inputTarget.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
			inputDeletion.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));

			inputInsertion.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));

			inputSubstitution.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));

			inputIdentity.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));

			btnStart.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));

			sourcePanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
			targetPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
			costInputs.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
			buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		}

		addComponentListener(new ResizeListener(this));
	}

	/**
	 * parse the deletion value from the input field and return it
	 * 
	 * @return the deletion penalty to use
	 */
	public int getDeletion() {
		int d = 1;

		d = Integer.parseInt(inputDeletion.getText());

		return d;
	}

	/**
	 * parse the insertion value from the input field and return it
	 * 
	 * @return the insertion penalty to use
	 */
	public int getInsertion() {
		int i = 1;

		i = Integer.parseInt(inputInsertion.getText());

		return i;
	}

	/**
	 * parse the substitution value from the input field and return it
	 * 
	 * @return the substitution penalty to use
	 */
	public int getSubstitution() {
		int s = 1;

		s = Integer.parseInt(inputSubstitution.getText());

		return s;
	}

	/**
	 * parse the identity value from the input field and return it
	 * 
	 * @return the identity penalty to use
	 */
	public int getIdentity() {
		int i = 0;

		i = Integer.parseInt(inputIdentity.getText());

		return i;
	}

	/**
	 * 
	 * @return the source word
	 */
	public String getSource() {
		return inputSource.getText();
	}

	/**
	 * 
	 * @return the target word
	 */
	public String getTarget() {
		return inputTarget.getText();
	}

	public void onResized() {
		Dimension dimWindow = getSize();
		Dimension dimContent = contentPanel.getSize();

		double sizeHeight = (dimWindow.getHeight() * 2.0 / 3.0) / 12.0;
		double sizeWidth = (dimWindow.getWidth() * 2.0 / 3.0) / 33.0;
		int size = (int) Math.min(sizeHeight, sizeWidth);

		Font lblFont = lblSource.getFont();
		lblFont = new Font(lblFont.getFontName(), lblFont.getStyle(), size);

		setFont(lblSource, lblFont);
		setFont(lblTarget, lblFont);
		setFont(lblCostFunction, lblFont);
		setFont(lblDeletion, lblFont);
		setFont(lblInsertion, lblFont);
		setFont(lblSubstitution, lblFont);
		setFont(lblIdentity, lblFont);

		Font inputFont = inputSource.getFont();
		inputFont = new Font(inputFont.getFontName(), inputFont.getStyle(), size);

		setFont(inputSource, inputFont);
		setFont(inputTarget, inputFont);
		setFont(inputDeletion, inputFont);
		setFont(inputInsertion, inputFont);
		setFont(inputSubstitution, inputFont);
		setFont(inputIdentity, inputFont);

		Font btnFont = btnStart.getFont();
		btnFont = new Font(btnFont.getFontName(), btnFont.getStyle(), size);

		setFont(btnStart, btnFont);


		contentPanel.repaint();
		contentPanel.revalidate();

	}

	private void setFont(JComponent comp, Font font) {
		comp.setFont(font);
		comp.revalidate();
		comp.repaint();
	}

}
