package org.jalgo.module.app.view.graph;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import org.jalgo.module.app.controller.GraphActionListener.EditMode;
import org.jalgo.module.app.core.dataType.DataType;
import org.jalgo.module.app.core.graph.Edge;
import org.jalgo.module.app.core.graph.Node;
import org.jalgo.module.app.view.InterfaceConstants;

/**
 * The graphical representation of an edge in the user interface. This class
 * wraps an object from type Edge.
 */
public class EdgeElement extends GraphElement {

	private static final long serialVersionUID = -4469336801616982360L;
	private Edge edge;
	private EdgeShape shape;

	// attributes for the WeightEditing
	private JTextField input;
	private HighlightPainter mistakePainter;
	private Object currentHighlight;
	private FontRenderContext renderContext;
	
	private boolean isWeightEditing;
	private DataType oldweight;
	
	private static boolean beamerMode;

	@SuppressWarnings("serial")
	public class EdgeTextField extends JTextField {
	}
	
	public EdgeElement(Edge edge) {
		this.edge = edge;

		beamerMode = false;
		
		highlightState = HighlightState.NONE;
		renderContext = new FontRenderContext(new AffineTransform(), true, false);
		isWeightEditing = false;
		
		input = new EdgeTextField();
		input.setVisible(false);
	}

	@Override
	public boolean contains(Point2D point) {
		return (shape.arrow.contains(point) || shape.labelShape.contains(point) || shape.lineShape
				.contains(point));
	}

	@Override
	public void paint(Graphics2D g) {
		calculateShape();
		if (shape == null)
			return;

		// Draw line & arrow
		g.setColor(InterfaceConstants
				.borderColorForHighlightState(highlightState));
		g.setStroke(new BasicStroke(InterfaceConstants.getEdgeWidth(beamerMode)));

		g.fill(shape.lineShape);
		
		g.fill(shape.arrow);
		
		
		// Draw weight box
		if (!isWeightEditing) {
			g.setColor(InterfaceConstants
					.fillColorForHighlightStateEdge(highlightState));
				g.fill(shape.labelShape);
	
			// Draw weight
			Font font;
	
			font = InterfaceConstants.getGraphWeightFont(beamerMode);
			g.setColor(InterfaceConstants
					.textColorForHighlightStateEdge(highlightState));
			g.setFont(font);
			
			g.drawString(getEdgeWeight(),
					(float) (shape.labelBounds.getCenterX() - shape.labelSize
							.getWidth() / 2), (float) (shape.labelBounds
							.getCenterY()
							- shape.metrics.getHeight() / 2 + shape.metrics
							.getAscent()));
		}
	}

	private class EdgeShape {
		public Point2D beginPoint, endPoint;
		public Rectangle2D labelSize, labelBounds;
		public LineMetrics metrics;
		public GeneralPath arrow, lineShape;
		public Shape labelShape;
	}

