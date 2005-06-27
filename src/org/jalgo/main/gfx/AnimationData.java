/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

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
