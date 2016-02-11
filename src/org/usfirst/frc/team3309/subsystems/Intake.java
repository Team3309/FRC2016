package org.usfirst.frc.team3309.subsystems;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.KragerMath;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake extends ControlledSubsystem {

	private Spark sideMotor = new Spark(RobotMap.INTAKE_SIDE_MOTOR);
	private Spark frontMotor = new Spark(RobotMap.INTAKE_FRONT_MOTOR);
	private CANTalon intakePivot = new CANTalon(RobotMap.INTAKE_PIVOT_ID);

	private double curAngle = 0;
	private double goalAngle = 0;
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
		curAngle = this.getPivotAngle();
		// Find goal Angle 
		// Run Intake itself
		if (Math.abs(Controls.operatorController.getRightTrigger()) > 0.15
				|| Math.abs(Controls.operatorController.getLeftTrigger()) > 0.15) {
			if (Controls.operatorController.getRightTrigger() > 0) {
				this.setIntake(Controls.operatorController.getRightTrigger());
			} else if (Controls.operatorController.getLeftTrigger() > 0) {
				this.setIntake(Controls.operatorController.getLeftTrigger());
			}
		} else {
			if (Controls.driverController.getRightTrigger() > 0) {
				this.setIntake(KragerMath.threshold(Controls.driverController.getRightTrigger()));
			} else if (Controls.driverController.getLeftTrigger() > 0) {
				this.setIntake(KragerMath.threshold(Controls.driverController.getLeftTrigger()));
			}
		}
	}

	/**
	 * Set Intake
	 * @param power
	 */
	private void setIntake(double power) {
		this.sideMotor.set(power);
		this.frontMotor.set(power);
	}

	@Override
	public InputState getInputState() {
		InputState state = new InputState();
		state.setError(this.getPivotAngle());
		return null;
	}
	
	public double getPivotAngle() {
		return ((double)intakePivot.getPulseWidthPosition()) * (360/4096);
	}

	@Override
	public void sendToSmartDash() {
		SmartDashboard.putNumber(this.getName() + " side", this.sideMotor.get());
		SmartDashboard.putNumber(this.getName() + " front", this.frontMotor.get());
	}

}
