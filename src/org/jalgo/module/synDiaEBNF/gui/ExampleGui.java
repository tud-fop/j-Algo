/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * Created on May 25, 2004
 */
 
package org.jalgo.module.synDiaEBNF.gui;

import java.io.Serializable;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.jalgo.main.gfx.BorderFlowLayout;
import org.jalgo.main.gfx.FixPointAnchor;
import org.jalgo.main.gfx.RoundedManhattanConnectionRouter;
import org.jalgo.main.gui.actions.AddCircleAction;
import org.jalgo.main.gui.actions.AddCurvedLineAction;
import org.jalgo.main.gui.actions.AddLineAction;
import org.jalgo.main.gui.actions.AddRectangleAction;
import org.jalgo.main.gui.actions.AddRoundedRectangleAction;
import org.jalgo.main.gui.actions.ExportAction;
import org.jalgo.main.gui.actions.PrintViewAction;
import org.jalgo.main.gui.actions.ZoomInAction;
import org.jalgo.main.gui.actions.ZoomOutAction;
import org.jalgo.main.gui.widgets.GraphViewForm;
import org.jalgo.main.gui.widgets.Splitter;
import org.jalgo.main.gui.widgets.StackViewForm;
import org.jalgo.main.gui.widgets.TextViewForm;


/**
 * This class provides the GUI for the EBNF-Module
 * 
 * @author Cornelius Hald
 * @author Christopher Friedrich
 */
public class ExampleGui extends Gui  implements Serializable{

	private static final long serialVersionUID = -253027503594404507L;
	TextViewForm form1;
	GraphViewForm form2;
	StackViewForm form3;

