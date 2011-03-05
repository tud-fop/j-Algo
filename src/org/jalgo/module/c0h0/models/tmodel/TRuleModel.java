package org.jalgo.module.c0h0.models.tmodel;

import org.jalgo.module.c0h0.controller.Controller;
import org.jalgo.module.c0h0.models.Performer;
import org.jalgo.module.c0h0.models.ast.tools.DFSIterator;

/**
 * saves the address-rule-assignment
 * 
 * @author mathias.kaufmann
 * 
 */
public class TRuleModel implements Performer {
	private TGeneratingVisitor visitor;
	private Controller controller;
	private String markedNode;
	private boolean active = false;
	
	/**
	 * @param controller
	 */
	public TRuleModel(Controller controller) {
		this.controller = controller;
		visitor = new TGeneratingVisitor();
	}
	
	/**
	 * current sttrans-rule from marked node
	 * 
	 * @return the rule
	 */
	public String getSTTrans() {
		return visitor.getRuleByAddress(markedNode);
	}
	
	/**
	 * update marked node
	 */
	public void setSTTrans() {
		markedNode = controller.getASTModel().getMarkedNode();
	}
	
	/**
	 * set marked node
	 * @param markedNode
	 */
	public void setSTTrans(String markedNode) {
		this.markedNode = markedNode;
	}
	/**
	 * creates the assignment
	 */
	public void generate() {
		for (DFSIterator bit = new DFSIterator(controller.getASTModel()); bit.hasNext();) {
			bit.next().accept(visitor);
		}
	}
	public boolean isDone() {
		// stateless, beeing in every state, thus return true
		return true;
	}

	public boolean isClear() {
		// stateless, in case of doubt, i'm cleared too
		return true;
	}

	public void performStep() {
		// stateless
	}
	
	public void performAll() {
		// stateless
	}

	public void undoStep() {
		// stateless
	}

	public void undoAll() {
		// stateless
	}

	public void clear() {
		// stateless
	}

	public void setActive(boolean a) {
		active = a;
	}
	public boolean isActive() {
		return active;
	}
}
