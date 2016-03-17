package org.usfirst.frc.team3309.auto;

import org.team3309.lib.controllers.Controller;
import org.team3309.lib.controllers.drive.DriveAngleVelocityController;
import org.team3309.lib.controllers.drive.DriveEncodersVelocityController;
import org.usfirst.frc.team3309.robot.Sensors;
import org.usfirst.frc.team3309.subsystems.Drive;

import edu.wpi.first.wpilibj.Timer;

public class RoutineBased {

	/**
	 * Tracks how long auto has been running
	 */
	protected Timer autoTimer = new Timer();
	// All the subsystems
	protected Drive mDrive = Drive.getInstance();

	public void waitForController(Controller c, double timeout) throws TimedOutException, InterruptedException {
		Timer waitTimer = new Timer();
		waitTimer.start();
		while (!c.isCompleted()) {
			if (waitTimer.get() > timeout) {
				throw new TimedOutException();
			}
			Thread.sleep(100);
		}
	}

	/**
	 * Waits for drive train to complete its current controller's task
	 * 
	 * @param timeout
	 *            if it hits this (ms) , then it will timeout
	 * @throws TimedOutException
	 *             if waits for more than specified timeout
	 * @throws InterruptedException
	 */
	public void waitForDrive(double timeout) throws TimedOutException, InterruptedException {
		Timer waitTimer = new Timer();
		waitTimer.start();
		while (!mDrive.isOnTarget()) {
			if (waitTimer.get() > timeout) {
				throw new TimedOutException();
			}
			Thread.sleep(100);
		}
	}

	/**
	 * Waits for drive train to meet its encoder goal
	 * 
	 * @param timeout
	 *            if it hits this (ms) , then it will timeout
	 * @throws TimedOutException
	 *             if waits for more than specified timeout
	 * @throws InterruptedException
	 */
	public void waitForDriveEncoder(double encoderGoal, double timeout) throws TimedOutException {
		Timer waitTimer = new Timer();
		waitTimer.start();
		while (!mDrive.isEncoderCloseTo(encoderGoal)) {
			if (waitTimer.get() > timeout)
				throw new TimedOutException();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Waits for drive train to meet its angle oder goal
	 * 
	 * @param timeout
	 *            if it hits this (ms) , then it will timeout
	 * @throws TimedOutException
	 *             if waits for more than specified timeout
	 * @throws InterruptedException
	 */
	public void waitForDriveAngle(double angleGoal, double timeout) throws TimedOutException {
		Timer waitTimer = new Timer();
		waitTimer.start();
		while (!mDrive.isAngleCloseTo(angleGoal)) {
			if (waitTimer.get() > timeout)
				throw new TimedOutException();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void driveEncoder(double goal, double maxEnc, double timeout) {
		Sensors.resetDrive();
		DriveEncodersVelocityController x = new DriveEncodersVelocityController(goal);
		x.setMAX_ENCODER_VEL(maxEnc);
		Drive.getInstance().setAutoController(x);
		try {
			this.waitForController(x, timeout);
		} catch (Exception e) {
		}
		mDrive.stopDrive();
	}

	public void turnToAngle(double goal, double timeout) {
		DriveAngleVelocityController x = new DriveAngleVelocityController(goal);
		Drive.getInstance().setAutoController(x);
		try {
			this.waitForController(x, 4);
		} catch (Exception e) {

		}
		mDrive.stopDrive();
	}

	public void waitForEndOfAuto() {
		while (autoTimer.get() < 14.900) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {

			}
		}
	}
}
