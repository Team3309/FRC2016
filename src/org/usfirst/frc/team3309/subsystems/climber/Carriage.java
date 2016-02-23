package org.usfirst.frc.team3309.subsystems.climber;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.KragerMath;
import org.team3309.lib.controllers.generic.PIDVelocityController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;

public class Carriage extends ControlledSubsystem {

	private static Carriage instance;
	private double goalVelocity = 0;
	private CANTalon carriageTalon = new CANTalon(RobotMap.CLIMBER_CARRIAGE_ID);
	private final double MAX_VEL = 10;

	public static Carriage getInstance() {
		if (instance == null) {
			instance = new Carriage("Carriage");
		}
		return instance;
	}

	private Carriage(String name) {
		super(name);
		mController = new PIDVelocityController(.01, 0, 0);
	}

	@Override
	public void update() {
		goalVelocity = KragerMath.threshold(Controls.operatorController.getRightY()) * this.MAX_VEL;
		double output = mController.getOutputSignal(getInputState()).getMotor();
		this.setCarriage(output);
	}

	@Override
	public InputState getInputState() {
		InputState inputState = new InputState();
		inputState.setError(goalVelocity - this.getCarriageVelocity());
		return inputState;
	}

	@Override
	public void sendToSmartDash() {

	}

	public double getCarriagePosition() {
		return this.carriageTalon.getPulseWidthPosition();
	}

	public double getCarriageVelocity() {
		return this.carriageTalon.getPulseWidthVelocity();
	}

	public void setCarriage(double power) {
		this.carriageTalon.set(power);
	}

	@Override
	public void manualControl() {
		this.setCarriage(KragerMath.threshold(Controls.operatorController.getRightY()));
	}
}
