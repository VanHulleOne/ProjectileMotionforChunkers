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
 * The SubButton class creates the individual buttons used in the program. It tests if the
 * button has been pressed, returns if it is still pressed, and returns it's action, either
 * -1 or +1. It can return Zero if and FullButton is not used for UpDown purposes. Right now
 * this only happens with the reCalculate button. That button probably should have been another class
 * but I haven't gotten around to that yet.
 * 
 * @author Luke Van Hulle
 * 
 */
public class SubButton {
	
	private PApplet p;//the Processing Applet
	private int color, //the color of the SubButton
		action,//the buttons action, either -1 or +1
		locX,//the left location of the SubButton
		locY;//the upper location of the button 
	
	final static int CLICKTIME = 75,//to help prevent double clicks this is the wait time on a single click
					HOLDTIME = 1500,//if the button has been held longer than HOLDTIME FASTACTION is used
					FASTACTION = 3,//if the button has been held for a while this is multiplied by the action to move it faster
					COLORUP = Graph.blue,//the color of the button if it increases it's variable
					COLORDOWN = Graph.orange,//the color of the button if it decreases
					COLORBLANK = Graph.green,//the reCalculate button is green
					DOWN = -1,//the return if the button decreases
					UP = 1,//the return if the button increases
					BLANK = 0,//the reCalculate button return
					WIDE = 40,//how wide the button is
					TALL = 20,//the height of the button
					FONTSIZE = 10;//the font size for the label inside the button
	
	final static char UPLABEL = '+',//the up label
						DOWNLABEL = '-',//the down label
						BLANKLABEL = ' '; //the blank label for the reCalculate button
	
	private char label;//the label used on the SubButton
	private boolean isPressed;//if the button is pressed this is true
	private long clickMillis, holdMillis;//hold time in millis to determine if the button is pressed or just clicked
/**
 * 	The constructor takes in the PApplet and the button's action and sets
 * the various fields accordingly
 * 
 * @param p the PApplet
 * @param action either -1 or 1 can be 0 for the reCalculate button
 */
	public SubButton(PApplet p, int action){
		this.p = p;
		if(action == UP){
			color = COLORUP;
			label = UPLABEL;
			this.action = UP; 
		}
		else if(action == DOWN){
			color = COLORDOWN;
			label = DOWNLABEL;
			this.action = DOWN;
		}
		else{
			color = COLORBLANK;
			label = BLANKLABEL;
			this.action = BLANK;
		}
	}
/**
 * The draw() method draws the SubButton. The rectangle and it's label
 * to make the reCalculate button look like one button the noStroke() method it called	
 */
	public void draw(){
		p.fill(color);//the color of the rectangle
			//if the action is BLANK call the noStroke() method to make the reCalculate button look like one button
		if(action == BLANK) p.noStroke();
		else p.stroke(0);
		
		p.rect(locX, locY, WIDE, TALL);//draw the rectangle
		p.fill(0);//set the label color to black
		p.textSize(FONTSIZE);//set the text size
		p.textAlign(p.CENTER, p.CENTER);//set the alignment to put the label in the center
		p.text(label, locX+WIDE/2, locY+TALL/2);//draw the label in the center of the rectangle
	}
/**
 * testPress() checks to see if the mouse click was over the button
 * 	
 * @return true if it was clicked else false
 */
	public boolean testPress(){
		holdMillis = p.millis();//records the current millis for later testing to see how long the button has been held
		if(p.mouseX > locX && p.mouseX < locX + WIDE){
			if(p.mouseY > locY && p.mouseY < locY + TALL){
				isPressed = true;
			}
		}
		return isPressed;
	}
/**
 * setLocation() sets the location for the button with it's upper left corner	
 * @param x the location in X
 * @param y the location in Y
 */
	public void setLocation(int x, int y){
		locX = x;
		locY = y;
	}
/**
 * getAction() returns the action of the button if the button is pressed. It also uses the CLICKTIME
 * variable to help reduce multiple clicks and the HOLDTIME variable to use the FASTACTION multiplier to
 * speed up the action.
 * 
 * if the button is not pressed it returns 0
 * @return the button action
 */
	public int getAction(){
		if(isPressed){
			if(p.millis() - holdMillis > HOLDTIME) return action * FASTACTION;
			if(p.millis() - clickMillis > CLICKTIME){
				clickMillis = p.millis();
				return action;
			}
		}
		return 0;
	}
/**
 * when the button is released released() sets isPressed to false	
 */
	public void released(){
		isPressed = false;
	}
/**
 * returns the isPressed variable
 * @return the boolean variable IsPressed
 */
	public boolean isPressed(){
		return isPressed;
	}
}
