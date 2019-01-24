package org.jalgo.module.unifikation.algo.HTMLParser;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JEditorPane;
import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.InlineView;
import javax.swing.text.html.HTMLEditorKit.LinkController;

public class SpecialEditorKit extends HTMLEditorKit {
	private static final long serialVersionUID = -5630181697612056591L;
	
    MyLinkController handler=new MyLinkController();
    @Override
    public void install(JEditorPane c) {
        MouseListener[] oldMouseListeners=c.getMouseListeners();
        MouseMotionListener[] oldMouseMotionListeners=c.getMouseMotionListeners();
        super.install(c);
        //the following code removes link handler added by original
        //HTMLEditorKit

        for (MouseListener l: c.getMouseListeners()) {
            c.removeMouseListener(l);
        }
        for (MouseListener l: oldMouseListeners) {
            c.addMouseListener(l);
        }

        for (MouseMotionListener l: c.getMouseMotionListeners()) {
            c.removeMouseMotionListener(l);
        }
        for (MouseMotionListener l: oldMouseMotionListeners) {
            c.addMouseMotionListener(l);
        }

        //add our link handler instead of removed one
        c.addMouseListener(handler);
        c.addMouseMotionListener(handler);
    }

    @Override
	public ViewFactory getViewFactory() {
		return new VerboseViewFactory();
	}
	  
	/*protected Parser defaultParser=null;

	@Override
	protected Parser getParser(){
		if(defaultParser==null){
			defaultParser=new XParserDelegator();
		}
		return defaultParser;
	}
	

	@Override
	public Document createDefaultDocument() {
		StyleSheet styles = getStyleSheet();
		StyleSheet ss = new StyleSheet();
	
		ss.addStyleSheet(styles);
	
		HTMLDocument doc = new SpecialDocument(ss);
		doc.setParser(getParser());
		doc.setAsynchronousLoadPriority(4);
		doc.setTokenThreshold(100);
		return doc;
	}*/

	  
	class VerboseViewFactory extends HTMLEditorKit.HTMLFactory implements ViewFactory{
		
		public View create(Element element) {
            View ret=super.create(element);
            if(ret.getClass().getName().endsWith("InlineView")){
            	//fix the size
            	ret=new FixSizeInlineView(element);
            }
            return ret;
		}
	}
	
	/*
	 * View that does not display something
	 * @author Alex
	 *
	 */
	/*
	class InvisibleView extends View{
		public InvisibleView(Element elem) {
			super(elem);
		}
		
		@Override	
		public void paint(Graphics g, Shape allocation) {
	    }

		@Override
		public float getPreferredSpan(int axis) {
			return 0;
		}

		@Override
		public Shape modelToView(int pos, Shape a, Bias b)
				throws BadLocationException {
			return null;
		}

		@Override
		public int viewToModel(float x, float y, Shape a, Bias[] biasReturn) {
			return 0;
		} 
	}*/


}

/**
 * View that fixes size of background color and underline
 * @author Alex
 *
 */
class FixSizeInlineView extends InlineView{
	private int y=0;
	
	public FixSizeInlineView(Element elem) {
		super(elem);
	}
	
	public int getY(){
		return y;
	}
	
	public void paint(Graphics g, Shape allocation) {
		Color cl=this.getBackground();
		if(cl!=null || this.isUnderline()){
			Rectangle rect=(Rectangle) allocation;
        	if(this.isSubscript()){
        		//sup-style
        		int Offset=(int) (rect.height/2);
        		int ct=getParent().getViewCount();
        		int cY=rect.y;

        		for(int i=0;i<ct;i++){
        			View cur=getParent().getView(i);
        			if(cur.equals(this)){
        				cY=((FixSizeInlineView)getParent().getView(i-1)).getY();
        				break;
        			}
        		}
        		if(cl!=null){
	        		g.setColor(cl);
        			g.fillRect(rect.x,cY,rect.width,rect.height+Offset);
        		}
        		if(this.isUnderline()){
        			g.setColor(Color.black);
        			g.fillRect(rect.x, cY+rect.height+Offset-1, rect.width, 2);
        		}
        	}else{
        		//normal
        		int ct=getParent().getViewCount();
        		int height=rect.height;
        		for(int i=0;i<ct;i++){
        			View cur=getParent().getView(i);
        			if(cur instanceof FixSizeInlineView && ((FixSizeInlineView)cur).isSubscript()){
        				height=(int) (cur.getPreferredSpan(Y_AXIS)*1.5);
        				break;
        			}
        		}
        		y=rect.y;
        		if(cl!=null){
	        		g.setColor(cl);
	        		g.fillRect(rect.x,y,rect.width,height);
        		}
        		if(this.isUnderline()){
        			g.setColor(Color.black);
        			g.fillRect(rect.x, y+height-1, rect.width, 2);
        		}
        	}
    		this.setBackground(null);
		}
		boolean underLine=this.isUnderline();
		this.setUnderline(false);
		super.paint(g, allocation);
		this.setBackground(cl);
		this.setUnderline(underLine);
    }
	
	@Override
	public float getAlignment(int axis){
		if((this.isSubscript()) && axis==View.Y_AXIS){
			return (float) 0.5;
		}else return super.getAlignment(axis);
	}
}

class MyLinkController extends LinkController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5631192194063159282L;

	public void mouseDragged(MouseEvent e) {
		super.mouseMoved(e);
    }
}