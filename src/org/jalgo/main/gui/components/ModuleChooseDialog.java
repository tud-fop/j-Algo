package org.jalgo.main.gui.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jalgo.main.IModuleInfo;
import org.jalgo.main.JAlgoMain;
import org.jalgo.main.gui.JAlgoWindow;
import org.jalgo.main.util.Messages;

/**
 * @author Matthias Schmidt
 */
public class ModuleChooseDialog
extends JDialog {

	JAlgoWindow appWin;
	private static ModuleChooseDialog instance;
	
	private JLabel contentLabel;
	private JList moduleList;
	private JButton okButton;
	
	//---------
	
	private ModuleChooseDialog(JFrame parent) {
		super(parent, Messages.getString("main", "New.Title"), true);
		
		if (!(parent instanceof JAlgoWindow))
			System.err.println("The ModuleChooseDialog needs to be opened by the JAlgoWindow.");
		else appWin = (JAlgoWindow) parent;

		
		int space = 5;
		
		setLayout(new BorderLayout());
		JPanel westPane = new JPanel();
		westPane.setLayout(new BoxLayout(westPane, BoxLayout.Y_AXIS));
		
		JLabel listLabel = new JLabel(Messages.getString("main", "New.ModuleList"));
		listLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);		
		westPane.add(Box.createVerticalStrut(space));
		westPane.add(listLabel);
		
		moduleList = new JList(getModuleList());
		moduleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		moduleList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("synthetic-access")
			public void valueChanged(ListSelectionEvent e) {
				updateInfo(moduleList.getSelectedIndex());
				okButton.setEnabled(true);
			}
		});
		JScrollPane listScrollPane = new JScrollPane(moduleList,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		westPane.add(listScrollPane);
		westPane.add(Box.createVerticalStrut(space));		

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));
		//ok-button
		okButton = new JButton(Messages.getString("main", "DialogConstants.Ok"));
		okButton.addActionListener(new ActionListener() {
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(ActionEvent e) {
					dispose();
					JAlgoMain.getInstance().newInstance(moduleList.getSelectedIndex());
			}
		});
		okButton.setEnabled(false);
		okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		//open-button
		JButton openButton = new JButton(Messages.getString("main","ui.Open_file"));
		openButton.addActionListener(new ActionListener() {
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(ActionEvent e) {
				dispose();
				appWin.showOpenDialog(true, false);
			}
		});
		openButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		//cancel-button
		JButton cancelButton = new JButton(Messages.getString("main", "DialogConstants.Cancel"));
		cancelButton.addActionListener(new ActionListener() {
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		buttonPane.add(okButton);
		buttonPane.add(Box.createVerticalStrut(space));
		buttonPane.add(openButton);
		buttonPane.add(Box.createVerticalStrut(space));
		buttonPane.add(cancelButton);
		buttonPane.add(Box.createVerticalStrut(space));
		westPane.add(buttonPane);
		
		add(westPane,BorderLayout.WEST);
		
		
		contentLabel = new JLabel("", SwingConstants.CENTER);
		// the following is for optimizing layout under linux, otherwise
		// some of the text does not fit into dialog
		contentLabel.setFont(new Font(contentLabel.getFont().getName(),contentLabel.getFont().getStyle(),11));
		contentLabel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createCompoundBorder(
				new EmptyBorder(5, 5, 5, 5),
				new EtchedBorder()),
			new EmptyBorder(5, 5, 5, 5)));
		contentLabel.setText(Messages.getJAlgoInfoAsHTML());


		add(contentLabel,BorderLayout.CENTER);
		

		pack();
		setLocationRelativeTo(parent);
	}
	
	
	public static void open(JFrame parent) {
		if (instance == null) instance = new ModuleChooseDialog(parent);
		instance.setSize(430, 300);
		instance.setLocationRelativeTo(parent);
		instance.setVisible(true);
	}
	
	private static String[] getModuleList(){
		String[] back = new String[JAlgoMain.getInstance().getKnownModuleInfos().size()];
		int index = 0;
		Iterator<IModuleInfo> it = JAlgoMain.getInstance().getKnownModuleInfos().iterator();
		
		while(it.hasNext()){
			back[index] = it.next().getName();
			index++;
		}
		return back;
	}
	
	private static void updateInfo(int index){		
		instance.contentLabel.removeAll();
		instance.contentLabel.setText(Messages.getModuleInfoAsHTML(index));
		//contentLabel.updateUI();
	}
}