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
 * The Calculations class does the majority of the calculations for each
 * graph. It handles all of the variables for the projectile.
 * 
 * @author Luke Van Hulle
 *
 */
public abstract class Calculations {
	
//	PApplet p;//an instance of the PApplet to pass to the graphs
	 
	static double mass = 4.53, //mass of projectile kg
	    Cl = .09, //coefficient of lift
	    dia = .22, //diameter of projectile m
	    initialY = 8,//Height above the ground of the projectile at release m
	    rho = 1.204,//(.810 air density in Moab Utah at 80F) (1.204 sea level) density of air kg/m^3
	    area = (dia*dia*Math.PI/4);//cross sectional area of projectile
	
	double da = Math.PI/4/100, //100 steps between 0 and 45 degrees
	    dt, //seconds between calculations
	    Cd, //coefficient of drag
	    b1Variable,//the variable changed by button 1
	    b2Variable;//the variable change by button 2
	
	final static int MAXDATAPOINTS = 50, //the number of data points for each graph
					FACTOR = 1000,//used to stop rounding errors in a few methods
					//the condition buttons are created in order and these variable denote the order
					MASS = 0,//the mass button is first etc.
					CL = 1,
					DIA = 2,
					INITIALY = 3,
					RHO = 4,
					MACH = 340;//the speed of sound in meters per second
	final double STARTANGLE = 46.0/360 * 2*Math.PI,//to increase the speed for finding the optimum angle start at 46 degrees in radians
				MINDT = .001,//The lowest delta t, dt, used in the shot() method. Smaller takes longer, larger is less accurate
				MAXDT = .5, //The highest dt used in the shot() method
				g = -9.806; //m/s^2 acceleration due to gravity 45degrees latitude at sea level,  See Resource 1 at bottom
			    
	
	
	boolean sameAxis = false;//if the 2 button variables control the upper and lower ends of the
								//same axis on a graph set this to true so they don't cross each other
	
	int lineColor;//the color of the data line on the graph
	
	Graph graph;//the Graph object where all the data will go to be drawn
	
	String xLabel,//the label for the X-axis
			yLabel, //the Y axis label
			POILabel,//some graphs have a point of interest, typically a maximum on a curve, it get a shorter label
			b1Label,//the label next to button number 1
			b2Label,//the label next to button number 2
			title; //the title for the graph
/**
 * The constructor that starts it all off and creates the graph	
 * @param p
 */
	public Calculations(PApplet p){
//		this.p = p;
		graph = new Graph(p);
	}

