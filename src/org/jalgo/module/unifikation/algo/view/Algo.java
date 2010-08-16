package org.jalgo.module.unifikation.algo.view;



import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle;
import javax.swing.border.EtchedBorder;

import org.jalgo.main.util.Messages;
import org.jalgo.module.unifikation.Application;
import org.jalgo.module.unifikation.Constants;
import org.jalgo.module.unifikation.IAppView;
import org.jalgo.module.unifikation.algo.HTMLParser.SpecialEditorKit;
import org.jalgo.module.unifikation.algo.controller.ButtonListenerFactory;
import org.jalgo.module.unifikation.algo.controller.DecompButtonAction;
import org.jalgo.module.unifikation.algo.controller.DoBlockStepButtonAction;
import org.jalgo.module.unifikation.algo.controller.DoStepButtonAction;
import org.jalgo.module.unifikation.algo.controller.EditorButtonAction;
import org.jalgo.module.unifikation.algo.controller.ElimButtonAction;
import org.jalgo.module.unifikation.algo.controller.HLLPairs;
import org.jalgo.module.unifikation.algo.controller.NotUnifiableButtonAction;
import org.jalgo.module.unifikation.algo.controller.PerformAllButtonAction;
import org.jalgo.module.unifikation.algo.controller.RuleCheckButtonAction;
import org.jalgo.module.unifikation.algo.controller.SubButtonAction;
import org.jalgo.module.unifikation.algo.controller.SwapButtonAction;
import org.jalgo.module.unifikation.algo.controller.UndoAllButtonAction;
import org.jalgo.module.unifikation.algo.controller.UndoBlockStepButtonAction;
import org.jalgo.module.unifikation.algo.controller.UndoStepButtonAction;
import org.jalgo.module.unifikation.algo.controller.UnifiedButtonAction;
import org.jalgo.module.unifikation.algo.model.Formating;
import org.jalgo.module.unifikation.algo.model.History;
import org.jalgo.module.unifikation.algo.model.ProblemSet;
import org.jalgo.module.unifikation.parser.ISetParser;
import org.jalgo.module.unifikation.parser.SetParser;


/**
 * algo class
 * initializes the algo-windows and works as a connector to application
 * 
 * 
 * 
 */
public class Algo implements IAppView, MouseListener {
	
	private Application app=null;
	private JEditorPane content;
	protected JEditorPane jEditorPaneRegel;
	public JEditorPane jErrorPane;
	private JComponent contentPane;
	protected History history;
	//private EditorFormat editorFormat;
	
	protected JButton editorButton;	
	protected JButton jButtonNoMoreRules;
	protected JButton jButtonNotUnifiable;
	protected JButton jButtonUnified;
	protected JButton jButtonDekomp;
	protected JButton jButtonSubst;
	protected JButton jButtonVertau;
	protected JButton jButtonElimin;
	
	private boolean mouseDown;
	
	protected ProblemSet curSet;
	private IAlgoState curState;
	
	public Algo(){
		history=new History();
		curState=null;
		mouseDown=false;
		//editorFormat=new EditorFormat();
	}
	
	public JComponent getContentPane() {
		return this.content;
	}
	
	public IAlgoState getState(){
		return this.curState;
	}
		
	public void setApplication(Application app) {
		this.app=app;
		contentPane = app.contentPane;
	}
	
	public ProblemSet getProblemSet(){
		return curSet;
	}

	public History getHistory(){
		return history;
	}
	
	public boolean isMouseDown(){
		return mouseDown;
	}
	
	public void setMouseDown(boolean mouseDown){
		this.mouseDown=mouseDown;
	}
	
