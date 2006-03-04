package org.jalgo.module.dijkstra.gui.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import org.jalgo.main.util.Messages;
import org.jalgo.module.dijkstra.gui.Controller;
import org.jalgo.module.dijkstra.model.BorderState;
import org.jalgo.module.dijkstra.model.Edge;
import org.jalgo.module.dijkstra.model.Node;
import org.jalgo.module.dijkstra.model.State;

/**
 * This class defines the <code>TableModel</code> for the calculation table in
 * algorithm mode.
 * 
 * @author Alexander Claus
 */
public class CalculationTableModel
extends AbstractTableModel
implements Observer {

	private List<String> chosenNodes;
	private List<String> fringeNodes;
	private List<List<String>> tableContent;

	private static final String[] columnNames = new String[] {
		Messages.getString("dijkstra", //$NON-NLS-1$
			"AlgorithmCalculationTableComposite.Chosen"),//$NON-NLS-1$
		Messages.getString("dijkstra", //$NON-NLS-1$
			"AlgorithmCalculationTableComposite.Fringe_node")}; //$NON-NLS-1$;

	public CalculationTableModel(Controller controller) {
		chosenNodes = new LinkedList<String>();
		fringeNodes = new LinkedList<String>();
		tableContent = new ArrayList<List<String>>();
		tableContent.add(chosenNodes);
		tableContent.add(fringeNodes);

		controller.addObserver(this);
	}

	public int getRowCount() {
		return chosenNodes.size();
	}

	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return tableContent.get(columnIndex).get(rowIndex);
	}

	/**
	 * Updates the table content.
	 */
	public void update(Observable o, Object arg) {
		Controller controller = (Controller)o;
		// only react, when in algorithm mode
		if (controller.getEditingMode() != Controller.MODE_ALGORITHM) return;

		State dj = controller.getState(controller.getCurrentStep());
		if (dj != null) {
			if (dj.getBorderStates() == null) return;

			chosenNodes.clear();
			fringeNodes.clear();

			Iterator iter = dj.getBorderStates().iterator();
			while (iter.hasNext()) {
				BorderState bdstate = (BorderState)iter.next();

				// Write chosen edge. As we are given a node, try to get the
				// edge from the graph.
				Node chosen = bdstate.getChosen();
				Node realChosen = dj.getGraph().findNode(chosen.getIndex());
				Node pred = realChosen.getPredecessor();

				if (pred != null) {
					Edge chosenEdge = dj.getGraph().findEdge(realChosen, pred);
					chosenNodes.add("" + chosenEdge.getAlgoText(true, bdstate)); //$NON-NLS-1$
				}
				else {
					int index = chosen.getIndex();
					chosenNodes.add("(" + index + ",0," + index + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				Iterator edgeIter = bdstate.getBorder().iterator();
				String strText = ""; //$NON-NLS-1$
				while (edgeIter.hasNext()) {
					Edge ed = (Edge)edgeIter.next();
					if (strText.length() > 0) strText += ", "; //$NON-NLS-1$
					strText += ed.getAlgoText(true, bdstate);
				}
				fringeNodes.add(strText);
			}
			fireTableDataChanged();
		}
	}
}