	/**
	 * This is the big number cruncher method. With the initial velocity and launch angle it
	 * takes calculates the shot taking into account all of the projectile variables plus
	 * the air density, accounting for air resistance with the coefficient of drag, Cd, and magnus affect
	 * which causes lift due to the backwards rotation of the projectile with the coefficient of lift, Cl.
	 * To reduce calculation time but still maintain accuracy the delta t, dt, value is dynamically changed
	 * throughout the simulation. It is set to the inverse of the magnitude of the acceleration squared
	 * dt = 1/magAcc^2, with a lower bounds of MINDT and an upper bounds of MAXDT. This makes dt small when the accelerations are
	 * large but also prevents dt from getting to large when the projectile nears terminal velocity on the freefall back down
	 * The completed shot is returned
	 * 
	 * @param initialVelocity the initial launch velocity
	 * @param angle the initial angle of travel in radians
	 * @return the completed shot
	 */
		public Shot shot(double initialVelocity, double angle){
			double velocity = initialVelocity;//set the velocity to the initial velocity
			double velX = velocity * Math.cos(angle);//break the velocity into its component, the X velocity
			double velY = velocity * Math.sin(angle);//the Y component of velocity
			double posX = 0;//start and at position 0
			double posY = initialY;//set the initial Y location
			double hangTime = 0;//a variable to accumulate the hang time
			double accX = 0;//the acceleration in the X direction
			double accY = 0;//the acceleration in the Y direction
			double maxHeight = 0;//will store the max height of the shot
			
				//while the projectile is above the ground do the calculations
			while(posY >= 0){
				if(posX < 0) return null;// if the shot goes backwards the method returns null
				
				setCoefficientOfDrag(velocity);
					//the acceleration in X. The lift is dependent on the velocity in Y, the Coefficient of lift, Cl, and drag, Cd
				accX = -.5 * rho * velocity * area * (velX*Cd + velY*Cl)/mass;
					//the acceleration in Y. The same coefficient that causes the lift causes a little extra drag dependent on the velocity in X
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
				
				posX += velX*dt + .5*accX*dt*dt;//the distance traveled is added to the current position in X
				posY += velY*dt + .5*accY*dt*dt;//and in Y

				velX += accX*dt;//the change in velocity X is added to velX
				velY += accY*dt;//the same in Y
				velocity = Math.sqrt(velX*velX+velY*velY);//the new magnitude of the velocity
				hangTime += dt;//increment the hang time
					//if the posY is greater than maxHeight, set as the new maxHeight
				if(posY > maxHeight) maxHeight = posY;
			}
				//the final position calculated is a little below the ground since the delta wont come out
				//perfect every time. This step figures out how long ago the projectile actually hit the ground
				//based on the last known position, velocity, and accelerations in Y 
			double t = (-velY - Math.sqrt(velY*velY + 2*accY*posY))/accY;
				//the position in X calculated from the time above and its last position, velocity, and acceleration
			posX = posX - velX*t-.5*accX*t*t;
			
				//if posX is less than 0 the shot went backwards and isn't valid so return null
			if(posX < 0){
				return null;
			}
				//return the completed shot
			return new Shot(angle, posX, hangTime, maxHeight, initialVelocity);
		}
/**
 * This method sets the coefficient of drag based on data noted in Reference 2
 * 		
 * @param velocity the velocity at which the Cd is to be found
 */
		public void setCoefficientOfDrag(double velocity){
			//If the velocity is < .65*Mach use the default value of Cd = .3
			if(velocity < .65*MACH) Cd = .3;
			//from the data in Reference 2a this formula was derived for the range of
			//.65 mach to .96 mach
			else if(velocity < .96 * MACH){
				Cd = 1.67 * velocity/MACH - .786;
			}
			//1 approximates Cd from .96 mach to mach 2.5
			else if(velocity < 2.5 * MACH) Cd = 1;
			else Cd = .9;//everything beyond mach 2.5 assume Cd = .9
		}
/**
 * After one shot has been tested for a given velocity the next velocity will have an optimum launch angle
 * close to the previous launch angle. This method uses the optimized launch angle from the previous shot 
 * as a starting angle, tests if the angle should be higher or lower, and then begins calculations from there.
 * An angle with its initial velocity is sent to the shot() method, which calculates the shot.
 * That shot is then compared to the previous shot. If it is less we know that the previous angle was the optimum angle.
 * That previous shot is then returned. 
 * 	
 * @param velocity the initial launch velocity
 * @param angle the optimized angle from the previous launch in radians
 * @return the optimized shot
 */
	public Shot optimizeAngle(double velocity, double angle){
		Shot previousShot = new Shot();//stores the previous shot
		Shot currentShot = shot(velocity, angle);//calculates the shot with the new velocity and old angle
		if(currentShot == null) return null;
			//if a delta angle, da, of the opposite sign makes the shot go farther, switch the sign of da
		if(currentShot.compareTo(shot(velocity, angle - da)) == -1) da *= -1;
			//calls the compareTo() method for the shot to see if it is farther than the previous
			//if it is it keeps going.
		while(currentShot.compareTo(previousShot) >= 0){
			angle += da;//increment the angle by da
			previousShot = currentShot;//set the current shot to the previous shot
			currentShot = shot(velocity, angle);//call the shot method to get the next shot
			if(currentShot == null) break; //if the shot goes backwards the shot() method returns null
		}
		return previousShot;//return the previous shot which went the farthest
	}
/**
 * For the first shot without a reference angle this method is used. Performs the same functions
 * as the previous method.	
 * @param velocity the input velocity for which to find the optimum angle
 * @return the optimized shot
 */
	public Shot optimizeAngle(double velocity){
		da = Math.abs(da);//the other optimizeAngle method may leave da negative, this makes sure it's positive
		double angle = STARTANGLE; //instead of a reference angle the STARTANGLE variable is used as the first angle
		Shot previousShot = new Shot();//initiates the variable for storing the previous shot
		Shot currentShot = shot(velocity, angle);//the first shot
		
		if(currentShot == null) return null; //if the STARTANGLE does not produce a valid shot return null
		
			//compares currentShot with previousShot continues until currentShot is shorter
		while(currentShot.compareTo(previousShot) >= 0){
			angle -= da;//decrement the angle by da
			previousShot = currentShot;//set the current shot to the previous shot
			currentShot = shot(velocity, angle);//call the shot method to get the next shot
			if(currentShot == null) break;//if the shot goes backwards the shot() method returns null
		}
		return previousShot; //return the previous shot which went the farthest
	}
/**
 * The adjustCondition() method handles changing some of the initial conditions of the shot,
 * mass, coefficient of Lift, diameter of projectile, initial release height, and the density of the air
 * 
 * It takes in two parameters, the first is the number of the variable to be changed, the second it
 * either a -1 or +1 from the button action. The switch statement then tests which variable should be switched
 * and uses a multiplication factor to scale the action. Most of the variable are also checked to ensure they are not
 * changed to invalid numbers, mostly negative masses, diameter, etc.
 * 
 * It then returns the new value for that variable used in the FullButton label.
 * 	
 * @param variable the number of the variable to be switched
 * @param action either -1 or +1
 * @return the new value for the changed variable
 */
	public static double  adjustCondition(int variable, int action){
		switch (variable){
			case MASS:
				mass += action * .05;
				if(mass < .05) mass = .05;
				return mass;
			case CL:
				//The coefficient of lift is allowed to be either positive or negative because
				//it would be possible for an air cannon to put top spin on a projectile instead of
				//the back spin you get from a sling
				Cl += action * .005;
				return Cl;
			case DIA:
				dia += action * .01;
				if(dia < .01) dia = .01;
				area = (dia*dia*Math.PI/4);
				return dia;
			case INITIALY:
				initialY += action *.5;
				if(initialY < 0) initialY = 0;
				return initialY;
			case RHO:
				rho += action *.002;
				if(rho < 0) rho = 0;
				return rho;
			//if non of the listed variables are selected an error message is printed to the system
			//and 0 is returned
			default:
				System.out.println("Calculations: adjustCondition() error");
			return 0;
		}
	}
/**
 * b1Action() takes in the action from a button -1 or 1 if pressed, 0 if not pressed
 * if the action is non zero the b1Variable is changed and then tested to make sure it is valid
 * 	
 * @param a the button action -1 or 1 if pressed else 0
 */
	public void b1Action(int a){
		if(a != 0){
			b1Variable += a;
			if(b1Variable < 1) b1Variable = 1;//non of the b1Variables are valid for negative numbers
			if(sameAxis) checkBVariables();//if the variable are on the same axis check to make sure they haven't crossed
			reCalculate();//clears and recalculates the graph
		}
	}
/**
 * b2Action() performs the same function as b1Action() just for b2Variable. This method is
 * over ridden once in the FlighPath class to handle the radians
 * 	
 * @param a the button action -1 or 1 if pressed else 0
 */
	public void b2Action(int a){
		if(a != 0){
			b2Variable += a;
			if(b2Variable < 1) b2Variable = 1;
			if(sameAxis) checkBVariables();
			reCalculate();//clears and recalculates the graph
		}
	}
/**
 * if b1Variable and b2Variable are on the same axis this method makes sure that the
 * b2Variable always stays less than the b1Variable	
 */
	public void checkBVariables(){
		if(b2Variable <= b1Variable) b2Variable = b1Variable + 1;
	}
/**
 * addDataPoint takes in a shot, calls the getDataPoint method, abstract in this class, and adds
 * that data point to the graph
 * 	
 * @param shot the shot who's DataPoint is to be added
 */
	public void addDataPoint(Shot shot){
		graph.addDataPoint(getDataPoint(shot));
	}
	
/**
 * calls the draw function for its graph	
 */
	public void draw(){
		graph.draw();
	}
/**
 * sends the labels and title to the graph
 */
	public void setLabels(){
		graph.setLabels(xLabel, yLabel, title);
	}
/**
 * sets the line color for the graph	
 */
	public void setLineColor(){
		graph.setLineColor(lineColor);
	}
/**
 * returns the graph so it can be drawn ProjectileMotionforChunkers class	
 * @return
 */
	public Graph getGraph(){
		return graph;
	}
/**
 * returns the b1Label, this is used for the button label
 * @return the b1Label
 */
	public String getB1Label(){
		return b1Label;
	}
/**
 * returns the b2Label, used for the button label
 * @return
 */
	public String getB2Label(){
		return b2Label;
	}
/**
 * when the graph needs to be recalculated this method is called it
 * clears the current graph and then calls the calculated method	
 */
	public void reCalculate(){
		graph.clearGraph();
		calculate();
	}
/**
 * this method does the calculations to get the data for the individual graphs	
 */
	public abstract void calculate();
/**
 * each class overrides this method to parse out the important data from
 * their shot which is then sent to the graph
 * 
 * @param shot the shot whose data we want
 * @return a DataPoint object with the shots desired data points
 */
	public abstract DataPoint getDataPoint(Shot shot);
}

