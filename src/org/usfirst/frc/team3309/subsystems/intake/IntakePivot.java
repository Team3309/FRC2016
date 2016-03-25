package org.usfirst.frc.team3309.subsystems.intake;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.KragerMath;
import org.team3309.lib.controllers.generic.PIDController;
import org.team3309.lib.controllers.generic.PIDPositionController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.robot.Constants;
import org.usfirst.frc.team3309.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IntakePivot extends ControlledSubsystem {

	private CANTalon intakePivot = new CANTalon(RobotMap.INTAKE_PIVOT_ID);
	private static IntakePivot instance;
	private double UP_ANGLE = 4;
	private double INTAKE_ANGLE = 95;
	// up = 4, intake_angle = 92;
	private double goalAngle = INTAKE_ANGLE;
	private boolean isAtHighPoint = false;
	private boolean isButtonBeingHeld = false;

	public static IntakePivot getInstance() {
		if (instance == null) {
			instance = new IntakePivot("Pivot");
		}
		return instance;
	}

	// .019, 0, .009 = intake to top
	// .007, 0, .016
	private IntakePivot(String name) {
		super(name);
		intakePivot.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		teleopController = new PIDPositionController(.012, 0, .005);
		autoController = new PIDPositionController(.012, 0, .005);
		autoController.setName("Pivot");
		teleopController.setName("Pivot");
		goalAngle = INTAKE_ANGLE;
	}

	@Override
	public void initTeleop() {

	}

	@Override
	public void initAuto() {
		goalAngle = INTAKE_ANGLE;
		this.toIntakePosition();
	}

	@Override
	public void updateTeleop() {
		double output = 0;
		if (Controls.operatorController.getRB() && !this.isButtonBeingHeld) {
			this.isButtonBeingHeld = true;
			if (!this.isAtHighPoint) {
				goalAngle = this.UP_ANGLE;
				this.setTeleopController(new PIDPositionController(.019, 0, .009)); // .019,
				// 0
				// ,
				// .009

				((PIDController) this.teleopController).setUseSmartDash(false);
			} else {
				goalAngle = this.INTAKE_ANGLE;
				this.setTeleopController(new PIDPositionController(.0175, 0, .008)); // .007,
				// 0,
				// .017
				((PIDController) this.teleopController).setUseSmartDash(false);
			}
			isAtHighPoint = !isAtHighPoint;
			output = -teleopController.getOutputSignal(getInputState()).getMotor();
		} else if (Controls.operatorController.getRB()) {
			output = -teleopController.getOutputSignal(getInputState()).getMotor();
		} else if (Math.abs(KragerMath.threshold(Controls.operatorController.getLeftY())) > .15) {
			output = KragerMath.threshold(Controls.operatorController.getLeftY());
			goalAngle = -1000;
		} else {
			if (goalAngle < 0) {
				output = 0;
			} else {
				output = -teleopController.getOutputSignal(getInputState()).getMotor();
			}
			this.isButtonBeingHeld = false;
		}
		if ((this.getPivotAngle() > 160 && this.goalAngle > 0)
				|| (this.getPivotAngle() < (this.goalAngle + 8) && this.getPivotAngle() > (this.goalAngle - 8))) {
			if (this.isAtHighPoint) {
				output = .04;
			} else {
				output = .08;
			}
		}
		if (Math.abs(output) > .5) {
			if (output > 0)
				output = .5;
			else if (output < 0)
				output = -.5;
		}
		this.setIntakePivot(-output);
	}

	@Override
	public void updateAuto() {
		double output = -teleopController.getOutputSignal(getInputState()).getMotor();
		if ((this.getPivotAngle() > 160 && this.goalAngle > 0)
				|| (this.getPivotAngle() < (this.goalAngle + 8) && this.getPivotAngle() > (this.goalAngle - 8))) {
			if (this.isAtHighPoint) {
				output = .04;
			} else {
				output = .08;
			}
		}
		if (Math.abs(output) > .5) {
			if (output > 0)
				output = .5;
			else if (output < 0)
				output = -.5;
		}
		this.setIntakePivot(-output);
	}

	public double getPivotAngle() {
		double curAngle = Constants.getPivotTopValue()
				- ((double) intakePivot.getPulseWidthPosition()) * (360.0 / 4096.0);
		// System.out.println("INTAKE PIVOT: " + curAngle);
		while (curAngle > 360) {
			curAngle -= 360;
		}
		while (curAngle < -10) {
			curAngle += 360;
		}
		return curAngle;
	}

	public void toIntakePosition() {
		goalAngle = this.INTAKE_ANGLE;
		this.setAutoController(new PIDPositionController(.0175, 0, .008));
		isAtHighPoint = false;
		((PIDController) this.autoController).setUseSmartDash(false);
	}

	public void toUpPosition() {
		goalAngle = this.UP_ANGLE;
		this.setAutoController(new PIDPositionController(.019, 0, .009));
		isAtHighPoint = true;
		((PIDController) this.autoController).setUseSmartDash(false);
	}

	@Override
	public InputState getInputState() {
		InputState state = new InputState();
		state.setError(goalAngle - getPivotAngle());
		return state;
	}

	@Override
	public void sendToSmartDash() {

		SmartDashboard.putNumber(this.getName() + " goal angle", this.goalAngle);
		SmartDashboard.putNumber(this.getName() + " current angle", this.getPivotAngle());
		SmartDashboard.putNumber(this.getName() + " power", this.intakePivot.get());
	}

	public void setIntakePivot(double power) {
		this.intakePivot.set(-power);
	}

	@Override
	public void manualControl() {
		if (KragerMath.threshold(Controls.operatorController.getLeftY()) == 0) {
			this.setIntakePivot(.073309);
		} else {
			this.setIntakePivot(0.75 * KragerMath.threshold(Controls.operatorController.getLeftY()));
		}
	}
}