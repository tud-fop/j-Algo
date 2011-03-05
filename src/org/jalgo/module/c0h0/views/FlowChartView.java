package org.jalgo.module.c0h0.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.Paper;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jalgo.module.c0h0.controller.Controller;
import org.jalgo.module.c0h0.controller.ViewManager;
import org.jalgo.module.c0h0.models.flowchart.Bundle;
import org.jalgo.module.c0h0.models.flowchart.Edge;
import org.jalgo.module.c0h0.controller.InterfaceConstants;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

/**
 * draws the flowchart
 * 
 * @author Philipp Geissler
 * @author Mathias Kaufmann
 * 
 */
public class FlowChartView extends View {

	private static final long serialVersionUID = 1993191423448829250L;

	private Controller con;
	private ViewManager viewManager;
	public mxGraphComponent graphComponent;
	private EventHandler eventHandler;

	private JPanel completeFlowChartView;
	private JPanel navigateFlowChart;
	private JSlider zoomSlider;
	private mxGraph flowChartGraph;
	private Object parent, from, to, connect;

	private final double ZOOMFACTOR = 1.1;
	private final double NOBEAMERMODEZOOM = 1.5;
	private final double BEAMERMODEZOOM = 2;

	private Paper paper;
	private int markedMinY;
	private int markedMaxY;
	private mxRectangle minimumSize;

	private Map<Object, String> objectConnector; // connects drawn objects and
	// elements of the model
	private List<Bundle> elementList;
	private boolean beamerMode = false; // to toggle beamer mode only once when

	// selected

