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

    // private static Shot[] = {new Shot(goalHoodAngle, goalHoodAngle,
    // goalHoodAngle)};

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

    private DigitalOutput out = new DigitalOutput(RobotMap.LIGHT);

    private Vision() {
    }

    public void start() {
        VisionClient.getInstance().start();
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
        while (true) {
            // wait for new goals to be available and then process them
            List<Goal> currentGoals = VisionClient.getInstance().waitForGoals();
            double currentBiggest = 0;
            if (currentGoals.size() != 0) {
                for (Goal x : currentGoals) {
                    if (Math.abs(x.width) > currentBiggest) {
                        currentBiggest = x.width;
                        currentGoal = x;
                        System.out.println("Current: " + currentGoal);
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
