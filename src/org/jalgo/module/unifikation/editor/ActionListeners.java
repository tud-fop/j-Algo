//JOptionPane.showMessageDialog(null, editWorkspace.getText(), "HTMLCode", JOptionPane.INFORMATION_MESSAGE);
					
package org.jalgo.module.unifikation.editor;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.jalgo.module.unifikation.Constants;

public class ActionListeners {
	private static boolean NumberExpected=false;
	private static int PrevCaretPos=5;
	private static String ErrorTextKomma = "<html><head><style>body{color: black;}</style></head>Syntax:<br>Nicht erlaubt nach:<br>{, (, ','</html>";
	private static String ErrorTextFunc = "<html><head><style>body{color: black;}</style></head>Syntax:<br>Nur erlaubt nach:<br>( oder ','</html>";
	private static String NoErrorText = "<html>Syntax:</html>";
	
	public static HyperlinkListener getErrorLinkListener(final Editor TheEdit)
	{
		HyperlinkListener hl = new HyperlinkListener()
		{
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
				{
				    String link = (String) ((AttributeSet) e.getSourceElement().getAttributes().getAttribute(HTML.Tag.A)).getAttribute(HTML.Attribute.HREF);
				    TheEdit.editWorkspace.requestFocus();
				    TheEdit.editWorkspace.setCaretPosition(Integer.parseInt(link));
				}
			}
		};
		return hl;
	}
	
	public static CaretListener getEditCaretListener(final Editor TheEdit)
	{
		final JEditorPane editWorkspace = TheEdit.editWorkspace;
		CaretListener cl = new CaretListener(){
			public void caretUpdate(CaretEvent e) {
				if(NumberExpected==true)
				{
					try {
						NumberExpected=false;
						editWorkspace.getDocument().remove(PrevCaretPos, 1);	
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				}
				NumberExpected=false;
				PrevCaretPos=e.getDot();
				//Wenn Cursor vor { dann springe nach {
				if(e.getDot()<4)
					editWorkspace.setCaretPosition(4);
				//Wenn Cursor nach } dann springe vor }	
				if(e.getDot()==(editWorkspace.getDocument().getLength()))
					editWorkspace.setCaretPosition(editWorkspace.getDocument().getLength()-1);
				//Wenn SelectionStart vor { beginnt -> SetCursor nach {
				if(editWorkspace.getSelectionStart()<4)
					editWorkspace.setCaretPosition(4);
				//Wenn SelectionEnd nach } -> SetCursor vor }
				if(editWorkspace.getSelectionEnd()==editWorkspace.getDocument().getLength())
					editWorkspace.setCaretPosition(editWorkspace.getDocument().getLength()-1);
				
			}};
		return cl;
	}
	
	public static KeyListener   getEditKeyListener(final Editor TheEdit)
	{
		final JEditorPane editWorkspace = TheEdit.editWorkspace;
		KeyListener kl = new KeyListener(){
			public void keyPressed(KeyEvent e) {
				//Kein strg+v
				if(e.getKeyCode()==86)
					{e.consume();return;}
				//Wenn Backspace hinter { -> Consume
				if((e.getKeyChar()==KeyEvent.VK_BACK_SPACE) && (editWorkspace.getCaretPosition()==4) )
					{e.consume();return;}
				//Wenn Enft vor } -> Consume
				if((e.getKeyChar()==KeyEvent.VK_DELETE) && (editWorkspace.getCaretPosition()>=editWorkspace.getDocument().getLength()-1) )
					{e.consume();return;}
				//Wenn Selection vor { -> consume alles
				if(editWorkspace.getSelectionStart()<4)
					{e.consume();return;}
				//Wenn Selection nach } -> Consume alles
				if(editWorkspace.getSelectionEnd()==editWorkspace.getDocument().getLength())
					{e.consume();return;}
				//Enter fällt aus
				if(e.getKeyChar()==KeyEvent.VK_ENTER)
					{e.consume();return;}
				//Tab fällt aus
				if(e.getKeyChar()==KeyEvent.VK_TAB)
					{e.consume();return;}
				//Del bei Index erwartet
				if(NumberExpected==true && e.getKeyChar()==KeyEvent.VK_BACK_SPACE)
				{
					try {
						NumberExpected=false;
						editWorkspace.getDocument().remove(PrevCaretPos-1, 2);	
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
					e.consume();
				}
					
				TheEdit.getApplication().notifyChange();
			}

			public void keyReleased(KeyEvent e) {}

			public void keyTyped(KeyEvent e) {
				TheEdit.getErrors();
				TheEdit.getApplication().notifyChange();
				//Wenn Selection vor { -> consume alles (Buchstaben)
				if(editWorkspace.getSelectionStart()<4)
					{e.consume();return;}
				//Wenn Selection nach } -> Consume alles (Buchstaben)
				if(editWorkspace.getSelectionEnd()==editWorkspace.getDocument().getLength())
					{e.consume();return;}
				//Buchstaben abfangen
				char key = e.getKeyChar();
				switch(key){
				case 'a':
					writeFunction(Constants.ALPHA,TheEdit);
					e.consume();
					break;
				case 'b':
					writeFunction(Constants.BETA,TheEdit);
					e.consume();
					break;
				case 'g':
					writeFunction(Constants.GAMMA,TheEdit);
					e.consume();
					break;
				case 'd':
					writeFunction(Constants.DELTA,TheEdit);
					e.consume();
					break;
				case 'e':
					writeFunction(Constants.EPSILON,TheEdit);
					e.consume();
					break;
				case 't':
					writeFunction(Constants.THETA,TheEdit);
					e.consume();
					break;
				case 'u':
					writeLetter("u",TheEdit);
					e.consume();
					break;
				case 'v':
					writeLetter("v",TheEdit);
					e.consume();
					break;
				case 'w':
					writeLetter("w",TheEdit);
					e.consume();
					break;
				case 'x':
					writeLetter("x",TheEdit);
					e.consume();
					break;
				case 'y':
					writeLetter("y",TheEdit);
					e.consume();
					break;
				case 'z':
					writeLetter("z",TheEdit);
					e.consume();
					break;
				case ',':
					writeKomma(TheEdit);
					e.consume();
					break;
				case 'p':
					writeLetter("p",TheEdit);
					e.consume();
					break;
				case 'n':
					writeNumber(Constants.Placeholder,TheEdit);
					e.consume();
					break;
				case '(':
					writeLetter("(",TheEdit);
					e.consume();
					break;
				case ')':
					writeLetter(")",TheEdit);
					e.consume();
					break;
				case '1':
					writeNumber("1",TheEdit);
					e.consume();
					break;
				case '2':
					writeNumber("2",TheEdit);
					e.consume();
					break;
				case '3':
					writeNumber("3",TheEdit);
					e.consume();
					break;
				case '4':
					writeNumber("4",TheEdit);
					e.consume();
					break;
				case '5':
					writeNumber("5",TheEdit);
					e.consume();
					break;
				case '6':
					writeNumber("6",TheEdit);
					e.consume();
					break;
				case '7':
					writeNumber("7",TheEdit);
					e.consume();
					break;
				case '8':
					writeNumber("8",TheEdit);
					e.consume();
					break;
				case '9':
					writeNumber("9",TheEdit);
					e.consume();
					break;
				
				default:
					//editWorkspace.setText(e.toString());
					e.consume();
					break;
				}
			}};
		return kl;
	}
	
	public static ActionListener getButtonFunctionListener(final Editor TheEdit)
	{
		final JEditorPane editWorkspace = TheEdit.editWorkspace;
		ActionListener al = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				writeFunction(((JButton)e.getSource()).getText().substring(0,1),TheEdit);
				editWorkspace.requestFocus();
			}
		};
		return al;
	}
	
	public static ActionListener getButtonLetterListener(final Editor TheEdit)
	{
		final JEditorPane editWorkspace = TheEdit.editWorkspace;
		ActionListener al = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				writeLetter(((JButton)e.getSource()).getText(),TheEdit);
				editWorkspace.requestFocus();
			}			
		};
			
		return al;
	}
	
	public static ActionListener getButtonKommaListener(final Editor TheEdit)
	{
		ActionListener al = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				writeKomma(TheEdit);
			}
		};
			
		return al;
	}
	
	public static ActionListener getButtonNPairListener(final Editor TheEdit)
	{
		ActionListener al = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				writeLetter("p",TheEdit);
			}			
		};
			
		return al;
	}

	public static ActionListener getButtonBackSpaceListener(final Editor TheEdit)
	{
		final JEditorPane editWorkspace = TheEdit.editWorkspace;
		ActionListener al = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				editWorkspace.dispatchEvent(new KeyEvent(editWorkspace, KeyEvent.KEY_PRESSED, System.currentTimeMillis(),0,8, (char) KeyEvent.VK_BACK_SPACE));
				TheEdit.getErrors();
				editWorkspace.requestFocus();
				TheEdit.getApplication().notifyChange();
			}
		};
			
		return al;
	}
	
	public static ActionListener getButtonDelListener(final Editor TheEdit)
	{
		final JEditorPane editWorkspace = TheEdit.editWorkspace;
		ActionListener al = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				editWorkspace.dispatchEvent(new KeyEvent(editWorkspace, KeyEvent.KEY_PRESSED, System.currentTimeMillis(),0,127, (char) KeyEvent.VK_DELETE));
				TheEdit.getErrors();
				editWorkspace.requestFocus();
				TheEdit.getApplication().notifyChange();
			}
		};
			
		return al;
	}
	
	//ATTENTION	
	public static ActionListener getButtonX1234Listener(final Editor TheEdit)
	{
		final JEditorPane editWorkspace = TheEdit.editWorkspace;
		final HTMLEditorKit editWorkspaceKit = (HTMLEditorKit)TheEdit.editWorkspace.getEditorKit();
		ActionListener al = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					if(validationTestFuncChar(editWorkspace.getDocument().getText(editWorkspace.getCaretPosition()-1, 2)))
					{
						String bttext = ((JButton)e.getSource()).getText();
						bttext= bttext.substring(6, bttext.length()-7);
						editWorkspaceKit.insertHTML((HTMLDocument) editWorkspace.getDocument(), editWorkspace.getCaretPosition(), "<span class=\"var\">"+bttext+"</span>", 0, 0, HTML.Tag.SPAN);
						TheEdit.getApplication().notifyChange();
						TheEdit.lblSyntax.setText(NoErrorText);
						TheEdit.getErrors();
					}else TheEdit.lblSyntax.setText(ErrorTextFunc);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				editWorkspace.requestFocus();
			}
		};
		return al;
	}
	
	public static ActionListener getButtonXNListener(final Editor TheEdit)
	{
		final JEditorPane editWorkspace = TheEdit.editWorkspace;
		ActionListener al = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				writeNumber(Constants.Placeholder,TheEdit);
				editWorkspace.requestFocus();
			}
		};
			
		return al;
	}
	
	public static ActionListener getButtonHelpListener(final Editor TheEdit)
	{
		ActionListener al = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(TheEdit.contentPane.getLayout().equals(TheEdit.helpLayout))
					TheEdit.contentPane.setLayout(TheEdit.noHelpLayout);
				else
					TheEdit.contentPane.setLayout(TheEdit.helpLayout);
				
				TheEdit.contentPane.updateUI();
			}
		};
		return al;
	}
	
	public static ActionListener getButtonStartListener(final Editor TheEdit)
	{
		ActionListener al = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					TheEdit.doStart();
				} catch (HeadlessException e1) {
					e1.printStackTrace();
				}
			}
		};
			
		return al;
	}
	
	private static boolean validationTestFuncChar(String Characters)
	{
		switch(Characters.toCharArray()[1])
		{
		case 'x':
			return false;
		case 'y':
			return false;
		case 'z':
			return false;
		case 'u':
			return false;
		case 'v':
			return false;
		case 'w':
			return false;
		}
		//JAlgoGUIConnector.getInstance().showInfoMessage(String.valueOf(Characters.toCharArray()[1]));
		if(String.valueOf(Characters.toCharArray()[1]).equals(Constants.ALPHA))
			return false;
		if(String.valueOf(Characters.toCharArray()[1]).equals(Constants.BETA))
			return false;
		if(String.valueOf(Characters.toCharArray()[1]).equals(Constants.GAMMA))
			return false;
		if(String.valueOf(Characters.toCharArray()[1]).equals(Constants.DELTA))
			return false;
		if(String.valueOf(Characters.toCharArray()[1]).equals(Constants.EPSILON))
			return false;
		if(String.valueOf(Characters.toCharArray()[1]).equals(Constants.THETA))
			return false;
		
		switch(Characters.toCharArray()[0])
		{
		case '(':
			return true;
		case ',':
			return true;
		}
		return false;
	}
	
	private static boolean validationTestKomma(String Characters)
	{
		if(Characters.toCharArray()[0]=='(' && Characters.toCharArray()[1]==')')
			return true;
		
		switch(Characters.toCharArray()[0])
		{
		case '(':
			return false;
		case ',':
			return false;
		case '{':
			return false;
		}
		return true;
	}
	
	private static void writeNumber(String Number,Editor TheEdit)
	{
		final JEditorPane editWorkspace =TheEdit.editWorkspace;
		if(NumberExpected==true)
		{
			NumberExpected=false;
			final HTMLEditorKit editWorkspaceKit = (HTMLEditorKit)TheEdit.editWorkspace.getEditorKit();
			
			try {
				editWorkspace.getDocument().remove(editWorkspace.getCaretPosition()-1, 2);
				editWorkspaceKit.insertHTML((HTMLDocument) editWorkspace.getDocument(), editWorkspace.getCaretPosition(), "<span class=\"var\">x<sub>"+Number+"</sub></span>", 0, 0, HTML.Tag.SPAN);
				TheEdit.getApplication().notifyChange();
				TheEdit.getErrors();
			} catch (BadLocationException e1) {e1.printStackTrace();
			} catch (IOException e1) {e1.printStackTrace();
			}
		}else
		{
			final HTMLEditorKit editWorkspaceKit = (HTMLEditorKit)TheEdit.editWorkspace.getEditorKit();
			try {
				if(validationTestFuncChar(editWorkspace.getDocument().getText(editWorkspace.getCaretPosition()-1, 2)))
				{
					editWorkspaceKit.insertHTML((HTMLDocument) editWorkspace.getDocument(), editWorkspace.getCaretPosition(), "<span class=\"var\">x<sub>"+Number+"</sub></span>", 0, 0, HTML.Tag.SPAN);
					if(Number.equals(Constants.Placeholder))
					{
						editWorkspace.setCaretPosition(editWorkspace.getCaretPosition()-1);
						NumberExpected=true;
					}
					TheEdit.getApplication().notifyChange();
					TheEdit.lblSyntax.setText(NoErrorText);
				}else TheEdit.lblSyntax.setText(ErrorTextFunc);
				TheEdit.getErrors();
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private static void writeKomma(Editor TheEdit)
	{
		final JEditorPane editWorkspace =TheEdit.editWorkspace;
		if(NumberExpected==true)
		{
			NumberExpected=false;
			try {
				editWorkspace.getDocument().remove(editWorkspace.getCaretPosition(), 1);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		try {
			if(validationTestKomma(editWorkspace.getDocument().getText(editWorkspace.getCaretPosition()-1, 2)))
			{
				editWorkspace.getDocument().insertString(editWorkspace.getCaretPosition(),",", null);
				TheEdit.getApplication().notifyChange();
				TheEdit.lblSyntax.setText(NoErrorText);
				TheEdit.getErrors();
			}else TheEdit.lblSyntax.setText(ErrorTextKomma);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		editWorkspace.requestFocus();
	}
	
	private static void writeLetter(String Letter,Editor TheEdit)
	{
		final JEditorPane editWorkspace =TheEdit.editWorkspace;
		if(NumberExpected==true)
		{
			NumberExpected=false;
			try {
				editWorkspace.getDocument().remove(editWorkspace.getCaretPosition(), 1);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		try {
			if(Letter.equals("(") || Letter.equals(")") )
			{
				editWorkspace.getDocument().insertString(editWorkspace.getCaretPosition(),Letter, null);
				editWorkspace.requestFocus();
				TheEdit.getApplication().notifyChange();
				TheEdit.getErrors();
				return;
			}
			if(Letter.equals("p"))
			{
				editWorkspace.getDocument().insertString(editWorkspace.getCaretPosition(),"(,)", null);
				editWorkspace.setCaretPosition(editWorkspace.getCaretPosition()-2);
				editWorkspace.requestFocus();
				TheEdit.getApplication().notifyChange();
				TheEdit.getErrors();
				return;
			}
			if(validationTestFuncChar(editWorkspace.getDocument().getText(editWorkspace.getCaretPosition()-1, 2)))
			{
				try {
					final HTMLEditorKit editWorkspaceKit = (HTMLEditorKit)TheEdit.editWorkspace.getEditorKit();
					editWorkspaceKit.insertHTML((HTMLDocument) editWorkspace.getDocument(), editWorkspace.getCaretPosition(), "<span class=\"var\">"+Letter+"</span>", 0, 0, HTML.Tag.SPAN);
				} catch (IOException e) {e.printStackTrace();}
				TheEdit.getApplication().notifyChange();
				TheEdit.lblSyntax.setText(NoErrorText);
				TheEdit.getErrors();
			}else TheEdit.lblSyntax.setText(ErrorTextFunc);
		} catch (BadLocationException e1) {e1.printStackTrace();}
	}
	
	private static void writeFunction(String Letter,Editor TheEdit)
	{
		final JEditorPane editWorkspace =TheEdit.editWorkspace;
		final HTMLEditorKit editWorkspaceKit = (HTMLEditorKit)TheEdit.editWorkspace.getEditorKit();
		try {
			if(validationTestFuncChar(editWorkspace.getDocument().getText(editWorkspace.getCaretPosition()-1, 2)))
			{
				try {
					editWorkspaceKit.insertHTML((HTMLDocument) editWorkspace.getDocument(), editWorkspace.getCaretPosition(), "<span class=\"constsymb\">"+Letter+"</span>()", 0, 0, HTML.Tag.SPAN);
				} catch (IOException e) {e.printStackTrace();}
				
				editWorkspace.setCaretPosition(editWorkspace.getCaretPosition()-1);
				
				TheEdit.getApplication().notifyChange();
				TheEdit.lblSyntax.setText(NoErrorText);
				TheEdit.getErrors();
			}else TheEdit.lblSyntax.setText(ErrorTextFunc);
		} catch (BadLocationException e1) {e1.printStackTrace();}
	}
	
}
