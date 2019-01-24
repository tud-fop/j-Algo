package org.jalgo.module.em.gui.output.diagram;


import static java.lang.Math.PI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;

import org.jalgo.main.util.Messages;
import org.jalgo.module.em.control.LogState;
import org.math.plot.Plot2DPanel;
import org.math.plot.Plot3DPanel;
import org.math.plot.PlotPanel;
import org.math.plot.utils.Array;
/**
 * The Factory builds groups of graphs to show the functions of the Emalgo
 * @author Tom Schumann
 * @version 1.0
 *
 */
public class DiagramFactory{

	private org.jalgo.module.em.control.LogState logState;
	
	private double[] xRangeFor3D;
    private double[] yRangeFor3D;
    private double[] xRangeFor3DLog;
    private double[] yRangeFor3DLog;
    
    private double[] xRangeFor2D;
    private double[] xRangeFor2DDots;
    private double[] xRangeFor2D_d_h;
    private double[] xRangeFor2D_d_hDots;
    private double[] axisSlicing;
    
    private double[][] mountains;
    private double[][] mountains3D;
    
	private Color[] colors = {Color.BLUE,Color.RED,Color.CYAN,
			Color.MAGENTA,Color.ORANGE,Color.PINK,
			Color.YELLOW, Color.BLACK};

	
	/**
	 * 
	 * @param logstate for the informations they needed too build graphs
	 */
	public DiagramFactory(LogState logstate){
		this.logState = logstate;
		
	}
	
	
	/**
	 * build the requested graphs
	 * @return the builded plots
	 */
	public PlotPanel[] draw(){
		PlotPanel[] plotParts = new PlotPanel[6];
		Plot2DPanel plot2DForL = new Plot2DPanel();
		Plot2DPanel plot2DForP = new Plot2DPanel();
		Plot2DPanel plot2DForH = new Plot2DPanel();
		Plot2DPanel plot2DForD = new Plot2DPanel();
		Plot2DPanel plot2DForPSingle = new Plot2DPanel();
		Plot3DPanel plot3D = new Plot3DPanel();
		Font f = org.jalgo.module.em.gui.UIConstants.DEFAULT_FONT;
		
		
     	// Calculating the axis Slicing
     	int steps = logState.getStepCount();
     	int delta = 20;
     	int scaling = ((steps-1)/delta)+1;
     	axisSlicing = org.math.array.DoubleArray.increment(0.0, scaling, steps + scaling + ((steps % scaling == 0) ? 0 : scaling));

		//configure the plotPanels
	    //Plot2DForL
		String logPostfix =
				(logState.isLogarithmicLikelihoodRepresentationEnabled())
				? " (log)": "";
        String[] names2d = {"Iteration","Likelihood"+logPostfix};
        plot2DForL.setAxisLabels(names2d);
        plot2DForL.setLegendOrientation("SOUTH");
        plot2DForL.setFont(new Font("Courier", Font.BOLD, 12));
        // customize X axe
     	// rotate light labels
     	plot2DForL.getAxis(0).setLightLabelAngle(-PI / 4);
     	// change axe title position relatively to the base of the plot
     	plot2DForL.getAxis(0).setLabelPosition(0.5, -0.15);
     	plot2DForL.getAxis(0).setColor(Color.black);
     	plot2DForL.getAxis(0).setLabelFont(f);
     	// customize Y axe
     	// rotate light labels
     	plot2DForL.getAxis(1).setLightLabelAngle(-PI / 4);
     	// change axe title position relatively to the base of the plot
     	plot2DForL.getAxis(1).setLabelPosition(-0.15, 0.5);
     	// change axe title angle
     	plot2DForL.getAxis(1).setLabelAngle(-PI / 2);
     	plot2DForL.getAxis(1).setColor(Color.black);
     	plot2DForL.getAxis(1).setLabelFont(f);
     	//Changing the Color of the Axis Scale Labels for each axis:
     	plot2DForL.getAxis(0).setLightLabelColor(Color.black);
     	plot2DForL.getAxis(1).setLightLabelColor(Color.black);
     	//Setting the Slicing on for the Step-Axis of the L-Plot
     	plot2DForL.getAxis(0).setCustomSlicing(axisSlicing, axisSlicing);
     	//Disable editing of the legend
		plot2DForL.setEditable(false);
		
     	names2d[1] = Messages.getString("em", "DiagramPanel.d");
        plot2DForD.setAxisLabels(names2d);
        plot2DForD.setLegendOrientation("SOUTH");
        plot2DForD.setFont(new Font("Courier", Font.BOLD, 12));
     	plot2DForD.getAxis(0).setLightLabelAngle(-PI / 4);
     	plot2DForD.getAxis(0).setLabelPosition(0.5, -0.15);
     	plot2DForD.getAxis(0).setColor(Color.black);
     	plot2DForD.getAxis(0).setLabelFont(f);
     	plot2DForD.getAxis(1).setLightLabelAngle(-PI / 4);
     	plot2DForD.getAxis(1).setLabelPosition(-0.15, 0.5);
     	plot2DForD.getAxis(1).setLabelAngle(-PI / 2);
     	plot2DForD.getAxis(1).setColor(Color.black);
     	plot2DForD.getAxis(0).setLightLabelColor(Color.black);
		plot2DForD.getAxis(1).setLightLabelColor(Color.black);
     	plot2DForD.getAxis(1).setLabelFont(f);
     	plot2DForD.getAxis(0).setCustomSlicing(axisSlicing, axisSlicing);
     	plot2DForD.setEditable(false);

		names2d[1] = Messages.getString("em", "DiagramPanel.h");
        plot2DForH.setAxisLabels(names2d);
        plot2DForH.setLegendOrientation("SOUTH");
        plot2DForH.setFont(new Font("Courier", Font.BOLD, 12));
     	plot2DForH.getAxis(0).setLightLabelAngle(-PI / 4);
     	plot2DForH.getAxis(0).setLabelPosition(0.5, -0.15);
     	plot2DForH.getAxis(0).setColor(Color.black);
     	plot2DForH.getAxis(0).setLightLabelColor(Color.black);
		plot2DForH.getAxis(1).setLightLabelColor(Color.black);
     	plot2DForH.getAxis(0).setLabelFont(f);
     	plot2DForH.getAxis(1).setLightLabelAngle(-PI / 4);
     	plot2DForH.getAxis(1).setLabelPosition(-0.15, 0.5);
     	plot2DForH.getAxis(1).setLabelAngle(-PI / 2);
     	plot2DForH.getAxis(1).setColor(Color.black);
     	plot2DForH.getAxis(1).setLabelFont(f);
     	plot2DForH.getAxis(0).setCustomSlicing(axisSlicing, axisSlicing);
     	plot2DForH.setEditable(false);
     	
     	names2d[1] = Messages.getString("em", "DiagramPanel.p");
        plot2DForP.setAxisLabels(names2d);
        plot2DForP.setLegendOrientation("SOUTH");
        plot2DForP.setFont(new Font("Courier", Font.BOLD, 12));
     	plot2DForP.getAxis(0).setLightLabelAngle(-PI / 4);
     	plot2DForP.getAxis(0).setLabelPosition(0.5, -0.15);
     	plot2DForP.getAxis(0).setColor(Color.black);
     	plot2DForP.getAxis(0).setLightLabelColor(Color.black);
		plot2DForP.getAxis(1).setLightLabelColor(Color.black);
     	plot2DForP.getAxis(0).setLabelFont(f);
     	plot2DForP.getAxis(1).setLightLabelAngle(-PI / 4);
     	plot2DForP.getAxis(1).setLabelPosition(-0.15, 0.5);
     	plot2DForP.getAxis(1).setLabelAngle(-PI / 2);
     	plot2DForP.getAxis(1).setColor(Color.black);
     	plot2DForP.getAxis(1).setLabelFont(f);
     	plot2DForP.getAxis(0).setCustomSlicing(axisSlicing, axisSlicing);
     	plot2DForP.setEditable(false);
     	
		names2d[1] = Messages.getString("em", "DiagramPanel.psingle");
        plot2DForPSingle.setAxisLabels(names2d);
        plot2DForPSingle.setLegendOrientation("SOUTH");
        plot2DForPSingle.setFont(new Font("Courier", Font.BOLD, 12));
     	plot2DForPSingle.getAxis(0).setLightLabelAngle(-PI / 4);
     	plot2DForPSingle.getAxis(0).setLabelPosition(0.5, -0.15);
     	plot2DForPSingle.getAxis(0).setColor(Color.black);
     	plot2DForPSingle.getAxis(0).setLightLabelColor(Color.black);
		plot2DForPSingle.getAxis(1).setLightLabelColor(Color.black);
     	plot2DForPSingle.getAxis(0).setLabelFont(f);
     	plot2DForPSingle.getAxis(1).setLightLabelAngle(-PI / 4);
     	plot2DForPSingle.getAxis(1).setLabelPosition(-0.15, 0.5);
     	plot2DForPSingle.getAxis(1).setLabelAngle(-PI / 2);
     	plot2DForPSingle.getAxis(1).setColor(Color.black);
     	plot2DForPSingle.getAxis(1).setLabelFont(f);
     	plot2DForPSingle.getAxis(0).setCustomSlicing(axisSlicing, axisSlicing);
     	plot2DForPSingle.setEditable(false);
		
     	//Plot3D
     	String[] names3d = {Messages.getString("em", "DiagramPanel.3DAxisX"),
     			Messages.getString("em", "DiagramPanel.3DAxisY"),
     			Messages.getString("em", "DiagramPanel.l")};
        plot3D.setAxisLabels(names3d);
        plot3D.setLegendOrientation("SOUTH");
        plot3D.setForeground(Color.LIGHT_GRAY);
		plot3D.setFont(new Font("Courier", Font.BOLD, 12));
		plot3D.getAxis(0).setColor(Color.black);
		plot3D.getAxis(1).setColor(Color.black);
		plot3D.getAxis(2).setColor(Color.black);
		plot3D.getAxis(0).setLightLabelColor(Color.black);
		plot3D.getAxis(1).setLightLabelColor(Color.black);
		plot3D.getAxis(2).setLightLabelColor(Color.black);
		plot3D.getAxis(0).setLabelFont(f);
		plot3D.getAxis(1).setLabelFont(f);
		plot3D.getAxis(2).setLabelFont(f);
		plot3D.getAxis(1).setLightLabelAngle(-PI/4);
		plot3D.getAxis(0).setLabelPosition(1.0,-0.25,0.5);
		plot3D.getAxis(1).setLabelPosition(-0.25,1.0,0.5);
		plot3D.getAxis(2).setLabelPosition(0.1,0.1,1.1);
		plot3D.setEditable(false);
		
		//add PlotPanel to plotParts
		//Liklihood - Iteration
		plotParts[0] = plot2DForL;
		
		//d-Iteration
		plotParts[1] = plot2DForD;
		
		//h-Iteration
		plotParts[2] = plot2DForH;
		
		//p-Iteration
		plotParts[3] = plot2DForP;
		
		//pSingle-Iteration
		plotParts[4] = plot2DForPSingle;
		
		this.range();
		
		if(this.logState.getStepCount() >= 0){
			//Create plots:
			//2D Plots:
			//Likelihood - Iteration
			this.plot2DForL(plot2DForL);
			
			//d-Iteration
			this.plot2DForD(plot2DForD);
			
			//h-Iteration
			this.plot2DForH(plot2DForH);
			
			//p-Iteration
			this.plot2DForP(plot2DForP);
			
			//pSingle-Iteration
			this.plot2DForPSingle(plot2DForPSingle);
		}
		
		//3D Plot:
		if(this.logState.activate3D()) {
			this.mountains(plot3D);
			if(this.logState.getStepCount()>= 0){
				this.plot3D(plot3D);
			}
			plotParts[5] = plot3D;
		}
		else {
			plotParts[5] = null;
		}
		
		//Calculating the upper X Bound
		int upperXBound = (logState.getStepCount() / 10 + 1) * 10;
		if ((upperXBound - logState.getStepCount()) > 9) {
			upperXBound -= 10;
		}
		if (steps == 0) {
			upperXBound = 10;
		}
		if (upperXBound < steps + (scaling - (steps % scaling)) * ((steps % scaling != 0) ? 1 : 0)) {
			upperXBound += 10;
		}
		//Setting the upper X Bound
		plot2DForL.setFixedBounds(0, 0, upperXBound);
		plot2DForD.setFixedBounds(0, 0, upperXBound);
		plot2DForH.setFixedBounds(0, 0, upperXBound);
		plot2DForP.setFixedBounds(0, 0, upperXBound);
		plot2DForPSingle.setFixedBounds(0, 0, upperXBound);
		
		return plotParts;
	}
	