	/**
	 * Gets the contentPane so it can create the GUI
	 */
	public void setContentPane(JComponent contentPaneOLD) {
		ISetParser parser=new SetParser();
		if(parser.parse(app.getProblem())){
			curSet=parser.getResult();
		}else{
			app.setFinished(app.getProblem());
			return;
		}
		

		GroupLayout thisLayout = new GroupLayout(contentPane);
		contentPane.setLayout(thisLayout);
		
		//create helpbox
		jEditorPaneRegel = new JEditorPane("text/html","");
		jEditorPaneRegel.setEditable(false);
		jEditorPaneRegel.setBackground(Color.WHITE);
		jEditorPaneRegel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		//create errorbox
		jErrorPane = new JEditorPane("text/html","");
		jErrorPane.setEditable(false);
		jErrorPane.setBackground(Color.WHITE);
		jErrorPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		jErrorPane.setText("<html><body><b>"+Formating.addColor("W" + Constants.lowercasedAE + "hlen Sie ein Paar aus um eine Regel anzuwenden oder nutzen Sie die Pfeile in der Toolbar als Hilfe um Schritte automatisch auszuf" + Constants.lowercasedUE + "hren.","#FF0000")+"</b></body></html>");
	
		ButtonListenerFactory blFactory=new ButtonListenerFactory(this);
		//Toolbar aktivieren
		app.installToolbar(); 
		editorButton = new JButton((new ImageIcon(Messages.getResourceURL("unifikation", "Icon.Editor"))));
		editorButton.setToolTipText("Menge editieren");
		app.toolbar.add(editorButton);
		editorButton.addActionListener(blFactory.getClickListener(EditorButtonAction.class));
		app.performBlockStep.addActionListener(blFactory.getClickListener(DoBlockStepButtonAction.class));
		app.undoBlockStep.addActionListener(blFactory.getClickListener(UndoBlockStepButtonAction.class));
		app.performStep.addActionListener(blFactory.getClickListener(DoStepButtonAction.class));
		app.undoStep.addActionListener(blFactory.getClickListener(UndoStepButtonAction.class));
		app.undoAll.addActionListener(blFactory.getClickListener(UndoAllButtonAction.class));
		app.performAll.addActionListener(blFactory.getClickListener(PerformAllButtonAction.class));
		
		//create activity pane

		content = new JEditorPane("text/html","");
		content.setEditorKit(new SpecialEditorKit());
		JScrollPane jScrollPane1 = new JScrollPane(content);
		jScrollPane1.setBorder(null);
		content.setBackground(Color.WHITE);
		content.setEditable(false);
		content.addHyperlinkListener(new HLLPairs(this));
		content.addMouseListener(this);
		content.setFocusable(false);
		
		
		//create buttons
		
		jButtonNoMoreRules = new JFadeButton();
		jButtonNoMoreRules.setText("keine Regel anwendbar");
		jButtonNoMoreRules.setFont(Constants.ButtonAlgoFont);
		jButtonNoMoreRules.addActionListener(blFactory.getClickListener(RuleCheckButtonAction.class));
		
		jButtonNotUnifiable = new JFadeButton();
		jButtonNotUnifiable.setText("nicht unifizierbar");
		jButtonNotUnifiable.setFont(Constants.ButtonAlgoFont);
		jButtonNotUnifiable.addActionListener(blFactory.getClickListener(NotUnifiableButtonAction.class));
	
		jButtonUnified = new JFadeButton();
		jButtonUnified.setText("unifiziert");
		jButtonUnified.setFont(Constants.ButtonAlgoFont);
		jButtonUnified.addActionListener(blFactory.getClickListener(UnifiedButtonAction.class));
	
		jButtonDekomp = new JButton();
		jButtonDekomp.setText("Dekomposition");
		jButtonDekomp .setFont(Constants.ButtonAlgoFont);
		jButtonDekomp.addActionListener(blFactory.getClickListener(DecompButtonAction.class));
		jButtonDekomp.addMouseListener(blFactory.getHoverListener(DecompButtonAction.class));
	
		jButtonSubst = new JButton();
		jButtonSubst.setText("Substitution");
		jButtonSubst.setFont(Constants.ButtonAlgoFont);
		jButtonSubst.addActionListener(blFactory.getClickListener(SubButtonAction.class));
		jButtonSubst.addMouseListener(blFactory.getHoverListener(SubButtonAction.class));
	
		jButtonVertau = new JButton();
		jButtonVertau.setText("Vertauschung");
		jButtonVertau.setFont(Constants.ButtonAlgoFont);
		jButtonVertau.addActionListener(blFactory.getClickListener(SwapButtonAction.class));
		jButtonVertau.addMouseListener(blFactory.getHoverListener(SwapButtonAction.class));
	
		jButtonElimin = new JButton();
		jButtonElimin.setText("Elimination");	
		jButtonElimin.setFont(Constants.ButtonAlgoFont);
		jButtonElimin.addActionListener(blFactory.getClickListener(ElimButtonAction.class));
		jButtonElimin.addMouseListener(blFactory.getHoverListener(ElimButtonAction.class));
		
		//create layout
		thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
			.addContainerGap(12, 12)
			.addComponent(jScrollPane1, 0, 349, Short.MAX_VALUE)
			.addGap(12)
			.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
			    .addComponent(jButtonDekomp, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
			    .addComponent(jButtonSubst, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
			    .addComponent(jButtonVertau, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
			    .addComponent(jButtonElimin, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
			    .addComponent(jButtonNoMoreRules, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
			    .addComponent(jButtonUnified, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
			    .addComponent(jButtonNotUnifiable, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
			.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
			.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(jEditorPaneRegel, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
					.addComponent(jErrorPane, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE))
			.addContainerGap(26, 26));
		
		thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup()
			.addContainerGap(5, 5)
			.addGroup(thisLayout.createParallelGroup()
			    .addGroup(thisLayout.createSequentialGroup()
			        .addComponent(jButtonDekomp, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
			        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
			        .addComponent(jButtonSubst, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
			        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
			        .addComponent(jButtonVertau, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
			        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
			        .addComponent(jButtonElimin, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
			        .addGap(20)
			        .addComponent(jButtonNoMoreRules, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)
			        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
			        .addComponent(jButtonUnified, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
			        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
			        .addComponent(jButtonNotUnifiable, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
			    )
			    .addGroup(thisLayout.createSequentialGroup()
			    .addComponent(jEditorPaneRegel, 0, 834, Short.MAX_VALUE)
			    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
			    .addComponent(jErrorPane, 0, 434, Short.MAX_VALUE))
			    .addComponent(jScrollPane1, GroupLayout.Alignment.LEADING, 0, 834, Short.MAX_VALUE)
			    )
			.addContainerGap(5, 5));
		
		//Do this only after everything has been created!
		history.reset();
		curState=new StateAlgoRunning();
		this.notifySetChanged();
		
	}
	
	/**
	 * Enables/Disables rule buttons
	 * @param enabled new state of the buttons
	 */
	public void switchRuleButtons(boolean enabled){
		jButtonDekomp.setEnabled(enabled);
		jButtonSubst.setEnabled(enabled);
		jButtonVertau.setEnabled(enabled);
		jButtonElimin.setEnabled(enabled);
	}
	
	/**
	 * Switch the buttons which decide if unifiable or not
	 * @param enabled new state of the buttons
	 */
	public void switchUnifiableButtons(boolean enabled){
		jButtonNotUnifiable.setEnabled(enabled);
		jButtonUnified.setEnabled(enabled);
	}

	/**
	 * called when "Done"-Button is clicked
	 */
	public void onDoneClick(){
		//Remove listener!!!
		content.removeMouseListener(this);
		//Close this view
		app.setFinished(app.getProblem());
	}
	
	private void updateContent(String sSet){
		content.setText("<html><head><style>"
				+ Constants.NBodyCSS
				+"</style></head>"
				+"<body>"
				+Formating.setTextSize(history.getHistory(),false)
				+Formating.setTextSize(sSet,true)
				+"</body></html>");
	}
	
	/**
	 * Updates the algo view where set is shown 
	 */
	public void updateSetView(){
		curState.updateView();
	}
	
	public class StateAlgoRunning implements IAlgoState{

		public StateAlgoRunning(){
			curSet.setSelectedPair(-1);
			curSet.setHoverPair(-1);
			curSet.setActive(true);
			jButtonNoMoreRules.setEnabled(true);
			switchUnifiableButtons(false);
			switchRuleButtons(false);
		}

		public boolean next() {
			if(curSet.getUsablePair()<0){
				curState=new StateNoRuleUsable();
				return true;
			}else return false;
		}

		public boolean prev() {
			return false;
		}

		public void updateView() {
			switchRuleButtons(curSet.getSelectedPair()>=0);
			updateContent(curSet.getFormatText());
		}
	}
	
	public class StateNoRuleUsable implements IAlgoState{
		public StateNoRuleUsable(){
			curSet.setSelectedPair(-1);
			curSet.setHoverPair(-1);
			curSet.setActive(true);
			jButtonNoMoreRules.setEnabled(false);
			switchUnifiableButtons(true);
			switchRuleButtons(false);
		}

		public boolean next() {
			curState=new StateAlgoFinished();
			return true;
		}

		public boolean prev() {
			curState=new StateAlgoRunning();
			return true;
		}

		public void updateView() {
			updateContent(Formating.addBackgroundColor(curSet.getFormatText(),"#CCCCCC"));
		}
	}
	
	public class StateAlgoFinished implements IAlgoState{
		private boolean unified;
		
		public StateAlgoFinished(){
			unified=curSet.getPairNotFinal()<0;
			curSet.setSelectedPair(-1);
			curSet.setHoverPair(-1);
			curSet.setActive(true);
			jButtonNoMoreRules.setEnabled(false);
			switchUnifiableButtons(false);
			switchRuleButtons(false);
		}

		public boolean next() {
			return false;
		}

		public boolean prev() {
			curState=new StateNoRuleUsable();
			return true;
		}

		public void updateView() {
			StringBuffer sSet=new StringBuffer(curSet.getFormatText(true));
			if(unified){
				sSet.append("<br>" + Constants.PHI + " : ");
				for(int i=0;i<curSet.getNumberOfPairs();i++){
					sSet.append(curSet.getPair(i).getFirstTerm().getFormatText() + " " + Constants.UNIFIEDARROW + " " + curSet.getPair(i).getSecondTerm().getFormatText());
					if(i != curSet.getNumberOfPairs()-1)
						sSet.append( " ,&nbsp; ");
				}
			}else sSet.append(" \u2192 Nicht unifizierbar");
			updateContent(sSet.toString());
		}
		
	}
	
	/**
	 * Notify the algo view, that the selection of the set has changed
	 */
	public void notifySelectionChanged(){
		history.setSelected(curSet.getSelectedPair());
		updateSetView();
	}
	
	/**
	 * Notify the algo view, that the hover mark of the set has changed
	 */
	public void notifyHoverChanged(){
		updateSetView();
	}
	
	/**
	 * Notify the algo view, that the set has been changed
	 */
	public void notifySetChanged(){
		history.addStep(curSet);
		curSet.setHoverPair(-1);
		curSet.setSelectedPair(-1);
		//editorFormat.reset();
		updateSetView();
	}
	
	/**
	 * Notify the algo view, that a step was made in history
	 */
	public void notifyStepChanged(){
		curSet=history.getCurrentSet();
		//editorFormat.reset();
		updateSetView();
	}

	/**
	 * Sets the help text
	 * @param helpText Text to set
	 */
	public void setHelpText(String helpText) {
		this.jEditorPaneRegel.setText("<html><body>"+helpText+"</body></html>");
	}
	
	public void setErrorText(String errorText) {
		this.jErrorPane.setText("<html><body><b>"+Formating.addColor(errorText,"#FF0000")+"</b></body></html>");
	}

	public void mouseClicked(MouseEvent arg0) {	
	}

	public void mouseEntered(MouseEvent arg0) {	
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
		if(arg0.getButton()==1){
			mouseDown=true;
		}
	}

	public void mouseReleased(MouseEvent arg0) {
		if(arg0.getButton()==1){
			mouseDown=false;
		}
	}
}
