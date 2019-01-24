package org.jalgo.module.dijkstra.gui.components;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import org.jalgo.main.gui.event.StatusLineUpdater;
import org.jalgo.main.util.Messages;
import org.jalgo.module.dijkstra.actions.ActionException;
import org.jalgo.module.dijkstra.actions.ApplyEdgeListAction;
import org.jalgo.module.dijkstra.gui.Controller;
import org.jalgo.module.dijkstra.model.Edge;
import org.jalgo.module.dijkstra.model.Graph;
import org.jalgo.module.dijkstra.util.DefaultExceptionHandler;

/**
 * The class <code>MatrixPanel</code> provides an editable view for the
 * adjacency matrix.
 * 
 * @author Alexander Claus
 */
public class MatrixPanel
extends JPanel
implements Observer, KeyListener, ActionListener, FocusListener {

	private final Controller controller;
	private JTextField[][] textFields;
	private HashMap<JTextField, JTextField> symmetrics;

	public MatrixPanel(Controller controller) {
		super();
		this.controller = controller;
		setBorder(BorderFactory.createTitledBorder(new EtchedBorder(),
			Messages.getString("dijkstra", "MatrixPanel.Distance_matrix"))); //$NON-NLS-1$ //$NON-NLS-2$
		// the following is for optimizing layout under linux, otherwise the
		// edgelist is not really shown
		if (System.getProperty("os.name").toLowerCase().startsWith("linux"))
			setLayout(new GridLayout(10, 10, 2, 2));
		else setLayout(new GridLayout(10, 10, 5, 5));
		//create columns headers
		add(new JLabel());
		for (int i=1; i<10; i++)
			add(new JLabel(""+i, SwingConstants.CENTER));
		//create rows
		textFields = new JTextField[9][9];
		for (int y=0; y<9; y++) {
			add(new JLabel(""+(y+1), SwingConstants.CENTER));
			for (int x=0; x<9; x++) {
				textFields[y][x] = new JTextField();
				textFields[y][x].putClientProperty("X",x);
				textFields[y][x].putClientProperty("Y",y);
				textFields[y][x].setToolTipText("("+(y+1)+","+(x+1)+")");
				if (y==x) {
					textFields[y][x].setText("0"); //$NON-NLS-1$
					textFields[y][x].setEditable(false);
				}
				textFields[y][x].addKeyListener(this);
				textFields[y][x].addActionListener(this);
				textFields[y][x].addFocusListener(this);				
				add(textFields[y][x]);
			}
		}
		// build a mapping between symmetric fields
		symmetrics = new HashMap<JTextField, JTextField>();
		for (int y=0; y<9; y++) {
			for (int x=0; x<9; x++) {
				if (x == y) continue;
				symmetrics.put(textFields[y][x], textFields[x][y]);
			}
		}
		setToolTipText(Messages.getString(
				"dijkstra", "MatrixPanel.Tooltip")); //$NON-NLS-1$ //$NON-NLS-2$
		addMouseListener(StatusLineUpdater.getInstance());

		controller.addObserver(this);
	}

	/**
	 * the getEdgeList() method returns the actual graph() as an EdgeList using
	 * entries of the matrix
	 * @return String with Syntax of EdgeList
	 * 
	 * @author Steven Voigt
	 */
	public String getEdgeList() {
		String edgeList = ""; //$NON-NLS-1$
		for (int m = 0; m < 9; m++) {
			if (nodeExist(m + 1)) {
				for (int n = 0; n < 9; n++) {
					boolean notminus = (!textFields[n][m].getText().equals(
						"-")); //$NON-NLS-1$
					boolean notnull = (!textFields[n][m].getText().equals(
						"")); //$NON-NLS-1$
					if (notminus && !(n == m) && notnull) {
						edgeList = edgeList
							+ "(" + (m + 1) + "," + textFields[n][m].getText() //$NON-NLS-1$ //$NON-NLS-2$
							+ "," + (n + 1) + "),"; //$NON-NLS-1$ //$NON-NLS-2$
					}
					if (!notminus) edgeList = edgeList
						+ "(" + (m + 1) + "," + "1," + (m + 1) + "),"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				}
			}
		}
		if (edgeList.endsWith(",")) //$NON-NLS-1$
		edgeList = edgeList.substring(0, edgeList.length() - 1);
		return edgeList;
	}

	/**
	 * check existing of a node if nodeexist
	 * @return boolean true else false
	 * 
	 * @author Steven Voigt
	 */
	protected boolean nodeExist(int nodeNr) {
		for (int i=0; i<nodeNr; i++)
			if (textFields[i][nodeNr-1].getText().equals("")) //$NON-NLS-1$
			return false;
		return true;
	}

	/**
	 * Updates the contents of all textfields.
	 */
	public void update(Observable o, Object arg) {
		if (o == null) return;

		Graph g = ((Controller)o).getGraph();
		int nodeCount = g.getNodeList().size()-1;
		for (int y=0; y<9; y++) {
			for (int x=0; x<9; x++) {
				if (x == y) continue;
				if (y > nodeCount ||
					x > nodeCount) textFields[y][x].setText(null);
				else textFields[y][x].setText("-"); //$NON-NLS-1$
			}
		}
		for (Edge edge : g.getEdgeList()) {
			int y = edge.getStartNodeIndex() - 1;
			int x = edge.getEndNodeIndex() - 1;
			if ((y>=0) && (y<=8) && (x>=0) && (x<=8)) {
				textFields[y][x].setText("" + edge.getWeight()); //$NON-NLS-1$
				textFields[x][y].setText("" + edge.getWeight()); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Asserts, that only integers and minus ('-') are accepted. Other keys
	 * will be ignored.
	 */
	public void keyTyped(KeyEvent e) {
		JTextField source = (JTextField)e.getSource();
		// check for maximum of 2 characters
		if (source.getText().length() >= 2) e.consume();
		// check that only minus is accepted, when field is empty
		if (e.getKeyChar() == '-' && source.getText().length() > 0) e.consume();
		// check that only numbers an minus is accepted
		if ((e.getKeyChar() < 48 || e.getKeyChar() > 57) &&
			e.getKeyChar() != '-') e.consume();
	}

	public void keyPressed(KeyEvent e) {
		// this method has no effect
	}

	/**
	 * Validates the symmetric field to the field, the user typed in. Checks, if
	 * all nodes with smaller indices exist and fill the according fields, if
	 * necessary. Finally this method updates the graph structure.
	 */
	public void keyReleased(KeyEvent e) {
		// if key is number or minus, copy value to the symmetric field
		if ((e.getKeyChar() >= 48 && e.getKeyChar() <= 57) ||
			e.getKeyChar() == '-' ||
			e.getKeyCode() == KeyEvent.VK_DELETE ||
			e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			JTextField source = (JTextField)e.getSource();
			if (source.getText().length() == 0) return;

			// check that all nodes with smaller index exist
			int nodeIndex = Math.max(
				(Integer)source.getClientProperty("X"),
				(Integer)source.getClientProperty("Y"));
			for (int i=0; i<nodeIndex; i++) {
				if (textFields[0][i].getText().length() == 0)
					for (int j=0; j<i; j++) {
						textFields[i][j].setText("-");
						textFields[j][i].setText("-");
					}
			}
			// check that node with current index exist
			for (int i=0; i<nodeIndex; i++) {
				if (textFields[i][nodeIndex].getText().length() == 0) {
					if (i != (Integer)source.getClientProperty("X"))
						textFields[nodeIndex][i].setText("-");
					if (i != (Integer)source.getClientProperty("Y"))
						textFields[i][nodeIndex].setText("-");
				}
			}
			getSymmetricFieldFor(source).setText(source.getText());
			fireChangedEvent();
		}
	}

	/**
	 * This method is called, when a textfield losts the focus or when the
	 * user presses enter or simply when the user presses a key. It causes to
	 * apply the changes.
	 */
	private void fireChangedEvent() {
		try {
			new ApplyEdgeListAction(controller, getEdgeList());
		}
		catch (ActionException exc) {
			new DefaultExceptionHandler(exc);
		}
	}

	private JTextField getSymmetricFieldFor(JTextField field) {
		return symmetrics.get(field);
	}

	/**
	 * Updates the graph structure, when ENTER is pressed. So also empty matrix
	 * fields can be validated.
	 */
	public void actionPerformed(ActionEvent e) {
		fireChangedEvent();
	}

	public void focusGained(FocusEvent e) {
		// this method has no effect
	}

	/**
	 * Updates the graph structure, when the user clicks away. So also empty
	 * matrix fields can be validated.
	 */
	public void focusLost(FocusEvent e) {
		fireChangedEvent();
	}
}