	/**
	 * controls the range of the scales
	 */
	private void range(){
		this.xRangeFor2D = org.math.array.DoubleArray.increment(0.0, 1.0, this.logState.getStepCount()+1);
		this.xRangeFor2DDots = Array.copy(axisSlicing);
		if (this.xRangeFor2DDots[this.xRangeFor2DDots.length - 1] > this.logState.getStepCount()){
			this.xRangeFor2DDots[this.xRangeFor2DDots.length -1] = this.logState.getStepCount();
		}
		this.xRangeFor2D_d_h = org.math.array.DoubleArray.increment(1.0, 1.0, this.logState.getStepCount()+1);
		this.xRangeFor2D_d_hDots = new double[xRangeFor2DDots.length - 1];
		for (int i = 0; i < xRangeFor2D_d_hDots.length; i++){
			xRangeFor2D_d_hDots[i] = xRangeFor2DDots[i + 1];
		}
	}
	
	/**
	 * Plots the lines and dots for the Likelihood.
	 * @param plot2D The <code>Plot2DPanel</code> in which the Curves and Lines shall be plotted.
	 */
	private void plot2DForL(Plot2DPanel plot2D){
		List<Integer> dotLegends = new LinkedList<Integer>();
		for(int p0 = 0; p0 < this.logState.getP0Count(); p0++){
			double[] yLine = new double[this.xRangeFor2D.length];
			double[] yDots = new double[this.xRangeFor2DDots.length];
			for(int iterationStep = 0; iterationStep <= this.logState.getStepCount(); iterationStep++){
				yLine[iterationStep] = this.logState.getL(p0, iterationStep);	
			}
			for (int dotIndex = 0; dotIndex < yDots.length; dotIndex ++){
				yDots[dotIndex] = yLine[(int) xRangeFor2DDots[dotIndex]];
			}
			
			if(this.logState.getStepCount() > 0){
				dotLegends.add(plot2D.addScatterPlot("<html>p<sub>0</sub>["+(p0 + 1)+"]</html>", this.colors[(p0%this.colors.length)+1], this.xRangeFor2DDots, yDots));
				plot2D.addLinePlot("<html>p<sub>0</sub>["+(p0 + 1)+"]</html>", this.colors[(p0%this.colors.length)+1], this.xRangeFor2D, yLine);
			} else {
				plot2D.addScatterPlot("<html>p<sub>0</sub>["+(p0 + 1)+"]</html>", this.colors[(p0%this.colors.length)+1], this.xRangeFor2D, yLine);
			}
		}
		for (int i = dotLegends.size() - 1; i >= 0; i--) {
			((JPanel) plot2D.plotLegend.getComponent(0)).remove(dotLegends.get(i));
		}			
	}
	
