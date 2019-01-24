/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Created on 09.05.2005
 */
package org.jalgo.module.dijkstra.actions;

/**
 * This class implements a simple stack. In this stack You can move forward and
 * backward. You can limit the stack size by providing an int in the
 * constructor. A value of zero means unlimited size.
 * 
 * @author Frank Staudinger
 */
// FIXME wieso kein Adapter zu org.jalgo.main.util.Stack (Stephan)
// wenn, dann java.util.Stack, org.jalgo.main.util.Stack ist gelöscht
// (Alexander)
public class ActionStack {

	final protected class Node {

		private Node m_PrevNode;
		private Node m_NextNode;
		private Action m_Action;

		public Node(Node PrevNode, Action action) {
			m_PrevNode = PrevNode;
			m_NextNode = null;
			m_Action = action;
		}

		public boolean hasNextNode() {
			return m_NextNode != null;
		}

		public boolean hasPrevNode() {
			return m_PrevNode != null;
		}

		public Action getAction() {
			return m_Action;
		}

		public Node getPrevNode() {
			return m_PrevNode;
		}

		public void setPrevNode(Node pNode) {
			m_PrevNode = pNode;
		}

		public Node getNextNode() {
			return m_NextNode;
		}

		public void setNextNode(Node pNode) {
			m_NextNode = pNode;
		}
	}

	protected int m_nMaxCount;
	protected int m_nCurrentCount;
	protected Node m_pHead;
	protected Node m_pTail;

	/**
	 * @param nMaxCount maximum of stack elements. A value of zero means
	 *            unlimited size.
	 */
	public ActionStack(int nMaxCount) {
		super();
		m_nCurrentCount = 0;
		m_pHead = null;
		m_pTail = null;
		m_nMaxCount = (nMaxCount >= 0) ? nMaxCount : 0;
	}

	/**
	 * @param pAction Reference to the action You want to add.
	 * @return Returns true if the action is in the stack, false if the stack is
	 *         full
	 */
	public boolean add(Action pAction) {
		boolean bCanInsert = ((m_nMaxCount <= 0) ? (true)
			: (m_nCurrentCount < m_nMaxCount));

		if (bCanInsert == true) {
			Node pNewNode = new Node(m_pTail, pAction);
			if (m_nCurrentCount == 0) m_pHead = pNewNode;
			if (m_pTail != null) {
				m_pTail.setNextNode(pNewNode);
			}
			m_pTail = pNewNode;
			m_nCurrentCount++;
		}
		else {
			// Stack ist voll -> die letzte Action fliegt raus
			Node pOldHead = m_pHead;
			m_pHead = pOldHead.getNextNode();
			m_nCurrentCount--;
			return add(pAction);
		}
		return bCanInsert;
	}

	/**
	 * @return Returns true if the stack pointer doesn't point to the top
	 *         element.
	 */
	public boolean canMoveNext() {
		if (m_nCurrentCount == 0) { return (m_pHead != null); }
		return m_pTail.hasNextNode();
	}

	/**
	 * @return Returns the next action in the stack or null.
	 */
	public Action moveNext() {
		if (canMoveNext() == false) return null;

		// Die erste Action wieder aus der Versenkung holen
		if ((m_nCurrentCount == 0) && (m_pHead != null)) {
			m_nCurrentCount++;
			m_pTail = m_pHead;
			return m_pHead.getAction();

		}

		m_pTail = m_pTail.getNextNode();
		m_nCurrentCount++;
		return m_pTail.getAction();
	}

	/**
	 * @return Returns true if the stack pointer doesn't point to the bottom
	 *         element.
	 */
	public boolean canMovePrevious() {
		return m_nCurrentCount != 0;
	}

	/**
	 * @return Returns the previous action in the stack or null.
	 */
	public Action movePrevious() {
		if (canMovePrevious() == false) return null;

		Action pAction = m_pTail.getAction();
		m_pTail = m_pTail.getPrevNode();
		m_nCurrentCount--;
		return pAction;
	}

	/**
	 * @return Returns the m_nCurrentCount field.
	 */
	public int getCount() {
		return m_nCurrentCount;
	}
}