package org.jalgo.module.unifikation.algo.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.Timer;

public class JFadeButton extends JButton {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8230265807260757953L;
	
	private Color fadeColor=Color.red;
	private int fadeInterval=50;
	private int fadeDuration=1500;
	private Timer fadeTimer=null;
	private boolean startOnEnable=true;

	/**
	 * Sets start color for fading
	 * @param fadeColor
	 */
	public void setFadeColor(Color fadeColor) {
		this.fadeColor = fadeColor;
	}

	/**
	 * Get current start color for fading
	 * @return current start color for fading
	 */
	public Color getFadeColor() {
		return fadeColor;
	}
	
	/**
	 * Get interval for fading
	 * @return interval for fading in ms
	 */
	public int getFadeInterval() {
		return fadeInterval;
	}

	/**
	 * Set the interval for updating the fade effect
	 * @param fadeInterval interval for updating the fade effect in ms
	 */
	public void setFadeInterval(int fadeInterval) {
		this.fadeInterval = fadeInterval;
	}

	/**
	 * Gets the fading duration
	 * @return fading duration in ms
	 */
	public int getFadeDuration() {
		return fadeDuration;
	}

	/**
	 * Sets the duration during which the fading effect is completed
	 * @param fadeDuration in ms
	 */
	public void setFadeDuration(int fadeDuration) {
		this.fadeDuration = fadeDuration;
	}

	/**
	 * Sets if fading should start when button is just enabled
	 * @param startOnEnable
	 */
	public void setStartOnEnable(boolean startOnEnable) {
		this.startOnEnable = startOnEnable;
	}

	/**
	 * Gets if fading should start when button is just enabled
	 * @return True if fading should start when button is just enabled
	 */
	public boolean isStartOnEnable() {
		return startOnEnable;
	}

	/**
	 * Starts the fading if it is not currently running
	 * @return True if it was started
	 */
	public boolean fade(){
		if(fadeTimer!=null) return false;
		fadeTimer=new Timer(fadeInterval,new fadeTask());
		fadeTimer.start();
		return true;
	}
	
	@Override
	public void setEnabled(boolean b){
		if(b && !this.isEnabled()){
			super.setEnabled(true);
			fade();
		}else super.setEnabled(b);
	}
	
    public void paintComponent(Graphics g) {
    	if(fadeTimer==null || !this.isEnabled()){
    		super.paintComponent(g);
    	}else{
	        g.setColor(this.getBackground());
	        int w = getWidth();       // Size might have changed if
	        int h = getHeight();      // user resized window.
    		g.clearRect(0, 0, w, h);
	        g.fillRoundRect(1, 1, w-3, h-3, 3, 3);
	        this.setContentAreaFilled(false);
	        super.paintComponent(g);
	        this.setContentAreaFilled(true);
	        g.setColor(Color.gray);
	        g.drawRoundRect(1, 1, w-3, h-3, 3, 3);
    	}
    }
	
	private class fadeTask implements ActionListener{
		private Color fromColor,targetColor;
		private int curCall,times;
		
	    public Color combine(Color c1, Color c2, double alpha) {
	        int r = (int) (alpha * c1.getRed()   + (1 - alpha) * c2.getRed());
	        int g = (int) (alpha * c1.getGreen() + (1 - alpha) * c2.getGreen());
	        int b = (int) (alpha * c1.getBlue()  + (1 - alpha) * c2.getBlue());
	        return new Color(r, g, b);
	    }
	    
		public fadeTask(){
			super();
			targetColor=getBackground();
			fromColor=fadeColor;
			times=fadeDuration/fadeInterval;
			curCall=0;
			setOpaque(true);
			actionPerformed(null);
		}

		public void actionPerformed(ActionEvent arg0) {
			if(curCall>=times || (fadeTimer!=null && !isEnabled())){
				setBackground(targetColor);
				fadeTimer.stop();
				fadeTimer=null;
				setOpaque(false);
			}else{
				setBackground(combine(targetColor,fromColor,((double)curCall)/((double)times)));
				curCall++;
			}
			updateUI();
		}
		
	}

}
