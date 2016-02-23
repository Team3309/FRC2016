package org.usfirst.frc.team3309.subsystems.intake;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.KragerMath;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.robot.RobotMap;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FrontAndSideRollers extends ControlledSubsystem {

	private static FrontAndSideRollers instance;
	private Spark intakeFront = new Spark(RobotMap.INTAKE_FRONT_MOTOR);
	private Spark intakeSide = new Spark(RobotMap.INTAKE_SIDE_MOTOR);

	public static FrontAndSideRollers getInstance() {
		if (instance == null) {
			instance = new FrontAndSideRollers("Front and Side Rollers");
		}
		return instance;
	}

	private FrontAndSideRollers(String name) {
		super(name);
	}

	@Override
	public void update() {
		double power = 0;
		if (KragerMath.threshold(Controls.driverController.getRightTrigger()) != 0) {
			power = KragerMath.threshold(Controls.driverController.getRightTrigger());
		} else if (KragerMath.threshold(Controls.driverController.getLeftTrigger()) != 0) {
			power = KragerMath.threshold(-Controls.driverController.getLeftTrigger());
		}
		this.setIntake(power);
	}

	@Override
	public InputState getInputState() {
		InputState state = new InputState();
		return state;
	}

	@Override
	public void sendToSmartDash() {
		SmartDashboard.putNumber(this.getName() + " Power", intakeFront.get());
	}

	private void setIntake(double power) {
		this.intakeFront.set(power);
		this.intakeSide.set(-power);
	}

	@Override
	public void manualControl() {
		update();
	}
}