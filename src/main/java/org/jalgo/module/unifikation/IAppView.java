package org.jalgo.module.unifikation;

import javax.swing.JComponent;

/**
 * Interface for different Views (Editor, Algo...)
 *
 */
public interface IAppView{
	public void setApplication(Application app);
	public void setContentPane(JComponent contentPane);
	/**
	 * Refreshes the view where set is shown
	 */
	public void updateSetView();
	/**
	 * Sets the help text
	 * @param helpText Text to set
	 */
	public void setHelpText(String helpText);
}