/**
 * Resource 1
 * 
 * Glover, Thomas. "Acceleration Due to Gravity." Pocket Ref. 3rd ed. Littleton, Co: Sequoia Publ., 2005. 334. Print.
 * 
 * Although gravity does wary with latitude and altitude that value of 9.806 was chosen to be a fair approximation for
 * most locations. It should be noted that even on the equator at 9km altitude (higher than Mt Everest) gravity is only 0.5% less 
 */

/**
 * Resource 2
 * 
 * Resource 2a is the source the Coefficent of Drag data was derived from since it most closely matched the projectile's
 * dimensions and high end speeds. The data was taken from Figure 2 in the report. The default value of Cd = 0.3
 * for velocities below 0.65 Mach is derived and estimated through a wide number of graphs and seems to best fit
 * data for a rough sphere at the Reynolds Numbers expected by a pumpkin. Resource 2b was used to generally confirm
 * the Cd for projectiles of smaller diameters. That data could be parsed through to create a potentially more accurate
 * simulator for smaller, smooth, faster moving projectiles but it was considered beyond the scope of this project.
 * Only a small amount of searching will show that calculating Cd is dependent on a wide range of variables,
 * projectile shape, size, surface finish, air density, velocity, and spin.
 * 
 * Resource 2a 
 * 
 * 	Areodynamics of a Sphere and an Oblate Spheroid for Mach Numbers from
 * 	0.6 to 10.5 including some effects of Test Conditions
 * 
 *	M. Leroy Spearman and Dorothy O. Braswell
 * 	August 1993
 * 	http://ntrs.nasa.gov/archive/nasa/casi.ntrs.nasa.gov/19940008699_1994008699.pdf
 * 
 * 
 * Resource 2b
 * 
 * 	Free-Flight Measuremens of Sphere Drag at Subsonic, Transonic, Supersonic,
 * 	and Hypersonic Speeds for Continuum, Transitions, and Near-Free-Molecular
 * 	Flow Conditions
 * 
 * 	A. B. Baily and J. Hiatt, ARO, Inc
 * 	March 1971
 * 	http://www.dtic.mil/cgi-bin/GetTRDoc?AD=AD0721208
 * 
 */

