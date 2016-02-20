package org.usfirst.frc.team3309.subsystems.intake;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.KragerMath;
import org.team3309.lib.controllers.generic.PIDPositionController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.robot.RobotMap;
import org.usfirst.frc.team3309.robot.Sensors;

import edu.wpi.first.wpilibj.CANTalon;

public class IntakePivot extends ControlledSubsystem {

	private CANTalon intakePivot = new CANTalon(RobotMap.INTAKE_PIVOT_ID);
	private static IntakePivot instance;
	private double goalAngle = 0;
	private double UP_ANGLE = 90;
	private double INTAKE_ANGLE = 30;

	public static IntakePivot getInstance() {
		if (instance == null) {
			instance = new IntakePivot("Intake Pivot");
		}
		return instance;
	}

	private IntakePivot(String name) {
		super(name);
		mController = new PIDPositionController(.001, 0, 0);
	}

	@Override
	public void update() {
		if (Controls.operatorController.getRB()) {
			goalAngle = UP_ANGLE;
		} else if (Controls.operatorController.getLB()) {
			goalAngle = INTAKE_ANGLE;
		}
		if (KragerMath.threshold(Controls.operatorController.getLeftY()) != 0) {
			this.setIntakePivot(KragerMath.threshold(Controls.operatorController.getLeftY()));
			goalAngle = Sensors.getIntakePivotAngle();
		} else {
			double output = mController.getOutputSignal(getInputState()).getMotor();
			this.setIntakePivot(output);
		}
	}

	public double getPivotAngle() {
		return ((double) intakePivot.getPulseWidthPosition()) * (360 / 4096);
	}

	@Override
	public InputState getInputState() {
		InputState state = new InputState();
		state.setError(goalAngle - Sensors.getIntakePivotAngle());
		return state;
	}

	@Override
	public void sendToSmartDash() {

	}

	public void setIntakePivot(double power) {
		this.intakePivot.set(power);
	}

	@Override
	public void manualControl() {
		this.setIntakePivot(KragerMath.threshold(Controls.operatorController.getLeftY()));
	}
}