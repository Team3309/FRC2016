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
	public enum GoalSide {
		LEFT, RIGHT, CENTER
	}

	private final Thread thread;

	private static Shot[] shots = { new Shot(160, 29, .895833), new Shot(160, 30.85, .6625),
			new Shot(160, 34.5, .35208333), new Shot(160, 36, .066666), new Shot(160, 40, -.33958),
			new Shot(160, 42, -.475), new Shot(160, 42.4, -.575) };
	// new Shot(goalRPS, goalHood, distanceAway)
	private static final long TIMEOUT = 500;

	private static Vision instance;
	private double goalHoodAngle = 0;
	private double goalRPS = 0;
	private GoalSide preferredGoal = GoalSide.CENTER;
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
		Shot x = new Shot(currentGoal.azimuth);
		boolean isCurrentShotYGreaterThanArrayY = false;
		double closestShot = Integer.MAX_VALUE;
		for (int i = 0; i < shots.length; i++) {
			Shot shot = shots[i];
			if (Math.abs(currentGoal.y - shot.getYCoordinate()) < closestShot) {
				closestShot = Math.abs(currentGoal.y - shot.getYCoordinate());
				x.setGoalHoodAngle(shot.getGoalHoodAngle());
				x.setGoalRPS(shot.getGoalRPS());
				x.setYCoordinate(shot.getYCoordinate());
				if (currentGoal.y < shot.getYCoordinate()) {
					if (i != 0) {
						isCurrentShotYGreaterThanArrayY = false;
						Shot previousShot = shots[i - 1];
						double slope = (previousShot.getGoalHoodAngle() - x.getGoalHoodAngle())
								/ (previousShot.getYCoordinate() - x.getYCoordinate());
						double b = previousShot.getGoalHoodAngle() - (slope * previousShot.getYCoordinate());
						double newOutput = slope * currentGoal.y + b;
						x.setGoalHoodAngle(newOutput);
					}
				} else {
					if (i != shots.length - 1) {
						Shot upperShot = shots[i + 1];
						isCurrentShotYGreaterThanArrayY = true;
						double slope = (upperShot.getGoalHoodAngle() - x.getGoalHoodAngle())
								/ (upperShot.getYCoordinate() - x.getYCoordinate());
						double b = upperShot.getGoalHoodAngle() - (slope * upperShot.getYCoordinate());
						double newOutput = slope * currentGoal.y + b;
						x.setGoalHoodAngle(newOutput);
					}
				}
			}
		}
		x.setYCoordinate(currentGoal.y);
		System.out.println("Here is my shot " + x.getYCoordinate() + " Hood Angle " + x.getGoalHoodAngle());
		return x;
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

	public GoalSide getPreferredGoal() {
		return preferredGoal;
	}

	public void setPreferredGoal(GoalSide preferredGoal) {
		preferredGoal = preferredGoal;
	}

}
