package org.jalgo.module.c0h0.models.flowchart;

import java.util.List;

import org.jalgo.module.c0h0.controller.Controller;
import org.jalgo.module.c0h0.models.Generator;
import org.jalgo.module.c0h0.models.Performer;
import org.jalgo.module.c0h0.models.ast.tools.DFSIterator;
import org.jalgo.module.c0h0.views.TerminalView;

/**
 * contains the flowchart and coordinates visibility
 * 
 * @author Philipp Geissler
 * 
 */
public class FlowChartModel implements Performer, Generator {
	private FcGeneratingVisitor fcVisitor;
	private List<Bundle> elementList;
	private int visibleTo;
	private Controller controller;
	private boolean active = false;

	public FlowChartModel(Controller controller) {
		this.controller = controller;
		fcVisitor = new FcGeneratingVisitor();
		visibleTo = 0;
	}

	/**
	 * @return list of bundles with all flowchart elements
	 */
	public List<Bundle> getElements() {
		return elementList;
	}

	/**
	 * @return index of last visible bundle in flowchart list
	 */
	public int getVisibleTo() {
		return visibleTo;
	}

	public void performStep() {
		visibleTo++;
		if (visibleTo > elementList.size() + 1)
			visibleTo = elementList.size() + 1;
		markNode();
		TerminalView.println("flowChart "+visibleTo+" of "+ elementList.size());
	}

	public void performAll() {
		visibleTo = elementList.size()+1;
		markNode();
		TerminalView.println("flowChart "+visibleTo+" of "+ elementList.size());
	}

	public void undoStep() {
		if (visibleTo > 0)
			visibleTo--;
		markNode();
		TerminalView.println("flowChart "+visibleTo+" of "+ elementList.size());
	}

	public void undoAll() {
		visibleTo = 0;
		markNode();
		TerminalView.println("flowChart "+visibleTo+" of "+ elementList.size());
	}

	public void clear() {
		fcVisitor = new FcGeneratingVisitor();
		visibleTo = 0;
		markNode();
	}

	public boolean isClear() {
		return visibleTo == 0;
	}

	public boolean isDone() {
		return visibleTo == elementList.size()+1;
	}

	public void generate() {
		fcVisitor = new FcGeneratingVisitor();
		DFSIterator iter = new DFSIterator(controller.getASTModel());
		while (iter.hasNext()) {
			iter.next().accept(fcVisitor);
		}
		elementList = fcVisitor.getElementList(controller.getASTModel().getProgram().getPrintf());
	}

	/**
	 * marks the current node
	 */
	public void markNode() {
		if (visibleTo > 0 && elementList.size()>=visibleTo)
			controller.markNode(elementList.get(visibleTo - 1).getAddress());
		else
			controller.markNode("");
	}
	public void setActive(boolean a) {
		active = a;
	}

	public boolean isActive() {
		return active;
	}
}