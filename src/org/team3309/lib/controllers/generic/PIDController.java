package org.team3309.lib.controllers.generic;

import org.team3309.lib.KragerTimer;
import org.team3309.lib.controllers.Controller;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Basic PID Controller. The isCompleted Method will always return false.
 * 
 * @author TheMkrage
 *
 */
public abstract class PIDController extends Controller {
	/**
	 * Gains
	 */
	private double kP, kD, kI;
	/**
	 * Limit of the Integral. mIntegral is capped off at the kILimit.
	 */
	private double kILimit;
	/**
	 * Stores previous error
	 */
	protected double previousError = 0;
	/**
	 * Running Integral term to use between loops.
	 */
	private double mIntegral = 0;
	/**
	 * Tells if Controller ends when it maintains a low error for a certain
	 * amount of time.
	 */
	protected boolean completable = true;
	/**
	 * Margin of how close the error can be close to 0.
	 */
	protected double THRESHOLD = 30;
	/**
	 * Time the error must stay between the certain margin within the threshold.
	 */
	protected double TIME_TO_BE_COMPLETE_MILLISECONDS = 250;
	/**
	 * Timer to count how much time the error has been low.
	 */
	protected KragerTimer doneTimer = new KragerTimer(TIME_TO_BE_COMPLETE_MILLISECONDS);

	public PIDController(double kP, double kI, double kD) {
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		SmartDashboard.putNumber(this.getName() + " kP", kP);
		SmartDashboard.putNumber(this.getName() + " kI", kI);
		SmartDashboard.putNumber(this.getName() + " kD", kD);
	}

	public PIDController(double kP, double kI, double kD, double kILimit) {
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		this.kILimit = kILimit;
		SmartDashboard.putNumber(this.getName() + " kP", kP);
		SmartDashboard.putNumber(this.getName() + " kI", kI);
		SmartDashboard.putNumber(this.getName() + " kD", kD);
	}

	// You would want to set the mIntegral and previousError to zero when
	// reusing a PID Loop
	@Override
	public void reset() {
		mIntegral = 0;
		previousError = 0;
	}

	@Override
	public OutputSignal getOutputSignal(InputState inputState) {
		double error = inputState.getError();

		// Add to mIntegral term
		mIntegral += error;

		// Check for integral hitting the limit
		if (mIntegral > kILimit)
			mIntegral = kILimit;

		if (mIntegral < -kILimit)
			mIntegral = -kILimit;

		// Make OutputSignal and fill it with calculated values
		OutputSignal signal = new OutputSignal();
		double output = (kP * error) + (kI * mIntegral) + (kD * (error - previousError));
		signal.setMotor(output);
		previousError = error;
		return signal;
	}

	/**
	 * @return if timer can end
	 */
	public boolean isCompletable() {
		return completable;
	}

	/**
	 * @param completable
	 *            whether the loop can end execution
	 */
	public void setCompletable(boolean completable) {
		this.completable = completable;
	}

	/**
	 * @param tHRESHOLD
	 *            gap that
	 */
	public void setTHRESHOLD(double tHRESHOLD) {
		THRESHOLD = tHRESHOLD;
	}

	/**
	 * @param tIME_TO_BE_COMPLETE_MILLISECONDS
	 *            time
	 */
	public void setTIME_TO_BE_COMPLETE_MILLISECONDS(double tIME_TO_BE_COMPLETE_MILLISECONDS) {
		TIME_TO_BE_COMPLETE_MILLISECONDS = tIME_TO_BE_COMPLETE_MILLISECONDS;
	}

	public void setConstants(double kP, double kI, double kD) {
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
	}

	@Override
	public boolean isCompleted() {
		// If the Controller is completable, then the error will need to be
		// between a certain threshold before isCompleted return true
		if (completable) {
			this.doneTimer.isConditionMaintained(Math.abs(previousError) < THRESHOLD);
		}
		return false;
	}

	@Override
	public void sendToSmartDash() {
		kP = SmartDashboard.getNumber(this.getName() + " kP", kP);
		kI = SmartDashboard.getNumber(this.getName() + " kI", kI);
		kD = SmartDashboard.getNumber(this.getName() + " kD", kD);
		SmartDashboard.putNumber(this.getName() + " kP", kP);
		SmartDashboard.putNumber(this.getName() + " kI", kI);
		SmartDashboard.putNumber(this.getName() + " kD", kD);
		SmartDashboard.putNumber(this.getName() + " ERROR", this.previousError);
	}
}
