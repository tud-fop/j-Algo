package org.jalgo.module.unifikation.editor;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import org.jalgo.main.util.Messages;
import org.jalgo.module.unifikation.Application;
import org.jalgo.module.unifikation.Constants;
import org.jalgo.module.unifikation.IAppView;
import org.jalgo.module.unifikation.algo.model.Formating;
import org.jalgo.module.unifikation.parser.ISetParser;
import org.jalgo.module.unifikation.parser.ParserError;
import org.jalgo.module.unifikation.parser.SetParser;

/*
 * editor class
 * initializes the editor and works as a connector to application
 */
public class Editor implements IAppView {
	private Application app=null;
	public JEditorPane editWorkspace;
	public JEditorPane lblSyntax;
	public GroupLayout helpLayout;
	public GroupLayout noHelpLayout;
	public JComponent contentPane;
	
	public void setApplication(Application app) {
		this.app=app;
	}
	
	public Application getApplication() {
		return app;
	}
	
	/**
	 * Write ParseErros at the SyntaxLbl
	 */
	public void getErrors()
	{
		ISetParser parser=new SetParser();
		String text=editWorkspace.getText();
		parser.parse(text);
			List<ParserError> errors=parser.getErrors();
			StringBuffer errorString=new StringBuffer();
			if(errors!=null && errors.size()>0){
				errorString.append("<html><head><style>a{color: black;}</style><body>Syntax:<br>");
				for(ParserError error:errors){
					Integer pos = error.getPosition()+1;
					switch (error.getType())
					{
					case ParserError:
						errorString.append("<a href=\""+pos+"\">"+error.getType()+" an Postion "+pos+"</a><br />");
						break;
					case InvalidArity:
						try {
							errorString.append("<a href=\""+pos+"\">"+error.getType()+" von "+editWorkspace.getDocument().getText(error.getPosition()+1,1)+" an Postion "+pos +"</a><br />");
						} catch (BadLocationException e) {
							e.printStackTrace();
						}
						break;
					}
				}
				errorString.append("</body></html>");
			}else errorString.append("<html><head><b><font color=\"#00D800\">g" + Constants.lowercasedUE + "ltiger Ausdruck</font></b></body></html>");
			lblSyntax.setText(errorString.toString());
	}
	
	/**
	 * Starts the AlgoView if no ParserError appears
	 */
	public void doStart(){
		ISetParser parser=new SetParser();
		String text=editWorkspace.getText();
		if(parser.parse(text)){
			app.setFinished(text);
		}else{
			getErrors();
		}
	}

