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
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IntakePivot extends ControlledSubsystem {

	private CANTalon intakePivot = new CANTalon(RobotMap.INTAKE_PIVOT_ID);
	private static IntakePivot instance;
	private double goalAngle = 0;
	private double UP_ANGLE = 8;
	private double INTAKE_ANGLE = 94;
	private boolean isAtHighPoint = true;
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

		// intakePivot.enableBrakeMode(false);
		// intakePivot.setPulseWidthPosition(0);
		intakePivot.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		mController = new PIDPositionController(.001, 0, 0);
		mController.setName("Pivot");
		goalAngle = this.getPivotAngle();
	}

	@Override
	public void update() {
		double output = 0;
		if (Controls.operatorController.getRB() && !this.isButtonBeingHeld) {
			this.isButtonBeingHeld = true;
			if (!this.isAtHighPoint) {
				goalAngle = this.UP_ANGLE;
				this.setController(new PIDPositionController(.019, 0, .009)); // .019,
																				// 0
																				// ,
																				// .009
				((PIDController) this.mController).setUseSmartDash(false);
			} else {
				goalAngle = this.INTAKE_ANGLE;
				this.setController(new PIDPositionController(.012, 0, .005)); // .007,
																				// 0,
																				// .017
				((PIDController) this.mController).setUseSmartDash(false);
			}
			isAtHighPoint = !isAtHighPoint;
			output = -mController.getOutputSignal(getInputState()).getMotor();
		} else if (Controls.operatorController.getRB()) {
			output = -mController.getOutputSignal(getInputState()).getMotor();
		} else if (KragerMath.threshold(Controls.operatorController.getLeftY()) != 0) {
			output = KragerMath.threshold(Controls.operatorController.getLeftY());
			goalAngle = -1000;
		} /*
			 * else if (Controls.operatorController.getA() ||
			 * Controls.operatorController.getB() ||
			 * Controls.operatorController.getXBut() ||
			 * Controls.operatorController.getYBut() ||
			 * Controls.operatorController.getStart()) { goalAngle = 30;
			 * this.setController(new PIDPositionController(.019, 0, .005));
			 * ((PIDController) this.mController).setUseSmartDash(false); }
			 */else {
			if (goalAngle < 0) {
				output = 0;
			} else {
				output = -mController.getOutputSignal(getInputState()).getMotor();
			}
			this.isButtonBeingHeld = false;
		}
		if ((this.getPivotAngle() > 160 && this.goalAngle > 0)
				|| (this.getPivotAngle() < (this.goalAngle + 10) && this.getPivotAngle() > (this.goalAngle - 10))) {
			if (this.isAtHighPoint) {
				output = .04;
			} else {
				output = .05;
			}
		}

		if (Math.abs(output) > .5) {
			if (output > 0)
				output = .5;
			else if (output < 0)
				output = -.5;
		}

		this.setIntakePivot(output);
	}

	public double getPivotAngle() {
		double curAngle = Constants.getPivotTopValue()
				- ((double) intakePivot.getPulseWidthPosition()) * (360.0 / 4096.0);

		while (curAngle > 360) {
			curAngle -= 360;
		}
		while (curAngle < 0) {
			curAngle += 360;
		}

		return curAngle;
	}

	@Override
	public InputState getInputState() {
		InputState state = new InputState();
		state.setError(goalAngle - getPivotAngle());
		return state;
	}

	@Override
	public void sendToSmartDash() {
		this.mController.sendToSmartDash();
		SmartDashboard.putNumber(this.getName() + " goal angle", this.goalAngle);
		SmartDashboard.putNumber(this.getName() + " current angle", this.getPivotAngle());
		SmartDashboard.putNumber(this.getName() + " power", this.intakePivot.get());
	}

	public void setIntakePivot(double power) {
		this.intakePivot.set(power);
	}

	@Override
	public void manualControl() {
		// this.setIntakePivot(.065);

		if (KragerMath.threshold(Controls.operatorController.getLeftY()) == 0) {
			this.setIntakePivot(.05);
		} else {
			this.setIntakePivot(0.75 * KragerMath.threshold(Controls.operatorController.getLeftY()));
		}
	}
}