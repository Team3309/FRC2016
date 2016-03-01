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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.json.JSONArray;
import org.json.JSONObject;
import org.usfirst.frc.team3309.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalOutput;

public class Vision implements Runnable {
	public enum GoalSide {
		LEFT, RIGHT, CENTER
	}
	
	//private static Shot[] = {new Shot(goalHoodAngle, goalHoodAngle, goalHoodAngle)};

	private static final long TIMEOUT = 500;

	private static Vision instance;
	private double goalHoodAngle = 0;
	private double goalRPS = 0;
	private GoalSide preferredGoal = GoalSide.CENTER;
	private Goal currentGoal;

	public static Vision getInstance() {
		if (instance == null) {
			instance = new Vision();
		}
		return instance;
	}

	private final Thread thread;
	private final Lock lock;
	private List<Goal> latestGoals;
	private long lastUpdate = 0;
	private long lastTimeoutTime = 0;
	private DigitalOutput out = new DigitalOutput(RobotMap.LIGHT);

	private Vision() {
		this.thread = new Thread(this);
		this.lock = new ReentrantLock();
	}

	public void start() {
		thread.start();
		out.enablePWM(0);
		out.setPWMRate(19000);

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
		double toBeGoalRPS = 0, toBeGoalHoodAngle = 2;
		Shot x = new Shot(toBeGoalRPS, toBeGoalHoodAngle, currentGoal.azimuth);
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
		try {
			DatagramSocket socket = new DatagramSocket(3309);
			System.out.println("Vision server started.");
			while (true) {
				byte[] buf = new byte[2048];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);

				String messageString = new String(packet.getData(), 0, packet.getLength());
				JSONArray goalsJson = new JSONArray(messageString);
				List<Goal> goals = new LinkedList<Goal>();
				for (int i = 0; i < goalsJson.length(); i++) {
					JSONObject goalJson = goalsJson.getJSONObject(i);
					JSONObject pos = goalJson.getJSONObject("pos");
					JSONObject size = goalJson.getJSONObject("size");
					goals.add(new Goal(pos.getDouble("x"), pos.getDouble("y"), size.getDouble("width"),
							size.getDouble("height"), goalJson.getDouble("distance"),
							goalJson.getDouble("elevation_angle"), goalJson.getDouble("azimuth")));
				}
				this.lock.lock();
				this.lastUpdate = System.currentTimeMillis();
				this.latestGoals = goals;
				this.lock.unlock();
				List<Goal> currentGoals = getGoals();
				double currentBiggest = 0;
				if (currentGoals.size() != 0) {
					for (Goal x : currentGoals) {
						if (Math.abs(x.width) > currentBiggest) {
							currentBiggest = x.width;
							currentGoal = x;
						}
					}
				} else {
					currentGoal = null;
				}
				/*
				 * if (this.preferredGoal == GoalSide.CENTER) { double
				 * closestToZero = 1.1; for (Goal x : currentGoals) { if
				 * (Math.abs(x.x) < closestToZero) { closestToZero = x.x;
				 * this.currentGoal = x; }
				 * 
				 * } } else if (this.preferredGoal == GoalSide.LEFT) {
				 * 
				 * } else if (this.preferredGoal == GoalSide.RIGHT) {
				 * 
				 * } else {
				 * 
				 * }
				 */

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Goal> getGoals() {
		this.lock.lock();
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastUpdate > TIMEOUT && lastUpdate != lastTimeoutTime) {
			this.latestGoals = null;
			this.lastTimeoutTime = lastUpdate;
			System.out.println("Vision timed out");
		}
		List<Goal> goals = this.latestGoals;
		this.lock.unlock();
		if (goals == null) {
			return new LinkedList<Goal>();
		}
		return goals;
	}

	public GoalSide getPreferredGoal() {
		return preferredGoal;
	}

	public void setPreferredGoal(GoalSide preferredGoal) {
		preferredGoal = preferredGoal;
	}

}
