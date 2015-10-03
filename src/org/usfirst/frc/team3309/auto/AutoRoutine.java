package org.usfirst.frc.team3309.auto;

import org.usfirst.frc.team3309.subsystems.Drive;

public abstract class AutoRoutine {
	/**
	 * Tracks how long auto has been running
	 */
	protected Timer autoTimer = new Timer();
	// All the subsystems
	protected Drive mDrive = Drive.getInstance();

	/**
	 * The entire auto routine.
	 */
	public abstract void routine();

	/**
	 * Waits for drive train to complete its current controller's task
	 * 
	 * @param timeout
	 *            if it hits this (ms) , then it will timeout
	 * @throws TimedOutException
	 *             if waits for more than specified timeout
	 */
	
	public void waitForDrive(double timeout) throws TimedOutException {
		Timer waitTimer = new Timer();
		waitTimer.start();
		while (!mDrive.isOnTarget()) {
			if (waitTimer.get() > timeout)
				throw new TimedOutException();
			Thread.sleep(100);
		}
	}

	public void waitForDriveEncoder(double encoderGoal, double timeout)
			throws TimedOutException {
		Timer waitTimer = new Timer();
		waitTimer.start();
		while (!mDrive.isEncoderCloseTo(encoderGoal)) {
			if (waitTimer.get() > timeout)
				throw new TimedOutException();
			Thread.sleep(100);
		}
		;
	}

	public void waitForDriveAngle(double angleGoal, double timeout) throws TimedOutException{
		Timer waitTimer = new Timer();
		waitTimer.start();
		while(!mDrive.isAngleCloseTo(angleGoal)) {
			if(waitTimer.get() > timeout) throw new TimedOutException();
			Thread.sleep(100);
		};
	}
}