	/**
	 * Plots the lines and dots for the <code>Event</code> Statistical Analyzer.
	 * @param plot2D The <code>Plot2DPanel</code> in which the Curves and Lines shall be plotted.
	 */
	private void plot2DForD(Plot2DPanel plot2D){
		if(this.logState.getStepCount() == 0) {
		} else{
			List<Integer> dotLegends = new LinkedList<Integer>();
			for(int p0 = 0; p0 < this.logState.getP0Count(); p0++) {
				double[] yLine = new double[this.xRangeFor2D_d_h.length];
				double[] yDots = new double[this.xRangeFor2D_d_hDots.length];
				for(org.jalgo.module.em.data.Event event : this.logState.getEvents()){
					for(int iterationStep = 1; iterationStep <= this.logState.getStepCount(); iterationStep++){
						yLine[iterationStep - 1] = this.logState.getD(p0, iterationStep, event);
					}
					for (int dotIndex = 0; dotIndex < yDots.length; dotIndex ++){
						yDots[dotIndex] = yLine[(int) xRangeFor2D_d_hDots[dotIndex] - 1];
					}
					if(this.logState.getStepCount() > 1){
						dotLegends.add(plot2D.addScatterPlot(event.toString(), this.colors[p0%this.colors.length], this.xRangeFor2D_d_hDots, yDots));
						plot2D.addLinePlot(event.toString(), this.colors[p0%this.colors.length], this.xRangeFor2D_d_h, yLine);
					} else if (logState.getStepCount() > 0){
						plot2D.addScatterPlot(event.toString(), this.colors[p0%this.colors.length], this.xRangeFor2D_d_h, yLine);
					}
				}
			}
			for (int i = dotLegends.size() - 1; i >= 0; i--) {
				((JPanel) plot2D.plotLegend.getComponent(0)).remove(dotLegends.get(i));
			}
		}
	}

