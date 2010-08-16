package org.jalgo.module.unifikation.algo.view;

import java.awt.Color;

import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTMLDocument;

import org.jalgo.module.unifikation.algo.model.Formating;

public class EditorFormat {
	private int hoverStart,hoverLen,selectStart,selectLen;
	private int NewHoverStart,NewHoverLen,NewSelectStart,NewSelectLen;
	private StringBuffer lastInput;
	
	public EditorFormat(){
		lastInput=new StringBuffer();
		reset();
	}
	
	public void reset(){
		hoverStart=-1;
		selectStart=-1;
		NewHoverStart=-1;
		NewSelectStart=-1;
		lastInput.setLength(0);
	}
	
	public String update(String s){
		long start=System.currentTimeMillis();
		StringBuffer buf=new StringBuffer();
		NewHoverStart=-1;
		NewSelectStart=-1;
		buf.append(s.replace("&nbsp;", " "));
		int pos2,pos3,pos4,offset,offset2;
		String color;
		offset=0;pos3=0;
		String search="<span style=\"background-color:";
		int pos=buf.indexOf(search);
		while(pos>0){
			pos3=buf.indexOf("<",pos3);
			while(pos3>=0 && pos3<pos){
				pos2=buf.indexOf(">",pos3);
				offset+=pos2-pos3+1;
				pos3=buf.indexOf("<",pos2+1);
			}
			pos2=buf.indexOf("\"",pos+search.length());
			color=buf.substring(pos+search.length(),pos2);
			if(color.equalsIgnoreCase(Formating.Hover)){
				NewHoverStart=pos-offset+1;
			}else{
				NewSelectStart=pos-offset+1;
			}
			pos2=buf.indexOf(">",pos2);
			buf.delete(pos, pos2+1);
			offset2=0;
			pos2=buf.indexOf("</span>",pos);
			pos3=buf.indexOf("<",pos3);
			while(pos3>=0 && pos3<pos2){
				if(buf.substring(pos3,pos3+5).equalsIgnoreCase("<span")){
					pos2=buf.indexOf("</span>",pos2+7);
				}
				pos4=buf.indexOf(">",pos3);
				offset2+=pos4-pos3+1;
				pos3=buf.indexOf("<",pos4+1);
			}
			buf.delete(pos2, pos2+7);
			if(color.equalsIgnoreCase(Formating.Hover)){
				NewHoverLen=pos2-pos-offset2;
			}else{
				NewSelectLen=pos2-pos-offset2;
			}
			pos3=pos;
			pos=buf.indexOf(search,pos);
		}
		//System.out.println(NewHoverStart+"");
		//System.out.println(NewHoverLen+"");
		System.out.println("Update:"+(System.currentTimeMillis()-start));
		if(buf.toString().equalsIgnoreCase(lastInput.toString())){
			return "";
		}else{
			lastInput.setLength(0);
			lastInput.append(buf);
			return buf.toString().replace("  ", "&nbsp;&nbsp;");
		}
	}
	
	public void doFormat(JEditorPane editor){
		long start2=System.currentTimeMillis();
		HTMLDocument doc = (HTMLDocument) editor.getDocument (); 
		SimpleAttributeSet attr = new SimpleAttributeSet();
		int start=-1;
		try {
			start = doc.getText(0, doc.getLength()).trim().lastIndexOf("M=");
		} catch (BadLocationException e) {
			start=0;
		}
		if(hoverStart>=0){
			StyleConstants.setBackground(attr, editor.getBackground());
			doc.setCharacterAttributes(start+hoverStart, hoverLen, attr, false);
		}
		if(selectStart>=0){
			StyleConstants.setBackground(attr, editor.getBackground());
			doc.setCharacterAttributes(start+selectStart, selectLen, attr, false);
		}
		hoverStart=NewHoverStart;
		hoverLen=NewHoverLen;
		selectStart=NewSelectStart;
		selectLen=NewSelectLen;
		if(hoverStart>=0){
			StyleConstants.setBackground(attr, Color.decode(Formating.Hover));
			doc.setCharacterAttributes(start+hoverStart, hoverLen, attr, false);
		}
		if(selectStart>=0){
			StyleConstants.setBackground(attr, Color.decode(Formating.Selected));
			doc.setCharacterAttributes(start+selectStart, selectLen, attr, false);
		}
		System.out.println("Format:"+(System.currentTimeMillis()-start2));
	}
	

}