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
 * This class does the calculations for the optimized distance vs velocity graph.
 * It extends Calculations.
 * 
 * This class takes the longest to calculate.
 * 
 * @author Luke Van Hulle
 *
 */
public class DistanceVsVelocity extends Calculations{
/**
 * The constructor initializes its variables and takes in an instance of the
 * PApplet to pass it along to its graph
 * 
 * @param p the PApplet
 */
	public DistanceVsVelocity(PApplet p) {
		super(p);
		xLabel = "Velocity (m/s)";
		yLabel = "Distance m";
		title = "Optimum Distance vs Velocity";
		lineColor = graph.red;
		
		b1Label = "Lower\nVelocity";
		b2Label = "Upper\nVelocity";
		b1Variable = 10;//default value for lower velocity, m/s
		b2Variable = 100;//default value for higher velocity. m/s
		
		sameAxis = true;//the two buttons for this calculation control the same axis
		
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
 * This method is identical to the one used in OptimumReleaseAngleVsVelocity
 */
	public void calculate(){
		//to reduce rounding errors that caused some jumpyness issues in the graphs
		//ints and a multiplication factor are used for a few key variables instead of doubles.
		//The ints are multiplied by FACTOR to account for decimals and then divided by
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
 * The data point for this calculation is the initial launch velocity and the
 * distance it went with its best launch angle	
 */
	public DataPoint getDataPoint(Shot shot){
		return new DataPoint(shot.getInitialVelocity(), shot.getDistance());
	}	
}
