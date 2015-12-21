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
 * A simple class for storing and recalling the data points used on each graph
 * 
 * @author Luke Van Hulle
 *
 */
public class DataPoint {
	
	float x, y;//the two required datapoints stored as floats so they can be drawn in the PApplet method draw()

/**
 * the constructor that takes in the two data points
 * 
 * @param x
 * @param y
 */
	DataPoint(double x, double y){
		this.x = (float) x;
		this.y = (float) y;
	}
/**
 * returns the X location of the data point
 * @return the Y location
 */
	public float getX() {
		return x;
	}
/**
 * return the Y location of the data point
 * @return the Y location
 */
	public float getY() {
		return y;
	}
/**
 * toString() returns the DataPoint as a string with labels for
 * debugging purposes. It isn't used by any class
 * 
 * @return the String with the data point and some labels
 */
	public String toString(){
		return "X: " + x + " Y: " + y;
	}

}
