package org.usfirst.frc.team3309.subsystems.controllers.driveequations;

import org.team3309.lib.controllers.Controller;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;
import org.usfirst.frc.team3309.driverstation.Controls;

public class DriveCheezyDriveEquation extends Controller {

	@Override
	public void reset() {
	}

	private double oldWheel, quickStopAccumulator;
	private double throttleDeadband = 0.02;
	private double wheelDeadband = 0.02;

	@Override
	public OutputSignal getOutputSignal(InputState inputState) {
		double throttle = Controls.driverController.getLeftY();
		double wheel = Controls.driverController.getRightX();
		boolean isHighGear = false;
		boolean isQuickTurn = Controls.driverController.getRB();
		double wheelNonLinearity;

		wheel = handleDeadband(wheel, wheelDeadband);
		throttle = handleDeadband(throttle, throttleDeadband);

		double negInertia = wheel - oldWheel;
		oldWheel = wheel;

		if (isHighGear) {
			wheelNonLinearity = 0.6;
			// Apply a sin function that's scaled to make it feel better.
			wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
			wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
		} else if (!isQuickTurn) {
			wheelNonLinearity = 0.5;
			// Apply a sin function that's scaled to make it feel better.
			wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
			wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
			wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
		}

		double leftPwm, rightPwm, overPower;
		double sensitivity;

		double angularPower;
		double linearPower;

		// Negative inertia!
		double negInertiaAccumulator = 0.0;
		double negInertiaScalar;
		if (isHighGear) {
			negInertiaScalar = 4.0;
			sensitivity = .75;
		} else {
			if (wheel * negInertia > 0) {
				negInertiaScalar = 0.0;
			} else {
				if (Math.abs(wheel) > 0.65) {
					negInertiaScalar = 0.0;
				} else {
					negInertiaScalar = 0.0;
				}
			}
			sensitivity = .85; // Constants.sensitivityLow.getDouble();
		}
		double negInertiaPower = negInertia * negInertiaScalar;
		negInertiaAccumulator += negInertiaPower;

		wheel = wheel + negInertiaAccumulator;
		if (negInertiaAccumulator > 1) {
			negInertiaAccumulator -= 1;
		} else if (negInertiaAccumulator < -1) {
			negInertiaAccumulator += 1;
		} else {
			negInertiaAccumulator = 0;
		}
		linearPower = throttle;

		// Quickturn!
		if (isQuickTurn) {
			if (Math.abs(linearPower) < 0.2) {
				double alpha = 0.1;
				quickStopAccumulator = (1 - alpha) * quickStopAccumulator + alpha * limit(wheel, 1.0) * 5;
			}
			overPower = 1.0;
			if (isHighGear) {
				sensitivity = .525;
			} else {
				sensitivity = .525;
			}
			angularPower = wheel * sensitivity;
			rightPwm = leftPwm = linearPower;
			leftPwm += angularPower;
			rightPwm -= angularPower;
		} else {
			overPower = 0;
			angularPower = wheel;
			leftPwm = rightPwm = 0;
			leftPwm += angularPower;
			rightPwm -= angularPower;
			if (quickStopAccumulator > 1) {
				quickStopAccumulator -= 1;
			} else if (quickStopAccumulator < -1) {
				quickStopAccumulator += 1;
			} else {
				quickStopAccumulator = 0.0;
			}
		}

		if (leftPwm > 1.0) {
			rightPwm -= overPower * (leftPwm - 1.0);
			leftPwm = 1.0;
		} else if (rightPwm > 1.0) {
			leftPwm -= overPower * (rightPwm - 1.0);
			rightPwm = 1.0;
		} else if (leftPwm < -1.0) {
			rightPwm += overPower * (-1.0 - leftPwm);
			leftPwm = -1.0;
		} else if (rightPwm < -1.0) {
			leftPwm += overPower * (-1.0 - rightPwm);
			rightPwm = -1.0;
		}
		OutputSignal signal = new OutputSignal();
		signal.setLeftMotor(leftPwm);
		signal.setRightMotor(rightPwm);
		return signal;
	}

	@Override
	public boolean isCompleted() {
		return false;
	}

	public double handleDeadband(double val, double deadband) {
		return (Math.abs(val) > Math.abs(deadband)) ? val : 0.0;
	}

	public static double limit(double v, double limit) {
		return (Math.abs(v) < limit) ? v : limit * (v < 0 ? -1 : 1);
	}
}