package org.usfirst.frc.team3309.subsystems.climber;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.robot.RobotMap;

import edu.wpi.first.wpilibj.Spark;

public class CarriagePivot extends ControlledSubsystem {

	private Spark carriagePivot = new Spark(RobotMap.CLIMBER_PIVOT_MOTOR);

	private CarriagePivot(String name) {
		super(name);
	}

	@Override
	public void update() {

	}

	@Override
	public InputState getInputState() {
		return null;
	}

	@Override
	public void sendToSmartDash() {

	}

	public void setCarriagePivot(double power) {
		this.carriagePivot.set(power);
	}

	@Override
	public void manualControl() {

	}
}