	/*
	 * Gets the contentPane so it can create the GUI
	 */
	public void setContentPane(JComponent contentPane) {
		
		this.contentPane=contentPane;
		//Create HelpLabel
		String HelpText = "<html><body><b>Anleitung</b><br>" +
				"Erstellen Sie hier die Menge f" + Constants.lowercasedUE + "r die Unifikationsaufgabe. " +
				"Benutzen Sie dazu entweder die virtuelle Tastatur unter dem Eingabebereich" +
				" oder ihre echte Tastatur. Fahren Sie mit der Maus " + Constants.lowercasedUE + "ber die virtuellen Tasten, um die" +
				" entsprechenden echten Tasten anzuzeigen.<br><br>Sind Sie mit dem Erstellen fertig," +
				" klicken Sie auf \"Start\", um das Unifikationsproblem zu l" + Constants.lowercasedOE + "sen.<br><br>" +
				"<b>Beispiel:</b><br>" +
				"M={(x, u),(x, "+Constants.ALPHA+"( y, x<sub>1</sub>))}<br><br>Sollte es bei der Eingabe zu Syntaxfehlern kommen, k"+Constants.lowercasedOE+"nnen Sie unten " +
				"rechts auf den Fehler klicken und der Cursor springt an die angegebene Position.</body></html>";
		JLabel  lblHelp = new JLabel(HelpText);
		lblHelp.setBackground(new Color(255,255,128));
		lblHelp.setOpaque(true);
		lblHelp.setVerticalAlignment(SwingConstants.TOP);
		lblHelp.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		lblHelp.setFont(Constants.TextFont);
		
		//Toolbar deaktivieren
		app.installToolbar(); 
		app.disableToolbar();      

		//Create SyntaxErrorLabel		
		lblSyntax = new JEditorPane("text/html","<html><head><style>body {font-family:Tahoma;}</style></head>" +
				"<body>Syntax:</body></html>");
		lblSyntax.setOpaque(false);
		lblSyntax.setEditable(false);
		lblSyntax.setBackground(new Color(224,223,227));
		//lblSyntax.setVerticalAlignment(SwingConstants.TOP);
		lblSyntax.setBorder(null);
		lblSyntax.addHyperlinkListener(ActionListeners.getErrorLinkListener(this));
		
		JScrollPane scrollSyntax = new JScrollPane(lblSyntax);
		scrollSyntax.setBorder(null);
		//Create StartButton
		JButton btnStart = new JButton("Start");
		btnStart.setFont(Constants.ButtonFont);
		btnStart.addActionListener(ActionListeners.getButtonStartListener(this));
		
		//Create Workspace
		editWorkspace = new JEditorPane();

		HTMLEditorKit editWorkspaceKit = new HTMLEditorKit();
		HTMLDocument editWorkspaceDoc = new HTMLDocument();
		
		editWorkspace.setEditorKit(editWorkspaceKit);
		editWorkspace.setDocument(editWorkspaceDoc);
		//editWorkspace.setBorder(BorderFactory.createMatteBorder(
                //1, 1, 1, 1, Color.red));
	
		try {
			editWorkspaceKit.insertHTML(editWorkspaceDoc, 0, app.getProblem(), 0, 0, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		editWorkspace.setCaretPosition(editWorkspace.getDocument().getLength()-3);
		editWorkspace.addCaretListener(ActionListeners.getEditCaretListener(this));
		editWorkspace.addKeyListener(ActionListeners.getEditKeyListener(this));

		//Create VirtualKeyBoard
		JPanel panTastatur = VKeyboard.getVKeyboard(this);
	
		//Create ToggelHelp Toolbar
		JToolBar toolbar = app.toolbar;
		JButton button = new JButton(
			new ImageIcon(Messages.getResourceURL("main", "ui.Help_contents")));
		button.setToolTipText("Editorhilfe einblenden");
		button.addActionListener(ActionListeners.getButtonHelpListener(this));
		toolbar.add(button);
		
		//Setup NoHelpLayout
		noHelpLayout = new GroupLayout((JComponent)contentPane);
		noHelpLayout.setVerticalGroup(noHelpLayout.createSequentialGroup()
				.addContainerGap(12, 12)
				.addComponent(editWorkspace, 0, 129, Short.MAX_VALUE)
				.addGap(5)
				.addGroup(noHelpLayout.createParallelGroup()
				    .addGroup(GroupLayout.Alignment.LEADING, noHelpLayout.createSequentialGroup()
				        .addComponent(btnStart, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
				        .addComponent(scrollSyntax, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE))
				    .addComponent(panTastatur, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE)));
			noHelpLayout.setHorizontalGroup(noHelpLayout.createSequentialGroup()
					.addGap(12)
				.addGroup(noHelpLayout.createParallelGroup()
				    .addGroup(GroupLayout.Alignment.LEADING, noHelpLayout.createSequentialGroup()
				        .addComponent(panTastatur, 0, 161, Short.MAX_VALUE)
				        .addGap(7)
				        .addGroup(noHelpLayout.createParallelGroup()
				            .addComponent(scrollSyntax, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
				            .addComponent(btnStart, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)))
				    .addComponent(editWorkspace, GroupLayout.Alignment.LEADING, 0, 368, Short.MAX_VALUE))
				.addGap(12));
		
		//Setup HelpLayout
		helpLayout = new GroupLayout((JComponent)contentPane);
		helpLayout.setVerticalGroup(helpLayout.createSequentialGroup()
				.addContainerGap(12, 12)
				.addGroup(helpLayout.createParallelGroup()
				    .addComponent(lblHelp, GroupLayout.Alignment.LEADING, 0, 18, Short.MAX_VALUE)
				    .addComponent(editWorkspace, GroupLayout.Alignment.LEADING, 0, 18, Short.MAX_VALUE))
				.addGap(5)
				.addGroup(helpLayout.createParallelGroup()
				    .addGroup(GroupLayout.Alignment.LEADING, helpLayout.createSequentialGroup()
				        .addComponent(btnStart, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
				        .addComponent(scrollSyntax, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE))
				    .addComponent(panTastatur, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE)));
			helpLayout.setHorizontalGroup(helpLayout.createSequentialGroup()
					.addGap(12)
				.addGroup(helpLayout.createParallelGroup()
					.addComponent(panTastatur, GroupLayout.Alignment.LEADING, 0, 235, Short.MAX_VALUE)
				    .addComponent(editWorkspace, GroupLayout.Alignment.LEADING, 0, 235, Short.MAX_VALUE))
				.addGap(7)
				.addGroup(helpLayout.createParallelGroup()
				    .addGroup(GroupLayout.Alignment.LEADING, helpLayout.createSequentialGroup()
				        .addComponent(btnStart, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE))
				    .addGroup(GroupLayout.Alignment.LEADING, helpLayout.createSequentialGroup()
				    	.addComponent(lblHelp, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE))
				    .addGroup(GroupLayout.Alignment.LEADING, helpLayout.createSequentialGroup()
				        .addComponent(scrollSyntax, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
				.addGap(12))));
		
		contentPane.setLayout(helpLayout);
		this.updateSetView();
		editWorkspace.requestFocus();
	}

	public void updateSetView() {
		HTMLDocument editWorkspaceDoc = (HTMLDocument) this.editWorkspace.getDocument();
		StyleSheet editWorkspaceSS = editWorkspaceDoc.getStyleSheet();
		if(Formating.isBeamerMode()==true)
		{
			editWorkspaceSS.addRule(Constants.BBodyCSS);
			contentPane.setLayout(noHelpLayout);
		}else
		{
			editWorkspaceSS.addRule(Constants.NBodyCSS);
		}
	}

	public void setHelpText(String helpText) {
	}
	
}
