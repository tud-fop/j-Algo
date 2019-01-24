package org.jalgo.module.em.gui.output.diagram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.jalgo.main.util.Messages;
import org.jalgo.module.em.control.LogState;
import org.math.plot.PlotPanel;
import org.math.plot.canvas.Plot3DCanvas;
import org.math.plot.canvas.PlotCanvas;


/**
 * 	Controls the shown Graphs and command the graph factory.
 * 
 * 	@author Tom Schumann
 *	@version 1.0
 *
 */
public class DiagramPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = -9192842176882009609L;
	private DiagramFactory diafactory;
	
    private JPanel toolBar = new JPanel();
    private JButton bStandartZoom = new JButton();
    private JButton bIterationToLiklihood = new JButton();
    private JButton bIterationToD = new JButton();
    private JButton bIterationToH = new JButton();
    private JButton bIterationToP = new JButton();
    private JButton bIterationToPSingle = new JButton();
    private JButton bTwoCoinsPlot = new JButton();
    private PlotPanel[] usedplot;
    private JPanel legende = new JPanel();
    private Map<Integer,PlotPanel[]> plotHistory;
    private Map<Integer,PlotPanel[]> plotHistorylog;
    private Map<Integer,Boolean> scalesHistory;
    
    private BorderLayout layout = new BorderLayout();
    private int controlPlots;
    private LogState logState;
    private boolean usedScales;
    
    private double phi;
    private double theta;

	/**
	 * Build the Panel and its factory.
	 * @param logstate The <code>LogState</code>  that provides the Data that shall be visualized.
	 */
	public DiagramPanel(org.jalgo.module.em.control.LogState logstate){
		init();
		this.logState = logstate;
		this.plotHistory = new TreeMap<Integer, PlotPanel[]>();
		this.plotHistorylog = new TreeMap<Integer, PlotPanel[]>();
		this.scalesHistory = new TreeMap<Integer, Boolean>();
		this.diafactory = new DiagramFactory(logstate);
		this.controlPlots = 0;
		if(!logstate.activate3D()){
			remove(this.bTwoCoinsPlot);
			this.bTwoCoinsPlot.setVisible(false);
		}
		this.usedScales = this.logState.isLogarithmicLikelihoodRepresentationEnabled();
		this.scalesHistory.put(this.logState.getStepCount(), this.usedScales);
		if(!this.logState.isLogarithmicLikelihoodRepresentationEnabled()){
			this.plotHistory.put(logstate.getStepCount(), this.diafactory.draw());
		}else
		{
			this.plotHistorylog.put(logstate.getStepCount(), this.diafactory.draw());
		}
		this.usedplot = this.plotHistory.get(logstate.getStepCount());
	}
	
	/**
	 * Build the basics of this panel
	 */
	public void init(){
		setLayout(this.layout);
		add(this.toolBar, BorderLayout.NORTH);
		
		this.toolBar.add(this.bStandartZoom);
		this.toolBar.add(this.bIterationToLiklihood);
		this.toolBar.add(this.bTwoCoinsPlot);
		
		this.bIterationToLiklihood.addActionListener(this);
		this.bIterationToD.addActionListener(this);
		this.bIterationToH.addActionListener(this);
		this.bIterationToP.addActionListener(this);
		this.bIterationToPSingle.addActionListener(this);
		this.bStandartZoom.addActionListener(this);
		this.bTwoCoinsPlot.addActionListener(this);
		
		this.bStandartZoom.setText(Messages.getString("em", "DiagramPanel.StandartZoom"));
		this.bIterationToLiklihood.setText(Messages.getString("em", "DiagramPanel.l"));
		this.bIterationToD.setText(Messages.getString("em", "DiagramPanel.d"));
		this.bIterationToH.setText(Messages.getString("em", "DiagramPanel.h"));
		this.bIterationToP.setText(Messages.getString("em", "DiagramPanel.p"));
		this.bIterationToPSingle.setText(Messages.getString("em", "DiagramPanel.psingle"));
		this.bTwoCoinsPlot.setText(Messages.getString("em", "DiagramPanel.twoCoinsPlot"));
		
		this.bStandartZoom.setToolTipText(Messages.getString("em", "DiagramPanel.StandartZoom"));
		this.bIterationToLiklihood.setToolTipText(Messages.getString("em", "DiagramPanel.l"));
		this.bIterationToD.setToolTipText(Messages.getString("em", "DiagramPanel.d"));
		this.bIterationToH.setToolTipText(Messages.getString("em", "DiagramPanel.h"));
		this.bIterationToP.setToolTipText(Messages.getString("em", "DiagramPanel.p"));
		this.bIterationToPSingle.setToolTipText(Messages.getString("em", "DiagramPanel.psingle"));
		this.bTwoCoinsPlot.setToolTipText(Messages.getString("em", "DiagramPanel.twoCoinsPlot"));
		
		
		this.toolBar.add(this.bStandartZoom);
		this.toolBar.add(this.bIterationToLiklihood);
		this.toolBar.add(this.bIterationToD);
		this.toolBar.add(this.bIterationToH);
		this.toolBar.add(this.bIterationToP);
		this.toolBar.add(this.bIterationToPSingle);
		this.toolBar.add(this.bTwoCoinsPlot);
//		Dimension maximumSize = new Dimension();
//		maximumSize.width = 100;
//		maximumSize.height = 50;
//		this.legende.setAutoscrolls(true);
//		this.legende.setMinimumSize(maximumSize);
		this.legende.setBackground(Color.WHITE);
//		this.legende.setMaximumSize(maximumSize);
		this.legende.setVisible(true);
		this.setAutoscrolls(true);
		this.add(this.legende, BorderLayout.SOUTH);
	}
	
	/**
	 * Updates the panel, to apply step or logarithmicLikelihoodRepresentation changes.	
	 */
	public void upDate(){
		if (this.usedplot != null) {
			if (this.controlPlots == 5) {
				this.saveViewCoords();
			}
		}
		
		//Makes the d and h diagram synchron to the singleSteps.
		int shift = 0;
		if (((this.controlPlots == 1) && (this.logState.getSingleStepCount() > 0))
				|| ((this.controlPlots == 2) && (this.logState.getSingleStepCount() > 1))) {
			shift = 1;
			logState.stepForward();
		}
		
		if(!this.logState.isLogarithmicLikelihoodRepresentationEnabled()){
			if(this.plotHistory.containsKey(this.logState.getStepCount())){
				this.usedplot = this.plotHistory.get(this.logState.getStepCount());
			}
			else {
				this.usedplot = this.diafactory.draw();
				this.plotHistory.put(this.logState.getStepCount(), this.usedplot);
				this.scalesHistory.put(this.logState.getStepCount(), this.logState.isLogarithmicLikelihoodRepresentationEnabled());
			}
		}
		else{
			if(this.plotHistorylog.containsKey(this.logState.getStepCount())){
				this.usedplot = this.plotHistorylog.get(this.logState.getStepCount());
			}
			else{
				this.usedplot = this.diafactory.draw();
				this.plotHistorylog.put(this.logState.getStepCount(), this.usedplot);
				this.scalesHistory.put(this.logState.getStepCount(), this.logState.isLogarithmicLikelihoodRepresentationEnabled());
			}
		}
		this.change();
		if (shift == 1) {
			logState.stepBackward();
		}
	}
	
	/**
	 * Changes the Diagram and Legend, if the step was changed or a different Diagram type chosen.
	 */
	public void change(){
		if (this.layout.getLayoutComponent(BorderLayout.CENTER) != null){
			this.remove(this.layout.getLayoutComponent(BorderLayout.CENTER));
		}
		if (this.layout.getLayoutComponent(BorderLayout.SOUTH) != null){
			this.remove(this.layout.getLayoutComponent(BorderLayout.SOUTH));
		}
		this.legende.removeAll();
		if (this.controlPlots == 5) {
			Plot3DCanvas canvas = ((Plot3DCanvas) usedplot[5].plotCanvas);
			((org.math.plot.render.Projection3D) ((org.math.plot.render.AWTDrawer3D) canvas.getDraw()).getProjection()).setView(theta, phi);
		} 
		this.add(this.usedplot[this.controlPlots].plotCanvas, BorderLayout.CENTER);	
		this.legende.add(this.usedplot[this.controlPlots].plotLegend);
		if((this.controlPlots < 4) && (this.controlPlots > 0)) {
			if(this.logState.getEvents().size() * this.logState.getP0Count() <= 40) {
				this.add(this.legende, BorderLayout.SOUTH);
			}
		}
		if ((this.controlPlots == 4) && (this.logState.getSingleEvents().size() * this.logState.getP0Count() <= 40)) {
			this.add(this.legende, BorderLayout.SOUTH);
		}
		if((this.controlPlots == 0) || (this.controlPlots == 5)) {
			this.add(this.legende, BorderLayout.SOUTH);
		}
		this.updateUI();
	}

	/**
	 * Controls the buttons in this panel and management the events
	 * @param ActionEvent Event that
	 */
	@Override
	public void actionPerformed(ActionEvent arg) {
		if(arg.getSource() == this.bStandartZoom){
//			System.out.println("control Plots : "+controlPlots);
			PlotPanel panel = usedplot[controlPlots];
			PlotCanvas canvas = panel.plotCanvas;
			canvas.resetBase();
		}
		if(arg.getSource() == this.bIterationToLiklihood){
			this.controlPlots = 0;
			this.upDate();
		}
		if(arg.getSource() == this.bIterationToD){
			this.controlPlots = 1;
			this.upDate();
		}
		if(arg.getSource() == this.bIterationToH){
			this.controlPlots = 2;
			this.upDate();
		}
		if(arg.getSource() == this.bIterationToP){
			this.controlPlots = 3;
			this.upDate();
		}
		if(arg.getSource() == this.bIterationToPSingle){
			this.controlPlots = 4;
			this.upDate();
		}
		if(arg.getSource() == this.bTwoCoinsPlot){			
			this.controlPlots = 5;
			this.upDate();
		}
	}

	/**
	 * Saves the current view angle of the 3D Graph.
	 */
	public void saveViewCoords() {
		if (usedplot[5] != null){
			Plot3DCanvas canvas = ((Plot3DCanvas) usedplot[5].plotCanvas);
			phi = ((org.math.plot.render.Projection3D) ((org.math.plot.render.AWTDrawer3D) canvas.getDraw()).getProjection()).getPhi();
			theta = ((org.math.plot.render.Projection3D) ((org.math.plot.render.AWTDrawer3D) canvas.getDraw()).getProjection()).getTheta();
		}
		
	}
	
	/**
	 * Shrinks the buttons, if the JPanel is smaller than 1070px,
	 * to make them fit into it.
	 * @author Kilian Gebhardt
	 */
	public void resetButtons() {
	if (this.getWidth() > 1070) {
			this.bStandartZoom.setText(Messages.getString("em", "DiagramPanel.StandartZoom"));
			this.bIterationToLiklihood.setText(Messages.getString("em", "DiagramPanel.l"));
			this.bIterationToD.setText(Messages.getString("em", "DiagramPanel.d"));
			this.bIterationToH.setText(Messages.getString("em", "DiagramPanel.h"));
			this.bIterationToP.setText(Messages.getString("em", "DiagramPanel.p"));
			this.bIterationToPSingle.setText(Messages.getString("em", "DiagramPanel.psingle"));
			this.bTwoCoinsPlot.setText(Messages.getString("em", "DiagramPanel.twoCoinsPlot"));
		} else {
			this.bStandartZoom.setText(Messages.getString("em", "DiagramPanel.StandartZoom"));
			this.bIterationToLiklihood.setText(Messages.getString("em", "DiagramPanel.l.short"));
			this.bIterationToD.setText(Messages.getString("em", "DiagramPanel.d.short"));
			this.bIterationToH.setText(Messages.getString("em", "DiagramPanel.h.short"));
			this.bIterationToP.setText(Messages.getString("em", "DiagramPanel.p.short"));
			this.bIterationToPSingle.setText(Messages.getString("em", "DiagramPanel.psingle.short"));
			this.bTwoCoinsPlot.setText(Messages.getString("em", "DiagramPanel.twoCoinsPlot.short"));
		}
	}	
	
}
