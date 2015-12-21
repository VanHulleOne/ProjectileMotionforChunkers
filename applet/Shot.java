package projectilemotionforchunkers;
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
 * This class holds all of the data for an individual shot
 * 
 * @author Luke Van Hulle
 *
 */
public class Shot {
	private double distance,//how far the shot went
				maxHeight,//what the maximum height was
				angleRadians,//its launch angle in radians
				hangTime,//how long it was up in the air, currently unused
				initialVelocity;//the initial lanuch velocity
/**
 * The constructor receives all of the data for the shot
 * 	
 * @param a launch angle, radians
 * @param d distance, meters
 * @param h hang time, seconds
 * @param m  max height, meters
 * @param i initial velocity, meters/second
 */
	public Shot (double a, double d, double h, double m, double i){
	  angleRadians = a;
	  distance = d;
	  hangTime = h;
	  maxHeight = m;
	  initialVelocity = i;
	}
/**
 * a default constructor that I thought I don't need add but it appears I do		
 */
	public Shot() {	}
/**
 * Get the Hang time	
 * @return the hang time
 */
	public double getHangTime(){
		return hangTime;
	}
/**
 * get the distance
 * @return the distance
 */
	double getDistance(){
	  return distance;
	}
/**
 * get the angle in radians	
 * @return the angle in radians
 */
	double getAngleRadians(){
	  return angleRadians;
	}
/**
 * get the angle in degrees, saves some math for the gui	
 * @return angle in degrees
 */
	double getAngleDegrees(){
		return angleRadians/(2*Math.PI)*360;
	}
/**
 * get the initial launch velocity	
 * @return the inital velocity
 */
	double getInitialVelocity(){
		return initialVelocity;
	}
/**
 * The only method that really does something in this class. If this shot
 * went farther than the inputed shot return 1, if it went shorter return -1
 * if they are the same distance it goes to max height, if both distance and
 * max height are the same it return 0.
 * 	
 * @param thatShot the shot to compare with this one
 * @return 1 for farther, -1 for shorter, 0 for the same
 */
	public int compareTo(Shot thatShot){
		if(thatShot == null) return 1;
		if(this.distance > thatShot.distance) return 1;
		if(this.distance < thatShot.distance) return -1;
		if(this.maxHeight > thatShot.maxHeight) return 1;
		return 0;		
	}
/**
 * A toString() is included to help with trouble shooting. This method is not used in the final
 * application	
 */
	public String toString(){
	  return "Angle: " + getAngleDegrees() + "\nVi: " + initialVelocity + "\nDistance: "
	  			+ distance + "\nHang Time: " + hangTime + "\nMax Height: " + maxHeight + "\n\n";
	}
}
