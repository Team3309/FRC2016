package org.usfirst.frc.team3309.subsystems.intake;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.KragerMath;
import org.team3309.lib.actuators.TalonSRXMC;
import org.team3309.lib.controllers.generic.PIDController;
import org.team3309.lib.controllers.generic.PIDPositionController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.robot.RobotMap;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IntakePivot extends ControlledSubsystem {

	private TalonSRXMC intakePivot = new TalonSRXMC(RobotMap.INTAKE_PIVOT_ID);
	private static IntakePivot instance;
	private double UP_ANGLE = 1;
	private double INTAKE_ANGLE = 87;
	private double pastPower = 0;
	// up = 4, intake_angle = 92;
	private double goalAngle = INTAKE_ANGLE;

	public double getGoalAngle() {
		return goalAngle;
	}

	public void setGoalAngle(double goalAngle) {
		this.goalAngle = goalAngle;
	}

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
		// aBS
		// intakePivot.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		// Relative
		intakePivot.setReversed(true);
		//intakePivot.;

		teleopController = new PIDPositionController(.012, 0, .005);
		autoController = new PIDPositionController(.012, 0, .005);
		autoController.setName("Pivot");
		teleopController.setName("Pivot");
		goalAngle = INTAKE_ANGLE;
		// intakePivot.
	}

	@Override
	public void initTeleop() {
		// this.toUpPosition();
	}

	@Override
	public void initAuto() {
		//this.toIntakePosition();
	}

	@Override
	public void updateTeleop() {
		double output = 0;
		/*
		 * if (Controls.operatorController.getRB() && !this.isButtonBeingHeld) {
		 * this.isButtonBeingHeld = true; if (!this.isAtHighPoint) { goalAngle =
		 * this.UP_ANGLE; this.setTeleopController(new
		 * PIDPositionController(.019, 0, .009)); // .019, // 0 // , // .009
		 * 
		 * ((PIDController) this.teleopController).setUseSmartDash(false); }
		 * else { goalAngle = this.INTAKE_ANGLE; this.setTeleopController(new
		 * PIDPositionController(.0175, 0, .008)); // .007, // 0, // .017
		 * ((PIDController) this.teleopController).setUseSmartDash(false); }
		 * isAtHighPoint = !isAtHighPoint; output =
		 * -teleopController.getOutputSignal(getInputState()).getMotor(); } else
		 * if (Controls.operatorController.getRB()) { output =
		 * -teleopController.getOutputSignal(getInputState()).getMotor(); } else
		 */
		if (Math.abs(KragerMath.threshold(Controls.operatorController.getLeftY())) > .15) {
			output = KragerMath.threshold(Controls.operatorController.getLeftY()) * .75;
			goalAngle = -1000;
		} else {
			output = 0;
			/*
			 * if (goalAngle < 0) { output = 0; } else { output =
			 * -teleopController.getOutputSignal(getInputState()).getMotor(); }
			 */
			this.isButtonBeingHeld = false;
		}
		/*
		 * if ((this.getPivotAngle() > 160 && this.goalAngle > 0) ||
		 * (this.getPivotAngle() < (this.goalAngle + 8) && this.getPivotAngle()
		 * > (this.goalAngle - 8))) { if (this.isAtHighPoint) { output = 0; }
		 * else { output = 0; } }
		 */
		if (Controls.operatorController.getLB()) {
			intakePivot.getTalon().setEncPosition(0);
			intakePivot.getTalon().setPosition(0);
			intakePivot.getTalon().setPulseWidthPosition(0);
			this.goalAngle = 0;

		}
		if ((pastPower > 0 && output == 0) || (output == .1)) {
			output = .1;
		}
		pastPower = output;
		if (Controls.operatorController.getLeftStickButton() || Controls.driverController.getA()) {
			if (Math.abs(output) > .5) {
				if (output > 0)
					output = .5;
				else if (output < 0)
					output = -.5;
			}
		}
		this.setIntakePivot(-output);
	}

	@Override
	public void updateAuto() {
		double output = -autoController.getOutputSignal(getInputState()).getMotor();
		if ((this.getPivotAngle() > 160 && this.goalAngle > -5)
				|| (this.getPivotAngle() < (this.goalAngle + 8) && this.getPivotAngle() > (this.goalAngle - 8))) {
			if (this.isAtHighPoint) {
				output = .0;
			} else {
				output = .0;
			}
		}
		if (Math.abs(output) > .5) {
			if (output > 0)
				output = .5;
			else if (output < 0)
				output = -.5;
		}
		if (this.isAtHighPoint) {
			this.setIntakePivot(-.1);
		}
		// this.setIntakePivot(-output);
	}

	public double getPivotAngle() {
		// double curAngle = Constants.getPivotTopValue()
		// - ((double) intakePivot.getPulseWidthPosition()) * (360.0 / 4096.0);
		double curAngle = -(intakePivot.getTalon().getEncPosition() * (360.0 / 4096.0)) / 210;
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
		this.setIntakePivot(.5);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setIntakePivot(0);
	}

	public void toUpPosition() {
		goalAngle = this.UP_ANGLE;
		this.setAutoController(new PIDPositionController(.019, 0, .009));
		isAtHighPoint = true;
		((PIDController) this.autoController).setUseSmartDash(false);
		this.setIntakePivot(-.75);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setIntakePivot(-.1);
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
		SmartDashboard.putNumber(this.getName() + " power", this.intakePivot.getTalon().get());
	}

	public void setIntakePivot(double power) {
		this.intakePivot.setDesiredOutput(power);
	}

	@Override
	public void manualControl() {
		if (KragerMath.threshold(Controls.operatorController.getLeftY()) == 0) {
			this.setIntakePivot(.00);
		} else {
			this.setIntakePivot(0.75 * KragerMath.threshold(Controls.operatorController.getLeftY()));
		}
	}
}