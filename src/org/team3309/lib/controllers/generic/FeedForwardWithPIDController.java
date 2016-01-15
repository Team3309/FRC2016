package org.team3309.lib.controllers.generic;

import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FeedForwardWithPIDController extends PIDController {
	private double kA = 0.0, kV = 0.0;
	private double aimAcc = 0.0, aimVel = 0.0;

	public FeedForwardWithPIDController(double kV, double kA, double kP, double kI, double kD) {
		super(kP, kI, kD);
		this.kA = kA;
		this.kV = kV;
	}

	public FeedForwardWithPIDController(double kV, double kA, double kP, double kI, double kD, double kILimit) {
		super(kP, kI, kD, kILimit);
		this.kA = kA;
		this.kV = kV;
	}

	@Override
	public void reset() {
		super.reset();
	}

	@Override
	public OutputSignal getOutputSignal(InputState inputState) {
		double power = super.getOutputSignal(inputState).getMotor() + this.kA * aimAcc + this.kV * aimVel;
		OutputSignal signal = new OutputSignal();
		signal.setMotor(power);
		return signal;
	}

	@Override
	public boolean isCompleted() {
		if (this.completable) {
			return super.isCompleted();
		} else {
			return false;
		}
	}

	public double getkA() {
		return kA;
	}

	public void setkA(double kA) {
		this.kA = kA;
	}

	public double getkV() {
		return kV;
	}

	public void setkV(double kV) {
		this.kV = kV;
	}

	public double getAimAcc() {
		return aimAcc;
	}

	public void setAimAcc(double aimAcc) {
		this.aimAcc = aimAcc;
	}

	public double getAimVel() {
		return aimVel;
	}

	public void setAimVel(double aimVel) {
		this.aimVel = aimVel;
	}
	
	public void sendToSmartDash() {
		super.sendToSmartDash();
		SmartDashboard.putNumber(this.getName() + " kA", kA);
		SmartDashboard.putNumber(this.getName() + " kV", kV);
		kA = SmartDashboard.getNumber(this.getName() + " kA", kA);
		kV = SmartDashboard.getNumber(this.getName() + " kV", kV);
	}
}