	private void calculateShape() {
		Point2D beginPoint, midPoint, endPoint;
		boolean hasInverse;

		// Get necessary data
		beginPoint = new Point2D.Float();
		endPoint = new Point2D.Float();

		beginPoint.setLocation(getBeginNodePoint());
		endPoint.setLocation(getEndNodePoint());

		if (getBeginNode() != null && getEndNode() != null)
			hasInverse = (getComponent().getGraphActionListener().getGraph()
					.getEdge(getEndNode(), getBeginNode()) != null);
		else
			hasInverse = false;

		// Calculate mid point
		AffineTransform rotation;
		Point2D offsetPoint;
		float distance;
		double dx, dy;

		midPoint = new Point2D.Float();
		midPoint.setLocation((beginPoint.getX() + endPoint.getX()) / 2,
				(beginPoint.getY() + endPoint.getY()) / 2);

		// get delta
		dx = endPoint.getX() - beginPoint.getX();
		dy = endPoint.getY() - beginPoint.getY();

		// offset point if it has an inverse
		if (hasInverse) {
			distance = InterfaceConstants.getDoubleEdgeDistance(beamerMode) / 2;
			offsetPoint = rotateVerticalDistance(getRotation(dx, dy), distance);
			midPoint.setLocation(midPoint.getX() + offsetPoint.getX(), midPoint
					.getY()
					+ offsetPoint.getY());
		}

		// Fix begin point
		double x, y;
		float nodeRadius;

		nodeRadius = getBeginNodeRadius();

		dx = midPoint.getX() - beginPoint.getX();
		dy = midPoint.getY() - beginPoint.getY();

		x = dx * nodeRadius / Math.sqrt(dx * dx + dy * dy);
		y = dy * nodeRadius / Math.sqrt(dx * dx + dy * dy);

		beginPoint = new Point2D.Float((float) (beginPoint.getX() + x),
				(float) (beginPoint.getY() + y));

		// Fix end point
		dx = endPoint.getX() - midPoint.getX();
		dy = endPoint.getY() - midPoint.getY();

		nodeRadius = getEndNodeRadius();
		nodeRadius += InterfaceConstants.getEdgeArrowLength(beamerMode);
		x = dx * nodeRadius / Math.sqrt(dx * dx + dy * dy);
		y = dy * nodeRadius / Math.sqrt(dx * dx + dy * dy);

		endPoint = new Point2D.Float((float) (endPoint.getX() - x),
				(float) (endPoint.getY() - y));

		// Create arrow
		AffineTransform move;
		GeneralPath arrow;

		rotation = getRotation(dx, dy);
		arrow = getArrowShape();
		arrow.transform(rotation);

		move = new AffineTransform();
		move.translate(endPoint.getX(), endPoint.getY());
		arrow.transform(move);

		// Create line shape
		float xOffset[], yOffset[];
		GeneralPath lineShape;

		xOffset = new float[3];
		yOffset = new float[3];

		distance = InterfaceConstants.getEdgeWidth(beamerMode) / 2;

		rotation = getRotation(midPoint.getX() - beginPoint.getX(), midPoint
				.getY()
				- beginPoint.getY());
		offsetPoint = rotateVerticalDistance(rotation, distance);
		xOffset[0] = (float) offsetPoint.getX();
		yOffset[0] = (float) offsetPoint.getY();

		rotation = getRotation(endPoint.getX() - beginPoint.getX(), endPoint
				.getY()
				- beginPoint.getY());
		offsetPoint = rotateVerticalDistance(rotation, distance);
		xOffset[1] = (float) offsetPoint.getX();
		yOffset[1] = (float) offsetPoint.getY();

		rotation = getRotation(endPoint.getX() - midPoint.getX(), endPoint
				.getY()
				- midPoint.getY());
		offsetPoint = rotateVerticalDistance(rotation, distance);
		xOffset[2] = (float) offsetPoint.getX();
		yOffset[2] = (float) offsetPoint.getY();

		lineShape = new GeneralPath();

		lineShape.moveTo((float) beginPoint.getX() - xOffset[0],
				(float) beginPoint.getY() - yOffset[0]);
		lineShape.curveTo((float) midPoint.getX() - xOffset[1],
				(float) midPoint.getY() - yOffset[1], (float) midPoint.getX()
						- xOffset[1], (float) midPoint.getY() - yOffset[1],
				(float) endPoint.getX() - xOffset[2], (float) endPoint.getY()
						- yOffset[2]);

		lineShape.lineTo((float) endPoint.getX() + xOffset[2], (float) endPoint
				.getY()
				+ yOffset[2]);
		lineShape.curveTo((float) midPoint.getX() + xOffset[1],
				(float) midPoint.getY() + yOffset[1], (float) midPoint.getX()
						+ xOffset[1], (float) midPoint.getY() + yOffset[1],
				(float) beginPoint.getX() + xOffset[0], (float) beginPoint
						.getY()
						+ yOffset[0]);

		lineShape.closePath();

		// Get label attributes
		Rectangle2D labelSize, labelBounds;
		LineMetrics metrics;
		float labelBorder;
		Shape labelShape;
		String label;
		Font font;

		if (!isWeightEditing)
			label = getEdgeWeight();
		else if (input.getText() != null)
			label = input.getText();
		else
			label = "";

		font = InterfaceConstants.getGraphWeightFont(beamerMode);
		labelBorder = InterfaceConstants.getEdgeWeightBorder(beamerMode);

		labelSize = font.getStringBounds(label, renderContext);
		metrics = font.getLineMetrics(label, renderContext);

		// Add border to label size
		labelBounds = (Rectangle2D) labelSize.clone();
		labelBounds.setFrame(labelBounds.getX() - labelBorder, labelBounds
				.getY()
				- labelBorder, labelBounds.getWidth() + 2 * labelBorder,
				labelBounds.getHeight() + 2 * labelBorder);

		// Calculate position
		x = midPoint.getX();
		y = midPoint.getY();

		labelBounds.setFrame(x - labelBounds.getWidth() / 2, y
				- labelBounds.getHeight() / 2, labelBounds.getWidth(),
				labelBounds.getHeight());

		// Adjust weight box
		if (highlightState != HighlightState.CURRENT) {
			Ellipse2D ellipse;

			ellipse = new Ellipse2D.Float();
			ellipse.setFrame(labelBounds);

			labelShape = ellipse;
		}
		else{
			labelShape = labelBounds;
		}

		// Create Shape object
		shape = new EdgeShape();

		shape.arrow = arrow;
		shape.beginPoint = beginPoint;
		shape.endPoint = endPoint;
		shape.labelSize = labelSize;
		shape.labelBounds = labelBounds;
		shape.metrics = metrics;
		shape.labelShape = labelShape;
		shape.lineShape = lineShape;
	
	}