	/**
	 * Plots the lines and dots for the <code>Event</code> Frequency.
	 * @param plot2D The <code>Plot2DPanel</code> in which the Curves and Lines shall be plotted.
	 */
	private void plot2DForH(Plot2DPanel plot2D) {
		if(this.logState.getStepCount() == 0){
		} else {
			List<Integer> dotLegends = new LinkedList<Integer>();
			for(int p0 = 0; p0 < this.logState.getP0Count(); p0++){
				double[] yLine = new double[this.xRangeFor2D_d_h.length];
				double[] yDots = new double[this.xRangeFor2D_d_hDots.length];
				for(org.jalgo.module.em.data.Event event : this.logState.getEvents()){
					for(int iterationStep = 1; iterationStep <= this.logState.getStepCount(); iterationStep++){
						yLine[iterationStep - 1] = this.logState.getH(p0, iterationStep, event);
					}
					for (int dotIndex = 0; dotIndex < yDots.length; dotIndex ++){
						yDots[dotIndex] = yLine[(int) xRangeFor2D_d_hDots[dotIndex] - 1];
					}
					if(this.logState.getStepCount() > 1){
						dotLegends.add(plot2D.addScatterPlot(event.toString(), this.colors[p0%this.colors.length], this.xRangeFor2D_d_hDots, yDots));
						plot2D.addLinePlot(event.toString(), this.colors[p0%this.colors.length], this.xRangeFor2D_d_h, yLine);
					} else if (logState.getStepCount() > 0){
						plot2D.addScatterPlot(event.toString(), this.colors[p0%this.colors.length], this.xRangeFor2D_d_h, yLine);
					}
				}
			}
			for (int i = dotLegends.size() - 1; i >= 0; i--) {
				((JPanel) plot2D.plotLegend.getComponent(0)).remove(dotLegends.get(i));
			}
		}
	}
	
