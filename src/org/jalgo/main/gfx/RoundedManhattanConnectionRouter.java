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

package org.jalgo.main.gfx;

import org.eclipse.draw2d.AbstractRouter;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

/**
 * @author Anne Kersten
 */
public class RoundedManhattanConnectionRouter extends AbstractRouter {

	/**
	 * Routes the Connection.
	 * @param conn The Connection to route
	 * @see org.eclipse.draw2d.ConnectionRouter#route(org.eclipse.draw2d.Connection)
	 */
	public void route(Connection conn) {
		
		int offset,i,r1,r2;
		
		Point start= this.getStartPoint(conn);
		conn.translateToRelative(start);
		Point end=this.getEndPoint(conn);
		conn.translateToRelative(end);
		IFigure source=conn.getSourceAnchor().getOwner();
		IFigure target=conn.getTargetAnchor().getOwner();
		Rectangle rs=source.getBounds().getCopy();
		Rectangle rt=target.getBounds().getCopy();
		
		if (start.y>=end.y)
			if (start.x<=end.x) offset=0;			//target oben rechts von source
			else offset=1;                 			//target oben links von source
		else if (start.x<=end.x) offset=2;	 		//target unten rechts von source
			 else offset=3;            				//target unten links von source
		if(rs.x+rs.width/2<=start.x)         		//out rechts
			if (rt.x+rt.width/2>=end.x)offset+=0;	//in links	
			else offset+=4;                  		//in rechts
		else                                 		//out links
			if (rt.x+rt.width/2>=end.x)offset+=8; 	//in links	
			else offset+=12;                		//in rechts
	
		if (Math.abs(start.x-end.x)>=20 &&Math.abs(start.y-end.y)>=20)r1=10;
		else r1=Math.min(Math.abs(start.x-end.x)/2,Math.abs(start.y-end.y)/2);
		if(Math.abs(start.y-end.y)>=20)r2=10;
		else r2=Math.abs((Math.abs(start.y-end.y))/2);
	
		PointList l=new PointList();
		l.addPoint(start);
		switch (offset){
			case 0: 
			{		
				PointList p1=leftCorner(new Point(end.x-((end.x-start.x)/2),start.y),SWT.LEFT,r1);
				PointList p2=rightCorner(new Point(end.x-((end.x-start.x)/2),end.y),SWT.BOTTOM,r1);
				for(i=0;i<p1.size();i++)
				l.addPoint(p1.getPoint(i));
				for(i=0;i<p2.size();i++)
				l.addPoint(p2.getPoint(i));
				break;
			}
			case 1:
			{
				if (source.equals(target))
				{
					PointList p1=rightCorner(new Point(start.x+20,start.y),SWT.LEFT,10);
					PointList p2=rightCorner(new Point(start.x+20,rs.y+rs.height+15),SWT.TOP,10);
					PointList p3=rightCorner(new Point(end.x-20,rs.y+rs.height+15),SWT.RIGHT,10);
					PointList p4=rightCorner(new Point(end.x-20,end.y),SWT.BOTTOM,10);
					for(i=0;i<p1.size();i++)
					l.addPoint(p1.getPoint(i));
					for(i=0;i<p2.size();i++)
					l.addPoint(p2.getPoint(i));
					for(i=0;i<p3.size();i++)
					l.addPoint(p3.getPoint(i));
					for(i=0;i<p4.size();i++)
					l.addPoint(p4.getPoint(i));
				}
				else 
				{
					if(Math.abs(start.y-end.y)>=40)
					{
					PointList p1=leftCorner(new Point(start.x+20,start.y),SWT.LEFT,10);
					PointList p2=leftCorner(new Point(start.x+20,end.y-(end.y-start.y)/2),SWT.BOTTOM,10);
					PointList p3=rightCorner(new Point(end.x-20,end.y-(end.y-start.y)/2),SWT.RIGHT,10);
					PointList p4=rightCorner(new Point(end.x-20,end.y),SWT.BOTTOM,10);
					for(i=0;i<p1.size();i++)
					l.addPoint(p1.getPoint(i));
					for(i=0;i<p2.size();i++)
					l.addPoint(p2.getPoint(i));
					for(i=0;i<p3.size();i++)
					l.addPoint(p3.getPoint(i));
					for(i=0;i<p4.size();i++)
					l.addPoint(p4.getPoint(i));
					}
					else
					{
						PointList p3=halfleftCircle(new Point(start.x+20,start.y),SWT.LEFT,Math.abs(start.y-end.y)/4);
						PointList p4=halfrightCircle(new Point(end.x-20,end.y-(end.y-start.y)/2),SWT.RIGHT,Math.abs(start.y-end.y)/4);
						for(i=0;i<p3.size();i++)
						l.addPoint(p3.getPoint(i));
						for(i=0;i<p4.size();i++)
						l.addPoint(p4.getPoint(i));					
					}
				}
				break;
			}
			case 2:
			{
				PointList p1=rightCorner(new Point(start.x+(end.x-start.x)/2,start.y),SWT.LEFT,r1);
				PointList p2=leftCorner(new Point(start.x+(end.x-start.x)/2,end.y),SWT.TOP,r1);
				for(i=0;i<p1.size();i++)
				l.addPoint(p1.getPoint(i));
				for(i=0;i<p2.size();i++)
				l.addPoint(p2.getPoint(i));
				break;				
			}
			case 3:
			{
				if (Math.abs(start.y-end.y)>=40)
				{
				PointList p1=rightCorner(new Point(start.x+20,start.y),SWT.LEFT,10);
				PointList p2=rightCorner(new Point(start.x+20,end.y-(end.y-start.y)/2),SWT.TOP,10);
				PointList p3=leftCorner(new Point(end.x-20,end.y-(end.y-start.y)/2),SWT.RIGHT,10);
				PointList p4=leftCorner(new Point(end.x-20,end.y),SWT.TOP,10);
				for(i=0;i<p1.size();i++)
				l.addPoint(p1.getPoint(i));
				for(i=0;i<p2.size();i++)
				l.addPoint(p2.getPoint(i));
				for(i=0;i<p3.size();i++)
				l.addPoint(p3.getPoint(i));
				for(i=0;i<p4.size();i++)
				l.addPoint(p4.getPoint(i));
				}
				else
				{
					PointList p3=halfrightCircle(new Point(start.x+20,start.y),SWT.LEFT,Math.abs(start.y-end.y)/4);
					PointList p4=halfleftCircle(new Point(end.x-20,end.y-(end.y-start.y)/2),SWT.RIGHT,Math.abs(start.y-end.y)/4);
					for(i=0;i<p3.size();i++)
					l.addPoint(p3.getPoint(i));
					for(i=0;i<p4.size();i++)
					l.addPoint(p4.getPoint(i));
				}
				break;
			}
			case 4:
			{
				if (r2==10)
				{
				PointList p1=leftCorner(new Point(end.x+20,start.y),SWT.LEFT,10);
				PointList p2=leftCorner(new Point(end.x+20,end.y),SWT.BOTTOM,10);
				for(i=0;i<p1.size();i++)
				l.addPoint(p1.getPoint(i));
				for(i=0;i<p2.size();i++)
				l.addPoint(p2.getPoint(i));
				}
				else 
				{
					PointList p1=halfleftCircle(new Point(end.x+20,start.y),SWT.LEFT,r2);
					for(i=0;i<p1.size();i++)
					l.addPoint(p1.getPoint(i));
				}
				break;
			}
			case 5:
			{
				if(r2==10)
				{
				PointList p1=leftCorner(new Point(start.x+20,start.y),SWT.LEFT,10);
				PointList p2=leftCorner(new Point(start.x+20,end.y),SWT.BOTTOM,10);
				for(i=0;i<p1.size();i++)
				l.addPoint(p1.getPoint(i));
				for(i=0;i<p2.size();i++)
				l.addPoint(p2.getPoint(i));
				}
				else 
				{
					PointList p1=halfleftCircle(new Point(start.x+20,start.y),SWT.LEFT,r2);
					for(i=0;i<p1.size();i++)
					l.addPoint(p1.getPoint(i));
				}
				break;
			}
			case 6:
			{
				if(r2==10)
				{
				PointList p1=rightCorner(new Point(end.x+20,start.y),SWT.LEFT,10);
				PointList p2=rightCorner(new Point(end.x+20,end.y),SWT.TOP,10);
				for(i=0;i<p1.size();i++)
				l.addPoint(p1.getPoint(i));
				for(i=0;i<p2.size();i++)
				l.addPoint(p2.getPoint(i));
				}
				else 
				{
					PointList p1=halfrightCircle(new Point(end.x+20,start.y),SWT.LEFT,r2);
					for(i=0;i<p1.size();i++)
					l.addPoint(p1.getPoint(i));
				}
				break;
			}
			case 7:
			{
				if(r2==10)
				{
				PointList p1=rightCorner(new Point(start.x+20,start.y),SWT.LEFT,10);
				PointList p2=rightCorner(new Point(start.x+20,end.y),SWT.TOP,10);
				for(i=0;i<p1.size();i++)
				l.addPoint(p1.getPoint(i));
				for(i=0;i<p2.size();i++)
				l.addPoint(p2.getPoint(i));
				}
				else 
				{
					PointList p1=halfrightCircle(new Point(start.x+20,start.y),SWT.LEFT,r2);
					for(i=0;i<p1.size();i++)
					l.addPoint(p1.getPoint(i));
				}
				break;
			}
			case 8:
			{
				if(r2==10)
				{
				PointList p1=rightCorner(new Point(start.x-20,start.y),SWT.RIGHT,10);
				PointList p2=rightCorner(new Point(start.x-20,end.y),SWT.BOTTOM,10);
				for(i=0;i<p1.size();i++)
				l.addPoint(p1.getPoint(i));
				for(i=0;i<p2.size();i++)
				l.addPoint(p2.getPoint(i));
				}
				else 
				{
					PointList p1=halfrightCircle(new Point(start.x-20,start.y),SWT.RIGHT,r2);
					for(i=0;i<p1.size();i++)
					l.addPoint(p1.getPoint(i));
				}			
				break;
			}
			case 9:
			{
				if(r2==10)
				{
				PointList p1=rightCorner(new Point(end.x-20,start.y),SWT.RIGHT,10);
				PointList p2=rightCorner(new Point(end.x-20,end.y),SWT.BOTTOM,10);
				for(i=0;i<p1.size();i++)
				l.addPoint(p1.getPoint(i));
				for(i=0;i<p2.size();i++)
				l.addPoint(p2.getPoint(i));
				}
				else 
				{
					PointList p1=halfrightCircle(new Point(end.x-20,start.y),SWT.RIGHT,r2);
					for(i=0;i<p1.size();i++)
					l.addPoint(p1.getPoint(i));
				}
				break;
			}
			case 10:
			{
				if(r2==10)
				{
				PointList p1=leftCorner(new Point(start.x-20,start.y),SWT.RIGHT,10);
				PointList p2=leftCorner(new Point(start.x-20,end.y),SWT.TOP,10);
				for(i=0;i<p1.size();i++)
				l.addPoint(p1.getPoint(i));
				for(i=0;i<p2.size();i++)
				l.addPoint(p2.getPoint(i));
				}
				else 
				{
					PointList p1=halfleftCircle(new Point(start.x-20,start.y),SWT.RIGHT,r2);
					for(i=0;i<p1.size();i++)
					l.addPoint(p1.getPoint(i));
				}
				break;				
			}
			case 11:
			{
				if(r2==10)
				{
				PointList p1=leftCorner(new Point(end.x-20,start.y),SWT.RIGHT,10);
				PointList p2=leftCorner(new Point(end.x-20,end.y),SWT.TOP,10);
				for(i=0;i<p1.size();i++)
				l.addPoint(p1.getPoint(i));
				for(i=0;i<p2.size();i++)
				l.addPoint(p2.getPoint(i));
				}
				else 
				{
					PointList p1=halfleftCircle(new Point(end.x-20,start.y),SWT.RIGHT,r2);
					for(i=0;i<p1.size();i++)
					l.addPoint(p1.getPoint(i));
				}
				break;				
			}
			case 12:
			{
				if (source.equals(target))
				{
					PointList p1=leftCorner(new Point(start.x-20,start.y),SWT.RIGHT,10);
					PointList p2=leftCorner(new Point(start.x-20,rs.y+rs.height+15),SWT.TOP,10);
					PointList p3=leftCorner(new Point(end.x+20,rs.y+rs.height+15),SWT.LEFT,10);
					PointList p4=leftCorner(new Point(end.x+20,end.y),SWT.BOTTOM,10);
					for(i=0;i<p1.size();i++)
					l.addPoint(p1.getPoint(i));
					for(i=0;i<p2.size();i++)
					l.addPoint(p2.getPoint(i));
					for(i=0;i<p3.size();i++)
					l.addPoint(p3.getPoint(i));
					for(i=0;i<p4.size();i++)
					l.addPoint(p4.getPoint(i));
				}
				else
				{
				if (Math.abs(start.y-end.y)>=40){
					PointList p1=rightCorner(new Point(start.x-20,start.y),SWT.RIGHT,r2);
					PointList p2=rightCorner(new Point(start.x-20,end.y-(end.y-start.y)/2),SWT.BOTTOM,r2);
					PointList p3=leftCorner(new Point(end.x+20,end.y-(end.y-start.y)/2),SWT.LEFT,r2);
					PointList p4=leftCorner(new Point(end.x+20,end.y),SWT.BOTTOM,r2);
					for(i=0;i<p1.size();i++)
					l.addPoint(p1.getPoint(i));
					for(i=0;i<p2.size();i++)
					l.addPoint(p2.getPoint(i));
					for(i=0;i<p3.size();i++)
					l.addPoint(p3.getPoint(i));
					for(i=0;i<p4.size();i++)
					l.addPoint(p4.getPoint(i));
				}
				else{
					PointList p3 =halfrightCircle(new Point(start.x-20,start.y),SWT.RIGHT,Math.abs(start.y-end.y)/4);
					PointList p4 =halfleftCircle(new Point(end.x+20,end.y-(end.y-start.y)/2),SWT.LEFT,Math.abs(start.y-end.y)/4);
					for(i=0;i<p3.size();i++)
					l.addPoint(p3.getPoint(i));
					for(i=0;i<p4.size();i++)
					l.addPoint(p4.getPoint(i));
				}
				}
				break;
			}
			case 13:
			{
				PointList p1=rightCorner(new Point(start.x+(end.x-start.x)/2,start.y),SWT.RIGHT,r1);
				PointList p2=leftCorner(new Point(start.x+(end.x-start.x)/2,end.y),SWT.BOTTOM,r1);
				for(i=0;i<p1.size();i++)
				l.addPoint(p1.getPoint(i));
				for(i=0;i<p2.size();i++)
				l.addPoint(p2.getPoint(i));
				break;
			}
			case 14:
			{
				if (Math.abs(start.y-end.y)>=40){
					PointList p1=leftCorner(new Point(start.x-20,start.y),SWT.RIGHT,r2);
					PointList p2=leftCorner(new Point(start.x-20,end.y-(end.y-start.y)/2),SWT.TOP,r2);
					PointList p3=rightCorner(new Point(end.x+20,end.y-(end.y-start.y)/2),SWT.LEFT,r2);
					PointList p4=rightCorner(new Point(end.x+20,end.y),SWT.TOP,r2);
					for(i=0;i<p1.size();i++)
					l.addPoint(p1.getPoint(i));
					for(i=0;i<p2.size();i++)
					l.addPoint(p2.getPoint(i));
					for(i=0;i<p3.size();i++)
					l.addPoint(p3.getPoint(i));
					for(i=0;i<p4.size();i++)
					l.addPoint(p4.getPoint(i));
				}
				else
				{
					PointList p3=halfleftCircle(new Point(start.x-20,start.y),SWT.RIGHT,Math.abs(start.y-end.y)/4);
					PointList p4=halfrightCircle(new Point(end.x+20,end.y-(end.y-start.y)/2),SWT.LEFT,Math.abs(start.y-end.y)/4);
					for(i=0;i<p3.size();i++)
					l.addPoint(p3.getPoint(i));
					for(i=0;i<p4.size();i++)
					l.addPoint(p4.getPoint(i));
				}
				break;
			}
			case 15:
			{
				PointList p1=leftCorner(new Point(start.x+(end.x-start.x)/2,start.y),SWT.RIGHT,r1);
				PointList p2=rightCorner(new Point(start.x+(end.x-start.x)/2,end.y),SWT.TOP,r1);
				for(i=0;i<p1.size();i++)
				l.addPoint(p1.getPoint(i));
				for(i=0;i<p2.size();i++)
				l.addPoint(p2.getPoint(i));
				break;				
			}
			default:
		}
		l.addPoint(end);
		conn.setPoints(l);
		conn.repaint();
	}
	
