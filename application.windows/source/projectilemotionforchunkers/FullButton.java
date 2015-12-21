package projectilemotionforchunkers;

import java.text.DecimalFormat;
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
 * FullButton holds two instances of SubButton, one to make the variable increase,
 * the other to make the variable decrease. (Except for that pesky reCalculate button of course)
 * 
 * @author Luke Van Hulle
 *
 */
public class FullButton {
	
	PApplet p; //the PApplet used to draw things
	
	DecimalFormat format = new DecimalFormat("0.0##");//formats the decimals of the buttons
	
	int locX, locY;//the upper left corner of the buttons
	final static int FONTSIZE = 12;//the size of the label's font
	String label,//the text next to the button
			valueLabel = "";//the number next to the button
	private boolean isVisible = true;// used only for the recalculate button
	
	SubButton upButton, downButton; //the two SubButtons
/**
 * This constructor takes in the PApplet and the label which goes to the right of the
 * button. It then creates the two SubButtons
 * 
 * @param p the PApplet
 * @param label the buttons label
 */
	public FullButton(PApplet p, String label){
		this.p = p;
		this.label = label;
		//create the up button by passing it p and SubButtons static int UP
		upButton = new SubButton(p, SubButton.UP);
		downButton = new SubButton(p, SubButton.DOWN);
	}
/**
 * This constructor is only used for the recalculate button, in takes in and extra it
 * at the value of Zero which makes the SubButtons the same color
 * 
 * @param p the PApplet
 * @param label the button's label
 * @param i and int at value 0
 */
	public FullButton(PApplet p, String label, int i){
		this.p = p;
		this.label = label;
		upButton = new SubButton(p, i);
		downButton = new SubButton(p, i);
	}
/**
 * Calls draw() from the SubButtons and draws the label	
 */
	public void draw(){
		//if the button is visible, only recalculate is ever not visible
		if(isVisible){
			upButton.draw();
			downButton.draw();
			p.fill(0);//set the text fill color to black
		}
		else p.fill(80);//if not visible gray out the label
		
		p.textAlign(p.LEFT, p.CENTER);//align the text
		p.textSize(FONTSIZE);
		//draw the valueLabel and label next to the button
		p.text(valueLabel + label, locX + SubButton.WIDE + FONTSIZE, locY + SubButton.TALL);
	}
/**
 * Takes in an int array and sets the locations to it
 * 
 * @param loc An int array with the X location in the 0th offset, Y location in the 1st offset
 */
	public void setButtonLocation(int[] loc){
		locX = loc[0];//the location in X
		locY = loc[1];//the location in Y
		//set the location of the upButton
		upButton.setLocation(locX, locY);
		//set the location of the down button just below the upButton
		downButton.setLocation(locX, locY + SubButton.TALL);		
	}
/**
 * Calls the testPress methods for the two buttons and returns true if either
 * of them return true, else return false	
 * @return return true if pressed else false
 */
	public boolean testPress(){
		if(upButton.testPress() || downButton.testPress()) return true;
		return false;
	}
/**
 * Return the action from the button	
 * @return the button's action
 */
	public int getAction(){
		if(upButton.isPressed()) return upButton.getAction();
		if(downButton.isPressed()) return downButton.getAction();
		return 0;
	}
/**
 * releases the buttons	
 */
	public void released(){
		upButton.released();
		downButton.released();
	}
/**
 * When you send the value label this formats it into a nice string
 * 	
 * @param v the value label
 */
	public void setValueLabel(double v){
		valueLabel = format.format(v);
	}
/**
 * Test if a mouse press was on either of the buttons, if yes return true	
 * @return
 */
	public boolean isPressed(){
		if(upButton.isPressed() || downButton.isPressed()) return true;
		return false;
	}
/**
 * Set isVisible to true if you want to see it, false if you don't	
 * @param v what you want isVisble set to
 */
	public void setIsVisibile(boolean v){
		isVisible = v;
	}
/**
 * Gets isVisble	
 * @return isVisible
 */
	public boolean getIsVisible(){
		return isVisible;
	}
}
