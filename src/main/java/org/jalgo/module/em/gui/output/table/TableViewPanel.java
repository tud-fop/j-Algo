package org.jalgo.module.em.gui.output.table;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;

import org.jalgo.module.em.control.LogState;
import org.jalgo.module.em.data.Event;
import org.jalgo.module.em.gui.UIConstants;

/**
 * Table View Panel 
 * @author Meinhardt Branig 
 */
public class TableViewPanel extends JPanel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private JScrollPane[] tblPanelLike;
    private JScrollPane[][] tblPanelSE;
    private JScrollPane[] tblPanel;
    private TableModelLike[] tblModLike;
    private TableModelSE[][] tblModSE;
    private TableModel[] tblMod;
    private JTable[] tblLike;
    private JTable[][] tblSE;
    private JTable[] tbl;
    private LogState log;
    private int shownum = 4; //number of shown steps
    private int index; //Index der ersten Spalte z.B. p0|d0|h0||p1|d1|h1||...
    private int click = 0;
    private boolean beam;

	private MouseAdapter tableSizingListener = null;

	private int TABLE_ROW_PADDING = 10;
    
	
	public class ColumnHeaderListener extends MouseAdapter {
        public void mouseClicked(MouseEvent evt) {
        	click = (click +1) %4;
        	clickLike();
        	tableSizingListener.mouseClicked(null);
        }
    }
    
    /**
     * switch on or off the presentation mode
     * @param beam
     */
    public void setBeamerMode(boolean beam) {
    	this.beam = beam;
    	sizeTable();
    }
    
    /**
     * set the number of shown columns
     * @param number of shown columns
     */
    public void setShownum(int shownum) {
    	this.shownum = shownum;
    	//initializes the tables
    	for (int i=0; i<=(log.getP0Count()-1); i++ ){
    		this.drawTbl(i);
    	}
    	clickLike();
    	updateUI();
    }
    
    /**
     * TableViewPanel only displays the Likelihood table
     */
    public void setViewLike() {
    	click = 0;
    	this.removeAll();
		GridBagLayout gridbag = new GridBagLayout();
		this.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
				
		for (int num=0; num<=(log.getP0Count()-1); num++ ){
			c.gridx=0;
			c.gridy=num;
			this.add(tblPanelLike[num], c);  			
		}
		
		this.updateUI();
    }

    /**
     * TableViewPanel displays the Likelihood table and the SingleEvent table
     */
    public void setViewLikeSingle() {
    	click = 1;
    	this.removeAll();
		GridBagLayout gridbag = new GridBagLayout();
		this.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
				
		int anzSE=0;
		for (int num=0; num<=(log.getP0Count()-1); num++ ){
			c.gridx=0;
			c.gridy=2*num+anzSE;
			this.add(tblPanelLike[num], c);
			for (int i=0; i<log.getObjectNames().length; i++){
				anzSE++;
				c.gridx=0;
				c.gridy=2*num+anzSE;
				this.add(tblPanelSE[num][i], c);
			}
		}
		
		this.updateUI();
    }
    
    /**
     * TableViewPanel displays the Likelihood table and the table with chosen partitions
     */
    public void setViewLikePart() {
    	click = 3;
    	this.removeAll();
		GridBagLayout gridbag = new GridBagLayout();
		this.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
				
		int anzSE=0;
		for (int num=0; num<=(log.getP0Count()-1); num++ ){
			c.gridx=0;
			c.gridy=2*num+anzSE;
			this.add(tblPanelLike[num], c);
			c.gridx=0;
			c.gridy=2*num+1+anzSE;
			this.add(tblPanel[num], c);
		}
		
		this.updateUI();
    }
    
    /**
     * TableViewPanel displays the Likelihood table, the SingleEvent table and the table with chosen partitions
     */
    public void setViewLikeSinglePart() {
    	click = 2;
    	this.removeAll();
		GridBagLayout gridbag = new GridBagLayout();
		this.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
				
		int anzSE=0;
		for (int num=0; num<=(log.getP0Count()-1); num++ ){
			c.gridx=0;
			c.gridy=2*num+anzSE;
			this.add(tblPanelLike[num], c);
			for (int i=0; i<log.getObjectNames().length; i++){
				anzSE++;
				c.gridx=0;
				c.gridy=2*num+anzSE;
				this.add(tblPanelSE[num][i], c);
			}
			c.gridx=0;
			c.gridy=2*num+1+anzSE;
			this.add(tblPanel[num], c);
		}
		
		this.updateUI();
    }
    
    /**
     * changes the view if you click on the Likelihood tableheader
     */
    protected void clickLike(){
    	if (click == 0){
    		this.setViewLike();
    	}
    	else if (click == 1){
    		this.setViewLikeSingle();
    	}
    	else if (click == 2){
    		this.setViewLikeSinglePart();
    	}
    	else if (click == 3){
    		this.setViewLikePart();
    	}
    }
    
    
    /**
     * updates the data of the table
     */
    public void update() {
        int steps = log.getStepCount(); //wieviele Große Schritte es gibt
        int singleSteps = log.getSingleStepCount(); //Anzahl kleiner Schritte
        int fullSteps; //number of full steps (ganze anzuzeigende Schritte)
        int firstStep; //the first step (mit dem man beginnt)
        int curStep; //current Step (wo man gerade ist)
        int evIndex = 0; //EventIndex (quasi in welcher Zeile)
             
        /*---volle anzuzeigende Schritte ausrechnen---*/
        if (singleSteps > 0) {fullSteps = shownum - 2;}
        else {fullSteps = shownum-1;}
        
        if (steps < fullSteps){
        	fullSteps = steps;
        }
        
        /*---kleine anzuzeigende Schritte ausrechnen---*/
        if ((steps < shownum) && (singleSteps == 0)) {firstStep = 0;}
        else {firstStep = steps - fullSteps;}
        
        index = firstStep;
        
        for (int num = 0; num<=(log.getP0Count() -1); num++){		//für jede P0-Tabelle
        	curStep = firstStep;									//aktueller Schritt ist am Anfang der erste
        	/*----fill full steps----*/
        	for (int col=0; (col) <= fullSteps; col++){
        		tblModLike[num].setValueAt((log.getL(num, curStep)).toString(), 0, col+1);
        		for (int i=0; i<log.getObjectNames().length; i++){
        			evIndex = 0;
        			for (int objectSide = 1; objectSide <= log.getExperiment().get(i); objectSide++) {
        				tblModSE[num][i].setValueAt((log.getPSingleEvent(num, curStep, (new Point(objectSide, i)))).toString(), evIndex, col+1);
        				evIndex++;
        			}
        		}	
        		evIndex = 0;
        		for (Event event : log.getEvents()) {
        			tblMod[num].setValueAt((log.getP(num, curStep, event)).toString(), evIndex, ((col*3)+3));
        			if (curStep == 0){
        				tblMod[num].setValueAt(" ", evIndex, ((col*3)+1));
        				tblMod[num].setValueAt(" ", evIndex, ((col*3)+2));
        			}
        			else {
        				tblMod[num].setValueAt((log.getD(num, curStep, event)).toString(), evIndex, ((col*3)+1));
        				tblMod[num].setValueAt((log.getH(num, curStep, event)).toString(), evIndex, ((col*3)+2));
        			}
        			evIndex++;
        		}
        		curStep++;
        	}
        	
        	/*----macht leere Spalten leer----*/
        	if (fullSteps < (shownum)) {
        		int kor;
        		if (singleSteps == 0){kor=0;}
        		else{kor=1;}
        		for (int i = 1; i<=(shownum-(fullSteps+1))-kor; i++) {
        			tblModLike[num].setValueAt("", 0, (fullSteps)+i+1);
        			for (int j=0; j<log.getObjectNames().length; j++){
        				evIndex = 0;
        				for (int objectSide = 1; objectSide <= log.getExperiment().get(j); objectSide++) {
            				tblModSE[num][j].setValueAt("", evIndex, (fullSteps)+i+1);
            				evIndex++;
            			}
        			}
        			evIndex = 0;
            		for (Event event : log.getEvents()) {
            			tblMod[num].setValueAt("", evIndex, ((((fullSteps) + i + kor)*3)+3));
            			tblMod[num].setValueAt("", evIndex, ((((fullSteps) + i + kor)*3)+1));
            			tblMod[num].setValueAt("", evIndex, ((((fullSteps) + i + kor)*3)+2));
            			evIndex++;
            		}
        		}
        	}
        	
        	/*----fill single steps----*/
        	if (singleSteps == 1){
        		evIndex=0;
        		tblModLike[num].setValueAt("", 0, (fullSteps+2));
        		for (int i=0; i<log.getObjectNames().length; i++){
        			evIndex = 0;
        			for (int objectSide = 1; objectSide <= log.getExperiment().get(i); objectSide++) {
        				tblModSE[num][i].setValueAt("", evIndex, (fullSteps+2));
        				evIndex++;
        			}
        		}	
        		evIndex = 0;
        		for(Event event : log.getEvents()){
        			tblMod[num].setValueAt((log.getD(num, curStep, event)).toString(), evIndex, ((fullSteps + 1)*3+1));
        			tblMod[num].setValueAt("", evIndex, ((fullSteps + 1)*3+2));
        			tblMod[num].setValueAt("", evIndex, ((fullSteps + 1)*3+3));
        			evIndex++;
        		}
        	}
        	else if (singleSteps == 2){
        		evIndex=0;
        		tblModLike[num].setValueAt("", 0, (fullSteps+2));
        		for (int i=0; i<log.getObjectNames().length; i++){
        			evIndex = 0;
        			for (int objectSide = 1; objectSide <= log.getExperiment().get(i); objectSide++) {
        				tblModSE[num][i].setValueAt("", evIndex, (fullSteps+2));
        				evIndex++;
        			}
        		}	
        		evIndex = 0;
        		for(Event event : log.getEvents()){
        			tblMod[num].setValueAt((log.getD(num, curStep, event)).toString(), evIndex, ((fullSteps + 1)*3+1));
        			tblMod[num].setValueAt((log.getH(num, curStep, event)).toString(), evIndex, (((fullSteps + 1)*3)+2));
        			tblMod[num].setValueAt("", evIndex, ((fullSteps + 1)*3+3));
        			evIndex++;
        		}
        	}
        	
        	//log setzen oder rücksetzen
        	if (log.isLogarithmicLikelihoodRepresentationEnabled()){
        		tblModLike[num].setLog(true);
        	} else {tblModLike[num].setLog(false);}
        	//index übergeben
        	tblModLike[num].setIndex(index);
        	for (int i=0; i<log.getObjectNames().length; i++){
        		tblModSE[num][i].setIndex(index);
        	}
        	tblMod[num].setIndex(index);
        	//Strukturupdate und resize
        	tblModLike[num].fireTableStructureChanged();
        	for (int i=0; i<log.getObjectNames().length; i++){
        		tblModSE[num][i].fireTableStructureChanged();
        	}
        	tblMod[num].fireTableStructureChanged();
        	this.sizeTable();
        }
        this.fireDataUpdates();
        this.updateUI();
    }
        
    private void fireDataUpdates() {
		for (JTable table : tblLike) {
			((AbstractTableModel)table.getModel()).fireTableDataChanged();
		}
		
	}
    
    /**
     * draws the tables with the shownum amount of columns
     */
    private void drawTbl(int num){
    	tblMod[num] = new TableModel(log.getEvents());
        tblModLike[num] = new TableModelLike();
        tblLike[num] = new JTable( tblModLike[num] );
        tbl[num] = new JTable( tblMod[num] );
        
        //adds MouseListener to the Table Header
        JTableHeader header = tblLike[num].getTableHeader();
        header.addMouseListener(new ColumnHeaderListener());
        
        
        //configuring the tblLike
        tblLike[num].getTableHeader().setResizingAllowed(false);
        tblLike[num].getTableHeader().setSize(200, 400);
        tblLike[num].setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblLike[num].getTableHeader().setFont(UIConstants.PRESENTATION_FONT_BOLD);
        tblLike[num].setFont(UIConstants.PRESENTATION_FONT);
        tblLike[num].setRowHeight(23);
        tblLike[num].getColumnModel().getColumn(0).setPreferredWidth(150);
        tblLike[num].getTableHeader().setReorderingAllowed(false);
        tblLike[num].setColumnSelectionAllowed(false);
        tblLike[num].setRowSelectionAllowed(false);
        
        //configuring the tbl
        tbl[num].getTableHeader().setResizingAllowed(false);
        tbl[num].getTableHeader().setSize(200, 400);
        tbl[num].setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbl[num].getTableHeader().setFont(UIConstants.PRESENTATION_FONT_BOLD);
		tbl[num].setFont(UIConstants.PRESENTATION_FONT);
        tbl[num].setRowHeight(23);
        tbl[num].getColumnModel().getColumn(0).setPreferredWidth(150);
        tbl[num].getTableHeader().setReorderingAllowed(false);
        tbl[num].setColumnSelectionAllowed(false);
        tbl[num].setRowSelectionAllowed(false);
        
        //puts tbl and tblLike in ScrollPane
        tblPanelLike[num] = new JScrollPane(tblLike[num]);
        tblPanel[num] = new JScrollPane(tbl[num]);
//		tblPanel[num].setPreferredSize(new Dimension((150+shownum*240+10), (int) 23.1 * (tbl[num].getRowCount()) + 30));
//		tblPanel[num].setMinimumSize(new Dimension((150+shownum*240+10), (int) 23.1 * (tbl[num].getRowCount()) + 30));
		
		//tblSE initialisieren und Größe anpassen
		for(int i=0; i<log.getObjectNames().length; i++){
			tblModSE[num][i] = new TableModelSE(log, i);
			tblSE[num][i] = new JTable(tblModSE[num][i]);
			tblSE[num][i].getTableHeader().setResizingAllowed(false);
	        tblSE[num][i].getTableHeader().setSize(200, 400);
	        tblSE[num][i].setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	        tblSE[num][i].getTableHeader().setFont(UIConstants.PRESENTATION_FONT_BOLD);
	        tblSE[num][i].setFont(UIConstants.PRESENTATION_FONT);
	        tblSE[num][i].setRowHeight(23);
	        tblSE[num][i].getColumnModel().getColumn(0).setPreferredWidth(150);
	        tblSE[num][i].getTableHeader().setReorderingAllowed(false);
	        tblSE[num][i].setColumnSelectionAllowed(false);
	        tblSE[num][i].setRowSelectionAllowed(false);
			tblPanelSE[num][i] = new JScrollPane(tblSE[num][i]);
//			tblPanelSE[num][i].setPreferredSize(new Dimension((150+shownum*240+10), (int) 23.1 * (tblSE[num][i].getRowCount()) + 30));
//			tblPanelSE[num][i].setMinimumSize(new Dimension((150+shownum*240+10), (int) 23.1 * (tblSE[num][i].getRowCount()) + 30));
		}

		//log setzen oder rücksetzen
    	if (log.isLogarithmicLikelihoodRepresentationEnabled()){
    		tblModLike[num].setLog(true);
    	} else {tblModLike[num].setLog(false);}
		
		for (int j=0; j < (shownum); j++){
            /*----add L----*/
            tblModLike[num].addColumn();            
            tblModLike[num].fireTableDataChanged();
            tblModLike[num].fireTableStructureChanged();
            for (int i = 0; i<tblModLike[num].getColumnCount(); i++){
                tblLike[num].getColumnModel().getColumn(i).setPreferredWidth(240);
            }
            tblLike[num].getColumnModel().getColumn(0).setPreferredWidth(150);
        }
		for (int j=0; j<(shownum); j++){
			/*----add SE----*/
			for (int i=0; i<log.getObjectNames().length; i++){
				tblModSE[num][i].addColumn();
				tblModSE[num][i].fireTableDataChanged();
				tblModSE[num][i].fireTableStructureChanged();
				for (int k = 0; k<tblModSE[num][i].getColumnCount(); k++){
					tblSE[num][i].getColumnModel().getColumn(k).setPreferredWidth(240);
				}
				tblSE[num][i].getColumnModel().getColumn(0).setPreferredWidth(150);
			}
		}
        for (int j=0; j < (shownum); j++){    
        	/*----add----*/
        	tblMod[num].addColumn();
        	tblMod[num].addColumn();
        	tblMod[num].addColumn();
        	tblMod[num].fireTableDataChanged();
        	tblMod[num].fireTableStructureChanged();
        	for (int i = 0; i<tblMod[num].getColumnCount(); i++){
        		tbl[num].getColumnModel().getColumn(i).setPreferredWidth(80);    
        	}
        	tbl[num].getColumnModel().getColumn(0).setPreferredWidth(150);
        }

    }

	/**
	 * resizes the JScrollPanes to fit with the tables
	 */
    private void sizeTableScrollPanes() {
		for (int num = 0; num < log.getP0Count(); num ++) {
			int likeTableHeight = tblLike[0].getTableHeader().getFont().getSize()
					+ tblLike[0].getFont().getSize() + 2 * TABLE_ROW_PADDING + 10;
			//TODO different Solutions for different Objects!
			int eventTableHeight = tbl[0].getTableHeader().getFont().getSize()
					+ 14
					+ (tbl[0].getFont().getSize() + TABLE_ROW_PADDING)
					* tbl[0].getRowCount() + 5;
					
			tblPanelLike[num].setPreferredSize(new Dimension((150 + shownum * 240 + 15), likeTableHeight));
			//tblPanelLike[num].setMinimumSize(new Dimension((150 + shownum * 240 + 10), likeTableHeight));
			
			for (int i = 0; i < log.getObjectNames().length ; i++) {
				tblPanelSE[num][i].setPreferredSize(new Dimension((150 + shownum * 240 + 15), calcSETableHeight(i)));
				//tblPanelSE[num][i].setMinimumSize(new Dimension((150 + shownum * 240 + 10), calcSETableHeight(i)));
			}
			
			tblPanel[num].setPreferredSize(new Dimension((150 + shownum*240 + 15), eventTableHeight));
			//tblPanel[num].setMinimumSize(new Dimension((150 + shownum*240 + 10), eventTableHeight));
		}
	}

	/**
	 * calculates the height of the SingleEvent table
	 * @param i
	 * @return height of the SingleEvent table
	 */
    private int calcSETableHeight(final int i) {
		int SETableHeight = tblSE[0][i].getTableHeader().getFont().getSize()
				+ 14
				+ (tblSE[0][i].getFont().getSize() + TABLE_ROW_PADDING)
				* tblSE[0][i].getRowCount() + 5;
		return SETableHeight;
	}

    
    /**
     * resizes the table
     */
    public void sizeTable(){
    	for (int num=0; num<=(log.getP0Count()-1); num++ ){ 
    		if (beam){
    			tblLike[num].getTableHeader().setFont(UIConstants.PRESENTATION_FONT_BOLD);
    			tblLike[num].setFont(UIConstants.PRESENTATION_FONT);
    			tblLike[num].setRowHeight(UIConstants.PRESENTATION_FONT.getSize() + TABLE_ROW_PADDING );
    			tbl[num].getTableHeader().setFont(UIConstants.PRESENTATION_FONT_BOLD);
    			tbl[num].setFont(UIConstants.PRESENTATION_FONT);
    			tbl[num].setRowHeight(UIConstants.PRESENTATION_FONT.getSize() + TABLE_ROW_PADDING);
    			for (int i=0; i<log.getObjectNames().length; i++){
    				tblSE[num][i].getTableHeader().setFont(UIConstants.PRESENTATION_FONT_BOLD);
        			tblSE[num][i].setFont(UIConstants.PRESENTATION_FONT);
        			tblSE[num][i].setRowHeight(UIConstants.PRESENTATION_FONT.getSize() + TABLE_ROW_PADDING);
    			}
    		}
    		else {
    			tblLike[num].getTableHeader().setFont(UIConstants.DEFAULT_FONT_BOLD);
    			
    			tblLike[num].setFont(UIConstants.DEFAULT_FONT);
    			tblLike[num].setRowHeight(UIConstants.DEFAULT_FONT.getSize() + TABLE_ROW_PADDING);
    			tbl[num].getTableHeader().setFont(UIConstants.DEFAULT_FONT_BOLD);
    			tbl[num].setFont(UIConstants.DEFAULT_FONT);
    			tbl[num].setRowHeight(UIConstants.DEFAULT_FONT.getSize() + TABLE_ROW_PADDING);
    			for (int i=0; i<log.getObjectNames().length; i++){
    				tblSE[num][i].getTableHeader().setFont(UIConstants.DEFAULT_FONT_BOLD);
        			tblSE[num][i].setFont(UIConstants.DEFAULT_FONT);
        			tblSE[num][i].setRowHeight(UIConstants.DEFAULT_FONT.getSize() + TABLE_ROW_PADDING);
    			}
    		}
    		
    		for (int i = 0; i<tblModLike[num].getColumnCount(); i++){
    			tblLike[num].getColumnModel().getColumn(i).setPreferredWidth(240); 
    		}
    		tblLike[num].getColumnModel().getColumn(0).setPreferredWidth(150);
    		
    		for (int i=0; i<log.getObjectNames().length; i++){
				for (int k = 0; k<tblModSE[num][i].getColumnCount(); k++){
					tblSE[num][i].getColumnModel().getColumn(k).setPreferredWidth(240);
				}
				tblSE[num][i].getColumnModel().getColumn(0).setPreferredWidth(150);
			}
    		
    		for (int i = 0; i<tblMod[num].getColumnCount(); i++){
    			tbl[num].getColumnModel().getColumn(i).setPreferredWidth(80);    
    		}
    		tbl[num].getColumnModel().getColumn(0).setPreferredWidth(150);
    	}
    	sizeTableScrollPanes();
    }
    
    /**
     * Constructor
     * @param LogState
     */
    public TableViewPanel(LogState log, MouseAdapter tableSizingListener) {
    	this.tableSizingListener = tableSizingListener;
    	this.log = log;
    	tblMod = new TableModel[log.getP0Count()];
    	tblModSE = new TableModelSE[log.getP0Count()][log.getObjectNames().length];
    	tblModLike = new TableModelLike[log.getP0Count()];
    	tbl = new JTable[log.getP0Count()];
    	tblSE = new JTable[log.getP0Count()][log.getObjectNames().length]; 
    	tblLike = new JTable[log.getP0Count()];
    	tblPanel = new JScrollPane[log.getP0Count()];
    	tblPanelSE = new JScrollPane[log.getP0Count()][log.getObjectNames().length];
    	tblPanelLike = new JScrollPane[log.getP0Count()];
    	
    	  	
    	
        //initializes the tables
		for (int i=0; i<=(log.getP0Count()-1); i++ ){
			this.drawTbl(i);
		}

		//Setting up GridBagLayout
		GridBagLayout gridbag = new GridBagLayout();
		this.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
				
		int anzSE=0;
		for (int num=0; num<=(log.getP0Count()-1); num++ ){
			c.gridx=0;
			c.gridy=2*num+anzSE;
			this.add(tblPanelLike[num], c);
			for (int i=0; i<log.getObjectNames().length; i++){
				anzSE++;
				c.gridx=0;
				c.gridy=2*num+anzSE;
				this.add(tblPanelSE[num][i], c);
			}
			c.gridx=0;
			c.gridy=2*num+1+anzSE;
			this.add(tblPanel[num], c);
		}
		
		this.updateUI();
    }	
}
