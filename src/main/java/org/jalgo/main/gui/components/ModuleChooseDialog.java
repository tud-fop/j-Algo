package org.jalgo.main.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
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
import org.jalgo.main.util.Settings;

/**
 * The dialog to open a new module. 
 * @author Matthias Schmidt
 */
public class ModuleChooseDialog
extends JDialog {

	private static final long serialVersionUID = -4607723819515165988L;
	private JAlgoWindow appWin;
	private static ModuleChooseDialog instance;
	
	private JLabel contentLabel;
	private JList moduleList;
	private JButton okButton;
	private JCheckBox mcdOnStartup;
	
	private ModuleChooseDialog(JFrame parent) {
		super(parent, Messages.getString("main", "New.Title"), true);
		
		if (!(parent instanceof JAlgoWindow))
			System.err.println("The ModuleChooseDialog needs to be opened by the JAlgoWindow.");
		else appWin = (JAlgoWindow) parent;

		int space = 5;
		
		setLayout(new BorderLayout());
		
		JPanel overlayPane = new JPanel();
		overlayPane.setLayout(new BorderLayout());
		
		JPanel westPane = new JPanel();
		westPane.setLayout(new BoxLayout(westPane, BoxLayout.Y_AXIS));
		
		JLabel listLabel = new JLabel(Messages.getString("main", "New.ModuleList"));
		listLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);		
		westPane.add(Box.createVerticalStrut(space));
		westPane.add(listLabel);
		
		moduleList = new JList(getModuleList());
		moduleList.setCellRenderer(new MyCellRenderer());
		moduleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		moduleList.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("synthetic-access")
			public void valueChanged(ListSelectionEvent e) {
				updateInfo(moduleList.getSelectedIndex());
				okButton.setEnabled(true);
			}
		});
		moduleList.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("synthetic-access")
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) moduleSelected();
			}
		});
		moduleList.addKeyListener(new KeyAdapter() {
			@SuppressWarnings("synthetic-access")
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) moduleSelected();
			}
		});
		
		
		JScrollPane listScrollPane = new JScrollPane(moduleList,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		westPane.add(listScrollPane);
		westPane.add(Box.createVerticalStrut(space));		

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new GridLayout(3,1));
		//ok-button
		okButton = new JButton(Messages.getString("main", "DialogConstants.Ok"));
		okButton.addActionListener(new ActionListener() {
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(ActionEvent e) {
				moduleSelected();
			}
		});
		okButton.setEnabled(false);
		okButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		//open-button
		JButton openButton = new JButton(Messages.getString("main", "ui.Open_file"));
		openButton.addActionListener(new ActionListener() {
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(ActionEvent e) {
				dispose();
				appWin.showOpenDialog(true, false);
			}
		});
		openButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		//cancel-button
		JButton cancelButton = new JButton(Messages.getString("main", "DialogConstants.Cancel"));
		cancelButton.addActionListener(new ActionListener() {
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancelButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		cancelButton.setFocusCycleRoot(true);
		
		buttonPane.add(okButton);
		buttonPane.add(openButton);
		buttonPane.add(cancelButton);
		westPane.add(buttonPane);
		overlayPane.add(westPane,BorderLayout.WEST);
		
		contentLabel = new JLabel("", SwingConstants.CENTER);
		// the following is for optimizing layout under linux, otherwise
		// some of the text does not fit into dialog
		contentLabel.setFont(new Font(contentLabel.getFont().getName(),contentLabel.getFont().getStyle(), 11));
		contentLabel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createCompoundBorder(
				new EmptyBorder(5, 5, 0, 5),
				new EtchedBorder()),
			new EmptyBorder(5, 5, 5, 5)));
		contentLabel.setText(Messages.getJAlgoInfoAsHTML());

		overlayPane.add(contentLabel, BorderLayout.CENTER);
		
		mcdOnStartup = new JCheckBox(
				Messages.getString("main", "Prefs.ShowModuleChooserOnStartup"));
		mcdOnStartup.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		mcdOnStartup.addActionListener(new ActionListener(){
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(ActionEvent e) {
				Settings.setBoolean("main", "ShowModuleChooserOnStartup",
						mcdOnStartup.isSelected());		
			}
		});
		overlayPane.add(mcdOnStartup, BorderLayout.SOUTH);
		
		overlayPane.setBorder(new EmptyBorder(0,5,0,0));
		
		add(overlayPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(parent);
	}

	/**
	 * Starts a new module out of the <code>moduleList</code>.
	 */
	private void moduleSelected() {
		if (! moduleList.isSelectionEmpty()){
			dispose();
			JAlgoMain.getInstance().newInstance(moduleList.getSelectedIndex());
		}
	}

	public synchronized static void open(JFrame parent) {
		if (instance == null) instance = new ModuleChooseDialog(parent);
		instance.setSize(430, 330);
		instance.setLocationRelativeTo(parent);
		instance.mcdOnStartup.setSelected(Settings.getBoolean("main", "ShowModuleChooserOnStartup"));
		instance.setVisible(true);
	}
	
	/**
	 * Retrieves a <code>String</code> array filled with the installed modules.
	 * @return module list
	 */
	private static String[] getModuleList() {
		String[] back = new String[JAlgoMain.getInstance().getKnownModuleInfos().size()];
		int index = 0;
		Iterator<IModuleInfo> it = JAlgoMain.getInstance().getKnownModuleInfos().iterator();
		
		while(it.hasNext()) {
			back[index] = it.next().getName();
			index++;
		}
		return back;
	}
	
	/**
	 * Updates the InfoPane when a new module is choosen.
	 * @param index of the choosen module
	 */
	private static void updateInfo(int index) {		
		instance.contentLabel.removeAll();
		instance.contentLabel.setText(Messages.getModuleInfoAsHTML(index));
		//contentLabel.updateUI();
	}
	
	
	/**
	 * A special ListCellRenderer to add images in the moduleList.
	 * @author Matthias Schmidt
	 */
	 class MyCellRenderer extends JLabel implements ListCellRenderer {
		 private List<IModuleInfo> knownModules;
		 
	     public MyCellRenderer() {
	         setOpaque(true);
	         knownModules = JAlgoMain.getInstance().getKnownModuleInfos();
	     }
	     public Component getListCellRendererComponent(
	         JList list,
	         Object value,
	         int index,
	         boolean isSelected,
	         boolean cellHasFocus)
	     {
	         setText(value.toString());
	         setBackground(isSelected ? Color.blue : Color.white);
	         setForeground(isSelected ? Color.white : Color.black);
	         setIcon(getIconForValue(value));
	         setBorder(new EmptyBorder(2,2,2,0));
	         return this;
	     }
	     
	     // Retrieves to module specific Icon for the JList entry.
	     private Icon getIconForValue(Object value){	    	 
	    	 for (IModuleInfo element : knownModules) {
				if (element.getName().equals(value))
					return new ImageIcon(element.getLogoURL());
			}
	    	return null;
	     }
	 } 
}
