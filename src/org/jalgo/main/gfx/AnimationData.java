/*
 * Created on 25.05.2004
 */
 
package org.jalgo.main.gfx;

import org.eclipse.draw2d.geometry.Point;

/**
 * @author Hauke Menges
 */
public class AnimationData {
	private org.jalgo.main.util.Timer timer;
	private int offset, duration;
	private Point endPoint, startPoint;
	private IAnimationConcept animationConcept;
	

	public AnimationData(Point endPoint, IAnimationConcept animationConcept, int offset, int duration){
		this.endPoint = endPoint;
		this.animationConcept = animationConcept;
		this.offset = offset;
		this.duration = duration;
	}
	
	public org.jalgo.main.util.Timer getTimer(){
		return this.timer;
	}
	
	public int getOffset(){
		return this.offset;
	}
	
	public int getDuration(){
		return this.duration;
	}
	
	public Point getEndPoint(){
		//return new LinearAnimationConcept().calculate(new Point(0,0),new Point(0,0),0.0); //Test... works :). Usually: new Point();
		return this.endPoint;
	}
	
	public Point getStartPoint(){
		return this.startPoint;
	}
	
	public IAnimationConcept getAnimationConcept(){
		return this.animationConcept;
	}

}
