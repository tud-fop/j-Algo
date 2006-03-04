package org.jalgo.module.dijkstra.gui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import org.jalgo.main.util.Messages;
import org.jalgo.module.dijkstra.actions.ActionException;
import org.jalgo.module.dijkstra.actions.ApplyNodeListAction;
import org.jalgo.module.dijkstra.gui.Controller;
import org.jalgo.module.dijkstra.util.DefaultExceptionHandler;

/**
 * The <code>NodeListPanel</code> provides a textfield to edit the nodelist for
 * the graph.
 * 
 * @author Alexander Claus
 */
public class NodeListPanel
extends JPanel
implements Observer {

	private final Controller controller;
	private JTextField textField;

	public NodeListPanel(Controller controller) {
		super();
		this.controller = controller;
		setBorder(BorderFactory.createTitledBorder(new EtchedBorder(),
			Messages.getString("dijkstra", "NodeListPanel.Node_list"))); //$NON-NLS-1$ //$NON-NLS-2$
		setLayout(new BorderLayout());
		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireChangedEvent();
			}
		});
		textField.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				fireChangedEvent();
			}
		});
		add(textField, BorderLayout.CENTER);
		setMaximumSize(new Dimension(
			getMaximumSize().width, getMinimumSize().height));

		controller.addObserver(this);
	}

	/**
	 * This method is called, when this component lost the focus or when the
	 * user presses enter. It causes to apply the changes.
	 */
	void fireChangedEvent() {
		String strText = textField.getText();
		try {
			new ApplyNodeListAction(controller, strText);
		}
		catch (ActionException ex) {
			new DefaultExceptionHandler(ex);
			textField.setText(strText);
		}		
	}

	/**
	 * Updates the content of the textfiel.
	 */
	public void update(Observable o, Object arg) {
		if (o == null) return;

		textField.setText(((Controller)o).getGraph().getNodeListText());
	}
}