	protected float getBeginNodeRadius() {
		return (InterfaceConstants.getNodeDiameter(beamerMode) + InterfaceConstants
				.getNodeBorderWidth(beamerMode) / 2) / 2;
	}

	protected float getEndNodeRadius() {
		return (InterfaceConstants.getNodeDiameter(beamerMode) + InterfaceConstants
				.getNodeBorderWidth(beamerMode) / 2) / 2;
	}

	protected Node getBeginNode() {
		return edge.getBegin();
	}

	protected Node getEndNode() {
		return edge.getEnd();
	}

	protected Point2D getBeginNodePoint() {
		return component.getScaledLocation(getBeginNode().getLocation());
	}

	protected Point2D getEndNodePoint() {
		return component.getScaledLocation(getEndNode().getLocation());
	}

	protected String getEdgeWeight() {
		return edge.getWeight().toString();
	}

	private static AffineTransform getRotation(double dx, double dy) {
		AffineTransform rotation;

		rotation = new AffineTransform();
		if (dx >= 0)
			rotation.rotate(Math.atan(dy / dx));
		else
			rotation.rotate(Math.atan(dy / dx) + Math.PI);

		return rotation;
	}

	private static Point2D rotateVerticalDistance(AffineTransform rotation,
			double distance) {
		GeneralPath offset;

		offset = new GeneralPath();
		offset.moveTo(0, (float) distance);

		offset.transform(rotation);

		return offset.getCurrentPoint();
	}

	private static GeneralPath getArrowShape() {
		GeneralPath arrow;

		arrow = new GeneralPath();
		arrow.moveTo(InterfaceConstants.getEdgeArrowLength(beamerMode), 0);
		arrow.lineTo(0, -InterfaceConstants.getEdgeArrowWidth(beamerMode) / 2);
		arrow.lineTo(0, InterfaceConstants.getEdgeArrowWidth(beamerMode) / 2);
		arrow.closePath();

		return arrow;
	}

	public Edge getEdge() {
		return edge;
	}