	/**
	 * @param con
	 *            the controller
	 * @param viewManager
	 */
	public FlowChartView(final Controller con, ViewManager viewManager) {

		this.viewManager = viewManager;

		objectConnector = new HashMap<Object, String>();

		this.con = con;

		completeFlowChartView = new JPanel();
		completeFlowChartView.setLayout(new BoxLayout(completeFlowChartView,
				BoxLayout.Y_AXIS));
		navigateFlowChart = new JPanel();
		navigateFlowChart.setLayout(new BoxLayout(navigateFlowChart,
				BoxLayout.X_AXIS));

		eventHandler = new EventHandler();

		flowChartGraph = new mxGraph();

		zoomSlider = new JSlider(JSlider.HORIZONTAL, 5, 25,
				(int) (NOBEAMERMODEZOOM * 10));
		zoomSlider.addChangeListener(eventHandler);
		zoomSlider.setPaintTicks(true);
		zoomSlider.setPaintLabels(false);

		minimumSize = new mxRectangle();
		minimumSize.setWidth(220);
		minimumSize.setHeight(230);

		parent = flowChartGraph.getDefaultParent();

		// create element styles

		mxStylesheet stylesheet = flowChartGraph.getStylesheet();

		Hashtable<String, Object> rhombus = new Hashtable<String, Object>();
		Hashtable<String, Object> rectangle = new Hashtable<String, Object>();
		Hashtable<String, Object> anker = new Hashtable<String, Object>();
		Hashtable<String, Object> address = new Hashtable<String, Object>();
		Hashtable<String, Object> leftAddress = new Hashtable<String, Object>();
		Hashtable<String, Object> vertical = new Hashtable<String, Object>();
		Hashtable<String, Object> horizontal = new Hashtable<String, Object>();
		Hashtable<String, Object> arrow = new Hashtable<String, Object>();

		rhombus.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RHOMBUS);
		rhombus.put(mxConstants.STYLE_SPACING_TOP, 2);
		rhombus.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		rectangle.put(mxConstants.STYLE_SPACING_TOP, 2);
		rectangle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		address.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
		address.put(mxConstants.STYLE_FILLCOLOR, "#FF0000");
		address.put(mxConstants.STYLE_FONTCOLOR, "#FF0000");
		address.put(mxConstants.STYLE_SPACING_TOP, 12);
		leftAddress.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
		leftAddress.put(mxConstants.STYLE_FILLCOLOR, "#FF0000");
		leftAddress.put(mxConstants.STYLE_FONTCOLOR, "#FF0000");
		leftAddress.put(mxConstants.STYLE_SPACING_TOP, 12);
		vertical.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_LINE);
		vertical.put(mxConstants.STYLE_DIRECTION, mxConstants.DIRECTION_SOUTH);
		vertical.put(mxConstants.STYLE_SPACING_LEFT, 10);
		vertical.put(mxConstants.STYLE_STROKEWIDTH, 2);
		horizontal.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_LINE);
		horizontal.put(mxConstants.STYLE_DIRECTION, mxConstants.DIRECTION_WEST);
		horizontal.put(mxConstants.STYLE_SPACING_BOTTOM, 5);
		horizontal.put(mxConstants.STYLE_STROKEWIDTH, 2);
		arrow.put(mxConstants.STYLE_SPACING_RIGHT, 10);
		arrow.put(mxConstants.STYLE_STROKEWIDTH, 2);
		anker.put(mxConstants.STYLE_OPACITY, 0);

		stylesheet.putCellStyle("RHOMBUS", rhombus);
		stylesheet.putCellStyle("ADDRESS", address);
		stylesheet.putCellStyle("LEFTADDRESS", leftAddress);
		stylesheet.putCellStyle("VERTICAL", vertical);
		stylesheet.putCellStyle("HORIZONTAL", horizontal);
		stylesheet.putCellStyle("ARROW", arrow);
		stylesheet.putCellStyle("RECTANGLE", rectangle);
		stylesheet.putCellStyle("ANKER", anker);

		
		graphComponent = new mxGraphComponent(flowChartGraph);
		setBackground();

		graphComponent.getViewport().setOpaque(false);
		graphComponent.setMinimumSize(new Dimension());
		graphComponent.setEnabled(false);
		graphComponent.getVerticalScrollBar().setUnitIncrement(10);

		graphComponent.setZoomFactor(ZOOMFACTOR);
		graphComponent.zoom(NOBEAMERMODEZOOM);

		graphComponent.setCenterPage(true);
		graphComponent.setPageVisible(true);
		graphComponent.setPageScale(1);
		graphComponent.setPreferPageSize(true);
		paper = new Paper();
		paper.setSize(500, 900);
		graphComponent.getPageFormat().setPaper(paper);
		graphComponent.setCenterZoom(true);

		flowChartGraph.setMinimumGraphSize(minimumSize);
		flowChartGraph.setCellsLocked(true);
		flowChartGraph.setConnectableEdges(false);
		flowChartGraph.setAllowDanglingEdges(false);

		navigateFlowChart.add(zoomSlider);

		completeFlowChartView.add(graphComponent);
		completeFlowChartView.add(navigateFlowChart);
		setLayout(new BorderLayout());
		this.add(completeFlowChartView);

		MouseAdapter m = new MouseAdapter() {

			public void mouseReleased(MouseEvent e) {
				Object cell = graphComponent.getCellAt(e.getX(), e.getY());
				if (cell != null) {
					// mark symbol
					String add = objectConnector.get(cell);
					if (add != null)
						con.markNode(add); // if element is visible
				}
			}
		};

		graphComponent.getGraphControl().addMouseListener(m);
	}

	private void setBackground() {
		Color notWhite = Color.decode(viewManager.backgroundColor(con
				.getFcmodel().isActive()));

		graphComponent.setBackground(notWhite);
		graphComponent.setPageBackgroundColor(notWhite);
		graphComponent.setPageBorderColor(notWhite);
		graphComponent.setPageShadowColor(notWhite);

	}

	/**
	 * action listener for the zoom buttons of the flowchart
	 * 
	 */
	public class EventHandler implements ChangeListener {

		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider) e.getSource();
			flowChartGraph.getView().setScale(source.getValue() * .1);
			centerHorizontalScrollbar();
		}
	}

	/**
	 * Creates the flowchart-graph which is visibleTo a specific node
	 * 
	 * @param visibleTo
	 * 				Integer with the index of the last visible node 
	 */
	private void createFlowChartGraph(int visibleTo) {
		int mid = (int) (graphComponent.getPageFormat().getWidth() / 2);
		Bundle theMarkedBundle = null;

		// clear flowchart
		flowChartGraph.selectAll();
		flowChartGraph.removeCells();

		flowChartGraph.getModel().beginUpdate();
		try {

			for (int i = 0; i < elementList.size(); i++) {

				Bundle l = elementList.get(i);
				List<Edge> edgeList = l.getEdges();

				for (Edge e : edgeList) {

					if (i < visibleTo) {
						// marked elements
						if (l.getAddress().equals(
								con.getASTModel().getMarkedNode())) {
							theMarkedBundle = l;
							break;
						}
						// draw unmarked elements

						from = flowChartGraph.insertVertex(parent, null, e
								.getFrom().getLabel(),
								e.getFrom().getX() + mid, e.getFrom().getY(), e
										.getFrom().getWidth(), e.getFrom()
										.getHeight(), e.getFrom().getStyle());
						to = flowChartGraph.insertVertex(parent, null, e
								.getTo().getLabel(), e.getTo().getX() + mid, e
								.getTo().getY(), e.getTo().getWidth(), e
								.getTo().getHeight(), e.getTo().getStyle());
						connect = flowChartGraph.insertEdge(parent, null, e
								.getLabel(), from, to, e.getStyle());

						objectConnector.put(connect, l.getAddress());
						objectConnector.put(from, l.getAddress());
						objectConnector.put(to, l.getAddress());
					}

					else {
						// draw invisible elements
						from = flowChartGraph.insertVertex(parent, null, e
								.getFrom().getLabel(),
								e.getFrom().getX() + mid, e.getFrom().getY(), e
										.getFrom().getWidth(), e.getFrom()
										.getHeight(), e.getFrom().getStyle()
										+ getInv(e.getFrom().getStyle()));
						to = flowChartGraph.insertVertex(parent, null, e
								.getTo().getLabel(), e.getTo().getX() + mid, e
								.getTo().getY(), e.getTo().getWidth(), e
								.getTo().getHeight(), e.getTo().getStyle()
								+ getInv(e.getTo().getStyle()));
						connect = flowChartGraph.insertEdge(parent, null, e
								.getLabel(), from, to, e.getStyle()
								+ getInv(e.getStyle()));
					}
				}
			}
			if (theMarkedBundle != null) {
				// draw marked elements

				markedMaxY = 0;
				markedMinY = 9999;

				List<Edge> edgeList2 = theMarkedBundle.getEdges();
				for (Edge e : edgeList2) {

					from = flowChartGraph.insertVertex(parent, null, e
							.getFrom().getLabel(), e.getFrom().getX() + mid, e
							.getFrom().getY(), e.getFrom().getWidth(), e
							.getFrom().getHeight(), e.getFrom().getStyle()
							+ getMark(e.getFrom().getStyle()));
					to = flowChartGraph.insertVertex(parent, null, e.getTo()
							.getLabel(), e.getTo().getX() + mid, e.getTo()
							.getY(), e.getTo().getWidth(), e.getTo()
							.getHeight(), e.getTo().getStyle()
							+ getMark(e.getTo().getStyle()));
					connect = flowChartGraph.insertEdge(parent, null, e
							.getLabel(), from, to, e.getStyle()
							+ getMark(e.getStyle()));

					objectConnector.put(connect, theMarkedBundle.getAddress());
					objectConnector.put(from, theMarkedBundle.getAddress());
					objectConnector.put(to, theMarkedBundle.getAddress());

					// coordinates for automatic scrolling
					if (e.getFrom().getStyle() != "ANKER") {
						if (e.getFrom().getY() < markedMinY)
							markedMinY = e.getFrom().getY();
						if (e.getFrom().getY() > markedMaxY)
							markedMaxY = e.getFrom().getY();
					}

				}

				// scroll automatically

				int height = (int) (graphComponent.getHeight());
				int min = (int) (markedMinY * flowChartGraph.getView()
						.getScale());
				int max = (int) (markedMaxY * flowChartGraph.getView()
						.getScale());

				if (markedMinY != 9999 && markedMaxY != 0)
					graphComponent.getVerticalScrollBar().setValue(
							(int) ((min + max) / 2 - height / 2));

			}

		} finally {
			flowChartGraph.getModel().endUpdate();
		}
	}
	public boolean render() {
		elementList = con.getFcmodel().getElements();

		if(elementList==null) return false;

		objectConnector.clear();

		// Creates the flowchartgraph
		createFlowChartGraph(con.getFcmodel().getVisibleTo());

		// adapt paper to flowchart
		paper.setSize(
				(flowChartGraph.getGraphBounds().getWidth() / flowChartGraph
						.getView().getScale()) * (1.5), flowChartGraph
						.getGraphBounds().getHeight()
						/ flowChartGraph.getView().getScale());
		graphComponent.getPageFormat().setPaper(paper);

		graphComponent.updateUI();
		completeFlowChartView.updateUI();
		
		centerHorizontalScrollbar();
		
		return true;
	}

	/**
	 * defines the style of invisible elements
	 * 
	 * @param style
	 * @return style addition
	 */
	private String getInv(String style) {
		String GREYBACKG = viewManager.backgroundColor(con.getFcmodel().isActive()).substring(1);
		String GREY = "CCCCCC";
		
		if (style == "RHOMBUS" || style == "RECTANGLE")
			return ";fillColor=#"+GREY+";strokeColor=#"+GREY+";fontColor=#"+GREY;
		return ";fillColor=#"+GREY+";strokeColor=#"+GREY+";fontColor=#"+GREYBACKG;
	}

	/**
	 * defines the style of marked elements
	 * 
	 * @param style
	 * @return style addition
	 */
	private String getMark(String style) {
		if (style == "RHOMBUS" || style == "RECTANGLE")
			return ";fillColor=#" + InterfaceConstants.MARKEDCOLOR_ACTIVE
					+ ";strokeColor=#" + InterfaceConstants.MARKEDCOLOR_ACTIVE;
		return ";fillColor=#" + InterfaceConstants.MARKEDCOLOR_ACTIVE
				+ ";fontColor=#" + InterfaceConstants.MARKEDCOLOR_ACTIVE
				+ ";strokeColor=#" + InterfaceConstants.MARKEDCOLOR_ACTIVE;
	}
	
	/**
	 * centers the horizontal scrollbar
	 */
	private void centerHorizontalScrollbar(){
		graphComponent.zoomAndCenter();
		graphComponent.getHorizontalScrollBar().setValue(
				(int) (((1-(graphComponent.getWidth()/(graphComponent.getPageFormat().getWidth()
				*flowChartGraph.getView().getScale())))*graphComponent.getHorizontalScrollBar().getMaximum())/2));
	}

	public boolean update() {
		setBackground();

		// check for beamer mode
		if (viewManager.isBeamerMode() && !beamerMode) {
			flowChartGraph.getView().setScale(BEAMERMODEZOOM);
			zoomSlider.setValue((int) (BEAMERMODEZOOM * 10));
			beamerMode = true;
		} else if (!viewManager.isBeamerMode() && beamerMode) {
			flowChartGraph.getView().setScale(NOBEAMERMODEZOOM);
			zoomSlider.setValue((int) (NOBEAMERMODEZOOM * 10));
			beamerMode = false;
		}

		render();
		return true;
	}

	/**
	 * Returns an image of the graph
	 * 
	 * @return Image
	 */
	public Image getGraph() {
		String backUp = con.getASTModel().getMarkedNode();
		con.getASTModel().setMarkedNode("");
		createFlowChartGraph(elementList.size());
		Image img = mxCellRenderer.createBufferedImage(flowChartGraph, null, 1, Color.WHITE, false, null);
		con.getASTModel().setMarkedNode(backUp);
		render();
		return  img;
	}
}
