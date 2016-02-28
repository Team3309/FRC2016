package org.usfirst.frc.team3309.subsystems.shooter;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.KragerMath;
import org.team3309.lib.controllers.generic.FeedForwardWithPIDController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.robot.RobotMap;
import org.usfirst.frc.team3309.robot.Sensors;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FeedyWheel extends ControlledSubsystem {

	private static FeedyWheel instance;
	private double goalVel = 0;
	private final double FEEDING_SPEED = 12;
	private Spark feedyWheelSpark = new Spark(RobotMap.FEEDY_WHEEL_MOTOR);

	public static FeedyWheel getInstance() {
		if (instance == null) {
			instance = new FeedyWheel("Feedy Wheel");
		}
		return instance;
	}

	private FeedyWheel(String name) {
		super("Feedy Wheel");
		this.mController = new FeedForwardWithPIDController(.005, 0, .007, .001, 0);
	}

	@Override
	public void update() {
		if (Controls.driverController.getLB()
				|| KragerMath.threshold(Controls.driverController.getLeftTrigger()) != 0) {
			goalVel = FEEDING_SPEED;
		} else {
			goalVel = 0;
		}
		((FeedForwardWithPIDController) this.mController).setAimVel(goalVel);
	}

	@Override
	public InputState getInputState() {
		InputState state = new InputState();
		state.setError(goalVel - Sensors.getFeedyWheelVel());
		return state;
	}

	@Override
	public void sendToSmartDash() {
		this.mController.sendToSmartDash();
		SmartDashboard.putNumber(this.getName() + " Power", this.feedyWheelSpark.get());
	}

	public void setFeedyWheel(double power) {
		this.feedyWheelSpark.set(-power);
	}

	@Override
	public void manualControl() {
		double power = 0;
		if (KragerMath.threshold(Controls.driverController.getRightTrigger()) != 0) {
			power = KragerMath.threshold(Controls.driverController.getRightTrigger());
		} else if (KragerMath.threshold(Controls.driverController.getLeftTrigger()) != 0) {
			power = KragerMath.threshold(-Controls.driverController.getLeftTrigger());
		} else if (KragerMath.threshold(Controls.operatorController.getRightTrigger()) != 0) {
			power = KragerMath.threshold(Controls.operatorController.getRightTrigger());
		} else if (KragerMath.threshold(Controls.operatorController.getLeftTrigger()) != 0) {
			power = KragerMath.threshold(-Controls.operatorController.getLeftTrigger());
		}

		this.setFeedyWheel(power);
	}
}
