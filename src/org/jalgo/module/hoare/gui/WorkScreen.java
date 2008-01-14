package org.jalgo.module.hoare.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 * The main graphical Environment
 * 
 * @author Peter, Markus
 * 
 */

public class WorkScreen extends JPanel implements GUIConstants {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HTMLTree htmltree;

	private RuleView ruleview;

	private CondInput condinput;

	private SourceView sourceview;

	private EventLog eventlog;

	private Splitter sourceMiniSplitter;

	private Splitter treeRuleSplitter;

	private Splitter mainSplitter;

	private Splitter sourceLogSplitter;

	private JPanel panelEast;

	public WorkScreen(GuiControl gCtrl) {

		super();
		this.htmltree = new HTMLTree(gCtrl);
		this.ruleview = new RuleView(gCtrl);
		this.condinput = new CondInput(gCtrl);
		this.sourceview = new SourceView(gCtrl);
		this.eventlog = new EventLog(gCtrl);

		BorderLayout layout = new BorderLayout();
		this.setLayout(layout);
		this.setSize(800, 600);

		this.panelEast = new JPanel();
		panelEast.setLayout(new BorderLayout());
		sourceLogSplitter = new Splitter(JSplitPane.VERTICAL_SPLIT,
				this.sourceview, this.eventlog);
		panelEast.add(sourceLogSplitter);

		treeRuleSplitter = new Splitter(JSplitPane.HORIZONTAL_SPLIT,
				this.htmltree, this.panelEast);
		sourceMiniSplitter = new Splitter(JSplitPane.HORIZONTAL_SPLIT,
				this.ruleview, this.condinput);
		mainSplitter = new Splitter(JSplitPane.VERTICAL_SPLIT,
				treeRuleSplitter, sourceMiniSplitter);
		this.add(mainSplitter);

		mainSplitter.setDividerLocation(0.77);
		mainSplitter.setResizeWeight(0.77);
		sourceMiniSplitter.setDividerLocation(0.6);
		sourceMiniSplitter.setResizeWeight(0.6);
		treeRuleSplitter.setDividerLocation(0.8);
		treeRuleSplitter.setResizeWeight(0.8);
		sourceLogSplitter.setDividerLocation(0.8);
		sourceLogSplitter.setResizeWeight(0.8);

	}
	/**
	 * sets whether beamer mode is active or inactive
	 * 
	 * @author Markus
	 * @param b
	 */
	public void setBeamer(boolean b) {

		if (b) {

			mainSplitter.setDividerLocation(0.77);
			mainSplitter.setResizeWeight(0.77);
			sourceMiniSplitter.setDividerLocation(0.6);
			sourceMiniSplitter.setResizeWeight(0.6);
			treeRuleSplitter.setDividerLocation(0.8);
			treeRuleSplitter.setResizeWeight(0.8);
			sourceLogSplitter.setDividerLocation(0.8);
			sourceLogSplitter.setResizeWeight(0.8);
		} else {
			mainSplitter.setDividerLocation(0.77);
			mainSplitter.setResizeWeight(0.77);
			sourceMiniSplitter.setDividerLocation(0.6);
			sourceMiniSplitter.setResizeWeight(0.6);
			treeRuleSplitter.setDividerLocation(0.8);
			treeRuleSplitter.setResizeWeight(0.8);
			sourceLogSplitter.setDividerLocation(0.8);
			sourceLogSplitter.setResizeWeight(0.8);

		}
		condinput.setBeamer(b);
		sourceview.setBeamer(b);
		eventlog.setBeamer(b);
		htmltree.setBeamer(b);
		ruleview.setBeamer(b);
	}

}