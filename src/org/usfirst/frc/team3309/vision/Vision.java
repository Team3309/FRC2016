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
	private static Shot[] shots = { new Shot(120, 27, .6775), new Shot(120, 29.03309, 0.52708),
			new Shot(120, 30.03309, 0.4125), new Shot(120, 33.7, .1208), new Shot(160, 36, .1),
			new Shot(160, 36.8, .025), new Shot(160, 38, -.075), new Shot(160, 39.3, -.165),
			new Shot(160, 40.25, -.227), new Shot(160, 42.2, -.36666), new Shot(160, 42.7, -.43333),
			new Shot(160, 42.7, -.4275), new Shot(160, 42.85, -.4833), new Shot(160, 43.4, -.6375),
			new Shot(160, 44.9, -.825), };
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
						System.out.println("previousShot: " + previousShot + " shot: " + shot);
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
						System.out.println("upperShot: " + upperShot + " shot: " + shot);
					}
				}
			}
		}
		shotToBeReturned.setYCoordinate(currentY);
		 System.out.println("Here is my shot " + shotToBeReturned.getYCoordinate() + " Hood Angle " + shotToBeReturned.getGoalHoodAngle());
		return shotToBeReturned;
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
