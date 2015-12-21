package projectilemotionforchunkers;

import processing.core.PApplet;
/**
 * Projectile Motion for Chunkers is designed to help accurately determine the potential range 
 * of your machine as well as the optimum release angle under various conditions. 
 *
 *           Copyright (C) 2012 Luke Van Hulle
 *           Contact: BeardedOne85@gmail.com
 */

/*
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

/*
 * The main class that runs everything. Creates all of the Calculations and handles all of the events.
 * 
 * This class creates the events, places and moves the graphs, creates the buttons and handles all
 * of their actions.
 * 
 * The purpose of this program is to provide insight into projectile motion in various physical environments.
 * The program takes into account the coefficent of drag (wind resistance) and coefficient of lift and will
 * allows the user to know both how far a projectile can go and what launch angles will make it go the farthest.
 * It was initially intended for use with pumpkin chunking style launching machine and can be used on all types,
 * from trebuchets and catapults to air cannons. It tells you what is happening with your projectile the moment
 * it is released. 
 * 
 * When the noLoop() method is used causing draw() to not be looped the program encounters some errors
 * and behaves incorrectly. Mostly the graphs don't scale properly sometimes and the graphics semi freeze
 * up. These problems go away when noLoop() is not used but then the processor is running non-stop to just
 * leave the graphics the same as they were. I think this is a problem with Processing itself and could
 * maybe be fixed if the program was written in an actual java applet, which would make a bunch of stuff
 * a lot harder but would make the buttons easier.
 * 
 * This program also has some problems with very low density projectiles, large diameters and low masses.
 * Somewhere near beach ball or balloon status. I think they decelerate too much at the start of their
 * launch for the program to handle properly. if the delta t, dt, was decreased it might help but since
 * these were outside the scope of this project I have not experimented with it much.
 * 
 * See the Read_Me.txt file for further details
 * 
 * @author Luke Van Hulle
 * 
 */
public class ProjectileMotionforChunkers extends PApplet {
	final int RECALCBUT = 5,//the offset of the recalculate button, the last in the conditionButtons array
				BUTTON1 = 0,//offset of the first graph button
				BUTTON2 = 1,//offset of the 2nd graph button
				NOGRAPH = -1;//this is the value for movingGraph when it isn't holding a graph
	final int[] LARGEGRAPH = {5, 5};//the XY location, upper left, of the graph that is large
	
	int movingGraph = NOGRAPH,//when a graph is clicked this variable stores its location in the graph array
		oldMouseX,//to keep track of a graph being dragged these two variables are used
		oldMouseY;
	
	boolean endLoop = false;//if noLoop() is used this boolean ends the loop() after a button press
	
	//the X and Y locations, upper right, of the graphs
	final int [][] graphLocations = {LARGEGRAPH,
								{675, 0},
								{675, 200},
								{675, 400}},
				//the X Y locations of the condition buttons
				conditionButtonLocations = {{15, 500},
											{15, 550},
											{215, 500},
											{215, 550},
											{415, 500},
											{415, 550}};//the last one is the recalculateButton
	//the labels for the condition buttons.
	String [] conditionButtonLabels = {" kg", " Coeff of Lift", " diameter (m)", 
			" Release Height (m)", " air density (kg/m^3)", "Recalculate"};

	//A 2-d array to create and store all of the buttons for the graphs
	//the first dimension denotes which graph, the second denotes the 1st or 2nd button for that graph
	FullButton[][] graphButtons = new FullButton[4][2];
	//I'm calling the buttons on the bottom of the GUI that affect all of the graphs Condition Buttons
	FullButton [] conditionButtons = new FullButton[RECALCBUT+1];
	FullButton recalculateButton;//the recalculate button reruns the calculations after the conditions have been changed
	