	/**
	 * Plots the lines and dots for the <code>Event</code> Probability.
	 * @param plot2D The <code>Plot2DPanel</code> in which the Curves and Lines shall be plotted.
	 */
	private void plot2DForP(Plot2DPanel plot2D) {
		List<Integer> dotLegends = new LinkedList<Integer>();
		for(int p0 = 0; p0 < this.logState.getP0Count(); p0++){
			double[] yLine = new double[this.xRangeFor2D.length];
			double[] yDots = new double[this.xRangeFor2DDots.length];
			for(org.jalgo.module.em.data.Event event : this.logState.getEvents()){
				for(int iterationStep = 0; iterationStep <= this.logState.getStepCount(); iterationStep++){
					yLine[iterationStep] = this.logState.getP(p0, iterationStep, event);
					}
				for (int dotIndex = 0; dotIndex < yDots.length; dotIndex ++){
					yDots[dotIndex] = yLine[(int) xRangeFor2DDots[dotIndex]];
				}
				
				if(this.logState.getStepCount() > 0){
					dotLegends.add(plot2D.addScatterPlot(event.toString(), this.colors[p0%this.colors.length], this.xRangeFor2DDots, yDots));
					plot2D.addLinePlot(event.toString(), this.colors[p0%this.colors.length], this.xRangeFor2D, yLine);
				} else {
					plot2D.addScatterPlot(event.toString(), this.colors[p0%this.colors.length], this.xRangeFor2D, yLine);
				}
			}
		}
		for (int i = dotLegends.size() - 1; i >= 0; i--) {
			((JPanel) plot2D.plotLegend.getComponent(0)).remove(dotLegends.get(i));
		}
	}
	

