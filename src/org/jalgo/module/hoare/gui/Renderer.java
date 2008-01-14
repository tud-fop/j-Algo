package org.jalgo.module.hoare.gui;

import javax.swing.text.JTextComponent;

import org.jalgo.module.hoare.model.VerificationFormula;

public interface Renderer {
	public void render(VerificationFormula vf, JTextComponent component);
}