	//creates and holds the various Calculations
	Calculations[] calcs = new Calculations[4];
	//holds the graphs for each calculation
	Graph [] graphs = new Graph[4];
	
/*
 * Sets up all of the Calculations, buttons, and graphs
 */
	public void setup() {
		size(1000, 600);
		createCalcs();
		createButtons();
		createGraphs();
		smooth();//A PApplet method to smooth out the graphics
//TODO	Uncomment the following line and 2 others
//		noLoop();//to reduce processor demand this is used. It doesn't quite work. More explanation in draw()
	}
/*
 * Initializes all of the calculations and places them in their array. The print lines and millis in
 * this class can be used to test how long some calculations take. Before the dynamic dt was used in the shot()
 * method some took almost two seconds causing jerky graphics and slow variable changes. I had considered putting 
 */
	public void createCalcs(){
//		long pm = millis();
		calcs[0] = new DistanceVsVelocity(this);
//		println(millis() - pm);
//		pm = millis();
		calcs[1] = new OptimumReleaseAngleVsVelocity(this);
//		println(millis() - pm);
//		pm = millis();
		calcs[2] = new DistanceVsReleaseAngle(this);
//		println(millis() - pm);
//		pm = millis();
		calcs[3] = new FlightPath(this);
//		println(millis() - pm);
	}
/*
 * Although the method is called creatGraphs() it really just gets the graphs
 * from the Calculations. Then it sets their location and size	
 */
	public void createGraphs(){
		for(int i = 0; i < calcs.length; i++){
			graphs[i] = calcs[i].getGraph();//calls the graphs and sets them in the array
			graphs[i].setLocXY(graphLocations[i]);//sets the location of the graph
			//if it is the LARGEGRAPH locations, set isLarge to true else false
			graphs[i].setGraphIsLarge(graphLocations[i] == LARGEGRAPH ? true : false);
		}
	}
/*
 * This method does create buttons. It creates both the buttons located near each graph, the graphButtons,
 * and the buttons on the bottom of the GUI, the conditionButtons.
 */
	public void createButtons(){
		//the graph buttons are in a 2D array, the first dimension tells which graph they are in, the 2nd
		//dimension is the 1st or 2nd button. If a graph doesn't have a second button its label return is null
		//and no button is created in that position
		for(int graphNum = 0; graphNum < calcs.length; graphNum++){
			graphButtons[graphNum][BUTTON1] = new FullButton(this, calcs[graphNum].getB1Label());
			//not all of the graphs have a 2nd button so the label is tested before it is applied
			if(calcs[graphNum].getB2Label() != null){
				graphButtons[graphNum][BUTTON2] = new FullButton(this, calcs[graphNum].getB2Label());
			}
		}
		//the condition buttons are stored in a 1D array with the last one being the
		//recalculate button which gets some special treatment in a few places
		for(int i = 0; i < conditionButtons.length; i++){
			//if it is the recalculate buttons position
			if(i == RECALCBUT){
				//the button uses its own constructor so set it one color with no internal label
				conditionButtons[RECALCBUT] = new FullButton(this, conditionButtonLabels[RECALCBUT], SubButton.BLANK);
				//make the button not visible
				conditionButtons[RECALCBUT].setIsVisibile(false);
			}
			//all other buttons handle this way
			else{
				//create the button with its label from the conditionButtonLabels array
				conditionButtons[i] = new FullButton(this, conditionButtonLabels[i]);
				//this is a bit of a cheat to set the value next to the buttons label but it works
				//it calls the adjustCondition(int, int) method in Calculations but sends it zero
				//for it's action, one of the few magic numbers that is used.
				conditionButtons[i].setValueLabel(Calculations.adjustCondition(i, 0));
			}
			//get the location from the location array
			conditionButtons[i].setButtonLocation(conditionButtonLocations[i]);
		}
	}
/*
 * This method was created to get everything that wasn't drawing out of the draw() method.
 * It sets the locations for the  graphButtons as well as gets the actions
 * for all of the buttons except the recalculate button which is handled right when it is pressed
 */
	public void moveAndAdjustButtons(){
		//This for() loop sets the locations of the buttons by getting the information from the graphs
		for(int graphNum = 0; graphNum < graphs.length; graphNum++){
			graphButtons[graphNum][BUTTON1].setButtonLocation(graphs[graphNum].getB1Location());
			//not all graphs have a Button2 so it is tested here
			if(graphButtons[graphNum][BUTTON2] != null){
				graphButtons[graphNum][BUTTON2].setButtonLocation(graphs[graphNum].getB2Location());
			}
		}
		//the actions in this for() loop could be combined with the previous for loop
		//however i have kept them separate since they are a little complex to view as it is
		//this for() loop gets the actions from each button and sends them to the appropriate
		//calculation. If the button isn't pressed it sends a 0
		for(int calcNum = 0; calcNum < calcs.length; calcNum++){
			calcs[calcNum].b1Action(graphButtons[calcNum][BUTTON1].getAction());
			//not all graphs have a Button2 so it is tested here
			if(graphButtons[calcNum][BUTTON2] != null){
				calcs[calcNum].b2Action(graphButtons[calcNum][BUTTON2].getAction());
			}
		}
		//the condition buttons, except for the recalculate button, are tested to see if they
		//are pressed. If they are their valueLabel is updated and the adjustContion() method is called
		for(int i = 0; i < conditionButtons.length-1; i++){
			if(conditionButtons[i].isPressed()){//is the button is pressed
				//set the label for the current button to the return of adjustCondition()
				//send adjustCondition i, the condition to change, and...
				conditionButtons[i].setValueLabel(Calculations.adjustCondition(i,
						//...the buttons action
						conditionButtons[i].getAction()));
				break;//only one button can be pressed so break the for loop and stop checking them
			}
		}
	}
/*
 * The draw() method calls the individual draw() methods for each object
 * 
 * 	
 * I wish I could get this to work so draw() wasn't running non-stop when nothing was happening
 *	It should work like this: noLoop() is called in setup() but it default calls draw() once.
 *	when the mouse is pressed loop() is called to make it constantly update the gui while stuff is changing
 *	then when the mouse is released, endLoop is set to true so that draw() is run one last time
 *	comes into the if statement, sets endLoop so false, and calls noLoop(), stopping the continuous draw()
 *	calls and reducing processor needs.
 *	
 *	This all seems to work just fine, the steps go as listed, but for some reason it messes up the graphs, their data doesn't get
 *	mapped quite right and some problems moving graphs. You click and drag a graph but nothing happens. If you drag it over
 *	another graph still nothing happens, but click again and it moves.
 *	
 *	There is a possibility that these drawing errors are caused because everything isn't actually drawn in this classes
 *	draw() method.
 */
	public void draw(){
		background(150);
		
		moveAndAdjustButtons();//makes sure the buttons are in the right spot and their actions are handled
		
		//enhanced for loop to draw all of the condition buttons
		for(FullButton b : conditionButtons) b.draw();
		//enhanced for loop for the graph buttons. This is done in two steps
		//the first one breaks the 2D array into 1D arrays, tempArray
		for(FullButton [] tempArray : graphButtons){
			//the second one breaks the 1D tempArray into its individual objects
			for(FullButton button : tempArray){
				if(button != null) button.draw();//Some may be null so they are tested before they are drawn
			}
		}		
		//enhanced for loop to draw all of the graphs
		for(Graph g : graphs) g.draw();
		
		//read the java doc to find out why this if statement should be used but isn't 
		if(endLoop){
			endLoop = false;
			noLoop();
		}
	}

/*
 * This method rearranges the graphs when they are clicked and dragged over each other in the gui,
 * most notably to make a graph large. 
 * 
 * Basically it is testing to see if the mouse is over a graph that isn't the one the mouse is
 * currently dragging. If the mouse is the method switches the order of the graphLocations array
 * so those two graphs switch locations. This method is only called when the mouse is dragged and
 * it is moving a graph (movingGraph != NOGRAPH)
 * 
 */
	public void rearrangeGraphs(){
		//run through the graphs array
		for(int i = 0; i < graphs.length; i++){
			//if the mouse in on a graph and it isn't the movingGraph which is being moved
			if(graphs[i].isOnGraph() && i != movingGraph){
				//create a temporary array with the location of the movingGraph
				int [] tempArray = graphLocations[movingGraph];
				//change the spot in the graphLocations that was holding the moving graph
				//to the location of the graph it was just moved over
				graphLocations[movingGraph] = graphLocations[i];
				//change the movingGraph's old location to the location of the graph it just replaced
				graphLocations[i] = tempArray;
				
				//after the graphLocations array has been rearranged send the new locations to the graphs
				//and make the correct graph large
				for(int j = 0; j < graphs.length; j++){
					graphs[j].setLocXY(graphLocations[j]);//sets the location
					//if the location is the LARGEGRAPH location, set isLarge to true, else false
					graphs[j].setGraphIsLarge(graphLocations[j] == LARGEGRAPH ? true : false);
				}
				break;//when the graph has moved stop checking them
			}
		}		
	}
/*
 * mousePress() is activated when they mouse is pressed. It tests if the press is on a button
 * or graph and lets them take it from there.	
 */
	public void mousePressed(){
//TODO this is the 2nd of 3 lines that needs to be uncommented to reduce processor requirements
//		loop();
		//run through the conditionButtons
		for(int i = 0; i < conditionButtons.length; i++){
			//testPress() returns true if the press was on the button
			if(conditionButtons[i].testPress()){
				//check if it is the recalculate button and if it isVisible
				if(i == RECALCBUT && conditionButtons[RECALCBUT].getIsVisible()){
					//recalculate all of the Calculations with an enhanced for loop
					for(Calculations calc : calcs) calc.reCalculate();
					//set isVisible to false so you can't see the button
					conditionButtons[RECALCBUT].setIsVisibile(false);
				}
				else if(i != RECALCBUT){//if it was not the recalculate button
					//make the recalculate button visible
					conditionButtons[RECALCBUT].setIsVisibile(true);
				}
				break;//there can only be one button pressed at a time so if there was one pressed break to end the for loop
			}
		}
		//Two enhanced for loops to test the graph buttons, one to break it into 1D arrays
		//the second to test the individual objects
		for(FullButton [] tempArray : graphButtons){
			for(FullButton button: tempArray){
				//some locations are null so test them
				if(button != null && button.testPress()) break;
			}
		}
		//test the graphs so see if the mouse press was on them
		for(int i = 0; i < graphs.length; i++){			
			//if the press was on a graph
			if(graphs[i].isOnGraph()){
				movingGraph = i;//set movingGraph to the graph number
				break;//stop testing graphs
			}
		}
		//these two are used for dragging the graphs, they store the XY location of the press
		oldMouseX = mouseX;
		oldMouseY = mouseY;
	}
/*
 * When they mouse is released release all of the buttons and if a graph was moving
 * set its location.
 */
	public void mouseReleased(){
		//the two enhanced for loops to release all of the graphButtons
		for(FullButton [] tempArray : graphButtons){
			for(FullButton button : tempArray){
				//make sure non of the buttons equal null
				if(button != null) button.released();
			}
		}
		//enhanced for loop to release all of the condition buttons
		for(FullButton button : conditionButtons) button.released();

		//if a graph was moving set its location
		if(movingGraph != NOGRAPH){
			graphs[movingGraph].setLocXY(graphLocations[movingGraph]);
			movingGraph = NOGRAPH;//set movingGraph back to NOGRAPH
		}
//TODO The third line to uncomment		
//		endLoop = true;
	}

/*
 * moves the graph on the screen and calls rearrangeGraphs() to switch graphs if needed
 */
	public void mouseDragged(){
		//if there is a moving graph
		if(movingGraph != NOGRAPH){
			//send the graph how much it should move in X and Y
			graphs[movingGraph].moveGraph(oldMouseX - mouseX, oldMouseY - mouseY);
			oldMouseX = mouseX;//set oldMouseX to the current mouseX
			oldMouseY = mouseY;//set oldMouseY to the current mouseY
			rearrangeGraphs();//call to rearrange the graphs
		}
	}
}


