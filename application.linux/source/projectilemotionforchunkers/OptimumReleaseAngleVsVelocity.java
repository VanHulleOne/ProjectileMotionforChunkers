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
 * This class calculates the optimum release angle for a range of launch velocities.
 * It extends Calculations
 * 
 * This class takes the 2nd longest to calculate and it's graph is the first one to start showing
 * errant data as projectiles become less dense. I haven't figured out where the data error is coming from
 * but it does work quite nicely for projectiles within the scope of this project.
 * 
 * @author Luke Van Hulle
 *
 */

public class OptimumReleaseAngleVsVelocity extends Calculations{
/**
 * Call the super constructor, set variables
 * 
 * @param p
 */
	public OptimumReleaseAngleVsVelocity(PApplet p) {
		super(p);
		xLabel = "Velocity (m/s)";
		yLabel = "Angle Degrees";
		title = "Optimum Release Angle vs Velocity";
		lineColor = Graph.green;
		
		b1Label = "Lower Vel\nVvsORA";
		b2Label = "Upper Vel\nVvsORA";
		b1Variable = 10;
		b2Variable = 400;
		sameAxis = true;//the two buttons are on the same axis so set true
		da = Math.PI/4/500;//a small da helps keep this graph smooth at lower desnity shots
		
		setLineColor();//in the super class
		setLabels();//in the super class
		calculate();
	}
/**
 * Calculates the data for the graph by sending each launch velocity to optimizeAngle in the super class.
 * After the initial shot the subsequent shots are send to optimizeAngle() with the preceding
 * shot's best angle which drastically reduces the number of iterations it takes to get the optimum
 * angle of the next shot.
 * 
 * This method is identical to calculate() in DistanceVsVelocity
 */
	public void calculate(){
		//to reduce rounding errors that caused some jumpiness issues in the graphs
		//ints and a multiplication factor are used for a few key variables instead of doubles.
		//The ints are multiplied by FACTOR, which accounts for decimals, and then divided by
		//FACTOR when sent to methods that take in doubles
		int currentVel = (int) b1Variable * FACTOR;//starts at the lower velocity
		
		//this variable isn't needed, b2Variable could have been used
		//in each location lastVel is used but it was left in to help make the code
		//easier to understand. One extra double wont jump my memory utilization too high
		int lastVel = (int) b2Variable * FACTOR;//the high velocity where the calculations should stop
		int dv = (lastVel - currentVel)/MAXDATAPOINTS;//the amount to increment the velocity between shots
		Shot currentShot = optimizeAngle(currentVel/FACTOR);//the first shot
		Shot farthestShot = new Shot();
		//while the current velocity is <= the last velocity
		while(currentVel <= lastVel){
			if(currentShot == null) break;//if the shot went backwards stop testing new shots
			addDataPoint(currentShot);//add the datapoint for the current shot
			currentVel += dv;//increment the velocity
			//get the next shot by sending the new velocity and the angle of the previous shot
			currentShot = optimizeAngle(currentVel/FACTOR, currentShot.getAngleRadians());
		}
	}
/**
 * The data for this graph in the initial velocity and the angle that
 * produced the farthest shot for that velocity
 */
	public DataPoint getDataPoint(Shot shot){
		return new DataPoint(shot.getInitialVelocity(), shot.getAngleDegrees());
	}

}
