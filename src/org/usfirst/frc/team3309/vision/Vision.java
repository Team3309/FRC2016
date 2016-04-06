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

import java.util.List;

import org.usfirst.frc.team3309.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision implements Runnable {

	private final Thread thread;
	public double BRIGHTNESS = .3;

	// These are the shots
/*	private static Shot[] shots = { new Shot(140, 27.5, .57291), new Shot(140, 31.3003309, 0.308),
			new Shot(140, 33.303309, 0.091666), new Shot(160, 33.8, .0708), new Shot(160, 34.2, -.04375),
			new Shot(160, 34.9, -.164), new Shot(160, 37.9, -.2541), new Shot(160, 38.2, -.3565),
			new Shot(160, 40.1, -.46458), new Shot(160, 41.6, -.56041), new Shot(180, 42.4, -.702),
			new Shot(180, 46, -.777), new Shot(180, 42.5, -.94555) };
			*/
	private static Shot[] shots = { new Shot(120, 29, .681), new Shot(120, 31.4, 0.450),
			new Shot(120, 33.4, 0.308), new Shot(120, 35.4, .148), new Shot(120, 36.4, -.004),
			new Shot(140, 38.5, -.005), new Shot(140, 40.0, -.140), new Shot(140, 42.4, -.279),
			new Shot(140, 44, -.4), new Shot(140, 45.3, -.490), new Shot(160, 46.3, -.491),
			new Shot(160, 47.4, -.59), new Shot(160, 50.9, -.94555) };
	// new Shot(goalRPS, goalHood, y)

	private static Vision instance;
	private double goalHoodAngle = 0;
	private double goalRPS = 0;
	private Goal currentGoalToAimTowards;
	private Shot currentShotToAimTowards;		

	public static Vision getInstance() {
		if (instance == null) {
			instance = new Vision();
		}
		return instance;
	}

	private DigitalOutput out = new DigitalOutput(RobotMap.LIGHT);

	private Vision() {
		thread = new Thread(this);
		SmartDashboard.putNumber("VISION BRIGHTNESS", BRIGHTNESS);
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

	public Shot getShotToAimTowards() {
		return currentShotToAimTowards;
	}

	public boolean hasShot() {
		return currentGoalToAimTowards != null;
	}

	public double getGoalHoodAngle() {
		return currentShotToAimTowards.getGoalHoodAngle();
	}

	public double getGoalRPS() {
		return currentShotToAimTowards.getGoalRPS();
	}

	@Override
	public void run() {
		while (true) {
			this.BRIGHTNESS = SmartDashboard.getNumber("VISION BRIGHTNESS", .2);
			// wait for new goals to be available and then process them
			List<Goal> currentGoals = VisionClient.getInstance().getGoals();
			double currentBiggest = 0;
			if (currentGoals.size() != 0) {
				for (Goal x : currentGoals) {
					if (Math.abs(x.width) > currentBiggest) {
						currentBiggest = x.width;
						currentGoalToAimTowards = x;
					}
				}
			} else {
				currentGoalToAimTowards = null;
			}
			// Find Shot To Aim At
			if (currentGoalToAimTowards == null) {
				currentShotToAimTowards = null;
			} else {
				// currentShotToAimTowards = new
				// Shot(currentGoalToAimTowards.azimuth);
				double closestShot = Integer.MAX_VALUE;
				double currentY = currentGoalToAimTowards.y;
				Shot shotToBeSet = new Shot(currentGoalToAimTowards.azimuth);
				for (int i = 0; i < shots.length; i++) {
					Shot shot = shots[i];
					if (Math.abs(currentY - shot.getYCoordinate()) < closestShot) {
						closestShot = Math.abs(currentY - shot.getYCoordinate());
						shotToBeSet.setGoalHoodAngle(shot.getGoalHoodAngle());
						shotToBeSet.setGoalRPS(shot.getGoalRPS());
						shotToBeSet.setYCoordinate(shot.getYCoordinate());
						if (currentY > shot.getYCoordinate()) {
							if (i != 0) {
								Shot previousShot = shots[i - 1];
								double slope = (previousShot.getGoalHoodAngle() - shot.getGoalHoodAngle())
										/ (previousShot.getYCoordinate() - shot.getYCoordinate());
								double b = previousShot.getGoalHoodAngle() - (slope * previousShot.getYCoordinate());
								double newOutput = slope * currentY + b;
								shotToBeSet.setGoalHoodAngle(newOutput );
							}
						} else {
							if (i != shots.length - 1) {
								Shot upperShot = shots[i + 1];
								double slope = (upperShot.getGoalHoodAngle() - shot.getGoalHoodAngle())
										/ (upperShot.getYCoordinate() - shot.getYCoordinate());
								double b = upperShot.getGoalHoodAngle() - (slope * upperShot.getYCoordinate());
								double newOutput = slope * currentY + b;
								shotToBeSet.setGoalHoodAngle(newOutput );
							}
						}
					}
				}
				shotToBeSet.setYCoordinate(currentY);
				currentShotToAimTowards = shotToBeSet;
				// System.out.println("Y FOR HOOD: " + currentY);
			}
		}
	}

	public List<Goal> getGoals() {
		return VisionClient.getInstance().getGoals();
	}
}