	/**
	 * Plots the lines and dots for the SingleEvent Probability.
	 * @param plot2D The <code>Plot2DPanel</code> in which the Curves and Lines shall be plotted.
	 */
	private void plot2DForPSingle(Plot2DPanel plot2D) {
		List<Integer> dotLegends = new LinkedList<Integer>();
		for(int p0 = 0; p0 < this.logState.getP0Count(); p0++){
			double[] yLine = new double[this.xRangeFor2D.length];
			double[] yDots = new double[this.xRangeFor2DDots.length];
			for (int objectIndex = 0; objectIndex < this.logState.getExperiment().size(); objectIndex ++) {
				for (int side = 1; side <= this.logState.getExperiment().get(objectIndex); side ++) {
					Point singleEvent = new Point(side, objectIndex);
					for(int iterationStep = 0; iterationStep <= this.logState.getStepCount(); iterationStep++){
						yLine[iterationStep] = this.logState.getPSingleEvent(p0, iterationStep, singleEvent);
					}
					for (int dotIndex = 0; dotIndex < yDots.length; dotIndex ++){
						yDots[dotIndex] = yLine[(int) xRangeFor2DDots[dotIndex]];
					}
					String name = ((LogState) this.logState).singleEventToString(singleEvent);
					
					if(this.logState.getStepCount() > 0){
						dotLegends.add(plot2D.addScatterPlot(name, this.colors[p0%this.colors.length], this.xRangeFor2DDots, yDots));
						plot2D.addLinePlot(name, this.colors[p0%this.colors.length], this.xRangeFor2D, yLine);
					} else {
						plot2D.addScatterPlot(name, this.colors[p0%this.colors.length], this.xRangeFor2D, yLine);
					}
				}
			}
		}
		for (int i = dotLegends.size() - 1; i >= 0; i--) {
			((JPanel) plot2D.plotLegend.getComponent(0)).remove(dotLegends.get(i));
		}
	}

