package org.usfirst.frc.team3309.subsystems.shooter;

import java.util.LinkedList;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.KragerMath;
import org.team3309.lib.controllers.generic.PIDPositionController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FeedyWheel extends ControlledSubsystem {

	private static FeedyWheel instance;
	private AnalogInput input = new AnalogInput(RobotMap.FLEX_SENSORS_ANALOG);
	private double autoPower = 0;
	private Spark feedyWheelSpark = new Spark(RobotMap.FEEDY_WHEEL_MOTOR);
	private double currentFlex = 0;
	private LinkedList<Double> averages = new LinkedList<Double>();
	private boolean isLiningUpForShooter = true;
	private final double GOAL_POS_FOR_SHOOTER = 2.44;
	private double pastFlex = currentFlex;

	public static FeedyWheel getInstance() {
		if (instance == null) {
			instance = new FeedyWheel("Feedy Wheel");
		}
		return instance;
	}

	private FeedyWheel(String name) {
		super("Feedy Wheel");
		this.teleopController = new PIDPositionController(001, 0, 0);
		teleopController.setName("FW Pos");
	}

	@Override
	public void initTeleop() {
		currentFlex = pastFlex = averageFlex();
	}

	@Override
	public void initAuto() {
		currentFlex = pastFlex = averageFlex();
	}

	@Override
	public void updateTeleop() {
		currentFlex = input.getVoltage();// averageFlex();
		//System.out.println("Voltage: " + currentFlex);
		/*
		 * if (isLiningUpForShooter &&
		 * (KragerMath.threshold(Controls.driverController.getRightTrigger()) ==
		 * 0 && KragerMath.threshold(Controls.driverController.getLeftTrigger())
		 * == 0)) { double output =
		 * this.teleopController.getOutputSignal(getInputState()).getMotor();
		 * this.setFeedyWheel(output); } else {
		 */
		this.manualControl();
		// }
		System.out.println("ERROR: " + Math.abs(currentFlex - pastFlex));
		if (Math.abs(currentFlex - pastFlex) > .005 && this.feedyWheelSpark.get() != 0)
			Controls.driverController.setRumble((float) 0.9);
		else {
			Controls.driverController.setRumble((float) 0);
		}
		pastFlex = currentFlex;// averages.getLast();
	}

	private double averageFlex() {
		averages.addFirst(input.getVoltage());
		double sum = 0;
		if (averages.size() > 10)
			averages.removeLast();
		for (int i = 0; i < averages.size(); i++) {
			sum += averages.get(i);
		}
		return sum / averages.size();
	}

	@Override
	public void updateAuto() {
		this.setFeedyWheel(autoPower);
	}

	@Override
	public InputState getInputState() {
		InputState state = new InputState();
		state.setError(this.GOAL_POS_FOR_SHOOTER - currentFlex);
		return state;
	}

	@Override
	public void sendToSmartDash() {
		this.teleopController.sendToSmartDash();
		SmartDashboard.putNumber(this.getName() + " Power", this.feedyWheelSpark.get());
		SmartDashboard.putNumber(this.getName() + " Flex", currentFlex);
	}

	public void setFeedyWheel(double power) {
		if (DriverStation.getInstance().isAutonomous())
			autoPower = power;
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
