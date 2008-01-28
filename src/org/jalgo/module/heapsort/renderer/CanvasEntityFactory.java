/**
 * 
 */
package org.jalgo.module.heapsort.renderer;

import java.awt.Point;

/**
 * <p>This interface defines the canvas entity classes supported
 * by the system, which makes it sort of a reference. If you add
 * a new canvas entity class, this has to be reflected here.</p>
 * 
 * @author mbue
 */
public interface CanvasEntityFactory {
	CanvasEntity createRoot();
	Node createNode(Point pos, String label);
	Text createText(String text, int width, int height);
	MarkingRect createMarkingRect();
	SequenceElement createSequenceElement(Point pos, String label);
	Edge createEdge(Point from, Point to);
}