	private PointList leftCorner(Point p,int start, int r)
	{
		double i;
		double offset;
		PointList pl=new PointList();
		Point m=new PrecisionPoint();
		switch(start)
		{
			case SWT.TOP:offset=0.5;m.setLocation(p.x+r,p.y-r);break;
			case SWT.RIGHT:offset=1;m.setLocation(p.x+r,p.y+r);break;
			case SWT.BOTTOM:offset=1.5;m.setLocation(p.x-r,p.y+r);break;
			case SWT.LEFT:offset=0;m.setLocation(p.x-r,p.y-r);break;
			default:offset=0;m.setLocation(p.x-r,p.y-r);
		}
		for(i=(offset+0.5)*Math.PI;i>=offset*Math.PI; i-=0.05*Math.PI)
		{
			pl.addPoint(new PrecisionPoint(m.x+r*Math.cos(i),m.y+r*Math.sin(i)));
		}
		return pl;
	}
	
	private PointList rightCorner(Point p,int start,int r)
	{
		double i;
		double offset;
		PointList pl=new PointList();
		Point m=new PrecisionPoint();
		switch(start)
		{
			case SWT.TOP:offset=0;m.setLocation(p.x-r,p.y-r);break;
			case SWT.RIGHT:offset=0.5;m.setLocation(p.x+r,p.y-r);break;
			case SWT.BOTTOM:offset=1;m.setLocation(p.x+r,p.y+r);break;
			case SWT.LEFT:offset=1.5;m.setLocation(p.x-r,p.y+r);break;
			default:offset=0;m.setLocation(p.x+r,p.y+r);			
		}
		for(i=offset*Math.PI;i<=(offset+0.5)*Math.PI; i+=0.05*Math.PI)
		{
			pl.addPoint(new PrecisionPoint(m.x+r*Math.cos(i),m.y+r*Math.sin(i)));
		}
		return pl;
	}
	
