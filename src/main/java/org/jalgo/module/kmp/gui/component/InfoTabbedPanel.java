package org.jalgo.module.kmp.gui.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;

import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.jalgo.main.util.Messages;
import org.jalgo.module.kmp.algorithm.KMPHighlighter;
import org.jalgo.module.kmp.Constants;
import org.jalgo.module.kmp.gui.GUIConstants;
import org.jalgo.module.kmp.gui.event.PhaseOneScreenListener;
import org.jalgo.module.kmp.gui.event.PhaseTwoScreenListener;

/**
 * This is the Panel where the user can choose the visibility of several informations which are:
 * the code, the protocol, the code and the protocol in phase one
 * and the code, the protocol, the code and the protocol, the code and the searchtext, 
 * the protocol and the searchtext in phase two.
 * 
 * @author Danilo Lisske
 */
public class InfoTabbedPanel extends JTabbedPane {
	private static final long serialVersionUID = 9930886361248572L;
	
	private PhaseOneScreenListener listener1;
	private PhaseTwoScreenListener listener2;
	
	private JTextPane tacodeonly;
	private JTextPane tadesconly;
	private JTextPane tacodewithdesc;
	private JTextPane tadescwithcode;
	private JTextPane tacodewithst;
	private JTextPane tadescwithst;
	private JTextPane tastwithcode;
	private JTextPane tastwithdesc;
	
	private DefaultStyledDocument descdoc;
	private SimpleAttributeSet normalstyle;
	private SimpleAttributeSet highlightstyle;
	private Font codefont;
	private Font stfont;
	
