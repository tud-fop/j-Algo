package org.jalgo.module.em.gui.input;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;

import org.jalgo.main.util.Messages;
import org.jalgo.module.em.data.StartParameters;
import org.jalgo.module.em.gui.UIConstants;

/**
 * A special panel for frequency input for partitions.
 * 
 * @author Tobias Nett
 *
 */
public class YFrequencyPanel extends JPanel implements InputPanel, UIConstants {

	private static final long serialVersionUID = -8903979650436463706L;
	
	private boolean beamerMode = false; 

	private StartParameters params;

	private JLabel lHeading;
	private JLabel lDescription;

	// Button Panel
	private JPanel buttonPanel;
	private JButton bForward;
	private JButton bBack;

	private Container tablePanel;

	private JTable frequencyTable;

	/**
	 * Creates a new object of <code>YFrequencyPanel</code> which offers the user an interface 
	 * to define specific frequency for the partitions.
	 * 
	 * @param params StartParameters, object which is used to store and transfer the input data
	 * @param beamerMode specifies if beamer mode is enabled or not
	 */
	public YFrequencyPanel(StartParameters params, boolean beamerMode) {
		this.params = params;
		this.beamerMode = beamerMode;
		this.init();
	}

	/**
	 * Initializes the <code>YFrequencyPanel</code> (containing a table to edit the values) and
	 * the button panel (containing back/forward buttons).
	 */
	public void init() {
		this.initTablePanel();
		this.initButtonPanel();

        checkInput();
		
		GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(tablePanel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonPanel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tablePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );        
        this.setBeamerMode(beamerMode);
	}

	private void initTablePanel() {
		tablePanel = new JPanel();
		
		lHeading = new JLabel(Messages.getString("em", "YFrequencyPanel.lHeading"));
		lDescription = new JLabel(Messages.getString("em", "YFrequencyPanel.lDescription"));

		TableModel model = new YFrequencyTableModel(params.getObservations());
		
		/**
		 * Customized <code>JTable</code> so that the value of a cell is selected when entering 
		 * edit mode.
		 * 
		 * @author tobias
		 *
		 */
		class MyTable extends JTable {
			private static final long serialVersionUID = 1L;

			public MyTable(TableModel model){
				super(model);
			}
			
			@Override
			public boolean editCellAt(int row, int column, EventObject e) {
				boolean result =  super.editCellAt(row, column, e);
				final Component editor = getEditorComponent();
				if (!(editor == null || !(editor instanceof JTextComponent))){
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							((JTextComponent)editor).selectAll();							
							//((JTextComponent)editor).setText("");
						}
					});
				}	
				return result;
			}
		}
		
		frequencyTable = new MyTable(model);
		
		frequencyTable.setDefaultEditor(Object.class, new YFrequencyCellEditor());
		frequencyTable.setRowHeight(frequencyTable.getFontMetrics(frequencyTable.getFont()).getHeight() + 10);
		// enable single click editing
		((DefaultCellEditor)frequencyTable.getDefaultEditor(Object.class)).setClickCountToStart(1);
		
		frequencyTable.getModel().addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e) {				
				checkInput();			
			}
		});
		
		JScrollPane sp = new JScrollPane(frequencyTable);
		
		javax.swing.GroupLayout tablePanelLayout = new javax.swing.GroupLayout(tablePanel);
        tablePanel.setLayout(tablePanelLayout);
        tablePanelLayout.setHorizontalGroup(
            tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)
                    .addComponent(lHeading, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lDescription, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)
                 )
                .addContainerGap())
        );
        tablePanelLayout.setVerticalGroup(
            tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lHeading)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lDescription)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sp, javax.swing.GroupLayout.PREFERRED_SIZE, 261, Short.MAX_VALUE)
                .addContainerGap())
        );
	}

	/**
	 * Initializes the button panel which contains the 'back' and 'next' button.
	 * The 'back' button is placed on the left side, the next button is place
	 * opposite on the right side.
	 */
	private void initButtonPanel() {
		buttonPanel = new JPanel();

		// labels the buttons in the specified language
		bForward = new JButton(Messages.getString("em", "InputPanel.bForward"));
		bBack = new JButton(Messages.getString("em", "InputPanel.bBack"));

		GroupLayout l = new GroupLayout(buttonPanel);
		buttonPanel.setLayout(l);

		l.setHorizontalGroup(l.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				l.createSequentialGroup()
						.addContainerGap()
						.addComponent(bBack)
						.addPreferredGap(
								LayoutStyle.ComponentPlacement.RELATED, 245,
								Short.MAX_VALUE).addComponent(bForward)
						.addContainerGap()));

		l.setVerticalGroup(l.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						GroupLayout.Alignment.TRAILING,
						l.createSequentialGroup().addGroup(
								l.createParallelGroup(
										GroupLayout.Alignment.BASELINE)
										.addComponent(bBack)
										.addComponent(bForward))));
		
		ActionListener al = new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (frequencyTable.getEditingRow() != -1) {
					// stops cell editing only if a cell is edited at the moment
					frequencyTable.getDefaultEditor(Object.class).stopCellEditing();
				}
			}
		};
		
		bForward.addActionListener(al);
		bBack.addActionListener(al);
	}

	@Override
	public JButton getForwardButton() {
		return bForward;
	}

	@Override
	public JButton getBackButton() {
		return bBack;
	}
	
	private void checkInput() {
		if (((YFrequencyTableModel)frequencyTable.getModel()).getFrequencySum() <= 0){
			bForward.setEnabled(false);
		} else {			
			bForward.setEnabled(true);
		}
	}
	
	/**
	 * Sets the UI mode to beamer or normal mode. Call the method with <code>true</code>
	 * to enter beamer mode or with <code>false</code> to leave it.
	 * <p> In beamer mode, all button texts are replaced with icons and the font size is increased. 
	 * 
	 * @param modeOn <code>true</code> if the beamer mode should be started, 
	 * 					<code>false</code> if view should be set to standard view
	 */
	public void setBeamerMode(boolean modeOn) {
		beamerMode = modeOn;
		if (beamerMode){
			for (Component c : tablePanel.getComponents()) {
				c.setFont(PRESENTATION_FONT);
			}
			frequencyTable.setFont(PRESENTATION_FONT);
			frequencyTable.getTableHeader().setFont(PRESENTATION_FONT);
			
			bForward.setIcon(FORWARD_ICON);
			bForward.setText(null);
			bBack.setIcon(BACK_ICON);
			bBack.setText(null);
		} else {
			for (Component c : tablePanel.getComponents()) {
				c.setFont(DEFAULT_FONT);
			}
			frequencyTable.setFont(DEFAULT_FONT);
			frequencyTable.getTableHeader().setFont(DEFAULT_FONT);
			
			bForward.setText(Messages.getString("em", "InputPanel.bForward"));
			bForward.setIcon(FORWARD_ICON_SMALL);
			bBack.setText(Messages.getString("em", "InputPanel.bBack"));
			bBack.setIcon(BACK_ICON_SMALL);
		}
	}
}
