package org.usfirst.frc.team3309.subsystems;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.robot.RobotMap;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake extends ControlledSubsystem {

	private Victor sideMotor = new Victor(RobotMap.INTAKE_SIDE_MOTOR);
	private Victor frontMotor = new Victor(RobotMap.INTAKE_FRONT_MOTOR);

	private static Intake instance;

	/**
	 * Singleton Pattern
	 * 
	 * @return the single instance
	 */
	public static Intake getInstance() {
		if (instance == null)
			instance = new Intake("Intake");
		return instance;
	}

	private Intake(String name) {
		super(name);
	}

	@Override
	public void update() {
		if (Math.abs(Controls.operatorController.getRightTrigger()) > 0.15
				|| Math.abs(Controls.operatorController.getLeftTrigger()) > 0.15) {
			if (Controls.operatorController.getRightTrigger() > 0) {
				this.setIntake(Controls.operatorController.getRightTrigger());
			} else if (Controls.operatorController.getLeftTrigger() > 0) {
				this.setIntake(-Controls.operatorController.getLeftTrigger());
			}
		} else {
			if (Controls.driverController.getRightTrigger() > 0) {
				this.setIntake(Controls.driverController.getRightTrigger());
			} else if (Controls.driverController.getLeftTrigger() > 0) {
				this.setIntake(-Controls.driverController.getLeftTrigger());
			}
		}
	}

	private void setIntake(double power) {
		this.sideMotor.set(power);
		this.frontMotor.set(power);
	}

	@Override
	public InputState getInputState() {
		return null;
	}

	@Override
	public void sendToSmartDash() {
		SmartDashboard.putNumber(this.getName() + " side", this.sideMotor.get());
		SmartDashboard.putNumber(this.getName() + " front", this.frontMotor.get());
	}

}