	/**
	 * plots the lines and dots into the 3D Plot.
	 */
	private void plot3D(org.math.plot.Plot3DPanel plot3D){
		List<Integer> dotLegends = new LinkedList<Integer>();
		
		if(this.logState.getStepCount() == 0){
			for(int p0 = 0; p0 < this.logState.getP0Count(); p0++){
				double[] xRange = new double [this.logState.getStepCount() + 1];
				double[] yRange = new double [this.logState.getStepCount() + 1];
				double[] zRange = new double [this.logState.getStepCount() + 1];
				for(int iterationStep = 0; iterationStep <= this.logState.getStepCount(); iterationStep++){
					xRange[iterationStep] = this.logState.getPSingleEvent(p0, iterationStep, new Point(2,0));
					yRange[iterationStep] = this.logState.getPSingleEvent(p0, iterationStep, new Point(2,1));
					zRange[iterationStep] = this.logState.getL(p0, iterationStep);
				}
				plot3D.addScatterPlot("<html>p<sub>0</sub>["+(p0 + 1)+"]</html>",this.colors[p0%this.colors.length], xRange, yRange, zRange);
			}
		}
		else {
			for(int p0 = 0; p0 < this.logState.getP0Count(); p0++){
				double[] xRange = new double [this.logState.getStepCount() + 1];
				double[] yRange = new double [this.logState.getStepCount() + 1];
				double[] zRange = new double [this.logState.getStepCount() + 1];
				for(int iterationStep = 0; iterationStep <= this.logState.getStepCount(); iterationStep++){
					xRange[iterationStep] = this.logState.getPSingleEvent(p0, iterationStep, new Point(2,0));
					yRange[iterationStep] = this.logState.getPSingleEvent(p0, iterationStep, new Point(2,1));
					zRange[iterationStep] = this.logState.getL(p0, iterationStep);
				}
				//In  Case of no Change => no line can be drawn
				if (xRange[0] == xRange[1]) {
					
					plot3D.addScatterPlot("<html>p<sub>0</sub>["+(p0 + 1)+"]</html>",this.colors[p0%this.colors.length], xRange, yRange, zRange);
					
				} else {
					dotLegends.add(plot3D.addScatterPlot("<html>p<sub>0</sub>["+(p0 + 1)+"]</html>",this.colors[p0%this.colors.length], xRange, yRange, zRange));
					plot3D.addLinePlot("<html>p<sub>0</sub>["+(p0 + 1)+"]</html>",this.colors[p0%this.colors.length], xRange, yRange, zRange);
				}
			}
		}
		
		for (int i = dotLegends.size() - 1; i >= 0; i--) {
			((JPanel) plot3D.plotLegend.getComponent(0)).remove(dotLegends.get(i));
		}
	}
	

	/**
	 * Build the ground of the 3D-Graph.
	 */
	private void mountains(org.math.plot.Plot3DPanel plot3D){
		double[][] mountains;
		double [] xRangeFor3D;
		double [] yRangeFor3D;
		if (logState.isLogarithmicLikelihoodRepresentationEnabled()) {
			mountains = this.mountains3D;
			xRangeFor3D = this.xRangeFor3DLog;
			yRangeFor3D = this.yRangeFor3DLog;
		} else {
			mountains = this.mountains;
			xRangeFor3D = this.xRangeFor3D;
			yRangeFor3D = this.yRangeFor3D;
		}
		if (mountains == null){
			if (! logState.isLogarithmicLikelihoodRepresentationEnabled()) {
				this.xRangeFor3D = org.math.array.DoubleArray.increment(0.0, 0.025, 1.026);
				this.yRangeFor3D = org.math.array.DoubleArray.increment(0.0, 0.025, 1.026);
				xRangeFor3D = this.xRangeFor3D;
				yRangeFor3D = this.yRangeFor3D;
			} else {
				this.xRangeFor3DLog = org.math.array.DoubleArray.increment(0.0, 0.025, 1.026);
				this.xRangeFor3DLog[0] = 0.005;
				this.xRangeFor3DLog[xRangeFor3DLog.length - 1] = 0.995;
				this.yRangeFor3DLog = org.math.array.DoubleArray.increment(0.0, 0.025, 1.026);
				this.yRangeFor3DLog[0] = 0.005;
				this.yRangeFor3DLog[xRangeFor3DLog.length - 1] = 0.995;
				xRangeFor3D = this.xRangeFor3DLog;
				yRangeFor3D = this.yRangeFor3DLog;
			}
			mountains = new double[this.xRangeFor3D.length][this.yRangeFor3D.length];
			for(int x = 0; x < xRangeFor3D.length;x++){
					for(int y = 0; y< yRangeFor3D.length;y++){
						mountains[y][x] = this.logState.getL3DGraph(xRangeFor3D[x], yRangeFor3D[y]);
					}
			}
			if (logState.isLogarithmicLikelihoodRepresentationEnabled()) {
				this.mountains3D = mountains;
			} else {
				this.mountains = mountains;
			}
		}
		plot3D.addGridPlot("Ground", Color.gray, xRangeFor3D, yRangeFor3D, mountains);
	}
	
}