	private PointList halfrightCircle(Point p, int start, int r)
	{
		double i;
		double offset;
		PointList pl=new PointList();
		Point m=new PrecisionPoint();
		switch(start)
		{
			case SWT.TOP:offset=0;m.setLocation(p.x-r,p.y-r);break;
			case SWT.RIGHT:offset=0.5;m.setLocation(p.x+r,p.y-r);break;
			case SWT.BOTTOM:offset=1;m.setLocation(p.x+10,p.y+r);break;
			case SWT.LEFT:offset=1.5;m.setLocation(p.x-r,p.y+r);break;
			default:offset=0;m.setLocation(p.x+r,p.y+r);			
		}
		for(i=offset*Math.PI;i<=(offset+1)*Math.PI; i+=0.05*Math.PI)
		{
			pl.addPoint(new PrecisionPoint(m.x+r*Math.cos(i),m.y+r*Math.sin(i)));
		}
		return pl;
	}
	
	private PointList halfleftCircle(Point p, int start, int r)
	{
		double i;
		double offset;
		PointList pl=new PointList();
		Point m=new PrecisionPoint();
		switch(start)
		{
			case SWT.TOP:offset=0;m.setLocation(p.x+r,p.y-r);break;
			case SWT.RIGHT:offset=0.5;m.setLocation(p.x+r,p.y+r);break;
			case SWT.BOTTOM:offset=1;m.setLocation(p.x-r,p.y+r);break;
			case SWT.LEFT:offset=1.5;m.setLocation(p.x-r,p.y-r);break;
			default:offset=0;m.setLocation(p.x-r,p.y-r);
		}
		for(i=(offset+1)*Math.PI;i>=offset*Math.PI; i-=0.05*Math.PI)
		{
			pl.addPoint(new PrecisionPoint(m.x+r*Math.cos(i),m.y+r*Math.sin(i)));
		}
		return pl;
	}
}
