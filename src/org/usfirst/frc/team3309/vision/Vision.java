/*
 * Copyright 2016 Vinnie Magro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.usfirst.frc.team3309.vision;

import edu.wpi.first.wpilibj.DigitalOutput;
import org.usfirst.frc.team3309.robot.RobotMap;

import java.util.List;

public class Vision implements Runnable {

	private final Thread thread;

	// These are the shots
	private static Shot[] shots = { new Shot(140, 27.5, .57291), new Shot(140, 32.3003309, 0.308),
			new Shot(140, 34.303309, 0.091666), new Shot(160, 34.8, .0708), new Shot(160, 35.2, -.04375),
			new Shot(160, 35.9, -.164), new Shot(160, 38.9, -.2541), new Shot(160, 39.2, -.3565),
			new Shot(160, 40.1, -.46458), new Shot(160, 41.6, -.56041), new Shot(160, 42.4, -.702),
			new Shot(160, 43.0, -.79375), new Shot(170, 43.5, -.94555) };
	// new Shot(goalRPS, goalHood, y)

	// new Shot(160, 20, 20)
	private static final long TIMEOUT = 500;

	private static Vision instance;
	private double goalHoodAngle = 0;
	private double goalRPS = 0;
	private Goal currentGoal;

	// private Thread
	

	public static Vision getInstance() {
		if (instance == null) {
			instance = new Vision();
		}
		return instance;
	}

	private DigitalOutput out = new DigitalOutput(RobotMap.LIGHT);

	private Vision() {
		thread = new Thread(this);
	}

	public void start() {
		VisionClient.getInstance().start();
		out.enablePWM(0);
		out.setPWMRate(19000);
		thread.start();

	}

	/**
	 * Sets light between 0 - 1
	 *
	 * @param power
	 */
	public void setLight(double power) {
		out.updateDutyCycle(power);
	}

	public Shot getShot() {
		if (currentGoal == null) {
			return null;
		}
		Shot shotToBeReturned = new Shot(currentGoal.azimuth);
		boolean isCurrentShotYGreaterThanArrayY = false;
		double closestShot = Integer.MAX_VALUE;
		double currentY = currentGoal.y;
		for (int i = 0; i < shots.length; i++) {
			Shot shot = shots[i];
			if (Math.abs(currentY - shot.getYCoordinate()) < closestShot) {
				closestShot = Math.abs(currentY - shot.getYCoordinate());
				shotToBeReturned.setGoalHoodAngle(shot.getGoalHoodAngle());
				shotToBeReturned.setGoalRPS(shot.getGoalRPS());
				shotToBeReturned.setYCoordinate(shot.getYCoordinate());
				if (currentY > shot.getYCoordinate()) {
					if (i != 0) {
						isCurrentShotYGreaterThanArrayY = false;
						Shot previousShot = shots[i - 1];
						double slope = (previousShot.getGoalHoodAngle() - shot.getGoalHoodAngle())
								/ (previousShot.getYCoordinate() - shot.getYCoordinate());
						double b = previousShot.getGoalHoodAngle() - (slope * previousShot.getYCoordinate());
						double newOutput = slope * currentY + b;
						shotToBeReturned.setGoalHoodAngle(newOutput);
						// System.out.println("previousShot: " + previousShot +
						// " shot: " + shot);
					}
				} else {
					if (i != shots.length - 1) {
						Shot upperShot = shots[i + 1];
						isCurrentShotYGreaterThanArrayY = true;
						double slope = (upperShot.getGoalHoodAngle() - shot.getGoalHoodAngle())
								/ (upperShot.getYCoordinate() - shot.getYCoordinate());
						double b = upperShot.getGoalHoodAngle() - (slope * upperShot.getYCoordinate());
						double newOutput = slope * currentY + b;
						shotToBeReturned.setGoalHoodAngle(newOutput);
						// System.out.println("upperShot: " + upperShot + "
						// shot: " + shot);
					}
				}
			}
		}
		shotToBeReturned.setYCoordinate(currentY);
		System.out.println("Here is my shot " + shotToBeReturned.getYCoordinate() + " Hood Angle "
				+ shotToBeReturned.getGoalHoodAngle());
		return shotToBeReturned;
	}
	
	public boolean hasShot() {
		return currentGoal != null;
	}

	public double getGoalHoodAngle() {
		return goalHoodAngle;
	}

	public double getGoalRPS() {
		return goalRPS;
	}

	@Override
	public void run() {
		while (true) {
			// wait for new goals to be available and then process them
			List<Goal> currentGoals = VisionClient.getInstance().getGoals();
			double currentBiggest = 0;
			if (currentGoals.size() != 0) {
				for (Goal x : currentGoals) {
					// System.out.println("Current: " + currentGoal);
					if (Math.abs(x.width) > currentBiggest) {
						currentBiggest = x.width;
						currentGoal = x;
						
						// System.out.println("Current: " + currentGoal);
					}
				}
			} else {
				currentGoal = null;
			}
		}
	}

	public List<Goal> getGoals() {
		return VisionClient.getInstance().getGoals();
	}

}
