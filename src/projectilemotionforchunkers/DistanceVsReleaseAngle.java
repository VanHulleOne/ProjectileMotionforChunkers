package projectilemotionforchunkers;

import processing.core.PApplet;
/**
 *	Copyright (C) 2012  Luke Van Hulle
 *	Contact BeardedOne85@gmail.com
 *
 *	This file is part of Projectile Motion for Chunkers.
 *
 *	Projectile Motion for Chunkers is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	Projectile Motion for Chunkers is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with Projectile Motion for Chunkers in COPYING.txt.
 *	If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Does the calculations for the graph that shows the distance the projectile travels
 * when launched at different angles for a given velocity. It extends Calculations
 * 
 * @author Luke Van Hulle
 *
 */
public class DistanceVsReleaseAngle extends Calculations {
/**
 * The constructor initializes its variables and takes in an instance of the
 * PApplet to pass it along to its graph
 * 
 * @param p the PApplet
 */
	public DistanceVsReleaseAngle(PApplet p) {
		super(p);//call the super constructor
		xLabel = "Angle (Degrees)";
		yLabel = "Distance m";
		title = "Distance vs Release Angle";
		POILabel = " Degrees";//this graph does have a point of interest, the angle of the maximum distance
		lineColor = Graph.blue;
		
		b1Label = "Launch\nVelocity";
		b2Label = null;//This is the only class that doesn't have a 2nd button, which then needs checking
						//all over the place in ProjectileMotionforChunkers
		b1Variable = 100;//the initial value for the b1Variable, the initial launch velocity m/s
		
		setLineColor();//a call to a method in the super class to set the line color of the graph
		setLabels();//in the super class as well
		calculate();//calculate the date for the graph
	}
/**
 * Calculates the data for the graph. At a given initial launch velocity it runs through
 * the launch angles and calculates how far each angle would all the shot to go
 */
	public void calculate() {
		double launchVelocity = b1Variable;//the initial velocity set by the button
		graph.setSpecialLabel("Vi: " + (int)launchVelocity + " m/s");//the special label is the initial velocity
		double incrementAngle = Math.PI/2/MAXDATAPOINTS;//the angle increment between shots 
		double angle = 0;//start at angle 0, level with the ground
		Shot shot = shot(launchVelocity, angle);//the shot
		Shot farthestShot = new Shot();//a blank farthest shot
		
		while(shot != null){//while the shot != null i.e. goes forward
			addDataPoint(shot);//add the shot
			angle += incrementAngle;//increment the angle
			//if the shot went farther than the previous farthestShot
			if(shot.compareTo(farthestShot) == 1){
				farthestShot = shot;//set farthestShot to the current shot
			}
			shot = shot(launchVelocity, angle);//calculate a new shot
		}
		//add the farthest shot to the graph as a point of interest
		graph.setPointOfInterest(getDataPoint(farthestShot), POILabel);
	}
/**
 * The data point for this graph in the angle in degrees and the distance of the shot	
 */
	public DataPoint getDataPoint(Shot shot){
		return new DataPoint(shot.getAngleDegrees(), shot.getDistance());
	}

}
