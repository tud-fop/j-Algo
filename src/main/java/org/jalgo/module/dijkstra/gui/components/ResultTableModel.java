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
import org.jalgo.module.dijkstra.model.Node;
import org.jalgo.module.dijkstra.model.State;

/**
 * This class defines the <code>TableModel</code> for the result table in
 * algorithm mode.
 * 
 * @author Alexander Claus
 */
public class ResultTableModel
extends AbstractTableModel
implements Observer {

	private List<String> targetNodes;
	private List<String> shortestPaths;
	private List<String> pathLengths;
	private List<List<String>> tableContent;

	private static final String[] columnNames = new String[] {
		Messages.getString("dijkstra", //$NON-NLS-1$
			"AlgorithmResultTableComposite.Target_node"),//$NON-NLS-1$
		Messages.getString("dijkstra", //$NON-NLS-1$
			"AlgorithmResultTableComposite.Shortest_path"), //$NON-NLS-1$;
		Messages.getString("dijkstra", //$NON-NLS-1$
			"AlgorithmResultTableComposite.Path_length")}; //$NON-NLS-1$;

	public ResultTableModel(Controller controller) {
		targetNodes = new LinkedList<String>();
		shortestPaths = new LinkedList<String>();
		pathLengths = new LinkedList<String>();
		tableContent = new ArrayList<List<String>>();
		tableContent.add(targetNodes);
		tableContent.add(shortestPaths);
		tableContent.add(pathLengths);

		controller.addObserver(this);
	}

	public int getRowCount() {
		return targetNodes.size();
	}

	public int getColumnCount() {
		return 3;
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
			targetNodes.clear();
			shortestPaths.clear();
			pathLengths.clear();

			Iterator iter = dj.getGraph().getNodeList().iterator();
			dj.getBorderStates().iterator();
			while (iter.hasNext()) {
				Node node = (Node)iter.next();
				if (node != dj.getGraph().getStartNode()) {
					targetNodes.add(node.getLabel());
					String strPath = node.getShortestPath();
					if (strPath.length() > 0) shortestPaths.add(
						"("	+ node.getShortestPath() + "," + //$NON-NLS-1$ //$NON-NLS-2$
						node.getIndex() + ")"); //$NON-NLS-1$
					else shortestPaths.add("");
					if (node.getDistance() > 0) pathLengths.add(
						"" + node.getDistance()); //$NON-NLS-1$
					else pathLengths.add("");
				}
			}
			fireTableDataChanged();
		}
	}
}