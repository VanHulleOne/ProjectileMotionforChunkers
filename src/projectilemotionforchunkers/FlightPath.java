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
 * FlightPath handles everything for an individual shot
 * 
 * This class calculates the position in X and Y of the projectile and places
 * them into a graph
 * 
 * @author Luke Van Hulle
 *
 */
public class FlightPath extends Calculations {
/**
 * The constructor sets all of the necessary variables and initial conditions and then
 * calculates the graph
 * 
 * @param p the PApplet
 */
	public FlightPath(PApplet p) {
		super(p);
		xLabel = "Distance (m)";
		yLabel = "Height m";
		title = "Flight Path";
		POILabel = " meters out";
		lineColor = Graph.orange;
		
		b1Label = "Initial\nVelocity";
		b2Label = "Launch\nAngle";
		//The b1&2Variables need to be initialized to some value
		b1Variable = 300;//the initial launch velocity
		b2Variable = 41.0/360 * 2*Math.PI;//the initial launch angle
		
		setLineColor();
		setLabels();
		calculate();
	}
/**
 * calculate() overrides the Calculations method. It is nearly identical to the shot() method except that
 * it has a few extra steps so  it can pass the data points of the X and Y position
 * to the graph
 * 
 */
	@Override
	public void calculate() {
		double initialVel = b1Variable;//the b1Variable is the initial launch velocity
		double angle = b2Variable;//the b2Variable is the launch angle
		//the special label is the launch velocity and the launch angle in degrees
		graph.setSpecialLabel("Vi: " +(int)initialVel + " m/s"+ 
				"\nAngle: " + (int) (angle/(2*Math.PI)*360)+" degrees");
		
		//the first line in the try statement may throw an error it the shot is null
		try{
			//this runs the complete shot to get the final distance which is then divided by the
			//MAXDATAPOINTS to get the delta x, dx, interval
			double dx = shot(initialVel, angle).getDistance()/MAXDATAPOINTS;

			double nextPosX = 0;//stores the next posX needed at which a data point should be added
			
			double velocity = initialVel;//the current velocity during the flight
			double velX = velocity * Math.cos(angle);//velocity in the X direction
			double velY = velocity * Math.sin(angle);//velocity in the Y direction
			double posX = 0;//current position in X
			double posY = initialY;//the initial release height in Y
			double hangTime = 0;
			double accX = 0;
			double accY = 0;
			double maxHeight = 0;//stores data for the point of interest
			double xOfMaxHeight = 0;//stores data for the point of interest
			  
			do{
				setCoefficientOfDrag(velocity);//in the super class to set the Cd
				//see Calculations shot() for more detail
				accX = -.5 * rho * velocity * area * (velX*Cd + velY*Cl)/mass;
				accY = -.5 * rho * velocity * area * (velY*Cd - velX*Cl)/mass + g;
				
				//this is the inverse magnitude of the acceleration squared used for determining delta t, dt 
				//the magnitude would be a sqrt which the square cancels out
				//the conditional operator is added here to prevent the divide by zero error which could occur at terminal velocity
				double invsMagAccSqrd = (accX*accX+accY*accY) > 0 ? 1.0/(accX*accX + accY*accY) : MAXDT;
				//if invsMagAccSqrd is less than MINDT it is set to MINDT
				//if it is greater than MAXDT it is set to MAXDT
				//else set dt to invsMagAccSqrd
				if(invsMagAccSqrd < MINDT) dt = MINDT;
				else if(invsMagAccSqrd > MAXDT) dt = MAXDT;
				else dt = invsMagAccSqrd;
				
					posX += velX*dt + .5*accX*dt*dt;
					posY += velY*dt + .5*accY*dt*dt;
				
					velX += accX*dt;
					velY += accY*dt;
					velocity = Math.sqrt(velX*velX+velY*velY);
					hangTime += dt;
				
				//tests to see if the current height is more than the stored max height
				//if it is set the new maxHeight and xOfMaxHeight
				if(posY > maxHeight){
					maxHeight = posY;
					xOfMaxHeight = posX;
				}
				
				//if the posX is >= nextPosX add the data points and increment nextPosX
				if(posX >= nextPosX){
					addDataPoint(posX, posY);
					nextPosX += dx;
				}		  
			}while(posY > 0);//while still above the ground
			
			//how long ago the projectile was at the ground y=0
			double t = (-velY - Math.sqrt(velY*velY + 2*accY*posY))/accY;
			//the position in X calculated from the time above and its last position, velocity, and acceleration
			posX = posX - velX*t-.5*accX*t*t;
			
			addDataPoint(posX, posY);//add the last data point
			//set the point of interest
			graph.setPointOfInterest(new DataPoint(xOfMaxHeight, maxHeight), POILabel);
		}
		//the required catch statement for the try nothing needed in there
		catch(Exception e){

		}
		
	}
/**
 * takes in the two data point locations and add's them to the graph
 * @param x
 * @param y
 */
	public void addDataPoint(double x, double y){
		graph.addDataPoint(new DataPoint(x, y));
	}
/**
 * the +/-1 received from the button action needs to be converted to radians
 * otherwise the calculations don't work	
 */
	@Override
	public void b2Action(int a){
		if(a != 0){
			b2Variable += 1.0*a/360 * 2*Math.PI;
			if(b2Variable < .017) b2Variable = .017; //.017 is 1 degree worth of radians
			else if(b2Variable > Math.PI/2) b2Variable = Math.PI/2; //don't do over 90 degrees
			reCalculate();
		}
	}
/**
 * This method isn't needed but is abstract in the parent class Calculations
 * so it needed to be over ridden here 
 */
	@Override
	public DataPoint getDataPoint(Shot shot) {
		return null;
	}

}
