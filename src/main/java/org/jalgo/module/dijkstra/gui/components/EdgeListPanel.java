package org.jalgo.module.dijkstra.gui.components;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;

import org.jalgo.main.util.Messages;
import org.jalgo.module.dijkstra.actions.ActionException;
import org.jalgo.module.dijkstra.actions.ApplyEdgeListAction;
import org.jalgo.module.dijkstra.gui.Controller;
import org.jalgo.module.dijkstra.util.DefaultExceptionHandler;

/**
 * The <code>EdgeListPanel</code> provides a textfield and a button to edit the
 * edgelist for the graph.
 * 
 * @author Alexander Claus
 */
public class EdgeListPanel
extends JPanel
implements Observer {

	private final Controller controller;
	private JTextArea textArea;

	public EdgeListPanel(Controller controller) {
		super();
		this.controller = controller;
		setBorder(BorderFactory.createTitledBorder(new EtchedBorder(),
			Messages.getString("dijkstra", "EdgeListPanel.Edge_list"))); //$NON-NLS-1$ //$NON-NLS-2$
		setLayout(new BorderLayout(2, 2));
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(textArea,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		textArea.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				fireChangedEvent();
			}
		});
		add(scrollPane, BorderLayout.CENTER);
		JPanel buttonPane = new JPanel(new BorderLayout());
		JButton applyButton = new JButton(Messages.getString(
			"dijkstra", "EdgeListPanel.Apply")); //$NON-NLS-1$ //$NON-NLS-2$
		applyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireChangedEvent();
			}
		});
		buttonPane.add(applyButton, BorderLayout.EAST);
		add(buttonPane, BorderLayout.SOUTH);

		controller.addObserver(this);
	}

	/**
	 * This method is called, when this component lost the focus or when the
	 * user presses the apply-button. It causes to apply the changes.
	 */
	void fireChangedEvent() {
		String strText = textArea.getText();
		try {
			new ApplyEdgeListAction(controller, strText);
		}
		catch (ActionException ex) {
			new DefaultExceptionHandler(ex);
			textArea.setText(strText);
		}
	}

	/**
	 * Updates the content of the text area.
	 */
	public void update(Observable o, Object arg) {
		if (o == null) return;

		textArea.setText(((Controller)o).getGraph().getEdgeListText());
	}
}