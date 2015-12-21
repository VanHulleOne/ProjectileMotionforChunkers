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
 * The Graph class draws the graphs and handles all of their datapoints.
 * It also tells the graph related buttons where to be.
 * 
 * @author Luke Van Hulle
 *
 */
public class Graph {
	
	private PApplet p;
	
	private int wide,//the width of the graph
		tall,//height of the graph
		leftBoarder,//size of the left boarder on the graph, the space left of the Y axis
		rightBoarder,
		topBoarder,
		bottomBoarder,
		numXDashes,//number of dashes on the x axis
		numYDashes,//on the Y axis
		traceSpacing,//how far apart the small dots that trace out from the dashes are
		fontSize,
		strokeWeight,//thickness of the graphline
		dashLength,//how long the dashes are
		xAxisLength,//length of the x axis
		yAxisLength,//length of the y axis
		lineColor,//color of the graph line
		locX, //upper left location of graph including boarder
		locY; //upper left location of graph including boarder
	
	private int currentDataPoint = 0;//the location on the dataPoints array where the next point should be added
	
	private float maxX, maxY, minX, minY;//the lowest and highest values on each axis used for mapping the data points
	
	private String xLabel, yLabel, POILabel, title, specialLabel = " ";
	
	private boolean graphIsLarge;//one graph is large, this tells the graph if it is the winner
	