	/**
	 * The constructor of the <code>InfoTabbedPanel</code>.
	 * 
	 * @param l1 the <code>PhaseOneScreenListener</code>
	 * @param l2 the <code>PhasetwoScreenListener</code>
	 */
	public InfoTabbedPanel(PhaseOneScreenListener l1, PhaseTwoScreenListener l2) {
		listener1 = l1;
		listener2 = l2;
		boolean isphaseone;
		if(listener1 == null) isphaseone = false;
		else isphaseone = true;
		
		setFont(GUIConstants.SCREEN_FONT);
		
		descdoc = new DefaultStyledDocument();
		codefont = GUIConstants.CODE_FONT;
		stfont = GUIConstants.SEARCHTEXT_FONT;
		
		normalstyle = new SimpleAttributeSet();
		StyleConstants.setFontFamily(normalstyle,"SansSerif");
		StyleConstants.setFontSize(normalstyle, 11);
		StyleConstants.setForeground(normalstyle, Color.BLACK);
		
		highlightstyle = new SimpleAttributeSet();
		StyleConstants.setFontFamily(highlightstyle,"SansSerif");
		StyleConstants.setFontSize(highlightstyle, 14);
		StyleConstants.setForeground(highlightstyle, Color.BLUE);
		
		JLabel lbcodeonly = new JLabel(Messages.getString("kmp","ITP.Label_code") + ":");
		lbcodeonly.setFont(getFont());
		JLabel lbdesconly = new JLabel(Messages.getString("kmp","ITP.Label_desc") + ":");
		lbdesconly.setFont(getFont());
		JLabel lbcodewithdesc = new JLabel(Messages.getString("kmp","ITP.Label_code") + ":");
		lbcodewithdesc.setFont(getFont());
		JLabel lbdescwithcode = new JLabel(Messages.getString("kmp","ITP.Label_desc") + ":");
		lbdescwithcode.setFont(getFont());
		JLabel lbcodewithst = new JLabel(Messages.getString("kmp","ITP.Label_code") + ":");
		lbcodewithst.setFont(getFont());
		JLabel lbdescwithst = new JLabel(Messages.getString("kmp","ITP.Label_desc") + ":");
		lbdescwithst.setFont(getFont());
		JLabel lbstwithcode = new JLabel(Messages.getString("kmp","ITP.Label_searchtext") + ":");
		lbstwithcode.setFont(getFont());
		JLabel lbstwithdesc = new JLabel(Messages.getString("kmp","ITP.Label_searchtext") + ":");
		lbstwithdesc.setFont(getFont());
		
		JPanel codeanddescPane = new JPanel(new GridBagLayout());
		JPanel codeonlyPane = new JPanel(new GridBagLayout());
		JPanel desconlyPane = new JPanel(new GridBagLayout());
		JPanel descandstPane = new JPanel(new GridBagLayout());
		JPanel codeandstPane = new JPanel(new GridBagLayout());
		
		tacodeonly = new JTextPane();
		tacodeonly.setEditable(false);
		tacodeonly.setName(Messages.getString("kmp","ITP.TextPane_code_ttt"));
		tacodeonly.setFont(codefont);
		JScrollPane spcodeonly = new JScrollPane(tacodeonly,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		tadesconly = new JTextPane();
		tadesconly.setEditable(false);
		tadesconly.setName(Messages.getString("kmp","ITP.TextPane_desc_ttt"));
		tadesconly.setDocument(descdoc);
		JScrollPane spdesconly = new JScrollPane(tadesconly,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		tacodewithdesc = new JTextPane();
		tacodewithdesc.setEditable(false);
		tacodewithdesc.setName(Messages.getString("kmp","ITP.TextPane_code_ttt"));
		tacodewithdesc.setPreferredSize(new Dimension(codeanddescPane.getPreferredSize().width / 2,
				tacodewithdesc.getPreferredSize().height));
		tacodewithdesc.setFont(codefont);
		JScrollPane spcodewithdesc = new JScrollPane(tacodewithdesc,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		tadescwithcode = new JTextPane();
		tadescwithcode.setEditable(false);
		tadescwithcode.setName(Messages.getString("kmp","ITP.TextPane_desc_ttt"));
		tadescwithcode.setPreferredSize(new Dimension(codeanddescPane.getPreferredSize().width / 2,
				tadescwithcode.getPreferredSize().height));
		tadescwithcode.setDocument(descdoc);
		JScrollPane spdescwithcode = new JScrollPane(tadescwithcode,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		tacodewithst = new JTextPane();
		tacodewithst.setEditable(false);
		tacodewithst.setName(Messages.getString("kmp","ITP.TextPane_code_ttt"));
		tacodewithst.setPreferredSize(new Dimension(codeandstPane.getPreferredSize().width / 2,
				tacodewithst.getPreferredSize().height));
		tacodewithst.setFont(codefont);
		JScrollPane spcodewithst = new JScrollPane(tacodewithst,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		tadescwithst = new JTextPane();
		tadescwithst.setEditable(false);
		tadescwithst.setName(Messages.getString("kmp","ITP.TextPane_desc_ttt"));
		tadescwithst.setPreferredSize(new Dimension(descandstPane.getPreferredSize().width / 2,
				tadescwithst.getPreferredSize().height));
		tadescwithst.setDocument(descdoc);
		JScrollPane spdescwithst = new JScrollPane(tadescwithst,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		tastwithcode = new JTextPane();
		tastwithcode.setEditable(false);
		tastwithcode.setName(Messages.getString("kmp","ITP.TextPane_st_ttt"));
		tastwithcode.setPreferredSize(new Dimension(codeandstPane.getPreferredSize().width / 2,
				tastwithcode.getPreferredSize().height));
		tastwithcode.setFont(stfont);
		JScrollPane spstwithcode = new JScrollPane(tastwithcode,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		tastwithdesc = new JTextPane();
		tastwithdesc.setEditable(false);
		tastwithdesc.setName(Messages.getString("kmp","ITP.TextPane_st_ttt"));
		tastwithdesc.setPreferredSize(new Dimension(descandstPane.getPreferredSize().width / 2,
				tastwithdesc.getPreferredSize().height));
		tastwithdesc.setFont(stfont);
		JScrollPane spstwithdesc = new JScrollPane(tastwithdesc,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 1;
		c.insets = new Insets(2,2,2,2);
		codeanddescPane.add(lbcodewithdesc,c);
		codeonlyPane.add(lbcodeonly,c);
		desconlyPane.add(lbdesconly,c);
		codeandstPane.add(lbcodewithst,c);
		descandstPane.add(lbdescwithst,c);
		c.gridx = 1;
		codeanddescPane.add(lbdescwithcode,c);
		codeandstPane.add(lbstwithcode,c);
		descandstPane.add(lbstwithdesc,c);
		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.weighty = 1;
		codeanddescPane.add(spcodewithdesc,c);
		codeandstPane.add(spcodewithst,c);
		descandstPane.add(spdescwithst,c);
		c.gridx = 1;
		codeanddescPane.add(spdescwithcode,c);
		codeandstPane.add(spstwithcode,c);
		descandstPane.add(spstwithdesc,c);
		c.gridx = 0;
		codeonlyPane.add(spcodeonly,c);
		desconlyPane.add(spdesconly,c);
		
		addTab(Messages.getString("kmp","ITP.Tab_codeanddesc"),codeanddescPane);
		addTab(Messages.getString("kmp","ITP.Tab_codeonly"),codeonlyPane);
		addTab(Messages.getString("kmp","ITP.Tab_desconly"),desconlyPane);
		if(!isphaseone) {
			tacodeonly.addMouseListener(listener2);
			tadesconly.addMouseListener(listener2);
			tacodewithdesc.addMouseListener(listener2);
			tadescwithcode.addMouseListener(listener2);
			tacodewithst.addMouseListener(listener2);
			tadescwithst.addMouseListener(listener2);
			tastwithcode.addMouseListener(listener2);
			tastwithdesc.addMouseListener(listener2);
			addTab(Messages.getString("kmp","ITP.Tab_codeandst"),codeandstPane);
			addTab(Messages.getString("kmp","ITP.Tab_descandst"),descandstPane);
		}
		else {
			tacodeonly.addMouseListener(listener1);
			tadesconly.addMouseListener(listener1);
			tacodewithdesc.addMouseListener(listener1);
			tadescwithcode.addMouseListener(listener1);
			tacodewithst.addMouseListener(listener1);
			tadescwithst.addMouseListener(listener1);
			tastwithcode.addMouseListener(listener1);
			tastwithdesc.addMouseListener(listener1);
		}
	}
	
	/**
	 * Sets the highlighters in the code textarea.
	 * 
	 * @param hl a List of <code>KMPHighlighter</code>
	 */
	public void setKMPHighlighter(List<KMPHighlighter> hl) {
		try {
			tacodeonly.getHighlighter().removeAllHighlights();
			tacodewithdesc.getHighlighter().removeAllHighlights();
			tacodewithst.getHighlighter().removeAllHighlights();
			for(KMPHighlighter kmph : hl) {
				tacodeonly.getHighlighter().addHighlight(kmph.getStartPos(),kmph.getEndPos(),kmph.getHightlightPainter());
				tacodewithdesc.getHighlighter().addHighlight(kmph.getStartPos(),kmph.getEndPos(),kmph.getHightlightPainter());
				tacodewithst.getHighlighter().addHighlight(kmph.getStartPos(),kmph.getEndPos(),kmph.getHightlightPainter());
			}
		}
		catch(BadLocationException ble) {
			ble.printStackTrace();
		}
		catch(NullPointerException npe) {
			
		}
	}
	
	/**
	 * Sets the highlighter in the searchtextarea.
	 * 
	 * @param start the startvalue
	 * @param end the endvalue
	 */
	public void setSearchTextHighlighter(int start, int end) {
		try {
			tastwithcode.getHighlighter().removeAllHighlights();
			tastwithdesc.getHighlighter().removeAllHighlights();
			tastwithcode.getHighlighter().addHighlight(start, end, 
				new DefaultHighlighter.DefaultHighlightPainter(GUIConstants.SEARCHTEXT_HIGHLIGHT));
			tastwithdesc.getHighlighter().addHighlight(start, end, 
				new DefaultHighlighter.DefaultHighlightPainter(GUIConstants.SEARCHTEXT_HIGHLIGHT));
		}
		catch(BadLocationException ble) {
			ble.printStackTrace();
		}
		catch(NullPointerException npe) {
			
		}
	}
	
	/**
	 * Sets the searchtext in the searchtextareas.
	 * 
	 * @param s the searchtext
	 */
	public void setSearchText(String s) {
		tastwithcode.setText(s);
		tastwithdesc.setText(s);
	}
	
	/**
	 * Sets the codetext in the codeareas.
	 * 
	 * @param c the code text
	 */
	public void setCode(String c) {
		tacodeonly.setText(c);
		tacodewithdesc.setText(c);
		tacodewithst.setText(c);
	}
	
	/**
	 * Sets the protocol. Either append the new text or clears the old one and writes only the
	 * new text to the protocolareas.
	 * 
	 * @param d the protocoltext
	 * @param append append the text
	 */
	public void setDescription(String d, boolean append) {
		try {
			if(append) descdoc.setCharacterAttributes(0, descdoc.getLength(), normalstyle, true);
			else descdoc.remove(0, descdoc.getLength());
			descdoc.insertString(descdoc.getLength(), Constants.SEPARATOR + d, highlightstyle);
			tadesconly.setCaretPosition(descdoc.getLength());
			tadescwithcode.setCaretPosition(descdoc.getLength());
			tadescwithst.setCaretPosition(descdoc.getLength());
		}
		catch (BadLocationException ble) {
			ble.printStackTrace();
		}
	}
	
	/**
	 * The method is called when the sizeslider is moved to another value.
	 * 
	 * @param scalefactor the scalefactor
	 */
	public void scaleScreen(double scalefactor) {
		Font newcodefont = new Font(GUIConstants.CODE_FONT.getName(),
				GUIConstants.CODE_FONT.getStyle(),(int)(GUIConstants.CODE_FONT.getSize()*scalefactor));
		Font newstfont = new Font(GUIConstants.SEARCHTEXT_FONT.getName(),
				GUIConstants.SEARCHTEXT_FONT.getStyle(),(int)(GUIConstants.SEARCHTEXT_FONT.getSize()*scalefactor));
		tacodeonly.setFont(newcodefont);
		tacodewithdesc.setFont(newcodefont);
		tacodewithst.setFont(newcodefont);
		tastwithcode.setFont(newstfont);
		tastwithdesc.setFont(newstfont);
		StyleConstants.setFontSize(normalstyle,(int)(11*scalefactor));
		StyleConstants.setFontSize(highlightstyle,(int)(14*scalefactor));
		descdoc.setCharacterAttributes(0, descdoc.getLength(), normalstyle, true);
	}
	
	/**
	 * Sets the focus to the code tab.
	 * 
	 * @param tab the tab number
	 */
	public void setTabFocus(int tab) {
		setSelectedIndex(tab);
	}
}