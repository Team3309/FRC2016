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
	private LinkedList<Double> averagesForFlexSamples = new LinkedList<Double>();
	private double pastFlex = currentFlex;

	public static FeedyWheel getInstance() {
		if (instance == null) {
			instance = new FeedyWheel("Feedy Wheel");
		}
		return instance;
	}

	private FeedyWheel(String name) {
		super("Feedy Wheel");
		this.teleopController = new PIDPositionController(this, false, 001, 0, 0);
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
		currentFlex = input.getVoltage();// averageFlex()
		this.manualControl();
		// System.out.println("ERROR: " + Math.abs(currentFlex - pastFlex));
		pastFlex = currentFlex;// averages.getLast();

	}

	private double averageFlex() {
		averagesForFlexSamples.addFirst(input.getVoltage());
		double sum = 0;
		if (averagesForFlexSamples.size() > 10)
			averagesForFlexSamples.removeLast();
		for (int i = 0; i < averagesForFlexSamples.size(); i++) {
			sum += averagesForFlexSamples.get(i);
		}
		return sum / averagesForFlexSamples.size();
	}

	@Override
	public void updateAuto() {
		this.setFeedyWheel(autoPower);
	}

	@Override
	public InputState getInputState() {
		InputState state = new InputState();
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
