package org.jalgo.module.unifikation.editor;


import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.jalgo.module.unifikation.Constants;


public class VKeyboard {
	public static JPanel getVKeyboard(final Editor TheEdit){
		JPanel panKeyboard = new JPanel();
		panKeyboard.setLayout(null);
		//panKeyboard.setBackground(new Color(123,123,123));
		
		//Tasten erstellen
		//////////////ZEILE1/////////////////////////////////////
		JButton btnAlpha = new JButton(Constants.ALPHA+"()");
		btnAlpha.setToolTipText("Taste: a");
		btnAlpha.setSize(Constants.ButtonSize);
		btnAlpha.setFont(Constants.ButtonFont);
		btnAlpha.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnAlpha.setLocation(0*(Constants.ButtonSize.width+Constants.ButtonGap), 0*(Constants.ButtonSize.height+Constants.ButtonGap));
		btnAlpha.addActionListener(ActionListeners.getButtonFunctionListener(TheEdit));
		
		JButton btnBeta = new JButton(Constants.BETA+"()");
		btnBeta.setToolTipText("Taste: b");
		btnBeta.setSize(Constants.ButtonSize);
		btnBeta.setFont(Constants.ButtonFont);
		btnBeta.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnBeta.setLocation(1*(Constants.ButtonSize.width+Constants.ButtonGap), 0*(Constants.ButtonSize.height+Constants.ButtonGap));
		btnBeta.addActionListener(ActionListeners.getButtonFunctionListener(TheEdit));
		
		JButton btnGamma = new JButton(Constants.GAMMA+"()");
		btnGamma.setToolTipText("Taste: g");
		btnGamma.setSize(Constants.ButtonSize);
		btnGamma.setFont(Constants.ButtonFont);
		btnGamma.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnGamma.setLocation(2*(Constants.ButtonSize.width+Constants.ButtonGap), 0*(Constants.ButtonSize.height+Constants.ButtonGap));
		btnGamma.addActionListener(ActionListeners.getButtonFunctionListener(TheEdit));
		
		JButton btnDelta = new JButton(Constants.DELTA+"()");
		btnDelta.setToolTipText("Taste: d");
		btnDelta.setSize(Constants.ButtonSize);
		btnDelta.setFont(Constants.ButtonFont);
		btnDelta.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnDelta.setLocation(3*(Constants.ButtonSize.width+Constants.ButtonGap)+15, 0*(Constants.ButtonSize.height+Constants.ButtonGap));
		btnDelta.addActionListener(ActionListeners.getButtonFunctionListener(TheEdit));
		
		JButton btnEpsilon = new JButton(Constants.EPSILON+"()");
		btnEpsilon.setToolTipText("Taste: e");
		btnEpsilon.setSize(Constants.ButtonSize);
		btnEpsilon.setFont(Constants.ButtonFont);
		btnEpsilon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnEpsilon.setLocation(4*(Constants.ButtonSize.width+Constants.ButtonGap)+15, 0*(Constants.ButtonSize.height+Constants.ButtonGap));
		btnEpsilon.addActionListener(ActionListeners.getButtonFunctionListener(TheEdit));
		
		JButton btnTheta = new JButton(Constants.THETA+"()");
		btnTheta.setToolTipText("Taste: t");
		btnTheta.setSize(Constants.ButtonSize);
		btnTheta.setFont(Constants.ButtonFont);
		btnTheta.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnTheta.setLocation(5*(Constants.ButtonSize.width+Constants.ButtonGap)+15, 0*(Constants.ButtonSize.height+Constants.ButtonGap));
		btnTheta.addActionListener(ActionListeners.getButtonFunctionListener(TheEdit));
		
		JButton btnNPair = new JButton("( , )");
		btnNPair.setToolTipText("Taste: p");
		btnNPair.setSize(Constants.ButtonSize);
		btnNPair.setFont(Constants.ButtonFont);
		btnNPair.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnNPair.setLocation(6*(Constants.ButtonSize.width+Constants.ButtonGap)+30, 0*(Constants.ButtonSize.height+Constants.ButtonGap));
		btnNPair.addActionListener(ActionListeners.getButtonNPairListener(TheEdit));
		
		JButton btnKomma = new JButton(",");
		btnKomma.setSize(Constants.ButtonSize);
		btnKomma.setFont(Constants.ButtonFont);
		btnKomma.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnKomma.setLocation(7*(Constants.ButtonSize.width+Constants.ButtonGap)+30, 0*(Constants.ButtonSize.height+Constants.ButtonGap));
		btnKomma.addActionListener(ActionListeners.getButtonKommaListener(TheEdit));
		
		//////////////ZEILE2/////////////////////////////////////
		JButton btnX = new JButton("x");
		btnX.setToolTipText("Taste: x");
		btnX.setSize(Constants.ButtonSize);
		btnX.setFont(Constants.ButtonFont);
		btnX.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnX.setLocation(0*(Constants.ButtonSize.width+Constants.ButtonGap), 1*(Constants.ButtonSize.height+Constants.ButtonGap));
		btnX.addActionListener(ActionListeners.getButtonLetterListener(TheEdit));
		
		JButton btnY = new JButton("y");
		btnY.setToolTipText("Taste: y");
		btnY.setSize(Constants.ButtonSize);
		btnY.setFont(Constants.ButtonFont);
		btnY.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnY.setLocation(1*(Constants.ButtonSize.width+Constants.ButtonGap), 1*(Constants.ButtonSize.height+Constants.ButtonGap));
		btnY.addActionListener(ActionListeners.getButtonLetterListener(TheEdit));
		
		JButton btnZ = new JButton("z");
		btnZ.setToolTipText("Taste: z");
		btnZ.setSize(Constants.ButtonSize);
		btnZ.setFont(Constants.ButtonFont);
		btnZ.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnZ.setLocation(2*(Constants.ButtonSize.width+Constants.ButtonGap), 1*(Constants.ButtonSize.height+Constants.ButtonGap));
		btnZ.addActionListener(ActionListeners.getButtonLetterListener(TheEdit));
		
		JButton btnU = new JButton("u");
		btnU.setToolTipText("Taste: u");
		btnU.setSize(Constants.ButtonSize);
		btnU.setFont(Constants.ButtonFont);
		btnU.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnU.setLocation(3*(Constants.ButtonSize.width+Constants.ButtonGap)+15, 1*(Constants.ButtonSize.height+Constants.ButtonGap));
		btnU.addActionListener(ActionListeners.getButtonLetterListener(TheEdit));
		
		JButton btnV = new JButton("v");
		btnV.setToolTipText("Taste: v");
		btnV.setSize(Constants.ButtonSize);
		btnV.setFont(Constants.ButtonFont);
		btnV.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnV.setLocation(4*(Constants.ButtonSize.width+Constants.ButtonGap)+15, 1*(Constants.ButtonSize.height+Constants.ButtonGap));
		btnV.addActionListener(ActionListeners.getButtonLetterListener(TheEdit));
		
		JButton btnW = new JButton("w");
		btnW.setToolTipText("Taste: w");
		btnW.setSize(Constants.ButtonSize);
		btnW.setFont(Constants.ButtonFont);
		btnW.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnW.setLocation(5*(Constants.ButtonSize.width+Constants.ButtonGap)+15, 1*(Constants.ButtonSize.height+Constants.ButtonGap));
		btnW.addActionListener(ActionListeners.getButtonLetterListener(TheEdit));
		
		JButton btnBackspace = new JButton("\u2190");
		btnBackspace.setToolTipText("Taste: \u2190");
		btnBackspace.setSize(Constants.ButtonSize);
		btnBackspace.setFont(new Font("Times New Roman",Font.PLAIN,14));
		btnBackspace.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnBackspace.setLocation(6*(Constants.ButtonSize.width+Constants.ButtonGap)+30, 1*(Constants.ButtonSize.height+Constants.ButtonGap));
		btnBackspace.addActionListener(ActionListeners.getButtonBackSpaceListener(TheEdit));
		
		JButton btnDelete = new JButton("Entf");
		btnDelete.setToolTipText("Taste: Entf");
		btnDelete.setSize(Constants.ButtonSize);
		btnDelete.setFont(Constants.ButtonFont);
		btnDelete.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnDelete.setLocation(7*(Constants.ButtonSize.width+Constants.ButtonGap)+30, 1*(Constants.ButtonSize.height+Constants.ButtonGap));
		btnDelete.addActionListener(ActionListeners.getButtonDelListener(TheEdit));
		
		//////////////ZEILE3/////////////////////////////////////
		JButton btnX1 = new JButton("<html>x<sub>1</sub></html>");
		btnX1.setToolTipText("Taste: 1");
		btnX1.setSize(Constants.ButtonSize);
		btnX1.setFont(Constants.ButtonFont);
		btnX1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnX1.setLocation(0*(Constants.ButtonSize.width+Constants.ButtonGap), 2*(Constants.ButtonSize.height+Constants.ButtonGap));
		btnX1.addActionListener(ActionListeners.getButtonX1234Listener(TheEdit));
		
		JButton btnX2 = new JButton("<html>x<sub>2</sub></html>");
		btnX2.setToolTipText("Taste: 2");
		btnX2.setSize(Constants.ButtonSize);
		btnX2.setFont(Constants.ButtonFont);
		btnX2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnX2.setLocation(1*(Constants.ButtonSize.width+Constants.ButtonGap), 2*(Constants.ButtonSize.height+Constants.ButtonGap));
		btnX2.addActionListener(ActionListeners.getButtonX1234Listener(TheEdit));
		
		JButton btnX3 = new JButton("<html>x<sub>3</sub></html>");
		btnX3.setToolTipText("Taste: 3");
		btnX3.setSize(Constants.ButtonSize);
		btnX3.setFont(Constants.ButtonFont);
		btnX3.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnX3.setLocation(2*(Constants.ButtonSize.width+Constants.ButtonGap), 2*(Constants.ButtonSize.height+Constants.ButtonGap));
		btnX3.addActionListener(ActionListeners.getButtonX1234Listener(TheEdit));
		
		JButton btnX4 = new JButton("<html>x<sub>4</sub></html>");
		btnX4.setToolTipText("Taste: 4");
		btnX4.setSize(Constants.ButtonSize);
		btnX4.setFont(Constants.ButtonFont);
		btnX4.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnX4.setLocation(3*(Constants.ButtonSize.width+Constants.ButtonGap)+15, 2*(Constants.ButtonSize.height+Constants.ButtonGap));
		btnX4.addActionListener(ActionListeners.getButtonX1234Listener(TheEdit));
		
		JButton btnX5 = new JButton("<html>x<sub>5</sub></html>");
		btnX5.setToolTipText("Taste: 5");
		btnX5.setSize(Constants.ButtonSize);
		btnX5.setFont(Constants.ButtonFont);
		btnX5.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnX5.setLocation(4*(Constants.ButtonSize.width+Constants.ButtonGap)+15, 2*(Constants.ButtonSize.height+Constants.ButtonGap));
		btnX5.addActionListener(ActionListeners.getButtonX1234Listener(TheEdit));
	
		JButton btnXN = new JButton("<html>x<sub>n</sub></html>");
		btnXN.setToolTipText("Taste: n");
		btnXN.setSize(Constants.ButtonSize);
		btnXN.setFont(Constants.ButtonFont);
		btnXN.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnXN.setLocation(5*(Constants.ButtonSize.width+Constants.ButtonGap)+15, 2*(Constants.ButtonSize.height+Constants.ButtonGap));
		btnXN.addActionListener(ActionListeners.getButtonXNListener(TheEdit));
		
		JButton btnCL = new JButton("(");
		btnCL.setToolTipText("Taste: (");
		btnCL.setSize(Constants.ButtonSize);
		btnCL.setFont(Constants.ButtonFont);
		btnCL.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnCL.setLocation(6*(Constants.ButtonSize.width+Constants.ButtonGap)+30, 2*(Constants.ButtonSize.height+Constants.ButtonGap));
		btnCL.addActionListener(ActionListeners.getButtonLetterListener(TheEdit));
		
		JButton btnCR = new JButton(")");
		btnCR.setToolTipText("Taste: )");
		btnCR.setSize(Constants.ButtonSize);
		btnCR.setFont(Constants.ButtonFont);
		btnCR.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnCR.setLocation(7*(Constants.ButtonSize.width+Constants.ButtonGap)+30, 2*(Constants.ButtonSize.height+Constants.ButtonGap));
		btnCR.addActionListener(ActionListeners.getButtonLetterListener(TheEdit));
		
		//Tasten anfügen
		panKeyboard.add(btnAlpha);
		panKeyboard.add(btnBeta);
		panKeyboard.add(btnGamma);
		panKeyboard.add(btnDelta);
		panKeyboard.add(btnEpsilon);
		panKeyboard.add(btnTheta);
		panKeyboard.add(btnNPair);
		panKeyboard.add(btnKomma);
		panKeyboard.add(btnX);
		panKeyboard.add(btnY);
		panKeyboard.add(btnZ);
		panKeyboard.add(btnU);
		panKeyboard.add(btnV);
		panKeyboard.add(btnW);
		panKeyboard.add(btnBackspace);
		panKeyboard.add(btnDelete);
		panKeyboard.add(btnX1);
		panKeyboard.add(btnX2);
		panKeyboard.add(btnX3);
		panKeyboard.add(btnX4);
		panKeyboard.add(btnX5);
		panKeyboard.add(btnXN);
		panKeyboard.add(btnCL);
		panKeyboard.add(btnCR);
		
		return panKeyboard;
	}

}