	public void setEdge(Edge edge) {
		this.edge = edge;
	}

	public void beginWeightEditing() {

		isWeightEditing = true;
		oldweight = edge.getWeight();
		
		input.setVisible(true);
		input.setText(edge.getWeight().toString());
		input.setBounds(shape.labelBounds.getBounds());

		input.setFont(InterfaceConstants.getGraphWeightFont(beamerMode));
		input.setAlignmentX(0.5f);
		input.setAlignmentY(0.5f);
		input.setSelectionStart(0);
		input.setSelectionEnd(input.getText().length());
		input.setHighlighter(new DefaultHighlighter());

		component.add(input);
		component.validate();

		input.validate();
		input.grabFocus();

		input.addActionListener(new InputActionListener());
		input.getDocument().addDocumentListener(new CurrentDocumentListener());
		input.addFocusListener(new TextFieldFocusListener());

		mistakePainter = new UnderliningHighlightPainter(
				InterfaceConstants.wrongInputColor());
		currentHighlight = null;
		
		repaint();
		
		// getInputMethodRequests()

//		component.setIgnoreEvents(true);
	}

	public void endWeightEditing() {
		isWeightEditing = false;
		input.setVisible(false);

		component.update();
//		component.setIgnoreEvents(false);
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_DELETE
				|| e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			component.getGraphActionListener().removeEdge(edge);
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			beginWeightEditing();
			super.mouseClicked(e);
		} else {
			if(component.getGraphActionListener().getEditMode() == EditMode.EDITING_WEIGHT)
				beginWeightEditing();
			super.mouseClicked(e);
		}
	}

	private class CurrentDocumentListener implements DocumentListener {

		private void updateCheck() {
			String weightText;
			DataType weight;
			weightText = input.getText();
			weight = null;
			int width, height, x, y;
			
			// Update input field size
			calculateShape();
			
			Rectangle inputsize = shape.labelBounds.getBounds();
			width = (int) (inputsize.getWidth());
			height = (int) inputsize.getHeight();
			x = (int) inputsize.getX();
			y = (int )inputsize.getY();
						
			inputsize.setBounds(x, y, width, height);
			input.setBounds(inputsize);
			input.setHorizontalAlignment(JTextField.CENTER);
						
			repaint();
			
			// Test for wrong inputs
			try {
				weight = component.getGraphActionListener().getDataType()
						.newInstance();
			} catch (InstantiationException e1) {
				return;
			} catch (IllegalAccessException e2) {
				return;
			}
			
			
			
			if (currentHighlight != null) {
				input.getHighlighter().removeHighlight(currentHighlight);
				currentHighlight = null;
			}

			if (weight.setFromString(weightText)) {
				component.getGraphActionListener().alterEdgeWeight(edge, weight);
			}
			else {	
				component.getGraphActionListener().alterEdgeWeight(edge, oldweight);
				try {
					currentHighlight = input.getHighlighter().addHighlight(0,
								weightText.length(), mistakePainter);
				} catch (BadLocationException e1) {
				}
			}
		}

		public void changedUpdate(DocumentEvent event) {
			updateCheck();
		}

		public void insertUpdate(DocumentEvent event) {
			updateCheck();
		}

		public void removeUpdate(DocumentEvent event) {
			updateCheck();
		}
		
	}

	private class InputActionListener implements ActionListener {

		public void actionPerformed(ActionEvent ae) {
			endWeightEditing();
		}

	}
	
	private class TextFieldFocusListener implements FocusListener {

		public void focusGained(FocusEvent event) {
			
		}

		public void focusLost(FocusEvent event) {
			if (!(event.getOppositeComponent() instanceof EditToolbar.JSpecialCharButton))
				if (event.getSource() == input)
					endWeightEditing();
		}
		
	}

	public void updateBeamerMode(boolean beamerMode) {
		EdgeElement.beamerMode = beamerMode;
	}
	
}