	public ExampleGui(Composite parent) {

		super(parent);

		// Generate 3 ViewForms separated by Sashs. The numbering is clockwise.
		// The Sash are now replaced with Splitters. Splitters are better so juste
		// use them.
		Splitter sash = new Splitter(parent, SWT.HORIZONTAL);

		form2 = new GraphViewForm(sash, SWT.BORDER);
		Splitter sash1 = new Splitter(sash, SWT.VERTICAL);

		form1 = new TextViewForm(sash1, SWT.BORDER);
		form3 = new StackViewForm(sash1, SWT.BORDER);

		sash.setWeights(new int[] { 80, 20 });
		sash1.setWeights(new int[] { 60, 40 });

		// ** Form 1 **
		// Create GUI

		form1.setText("Some TextCanvas");
		form1.setImage(ImageDescriptor.createFromURL(
			getClass().getResource("/main_pix/new.gif")).createImage());
		form1.getTextCanvas().setTextSegments(
			new String[] {
				"Das ist der erste Block",
				" und hier der zweite.",
				" Und noch ein dritter.",
				" Der vierte war zu sp?t!!!!" });
		form1.getTextCanvas().setMode(1);
		form1.getTextCanvas().mark(2);

		
		// Instanciate some nodes
		IFigure node1 = new RectangleFigure();
		Figure node2 = new RectangleFigure();
		Ellipse node3 = new Ellipse();
		Ellipse node4 = new Ellipse();
		Ellipse node5 = new Ellipse();
		Ellipse node6 = new Ellipse();
		Figure f = new Figure();

		
		// We need some text
		Label label1 = new Label("1");
		Label label2 = new Label("2");
		Label label3 = new Label("3");
		Label label4 = new Label("4");

		// Add text to nodes
		node1.add(label1);
		node2.add(label2);
		node3.add(label3);
		node4.add(label4);

		// Configure nodes
		node1.setBackgroundColor(ColorConstants.red);
		node1.setLocation(new Point(50, 30));
		//node1.setLocation(new Point(50, 10));
		node2.setBackgroundColor(ColorConstants.blue);
		//node2.setLocation(new Point(100, 100));
		node3.setBackgroundColor(ColorConstants.yellow);
		node3.setLocation(new Point(200, 20));
		//node3.setLocation(new Point(200, 20));
		node4.setBackgroundColor(ColorConstants.cyan);
		node5.setBackgroundColor(ColorConstants.orange);
		node6.setBackgroundColor(ColorConstants.darkBlue);

		// Create connection
		PolylineConnection conn1 = new PolylineConnection();
		//conn1.setConnectionRouter(new BendpointConnectionRouter());
		conn1.setSourceAnchor(new FixPointAnchor(node1, SWT.RIGHT));
		conn1.setTargetAnchor(new FixPointAnchor(node2, SWT.TOP));
		conn1.setConnectionRouter(new RoundedManhattanConnectionRouter());
		conn1.setSourceAnchor(new FixPointAnchor(node1, SWT.RIGHT));
		conn1.setTargetAnchor(new FixPointAnchor(node2, SWT.LEFT));
		conn1.setTargetDecoration(new PolygonDecoration());
		//conn1.setTargetDecoration(new PolygonDecoration());

		// Create another connection
		PolylineConnection conn2 = new PolylineConnection();
		conn2.setConnectionRouter(new RoundedManhattanConnectionRouter());
		//conn2.setConnectionRouter(new FanRouter());
		conn2.setSourceAnchor(new FixPointAnchor(node1, SWT.RIGHT));
		conn2.setTargetAnchor(new FixPointAnchor(node3, SWT.LEFT));
		//conn2.setSourceAnchor(new ChopboxAnchor(node1));
		conn2.setTargetAnchor(new FixPointAnchor(node3, SWT.LEFT));
		conn2.setSourceAnchor(new FixPointAnchor(node1, SWT.RIGHT));
		conn2.setTargetAnchor(new FixPointAnchor(node3, SWT.LEFT));
		PolylineConnection conn3 = new PolylineConnection();
		conn3.setConnectionRouter(new RoundedManhattanConnectionRouter());
		conn3.setSourceAnchor(new FixPointAnchor(node3, SWT.RIGHT));
		conn3.setTargetAnchor(new FixPointAnchor(node4, SWT.LEFT));
		PolylineConnection conn4 = new PolylineConnection();
		conn4.setConnectionRouter(new RoundedManhattanConnectionRouter());
		conn4.setSourceAnchor(new FixPointAnchor(node2, SWT.RIGHT));
		conn4.setTargetAnchor(new FixPointAnchor(node4, SWT.LEFT));
		PolylineConnection conn5 = new PolylineConnection();
		conn5.setConnectionRouter(new RoundedManhattanConnectionRouter());
		conn5.setSourceAnchor(new FixPointAnchor(node5, SWT.RIGHT));
		conn5.setTargetAnchor(new FixPointAnchor(node6, SWT.LEFT));
		PolylineConnection conn6 = new PolylineConnection();
		conn6.setConnectionRouter(new RoundedManhattanConnectionRouter());
		conn6.setSourceAnchor(new FixPointAnchor(node2, SWT.RIGHT));
		conn6.setTargetAnchor(new FixPointAnchor(node2, SWT.LEFT));
		PolylineConnection conn7 = new PolylineConnection();
		conn7.setConnectionRouter(new RoundedManhattanConnectionRouter());
		conn7.setSourceAnchor(new FixPointAnchor(node4, SWT.RIGHT));
		conn7.setTargetAnchor(new FixPointAnchor(node5, SWT.LEFT));

		// Add sticky lable to connection
		Label label = new Label("Mittelpunkt");
		label.setOpaque(true);
		label.setBackgroundColor(ColorConstants.buttonLightest);
		label.setBorder(new LineBorder());
		conn1.add(label, new MidpointLocator(conn1, 0));
		BorderFlowLayout flowlayout = new BorderFlowLayout();
		flowlayout.setMajorAlignment(FlowLayout.ALIGN_CENTER);
		//Ausrichtung des gesamten Systems
		flowlayout.setMinorAlignment(FlowLayout.ALIGN_CENTER);
		//Ausrichtung der einzelnen Knoten in den Reihen
		flowlayout.setMajorSpacing(100); //Abstand der Knoten in y-Richtung
		flowlayout.setMinorSpacing(100); //Abstand der Knoten in x-Richtung
		//flowlayout.setStretchMinorAxis(true);
		form2.getPanel().setLayoutManager(flowlayout);
		BorderFlowLayout flowlayout2 = new BorderFlowLayout();
		flowlayout2.setMajorAlignment(FlowLayout.ALIGN_CENTER);
		flowlayout2.setHorizontal(false); //vertikales FlowLayout
		flowlayout2.setMinorAlignment(FlowLayout.ALIGN_LEFTTOP);
		flowlayout2.setMajorSpacing(100); //Abstand der Knoten in x-Richtung
		flowlayout2.setMinorSpacing(50); //Abstand der Knoten in y-Richtung
		f.setLayoutManager(flowlayout2);
		//lay.layout(panel);
		//node1.setBorder(new Border());
		//node1.setSize(100, 100);

		// Add all to panel
		form2.getPanel().add(node1, 0);
		f.add(node2, 0);
		f.add(node3, 1);
		form2.getPanel().add(f, 1);
		form2.getPanel().add(node4, 2);
		form2.getPanel().add(node5, 3);
		form2.getPanel().add(node6, 4);
		form2.getPanel().add(conn1);
		form2.getPanel().add(conn2);
		form2.getPanel().add(conn3);
		form2.getPanel().add(conn4);
		form2.getPanel().add(conn5);
		form2.getPanel().add(conn6);
		form2.getPanel().add(conn7);
		//panel.add(navNode);
		//flowlayout.layout(panel);

		// Add dragger for D&D

		// Create Toolbar
		ToolBarManager toolbarMgr2 = new ToolBarManager(SWT.FLAT);
		toolbarMgr2.add(new AddCircleAction(form2.getPanel()));
		toolbarMgr2.add(new AddRectangleAction(form2.getPanel()));
		toolbarMgr2.add(new AddRoundedRectangleAction(form2.getPanel()));
		toolbarMgr2.add(new Separator());
		toolbarMgr2.add(new AddLineAction());
		toolbarMgr2.add(new AddCurvedLineAction());
		toolbarMgr2.add(new Separator());
		toolbarMgr2.add(new ZoomInAction(form2));
		toolbarMgr2.add(new ZoomOutAction(form2));
		toolbarMgr2.add(new Separator());
		toolbarMgr2.add(new PrintViewAction(form2.getPanel()));
		toolbarMgr2.add(new Separator());
		toolbarMgr2.add(new ExportAction(form2.getPanel()));

		ToolBar toolbar2 = toolbarMgr2.createControl(form2);

		// Attach all to ViewForm
		form2.setTopCenter(toolbar2);
		form2.setText("Graphenmodellierung");
		form2.setImage(ImageDescriptor.createFromURL(
			getClass().getResource("/ebnf_pix/new.gif")).createImage());

		// ** Form 3 **
		// Create GUI
		form3.setText("Stack");
		form3.setImage(ImageDescriptor.createFromURL(
			getClass().getResource("/ebnf_pix/new.gif")).createImage());
		form3.getStackCanvas().push("Element 1");
		form3.getStackCanvas().push("Element 2");
		form3.getStackCanvas().push("Element 3");
	}

}
