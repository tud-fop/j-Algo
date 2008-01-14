package org.jalgo.module.hoare.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import org.jalgo.module.hoare.gui.actions.ReparseCNullProg;
import org.jalgo.module.hoare.model.VerificationFormula;

public class SourceView extends JPanel implements Observer, GUIConstants {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Text-Panel which displays the Sourcecode
	 * 
	 * @author Peter
	 * 
	 */

	private JTextPane content;

	private JToolBar toolbar;

	private GuiControl gui;

	private JLabel file;

	Object highlight;

	private HighlightPainter painter;

	public SourceView(GuiControl gui) {
		super();

		gui.addObserver(this);
		this.removeAll();
		this.setLayout(new BorderLayout());
		this.gui = gui;
		JScrollPane scrollPane = new JScrollPane();
		content = new JTextPane();
		content.setFont(STANDARD_FONT);
		content.setEditable(true);
		JViewport viewp = new JViewport();
		viewp.setView(content);
		scrollPane.setViewport(viewp);
		this.add(scrollPane, BorderLayout.CENTER);

		toolbar = new JToolBar();
		toolbar.add(new ReparseCNullProg(gui, content));
		toolbar.setFloatable(false);

		file = new JLabel();
		file.setFont(STANDARD_FONT);

		this.add(toolbar, BorderLayout.NORTH);

		painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);

		try {
			highlight = content.getHighlighter().addHighlight(0, 0, painter);

		} catch (BadLocationException e) {

		}

	}

	public void update(Observable arg0, Object arg1) {
		// vorläufig mit replace, später mit style irgendwie ;)
		content.setText(gui.getCode());
		file.setText(gui.getFileName());

		if (gui.getActiveNode() >= 0) {
			VerificationFormula tmp = gui.getVF(gui.getActiveNode());
			try {

				int idxStart = gui.getIndex(tmp.getCodeStart());
				int idxEnd = 1 + gui.getIndex(tmp.getCodeEnd());

				// workaround, damit bei \r\n \r beachtet wird (wirds nämlich
				// nich!)

				if (System.getProperty("line.separator").equals("\r\n")) {
					idxStart -= (tmp.getCodeStart().getHeight() - 1);
					idxEnd -= (tmp.getCodeEnd().getHeight() - 1);
				}

				content.getHighlighter().removeAllHighlights();
				content.getHighlighter()
						.addHighlight(idxStart, idxEnd, painter);

			} catch (BadLocationException e) {

			}
		}
	}
	/**
	 * sets whether beamer mode is active or inactive
	 * 
	 * @author Markus
	 * @param b
	 */
	public void setBeamer(boolean b) {

		if (b) {
			content.setFont(BEAMER_FONT);
			file.setFont(BEAMER_FONT);
		} else {
			content.setFont(STANDARD_FONT);
			file.setFont(STANDARD_FONT);
		}

	}

}