	private DataPoint [] dataPoints;//the array that stores the datapoints
	private DataPoint pointOfInterest;//some graphs have a point, typically a maximum, of interest
	private final static int MAXDATAPOINTS = Calculations.MAXDATAPOINTS + 2;//due to a few math errors the length of the data points array has to be 2 bigger
	static int red, yellow, orange, green, blue;//the colors for the graph lines, also used for the buttons
/**
 * Construct the graph	
 * @param p the PApplet
 */
	public Graph(PApplet p){
		this.p = p;
		setSizes();//this method sets the sizes for various features dependent on the graph size/graphIsLarge
		dataPoints = new DataPoint[MAXDATAPOINTS];
		red = p.color(255, 0, 0);
		yellow = p.color(255, 207, 0);
		orange = p.color(255, 153, 0);
		green = p.color(0, 255, 0);
		blue = p.color(0, 0, 255);
	}
/**
 * Determines if the graph is large or small based on the graphIsLarge variable
 * and sets the sizes accordingly	
 */
	private void setSizes(){
		if(graphIsLarge){
			wide = 550;
			tall = 475;
			leftBoarder = 70;
			rightBoarder = 20;
			topBoarder = 45;
			bottomBoarder = 70;
			dashLength = 10;
			numXDashes = 10;
			numYDashes = 10;
			traceSpacing = 7;
			fontSize = 15;
			strokeWeight = 3;			
		}
		else{
			wide = 200;
			tall = 200;
			leftBoarder = 50;
			rightBoarder = 20;
			topBoarder = 20;
			bottomBoarder = 50;
			dashLength = 5;
			numXDashes = 4;
			numYDashes = 5;
			fontSize = 12;
			strokeWeight = 2;
			traceSpacing = 7;
		}
		//the axis length is the width or height of the graph minus the two corresponding boarders
		xAxisLength = wide - leftBoarder - rightBoarder;
		yAxisLength = tall - topBoarder - bottomBoarder;
		p.textSize(fontSize);
	}
/**
 * translates the graph to the correct locations, calls the appropriate methods to
 * draw the features	
 */
	public void draw(){
		p.translate(locX, locY);//translate everything the the appropriate X and Y
		//if there is data in the array
		if(dataPoints[0] != null){
			setMinMax();//set the min and max values for each axis
			drawPointOfInterest();
			drawData();
		}
		drawAxis();
		drawLabels();
		p.translate(-locX, -locY);//translate back so the next items drawn in the ProjectileMotionforChunkers class are in the right place
	}
	
/**
 * drawData() maps all the data and places it onto the graph by drawing lines
 * from point to point.
 */
	private void drawData(){		
		p.strokeWeight(strokeWeight);//set the thickness of the line
		p.stroke(lineColor);//color of the line
		//the first point is stored here. the data is mapped to the graph coordinates
		int prevX = (int) p.map(dataPoints[0].getX(), minX, maxX, leftBoarder, leftBoarder + xAxisLength);
		int prevY = (int) p.map(dataPoints[0].getY(), minY, maxY, tall - bottomBoarder, topBoarder);
		
		//the for loop starts at 1 which is the end point for the first line
		for(int i = 1; i < dataPoints.length; i++){
			if(dataPoints[i] == null) break;//if null there is no more data so break
			//the end point of the line is mapped here
			int x = (int) p.map(dataPoints[i].getX(), minX, maxX, leftBoarder, leftBoarder + xAxisLength);
			int y = (int) p.map(dataPoints[i].getY(), minY, maxY, tall - bottomBoarder, topBoarder);
			//draws the line from point to point
			p.line(prevX, prevY, x, y);
			
			//set the last end point as the start point for the next line
			prevX = x;
			prevY = y;
		}
		p.strokeWeight(1);//return the stokeWeight to its default value
	}

	
/**
 * the drawLabels() method puts the axis labeling, title, and specialLabel on the graph
 */
	private void drawLabels(){
		p.fill(0);//set the fill color to black
		
		//the graph title
		p.textAlign(p.BOTTOM);
		p.textSize((int) (fontSize*1.2));
		p.text(title, leftBoarder+fontSize, topBoarder - fontSize/2);
		
		//the X axis label
		p.textAlign(p.CENTER);
		p.textSize(fontSize);
		p.text(xLabel, xAxisLength/2 + leftBoarder, (int) (tall - fontSize));
		
		//Y axis Label
		int y = topBoarder+yAxisLength/2-yLabel.length()/2*fontSize+fontSize*2;
		
		p.textAlign(p.CENTER);
		//this for loop allows the letters to be written vertically
		for(int i = 0; i < yLabel.length(); i++){
			p.text(yLabel.charAt(i), fontSize/2, y);
			y+= fontSize;
		}
		//the special label for variables that aren't on the axis
		p.textAlign(p.LEFT, p.BOTTOM);
		p.text(specialLabel, wide-rightBoarder+fontSize, tall-bottomBoarder);
	}
	
/**
 * some graphs will have a point of interest that should be highlighted
 * this method places that highlight onto the graph
 */
	private void drawPointOfInterest(){
		//if there is a point of interest
		if(pointOfInterest != null){
			p.textSize(fontSize);
			//map the end point of the point of interest
			int x = (int) p.map(pointOfInterest.getX(), minX, maxX, leftBoarder, leftBoarder+xAxisLength);
			int y = (int) p.map(pointOfInterest.getY(), minY, maxY, topBoarder+yAxisLength, topBoarder);
			
			p.stroke(0);//set the line color to back
			//the line is always vertical
			p.line(x, tall - bottomBoarder, x, y);
			
			//align the text and the rotate it 90 degrees CCW
			p.textAlign(p.CENTER, p.BOTTOM);
			p.translate(0, 0);
			p.rotate(-p.HALF_PI);
			
			p.fill(0);//set the text color to black
			//write the text onto the graph
			p.text((int) pointOfInterest.getX() + POILabel, -tall*3/4 + POILabel.length()/2* fontSize, x);
			//undo the rotate and translate
			p.rotate(p.HALF_PI);
			p.translate(0, 0);
		}
	}
	
/**
 * This method draws the axis, dashes (tic marks) and the traces
 * onto the graph. Everything but the labels and data points	
 */
	private void drawAxis(){
		p.stroke(0);
		//the two axis
		p.line(leftBoarder, topBoarder, leftBoarder, tall - bottomBoarder);//Y axis
		p.line(leftBoarder, tall - bottomBoarder, wide - rightBoarder, tall - bottomBoarder);//X axis
		//for the X axis
		for(int i = 0; i <= numXDashes; i++){
			
			//the location in X for items to go
			int x = leftBoarder + i*xAxisLength/numXDashes;
			//the X axis dashes
			p.line(x, tall - bottomBoarder, x, tall - bottomBoarder + dashLength);
			
			//the X axis text
			p.fill(0);
			p.textAlign(p.CENTER);
			p.textSize(fontSize);
			//this step write the values next to the dashes on the X axis
			p.text((int) (minX + i*(maxX-minX)/numXDashes), x, topBoarder+yAxisLength+dashLength+fontSize);
			
			//draw the vertical trace lines
			int tracer = tall - bottomBoarder;
			while(tracer > topBoarder){
				p.point(x, tracer);
				tracer -= traceSpacing;
			}
		}
		
		//for the Y axis
		for(int i = 0; i <= numYDashes; i++){
			//the location in y for item to go
			int y = topBoarder + i*yAxisLength/numYDashes;
			//the Y axis dashes
			p.line(leftBoarder, y, leftBoarder - dashLength, y);
			//the Y axis text
			p.textAlign(p.RIGHT, p.CENTER);
			//writes the numbers on the Y axis
			p.text((int) (maxY - i*(maxY-minY)/numYDashes), leftBoarder - dashLength, y);
			
			//draws the horizontal trace points
			int tracer = wide - rightBoarder;
			while(tracer > leftBoarder){
				p.point(tracer, y);
				tracer -= traceSpacing;
			}
		}
	}
/**
 * sets the graph size to the size parameter sent in and the resizes the graph
 * @param large is the graph large
 */
	public void setGraphIsLarge(boolean large){
		graphIsLarge = large;
		setSizes();
	}
/**
 * takes in the X label, Y Label, and title for the graph	
 * @param xL the X label
 * @param yL the Y label
 * @param t the title
 */
	public void setLabels(String xL, String yL, String t){
		xLabel = xL;
		yLabel = yL;
		title = t;
	}
/**
 * takes in the point of interest and its label
 * 
 * @param dP the point of interest DataPoint
 * @param label the point of interest label
 */
	public void setPointOfInterest(DataPoint dP, String label){
		POILabel = label;
		pointOfInterest = dP;
	}
/**
 * Sets the min and max values for both axis which are then used for mapping the data.
 * The dataPoints array is tested to make sure it contains at least on dataPoint before this
 * method is called so it is not tested here	
 */
	private void setMinMax(){
		minX = dataPoints[0].getX();
		maxX = minX;
		minY = dataPoints[0].getY();
		maxY = minY;
		//the enhanced for loop to run through the array
		for(DataPoint dp: dataPoints){
			if(dp == null) break;
			float x = dp.getX();
			float y = dp.getY();
			if(x < minX) minX = x;
			if(x > maxX) maxX = x;
			if(y < minY) minY = y;
			if(y > maxY) maxY = y;
		}
	}
/**
 * Sets the location of the graph. Used in the translate function to draw the
 * graph in the correct location
 * 	
 * @param x the location in X
 * @param y the location in Y
 */
	public void setLocXY(int [] loc){
		locX = loc[0];
		locY = loc[1];
	}
/**
 * Is called by the Calculations sub classes to set the line color on the graph
 * 	
 * @param c the color of the graph from the color() method in PApplet
 */
	public void setLineColor(int c){
		lineColor = c;
	}
/**
 * Tests if the mouse is on/over the graph
 * 	
 * @return true if on graph else false
 */
	public boolean isOnGraph(){
		if(p.mouseX > locX+leftBoarder && p.mouseX < locX + wide - rightBoarder){
			if(p.mouseY > locY + topBoarder && p.mouseY < locY + tall - bottomBoarder){
				return true;
			}
		}
		return false;
	}
/**
 * When the graph is clicked and dragged this method is called to tell it how far to
 * adjust the locX and locY
 * 	
 * @param dX the difference in X
 * @param dY the difference in Y
 */
	public void moveGraph(int dX, int dY){
		locX -= dX;
		locY -= dY;
	}
/**
 * Takes in a DataPoint and adds it to the dataPoints array
 * 	
 * @param dP the data point to be added
 */
	public void addDataPoint(DataPoint dP){
		dataPoints[currentDataPoint++] = dP;
	}
/**
 * returns the location in an array form X in offset 0 Y in offset 1
 * of where the first button should be based on the location of the graph
 * 	
 * @return the location array of button one
 */
	public int[] getB1Location(){
		return new int[] {locX + wide, locY + topBoarder + 5* SubButton.TALL/2};
	}
/**
 * returns the location in an array form X in offset 0 Y in offset 1
 * of where the second button should be based on the location of the graph
 * 	
 * @return the location array of button two
 */
	public int[] getB2Location(){
		return new int[] {locX + wide, locY + topBoarder};
	}
/**
 * A few graphs have buttons that are not changing variables on the axis
 * for those graphs special labels are added to show the user their current value
 * 	
 * @param s the special label
 */
	public void setSpecialLabel(String s){
		specialLabel = s;
	}
/**
 * When new data is calculated the graph must be cleared, this method does just
 * that when called and resets the currentDataPoint to 0	
 */
	public void clearGraph(){
		currentDataPoint = 0;
		dataPoints = new DataPoint[MAXDATAPOINTS];
	}
}
