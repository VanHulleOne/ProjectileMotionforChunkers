
Copyright (c) 2012 Luke Van Hulle
Contact BeardedOne85@gmail.com

    Permission is granted to copy, distribute and/or modify this document
    under the terms of the GNU Free Documentation License, Version 1.3
    or any later version published by the Free Software Foundation;
    with no Invariant Sections, no Front-Cover Texts, and no Back-Cover Texts.
    A copy of the license is included in the section entitled "GNU
    Free Documentation License".
    
Purpose:
	This project is designed to accurately simulate shots of pumpkins at velocities from near zero to approaching
	Mach 1, 340 meters/second, 760 miles/hour by taking into account both the coefficient of drag (wind resistance) and
	the coefficient of lift (Magnus Effect, excellently introduced here <http://youtu.be/23f1jvGUWJs> ). Most people are familiar
	with wind resistance but the Magnus Effect is less widely known. Basically the Magnus Effect causes a force on a rotating object
	that is moving through a fluid. It is what causes golf balls to hook and slice or what causes a ping pong ball to dive with top spin.
	The Magnus Effect is also what causes Hop-Up in airsoft guns, which may be of particular interest to air cannon Chunkers.
	(Please note that the Magnus Effect is NOT related to Bernoulli's Principle where faster air has lower pressure, slower air has higher pressure.)
	What is fortunate for a Chunker is that a sling on a trebuchet, maganel, etc, puts back spin on the projectile which causes lift.
	Although this extra lift does allow for greater distance it has the largest effect on the Optimum Launch Angle, lowering it considerably
	from the theoretical optimum of 45 degrees. LDVance, a high school physics teacher, trebuchet enthusiast, and talented modeler told me that
	in his observations the coefficent of lift tends to be around 0.09(it is a scalar so no units necessary) so that is the default value I have used.
	
	I also hope this program is found to be useful to Technology Education and Physics classes who can use the data from this project to help determine
	the energy efficiencies of their machines. Typically an assignment would have students calculate the potential energy in their machine and, from their
	launch distance, the kinetic energy put into the projectile, without wind resistance. Although this program does not track how much energy loss
	is caused by wind resistance, students could use the maximum theoretical launch velocity (PE = KE, Mass*Gravity*Height = 1/2*Mass * Velocity^2), enter
	it into the simulator and use that distance as their potential 100% energy conversion launch. It would also be fairly
	easy to add an additional few lines into the shot() method in Calculations that would tabulate the energy lost to 
	wind resistance and print it to the serial monitor.
	
	Finally, this program allows you to change the density of the air. Allowing this change came to my attention when reading about
	The Big 10 Inch machine and its World Record launch in 2010 of 5,524 ft, 1690 meters. They claimed favorable conditions,
	warm weather and at 1 mile altitude, allowed them to finally break the 1 mile barrier. I determined that the density of the
	air at 5,000ft above sea level in 80 degree weather was around 0.810 kg/m^3, about 2/3rds what it would be in Delaware.
	From the data this simulation provides, the 4,500ft shots in Delaware and the 5,500 ft shots in Moab, UT are all probably
	going just a little under Mach 1, around 320/330 meter/sec. So it seems that it isn't the 1 mile barrier that is holding
	up these air cannons, but instead that pesky old speed barrier, the speed of sound.

Understanding the Graphs:
	Optimum Distance vs Velocity
			Variables:
				The upper and lower velocities for the X axis.
			What it shows:
				This graph shows the maximum distance a shot can go for a range of initial velocities.
				With the given initial velocity, the X axis, the shot's optimum launch angle is found and the
					distance of that shot is used as the corresponding Y Axis location
			Use:
				This graph would be used when you have a distance goal for your machine and you want to know
					how fast your projectile will have to go to get it there.
				This graph can also be used if you have a weight and height idea for your trebuchet and want
					to know how far your projectile will go. 
	
	Optimum Release Angle vs Velocity
			Variables:
				The upper and lower velocities for the X axis.
			What it shows:
				This graph shows the optimum angle of release for a range of velocities.
			Use:
				This graph would be used when you have an approximate launch velocity and you want
				to figure out about where you should be releasing. At high speeds the graph is pretty flat
				but based on initial launch height, it can get a little steep for lower velocites.
	
	Distance vs Release Angle
			Variable:
				Initial launch velocity
			What it shows:
				This graph shows how far your projectile will go with a given initial velocity at various
				launch angles. It also shows the optimum launch angle for that velocity.
			Use:
				I think the most useful part of this graph is that it shows there to be a pretty flat range
				around the optimum launch angle. At defafult condition values a 100m/s shot has about a
				17 degree window that will produce a shot within 5% of the max distance meaning release angle
				is not as critical as most people make it out to be. What this means for machines with slings
				is that we should be spending effort increasing the energy efficiency of our machine instead
				of worrying about if our launch angle looked good.
	
	Fight Path
			Variables:
				Launch Angle
				Initial Velocity
			What it shows:
				This graph shows the location in X and Y, altitude and distance, for and individual shot.
			Use:
				This graph has several uses. One is that it can be used to estimate how far a shot will go
				at various launch velocities and angles. It can also be used to reverse engineer a shot.
				Say you can get the launch angle from a video and you know your distance, you can figure out
				what your initial velocity was. It can also be used to help experimentally verify the coefficient
				of lift you are experiencing with your machine. This would most easily be done by comparing
				launch angle and max height of the simulation and an actual launch. If you know you are aiming
				at targets and you can use it to determine what angle you should launch at next to get a different range.  
		
Potential Improvements:
	More accurate Coefficient of Drag calculation.
	Cross Hairs for the graphs to allow more accurate readings at various points
	Ability to type in desired data values instead of using the up/down buttons
	Ability to save graphs to a pdf (this one would be pretty easy for Processing)
	Ability to remember previous data values so the graphs start with them the next time the program is started
	Lower processor demand
	Graph energy requirements/list energy lost due to friction
	Graph deceleration along flight path
	
Assumptions:
	Round projectile
	Buoyancy is negligible, ie items are not near, or for that matter even 10%, the density of the fluid in which they are traveling
	Coriolis Effect is negligible, your projectile is traveling less than 100 miles
	The program does not allow for backwards shots, even with a launch angle less that 90 degrees the Coefficient
		of Lift can cause the shot to go backwards
	The projectile is not perfectly smooth
	The roughness on the projectile is uniform
	The air immediately encountered upon release is stationary. This would not be true for an air cannon although
		the difference probably is probably negligible
		
Useful things to know:
	If you want to test a shot without wind resistance set the air density to Zero
	
	
Development Software:
	This program was written in Eclipse v3.4.2 using the Processing (v1.5.1) add-on Proclipsing (the Beta version I believe).

Works Cited:
	*Areodynamics of a Sphere and an Oblate Spheroid for Mach Numbers from
	 0.6 to 10.5 including some effects of Test Conditions 
	 M. Leroy Spearman and Dorothy O. Braswell
 	 August 1993
 	 http://ntrs.nasa.gov/archive/nasa/casi.ntrs.nasa.gov/19940008699_1994008699.pdf
 	
	*Free-Flight Measuremens of Sphere Drag at Subsonic, Transonic, Supersonic,
	 and Hypersonic Speeds for Continuum, Transitions, and Near-Free-Molecular
 	 Flow Conditions
 	 A. B. Baily and J. Hiatt, ARO, Inc
 	 March 1971
 	 http://www.dtic.mil/cgi-bin/GetTRDoc?AD=AD0721208
 	
 	*Glover, Thomas. "Acceleration Due to Gravity." Pocket Ref. 3rd ed. Littleton, Co: Sequoia Publ., 2005. 334. Print.
	
	 